# Foundry 本地 Spring Boot 教程

在你自己的机器上运行一个小型语言模型，并从 Java 控制台应用调用其 OpenAI 兼容的
REST 端点。无需 Azure 部署、Azure 登录、
云 API 密钥或云推理。**GPT-5.6 Luna 仅限 Azure；请勿
将其配置为 Foundry 本地模型。**

## 版本和前提条件

| 组件 | 版本 |
| --- | --- |
| Java | 21 或更高版本 |
| Maven | 3.6.3 或更高版本 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry 本地 SDK（本地 REST 服务器） | 2.0.1 |
| Node.js（本地 REST 服务器） | 20 或更高版本 |
| Foundry 本地 CLI（可选，单独发布） | 0.10.3 预览版 |

Spring Boot 管理 Spring Framework、Jackson、JUnit 和 Maven 插件版本。
本示例直接使用 OpenAI Java SDK，而非 Spring AI。已移除未使用的
Spring AI 里程碑属性和仓库。

推荐的起步模型是 **Qwen 2.5 0.5B** 的 CPU 变体
`qwen2.5-0.5b-instruct-generic-cpu:4`（目录中大约 822 MB）。
它避免了需要 GPU 执行提供程序。可以显式选择其他支持的、已缓存的小模型。
模型和运行时安装需要网络访问；
提示和推理保持本地。即使禁用非必要的遥测，Foundry 本地仍可能发出最小的运行时
诊断信息。

在此示例目录下运行以下命令。

## 构建和测试 Java

```powershell
mvn clean verify
```

HTTP 合约测试启动一个临时回环服务器并检验实际的
OpenAI Java SDK。覆盖请求序列化、模型发现、显式模型
选择、模糊或格式错误的模型列表、HTTP 失败、空响应、
本地专用 URL 和命令行失败传播。它们不需要模型或
网络访问，除了 Maven 依赖安装。实时测试是可选的。

## 启动本地模型

### 推荐：固定版本 SDK 服务器

目前没有原生 Foundry 本地 Java SDK。小型 Node.js 辅助程序托管官方
SDK 的 REST 服务器；应用程序和聊天请求仍然是 Java。

安装固定版本的运行时依赖：

```powershell
npm ci
```

如果 Windows x64 在 SDK 的本地安装过程中无法访问 NuGet，请使用提供的
备用方案。它下载匹配的官方 GitHub 运行时归档，校验
发布的 SHA-256 摘要，并将其 DLL 文件放置在本地插件旁边。它不会
禁用 TLS 验证、不需要提升权限或修改 SDK 源码。

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

列出此机器上已缓存的模型：

```powershell
npm run start:foundry -- --list
```

首次运行时，显式允许下载小型 CPU 模型：

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

后续运行时，省略 `--download` 以要求使用已缓存模型：

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

辅助程序优先使用匹配的缓存模型，接受别名或精确的变体 ID，
且除非提供 `--download`，否则拒绝缺失模型。只有在需要时才
注册所选模型的执行提供程序。已缓存的 GPU 变体仍可能
需要兼容的执行提供程序包和驱动。

如果端口 5273 被占用，传入 `--port 0` 以使用可用端口。辅助程序准备就绪时会打印
`FOUNDRY_LOCAL_BASE_URL`、确切的 `FOUNDRY_LOCAL_MODEL` ID 和其 PID。
在 Java 中使用打印的端点。运行 Java 时保持此终端打开；
**Ctrl+C** 停止 REST 服务器并释放模型。

默认缓存路径是 `~/.foundry/cache/models`。设置 `FOUNDRY_LOCAL_CACHE_DIR` 指定
另一个已有的缓存目录。日志和辅助程序状态写入本示例的
`target/foundry-local` 目录。在运行 `mvn clean` 之前停止辅助程序。

### 可选：Foundry 本地 CLI

CLI 和 SDK 独立发布：CLI **0.10.3** 捆绑了 SDK **1.2.4**；
上述辅助程序使用 SDK **2.0.1**。安装最新 CLI 不会安装
最新语言 SDK。详见 [CLI 发布说明](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3)。

在 Windows 上，如果缺少 CLI，请使用用户级安装命令：

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

或升级已安装版本：

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x 用 `foundry server` 替换了旧 `foundry service` 命令：

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` 需要已下载模型。查看 `foundry model --help` 了解
下载命令。使用状态输出的实际端点；否则 CLI 默认
自动分配端口。不要在同一端口同时启动 CLI 和 SDK 辅助程序。
完成后：

```powershell
foundry server stop
```

## 运行 Java 应用程序

在另一个终端中，设置服务器打印的端点和精确模型 ID：

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

或运行打包好的应用：

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

单一 Java 入口点为 `com.example.Application`。它打印所选
端点、实际模型 ID、提示和生成响应，然后关闭 Spring
上下文和 HTTP 客户端。推理失败或响应缺失则会产生
失败退出，而非成功形态的占位符。

### 配置

| 环境变量 | 默认值 | 作用 |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | 回环 HTTP 端点，包含 `/v1` |
| `FOUNDRY_LOCAL_MODEL` | 空 | 精确模型 ID；否则选择唯一宣传模型 |
| `FOUNDRY_LOCAL_PROMPT` | 关于本地模型的一个简短问题 | 控制台运行者发送的提示 |

等效的 Spring 参数是 `--foundry.local.base-url=...`、
`--foundry.local.model=...` 和 `--foundry.local.prompt=...`。
只接受回环 HTTP 端点。拒绝远程/云端点、嵌入的
凭据、查询字符串和不含 `/v1` 的路径。

空模型设置仅当 `/v1/models` 宣传恰好一个模型时有效。
宣传的模型不一定被加载。若宣传多个模型，
应设置精确加载 ID，而非依赖目录排序。

请求使用 `temperature=0`、150 令牌输出限制、120 秒超时，
无自动重试。请求字段 `max_tokens` 是有意为之：
它受 Foundry 本地 REST 协议支持，尽管 OpenAI Java 对更新云端模型
弃用了这个字段。模型身份来自配置或发现，
而非模型自我声明。

## 实时验证

本地服务器运行时，运行所有测试包括可选的实时测试。
用服务器打印的端口替换端点端口。在 PowerShell 中引用带点的
Maven 属性：

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

实时测试调用 `Application.main`，提供事实 "法国的首都是巴黎"，
询问该城市，并断言实际生成文本为
`巴黎`。它检查语义结果，不仅仅是 HTTP 状态成功。

这是集成检查，而非准确性基准。在验证期间，
该 0.5B 模型通过 Java 和直接
REST 对另一个 "2 + 2" 的提示作答为 `3`。不要依赖它进行算术或事实准确性，除非有独立
验证；请使用确定性工具进行计算。

## 故障排除

| 症状 | 检查 |
| --- | --- |
| 连接被拒绝 | 等待准备消息；使用打印的端口和 `/v1` 路径。 |
| 宣传多个模型 | 设置 `FOUNDRY_LOCAL_MODEL` 为已加载模型的精确 ID。 |
| 模型缺失 | 使用 `--list`，或显式允许使用 `--download` 下载。 |
| GPU 提供程序失败或卡住 | 使用小型 CPU 模型。已缓存的 GPU 模型仍需其提供程序。 |
| CLI 保持“初始化中”状态 | 阅读 `foundry server logs --lines 80`；停止守护进程并使用 SDK 辅助程序。 |
| NuGet TLS/下载失败 | 修复网络访问或使用上述验证过的 Windows x64 备用方案。不禁用 TLS。 |
| 端口被占用 | 使用 `--port 0` 并用打印的端点配置 Java。 |
| 无选项或空文本 | 应用故意失败；检查模型和运行时日志。 |

## 源代码和参考

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java)：一次性 Spring Boot 运行器。
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java)：类型化发现和本地聊天补全。
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java)：HTTP 合约、运行器和实时测试。
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs)：官方 SDK REST 服务器，支持缓存模型选择和清理。
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1)：验证的 Windows x64 本地运行时备用方案。
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties)、[pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) 和 [package.json](../../../../04-PracticalSamples/foundrylocal/package.json)：配置和依赖。
- [Foundry Local REST 集成](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks)。
- [Foundry Local 2.0.1 版本和迁移说明](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1)。
- [第 04 章：实用样例](../README.md)。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版文件应视为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译而产生的任何误解或误释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->