# MCP 計算機新手教學

## 目錄

- [你將學到什麼](#你將學到什麼)
- [先決條件](#先決條件)
- [依賴版本](#依賴版本)
- [了解專案架構](#了解專案架構)
- [核心元件說明](#核心元件說明)
  - [1. 主應用程式](#1-主應用程式)
  - [2. 計算機服務](#2-計算機服務)
  - [3. 直接 MCP 用戶端](#3-直接-mcp-用戶端)
  - [4. AI 驅動用戶端](#4-ai-驅動用戶端)
- [執行範例](#執行範例)
- [離線測試](#離線測試)
- [整合運作原理](#整合運作原理)
- [下一步](#下一步)

## 你將學到什麼

本教學說明如何使用模型上下文協定 (MCP) 建立計算機服務。你將了解：

- 如何建立 AI 可用作工具的服務
- 如何設定與 MCP 服務的直接通訊
- AI 模型如何自動選擇使用哪些工具
- 直接協定呼叫與 AI 輔助互動的差異

## 先決條件

開始前，請確定你已安裝：
- Java 21 或以上版本
- 用於依賴管理的 Maven
- Java 和 Spring Boot 的基本知識

只有 AI 用戶端需要 Azure OpenAI 部署和經過身份驗證的 `DefaultAzureCredential`，
例如本地已登入的 Azure CLI 或 Azure 的管理身分。該身分需
具有資源上的認知服務 OpenAI 使用者角色。詳見 [第 2 章](../../02-SetupDevEnvironment/getting-started-azure-openai.md)。
伺服器、直接 SDK 用戶端和所有自動化測試均不需 Azure 帳戶或模型存取權。

## 依賴版本

於 2026-09-14 驗證的發行依賴：

| 依賴項 | 版本 |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (由 Spring AI 管理) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j 官方 OpenAI 適配器 | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (由 Boot 管理) | 6.0.3 |

MCP 和官方 OpenAI 適配器為 Maven Central 發佈的測試版本，非快照。
它們的版本與 LangChain4j core 不同，無需快照或里程碑倉庫。
只有用戶端依賴設為測試範圍，因可執行範例位於 `src/test/java`。

## 了解專案架構

計算機專案包含幾個重要檔案：

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

## 核心元件說明

### 1. 主應用程式

**檔案：** `McpServerApplication.java`

這是我們計算機服務的進入點。它是標準 Spring Boot 應用，但有一項特殊新增功能：

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

**此功能作用：**
- 啟動在 8080 埠的 Spring Boot 網頁伺服器
- 建立 `ToolCallbackProvider`，讓我們的計算方法可作為 MCP 工具被使用
- `@Bean` 標註告訴 Spring 這是可被其他部分使用的元件

### 2. 計算機服務

**檔案：** `CalculatorService.java`

這裡執行所有數學運算。每個方法都用 `@Tool` 標記，使其可透過 MCP 使用：

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

**主要特色：**

1. **`@Tool` 標註**：告訴 MCP 此方法可供外部用戶端調用
2. <strong>清楚描述</strong>：每個工具有描述，幫助 AI 理解何時使用
3. <strong>一致的返回格式</strong>：所有運算回傳人類可讀的字串，如 "5.00 + 3.00 = 8.00"
4. <strong>錯誤處理</strong>：除以零與負平方根會回傳錯誤訊息

**可用運算：**
- `add(a, b)` - 加法
- `subtract(a, b)` - 減法（第一數減第二數）
- `multiply(a, b)` - 乘法
- `divide(a, b)` - 除法（第一數除以第二數，含零檢查）
- `power(base, exponent)` - 次方
- `squareRoot(number)` - 開平方（含負數檢查）
- `modulus(a, b)` - 取餘數
- `absolute(number)` - 絕對值
- `help()` - 列出所有運算資訊

### 3. 直接 MCP 用戶端

參見 [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)。

此用戶端使用 `/mcp` 的 `HttpClientStreamableHttpTransport`，初始化連線，
偵測伺服器並遵循工具清單分頁。它檢查所有預期的九個工具
是否存在，並呼叫每個工具，包括 `modulus` 和 `help`，不需 AI 模型。

目前的請求建構模式如下：

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

協定錯誤會使用戶端失敗，而非顯示誤導成功訊息。MCP 用戶端
使用 try-with-resources 關閉，包括偵測或工具呼叫失敗時。

### 4. AI 驅動用戶端

參見 [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
與 [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)。

`OpenAiOfficialChatModel` 實作現行 LangChain4j `ChatModel` API。
`StreamableHttpMcpTransport` 將它連接至與 SDK 用戶端相同的 `/mcp` 端點。
`AiServices` 負責工具偵測與管理工具呼叫／結果對話。

預設部署為 **GPT-5.6 Luna**，且明確禁用推理：

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

這些預設適用於每次補全，包括工具執行後的追蹤。
用戶端採用可更新的 `BearerTokenCredential`，由 `DefaultAzureCredential`
與 `https://ai.azure.com/.default` 範圍支援，非一次性 API 金鑰。
支援資源 URL 和以 `/openai/v1` 結尾的 URL。

Bot 保持有限會話歷史，列印帶真實 MCP 結果的 `Tool executed: ...`，
若回應跳過工具則失敗。工具迴圈限制四輪。
認證、模型、MCP 與工具錯誤會傳播；關閉自動模型重試。
MCP 傳輸／用戶端與官方 OpenAI 用戶端在成功或失敗時均關閉。

## 執行範例

### 步驟 1：啟動計算機伺服器

伺服器不需 Azure 設定。以下指令須在此範例目錄執行。
範例用 18081 埠以避免與其他範例衝突，預設仍為 8080。

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP 端點為 `http://localhost:18081/mcp`。健康狀態與發現資訊分別在
`http://localhost:18081/health` 與 `http://localhost:18081/info`。
Streamable HTTP 替代原本僅 SSE 的傳輸；`/sse` 與 `/v1/tools` 不再是端點。

### 步驟 2：使用直接用戶端測試

於另一個 PowerShell 終端執行：

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

不需輸入參數。九個工具皆被測試。預期算術結果有
8、6、42、5、256、4、2 與 5.5，接著是說明文字。

### 步驟 3：使用 AI 用戶端測試

通過先決條件認證後，在同終端設定 AI 用戶端：

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

預期顯示含數值 `41.80` 的 `Tool executed: add` 行，隨後是模型回應。
單次提示模式執行後退出，不等輸入。若要執行原本四提示示範：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

示範呼叫 `add`、`squareRoot`、`help` 以及依序的 `power` 與 `divide`。
預期數值答案為 41.8、12 與 64。不帶參數也會執行此示範。

### 步驟 4：執行互動式 Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

輸入 `Multiply 6 by 7 using the calculator service`，再輸入 `exit` 或 `quit`。
預期返回實際的 `multiply` 工具結果 42。空行會被忽略；EOF 也結束會話。
非互動式煙霧測試此入口：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

兩個 AI 入口均支援 `--prompt "question"`、`--demo` 和 `--interactive`。
無效選項於連線前失敗。每個 Maven `-D...` 參數在 PowerShell 中須完整加引號，
Bash 中則使用 `export NAME=value` 替代 `$env:NAME = "value"`。

**配額：** 連續執行 AI 範例。簡易提示通常需兩次模型請求；
完整示範通常需九次，包括工具結果回饋。共享 10 RPM 部署時，
下一次 AI 執行前請留新配額視窗。429 失敗會明顯顯示，無自動重試；
請遵循服務的重試建議。實際請求數取決模型。
離線測試不消耗配額，不建立 Luna 可用性或回答品質。

### 設定與關閉

| 設定項 | 預設／行為 |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`；基本 URL，無 `/mcp` |
| `-Dmcp.server.url=...` | 取代所有用戶端的 `MCP_SERVER_URL` |
| `AZURE_OPENAI_ENDPOINT` | 僅 AI 用戶端需；資源 URL 或 `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`；Azure 部署名稱 |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`；正整數 |
| 推理努力 | 永遠為 `none`，包括工具迴圈追蹤 |

被覆寫部署必須支援 `reasoning_effort=none` 與 `max_completion_tokens`。
用戶端不會自動讀取 `.env` 檔。測試後使用 `Ctrl+C` 停止伺服器。
用戶端正常返回，無 `System.exit` 或關機等待。

## 離線測試

```powershell
mvn -B -ntp clean verify
```

所有測試均離線，不涉及 Azure：協定套件開啟 Spring 伺服器及
OpenAI 相容模擬器於隨機迴環位址埠，隨後關閉。Maven 可能仍需
下載依賴。無使用任何認證、實際部署或現存 MCP 伺服器。

- 計算機單元測試涵蓋所有算術運算、十進位結果、說明及領域錯誤。
- MCP 測試涵蓋初始化、發現、九個工具呼叫、工具失敗及健康／資訊。
- AI 協定測試執行完整示範與互動式 Bot 對實際計算機，
  驗證工具結果餵入下一次補全，並檢查 Luna、
  `reasoning_effort: "none"` 及 `max_completion_tokens`，無舊版 `max_tokens`。
- 配置／輸入測試涵蓋部署及端點覆寫、空行、EOF、退出／結束、
  單次提示模式、無效選項與錯誤傳播。配額測試證明 429 不會重試。

## 整合運作原理

當你問 AI「5 + 3 等於多少？」時，完整流程如下：

1. <strong>你</strong> 用自然語言向 AI 詢問
2. **AI** 分析請求並判斷要加法
3. **AI** 呼叫 MCP 伺服器：`add(5.0, 3.0)`
4. <strong>計算機服務</strong> 執行：`5.0 + 3.0 = 8.0`
5. <strong>計算機服務</strong> 回傳：`"5.00 + 3.00 = 8.00"`
6. **AI** 接收結果並整理自然回應
7. <strong>你</strong> 得到：「5 與 3 的和是 8」

## 下一步

如需更多範例，請參見 [第 04 章：實用範例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->