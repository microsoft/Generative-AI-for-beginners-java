<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T14:47:45+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "ja"
}
-->
# 実践的な応用とプロジェクト

## 学べること
このセクションでは、Javaを使った生成AI開発パターンを紹介する3つの実践的なアプリケーションをデモします：
- クライアントサイドとサーバーサイドのAIを組み合わせたマルチモーダルなペットストーリー生成アプリを作成
- Foundry Local Spring Bootデモを使ったローカルAIモデルの統合を実装
- 電卓の例を使ったModel Context Protocol (MCP) サービスを開発

## 目次

- [はじめに](../../../04-PracticalSamples)
  - [Foundry Local Spring Bootデモ](../../../04-PracticalSamples)
  - [ペットストーリー生成アプリ](../../../04-PracticalSamples)
  - [MCP電卓サービス（初心者向けMCPデモ）](../../../04-PracticalSamples)
- [学習の進め方](../../../04-PracticalSamples)
- [まとめ](../../../04-PracticalSamples)
- [次のステップ](../../../04-PracticalSamples)

## はじめに

この章では、Javaを使った生成AI開発パターンを示す**サンプルプロジェクト**を紹介します。各プロジェクトは完全に動作するもので、特定のAI技術、アーキテクチャパターン、ベストプラクティスを示しており、自分のアプリケーションに応用することができます。

### Foundry Local Spring Bootデモ

**[Foundry Local Spring Bootデモ](foundrylocal/README.md)**は、**OpenAI Java SDK**を使用してローカルAIモデルと統合する方法を示します。このデモでは、Foundry Local上で動作する**Phi-3.5-mini**モデルに接続し、クラウドサービスに依存せずにAIアプリケーションを実行する方法を紹介します。

### ペットストーリー生成アプリ

**[ペットストーリー生成アプリ](petstory/README.md)**は、創造的なペットストーリーを生成する**マルチモーダルAI処理**をデモする魅力的なSpring Bootウェブアプリケーションです。このアプリは、ブラウザベースのAIインタラクションにtransformer.jsを使用し、サーバーサイド処理にOpenAI SDKを組み合わせています。

### MCP電卓サービス（初心者向けMCPデモ）

**[MCP電卓サービス](calculator/README.md)**は、Spring AIを使用した**Model Context Protocol (MCP)**のシンプルなデモです。MCPの基本概念を初心者向けに紹介し、MCPクライアントとやり取りする基本的なMCPサーバーの作成方法を示します。

## 学習の進め方

これらのプロジェクトは、前の章で学んだ概念を基に構築されています：

1. **シンプルに始める**：Foundry Local Spring Bootデモから始め、ローカルモデルとの基本的なAI統合を理解する
2. **インタラクティブ性を追加**：ペットストーリー生成アプリに進み、マルチモーダルAIとウェブベースのインタラクションを学ぶ
3. **MCPの基本を学ぶ**：MCP電卓サービスを試して、Model Context Protocolの基本を理解する

## まとめ

お疲れさまでした！以下のような実際のアプリケーションを探求しました：

- ブラウザとサーバーの両方で動作するマルチモーダルAI体験
- 最新のJavaフレームワークとSDKを使用したローカルAIモデルの統合
- AIツールとの統合方法を学ぶための初めてのModel Context Protocolサービス

## 次のステップ

[第5章: 責任ある生成AI](../05-ResponsibleGenAI/README.md)

**免責事項**:  
この文書は、AI翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を追求しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。元の言語で記載された文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。この翻訳の使用に起因する誤解や誤解釈について、当方は一切の責任を負いません。