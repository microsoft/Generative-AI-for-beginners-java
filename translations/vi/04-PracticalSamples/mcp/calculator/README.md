<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:52:52+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "vi"
}
-->
# Hướng Dẫn Sử Dụng MCP Calculator Dành Cho Người Mới Bắt Đầu

## Mục Lục

- [Những Gì Bạn Sẽ Học](../../../../../04-PracticalSamples/mcp/calculator)
- [Yêu Cầu Trước Khi Bắt Đầu](../../../../../04-PracticalSamples/mcp/calculator)
- [Hiểu Cấu Trúc Dự Án](../../../../../04-PracticalSamples/mcp/calculator)
- [Giải Thích Các Thành Phần Chính](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Ứng Dụng Chính](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Dịch Vụ Máy Tính](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Khách Hàng MCP Trực Tiếp](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Khách Hàng Sử Dụng AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Chạy Các Ví Dụ](../../../../../04-PracticalSamples/mcp/calculator)
- [Cách Mọi Thứ Hoạt Động Cùng Nhau](../../../../../04-PracticalSamples/mcp/calculator)
- [Bước Tiếp Theo](../../../../../04-PracticalSamples/mcp/calculator)

## Những Gì Bạn Sẽ Học

Hướng dẫn này giải thích cách xây dựng một dịch vụ máy tính sử dụng Model Context Protocol (MCP). Bạn sẽ hiểu:

- Cách tạo một dịch vụ mà AI có thể sử dụng như một công cụ
- Cách thiết lập giao tiếp trực tiếp với các dịch vụ MCP
- Cách các mô hình AI tự động chọn công cụ cần sử dụng
- Sự khác biệt giữa các cuộc gọi giao thức trực tiếp và tương tác hỗ trợ bởi AI

## Yêu Cầu Trước Khi Bắt Đầu

Trước khi bắt đầu, hãy đảm bảo bạn có:
- Java 21 hoặc cao hơn đã được cài đặt
- Maven để quản lý phụ thuộc
- Một tài khoản GitHub với personal access token (PAT)
- Hiểu biết cơ bản về Java và Spring Boot

## Hiểu Cấu Trúc Dự Án

Dự án máy tính bao gồm một số tệp quan trọng:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## Giải Thích Các Thành Phần Chính

### 1. Ứng Dụng Chính

**Tệp:** `McpServerApplication.java`

Đây là điểm khởi đầu của dịch vụ máy tính. Đây là một ứng dụng Spring Boot tiêu chuẩn với một bổ sung đặc biệt:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Chức năng:**
- Khởi động một máy chủ web Spring Boot trên cổng 8080
- Tạo một `ToolCallbackProvider` để làm cho các phương thức máy tính của chúng ta có sẵn như các công cụ MCP
- Annotation `@Bean` cho phép Spring quản lý thành phần này để các phần khác có thể sử dụng

### 2. Dịch Vụ Máy Tính

**Tệp:** `CalculatorService.java`

Đây là nơi thực hiện tất cả các phép toán. Mỗi phương thức được đánh dấu bằng `@Tool` để có thể truy cập qua MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Các tính năng chính:**

1. **Annotation `@Tool`**: Cho MCP biết rằng phương thức này có thể được gọi bởi các khách hàng bên ngoài
2. **Mô Tả Rõ Ràng**: Mỗi công cụ có một mô tả giúp các mô hình AI hiểu khi nào nên sử dụng
3. **Định Dạng Kết Quả Nhất Quán**: Tất cả các phép toán trả về chuỗi dễ đọc như "5.00 + 3.00 = 8.00"
4. **Xử Lý Lỗi**: Chia cho 0 và căn bậc hai số âm trả về thông báo lỗi

**Các Phép Toán Có Sẵn:**
- `add(a, b)` - Cộng hai số
- `subtract(a, b)` - Trừ số thứ hai khỏi số thứ nhất
- `multiply(a, b)` - Nhân hai số
- `divide(a, b)` - Chia số thứ nhất cho số thứ hai (kiểm tra chia cho 0)
- `power(base, exponent)` - Lũy thừa của cơ số với số mũ
- `squareRoot(number)` - Tính căn bậc hai (kiểm tra số âm)
- `modulus(a, b)` - Trả về phần dư của phép chia
- `absolute(number)` - Trả về giá trị tuyệt đối
- `help()` - Trả về thông tin về tất cả các phép toán

### 3. Khách Hàng MCP Trực Tiếp

**Tệp:** `SDKClient.java`

Khách hàng này giao tiếp trực tiếp với máy chủ MCP mà không sử dụng AI. Nó gọi thủ công các hàm máy tính cụ thể:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Chức năng:**
1. **Kết Nối** với máy chủ máy tính tại `http://localhost:8080`
2. **Liệt Kê** tất cả các công cụ có sẵn (các hàm máy tính của chúng ta)
3. **Gọi** các hàm cụ thể với tham số chính xác
4. **In** kết quả trực tiếp

**Khi nào sử dụng:** Khi bạn biết chính xác phép toán cần thực hiện và muốn gọi nó bằng lập trình.

### 4. Khách Hàng Sử Dụng AI

**Tệp:** `LangChain4jClient.java`

Khách hàng này sử dụng một mô hình AI (GPT-4o-mini) có thể tự động quyết định công cụ máy tính nào cần sử dụng:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Chức năng:**
1. **Tạo** kết nối với mô hình AI sử dụng token GitHub của bạn
2. **Kết Nối** AI với máy chủ MCP máy tính của chúng ta
3. **Cung Cấp** cho AI quyền truy cập vào tất cả các công cụ máy tính
4. **Cho Phép** yêu cầu ngôn ngữ tự nhiên như "Tính tổng của 24.5 và 17.3"

**AI tự động:**
- Hiểu rằng bạn muốn cộng các số
- Chọn công cụ `add`
- Gọi `add(24.5, 17.3)`
- Trả về kết quả trong một phản hồi tự nhiên

## Chạy Các Ví Dụ

### Bước 1: Khởi Động Máy Chủ Máy Tính

Đầu tiên, thiết lập token GitHub của bạn (cần thiết cho khách hàng AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Khởi động máy chủ:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Máy chủ sẽ khởi động tại `http://localhost:8080`. Bạn sẽ thấy:
```
Started McpServerApplication in X.XXX seconds
```

### Bước 2: Kiểm Tra Với Khách Hàng Trực Tiếp

Trong một terminal mới:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Bạn sẽ thấy đầu ra như:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Bước 3: Kiểm Tra Với Khách Hàng AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Bạn sẽ thấy AI tự động sử dụng các công cụ:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Cách Mọi Thứ Hoạt Động Cùng Nhau

Dưới đây là luồng hoàn chỉnh khi bạn hỏi AI "5 + 3 là bao nhiêu?":

1. **Bạn** hỏi AI bằng ngôn ngữ tự nhiên
2. **AI** phân tích yêu cầu của bạn và nhận ra bạn muốn cộng
3. **AI** gọi máy chủ MCP: `add(5.0, 3.0)`
4. **Dịch Vụ Máy Tính** thực hiện: `5.0 + 3.0 = 8.0`
5. **Dịch Vụ Máy Tính** trả về: `"5.00 + 3.00 = 8.00"`
6. **AI** nhận kết quả và định dạng phản hồi tự nhiên
7. **Bạn** nhận được: "Tổng của 5 và 3 là 8"

## Bước Tiếp Theo

Để xem thêm ví dụ, hãy tham khảo [Chương 04: Các ví dụ thực tế](../../README.md)

**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được coi là nguồn thông tin chính thức. Đối với các thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.