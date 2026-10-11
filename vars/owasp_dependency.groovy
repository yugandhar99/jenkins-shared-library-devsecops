def call(Map params = [:]) {
    def scanPath = params.ScanPath ?: params.scanPath ?: './'
    def installation = params.Installation ?: params.installation ?: 'OWASP'
    def reportPattern = params.ReportPattern ?: params.reportPattern ?: '**/dependency-check-report.xml'
    def additionalArguments = params.AdditionalArguments ?: params.additionalArguments ?: "--scan ${scanPath} --format XML --format HTML --out reports/dependency-check"

    dependencyCheck additionalArguments: additionalArguments, odcInstallation: installation
    dependencyCheckPublisher pattern: reportPattern

    archiveArtifacts artifacts: 'reports/dependency-check/**', allowEmptyArchive: true
}
