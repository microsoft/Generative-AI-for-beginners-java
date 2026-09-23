# 初心者向けペットストーリー生成チュートリアル

ペットの写真をアップロードし、GPT-5.6 Lunaで分析し、その結果の説明をもとにストーリーを生成します。両方のモデルリクエストは `reasoning_effort: none` を使用します。

| コンポーネント | バージョン |
| --- | --- |
| Java | 21以上 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## 目次

- [前提条件](#前提条件)
- [プロジェクト構成の理解](#プロジェクト構成の理解)
- [主要コンポーネントの説明](#主要コンポーネントの説明)
  - [1. メインアプリケーション](#1-メインアプリケーション)
  - [2. Webコントローラー](#2-webコントローラー)
  - [3. ストーリーサービス](#3-ストーリーサービス)
  - [4. Webテンプレート](#4-webテンプレート)
  - [5. 設定](#5-設定)
- [アプリケーションの実行](#アプリケーションの実行)
- [オフラインテスト](#オフラインテスト)
- [全体の動作](#全体の動作)
- [AI統合の理解](#ai統合の理解)
- [次のステップ](#次のステップ)

## 前提条件

はじめる前に、次のものが揃っていることを確認してください:
- Java 21以上がインストールされていること
- 依存関係管理にMavenを使用
- `gpt-5.6-luna` という名前のAzure AI Foundry上のGPT-5.6 Lunaデプロイメント、またはそれを指す `AZURE_OPENAI_DEPLOYMENT` のオーバーライド。プロビジョニングとキー不要認証のための `az login` については [第2章](../../02-SetupDevEnvironment/getting-started-azure-openai.md) を参照。デプロイメントは画像入力と `reasoning_effort: none` をサポートしている必要があります。
- Java, Spring Boot, Web開発の基本的な理解

## プロジェクト構成の理解

ペットストーリープロジェクトには重要なファイルがいくつかあります:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## 主要コンポーネントの説明

### 1. メインアプリケーション

**ファイル:** `PetStoryApplication.java`

これはSpring Bootアプリケーションのエントリポイントです:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**動作内容:**
- `@SpringBootApplication` アノテーションが自動設定およびコンポーネントスキャンを有効化
- 組み込みウェブサーバー（Tomcat）をポート8080で起動
- 必要なSpringビーンとサービスを自動的に作成

### 2. Webコントローラー

**ファイル:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| エンドポイント | リクエスト | 成功時レスポンス |
| --- | --- | --- |
| `GET /` | ボディなし | CSRFトークン付きのHTMLアップロードフォーム |
| `POST /analyze-image` | `multipart/form-data`、ファイルフィールド名 `image` | JSON: `{"description":"遊び好きなペット..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`、フィールド名 `description` | 説明と生成されたストーリーを含むHTML結果ページ |

両方のPOSTエンドポイントは、`GET /`から取得したセッションクッキーとCSRFトークンが必要です。アップロードスクリプトは隠し `_csrf` 値を `X-CSRF-TOKEN` ヘッダーで送信し、ストーリー送信は `_csrf` フォームフィールドとして送信します。APIクライアントはリクエスト間でクッキーを保持する必要があります。これらはJSONリクエストではなくフォームエンドポイントです。

説明は空でなく、1000文字以下である必要があります。コントローラーは説明をトリムし、`<`、`>`、ダブルクォート、アポストロフィ、`&`を除去してサービスに渡します。結果テンプレートではモデル出力も `th:text` でエスケープしています。

画像検証失敗はHTTP 400と`error`フィールドを返し、モデル失敗はHTTP 502と`error`フィールドを返して`description`は含みません。無効な説明やモデル失敗は`/`へリダイレクトしエラーメッセージを表示します。必須フィールドの欠如はHTTP 400、CSRFトークンの欠如や無効はHTTP 403です。代替の説明やストーリーを成功したAI結果として提示しません。

### 3. ストーリーサービス

**ファイル:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

公式OpenAI Java SDK 4.63.1がAzure AI FoundryのOpenAI互換チャット補完APIを呼び出します。Azure Identity 1.18.6は`DefaultAzureCredential`経由でMicrosoft Entraベアラートークンを提供し、APIキーは不要です。

| 操作 | 入力 | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | アップロードされたMIMEタイプでエンコードされたベース64データURLの画像バイト列 | 300 |
| `generateStory` | ユーザーメッセージに含まれるペット説明 | 800 |

両リクエストとも設定されたデプロイメントを使用し、デフォルトは`gpt-5.6-luna`、明示的に`ReasoningEffort.NONE`（`reasoning_effort: none`）を設定します。`temperature`や旧式の`max_tokens`パラメーターは送信しません。

画像解析はJPEG, PNG, GIF, WebPを受け入れ、空の画像や10MB超のファイルを拒否し、結果の説明は1000文字以内に制限します。ストーリープロンプトは家族向けの短編を要求します。空の選択肢や空のモデル出力はエラーであり、失敗は元の原因を保持してサーバー側で診断します。SDKクライアントはアプリ終了時にクローズされます。

### 4. Webテンプレート

**ファイル:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) （アップロードフォーム）

ページは説明テキストエリアではなく、写真選択から始まります。<strong>画像を分析</strong>は選択した写真をプレビューし、`/analyze-image`へ送信します。成功レスポンスは説明を表示し、隠し`description`フィールドを埋め、<strong>ストーリーを生成</strong>ボタンを表示します。そのボタンは既存フォームを`/generate-story`に送信します。

ブラウザ側のモデルダウンロードやCDN依存はありません。画像解析は設定済みAzureデプロイメントのサーバー側で行います。失敗は表示され、偽の説明でストーリー生成はできません。ファイルを変えると前回の解析はクリアされます。

**ファイル:** `result.html` （ストーリー表示）

生成されたストーリーを表示:

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

1. **Thymeleaf統合**：動的コンテンツ用の `th:` 属性を使用
2. <strong>レスポンシブデザイン</strong>：モバイルとデスクトップ用のCSSスタイリング
3. <strong>エラーハンドリング</strong>：検証エラーをユーザーに表示
4. <strong>アップロード処理</strong>：JavaScriptで写真をプレビューし、CSRF保護付きmultipartリクエストを送信、返された説明を表示

### 5. 設定

**ファイル:** `application.properties`

アプリケーションの設定内容:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**設定の説明:**

1. <strong>ファイルアップロード</strong>：ファイルとmultipart全体が10MBまでに制限。マルチパートヘッダー分の余裕を考え、写真はこの制限内に収める
2. <strong>ログ記録</strong>：実行中のログ出力を制御
3. **Azure AI Foundry**：使用するエンドポイントとモデルデプロイメントを指定（キー不要認証）
4. <strong>セキュリティ</strong>：CSRF保護は有効のまま、モデル診断はサーバーログに出力、コントローラーは一般的なモデル失敗メッセージを表示

## アプリケーションの実行

### ステップ1: サインインとエンドポイント設定

認証はキー不要（Microsoft Entra ID）なのでAPIキーは不要です。サインインしFoundryのエンドポイントを設定してください:

**Windows（コマンドプロンプト）：**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows（PowerShell）：**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**必要な理由:**
- Azure AI FoundryはMicrosoft Entra IDを使って推論リクエストを認証
- キー不要認証はソースコードや環境に秘密情報を残さない
- アカウントには該当リソースの **Cognitive Services OpenAI User** ロールが必要

デフォルトのデプロイメント名は`gpt-5.6-luna`です。もし別の名前のGPT-5.6 Lunaを使う場合は、アプリ起動前に同じ端末で `AZURE_OPENAI_DEPLOYMENT` を設定してください。画像解析とストーリー生成の両方でこの設定を使用します。

### ステップ2: ビルドと実行

プロジェクトディレクトリに移動します:
```bash
cd 04-PracticalSamples/petstory
```

スタンドアロンの実行可能JARをビルドし、すべてのオフラインテストを実行:
```bash
mvn clean package
```

サーバーを起動します:
```bash
mvn spring-boot:run
```

アプリは `http://localhost:8080` で起動します。

代わりに任意の空きポートでパッケージ済みJARを起動可能で、例えば:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

この場合は `http://localhost:8083/` を開いてください。`/analyze-image` と `/generate-story` は選択したポートで利用可能です。

### ステップ3: アプリケーションのテスト

1. ブラウザで `http://localhost:8080` を開く
2. JPEG、PNG、GIF、WebP形式の10MB以下の鮮明なペット写真を選択
3. 「画像を分析」をクリックし、ペットの説明を待つ
4. 成功した解析後、「ストーリーを生成」をクリック
5. ストーリーを表示し、結果ページのリンクでアップロードフォームに戻る

写真からストーリーへの正常なフローはボタンごとにモデルを2回呼び出します。ライブ推論はデプロイメントのクォータを消費し課金される場合があります。レート制限があるデプロイメントを共有する場合は連続してスモークテストを実行してください。ホームページをロードしてもモデルは呼び出しされません。

## オフラインテスト

サンプルディレクトリから次のコマンドを実行:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) はループバックHTTPフィクスチャで実際のOpenAI SDKリクエストをキャプチャします。両リクエストのデプロイメント、`reasoning_effort: none`、トークン制限、画像ペイロード、入力検証、空応答、上流エラーをチェックします。

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) はMockMvcとモックのモデルサービスでThymeleafページのレンダリング、アップロード契約、CSRF、検証、出力エスケープ、可視的失敗をテストします。これらのテストはAzure資格情報不要で、課金されるAzure推論も呼び出しません。MavenはSurefireレポートを `target/surefire-reports` に出力します。

## 全体の動作

ペットストーリー生成時の完全なフローは次の通りです:

1. <strong>写真選択</strong>：アップロードフォームでペット画像を選ぶ
2. <strong>画像アップロード</strong>：「画像を分析」がCSRFヘッダー付のmultipart POSTを`/analyze-image`へ送信
3. <strong>画像解析</strong>：`StoryService`が理由づけを`none`に設定し画像をGPT-5.6 Lunaに送信
4. <strong>説明表示</strong>：ブラウザが返された説明を表示しフォームに保存
5. <strong>ストーリー送信</strong>：「ストーリーを生成」が`description`と`_csrf`を`/generate-story`へPOST
6. <strong>ストーリー生成</strong>：コントローラーが説明を検証し、同じデプロイメントに理由づけ`none`で呼び出し
7. <strong>テンプレートレンダリング</strong>：Thymeleafで説明とストーリーを結果ページにエスケープ表示

**エラーハンドリングフロー:**
モデル失敗時はサーバーに原因ログが残ります。画像解析はHTTP 502を返し、ブラウザには「ストーリーを生成」を表示せずエラーだけ表示。ストーリー生成はフォームへリダイレクトしエラーメッセージを出します。どちらの経路も事前作成結果と静かに置き換えを行いません。

## AI統合の理解

### Azure AI Foundry（キー不要）
サービスはSDKをリソースの `/openai/v1/` エンドポイントで構成。`DefaultAzureCredential` と `AuthenticationUtil.getBearerTokenSupplier` がMicrosoft Entraトークンを `https://ai.azure.com/.default` 用に供給。ローカル開発はAzure CLIサインインを利用でき、Azureホストのアプリは必要なリソース権限付きのマネージドIDを使用可能。

### プロンプトエンジニアリング
画像解析は観察可能なペットの特徴を短い段落で要求し、画像内テキストを命令ではなくデータとして扱うようモデルに指示。ストーリー生成は返された説明を用い、別途家族向け短編を書かせる。どちらも理由づけや温度パラメーター変更は無効。

### レスポンス処理
共通レスポンスハンドラーは選択肢の欠如や空白のみの内容を拒否し、有効な内容をトリムし上流の失敗は維持。画像説明は後続のストーリーフォームに収めるため1000文字に制限。元のモデル失敗はユーザーには表示せず診断用に保持。

## 次のステップ

さらなる例は [第04章: 実践的サンプル集](../README.md) を参照

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類は AI 翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知おきください。原文の原語版が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->