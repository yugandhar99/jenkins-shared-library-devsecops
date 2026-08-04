def call(Map params = [:]) {
    def target = params.Target ?: params.target ?: '.'
    def severities = params.Severities ?: params.Severity ?: ['HIGH', 'CRITICAL']
    def scanners = params.Scanners ?: ['vuln', 'secret', 'misconfig']
    def exitCode = params.ExitCode == null ? 1 : params.ExitCode
    def reportDir = params.ReportDir ?: 'reports'
    def reportFile = params.ReportFile ?: "${reportDir}/trivy-fs-report.txt"

    def status = sh(
        label: 'Trivy filesystem scan',
        returnStatus: true,
        script: """
            mkdir -p ${shellQuote(reportDir)}
            trivy fs \\
              --severity ${shellQuote(severities.join(','))} \\
              --scanners ${shellQuote(scanners.join(','))} \\
              --exit-code ${exitCode} \\
              --format table \\
              --output ${shellQuote(reportFile)} \\
              ${shellQuote(target)}
            cat ${shellQuote(reportFile)} || true
        """
    )

    archiveArtifacts artifacts: reportFile, allowEmptyArchive: true

    if (status != 0) {
        error("Trivy filesystem scan failed. See archived report: ${reportFile}")
    }
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
