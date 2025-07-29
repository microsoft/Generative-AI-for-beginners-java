<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:47:09+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "ja"
}
-->
# Java向け生成AI開発環境のセットアップ

> **クイックスタート**: クラウドで2分でコードを書く - [GitHub Codespacesのセットアップ](../../../02-SetupDevEnvironment)に進んでください - ローカルインストール不要でGitHubモデルを使用可能！

> **Azure OpenAIに興味がありますか？** [Azure OpenAIセットアップガイド](getting-started-azure-openai.md)をご覧ください。新しいAzure OpenAIリソースを作成する手順が記載されています。

## 学べること

- AIアプリケーション向けのJava開発環境をセットアップする方法
- 好みの開発環境を選択して設定する方法（Codespacesを使ったクラウド優先、ローカル開発コンテナ、完全なローカルセットアップ）
- GitHubモデルに接続してセットアップをテストする方法

## 目次

- [学べること](../../../02-SetupDevEnvironment)
- [はじめに](../../../02-SetupDevEnvironment)
- [ステップ1: 開発環境をセットアップする](../../../02-SetupDevEnvironment)
  - [オプションA: GitHub Codespaces (推奨)](../../../02-SetupDevEnvironment)
  - [オプションB: ローカル開発コンテナ](../../../02-SetupDevEnvironment)
  - [オプションC: 既存のローカルインストールを使用する](../../../02-SetupDevEnvironment)
- [ステップ2: GitHub個人アクセストークンを作成する](../../../02-SetupDevEnvironment)
- [ステップ3: セットアップをテストする](../../../02-SetupDevEnvironment)
- [トラブルシューティング](../../../02-SetupDevEnvironment)
- [まとめ](../../../02-SetupDevEnvironment)
- [次のステップ](../../../02-SetupDevEnvironment)

## はじめに

この章では開発環境のセットアップ方法を説明します。**GitHubモデル**を主な例として使用します。これは無料で、GitHubアカウントだけで簡単にセットアップでき、クレジットカード不要で複数のモデルを試すことができます。

**ローカルセットアップは不要！** GitHub Codespacesを使用すれば、ブラウザ内で完全な開発環境をすぐに開始できます。

<img src="./images/models.webp" alt="スクリーンショット: GitHubモデル" width="50%">

このコースでは[**GitHubモデル**](https://github.com/marketplace?type=models)を使用することを推奨します。理由は以下の通りです：
- **無料**で始められる
- **簡単**にGitHubアカウントだけでセットアップ可能
- **クレジットカード不要**
- **複数のモデル**を試せる

> **注意**: このトレーニングで使用するGitHubモデルには以下の無料制限があります：
> - 1分間に15リクエスト（1日150リクエスト）
> - リクエストごとに約8,000語入力、約4,000語出力
> - 同時に5リクエストまで
> 
> 本番環境で使用する場合は、AzureアカウントでAzure AI Foundryモデルにアップグレードしてください。コードを変更する必要はありません。[Azure AI Foundryドキュメント](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models)をご覧ください。

## ステップ1: 開発環境をセットアップする

<a name="quick-start-cloud"></a>

このJava向け生成AIコースのために、必要なツールをすべて備えた事前構成済みの開発コンテナを作成しました。以下の方法からお好みの開発アプローチを選択してください：

### 環境セットアップオプション：

#### オプションA: GitHub Codespaces (推奨)

**2分でコードを開始 - ローカルセットアップ不要！**

1. このリポジトリをGitHubアカウントにフォークします
   > **注意**: 基本設定を編集したい場合は[Devコンテナ設定](../../../.devcontainer/devcontainer.json)をご覧ください。
2. **Code** → **Codespaces**タブ → **...** → **New with options...**をクリックします
3. デフォルトを使用します - これにより、このコース用に作成された**Generative AI Java Development Environment**カスタムDevコンテナ設定が選択されます
4. **Create codespace**をクリックします
5. 環境が準備されるまで約2分待ちます
6. [ステップ2: GitHubトークンを作成する](../../../02-SetupDevEnvironment)に進みます

<img src="./images/codespaces.png" alt="スクリーンショット: Codespacesサブメニュー" width="50%">

<img src="./images/image.png" alt="スクリーンショット: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="スクリーンショット: Create codespace options" width="50%">

> **Codespacesの利点**:
> - ローカルインストール不要
> - ブラウザがあればどのデバイスでも動作
> - すべてのツールと依存関係が事前構成済み
> - 個人アカウントで月60時間無料
> - すべての学習者に一貫した環境を提供

#### オプションB: ローカル開発コンテナ

**Dockerを使用したローカル開発を好む開発者向け**

1. このリポジトリをローカルマシンにフォークしてクローンします
   > **注意**: 基本設定を編集したい場合は[Devコンテナ設定](../../../.devcontainer/devcontainer.json)をご覧ください。
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/)と[VS Code](https://code.visualstudio.com/)をインストールします
3. VS Codeに[Dev Containers拡張機能](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)をインストールします
4. リポジトリフォルダをVS Codeで開きます
5. プロンプトが表示されたら**Reopen in Container**をクリックします（または`Ctrl+Shift+P` → "Dev Containers: Reopen in Container"を使用）
6. コンテナがビルドされて起動するまで待ちます
7. [ステップ2: GitHubトークンを作成する](../../../02-SetupDevEnvironment)に進みます

<img src="./images/devcontainer.png" alt="スクリーンショット: Devコンテナセットアップ" width="50%">

<img src="./images/image-3.png" alt="スクリーンショット: Devコンテナビルド完了" width="50%">

#### オプションC: 既存のローカルインストールを使用する

**既存のJava環境を持つ開発者向け**

必要条件:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com)またはお好みのIDE

手順:
1. このリポジトリをローカルマシンにクローンします
2. プロジェクトをIDEで開きます
3. [ステップ2: GitHubトークンを作成する](../../../02-SetupDevEnvironment)に進みます

> **プロのヒント**: スペックの低いマシンを使用している場合でも、ローカルでVS Codeを使用したい場合はGitHub Codespacesを使用してください！ローカルのVS CodeをクラウドホストされたCodespaceに接続することで、両方の利点を享受できます。

<img src="./images/image-2.png" alt="スクリーンショット: ローカルDevコンテナインスタンス作成済み" width="50%">

## ステップ2: GitHub個人アクセストークンを作成する

1. [GitHub設定](https://github.com/settings/profile)に移動し、プロフィールメニューから**Settings**を選択します。
2. 左側のサイドバーで**Developer settings**をクリックします（通常は一番下にあります）。
3. **Personal access tokens**の下で**Fine-grained tokens**をクリックします（またはこの[リンク](https://github.com/settings/personal-access-tokens)を直接使用してください）。
4. **Generate new token**をクリックします。
5. 「Token name」に説明的な名前を入力します（例: `GenAI-Java-Course-Token`）。
6. 有効期限を設定します（推奨: セキュリティのベストプラクティスとして7日）。
7. 「Resource owner」で自分のユーザーアカウントを選択します。
8. 「Repository access」でGitHubモデルを使用するリポジトリを選択します（必要に応じて「All repositories」を選択）。
9. 「Repository permissions」で**Models**を見つけて**Read and write**に設定します。
10. **Generate token**をクリックします。
11. **今すぐトークンをコピーして保存してください** - 後で再表示することはできません！

> **セキュリティのヒント**: 必要最小限のスコープと最短の実用的な有効期限を使用してアクセストークンを管理してください。

## ステップ3: GitHubモデルの例でセットアップをテストする

開発環境が準備できたら、[`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models)にある例のアプリケーションを使用してGitHubモデルの統合をテストしましょう。

1. 開発環境でターミナルを開きます。
2. GitHubモデルの例に移動します：
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. GitHubトークンを環境変数として設定します：
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. アプリケーションを実行します：
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

以下のような出力が表示されるはずです：
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### 例のコードを理解する

まず、実行した内容を理解しましょう。`examples/github-models`にある例は、OpenAI Java SDKを使用してGitHubモデルに接続します：

**このコードが行うこと**:
- GitHubモデルに個人アクセストークンを使用して**接続**
- AIモデルに「Say Hello World!」という簡単なメッセージを**送信**
- AIの応答を**受信**して表示
- セットアップが正しく動作していることを**検証**

**主要な依存関係**（`pom.xml`内）:
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**メインコード**（`App.java`）:
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## まとめ

素晴らしい！以下の準備が整いました：

- AIモデルアクセスに必要な権限を持つGitHub個人アクセストークンを作成
- Java開発環境を稼働（Codespaces、Devコンテナ、またはローカル環境）
- OpenAI Java SDKを使用してGitHubモデルに接続し、無料でAI開発を開始
- AIモデルと対話する簡単な例で動作確認済み

## 次のステップ

[第3章: 生成AIの基本技術](../03-CoreGenerativeAITechniques/README.md)

## トラブルシューティング

問題が発生しましたか？以下は一般的な問題と解決策です：

- **トークンが機能しない？** 
  - トークン全体を余分なスペースなしでコピーしたことを確認してください
  - トークンが環境変数として正しく設定されていることを確認してください
  - トークンに正しい権限（Models: Read and write）があることを確認してください

- **Mavenが見つからない？** 
  - DevコンテナやCodespacesを使用している場合、Mavenは事前インストールされています
  - ローカルセットアップの場合、Java 21+とMaven 3.9+がインストールされていることを確認してください
  - `mvn --version`を試してインストールを確認してください

- **接続の問題？** 
  - インターネット接続を確認してください
  - GitHubがネットワークからアクセス可能であることを確認してください
  - GitHubモデルのエンドポイントをブロックしているファイアウォールがないことを確認してください

- **Devコンテナが起動しない？** 
  - Docker Desktopが実行中であることを確認してください（ローカル開発の場合）
  - コンテナを再構築してみてください：`Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **アプリケーションのコンパイルエラー？**
  - 正しいディレクトリにいることを確認してください：`02-SetupDevEnvironment/examples/github-models`
  - クリーンと再ビルドを試してください：`mvn clean compile`

> **助けが必要ですか？**: まだ問題が解決しない場合は、リポジトリでIssueを開いてください。サポートします。

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当方は一切の責任を負いません。