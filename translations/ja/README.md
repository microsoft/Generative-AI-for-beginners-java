<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:05:58+00:00",
  "source_file": "README.md",
  "language_code": "ja"
}
-->
# 初心者のための生成AI - Java版
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![初心者のための生成AI - Java版](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ja.png)

**所要時間**: このワークショップはローカルセットアップなしでオンラインで完了できます。環境設定には2分、サンプルの探索には深さに応じて1～3時間かかります。

> **クイックスタート**

1. このリポジトリを自分のGitHubアカウントにフォークします
2. **Code** → **Codespaces** タブ → **...** → **New with options...** をクリックします
3. デフォルト設定を使用します – このコース用に作成された開発コンテナが選択されます
4. **Create codespace** をクリックします
5. 環境が準備されるまで約2分待ちます
6. [最初の例](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)に直接進みます

## 多言語サポート

### GitHub Actionによるサポート (自動化 & 常に最新)

[フランス語](../fr/README.md) | [スペイン語](../es/README.md) | [ドイツ語](../de/README.md) | [ロシア語](../ru/README.md) | [アラビア語](../ar/README.md) | [ペルシャ語 (ファルシ)](../fa/README.md) | [ウルドゥー語](../ur/README.md) | [中国語 (簡体字)](../zh/README.md) | [中国語 (繁体字, マカオ)](../mo/README.md) | [中国語 (繁体字, 香港)](../hk/README.md) | [中国語 (繁体字, 台湾)](../tw/README.md) | [日本語](./README.md) | [韓国語](../ko/README.md) | [ヒンディー語](../hi/README.md) | [ベンガル語](../bn/README.md) | [マラーティー語](../mr/README.md) | [ネパール語](../ne/README.md) | [パンジャブ語 (グルムキー)](../pa/README.md) | [ポルトガル語 (ポルトガル)](../pt/README.md) | [ポルトガル語 (ブラジル)](../br/README.md) | [イタリア語](../it/README.md) | [ポーランド語](../pl/README.md) | [トルコ語](../tr/README.md) | [ギリシャ語](../el/README.md) | [タイ語](../th/README.md) | [スウェーデン語](../sv/README.md) | [デンマーク語](../da/README.md) | [ノルウェー語](../no/README.md) | [フィンランド語](../fi/README.md) | [オランダ語](../nl/README.md) | [ヘブライ語](../he/README.md) | [ベトナム語](../vi/README.md) | [インドネシア語](../id/README.md) | [マレー語](../ms/README.md) | [タガログ語 (フィリピン語)](../tl/README.md) | [スワヒリ語](../sw/README.md) | [ハンガリー語](../hu/README.md) | [チェコ語](../cs/README.md) | [スロバキア語](../sk/README.md) | [ルーマニア語](../ro/README.md) | [ブルガリア語](../bg/README.md) | [セルビア語 (キリル文字)](../sr/README.md) | [クロアチア語](../hr/README.md) | [スロベニア語](../sl/README.md) | [ウクライナ語](../uk/README.md) | [ビルマ語 (ミャンマー)](../my/README.md)

## コース構成 & 学習パス

### **第1章: 生成AIの概要**
- **基本概念**: 大規模言語モデル、トークン、埋め込み、AIの能力を理解する
- **Java AIエコシステム**: Spring AIとOpenAI SDKの概要
- **モデルコンテキストプロトコル**: MCPとAIエージェント間の通信における役割の紹介
- **実践的な応用**: チャットボットやコンテンツ生成などの実例
- **[→ 第1章を始める](./01-IntroToGenAI/README.md)**

### **第2章: 開発環境のセットアップ**
- **マルチプロバイダー構成**: GitHub Models、Azure OpenAI、OpenAI Java SDKの統合設定
- **Spring Boot + Spring AI**: エンタープライズAIアプリケーション開発のベストプラクティス
- **GitHub Models**: プロトタイピングと学習のための無料AIモデルアクセス (クレジットカード不要)
- **開発ツール**: Dockerコンテナ、VS Code、GitHub Codespacesの設定
- **[→ 第2章を始める](./02-SetupDevEnvironment/README.md)**

### **第3章: 生成AIのコア技術**
- **プロンプトエンジニアリング**: AIモデルの最適な応答を得るための技術
- **埋め込み & ベクトル操作**: セマンティック検索と類似性マッチングの実装
- **RAG (情報検索強化生成)**: AIと独自のデータソースを組み合わせる
- **関数呼び出し**: カスタムツールやプラグインでAIの能力を拡張
- **[→ 第3章を始める](./03-CoreGenerativeAITechniques/README.md)**

### **第4章: 実践的な応用 & プロジェクト**
- **ペットストーリー生成器** (`petstory/`): GitHub Modelsを使ったクリエイティブコンテンツ生成
- **Foundryローカルデモ** (`foundrylocal/`): OpenAI Java SDKを使ったローカルAIモデル統合
- **MCP電卓サービス** (`calculator/`): Spring AIを使った基本的なモデルコンテキストプロトコルの実装
- **[→ 第4章を始める](./04-PracticalSamples/README.md)**

### **第5章: 責任あるAI開発**
- **GitHub Modelsの安全性**: コンテンツフィルタリングと安全機構 (ハードブロックとソフト拒否) のテスト
- **責任あるAIデモ**: 現代のAI安全システムが実際にどのように機能するかを示すハンズオン例
- **ベストプラクティス**: 倫理的なAI開発と展開のための重要なガイドライン
- **[→ 第5章を始める](./05-ResponsibleGenAI/README.md)**

## 追加リソース

- [初心者のためのMCP](https://github.com/microsoft/mcp-for-beginners)
- [初心者のためのAIエージェント](https://github.com/microsoft/ai-agents-for-beginners)
- [.NETを使った初心者のための生成AI](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [JavaScriptを使った初心者のための生成AI](https://github.com/microsoft/generative-ai-with-javascript)
- [初心者のための生成AI](https://github.com/microsoft/generative-ai-for-beginners)
- [初心者のための機械学習](https://aka.ms/ml-beginners)
- [初心者のためのデータサイエンス](https://aka.ms/datascience-beginners)
- [初心者のためのAI](https://aka.ms/ai-beginners)
- [初心者のためのサイバーセキュリティ](https://github.com/microsoft/Security-101)
- [初心者のためのWeb開発](https://aka.ms/webdev-beginners)
- [初心者のためのIoT](https://aka.ms/iot-beginners)
- [初心者のためのXR開発](https://github.com/microsoft/xr-development-for-beginners)
- [AIペアプログラミングのためのGitHub Copilotマスター](https://aka.ms/GitHubCopilotAI)
- [C#/.NET開発者のためのGitHub Copilotマスター](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [GitHub Copilotの冒険を選ぼう](https://github.com/microsoft/CopilotAdventures)
- [Azure AIサービスを使ったRAGチャットアプリ](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当方は一切の責任を負いません。