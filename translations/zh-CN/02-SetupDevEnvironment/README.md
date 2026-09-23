# Java 生成式 AI 开发环境搭建

> **快速开始:** 通过 Bicep + `azd` 在几分钟内，将您的 AI 模型以代码形式部署到 **Azure AI Foundry** — 详见 [Azure AI Foundry 设置指南](getting-started-azure-openai.md)。认证采用<strong>无密钥</strong>方式（Microsoft Entra ID），无需管理 API 密钥。

## 您将学习到的内容

- 搭建用于 AI 应用的 Java 开发环境
- 选择并配置您偏好的开发环境（优先云端 Codespaces，或本地开发容器，或完全本地环境）
- 通过连接 Azure AI Foundry 模型来测试您的环境

## 目录

- [您将学习到的内容](#您将学习到的内容)
- [简介](#简介)
- [步骤 1：设置开发环境](#步骤-1：设置开发环境)
  - [选项 A：GitHub Codespaces（推荐）](#选项-a：github-codespaces（推荐）)
  - [选项 B：本地开发容器](#选项-b：本地开发容器)
  - [选项 C：使用现有本地安装](#选项-c：使用现有本地安装)
- [步骤 2：部署 Azure AI Foundry](#步骤-2：部署-azure-ai-foundry)
- [步骤 3：测试环境](#步骤-3：测试环境)
- [故障排除](#故障排除)
- [总结](#总结)
- [下一步](#下一步)

## 简介

本章将指导您搭建开发环境。课程中我们将使用 **Azure AI Foundry** 作为模型。您可用 Bicep 和 Azure Developer CLI（`azd`）将模型以代码方式部署，然后用<strong>无密钥认证</strong>（Microsoft Entra ID）连接——无需复制或泄露 API 密钥。

**无需本地设置！** 你可以使用 GitHub Codespaces，它在浏览器中提供完整开发环境，并从那里部署 Foundry。

我们选择 **Azure AI Foundry** 作为课程模型服务，因为它：
- <strong>以代码形式部署</strong> — 一条 `azd up` 命令即可部署账号及模型
- <strong>无密钥认证</strong> — 使用您的 Azure 登录或托管身份验证
- <strong>适合生产</strong> — 同一套代码在本地和 Azure 上均可运行
- <strong>灵活性强</strong> — 只需更改部署名称即可替换模型，代码无需改动

> <strong>提示</strong>：Azure AI Foundry 部署按 token 计费（按量付费）。详见 [Azure AI Foundry 设置指南](getting-started-azure-openai.md) 获取部署、区域及费用详情。


## 步骤 1：设置开发环境

<a name="quick-start-cloud"></a>

我们为本 Java 生成式 AI 课程提前创建了预配置的开发容器，以减少设置时间并确保您拥有所有必要的工具。请选择您偏好的开发方式：

### 环境选择：

#### 选项 A：GitHub Codespaces（推荐）

**2 分钟开始编码——无需本地配置！**

1. 将此仓库 Fork 到您的 GitHub 账户
   > <strong>提示</strong>：如需编辑基础配置，请查看 [Dev Container 配置](../../../.devcontainer/devcontainer.json)
2. 点击 **Code** → **Codespaces** 标签 → **...** → **New with options...**
3. 使用默认设置 — 将选择为本课程定制的 **生成式 AI Java 开发环境** 开发容器配置
4. 点击 **Create codespace**
5. 等待约 2 分钟，环境准备就绪
6. 继续进行 [步骤 2：部署 Azure AI Foundry](#步骤-2：部署-azure-ai-foundry)

<img src="../../../translated_images/zh-CN/codespaces.9945ded8ceb431a5.webp" alt="Screenshot: Codespaces submenu" width="50%">

<img src="../../../translated_images/zh-CN/image.833552b62eee7766.webp" alt="Screenshot: New with options" width="50%">

<img src="../../../translated_images/zh-CN/codespaces-create.b44a36f728660ab7.webp" alt="Screenshot: Create codespace options" width="50%">


> **Codespaces 优势**：
> - 无需本地安装
> - 任何带浏览器的设备均可使用
> - 预装所有工具和依赖
> - 个人账户每月免费 60 小时
> - 为所有学习者提供一致环境

#### 选项 B：本地开发容器

**适合偏好用 Docker 进行本地开发的开发者**

1. Fork 并克隆此仓库到本地
   > <strong>提示</strong>：如需编辑基础配置，请查看 [Dev Container 配置](../../../.devcontainer/devcontainer.json)
2. 安装 [Docker Desktop](https://www.docker.com/products/docker-desktop/) 和 [VS Code](https://code.visualstudio.com/)
3. 在 VS Code 中安装 [Dev Containers 扩展](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
4. 在 VS Code 中打开仓库文件夹
5. 出现提示时，点击 **Reopen in Container** （或使用 `Ctrl+Shift+P` → "Dev Containers: Reopen in Container"）
6. 等待容器构建并启动
7. 继续进行 [步骤 2：部署 Azure AI Foundry](#步骤-2：部署-azure-ai-foundry)

<img src="../../../translated_images/zh-CN/devcontainer.21126c9d6de64494.webp" alt="Screenshot: Dev container setup" width="50%">

<img src="../../../translated_images/zh-CN/image-3.bf93d533bbc84268.webp" alt="Screenshot: Dev container build complete" width="50%">

#### 选项 C：使用现有本地安装

**适合已有 Java 环境的开发者**

前提条件：
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) 或您偏好的 IDE

操作步骤：
1. 克隆此仓库至本地
2. 在您的 IDE 中打开项目
3. 继续进行 [步骤 2：部署 Azure AI Foundry](#步骤-2：部署-azure-ai-foundry)

> <strong>专业提示</strong>：如果您的电脑配置较低但想用本地 VS Code，请使用 GitHub Codespaces！您可将本地 VS Code 连接到云端 Codespace，实现两者兼得。

<img src="../../../translated_images/zh-CN/image-2.fc0da29a6e4d2aff.webp" alt="Screenshot: created local devcontainer instance" width="50%">


## 步骤 2：部署 Azure AI Foundry

把课程提供的 AI 模型作为代码部署到 Azure AI Foundry。操作路径为仓库根目录：

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` 会提示您输入环境名称、订阅和区域，自动部署带有 `gpt-5.6-luna` 和 `text-embedding-3-small` 的 Azure AI Foundry 账号及模型，并将端点写入示例的 `.env` 文件 —— 全过程使用 <strong>无密钥认证</strong>（无 API 密钥）。

> **完整指导：** 详见 [Azure AI Foundry 设置指南](getting-started-azure-openai.md) 获取前提条件、手动（门户）替代方案、区域建议及费用/清理说明。

## 步骤 3：测试环境

Foundry 模型部署完成后，使用 [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) 中的示例应用测试连接。

1. 在您的开发环境中打开终端
2. 进入示例目录：
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. 确认已登录（无密钥认证需令牌）：
   ```bash
   az login
   ```
   > 如果您执行过 `azd up`，`.env` 文件包含您的端点信息，已自动生成。
4. 运行应用：
   ```bash
   mvn clean spring-boot:run
   ```

您应当看到来自 `gpt-5.6-luna` 模型的响应。

### 示例代码解析

[basic-chat 示例](./examples/basic-chat-azure/README.md) 使用了 **Spring Boot 4.1.1** 和 **Spring AI 2.0.1**。Spring AI 的 `ChatClient` 基于官方 OpenAI Java SDK，连接 Azure OpenAI **v1** 端点，采用无密钥认证。

**代码功能说明：**
- 使用您的 Azure 登录（Microsoft Entra ID）连接 Azure AI Foundry — 无需 API 密钥
- 给 `gpt-5.6-luna` 模型发送提示
- 接收并展示 AI 回复
- 验证您的环境是否正常工作

<strong>关键依赖</strong>（节选自 [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)）：
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
</dependency>
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>${azure-identity.version}</version>
</dependency>
```

POM 显式管理 OpenAI Java **4.63.1**，同时显式设置 Azure Identity **1.18.6**。Spring AI 2 已移除特定 Azure 启动器，但仍需要 Azure Identity 进行凭证 Bean 配置。

<strong>配置文件</strong>（[application.yml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)）：
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

无密钥认证在 [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中明确配置，不依赖于缺失的 API 密钥推断。其承载凭证使用 `DefaultAzureCredential`，范围为 `https://ai.azure.com/.default`，`OpenAIClient` 目标为 `/openai/v1`。应用向 Spring AI 的聊天模型提供该客户端，因此全局 `OPENAI_API_KEY` 无法覆盖 Azure 认证。

聊天设置直接置于 `spring.ai.openai.chat` 下，无 `options` 块。本节保留 Chat Completions 并设置 `reasoning-effort: none` 和 500 token 完成上限；未设置 `temperature` 或 `max-tokens`。详见 [示例配置参考](./examples/basic-chat-azure/README.md#spring-configuration) 关于 API 选择和工具调用指引。

## 总结

完成以上步骤后，您将拥有：

- 通过 Bicep + `azd` 以代码形式部署的 Azure AI Foundry 模型
- 运行良好的 Java 开发环境（无论是 Codespaces、开发容器还是本地）
- 基于无密钥认证（Microsoft Entra ID）连接的 Azure AI Foundry — 无需 API 密钥
- 使用与您的模型通信的简单示例进行功能验证

## 下一步

[第 3 章：核心生成式 AI 技术](../03-CoreGenerativeAITechniques/README.md)

## 故障排除

遇到问题？这里列出了常见问题及解决方案：

- **认证失败（401/403）？** 
  - 运行 `az login` — 认证为无密钥，须先登录
  - 确认您的账户拥有资源上的 **认知服务 OpenAI 用户** 角色
  - 若刚刚部署，等待几分钟以让角色分配生效

- **找不到 Maven？** 
  - 使用开发容器/Codespaces 时，Maven 应预装
  - 本地设置时，确保已安装 Java 21+ 和 Maven 3.9+
  - 可用 `mvn --version` 验证安装情况

- **找不到 `azd` 或部署失败？** 
  - 安装 [Azure Developer CLI](https://aka.ms/azure-dev/install) 并运行 `azd auth login`
  - 选择 `gpt-5.6-luna` 和 `text-embedding-3-small` 可用的区域（如 `eastus2`），且确保订阅中有足够配额
  - 详见 [Azure AI Foundry 设置指南](getting-started-azure-openai.md)

- **开发容器启动失败？** 
  - 确认已启动 Docker Desktop（本地开发必备）
  - 尝试重建容器：`Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **应用编译错误？**
  - 确认在正确目录：`02-SetupDevEnvironment/examples/basic-chat-azure`
  - 尝试清理并重建：`mvn clean compile`

> **需要帮助？**：如果仍有问题，请在仓库中开启 issue，我们将协助您解决。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->