# MCP Calculator 初心者向けチュートリアル

## 目次

- [学べること](#学べること)
- [前提条件](#前提条件)
- [依存関係のバージョン](#依存関係のバージョン)
- [プロジェクト構造の理解](#プロジェクト構造の理解)
- [コアコンポーネントの説明](#コアコンポーネントの説明)
  - [1. メインアプリケーション](#1-メインアプリケーション)
  - [2. 計算サービス](#2-計算サービス)
  - [3. 直接MCPクライアント](#3-直接mcpクライアント)
  - [4. AI対応クライアント](#4-ai対応クライアント)
- [例の実行](#例の実行)
- [オフラインテスト](#オフラインテスト)
- [全体の動作](#全体の動作)
- [次のステップ](#次のステップ)

## 学べること

このチュートリアルでは、Model Context Protocol (MCP) を使って計算サービスを構築する方法を説明します。以下を理解できます：

- AIがツールとして使えるサービスを作る方法
- MCPサービスと直接通信する設定方法
- AIモデルがどのツールを自動選択するか
- 直接のプロトコール呼び出しとAI支援の相違点

## 前提条件

開始前に以下が揃っていることを確認してください：
- Java 21以上がインストールされている
- 依存関係管理にMavenを使う
- JavaとSpring Bootの基本知識

AIクライアントのみAzure OpenAIのデプロイと認証済み `DefaultAzureCredential` が必要です。
例えば、ローカルのAzure CLIサインインやAzureのマネージドIDなど。IDには
Cognitive Services OpenAI User ロールが必要です。[Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md)を参照してください。
サーバー、直接SDKクライアント、およびすべての自動テストはAzureアカウントやモデルアクセスを必要としません。

## 依存関係のバージョン

2026-09-14時点で検証済みのリリース依存関係：

| 依存関係 | バージョン |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI管理) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j公式OpenAIアダプター | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot管理) | 6.0.3 |

MCPと公式OpenAIアダプターはMaven Centralのベータリリースで、スナップショットではありません。
バージョンはLangChain4jコアと異なります。スナップショットやマイルストーンリポジトリは不要です。
クライアント専用依存関係はテストスコープで、実行可能な例は `src/test/java` 配下にあります。

## プロジェクト構造の理解

計算プロジェクトにはいくつか重要なファイルがあります：

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## コアコンポーネントの説明

### 1. メインアプリケーション

**ファイル:** `McpServerApplication.java`

これは計算サービスのエントリポイントです。標準的なSpring Bootアプリケーションで、ひとつ特別な追加があります：

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**機能内容:**
- ポート8080でSpring Bootウェブサーバーを起動
- 計算メソッドをMCPツールとして利用可能にする `ToolCallbackProvider` を作成
- `@Bean`アノテーションはSpringにコンポーネントとして管理させ、他から利用可能にする

### 2. 計算サービス

**ファイル:** `CalculatorService.java`

ここで全ての計算が行われます。各メソッドは `@Tool` でマークされ、MCP経由で利用可能です：

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // さらに多くの計算機操作...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**特徴:**

1. **`@Tool`アノテーション**: 外部クライアントから呼び出せるメソッドであることを示す
2. <strong>明確な説明</strong>: 各ツールにはAIモデルがいつ使用すべきか判断しやすい説明付き
3. <strong>一貫した戻り値フォーマット</strong>: すべての演算は「5.00 + 3.00 = 8.00」のような人間に読みやすい文字列を返す
4. <strong>エラーハンドリング</strong>: ゼロ除算や負の平方根はエラーメッセージを返す

**利用可能な演算:**
- `add(a, b)` - 2つの数の加算
- `subtract(a, b)` - 2つ目を1つ目から引く
- `multiply(a, b)` - 2つの数の乗算
- `divide(a, b)` - 1つ目を2つ目で割る（ゼロチェックあり）
- `power(base, exponent)` - baseのexponent乗
- `squareRoot(number)` - 平方根を計算（負数チェックあり）
- `modulus(a, b)` - 剰余を返す
- `absolute(number)` - 絶対値を返す
- `help()` - すべての操作の情報を返す

### 3. 直接MCPクライアント

[SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)を参照してください。

このクライアントは `/mcp` で `HttpClientStreamableHttpTransport` を使い、接続を初期化し、
サーバーにpingし、ツール一覧のページネーションをたどります。予期される9つのツールすべてが
存在するかを確認し、AIモデルなしで `modulus` と `help` を含むそれぞれを呼び出します。

現在のリクエストビルダーは以下のようになっています：

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

プロトコルエラーは成功誤認ではなく、クライアントの失敗を誘発します。MCPクライアントは
try-with-resourcesでクローズされ、発見失敗やツール呼び出し失敗時も同様です。

### 4. AI対応クライアント

[LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
と [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)を参照してください。

`OpenAiOfficialChatModel` は現在のLangChain4j `ChatModel` APIを実装しています。
`StreamableHttpMcpTransport` はSDKクライアントと同じ `/mcp` エンドポイントに接続します。
`AiServices` はツールの検出とツール呼び出しや結果の会話管理を行います。

デフォルトデプロイメントは<strong>GPT-5.6 Luna</strong>で、推論は明示的に無効化されています：

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

これらのデフォルトはツール実行後のフォローアップを含めすべての補完に適用されます。
クライアントは `DefaultAzureCredential` バックのリフレッシュ可能な `BearerTokenCredential` と
`https://ai.azure.com/.default` スコープを使用し、APIキーとしての一時トークンは使いません。
リソースのURLおよび `/openai/v1`で終わるURLの両方が受け入れられます。

ボットは制限された会話履歴を保持し、実際のMCP結果付きで `Tool executed: ...` を表示し、
ツール無しのレスポンスは失敗扱いです。ツールのループは4回までです。
認証、モデル、MCP、ツールのエラーは伝播し、自動リトライは無効です。
MCPトランスポート/クライアントと公式OpenAIクライアントは成功・失敗時に閉じられます。

## 例の実行

### ステップ1: 計算サーバーの起動

サーバーにはAzure設定は不要です。以下のコマンドはこのサンプルのディレクトリで実行します。
この例ではポート **18081** を使い、別サンプルと衝突しないようにしています。デフォルトは8080です。

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCPエンドポイントは `http://localhost:18081/mcp` です。ヘルスとディスカバリー情報は
`http://localhost:18081/health` と `http://localhost:18081/info` にあります。
Streamable HTTPは旧SSEのみのトランスポートに代わるもので、`/sse` と `/v1/tools` はエンドポイントではありません。

### ステップ2: 直接クライアントでテスト

別のPowerShellターミナルで：

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

入力不要で、9つ全ツールが実行されます。期待される算術結果は
8, 6, 42, 5, 256, 4, 2, 5.5で、最後にヘルプテキストが表示されます。

### ステップ3: AIクライアントでテスト

前提条件の認証後、同じターミナルでAIクライアントを設定します：

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

`Tool executed: add` の行と `41.80` を含むモデルの回答が期待されます。
シングルプロンプトモードは入力待ちせずに終了します。元の4プロンプトデモを実行するには：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

デモは `add`, `squareRoot`, `help`, 連続した `power` と `divide` を呼びます。
期待される数値回答は41.8、12、64です。引数省略でもこのデモは実行されます。

### ステップ4: 対話型ボットを実行

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

「Multiply 6 by 7 using the calculator service」と入力し、その後 `exit` または `quit` と入力します。
実際の `multiply` ツール結果42が得られることを期待してください。空行は無視され、EOFもセッション終了です。
このエントリポイントの非対話型スモークテストは：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

両AIエントリポイントは `--prompt "question"`、`--demo`、`--interactive` を受け付けます。
無効なオプションは接続開始前にエラーになります。Mavenの `-D...` 引数はPowerShellで完全にクォートしてください。
Bashでは `$env:NAME = "value"` の代わりに `export NAME=value` を使用します。

**クォータ:** AIサンプルは順番に実行してください。単純なプロンプトは通常2回のモデル要求が必要で、
完全なデモは通常9回、ツール結果のフォローアップを含みます。共有10 RPMデプロイメントで
実行間に新しいクォータウィンドウを空けてください。429は自動リトライせず明確に失敗します；
サービスのretry-after指示に従ってください。実際のリクエスト回数はモデルに依存します。
オフラインテストはクォータを消費せず、Lunaの可用性や回答品質を確立しません。

### 設定とシャットダウン

| 設定項目 | デフォルト/挙動 |
| --- | --- |
| `MCP_SERVER_URL` | `/mcp` なしのベースURLで `http://localhost:8080` |
| `-Dmcp.server.url=...` | すべてのクライアントで `MCP_SERVER_URL` を上書き |
| `AZURE_OPENAI_ENDPOINT` | AIクライアントのみ必須。リソースURLまたは `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`。Azureのデプロイ名 |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`。正の整数 |
| 推論の努力 | 常に `none`、ツールループのフォローアップも含む |

上書きしたデプロイメントは `reasoning_effort=none` と `max_completion_tokens` をサポートする必要があります。
クライアントは `.env` ファイルを自動で読みません。テスト後は `Ctrl+C` でサーバーを停止してください。
クライアントは正常終了し、`System.exit` やシャットダウンスリープを行いません。

## オフラインテスト

```powershell
mvn -B -ntp clean verify
```

すべてのテストはAzureに対してオフラインです。プロトコルスイートはSpringサーバーと
OpenAI互換スタブをランダムループバックポートで開始し、閉じます。Mavenは依存関係のダウンロードを行う場合があります。
資格情報、生のデプロイ、既存のMCPサーバーは使用しません。

- 計算ユニットテストはすべての演算、十進数結果、ヘルプ、ドメインエラーを網羅
- MCPテストは初期化、ディスカバリー、9つのツール呼び出し、ツール失敗、ヘルス/情報をカバー
- AIプロトコルテストは本物の計算機で完全デモと対話型ボットを実行、
  ツール結果が次の補完に反映されることを検証し、Luna、
  `reasoning_effort: "none"`, `max_completion_tokens`（古い `max_tokens` なし）をすべてのHTTPボディで検査
- 設定/入力テストはデプロイとエンドポイントの上書き、空行、EOF、exit/quit、
  シングルプロンプトモード、無効オプション、エラー伝播をチェック。クォータテストでは429はリトライされません。

## 全体の動作

AIに「5 + 3は？」と尋ねたときの完全なフローは次のとおりです：

1. <strong>あなた</strong>は自然言語でAIに質問する
2. <strong>AI</strong>はリクエストを解析し、足し算だと判断する
3. <strong>AI</strong>はMCPサーバーに呼び出す：`add(5.0, 3.0)`
4. <strong>計算サービス</strong>は計算を行う：`5.0 + 3.0 = 8.0`
5. <strong>計算サービス</strong>は結果を返す：`"5.00 + 3.00 = 8.00"`
6. <strong>AI</strong>は結果を受け取り自然な返答にフォーマット
7. <strong>あなた</strong>は「5と3の合計は8です」と受け取る

## 次のステップ

さらなる例については [Chapter 04: Practical samples](../README.md) を参照してください。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類は AI 翻訳サービス [Co-op Translator](https://github.com/Azure/co-op-translator) を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知おきください。原文の原語版が正式な情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->