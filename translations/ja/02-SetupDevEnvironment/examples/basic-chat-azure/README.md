# Azure AI Foundry を使った基本チャット - エンドツーエンドの例

この例はシンプルな Spring Boot アプリケーションで、**Azure AI Foundry** モデルに<strong>キー不要認証</strong>（Microsoft Entra ID）で接続し、セットアップをテストします。Spring AI の `ChatClient` を使用し、内部的には **公式 OpenAI Java SDK** と **Azure OpenAI v1** エンドポイントを利用しています。

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) に記載のバージョンは Spring Boot **4.1.1**、Spring AI **2.0.1**、OpenAI Java **4.63.1**、Azure Identity **1.18.6**、dotenv-java **3.2.0** です。サンプルは `spring-ai-starter-model-openai` を利用し、`openai-java` と `azure-identity` を明示的に宣言しています。Spring AI 2 で古い Azure OpenAI スターターは削除されました。

## 目次

- [前提条件](#前提条件)
- [クイックスタート](#クイックスタート)
- [認証の仕組み](#認証の仕組み)
- [アプリケーションの実行](#アプリケーションの実行)
  - [Maven の使用](#maven-の使用)
  - [VS Code の使用](#vs-code-の使用)
  - [期待される出力](#期待される出力)
- [設定リファレンス](#設定リファレンス)
  - [環境変数](#環境変数)
  - [Spring の設定](#spring-の設定)
- [トラブルシューティング](#トラブルシューティング)
  - [よくある問題](#よくある問題)
  - [デバッグモード](#デバッグモード)
- [次のステップ](#次のステップ)
- [リソース](#リソース)

## 前提条件

この例を実行する前に次を確認してください：

- `gpt-5.6-luna` デプロイメント付きの Azure AI Foundry リソース - `azd up` でプロビジョニングするか、[Azure AI Foundry 設定ガイド](../../getting-started-azure-openai.md) に従って手動で作成
- そのリソースに対して **Cognitive Services OpenAI User** ロールが割り当てられていること（Bicep テンプレートが自動で割り当てます）
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli) をインストールし、`az login` でサインイン済み
- Java 21 以上と Maven 3.9 以上

> **API キーは不要です** — 認証は Microsoft Entra ID を使ったキー不要認証です。

## クイックスタート

```bash
# 1. プロジェクトに移動します
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. サインインしてキーなし認証がトークンを取得できるようにします
az login

# 3. エンドポイントを設定します
#    - `azd up` を実行した場合、.env は自動的に作成されます（スキップしてください）。
#    - そうでなければテンプレートをコピーして AZURE_OPENAI_ENDPOINT を設定してください。
cp .env.example .env

# 4. アプリケーションを実行します
mvn spring-boot:run
```

## 認証の仕組み

この例は **Microsoft Entra ID** で認証しており、API キーは使用しません。

アプリケーションは [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) で明示的に認証を設定しています：

1. `azureCredential()` が `AuthenticationUtil.getBearerTokenSupplier` を使い、`DefaultAzureCredential` と `https://ai.azure.com/.default` スコープで `BearerTokenCredential` を作成
2. `azureOpenAiClient()` は `OpenAIOkHttpClient.builder()` を使い、リソースエンドポイントを `/openai/v1` に解決して、ベアラー資格情報を `.credential(...)` で渡しつつ `OpenAIClient` を構築
3. `azureChatModel()` はそのクライアントを Spring AI の `OpenAiChatModel` に渡し、本レッスンの `ChatClient` を提供

これら明示的な Bean により、グローバル `OPENAI_API_KEY` が Azure 認証を上書きすることを防ぎます。YAML から API キーを省略するだけでは認証設定になりません。`DefaultAzureCredential` はローカルでは `az login` セッションを利用し、Azure ではマネージドアイデンティティを利用可能です。どちらのアイデンティティであってもリソースロールの割り当てが必須です。

## アプリケーションの実行

### Maven の使用

```bash
mvn spring-boot:run
```

### VS Code の使用

1. VS Code でプロジェクトを開く
2. `F5` を押すか「実行とデバッグ」パネルを使用
3. 「Spring Boot-BasicChatApplication」構成を選択

> <strong>注意</strong>：アプリケーションは作業ディレクトリから `.env` を読み込みます。VS Code から起動時も同様です。

### 期待される出力

実行成功後の例示的な出力（起動ログは省略、応答内容は異なる場合があります）：

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## 設定リファレンス

### 環境変数

| 変数 | 説明 | 必須 | 例 |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry（Azure OpenAI）エンドポイント URL | 必須 | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | チャットモデルのデプロイメント名 | 任意 | `gpt-5.6-luna`（デフォルト） |

> API キー変数は <strong>ありません</strong> — 認証はキー不要（`az login` による Microsoft Entra ID）です。

### Spring の設定

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) の設定は `spring.ai.openai` プレフィックスと平坦化されたチャットプロパティを使っています（`options` ブロックなし）：

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` は **Azure のデプロイメント名** です。認証は上記の明示的な Bean から取得し、`api-key` 設定は使いません。このレッスンでは推論を無効にし、完了トークン数を最大 500 に設定しています。`temperature` と旧式の `max-tokens` は設定していません。

Microsoft は [新規アプリケーション向けに公式 OpenAI SDK と Azure OpenAI v1 および Responses API の使用を推奨しています](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)。本メッセージベースレッスンでは Chat Completions は引き続きサポートされています。GPT-5.6 ではツールを含む Chat Completions リクエストは `reasoning_effort` を `none` に設定する必要があります。推論とツールを組み合わせる場合は Responses を使います。[推論モデルでのツール呼び出し](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)を参照してください。

## トラブルシューティング

### よくある問題

<details>
<summary><strong>エラー: 401 / "PermissionDenied" / トークンエラー</strong></summary>

- `az login` を実行 — キー不要認証はトークン取得に有効なサインインが必要です
- アカウントに対象リソースの **Cognitive Services OpenAI User** ロールがあるか確認
- ロールを割り当てたばかりの場合は伝播まで少し待つ
- 正しいテナントとサブスクリプションにいるか確認（`az account show`）
</details>

<details>
<summary><strong>エラー: "The endpoint is not valid" / 接続エラー</strong></summary>

- `AZURE_OPENAI_ENDPOINT` は完全なベース URL か（例: `https://your-resource.openai.azure.com/`）
- 末尾のスラッシュの有無を揃える
- エンドポイントがプロビジョニング済みのリソースと一致しているか確認（`azd env get-values`）
</details>

<details>
<summary><strong>エラー: "The deployment was not found"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` が Azure 内のデプロイメント名と一致しているか確認
- モデルが正常にデプロイされアクティブであるか確認
- デフォルトのデプロイメント名は `gpt-5.6-luna`
</details>

<details>
<summary><strong>エラー: 429 / レートリミット超過</strong></summary>

- デフォルトの GPT-5.6 Luna デプロイメントは Global Standard 容量 10：分間 10 リクエスト、分間 10,000 トークン
- サービスのリトライ間隔を待ってから、実行を順番に行う
- 本基本例は SDK の自動リトライを無効化しているため、失敗したリクエストは直接エラー報告されます
</details>

<details>
<summary><strong>VS Code: 環境変数が読み込まれない</strong></summary>

- プロジェクトルートディレクトリ（`pom.xml` と同じ階層）に `.env` ファイルがあることを確認
- VS Code の統合ターミナルで `mvn spring-boot:run` を試す
- VS Code Java 拡張機能が正しくインストールされているか確認
</details>

### デバッグモード

詳細なログ記録を有効にするには、[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) の以下の行のコメントを外してください：

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## 次のステップ

**セットアップ完了！** 次の学習を進めましょう：

[第3章：コア生成AI技術](../../../03-CoreGenerativeAITechniques/README.md)

## リソース

- [Spring AI 2 OpenAI Java SDK 移行ガイド](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 と公式 OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID を使ったキー不要認証](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry ポータル](https://ai.azure.com/)
- [Azure AI Foundry ドキュメント](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類は AI 翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知おきください。原文の原語版が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->