# Pet Story Generator Tori Tok for Beginners

Upload pet foto, use GPT-5.6 Luna check am, then make tori from di description wey e give. Both model request dey use `reasoning_effort: none`.

| Component | Version |
| --- | --- |
| Java | 21 or pass |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Table of Contents

- [Wetin You Go Need](#wetin-you-go-need)
- [How Project Setup Be](#how-project-setup-be)
- [Wetin Di Core Components Mean](#wetin-di-core-components-mean)
  - [1. Main Application](#1-main-application)
  - [2. Web Controller](#2-web-controller)
  - [3. Story Service](#3-story-service)
  - [4. Web Templates](#4-web-templates)
  - [5. Configuration](#5-configuration)
- [How To Run Di Application](#how-to-run-di-application)
- [Offline Tests](#offline-tests)
- [How E Dey Work Together](#how-e-dey-work-together)
- [How AI Dem Join Am](#how-ai-dem-join-am)
- [Next Steps](#next-steps)

## Wetin You Go Need

Before you start, make sure say:
- Java 21 or pass dey installed
- Maven dey for dependency management
- Azure AI Foundry GPT-5.6 Luna deployment wey dem name `gpt-5.6-luna`, or make you get `AZURE_OPENAI_DEPLOYMENT` override wey point to dat deployment. See [Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) for how to arrange am and sign in with `az login` for keyless authentication. Di deployment gats fit take image input and `reasoning_effort: none`.
- Basic sabi Java, Spring Boot, and web development

## How Project Setup Be

Di pet story project get plenti important files:

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

## Wetin Di Core Components Mean

### 1. Main Application

**File:** `PetStoryApplication.java`

Na di main entry point for our Spring Boot application be dis:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Wetin dis one dey do:**
- `@SpringBootApplication` annotation dey enable auto-configuration and component scanning
- E dey start embedded web server (Tomcat) for port 8080
- E dey automatically create all Spring beans and services wey necessary

### 2. Web Controller

**File:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Request | Successful response |
| --- | --- | --- |
| `GET /` | No body | HTML upload form wey get CSRF token |
| `POST /analyze-image` | `multipart/form-data`, file field `image` | JSON: `{"description":"A playful pet..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, field `description` | HTML result page wey get di description and di created story |

Both POST endpoints gats session cookie and CSRF token wey you get from `GET /`. Di upload script dey send di hidden `_csrf` value for `X-CSRF-TOKEN` header; story submission go send am as `_csrf` form field. API clients gats save di cookie between requests. Dem be form endpoints, no be JSON request endpoints.

Description gats no empty and no pass 1000 characters. Di controller go trim description, remove `<`, `>`, double quotes, apostrophes, and `&` before e pass am to di service. Di result template go also escape model output with `th:text`.

If image validation fail, e go return HTTP 400 with `error` field; model failure go return HTTP 502 with `error` field and no `description`. Wrong story description or model failure go redirect go `/` with error wey you fit see. If required fields miss, e go return HTTP 400, and if CSRF token miss or no valid, e go return HTTP 403. No fallback descriptions or stories dey show as successful AI results.

### 3. Story Service

**File:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Di official OpenAI Java SDK 4.63.1 dey call Azure AI Foundry OpenAI-compatible Chat Completions API. Azure Identity 1.18.6 dey provide Microsoft Entra bearer token through `DefaultAzureCredential`; no API key tey tey require.

| Operation | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Image bytes wey encoded as base64 data URL with uploaded MIME type | 300 |
| `generateStory` | Pet description for user message | 800 |

Both request dey use di configured deployment, wey by default na `gpt-5.6-luna`, and dem set `ReasoningEffort.NONE` (`reasoning_effort: none`). No request dey send `temperature` or old `max_tokens` parameter.

Image analysis fit handle JPEG, PNG, GIF, and WebP, e no go accept empty images or files wey pass 10MB, and e limit description to 1000 characters. Di story prompt dey ask for short family-friendly story. Empty choices or blank model content be error, and failure go keep original cause for server diagnosis. SDK client go close wen application shut down.

### 4. Web Templates

**File:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Upload Form)

Di page dey start wit photo picker, no be description text area. **Analyze Image** go preview di selected photo and post am go `/analyze-image`. If e successful, e go show description, fill hidden `description` field, and make **Generate Story** show. Dis button go submit di form to `/generate-story`.

No browser model download or CDN dey. Image analysis dey run on server through Azure deployment. Failure go remain visible, no story generation with fake description. If you select different file, e go clear old analysis.

**File:** `result.html` (Story Display)

E dey show di generated story:

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

1. **Thymeleaf Integration**: E dey use `th:` attributes for dynamic content
2. **Responsive Design**: CSS styling for mobile and desktop
3. **Error Handling**: E dey show validation errors to users
4. **Upload Handling**: JavaScript dey preview foto, send CSRF-protected multipart request, and display description wey e return

### 5. Configuration

**File:** `application.properties`

Di settings for application configuration:

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

**Wetin configuration mean:**

1. **File Upload**: Both file and complete multipart request size capped at 10MB; try make photos below dis limit to allow multipart headers
2. **Logging**: E control wetin dem dey log during application run
3. **Azure AI Foundry**: E specify endpoint and model deployment to use (keyless auth)
4. **Security**: CSRF protection still dey active; model diagnostics dey logged on server, controller go just show general model failure message

## How To Run Di Application

### Step 1: Sign In and Set Your Endpoint

Authentication no need key (Microsoft Entra ID), so no API key dey involved. Sign in and set your Foundry endpoint:

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

**Why e dey important:**
- Azure AI Foundry dey use Microsoft Entra ID authenticate inference request
- Keyless auth mean no secrets for your source code or environment
- Your account need **Cognitive Services OpenAI User** role on di resource

Di default deployment name na `gpt-5.6-luna`. If your GPT-5.6 Luna deployment get one oda name, set `AZURE_OPENAI_DEPLOYMENT` for di same terminal before you start di application. Both image analysis and story generation dey use dis setting.

### Step 2: Build and Run

Go project directory:
```bash
cd 04-PracticalSamples/petstory
```

Build di standalone executable JAR and run all offline tests:
```bash
mvn clean package
```

Start di server:
```bash
mvn spring-boot:run
```

Di application go start for `http://localhost:8080`.

Alternatively, start di packaged JAR on one free port, for example:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

For dat command, open `http://localhost:8083/`. Di same `/analyze-image` and `/generate-story` routes dey available for di selected port.

### Step 3: Test the Application

1. **Open** `http://localhost:8080` for your browser
2. **Select** clear pet photo for JPEG, PNG, GIF, or WebP format, below 10MB
3. **Click** "Analyze Image" and wait for pet description
4. **Click** "Generate Story" after analysis successful
5. **View** di story and use di result page link to go back to upload form

Di successful photo-to-story flow dey make two model calls, one per button. Live inference dey use your deployment quota and fit make you pay; make you run smoke tests one by one if you dey share rate-limited deployment. Just opening home page no dey call model.

## Offline Tests

From sample directory, run:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) dey capture real OpenAI SDK requests wit loopback HTTP fixture. E dey check deployment, `reasoning_effort: none`, token limits, image payload, input validation, empty responses, and upstream errors for both requests.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) dey use MockMvc wit mocked model service to test Thymeleaf pages rendering, upload contract, CSRF, validation, output escaping, and visible failure. These tests no need Azure credentials and no ever call paid Azure inference. Maven go write Surefire reports under `target/surefire-reports`.

## How E Dey Work Together

Dis na di full flow when you wan generate pet story:

1. **Photo Selection**: You choose pet image for upload form
2. **Image Upload**: "Analyze Image" send multipart POST to `/analyze-image` wit CSRF header
3. **Image Analysis**: `StoryService` go send image to GPT-5.6 Luna with reasoning set to `none`
4. **Description Display**: Browser go show description and store am for form
5. **Story Submission**: "Generate Story" go post `description` and `_csrf` to `/generate-story`
6. **Story Generation**: Controller go validate description and call same deployment with reasoning set to `none`
7. **Template Rendering**: Thymeleaf go escape and show description and story for result page

**Error Handling Flow:**
If model fail, server go log di reason. Image analysis go return HTTP 502 and browser go show error without "Generate Story". Story generation go redirect back to form wit error message. Neither go quietly replace wit pre-made result.

## How AI Dem Join Am

### Azure AI Foundry (keyless)
Service dey configure SDK wit your resource `/openai/v1/` endpoint. `DefaultAzureCredential` and `AuthenticationUtil.getBearerTokenSupplier` supply Microsoft Entra tokens for `https://ai.azure.com/.default`. Local development fit use your Azure CLI sign-in; Azure-hosted app fit use managed identity wit correct resource permission.

### Prompt Engineering
Image analysis dey ask make model see pet features in short paragraph and treat text inside image as data, no be commands. Story generation go use returned description for family-friendly writing request separately. Neither call enable reasoning or set temperature override.

### Response Processing
Shared response handler go reject missing choices and empty/whitespace-only content, trim valid content, and keep upstream failure info. Image descriptions capped at 1000 characters to fit next story form. Original model failure dey for diagnostics but no dey show user.

## Next Steps

For more examples, check [Chapter 04: Practical samples](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg make you know say automated translation fit get errors or mistakes. Di original document for dia own language na im be di correct source. For important info, make person wey sabi human translation do am. We no go responsible for any misunderstanding or wrong understanding wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->