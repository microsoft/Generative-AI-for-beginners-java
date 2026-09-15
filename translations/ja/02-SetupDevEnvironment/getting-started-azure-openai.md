# Azure AI Foundry の開発環境セットアップ

> このガイドでは、本コースの Java AI アプリ向けに **Azure AI Foundry** モデルを <strong>キー不要</strong> 認証 (Microsoft Entra ID) を使ってセットアップします — 管理する API キーはありません。ツールに不慣れな方は、[開発環境ガイド](./README.md)から始めてください。

このガイドは、本コースの Java AI アプリ用に **Azure AI Foundry** モデルをセットアップします。選択肢は2つあります:

- **オプションA — `azd` + Bicep でプロビジョニング (推奨)：** 1つのコマンドで Foundry アカウントとモデルをコードとしてデプロイ。ポータルでクリック不要。
- **オプションB — Azure AI Foundry ポータルでリソースを手動作成**。

両パスとも <strong>キー不要認証</strong> (Microsoft Entra ID) を使用します — コピーや漏洩する API キーはありません。

## 目次

- [作成されるもの](#作成されるもの)
- [前提条件](#前提条件)
- [オプションA：azd + Bicep でプロビジョニング (推奨)](#option-a-provision-with-azd--bicep-recommended)
- [オプションB：リソースを手動作成](#オプションb：リソースを手動作成)
- [環境設定](#環境設定)
- [セットアップのテスト](#セットアップのテスト)
- [次にやること](#次にやること)
- [リソース](#リソース)
- [追加リソース](#追加リソース)

## 作成されるもの

[`infra/`](../../../02-SetupDevEnvironment/infra) の Bicep テンプレートでは以下をプロビジョニングします:

- プロジェクト付きの **Azure AI Foundry** アカウント（`Microsoft.CognitiveServices/accounts`, 種別 `AIServices`）
- チャット用デプロイメント - GPT-5.6 Luna (`gpt-5.6-luna`)、バージョン `2026-07-09`、`GlobalStandard` キャパシティ `10`（このモデルの10リクエスト/分、10,000トークン/分）
- 埋め込み用デプロイメント - `text-embedding-3-small`、バージョン `1`（後半章で使用）
- キー不要のロール割り当て (`Cognitive Services OpenAI User`) で `az login` でサインインでき、キー管理不要

## 前提条件

- [Azure サブスクリプション](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) および [Maven 3.9+](https://maven.apache.org/download.cgi)

## オプションA：azd + Bicep でプロビジョニング (推奨)

`02-SetupDevEnvironment` フォルダーから:

```bash
cd 02-SetupDevEnvironment

# サインイン（両ツール）
azd auth login
az login

# Foundryアカウントとモデルデプロイのプロビジョニング
azd up
```

`azd` は <strong>環境名</strong>（例: `genai-java`）、<strong>サブスクリプション</strong>、<strong>リージョン</strong> を尋ねます。`gpt-5.6-luna` と `text-embedding-3-small` が利用可能なリージョン (例: `eastus2`) と、ご自身のサブスクリプションを選択してください。サブスクリプションにそのリージョンでモデルとデプロイメントタイプに十分なクォータがあることを確認してください。利用可能性とクォータはサブスクリプションによって異なります。

プロビジョニング完了後、azd は:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) に定義されたすべてをデプロイします。
2. ポストプロビジョンフックを実行し、エンドポイントとデプロイメント名を含む [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) を書き込みます（秘密情報なし）。

> **ヒント:** `azd up` は何度でも実行して変更を適用してください。`azd down` を実行するとすべて削除され、費用が発生しなくなります。

生成された設定を見るには:

```bash
azd env get-values
```

次に [セットアップのテスト](#セットアップのテスト) に進んでください。

## オプションB：リソースを手動作成

ポータルを使いたいですか？手動でリソースを作成してください:

1. [Azure AI Foundry ポータル](https://ai.azure.com/)にアクセスし、サインインします。
2. <strong>プロジェクトを作成</strong>（これで AI Foundry リソースも作成されます）。`GenAIJava` のような名前を付けます。
3. プロジェクト内で **Models + endpoints** → **Deploy model** → **Deploy base model** を開きます。
4. **GPT-5.6 Luna**（モデルおよびデプロイ名 `gpt-5.6-luna`、バージョン `2026-07-09`）を **Global Standard** キャパシティ `10` でデプロイ。埋め込み例が必要な場合は、**text-embedding-3-small** バージョン `1` も同様にデプロイしてください。
5. <strong>概要</strong>から <strong>エンドポイント</strong> をコピーします（例: `https://<resource>.openai.azure.com/`）。
6. キー不要アクセスを許可します: リソースで **アクセス制御 (IAM)** → <strong>ロール割り当ての追加</strong> → **Cognitive Services OpenAI User** を自分のアカウントに割り当てます。

> **まだ問題がありますか？** [Azure AI Foundry ドキュメント](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)をご覧ください。

## 環境設定

**オプションA (`azd up`) を利用した場合**、設定ファイルはすでに書き込まれているため、設定は不要です。[セットアップのテスト](#セットアップのテスト)へ進んでください。

**オプションB (手動) を利用した場合**、サンプルの `.env` ファイルを自分で作成してください:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` をエンドポイントに合わせて編集してください（キー不要 — 認証はキー不要です）:

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

プロジェクト URL ではなく、リソースの Azure OpenAI エンドポイントを使用してください。basic-chat アプリではそれを `/openai/v1` に解決し、明示的なベアラートークンクライアントを設定します。API キーは不要です。

> **セキュリティ注意:** 保管すべき API キーはありません。`az login` (ローカル) またはマネージド ID (Azure内) を介して Microsoft Entra ID で認証します。`.env` には秘密でない設定のみを保持し、`.gitignore` で保護されています。

## セットアップのテスト

キー不要認証用のトークンを取得できるようにサインインしてから、例を実行します:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # まだサインインしていない場合
mvn clean spring-boot:run
```

`gpt-5.6-luna` モデルからの応答が見えるはずです。例を連続で実行して小さいデフォルトクォータ内に収めてください。HTTP 429 エラーが出たら、リトライ間隔を待ってから再試行してください。

> **VS Code ユーザー:** `F5` キーで実行します。アプリが `.env` を自動で読み込みます。

> **完全な例:** 詳細とトラブルシューティングは [Basic Chat with Azure AI Foundry example](./examples/basic-chat-azure/README.md) をご覧ください。

## 次にやること

プロビジョニングと例の成功実行後には以下が整っています:
- `gpt-5.6-luna` と `text-embedding-3-small` がデプロイされた Azure AI Foundry
- キー不要認証 (Microsoft Entra ID) — 管理すべきキーなし
- エンドポイントとデプロイ名を含むローカルの `.env`
- すぐに使える Java 開発環境

<strong>続けて</strong> [第3章：コアの生成AI技術](../03-CoreGenerativeAITechniques/README.md) で AI アプリの構築を始めましょう！

## リソース

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID でのキー不要認証](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry ドキュメント](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK への移行](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 対応の公式 OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## 追加リソース

- [VS Code ダウンロード](https://code.visualstudio.com/Download)
- [Docker Desktop 入手](https://www.docker.com/products/docker-desktop)
- [開発コンテナ設定](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類は AI 翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知おきください。原文の原語版が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->