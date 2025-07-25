<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:52:05+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "vi"
}
-->
# Ứng Dụng Thực Tiễn & Dự Án

## Những Gì Bạn Sẽ Học
Trong phần này, chúng ta sẽ trình bày ba ứng dụng thực tiễn minh họa các mẫu phát triển AI sinh với Java:
- Tạo một Trình Tạo Câu Chuyện Về Thú Cưng đa phương thức kết hợp AI phía client và server
- Triển khai tích hợp mô hình AI cục bộ với demo Foundry Local Spring Boot
- Phát triển dịch vụ Model Context Protocol (MCP) với ví dụ Máy Tính

## Mục Lục

- [Giới Thiệu](../../../04-PracticalSamples)
  - [Demo Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Trình Tạo Câu Chuyện Về Thú Cưng](../../../04-PracticalSamples)
  - [Dịch Vụ MCP Máy Tính (Demo MCP Dành Cho Người Mới Bắt Đầu)](../../../04-PracticalSamples)
- [Tiến Trình Học Tập](../../../04-PracticalSamples)
- [Tóm Tắt](../../../04-PracticalSamples)
- [Bước Tiếp Theo](../../../04-PracticalSamples)

## Giới Thiệu

Chương này giới thiệu các **dự án mẫu** minh họa các mẫu phát triển AI sinh với Java. Mỗi dự án đều hoạt động đầy đủ và trình bày các công nghệ AI cụ thể, các mẫu kiến trúc, và các thực hành tốt nhất mà bạn có thể áp dụng cho các ứng dụng của riêng mình.

### Demo Foundry Local Spring Boot

**[Demo Foundry Local Spring Boot](foundrylocal/README.md)** minh họa cách tích hợp với các mô hình AI cục bộ bằng cách sử dụng **OpenAI Java SDK**. Nó trình bày cách kết nối với mô hình **Phi-3.5-mini** chạy trên Foundry Local, cho phép bạn chạy các ứng dụng AI mà không cần phụ thuộc vào các dịch vụ đám mây.

### Trình Tạo Câu Chuyện Về Thú Cưng

**[Trình Tạo Câu Chuyện Về Thú Cưng](petstory/README.md)** là một ứng dụng web Spring Boot thú vị minh họa **xử lý AI đa phương thức** để tạo ra các câu chuyện sáng tạo về thú cưng. Nó kết hợp khả năng AI phía client và server bằng cách sử dụng transformer.js cho các tương tác AI trên trình duyệt và OpenAI SDK cho xử lý phía server.

### Dịch Vụ MCP Máy Tính (Demo MCP Dành Cho Người Mới Bắt Đầu)

**[Dịch Vụ MCP Máy Tính](mcp/calculator/README.md)** là một minh họa đơn giản về **Model Context Protocol (MCP)** sử dụng Spring AI. Nó cung cấp một giới thiệu dễ hiểu về các khái niệm MCP, cho thấy cách tạo một MCP Server cơ bản tương tác với các MCP client.

## Tiến Trình Học Tập

Các dự án này được thiết kế để xây dựng dựa trên các khái niệm từ các chương trước:

1. **Bắt Đầu Đơn Giản**: Bắt đầu với Demo Foundry Local Spring Boot để hiểu tích hợp AI cơ bản với các mô hình cục bộ
2. **Thêm Tính Tương Tác**: Tiến tới Trình Tạo Câu Chuyện Về Thú Cưng để khám phá AI đa phương thức và các tương tác trên web
3. **Học Cơ Bản Về MCP**: Thử Dịch Vụ MCP Máy Tính để hiểu các nguyên tắc cơ bản của Model Context Protocol

## Tóm Tắt

**Chúc mừng bạn!** Bạn đã hoàn thành:

- **Tạo trải nghiệm AI đa phương thức** kết hợp xử lý AI phía client và server
- **Triển khai tích hợp mô hình AI cục bộ** bằng cách sử dụng các framework và SDK Java hiện đại
- **Phát triển các dịch vụ Model Context Protocol** minh họa các mẫu tích hợp công cụ

## Bước Tiếp Theo

[Chương 5: AI Sinh Có Trách Nhiệm](../05-ResponsibleGenAI/README.md)

**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được coi là nguồn thông tin chính thức. Đối với các thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.