# Thiết Lập Môi Trường Phát Triển cho Azure AI Foundry

> Hướng dẫn này thiết lập các mô hình **Azure AI Foundry** cho các ứng dụng AI Java trong khóa học này, sử dụng xác thực **không cần khóa** (Microsoft Entra ID) — không cần quản lý khóa API. Mới làm quen với công cụ? Bắt đầu với [hướng dẫn môi trường phát triển](./README.md).

Hướng dẫn này thiết lập các mô hình **Azure AI Foundry** cho các ứng dụng AI Java trong khóa học này. Bạn có hai lựa chọn:

- **Lựa chọn A — Cấp phát với `azd` + Bicep (khuyến nghị):** một lệnh triển khai tài khoản Foundry và các mô hình dưới dạng mã. Không cần click portal.
- **Lựa chọn B — Tạo tài nguyên thủ công** trong portal Azure AI Foundry.

Cả hai đường đi đều sử dụng **xác thực không cần khóa** (Microsoft Entra ID) — không có khóa API cần sao chép hoặc rò rỉ.

## Mục Lục

- [Những Gì Được Tạo](#những-gì-được-tạo)
- [Yêu cầu trước](#yêu-cầu-trước)
- [Lựa chọn A: Cấp phát với azd + Bicep (Khuyến nghị)](#option-a-provision-with-azd--bicep-recommended)
- [Lựa chọn B: Tạo tài nguyên thủ công](#lựa-chọn-b-tạo-tài-nguyên-thủ-công)
- [Cấu hình Môi trường của Bạn](#cấu-hình-môi-trường-của-bạn)
- [Kiểm Tra Thiết Lập](#kiểm-tra-thiết-lập)
- [Tiếp Theo Là Gì?](#tiếp-theo-là-gì)
- [Tài nguyên](#tài-nguyên)
- [Tài nguyên Bổ sung](#tài-nguyên-bổ-sung)

## Những Gì Được Tạo

Các mẫu Bicep trong [`infra/`](../../../02-SetupDevEnvironment/infra) cấp phát:

- Một tài khoản **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, loại `AIServices`) với một dự án
- Một triển khai **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), phiên bản `2026-07-09`, với công suất `GlobalStandard` `10` (10 yêu cầu/phút và 10,000 token/phút cho mô hình này)
- Một triển khai **embedding** - `text-embedding-3-small`, phiên bản `1` (được sử dụng trong các chương sau)
- Một **gán vai trò không cần khóa** (`Cognitive Services OpenAI User`) để bạn đăng nhập bằng `az login` thay vì quản lý khóa

## Yêu Cầu Trước

- Một [đăng ký Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) và [Maven 3.9+](https://maven.apache.org/download.cgi)

## Lựa chọn A: Cấp phát với azd + Bicep (Khuyến nghị)

Từ thư mục `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Đăng nhập (cả hai công cụ)
azd auth login
az login

# Cung cấp tài khoản Foundry + triển khai mô hình
azd up
```

`azd` sẽ hỏi bạn tên **môi trường** (ví dụ `genai-java`), **đăng ký**, và **vùng**. Chọn đăng ký của bạn và một vùng có `gpt-5.6-luna` và `text-embedding-3-small` khả dụng, ví dụ `eastus2`. Xác nhận đăng ký có đủ hạn mức cho mô hình và loại triển khai tại vùng đó; tính khả dụng và hạn mức thay đổi theo đăng ký.

Khi cấp phát xong, azd:

1. Triển khai mọi thứ được định nghĩa trong [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Chạy hook sau cấp phát ghi [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) với tên điểm cuối và tên triển khai của bạn (không có bí mật).

> **Mẹo:** Chạy lại `azd up` bất cứ lúc nào để áp dụng thay đổi. Chạy `azd down` để xóa mọi thứ và ngừng phát sinh chi phí.

Để xem các thiết lập đã tạo:

```bash
azd env get-values
```

Bây giờ chuyển đến [Kiểm Tra Thiết Lập](#kiểm-tra-thiết-lập).

## Lựa chọn B: Tạo Tài Nguyên Thủ Công

Thích dùng portal? Tạo tài nguyên thủ công:

1. Đến [portal Azure AI Foundry](https://ai.azure.com/) và đăng nhập.
2. **Tạo một dự án** (điều này cũng tạo một tài nguyên AI Foundry). Đặt tên như `GenAIJava`.
3. Trong dự án của bạn, mở **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Triển khai **GPT-5.6 Luna** (tên mô hình và triển khai `gpt-5.6-luna`, phiên bản `2026-07-09`) với công suất **Global Standard** `10`. Lặp lại với **text-embedding-3-small**, phiên bản `1`, nếu bạn muốn ví dụ embedding.
5. Từ **Overview**, sao chép **điểm cuối** (ví dụ `https://<resource>.openai.azure.com/`).
6. Cấp quyền truy cập không cần khóa cho bạn: tại tài nguyên, mở **Access control (IAM)** → **Add role assignment** → gán vai trò **Cognitive Services OpenAI User** cho tài khoản của bạn.

> **Vẫn gặp khó khăn?** Xem tài liệu [Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Cấu Hình Môi Trường Của Bạn

**Nếu bạn dùng Lựa chọn A (`azd up`)**, file thiết lập của bạn đã được tạo sẵn — không cần cấu hình gì. Chuyển tiếp đến [Kiểm Tra Thiết Lập](#kiểm-tra-thiết-lập).

**Nếu bạn dùng Lựa chọn B (thủ công)**, tự tạo file `.env` cho ví dụ:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Chỉnh sửa `.env` với điểm cuối của bạn (không cần khóa — xác thực không cần khóa):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Sử dụng điểm cuối Azure OpenAI của tài nguyên, không phải URL dự án. Ứng dụng basic-chat sẽ chuyển đổi nó thành `/openai/v1` và cấu hình client dùng bearer-token rõ ràng; không cần khóa API.

> **Lưu ý bảo mật:** Không có khóa API để lưu. Bạn xác thực bằng Microsoft Entra ID qua `az login` (cục bộ) hoặc managed identity (trên Azure). File `.env` chỉ chứa các thiết lập không bí mật và đã được thêm vào `.gitignore`.

## Kiểm Tra Thiết Lập

Đảm bảo bạn đã đăng nhập để xác thực không cần khóa có thể lấy token, rồi chạy ví dụ:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # nếu bạn chưa đăng nhập
mvn clean spring-boot:run
```

Bạn sẽ thấy phản hồi từ mô hình `gpt-5.6-luna`. Chạy các ví dụ theo thứ tự để giữ trong hạn mức mặc định nhỏ; nếu nhận HTTP 429, đợi khoảng thời gian thử lại trước khi thử lại.

> **Người dùng VS Code:** Nhấn `F5` để chạy. Ứng dụng tự động tải `.env` của bạn.

> **Ví dụ đầy đủ:** Xem [Ví dụ Basic Chat với Azure AI Foundry](./examples/basic-chat-azure/README.md) để biết chi tiết và khắc phục sự cố.

## Tiếp Theo Là Gì?

Sau khi cấp phát và chạy thành công ví dụ, bạn sẽ có:
- Azure AI Foundry với `gpt-5.6-luna` và `text-embedding-3-small` đã triển khai
- Xác thực không cần khóa (Microsoft Entra ID) — không có khóa cần quản lý
- Một file `.env` cục bộ với điểm cuối và tên triển khai của bạn
- Môi trường phát triển Java sẵn sàng để sử dụng

**Tiếp tục tới** [Chương 3: Kỹ Thuật AI Tạo Sinh Cốt Lõi](../03-CoreGenerativeAITechniques/README.md) để bắt đầu xây dựng ứng dụng AI!

## Tài nguyên

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Xác thực không cần khóa với Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Tài liệu Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Chuyển đổi Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java OpenAI chính thức với Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Tài nguyên Bổ sung

- [Tải VS Code](https://code.visualstudio.com/Download)
- [Tải Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Cấu hình Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ gốc nên được coi là nguồn tin chính thức. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->