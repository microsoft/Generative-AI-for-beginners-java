<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:53:00+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "zh"
}
-->
# MCP计算器初学者教程

## 目录

- [你将学到什么](../../../../../04-PracticalSamples/mcp/calculator)
- [前置条件](../../../../../04-PracticalSamples/mcp/calculator)
- [理解项目结构](../../../../../04-PracticalSamples/mcp/calculator)
- [核心组件解析](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. 主应用程序](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. 计算器服务](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. 直接MCP客户端](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI驱动客户端](../../../../../04-PracticalSamples/mcp/calculator)
- [运行示例](../../../../../04-PracticalSamples/mcp/calculator)
- [整体工作原理](../../../../../04-PracticalSamples/mcp/calculator)
- [下一步](../../../../../04-PracticalSamples/mcp/calculator)

## 你将学到什么

本教程讲解如何使用模型上下文协议（MCP）构建一个计算器服务。你将了解：

- 如何创建一个AI可以作为工具使用的服务
- 如何设置与MCP服务的直接通信
- AI模型如何自动选择使用哪些工具
- 直接协议调用与AI辅助交互的区别

## 前置条件

开始之前，请确保你已具备以下条件：
- 安装了Java 21或更高版本
- 使用Maven进行依赖管理
- 拥有一个GitHub账户并设置了个人访问令牌（PAT）
- 对Java和Spring Boot有基本了解

## 理解项目结构

计算器项目包含几个重要文件：

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## 核心组件解析

### 1. 主应用程序

**文件:** `McpServerApplication.java`

这是我们的计算器服务的入口点。它是一个标准的Spring Boot应用程序，并有一个特别的功能：

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

**功能说明:**
- 在端口8080上启动一个Spring Boot Web服务器
- 创建一个`ToolCallbackProvider`，使我们的计算器方法可以作为MCP工具使用
- `@Bean`注解告诉Spring将其管理为其他部分可以使用的组件

### 2. 计算器服务

**文件:** `CalculatorService.java`

这里是所有数学运算的核心。每个方法都用`@Tool`标记，以便通过MCP调用：

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**关键特点:**

1. **`@Tool`注解**: 告诉MCP该方法可以被外部客户端调用
2. **清晰的描述**: 每个工具都有一个描述，帮助AI模型理解何时使用它
3. **一致的返回格式**: 所有操作返回人类可读的字符串，例如"5.00 + 3.00 = 8.00"
4. **错误处理**: 除以零和负数开平方会返回错误信息

**可用操作:**
- `add(a, b)` - 两数相加
- `subtract(a, b)` - 第一个数减去第二个数
- `multiply(a, b)` - 两数相乘
- `divide(a, b)` - 第一个数除以第二个数（带零检查）
- `power(base, exponent)` - 基数的指数次幂
- `squareRoot(number)` - 计算平方根（带负数检查）
- `modulus(a, b)` - 返回除法的余数
- `absolute(number)` - 返回绝对值
- `help()` - 返回所有操作的信息

### 3. 直接MCP客户端

**文件:** `SDKClient.java`

此客户端直接与MCP服务器通信，不使用AI。它手动调用特定的计算器功能：

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**功能说明:**
1. **连接**到计算器服务器`http://localhost:8080`
2. **列出**所有可用工具（我们的计算器功能）
3. **调用**特定功能并传递精确参数
4. **直接打印**结果

**使用场景:** 当你明确知道需要执行的计算并希望以编程方式调用时。

### 4. AI驱动客户端

**文件:** `LangChain4jClient.java`

此客户端使用AI模型（GPT-4o-mini），可以自动决定使用哪些计算器工具：

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**功能说明:**
1. **创建**一个AI模型连接，使用你的GitHub令牌
2. **连接**AI到我们的计算器MCP服务器
3. **赋予**AI访问所有计算器工具的权限
4. **支持**自然语言请求，例如"计算24.5和17.3的和"

**AI自动完成:**
- 理解你想要进行加法运算
- 选择`add`工具
- 调用`add(24.5, 17.3)`
- 以自然语言返回结果

## 运行示例

### 第一步: 启动计算器服务器

首先，设置你的GitHub令牌（AI客户端需要）：

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

启动服务器:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

服务器将在`http://localhost:8080`启动。你应该看到:
```
Started McpServerApplication in X.XXX seconds
```

### 第二步: 使用直接客户端测试

在新终端中运行:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

你会看到类似以下的输出:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### 第三步: 使用AI客户端测试

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

你会看到AI自动使用工具:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## 整体工作原理

当你向AI询问"5 + 3是多少？"时，完整流程如下：

1. **你**以自然语言向AI提问
2. **AI**分析你的请求并确定你需要加法运算
3. **AI**调用MCP服务器: `add(5.0, 3.0)`
4. **计算器服务**执行: `5.0 + 3.0 = 8.0`
5. **计算器服务**返回: `"5.00 + 3.00 = 8.00"`
6. **AI**接收结果并格式化为自然语言响应
7. **你**得到: "5和3的和是8"

## 下一步

更多示例请参见[第04章: 实用样例](../../README.md)

**免责声明**：  
本文档使用AI翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们努力确保翻译的准确性，但请注意，自动翻译可能包含错误或不准确之处。原始语言的文档应被视为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误读承担责任。