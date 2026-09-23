# Pet Story Generator Tutorial para sa mga Nagsisimula

Mag-upload ng larawan ng alagang hayop, suriin ito gamit ang GPT-5.6 Luna, at gumawa ng kwento mula sa resulta ng paglalarawan. Parehong gumagamit ang mga kahilingan ng modelo ng `reasoning_effort: none`.

| Component | Bersyon |
| --- | --- |
| Java | 21 o mas mataas |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Tala ng Mga Nilalaman

- [Mga Kinakailangan](#mga-kinakailangan)
- [Pag-unawa sa Istruktura ng Proyekto](#pag-unawa-sa-istruktura-ng-proyekto)
- [Paliwanag ng mga Pangunahing Komponent](#paliwanag-ng-mga-pangunahing-komponent)
  - [1. Main Application](#1-main-application)
  - [2. Web Controller](#2-web-controller)
  - [3. Story Service](#3-story-service)
  - [4. Web Templates](#4-web-templates)
  - [5. Configuration](#5-configuration)
- [Pagpapatakbo ng Aplikasyon](#pagpapatakbo-ng-aplikasyon)
- [Mga Offline na Pagsusulit](#mga-offline-na-pagsusulit)
- [Paano Ito Lahat Gumagana Nang Sabay](#paano-ito-lahat-gumagana-nang-sabay)
- [Pag-unawa sa Integrasyon ng AI](#pag-unawa-sa-integrasyon-ng-ai)
- [Mga Susunod na Hakbang](#mga-susunod-na-hakbang)

## Mga Kinakailangan

Bago magsimula, siguraduhing mayroon kang:
- Java 21 o mas mataas ang naka-install
- Maven para sa pangangasiwa ng dependency
- Isang Azure AI Foundry deployment ng GPT-5.6 Luna na pinangalanang `gpt-5.6-luna`, o isang `AZURE_OPENAI_DEPLOYMENT` override na tumuturo sa deployment na iyon. Tingnan ang [Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) para sa provisioning at mag-sign in gamit ang `az login` para sa keyless na pagpapatotoo. Dapat suportahan ng deployment ang image input at `reasoning_effort: none`.
- Pangunahing pag-unawa sa Java, Spring Boot, at web development

## Pag-unawa sa Istruktura ng Proyekto

Ang pet story project ay may ilang mahahalagang files:

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

## Paliwanag ng mga Pangunahing Komponent

### 1. Main Application

**File:** `PetStoryApplication.java`

Ito ang entry point para sa aming Spring Boot application:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ano ang ginagawa nito:**
- Ang annotation na `@SpringBootApplication` ay nagpapagana ng auto-configuration at component scanning
- Nagsisimula ng embedded web server (Tomcat) sa port 8080
- Kusang lumilikha ng lahat ng kinakailangang Spring beans at services

### 2. Web Controller

**File:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Request | Matagumpay na tugon |
| --- | --- | --- |
| `GET /` | Walang katawan | HTML upload form na may CSRF token |
| `POST /analyze-image` | `multipart/form-data`, file field `image` | JSON: `{"description":"A playful pet..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, field `description` | HTML resulta ng pahina na may paglalarawan at generated na kwento |

Parehong POST endpoints ay nangangailangan ng session cookie at CSRF token mula sa `GET /`. Ipinapadala ng upload script ang nakatagong `_csrf` value sa `X-CSRF-TOKEN` header; ipinapasa naman ito sa kwento bilang `_csrf` na form field. Dapat panatilihin ng mga API client ang cookie sa pagitan ng mga kahilingan. Ang mga ito ay form endpoints, hindi JSON request endpoints.

Dapat hindi walang laman at hindi lalampas sa 1000 characters ang mga paglalarawan. Pinipino ng controller ang paglalarawan at tinatanggal ang `<`, `>`, double quotes, apostrophes, at `&` bago ito ipadala sa service. Pinoprotektahan din ng resulta ng template ang output ng modelo gamit ang `th:text`.

Ang mga pagkabigo sa pag-validate ng larawan ay nagbabalik ng HTTP 400 na may `error` na field; ang pagkabigo sa modelo ay nagbabalik ng HTTP 502 na may `error` na field at walang `description`. Ang mga di-wastong paglalarawan ng kwento o pagkabigo ng modelo ay nire-redirect sa `/` na may nakitang error. Ang nawawalang mga kinakailangang fields ay nagbabalik ng HTTP 400, at ang nawawala o di-wastong CSRF tokens ay nagbabalik ng HTTP 403. Walang fallback na paglalarawan o kwento ang ipinapakita bilang matagumpay na resulta ng AI.

### 3. Story Service

**File:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Ang opisyal na OpenAI Java SDK 4.63.1 ay tumatawag sa Azure AI Foundry's OpenAI-compatible Chat Completions API. Nagbibigay ang Azure Identity 1.18.6 ng Microsoft Entra bearer token gamit ang `DefaultAzureCredential`; hindi kailangan ng API key.

| Operasyon | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Mga bytes ng larawan na naka-encode bilang base64 data URL na may MIME type ng na-upload na file | 300 |
| `generateStory` | Isang paglalarawan ng alaga sa isang mensahe ng user | 800 |

Parehong kahilingan ay gumagamit ng na-configure na deployment, default sa `gpt-5.6-luna`, at tahasang itinatakda ang `ReasoningEffort.NONE` (`reasoning_effort: none`). Walang nagpapadala ng `temperature` o ng legacy na `max_tokens` parameter.

Tinatanggap ng pagsusuri ng larawan ang JPEG, PNG, GIF, at WebP, tinatanggihan ang mga walang laman na larawan at mga file na higit sa 10MB, at nililimitahan ang resulta ng paglalarawan sa 1000 characters. Humihiling ang story prompt ng isang family-friendly na maikling kwento. Ang mga walang laman na pagpipilian o blangkong nilalaman ng modelo ay mga error, at pinananatili ang orihinal na sanhi ng pagkabigo para sa server-side diagnostics. Isinasara ang SDK client kapag nagsara ang application.

### 4. Web Templates

**File:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Upload Form)

Nagsisimula ang pahina sa isang photo picker, hindi sa isang description text area. **Analyze Image** ay nagbibigay ng preview ng napiling larawan at ipinapadala ito sa `/analyze-image`. Ang matagumpay na tugon ay nagpapakita ng paglalarawan, pinupuno ang nakatagong `description` field, at ipinapakita ang **Generate Story**. Ang button na iyon ay nagsusumite ng umiiral na form sa `/generate-story`.

Walang browser model download o CDN dependency. Ang pagsusuri ng larawan ay tumatakbo sa server sa pamamagitan ng naka-configure na Azure deployment. Mananatiling nakikita ang mga pagkabigo at hindi pinapayagan ang pagbuo ng kwento gamit ang pekeng paglalarawan. Ang pagpili ng ibang file ay naglilinis ng naunang pagsusuri.

**File:** `result.html` (Pagpapakita ng Kwento)

Ipinapakita ang nabuo na kwento:

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

**Mga tampok ng template:**

1. **Thymeleaf Integration**: Gumagamit ng `th:` attributes para sa dynamic na nilalaman
2. **Responsive Design**: CSS styling para sa mobile at desktop
3. **Error Handling**: Ipinapakita ang mga validation errors sa mga user
4. **Upload Handling**: JavaScript na nagpe-preview ng larawan, nagpapadala ng CSRF-protected na multipart request, at nagpapakita ng ibinalik na paglalarawan

### 5. Configuration

**File:** `application.properties`

Mga setting ng configuration para sa aplikasyon:

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

**Paliwanag sa configuration:**

1. **Pag-upload ng File**: Parehong ang file at ang buong multipart request ay nililimitahan sa 10MB; panatilihing mas mababa sa limit ang mga larawan upang may puwang para sa multipart headers
2. **Pag-log**: Kinokontrol kung anong impormasyon ang nilalathala habang tumatakbo ang aplikasyon
3. **Azure AI Foundry**: Itinatakda ang endpoint at model deployment na gagamitin (keyless auth)
4. **Seguridad**: Mananatiling naka-enable ang CSRF protection; ang mga diagnostic ng modelo ay nilalathala sa server, habang ang controller ay nagpapakita ng pangkalahatang mga mensahe ng pagkabigo ng modelo

## Pagpapatakbo ng Aplikasyon

### Hakbang 1: Mag-Sign In at Itakda ang Iyong Endpoint

Ang pagpapatotoo ay keyless (Microsoft Entra ID), kaya walang API key. Mag-sign in at itakda ang iyong Foundry endpoint:

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

**Bakit ito kailangan:**
- Ginagamit ng Azure AI Foundry ang Microsoft Entra ID para i-authenticate ang mga inference request
- Ang keyless auth ay nangangahulugang walang sikreto sa iyong source code o environment
- Kailangan ng iyong account ang **Cognitive Services OpenAI User** role sa resource

Ang default na pangalan ng deployment ay `gpt-5.6-luna`. Kung ang iyong GPT-5.6 Luna deployment ay may ibang pangalan, itakda ang `AZURE_OPENAI_DEPLOYMENT` sa parehong terminal bago simulan ang aplikasyon. Parehong image analysis at story generation ay gumagamit ng setting na ito.

### Hakbang 2: I-build at Patakbuhin

Pumunta sa directory ng proyekto:
```bash
cd 04-PracticalSamples/petstory
```

I-build ang standalone executable na JAR at patakbuhin ang lahat ng offline na pagsubok:
```bash
mvn clean package
```

Simulan ang server:
```bash
mvn spring-boot:run
```

Magsisimula ang aplikasyon sa `http://localhost:8080`.

Bilang alternatibo, simulan ang naka-package na JAR sa isang libreng port, halimbawa:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Para sa utos na iyon, buksan ang `http://localhost:8083/`. Ang parehong mga ruta na `/analyze-image` at `/generate-story` ay available sa napiling port.

### Hakbang 3: Subukan ang Application

1. **Buksan** ang `http://localhost:8080` sa iyong browser
2. **Piliin** ang malinaw na larawan ng alaga sa format na JPEG, PNG, GIF, o WebP, na mas mababa sa 10MB
3. **I-click ang** "Analyze Image" at hintayin ang paglalarawan ng alaga
4. **I-click ang** "Generate Story" pagkatapos ng matagumpay na pagsusuri
5. **Tingnan** ang kwento at gamitin ang link sa resulta ng pahina para bumalik sa upload form

Ang matagumpay na photo-to-story na daloy ay gumagawa ng dalawang tawag sa modelo, isa sa bawat button. Ang live inference ay kumokonsumo ng quota ng iyong deployment at maaaring magdulot ng bayarin; patakbuhin ang mga smoke test nang sunud-sunod kapag nagbabahagi ng rate-limited na deployment. Ang pag-load ng home page ay hindi tumatawag ng modelo.

## Mga Offline na Pagsusulit

Mula sa sample directory, patakbuhin:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) ay kumukuha ng totoong OpenAI SDK requests gamit ang loopback HTTP fixture. Sinusuri nito ang deployment ng parehong mga request, `reasoning_effort: none`, mga limitasyon ng token, payload ng larawan, input validation, walang laman na tugon, at upstream na mga pagkakamali.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) ay gumagamit ng MockMvc na may mocked model service upang subukan ang mga rendered Thymeleaf na pahina, upload contract, CSRF, validation, pag-escape ng output, at nakikita na mga pagkabigo. Hindi kailangan ng mga pagsusulit na ito ang Azure credentials at hindi tumatawag sa bayad na Azure inference. Ang Maven ay nagsusulat ng Surefire reports sa ilalim ng `target/surefire-reports`.

## Paano Ito Lahat Gumagana Nang Sabay

Narito ang kumpletong daloy kapag gumagawa ka ng kwento ng alaga:

1. **Pagpili ng Larawan**: Pumili ka ng larawan ng alaga sa upload form
2. **Pag-upload ng Larawan**: Pinapadala ng "Analyze Image" ang multipart POST sa `/analyze-image` kasama ang CSRF header
3. **Pagsusuri ng Larawan**: Pinapadala ng `StoryService` ang larawan sa GPT-5.6 Luna na may reasoning na naka-set sa `none`
4. **Pagpapakita ng Paglalarawan**: Ipinapakita ng browser ang ibinalik na paglalarawan at iniimbak ito sa form
5. **Pagsusumite ng Kwento**: Pinapadala ng "Generate Story" ang `description` at `_csrf` sa `/generate-story`
6. **Pagbuo ng Kwento**: Vini-validate ng controller ang paglalarawan at tinatawag ang parehong deployment na may reasoning na naka-set sa `none`
7. **Pag-render ng Template**: In-e-escape ng Thymeleaf at ipinapakita ang paglalarawan at kwento sa resulta ng pahina

**Daloy ng Pag-handle ng Error:**
Kung mabigo ang modelo, nilalathala ng server ang sanhi. Ang pagsusuri ng larawan ay nagbabalik ng HTTP 502 at ipinapakita ng browser ang error nang hindi ipinapakita ang "Generate Story". Ang pagbuo ng kwento ay nire-redirect sa form na may mensahe ng error. Walang landas ang tahimik na pumapalit ng pre-sinulat na resulta.

## Pag-unawa sa Integrasyon ng AI

### Azure AI Foundry (keyless)
Kinokonekta ng serbisyo ang SDK sa endpoint na `/openai/v1/` ng iyong resource. Nagbibigay ang `DefaultAzureCredential` at `AuthenticationUtil.getBearerTokenSupplier` ng Microsoft Entra tokens para sa `https://ai.azure.com/.default`. Maaaring gamitin ng lokal na pag-develop ang Azure CLI sign-in; ang Azure-hosted app ay maaaring gumamit ng managed identity na may kinakailangang permiso sa resource.

### Prompt Engineering
Ang pagsusuri ng larawan ay humihiling ng mga napapansing tampok ng alaga sa isang maikling talata at sinasabi sa modelo na ituring ang teksto sa larawan bilang data, hindi bilang mga utos. Ginagamit ng pagbuo ng kwento ang ibinalik na paglalarawan sa isang hiwalay, family-friendly na request para sa pagsulat. Walang tawag ang nagpapagana ng reasoning o nagtatakda ng temperature override.

### Pagsusuri ng Tugon
Tinanggihan ng pinag-isang response handler ang nawawalang mga pagpipilian at walang laman o blangkong nilalaman, pinipino ang wastong nilalaman, at pinananatili ang upstream failures. Nililimitahan sa 1000 characters ang mga paglalarawan ng larawan upang magkasya sa kasunod na story form. Ang orihinal na pagkabigo ng modelo ay pinananatili para sa diagnostics ngunit hindi ipinapakita sa user.

## Mga Susunod na Hakbang

Para sa karagdagang mga halimbawa, tingnan ang [Chapter 04: Practical samples](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang maling pagkakaintindi o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->