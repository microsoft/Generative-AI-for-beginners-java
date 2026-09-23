# 核心生成式 AI 技術教學

## 目錄

- [前置條件](#前置條件)
- [入門指南](#入門指南)
- [模型選擇指南](#模型選擇指南)
- [教學 1: LLM 補全與聊天](#教學-1：llm-補全與聊天)
- [教學 2: 函數呼叫](#教學-2：函數呼叫)
- [教學 3: RAG（檢索增強生成）](#教學-3：rag（檢索增強生成）)
- [教學 4: 負責任的 AI](#教學-4：負責任的-ai)
- [範例間的常見模式](#範例間的常見模式)
- [單元測試](#單元測試)
- [序列即時驗證](#序列即時驗證)
- [故障排除](#疑難排解)
- [後續步驟](#下一步)

## 概述

四個獨立的 Java 程式示範聊天、對話歷史、函數呼叫、整篇文件檢索增強生成（RAG）與負責任 AI 回應處理。所有聊天請求預設均針對 **GPT-5.6 Luna 且推理工作量 `none`**。

這些範例使用官方 OpenAI Java SDK 和 Azure OpenAI 的 v1 端點，遵循 [Microsoft 的 SDK 指導](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages)。舊版的 `azure-ai-openai` 套件不再是依賴。聊天補全則保留用於教學現有以訊息為基礎的工作流程；其他 API 選項請參閱 [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure)。

## 前置條件

- Java 21 或更新版本、Maven 3.6.3 或以上。
- 名為 `gpt-5.6-luna` 的 Azure OpenAI 聊天部署，或相容 Chat Completions 設定的覆寫。
- 已登入之 Azure 身份，且在資源上擁有 **認知服務 OpenAI 使用者** 角色。本地開發使用 Azure CLI 登入；託管應用可用受管身份。
- 請參見 [第二章](../02-SetupDevEnvironment/getting-started-azure-openai.md) 了解資源設定與登入指引。

[Maven 設定檔](../../../03-CoreGenerativeAITechniques/examples/pom.xml) 鎖定以下版本，於 2026-09-14 確認：

| 元件 | 版本 | 用途 |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | 官方 Azure v1 相容用戶端 |
| `com.azure:azure-identity` | 1.18.6 | 無密鑰認證與令牌刷新 |
| `net.objecthunter:exp4j` | 0.4.8 | 算術表達式解析，不執行代碼 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | 離線 Jupiter 單元測試 |
| Maven 編譯器 / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 編譯、測試、可執行範例 |

編譯器使用 `--release 21`。這些獨立範例不需 Spring Boot、Spring AI 或 LangChain4j 依賴。

## 入門指南

從倉庫根目錄，在 shell 設定資源端點和選擇性部署覆寫。

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

測試不需要 Azure 憑證或端點。Maven 不會自動讀取環境檔；請在啟動即時範例的 shell 中設定變數。IDE 啟動時請確認所提供的環境。

## 模型選擇指南

| 環境變數 | 意義 | 預設值 |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure 資源根目錄或已標準化的 `/openai/v1` URL | 直播執行必填 |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天部署名稱，非模型版本 | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | 獨立嵌入部署設定，這四個程式不使用 | `text-embedding-3-small` |

空白部署覆寫使用預設。設定會準確附加一次 `/openai/v1`，並拒絕端點中憑證、查詢字串與舊版部署路徑。

每個聊天請求都明確設置 `reasoningEffort(ReasoningEffort.NONE)` 和 `maxCompletionTokens(...)`。沒有請求設定 `temperature`、`top_p` 或舊版補全令牌選項。包含工具選擇及工具結果後續。GPT-5.6 聊天補全工具需推理工作量 `none`；詳見 [Microsoft 的聊天指導](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)。

**本章無串流或嵌入入口。** 讀者取回整篇文件，而非向量。如擴充嵌入，請使用獨立嵌入部署如 `text-embedding-3-small`，絕不使用 Luna。

## 教學 1：LLM 補全與聊天

來源：[LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)。

程式執行簡易 Java streams 解說、兩回合 HashMap/TreeMap 對話與互動式聊天。第二回合含首個助理回應；每個互動回合也送出之前對話。

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` 提供部署和明確推理設定。互動聊天跳過空行，以 `exit` 或 EOF 結束，保留系統訊息加九回合完成的使用者／助理對話。回合數修剪是教學限制，非嚴格的令牌預算保證。

從 examples 目錄：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

預期三個初始答案，然後顯示 `You:` 提示。每個非空互動提問都新增一個請求。補全限制分別為每回合 200、300、400，再到 500 令牌。

## 教學 2：函數呼叫

來源：[FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)。

SDK 從帶註解的 `WeatherArguments` 和 `CalculationArguments` 記錄導出 JSON 架構。必需工具選擇使每個範例都用工具協定，而非接受模型獨立回答。

1. 送出包含允許工具、推理工作量 `none` 和 300 令牌補全限制的提問。
2. 必須以 `tool_calls` 作為結束理由，驗證函數名稱和呼叫 ID，並解析型別化 JSON 參數。
3. 執行本機函數。模型不執行 Java 或任意代碼。
4. 添加助理工具呼叫訊息一次，然後依序加入每個對應 `tool_call_id` 的結果。
5. 最後送出一個無工具的 300 令牌請求，要求完成且非空答案。

`get_weather` 回傳 <strong>模擬</strong> 的非即時天氣。尊重城市參數，若要求，顯示模式會將示例攝氏 22 度轉為華氏。`calculate` 利用 exp4j 評估所給算式，支援格式如 `15% of 240` 與 `2 + 3 * 4`，拒絕空白、過大、無效或非有限計算。該計算使用浮點運算，非金融十進制精度。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

預期看到 `Function: get_weather`、模擬西雅圖天氣、`Function: calculate`、`Function result: 36`，以及兩個最終答案。不需 stdin 或外部天氣憑證。一個成功運行精確使用四個聊天請求。

## 教學 3：RAG（檢索增強生成）

來源：[SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)。輸入：[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)。

此入門版 RAG 範例檢索整篇 UTF-8 文件，隨問題包含於使用者訊息中。另有系統訊息指示模型將文件內容視為不可信資料，且僅從該內容回答。如文件未包含答案，應回覆：`我在提供的文件中找不到該資訊。`

接地文可減少幻覺，但分隔符與系統指令無法保證準確或防範所有提示注入。請檢查即時答案。實務中 RAG 通常會加入分段、檢索、引用、存取控制與評估。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

輸入一個問題，例如 `文件描述的是哪種身份驗證方法？`。預期答案提及 Microsoft Entra ID。程式以 500 令牌補全限制執行一個聊天請求後結束。

預設檔案搜索依序從倉庫根目錄、章節目錄或 examples 目錄。也支援明確的檔案路徑：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

輸入不得為空白：UTF-8 文件資料最大 32 KiB，問題最多 2000 字元。缺檔、空白/EOF 題目及過大輸入都會在推理前失敗。

## 教學 4：負責任的 AI

來源：[ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)。

六個探針涵蓋有害指示、仇恨言論、隱私、醫療錯誤資訊、非法內容以及一則良性負責任 AI 問題。程式觀察回應，非假設每個探針皆必觸發過濾。

| 結果 | 證據 |
| --- | --- |
| `FILTERED` | 明確的 `content_filter` / `ResponsibleAIPolicyViolation` 錯誤碼，或補全以 `content_filter` 結束 |
| `REFUSED` | 非空結構化的 `message.refusal` 欄位 |
| `POSSIBLE_REFUSAL` | 普通文字開頭拒絕語句；一種需人工審查的啟發式 |
| `GENERATED` | 已完成且非空回應；不代表內容安全的證明 |

普通 HTTP 400 <strong>非</strong>過濾證據。無效參數、驗證失敗、配額限制、伺服器錯誤、錯誤格式回應與截斷輸出會導致執行失敗，而非假成功。良性解說中的「有害內容」等寬泛字詞不視為拒絕。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

預期六個類別結果及總結表示觀察結果非安全認證。每個探針補全限制 300 令牌。對異常生成與可能拒絕需人工复查；良性對照應生成實質負責任 AI 解說。不需 stdin。

## 範例間的常見模式

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) 集中處理端點標準化、部署覆寫、無密鑰認證及聊天選項：

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

令牌提供者依需刷新存取令牌。請勿紀錄令牌或用 API 金鑰取代。每個程式重複使用自己的用戶端，並於 `finally` 或其自有的 `AutoCloseable` 包裝類中關閉；SDK 的 `OpenAIClient` 本身不具 `AutoCloseable`。

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) 要求完成且非空的文本答案。空白選擇、拒絕、過濾與截斷答案不會靜默當作成功輸出。負責任 AI 範例明確處理預期的過濾／拒絕結果。未處理失敗將使 Java/Maven 程序以非零狀態碼退出。

**已禁用自動 SDK 重試**，以維持共享低 RPM 部署上請求計數的可預測性。每個推理請求有 60 秒超時。令牌取得可能需額外時間。應用層排程務必尊重配額；勿盲目重試失敗的付費請求。

## 單元測試

從 examples 目錄：

```powershell
mvn -B -ntp clean test
```

測試傳輸層完全替代 SDK 的 HTTP 層，擷取實際序列化的請求體，並提供排隊回應。不開啟任何 socket、不取得 Azure 令牌，遇意外請求即失敗。這些測試驗證應用行為與 SDK 協定，非即時模型品質或部署可用性。

| 測試套件 | 涵蓋範圍 |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | 端點標準化／拒絕、部署覆寫、推理與令牌選項 |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | 所有補全工作流程、訊息歷史、完整回合修剪、EOF、失敗 |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | 工具架構、型別參數、算術、ID、多工具結果、失敗後續 |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | 檔案查找、UTF-8、大小限制、接地載荷、輸入及 API 錯誤 |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | 全部六個探針、明確過濾、拒絕分類、普通 400 及其他失敗 |

單套件執行範例：`mvn -B -ntp test "-Dtest=FunctionsAppTest"`。共用夥伴元件存放於 [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)。

## 序列即時驗證

即時呼叫與單元測試分離。以下指令須<strong>分別</strong>於倉庫根目錄執行，且僅在憑證與部署存取準備完畢後執行。無需任何服務或持久化進程。

針對共用的 **每分鐘 10 請求** 部署，請於啟動下一個程式前保留足夠額度：5、4、1，再到 6 請求。僅序列處理無法保證速率限制遵守。請與其他所有呼叫者協調滾動分鐘；勿將四次呼叫貼成不間斷批次。

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. 補全，多回合及兩回互動：**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

檢查所有三個章節標題、五個答案、最後一個回顧 Ada 的互動答案、`Goodbye!`，以及退出代碼 0。預算：**5 個請求，最多 1,900 個完成標記**。若要進行較小的運行，只傳入 `exit`：3 個請求 / 900 個標記，但那不會執行互動推斷。

**2. 兩個函數調用工作流程：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

檢查兩個函數名稱、模擬西雅圖天氣、計算結果 36、兩個最終答案，以及退出代碼 0。預算：**4 個請求，最多 1,200 個完成標記**。

**3. 以文件為依據的答案：**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

檢查文件路徑、提及 Microsoft Entra ID 的答案，以及退出代碼 0。預算：**1 個請求，最多 500 個完成標記**。[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) 是唯一必需的輸入檔案。可選的第二次運行詢問不存在的主題應該避免回答，並增加一個請求 / 500 個標記。

**4. 負責任 AI 觀察：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

檢查六個分類及觀察總結，審核產生的內容，並要求退出代碼 0 作為技術完成。成功結束流程並不保證模型的安全性。預算：**6 個請求，最多 1,800 個完成標記**。

**四個命令總計：16 次聊天室請求和最多 5,400 個完成標記**，加上輸入標記（包括重複的對話和工具架構／歷史）。無嵌入請求。實際標記使用視模型而定，可能較低，尤其對被過濾的提示。美元費用視部署價格而定；不代表固定的貨幣估計。所有請求限制假設不進行手動重跑。請在執行每個命令後立即檢查 `$LASTEXITCODE`；非零代表運行未成功完成。

## 疑難排解

- **缺少端點 / 401 / 403：** 在啟動程序中設定端點，驗證本地 Azure 登入與資源範圍角色，並檢查是否有意外的身份環境覆蓋。
- **400 / 404：** 確認部署存在且支援帶有 `none` 推理努力的聊天完成。使用 HTTPS 資源根目錄或 `/openai/v1` URL，不要使用舊有部署 URL。普通 400 錯誤為技術失敗，不是安全阻擋。
- **429：** 協調共享的 RPM 和標記配額後再重試。示例特意不自動重試。
- **`Incomplete chat response: length`：** 輸出達到完成限制。請在增加限制及其文件化預算之前檢閱回應及提示；不要將截斷的運行視為成功。
- **檔案或標準輸入錯誤：** 從受支援的目錄啟動或傳入明確的文件路徑。提供非空讀取器問題。完成可在 EOF 或 `exit` 正常結束。
- **編譯錯誤：** 確認 Java 21 或更新版本，接著執行 `mvn -B -ntp clean test`。在 PowerShell 中，整個包含點屬性的 Maven 參數要加引號，例如 `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`。

## 下一步

繼續閱讀 [第 4 章：實用範例](../04-PracticalSamples/README.md)。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議進行專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->