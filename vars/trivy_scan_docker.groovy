def call(Map params = [:]) {
    def image = params.Image ?: params.image
    def tag = params.Tag ?: params.tag ?: 'latest'
    def severities = params.Severities ?: params.Severity ?: params.severities ?: ['HIGH', 'CRITICAL']
    def scanners = params.Scanners ?: params.VulnTypes ?: params.vulnTypes ?: ['vuln', 'secret']
    def exitCode = params.ExitCode == null ? 1 : params.ExitCode
    def ignoreUnfixed = params.IgnoreUnfixed == null ? true : params.IgnoreUnfixed
    def reportDir = params.ReportDir ?: 'reports'
    def reportFile = params.ReportFile ?: "${reportDir}/trivy-image-report.txt"

    if (!image?.trim()) {
        error("Trivy scan parameter 'Image' is required.")
    }

    def fullImageName = "${image}:${tag}"
    def ignoreFlag = ignoreUnfixed ? '--ignore-unfixed' : ''

    echo "Starting Trivy image scan for image: ${fullImageName}"

    def status = sh(
        label: 'Trivy image scan',
        returnStatus: true,
        script: """
            mkdir -p ${shellQuote(reportDir)}
            trivy image \\
              --severity ${shellQuote(severities.join(','))} \\
              --scanners ${shellQuote(scanners.join(','))} \\
              ${ignoreFlag} \\
              --exit-code ${exitCode} \\
              --format table \\
              --output ${shellQuote(reportFile)} \\
              ${shellQuote(fullImageName)}
            cat ${shellQuote(reportFile)} || true
        """
    )

    archiveArtifacts artifacts: reportFile, allowEmptyArchive: true

    if (status != 0) {
        error("Trivy image scan failed. See archived report: ${reportFile}")
    }
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
