def call(String registry, String username, String password) {
    if (!registry?.trim()) {
        registry = 'https://index.docker.io/v1/'
    }
    if (!username?.trim()) {
        error('Docker username is required.')
    }
    if (!password?.trim()) {
        error('Docker password/token is required. Store it in Jenkins Credentials, not in source code.')
    }

    withEnv([
        "DOCKER_REGISTRY_URL=${registry}",
        "DOCKER_LOGIN_USER=${username}",
        "DOCKER_LOGIN_PASSWORD=${password}"
    ]) {
        sh label: 'Docker login', script: '''
            set +x
            printf '%s' "$DOCKER_LOGIN_PASSWORD" | docker login "$DOCKER_REGISTRY_URL" -u "$DOCKER_LOGIN_USER" --password-stdin
        '''
    }
}

def call(String username, String password) {
    call('https://index.docker.io/v1/', username, password)
}
