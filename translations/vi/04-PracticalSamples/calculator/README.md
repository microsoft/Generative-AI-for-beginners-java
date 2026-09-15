# Hướng Dẫn Máy Tính MCP cho Người Mới Bắt Đầu

## Mục Lục

- [Bạn Sẽ Học Gì](#bạn-sẽ-học-gì)
- [Yêu Cầu Trước](#yêu-cầu-trước)
- [Phiên Bản Phụ Thuộc](#phiên-bản-phụ-thuộc)
- [Hiểu Cấu Trúc Dự Án](#hiểu-cấu-trúc-dự-án)
- [Giải Thích Các Thành Phần Cốt Lõi](#giải-thích-các-thành-phần-cốt-lõi)
  - [1. Ứng Dụng Chính](#1-ứng-dụng-chính)
  - [2. Dịch Vụ Máy Tính](#2-dịch-vụ-máy-tính)
  - [3. Khách Hàng MCP Trực Tiếp](#3-khách-hàng-mcp-trực-tiếp)
  - [4. Khách Hàng Sử Dụng AI](#4-khách-hàng-sử-dụng-ai)
- [Chạy Các Ví Dụ](#chạy-các-ví-dụ)
- [Kiểm Tra Ngoại Tuyến](#kiểm-tra-ngoại-tuyến)
- [Cách Mọi Thứ Hoạt Động Cùng Nhau](#cách-mọi-thứ-hoạt-động-cùng-nhau)
- [Bước Tiếp Theo](#bước-tiếp-theo)

## Bạn Sẽ Học Gì

Hướng dẫn này giải thích cách xây dựng một dịch vụ máy tính sử dụng Giao Thức Ngữ Cảnh Mô Hình (MCP). Bạn sẽ hiểu:

- Cách tạo một dịch vụ mà AI có thể dùng làm công cụ
- Cách thiết lập giao tiếp trực tiếp với các dịch vụ MCP
- Cách các mô hình AI tự động chọn công cụ phù hợp để sử dụng
- Sự khác biệt giữa các cuộc gọi giao thức trực tiếp và tương tác được trợ giúp bởi AI

## Yêu Cầu Trước

Trước khi bắt đầu, hãy đảm bảo bạn có:
- Java 21 hoặc cao hơn đã cài đặt
- Maven để quản lý phụ thuộc
- Hiểu biết cơ bản về Java và Spring Boot

Chỉ các khách hàng AI mới yêu cầu một triển khai Azure OpenAI và `DefaultAzureCredential` đã xác thực,
như đã đăng nhập Azure CLI hiện có trên máy hoặc một danh tính được quản lý trong Azure. Danh tính cần
vai trò Người dùng Cognitive Services OpenAI trên tài nguyên. Xem [Chương 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Máy chủ, khách hàng SDK trực tiếp và tất cả các bài kiểm tra tự động không cần tài khoản Azure hoặc truy cập mô hình.

## Phiên Bản Phụ Thuộc

Các phụ thuộc phiên bản phát hành được xác minh vào ngày 2026-09-14:

| Phụ Thuộc | Phiên Bản |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (do Spring AI quản lý) | 2.0.0 |
| LangChain4j / cốt lõi | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Bộ chuyển đổi OpenAI chính thức LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (do Boot quản lý) | 6.0.3 |

Các bộ chuyển đổi MCP và OpenAI chính thức là bản phát hành beta được xuất bản trên Maven Central, không phải bản snapshot.
Phiên bản của chúng khác với LangChain4j cốt lõi. Không cần kho lưu trữ snapshot hoặc milestone.
Phụ thuộc chỉ dùng cho khách hàng có phạm vi test vì các ví dụ có thể chạy nằm trong `src/test/java`.

## Hiểu Cấu Trúc Dự Án

Dự án máy tính có một số tệp quan trọng:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Giải Thích Các Thành Phần Cốt Lõi

### 1. Ứng Dụng Chính

**Tệp:** `McpServerApplication.java`

Đây là điểm vào của dịch vụ máy tính của chúng ta. Nó là một ứng dụng Spring Boot tiêu chuẩn với một bổ sung đặc biệt:

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

**Điều này làm gì:**
- Khởi động một máy chủ web Spring Boot trên cổng 8080
- Tạo một `ToolCallbackProvider` để các phương thức máy tính của chúng ta được dùng như công cụ MCP
- Chú thích `@Bean` bảo Spring quản lý thành phần này để các phần khác có thể dùng

### 2. Dịch Vụ Máy Tính

**Tệp:** `CalculatorService.java`

Đây là nơi thực hiện mọi phép toán. Mỗi phương thức được đánh dấu `@Tool` để cho phép truy cập qua MCP:

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
    
    // Thêm các phép toán máy tính...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Tính năng chính:**

1. **Chú thích `@Tool`**: Cho MCP biết phương thức này có thể được gọi bởi khách hàng bên ngoài
2. **Mô tả rõ ràng**: Mỗi công cụ có mô tả giúp mô hình AI hiểu khi nào nên dùng
3. **Định dạng trả về nhất quán**: Tất cả các phép toán trả về chuỗi dễ đọc như "5.00 + 3.00 = 8.00"
4. **Xử lý lỗi**: Chia cho 0 và căn bậc hai số âm trả về thông báo lỗi

**Các phép toán có sẵn:**
- `add(a, b)` - Cộng hai số
- `subtract(a, b)` - Trừ số thứ hai từ số thứ nhất
- `multiply(a, b)` - Nhân hai số
- `divide(a, b)` - Chia số thứ nhất cho số thứ hai (có kiểm tra chia 0)
- `power(base, exponent)` - Lũy thừa cơ số với số mũ
- `squareRoot(number)` - Tính căn bậc hai (có kiểm tra số âm)
- `modulus(a, b)` - Trả về phần dư phép chia
- `absolute(number)` - Trả về giá trị tuyệt đối
- `help()` - Trả về thông tin về tất cả các phép toán

### 3. Khách Hàng MCP Trực Tiếp

Xem [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Khách hàng này sử dụng `HttpClientStreamableHttpTransport` tại `/mcp`, khởi tạo kết nối,
ping máy chủ, và theo dõi phân trang danh sách công cụ. Nó kiểm tra tất cả chín công cụ dự kiến
tồn tại và gọi từng công cụ trong đó có `modulus` và `help`, không cần mô hình AI.

Bộ tạo yêu cầu hiện tại trông như sau:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Lỗi giao thức sẽ khiến khách hàng thất bại thay vì in ra thành công gây hiểu nhầm. Khách hàng MCP
được đóng bằng try-with-resources, kể cả khi phát hiện hoặc gọi công cụ thất bại.

### 4. Khách Hàng Sử Dụng AI

Xem [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
và [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` triển khai API `ChatModel` hiện tại của LangChain4j.
`StreamableHttpMcpTransport` kết nối nó tới cùng điểm cuối `/mcp` như khách hàng SDK.
`AiServices` phát hiện công cụ và quản lý hội thoại gọi công cụ/kết quả.

Triển khai mặc định là **GPT-5.6 Luna**, với lý luận bị tắt rõ ràng:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Các mặc định này áp dụng cho mọi lần hoàn thành, bao gồm các bước tiếp theo sau khi chạy công cụ.
Khách hàng sử dụng `BearerTokenCredential` có thể làm mới, dựa trên `DefaultAzureCredential`
và phạm vi `https://ai.azure.com/.default`, không phải token một lần truyền dưới dạng khóa API.
URL tài nguyên và URL đã kết thúc bằng `/openai/v1` đều được chấp nhận.

Bot giữ lịch sử hội thoại có giới hạn, in `Tool executed: ...` cùng kết quả thực tế
từ MCP, và thất bại nếu một phản hồi bỏ qua công cụ. Vòng lặp công cụ giới hạn bốn lượt.
Các lỗi xác thực, mô hình, MCP và công cụ được truyền; tự động thử lại mô hình bị vô hiệu.
Cả MCP transport/khách hàng và khách hàng OpenAI chính thức đều đóng khi thành công hoặc thất bại.

## Chạy Các Ví Dụ

### Bước 1: Khởi Động Máy Chủ Máy Tính

Không cần cấu hình Azure cho máy chủ. Các lệnh dưới đây chạy từ thư mục mẫu này.
Ví dụ dùng cổng **18081** để tránh xung đột với một ví dụ khác; mặc định vẫn là 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Điểm cuối MCP là `http://localhost:18081/mcp`. Thông tin trạng thái và khám phá có tại
`http://localhost:18081/health` và `http://localhost:18081/info`.
HTTP Streamable thay thế giao thức SSE cũ; `/sse` và `/v1/tools` không phải điểm cuối.

### Bước 2: Kiểm Tra Với Khách Hàng Trực Tiếp

Trong một terminal PowerShell khác:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Không cần nhập dữ liệu. Tất cả chín công cụ đều được thực hiện. Kết quả số học dự kiến gồm
8, 6, 42, 5, 256, 4, 2 và 5.5, tiếp theo là văn bản trợ giúp.

### Bước 3: Kiểm Tra Với Khách Hàng AI

Sau khi xác thực như mô tả ở phần yêu cầu trước, cấu hình khách hàng AI trong cùng terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Mong nhận được dòng `Tool executed: add` với kết quả `41.80`, tiếp theo là câu trả lời của mô hình.
Chế độ nhắc đơn thoát mà không đợi nhập liệu. Để chạy bản demo bốn nhắc lệnh ban đầu:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Bản demo gọi các phép `add`, `squareRoot`, `help`, và chuỗi thao tác `power` rồi `divide`.
Các câu trả lời số dự kiến là 41.8, 12 và 64. Bỏ qua đối số cũng chạy demo này.

### Bước 4: Chạy Bot Tương Tác

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Nhập `Multiply 6 by 7 using the calculator service`, rồi nhập `exit` hoặc `quit`.
Mong nhận kết quả thực sự công cụ `multiply` là 42. Dòng trống bị bỏ qua; EOF cũng kết thúc phiên.
Để kiểm tra không tương tác cho điểm vào này:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Cả hai điểm vào AI đều chấp nhận `--prompt "question"`, `--demo`, và `--interactive`.
Tùy chọn không hợp lệ thất bại trước khi mở kết nối. Mỗi tham số Maven `-D...` được bao trọn vẹn
cho PowerShell. Trên Bash, dùng `export NAME=value` thay cho `$env:NAME = "value"`.

**Hạn mức:** Chạy các ví dụ AI tuần tự. Một câu nhắc đơn thường cần hai yêu cầu đến mô hình;
bản demo hoàn chỉnh thường cần chín, bao gồm các bước tiếp theo sau khi có kết quả công cụ. Với triển khai chia sẻ 10 RPM,
hãy chờ cửa sổ hạn mức mới trước khi chạy AI lần kế tiếp. Mã lỗi 429 gây thất bại rõ ràng không có thử lại tự động;
làm theo hướng dẫn retry-after của dịch vụ. Số yêu cầu thực tế tùy thuộc mô hình.
Các kiểm tra ngoại tuyến không tiêu hao hạn mức và không thiết lập khả năng sẵn có hay chất lượng trả lời của Luna.

### Cấu Hình và Tắt Máy Chủ

| Cài Đặt | Mặc Định / Hành Vi |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL cơ sở, không có `/mcp` |
| `-Dmcp.server.url=...` | Ghi đè `MCP_SERVER_URL` cho tất cả khách hàng |
| `AZURE_OPENAI_ENDPOINT` | Chỉ cần cho khách hàng AI; URL tài nguyên hoặc URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; tên triển khai Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; số nguyên dương |
| Nỗ lực lý luận | Luôn là `none`, bao gồm các bước tiếp theo vòng lặp công cụ |

Một triển khai bị ghi đè phải hỗ trợ `reasoning_effort=none` và `max_completion_tokens`.
Các khách hàng không tự động đọc tệp `.env`. Dừng máy chủ bằng `Ctrl+C` sau khi kiểm tra.
Khách hàng trả về bình thường, không dùng `System.exit` hay ngủ khi tắt.

## Kiểm Tra Ngoại Tuyến

```powershell
mvn -B -ntp clean verify
```

Tất cả kiểm tra đều ngoại tuyến so với Azure: bộ giao thức khởi động một máy chủ Spring và
một stub tương thích OpenAI trên các cổng loopback ngẫu nhiên, rồi đóng lại. Maven vẫn có thể cần
tải phụ thuộc. Không sử dụng thông tin xác thực, triển khai trực tiếp hay máy chủ MCP tồn tại.

- Kiểm tra đơn vị máy tính bao phủ tất cả phép toán số học, kết quả thập phân, trợ giúp và lỗi miền.
- Kiểm tra MCP bao phủ khởi tạo, khám phá, gọi chín công cụ, lỗi công cụ, và sức khỏe/thông tin.
- Kiểm tra giao thức AI thực thi demo đầy đủ và Bot tương tác với máy tính thật,
  kiểm tra kết quả công cụ cung cấp cho lần hoàn thành tiếp theo, và kiểm tra từng HTTP body cho Luna,
  `reasoning_effort: "none"`, và `max_completion_tokens` không có `max_tokens` cũ.
- Kiểm tra cấu hình/đầu vào phủ các ghi đè triển khai và điểm cuối, dòng trống, EOF, exit/quit,
  chế độ nhắc đơn, tùy chọn không hợp lệ, và truyền lỗi. Kiểm tra hạn mức chứng minh lỗi 429 không thử lại.

## Cách Mọi Thứ Hoạt Động Cùng Nhau

Đây là quy trình hoàn chỉnh khi bạn hỏi AI "5 + 3 bằng bao nhiêu?":

1. **Bạn** hỏi AI bằng ngôn ngữ tự nhiên
2. **AI** phân tích yêu cầu và nhận ra bạn muốn cộng số
3. **AI** gọi máy chủ MCP: `add(5.0, 3.0)`
4. **Dịch Vụ Máy Tính** thực hiện: `5.0 + 3.0 = 8.0`
5. **Dịch Vụ Máy Tính** trả về: `"5.00 + 3.00 = 8.00"`
6. **AI** nhận kết quả và định dạng phản hồi tự nhiên
7. **Bạn** nhận được: "Tổng của 5 và 3 là 8"

## Bước Tiếp Theo

Để xem thêm ví dụ, xem [Chương 04: Ví dụ thực tế](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ gốc nên được coi là nguồn tin chính thức. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->