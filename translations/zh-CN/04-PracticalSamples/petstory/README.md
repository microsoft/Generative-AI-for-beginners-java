# 新手宠物故事生成器教程

上传宠物照片，使用 GPT-5.6 Luna 分析图片，并根据生成的描述创建故事。两个模型请求均使用 `reasoning_effort: none`。

| 组件 | 版本 |
| --- | --- |
| Java | 21 或更高 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## 目录

- [先决条件](#先决条件)
- [项目结构解析](#项目结构解析)
- [核心组件说明](#核心组件说明)
  - [1. 主应用程序](#1-主应用程序)
  - [2. Web 控制器](#2-web-控制器)
  - [3. 故事服务](#3-故事服务)
  - [4. Web 模板](#4-web-模板)
  - [5. 配置](#5-配置)
- [运行应用程序](#运行应用程序)
- [离线测试](#离线测试)
- [整体工作流程](#整体工作流程解析)
- [AI 集成理解](#ai-集成理解)
- [下一步](#下一步)

## 先决条件

开始之前，请确保您具备：
- 已安装 Java 21 或更高版本
- 使用 Maven 进行依赖管理
- 一个名为 `gpt-5.6-luna` 的 Azure AI Foundry GPT-5.6 Luna 部署，或设置 `AZURE_OPENAI_DEPLOYMENT` 覆盖指向该部署。参见 [第2章](../../02-SetupDevEnvironment/getting-started-azure-openai.md) 了解如何配置并使用 `az login` 进行无密钥认证。部署需支持图像输入和 `reasoning_effort: none`。
- 对 Java、Spring Boot 和 Web 开发有基本了解

## 项目结构解析

宠物故事项目包含几个重要文件：

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## 核心组件说明

### 1. 主应用程序

**文件：** `PetStoryApplication.java`

这是我们 Spring Boot 应用的入口点：

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**功能说明：**
- `@SpringBootApplication` 注解启用自动配置和组件扫描
- 启动嵌入式 Web 服务器（Tomcat），监听端口 8080
- 自动创建所有必要的 Spring Bean 和服务

### 2. Web 控制器

**文件：** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| 端点 | 请求 | 成功响应 |
| --- | --- | --- |
| `GET /` | 无请求体 | 带有 CSRF 令牌的 HTML 上传表单 |
| `POST /analyze-image` | `multipart/form-data`，文件字段 `image` | JSON: `{"description":"一只活泼的宠物..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`，字段 `description` | 带描述和生成故事的 HTML 结果页 |

两个 POST 端点均需从 `GET /` 获得的会话 cookie 和 CSRF 令牌。上传脚本将隐藏的 `_csrf` 值发送在 `X-CSRF-TOKEN` 头中；故事提交将其作为 `_csrf` 表单字段。API 客户端必须在请求间保留 cookie。这些是表单端点，而非 JSON 请求端点。

描述不能为空且最长不超过 1000 字符。控制器会修剪描述并在传递给服务前去除 `<`、`>`、双引号、撇号和 `&`。结果模板通过 `th:text` 逃逸模型输出。

图片验证失败返回 HTTP 400 并包含 `error` 字段；模型失败返回 HTTP 502 并包含 `error` 字段且无 `description`。无效故事描述或模型失败会重定向至 `/` 并显示错误。缺少必填字段返回 HTTP 400，缺失或无效 CSRF 令牌返回 HTTP 403。不会以成功 AI 结果展示回退描述或故事。

### 3. 故事服务

**文件：** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

官方 OpenAI Java SDK 4.63.1 调用 Azure AI Foundry 的兼容 OpenAI 聊天完成 API。Azure Identity 1.18.6 通过 `DefaultAzureCredential` 提供 Microsoft Entra 令牌，无需 API 密钥。

| 操作 | 输入 | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | 以上传的 MIME 类型编码为 base64 数据 URL 的图像字节 | 300 |
| `generateStory` | 用户消息中的宠物描述 | 800 |

两个请求均使用配置的部署，默认是 `gpt-5.6-luna`，显式设置 `ReasoningEffort.NONE`（`reasoning_effort: none`）。均不发送 `temperature` 或旧版 `max_tokens` 参数。

图像分析支持 JPEG、PNG、GIF 和 WebP，拒绝空图像和超过 10MB 的文件，描述长度限制为 1000 字符。故事提示请求一个适合家庭的短故事。空选项或空白模型内容视为错误，失败时保留原始原因供服务器诊断。应用关闭时关闭 SDK 客户端。

### 4. Web 模板

**文件：** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html)（上传表单）

页面以照片选择器开始，而非描述文本框。<strong>分析图片</strong> 预览所选照片并提交到 `/analyze-image`。成功响应显示描述，填写隐藏的 `description` 字段，并显示 <strong>生成故事</strong> 按钮。该按钮提交现有表单至 `/generate-story`。

无浏览器模型下载或 CDN 依赖。图像分析在服务器端通过配置的 Azure 部署运行。失败信息可见，且不会用伪造的描述启用故事生成。选择不同文件会清除之前的分析结果。

**文件：** `result.html`（故事展示）

展示生成的故事：

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**模板特点：**

1. **Thymeleaf 集成**：使用 `th:` 属性实现动态内容
2. <strong>响应式设计</strong>：适配移动端和桌面端的 CSS 样式
3. <strong>错误处理</strong>：向用户显示验证错误
4. <strong>上传处理</strong>：JavaScript 预览照片，发送带 CSRF 保护的 multipart 请求，并显示返回的描述

### 5. 配置

**文件：** `application.properties`

应用配置设置：

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**配置说明：**

1. <strong>文件上传</strong>：文件和完整 multipart 请求均限制在 10MB 以下；照片大小应低于此限制以保留 multipart 头空间
2. <strong>日志记录</strong>：控制执行过程中的日志输出信息
3. **Azure AI Foundry**：指定要使用的端点和模型部署（无密钥认证）
4. <strong>安全</strong>：CSRF 保护始终启用；模型诊断记录在服务器端，控制器展示通用模型失败信息

## 运行应用程序

### 步骤 1：登录并设置端点

认证为无密钥（Microsoft Entra ID），无需 API 密钥。登录并设置 Foundry 端点：

**Windows（命令提示符）：**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows（PowerShell）：**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS：**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**这样做的原因：**
- Azure AI Foundry 使用 Microsoft Entra ID 认证推理请求
- 无密钥认证意味着源码或环境中没有秘密
- 您的账户需拥有该资源的 **Cognitive Services OpenAI User** 角色

默认部署名为 `gpt-5.6-luna`。如果您的 GPT-5.6 Luna 部署名称不同，请在启动应用前于同一终端设置 `AZURE_OPENAI_DEPLOYMENT`。图像分析和故事生成均使用此设置。

### 步骤 2：构建并运行

进入项目目录：
```bash
cd 04-PracticalSamples/petstory
```

构建独立可执行 JAR 并运行所有离线测试：
```bash
mvn clean package
```

启动服务器：
```bash
mvn spring-boot:run
```

应用将启动于 `http://localhost:8080`。

另外，也可以在空闲端口启动打包的 JAR，例如：

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

在此命令下，打开 `http://localhost:8083/`。选定端口同样支持 `/analyze-image` 和 `/generate-story` 路由。

### 步骤 3：测试应用

1. <strong>打开</strong> 浏览器访问 `http://localhost:8080`
2. <strong>选择</strong> 一张清晰的宠物照片，格式为 JPEG、PNG、GIF 或 WebP，大小低于 10MB
3. <strong>点击</strong> “分析图片”，等待宠物描述输出
4. <strong>点击</strong> “生成故事” 按钮（分析成功后）
5. <strong>查看</strong> 故事，使用结果页的链接返回上传表单

成功的照片到故事流程包含两个模型调用，每个按钮一次。在线推理会消耗部署配额，并可能产生费用；在共享限速部署时请串行运行冒烟测试。主页加载不调用模型。

## 离线测试

在 sample 目录下运行：

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) 使用回环 HTTP 伪装捕获真实 OpenAI SDK 请求。检查请求的部署、`reasoning_effort: none`、令牌限制、图像载荷、输入验证、空响应和上游错误。

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) 通过 MockMvc 和模拟模型服务测试 Thymeleaf 渲染页面、上传合同、CSRF、验证、输出转义和可见失败。测试不需 Azure 凭据且不调用付费 Azure 推理。Maven 会将 Surefire 报告写入 `target/surefire-reports`。

## 整体工作流程解析

生成宠物故事时的完整流程：

1. <strong>选择照片</strong>：在上传表单中选取宠物图片
2. <strong>上传图片</strong>：“分析图片”发送带 CSRF 头的 multipart POST 到 `/analyze-image`
3. <strong>图像分析</strong>：`StoryService` 将图像发送给 GPT-5.6 Luna，推理设置为 `none`
4. <strong>显示描述</strong>：浏览器展示返回的描述并存储在表单中
5. <strong>提交故事</strong>：“生成故事”提交 `description` 和 `_csrf` 到 `/generate-story`
6. <strong>生成故事</strong>：控制器验证描述，调用相同部署，推理设置为 `none`
7. <strong>模板渲染</strong>：Thymeleaf 转义并展示描述和故事在结果页

**错误处理流程：**
若模型失败，服务器记录原因。图像分析返回 HTTP 502，浏览器显示错误，且不显示“生成故事”按钮。故事生成重定向回表单并显示错误信息。两者均不会悄无声息地替换为预写结果。

## AI 集成理解

### Azure AI Foundry（无密钥）
服务配置 SDK 使用您资源的 `/openai/v1/` 端点。`DefaultAzureCredential` 和 `AuthenticationUtil.getBearerTokenSupplier` 为 `https://ai.azure.com/.default` 提供 Microsoft Entra 令牌。本地开发可用 Azure CLI 登录；Azure 托管应用可用托管身份及所需权限。

### 提示工程
图像分析请求一段简短的可见宠物特征描述，告诉模型将图中文字作为数据，而非指令。故事生成则使用返回描述来完成另一个适合家庭的写作请求。两个调用均不启用推理，也不设置温度。

### 响应处理
共享响应处理拒绝缺失选项和空白或仅空白内容，修剪有效内容，保留上游失败原因。图像描述限制为 1000 字符，以适应后续故事表单。原始模型失败留作诊断，不展示给用户。

## 下一步

更多示例，参见 [第04章：实用示例](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->