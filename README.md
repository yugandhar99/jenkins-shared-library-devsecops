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

---

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0F2027,50:2C5364,100:00C9FF&height=120&section=footer&text=Let's%20Connect&fontColor=ffffff&fontSize=32&fontAlignY=70" />
</p>

<h2 align="center">🤝 Connect With Me</h2>

<p align="center">
  <em>
    Thanks for visiting this project! I’m continuously building hands-on DevOps, Cloud, Automation, and AI-enabled engineering projects to improve real-world deployment, monitoring, and infrastructure skills.
  </em>
</p>

<p align="center">
  <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=22&duration=2500&pause=800&color=00C9FF&center=true&vCenter=true&width=650&lines=DevOps+%7C+Cloud+%7C+Automation;CI%2FCD+%7C+Docker+%7C+Kubernetes+%7C+Terraform;Building+real-world+projects+one+commit+at+a+time" alt="Typing SVG" />
</p>

<p align="center">
  <a href="https://github.com/yugandhar99" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-yugandhar99-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub" />
  </a>
  <a href="https://www.linkedin.com/in/yugandhar-chowdary-33baa4216" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-Yugandhar%20Chowdary-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" />
  </a>
  <a href="https://yugandhar-portfolio-psi.vercel.app/" target="_blank">
    <img src="https://img.shields.io/badge/Portfolio-View%20My%20Work-FF5722?style=for-the-badge&logo=vercel&logoColor=white" alt="Portfolio" />
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Focus-DevOps%20Engineering-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Cloud-AWS%20%7C%20Azure%20%7C%20GCP-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/IaC-Terraform-purple?style=flat-square" />
  <img src="https://img.shields.io/badge/Containers-Docker%20%7C%20Kubernetes-2496ED?style=flat-square" />
</p>

---

<p align="center">
  ⭐ If this project added value, feel free to star the repository and connect with me!
</p>

<p align="center">
  <strong>Built with ❤️ using modern DevOps practices</strong>
</p>
