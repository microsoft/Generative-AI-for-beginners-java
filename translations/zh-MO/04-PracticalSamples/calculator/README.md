# 初學者 MCP 計算機教學

## 目錄

- [你將會學到什麼](#你將會學到什麼)
- [先決條件](#先決條件)
- [依賴版本](#依賴版本)
- [專案結構介紹](#專案結構介紹)
- [核心元件說明](#核心元件說明)
  - [1. 主應用程式](#1-主應用程式)
  - [2. 計算機服務](#2-計算機服務)
  - [3. 直接 MCP 用戶端](#3-直接-mcp-用戶端)
  - [4. AI 驅動用戶端](#4-ai-驅動用戶端)
- [執行範例](#執行範例)
- [離線測試](#離線測試)
- [整合運作原理](#整合運作原理)
- [下一步](#下一步)

## 你將會學到什麼

本教學將說明如何使用 Model Context Protocol (MCP) 建立一個計算機服務。你會了解：

- 如何建立供 AI 使用的工具服務
- 如何設定與 MCP 服務的直接通訊
- AI 模型如何自動選擇要使用的工具
- 直接協議呼叫與 AI 輔助互動的差別

## 先決條件

開始前，請確定你已經：
- 安裝 Java 21 或更高版本
- 使用 Maven 管理依賴
- 具備基本 Java 與 Spring Boot 知識

只有 AI 用戶端需要 Azure OpenAI 部署與認證的 `DefaultAzureCredential`，
例如本機已有 Azure CLI 登入或 Azure 的管理識別身份。該身份需要
在資源上擁有 Cognitive Services OpenAI 使用者角色。詳見[第 2 章](../../02-SetupDevEnvironment/getting-started-azure-openai.md)。
伺服器、直接 SDK 用戶端及所有自動化測試不需要 Azure 帳號或模型存取權限。

## 依賴版本

2026-09-14 驗證通過的釋出依賴版本：

| 依賴 | 版本 |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI 管理) | 2.0.0 |
| LangChain4j / 核心 | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j 官方 OpenAI 適配器 | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot 管理) | 6.0.3 |

MCP 和官方 OpenAI 適配器是 Maven 中央庫的正式 Beta 釋出版本，非快照版本。
它們的版本和 LangChain4j 核心不同。無需快照或里程碑版本庫。
用戶端依賴屬於測試範圍，因為可執行範例放在 `src/test/java` 之下。

## 專案結構介紹

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

這是我們計算機服務的進入點。它是標準的 Spring Boot 應用程式，但有一個特殊增加：

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

**作用：**
- 啟動一個監聽 8080 埠的 Spring Boot Web 伺服器
- 建立 `ToolCallbackProvider`，將計算機方法以 MCP 工具形式公開
- `@Bean` 註解讓 Spring 管理此元件，供其他部分使用

### 2. 計算機服務

**檔案：** `CalculatorService.java`

此處執行所有數學運算。每個方法都使用 `@Tool` 標記，使其可被 MCP 調用：

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
    
    // 更多計算器操作...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**主要特點：**

1. **`@Tool` 標註**：告知 MCP 此方法可被外部用戶端呼叫
2. <strong>清晰描述</strong>：每個工具都有說明，幫助 AI 模型判斷使用時機
3. <strong>統一回傳格式</strong>：所有運算回傳易讀字串，如 "5.00 + 3.00 = 8.00"
4. <strong>錯誤處理</strong>：除以零與負平方根將回傳錯誤訊息

**可用運算：**
- `add(a, b)` - 兩數相加
- `subtract(a, b)` - 用第一數減第二數
- `multiply(a, b)` - 兩數相乘
- `divide(a, b)` - 用第一數除以第二數（具除零檢查）
- `power(base, exponent)` - 計算冪次方
- `squareRoot(number)` - 計算平方根（具負值檢查）
- `modulus(a, b)` - 回傳除法餘數
- `absolute(number)` - 回傳絕對值
- `help()` - 回傳所有運算說明

### 3. 直接 MCP 用戶端

參見 [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)。

此用戶端使用 `/mcp` 路徑的 `HttpClientStreamableHttpTransport`，初始化連線，
向伺服器 ping，並處理工具列表分頁。它會檢查所有九個預期工具是否存在，
並呼叫其中每個工具，包括 `modulus` 和 `help`，不使用 AI 模型。

目前的請求建立器如下：

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

協議錯誤會使用戶端失敗，而非錯誤地顯示成功。MCP 用戶端使用 try-with-resources
包含在發現或工具呼叫失敗時會自動關閉。

### 4. AI 驅動用戶端

參見 [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
與 [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)。

`OpenAiOfficialChatModel` 實作最新的 LangChain4j `ChatModel` API。
`StreamableHttpMcpTransport` 將其連接到與 SDK 用戶端相同的 `/mcp` 端點。
`AiServices` 負責發現工具並管理工具呼叫與結果對話。

預設部署為 **GPT-5.6 Luna**，且明確禁用推理：

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

這些預設適用於每次完成請求，包括工具執行後的後續問答。
用戶端使用可刷新 `BearerTokenCredential`，由 `DefaultAzureCredential` 支持，
並使用 `https://ai.azure.com/.default` 範圍，而非一時性 API 金鑰令牌。
同時接受資源 URL 與已以 `/openai/v1` 結尾的 URL。

Bot 維持有限的對話歷史，印出 `Tool executed: ...` 並附上實際 MCP 結果，
若回應跳過工具則失敗。限制工具迴圈為四個回合。
驗證、模型、MCP 與工具錯誤會傳播；自動模型重試被禁用。
MCP 傳輸/用戶端與官方 OpenAI 用戶端於成功或失敗後皆關閉。

## 執行範例

### 步驟 1：啟動計算機伺服器

伺服器無需 Azure 配置。以下命令在本範例目錄執行。
範例使用埠號 **18081**，以避免與其他範例衝突；預設仍為 8080。

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP 端點為 `http://localhost:18081/mcp`。健檢與發現資訊位於
`http://localhost:18081/health` 和 `http://localhost:18081/info`。
Streamable HTTP 取代舊的 SSE-only 傳輸；`/sse` 與 `/v1/tools` 不再是有效端點。

### 步驟 2：使用直接用戶端測試

開啟另一個 PowerShell 終端：

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

無需輸入。所有九個工具均被調用。期望算術結果包含
8、6、42、5、256、4、2 和 5.5，最後是說明文字。

### 步驟 3：使用 AI 用戶端測試

驗證完成後，如先決條件所述，在相同終端配置 AI 用戶端：

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

預期看到 `Tool executed: add` 行帶有 `41.80`，接著是模型回答。
單次提示模式會在不等待輸入下退出。要執行原始四次提示示範：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

示範時會呼叫 `add`、`squareRoot`、`help` 和連鎖的 `power` 再 `divide` 運算。
預期數值答案為 41.8、12 和 64。省略參數也會執行此示範。

### 步驟 4：執行互動式 Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

輸入 `Multiply 6 by 7 using the calculator service`，再輸入 `exit` 或 `quit`。
預期真實呼叫 `multiply` 工具並得到 42。空行被忽略；EOF 也會結束會話。
此進入點的非互動煙霧測試：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

兩個 AI 進入點均接受 `--prompt "question"`、`--demo` 與 `--interactive`。
無效選項會在開啟連線前失敗。每個 Maven `-D...` 參數需在 PowerShell 中完整引號包裹。
Bash 中則使用 `export NAME=value` 替代 `$env:NAME = "value"`。

**配額：** 依序運行 AI 範例。簡單提示通常需兩次模型請求；
完整示範通常需九次，包括工具結果的後續追蹤。共享 10 RPM
部署時，運行下一次 AI 前應等待新的配額區間。429 錯誤會顯示失敗並無自動重試；
請遵循服務的重試提示。實際請求量依模型而異。
離線測試不會消耗任何配額，且不會建立 Luna 的可用性或回答品質。

### 設定與關閉

| 設定項目 | 預設 / 行為 |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`；基本 URL，不包含 `/mcp` |
| `-Dmcp.server.url=...` | 覆寫所有用戶端的 `MCP_SERVER_URL` |
| `AZURE_OPENAI_ENDPOINT` | 僅 AI 用戶端需要；資源 URL 或 `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`；Azure 部署名稱 |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`；正整數 |
| 推理努力 | 總是 `none`，包含工具迴圈後續 |

覆寫的部署必須支援 `reasoning_effort=none` 與 `max_completion_tokens`。
用戶端不會自動讀取 `.env` 檔案。測試結束後用 `Ctrl+C` 停止伺服器。
用戶端正常回傳，無 `System.exit` 或關閉等待。

## 離線測試

```powershell
mvn -B -ntp clean verify
```

所有測試均為針對 Azure 的離線測試：協議套件啟動 Spring 伺服器及
OpenAI 相容的存根於隨機回環埠，然後關閉。Maven 仍可能需要
下載依賴。未使用憑證、實體部署或預存 MCP 伺服器。

- 計算機單元測試涵蓋所有算術運算、小數結果、說明與領域錯誤。
- MCP 測試涵蓋初始化、發現、所有九個工具呼叫、工具失敗以及健康/信息。
- AI 協議測試執行完整示範及互動 Bot 於真實計算機，
  驗證工具結果餵入下一完成請求，並檢查每個 HTTP 主體有 Luna，
  `reasoning_effort: "none"` 與 `max_completion_tokens` 無舊版 `max_tokens`。
- 配置/輸入測試涵蓋部署與端點覆寫、空行、EOF、exit/quit，
  單提示模式、無效選項及錯誤傳播。配額測試證明 429 不會重試。

## 整合運作原理

當你問 AI「5 加 3 是多少？」時，完整流程如下：

1. <strong>你</strong> 以自然語言向 AI 詢問
2. **AI** 解析你的請求，判斷你要做加法
3. **AI** 呼叫 MCP 伺服器：`add(5.0, 3.0)`
4. <strong>計算機服務</strong> 執行：`5.0 + 3.0 = 8.0`
5. <strong>計算機服務</strong> 回傳：`"5.00 + 3.00 = 8.00"`
6. **AI** 收到結果並格式化自然回應
7. <strong>你</strong> 得到：「5 和 3 的合是 8」

## 下一步

更多範例請見 [第 04 章：實用範例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->