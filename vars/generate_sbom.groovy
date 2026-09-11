def call(Map params = [:]) {
    def target = params.Target ?: params.target ?: '.'
    def type = params.Type ?: params.type ?: 'fs'
    def output = params.Output ?: params.output ?: 'reports/sbom-cyclonedx.json'

    sh label: 'Generate SBOM', script: """
        mkdir -p reports
        trivy ${shellQuote(type)} \\
          --format cyclonedx \\
          --output ${shellQuote(output)} \\
          ${shellQuote(target)}
    """

    archiveArtifacts artifacts: output, allowEmptyArchive: true
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
