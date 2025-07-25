<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T10:52:25+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "zh"
}
-->
# Foundry Local Spring Boot 教程

## 目录

- [前置条件](../../../../04-PracticalSamples/foundrylocal)
- [项目概览](../../../../04-PracticalSamples/foundrylocal)
- [代码解析](../../../../04-PracticalSamples/foundrylocal)
  - [1. 应用配置 (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. 主应用类 (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI 服务层 (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. 项目依赖 (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [整体工作流程](../../../../04-PracticalSamples/foundrylocal)
- [设置 Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [运行应用](../../../../04-PracticalSamples/foundrylocal)
- [预期输出](../../../../04-PracticalSamples/foundrylocal)
- [下一步](../../../../04-PracticalSamples/foundrylocal)
- [故障排查](../../../../04-PracticalSamples/foundrylocal)

## 前置条件

在开始本教程之前，请确保您已完成以下准备工作：

- 系统中已安装 **Java 21 或更高版本**
- 安装了 **Maven 3.6+** 用于构建项目
- 已安装并运行 **Foundry Local**

### **安装 Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## 项目概览

该项目主要由以下四个部分组成：

1. **Application.java** - Spring Boot 应用的主入口
2. **FoundryLocalService.java** - 处理 AI 通信的服务层
3. **application.properties** - Foundry Local 连接的配置文件
4. **pom.xml** - Maven 依赖和项目配置

## 代码解析

### 1. 应用配置 (application.properties)

**文件路径:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**作用:**
- **base-url**: 指定 Foundry Local 的运行地址（默认端口为 5273）
- **model**: 指定用于文本生成的 AI 模型名称

**关键概念:** Spring Boot 会自动加载这些配置，并通过 `@Value` 注解将其提供给应用程序。

### 2. 主应用类 (Application.java)

**文件路径:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**作用:**
- `@SpringBootApplication` 启用 Spring Boot 的自动配置功能
- `WebApplicationType.NONE` 指定这是一个命令行应用，而不是 Web 服务
- 主方法启动 Spring 应用

**Demo Runner:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**作用:**
- `@Bean` 创建一个由 Spring 管理的组件
- `CommandLineRunner` 在 Spring Boot 启动后运行代码
- `foundryLocalService` 通过 Spring 自动注入（依赖注入）
- 向 AI 发送测试消息并显示响应

### 3. AI 服务层 (FoundryLocalService.java)

**文件路径:** `src/main/java/com/example/FoundryLocalService.java`

#### 配置注入:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**作用:**
- `@Service` 表明该类提供业务逻辑
- `@Value` 从 application.properties 中注入配置值
- `:default-value` 语法提供了默认值，以防配置未设置

#### 客户端初始化:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**作用:**
- `@PostConstruct` 在 Spring 创建服务后运行此方法
- 创建一个指向本地 Foundry Local 实例的 OpenAI 客户端
- `/v1` 路径是 OpenAI API 兼容性所需的
- API 密钥设置为 "unused"，因为本地开发不需要认证

#### Chat 方法:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**作用:**
- **ChatCompletionCreateParams**: 配置 AI 请求
  - `model`: 指定使用的 AI 模型
  - `addUserMessage`: 将用户消息添加到对话中
  - `maxCompletionTokens`: 限制响应长度（节省资源）
  - `temperature`: 控制随机性（0.0 = 确定性，1.0 = 创造性）
- **API 调用**: 向 Foundry Local 发送请求
- **响应处理**: 安全提取 AI 的文本响应
- **错误处理**: 包装异常并提供有用的错误信息

### 4. 项目依赖 (pom.xml)

**关键依赖:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**作用:**
- **spring-boot-starter**: 提供核心 Spring Boot 功能
- **openai-java**: 官方 OpenAI Java SDK，用于 API 通信
- **jackson-databind**: 处理 API 调用的 JSON 序列化/反序列化

## 整体工作流程

运行应用时的完整流程如下：

1. **启动**: Spring Boot 启动并读取 `application.properties`
2. **服务创建**: Spring 创建 `FoundryLocalService` 并注入配置值
3. **客户端设置**: `@PostConstruct` 初始化 OpenAI 客户端以连接 Foundry Local
4. **Demo 执行**: `CommandLineRunner` 在启动后执行
5. **AI 调用**: Demo 使用测试消息调用 `foundryLocalService.chat()`
6. **API 请求**: 服务构建并发送 OpenAI 兼容的请求到 Foundry Local
7. **响应处理**: 服务提取并返回 AI 的响应
8. **显示结果**: 应用打印响应并退出

## 设置 Foundry Local

按照以下步骤设置 Foundry Local：

1. **安装 Foundry Local**，参考 [前置条件](../../../../04-PracticalSamples/foundrylocal) 部分的说明。
2. **下载所需的 AI 模型**，例如 `phi-3.5-mini`，使用以下命令：
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **配置 application.properties** 文件以匹配您的 Foundry Local 设置，特别是端口或模型名称。

## 运行应用

### 第一步: 启动 Foundry Local
```bash
foundry model run phi-3.5-mini
```

### 第二步: 构建并运行应用
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## 预期输出

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## 下一步

更多示例请参考 [第 4 章: 实用示例](../README.md)

## 故障排查

### 常见问题

**"Connection refused" 或 "Service unavailable"**
- 确保 Foundry Local 正在运行: `foundry model list`
- 验证服务是否在端口 5273: 检查 `application.properties`
- 尝试重启 Foundry Local: `foundry model run phi-3.5-mini`

**"Model not found" 错误**
- 检查可用模型: `foundry model list`
- 更新 `application.properties` 中的模型名称以完全匹配
- 如果需要，下载模型: `foundry model run phi-3.5-mini`

**Maven 编译错误**
- 确保 Java 版本为 21 或更高: `java -version`
- 清理并重新构建: `mvn clean compile`
- 检查网络连接以下载依赖项

**应用启动但无输出**
- 验证 Foundry Local 是否响应: 在浏览器中打开 `http://localhost:5273`
- 检查应用日志中的具体错误信息
- 确保模型已完全加载并准备就绪

**免责声明**：  
本文档使用AI翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们努力确保翻译的准确性，但请注意，自动翻译可能包含错误或不准确之处。应以原始语言的文档作为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误读承担责任。