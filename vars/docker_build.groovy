def call(String imageName, List tags = ['latest'], String contextPath = '.') {
    validateRequired(imageName, 'imageName')
    validateRequired(contextPath, 'contextPath')

    if (!tags || tags.isEmpty()) {
        tags = ['latest']
    }

    def tagArgs = tags.collect { tag ->
        validateDockerTag(tag)
        "-t ${shellQuote(imageName + ':' + tag)}"
    }.join(' ')

    sh label: 'Docker build', script: "docker build ${tagArgs} ${shellQuote(contextPath)}"
}

private void validateRequired(value, String name) {
    if (value == null || value.toString().trim().isEmpty()) {
        error("${name} is required.")
    }
}

private void validateDockerTag(Object tag) {
    if (!(tag.toString() ==~ /^[A-Za-z0-9_.-]+$/)) {
        error("Invalid Docker tag: ${tag}. Use only letters, numbers, dot, underscore, and hyphen.")
    }
}

private String shellQuote(Object value) {
    return "'" + value.toString().replace("'", "'\\''") + "'"
}
