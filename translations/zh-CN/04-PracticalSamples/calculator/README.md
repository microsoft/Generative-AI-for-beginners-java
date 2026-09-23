# MCP 计算器新手教程

## 目录

- [你将学到什么](#你将学到什么)
- [先决条件](#先决条件)
- [依赖版本](#依赖版本)
- [了解项目结构](#了解项目结构)
- [核心组件讲解](#核心组件讲解)
  - [1. 主应用程序](#1-主应用程序)
  - [2. 计算器服务](#2-计算器服务)
  - [3. 直接 MCP 客户端](#3-直接-mcp-客户端)
  - [4. AI 驱动客户端](#4-ai-驱动客户端)
- [运行示例](#运行示例)
- [离线测试](#离线测试)
- [整体协作流程](#整体协作流程)
- [后续步骤](#后续步骤)

## 你将学到什么

本教程介绍如何使用模型上下文协议（MCP）构建计算器服务。你将理解：

- 如何创建 AI 可以用作工具的服务
- 如何设置与 MCP 服务的直接通信
- AI 模型如何自动选择使用哪些工具
- 直接协议调用与 AI 协助交互的区别

## 先决条件

开始前，请确保你具有：
- 已安装 Java 21 或更高版本
- 使用 Maven 进行依赖管理
- 基础的 Java 和 Spring Boot 理解

仅 AI 客户端需要 Azure OpenAI 部署和经过身份验证的 `DefaultAzureCredential`，
例如本地已有 Azure CLI 登录或 Azure 托管身份。该身份需要
在资源上拥有认知服务 OpenAI 用户角色。详见 [第2章](../../02-SetupDevEnvironment/getting-started-azure-openai.md)。
服务器、直接 SDK 客户端及所有自动测试均不需要 Azure 账号或模型访问权限。

## 依赖版本

已验证于 2026-09-14 的发布依赖：

| 依赖 | 版本 |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK（Spring AI 管理） | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j 官方 OpenAI 适配器 | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter（Boot 管理） | 6.0.3 |

MCP 和官方 OpenAI 适配器为在 Maven Central 发布的 beta 版本，而非快照版本。
它们的版本与 LangChain4j core 不同，无需快照或里程碑仓库。
仅客户端依赖为测试作用域，因为可运行示例位于 `src/test/java` 目录下。

## 了解项目结构

计算器项目包含几个重要文件：

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

## 核心组件讲解

### 1. 主应用程序

**文件：** `McpServerApplication.java`

这是我们计算器服务的入口点。它是一个标准的 Spring Boot 应用，但有一个特别的附加功能：

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

**功能说明：**
- 启动一个运行在 8080 端口的 Spring Boot 网页服务器
- 创建一个 `ToolCallbackProvider`，使计算器方法作为 MCP 工具可用
- `@Bean` 注解使 Spring 管理此组件，供其他部分使用

### 2. 计算器服务

**文件：** `CalculatorService.java`

这里实现所有数学运算。每个方法均带有 `@Tool` 注解，使其可通过 MCP 调用：

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
    
    // 更多计算器操作...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**主要特性：**

1. **`@Tool` 注解**：告诉 MCP 该方法可被外部客户端调用
2. <strong>清晰描述</strong>：每个工具均有描述，帮助 AI 模型了解何时使用
3. <strong>统一返回格式</strong>：所有操作返回可读字符串，如 "5.00 + 3.00 = 8.00"
4. <strong>错误处理</strong>：除数为零和负数开平方返回错误信息

**可用操作：**
- `add(a, b)` - 加法运算
- `subtract(a, b)` - 计算第一个数减第二个数
- `multiply(a, b)` - 乘法运算
- `divide(a, b)` - 第一个数除以第二个数（含零检验）
- `power(base, exponent)` - 计算 base 的 exponent 次方
- `squareRoot(number)` - 计算平方根（含负数检验）
- `modulus(a, b)` - 计算取余数
- `absolute(number)` - 返回绝对值
- `help()` - 返回所有操作信息

### 3. 直接 MCP 客户端

请参阅 [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)。

此客户端使用位于 `/mcp` 的 `HttpClientStreamableHttpTransport`，初始化连接，
发送心跳，支持工具列表分页。检查所有九个预期工具均存在，并调用它们，
包括 `modulus` 和 `help`，无需 AI 模型。

当前请求构建代码如下：

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

协议错误会导致客户端失败，而不是误报成功。MCP 客户端使用 try-with-resources
确保在发现问题或工具调用失败时正确关闭。

### 4. AI 驱动客户端

请参阅 [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
和 [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)。

`OpenAiOfficialChatModel` 实现了当前 LangChain4j 的 `ChatModel` API。
`StreamableHttpMcpTransport` 连接至同一 `/mcp` 端点，和 SDK 客户端一致。
`AiServices` 负责发现工具并管理工具调用和结果对话。

默认部署为 **GPT-5.6 Luna**，明确禁用推理能力：

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

这些默认设置适用于所有完成请求，包括工具执行后的后续请求。
客户端使用可刷新 `BearerTokenCredential`，基于 `DefaultAzureCredential`
和 `https://ai.azure.com/.default` 范围，而非作为 API key 的一次性令牌。
既接受资源 URL，也接受已以 `/openai/v1` 结尾的 URL。

Bot 保持有限的会话历史，打印 `Tool executed: ...` 和实际 MCP 结果，
若响应跳过工具调用则失败。工具循环限制为四轮往返。
身份验证、模型、MCP 和工具错误都会传播；自动模型重试被禁用。
MCP 传输/客户端和官方 OpenAI 客户端均在成功或失败时关闭。

## 运行示例

### 第一步：启动计算器服务器

服务器无需 Azure 配置。以下命令在本示例目录下运行。
该示例使用端口 **18081**，避免与其他示例冲突；默认端口为8080。

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP 端点为 `http://localhost:18081/mcp`。健康检查和发现信息分别位于
`http://localhost:18081/health` 和 `http://localhost:18081/info`。
流媒体 HTTP 取代了旧的仅 SSE 传输；`/sse` 和 `/v1/tools` 不再是端点。

### 第二步：用直接客户端测试

在另一个 PowerShell 终端中：

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

无需输入。所有九个工具都会被测试。期望的算术结果包括
8、6、42、5、256、4、2 和 5.5，以及随后输出的帮助文本。

### 第三步：用 AI 客户端测试

经身份验证（见先决条件）后，在同一终端配置 AI 客户端：

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

期望看到一行 `Tool executed: add`，结果为 `41.80`，随后是模型回答。
单提示模式执行完毕后会直接退出，无需等待输入。若想运行原始的四次提示演示：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

演示依次调用 `add`、`squareRoot`、`help` 及串联的 `power` 和 `divide` 操作。
期望数字结果为 41.8、12 和 64。省略参数时也会运行该演示。

### 第四步：运行交互式 Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

输入 `Multiply 6 by 7 using the calculator service`，然后输入 `exit` 或 `quit`。
期望得到实际的 `multiply` 工具结果 42。空白行会被忽略；EOF 也会结束会话。
非交互式的简单测试可以通过以下命令执行：

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

两个 AI 入口均支持 `--prompt "question"`、`--demo` 和 `--interactive` 参数。
无效参数会在打开连接前失败。Maven 的每个 `-D...` 参数请在 PowerShell 中完整引用。
Bash 环境使用 `export NAME=value` 代替 `$env:NAME = "value"`。

**配额:** AI 示例请顺序运行。简单提示通常需要两次模型请求；
完整演示通常需要九次，包括工具结果的后续请求。在共享 10 RPM
部署环境下，请等待新配额窗口后再运行下一个 AI 示例。遇到 429 状态会明显失败，且没有
自动重试；请遵循服务的 retry-after 指示。实际请求次数依模型而异。
离线测试不消耗任何配额，也不检验 Luna 的实时可用性或回答质量。

### 配置与关闭

| 设置项 | 默认/行为 |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`；基础地址，不含 `/mcp` |
| `-Dmcp.server.url=...` | 覆盖所有客户端的 `MCP_SERVER_URL` |
| `AZURE_OPENAI_ENDPOINT` | 仅 AI 客户端必需；资源 URL 或 `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`；Azure 部署名称 |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`；正整数 |
| 推理努力 | 总是 `none`，包括工具循环的后续请求 |

被覆盖的部署必须支持 `reasoning_effort=none` 和 `max_completion_tokens`。
客户端不会自动读取 `.env` 文件。测试完成后使用 `Ctrl+C` 停止服务器。
客户端正常返回，无需调用 `System.exit` 或等待关闭睡眠。

## 离线测试

```powershell
mvn -B -ntp clean verify
```

所有测试均为针对 Azure 的离线测试：协议套件启动 Spring 服务器和
模拟 OpenAI 兼容服务，监听随机回环端口，然后关闭。Maven 可能仍需
下载依赖。不使用任何凭据、实时部署或预先存在的 MCP 服务器。

- 计算器单元测试涵盖所有算术操作、小数结果、帮助信息和领域错误。
- MCP 测试涵盖初始化、发现、所有九个工具调用、工具失败以及健康/信息检查。
- AI 协议测试执行完整演示和交互式 Bot，连接真实计算器，
  验证工具结果是否喂入下一步完成，检查每个 HTTP 正文包含 Luna、
  `reasoning_effort: "none"` 和 `max_completion_tokens`，无旧版 `max_tokens`。
- 配置/输入测试涵盖部署和端点覆盖、空行、EOF、退出/退出命令、
  单提示模式、无效选项和错误传播。配额测试证明 429 不重试。

## 整体协作流程

当你向 AI 询问“5 + 3 等于多少？”时，完整流程如下：

1. <strong>你</strong> 用自然语言向 AI 提问
2. **AI** 分析你的请求，识别出你需要加法
3. **AI** 调用 MCP 服务器：`add(5.0, 3.0)`
4. <strong>计算器服务</strong> 执行运算：`5.0 + 3.0 = 8.0`
5. <strong>计算器服务</strong> 返回结果：`"5.00 + 3.00 = 8.00"`
6. **AI** 接收结果并格式化自然语言回答
7. <strong>你</strong> 得到结果：“5 和 3 的和是 8”

## 后续步骤

欲了解更多示例，请参见 [第04章：实用示例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->