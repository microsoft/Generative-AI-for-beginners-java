# 核心生成式 AI 技術教學

## 目錄

- [先決條件](#先決條件)
- [快速開始](#快速開始)
- [模型選擇指南](#模型選擇指南)
- [教學 1：大型語言模型完成與聊天](#教學-1：大型語言模型完成與聊天)
- [教學 2：函數呼叫](#教學-2：函數呼叫)
- [教學 3：RAG（檢索增強生成）](#教學-3：rag（檢索增強生成）)
- [教學 4：負責任的 AI](#教學-4：負責任的-ai)
- [範例中的常見模式](#範例中的常見模式)
- [單元測試](#單元測試)
- [序列化現場驗證](#序列化現場驗證)
- [疑難排解](#疑難排解)
- [後續步驟](#接下來的步驟)

## 概覽

四個獨立的 Java 程式示範聊天、對話歷史、函數呼叫、整篇文件檢索增強生成功能 (RAG) 及負責任的 AI 回應處理。所有聊天請求預設目標為 **GPT-5.6 Luna，推理努力為 `none`**。

這些範例使用官方 OpenAI Java SDK 搭配 Azure OpenAI 的 v1 端點，依據 [微軟的 SDK 指南](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages)。較舊的 `azure-ai-openai` 套件已不再是依賴。保留聊天完成（Chat Completions）以教授既有的訊息基工作流程；可參考 [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) 了解其他 API 選項。

## 先決條件

- Java 21 以上版本及 Maven 3.6.3 以上版本。
- Azure OpenAI 聊天部署名稱為 `gpt-5.6-luna`，或使用相容聊天完成設定的替代部署。
- 已登入的 Azure 身份，且在此資源上具備 **Cognitive Services OpenAI User** 角色。 本地端開發使用 Azure CLI 登入；託管的應用程式可使用託管身份識別。
- 請參閱 [章節 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) 了解資源設定與登入說明。

[Maven 設定](../../../03-CoreGenerativeAITechniques/examples/pom.xml) 鎖定以下版本，檢查日期為 2026-09-14：

| 元件 | 版本 | 用途 |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | 官方 Azure v1 相容用戶端 |
| `com.azure:azure-identity` | 1.18.6 | 無鍵認證與權杖刷新 |
| `net.objecthunter:exp4j` | 0.4.8 | 不執行程式碼的算術運算式解析 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | 離線 Jupiter 單元測試 |
| Maven 編譯器 / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 編譯、測試、可執行範例 |

編譯器使用 `--release 21`。這些獨立範例不需 Spring Boot、Spring AI 或 LangChain4j 依賴。

## 快速開始

從專案根目錄，在你的 shell 中設定資源端點與選擇性部署覆寫。

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

測試不需要 Azure 憑證或端點。Maven 不會自動讀取環境檔；請在啟動現場範例的 shell 設定變數。使用 IDE 啟動時，請確認啟動設定所提供的環境。

## 模型選擇指南

| 環境變數 | 說明 | 預設值 |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure 資源根 URL 或已正規化的 `/openai/v1` 端點 | 現場運行必填 |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天部署名稱，不是模型版本 | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | 獨立向量嵌入部署設定，這四個程式未使用 | `text-embedding-3-small` |

空白部署覆寫使用預設值。設定會正確附加 `/openai/v1` 一次，並拒絕端點中的憑證、查詢字串及舊有部署路徑。

每個聊天請求都明確設定 `reasoningEffort(ReasoningEffort.NONE)` 和 `maxCompletionTokens(...)`。沒請求會設定 `temperature`、`top_p` 或舊版完成代幣選項。這包括工具選擇及工具結果後續。GPT-5.6 聊天完成函數工具需推理努力為 `none`；詳見 [微軟聊天指南](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)。

**本章無串流或嵌入入口。** 讀取的是整個文件，而非向量。如要擴充嵌入，請使用獨立向量部署，如 `text-embedding-3-small`，切勿使用 Luna。

## 教學 1：大型語言模型完成與聊天

原始碼：[LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)。

程式執行一個簡單 Java 流解釋、兩輪 HashMap/TreeMap 對話，以及互動聊天。第二輪包含第一個助理回覆；每個互動回合也傳送其先前對話。

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` 提供部署與明確推理設定。互動聊天跳過空白行，輸入 `exit` 或 EOF 即結束，並保留系統訊息及九個完成的用戶/助理回合。回合數修剪是教育上的限制，而非精確的代幣預算保證。

從 examples 目錄：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

閱讀三個初始回答，接著看到 `You:` 提示。每個非空白互動問題會新增一次請求。完成代幣限制依序為 200、300、400，再到 500 代幣。

## 教學 2：函數呼叫

原始碼：[FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)。

SDK 透過註解的 `WeatherArguments` 與 `CalculationArguments` 紀錄推導 JSON 架構。必須選擇工具，使每個範例練習工具協定，而非接受模型的無輔助答案。

1. 傳送包含允許工具、推理努力為 `none`、完成代幣限制 300 的問題。
2. 需以 `tool_calls` 結束並驗證函數名及呼叫 ID，解析型別化 JSON 引數。
3. 執行本地函數，模型不會執行 Java 或任意程式碼。
4. 新增一次助理工具呼叫訊息，後續傳送每個結果及其匹配的 `tool_call_id`。
5. 傳送一次不含工具的 300 代幣請求，要求有完成且非空白答案。

`get_weather` 回傳的是<strong>模擬</strong>天氣，而非實時天氣。它依據城市呈現並將範例的 22 攝氏度轉換為華氏度（如被要求）。`calculate` 使用 exp4j 評估傳入的計算式，支援例如 `15% of 240` 和 `2 + 3 * 4` 形式，拒絕空白、超大、無效或非有限計算。計算使用浮點數運算，而非財務十進位精確度。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

預期看到 `Function: get_weather`、模擬的 Seattle 天氣，`Function: calculate`、`Function result: 36` 和兩個最終答案。無需標準輸入或外部天氣憑證。成功運行精確使用四個聊天請求。

## 教學 3：RAG（檢索增強生成）

原始碼：[SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)。輸入檔案：[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)。

此入門 RAG 範例擷取一整份 UTF-8 文件並包含於用戶訊息與問題中。系統訊息指示模型將文件內容視為未驗證資料，回答只能根據該上下文。若文件中不包含答案，模型回應為：`我無法在提供的文件中找到該資訊。`

以根據資料回答能減少幻覺，但分隔符與系統指示都無法保證正確性或阻止每次提示注入。請檢視現場答案。產品級 RAG 通常會加入分段、檢索、引用、存取控制與評估。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

輸入一個問題，例如 `該文件描述的是哪一種身份驗證方法？`。預期答案會提及 Microsoft Entra ID。程式在單次聊天請求後以 500 代幣完成限制結束。

預設檔案搜尋自專案根目錄、章節目錄或 examples 目錄，也支援指定完整路徑：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

輸入不得為空：最多 32 KiB UTF-8 文件資料、2,000 字元問題。缺失檔案、空白或 EOF 問題、過大輸入均會在推理前失敗。

## 教學 4：負責任的 AI

原始碼：[ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)。

六個測試涵蓋不良指令、仇恨言論、隱私、醫療錯誤資訊、違法內容與良性負責任 AI 問題。程式觀察回應，而非假設每個測試都必須觸發過濾。

| 結果 | 證據 |
| --- | --- |
| `FILTERED` | 明確的 `content_filter` / `ResponsibleAIPolicyViolation` 錯誤碼，或完成的 `content_filter` 結束原因 |
| `REFUSED` | 非空白結構化的 `message.refusal` 欄位 |
| `POSSIBLE_REFUSAL` | 普通文字中的開頭拒絕語句；需審查的啟發式判斷 |
| `GENERATED` | 一個完成且非空白的回答；不代表內容安全的證明 |

一般 HTTP 400 <strong>不是</strong> 過濾證據。無效參數、驗證失敗、頻率限制、伺服器錯誤、格式錯誤回應及截斷輸出會導致運行失敗，而非產生假正向安全成功。像「有害內容」這類寬泛詞彙在良性解釋中不算拒絕。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

預期六個類別結果與摘要說明此為觀察結果，非安全認證。每個測試限制為 300 代幣完成。手動檢視意外產生及可能拒絕回應；良性比較應產出實質負責任 AI 解釋。不需標準輸入。

## 範例中的常見模式

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) 集中管理端點正規化、部署覆寫、無鍵認證和聊天選項：

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

代幣供應器會視需要刷新存取權杖。切勿記錄權杖或改用 API 金鑰。每個程式重用其用戶端並在 `finally` 或自訂 `AutoCloseable` 包裝中關閉； SDK 的 `OpenAIClient` 本身不是 `AutoCloseable`。

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) 要求回應完成且非空白文本。空白選項、拒絕、過濾和截斷答案不會悄悄列印成成功。負責任 AI 範例明確處理預期的過濾/拒絕結果。未處理的失敗會導致 Java/Maven 程式非零退出碼。

**自動 SDK 重試已停用**，以保持共用低 RPM 部署中請求數的可預測性。每次推理請求有 60 秒逾時。代幣取得可能需額外時間。應用層排程必須尊重配額；切勿盲目重試已失敗的付費請求。

## 單元測試

從 examples 目錄：

```powershell
mvn -B -ntp clean test
```

測試傳輸模擬整個 SDK HTTP 層，擷取實際序列化的請求主體，並提供排隊的回應。它不會開啟任何套接字、不取得 Azure 權杖，並會在遇到非預期請求時失敗。這些測試驗證應用行為與 SDK 協定，不測試現場模型品質或部署狀態。

| 測試套件 | 範圍 |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | 端點正規化/拒絕、部署覆寫、推理與代幣選項 |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | 每種完成工作流程、訊息歷史、完整回合修剪、EOF、失敗 |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | 工具架構、型別化引數、算術、ID、複數工具結果、失敗後續處理 |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | 檔案查找、UTF-8、大小限制、根據載入、輸入與 API 錯誤 |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | 六個測試、明確過濾、拒絕分類、一般 400 與其他失敗 |

執行某套件，使用 `mvn -B -ntp test "-Dtest=FunctionsAppTest"`。共用 fixture 位於 [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)。

## 序列化現場驗證

現場呼叫獨立於單元測試。等憑證與部署權限準備好後，分別從專案根目錄使用下列指令。無需服務或常駐程序。

對於共用的 **10 請求/分鐘** 部署，啟動前請預留足夠配額覆蓋整個下個程式：5、4、1 再到 6 個請求。序列處理本身無法保證頻率限制合規。請與所有呼叫方協調滾動分鐘計時；勿將四個呼叫一口氣貼上執行。

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. 完成、多回合及兩輪互動：**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

檢查所有三個章節標題、五個回答、最後一個呼叫 Ada 的互動回答、「再見！」以及退出代碼 0。預算：**5 次請求，最多 1,900 完成標記**。若要較小的執行，僅傳入 `exit`：3 次請求 / 900 標記，但不會執行互動推理。

**2. 兩個函數呼叫工作流程：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

檢查兩個函式名稱、模擬西雅圖天氣、計算結果 36、兩個最終答案，及退出代碼 0。預算：**4 次請求，最多 1,200 完成標記**。

**3. 文檔為基礎的回答：**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

檢查文檔路徑、一個提到 Microsoft Entra ID 的回答，以及退出代碼 0。預算：**1 次請求，最多 500 完成標記**。現有的 [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) 是唯一所需的輸入檔案。關於缺失主題的可選第二次運行應避免回答，並增加一個請求 / 500 標記。

**4. 負責任 AI 觀察：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

檢查六個類別和觀察摘要，審核產生的內容，並要求退出代碼 0 作為技術完成依據。成功的程序退出並不代表模型安全。預算：**6 次請求，最多 1,800 完成標記**。

**四個命令總計：16 次聊天請求和最多 5,400 完成標記**，外加輸入標記（包括重複對話與工具結構/歷史）。無內嵌向量請求。實際標記使用量依模型而異，尤其是篩選過的提示可能更少。成本取決於部署定價；此處沒固定金額估計。所有請求限制假設無手動重跑。執行各命令後立即檢查 `$LASTEXITCODE`；非零代表執行不成功。

## 疑難排解

- **缺少端點 / 401 / 403：** 設定啟動流程端點，驗證本地 Azure 登入及資源範圍角色，並檢查是否有無意的身份環境覆寫。
- **400 / 404：** 確認部署存在且支持帶無推理努力的聊天回答。使用 HTTPS 資源根 URL 或 `/openai/v1`，而非舊版部署 URL。普通 400 錯誤是技術失敗非安全阻塞。
- **429：** 協調共享 RPM 及標記配額後再嘗試。範例故意不自動重試。
- **`Incomplete chat response: length`：** 輸出已達完成限制。檢查回答與提示後再增加限制及其預算；不應當將截斷執行視為成功。
- **檔案或標準輸入錯誤：** 從支援目錄啟動或傳入明確的文檔路徑。提供非空白的讀取者提問。完成可以在 EOF 或 `exit` 正常結束。
- **編譯錯誤：** 確認 Java 21 或以上，再執行 `mvn -B -ntp clean test`。在 PowerShell 中，包含點屬性的 Maven 參數整串加引號，例如 `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`。

## 接下來的步驟

繼續閱讀 [第 4 章：實用範例](../04-PracticalSamples/README.md)。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->