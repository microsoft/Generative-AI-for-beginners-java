<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:03:14+00:00",
  "source_file": "README.md",
  "language_code": "zh"
}
-->
# 初学者的生成式 AI - Java 版
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![初学者的生成式 AI - Java 版](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.zh.png)

**时间投入**：整个工作坊可以在线完成，无需本地设置。环境设置仅需2分钟，探索示例根据深度可能需要1-3小时。

> **快速开始**

1. 将此仓库 Fork 到你的 GitHub 账户
2. 点击 **Code** → **Codespaces** 标签 → **...** → **New with options...**
3. 使用默认设置——这将选择为本课程创建的开发容器
4. 点击 **Create codespace**
5. 等待约2分钟，环境准备完成
6. 直接跳转到[第一个示例](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## 多语言支持

### 通过 GitHub Action 支持（自动更新，始终保持最新）

[法语](../fr/README.md) | [西班牙语](../es/README.md) | [德语](../de/README.md) | [俄语](../ru/README.md) | [阿拉伯语](../ar/README.md) | [波斯语（法尔西语）](../fa/README.md) | [乌尔都语](../ur/README.md) | [中文（简体）](./README.md) | [中文（繁体，澳门）](../mo/README.md) | [中文（繁体，香港）](../hk/README.md) | [中文（繁体，台湾）](../tw/README.md) | [日语](../ja/README.md) | [韩语](../ko/README.md) | [印地语](../hi/README.md) | [孟加拉语](../bn/README.md) | [马拉地语](../mr/README.md) | [尼泊尔语](../ne/README.md) | [旁遮普语（古木基文）](../pa/README.md) | [葡萄牙语（葡萄牙）](../pt/README.md) | [葡萄牙语（巴西）](../br/README.md) | [意大利语](../it/README.md) | [波兰语](../pl/README.md) | [土耳其语](../tr/README.md) | [希腊语](../el/README.md) | [泰语](../th/README.md) | [瑞典语](../sv/README.md) | [丹麦语](../da/README.md) | [挪威语](../no/README.md) | [芬兰语](../fi/README.md) | [荷兰语](../nl/README.md) | [希伯来语](../he/README.md) | [越南语](../vi/README.md) | [印尼语](../id/README.md) | [马来语](../ms/README.md) | [塔加洛语（菲律宾语）](../tl/README.md) | [斯瓦希里语](../sw/README.md) | [匈牙利语](../hu/README.md) | [捷克语](../cs/README.md) | [斯洛伐克语](../sk/README.md) | [罗马尼亚语](../ro/README.md) | [保加利亚语](../bg/README.md) | [塞尔维亚语（西里尔文）](../sr/README.md) | [克罗地亚语](../hr/README.md) | [斯洛文尼亚语](../sl/README.md) | [乌克兰语](../uk/README.md) | [缅甸语（缅甸）](../my/README.md)

## 课程结构与学习路径

### **第一章：生成式 AI 简介**
- **核心概念**：了解大型语言模型、tokens、嵌入以及 AI 的能力
- **Java AI 生态系统**：Spring AI 和 OpenAI SDK 的概述
- **模型上下文协议**：介绍 MCP 及其在 AI 代理通信中的作用
- **实际应用**：包括聊天机器人和内容生成的真实场景
- **[→ 开始第一章](./01-IntroToGenAI/README.md)**

### **第二章：开发环境设置**
- **多提供商配置**：设置 GitHub Models、Azure OpenAI 和 OpenAI Java SDK 集成
- **Spring Boot + Spring AI**：企业 AI 应用开发的最佳实践
- **GitHub Models**：免费 AI 模型访问，用于原型设计和学习（无需信用卡）
- **开发工具**：Docker 容器、VS Code 和 GitHub Codespaces 配置
- **[→ 开始第二章](./02-SetupDevEnvironment/README.md)**

### **第三章：核心生成式 AI 技术**
- **提示工程**：优化 AI 模型响应的技术
- **嵌入与向量操作**：实现语义搜索和相似性匹配
- **检索增强生成（RAG）**：将 AI 与自己的数据源结合
- **函数调用**：通过自定义工具和插件扩展 AI 功能
- **[→ 开始第三章](./03-CoreGenerativeAITechniques/README.md)**

### **第四章：实际应用与项目**
- **宠物故事生成器** (`petstory/`)：使用 GitHub Models 进行创意内容生成
- **Foundry 本地演示** (`foundrylocal/`)：使用 OpenAI Java SDK 的本地 AI 模型集成
- **MCP 计算器服务** (`calculator/`)：使用 Spring AI 实现基本的模型上下文协议
- **[→ 开始第四章](./04-PracticalSamples/README.md)**

### **第五章：负责任的 AI 开发**
- **GitHub Models 安全性**：测试内置内容过滤和安全机制（硬性阻止和软性拒绝）
- **负责任的 AI 演示**：实践展示现代 AI 安全系统的工作方式
- **最佳实践**：伦理 AI 开发和部署的基本指南
- **[→ 开始第五章](./05-ResponsibleGenAI/README.md)**

## 其他资源

- [MCP 初学者指南](https://github.com/microsoft/mcp-for-beginners)
- [AI 代理初学者指南](https://github.com/microsoft/ai-agents-for-beginners)
- [使用 .NET 的生成式 AI 初学者指南](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [使用 JavaScript 的生成式 AI 初学者指南](https://github.com/microsoft/generative-ai-with-javascript)
- [生成式 AI 初学者指南](https://github.com/microsoft/generative-ai-for-beginners)
- [机器学习初学者指南](https://aka.ms/ml-beginners)
- [数据科学初学者指南](https://aka.ms/datascience-beginners)
- [AI 初学者指南](https://aka.ms/ai-beginners)
- [网络安全初学者指南](https://github.com/microsoft/Security-101)
- [Web 开发初学者指南](https://aka.ms/webdev-beginners)
- [物联网初学者指南](https://aka.ms/iot-beginners)
- [XR 开发初学者指南](https://github.com/microsoft/xr-development-for-beginners)
- [掌握 GitHub Copilot 进行 AI 配对编程](https://aka.ms/GitHubCopilotAI)
- [掌握 GitHub Copilot 适用于 C#/.NET 开发者](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [选择你的 Copilot 冒险之旅](https://github.com/microsoft/CopilotAdventures)
- [使用 Azure AI 服务的 RAG 聊天应用](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**免责声明**：  
本文档使用AI翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们努力确保翻译的准确性，但请注意，自动翻译可能包含错误或不准确之处。应以原始语言的文档作为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误读承担责任。