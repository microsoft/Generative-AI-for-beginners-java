# 寵物故事生成器初學者教學

上傳寵物照片，使用 GPT-5.6 Luna 進行分析，並根據結果描述生成故事。兩個模型請求均使用 `reasoning_effort: none`。

| 組件 | 版本 |
| --- | --- |
| Java | 21 或更高版本 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## 目錄

- [前置條件](#前置條件)
- [理解專案結構](#理解專案結構)
- [核心組件說明](#核心組件說明)
  - [1. 主要應用程式](#1-主要應用程式)
  - [2. Web 控制器](#2-web-控制器)
  - [3. 故事服務](#3-故事服務)
  - [4. Web 範本](#4-web-範本)
  - [5. 配置](#5-配置)
- [執行應用程式](#執行應用程式)
- [離線測試](#離線測試)
- [整體運作流程](#整體運作流程)
- [理解 AI 整合](#理解-ai-整合)
- [後續步驟](#後續步驟)

## 前置條件

開始之前，請確保您已具備：
- 已安裝 Java 21 或更高版本
- Maven 用於依賴管理
- 一個名為 `gpt-5.6-luna` 的 Azure AI Foundry GPT-5.6 Luna 部署，或指向該部署的 `AZURE_OPENAI_DEPLOYMENT` 覆蓋設定。請參閱 [第二章](../../02-SetupDevEnvironment/getting-started-azure-openai.md) 以瞭解佈署與使用 `az login` 進行無密鑰認證。該部署必須支援影像輸入和 `reasoning_effort: none`。
- 基本理解 Java、Spring Boot 以及網頁開發

## 理解專案結構

寵物故事專案含有數個重要檔案：

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

## 核心組件說明

### 1. 主要應用程式

**檔案：** `PetStoryApplication.java`

這是我們 Spring Boot 應用程式的入口點：

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**功能說明：**
- `@SpringBootApplication` 註解啟用自動設定與元件掃描
- 啟動內嵌網頁伺服器 (Tomcat)，監聽 8080 埠口
- 自動建立所有必要的 Spring bean 和服務

### 2. Web 控制器

**檔案：** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| 接口路徑 | 請求方式 | 成功回應 |
| --- | --- | --- |
| `GET /` | 無主體 | 含 CSRF 令牌的 HTML 上傳表單 |
| `POST /analyze-image` | `multipart/form-data`，檔案欄位 `image` | JSON：`{"description":"一隻活潑的寵物… "}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`，欄位 `description` | 含描述和生成故事的 HTML 結果頁面 |

兩個 POST 端點都需要從 `GET /` 取得的 session cookie 與 CSRF 令牌。上傳腳本將隱藏的 `_csrf` 值放在 `X-CSRF-TOKEN` 標頭；故事提交則作為 `_csrf` 表單欄位。API 客戶端必須在請求間保留 cookie。這些是表單端點，不是 JSON 請求端點。

描述必須非空且不超過 1000 字元。控制器會修剪描述並在傳給服務前移除 `<`, `>`, 雙引號, 單引號和 `&`。結果範本也會使用 `th:text` 對模型輸出進行轉義。

影像驗證失敗會回傳 HTTP 400 並帶 `error` 欄位；模型失敗則回傳 HTTP 502 帶 `error` 欄位且無 `description`。描述不合格或模型失敗會重定向到 `/` 並顯示錯誤。缺少必要欄位會回 HTTP 400，缺失或錯誤 CSRF 令牌會回 HTTP 403。不會以預設描述或故事當作成功的 AI 結果呈現。

### 3. 故事服務

**檔案：** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

使用官方 OpenAI Java SDK 4.63.1 呼叫 Azure AI Foundry 的 OpenAI 兼容聊天補全 API。Azure Identity 1.18.6 透過 `DefaultAzureCredential` 提供 Microsoft Entra 的 Bearer 令牌，無需 API 金鑰。

| 操作 | 輸入 | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | 以上傳 MIME 類型編碼成 base64 的圖片位元組資料 URL | 300 |
| `generateStory` | 使用者訊息中帶有寵物描述 | 800 |

兩個請求都使用設定的部署，預設為 `gpt-5.6-luna`，且明確設定 `ReasoningEffort.NONE` (`reasoning_effort: none`)。兩者都不設定 `temperature` 或舊參數 `max_tokens`。

影像分析支援 JPEG、PNG、GIF 和 WebP，拒絕空白圖片與超過 10MB 的檔案，並將結果描述限制在 1000 字元。故事提示請求家庭友善的短篇故事。空白的選項或空內容均視為錯誤，且失敗會保留原始原因以利伺服器端診斷。應用關閉時會關閉 SDK 用戶端。

### 4. Web 範本

**檔案：** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html)（上傳表單）

頁面以照片選擇器開始，而非描述輸入區。<strong>分析圖片</strong> 會預覽選取的照片並提交至 `/analyze-image`。成功回應會顯示描述，填入隱藏的 `description` 欄位，並顯示 <strong>生成故事</strong> 按鈕。按鈕會提交已填寫的表單至 `/generate-story`。

無瀏覽器模型下載或 CDN 依賴。影像分析由伺服器透過設定的 Azure 部署執行。失敗會顯示錯誤，且不會使用虛構描述啟用故事生成。選擇不同檔案時會清除先前分析結果。

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

**範本功能：**

1. **Thymeleaf 整合**：使用 `th:` 屬性動態產生內容
2. <strong>響應式設計</strong>：CSS 支援行動裝置與桌面
3. <strong>錯誤處理</strong>：向使用者顯示驗證錯誤
4. <strong>上傳處理</strong>：JavaScript 預覽照片，傳送 CSRF 保護的 multipart 請求，並顯示回傳描述

### 5. 配置

**檔案：** `application.properties`

應用程式的設定項目：

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

1. <strong>檔案上傳</strong>：限制檔案與 multipart 請求最大 10MB；照片宜低於此限制以留空間給 multipart 標頭
2. <strong>日誌記錄</strong>：控制執行期間記錄的資訊
3. **Azure AI Foundry**：指定使用的端點和模型部署（無密鑰認證）
4. <strong>安全性</strong>：啟用 CSRF 保護；模型診斷記錄於伺服器，控制器顯示通用模型錯誤訊息

## 執行應用程式

### 步驟 1：登入並設定端點

認證採無密鑰（Microsoft Entra ID），因此無 API 金鑰。請登入並設定您的 Foundry 端點：

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

**為何需要這個：**
- Azure AI Foundry 使用 Microsoft Entra ID 驗證推論請求
- 無密鑰認證意味著程式碼和環境中無需秘密資訊
- 您的帳戶需在該資源擁有 **認知服務 OpenAI 使用者** 角色

預設部署名稱為 `gpt-5.6-luna`。若您的 GPT-5.6 Luna 部署名稱不同，請在同一終端機設定 `AZURE_OPENAI_DEPLOYMENT` 環境變數後再啟動應用，影像分析與故事生成均使用此設定。

### 步驟 2：建置並執行

進入專案目錄：
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

應用程式將在 `http://localhost:8080` 啟動。

或者在其他空閒埠口啟動打包的 JAR，例如：

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

使用該命令時，請開啟 `http://localhost:8083/`。同樣的 `/analyze-image` 和 `/generate-story` 路由在此埠口有效。

### 步驟 3：測試應用程式

1. <strong>開啟</strong> 瀏覽器並訪問 `http://localhost:8080`
2. <strong>選擇</strong> 一張清晰的寵物照片，格式為 JPEG、PNG、GIF 或 WebP，檔案需小於 10MB
3. <strong>點擊</strong>「分析圖片」，等待寵物描述回應
4. <strong>成功分析後點擊</strong>「生成故事」
5. <strong>觀看</strong> 故事並利用結果頁面連結返回上傳表單

成功的「照片轉故事」流程會調用兩次模型，一次對應每個按鈕。實時推論將消耗您部署的配額，可能會產生費用；共用有速率限制的部署時請逐次執行煙霧測試。載入首頁不會調用模型。

## 離線測試

從 sample 目錄執行：

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) 使用迴圈 HTTP 案例捕捉真實的 OpenAI SDK 請求，檢查兩種請求的部署、`reasoning_effort: none`、令牌限制、圖像載荷、輸入驗證、空回應和上游錯誤。

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) 利用 MockMvc 搭配模擬的模型服務測試 Thymeleaf 頁面渲染、上傳合約、CSRF、驗證、輸出轉義及錯誤顯示。此測試不需 Azure 憑證，且不會調用付費 Azure 推論。Maven 將 Surefire 報告寫入 `target/surefire-reports`。

## 整體運作流程

生成寵物故事的完整流程如下：

1. <strong>選擇照片</strong>：在上傳表單中選擇寵物圖片
2. <strong>上傳圖片</strong>：點擊「分析圖片」以帶 CSRF 標頭的 multipart POST 發送至 `/analyze-image`
3. <strong>影像分析</strong>：`StoryService` 將圖片送往 GPT-5.6 Luna，推理設定為 `none`
4. <strong>顯示描述</strong>：瀏覽器顯示回傳描述並存入表單
5. <strong>提交故事</strong>：按「生成故事」表單提交 `description` 與 `_csrf` 送到 `/generate-story`
6. <strong>故事生成</strong>：控制器驗證描述，並用相同部署與 `reasoning_effort:none` 呼叫模型
7. <strong>範本渲染</strong>：Thymeleaf 轉義並在結果頁面顯示描述與故事

**錯誤處理流程：**
模型若失敗，伺服器會記錄原因。影像分析回傳 HTTP 502，瀏覽器呈現錯誤且不顯示「生成故事」按鈕。故事生成錯誤則重定向回表單並提示訊息。無任一路徑會悄悄替換成預製結果。

## 理解 AI 整合

### Azure AI Foundry（無密鑰）
服務使用您資源的 `/openai/v1/` 端點配置 SDK。`DefaultAzureCredential` 和 `AuthenticationUtil.getBearerTokenSupplier` 提供 Microsoft Entra 令牌給 `https://ai.azure.com/.default`。本地開發可使用 Azure CLI 登入；Azure 托管應用能用具備必要資源權限的託管身分。

### 提示設計
影像分析請求觀察可見寵物特徵短文，並告知模型將圖片中訊息視為資料非指令。故事生成則用回傳描述進行另一個家庭友善的寫作請求。兩者均不啟用推理或設定溫度覆寫。

### 回應處理
共用回應處理器拒絕缺少選項及空白或僅含空白內容，修剪有效內容，並保存上游失敗。影像描述限制為 1000 字元以符合後續故事表單。原始模型失敗保留用於診斷，但不對使用者顯示。

## 後續步驟

更多範例請參見 [第四章：實用範例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議進行專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->