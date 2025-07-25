<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:01:21+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "ja"
}
-->
# 実践的な応用とプロジェクト

## 学べること
このセクションでは、Javaを使った生成型AI開発パターンを紹介する3つの実践的なアプリケーションをデモします：
- クライアント側とサーバー側のAIを組み合わせたマルチモーダルなペットストーリー生成器を作成
- Foundry Local Spring Bootデモを使用したローカルAIモデルの統合を実装
- 電卓の例を使ったModel Context Protocol (MCP) サービスを開発

## 目次

- [はじめに](../../../04-PracticalSamples)
  - [Foundry Local Spring Bootデモ](../../../04-PracticalSamples)
  - [ペットストーリー生成器](../../../04-PracticalSamples)
  - [MCP電卓サービス（初心者向けMCPデモ）](../../../04-PracticalSamples)
- [学習の進行](../../../04-PracticalSamples)
- [まとめ](../../../04-PracticalSamples)
- [次のステップ](../../../04-PracticalSamples)

## はじめに

この章では、Javaを使った生成型AI開発パターンを示す**サンプルプロジェクト**を紹介します。各プロジェクトは完全に機能し、特定のAI技術、アーキテクチャパターン、ベストプラクティスを示しており、これらを自身のアプリケーションに応用することができます。

### Foundry Local Spring Bootデモ

**[Foundry Local Spring Bootデモ](foundrylocal/README.md)**では、**OpenAI Java SDK**を使用してローカルAIモデルと統合する方法を示します。このデモでは、Foundry Local上で動作する**Phi-3.5-mini**モデルに接続し、クラウドサービスに依存せずにAIアプリケーションを実行する方法を紹介します。

### ペットストーリー生成器

**[ペットストーリー生成器](petstory/README.md)**は、創造的なペットストーリーを生成する**マルチモーダルAI処理**を示す魅力的なSpring Bootウェブアプリケーションです。このアプリケーションは、ブラウザベースのAIインタラクションにtransformer.jsを使用し、サーバー側処理にOpenAI SDKを組み合わせています。

### MCP電卓サービス（初心者向けMCPデモ）

**[MCP電卓サービス](mcp/calculator/README.md)**は、Spring AIを使用した**Model Context Protocol (MCP)**の簡単なデモです。MCPの基本概念を初心者向けに紹介し、MCPクライアントとやり取りする基本的なMCPサーバーを作成する方法を示します。

## 学習の進行

これらのプロジェクトは、前の章で学んだ概念を基に構築されています：

1. **シンプルに始める**：Foundry Local Spring Bootデモから始め、ローカルモデルとの基本的なAI統合を理解する
2. **インタラクティブ性を追加**：ペットストーリー生成器に進み、マルチモーダルAIとウェブベースのインタラクションを学ぶ
3. **MCPの基本を学ぶ**：MCP電卓サービスを試して、Model Context Protocolの基礎を理解する

## まとめ

**おめでとうございます！** 以下を達成しました：

- **クライアント側とサーバー側のAI処理を組み合わせたマルチモーダルAI体験**を作成
- **最新のJavaフレームワークとSDKを使用したローカルAIモデル統合**を実装
- **ツール統合パターンを示すModel Context Protocolサービス**を開発

## 次のステップ

[第5章: 責任ある生成型AI](../05-ResponsibleGenAI/README.md)

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当方は一切の責任を負いません。