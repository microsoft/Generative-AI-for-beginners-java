<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T10:52:11+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "zh"
}
-->
# 实用应用与项目

## 学习内容
在本章节中，我们将演示三个展示使用 Java 开发生成式 AI的实际应用案例：
- 创建一个结合客户端和服务器端 AI的多模态宠物故事生成器
- 使用 Foundry Local Spring Boot 演示实现本地 AI 模型集成
- 使用计算器示例开发一个模型上下文协议（MCP）服务

## 目录

- [简介](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot 演示](../../../04-PracticalSamples)
  - [宠物故事生成器](../../../04-PracticalSamples)
  - [MCP 计算器服务（适合初学者的 MCP 演示）](../../../04-PracticalSamples)
- [学习进阶](../../../04-PracticalSamples)
- [总结](../../../04-PracticalSamples)
- [下一步](../../../04-PracticalSamples)

## 简介

本章节展示了**示例项目**，这些项目演示了使用 Java 开发生成式 AI的模式。每个项目都是完全功能性的，展示了特定的 AI 技术、架构模式以及最佳实践，您可以将其应用到自己的项目中。

### Foundry Local Spring Boot 演示

**[Foundry Local Spring Boot 演示](foundrylocal/README.md)** 展示了如何使用 **OpenAI Java SDK** 集成本地 AI 模型。它演示了如何连接到运行在 Foundry Local 上的 **Phi-3.5-mini** 模型，使您能够运行无需依赖云服务的 AI 应用。

### 宠物故事生成器

**[宠物故事生成器](petstory/README.md)** 是一个有趣的 Spring Boot Web 应用，展示了**多模态 AI处理**以生成创意宠物故事。它结合了客户端和服务器端 AI功能，使用 transformer.js 实现基于浏览器的 AI交互，并使用 OpenAI SDK 进行服务器端处理。

### MCP 计算器服务（适合初学者的 MCP 演示）

**[MCP 计算器服务](mcp/calculator/README.md)** 是一个简单的 **模型上下文协议（MCP）** 演示，使用 Spring AI 实现。它为 MCP 概念提供了一个适合初学者的入门示例，展示了如何创建一个基本的 MCP 服务器与 MCP 客户端交互。

## 学习进阶

这些项目设计为基于前面章节的概念逐步深入：

1. **从简单开始**：从 Foundry Local Spring Boot 演示开始，了解与本地模型的基本 AI集成
2. **增加交互性**：进阶到宠物故事生成器，体验多模态 AI和基于 Web 的交互
3. **学习 MCP 基础**：尝试 MCP 计算器服务，理解模型上下文协议的基本概念

## 总结

**恭喜您！** 您已经成功完成以下内容：

- **创建多模态 AI体验**，结合客户端和服务器端 AI处理
- **实现本地 AI模型集成**，使用现代 Java框架和 SDK
- **开发模型上下文协议服务**，展示工具集成模式

## 下一步

[第 5 章：负责任的生成式 AI](../05-ResponsibleGenAI/README.md)

**免责声明**：  
本文档使用AI翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)进行翻译。尽管我们努力确保准确性，但请注意，自动翻译可能包含错误或不准确之处。应以原始语言的文档作为权威来源。对于关键信息，建议使用专业人工翻译。对于因使用本翻译而引起的任何误解或误读，我们概不负责。