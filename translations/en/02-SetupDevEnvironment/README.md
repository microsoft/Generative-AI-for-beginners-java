<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:10:19+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "en"
}
-->
# Setting Up the Development Environment for Generative AI for Java

> **Quick Start**: Code in the Cloud in 2 minutes - Jump to [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) - no local installation required and uses GitHub Models!

> **Interested in Azure OpenAI?** See our [Azure OpenAI Setup Guide](getting-started-azure-openai.md) for steps to create a new Azure OpenAI resource.

## What You'll Learn

- How to set up a Java development environment for AI applications
- How to choose and configure your preferred development environment (cloud-first with Codespaces, local dev container, or full local setup)
- How to test your setup by connecting to GitHub Models

## Table of Contents

- [What You'll Learn](../../../02-SetupDevEnvironment)
- [Introduction](../../../02-SetupDevEnvironment)
- [Step 1: Set Up Your Development Environment](../../../02-SetupDevEnvironment)
  - [Option A: GitHub Codespaces (Recommended)](../../../02-SetupDevEnvironment)
  - [Option B: Local Dev Container](../../../02-SetupDevEnvironment)
  - [Option C: Use Your Existing Local Installation](../../../02-SetupDevEnvironment)
- [Step 2: Create GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Step 3: Test Your Setup](../../../02-SetupDevEnvironment)
- [Troubleshooting](../../../02-SetupDevEnvironment)
- [Summary](../../../02-SetupDevEnvironment)
- [Next Steps](../../../02-SetupDevEnvironment)

## Introduction

This chapter will guide you through setting up a development environment. We'll use **GitHub Models** as our primary example because it's free, easy to set up with just a GitHub account, requires no credit card, and provides access to multiple models for experimentation.

**No local setup required!** You can start coding immediately using GitHub Codespaces, which provides a full development environment in your browser.

<img src="./images/models.webp" alt="Screenshot: GitHub Models" width="50%">

We recommend using [**GitHub Models**](https://github.com/marketplace?type=models) for this course because it's:
- **Free** to get started
- **Easy** to set up with just a GitHub account
- **No credit card** required
- **Multiple models** available for experimentation

> **Note**: The GitHub Models used in this training have these free limits:
> - 15 requests per minute (150 per day)
> - ~8,000 words in, ~4,000 words out per request
> - 5 concurrent requests
> 
> For production use, upgrade to Azure AI Foundry Models with your Azure account. Your code doesn't need to change. See the [Azure AI Foundry documentation](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Step 1: Set Up Your Development Environment

<a name="quick-start-cloud"></a>

We've created a preconfigured development container to minimize setup time and ensure you have all the necessary tools for this Generative AI for Java course. Choose your preferred development approach:

### Environment Setup Options:

#### Option A: GitHub Codespaces (Recommended)

**Start coding in 2 minutes - no local setup required!**

1. Fork this repository to your GitHub account  
   > **Note**: If you want to edit the basic config, check the [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Click **Code** → **Codespaces** tab → **...** → **New with options...**
3. Use the defaults – this will select the **Dev container configuration**: **Generative AI Java Development Environment** custom devcontainer created for this course
4. Click **Create codespace**
5. Wait ~2 minutes for the environment to be ready
6. Proceed to [Step 2: Create GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Screenshot: Codespaces submenu" width="50%">

<img src="./images/image.png" alt="Screenshot: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Create codespace options" width="50%">

> **Benefits of Codespaces**:
> - No local installation required
> - Works on any device with a browser
> - Pre-configured with all tools and dependencies
> - Free 60 hours per month for personal accounts
> - Consistent environment for all learners

#### Option B: Local Dev Container

**For developers who prefer local development with Docker**

1. Fork and clone this repository to your local machine  
   > **Note**: If you want to edit the basic config, check the [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/) and [VS Code](https://code.visualstudio.com/)
3. Install the [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) in VS Code
4. Open the repository folder in VS Code
5. When prompted, click **Reopen in Container** (or use `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Wait for the container to build and start
7. Proceed to [Step 2: Create GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Screenshot: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev container build complete" width="50%">

#### Option C: Use Your Existing Local Installation

**For developers with existing Java environments**

Prerequisites:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) or your preferred IDE

Steps:
1. Clone this repository to your local machine
2. Open the project in your IDE
3. Proceed to [Step 2: Create GitHub Token](../../../02-SetupDevEnvironment)

> **Pro Tip**: If you have a low-spec machine but want VS Code locally, use GitHub Codespaces! You can connect your local VS Code to a cloud-hosted Codespace for the best of both worlds.

<img src="./images/image-2.png" alt="Screenshot: created local devcontainer instance" width="50%">

## Step 2: Create a GitHub Personal Access Token

1. Navigate to [GitHub Settings](https://github.com/settings/profile) and select **Settings** from your profile menu.
2. In the left sidebar, click **Developer settings** (usually at the bottom).
3. Under **Personal access tokens**, click **Fine-grained tokens** (or follow this direct [link](https://github.com/settings/personal-access-tokens)).
4. Click **Generate new token**.
5. Under "Token name", provide a descriptive name (e.g., `GenAI-Java-Course-Token`).
6. Set an expiration date (recommended: 7 days for security best practices).
7. Under "Resource owner", select your user account.
8. Under "Repository access", select the repositories you want to use with GitHub Models (or "All repositories" if needed).
9. Under "Repository permissions", find **Models** and set it to **Read and write**.
10. Click **Generate token**.
11. **Copy and save your token now** – you won't see it again!

> **Security Tip**: Use the minimum required scope and shortest practical expiration time for your access tokens.

## Step 3: Test Your Setup with the GitHub Models Example

Once your development environment is ready, let's test the GitHub Models integration with our example application in [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Open the terminal in your development environment.
2. Navigate to the GitHub Models example:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Set your GitHub token as an environment variable:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Run the application:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

You should see output similar to:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Understanding the Example Code

First, let's understand what we just ran. The example under `examples/github-models` uses the OpenAI Java SDK to connect to GitHub Models:

**What this code does:**
- **Connects** to GitHub Models using your personal access token
- **Sends** a simple "Say Hello World!" message to the AI model
- **Receives** and displays the AI's response
- **Validates** your setup is working correctly

**Key Dependency** (in `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Main Code** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Summary

Great! You now have everything set up:

- Created a GitHub Personal Access Token with the right permissions for AI model access
- Got your Java development environment running (whether that's Codespaces, dev containers, or local)
- Connected to GitHub Models using the OpenAI Java SDK for free AI development
- Tested it all works with a simple example that talks to AI models

## Next Steps

[Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md)

## Troubleshooting

Having issues? Here are common problems and solutions:

- **Token not working?** 
  - Ensure you copied the entire token without any extra spaces
  - Verify the token is set correctly as an environment variable
  - Check that your token has the correct permissions (Models: Read and write)

- **Maven not found?** 
  - If using dev containers/Codespaces, Maven should be pre-installed
  - For local setup, ensure Java 21+ and Maven 3.9+ are installed
  - Try `mvn --version` to verify installation

- **Connection issues?** 
  - Check your internet connection
  - Verify GitHub is accessible from your network
  - Ensure you're not behind a firewall blocking the GitHub Models endpoint

- **Dev container not starting?** 
  - Ensure Docker Desktop is running (for local development)
  - Try rebuilding the container: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Application compilation errors?**
  - Ensure you're in the correct directory: `02-SetupDevEnvironment/examples/github-models`
  - Try cleaning and rebuilding: `mvn clean compile`

> **Need help?**: Still having issues? Open an issue in the repository and we'll help you out.

**Disclaimer**:  
This document has been translated using the AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we aim for accuracy, please note that automated translations may include errors or inaccuracies. The original document in its native language should be regarded as the authoritative source. For critical information, professional human translation is advised. We are not responsible for any misunderstandings or misinterpretations resulting from the use of this translation.