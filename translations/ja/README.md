<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:15:23+00:00",
  "source_file": "README.md",
  "language_code": "ja"
}
-->
# ジェネレーティブAI入門 - Java版
[![Microsoft Foundry の Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ジェネレーティブAI入門 - Java版](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ja.png)

**所要時間**: このワークショップ全体はローカルのセットアップなしでオンラインで完了できます。環境のセットアップには2分、サンプルの確認には深さに応じて1〜3時間かかります。

> **クイックスタート** 

1. このリポジトリをあなたのGitHubアカウントにフォークしてください
2. 「**Code**」→「**Codespaces**」タブ→「**...**」→「**New with options...**」をクリック
3. デフォルトを使用します — これにより本コース用に作成されたDevelopmentコンテナが選択されます
4. 「**Create codespace**」をクリック
5. 環境が準備されるまで約2分待ちます
6. そのまま[最初の例](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)へ進む

> **ローカルでクローンしますか？**
>
> このリポジトリには50以上の言語翻訳が含まれており、ダウンロードサイズが大幅に増加します。翻訳を含めずにクローンするには、スパースチェックアウトを使用してください:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> これにより、はるかに高速にダウンロードでき、本コースを完了するために必要なすべてが得られます。


## 多言語サポート

### GitHub Actionでサポート（自動化 & 常に最新）

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[アラビア語](../ar/README.md) | [ベンガル語](../bn/README.md) | [ブルガリア語](../bg/README.md) | [ビルマ語（ミャンマー）](../my/README.md) | [中国語（簡体字）](../zh/README.md) | [中国語（繁体字、香港）](../hk/README.md) | [中国語（繁体字、マカオ）](../mo/README.md) | [中国語（繁体字、台湾）](../tw/README.md) | [クロアチア語](../hr/README.md) | [チェコ語](../cs/README.md) | [デンマーク語](../da/README.md) | [オランダ語](../nl/README.md) | [エストニア語](../et/README.md) | [フィンランド語](../fi/README.md) | [フランス語](../fr/README.md) | [ドイツ語](../de/README.md) | [ギリシャ語](../el/README.md) | [ヘブライ語](../he/README.md) | [ヒンディー語](../hi/README.md) | [ハンガリー語](../hu/README.md) | [インドネシア語](../id/README.md) | [イタリア語](../it/README.md) | [日本語](./README.md) | [カンナダ語](../kn/README.md) | [韓国語](../ko/README.md) | [リトアニア語](../lt/README.md) | [マレー語](../ms/README.md) | [マラヤーラム語](../ml/README.md) | [マラーティー語](../mr/README.md) | [ネパール語](../ne/README.md) | [ナイジェリア・ピジン語](../pcm/README.md) | [ノルウェー語](../no/README.md) | [ペルシャ語（ファルシ）](../fa/README.md) | [ポーランド語](../pl/README.md) | [ポルトガル語（ブラジル）](../br/README.md) | [ポルトガル語（ポルトガル）](../pt/README.md) | [パンジャブ語（グルムキー）](../pa/README.md) | [ルーマニア語](../ro/README.md) | [ロシア語](../ru/README.md) | [セルビア語（キリル文字）](../sr/README.md) | [スロバキア語](../sk/README.md) | [スロベニア語](../sl/README.md) | [スペイン語](../es/README.md) | [スワヒリ語](../sw/README.md) | [スウェーデン語](../sv/README.md) | [タガログ語（フィリピノ）](../tl/README.md) | [タミル語](../ta/README.md) | [テルグ語](../te/README.md) | [タイ語](../th/README.md) | [トルコ語](../tr/README.md) | [ウクライナ語](../uk/README.md) | [ウルドゥー語](../ur/README.md) | [ベトナム語](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## コース構成と学習の流れ

### **第1章: ジェネレーティブAI入門**
- **コアコンセプト**: 大規模言語モデル、トークン、埋め込み（embeddings）、およびAIの能力の理解
- **JavaのAIエコシステム**: Spring AIとOpenAI SDKsの概要
- **Model Context Protocol**: MCPの紹介とAIエージェント間通信での役割
- **実用的なアプリケーション**: チャットボットやコンテンツ生成を含む実世界のシナリオ
- **[→ 第1章を開始](./01-IntroToGenAI/README.md)**

### **第2章: 開発環境のセットアップ**
- **マルチプロバイダー設定**: GitHub Models、Azure OpenAI、OpenAI Java SDK の統合を設定
- **Spring Boot + Spring AI**: エンタープライズ向けAIアプリケーション開発のベストプラクティス
- **GitHub Models**: プロトタイピングと学習のための無料AIモデルアクセス（クレジットカード不要）
- **開発ツール**: Dockerコンテナ、VS Code、GitHub Codespaces の構成
- **[→ 第2章を開始](./02-SetupDevEnvironment/README.md)**

### **第3章: コアなジェネレーティブAI手法**
- **プロンプトエンジニアリング**: AIモデルの最適な応答のための技術
- **埋め込みとベクトル操作**: セマンティック検索と類似性マッチングを実装
- **Retrieval-Augmented Generation (RAG)**: AIを自分のデータソースと組み合わせる
- **関数呼び出し**: カスタムツールやプラグインでAIの機能を拡張
- **[→ 第3章を開始](./03-CoreGenerativeAITechniques/README.md)**

### **第4章: 実践的なアプリケーションとプロジェクト**
- **ペットストーリージェネレータ** (`petstory/`): GitHub Models を使ったクリエイティブなコンテンツ生成
- **Foundry Local デモ** (`foundrylocal/`): OpenAI Java SDK を使ったローカルAIモデル統合
- **MCP 電卓サービス** (`calculator/`): Spring AI を使った基本的な Model Context Protocol の実装
- **[→ 第4章を開始](./04-PracticalSamples/README.md)**

### **第5章: 責任あるAI開発**
- **GitHub Models の安全性**: 組み込みのコンテンツフィルタリングと安全機構（ハードブロックとソフト拒否）をテスト
- **責任あるAIデモ**: 現代のAI安全システムが実際にどのように機能するかを示す実践的な例
- **ベストプラクティス**: 倫理的なAI開発と展開のための重要なガイドライン
- **[→ 第5章を開始](./05-ResponsibleGenAI/README.md)**

## 追加リソース

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j 初心者向け](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js 初心者向け](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / エージェント
[![AZD 初心者向け](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI 初心者向け](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP 初心者向け](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AIエージェント 初心者向け](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ジェネレーティブAIシリーズ
[![ジェネレーティブAI 初心者向け](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ジェネレーティブAI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![ジェネレーティブAI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![ジェネレーティブAI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### コア学習
[![機械学習 初心者向け](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![データサイエンス 初心者向け](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI初心者向け](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![サイバーセキュリティ 初心者向け](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web開発 初心者向け](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![初心者向け IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![初心者向け XR 開発](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot シリーズ
[![AI ペアプログラミング向け Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET 向け Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot アドベンチャー](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ヘルプ

AI アプリの作成で行き詰まったり質問がある場合は、MCP に関する議論に参加してください。他の学習者や経験豊富な開発者と意見交換ができます。ここは質問が歓迎され、知識が自由に共有される支援的なコミュニティです。

[![Microsoft Foundry の Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

If you have product feedback or errors while building visit:

[![Microsoft Foundry 開発者フォーラム](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
免責事項:
この文書はAI翻訳サービス「Co‑op Translator」（https://github.com/Azure/co-op-translator）を用いて翻訳されました。正確さには努めていますが、自動翻訳には誤りや不正確な箇所が含まれる可能性があります。原文（原言語の文書）を正式な出典としてご確認ください。重要な情報については、専門の翻訳者による人による翻訳を推奨します。本翻訳の利用に起因するいかなる誤解や解釈の相違についても、当方は責任を負いません。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->