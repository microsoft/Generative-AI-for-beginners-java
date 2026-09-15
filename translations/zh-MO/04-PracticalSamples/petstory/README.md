# 新手寵物故事產生器教學

上載寵物照片，使用 GPT-5.6 Luna 進行分析，並根據結果描述生成故事。兩個模型請求均使用 `reasoning_effort: none`。

| 元件 | 版本 |
| --- | --- |
| Java | 21 或以上 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## 目錄

- [先決條件](#先決條件)
- [了解專案結構](#了解專案結構)
- [核心元件說明](#核心元件說明)
  - [1. 主應用程式](#1-主應用程式)
  - [2. 網頁控制器](#2-網頁控制器)
  - [3. 故事服務](#3-故事服務)
  - [4. 網頁範本](#4-網頁範本)
  - [5. 設定檔](#5-設定檔)
- [執行應用程式](#執行應用程式)
- [離線測試](#離線測試)
- [整體運作流程](#整體運作流程)
- [了解 AI 整合](#了解-ai-整合)
- [後續步驟](#後續步驟)

## 先決條件

開始前，請確保您已具備：
- 已安裝 Java 21 或以上版本
- Maven 依賴管理工具
- 一個名為 `gpt-5.6-luna` 的 Azure AI Foundry 部署的 GPT-5.6 Luna，或設有指向該部署的 `AZURE_OPENAI_DEPLOYMENT` 覆寫。詳情請參照[第二章](../../02-SetupDevEnvironment/getting-started-azure-openai.md)進行部署並以 `az login` 登入以使用無密鑰認證。部署必須支持圖像輸入與 `reasoning_effort: none`。
- 具備 Java、Spring Boot 及網頁開發基礎知識

## 了解專案結構

寵物故事專案包含幾個重要檔案：

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

## 核心元件說明

### 1. 主應用程式

**檔案：** `PetStoryApplication.java`

這是我們的 Spring Boot 應用程式入口：

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**功能說明：**
- `@SpringBootApplication` 註解啟用自動配置及元件掃描
- 在 8080 埠口啟動嵌入式網頁伺服器（Tomcat）
- 自動建立所有必要的 Spring bean 與服務

### 2. 網頁控制器

**檔案：** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| 端點 | 請求 | 成功回應 |
| --- | --- | --- |
| `GET /` | 無內容 | 帶 CSRF 令牌的 HTML 上傳表單 |
| `POST /analyze-image` | `multipart/form-data`，檔案欄位 `image` | JSON：`{"description":"一隻活潑的寵物..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`，欄位 `description` | 含描述及產生故事的 HTML 結果頁面 |

兩個 POST 端點都需帶有從 `GET /` 獲取的會話 Cookie 和 CSRF 令牌。上傳腳本會在 `X-CSRF-TOKEN` 標頭中送出隱藏的 `_csrf` 值；故事提交則以 `_csrf` 表單欄位附送。API 用戶端必須在請求間保留 Cookie。這些是表單端點，不是 JSON 請求端點。

描述必須為非空且不超過1000字元。控制器會修剪描述並去除 `<`、`>`、雙引號、單引號及 `&`，然後傳給服務。結果範本也會用 `th:text` 轉義模型輸出。

圖像驗證失敗返回 HTTP 400 並帶有 `error` 欄位；模型失敗則返回 HTTP 502 帶有 `error` 欄位且無 `description`。無效故事描述或模型失敗會重導至 `/` 並顯示錯誤。缺少必填欄位回 HTTP 400，缺失或無效 CSRF 令牌回 HTTP 403。不會呈現以成功 AI 結果之名出的替代描述或故事。

### 3. 故事服務

**檔案：** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

官方 OpenAI Java SDK 4.63.1 調用 Azure AI Foundry 的兼容 OpenAI 聊天補全 API。Azure Identity 1.18.6 透過 `DefaultAzureCredential` 提供 Microsoft Entra bearer 令牌；不需 API 金鑰。

| 操作 | 輸入 | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | 以上傳的 MIME 類型編碼為 base64 資料 URL 的影像位元組 | 300 |
| `generateStory` | 以使用者訊息形式的寵物描述 | 800 |

兩個請求均使用設定的部署，預設為 `gpt-5.6-luna`，並明確設定 `ReasoningEffort.NONE`（`reasoning_effort: none`）。都不送出 `temperature` 或舊版的 `max_tokens` 參數。

影像分析接受 JPEG、PNG、GIF 與 WebP 格式，拒絕空影像與超過10MB的檔案，且限制生成之描述不超過1000字元。故事提示要求家庭友善短篇故事。空選擇或空白模型內容視為錯誤，失敗原因保存供伺服器端診斷。應用關閉時關閉 SDK 客戶端。

### 4. 網頁範本

**檔案：** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html)（上傳表單）

頁面以照片選擇器起始，而非描述文字區。<strong>分析影像</strong> 預覽所選照片並送到 `/analyze-image`。成功回應顯示描述、填入隱藏的 `description` 欄位，並顯示 <strong>產生故事</strong> 按鈕。該按鈕送出現有表單至 `/generate-story`。

無須瀏覽器模型下載或 CDN 依賴。影像分析在伺服器端通過設定的 Azure 部署執行。失敗訊息仍會顯示，且不會因偽造描述而啟用故事產生。選擇不同檔案會清除先前分析結果。

**檔案：** `result.html`（故事展示）

顯示生成的故事：

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

**範本特色：**

1. **Thymeleaf 整合**：使用 `th:` 屬性實現動態內容
2. <strong>響應式設計</strong>：CSS 支援手機及桌面顯示
3. <strong>錯誤處理</strong>：向使用者顯示驗證錯誤
4. <strong>上傳處理</strong>：JavaScript 預覽照片，發送受 CSRF 保護的 multipart 請求，並顯示回傳描述

### 5. 設定檔

**檔案：** `application.properties`

應用程式的設定：

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

**設定說明：**

1. <strong>檔案上傳</strong>：檔案及整個 multipart 請求大小限制為 10MB；照片請在此限制內以保留 multipart 標頭空間
2. <strong>日誌</strong>：控制執行時紀錄內容
3. **Azure AI Foundry**：指定使用的端點與模型部署（無密鑰認證）
4. <strong>安全性</strong>：持續啟用 CSRF 保護；模型診斷記錄於伺服器，控制器顯示通用模型失敗訊息

## 執行應用程式

### 步驟 1：登入並設定端點

認證為無密鑰（Microsoft Entra ID），因此無 API 金鑰。登入並設定 Foundry 端點：

**Windows（命令提示字元）：**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows（PowerShell）：**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS：**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**為何需要這個步驟：**
- Azure AI Foundry 使用 Microsoft Entra ID 認證推理請求
- 無密鑰認證意味您的原始碼或環境中無需存放密鑰
- 您的帳戶需在資源上擁有 **Cognitive Services OpenAI User** 角色

預設部署名稱為 `gpt-5.6-luna`。若您的 GPT-5.6 Luna 部署名稱不同，在啟動應用程式前於同一終端機設定 `AZURE_OPENAI_DEPLOYMENT`。影像分析與故事產生均使用此設定。

### 步驟 2：建置並執行

切換到專案目錄：
```bash
cd 04-PracticalSamples/petstory
```

建置獨立可執行 JAR 並執行所有離線測試：
```bash
mvn clean package
```

啟動伺服器：
```bash
mvn spring-boot:run
```

應用程式將於 `http://localhost:8080` 啟動。

或者，在一個空閒埠口啟動該打包 JAR，例如：

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

使用該命令後，請開啟 `http://localhost:8083/`。相同的 `/analyze-image` 與 `/generate-story` 路由在所選埠口可用。

### 步驟 3：測試應用程式

1. <strong>於瀏覽器開啟</strong> `http://localhost:8080`
2. <strong>選擇</strong> 一張清晰的寵物照片，格式為 JPEG、PNG、GIF 或 WebP，大小低於 10MB
3. <strong>按下</strong>「分析影像」並等待寵物描述
4. <strong>成功分析後按下</strong>「產生故事」
5. <strong>閱讀</strong> 故事並使用結果頁面的連結返回上傳表單

成功的照片到故事流程會調用兩次模型，各自對應一個按鈕。線上推理會消耗部署配額並可能產生費用；共用有限速部署時請串行運行煙霧測試。載入首頁不會呼叫模型。

## 離線測試

從 sample 目錄執行：

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) 使用本地 HTTP 模擬捕捉真實 OpenAI SDK 請求。它檢查兩個請求的部署、`reasoning_effort: none`、令牌限制、影像負載、輸入驗證、空回應及上游錯誤。

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) 利用 MockMvc 搭配模型服務模擬器測試 Thymeleaf 頁面渲染、上傳契約、CSRF、驗證、輸出轉義及可見錯誤。這些測試無需 Azure 憑證，且不會呼叫付費 Azure 推理。Maven 會將 Surefire 報告寫入 `target/surefire-reports`。

## 整體運作流程

產生寵物故事的完整流程如下：

1. <strong>照片選擇</strong>：於上傳表單中選擇寵物圖片
2. <strong>影像上傳</strong>：「分析影像」以 multipart POST 並附 CSRF 標頭送至 `/analyze-image`
3. <strong>影像分析</strong>：`StoryService` 以 `reasoning_effort: none` 將影像發送給 GPT-5.6 Luna
4. <strong>描述顯示</strong>：瀏覽器顯示返回的描述並存入表單
5. <strong>故事提交</strong>：「產生故事」將 `description` 與 `_csrf` 發送至 `/generate-story`
6. <strong>故事生成</strong>：控制器驗證描述並以相同部署、`reasoning_effort: none` 呼叫模型
7. <strong>範本渲染</strong>：Thymeleaf 轉義並顯示描述及故事於結果頁

**錯誤處理流程：**
若模型失敗，伺服器記錄原因。影像分析回 HTTP 502，瀏覽器顯示錯誤且不顯示「產生故事」按鈕。故事生成會導向表單並顯示錯誤訊息。兩路徑均不會暗中替換成預先寫好的結果。

## 了解 AI 整合

### Azure AI Foundry（無密鑰）
此服務將 SDK 設定為您的資源 `/openai/v1/` 端點。`DefaultAzureCredential` 與 `AuthenticationUtil.getBearerTokenSupplier` 提供 Microsoft Entra 令牌授權 `https://ai.azure.com/.default`。本地開發可透過 Azure CLI 登入；Azure 托管應用可用具備相應資源權限的管理身分。

### 提示工程
影像分析請求以短段落描述可觀察的寵物特徵，且指示模型將圖中文字視為資料非指令。故事生成則在獨立的親子友善寫作請求中用回傳的描述。兩個呼叫均未啟用推理或設定溫度覆寫。

### 回應處理
共享的回應處理器會拒絕缺少 choices 及空白或僅空格的內容，修剪有效內容並保留上游失敗狀況。影像描述限制在 1000 字元以適應後續故事表單。原始模型失敗資訊保留作診斷，不會呈現給使用者。

## 後續步驟

欲了解更多範例，請見[第四章：實作範例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->