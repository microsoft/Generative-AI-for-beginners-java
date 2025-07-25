<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:01:33+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "ja"
}
-->
# Foundry Local Spring Boot チュートリアル

## 目次

- [前提条件](../../../../04-PracticalSamples/foundrylocal)
- [プロジェクト概要](../../../../04-PracticalSamples/foundrylocal)
- [コードの理解](../../../../04-PracticalSamples/foundrylocal)
  - [1. アプリケーション設定 (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. メインアプリケーションクラス (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AIサービス層 (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. プロジェクト依存関係 (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [全体の仕組み](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local のセットアップ](../../../../04-PracticalSamples/foundrylocal)
- [アプリケーションの実行](../../../../04-PracticalSamples/foundrylocal)
- [期待される出力](../../../../04-PracticalSamples/foundrylocal)
- [次のステップ](../../../../04-PracticalSamples/foundrylocal)
- [トラブルシューティング](../../../../04-PracticalSamples/foundrylocal)

## 前提条件

このチュートリアルを始める前に、以下を準備してください：

- **Java 21以上** がシステムにインストールされていること
- **Maven 3.6以上** を使用してプロジェクトをビルドできること
- **Foundry Local** がインストールされ、実行中であること

### **Foundry Local のインストール:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## プロジェクト概要

このプロジェクトは、以下の4つの主要コンポーネントで構成されています：

1. **Application.java** - Spring Boot アプリケーションのエントリーポイント
2. **FoundryLocalService.java** - AIとの通信を処理するサービス層
3. **application.properties** - Foundry Local 接続の設定
4. **pom.xml** - Maven 依存関係とプロジェクト設定

## コードの理解

### 1. アプリケーション設定 (application.properties)

**ファイル:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**これが行うこと:**
- **base-url**: Foundry Local が実行されている場所を指定（デフォルトポートは5273）
- **model**: テキスト生成に使用するAIモデルの名前を指定

**重要なポイント:** Spring Boot はこれらのプロパティを自動的に読み込み、`@Value` アノテーションを使用してアプリケーション内で利用可能にします。

### 2. メインアプリケーションクラス (Application.java)

**ファイル:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**これが行うこと:**
- `@SpringBootApplication` は Spring Boot の自動構成を有効化
- `WebApplicationType.NONE` は、このアプリがコマンドラインアプリであり、Webサーバーではないことを指定
- メインメソッドが Spring アプリケーションを起動

**デモランナー:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**これが行うこと:**
- `@Bean` は Spring が管理するコンポーネントを作成
- `CommandLineRunner` は Spring Boot 起動後にコードを実行
- `foundryLocalService` は Spring によって自動的に注入される（依存性注入）
- テストメッセージをAIに送信し、レスポンスを表示

### 3. AIサービス層 (FoundryLocalService.java)

**ファイル:** `src/main/java/com/example/FoundryLocalService.java`

#### 設定の注入:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**これが行うこと:**
- `@Service` は、このクラスがビジネスロジックを提供することを Spring に通知
- `@Value` は application.properties から設定値を注入
- `:default-value` 構文は、プロパティが設定されていない場合のフォールバック値を提供

#### クライアントの初期化:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**これが行うこと:**
- `@PostConstruct` は、Spring がサービスを作成した後にこのメソッドを実行
- OpenAI クライアントを作成し、ローカルの Foundry Local インスタンスを指すように設定
- `/v1` パスは OpenAI API 互換性のために必要
- APIキーは「未使用」と設定されており、ローカル開発では認証が不要

#### チャットメソッド:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**これが行うこと:**
- **ChatCompletionCreateParams**: AIリクエストを設定
  - `model`: 使用するAIモデルを指定
  - `addUserMessage`: 会話にユーザーメッセージを追加
  - `maxCompletionTokens`: レスポンスの長さを制限（リソース節約）
  - `temperature`: ランダム性を制御（0.0 = 決定論的、1.0 = 創造的）
- **APIコール**: リクエストを Foundry Local に送信
- **レスポンス処理**: AIのテキストレスポンスを安全に抽出
- **エラーハンドリング**: 例外をキャッチし、わかりやすいエラーメッセージを追加

### 4. プロジェクト依存関係 (pom.xml)

**主要な依存関係:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**これが行うこと:**
- **spring-boot-starter**: Spring Boot の基本機能を提供
- **openai-java**: OpenAI Java SDK を使用してAPI通信を実現
- **jackson-databind**: APIコールのためのJSONシリアライズ/デシリアライズを処理

## 全体の仕組み

アプリケーションを実行すると、以下の流れで動作します：

1. **起動**: Spring Boot が起動し、`application.properties` を読み込む
2. **サービス作成**: Spring が `FoundryLocalService` を作成し、設定値を注入
3. **クライアント設定**: `@PostConstruct` が OpenAI クライアントを初期化
4. **デモ実行**: `CommandLineRunner` が起動後に実行
5. **AIコール**: デモがテストメッセージを `foundryLocalService.chat()` に送信
6. **APIリクエスト**: サービスが OpenAI 互換リクエストを Foundry Local に送信
7. **レスポンス処理**: サービスがAIのレスポンスを抽出して返す
8. **表示**: アプリケーションがレスポンスを表示して終了

## Foundry Local のセットアップ

Foundry Local をセットアップするには、以下の手順に従ってください：

1. **Foundry Local をインストール**: [前提条件](../../../../04-PracticalSamples/foundrylocal) セクションの手順を参照
2. **使用するAIモデルをダウンロード**: 例えば、`phi-3.5-mini` を以下のコマンドでダウンロード
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties ファイルを設定**: Foundry Local の設定に合わせて、特にポートやモデルが異なる場合は調整

## アプリケーションの実行

### ステップ1: Foundry Local を起動
```bash
foundry model run phi-3.5-mini
```

### ステップ2: アプリケーションをビルドして実行
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## 期待される出力

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## 次のステップ

さらなる例については、[Chapter 04: Practical samples](../README.md) を参照してください。

## トラブルシューティング

### よくある問題

**"Connection refused" または "Service unavailable"**
- Foundry Local が実行中であることを確認: `foundry model list`
- サービスがポート5273で動作していることを確認: `application.properties` をチェック
- Foundry Local を再起動してみる: `foundry model run phi-3.5-mini`

**"Model not found" エラー**
- 利用可能なモデルを確認: `foundry model list`
- `application.properties` のモデル名を正確に一致させる
- 必要に応じてモデルをダウンロード: `foundry model run phi-3.5-mini`

**Maven のコンパイルエラー**
- Java 21以上を確認: `java -version`
- クリーンビルドを実行: `mvn clean compile`
- 依存関係のダウンロードのためにインターネット接続を確認

**アプリケーションは起動するが出力がない**
- Foundry Local が応答していることを確認: ブラウザで `http://localhost:5273` を開く
- アプリケーションログで具体的なエラーメッセージを確認
- モデルが完全にロードされ、準備ができていることを確認

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当社は責任を負いません。