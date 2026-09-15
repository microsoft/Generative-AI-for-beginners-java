# Hướng Dẫn Các Kỹ Thuật AI Sinh Tạo Cốt Lõi

## Mục Lục

- [Yêu Cầu Trước](#yêu-cầu-trước)
- [Bắt Đầu](#bắt-đầu)
- [Hướng Dẫn Chọn Mô Hình](#hướng-dẫn-chọn-mô-hình)
- [Hướng Dẫn 1: Hoàn Thành và Chat LLM](#hướng-dẫn-1-hoàn-thành-và-chat-llm)
- [Hướng Dẫn 2: Gọi Hàm](#hướng-dẫn-2-gọi-hàm)
- [Hướng Dẫn 3: RAG (Tăng Cường Sinh Tạo Qua Truy Xuất)](#hướng-dẫn-3-rag-tăng-cường-sinh-tạo-qua-truy-xuất)
- [Hướng Dẫn 4: AI Có Trách Nhiệm](#hướng-dẫn-4-ai-có-trách-nhiệm)
- [Các Mẫu Thông Dụng Qua Các Ví Dụ](#các-mẫu-thông-dụng-qua-các-ví-dụ)
- [Kiểm Tra Đơn Vị](#kiểm-tra-đơn-vị)
- [Xác Minh Trực Tiếp Theo Thứ Tự](#xác-minh-trực-tiếp-theo-thứ-tự)
- [Khắc Phục Sự Cố](#xử-lý-sự-cố)
- [Bước Tiếp Theo](#bước-tiếp-theo)

## Tổng Quan

Bốn chương trình Java độc lập trình bày trò chuyện, lịch sử hội thoại, gọi hàm, sinh tạo tăng cường truy xuất toàn văn bản (RAG), và xử lý phản hồi AI có trách nhiệm. Tất cả yêu cầu chat mặc định đều hướng tới **GPT-5.6 Luna với nỗ lực lý luận `none`**.

Những ví dụ này sử dụng SDK Java chính thức của OpenAI với điểm cuối Azure OpenAI v1, theo [hướng dẫn SDK của Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Gói cũ `azure-ai-openai` không còn là phụ thuộc. Chat Completions được giữ lại để dạy các luồng công việc dựa trên tin nhắn hiện tại; xem [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) để biết các lựa chọn API khác.

## Yêu Cầu Trước

- Java 21 trở lên và Maven 3.6.3 trở lên.
- Một triển khai chat Azure OpenAI tên `gpt-5.6-luna`, hoặc ghi đè với các thiết lập Chat Completions tương thích.
- Một định danh Azure đã đăng nhập với vai trò **Cognitive Services OpenAI User** trên tài nguyên. Phát triển cục bộ dùng đăng nhập Azure CLI của bạn; ứng dụng được lưu trữ có thể sử dụng định danh được quản lý.
- Xem [Chương 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) để hướng dẫn thiết lập tài nguyên và đăng nhập.

[Cấu hình Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) cố định các phiên bản này, kiểm tra ngày 14-09-2026:

| Thành phần | Phiên bản | Mục đích |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Khách hàng chính thức tương thích Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Xác thực không cần khóa và làm mới token |
| `net.objecthunter:exp4j` | 0.4.8 | Phân tích biểu thức số học mà không thực thi mã |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Kiểm tra đơn vị ngoại tuyến Jupiter |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Biên dịch Java 21, kiểm tra, ví dụ chạy được |

Trình biên dịch dùng `--release 21`. Không cần phụ thuộc Spring Boot, Spring AI, hay LangChain4j cho các ví dụ độc lập này.

## Bắt Đầu

Từ thư mục gốc của kho lưu trữ, thiết lập điểm cuối tài nguyên và ghi đè triển khai tùy chọn trong shell của bạn.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Các kiểm tra không cần thông tin đăng nhập Azure hay điểm cuối. Maven không tự động đọc tệp môi trường; đặt biến trong shell dùng để chạy ví dụ trực tiếp. Với khởi chạy IDE, xác minh môi trường được cung cấp bởi cấu hình khởi chạy của bạn.

## Hướng Dẫn Chọn Mô Hình

| Biến môi trường | Ý nghĩa | Mặc định |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Gốc tài nguyên Azure HTTPS hoặc URL `/openai/v1` đã chuẩn hóa | Bắt buộc cho chạy trực tiếp |
| `AZURE_OPENAI_DEPLOYMENT` | Tên triển khai chat, không phải phiên bản mô hình | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Cấu hình triển khai embedding riêng biệt, không dùng bởi bốn chương trình này | `text-embedding-3-small` |

Ghi đè triển khai trống dùng mặc định. Cấu hình thêm vào `/openai/v1` chính xác một lần và từ chối thông tin xác thực, chuỗi truy vấn và đường dẫn triển khai cũ trong điểm cuối.

Mỗi yêu cầu chat đặt rõ `reasoningEffort(ReasoningEffort.NONE)` và `maxCompletionTokens(...)`. Không yêu cầu nào đặt `temperature`, `top_p`, hoặc tùy chọn token hoàn thành cũ. Bao gồm lựa chọn công cụ và theo dõi kết quả công cụ. Công cụ Chat Completions GPT-5.6 yêu cầu nỗ lực lý luận `none`; xem [hướng dẫn chat của Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Chương này không có điểm nhập streaming hay embedding.** Người dùng lấy toàn bộ tài liệu, không phải vectơ. Nếu mở rộng với embedding, dùng triển khai embedding riêng như `text-embedding-3-small`, không dùng Luna.

## Hướng Dẫn 1: Hoàn Thành và Chat LLM

Nguồn: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Chương trình chạy giải thích Java streams đơn giản, hội thoại hai lượt HashMap/TreeMap, và chat tương tác. Lượt thứ hai gồm phản hồi trợ lý đầu tiên; mỗi lượt tương tác gửi kèm toàn bộ hội thoại trước đó.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` cung cấp triển khai và cấu hình nỗ lực lý luận rõ ràng. Chat tương tác bỏ qua dòng trống, kết thúc khi `exit` hoặc EOF, giữ lại tin nhắn hệ thống cộng thêm chín lượt người dùng/trợ lý đã hoàn thành. Giới hạn số lượt là giới hạn giáo dục, không phải bảo đảm ngân sách token chính xác.

Từ thư mục ví dụ:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Mong ba câu trả lời ban đầu, sau đó xuất hiện lời nhắc `You:`. Mỗi câu hỏi tương tác không trống tạo một yêu cầu. Giới hạn hoàn thành lần lượt là 200, 300, 400, rồi 500 token cho mỗi lượt tương tác.

## Hướng Dẫn 2: Gọi Hàm

Nguồn: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK tạo schema JSON từ các bản ghi có chú thích `WeatherArguments` và `CalculationArguments`. Việc gọi công cụ bắt buộc buộc mỗi ví dụ sử dụng giao thức công cụ thay vì chấp nhận câu trả lời không trợ giúp từ mô hình.

1. Gửi câu hỏi với công cụ cho phép, nỗ lực lý luận `none`, và giới hạn hoàn thành 300 token.
2. Yêu cầu lý do kết thúc `tool_calls`, xác thực tên hàm và ID cuộc gọi, phân tích đối số JSON có kiểu.
3. Thực thi hàm cục bộ. Mô hình không thực thi Java hay mã tùy ý.
4. Thêm một lần tin nhắn gọi công cụ trợ lý, sau đó từng kết quả cùng ID `tool_call_id` tương ứng.
5. Gửi một yêu cầu cuối 300 token không dùng công cụ và yêu cầu trả lời hoàn chỉnh, không rỗng.

`get_weather` trả về thời tiết **mô phỏng**, không phải thực tế. Nó tôn trọng thành phố và chuyển đổi mẫu 22 độ C sang Fahrenheit khi được yêu cầu. `calculate` đánh giá biểu thức qua exp4j, hỗ trợ dạng như `15% of 240` và `2 + 3 * 4`, và từ chối các phép tính trống, quá lớn, không hợp lệ hoặc không hữu hạn. Nó dùng số thực dấu phẩy động, không phải số thập phân tài chính.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Mong chờ `Function: get_weather`, thời tiết Seattle mô phỏng, `Function: calculate`, `Function result: 36`, và hai câu trả lời cuối. Không yêu cầu stdin hay thông tin đăng nhập thời tiết bên ngoài. Một lần chạy thành công dùng đúng bốn yêu cầu chat.

## Hướng Dẫn 3: RAG (Tăng Cường Sinh Tạo Qua Truy Xuất)

Nguồn: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Đầu vào: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Ví dụ RAG giới thiệu này truy xuất một tài liệu UTF-8 nguyên vẹn và chèn nó vào tin nhắn người dùng cùng câu hỏi. Tin nhắn hệ thống riêng hướng dẫn mô hình xem nội dung tài liệu như dữ liệu không đáng tin cậy và chỉ trả lời dựa trên ngữ cảnh đó. Nếu tài liệu không chứa câu trả lời, câu trả lời yêu cầu là: `Tôi không thể tìm thấy thông tin đó trong tài liệu được cung cấp.`

Theo ngữ cảnh có thể giảm ảo tưởng, nhưng không có dấu phân cách hay hướng dẫn hệ thống nào đảm bảo độ chính xác hoặc ngăn mọi tiêm prompt. Xem lại câu trả lời trực tiếp. RAG sản xuất thường bổ sung phân đoạn, truy xuất, trích dẫn, kiểm soát truy cập, và đánh giá.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Nhập một câu hỏi, ví dụ `Phương pháp xác thực nào được mô tả trong tài liệu?`. Mong chờ câu trả lời nhắc đến Microsoft Entra ID. Chương trình thoát sau một yêu cầu chat với giới hạn hoàn thành 500 token.

Tìm tệp mặc định hoạt động từ thư mục gốc kho lưu trữ, thư mục chương, hoặc thư mục ví dụ. Cũng hỗ trợ đường dẫn rõ ràng:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Đầu vào phải không trống: tối đa 32 KiB dữ liệu tài liệu UTF-8 và 2,000 ký tự câu hỏi. Thiếu tệp, câu hỏi trống/EOF, và đầu vào quá lớn đều thất bại trước suy luận.

## Hướng Dẫn 4: AI Có Trách Nhiệm

Nguồn: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Sáu phép thử bao gồm hướng dẫn có hại, ngôn ngữ thù địch, quyền riêng tư, thông tin sai y tế, nội dung bất hợp pháp, và câu hỏi AI có trách nhiệm thân thiện. Chương trình quan sát phản hồi thay vì giả định mọi phép thử đều kích hoạt bộ lọc.

| Kết quả | Bằng chứng |
| --- | --- |
| `FILTERED` | Mã lỗi rõ ràng `content_filter` / `ResponsibleAIPolicyViolation`, hoặc lý do hoàn thành `content_filter` |
| `REFUSED` | Trường cấu trúc `message.refusal` không trống |
| `POSSIBLE_REFUSAL` | Cụm từ từ chối mở đầu trong văn bản thường; là quy tắc heuristics cần xem xét |
| `GENERATED` | Phản hồi hoàn chỉnh không rỗng; không chứng minh nội dung an toàn |

HTTP 400 thông thường **không phải** bằng chứng của việc lọc. Tham số không hợp lệ, xác thực thất bại, giới hạn tốc độ, lỗi máy chủ, phản hồi sai định dạng, và kết quả đầu ra bị cắt đều làm thất bại quy trình thay vì tạo thành thành công an toàn sai lệch. Từ ngữ chung như "nội dung có hại" trong lời giải thích thân thiện không tính là từ chối.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Mong sáu kết quả phân loại và một bảng tóm tắt nói rằng quan sát không phải giấy chứng nhận an toàn. Mỗi phép thử giới hạn hoàn thành 300 token. Xem lại các kết quả sinh bất ngờ và các từ chối khả nghi bằng tay; so sánh thân thiện phải tạo ra giải thích AI có trách nhiệm thuyết phục. Không yêu cầu stdin.

## Các Mẫu Thông Dụng Qua Các Ví Dụ

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) tập trung chuẩn hóa điểm cuối, ghi đè triển khai, xác thực không khoá, và tùy chọn chat:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Bộ cung cấp token làm mới token truy cập khi cần. Không ghi nhật ký token hoặc thay thế bằng khóa API. Mỗi chương trình tái sử dụng khách hàng và đóng nó trong `finally` hoặc qua lớp bao `AutoCloseable` riêng; `OpenAIClient` của SDK không phải `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) yêu cầu câu trả lời hoàn chỉnh, không rỗng. Các lựa chọn trống, từ chối, lọc, và câu trả lời cắt không in ra thành công lặng lẽ. Ví dụ AI có trách nhiệm xử lý rõ ràng kết quả lọc/từ chối mong đợi. Thất bại không xử lý cho mã thoát không bằng 0 cho tiến trình Java/Maven.

**Tự động thử lại SDK bị vô hiệu hóa** để giữ số lượng yêu cầu dự đoán được trên các triển khai chia sẻ có RPM thấp. Mỗi yêu cầu suy luận có giới hạn thời gian 60 giây. Lấy token có thể mất thêm thời gian. Lập lịch cấp ứng dụng phải tôn trọng hạn mức; không tái chạy mù quáng yêu cầu trả phí thất bại.

## Kiểm Tra Đơn Vị

Từ thư mục ví dụ:

```powershell
mvn -B -ntp clean test
```

Giao thức kiểm tra thay thế hoàn toàn lớp HTTP SDK, bắt các thân yêu cầu được tuần tự hóa thực tế, và cung cấp các phản hồi trong hàng đợi. Nó không mở socket, không lấy token Azure, và thất bại với yêu cầu không mong đợi. Những kiểm tra này xác nhận hành vi ứng dụng và giao thức SDK, không phải chất lượng mô hình trực tiếp hay khả dụng triển khai.

| Bộ kiểm tra | Phạm vi |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Chuẩn hóa/từ chối điểm cuối, ghi đè triển khai, lựa chọn lý luận và token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Mọi luồng hoàn thành, lịch sử tin nhắn, cắt lượt hoàn chỉnh, EOF, thất bại |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schema công cụ, đối số kiểu, số học, ID, nhiều kết quả công cụ, theo dõi thất bại |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Tìm tệp, UTF-8, giới hạn kích thước, gói ngữ cảnh, lỗi đầu vào và API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Sáu phép thử, bộ lọc rõ ràng, phân loại từ chối, lỗi 400 và các lỗi khác thường |

Với một bộ, dùng `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Các tiện ích chung sống trong [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Xác Minh Trực Tiếp Theo Thứ Tự

Các cuộc gọi trực tiếp tách biệt với kiểm tra đơn vị. Dùng lệnh dưới đây **đơn lẻ**, từ thư mục gốc kho lưu trữ, chỉ khi thông tin đăng nhập và quyền truy cập triển khai đã sẵn sàng. Không cần dịch vụ hay tiến trình lâu dài.

Với triển khai **10 yêu cầu/phút** chia sẻ, hãy dự trữ đủ hạn mức cho toàn bộ chương trình tiếp theo trước khi khởi chạy: 5, 4, 1, rồi 6 yêu cầu. Các tiến trình tuần tự riêng không đảm bảo tuân thủ giới hạn tốc độ. Điều phối phút chạy chung với mọi người gọi khác; không dán bốn lệnh gọi như lô không điều phối.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Hoàn Thành, đa lượt, và hai lượt tương tác:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Kiểm tra tất cả ba tiêu đề phần, năm câu trả lời, một câu trả lời tương tác cuối cùng nhắc về Ada, `Tạm biệt!`, và mã thoát 0. Ngân sách: **5 yêu cầu, tối đa 1.900 token kết quả**. Để chạy nhỏ hơn, chỉ truyền `exit`: 3 yêu cầu / 900 token, nhưng điều đó không thực hiện suy luận tương tác.

**2. Cả hai quy trình gọi hàm:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Kiểm tra cả hai tên hàm, thời tiết giả lập ở Seattle, kết quả tính toán 36, hai câu trả lời cuối cùng và mã thoát 0. Ngân sách: **4 yêu cầu, tối đa 1.200 token kết quả**.

**3. Câu trả lời dựa trên tài liệu:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Kiểm tra đường dẫn tài liệu, một câu trả lời đề cập đến Microsoft Entra ID, và mã thoát 0. Ngân sách: **1 yêu cầu, tối đa 500 token kết quả**. Tệp [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) hiện có là tệp đầu vào duy nhất bắt buộc. Một lần chạy tùy chọn hỏi về chủ đề không có sẽ không trả lời và thêm một yêu cầu / 500 token.

**4. Các quan sát về AI có trách nhiệm:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Kiểm tra sáu danh mục và tóm tắt quan sát, xem xét nội dung được tạo, và yêu cầu mã thoát 0 để hoàn tất kỹ thuật. Việc kết thúc quy trình thành công không chứng nhận độ an toàn của mô hình. Ngân sách: **6 yêu cầu, tối đa 1.800 token kết quả**.

**Tổng số cho bốn lệnh: 16 yêu cầu chat và tối đa 5.400 token kết quả**, cộng với token đầu vào (bao gồm cuộc trò chuyện lặp lại và lịch sử/sơ đồ công cụ). Không có yêu cầu embedding. Mức sử dụng token thực tế phụ thuộc vào mô hình và có thể thấp hơn, đặc biệt với lời nhắc được lọc. Chi phí đô la phụ thuộc vào giá triển khai; không có ước tính tiền tệ cố định nào được ngụ ý. Tất cả giới hạn yêu cầu giả định không chạy lại thủ công. Kiểm tra `$LASTEXITCODE` ngay sau mỗi lệnh; khác 0 nghĩa là lần chạy không hoàn thành thành công.

## Xử lý sự cố

- **Thiếu endpoint / 401 / 403:** Đặt endpoint trong quy trình khởi chạy, xác minh đăng nhập Azure địa phương và vai trò phạm vi tài nguyên, và kiểm tra các ghi đè môi trường danh tính không mong muốn.
- **400 / 404:** Xác nhận rằng triển khai tồn tại và hỗ trợ Chat Completions với mức suy luận `none`. Dùng URL gốc HTTPS hoặc `/openai/v1`, không dùng URL triển khai cũ. Lỗi 400 thông thường là lỗi kỹ thuật, không phải là chặn an toàn.
- **429:** Điều phối RPM và hạn mức token dùng chung trước khi thử lại. Các ví dụ không tự động thử lại.
- **`Incomplete chat response: length`:** Đầu ra đã chạm giới hạn kết quả. Xem lại phản hồi và lời nhắc trước khi tăng giới hạn và ngân sách đã ghi; không ghi nhận lần chạy bị cắt ngắn là thành công.
- **Lỗi tệp hoặc stdin:** Khởi chạy từ thư mục được hỗ trợ hoặc truyền đường dẫn tài liệu rõ ràng. Cung cấp câu hỏi độc giả không để trống. Hoàn thành có thể kết thúc bình thường khi EOF hoặc `exit`.
- **Lỗi biên dịch:** Xác minh Java 21 hoặc mới hơn, rồi chạy `mvn -B -ntp clean test`. Trong PowerShell, đặt dấu ngoặc cho toàn bộ tham số Maven chứa thuộc tính có dấu chấm, ví dụ `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Bước tiếp theo

Tiếp tục với [Chương 4: Ví dụ Thực tế](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố miễn trừ trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc sai sót. Tài liệu gốc bằng ngôn ngữ gốc nên được coi là nguồn tin chính thức. Đối với thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ hiểu lầm hoặc giải thích sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->