<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T10:56:59+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "hk"
}
-->
# Foundry Local Spring Boot 教學

## 目錄

- [先決條件](../../../../04-PracticalSamples/foundrylocal)
- [項目概述](../../../../04-PracticalSamples/foundrylocal)
- [理解程式碼](../../../../04-PracticalSamples/foundrylocal)
  - [1. 應用程式配置 (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. 主應用程式類別 (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI 服務層 (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. 項目依賴 (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [整體運作方式](../../../../04-PracticalSamples/foundrylocal)
- [設置 Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [運行應用程式](../../../../04-PracticalSamples/foundrylocal)
- [預期輸出](../../../../04-PracticalSamples/foundrylocal)
- [下一步](../../../../04-PracticalSamples/foundrylocal)
- [故障排除](../../../../04-PracticalSamples/foundrylocal)

## 先決條件

在開始此教學之前，請確保您已經：

- 在系統上安裝了 **Java 21 或更高版本**
- 安裝了 **Maven 3.6+** 用於構建項目
- 安裝並運行了 **Foundry Local**

### **安裝 Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## 項目概述

此項目包含四個主要組件：

1. **Application.java** - Spring Boot 應用程式的主要入口點
2. **FoundryLocalService.java** - 處理 AI 通訊的服務層
3. **application.properties** - Foundry Local 連接的配置
4. **pom.xml** - Maven 依賴和項目配置

## 理解程式碼

### 1. 應用程式配置 (application.properties)

**檔案位置:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**此配置的作用:**
- **base-url**: 指定 Foundry Local 的運行位置 (默認端口 5273)
- **model**: 指定用於文本生成的 AI 模型名稱

**關鍵概念:** Spring Boot 自動加載這些屬性，並通過 `@Value` 註解使其在應用程式中可用。

### 2. 主應用程式類別 (Application.java)

**檔案位置:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**此程式的作用:**
- `@SpringBootApplication` 啟用 Spring Boot 的自動配置
- `WebApplicationType.NONE` 告訴 Spring 這是一個命令行應用程式，而不是 Web 伺服器
- 主方法啟動 Spring 應用程式

**示範執行器:**
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

**此程式的作用:**
- `@Bean` 創建由 Spring 管理的組件
- `CommandLineRunner` 在 Spring Boot 啟動後執行程式碼
- `foundryLocalService` 由 Spring 自動注入 (依賴注入)
- 發送測試消息給 AI 並顯示回應

### 3. AI 服務層 (FoundryLocalService.java)

**檔案位置:** `src/main/java/com/example/FoundryLocalService.java`

#### 配置注入:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**此程式的作用:**
- `@Service` 告訴 Spring 此類提供業務邏輯
- `@Value` 從 application.properties 中注入配置值
- `:default-value` 語法提供屬性未設置時的回退值

#### 客戶端初始化:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**此程式的作用:**
- `@PostConstruct` 在 Spring 創建服務後執行此方法
- 創建指向本地 Foundry Local 實例的 OpenAI 客戶端
- `/v1` 路徑是 OpenAI API 兼容所需的
- API 密鑰為 "unused"，因為本地開發不需要身份驗證

#### 聊天方法:
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

**此程式的作用:**
- **ChatCompletionCreateParams**: 配置 AI 請求
  - `model`: 指定使用的 AI 模型
  - `addUserMessage`: 將您的消息添加到對話中
  - `maxCompletionTokens`: 限制回應的長度 (節省資源)
  - `temperature`: 控制隨機性 (0.0 = 確定性, 1.0 = 創造性)
- **API 呼叫**: 發送請求到 Foundry Local
- **回應處理**: 安全提取 AI 的文本回應
- **錯誤處理**: 包裝異常並提供有用的錯誤信息

### 4. 項目依賴 (pom.xml)

**關鍵依賴:**

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

**此程式的作用:**
- **spring-boot-starter**: 提供核心 Spring Boot 功能
- **openai-java**: 用於 API 通訊的官方 OpenAI Java SDK
- **jackson-databind**: 處理 API 呼叫的 JSON 序列化/反序列化

## 整體運作方式

當您運行應用程式時，以下是完整的流程：

1. **啟動**: Spring Boot 啟動並讀取 `application.properties`
2. **服務創建**: Spring 創建 `FoundryLocalService` 並注入配置值
3. **客戶端設置**: `@PostConstruct` 初始化 OpenAI 客戶端以連接到 Foundry Local
4. **示範執行**: `CommandLineRunner` 在啟動後執行
5. **AI 呼叫**: 示範調用 `foundryLocalService.chat()` 發送測試消息
6. **API 請求**: 服務構建並發送 OpenAI 兼容的請求到 Foundry Local
7. **回應處理**: 服務提取並返回 AI 的回應
8. **顯示**: 應用程式打印回應並退出

## 設置 Foundry Local

按照以下步驟設置 Foundry Local：

1. **安裝 Foundry Local**，請參考 [先決條件](../../../../04-PracticalSamples/foundrylocal) 部分的指導。
2. **下載您想使用的 AI 模型**，例如 `phi-3.5-mini`，使用以下命令：
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **配置 application.properties** 文件以匹配您的 Foundry Local 設置，特別是如果您使用了不同的端口或模型。

## 運行應用程式

### 步驟 1: 啟動 Foundry Local
```bash
foundry model run phi-3.5-mini
```

### 步驟 2: 構建並運行應用程式
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## 預期輸出

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

更多示例，請參閱 [第 04 章: 實用範例](../README.md)

## 故障排除

### 常見問題

**"連接被拒絕" 或 "服務不可用"**
- 確保 Foundry Local 正在運行: `foundry model list`
- 驗證服務是否在端口 5273: 檢查 `application.properties`
- 嘗試重啟 Foundry Local: `foundry model run phi-3.5-mini`

**"模型未找到" 錯誤**
- 檢查可用模型: `foundry model list`
- 更新 `application.properties` 中的模型名稱以完全匹配
- 如果需要，下載模型: `foundry model run phi-3.5-mini`

**Maven 編譯錯誤**
- 確保 Java 21 或更高版本: `java -version`
- 清理並重新構建: `mvn clean compile`
- 檢查網絡連接以下載依賴項

**應用程式啟動但無輸出**
- 驗證 Foundry Local 是否有回應: 在瀏覽器中打開 `http://localhost:5273`
- 檢查應用程式日誌以獲取具體錯誤信息
- 確保模型已完全加載並準備好使用

**免責聲明**：  
本文件已使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們致力於提供準確的翻譯，請注意自動翻譯可能包含錯誤或不準確之處。原始語言的文件應被視為具權威性的來源。對於重要信息，建議使用專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或錯誤解釋不承擔責任。