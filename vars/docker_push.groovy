def call(Map params = [:]) {
    def registry = params.Registry ?: params.registry ?: 'https://index.docker.io/v1/'
    def credentialId = params.CredId ?: params.credentialsId ?: params.CredentialsId
    def imageBase = params.ImageBase ?: params.imageBase ?: params.Project ?: params.project
    def imageSuffix = params.ImageSuffix ?: params.imageSuffix ?: ''
    def tags = params.Tags ?: params.tags ?: ['latest']

    if (!credentialId) {
        error('Docker credentials id is required. Example: CredId: \'dockerhub-creds\'')
    }
    if (!imageBase) {
        error('ImageBase is required.')
    }

    withCredentials([usernamePassword(credentialsId: credentialId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        def imageName = buildImageName(env.DOCKER_USER, imageBase, imageSuffix)
        docker_login(registry, env.DOCKER_USER, env.DOCKER_PASS)

        tags.each { tag ->
            echo "Pushing Docker image: ${imageName}:${tag}"
            sh label: "Docker push ${tag}", script: "docker push ${shellQuote(imageName + ':' + tag)}"
        }
    }
}

private String buildImageName(String namespace, String imageBase, String imageSuffix) {
    def suffix = imageSuffix?.trim() ? "-${imageSuffix.trim()}" : ''
    return "${namespace}/${imageBase}${suffix}"
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
