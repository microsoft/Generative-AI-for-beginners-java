<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:02:21+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ja"
}
-->
# ペットストーリー生成チュートリアル（初心者向け）

## 目次

- [前提条件](../../../../04-PracticalSamples/petstory)
- [プロジェクト構造の理解](../../../../04-PracticalSamples/petstory)
- [コアコンポーネントの説明](../../../../04-PracticalSamples/petstory)
  - [1. メインアプリケーション](../../../../04-PracticalSamples/petstory)
  - [2. Webコントローラー](../../../../04-PracticalSamples/petstory)
  - [3. ストーリーサービス](../../../../04-PracticalSamples/petstory)
  - [4. Webテンプレート](../../../../04-PracticalSamples/petstory)
  - [5. 設定](../../../../04-PracticalSamples/petstory)
- [アプリケーションの実行](../../../../04-PracticalSamples/petstory)
- [全体の仕組み](../../../../04-PracticalSamples/petstory)
- [AI統合の理解](../../../../04-PracticalSamples/petstory)
- [次のステップ](../../../../04-PracticalSamples/petstory)

## 前提条件

始める前に、以下を準備してください：
- Java 21以上がインストールされていること
- 依存関係管理のためのMaven
- `models:read`スコープを持つ個人アクセストークン（PAT）が設定されたGitHubアカウント
- Java、Spring Boot、Web開発の基本的な知識

## プロジェクト構造の理解

ペットストーリープロジェクトには、いくつかの重要なファイルがあります：

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI story generation service
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## コアコンポーネントの説明

### 1. メインアプリケーション

**ファイル名:** `PetStoryApplication.java`

これはSpring Bootアプリケーションのエントリーポイントです：

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**このファイルの役割:**
- `@SpringBootApplication`アノテーションにより、自動構成とコンポーネントスキャンを有効化
- ポート8080で埋め込みWebサーバー（Tomcat）を起動
- 必要なSpringのBeanやサービスを自動的に作成

### 2. Webコントローラー

**ファイル名:** `PetController.java`

これはすべてのWebリクエストとユーザーインタラクションを処理します：

```java
@Controller
public class PetController {
    
    private final StoryService storyService;
    
    public PetController(StoryService storyService) {
        this.storyService = storyService;
    }
    
    @GetMapping("/")
    public String index() {
        return "index";  // Returns index.html template
    }
    
    @PostMapping("/generate-story")
    public String generateStory(@RequestParam("description") String description, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        
        // Input validation
        if (description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please provide a description.");
            return "redirect:/";
        }
        
        // Sanitize input for security
        String sanitizedDescription = sanitizeInput(description);
        
        // Generate story with error handling
        try {
            String story = storyService.generateStory(sanitizedDescription);
            model.addAttribute("caption", sanitizedDescription);
            model.addAttribute("story", story);
            return "result";  // Returns result.html template
            
        } catch (Exception e) {
            // Use fallback story if AI fails
            String fallbackStory = generateFallbackStory(sanitizedDescription);
            model.addAttribute("story", fallbackStory);
            return "result";
        }
    }
    
    private String sanitizeInput(String input) {
        return input.replaceAll("[<>\"'&]", "")  // Remove dangerous characters
                   .trim()
                   .substring(0, Math.min(input.length(), 500));  // Limit length
    }
}
```

**主な特徴:**

1. **ルート処理**: `@GetMapping("/")`でアップロードフォームを表示し、`@PostMapping("/generate-story")`で送信内容を処理
2. **入力検証**: 空の説明や長さ制限をチェック
3. **セキュリティ**: ユーザー入力をサニタイズしてXSS攻撃を防止
4. **エラーハンドリング**: AIサービスが失敗した場合にフォールバックストーリーを提供
5. **モデルバインディング**: Springの`Model`を使用してHTMLテンプレートにデータを渡す

**フォールバックシステム:**
コントローラーには、AIサービスが利用できない場合に使用される事前作成済みのストーリーテンプレートが含まれています：

```java
private String generateFallbackStory(String description) {
    String[] storyTemplates = {
        "Meet the most wonderful pet in the world – a furry ball of energy...",
        "Once upon a time, there lived a remarkable pet whose heart was as big...",
        "In a cozy home filled with love, there lived an extraordinary pet..."
    };
    
    // Use description hash for consistent responses
    int index = Math.abs(description.hashCode() % storyTemplates.length);
    return storyTemplates[index];
}
```

### 3. ストーリーサービス

**ファイル名:** `StoryService.java`

このサービスはGitHub Modelsと通信してストーリーを生成します：

```java
@Service
public class StoryService {
    
    private final OpenAIClient openAIClient;
    private final String modelName;
    
    public StoryService(@Value("${github.models.endpoint}") String endpoint,
                       @Value("${github.models.model}") String modelName) {
        
        String githubToken = System.getenv("GITHUB_TOKEN");
        if (githubToken == null || githubToken.isBlank()) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable must be set");
        }
        
        // Create OpenAI client configured for GitHub Models
        this.openAIClient = OpenAIOkHttpClient.builder()
                .baseUrl(endpoint)
                .apiKey(githubToken)
                .build();
    }
    
    public String generateStory(String description) {
        String systemPrompt = "You are a creative storyteller who writes fun, " +
                             "family-friendly short stories about pets. " +
                             "Keep stories under 500 words and appropriate for all ages.";
        
        String userPrompt = "Write a fun short story about a pet described as: " + description;
        
        // Configure the AI request
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(modelName)
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(500)  // Limit response length
                .temperature(0.8)          // Control creativity (0.0-1.0)
                .build();
        
        // Send request and get response
        ChatCompletion response = openAIClient.chat().completions().create(params);
        
        return response.choices().get(0).message().content().orElse("");
    }
}
```

**主な構成要素:**

1. **OpenAIクライアント**: GitHub Models用に構成された公式OpenAI Java SDKを使用
2. **システムプロンプト**: 家族向けのペットストーリーを書くようAIの動作を設定
3. **ユーザープロンプト**: 説明に基づいて具体的なストーリーを書くようAIに指示
4. **パラメータ**: ストーリーの長さや創造性レベルを制御
5. **エラーハンドリング**: コントローラーがキャッチして処理する例外をスロー

### 4. Webテンプレート

**ファイル名:** `index.html`（アップロードフォーム）

ユーザーがペットを説明するメインページ：

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Generator</title>
    <!-- CSS styling -->
</head>
<body>
    <div class="container">
        <h1>Pet Story Generator</h1>
        <p>Describe your pet and we'll create a fun story about them!</p>
        
        <!-- Error message display -->
        <div th:if="${error}" class="error" th:text="${error}"></div>
        
        <!-- Story generation form -->
        <form action="/generate-story" method="post">
            <div class="form-group">
                <label for="description">Describe your pet:</label>
                <textarea id="description" name="description" 
                         placeholder="Tell us about your pet - what they look like, their personality, favorite activities..."
                         maxlength="1000" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Generate Story</button>
        </form>
        
        <!-- Image upload section with client-side processing -->
        <div class="upload-section">
            <h2>Or Upload a Photo</h2>
            <input type="file" id="imageInput" accept="image/*" />
            <button onclick="analyzeImage()" class="upload-btn">Analyze Image</button>
        </div>
        
        <script>
            // Client-side image analysis using Transformers.js
            async function analyzeImage() {
                // Image processing code here
                // Generates description automatically from uploaded image
            }
        </script>
    </div>
</body>
</html>
```

**ファイル名:** `result.html`（ストーリー表示）

生成されたストーリーを表示：

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

**テンプレートの特徴:**

1. **Thymeleaf統合**: 動的コンテンツのために`th:`属性を使用
2. **レスポンシブデザイン**: モバイルとデスクトップ向けのCSSスタイリング
3. **エラーハンドリング**: ユーザーに検証エラーを表示
4. **クライアントサイド処理**: 画像解析のためのJavaScript（Transformers.jsを使用）

### 5. 設定

**ファイル名:** `application.properties`

アプリケーションの設定内容：

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# GitHub Models configuration
github.models.endpoint=https://models.github.ai/inference
github.models.model=openai/gpt-4.1-nano
```

**設定の説明:**

1. **ファイルアップロード**: 最大10MBの画像を許可
2. **ログ設定**: 実行中に記録される情報を制御
3. **GitHub Models**: 使用するAIモデルとエンドポイントを指定
4. **セキュリティ**: 機密情報が露出しないようエラーハンドリングを設定

## アプリケーションの実行

### ステップ1: GitHubトークンを設定

まず、GitHubトークンを環境変数として設定します：

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

**これが必要な理由:**
- GitHub ModelsはAIモデルへのアクセスに認証を必要とする
- 環境変数を使用することで、機密トークンをソースコードから分離
- `models:read`スコープによりAI推論へのアクセスを提供

### ステップ2: ビルドと実行

プロジェクトディレクトリに移動：
```bash
cd 04-PracticalSamples/petstory
```

アプリケーションをビルド：
```bash
mvn clean compile
```

サーバーを起動：
```bash
mvn spring-boot:run
```

アプリケーションは`http://localhost:8080`で起動します。

### ステップ3: アプリケーションをテスト

1. **ブラウザで** `http://localhost:8080` を開く
2. **ペットを説明**（例: "ボール遊びが大好きな元気なゴールデンレトリバー"）
3. **「ストーリーを生成」ボタンをクリック**してAI生成ストーリーを取得
4. **または**、ペットの画像をアップロードして説明を自動生成
5. **生成されたストーリーを表示**して楽しむ

## 全体の仕組み

ペットストーリーを生成する際の全体の流れ：

1. **ユーザー入力**: Webフォームでペットを説明
2. **フォーム送信**: ブラウザがPOSTリクエストを`/generate-story`に送信
3. **コントローラー処理**: `PetController`が入力を検証しサニタイズ
4. **AIサービス呼び出し**: `StoryService`がGitHub Models APIにリクエストを送信
5. **ストーリー生成**: AIが説明に基づいて創造的なストーリーを生成
6. **レスポンス処理**: コントローラーがストーリーを受け取りモデルに追加
7. **テンプレートレンダリング**: Thymeleafが`result.html`をストーリー付きでレンダリング
8. **表示**: ユーザーがブラウザで生成されたストーリーを確認

**エラーハンドリングの流れ:**
AIサービスが失敗した場合：
1. コントローラーが例外をキャッチ
2. 事前作成済みのテンプレートを使用してフォールバックストーリーを生成
3. AIが利用できない旨のメッセージとともにフォールバックストーリーを表示
4. ユーザー体験を損なわないよう配慮

## AI統合の理解

### GitHub Models API
このアプリケーションはGitHub Modelsを使用し、さまざまなAIモデルに無料でアクセスします：

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### プロンプトエンジニアリング
サービスは慎重に設計されたプロンプトを使用して良い結果を得ます：

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### レスポンス処理
AIのレスポンスを抽出し、検証します：

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## 次のステップ

さらなる例については、[Chapter 04: Practical samples](../README.md) を参照してください。

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確さが含まれる可能性があることをご承知おきください。元の言語で記載された原文が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤認について、当方は一切の責任を負いません。