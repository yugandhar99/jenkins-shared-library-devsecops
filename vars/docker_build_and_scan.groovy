def call(Map params = [:]) {
    def credentialId = params.CredId ?: params.credentialsId ?: params.CredentialsId
    def imageBase = params.ImageBase ?: params.imageBase ?: params.Project ?: params.project
    def imageSuffix = params.ImageSuffix ?: params.imageSuffix ?: ''
    def tags = params.Tags ?: params.tags ?: ['latest']
    def contextPath = params.ContextPath ?: params.contextPath ?: params.BuildContext ?: params.buildContext ?: '.'
 
    if (!credentialId) {
        error('Docker credentials id is required. Example: CredId: \'dockerhub-creds\'')
    }
    if (!imageBase) {
        error('ImageBase is required.')
    }

    withCredentials([usernamePassword(credentialsId: credentialId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        def imageName = buildImageName(env.DOCKER_USER, imageBase, imageSuffix)

        docker_build(imageName, tags, contextPath)

        trivy_scan_docker(
            Image: imageName,
            Tag: tags[0],
            Severities: params.Severities ?: params.Severity ?: ['HIGH', 'CRITICAL'],
            Scanners: params.Scanners ?: ['vuln', 'secret'],
            ExitCode: params.ExitCode ?: 1,
            IgnoreUnfixed: params.IgnoreUnfixed == null ? true : params.IgnoreUnfixed
        )
    }
}

private String buildImageName(String namespace, String imageBase, String imageSuffix) {
    def suffix = imageSuffix?.trim() ? "-${imageSuffix.trim()}" : ''
    return "${namespace}/${imageBase}${suffix}"
}
