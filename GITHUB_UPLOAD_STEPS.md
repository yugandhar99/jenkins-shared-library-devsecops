# GitHub Upload Steps

## Option 1: Upload using GitHub website

1. Create a new GitHub repository.
2. Do not select README, `.gitignore`, or license while creating the repo because this project already includes them.
3. Extract the ZIP file.
4. Open the extracted folder.
5. Select all files and folders inside the project folder.
6. Drag and drop them into the GitHub upload page.
7. Commit the files.

## Option 2: Upload using Git commands

```bash
git init
git add .
git commit -m "Initial commit - Jenkins shared library project"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/jenkins-shared-library-devsecops.git
git push -u origin main
```

## Suggested repository name

```text
jenkins-shared-library-devsecops
```

## Suggested GitHub description

```text
Reusable Jenkins Shared Library for Docker CI/CD, SonarQube, OWASP Dependency Check, Trivy scanning, SBOM generation, and optional GenAI release summaries.
```
