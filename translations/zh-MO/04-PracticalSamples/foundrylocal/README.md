# Foundry 本地 Spring Boot 教學

在您自己的機器上運行一個小型語言模型，並從 Java 控制台應用程序調用其與 OpenAI 兼容的
REST 端點。不使用 Azure 部署、Azure 登錄、
雲端 API 金鑰或雲端推理。**GPT-5.6 Luna 僅限 Azure；請勿將其配置為 Foundry 本地模型。**


## 版本和先決條件

| 組件 | 版本 |
| --- | --- |
| Java | 21 或更高版本 |
| Maven | 3.6.3 或更高版本 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry 本地 SDK（本地 REST 伺服器） | 2.0.1 |
| Node.js（本地 REST 伺服器） | 20 或更高版本 |
| Foundry 本地 CLI（可選，獨立版本） | 0.10.3 預覽版 |

Spring Boot 管理 Spring Framework、Jackson、JUnit 和 Maven 插件版本。
此範例直接使用 OpenAI Java SDK，而非 Spring AI。已移除舊的未使用
Spring AI 里程碑屬性和倉庫。

推薦的入門模型是 **Qwen 2.5 0.5B**，CPU 變體
`qwen2.5-0.5b-instruct-generic-cpu:4`（目錄中約 822 MB）。
它避免需要 GPU 執行提供者。其他受支持的已快取小型模型
可明確選擇。模型和運行時安裝需要網路存取；
提示和推理則保持本地。Foundry 本地即使在禁用非必要遙測時，仍可能發出最輕微的運行時
診斷。

從此範例目錄運行以下命令。

## 建構並測試 Java

```powershell
mvn clean verify
```

HTTP 合約測試啟動一個臨時回環伺服器並測試實際的
OpenAI Java SDK。它們涵蓋請求序列化、模型發現、明確模型
選擇、模糊或格式錯誤的模型列表、HTTP 失敗、空白回應、
本地專用網址，以及命令行失敗傳播。它們不需要模型或
除 Maven 依賴安裝外的網路存取。實時測試為選擇性。

## 啟動本地模型

### 推薦：鎖定版 SDK 伺服器

沒有原生 Foundry 本地 Java SDK。小型的 Node.js 輔助程式託管
官方 SDK 的 REST 伺服器；應用程式和聊天請求仍為 Java。

安裝鎖定版運行時依賴：

```powershell
npm ci
```

如果 Windows x64 在 SDK 原生安裝期間無法連接 NuGet，請使用隨附的
備用方案。它會下載相符的官方 GitHub 運行時壓縮包，檢查
發行版本的 SHA-256 摘要，並將其 DLL 放置於原生擴充旁邊。它不會
禁用 TLS 驗證、不需要提權，亦不修改 SDK 源碼。

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

列出此機器上已快取的模型：

```powershell
npm run start:foundry -- --list
```

初次運行時，明確允許下載小型 CPU 模型：

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

後續運行時，省略 `--download` 以要求使用已快取模型：

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

輔助程式偏好使用匹配的快取模型，接受別名或精確變體 ID，
且除非指定 `--download`，否則拒絕缺失模型。只有在需要執行提供者時，
才註冊所選模型的執行提供者。已快取的 GPU 變體仍可能
需要相容的執行提供者套件和驅動程式。

若埠 5273 已被佔用，請傳遞 `--port 0` 指定可用埠。輔助程式啟動後會列印
`FOUNDRY_LOCAL_BASE_URL`、精確的 `FOUNDRY_LOCAL_MODEL` ID 和其 PID。
在 Java 中使用列印的端點。運行 Java 時請保持此終端開啟；
**Ctrl+C** 停止 REST 伺服器並釋放模型。

預設快取路徑為 `~/.foundry/cache/models`。若要使用其他已存在快取，
請設置 `FOUNDRY_LOCAL_CACHE_DIR`。日誌和輔助狀態寫在此範例的
`target/foundry-local` 目錄下。運行 `mvn clean` 前請先停止輔助程式。

### 可選：Foundry 本地 CLI

CLI 與 SDK 有獨立版本：CLI **0.10.3** 捆綁 SDK **1.2.4**；
上述輔助程式使用 SDK **2.0.1**。安裝最新版 CLI 不代表安裝
最新語言 SDK。參見 [CLI 發行說明](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3)。

Windows 下，若無 CLI，請使用每用戶安裝命令：

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

或升級現有安裝：

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x 用 `foundry server` 替代舊的 `foundry service` 命令：

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` 需要已下載的模型。查詢 `foundry model --help` 以獲得
下載命令。使用狀態輸出中的實際端點；CLI 否則
預設為自動分配埠。不要在同一埠啟動 CLI 與 SDK 輔助程式。
完成後：

```powershell
foundry server stop
```

## 運行 Java 應用程式

在第二個終端，設置伺服器列印的端點和精確模型 ID：

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

或運行打包後的應用程式：

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

唯一的 Java 進入點是 `com.example.Application`。它會列印所選
端點、實際模型 ID、提示和生成的回應，然後關閉其 Spring
內容和 HTTP 用戶端。推理失敗或缺失回應文本會導致
失敗退出，而非成功形態的佔位符。

### 配置

| 環境變數 | 預設值 | 目的 |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | 回環 HTTP 端點，包含 `/v1` |
| `FOUNDRY_LOCAL_MODEL` | 空 | 精確模型 ID；否則選擇唯一廣告模型 |
| `FOUNDRY_LOCAL_PROMPT` | 一句關於本地模型的問題 | 控制台運行器發送的提示 |

等價的 Spring 參數是 `--foundry.local.base-url=...`、
`--foundry.local.model=...` 和 `--foundry.local.prompt=...`。
僅接受回環 HTTP 端點。遠端／雲端端點、內嵌
憑證、查詢字串及無 `/v1` 的路徑皆被拒絕。

空模型設定僅在 `/v1/models` 廣告恰好一個模型時可用。
廣告的模型未必已載入。如多模型廣告，
請設定精確載入 ID，而非依賴目錄排序。

請求使用 `temperature=0`、150 代幣輸出限制、120 秒超時，且
不自動重試。`max_tokens` 請求欄位是故意設計：它
被 Foundry 本地 REST 合約支持，儘管 OpenAI Java 已棄用
針對較新的雲端模型該欄位。模型身份來自配置或
探測，而非模型自身的聲稱。

## 即時驗證

啟動本地伺服器後，執行所有測試，包括選擇性的即時測試。
將端點的埠號替換為伺服器打印的值。在 PowerShell 中引用點狀
Maven 屬性：

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

即時測試調用 `Application.main`，提供事實「法國的首都是巴黎」，詢問城市，並斷言實際生成的文本為
`Paris`。它檢查語意結果，而不僅是成功的 HTTP 狀態。


0.5B 模型對另一個「2 + 2」提示，通過 Java 和直接
REST 回答為 `3`。未經獨立
驗證，不要依賴其算術或事實準確性；計算請使用確定性工具。


## 疑難排解

| 症狀 | 檢查 |
| --- | --- |
| 連線被拒絕 | 等待 Ready 訊息；使用列印出的埠號和 `/v1` 路徑。 |
| 宣告多重模型 | 將 `FOUNDRY_LOCAL_MODEL` 設為已載入模型的精確 ID。 |
| 找不到模型 | 使用 `--list`，或明確允許以 `--download` 下載。 |
| GPU 提供者失敗或停滯 | 使用小型 CPU 模型。已快取的 GPU 模型仍需其提供者。 |
| CLI 保持 `initializing` | 查看 `foundry server logs --lines 80`；停止 daemon 並使用 SDK 助手。 |
| NuGet TLS/下載故障 | 修復網絡存取或使用上述驗證過的 Windows x64 備用方式。切勿關閉 TLS。 |
| 埠號被佔用 | 使用 `--port 0` 並以所列端點配置 Java。 |
| 無選擇或空白文本 | 應用程式故意失敗；檢查模型與執行時日誌。 |

## 來源與參考資料

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): 一次性 Spring Boot 運行器。
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): 類型化探測與本地聊天完成。
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP 合約、運行器與即時測試。
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): 官方 SDK REST 伺服器，具快取模型選擇與清理功能。
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): 驗證過的 Windows x64 原生執行時備用。
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), 與 [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): 配置與依賴。
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->