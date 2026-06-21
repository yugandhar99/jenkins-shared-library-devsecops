def call(Map params = [:]) {
    def message = params.Message ?: params.message ?: "Build ${env.JOB_NAME} #${env.BUILD_NUMBER} finished with status ${currentBuild.currentResult}"
    def webhookCredentialId = params.WebhookCredentialId ?: params.webhookCredentialId

    if (!webhookCredentialId) {
        echo 'Slack webhook credential id not provided. Skipping notification.'
        return
    }

    withCredentials([string(credentialsId: webhookCredentialId, variable: 'SLACK_WEBHOOK_URL')]) {
        def payload = groovy.json.JsonOutput.toJson([text: message])
        writeFile file: 'slack-payload.json', text: payload
        sh label: 'Slack notification', script: '''
            set +x
            curl -sS -X POST -H 'Content-Type: application/json' --data @slack-payload.json "$SLACK_WEBHOOK_URL" >/dev/null
            rm -f slack-payload.json
        '''
    }
}
