def call(String project, String imageTag = 'latest', String dockerHubUser = '') {
    if (!project?.trim()) {
        error('Project/image name is required for Docker cleanup.')
    }

    def imageName = dockerHubUser?.trim() ? "${dockerHubUser}/${project}:${imageTag}" : "${project}:${imageTag}"
    sh label: 'Docker cleanup', script: "docker rmi ${shellQuote(imageName)} || true"
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
