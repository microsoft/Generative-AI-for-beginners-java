<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T10:56:23+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "hk"
}
-->
# 核心生成式 AI 技術教學

## 目錄

- [先決條件](../../../03-CoreGenerativeAITechniques)
- [開始使用](../../../03-CoreGenerativeAITechniques)
  - [步驟 1：設定環境變數](../../../03-CoreGenerativeAITechniques)
  - [步驟 2：進入範例目錄](../../../03-CoreGenerativeAITechniques)
- [教學 1：LLM 完成與聊天](../../../03-CoreGenerativeAITechniques)
- [教學 2：函數呼叫](../../../03-CoreGenerativeAITechniques)
- [教學 3：RAG（檢索增強生成）](../../../03-CoreGenerativeAITechniques)
- [教學 4：負責任的 AI](../../../03-CoreGenerativeAITechniques)
- [範例中的常見模式](../../../03-CoreGenerativeAITechniques)
- [下一步](../../../03-CoreGenerativeAITechniques)
- [故障排除](../../../03-CoreGenerativeAITechniques)
  - [常見問題](../../../03-CoreGenerativeAITechniques)

## 概述

此教學提供使用 Java 和 GitHub Models 的核心生成式 AI 技術的實作範例。你將學習如何與大型語言模型（LLMs）互動、實現函數呼叫、使用檢索增強生成（RAG），以及應用負責任的 AI 實踐。

## 先決條件

開始之前，請確保你已經：
- 安裝 Java 21 或更高版本
- 使用 Maven 進行依賴管理
- 擁有 GitHub 帳戶及個人存取權杖（PAT）

## 開始使用

### 步驟 1：設定環境變數

首先，你需要將 GitHub 權杖設為環境變數。此權杖允許你免費存取 GitHub Models。

**Windows（命令提示字元）：**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows（PowerShell）：**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS：**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### 步驟 2：進入範例目錄

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## 教學 1：LLM 完成與聊天

**檔案：** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### 此範例教學內容

此範例展示如何透過 OpenAI API 與大型語言模型（LLM）互動的核心機制，包括使用 GitHub Models 初始化客戶端、系統與使用者提示的訊息結構模式、透過訊息歷史累積管理對話狀態，以及調整參數以控制回應長度和創意水平。

### 主要程式碼概念

#### 1. 客戶端設定
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

此程式碼使用你的權杖建立與 GitHub Models 的連線。

#### 2. 簡單完成
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. 對話記憶
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI 只有在你於後續請求中包含先前訊息時，才會記住之前的對話。

### 執行範例
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### 執行結果

1. **簡單完成：** AI 在系統提示的指導下回答 Java 問題
2. **多輪聊天：** AI 在多個問題中保持上下文
3. **互動式聊天：** 你可以與 AI 進行真正的對話

## 教學 2：函數呼叫

**檔案：** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### 此範例教學內容

函數呼叫使 AI 模型能透過結構化協議請求執行外部工具和 API。模型分析自然語言請求，根據 JSON Schema 定義確定所需的函數呼叫及適當參數，並處理返回結果以生成上下文回應，而函數的實際執行則由開發者控制以確保安全性和可靠性。

### 主要程式碼概念

#### 1. 函數定義
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

此程式碼告訴 AI 可用的函數及其使用方式。

#### 2. 函數執行流程
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. 函數實作
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### 執行範例
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### 執行結果

1. **天氣函數：** AI 請求西雅圖的天氣資料，你提供後，AI 格式化回應
2. **計算器函數：** AI 請求計算（240 的 15%），你計算後，AI 解釋結果

## 教學 3：RAG（檢索增強生成）

**檔案：** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### 此範例教學內容

檢索增強生成（RAG）結合資訊檢索與語言生成，透過將外部文件內容注入 AI 提示，使模型能基於特定知識來源提供準確答案，而非依賴可能過時或不準確的訓練資料，同時透過策略性提示設計保持使用者問題與權威資訊來源之間的清晰界限。

### 主要程式碼概念

#### 1. 文件載入
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. 上下文注入
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

三重引號幫助 AI 區分上下文與問題。

#### 3. 安全回應處理
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

始終驗證 API 回應以防止程式崩潰。

### 執行範例
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### 執行結果

1. 程式載入 `document.txt`（包含 GitHub Models 的資訊）
2. 你詢問有關文件的問題
3. AI 僅根據文件內容回答，而非其一般知識

試著問：「什麼是 GitHub Models？」與「天氣如何？」

## 教學 4：負責任的 AI

**檔案：** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### 此範例教學內容

負責任的 AI 範例展示在 AI 應用中實施安全措施的重要性。它展示了安全過濾器如何檢測包括仇恨言論、騷擾、自我傷害、色情內容和暴力等有害內容類別，並展示生產環境中的 AI 應用如何透過適當的例外處理、使用者回饋機制和備援回應策略優雅地處理內容政策違規。

### 主要程式碼概念

#### 1. 安全測試框架
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. 測試的安全類別
- 暴力/傷害指令
- 仇恨言論
- 隱私侵犯
- 醫療錯誤資訊
- 非法活動

### 執行範例
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### 執行結果

程式測試各種有害提示，並展示 AI 安全系統如何：
1. **阻止危險請求：** 使用 HTTP 400 錯誤
2. **允許安全內容：** 正常生成
3. **保護使用者：** 避免有害 AI 輸出

## 範例中的常見模式

### 驗證模式
所有範例使用此模式與 GitHub Models 驗證：

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### 錯誤處理模式
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### 訊息結構模式
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## 下一步

[第 04 章：實用範例](../04-PracticalSamples/README.md)

## 故障排除

### 常見問題

**「GITHUB_TOKEN 未設定」**
- 確保你已設定環境變數
- 驗證你的權杖具有 `models:read` 權限

**「API 無回應」**
- 檢查你的網路連線
- 驗證你的權杖是否有效
- 檢查是否達到速率限制

**Maven 編譯錯誤**
- 確保你使用 Java 21 或更高版本
- 執行 `mvn clean compile` 以刷新依賴

**免責聲明**：  
本文件已使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。我們致力於提供準確的翻譯，但請注意，自動翻譯可能包含錯誤或不準確之處。應以原文文件作為權威來源。對於關鍵資訊，建議尋求專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或錯誤解釋概不負責。