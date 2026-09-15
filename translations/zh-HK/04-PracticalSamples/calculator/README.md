# MCP 計算機初學者教學

## 目錄

- [您將學到什麼](#您將學到什麼)
- [前置條件](#前置條件)
- [依賴版本](#依賴版本)
- [了解專案結構](#了解專案結構)
- [核心組件說明](#核心組件說明)
  - [1. 主應用程式](#1-主應用程式)
  - [2. 計算機服務](#2-計算機服務)
  - [3. 直接 MCP 用戶端](#3-直接-mcp-用戶端)
  - [4. AI 驅動用戶端](#4-ai-驅動用戶端)
- [執行範例](#執行範例)
- [離線測試](#離線測試)
- [整合運作原理](#整合運作原理)
- [下一步](#下一步)

## 您將學到什麼

本教學將解釋如何使用模型上下文協定（MCP）建立一個計算機服務。您將了解：

- 如何建立 AI 可用作工具的服務
- 如何設定與 MCP 服務的直接通訊
- AI 模型如何自動選擇使用哪些工具
- 直接協定呼叫與 AI 協助互動的差異

## 前置條件

開始之前，請確保您有：
- 已安裝 Java 21 或更高版本
- 使用 Maven 進行依賴管理
- 具備基本的 Java 和 Spring Boot 知識

只有 AI 用戶端需要 Azure OpenAI 部署與已驗證的 `DefaultAzureCredential`，
例如本機已有 Azure CLI 登入或在 Azure 中有管理身分識別。該身分需在資源上有
認知服務 OpenAI 使用者角色。詳見[第 2 章](../../02-SetupDevEnvironment/getting-started-azure-openai.md)。
伺服器、直接 SDK 用戶端及所有自動化測試均不需 Azure 帳戶或模型存取權。

## 依賴版本

於 2026-09-14 驗證發行版依賴：

| 依賴 | 版本 |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK（Spring AI 管理） | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j 官方 OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter（Boot 管理） | 6.0.3 |

MCP 和官方 OpenAI adapters 均為發佈中的 Maven Central beta 版本，非快照。
它們的版本與 LangChain4j core 不同。無須使用快照或里程碑倉庫。
只有用戶端依賴為測試範圍，因為可執行範例位於 `src/test/java` 下。

## 了解專案結構

計算機專案包含數個重要檔案：

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## 核心組件說明

### 1. 主應用程式

**檔案：** `McpServerApplication.java`

這是我們計算機服務的入口。它是一個標準的 Spring Boot 應用程式，帶有一個特殊新增：

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**此段功能：**
- 啟動一個埠號為 8080 的 Spring Boot 網頁伺服器
- 建立一個 `ToolCallbackProvider`，使計算機方法作為 MCP 工具可用
- `@Bean` 註解告訴 Spring 以元件方式管理，可被其他部分使用

### 2. 計算機服務

**檔案：** `CalculatorService.java`

這裡進行所有數學運算。每個方法標註 `@Tool`，使其能被 MCP 呼叫：

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // 更多計算機操作...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**主要特點：**

1. **`@Tool` 註解**: 告訴 MCP 此方法可被外部用戶端呼叫
2. <strong>明確說明</strong>: 每個工具都有描述，幫助 AI 模型判斷何時使用
3. <strong>一致回傳格式</strong>: 所有操作均回傳類似 "5.00 + 3.00 = 8.00" 的易讀字串
4. <strong>錯誤處理</strong>: 除以零與負平方根會回傳錯誤訊息

**可用運算：**
- `add(a, b)` - 加法
- `subtract(a, b)` - 減法（第二參數自第一參數減去）
- `multiply(a, b)` - 乘法
- `divide(a, b)` - 除法（含零檢查）
- `power(base, exponent)` - 計算 base 的 exponent 次方
- `squareRoot(number)` - 計算平方根（含負數檢查）
- `modulus(a, b)` - 取餘數
- `absolute(number)` - 取絕對值
- `help()` - 返回所有運算資訊

### 3. 直接 MCP 用戶端

請參閱 [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)。

此用戶端使用 `HttpClientStreamableHttpTransport` 連接 `/mcp`，初始化連線，
ping 伺服器，並遵循工具列表分頁。確認所有九個預期工具存在，
並呼叫每個工具包括 `modulus` 和 `help`，無需 AI 模型。

目前的請求建構器如下：

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

協定錯誤會使用戶端失敗，而非印出誤導的成功訊息。MCP 用戶端
以 try-with-resources 使用並關閉，即使發現失敗或工具呼叫失敗時亦然。

### 4. AI 驅動用戶端

請參閱 [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
及 [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)。

`OpenAiOfficialChatModel` 實作目前 LangChain4j 的 `ChatModel` API。
`StreamableHttpMcpTransport` 連接至與 SDK 用戶端相同的 `/mcp` 端點。
`AiServices` 負責發現工具並管理工具呼叫與結果的對話。

預設部署為 **GPT-5.6 Luna**，並明確禁止推理：

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

這些預設適用於所有完成操作，包括工具執行後的後續。
用戶端使用由 `DefaultAzureCredential` 支援、可刷新之 `BearerTokenCredential`，
和 `https://ai.azure.com/.default` 範圍，並非一次性 API 金鑰傳入的令牌。
支援資源 URL 與已以 `/openai/v1` 結尾的 URL。

Bot 保持有限的對話歷史，印出 `Tool executed: ...` 擺放實際 MCP 結果，
並於回應跳過工具時失敗。工具迴圈限制為四次往返。
驗證、模型、MCP 和工具錯誤均會向上傳播；自動重試模型已停用。
MCP 傳輸/用戶端與官方 OpenAI 用戶端於成功或失敗時皆會關閉。

## 執行範例

### 步驟 1：啟動計算機伺服器

伺服器不需 Azure 設定。以下指令於此範例目錄執行。
範例使用埠號 **18081** 避免與其他範例衝突；預設仍為 8080。

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP 端點為 `http://localhost:18081/mcp`。健康檢查與服務資訊位於
`http://localhost:18081/health` 和 `http://localhost:18081/info`。
Streamable HTTP 取代舊有 SSE 專用傳輸，`/sse` 與 `/v1/tools` 不再是端點。

### 步驟 2：用直接用戶端測試

於另一 PowerShell 終端機：

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

無需輸入。所有九個工具均被測試。預期運算結果包括
8、6、42、5、256、4、2 和 5.5，接著印出幫助文字。

### 步驟 3：用 AI 用戶端測試

按前置條件說明認證後，在同一終端機設定 AI 用戶端：

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

預期收到 `Tool executed: add` 並帶有 `41.80`，接著為模型回答。
單提示模式執行後不等待輸入即結束。若要執行原本四提示示範：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

示範呼叫 `add`、`squareRoot`、`help`，與鏈式的 `power` 再 `divide` 運算。
預期數值答案為 41.8、12 與 64。不輸入參數亦執行此示範。

### 步驟 4：運行交互式機械人

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

輸入 `Multiply 6 by 7 using the calculator service`，然後輸入 `exit` 或 `quit`。
預期會有實際的 `multiply` 工具結果 42。空白行將被忽略；EOF 同樣結束會話。
非交互式的快速測試用此入口點：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

兩個 AI 入口點皆接受 `--prompt "question"`、`--demo` 和 `--interactive`。
無效選項會在連線前失敗。每個 Maven `-D...` 參數都要完全引號包覆，
PowerShell 使用方式與 Bash 需用 `export NAME=value` 替代 `$env:NAME = "value"`。

**配額：** 依序運行 AI 範例。簡單提示通常需要兩次模型請求；
完整示範通常需要九次，包括工具結果的後續請求。在共用 10 RPM 部署中，
請隔開配額視窗再執行下一 AI 運作。429 錯誤會顯示失敗，無自動重試；
請遵循服務的重試等待指示。實際請求次數依模型不同而異。
離線測試不消耗任何配額，亦不反映 Luna 可用性或回答品質。

### 設定與關閉

| 設定 | 預設 / 行為 |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`；基底 URL，不含 `/mcp` |
| `-Dmcp.server.url=...` | 覆蓋所有用戶端的 `MCP_SERVER_URL` |
| `AZURE_OPENAI_ENDPOINT` | 僅 AI 用戶端需；資源 URL 或 `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`；Azure 部署名稱 |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`；正整數 |
| 推理工作量 | 一律為 `none`，包含工具迴圈後續 |

覆蓋部署必須支援 `reasoning_effort=none` 和 `max_completion_tokens`。
用戶端不會自動讀取 `.env` 檔。測試後請使用 `Ctrl+C` 結束伺服器。
用戶端正常返回，不會呼叫 `System.exit` 或進行關機等待。

## 離線測試

```powershell
mvn -B -ntp clean verify
```

所有測試均為離線 Azure：協定套件啟動 Spring 伺服器及
OpenAI 相容 stub 於隨機迴路端口，隨後關閉。Maven 可能仍需
下載依賴。無使用憑證、活躍部署或事先存在的 MCP 伺服器。

- 計算機單元測試涵蓋所有運算、十進位結果、幫助資訊與錯誤情境。
- MCP 測試涵蓋初始化、發現、九工具呼叫、工具失敗，以及健康/資訊檢查。
- AI 協定測試執行完整示範及互動式 Bot，對真實計算機測試，
  驗證工具結果供下一完成，並檢視 Luna、`reasoning_effort: "none"`，
  及 `max_completion_tokens` 的每個 HTTP 主體，無舊版 `max_tokens`。
- 設定/輸入測試涵蓋部署與端點覆蓋、空白行、EOF、exit/quit、
  單提示模式、無效選項及錯誤傳播。配額測試證明 429 不會自動重試。

## 整合運作原理

當您向 AI 詢問「5 + 3 是多少？」時，完整流程如下：

1. <strong>您</strong> 用自然語言問 AI
2. **AI** 分析請求，得知您要做加法
3. **AI** 呼叫 MCP 伺服器：`add(5.0, 3.0)`
4. <strong>計算機服務</strong> 執行：`5.0 + 3.0 = 8.0`
5. <strong>計算機服務</strong> 回傳：`"5.00 + 3.00 = 8.00"`
6. **AI** 收到結果並格式化為自然回答
7. <strong>您</strong> 獲得：「5 與 3 的和是 8」

## 下一步

欲了解更多範例，請參閱 [第 04 章：實作範例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議進行專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->