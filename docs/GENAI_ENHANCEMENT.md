# GenAI Enhancement

This project includes an optional Jenkins Shared Library step named `genai_release_notes`.

The goal is not to replace DevOps review. The goal is to help the release team quickly summarize:

- recent Git changes
- security scan observations
- deployment risk level
- next recommended action

## How it works

The step reads recent Git commit messages and available scan report files from the `reports/` folder. It creates a prompt and sends it to an OpenAI-compatible chat completion API.

The API token is not stored in code. It must be stored in Jenkins Credentials as a Secret Text credential.

Recommended Jenkins credential id:

```text
genai-api-token
```

## Example usage

```groovy
genai_release_notes(
    CredentialsId: 'genai-api-token',
    Model: 'gpt-4o-mini',
    CommitLimit: 10
)
```

## Safe usage notes

- Do not send application secrets, `.env` files, private keys, or customer data to any AI API.
- Keep the feature optional by controlling it with `GENAI_ENABLED=true`.
- Review the AI-generated output before sharing it with business or production teams.

## Interview explanation

You can explain this as:

> I added an optional GenAI-assisted release summary step to the Jenkins shared library. It collects recent commit information and security scan summaries, sends a controlled prompt to an AI API using Jenkins credentials, and archives the generated release summary as a pipeline artifact. This helps teams review changes and risks faster without exposing credentials in source code.
