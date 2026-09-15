# Chat Cơ Bản với Azure AI Foundry - Ví dụ Toàn diện

Ví dụ này là một ứng dụng Spring Boot đơn giản kết nối với mô hình **Azure AI Foundry** sử dụng **xác thực không cần khóa** (Microsoft Entra ID) và kiểm tra thiết lập của bạn. Nó giữ lại `ChatClient` của Spring AI, được hỗ trợ bởi **OpenAI Java SDK chính thức** và điểm cuối **Azure OpenAI v1**.

Các phiên bản trong [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) là Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, và dotenv-java **3.2.0**. Mẫu sử dụng `spring-ai-starter-model-openai` và khai báo rõ ràng `openai-java` cùng `azure-identity`; Spring AI 2 đã loại bỏ starter Azure OpenAI cũ.

## Mục Lục

- [Yêu Cầu Trước](#yêu-cầu-trước)
- [Khởi Đầu Nhanh](#khởi-đầu-nhanh)
- [Cách Xác Thực Hoạt Động](#cách-xác-thực-hoạt-động)
- [Chạy Ứng Dụng](#chạy-ứng-dụng)
  - [Dùng Maven](#dùng-maven)
  - [Dùng VS Code](#dùng-vs-code)
  - [Đầu Ra Mong Đợi](#đầu-ra-mong-đợi)
- [Tham Chiếu Cấu Hình](#tham-chiếu-cấu-hình)
  - [Biến Môi Trường](#biến-môi-trường)
  - [Cấu Hình Spring](#cấu-hình-spring)
- [Khắc Phục Sự Cố](#khắc-phục-sự-cố)
  - [Vấn Đề Thường Gặp](#vấn-đề-thường-gặp)
  - [Chế Độ Gỡ Lỗi](#chế-độ-gỡ-lỗi)
- [Bước Tiếp Theo](#bước-tiếp-theo)
- [Tài Nguyên](#tài-nguyên)

## Yêu Cầu Trước

Trước khi chạy ví dụ này, hãy đảm bảo bạn có:

- Một tài nguyên Azure AI Foundry với triển khai `gpt-5.6-luna` - tạo nó bằng `azd up` hoặc thủ công qua [hướng dẫn thiết lập Azure AI Foundry](../../getting-started-azure-openai.md)
- Vai trò **Cognitive Services OpenAI User** trên tài nguyên đó (các mẫu Bicep tự động gán vai trò này cho bạn)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), đã đăng nhập bằng `az login`
- Java 21+ và Maven 3.9+

> **Không cần khóa API** — xác thực không cần khóa qua Microsoft Entra ID.

## Khởi Đầu Nhanh

```bash
# 1. Điều hướng đến dự án
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Đăng nhập để xác thực không cần khóa có thể lấy token
az login

# 3. Cấu hình điểm cuối
#    - Nếu bạn đã chạy `azd up`, .env đã được tạo sẵn cho bạn (bỏ qua bước này).
#    - Nếu không, sao chép mẫu và thiết lập AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Chạy ứng dụng
mvn spring-boot:run
```

## Cách Xác Thực Hoạt Động

Ví dụ này xác thực với **Microsoft Entra ID** — không có khóa API.

Ứng dụng cấu hình xác thực rõ ràng trong [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` tạo `BearerTokenCredential` sử dụng `AuthenticationUtil.getBearerTokenSupplier` với `DefaultAzureCredential` và phạm vi `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` xây dựng `OpenAIClient` với `OpenAIOkHttpClient.builder()`, giải quyết điểm cuối tài nguyên tới `/openai/v1`, và cung cấp thông tin xác thực bearer với `.credential(...)`.
3. `azureChatModel()` cung cấp client đó cho `OpenAiChatModel` của Spring AI, hỗ trợ `ChatClient` trong bài học.

Những bean rõ ràng này giữ cho biến toàn cục `OPENAI_API_KEY` không ghi đè xác thực Azure. Chỉ bỏ biến API key trong YAML thì không đủ để thiết lập xác thực. `DefaultAzureCredential` có thể dùng phiên làm việc `az login` của bạn tại máy hoặc identity được quản lý trong Azure; bất kỳ identity nào được chọn phải có vai trò tài nguyên như trên.

## Chạy Ứng Dụng

### Dùng Maven

```bash
mvn spring-boot:run
```

### Dùng VS Code

1. Mở dự án trong VS Code
2. Nhấn `F5` hoặc dùng bảng "Run and Debug"
3. Chọn cấu hình "Spring Boot-BasicChatApplication"

> **Lưu ý**: Ứng dụng tải `.env` từ thư mục làm việc, bao gồm khi khởi chạy từ VS Code.

### Đầu Ra Mong Đợi

Kết quả minh họa sau khi chạy thành công (bỏ qua log khởi động; câu trả lời có thể khác):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Tham Chiếu Cấu Hình

### Biến Môi Trường

| Biến | Mô Tả | Bắt Buộc | Ví Dụ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL điểm cuối Foundry (Azure OpenAI) | Có | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Tên triển khai mô hình chat | Không | `gpt-5.6-luna` (mặc định) |

> Không có biến khóa API — xác thực không cần khóa (Microsoft Entra ID qua `az login`).

### Cấu Hình Spring

Cài đặt trong [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) dùng tiền tố `spring.ai.openai` và các thuộc tính chat dạng phẳng (không có khối `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` là **tên triển khai Azure**. Xác thực lấy từ các bean rõ ràng như mô tả ở trên, không phải thiết lập `api-key`. Bài học tắt tính toán và giới hạn token hoàn thành tối đa 500; để trống `temperature` và `max-tokens` cũ.

Microsoft đề xuất [OpenAI SDK chính thức với Azure OpenAI v1 và Responses API cho ứng dụng mới](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions vẫn được hỗ trợ cho bài học dựa trên tin nhắn hiện tại. Với GPT-5.6, yêu cầu có công cụ trên Chat Completions phải đặt `reasoning_effort` thành `none`; dùng Responses khi kết hợp reasoning với công cụ. Xem [gọi công cụ với mô hình reasoning](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Khắc Phục Sự Cố

### Vấn Đề Thường Gặp

<details>
<summary><strong>Lỗi: 401 / "PermissionDenied" / lỗi token</strong></summary>

- Chạy `az login` — xác thực không khóa cần đăng nhập hiện hoạt để lấy token
- Xác nhận tài khoản có vai trò **Cognitive Services OpenAI User** trên tài nguyên
- Nếu mới gán vai trò, chờ một phút để vai trò được áp dụng
- Xác nhận bạn đang ở tenant/subscription đúng (`az account show`)
</details>

<details>
<summary><strong>Lỗi: "Điểm cuối không hợp lệ" / lỗi kết nối</strong></summary>

- Đảm bảo `AZURE_OPENAI_ENDPOINT` là URL cơ sở đầy đủ (ví dụ `https://your-resource.openai.azure.com/`)
- Kiểm tra nhất quán dấu gạch chéo cuối
- Xác nhận điểm cuối trùng với tài nguyên bạn đã tạo (`azd env get-values`)
</details>

<details>
<summary><strong>Lỗi: "Không tìm thấy triển khai"</strong></summary>

- Xác nhận `AZURE_OPENAI_DEPLOYMENT` khớp tên triển khai trong Azure
- Kiểm tra mô hình được triển khai và đang hoạt động
- Tên triển khai mặc định là `gpt-5.6-luna`
</details>

<details>
<summary><strong>Lỗi: 429 / vượt quá giới hạn tần suất</strong></summary>

- Triển khai GPT-5.6 Luna mặc định có công suất Global Standard 10: 10 yêu cầu/phút và 10,000 token/phút
- Chạy các ví dụ tuần tự và chờ khoảng thời gian thử lại của dịch vụ trước khi thử lại
- Ví dụ cơ bản này tắt tính năng thử lại SDK tự động, nên yêu cầu thất bại sẽ báo ngay
</details>

<details>
<summary><strong>VS Code: Biến môi trường không tải</strong></summary>

- Đảm bảo file `.env` nằm trong thư mục gốc dự án (cùng cấp với `pom.xml`)
- Thử chạy `mvn spring-boot:run` trong terminal tích hợp của VS Code
- Kiểm tra cài đặt đúng phần mở rộng Java cho VS Code
</details>

### Chế Độ Gỡ Lỗi

Để bật ghi log chi tiết, bỏ chú thích các dòng sau trong [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Bước Tiếp Theo

**Thiết Lập Hoàn Thành!** Tiếp tục hành trình học của bạn:

[Chương 3: Kỹ Thuật AI Sinh Tạo Cốt Lõi](../../../03-CoreGenerativeAITechniques/README.md)

## Tài Nguyên

- [Chuyển sang Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK chính thức với Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Xác thực không cần khóa với Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Cổng Azure AI Foundry](https://ai.azure.com/)
- [Tài liệu Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ gốc nên được coi là nguồn tin chính thức. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->