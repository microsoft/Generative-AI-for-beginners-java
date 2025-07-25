<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:00:26+00:00",
  "source_file": "README.md",
  "language_code": "ja"
}
-->
# 初心者向け生成AI - Java版
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![初心者向け生成AI - Java版](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.ja.png)

**所要時間**: このワークショップはローカル環境のセットアップなしでオンラインで完了できます。サンプルを実行したい場合、環境設定は約2分で完了し、サンプルの探索には深さに応じて1～3時間かかります。

> **クイックスタート**

1. このリポジトリを自分のGitHubアカウントにフォークします
2. **Code** → **Codespaces** タブ → **...** → **New with options...** をクリックします
3. デフォルト設定を使用します – このコース用に作成された開発コンテナが選択されます
4. **Create codespace** をクリックします
5. 環境が準備されるまで約2分待ちます
6. [GitHub Models Tokenの作成](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)に直接進みます

## 多言語サポート

### GitHub Actionによるサポート (自動更新 & 常に最新)

[フランス語](../fr/README.md) | [スペイン語](../es/README.md) | [ドイツ語](../de/README.md) | [ロシア語](../ru/README.md) | [アラビア語](../ar/README.md) | [ペルシャ語 (ファルシ)](../fa/README.md) | [ウルドゥー語](../ur/README.md) | [中国語 (簡体字)](../zh/README.md) | [中国語 (繁体字, マカオ)](../mo/README.md) | [中国語 (繁体字, 香港)](../hk/README.md) | [中国語 (繁体字, 台湾)](../tw/README.md) | [日本語](./README.md) | [韓国語](../ko/README.md) | [ヒンディー語](../hi/README.md) | [ベンガル語](../bn/README.md) | [マラーティー語](../mr/README.md) | [ネパール語](../ne/README.md) | [パンジャブ語 (グルムキー)](../pa/README.md) | [ポルトガル語 (ポルトガル)](../pt/README.md) | [ポルトガル語 (ブラジル)](../br/README.md) | [イタリア語](../it/README.md) | [ポーランド語](../pl/README.md) | [トルコ語](../tr/README.md) | [ギリシャ語](../el/README.md) | [タイ語](../th/README.md) | [スウェーデン語](../sv/README.md) | [デンマーク語](../da/README.md) | [ノルウェー語](../no/README.md) | [フィンランド語](../fi/README.md) | [オランダ語](../nl/README.md) | [ヘブライ語](../he/README.md) | [ベトナム語](../vi/README.md) | [インドネシア語](../id/README.md) | [マレー語](../ms/README.md) | [タガログ語 (フィリピン)](../tl/README.md) | [スワヒリ語](../sw/README.md) | [ハンガリー語](../hu/README.md) | [チェコ語](../cs/README.md) | [スロバキア語](../sk/README.md) | [ルーマニア語](../ro/README.md) | [ブルガリア語](../bg/README.md) | [セルビア語 (キリル文字)](../sr/README.md) | [クロアチア語](../hr/README.md) | [スロベニア語](../sl/README.md) | [ウクライナ語](../uk/README.md) | [ビルマ語 (ミャンマー)](../my/README.md)

## コース構成と学習パス

### **第1章: 生成AIの概要**
- **基本概念**: 大規模言語モデル、トークン、埋め込み、AIの能力について理解する
- **Java AIエコシステム**: Spring AIとOpenAI SDKの概要
- **モデルコンテキストプロトコル**: MCPとAIエージェント間の通信における役割の紹介
- **実践的な応用**: チャットボットやコンテンツ生成などの実世界のシナリオ
- **[→ 第1章を始める](./01-IntroToGenAI/README.md)**

### **第2章: 開発環境のセットアップ**
- **マルチプロバイダー構成**: GitHub Models、Azure OpenAI、OpenAI Java SDKの統合設定
- **Spring Boot + Spring AI**: エンタープライズAIアプリケーション開発のベストプラクティス
- **GitHub Models**: クレジットカード不要でプロトタイプや学習に利用可能な無料AIモデル
- **開発ツール**: Dockerコンテナ、VS Code、GitHub Codespacesの設定
- **[→ 第2章を始める](./02-SetupDevEnvironment/README.md)**

### **第3章: 生成AIの主要技術**
- **プロンプトエンジニアリング**: AIモデルの最適な応答を得るための技術
- **埋め込みとベクトル操作**: セマンティック検索と類似性マッチングの実装
- **検索強化生成 (RAG)**: AIを独自のデータソースと組み合わせる
- **関数呼び出し**: カスタムツールやプラグインでAIの能力を拡張する
- **[→ 第3章を始める](./03-CoreGenerativeAITechniques/README.md)**

### **第4章: 実践的な応用とプロジェクト**
- **ペットストーリー生成器** (`petstory/`): GitHub Modelsを使った創造的なコンテンツ生成
- **Foundryローカルデモ** (`foundrylocal/`): OpenAI Java SDKを使ったローカルAIモデル統合
- **MCP計算サービス** (`mcp/calculator/`): Spring AIを使った基本的なモデルコンテキストプロトコルの実装
- **[→ 第4章を始める](./04-PracticalSamples/README.md)**

### **第5章: 責任あるAI開発**
- **GitHub Modelsの安全性**: 組み込みのコンテンツフィルタリングと安全メカニズムをテスト
- **責任あるAIデモ**: AI安全フィルターが実際にどのように機能するかを示すハンズオン例
- **ベストプラクティス**: 倫理的なAI開発と展開のための重要なガイドライン
- **[→ 第5章を始める](./05-ResponsibleGenAI/README.md)**

## 追加リソース

- [初心者向けAIエージェント](https://github.com/microsoft/ai-agents-for-beginners)
- [初心者向け生成AI (.NET版)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [初心者向け生成AI (JavaScript版)](https://github.com/microsoft/generative-ai-with-javascript)
- [初心者向け生成AI](https://github.com/microsoft/generative-ai-for-beginners)
- [初心者向け機械学習](https://aka.ms/ml-beginners)
- [初心者向けデータサイエンス](https://aka.ms/datascience-beginners)
- [初心者向けAI](https://aka.ms/ai-beginners)
- [初心者向けサイバーセキュリティ](https://github.com/microsoft/Security-101)
- [初心者向けWeb開発](https://aka.ms/webdev-beginners)
- [初心者向けIoT](https://aka.ms/iot-beginners)
- [初心者向けXR開発](https://github.com/microsoft/xr-development-for-beginners)
- [GitHub Copilotを使ったAIペアプログラミングの習得](https://aka.ms/GitHubCopilotAI)
- [C#/.NET開発者向けGitHub Copilotの習得](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [GitHub Copilotの冒険を選ぼう](https://github.com/microsoft/CopilotAdventures)
- [Azure AIサービスを使ったRAGチャットアプリ](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当社は責任を負いません。