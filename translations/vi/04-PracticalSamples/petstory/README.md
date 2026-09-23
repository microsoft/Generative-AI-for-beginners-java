# Hướng Dẫn Tạo Truyện Về Thú Cưng Cho Người Mới Bắt Đầu

Tải ảnh thú cưng lên, phân tích bằng GPT-5.6 Luna, và tạo ra một câu chuyện dựa trên mô tả kết quả. Cả hai yêu cầu mô hình đều sử dụng `reasoning_effort: none`.

| Thành phần | Phiên bản |
| --- | --- |
| Java | 21 hoặc cao hơn |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Mục lục

- [Yêu cầu tiên quyết](#yêu-cầu-tiên-quyết)
- [Hiểu cấu trúc dự án](#hiểu-cấu-trúc-dự-án)
- [Giải thích các thành phần chính](#giải-thích-các-thành-phần-chính)
  - [1. Ứng dụng chính](#1-ứng-dụng-chính)
  - [2. Bộ điều khiển web](#2-bộ-điều-khiển-web)
  - [3. Dịch vụ truyện](#3-dịch-vụ-truyện)
  - [4. Mẫu web](#4-mẫu-web)
  - [5. Cấu hình](#5-cấu-hình)
- [Chạy ứng dụng](#chạy-ứng-dụng)
- [Kiểm tra offline](#kiểm-tra-offline)
- [Cách hoạt động tổng thể](#cách-hoạt-động-tổng-thể)
- [Hiểu tích hợp AI](#hiểu-tích-hợp-ai)
- [Bước tiếp theo](#bước-tiếp-theo)

## Yêu cầu tiên quyết

Trước khi bắt đầu, hãy chắc chắn bạn có:
- Java 21 hoặc cao hơn đã được cài đặt
- Maven để quản lý phụ thuộc
- Một triển khai Azure AI Foundry của GPT-5.6 Luna tên là `gpt-5.6-luna`, hoặc một ghi đè `AZURE_OPENAI_DEPLOYMENT` trỏ tới triển khai đó. Xem [Chương 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) để cấu hình và đăng nhập với `az login` cho xác thực không cần khóa. Triển khai này phải hỗ trợ nhập ảnh và `reasoning_effort: none`.
- Hiểu biết cơ bản về Java, Spring Boot, và phát triển web

## Hiểu cấu trúc dự án

Dự án truyện thú cưng có một số tệp quan trọng:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Giải thích các thành phần chính

### 1. Ứng dụng chính

**Tệp:** `PetStoryApplication.java`

Đây là điểm bắt đầu cho ứng dụng Spring Boot của chúng ta:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Công việc thực hiện:**
- Chú thích `@SpringBootApplication` kích hoạt cấu hình tự động và quét thành phần
- Khởi động một máy chủ web nhúng (Tomcat) trên cổng 8080
- Tự động tạo tất cả các bean và dịch vụ Spring cần thiết

### 2. Bộ điều khiển web

**Tệp:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Điểm cuối | Yêu cầu | Phản hồi thành công |
| --- | --- | --- |
| `GET /` | Không có thân yêu cầu | Biểu mẫu HTML tải lên cùng token CSRF |
| `POST /analyze-image` | `multipart/form-data`, trường file `image` | JSON: `{"description":"Một thú cưng nghịch ngợm..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, trường `description` | Trang kết quả HTML với mô tả và câu chuyện được tạo |

Cả hai điểm cuối POST đều yêu cầu cookie phiên làm việc và token CSRF lấy từ `GET /`. Script tải lên gửi giá trị ẩn `_csrf` trong header `X-CSRF-TOKEN`; gửi truyện thì gửi nó dưới dạng trường `_csrf` trong form. Khách hàng API phải giữ cookie giữa các yêu cầu. Đây là các điểm cuối dạng form, không phải yêu cầu JSON.

Mô tả phải không rỗng và không dài hơn 1000 ký tự. Bộ điều khiển cắt bớt mô tả và loại bỏ `<`, `>`, dấu ngoặc kép, dấu nháy và `&` trước khi chuyển cho dịch vụ. Mẫu kết quả cũng thoát đầu ra của mô hình bằng `th:text`.

Lỗi xác thực ảnh trả về HTTP 400 với trường `error`; lỗi mô hình trả HTTP 502 với trường `error` và không có `description`. Mô tả truyện không hợp lệ hoặc lỗi mô hình chuyển hướng về `/` với thông báo lỗi hiển thị. Thiếu trường bắt buộc trả HTTP 400, thiếu hoặc token CSRF không hợp lệ trả HTTP 403. Không có mô tả hoặc truyện thay thế được trình bày như kết quả AI thành công.

### 3. Dịch vụ truyện

**Tệp:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

SDK chính thức OpenAI Java 4.63.1 gọi API Chat Completions phù hợp OpenAI của Azure AI Foundry. Azure Identity 1.18.6 cung cấp token người giữ Microsoft Entra qua `DefaultAzureCredential`; không cần khóa API.

| Hoạt động | Đầu vào | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Dữ liệu ảnh mã hóa base64 theo định dạng MIME tải lên | 300 |
| `generateStory` | Mô tả thú cưng trong một tin nhắn người dùng | 800 |

Cả hai yêu cầu dùng triển khai cấu hình, mặc định `gpt-5.6-luna`, và thiết lập rõ `ReasoningEffort.NONE` (`reasoning_effort: none`). Không gửi `temperature` hay tham số `max_tokens` cũ.

Phân tích ảnh chấp nhận JPEG, PNG, GIF, WebP; từ chối ảnh rỗng và tệp >10MB, mô tả kết quả giới hạn 1000 ký tự. Đề bài truyện yêu cầu truyện ngắn thân thiện gia đình. Lựa chọn rỗng hoặc nội dung mô hình trống là lỗi, lỗi được giữ lại nguyên nhân để chẩn đoán máy chủ. Khách đóng SDK sẽ được đóng khi ứng dụng tắt.

### 4. Mẫu web

**Tệp:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Biểu mẫu tải lên)

Trang bắt đầu với bộ chọn ảnh, không phải vùng nhập mô tả. **Phân tích ảnh** xem trước ảnh đã chọn và gửi đến `/analyze-image`. Phản hồi thành công hiển thị mô tả, điền trường `description` ẩn, và hiện nút **Tạo truyện**. Nút đó gửi form hiện có đến `/generate-story`.

Không có tải mô hình theo trình duyệt hay phụ thuộc CDN. Phân tích ảnh chạy trên máy chủ qua triển khai Azure đã cấu hình. Lỗi vẫn hiển thị và không cho phép tạo truyện với mô tả bịa đặt. Chọn file khác xóa phân tích trước đó.

**Tệp:** `result.html` (Hiển thị truyện)

Hiển thị câu chuyện được tạo:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Đặc điểm mẫu:**

1. **Tích hợp Thymeleaf**: Sử dụng thuộc tính `th:` cho nội dung động
2. **Thiết kế phản hồi**: CSS cho di động và máy tính để bàn
3. **Xử lý lỗi**: Hiển thị lỗi xác thực cho người dùng
4. **Xử lý tải lên**: JavaScript xem trước ảnh, gửi yêu cầu multipart có CSRF, và hiển thị mô tả trả về

### 5. Cấu hình

**Tệp:** `application.properties`

Thiết lập cấu hình cho ứng dụng:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Giải thích cấu hình:**

1. **Tải tập tin**: Cả tập tin và toàn bộ yêu cầu multipart đều giới hạn 10MB; giữ ảnh dưới giới hạn để còn chỗ cho header multipart
2. **Ghi log**: Điều khiển thông tin được ghi trong quá trình chạy
3. **Azure AI Foundry**: Chỉ định điểm cuối và triển khai mô hình sử dụng (xác thực không khóa)
4. **Bảo mật**: Bảo vệ CSRF vẫn bật; chẩn đoán mô hình được ghi trên máy chủ, bộ điều khiển hiển thị thông báo lỗi mô hình chung chung

## Chạy ứng dụng

### Bước 1: Đăng nhập và đặt đầu cuối

Xác thực không cần khóa (Microsoft Entra ID), nên không có khóa API. Đăng nhập và đặt đầu cuối Foundry của bạn:

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Tại sao cần điều này:**
- Azure AI Foundry dùng Microsoft Entra ID để xác thực các yêu cầu suy luận
- Xác thực không khóa nghĩa là không có bí mật trong mã nguồn hay môi trường
- Tài khoản của bạn cần vai trò **Cognitive Services OpenAI User** trên tài nguyên

Tên triển khai mặc định là `gpt-5.6-luna`. Nếu triển khai GPT-5.6 Luna của bạn có tên khác, hãy đặt biến `AZURE_OPENAI_DEPLOYMENT` trong cùng terminal trước khi bắt đầu ứng dụng. Cả phân tích ảnh và tạo truyện đều dùng thiết lập này.

### Bước 2: Xây dựng và chạy

Điều hướng đến thư mục dự án:
```bash
cd 04-PracticalSamples/petstory
```

Xây dựng tệp JAR thực thi độc lập và chạy tất cả kiểm tra offline:
```bash
mvn clean package
```

Khởi động máy chủ:
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại `http://localhost:8080`.

Ngoài ra, khởi động tệp JAR trên một cổng tự do, ví dụ:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Với lệnh đó, mở `http://localhost:8083/`. Các tuyến `/analyze-image` và `/generate-story` đều có trên cổng được chọn.

### Bước 3: Kiểm tra ứng dụng

1. **Mở** `http://localhost:8080` trong trình duyệt
2. **Chọn** ảnh thú cưng rõ nét định dạng JPEG, PNG, GIF, hoặc WebP, dưới 10MB
3. **Nhấn** "Phân tích ảnh" và chờ mô tả thú cưng
4. **Nhấn** "Tạo truyện" sau khi phân tích thành công
5. **Xem** truyện và dùng liên kết trên trang kết quả để quay lại biểu mẫu tải lên

Quy trình ảnh thành truyện thành công gọi hai lần mô hình, mỗi nút một lần. Suy luận trực tiếp tiêu thụ hạn mức triển khai và có thể phát sinh phí; chạy kiểm tra nhanh tuần tự khi chia sẻ triển khai bị giới hạn tần suất. Tải trang chủ không gọi mô hình.

## Kiểm tra offline

Từ thư mục mẫu, chạy:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) ghi lại các yêu cầu thực sự của SDK OpenAI bằng một fixture HTTP loopback. Nó kiểm tra khai triển, `reasoning_effort: none`, giới hạn token, payload ảnh, xác thực đầu vào, phản hồi rỗng, và lỗi thượng nguồn của cả hai yêu cầu.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) dùng MockMvc với dịch vụ mô hình mô phỏng để kiểm thử các trang Thymeleaf được render, hợp đồng tải lên, CSRF, xác thực, thoát dữ liệu đầu ra, và lỗi hiển thị. Những kiểm tra này không cần thông tin Azure và không gọi suy luận Azure trả phí. Maven ghi báo cáo Surefire trong `target/surefire-reports`.

## Cách hoạt động tổng thể

Dưới đây là quy trình đầy đủ khi bạn tạo truyện cho thú cưng:

1. **Chọn ảnh**: Bạn chọn ảnh thú cưng trong biểu mẫu tải lên
2. **Tải ảnh lên**: "Phân tích ảnh" gửi POST multipart đến `/analyze-image` kèm header CSRF
3. **Phân tích ảnh**: `StoryService` gửi ảnh đến GPT-5.6 Luna với chế độ reasoning đặt thành `none`
4. **Hiển thị mô tả**: Trình duyệt hiển thị mô tả trả về và lưu nó vào form
5. **Gửi truyện**: "Tạo truyện" gửi các trường `description` và `_csrf` đến `/generate-story`
6. **Tạo truyện**: Bộ điều khiển xác thực mô tả và gọi cùng triển khai với reasoning là `none`
7. **Render mẫu**: Thymeleaf thoát và hiển thị mô tả cùng truyện trên trang kết quả

**Luồng xử lý lỗi:**
Nếu mô hình lỗi, máy chủ ghi log nguyên nhân. Phân tích ảnh trả HTTP 502 và trình duyệt hiển thị lỗi mà không hiện "Tạo truyện". Tạo truyện chuyển hướng về form với thông báo lỗi. Không có lối đi nào thay thế âm thầm với kết quả có sẵn.

## Hiểu tích hợp AI

### Azure AI Foundry (xác thực không khóa)
Dịch vụ cấu hình SDK với điểm cuối `/openai/v1/` của tài nguyên bạn. `DefaultAzureCredential` và `AuthenticationUtil.getBearerTokenSupplier` cung cấp token Microsoft Entra cho `https://ai.azure.com/.default`. Phát triển cục bộ dùng đăng nhập Azure CLI của bạn; ứng dụng host trên Azure có thể dùng managed identity với quyền tài nguyên cần thiết.

### Kỹ thuật Đề bài
Yêu cầu phân tích ảnh quan sát các đặc điểm thú cưng trong đoạn văn ngắn và bảo mô hình xem đoạn chữ trong ảnh như dữ liệu, không phải lệnh. Tạo truyện dùng mô tả trả về trong yêu cầu viết riêng thân thiện với gia đình. Cả hai không bật reasoning hay điều chỉnh nhiệt độ.

### Xử lý phản hồi
Bộ xử lý phản hồi chia sẻ từ chối lựa chọn thiếu và nội dung trống hoặc chỉ toàn khoảng trắng, cắt bớt nội dung hợp lệ, và giữ nguyên lỗi thượng nguồn. Mô tả ảnh giới hạn ở 1000 ký tự để phù hợp form truyện phía sau. Lỗi mô hình gốc được giữ lại để chẩn đoán nhưng không được hiện với người dùng.

## Bước tiếp theo

Để xem thêm ví dụ, hãy xem [Chương 04: Các mẫu thực hành](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ gốc nên được coi là nguồn tin chính thức. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->