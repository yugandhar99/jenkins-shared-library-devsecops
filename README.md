# Jenkins Shared Library for CI/CD and DevSecOps

This repository contains a reusable **Jenkins Shared Library** for standardizing CI/CD stages across multiple applications. It helps teams avoid duplicated Jenkinsfile code by keeping common pipeline logic in one central GitHub repository.

The library includes reusable steps for Docker build and push, SonarQube analysis, OWASP Dependency Check, Trivy vulnerability scanning, SBOM generation, quality gate validation, Slack notification, and optional GenAI-assisted release summaries.

## Why this project matters

In real DevOps teams, different applications often repeat the same pipeline stages: checkout, build, code quality scan, dependency scan, Docker image build, image scan, image push, and deployment summary. A Jenkins Shared Library makes that process reusable, consistent, and easier to maintain.

This project is useful for showing practical DevOps and platform engineering skills because it focuses on reusable automation, not just one single Jenkinsfile.

## Key features

- Reusable Jenkins pipeline functions under the `vars/` directory
- Docker image build and multi-tag support
- Secure Docker login using Jenkins Credentials and `--password-stdin`
- Docker image push to DockerHub or another registry
- Trivy filesystem and container image scanning
- OWASP Dependency Check integration
- SonarQube analysis and quality gate validation
- SBOM generation using Trivy CycloneDX output
- Optional GenAI release summary generation
- Slack notification helper
- Example Jenkinsfiles for Spring Boot and Node.js projects
- GitHub Actions repository validation workflow
- Security documentation and GitHub upload guide

## Repository structure

```text
.
├── vars/                         # Jenkins Shared Library global steps
├── examples/                     # Example Jenkinsfiles using the library
├── docs/                         # Setup and GenAI documentation
├── assests/                      # Existing reference screenshots
├── .github/workflows/            # GitHub Actions validation
├── README.md
├── SECURITY.md
├── PORTFOLIO_NOTES.md
└── GITHUB_UPLOAD_STEPS.md
```

## Shared library functions

| Function | Purpose |
|---|---|
| `docker_build` | Builds Docker images with one or more tags |
| `docker_login` | Logs in to Docker registry securely |
| `docker_push` | Pushes Docker images using Jenkins credentials |
| `docker_build_and_scan` | Builds a Docker image and runs Trivy image scan |
| `docker_cleanup` | Removes Docker images from the Jenkins agent |
| `trivy_scan` | Runs Trivy filesystem scan |
| `trivy_scan_docker` | Runs Trivy container image scan |
| `owasp_dependency` | Runs OWASP Dependency Check |
| `sonarqube_analysis` | Runs SonarQube code analysis |
| `sonarqube_code_quality` | Waits for SonarQube quality gate |
| `generate_sbom` | Generates a CycloneDX SBOM artifact |
| `genai_release_notes` | Generates optional AI-assisted release summary |
| `slack_notify` | Sends a Slack notification using webhook credential |

## Jenkins setup

Go to:

```text
Manage Jenkins → System → Global Trusted Pipeline Libraries
```

Add this repository as a shared library:

```text
Name: devops-shared-lib
Default version: main
Retrieval method: Modern SCM
Source Code Management: Git
Project repository: https://github.com/YOUR_USERNAME/jenkins-shared-library-devsecops.git
```

Then use it in a Jenkinsfile:

```groovy
@Library('devops-shared-lib') _
```

## Example usage

```groovy
@Library('devops-shared-lib') _

pipeline {
    agent any

    stages {
        stage('Security Scan') {
            steps {
                trivy_scan(Target: '.', Severities: ['HIGH', 'CRITICAL'])
            }
        }

        stage('Docker Build and Scan') {
            steps {
                docker_build_and_scan(
                    CredId: 'dockerhub-creds',
                    ImageBase: 'springboot-api',
                    Tags: [env.BUILD_NUMBER, 'latest'],
                    ContextPath: '.'
                )
            }
        }

        stage('Generate SBOM') {
            steps {
                generate_sbom(Target: '.', Type: 'fs')
            }
        }
    }
}
```

More complete examples are available in the `examples/` folder.

## GenAI enhancement

This project includes an optional `genai_release_notes` step. It collects recent Git commit messages and available scan summaries, then sends a controlled prompt to an OpenAI-compatible API to create a release-risk summary.

The API token must be stored in Jenkins Credentials as Secret Text. Do not commit API keys into GitHub.

Example:

```groovy
genai_release_notes(
    CredentialsId: 'genai-api-token',
    Model: 'gpt-4o-mini',
    CommitLimit: 10
)
```

This is useful for creating quick release summaries after build, scan, and quality stages.

## Required Jenkins credentials

| Credential ID | Type | Used for |
|---|---|---|
| `dockerhub-creds` | Username with password | Docker login and push |
| `genai-api-token` | Secret text | Optional GenAI release summary |
| `slack-webhook` | Secret text | Optional Slack notification |

## Required Jenkins tools/plugins

- Jenkins Pipeline
- Credentials Binding Plugin
- Docker CLI on Jenkins agent
- Trivy installed on Jenkins agent
- SonarQube Scanner for Jenkins
- OWASP Dependency-Check Plugin
- SonarQube server configured in Jenkins

## Screenshots to add later

To make the GitHub repository stronger, add screenshots for Jenkins library setup, successful pipeline run, SonarQube quality gate, Trivy scan result, DockerHub image, and optional GenAI release summary. See `docs/SCREENSHOTS.md`.

## Career value

This project demonstrates:

- Jenkins Shared Library design
- Reusable CI/CD automation
- DevSecOps integration
- Docker automation
- Vulnerability scanning
- SBOM and supply-chain security basics
- AI-assisted release documentation
- Secure credential handling

## Suggested GitHub repo name

```text
jenkins-shared-library-devsecops
```

## Suggested GitHub description

```text
Reusable Jenkins Shared Library for Docker CI/CD, SonarQube, OWASP Dependency Check, Trivy scanning, SBOM generation, and optional GenAI release summaries.
```
