# 寵物故事生成器初學者教學

上傳寵物照片，使用 GPT-5.6 Luna 進行分析，並根據生成的描述產生故事。兩個模型請求均使用 `reasoning_effort: none`。

| 組件 | 版本 |
| --- | --- |
| Java | 21 或更高 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## 目錄

- [前置需求](#前置需求)
- [理解專案結構](#理解專案結構)
- [核心組件解說](#核心組件解說)
  - [1. 主應用程式](#1-主應用程式)
  - [2. 網頁控制器](#2-網頁控制器)
  - [3. 故事服務](#3-故事服務)
  - [4. 網頁模板](#4-網頁模板)
  - [5. 配置](#5-配置)
- [啟動應用程式](#啟動應用程式)
- [離線測試](#離線測試)
- [整體運作原理](#整體運作原理)
- [理解AI整合](#理解-ai-整合)
- [下一步](#下一步)

## 前置需求

開始前，請確認您已具備：
- 已安裝 Java 21 或更高版本
- 使用 Maven 作為依賴管理工具
- 擁有 Azure AI Foundry 部署的 GPT-5.6 Luna，部署名稱為 `gpt-5.6-luna`，或有一個指向該部署的 `AZURE_OPENAI_DEPLOYMENT` 覆寫設定。請參閱 [第二章](../../02-SetupDevEnvironment/getting-started-azure-openai.md) 了解部署配置並使用 `az login` 進行無金鑰認證。該部署必須支持圖片輸入且 `reasoning_effort: none`。
- 基本理解 Java、Spring Boot 和網頁開發

## 理解專案結構

寵物故事專案包含數個重要檔案：

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

## 核心組件解說

### 1. 主應用程式

**檔案：** `PetStoryApplication.java`

這是我們 Spring Boot 應用程式的入口：

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**此程式碼功能：**
- `@SpringBootApplication` 註解啟用自動配置與組件掃描
- 啟動嵌入式網頁伺服器（Tomcat）於 8080 埠口
- 自動建立所有必要的 Spring bean 和服務

### 2. 網頁控制器

**檔案：** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| 路由端點 | 請求 | 成功回應 |
| --- | --- | --- |
| `GET /` | 無請求體 | 含 CSRF 令牌的 HTML 上傳表單 |
| `POST /analyze-image` | `multipart/form-data`，檔案欄位為 `image` | JSON: `{"description":"一隻愛玩的寵物..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`，欄位為 `description` | 回傳包含描述和生成故事的 HTML 結果頁 |

兩個 POST 路由皆需會話 cookie 和從 `GET /` 獲取的 CSRF 令牌。上傳腳本將隱藏欄位 `_csrf` 值放入 `X-CSRF-TOKEN` 標頭；故事提交以 `_csrf` 表單欄位送出。API 用戶端必須在多次請求間保留 cookie。這些端點為表單提交，非 JSON 請求端點。

描述文字必須非空且不超過1000字元。控制器會修剪描述內容，並剝除 `<`，`>`，雙引號，撇號及 `&`，然後才將其傳給服務。結果模板亦使用 `th:text` 轉譯模型輸出。

圖片驗證失敗將回傳 HTTP 400 並帶有 `error` 欄位；模型呼叫失敗則回 HTTP 502，帶有 `error` 欄位且無 `description`。故事描述無效或模型失敗時會重定向回 `/` 顯示錯誤訊息。缺少必須欄位時回 HTTP 400，缺失或驗證失敗的 CSRF 令牌則回 HTTP 403。不會以備用描述或故事假裝成功結果。

### 3. 故事服務

**檔案：** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

官方 OpenAI Java SDK 4.63.1 調用 Azure AI Foundry 支援的 OpenAI 兼容聊天完成 API。Azure Identity 1.18.6 透過 `DefaultAzureCredential` 提供 Microsoft Entra 存取令牌，無需 API 金鑰。

| 操作 | 輸入 | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | 已編碼為 base64 data URL 且帶上上傳的 MIME 類型的圖片位元組 | 300 |
| `generateStory` | 使用者訊息中的寵物描述 | 800 |

兩個請求皆使用已配置的部署，預設為 `gpt-5.6-luna`，並明確設定 `ReasoningEffort.NONE`（`reasoning_effort: none`）。請求未傳送 `temperature` 或過時的 `max_tokens` 參數。

圖片分析支援 JPEG、PNG、GIF 及 WebP，拒絕空檔與超過 10MB 的檔案，結果描述限制為 1000 字元。故事提示要求產生適合家庭的短篇故事。空白選項或空白模型內容視為錯誤，失敗結果保留原始原因以供伺服器端診斷。應用程式關閉時 SDK 客戶端也會關閉。

### 4. 網頁模板

**檔案：** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html)（上傳表單）

該頁面以照片選擇器開始，非描述文字區。<strong>分析圖片</strong> 按鈕會預覽所選照片並傳送至 `/analyze-image`。成功回應後顯示描述文字，填入隱藏的 `description` 欄位，並顯示 <strong>產生故事</strong> 按鈕。該按鈕將提交表單至 `/generate-story`。

無需瀏覽器端模型下載或 CDN 依賴。圖片分析於伺服器端經由設定的 Azure 部署執行。失敗會持續顯示錯誤且不允許以虛構描述啟用故事生成。重新選擇不同檔案會清除先前分析結果。

**檔案：** `result.html`（故事顯示）

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

**模板功能：**

1. **Thymeleaf 整合**：使用 `th:` 屬性動態載入內容
2. <strong>響應式設計</strong>：CSS 支援行動及桌面樣式
3. <strong>錯誤處理</strong>：向使用者顯示驗證錯誤
4. <strong>上傳處理</strong>：JavaScript 預覽照片，傳送附 CSRF 保護的 multipart 請求，顯示回傳描述

### 5. 配置

**檔案：** `application.properties`

應用程式的配置設定：

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

**配置說明：**

1. <strong>檔案上傳</strong>：檔案與 multipart 整體請求皆限制為 10MB 以下；儘量保持照片大小限制以留空間給 multipart 標頭
2. <strong>日誌</strong>：控制執行時輸出哪些資訊
3. **Azure AI Foundry**：指定端點與模型部署（無金鑰認證）
4. <strong>安全性</strong>：保持 CSRF 保護啟用；模型診斷記錄於伺服器，控制器顯示通用的模型失敗訊息

## 啟動應用程式

### 步驟 1：登入並設定端點

認證採用無金鑰方式（Microsoft Entra ID），不需 API 金鑰。登入並設定 Foundry 端點：

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

**為什麼需要這個步驟：**
- Azure AI Foundry 使用 Microsoft Entra ID 驗證推論請求
- 無金鑰認證意味著程式碼或環境中無秘密資訊
- 您的帳號需有資源上的 **Cognitive Services OpenAI User** 角色

預設部署名稱為 `gpt-5.6-luna`。若您的 GPT-5.6 Luna 部署名稱不同，請在相同終端機中啟動應用程式前設定 `AZURE_OPENAI_DEPLOYMENT`。圖片分析與故事生成皆會使用此設定。

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

另可選擇於其他空閒埠口執行封裝的 JAR，例如：

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

針對該指令，請開啟 `http://localhost:8083/`。同樣有 `/analyze-image` 與 `/generate-story` 路由。

### 步驟 3：測試應用程式

1. <strong>開啟</strong> 您的瀏覽器並造訪 `http://localhost:8080`
2. <strong>選擇</strong> 一張清晰的 JPEG、PNG、GIF 或 WebP 格式寵物照片，大小低於 10MB
3. <strong>點擊</strong>「分析圖片」，等待寵物描述生成
4. <strong>點擊</strong> 成功分析後出現的「產生故事」
5. <strong>查看</strong> 生成的故事，並使用結果頁面的連結返回上傳表單

成功的照片至故事流程會呼叫兩次模型，對應兩個按鈕。即時推論會消耗您部署的額度並可能產生費用；在共享限速部署時，請依序運行測試。載入首頁不會呼叫模型。

## 離線測試

在 sample 目錄下執行：

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) 使用回圈 HTTP 模擬針對 OpenAI SDK 請求進行封包擷取。它檢查兩個請求的部署、`reasoning_effort: none`、令牌限制、圖片負載、輸入驗證、空白回應及上游錯誤。

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) 利用 MockMvc 與模擬模型服務，測試 Thymeleaf 渲染頁面、上傳協定、CSRF、防衛驗證、輸出轉譯與可見錯誤。這些測試不需 Azure 憑證，且不會呼叫付費的 Azure 推論服務。Maven 將 Surefire 報告寫入 `target/surefire-reports`。

## 整體運作原理

以下為產生寵物故事時的完整流程：

1. <strong>照片選擇</strong>：您在上傳表單中選擇寵物照片
2. <strong>圖片上傳</strong>：「分析圖片」送出附 CSRF 標頭的 multipart POST 請求至 `/analyze-image`
3. <strong>圖片分析</strong>：`StoryService` 將圖片送至 GPT-5.6 Luna，推理模式為 `none`
4. <strong>描述顯示</strong>：瀏覽器顯示回傳描述並存入表單中
5. <strong>故事提交</strong>：「產生故事」送出 `description` 和 `_csrf` 至 `/generate-story`
6. <strong>故事生成</strong>：控制器驗證描述，並呼叫同一部署，推理設定為 `none`
7. <strong>模板渲染</strong>：Thymeleaf 轉譯並在結果頁顯示描述和故事

**錯誤處理流程：**
若模型失敗，伺服器會記錄原因。圖片分析失敗回 HTTP 502，瀏覽器顯示錯誤且不顯示「產生故事」按鈕。故事生成失敗時重定向至表單並顯示錯誤訊息。兩者皆不會暗中替換為預設結果。

## 理解 AI 整合

### Azure AI Foundry（無金鑰認證）
服務透過 SDK 以資源的 `/openai/v1/` 端點設定。`DefaultAzureCredential` 與 `AuthenticationUtil.getBearerTokenSupplier` 提供 Microsoft Entra 存取權杖，範圍是 `https://ai.azure.com/.default`。本地開發可使用 Azure CLI 登入，Azure 托管應用可用具有資源權限的管理身份識別。

### 提示工程
圖片分析請求模型以短段落描述可觀察的寵物特徵，指示模型將圖片文字視為資料而非指令。故事生成則使用回傳描述，以另行獨立且適合家庭的風格編寫。皆未啟用推理或設定溫度調整。

### 回應處理
共享回應處理器會拒絕缺少選項或空白/僅空白內容，修剪有效內容並保留上游失敗。圖片描述限制為 1000 字元以適合後續故事表單。原始模型失敗記錄保留於診斷，未呈現給使用者。

## 下一步

更多範例請見 [第四章：實務範例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->