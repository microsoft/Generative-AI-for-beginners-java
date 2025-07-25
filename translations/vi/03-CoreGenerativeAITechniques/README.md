<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:51:36+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "vi"
}
-->
# Hướng Dẫn Kỹ Thuật AI Tạo Sinh Cốt Lõi

## Mục Lục

- [Yêu Cầu Trước](../../../03-CoreGenerativeAITechniques)
- [Bắt Đầu](../../../03-CoreGenerativeAITechniques)
  - [Bước 1: Thiết Lập Biến Môi Trường](../../../03-CoreGenerativeAITechniques)
  - [Bước 2: Điều Hướng Đến Thư Mục Ví Dụ](../../../03-CoreGenerativeAITechniques)
- [Hướng Dẫn 1: Hoàn Thành và Trò Chuyện với LLM](../../../03-CoreGenerativeAITechniques)
- [Hướng Dẫn 2: Gọi Hàm](../../../03-CoreGenerativeAITechniques)
- [Hướng Dẫn 3: RAG (Tạo Sinh Tăng Cường Truy Xuất)](../../../03-CoreGenerativeAITechniques)
- [Hướng Dẫn 4: AI Có Trách Nhiệm](../../../03-CoreGenerativeAITechniques)
- [Các Mẫu Chung Trong Các Ví Dụ](../../../03-CoreGenerativeAITechniques)
- [Bước Tiếp Theo](../../../03-CoreGenerativeAITechniques)
- [Khắc Phục Sự Cố](../../../03-CoreGenerativeAITechniques)
  - [Các Vấn Đề Thường Gặp](../../../03-CoreGenerativeAITechniques)

## Tổng Quan

Hướng dẫn này cung cấp các ví dụ thực hành về các kỹ thuật AI tạo sinh cốt lõi sử dụng Java và GitHub Models. Bạn sẽ học cách tương tác với Mô Hình Ngôn Ngữ Lớn (LLM), triển khai gọi hàm, sử dụng tạo sinh tăng cường truy xuất (RAG), và áp dụng các thực hành AI có trách nhiệm.

## Yêu Cầu Trước

Trước khi bắt đầu, hãy đảm bảo bạn đã:
- Cài đặt Java 21 hoặc cao hơn
- Sử dụng Maven để quản lý phụ thuộc
- Có tài khoản GitHub với mã thông báo truy cập cá nhân (PAT)

## Bắt Đầu

### Bước 1: Thiết Lập Biến Môi Trường

Đầu tiên, bạn cần thiết lập mã thông báo GitHub của mình làm biến môi trường. Mã thông báo này cho phép bạn truy cập GitHub Models miễn phí.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Bước 2: Điều Hướng Đến Thư Mục Ví Dụ

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Hướng Dẫn 1: Hoàn Thành và Trò Chuyện với LLM

**Tệp:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Những Gì Ví Dụ Này Dạy

Ví dụ này minh họa các cơ chế cốt lõi của việc tương tác với Mô Hình Ngôn Ngữ Lớn (LLM) thông qua API OpenAI, bao gồm khởi tạo client với GitHub Models, cấu trúc thông điệp cho các lời nhắc hệ thống và người dùng, quản lý trạng thái hội thoại thông qua tích lũy lịch sử thông điệp, và điều chỉnh tham số để kiểm soát độ dài và mức độ sáng tạo của phản hồi.

### Các Khái Niệm Mã Chính

#### 1. Thiết Lập Client
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Điều này tạo kết nối với GitHub Models bằng mã thông báo của bạn.

#### 2. Hoàn Thành Đơn Giản
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Bộ Nhớ Hội Thoại
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI chỉ nhớ các thông điệp trước đó nếu bạn bao gồm chúng trong các yêu cầu tiếp theo.

### Chạy Ví Dụ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Điều Gì Xảy Ra Khi Bạn Chạy Nó

1. **Hoàn Thành Đơn Giản**: AI trả lời một câu hỏi về Java với hướng dẫn từ lời nhắc hệ thống
2. **Trò Chuyện Nhiều Lượt**: AI duy trì ngữ cảnh qua nhiều câu hỏi
3. **Trò Chuyện Tương Tác**: Bạn có thể trò chuyện thực sự với AI

## Hướng Dẫn 2: Gọi Hàm

**Tệp:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Những Gì Ví Dụ Này Dạy

Gọi hàm cho phép các mô hình AI yêu cầu thực thi các công cụ và API bên ngoài thông qua một giao thức có cấu trúc, nơi mô hình phân tích các yêu cầu ngôn ngữ tự nhiên, xác định các cuộc gọi hàm cần thiết với các tham số phù hợp bằng cách sử dụng định nghĩa JSON Schema, và xử lý kết quả trả về để tạo phản hồi theo ngữ cảnh, trong khi việc thực thi hàm thực tế vẫn nằm dưới sự kiểm soát của nhà phát triển để đảm bảo an toàn và độ tin cậy.

### Các Khái Niệm Mã Chính

#### 1. Định Nghĩa Hàm
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Điều này cho AI biết các hàm nào có sẵn và cách sử dụng chúng.

#### 2. Luồng Thực Thi Hàm
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Triển Khai Hàm
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Chạy Ví Dụ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Điều Gì Xảy Ra Khi Bạn Chạy Nó

1. **Hàm Thời Tiết**: AI yêu cầu dữ liệu thời tiết cho Seattle, bạn cung cấp nó, AI định dạng phản hồi
2. **Hàm Máy Tính**: AI yêu cầu tính toán (15% của 240), bạn thực hiện nó, AI giải thích kết quả

## Hướng Dẫn 3: RAG (Tạo Sinh Tăng Cường Truy Xuất)

**Tệp:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Những Gì Ví Dụ Này Dạy

Tạo Sinh Tăng Cường Truy Xuất (RAG) kết hợp truy xuất thông tin với tạo sinh ngôn ngữ bằng cách chèn ngữ cảnh tài liệu bên ngoài vào các lời nhắc AI, cho phép các mô hình cung cấp câu trả lời chính xác dựa trên các nguồn kiến thức cụ thể thay vì dữ liệu huấn luyện có thể lỗi thời hoặc không chính xác, đồng thời duy trì ranh giới rõ ràng giữa các truy vấn của người dùng và các nguồn thông tin có thẩm quyền thông qua kỹ thuật lời nhắc chiến lược.

### Các Khái Niệm Mã Chính

#### 1. Tải Tài Liệu
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Chèn Ngữ Cảnh
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Dấu ngoặc ba giúp AI phân biệt giữa ngữ cảnh và câu hỏi.

#### 3. Xử Lý Phản Hồi An Toàn
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Luôn xác thực phản hồi API để tránh lỗi.

### Chạy Ví Dụ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Điều Gì Xảy Ra Khi Bạn Chạy Nó

1. Chương trình tải `document.txt` (chứa thông tin về GitHub Models)
2. Bạn đặt câu hỏi về tài liệu
3. AI chỉ trả lời dựa trên nội dung tài liệu, không phải kiến thức chung của nó

Hãy thử hỏi: "GitHub Models là gì?" so với "Thời tiết hôm nay thế nào?"

## Hướng Dẫn 4: AI Có Trách Nhiệm

**Tệp:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Những Gì Ví Dụ Này Dạy

Ví dụ AI Có Trách Nhiệm minh họa tầm quan trọng của việc triển khai các biện pháp an toàn trong các ứng dụng AI. Nó trình bày các bộ lọc an toàn phát hiện các danh mục nội dung có hại bao gồm ngôn từ kích động thù địch, quấy rối, tự làm hại, nội dung tình dục, và bạo lực, minh họa cách các ứng dụng AI trong sản xuất nên xử lý các vi phạm chính sách nội dung một cách duyên dáng thông qua xử lý ngoại lệ phù hợp, cơ chế phản hồi người dùng, và chiến lược phản hồi dự phòng.

### Các Khái Niệm Mã Chính

#### 1. Khung Kiểm Tra An Toàn
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Các Danh Mục An Toàn Được Kiểm Tra
- Hướng dẫn bạo lực/tự làm hại
- Ngôn từ kích động thù địch
- Vi phạm quyền riêng tư
- Thông tin sai lệch y tế
- Hoạt động bất hợp pháp

### Chạy Ví Dụ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Điều Gì Xảy Ra Khi Bạn Chạy Nó

Chương trình kiểm tra các lời nhắc có hại khác nhau và cho thấy cách hệ thống an toàn AI:
1. **Chặn các yêu cầu nguy hiểm** với lỗi HTTP 400
2. **Cho phép nội dung an toàn** được tạo bình thường
3. **Bảo vệ người dùng** khỏi các đầu ra AI có hại

## Các Mẫu Chung Trong Các Ví Dụ

### Mẫu Xác Thực
Tất cả các ví dụ sử dụng mẫu này để xác thực với GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Mẫu Xử Lý Lỗi
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Mẫu Cấu Trúc Thông Điệp
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Bước Tiếp Theo

[Chương 04: Các ví dụ thực tế](../04-PracticalSamples/README.md)

## Khắc Phục Sự Cố

### Các Vấn Đề Thường Gặp

**"GITHUB_TOKEN not set"**
- Đảm bảo bạn đã thiết lập biến môi trường
- Xác minh mã thông báo của bạn có phạm vi `models:read`

**"No response from API"**
- Kiểm tra kết nối internet của bạn
- Xác minh mã thông báo của bạn hợp lệ
- Kiểm tra xem bạn có vượt quá giới hạn tốc độ không

**Lỗi biên dịch Maven**
- Đảm bảo bạn có Java 21 hoặc cao hơn
- Chạy `mvn clean compile` để làm mới các phụ thuộc

**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được coi là nguồn thông tin chính thức. Đối với các thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.