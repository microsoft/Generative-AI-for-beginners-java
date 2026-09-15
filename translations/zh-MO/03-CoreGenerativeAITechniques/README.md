# 核心生成式人工智能技術教程

## 目錄

- [先決條件](#先決條件)
- [入門指南](#入門指南)
- [模型選擇指南](#模型選擇指南)
- [教程 1：LLM 完成與聊天](#教程-1：llm-完成與聊天)
- [教程 2：函數調用](#教程-2：函數調用)
- [教程 3：RAG（檢索增強生成）](#教程-3：rag（檢索增強生成）)
- [教程 4：負責任的 AI](#教程-4：負責任的-ai)
- [範例共通模式](#範例共通模式)
- [單元測試](#單元測試)
- [序列式實時驗證](#序列式實時驗證)
- [故障排除](#故障排除)
- [後續步驟](#下一步)

## 概述

四個獨立的 Java 程式展示聊天、會話歷史、函數調用、全文檢索增強生成 (RAG) 及負責任 AI 的回應處理。所有聊天請求預設目標為 **GPT-5.6 Luna 且推理努力設定為 `none`**。

這些範例使用官方 OpenAI Java SDK 搭配 Azure OpenAI 的 v1 端點，依照 [微軟的 SDK 指南](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) 實作。舊版的 `azure-ai-openai` 套件不再是依賴。保留 Chat Completions 以教導現有基於訊息的工作流程；其他 API 選項請參閱 [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure)。

## 先決條件

- Java 21 或以上版本與 Maven 3.6.3 或以上版本。
- 一個 Azure OpenAI 聊天部署名稱為 `gpt-5.6-luna`，或者具有相容 Chat Completions 設定的覆寫部署。
- 擁有資源上<strong>認知服務 OpenAI 使用者</strong>角色的已登入 Azure 身份。本地開發使用 Azure CLI 登入，託管應用可使用託管身份。
- 請參閱 [第 2 章](../02-SetupDevEnvironment/getting-started-azure-openai.md) 設置資源與登入指引。

[Maven 配置](../../../03-CoreGenerativeAITechniques/examples/pom.xml) 固定了以下版本（2026-09-14 進行確認）：

| 組件 | 版本 | 目的 |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | 官方 Azure v1 兼容客戶端 |
| `com.azure:azure-identity` | 1.18.6 | 無鑰匙認證與令牌續期 |
| `net.objecthunter:exp4j` | 0.4.8 | 不進行代碼執行的算術表達式解析 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | 離線 Jupiter 單元測試 |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 編譯、測試、可執行範例 |

編譯器使用 `--release 21`。這些獨立範例不需 Spring Boot、Spring AI 或 LangChain4j 依賴。

## 入門指南

從倉庫根目錄，在 shell 中設定資源端點與可選的部署覆寫。

**Windows PowerShell：**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS：**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

測試不需要 Azure 憑證或端點。Maven 不會自動讀取環境檔，請在啟動實時範例的 shell 設定環境變數。使用 IDE 啟動時，請確認啟動配置所提供的環境。

## 模型選擇指南

| 環境變數 | 意義 | 預設值 |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure 資源根 URL 或已正規化的 `/openai/v1` URL | 實時執行必填 |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天部署名稱，非模型版本 | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | 分開的嵌入部署設定，本四程式不使用 | `text-embedding-3-small` |

空白部署覆寫會使用預設值。配置會精確添加一次 `/openai/v1`，端點中不接受憑證、查詢字串與舊部署路徑。

每個聊天請求明確設定 `reasoningEffort(ReasoningEffort.NONE)` 與 `maxCompletionTokens(...)`。無請求使用 `temperature`、`top_p` 或舊有完成令牌選項。包含功能工具選擇與工具結果回應。GPT-5.6 聊天完成函數工具需推理努力 `none`；請參考 [微軟聊天指導](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)。

**本章無串流或嵌入入口。** 程式以檢索整份文件代替向量檢索。若要擴充嵌入，請使用獨立嵌入部署如 `text-embedding-3-small`，切勿用 Luna。

## 教程 1：LLM 完成與聊天

程式來源：[LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)。

程式執行簡單的 Java 流說明、兩回合 HashMap/TreeMap 對話與互動聊天。第二回合包含第一次助理回答；每個互動回合也會發送先前對話內容。

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` 提供部署與明確推理設定。互動聊天會跳過空白行，輸入 `exit` 或 EOF 結束，保留系統訊息及九回用戶／助理完成問答。回合數限制是教育示例，不是嚴格的令牌配額保證。

在 examples 目錄中：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

預期三次初始回答，接著出現 `You:` 提示。每個非空白互動問題新增一次請求。完成限制為每回合 200、300、400 然後 500 令牌。

## 教程 2：函數調用

程式來源：[FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)。

SDK 由註解過的 `WeatherArguments` 與 `CalculationArguments` 記錄推導 JSON 架構。須指定工具選擇，使每個範例執行工具協議而非接受模型未輔助答案。

1. 發送帶可用工具、推理努力為 `none` 與 300 令牌完成限制的問題。
2. 要求回傳 `tool_calls` 完成原因，驗證函數名稱與呼叫 ID，並解析類型化 JSON 參數。
3. 執行本地函數。模型不執行 Java 或任意代碼。
4. 添加一次助理工具調用訊息，接著依序添加帶有相符 `tool_call_id` 的每項結果。
5. 最後發送一次無工具的 300 令牌請求，要求完整且非空回答。

`get_weather` 回傳為<strong>模擬</strong>天氣非即時。尊重城市並在要求時將示例華氏溫度 22 攝氏轉換為華氏。`calculate` 利用 exp4j 評估表達式，支持如 `15% of 240` 和 `2 + 3 * 4` 的格式，拒絕空白、過大、無效或非有限數值運算。使用浮點運算，非財務級十進位精度。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

預期輸出 `Function: get_weather`、模擬西雅圖天氣、`Function: calculate`、`Function result: 36` 及兩個最後回答。不需 stdin 或外部天氣憑證。成功執行剛好用四次聊天請求。

## 教程 3：RAG（檢索增強生成）

來源：[SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)。輸入檔案：[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)。

此入門 RAG 範例檢索一整份 UTF-8 文件，並在用戶訊息中附帶問題。系統訊息指示模型將文件內容視為不可信數據，僅從該上下文回答。如文件不含答案，要求輸出：`I cannot find that information in the provided document.`。

取得基礎依據可降低幻覺，但定界符或系統指令無法保證準確或阻止所有提示注入。請檢查實時答案。生產環境 RAG 通常包括切塊、檢索、引用、訪問控制與評估。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

輸入一個問題，例如 `Which authentication method does the document describe?`，預期答案包含 Microsoft Entra ID。程式在一次 500 令牌完成限制的聊天請求後退出。

檔案預設從倉庫根目錄、章節資料夾或 examples 目錄尋找，也支援明確路徑設定：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

輸入必須非空白：最多 32 KiB UTF-8 文件資料與 2,000 字元問題。遺失文件、空白或 EOF 問題與過大輸入會在推理前失敗。

## 教程 4：負責任的 AI

來源：[ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)。

六種探測涵蓋有害指令、仇恨言論、隱私、醫療錯誤資訊、非法內容及善意負責任 AI 問題。程式觀察回應，而非假設每個探測必須觸發過濾。

| 結果 | 證據 |
| --- | --- |
| `FILTERED` | 明確的 `content_filter` / `ResponsibleAIPolicyViolation` 錯誤碼，或完成原因為 `content_filter` |
| `REFUSED` | 非空白結構化的 `message.refusal` 欄位 |
| `POSSIBLE_REFUSAL` | 普通文字中的開首拒絕語句；需審查的啟發式判斷 |
| `GENERATED` | 完成且非空回答；不保證內容安全 |

普通 HTTP 400 不是過濾證據。無效參數、認證失敗、速率限制、伺服器錯誤、格式錯誤回應與截斷輸出會導致執行失敗，而非偽造安全成功。「有害內容」等泛詞在善意解釋中不視為拒絕。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

預期六種分類結果與摘要，說明觀察不等同安全認證。每個探測限制 300 令牌完成。手動審查意外輸出與可能拒絕；善意範例應產生具內容的負責任 AI 解釋。不需 stdin。

## 範例共通模式

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) 集中管理端點正規化、部署覆寫、無鑰匙認證與聊天選項：

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

令牌供應者按需續期存取令牌。請勿記錄令牌或換成 API 金鑰。每個程式重用其客戶端，並在 `finally` 或自有 `AutoCloseable` 包裝中關閉；SDK 的 `OpenAIClient` 本身不實作 `AutoCloseable`。

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) 要求完成且非空的文字回答。空選項、拒絕、過濾與截斷回答不會無聲標示成功。負責任 AI 範例明確處理預期的過濾／拒絕結果。未處理的失敗會讓 Java/Maven 進程以非零碼退出。

**禁用自動 SDK 重試**，以維持共用低 RPM 部署的請求數可預測。每次推理請求有 60 秒超時。取得令牌可能須額外時間。應用層排程必須遵守配額；請勿盲目重試失敗的付費請求。

## 單元測試

在 examples 目錄中：

```powershell
mvn -B -ntp clean test
```

測試傳輸層完全替代 SDK HTTP 層，擷取實際序列化請求體並提供排隊回應。無套接字開啟，無 Azure 令牌取得，意外請求會失敗。這些測試驗證應用行為與 SDK 協議，非現場模型品質或部署可用性。

| 測試套件 | 覆蓋範圍 |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | 端點正規化/拒絕、部署覆寫、推理與令牌選項 |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | 所有完成工作流程、訊息歷史、完整回合裁剪、EOF、失敗 |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | 工具架構、類型化參數、算術、ID、多工具結果、失敗追蹤 |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | 檔案查找、UTF-8、大小限制、基礎負載、輸入與 API 錯誤 |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | 六種探測、明確過濾、拒絕分類、普通 400 與其他失敗 |

單一套件執行 `mvn -B -ntp test "-Dtest=FunctionsAppTest"`。共用夾具存放於 [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)。

## 序列式實時驗證

實時呼叫與單元測試分離。從倉庫根目錄，<strong>單獨使用</strong>下列指令，且僅在憑證和部署權限就緒後。無需服務或常駐程序。

對於共用 **10 次請求／分鐘** 部署，啟動下一程式前請保留足夠配額：5、4、1、接著 6 次請求。僅序列流程不保證速率限制合規。請與其他呼叫者協調滾動分鐘，勿貼批未節奏的四次調用。

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. 完成、多回合與兩個互動回合：**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

檢查所有三個章節標題、五個答案、一個最後的互動答案提及 Ada、`Goodbye!`，以及退出代碼 0。預算：**5 次請求，最多 1,900 完成標記數**。若是小規模運行，只處理 `exit`：3 次請求 / 900 標記，但這不會執行互動推理。

**2. 兩種函數調用工作流程：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

檢查兩個函數名稱、模擬的西雅圖天氣、計算結果 36、兩個最終答案，及退出代碼 0。預算：**4 次請求，最多 1,200 完成標記數**。

**3. 以文件為依據的答案：**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

檢查文件路徑、一個提到 Microsoft Entra ID 的答案，及退出代碼 0。預算：**1 次請求，最多 500 完成標記數**。存在的 [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) 是唯一必需的輸入文件。一個可選的第二次詢問缺失主題應避免回答，並增加一次請求 / 500 標記。

**4. 負責任AI觀察：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

檢查六個分類及觀察總結，審查產生的內容，並要求退出代碼 0 以技術完成。流程成功退出不代表模型安全。預算：**6 次請求，最多 1,800 完成標記數**。

**四個命令共計：16 次聊天請求以及最多 5,400 完成標記數**，加上輸入標記（包含重複對話和工具架構/歷史）。沒有嵌入請求。實際標記使用依賴模型，且可能較低，尤其對已過濾的提示字。花費依照部署價格而定；無固定金額估算。所有請求限制假設無人工重試。每個命令後立即檢查 `$LASTEXITCODE`；非零表示運行未成功完成。

## 故障排除

- **缺失端點 / 401 / 403：** 在啟動過程中設置端點，驗證本地 Azure 登錄及資源範圍角色，並檢查意外的身份環境覆蓋。
- **400 / 404：** 確認部署存在且支持帶推理努力 `none` 的聊天完成。請使用 HTTPS 資源根或 `/openai/v1` URL，而非舊版部署 URL。普通 400 錯誤是技術失敗，不是安全阻擋。
- **429：** 在重試之前協調共享 RPM 和標記配額。範例故意不自動重試。
- **`Incomplete chat response: length`：** 輸出達到完成限制。審查回應和提示，然後再增加限制及其文檔預算；不要將截斷的執行視為成功。
- **檔案或標準輸入錯誤：** 從支援目錄啟動或提供明確的文件路徑。提供非空白的讀者問題。完成可以正常結束於 EOF 或 `exit`。
- **編譯錯誤：** 驗證 Java 21 或更高版本，然後執行 `mvn -B -ntp clean test`。在 PowerShell 中，引用包含點屬性的整個 Maven 參數，例如 `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`。

## 下一步

繼續閱讀 [第4章：實際範例](../04-PracticalSamples/README.md)。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->