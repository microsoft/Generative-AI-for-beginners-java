# 为 Azure AI Foundry 设置开发环境

> 本指南为本课程中的 Java AI 应用设置 **Azure AI Foundry** 模型，使用 <strong>无密钥</strong> 认证（Microsoft Entra ID）— 无需管理 API 密钥。工具新手？请从 [开发环境指南](./README.md) 开始。

本指南设置本课程中 Java AI 应用的 **Azure AI Foundry** 模型。您有两种路径：

- **选项 A — 使用 `azd` + Bicep 进行预配（推荐）：** 一条命令以代码方式部署 Foundry 账户和模型。无需点击门户。
- **选项 B — 在 Azure AI Foundry 门户中手动创建资源**。

两种路径均使用 <strong>无密钥认证</strong>（Microsoft Entra ID）— 无需复制或泄露 API 密钥。

## 目录

- [创建了哪些内容](#创建了哪些内容)
- [先决条件](#先决条件)
- [选项 A：使用 azd + Bicep 进行预配（推荐）](#option-a-provision-with-azd--bicep-recommended)
- [选项 B：手动创建资源](#选项-b：手动创建资源)
- [配置您的环境](#配置您的环境)
- [测试您的设置](#测试您的设置)
- [接下来是什么？](#接下来是什么？)
- [资源](#资源)
- [附加资源](#附加资源)

## 创建了哪些内容

[`infra/`](../../../02-SetupDevEnvironment/infra) 中的 Bicep 模板预配：

- 一个带有项目的 **Azure AI Foundry** 账户（`Microsoft.CognitiveServices/accounts`，类型为 `AIServices`）
- 一个 <strong>聊天</strong> 部署 - GPT-5.6 Luna (`gpt-5.6-luna`)，版本 `2026-07-09`，带有 `GlobalStandard` 容量 `10`（此模型支持每分钟 10 个请求和 10,000 个令牌）
- 一个 <strong>嵌入</strong> 部署 - `text-embedding-3-small`，版本 `1`（后续章节使用）
- 一个 <strong>无密钥角色分配</strong>（`Cognitive Services OpenAI User`），使您可以通过 `az login` 登录，而无需管理密钥

## 先决条件

- 一个 [Azure 订阅](https://azure.microsoft.com/free/)
- [Azure 开发者 CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) 和 [Maven 3.9+](https://maven.apache.org/download.cgi)

## 选项 A：使用 azd + Bicep 进行预配（推荐）

在 `02-SetupDevEnvironment` 文件夹中：

```bash
cd 02-SetupDevEnvironment

# 登录（两个工具）
azd auth login
az login

# 配置Foundry账户及模型部署
azd up
```

`azd` 会提示输入 <strong>环境名称</strong>（例如 `genai-java`）、<strong>订阅</strong> 和 <strong>区域</strong>。请选择您自己的订阅和 `gpt-5.6-luna` 及 `text-embedding-3-small` 可用的区域，例如 `eastus2`。确认所选订阅在该区域内有足够的配额用于模型和部署类型；可用性和配额因订阅而异。

预配完成后，azd 会：

1. 部署 [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) 中定义的所有内容。
2. 运行后续钩子，写入 [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure)，包含您的端点和部署名称（无任何密钥）。

> **提示：** 可以随时重新运行 `azd up` 以应用更改。运行 `azd down` 可删除所有内容并停止费用产生。

要查看生成的设置：

```bash
azd env get-values
```

现在跳转到 [测试您的设置](#测试您的设置)。

## 选项 B：手动创建资源

偏好使用门户？请手动创建资源：

1. 访问 [Azure AI Foundry 门户](https://ai.azure.com/) 并登录。
2. <strong>创建项目</strong>（这也会创建一个 AI Foundry 资源）。命名为 `GenAIJava`。
3. 在您的项目中，打开 **模型 + 端点** → <strong>部署模型</strong> → <strong>部署基础模型</strong>。
4. 部署 **GPT-5.6 Luna**（模型和部署名称为 `gpt-5.6-luna`，版本为 `2026-07-09`），容量为 <strong>全球标准</strong> `10`。如果要使用嵌入示例，请重复部署 `text-embedding-3-small`，版本 `1`。
5. 在 <strong>概览</strong> 中，复制 <strong>端点</strong>（例如 `https://<resource>.openai.azure.com/`）。
6. 授予自己无密钥访问权限：在资源中打开 **访问控制（IAM）** → <strong>添加角色分配</strong> → 将 **Cognitive Services OpenAI User** 指派给您的账户。

> **仍有问题？** 请参见 [Azure AI Foundry 文档](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)。

## 配置您的环境

**如果使用了选项 A（`azd up`）**，您的设置文件已经写好 — 无需配置。跳转到 [测试您的设置](#测试您的设置)。

**如果使用了选项 B（手动）**，请自行创建示例的 `.env` 文件：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

使用您的端点编辑 `.env`（无密钥 — 认证是无密钥的）：

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

使用资源的 Azure OpenAI 端点，而非项目 URL。basic-chat 应用会将其解析至 `/openai/v1` 并配置一个明确的 Bearer 令牌客户端；无须 API 密钥。

> **安全提示：** 无需存储 API 密钥。您通过 `az login`（本地）或托管身份（在 Azure 中）使用 Microsoft Entra ID 认证。`.env` 文件只包含非机密设置，且已列入 `.gitignore`。

## 测试您的设置

确保您已登录，以便无密钥认证能获取令牌，然后运行示例：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # 如果你还没有登录
mvn clean spring-boot:run
```

您应看到来自 `gpt-5.6-luna` 模型的响应。请按顺序运行示例以保持在较小的默认配额内；如果收到 HTTP 429 错误，请等待重试间隔后再试。

> <strong>VS Code 用户：</strong>按 `F5` 运行。应用会自动加载您的 `.env` 文件。

> **完整示例：** 详情及故障排除请参见 [Azure AI Foundry 的基础聊天示例](./examples/basic-chat-azure/README.md)。

## 接下来是什么？

预配完成且示例成功运行后，您将拥有：
- 部署了 `gpt-5.6-luna` 和 `text-embedding-3-small` 的 Azure AI Foundry
- 无需管理的无密钥认证（Microsoft Entra ID）
- 一个包含端点和部署名称的本地 `.env`
- 一个准备就绪的 Java 开发环境

<strong>继续阅读</strong> [第三章：核心生成式 AI 技术](../03-CoreGenerativeAITechniques/README.md)，开始构建 AI 应用！

## 资源

- [Azure 开发者 CLI (azd)](https://aka.ms/azure-dev/install)
- [使用 Microsoft Entra ID 的无密钥认证](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 文档](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK 迁移](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 的官方 OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## 附加资源

- [下载 VS Code](https://code.visualstudio.com/Download)
- [获取 Docker Desktop](https://www.docker.com/products/docker-desktop)
- [开发容器配置](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->