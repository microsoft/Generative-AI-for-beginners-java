<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:00:55+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "ja"
}
-->
# コア生成AI技術チュートリアル

## 目次

- [前提条件](../../../03-CoreGenerativeAITechniques)
- [はじめに](../../../03-CoreGenerativeAITechniques)
  - [ステップ1: 環境変数を設定する](../../../03-CoreGenerativeAITechniques)
  - [ステップ2: サンプルディレクトリに移動する](../../../03-CoreGenerativeAITechniques)
- [チュートリアル1: LLMの補完とチャット](../../../03-CoreGenerativeAITechniques)
- [チュートリアル2: 関数呼び出し](../../../03-CoreGenerativeAITechniques)
- [チュートリアル3: RAG（検索強化生成）](../../../03-CoreGenerativeAITechniques)
- [チュートリアル4: 責任あるAI](../../../03-CoreGenerativeAITechniques)
- [サンプル全体に共通するパターン](../../../03-CoreGenerativeAITechniques)
- [次のステップ](../../../03-CoreGenerativeAITechniques)
- [トラブルシューティング](../../../03-CoreGenerativeAITechniques)
  - [よくある問題](../../../03-CoreGenerativeAITechniques)

## 概要

このチュートリアルでは、JavaとGitHub Modelsを使用してコア生成AI技術の実践的な例を提供します。大規模言語モデル（LLM）との対話、関数呼び出しの実装、検索強化生成（RAG）の使用、責任あるAIの実践方法を学びます。

## 前提条件

開始する前に、以下を準備してください：
- Java 21以上がインストールされていること
- 依存関係管理のためのMaven
- 個人アクセストークン（PAT）を持つGitHubアカウント

## はじめに

### ステップ1: 環境変数を設定する

まず、GitHubトークンを環境変数として設定する必要があります。このトークンを使用して、GitHub Modelsに無料でアクセスできます。

**Windows（コマンドプロンプト）:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows（PowerShell）:**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### ステップ2: サンプルディレクトリに移動する

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## チュートリアル1: LLMの補完とチャット

**ファイル:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### この例で学べること

この例では、GitHub Modelsを使用したクライアント初期化、システムおよびユーザープロンプトのメッセージ構造パターン、メッセージ履歴の蓄積による会話状態管理、応答の長さや創造性レベルを制御するためのパラメータ調整など、LLMとの基本的な対話の仕組みを示します。

### 主なコードの概念

#### 1. クライアントのセットアップ
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

GitHub Modelsにトークンを使用して接続を作成します。

#### 2. シンプルな補完
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

#### 3. 会話の記憶
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AIは、後続のリクエストに以前のメッセージを含めた場合にのみ、それらを記憶します。

### 実行方法
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### 実行時の挙動

1. **シンプルな補完**: システムプロンプトの指示に基づいて、AIがJavaの質問に回答します。
2. **マルチターンチャット**: AIが複数の質問にわたって文脈を保持します。
3. **インタラクティブチャット**: AIと実際の会話を行うことができます。

## チュートリアル2: 関数呼び出し

**ファイル:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### この例で学べること

関数呼び出しでは、AIモデルが自然言語リクエストを解析し、JSONスキーマ定義を使用して適切なパラメータで必要な関数呼び出しを決定し、返された結果を処理して文脈に応じた応答を生成します。実際の関数実行は、セキュリティと信頼性のために開発者の管理下にあります。

### 主なコードの概念

#### 1. 関数定義
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

AIに利用可能な関数とその使用方法を伝えます。

#### 2. 関数実行フロー
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

#### 3. 関数の実装
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

### 実行方法
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### 実行時の挙動

1. **天気関数**: AIがシアトルの天気データを要求し、提供されたデータを基に応答を整形します。
2. **計算関数**: AIが計算（240の15%）を要求し、計算結果を説明します。

## チュートリアル3: RAG（検索強化生成）

**ファイル:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### この例で学べること

検索強化生成（RAG）は、外部ドキュメントのコンテキストをAIプロンプトに注入することで、特定の知識ソースに基づいた正確な回答を提供します。これにより、AIがトレーニングデータに依存せず、戦略的なプロンプト設計を通じてユーザーのクエリと信頼できる情報ソースの間に明確な境界を維持します。

### 主なコードの概念

#### 1. ドキュメントの読み込み
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. コンテキストの注入
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

トリプルクォートを使用して、AIがコンテキストと質問を区別できるようにします。

#### 3. 安全な応答処理
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API応答を常に検証してクラッシュを防ぎます。

### 実行方法
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### 実行時の挙動

1. プログラムが`document.txt`を読み込みます（GitHub Modelsに関する情報を含む）。
2. ドキュメントに関する質問をします。
3. AIは一般的な知識ではなく、ドキュメントの内容に基づいて回答します。

以下を試してください: 「GitHub Modelsとは何ですか？」 vs 「天気はどうですか？」

## チュートリアル4: 責任あるAI

**ファイル:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### この例で学べること

責任あるAIの例では、AIアプリケーションにおける安全対策の重要性を示します。ヘイトスピーチ、嫌がらせ、自傷行為、性的コンテンツ、暴力などの有害なコンテンツカテゴリを検出する安全フィルターを実演し、適切な例外処理、ユーザーフィードバックメカニズム、フォールバック応答戦略を通じて、コンテンツポリシー違反を優雅に処理する方法を示します。

### 主なコードの概念

#### 1. 安全性テストフレームワーク
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

#### 2. テストされる安全性カテゴリ
- 暴力/危害の指示
- ヘイトスピーチ
- プライバシー侵害
- 医療に関する誤情報
- 違法行為

### 実行方法
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### 実行時の挙動

プログラムはさまざまな有害なプロンプトをテストし、AIの安全システムが以下をどのように処理するかを示します：
1. **危険なリクエストをブロック**（HTTP 400エラー）
2. **安全なコンテンツを通常通り生成**
3. **ユーザーを保護**（有害なAI出力を防止）

## サンプル全体に共通するパターン

### 認証パターン
すべての例で以下のパターンを使用してGitHub Modelsに認証します：

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### エラーハンドリングパターン
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### メッセージ構造パターン
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## 次のステップ

[Chapter 04: 実践的なサンプル](../04-PracticalSamples/README.md)

## トラブルシューティング

### よくある問題

**"GITHUB_TOKEN not set"**
- 環境変数を設定したことを確認してください。
- トークンに`models:read`スコープがあることを確認してください。

**"No response from API"**
- インターネット接続を確認してください。
- トークンが有効であることを確認してください。
- レート制限に達していないか確認してください。

**Mavenのコンパイルエラー**
- Java 21以上がインストールされていることを確認してください。
- `mvn clean compile`を実行して依存関係を更新してください。

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当方は責任を負いません。