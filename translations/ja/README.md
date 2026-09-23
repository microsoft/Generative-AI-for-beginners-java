# はじめての生成AI - Javaエディション
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![はじめての生成AI - Javaエディション](../../translated_images/ja/beg-genai-series.8b48be9951cc574c.webp)

<strong>所要時間</strong>: ワークショップ全体はローカルセットアップなしでオンライン完結できます。環境セットアップは2分、サンプルの探索は探索の深さにより1～3時間程度かかります。

> <strong>クイックスタート</strong> 

1. このリポジトリをGitHubアカウントにフォークする
2. **Code** → <strong>Codespaces</strong>タブ → **...** → **New with options...** をクリック
3. デフォルト設定を使用 – 本コース用開発コンテナが選択されます
4. **Create codespace** をクリック
5. 環境準備が整うまで約2分待つ
6. すぐに[第2章: Azure AI Foundryのプロビジョニング](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)に進む

## 多言語対応

### GitHub Actions による対応（自動・常時最新）

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[アラビア語](../ar/README.md) | [ベンガル語](../bn/README.md) | [ブルガリア語](../bg/README.md) | [ビルマ語（ミャンマー）](../my/README.md) | [中国語（簡体字）](../zh-CN/README.md) | [中国語（繁体字、香港）](../zh-HK/README.md) | [中国語（繁体字、マカオ）](../zh-MO/README.md) | [中国語（繁体字、台湾）](../zh-TW/README.md) | [クロアチア語](../hr/README.md) | [チェコ語](../cs/README.md) | [デンマーク語](../da/README.md) | [オランダ語](../nl/README.md) | [エストニア語](../et/README.md) | [フィンランド語](../fi/README.md) | [フランス語](../fr/README.md) | [ドイツ語](../de/README.md) | [ギリシャ語](../el/README.md) | [ヘブライ語](../he/README.md) | [ヒンディー語](../hi/README.md) | [ハンガリー語](../hu/README.md) | [インドネシア語](../id/README.md) | [イタリア語](../it/README.md) | [日本語](./README.md) | [カンナダ語](../kn/README.md) | [クメール語](../km/README.md) | [韓国語](../ko/README.md) | [リトアニア語](../lt/README.md) | [マレー語](../ms/README.md) | [マラヤーラム語](../ml/README.md) | [マラーティー語](../mr/README.md) | [ネパール語](../ne/README.md) | [ナイジェリアピジン](../pcm/README.md) | [ノルウェー語](../no/README.md) | [ペルシア語（ファルシ）](../fa/README.md) | [ポーランド語](../pl/README.md) | [ポルトガル語（ブラジル）](../pt-BR/README.md) | [ポルトガル語（ポルトガル）](../pt-PT/README.md) | [パンジャブ語（グルムキー）](../pa/README.md) | [ルーマニア語](../ro/README.md) | [ロシア語](../ru/README.md) | [セルビア語（キリル）](../sr/README.md) | [スロバキア語](../sk/README.md) | [スロベニア語](../sl/README.md) | [スペイン語](../es/README.md) | [スワヒリ語](../sw/README.md) | [スウェーデン語](../sv/README.md) | [タガログ語（フィリピン）](../tl/README.md) | [タミル語](../ta/README.md) | [テルグ語](../te/README.md) | [タイ語](../th/README.md) | [トルコ語](../tr/README.md) | [ウクライナ語](../uk/README.md) | [ウルドゥー語](../ur/README.md) | [ベトナム語](../vi/README.md)

> **ローカルクローンがお好みですか？**
>
> このリポジトリには50以上の言語翻訳が含まれており、ダウンロードサイズが大幅に増加します。翻訳なしでクローンするにはスパースチェックアウトを使用してください：
>
> **Bash / macOS / Linux:** 
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> これにより、本コースを完了するのに必要なすべてが、より高速なダウンロードで得られます。
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## コース構成と学習パス

### **第1章: 生成AI入門**
- <strong>基礎概念</strong>: 大規模言語モデル、トークン、埋め込み、AIの機能を理解する
- **Java AIエコシステム**: Spring AIとOpenAI SDKの概要
- <strong>モデルコンテキストプロトコル</strong>: MCPの紹介とAIエージェント間の通信における役割
- <strong>実用例</strong>: チャットボットやコンテンツ生成などの現実のシナリオ
- **[→ 第1章を始める](./01-IntroToGenAI/README.md)**

### **第2章: 開発環境のセットアップ**
- **Azure AI Foundry**: BicepとAzure Developer CLI (azd)でGPT-5.6 Lunaチャットとtext-embedding-3-small埋め込みをプロビジョニング
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: 公式OpenAI Java SDKとAzure OpenAI v1を使った`ChatClient`の学習
- <strong>キーなし認証</strong>: 管理不要のAPIキーなしでMicrosoft Entra IDと安全に接続
- <strong>開発ツール</strong>: Dockerコンテナ、VS Code、GitHub Codespacesの設定
- **[→ 第2章を始める](./02-SetupDevEnvironment/README.md)**

### **第3章: 生成AIのコア技術**
- **公式OpenAI Java SDK**: キーなし認証で直接Azure OpenAI v1を呼び出す
- <strong>プロンプトエンジニアリング</strong>: AIモデルから最適な応答を引き出す技術
- <strong>埋め込みとベクトル操作</strong>: セマンティック検索や類似度マッチングの実装
- **Retrieval-Augmented Generation (RAG)**: AIと独自のデータソースを統合する方法
- <strong>関数呼び出し</strong>: カスタムツールやプラグインでAIの機能拡張
- **[→ 第3章を始める](./03-CoreGenerativeAITechniques/README.md)**

### **第4章: 実践アプリケーションとプロジェクト**
- <strong>ペットストーリージェネレーター</strong> (`petstory/`): Azure AI Foundryを使った創造的なコンテンツ生成
- **Foundryローカルデモ** (`foundrylocal/`): OpenAI Java SDKを用いたローカルAIモデルの統合
- **MCP電卓サービス** (`calculator/`): Spring AIによる基本的なモデルコンテキストプロトコル実装
- **[→ 第4章を始める](./04-PracticalSamples/README.md)**

### **第5章: 責任あるAI開発**
- **Azure AI Foundryのコンテンツ安全機能**: 組み込みのコンテンツフィルタリングと安全機構のテスト（ハードブロックとソフト拒否）
- **責任あるAIデモ**: 実践的に最新のAI安全システムの動作を体験
- <strong>ベストプラクティス</strong>: 倫理的なAI開発と運用のための重要ガイドライン
- **[→ 第5章を始める](./05-ResponsibleGenAI/README.md)**

## 追加リソース

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![はじめてのLangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![はじめてのLangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![はじめてのLangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / エージェント
[![はじめてのAZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのEdge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのMCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのAIエージェント](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 生成AIシリーズ
[![はじめての生成AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![生成AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![生成AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![生成AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 中核学習
[![はじめてのML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのデータサイエンス](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのAI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのサイバーセキュリティ](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![はじめてのWeb開発](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのIoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![はじめてのXR開発](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilotシリーズ
[![AIペアプログラミング用Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET用Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilotアドベンチャー](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ヘルプを得るには

AIアプリ開発で行き詰まったり質問がある場合は、同じ学習者や経験豊富な開発者とMCPについて議論できるコミュニティに参加してください。質問は歓迎され、知識は自由に共有されます。

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

製品フィードバックや開発中のエラー報告は以下をご利用ください：

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類は AI 翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知おきください。原文の原語版が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->