# Security Notes

This repository is designed for Jenkins reusable pipeline automation. Do not commit secrets into this repository.

## Do not commit

- DockerHub passwords or access tokens
- SonarQube tokens
- GenAI/OpenAI API keys
- Slack webhook URLs
- private SSH keys
- `.env` files
- cloud access keys

## Recommended approach

Store sensitive values in Jenkins Credentials:

| Secret | Jenkins Credential Type | Example Credential ID |
|---|---|---|
| DockerHub username/password or token | Username with password | `dockerhub-creds` |
| GenAI API token | Secret text | `genai-api-token` |
| Slack webhook URL | Secret text | `slack-webhook` |
| SonarQube token | Managed by SonarQube Jenkins config | `sonarqube-server` |

## Pipeline security practices

- Use `--password-stdin` for Docker login.
- Archive scan reports as build artifacts.
- Fail the build for high and critical vulnerabilities where possible.
- Generate SBOM artifacts for better supply-chain visibility.
- Keep AI summary generation optional and avoid sending sensitive files to external APIs.
