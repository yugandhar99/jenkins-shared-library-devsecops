import groovy.json.JsonOutput

def call(Map params = [:]) {
    def enabled = params.Enabled == null ? true : params.Enabled
    if (!enabled) {
        echo 'GenAI release notes step is disabled.'
        return
    }

    def credentialsId = params.CredentialsId ?: params.credentialsId ?: 'genai-api-token'
    def apiUrl = params.ApiUrl ?: params.apiUrl ?: 'https://api.openai.com/v1/chat/completions'
    def model = params.Model ?: params.model ?: 'gpt-4o-mini'
    def commitLimit = params.CommitLimit ?: params.commitLimit ?: 10
    def outputFile = params.OutputFile ?: params.outputFile ?: 'genai-release-notes-response.json'

    def gitSummary = sh(
        script: "git log -n ${commitLimit as Integer} --pretty=format:'%h %an %s' || true",
        returnStdout: true
    ).trim()

    def scanSummary = sh(
        script: """
            set +e
            echo '--- Trivy Reports ---'
            find reports -maxdepth 3 -type f -name '*trivy*' -print -exec sed -n '1,80p' {} \\; 2>/dev/null
            echo '--- Dependency Check Reports ---'
            find reports -maxdepth 4 -type f -name '*dependency*' -print 2>/dev/null
        """,
        returnStdout: true
    ).trim()

    def prompt = """
Create a concise DevOps release summary for a Jenkins pipeline run.
Include:
1. What changed
2. Security/quality observations
3. Deployment risk level
4. Recommended next action

Git commit summary:
${gitSummary ?: 'No git summary available.'}

Scan summary:
${scanSummary ?: 'No scan summary available.'}
""".trim()

    def payload = [
        model: model,
        temperature: 0.2,
        messages: [
            [role: 'system', content: 'You are a DevOps release assistant. Be concise, practical, and avoid exposing secrets.'],
            [role: 'user', content: prompt]
        ]
    ]

    writeFile file: 'genai-payload.json', text: JsonOutput.prettyPrint(JsonOutput.toJson(payload))

    withCredentials([string(credentialsId: credentialsId, variable: 'GENAI_API_KEY')]) {
        withEnv(["GENAI_API_URL=${apiUrl}"]) {
            sh label: 'Generate AI release notes', script: """
                set +x
                curl -sS -X POST "$GENAI_API_URL" \\
                  -H "Authorization: Bearer $GENAI_API_KEY" \\
                  -H "Content-Type: application/json" \\
                  --data @genai-payload.json \\
                  --output ${shellQuote(outputFile)}
            """
        }
    }

    archiveArtifacts artifacts: "genai-payload.json,${outputFile}", allowEmptyArchive: true
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
