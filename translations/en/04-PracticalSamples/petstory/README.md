# Pet Story Generator Tutorial for Beginners

Upload a pet photo, analyze it with GPT-5.6 Luna, and generate a story from the resulting description. Both model requests use `reasoning_effort: none`.

| Component | Version |
| --- | --- |
| Java | 21 or higher |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Table of Contents

- [Prerequisites](#prerequisites)
- [Understanding the Project Structure](#understanding-the-project-structure)
- [Core Components Explained](#core-components-explained)
  - [1. Main Application](#1-main-application)
  - [2. Web Controller](#2-web-controller)
  - [3. Story Service](#3-story-service)
  - [4. Web Templates](#4-web-templates)
  - [5. Configuration](#5-configuration)
- [Running the Application](#running-the-application)
- [Offline Tests](#offline-tests)
- [How It All Works Together](#how-it-all-works-together)
- [Understanding the AI Integration](#understanding-the-ai-integration)
- [Next Steps](#next-steps)

## Prerequisites

Before starting, make sure you have:
- Java 21 or higher installed
- Maven for dependency management
- An Azure AI Foundry deployment of GPT-5.6 Luna named `gpt-5.6-luna`, or an `AZURE_OPENAI_DEPLOYMENT` override pointing to that deployment. See [Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) for provisioning and sign in with `az login` for keyless authentication. The deployment must support image input and `reasoning_effort: none`.
- Basic understanding of Java, Spring Boot, and web development

## Understanding the Project Structure

The pet story project has several important files:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Core Components Explained

### 1. Main Application

**File:** `PetStoryApplication.java`

This is the entry point for our Spring Boot application:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**What this does:**
- `@SpringBootApplication` annotation enables auto-configuration and component scanning
- Starts an embedded web server (Tomcat) on port 8080
- Creates all necessary Spring beans and services automatically

### 2. Web Controller

**File:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Request | Successful response |
| --- | --- | --- |
| `GET /` | No body | HTML upload form with a CSRF token |
| `POST /analyze-image` | `multipart/form-data`, file field `image` | JSON: `{"description":"A playful pet..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, field `description` | HTML result page with the description and generated story |

Both POST endpoints require the session cookie and CSRF token obtained from `GET /`. The upload script sends the hidden `_csrf` value in the `X-CSRF-TOKEN` header; story submission sends it as the `_csrf` form field. API clients must preserve the cookie between requests. These are form endpoints, not JSON request endpoints.

Descriptions must be nonempty and no longer than 1000 characters. The controller trims the description and strips `<`, `>`, double quotes, apostrophes, and `&` before passing it to the service. The result template also escapes model output with `th:text`.

Image validation failures return HTTP 400 with an `error` field; model failures return HTTP 502 with an `error` field and no `description`. Invalid story descriptions or model failures redirect to `/` with a visible error. Missing required fields return HTTP 400, and missing or invalid CSRF tokens return HTTP 403. No fallback descriptions or stories are presented as successful AI results.

### 3. Story Service

**File:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

The official OpenAI Java SDK 4.63.1 calls Azure AI Foundry's OpenAI-compatible Chat Completions API. Azure Identity 1.18.6 supplies a Microsoft Entra bearer token through `DefaultAzureCredential`; no API key is required.

| Operation | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Image bytes encoded as a base64 data URL with the uploaded MIME type | 300 |
| `generateStory` | A pet description in a user message | 800 |

Both requests use the configured deployment, defaulting to `gpt-5.6-luna`, and explicitly set `ReasoningEffort.NONE` (`reasoning_effort: none`). Neither request sends `temperature` or the legacy `max_tokens` parameter.

Image analysis accepts JPEG, PNG, GIF, and WebP, rejects empty images and files over 10MB, and limits the resulting description to 1000 characters. The story prompt requests a family-friendly short story. Empty choices or blank model content are errors, and failures preserve the original cause for server-side diagnostics. The SDK client is closed when the application shuts down.

### 4. Web Templates

**File:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Upload Form)

The page starts with a photo picker, not a description text area. **Analyze Image** previews the selected photo and posts it to `/analyze-image`. A successful response displays the description, fills the hidden `description` field, and reveals **Generate Story**. That button submits the existing form to `/generate-story`.

There is no browser model download or CDN dependency. Image analysis runs on the server through the configured Azure deployment. Failures remain visible and do not enable story generation with a fabricated description. Selecting a different file clears the previous analysis.

**File:** `result.html` (Story Display)

Shows the generated story:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Template features:**

1. **Thymeleaf Integration**: Uses `th:` attributes for dynamic content
2. **Responsive Design**: CSS styling for mobile and desktop
3. **Error Handling**: Displays validation errors to users
4. **Upload Handling**: JavaScript previews the photo, sends a CSRF-protected multipart request, and displays the returned description

### 5. Configuration

**File:** `application.properties`

Configuration settings for the application:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Configuration explained:**

1. **File Upload**: Both the file and the complete multipart request are capped at 10MB; keep photos below that limit to leave room for multipart headers
2. **Logging**: Controls what information is logged during execution
3. **Azure AI Foundry**: Specifies the endpoint and model deployment to use (keyless auth)
4. **Security**: CSRF protection remains enabled; model diagnostics are logged on the server, while the controller displays generic model-failure messages

## Running the Application

### Step 1: Sign In and Set Your Endpoint

Authentication is keyless (Microsoft Entra ID), so there's no API key. Sign in and set your Foundry endpoint:

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Why this is needed:**
- Azure AI Foundry uses Microsoft Entra ID to authenticate inference requests
- Keyless auth means no secrets in your source code or environment
- Your account needs the **Cognitive Services OpenAI User** role on the resource

The default deployment name is `gpt-5.6-luna`. If your GPT-5.6 Luna deployment has another name, set `AZURE_OPENAI_DEPLOYMENT` in the same terminal before starting the application. Both image analysis and story generation use this setting.

### Step 2: Build and Run

Navigate to the project directory:
```bash
cd 04-PracticalSamples/petstory
```

Build the standalone executable JAR and run all offline tests:
```bash
mvn clean package
```

Start the server:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

Alternatively, start the packaged JAR on a free port, for example:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

For that command, open `http://localhost:8083/`. The same `/analyze-image` and `/generate-story` routes are available on the selected port.

### Step 3: Test the Application

1. **Open** `http://localhost:8080` in your browser
2. **Select** a clear pet photo in JPEG, PNG, GIF, or WebP format, below 10MB
3. **Click** "Analyze Image" and wait for the pet description
4. **Click** "Generate Story" after successful analysis
5. **View** the story and use the result page's link to return to the upload form

The successful photo-to-story flow makes two model calls, one per button. Live inference consumes your deployment's quota and may incur charges; run smoke tests serially when sharing a rate-limited deployment. Loading the home page does not call the model.

## Offline Tests

From the sample directory, run:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) captures real OpenAI SDK requests with a loopback HTTP fixture. It checks both requests' deployment, `reasoning_effort: none`, token limits, image payload, input validation, empty responses, and upstream errors.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) uses MockMvc with a mocked model service to test the rendered Thymeleaf pages, upload contract, CSRF, validation, output escaping, and visible failures. These tests do not need Azure credentials and never call paid Azure inference. Maven writes Surefire reports under `target/surefire-reports`.

## How It All Works Together

Here's the complete flow when you generate a pet story:

1. **Photo Selection**: You choose a pet image in the upload form
2. **Image Upload**: "Analyze Image" sends a multipart POST to `/analyze-image` with the CSRF header
3. **Image Analysis**: `StoryService` sends the image to GPT-5.6 Luna with reasoning set to `none`
4. **Description Display**: The browser displays the returned description and stores it in the form
5. **Story Submission**: "Generate Story" posts `description` and `_csrf` to `/generate-story`
6. **Story Generation**: The controller validates the description and calls the same deployment with reasoning set to `none`
7. **Template Rendering**: Thymeleaf escapes and displays the description and story in the result page

**Error Handling Flow:**
If the model fails, the server logs the cause. Image analysis returns HTTP 502 and the browser shows the error without revealing "Generate Story". Story generation redirects to the form with an error message. Neither path silently substitutes a pre-written result.

## Understanding the AI Integration

### Azure AI Foundry (keyless)
The service configures the SDK with your resource's `/openai/v1/` endpoint. `DefaultAzureCredential` and `AuthenticationUtil.getBearerTokenSupplier` supply Microsoft Entra tokens for `https://ai.azure.com/.default`. Local development can use your Azure CLI sign-in; an Azure-hosted app can use a managed identity with the necessary resource permissions.

### Prompt Engineering
Image analysis requests observable pet features in a short paragraph and tells the model to treat text in the image as data, not instructions. Story generation uses the returned description in a separate, family-friendly writing request. Neither call enables reasoning or sets a temperature override.

### Response Processing
The shared response handler rejects missing choices and empty or whitespace-only content, trims valid content, and preserves upstream failures. Image descriptions are capped at 1000 characters to fit the subsequent story form. The original model failure is retained for diagnostics but not rendered to the user.

## Next Steps

For more examples, see [Chapter 04: Practical samples](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
This document has been translated using AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please be aware that automated translations may contain errors or inaccuracies. The original document in its native language should be considered the authoritative source. For critical information, professional human translation is recommended. We are not liable for any misunderstandings or misinterpretations arising from the use of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->