<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:37:38+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "mo"
}
-->
# 為 Java 的生成式 AI 設置開發環境

> **快速開始**：2 分鐘內在雲端編碼 - 跳至 [GitHub Codespaces 設置](../../../02-SetupDevEnvironment) - 無需本地安裝，並使用 GitHub 模型！

> **對 Azure OpenAI 感興趣嗎？** 請參閱我們的 [Azure OpenAI 設置指南](getting-started-azure-openai.md)，了解如何創建新的 Azure OpenAI 資源。

## 你將學到什麼

- 為 AI 應用設置 Java 開發環境
- 選擇並配置你偏好的開發環境（以 Codespaces 為主的雲端優先、本地開發容器或完整的本地設置）
- 測試你的設置，並連接到 GitHub 模型

## 目錄

- [你將學到什麼](../../../02-SetupDevEnvironment)
- [介紹](../../../02-SetupDevEnvironment)
- [步驟 1：設置開發環境](../../../02-SetupDevEnvironment)
  - [選項 A：GitHub Codespaces（推薦）](../../../02-SetupDevEnvironment)
  - [選項 B：本地開發容器](../../../02-SetupDevEnvironment)
  - [選項 C：使用你現有的本地安裝](../../../02-SetupDevEnvironment)
- [步驟 2：創建 GitHub 個人訪問令牌](../../../02-SetupDevEnvironment)
- [步驟 3：測試你的設置](../../../02-SetupDevEnvironment)
- [故障排除](../../../02-SetupDevEnvironment)
- [總結](../../../02-SetupDevEnvironment)
- [下一步](../../../02-SetupDevEnvironment)

## 介紹

本章將指導你設置開發環境。我們將使用 **GitHub 模型** 作為主要示例，因為它免費、易於設置（只需一個 GitHub 帳戶）、不需要信用卡，並提供多個模型供實驗。

**無需本地設置！** 你可以直接使用 GitHub Codespaces 在瀏覽器中開始編碼，它提供了一個完整的開發環境。

<img src="./images/models.webp" alt="截圖：GitHub 模型" width="50%">

我們推薦使用 [**GitHub 模型**](https://github.com/marketplace?type=models) 作為本課程的工具，因為它：
- **免費** 開始使用
- **簡單**，只需一個 GitHub 帳戶即可設置
- **不需要信用卡**
- 提供 **多個模型** 供實驗

> **注意**：本培訓中使用的 GitHub 模型有以下免費限制：
> - 每分鐘 15 次請求（每天 150 次）
> - 每次請求約 8,000 字輸入，約 4,000 字輸出
> - 5 個並發請求
> 
> 若需生產環境使用，可升級至 Azure AI Foundry 模型，使用你的 Azure 帳戶。代碼無需更改。請參閱 [Azure AI Foundry 文檔](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models)。

## 步驟 1：設置開發環境

<a name="quick-start-cloud"></a>

我們已創建了一個預配置的開發容器，以最大限度地減少設置時間，並確保你擁有完成本生成式 AI Java 課程所需的所有工具。選擇你偏好的開發方式：

### 環境設置選項：

#### 選項 A：GitHub Codespaces（推薦）

**2 分鐘內開始編碼 - 無需本地設置！**

1. 將此倉庫 Fork 到你的 GitHub 帳戶
   > **注意**：如果你想編輯基本配置，請查看 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 點擊 **Code** → **Codespaces** 標籤 → **...** → **New with options...**
3. 使用默認設置 - 這將選擇 **開發容器配置**：為本課程創建的 **生成式 AI Java 開發環境** 自定義開發容器
4. 點擊 **Create codespace**
5. 等待約 2 分鐘，環境準備就緒
6. 繼續執行 [步驟 2：創建 GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="截圖：Codespaces 子菜單" width="50%">

<img src="./images/image.png" alt="截圖：New with options" width="50%">

<img src="./images/codespaces-create.png" alt="截圖：Create codespace options" width="50%">

> **使用 Codespaces 的好處**：
> - 無需本地安裝
> - 適用於任何帶瀏覽器的設備
> - 預配置所有工具和依賴項
> - 個人帳戶每月免費 60 小時
> - 為所有學員提供一致的環境

#### 選項 B：本地開發容器

**適合偏好使用 Docker 進行本地開發的開發者**

1. Fork 並克隆此倉庫到你的本地機器
   > **注意**：如果你想編輯基本配置，請查看 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 安裝 [Docker Desktop](https://www.docker.com/products/docker-desktop/) 和 [VS Code](https://code.visualstudio.com/)
3. 在 VS Code 中安裝 [Dev Containers 擴展](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
4. 在 VS Code 中打開倉庫文件夾
5. 當提示時，點擊 **Reopen in Container**（或使用 `Ctrl+Shift+P` → "Dev Containers: Reopen in Container"）
6. 等待容器構建並啟動
7. 繼續執行 [步驟 2：創建 GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="截圖：開發容器設置" width="50%">

<img src="./images/image-3.png" alt="截圖：開發容器構建完成" width="50%">

#### 選項 C：使用你現有的本地安裝

**適合已有 Java 環境的開發者**

前置條件：
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) 或你偏好的 IDE

步驟：
1. 將此倉庫克隆到你的本地機器
2. 在你的 IDE 中打開該項目
3. 繼續執行 [步驟 2：創建 GitHub Token](../../../02-SetupDevEnvironment)

> **專家提示**：如果你的機器性能較低，但仍想在本地使用 VS Code，可以使用 GitHub Codespaces！你可以將本地 VS Code 連接到雲端托管的 Codespace，享受兩全其美的體驗。

<img src="./images/image-2.png" alt="截圖：創建的本地開發容器實例" width="50%">

## 步驟 2：創建 GitHub 個人訪問令牌

1. 前往 [GitHub 設置](https://github.com/settings/profile)，並從個人資料菜單中選擇 **Settings**。
2. 在左側邊欄中，點擊 **Developer settings**（通常在底部）。
3. 在 **Personal access tokens** 下，點擊 **Fine-grained tokens**（或直接訪問此 [鏈接](https://github.com/settings/personal-access-tokens)）。
4. 點擊 **Generate new token**。
5. 在 "Token name" 下，提供一個描述性名稱（例如 `GenAI-Java-Course-Token`）。
6. 設置過期日期（建議：7 天，符合安全最佳實踐）。
7. 在 "Resource owner" 下，選擇你的用戶帳戶。
8. 在 "Repository access" 下，選擇你希望與 GitHub 模型一起使用的倉庫（或選擇 "All repositories"）。
9. 在 "Repository permissions" 下，找到 **Models** 並設置為 **Read and write**。
10. 點擊 **Generate token**。
11. **立即複製並保存你的令牌** - 之後無法再次查看！

> **安全提示**：為訪問令牌使用最小必要的範圍和最短的實際過期時間。

## 步驟 3：使用 GitHub 模型示例測試你的設置

當你的開發環境準備就緒後，讓我們使用 [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) 中的示例應用測試 GitHub 模型集成。

1. 在你的開發環境中打開終端。
2. 導航到 GitHub 模型示例：
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. 將你的 GitHub 令牌設置為環境變量：
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. 運行應用程序：
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

你應該會看到類似以下的輸出：
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### 理解示例代碼

首先，讓我們了解剛剛運行的內容。`examples/github-models` 下的示例使用 OpenAI Java SDK 連接到 GitHub 模型：

**此代碼的功能：**
- 使用你的個人訪問令牌 **連接** 到 GitHub 模型
- 向 AI 模型 **發送** 一條簡單的 "Say Hello World!" 消息
- **接收** 並顯示 AI 的回應
- **驗證** 你的設置是否正確運行

**關鍵依賴項**（在 `pom.xml` 中）：
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**主代碼**（`App.java`）：
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## 總結

太棒了！你現在已完成以下設置：

- 創建了一個具有正確權限的 GitHub 個人訪問令牌，用於 AI 模型訪問
- 啟動了你的 Java 開發環境（無論是 Codespaces、開發容器還是本地）
- 使用 OpenAI Java SDK 連接到 GitHub 模型，進行免費的 AI 開發
- 測試了一個簡單的示例，與 AI 模型進行交互

## 下一步

[第 3 章：生成式 AI 核心技術](../03-CoreGenerativeAITechniques/README.md)

## 故障排除

遇到問題？以下是常見問題及解決方案：

- **令牌無法使用？** 
  - 確保你完整複製了令牌，且沒有多餘的空格
  - 驗證令牌是否正確設置為環境變量
  - 檢查令牌是否具有正確的權限（Models: Read and write）

- **找不到 Maven？** 
  - 如果使用開發容器/Codespaces，Maven 應已預安裝
  - 對於本地設置，確保已安裝 Java 21+ 和 Maven 3.9+
  - 嘗試運行 `mvn --version` 驗證安裝

- **連接問題？** 
  - 檢查你的網絡連接
  - 驗證 GitHub 是否可從你的網絡訪問
  - 確保防火牆未阻止 GitHub 模型端點

- **開發容器無法啟動？** 
  - 確保 Docker Desktop 正在運行（對於本地開發）
  - 嘗試重建容器：`Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **應用程序編譯錯誤？**
  - 確保你在正確的目錄下：`02-SetupDevEnvironment/examples/github-models`
  - 嘗試清理並重新構建：`mvn clean compile`

> **需要幫助？**：仍有問題？在倉庫中打開一個 issue，我們會幫助你解決。

**免責聲明**：  
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於提供準確的翻譯，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於關鍵信息，建議使用專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或誤釋不承擔責任。