# Portfolio Notes

## Project title

Jenkins Shared Library for Reusable CI/CD and DevSecOps Pipelines

## Project summary

This project provides reusable Jenkins Shared Library functions for Docker build and push, Trivy scanning, OWASP Dependency Check, SonarQube analysis, quality gate validation, SBOM generation, Slack notification, and optional GenAI-assisted release summaries.

## Why this is useful

In real DevOps teams, many applications repeat the same Jenkins pipeline stages. A shared library reduces duplicate Jenkinsfile code and gives teams one central place to maintain common CI/CD logic.

## Career-progressive value

This project shows growth from basic Jenkins pipeline writing to reusable platform engineering practices. It demonstrates CI/CD standardization, DevSecOps controls, Docker automation, supply-chain security, and AI-assisted release documentation.

## Resume bullet

Built a Jenkins Shared Library to standardize CI/CD stages across projects, including Docker image build/push, SonarQube analysis, OWASP dependency scanning, Trivy vulnerability scanning, SBOM generation, Slack notification, and optional GenAI-assisted release summaries using secure Jenkins credentials.

## Interview explanation

In one of my portfolio projects, I created a Jenkins Shared Library so different application teams could reuse common CI/CD stages instead of writing the same Jenkinsfile logic again and again. I added reusable functions for Docker build and push, SonarQube analysis, OWASP dependency scanning, Trivy scanning, and quality gate checks.

I also enhanced it with more current DevSecOps practices like SBOM generation and an optional GenAI release summary step. The GenAI step uses Jenkins credentials, reads commit history and scan summaries, and creates a release-risk summary artifact. I kept secrets out of code and made the AI step optional so it can be safely enabled only where required.
