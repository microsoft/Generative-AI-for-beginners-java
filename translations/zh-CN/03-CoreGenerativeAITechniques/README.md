# 核心生成式人工智能技术教程

## 目录

- [先决条件](#先决条件)
- [快速开始](#快速开始)
- [模型选择指南](#模型选择指南)
- [教程 1：LLM 补全与聊天](#教程-1：llm-补全与聊天)
- [教程 2：函数调用](#教程-2：函数调用)
- [教程 3：RAG（检索增强生成）](#教程-3：rag（检索增强生成）)
- [教程 4：负责任的人工智能](#教程-4：负责任的人工智能)
- [示例中的常见模式](#示例中的常见模式)
- [单元测试](#单元测试)
- [顺序现场验证](#顺序现场验证)
- [故障排除](#故障排除)
- [后续步骤](#下一步)

## 概述

四个独立的 Java 程序展示了聊天、会话历史、函数调用、全文本检索增强生成（RAG）和负责任的 AI 响应处理。所有聊天请求默认目标为**带有推理努力为 `none` 的 GPT-5.6 Luna**。

这些示例使用官方 OpenAI Java SDK，连接 Azure OpenAI v1 端点，遵循 [微软的 SDK 指南](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages)。旧版的 `azure-ai-openai` 包不再是依赖。聊天补全保留以教授现有基于消息的工作流程；其他 API 选项请参见 [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure)。

## 先决条件

- Java 21 或更高版本及 Maven 3.6.3 或更高版本。
- 一个名为 `gpt-5.6-luna` 的 Azure OpenAI 聊天部署，或兼容的聊天补全设置的替代配置。
- 一个已登录且具备资源上 **认知服务 OpenAI 用户** 角色的 Azure 身份。 本地开发使用你的 Azure CLI 登录；托管应用可用托管身份。
- 资源设置和登录说明见 [第 2 章](../02-SetupDevEnvironment/getting-started-azure-openai.md)。

[Maven 配置](../../../03-CoreGenerativeAITechniques/examples/pom.xml)固定了以下版本，检查时间为 2026-09-14：

| 组件 | 版本 | 用途 |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | 官方 Azure v1 兼容客户端 |
| `com.azure:azure-identity` | 1.18.6 | 无密钥认证与令牌刷新 |
| `net.objecthunter:exp4j` | 0.4.8 | 算术表达式解析，无代码执行 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | 离线 Jupiter 单元测试 |
| Maven 编译器 / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 编译、测试、可运行示例 |

编译器使用 `--release 21`。这些独立示例不需要 Spring Boot、Spring AI 或 LangChain4j 依赖。

## 快速开始

从仓库根目录，在你的 shell 中设置资源端点及可选的部署覆盖。

**Windows PowerShell：**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS：**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

测试不需要 Azure 凭据或端点。Maven 不会自动读取环境文件；请在启动现场示例的 shell 中设置变量。对于 IDE 启动，请核实启动配置提供的环境。

## 模型选择指南

| 环境变量 | 含义 | 默认值 |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure 资源根或已标准化的 `/openai/v1` URL | 现场运行必需 |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天部署名称，不是模型版本 | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | 独立嵌入部署配置，本四程序不使用 | `text-embedding-3-small` |

空部署覆盖使用默认值。配置仅追加一次 `/openai/v1`，且在端点中拒绝凭据、查询字符串及旧部署路径。

每个聊天请求显式设置 `reasoningEffort(ReasoningEffort.NONE)` 和 `maxCompletionTokens(...)`。无请求设置 `temperature`、`top_p` 或旧有的完成令牌选项。包括工具选择和工具结果跟进。GPT-5.6 聊天补全功能工具需推理努力为 `none`；详见 [微软聊天指导](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)。

**本章无流式或嵌入入口。** 读取其全部文档，而非向量。如果扩展使用嵌入，请使用单独的嵌入部署如 `text-embedding-3-small`，切勿用 Luna。

## 教程 1：LLM 补全与聊天

源码：[LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)。

程序运行一个简单的 Java 流解释、一次两轮 HashMap/TreeMap 对话和交互式聊天。第二轮包含第一轮助手回应；每个交互轮也发送其之前的对话。

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` 提供部署与显式推理设置。交互聊天跳过空行，在输入 `exit` 或 EOF 时结束，保留系统消息及九轮完整用户/助手交互。轮次限制是教学约束，不是严格的令牌预算保证。

从 examples 目录：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

期望三段初始回答，然后显示 `You:` 提示。每个非空交互问题增加一次请求。交互轮补全限制依次为 200、300、400、500 令牌。

## 教程 2：函数调用

源码：[FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)。

SDK 从带注解的 `WeatherArguments` 和 `CalculationArguments` 记录派生 JSON 模式。每个示例必须选择工具，强制使用工具协议，而非接受模型无辅助回答。

1. 发送允许的工具问题，推理努力 `none`，补全限制 300 令牌。
2. 要求 `tool_calls` 结束理由，验证函数名与调用 ID，并解析类型化 JSON 参数。
3. 执行本地函数。模型不执行 Java 或任意代码。
4. 添加一次助手工具调用消息，随后添加每个带匹配 `tool_call_id` 的结果。
5. 发送最终一条 300 令牌限制且无工具请求，要求完整非空回答。

`get_weather` 返回<strong>模拟</strong>天气而非实时。尊重城市设置，且在请求时将示例的 22 摄氏度转换为华氏度。`calculate` 通过 exp4j 计算表达式，支持 `15% of 240` 和 `2 + 3 * 4` 等形式，拒绝空白、超大、无效或非有限计算，使用浮点运算而非金融小数精度。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

期望出现 `Function: get_weather`、模拟的西雅图天气、`Function: calculate`、`Function result: 36` 和两段最终回答。不需 stdin 或外部天气凭据。一次成功执行刚好四个聊天请求。

## 教程 3：RAG（检索增强生成）

源码：[SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)。输入：[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)。

该 RAG 入门示例检索一个完整的 UTF-8 文档，并将其包含在带问题的用户消息中。另有独立系统消息指示模型将文档内容视为不可信数据，仅从该上下文回答。如果文档不包含答案，回应为：`我无法在提供的文档中找到该信息。`

用基础数据可降低幻觉，但分隔符或系统指令不能保证准确性或阻止所有提示注入。请审核现场答复。生产 RAG 通常附加分段、检索、引用、访问控制和评估。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

输入一个问题，例如 `文档描述了哪种身份验证方法？`。期望回答提到 Microsoft Entra ID。程序在一条带 500 令牌补全限制的聊天请求后退出。

默认文件查找从仓库根目录、章节目录或 examples 目录起点。也支持显式路径：

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

输入必须非空：最多 32 KiB 的 UTF-8 文档数据和 2,000 字符问题。缺失文件、空白/EOF 问题和超大输入在推理前失败。

## 教程 4：负责任的人工智能

源码：[ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)。

六个探测点涵盖有害指令、仇恨言论、隐私、医疗误导、非法内容和一个良性负责任AI问题。程序观察回应，不假设每个探测都必须触发过滤。

| 结论 | 证据 |
| --- | --- |
| `FILTERED` | 明确的 `content_filter` / `ResponsibleAIPolicyViolation` 错误码，或补全的 `content_filter` 结束原因 |
| `REFUSED` | 非空的结构化 `message.refusal` 字段 |
| `POSSIBLE_REFUSAL` | 普通文本中的开头拒绝短语；需人工复审的启发式判定 |
| `GENERATED` | 完成且非空回答；非内容安全证明 |

普通 HTTP 400 错误<strong>不</strong>视为过滤证据。无效参数、认证失败、限速、服务器错误、格式错误响应和截断输出导致失败，而非错误的安全成功。良性说明中的“有害内容”等词不算拒绝。

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

期望六个类别结果和一段未构成安全认证的摘要。每个探测补全限 300 令牌。手动复审异常生成和可能拒绝；良性对照应产生实质负责任AI说明。不需 stdin。

## 示例中的常见模式

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) 集中处理端点标准化、部署覆盖、无钥认证和聊天选项：

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

令牌供应器按需刷新访问令牌。不要记录令牌或用 API 密钥替换。每个程序重用客户端并在 `finally` 中或通过自有 `AutoCloseable` 包装关闭；SDK 的 `OpenAIClient` 自身非 `AutoCloseable`。

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) 需要完成且非空文本回答。空选项、拒绝、过滤和截断回答不会默默打印为成功。负责任 AI 示例显式处理预期过滤/拒绝结果。未处理失败让 Java/Maven 进程返回非零退出码。

**自动 SDK 重试被禁用**，保持共享低 RPS 部署请求数可预测。每个推理请求有60秒超时。获取令牌可能需额外时间。应用级调度务必遵守配额；勿盲目重试失败的付费请求。

## 单元测试

从 examples 目录：

```powershell
mvn -B -ntp clean test
```

测试传输层完全替代 SDK HTTP 层，捕获实际序列化请求体，提供排队响应。不打开套接字，不获取 Azure 令牌，遇到意外请求失败。这些测试检验应用行为和 SDK 协议，而非实时模型质量或部署可用性。

| 测试套件 | 覆盖范围 |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | 端点标准化/拒绝，部署覆盖，推理与令牌选项 |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | 每个补全工作流，消息历史，完整轮修剪，EOF，失败 |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | 工具模式，类型化参数，算术，ID，多工具结果，失败的跟进 |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | 文件查找，UTF-8，大小限制，基础负载，输入与 API 错误 |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | 全部六个探测点，明确过滤，拒绝分类，普通 400 及其它失败 |

单个套件示例：使用 `mvn -B -ntp test "-Dtest=FunctionsAppTest"`。共享夹具保存在 [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)。

## 顺序现场验证

现场调用独立于单元测试。下面命令需<strong>单独</strong>从仓库根目录执行，仅在凭据和部署访问就绪后运行。无需服务或持久进程。

对于共用的 **10 请求/分钟** 部署，启动前请为整个下一个程序预留足够额度：5、4、1、然后 6 请求。单独顺序进程不保证速率限制合规。请与所有其他调用方协调滚动分钟；勿将四次调用粘贴为无节奏批处理。

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. 补全，多轮以及两轮交互：**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

检查所有三个章节标题、五个答案、一个最终交互式答案回忆Ada、`Goodbye!`，以及退出码0。预算：**5次请求，最多1900个完成标记**。要小规模运行，仅传递`exit`：3次请求 / 900标记，但那不会执行交互式推理。

**2. 两个函数调用工作流均包含：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

检查两个函数名称、模拟的西雅图天气、计算结果36、两个最终答案，以及退出码0。预算：**4次请求，最多1200个完成标记**。

**3. 基于文档的答案：**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

检查文档路径、包含Microsoft Entra ID的答案，以及退出码0。预算：**1次请求，最多500个完成标记**。现有的[document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)是唯一必需的输入文件。可选的第二次运行询问缺失主题应放弃，并新增1次请求 / 500标记。

**4. 负责任的 AI 观察：**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

检查六个类别和观察总结，审查生成内容，并要求退出码0以完成技术操作。成功的进程退出不代表模型安全。预算：**6次请求，最多1800个完成标记**。

**四个命令总计：16次聊天请求，最多5400个完成标记**，加上输入标记（包含重复对话和工具架构/历史）。零嵌入请求。具体标记使用依赖模型，可能更低，尤其是过滤提示时。美元成本取决于部署定价；不包含固定货币估算。所有请求限制假设无人工重跑。每条命令后立即检查`$LASTEXITCODE`；非零表示运行未成功完成。

## 故障排除

- **缺少端点 / 401 / 403:** 在启动过程中设置端点，验证本地Azure登录和资源范围角色，检查是否存在意外的身份环境覆盖。
- **400 / 404:** 确认部署存在且支持不带推理努力的聊天补全。使用HTTPS资源根或`/openai/v1` URL，而非旧部署URL。普通400错误为技术故障，不是安全拦截。
- **429:** 协调共享RPM和令牌配额后重试。示例故意不自动重试。
- **`不完整的聊天响应：长度`:** 输出到达完成限制。审查响应与提示内容后再增加限制和文档预算；不要将截断的运行记录为成功。
- **文件或标准输入错误:** 从支持的目录启动或传递明确的文档路径。提供非空读者问题。补全可在EOF或`exit`时正常结束。
- **编译错误:** 确认Java 21或更高版本，然后运行`mvn -B -ntp clean test`。在PowerShell中，整个包含点属性的Maven参数需加引号，例如`"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`。

## 下一步

继续阅读[第4章：实用示例](../04-PracticalSamples/README.md)。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->