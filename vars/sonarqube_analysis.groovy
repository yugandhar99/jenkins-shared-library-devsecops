def call(String sonarQubeServer, String projectName, String projectKey) {
    call(
        ServerName: sonarQubeServer,
        ProjectName: projectName,
        ProjectKey: projectKey
    )
}

def call(Map params = [:]) {
    def serverName = params.ServerName ?: params.serverName ?: params.SonarQubeAPI
    def projectName = params.ProjectName ?: params.projectName
    def projectKey = params.ProjectKey ?: params.projectKey
    def sources = params.Sources ?: params.sources ?: '.'
    def exclusions = params.Exclusions ?: params.exclusions ?: '**/node_modules/**,**/target/**,**/build/**,**/.git/**'
    def scannerTool = params.ScannerTool ?: params.scannerTool ?: 'sonar-scanner'

    if (!serverName) {
        error('SonarQube server name is required.')
    }
    if (!projectName || !projectKey) {
        error('ProjectName and ProjectKey are required for SonarQube analysis.')
    }

    def scannerHome = tool scannerTool

    withSonarQubeEnv(serverName) {
        sh label: 'SonarQube analysis', script: """
            ${shellQuote(scannerHome)}/bin/sonar-scanner \\
              -Dsonar.projectName=${shellQuote(projectName)} \\
              -Dsonar.projectKey=${shellQuote(projectKey)} \\
              -Dsonar.sources=${shellQuote(sources)} \\
              -Dsonar.exclusions=${shellQuote(exclusions)}
        """
    }
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
