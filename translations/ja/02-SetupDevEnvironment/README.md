<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "4bdff5070d182c64143dfe5a581d0ec7",
  "translation_date": "2025-08-28T18:26:49+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "ja"
}
-->
# Java 向け Generative AI 開発環境のセットアップ

> **クイックスタート**: 2 分でクラウドでコードを作成 - [GitHub Codespaces セットアップ](#option-a-github-codespaces-recommended) へジャンプ - ローカルインストールは不要で、GitHub モデルを使用します！

> **Azure OpenAI にご興味がありますか?** 新しい Azure OpenAI リソースを作成する手順が記載されている [Azure OpenAI セットアップ ガイド](getting-started-azure-openai.md) をご覧ください。

## 学習内容

- AIアプリケーション用のJava開発環境を構築する
- 希望する開発環境（Codespacesを使用したクラウドファースト、ローカル開発コンテナ、または完全なローカル環境）を選択して構成する
- GitHub Modelsに接続して設定をテストする

## 目次

- [学習内容](#what-youll-learn)
- [概要](#introduction)
- [ステップ 1: 開発環境のセットアップ](#step-1-set-up-your-development-environment)
- [オプション A: GitHub Codespaces (推奨)](#option-a-github-codespaces-recommended)
- [オプション B: ローカル開発コンテナ](#option-b-local-dev-container)
- [オプション C: 既存のローカルインストールの使用](#option-c-use-your-existing-local-installation)
- [ステップ 2: GitHub 個人アクセストークンの作成](#step-2-create-a-github-personal-access-token)
- [ステップ 3: セットアップのテスト](#step-3-test-your-setup-with-the-github-models-example)
- [トラブルシューティング](#troubleshooting)
- [概要](#summary)
- [次のステップ](#next-steps)

## はじめに

この章では、開発環境の設定手順を説明します。主な例として **GitHub Models** を使用します。これは無料で、GitHub アカウントだけで簡単に設定でき、クレジットカードも不要で、複数のモデルにアクセスして実験できるためです。

**ローカル環境での設定は不要です！** ブラウザで完全な開発環境を提供する GitHub Codespaces を使用すれば、すぐにコーディングを開始できます。

<img src="/02-SetupDevEnvironment/images/models.webp" alt="スクリーンショット: GitHub モデル" width="50%">

このコースでは、[**GitHub モデル**](https://github.com/marketplace?type=models) の使用をお勧めします。その理由は以下のとおりです。
- **無料で** 開始可能
- GitHub アカウントだけで**簡単に** セットアップ可能
- **クレジットカードは不要**
- **複数のモデル** を実験に利用可能

> **注**: このトレーニングで使用する GitHub モデルには、以下の無料制限があります。
> - 1分あたり15リクエスト（1日あたり150リクエスト）
> - 1リクエストあたり約8,000ワードの受信、約4,000ワードの送信
> - 同時リクエスト数 5
>
> 本番環境でご利用の場合は、Azure アカウントを使用して Azure AI Foundry モデルにアップグレードしてください。コードを変更する必要はありません。 [Azure AI Foundry のドキュメント](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models)をご覧ください。

## ステップ 1: 開発環境のセットアップ

<a name="quick-start-cloud"></a>

セットアップ時間を最小限に抑え、この Java 向け Generative AI コースに必要なツールをすべて揃えるために、構成済みの開発コンテナーを作成しました。ご希望の開発アプローチを選択してください:

### 環境設定オプション:

#### オプション A: GitHub Codespaces (推奨)

**2 分でコーディングを開始 - ローカル環境の設定は不要です!**

1. このリポジトリを GitHub アカウントにフォークします。
> **注**: 基本設定を編集する場合は、[開発コンテナ構成](../.devcontainer/devcontainer.json) をご確認ください。
2. **コード** → **コードスペース** タブ → **...** → **オプション付きで新規作成...** をクリックします。
3. デフォルト設定を使用します。これにより、**開発コンテナ構成** が選択されます: **Generative AI Java 開発環境** このコース用に作成されたカスタム開発コンテナ
4. **コードスペースを作成** をクリックします。
5. 環境の準備ができるまで約 2 分待ちます。
6. [ステップ 2: GitHub トークンの作成](#step-2-create-a-github-personal-access-token) に進みます。

<img src="/translated_images/ja/codespaces.9945ded8ceb431a5.webp" alt="スクリーンショット: Codespaces サブメニュー" width="50%">

<img src="/translated_images/ja/image.833552b62eee7766.webp" alt="スクリーンショット: オプション付き新規作成" width="50%">

<img src="/translated_images/ja/codespaces-create.b44a36f728660ab7.webp" alt="スクリーンショット: Codespaces 作成オプション" width="50%">

> **Codespaces のメリット**:
> - ローカルインストールは不要
> - ブラウザ搭載のあらゆるデバイスで動作
> - すべてのツールと依存関係が事前設定済み
> - 個人アカウントで月60時間の無料利用が可能
> - すべての学習者に一貫した環境を提供

#### オプション B: ローカル開発コンテナ

**Docker を使用したローカル開発を希望する開発者向け**

1. このリポジトリをフォークしてローカルマシンにクローンします
> **注**: 基本設定を編集する場合は、 [開発コンテナの構成](../.devcontainer/devcontainer.json)
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/)と[VS Code](https://code.visualstudio.com/)をインストールします。
3. VS Codeに[開発コンテナ拡張機能](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)をインストールします。
4. VS Codeでリポジトリフォルダを開きます。
5. プロンプトが表示されたら、[**コンテナで再度開く**]をクリックします（またはCtrl+Shift+Pを押して[開発コンテナ: コンテナで再度開く]を選択します）。
6. コンテナがビルドされて起動するまで待ちます。
7. [ステップ2: GitHubトークンの作成](#step-2-create-a-github-per) に進みます。

<img src="/translated_images/ja/devcontainer.21126c9d6de64494.webp" alt="スクリーンショット: 開発コンテナのセットアップ" width="50%">

<img src="/translated_images/ja/image-3.bf93d533bbc84268.webp" alt="スクリーンショット: 開発コンテナのビルド完了" width="50%">

#### オプション C: 既存のローカルインストールを使用する

**既存の Java 環境をお持ちの開発者向け**

前提条件:
- [Java 21 以上](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven 3.9 以上](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) またはお好みの IDE

手順:
1. このリポジトリをローカルマシンにクローンします。
2. IDE でプロジェクトを開きます。
3. [手順 2: GitHub トークンの作成](#step-2-create-a-github-personal-access-token)

> **プロのヒント**: 低スペックのマシンを使っているけれど、VS Code をローカルで使いたい場合は、GitHub Codespaces をご利用ください。ローカルの VS Code をクラウドホストの Codespace に接続することで、両方のメリットを享受できます。

<img src="/translated_images/ja/image-2.fc0da29a6e4d2aff.webp" alt="スクリーンショット: ローカルの devcontainer インスタンスを作成" width="50%">

## ステップ 2: GitHub 個人アクセストークンの作成

1. [GitHub 設定](https://github.com/settings/profile) に移動し、プロフィールメニューから [**設定**] を選択します。
2. 左側のサイドバーで、[**開発者設定**] をクリックします (通常は一番下にあります)。
3. [**個人アクセストークン**] の下にある 「**きめ細かなトークン**」をクリックします（または、こちらの直接リンク [リンク](https://github.com/settings/personal-access-tokens) にアクセスしてください）。
4. 「**新しいトークンを生成**」をクリックします。
5. 「トークン名」に、わかりやすい名前を入力します（例：`GenAI-Java-Course-Token`）。
6. 有効期限を設定します（セキュリティのベストプラクティスとして、7日間を推奨）。
7. 「リソースオーナー」で、ユーザーアカウントを選択します。
8. 「リポジトリアクセス」で、GitHub モデルで使用するリポジトリを選択します（必要に応じて「すべてのリポジトリ」を選択することもできます）。
9. 「アカウント権限」で、「**モデル**」を見つけて、「**読み取り専用**」に設定します。
10. 「**トークンを生成**」をクリックします。
11. **トークンをコピーして保存してください** – 二度と表示されなくなります！

> **セキュリティのヒント**: アクセストークンには、必要最小限のスコープと、実用的な最短の有効期限を使用してください。

## ステップ 3: GitHub Models サンプルで設定をテストする

開発環境の準備が整ったら、[`02-SetupDevEnvironment/examples/github-models`](./examples/github-models/) にあるサンプルアプリケーションで GitHub Models の統合をテストしましょう。

1. 開発環境でターミナルを開きます。
2. GitHub Models のサンプルに移動します。
```bash
cd 02-SetupDevEnvironment/examples/github-models
```
3. GitHub トークンを環境変数として設定します。
```bash
# macOS/Linux
export GITHUB_TOKEN=your_token_here

# Windows (コマンドプロンプト)
set GITHUB_TOKEN=your_token_here

# Windows (PowerShell)
$env:GITHUB_TOKEN="your_token_here"
```

4. アプリケーションを実行します。
```bash
mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
```

次のような出力が表示されます。
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### サンプルコードの理解

まず、実行した内容を理解しましょう。`examples/github-models` にあるサンプルコードは、OpenAI Java SDK を使用して GitHub モデルに接続します。

**このコードの動作:**
- 個人アクセストークンを使用して GitHub モデルに **接続** します。
- シンプルな「Say Hello World!」を **送信** します。 AI モデルへのメッセージ
- AI の応答を **受信** して表示します
- セットアップが正しく動作していることを **検証** します

**キー依存関係** (`pom.xml` 内):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**メインコード** (`App.java`):
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

素晴らしい！これですべての設定が完了しました。

- AIモデルへのアクセスに必要な権限を持つGitHub Personal Access Tokenを作成しました。
- Java開発環境（Codespaces、開発コンテナ、ローカル）を起動しました。
- OpenAI Java SDKを使用してGitHub Modelsに接続しました。（無料AI開発環境）
- AIモデルと通信する簡単なサンプルで動作を確認しました。

## 次のステップ

[第3章：コアGenerative AIテクニック](../03-CoreGenerativeAITechniques/README.md)

## トラブルシューティング

問題がありますか?よくある問題と解決策を以下に示します。

- **トークンが機能していませんか？**
- 余分なスペースを含めずにトークン全体をコピーしたことを確認してください。
- トークンが環境変数として正しく設定されていることを確認してください。
- トークンに適切な権限（モデル：読み取りと書き込み）が付与されていることを確認してください。

- **Maven が見つかりませんか？**
- 開発コンテナ/Codespaces を使用する場合は、Maven が事前にインストールされている必要があります。
- ローカルセットアップの場合は、Java 21 以上と Maven 3.9 以上がインストールされていることを確認してください。
- インストールを確認するには、`mvn --version` を実行してみてください。

- **接続に問題がありますか？**
- インターネット接続を確認してください。
- ネットワークから GitHub にアクセスできることを確認してください。
- GitHub Models エンドポイントをブロックするファイアウォールの内側にいないことを確認してください。

- **開発コンテナが起動していませんか？**
- Docker Desktop が実行されていることを確認してください（ローカル開発の場合）。
- コンテナを再構築してみてください。`Ctrl+Shift+P` → "開発コンテナ：コンテナの再構築"

- **アプリケーションのコンパイルエラーですか？**
- 正しい場所にいることを確認してください。ディレクトリ: `02-SetupDevEnvironment/examples/github-models`
- クリーンアップとリビルドをお試しください: `mvn clean compile`

> **ヘルプが必要ですか？**: 問題が解決しない場合は、リポジトリで問題を開いてください。サポートいたします。

---

