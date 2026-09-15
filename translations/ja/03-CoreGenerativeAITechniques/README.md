# コア生成AI技術チュートリアル

## 目次

- [前提条件](#前提条件)
- [開始方法](#開始方法)
- [モデル選択ガイド](#モデル選択ガイド)
- [チュートリアル1：LLM完了とチャット](#チュートリアル1：llm完了とチャット)
- [チュートリアル2：関数呼び出し](#チュートリアル2：関数呼び出し)
- [チュートリアル3：RAG（検索拡張生成）](#チュートリアル3：rag（検索拡張生成）)
- [チュートリアル4：責任あるAI](#チュートリアル4：責任あるai)
- [例を通じた共通パターン](#例を通じた共通パターン)
- [ユニットテスト](#ユニットテスト)
- [連続ライブ検証](#連続ライブ検証)
- [トラブルシューティング](#トラブルシューティング)
- [次のステップ](#次のステップ)

## 概要

4つの独立したJavaプログラムが、チャット、会話履歴、関数呼び出し、全文書検索拡張生成（RAG）、および責任あるAI応答処理を示します。すべてのチャットリクエストはデフォルトで**推論努力が `none` の GPT-5.6 Luna**を対象としています。

これらの例は、[MicrosoftのSDKガイダンス](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages)に従い、Azure OpenAIのv1エンドポイント用の公式OpenAI Java SDKを使用します。旧`azure-ai-openai`パッケージは依存関係ではなくなりました。既存のメッセージベースのワークフローを学ぶためにChat Completionsは残されています。他のAPIオプションは [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) を参照してください。

## 前提条件

- Java 21以降およびMaven 3.6.3以降。
- `gpt-5.6-luna`という名前のAzure OpenAIチャットデプロイ、または互換性のあるChat Completions設定によるオーバーライド。
- リソースに対して<strong>Cognitive Services OpenAI User</strong>ロールを持つサインイン済みAzure ID。ローカル開発はAzure CLIのサインインを使い、ホスト型アプリケーションはマネージドIDを使用可能。
- リソース設定とサインイン手順は[第2章](../02-SetupDevEnvironment/getting-started-azure-openai.md)を参照。

[Maven設定](../../../03-CoreGenerativeAITechniques/examples/pom.xml)は以下のバージョンを指定し、2026-09-14に確認済み：

| コンポーネント | バージョン | 用途 |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | 公式Azure v1互換クライアント |
| `com.azure:azure-identity` | 1.18.6 | キーレス認証とトークン更新 |
| `net.objecthunter:exp4j` | 0.4.8 | コード評価なしの算術式解析 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | オフラインJupiterユニットテスト |
| Mavenコンパイラ / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21のコンパイル、テスト、実行可能な例 |

コンパイラは `--release 21` を使用。これらの独立した例にはSpring Boot、Spring AI、LangChain4jは不要。

## 開始方法

リポジトリのルートから、リソースエンドポイントとオプションのデプロイメントオーバーライドをシェルに設定します。

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

テストはAzure資格情報やエンドポイントを必要としません。Mavenは環境ファイルを自動読み込みしないので、ライブ例を起動するシェルで変数を設定してください。IDE起動時は起動構成で供給される環境を確認してください。

## モデル選択ガイド

| 環境変数 | 意味 | デフォルト |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azureリソースルートまたは正規化済み `/openai/v1` URL | ライブ実行で必須 |
| `AZURE_OPENAI_DEPLOYMENT` | チャットデプロイ名、モデルバージョンではない | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | 別の埋め込みデプロイ設定、これら4つのプログラムでは未使用 | `text-embedding-3-small` |

空のデプロイオーバーライドはデフォルトを使用。設定は `/openai/v1` を一度だけ追加し、エンドポイント内の資格情報、クエリ文字列、旧デプロイパスは拒否。

すべてのチャットリクエストは明示的に `reasoningEffort(ReasoningEffort.NONE)` と `maxCompletionTokens(...)` を設定。`temperature`、`top_p`、旧完了トークン設定は使わず。ツール選択とツール結果の後続も含む。GPT-5.6チャット完了関数ツールは推論努力 `none` が必要；[Microsoftのチャットガイダンス](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt) を参照。

**本章にはストリーミングや埋め込み用のエントリポイントはありません。** ドキュメント全文を取得し、ベクトルではありません。埋め込みを拡張に使う場合は、Lunaではなく `text-embedding-3-small` のような別の埋め込みデプロイを使用してください。

## チュートリアル1：LLM完了とチャット

ソース：[LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)。

プログラムは単純なJavaストリームの説明、2ターンのHashMap/TreeMap会話、インタラクティブチャットを実行。2ターン目には最初のアシスタント応答が含まれます。各インタラクティブターンは前の会話も送信。

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` でデプロイと明示的な推論設定を供給。インタラクティブチャットは空行をスキップし、`exit` またはEOFで終了。システムメッセージと9回分の完了済みユーザー/アシスタントターンを保持。ターン数制限は教育的な制約であり、トークン予算の保証ではない。

examplesディレクトリから：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

最初に3つの回答があり、その後 `You:` プロンプト。各非空のインタラクティブ質問は1つのリクエストを追加。完了トークン制限はインタラクティブターンごとに200、300、400、500。

## チュートリアル2：関数呼び出し

ソース：[FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)。

SDKは注釈付きの`WeatherArguments`と`CalculationArguments`レコードからJSONスキーマを生成。必須のツール選択により各例はモデルの補助なし回答ではなくツールプロトコルを実行。

1. 許可されたツール、推論努力 `none`、300トークン完了制限で質問を送信。
2. `tool_calls`終了理由を要求し、関数名と呼び出しIDを検証、型付きJSON引数を解析。
3. ローカル関数を実行。モデルはJavaや任意のコードを実行しない。
4. 一度アシスタントのツール呼び出しメッセージを追加し、続いて各結果を対応する `tool_call_id` 付きで追加。
5. ツールなしで300トークンの最終リクエストを送り、完了かつ空でない回答を要求。

`get_weather`は<strong>シミュレートされた</strong>天気を返す。都市を尊重し、例の22度摂氏を華氏に変換可能。`calculate`はexp4jを経由して与えられた式を評価し、 `15% of 240` や `2 + 3 * 4` 形式をサポート。空白、大きすぎる、無効、非有限の計算を拒否。金融用10進精度ではなく浮動小数点算術を使用。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

`Function: get_weather`、シミュレートされたシアトルの天気、`Function: calculate`、`Function result: 36`、2つの最終回答を期待。stdinや外部天気認証は不要。正常な実行は正確に4回のチャットリクエストを使用。

## チュートリアル3：RAG（検索拡張生成）

ソース：[SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)。入力：[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)。

このRAG入門例は1つのUTF-8全文書を取得し、それを質問と一緒にユーザーメッセージに含める。別システムメッセージで、文書内容を信頼できないデータとして扱い、その文脈内のみに基づいて回答するようモデルに指示。文書に回答が含まれない場合、求める回答は：`I cannot find that information in the provided document.`。

グラウンディングは幻覚を減らせるが、区切りやシステム指示は正確さや全プロンプトインジェクション防止を保証しない。ライブ回答をレビュー。プロダクションRAGは通常、チャンク分割、検索、引用、アクセス制御、評価を追加。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

例として `Which authentication method does the document describe?` など1つの質問を入力。Microsoft Entra IDを言及する回答を予期。プログラムは1回のチャットリクエスト500トークン完了制限後に終了。

デフォルトのファイル検索はリポジトリルート、章ディレクトリ、examplesディレクトリから動作。明示的なパスもサポート：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

入力は非空でなければならず：UTF-8のドキュメントデータは最大32KiB、質問は最大2,000文字。ファイル欠落、空またはEOF質問、大きすぎる入力は推論前に失敗。

## チュートリアル4：責任あるAI

ソース：[ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)。

6つの検査は有害な命令、憎悪発言、プライバシー、医療誤情報、違法コンテンツ、および無害な責任あるAI質問をカバー。プログラムはすべての検査がフィルタを起動すべきと想定せず、応答を観察。

| 結果 | 証拠 |
| --- | --- |
| `FILTERED` | 明示的な `content_filter` / `ResponsibleAIPolicyViolation` エラーコード、または完了の `content_filter` 終了理由 |
| `REFUSED` | 非空の構造化された `message.refusal` フィールド |
| `POSSIBLE_REFUSAL` | 通常テキスト内の拒否開始フレーズ；レビューが必要なヒューリスティック |
| `GENERATED` | 完了した非空の応答；内容が安全の証明ではない |

通常のHTTP 400はフィルタリングの証拠<strong>ではありません</strong>。無効パラメーター、認証失敗、レート制限、サーバーエラー、誤形成応答、切断出力は偽の安全成功を生まず失敗となる。無害な説明中の「有害コンテンツ」など幅広い語句は拒否とは見なさない。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

6つのカテゴリー結果と観察が安全認証ではない旨の要約を期待。各検査は300トークンの完了制限。予期しない生成と可能な拒否は手動でレビュー。無害比較は実質ある責任あるAI説明を出すはず。stdin不要。

## 例を通じた共通パターン

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java)はエンドポイント正規化、デプロイオーバーライド、キーレス認証、チャットオプションを集中管理：

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

トークンサプライヤは必要に応じてアクセストークンを更新。トークンをログに記録しないこと。またこれをAPIキーに置き換えない。各プログラムは自身のクライアントを使い回し、`finally` または独自の `AutoCloseable` ラッパーで閉じる。SDKの`OpenAIClient`自体は`AutoCloseable`ではない。

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java)は完了し非空のテキスト応答を要求。空選択肢、拒否、フィルタ、切断回答を成功として黙って表示しない。責任あるAI例は予期されるフィルタ／拒否結果を明示的に処理。未処理失敗はJava/Mavenプロセスに非ゼロ終了コードを付与。

<strong>SDKの自動再試行は無効</strong>であり、共有の低RPMデプロイでリクエスト数を予測可能に保つ。各推論リクエストは60秒タイムアウト。トークン取得は追加時間を要す場合あり。アプリケーションレベルのスケジューリングはクォータを尊重する必要があり、失敗した有料リクエストを盲目的に再実行しないこと。

## ユニットテスト

examplesディレクトリから：

```powershell
mvn -B -ntp clean test
```

テストトランスポートはSDKのHTTP層を完全に置換し、実際のシリアル化済リクエストボディをキャプチャし、キューイングされた応答を供給。ソケットを開かず、Azureトークンを取得せず、予期外リクエストで失敗。これらのテストはアプリケーションの挙動とSDKプロトコルを検証し、ライブモデルの品質やデプロイの可用性は検証しない。

| テストスイート | カバレッジ |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | エンドポイント正規化／拒否、デプロイオーバーライド、推論／トークンオプション |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | すべての完了ワークフロー、メッセージ履歴、完全ターントリミング、EOF、失敗 |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | ツールスキーマ、型付き引数、算術、ID、複数ツール結果、失敗したフォローアップ |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | ファイル検索、UTF-8、サイズ制限、グラウンディングペイロード、入力とAPIエラー |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | 全6つの検査、明示的フィルタ、拒否判定、通常の400およびその他失敗 |

1つのスイートを実行するには、`mvn -B -ntp test "-Dtest=FunctionsAppTest"`を使用。共有フィクスチャは [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) にあります。

## 連続ライブ検証

ライブ呼び出しはユニットテストと別。認証情報とデプロイアクセスが準備でき次第、リポジトリルートから以下のコマンドを<strong>個別に</strong>使用。サービスや永続プロセスは不要。

共有の<strong>10リクエスト／分</strong>デプロイでは、次のプログラム全体のクォータを開始前に確保してください：5、4、1、次に6リクエスト。連続プロセスだけではレート制限遵守を保証しません。他の呼び出し者と分単位の動きを調整し、4つの呼出を無分割で一括貼り付けしないでください。

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. 完了、多ターン、2つのインタラクティブターン：**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

すべての3つのセクション見出し、5つの回答、Adaを呼び戻す最終的な対話型回答、「Goodbye!」、および終了コード0を確認してください。予算：**5リクエスト、最大1,900完了トークン**。より小さな実行には `exit` のみをパイプしてください：3リクエスト / 900トークンですが、対話型推論は実行されません。

**2. 両方の関数呼び出しワークフロー：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

両方の関数名、シミュレートされたシアトルの天気、計算結果36、2つの最終回答、および終了コード0を確認してください。予算：**4リクエスト、最大1,200完了トークン**。

**3. ドキュメントに基づく回答：**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

ドキュメントパス、Microsoft Entra IDに言及する回答、および終了コード0を確認してください。予算：**1リクエスト、最大500完了トークン**。既存の[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)が唯一必要な入力ファイルです。欠落トピックについてのオプションの2回目の実行は控えるべきで、1リクエスト/500トークンを追加します。

**4. 責任あるAIの観察：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

6つのカテゴリーと観察の概要を確認し、生成されたコンテンツをレビューし、技術的完了のために終了コード0を要求してください。プロセスが正常終了してもモデルの安全性の保証にはなりません。予算：**6リクエスト、最大1,800完了トークン**。

**これら4つのコマンドの合計：16のチャットリクエスト、最大5,400完了トークン**、および入力トークン（繰り返し会話やツールのスキーマ/履歴を含む）。埋め込みのリクエストはゼロです。実際のトークン使用量はモデルに依存し、特にフィルター付きプロンプトでは少なくなる場合があります。費用はデプロイの料金によります；固定の金額見積もりは示されません。すべてのリクエスト制限は手動再実行なしの場合です。各コマンド直後に `$LASTEXITCODE` を確認してください；ゼロ以外は実行が正常に完了していません。

## トラブルシューティング

- **エンドポイントの欠落 / 401 / 403:** 起動プロセスでエンドポイントを設定し、ローカルのAzureサインインとリソーススコープの役割を確認し、意図しないID環境の上書きをチェックしてください。
- **400 / 404:** デプロイメントが存在し、推論努力なしのチャット補完をサポートしていることを確認してください。HTTPSのリソースルートまたは `/openai/v1` URLを使用し、旧デプロイメントURLは使わないでください。通常の400エラーは技術的失敗であり安全ブロックではありません。
- **429:** 共有RPMとトークンクォータの調整を行い、再試行してください。例は自動再試行を意図していません。
- **`Incomplete chat response: length`:** 出力が完了制限に達しました。応答とプロンプトを確認し、制限とその予算を増やす前に、途中打ち切りの実行を成功として記録しないでください。
- **ファイルまたは標準入力のエラー:** 対応するディレクトリから起動するか、明示的にドキュメントパスを渡してください。空でないリーダー質問を提供してください。完了はEOFまたは `exit` で正常終了できます。
- **コンパイルエラー:** Java 21以降を確認してから `mvn -B -ntp clean test` を実行してください。PowerShellでは、点付きのプロパティを含むMaven引数全体を引用符で囲んでください。例：`"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`。

## 次のステップ

[Chapter 4: Practical Samples](../04-PracticalSamples/README.md) に進んでください。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類は AI 翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知おきください。原文の原語版が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->