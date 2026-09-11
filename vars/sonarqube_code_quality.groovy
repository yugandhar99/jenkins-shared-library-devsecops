def call(Map params = [:]) {
    def timeoutMinutes = params.TimeoutMinutes ?: params.timeoutMinutes ?: 2
    def abortPipeline = params.AbortPipeline == null ? true : params.AbortPipeline

    timeout(time: timeoutMinutes as Integer, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: abortPipeline
    }
}
