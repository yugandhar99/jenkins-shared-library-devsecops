# Jenkins Shared Library Setup

## 1. Add the library in Jenkins

Go to:

```text
Manage Jenkins → System → Global Trusted Pipeline Libraries
```

Add:

```text
Name: devops-shared-lib
Default version: main
Retrieval method: Modern SCM
Source Code Management: Git
Project repository: <your GitHub repo URL>
```

## 2. Use it in a Jenkinsfile

Add this line at the top of your Jenkinsfile:

```groovy
@Library('devops-shared-lib') _
```

Then call any reusable function from the `vars/` folder:

```groovy
docker_build('my-dockerhub-user/my-app', ['latest'], '.')
trivy_scan(Target: '.')
sonarqube_code_quality(TimeoutMinutes: 3, AbortPipeline: true)
```

## 3. Required Jenkins plugins

Common plugins used by this project:

- Pipeline
- Docker Pipeline or Docker CLI on the Jenkins agent
- Credentials Binding
- SonarQube Scanner for Jenkins
- OWASP Dependency-Check
- HTML Publisher or Artifact Archiver for reports

## 4. Required tools on Jenkins agent

Install these tools on the Jenkins worker/agent:

- Docker CLI
- Trivy
- Sonar Scanner
- Maven or Node.js depending on the application pipeline
