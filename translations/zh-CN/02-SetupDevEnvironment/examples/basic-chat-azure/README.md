# 使用 Azure AI Foundry 的基础聊天 - 端到端示例

本示例是一个简单的 Spring Boot 应用程序，通过<strong>无密钥身份验证</strong>（Microsoft Entra ID）连接到 **Azure AI Foundry** 模型并测试您的设置。它使用 Spring AI 的 `ChatClient`，由<strong>官方 OpenAI Java SDK</strong>和<strong>Azure OpenAI v1</strong>端点支持。

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) 中的版本为 Spring Boot **4.1.1**、Spring AI **2.0.1**、OpenAI Java **4.63.1**、Azure Identity **1.18.6** 和 dotenv-java **3.2.0**。示例使用了 `spring-ai-starter-model-openai` 并显式声明了 `openai-java` 和 `azure-identity`；Spring AI 2 移除了旧的 Azure OpenAI starter。

## 目录

- [先决条件](#先决条件)
- [快速开始](#快速开始)
- [身份验证工作原理](#身份验证工作原理)
- [运行应用程序](#运行应用程序)
  - [使用 Maven](#使用-maven)
  - [使用 VS Code](#使用-vs-code)
  - [预期输出](#预期输出)
- [配置参考](#配置参考)
  - [环境变量](#环境变量)
  - [Spring 配置](#spring-配置)
- [故障排除](#故障排除)
  - [常见问题](#常见问题)
  - [调试模式](#调试模式)
- [后续步骤](#后续步骤)
- [资源](#资源)

## 先决条件

在运行此示例之前，请确保您具备：

- 一个带有 `gpt-5.6-luna` 部署的 Azure AI Foundry 资源 - 可通过 `azd up` 或手动按照 [Azure AI Foundry 设置指南](../../getting-started-azure-openai.md) 进行配置
- 该资源上的 **认知服务 OpenAI 用户** 角色（Bicep 模板会为您分配此角色）
- 已登录的 [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)，通过 `az login`
- Java 21+ 和 Maven 3.9+

> **无需 API 密钥** — 认证通过 Microsoft Entra ID 实现无密钥身份验证。

## 快速开始

```bash
# 1. 导航到项目
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. 登录以便无密认证可以获取令牌
az login

# 3. 配置端点
#    - 如果您运行了 `azd up`，.env 已为您写入（跳过此步骤）。
#    - 否则复制模板并设置 AZURE_OPENAI_ENDPOINT：
cp .env.example .env

# 4. 运行应用程序
mvn spring-boot:run
```

## 身份验证工作原理

本示例使用 **Microsoft Entra ID** 进行身份验证 — 无需 API 密钥。

应用程序在 [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中显式配置身份验证：

1. `azureCredential()` 使用 `AuthenticationUtil.getBearerTokenSupplier`，结合 `DefaultAzureCredential` 和 `https://ai.azure.com/.default` 范围，创建一个 `BearerTokenCredential`。
2. `azureOpenAiClient()` 使用 `OpenAIOkHttpClient.builder()` 构建一个 `OpenAIClient`，将资源端点解析为 `/openai/v1`，并通过 `.credential(...)` 提供令牌凭据。
3. `azureChatModel()` 将该客户端提供给 Spring AI 的 `OpenAiChatModel`，该模型支撑本课的 `ChatClient`。

这些显式的 bean 防止全局 `OPENAI_API_KEY` 覆盖 Azure 身份验证。仅在 YAML 中省略 API 密钥不等同于完整身份验证设置。`DefaultAzureCredential` 可在本地使用您的 `az login` 会话或 Azure 中的托管身份；所选身份必须具备上述资源角色。

## 运行应用程序

### 使用 Maven

```bash
mvn spring-boot:run
```

### 使用 VS Code

1. 在 VS Code 中打开项目
2. 按 `F5` 或使用“运行和调试”面板
3. 选择 “Spring Boot-BasicChatApplication” 配置

> <strong>注意</strong>：应用程序会从其工作目录加载 `.env` 文件，包括从 VS Code 启动时。

### 预期输出

成功运行后的示例输出（省略启动日志；响应措辞可能不同）：

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## 配置参考

### 环境变量

| 变量 | 描述 | 是否必须 | 示例 |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry（Azure OpenAI）端点 URL | 是 | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天模型部署名称 | 否 | `gpt-5.6-luna`（默认） |

> <strong>无</strong> API 密钥变量 — 身份验证基于无密钥（通过 `az login` 的 Microsoft Entra ID）。

### Spring 配置

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 设置使用 `spring.ai.openai` 前缀和扁平化的聊天属性（无 `options` 块）：

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` 是<strong>Azure 部署名称</strong>。身份验证来自上述显式 bean，而非 `api-key` 设置。本课禁用了推理并将完成令牌限制为 500；`temperature` 和旧的 `max-tokens` 保持未设置。

微软推荐 [官方 OpenAI SDK 与 Azure OpenAI v1 及 Responses API 用于新应用](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)。对于这个现有基于消息的课程，Chat Completions 仍然受支持。对于 GPT-5.6，包含工具的 Chat Completions 请求必须将 `reasoning_effort` 设置为 `none`；结合推理与工具时请使用 Responses。详见 [带推理模型的工具调用](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)。

## 故障排除

### 常见问题

<details>
<summary><strong>错误：401 / “PermissionDenied” / 令牌错误</strong></summary>

- 运行 `az login` — 无密钥身份验证需要已登录状态以获取令牌
- 验证您的账户是否具备资源上的 **认知服务 OpenAI 用户** 角色
- 如果您刚分配角色，请等待一分钟以让其生效
- 确认您处于正确的租户/订阅（`az account show`）
</details>

<details>
<summary><strong>错误：“端点无效” / 连接错误</strong></summary>

- 确保 `AZURE_OPENAI_ENDPOINT` 是完整的基础 URL（例如 `https://your-resource.openai.azure.com/`）
- 检查尾部斜杠一致性
- 验证端点是否与您配置的资源匹配（`azd env get-values`）
</details>

<details>
<summary><strong>错误：“未找到部署”</strong></summary>

- 验证 `AZURE_OPENAI_DEPLOYMENT` 是否与 Azure 中的部署名称匹配
- 检查模型是否成功部署并处于活动状态
- 默认部署名称为 `gpt-5.6-luna`
</details>

<details>
<summary><strong>错误：429 / 超过速率限制</strong></summary>

- 默认的 GPT-5.6 Luna 部署具备全球标准容量 10：每分钟 10 次请求和每分钟 10,000 个令牌
- 顺序运行示例并等待服务的重试间隔后再重试
- 该基础示例禁用了 SDK 的自动重试，因此请求失败会直接报错
</details>

<details>
<summary><strong>VS Code：环境变量未加载</strong></summary>

- 确保 `.env` 文件位于项目根目录（与 `pom.xml` 同级）
- 尝试在 VS Code 的集成终端中运行 `mvn spring-boot:run`
- 检查 VS Code Java 扩展是否正确安装
</details>

### 调试模式

要启用详细日志，请取消注释 [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 中的以下行：

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## 后续步骤

**设置完成！** 继续您的学习之旅：

[第 3 章：核心生成式 AI 技术](../../../03-CoreGenerativeAITechniques/README.md)

## 资源

- [Spring AI 2 到 OpenAI Java SDK 迁移](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 与 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [使用 Microsoft Entra ID 实现无密钥身份验证](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 门户](https://ai.azure.com/)
- [Azure AI Foundry 文档](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->