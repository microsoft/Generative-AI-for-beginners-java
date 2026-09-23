# 핵심 생성형 AI 기술 튜토리얼

## 목차

- [전제 조건](#전제-조건)
- [시작하기](#시작하기)
- [모델 선택 가이드](#모델-선택-가이드)
- [튜토리얼 1: LLM 완성 및 채팅](#튜토리얼-1-llm-완성-및-채팅)
- [튜토리얼 2: 함수 호출](#튜토리얼-2-함수-호출)
- [튜토리얼 3: RAG (검색 강화 생성)](#튜토리얼-3-rag-검색-강화-생성)
- [튜토리얼 4: 책임 있는 AI](#튜토리얼-4-책임-있는-ai)
- [예제 전반의 공통 패턴](#예제-전반의-공통-패턴)
- [단위 테스트](#단위-테스트)
- [순차적 실시간 검증](#순차적-실시간-검증)
- [문제 해결](#문제-해결)
- [다음 단계](#다음-단계)

## 개요

네 개의 독립적인 자바 프로그램이 채팅, 대화 기록, 함수 호출, 전체 문서 검색 강화 생성(RAG), 책임 있는 AI 응답 처리를 시연합니다. 모든 채팅 요청은 기본적으로 **추론 노력 `none`인 GPT-5.6 Luna**를 대상으로 합니다.

이 예제들은 [Microsoft의 SDK 지침](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages)에 따라 공식 OpenAI 자바 SDK와 Azure OpenAI v1 엔드포인트를 사용합니다. 이전의 `azure-ai-openai` 패키지는 더 이상 의존성이 아닙니다. 채팅 완성(Completions)은 기존 메시지 기반 워크플로우를 가르치기 위해 유지됩니다; 다른 API 옵션은 [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure)를 참조하세요.

## 전제 조건

- 자바 21 이상과 Maven 3.6.3 이상.
- `gpt-5.6-luna`라는 Azure OpenAI 채팅 배포 또는 호환 가능한 Chat Completions 설정의 오버라이드.
- 자원에 대해 **Cognitive Services OpenAI User** 역할이 할당된 로그인된 Azure ID. 로컬 개발은 Azure CLI 로그인 사용; 호스팅된 애플리케이션은 관리 ID 사용 가능.
- 자원 설정 및 로그인 지침은 [2장](../02-SetupDevEnvironment/getting-started-azure-openai.md) 참조.

[Maven 구성](../../../03-CoreGenerativeAITechniques/examples/pom.xml)은 2026-09-14에 확인한 다음 버전을 사용합니다:

| 구성 요소 | 버전 | 용도 |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | 공식 Azure v1 호환 클라이언트 |
| `com.azure:azure-identity` | 1.18.6 | 키 없는 인증 및 토큰 갱신 |
| `net.objecthunter:exp4j` | 0.4.8 | 코드 평가 없이 산술식 파싱 |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | 오프라인 Jupiter 단위 테스트 |
| Maven 컴파일러 / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | 자바 21 컴파일, 테스트, 실행 가능한 예제 |

컴파일러는 `--release 21`을 사용합니다. 이 독립 예제들은 Spring Boot, Spring AI, 또는 LangChain4j 의존성이 필요 없습니다.

## 시작하기

저장소 루트에서 리소스 엔드포인트와 선택적 배포 오버라이드를 셸에 설정하세요.

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

테스트는 Azure 자격 증명이나 엔드포인트를 필요로 하지 않습니다. Maven은 환경 파일을 자동으로 읽지 않으므로, 라이브 예제를 실행하는 셸에 변수를 설정하세요. IDE 실행 시 실행 구성의 환경 변수를 확인하세요.

## 모델 선택 가이드

| 환경 변수 | 의미 | 기본값 |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure 리소스 루트 또는 이미 정규화된 `/openai/v1` URL | 라이브 실행 시 필수 |
| `AZURE_OPENAI_DEPLOYMENT` | 채팅 배포 이름, 모델 버전 아님 | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | 별도의 임베딩 배포 설정, 이 네 프로그램에서는 미사용 | `text-embedding-3-small` |

빈 배포 오버라이드는 기본값을 사용합니다. 구성은 `/openai/v1`를 정확히 한 번 추가하며, 엔드포인트 내 자격 증명, 쿼리 문자열, 이전 배포 경로는 거부합니다.

모든 채팅 요청은 명시적으로 `reasoningEffort(ReasoningEffort.NONE)`과 `maxCompletionTokens(...)`를 설정합니다. 어떤 요청도 `temperature`, `top_p` 또는 이전의 완성 토큰 옵션을 설정하지 않습니다. 도구 선택과 도구 결과 후속 처리도 포함됩니다. GPT-5.6 채팅 완성 함수 도구는 추론 노력이 `none`이어야 합니다; [Microsoft 채팅 지침](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt) 참조.

**이 장에는 스트리밍이나 임베딩 진입점이 없습니다.** 문서 전체를 가져오며, 벡터가 아닙니다. 임베딩을 확장하려면 `text-embedding-3-small`과 같은 별도 임베딩 배포를 사용하고, Luna는 사용하지 마세요.

## 튜토리얼 1: LLM 완성 및 채팅

소스: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

이 프로그램은 간단한 자바 스트림 설명, 두 턴의 HashMap/TreeMap 대화, 그리고 인터랙티브 채팅을 실행합니다. 두 번째 턴에는 첫 번째 어시스턴트 응답이 포함되며, 각 인터랙티브 턴은 이전 대화도 함께 전송합니다.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)`는 배포 및 명시적 추론 설정을 제공합니다. 인터랙티브 채팅은 빈 줄을 건너뛰고 `exit` 또는 EOF에서 종료하며, 시스템 메시지와 완료된 사용자/어시스턴트 9턴을 유지합니다. 턴 수 제한은 교육용 제한으로 정확한 토큰 예산 보장이 아닙니다.

예제 디렉터리에서:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

초기 세 개 답변이 예상되며, 그 다음 `You:` 프롬프트가 나옵니다. 각 비빈 인터랙티브 질문은 요청 하나를 추가합니다. 완성 토큰 제한은 인터랙티브 턴마다 차례로 200, 300, 400, 500 토큰입니다.

## 튜토리얼 2: 함수 호출

소스: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK는 주석이 붙은 `WeatherArguments`와 `CalculationArguments` 레코드에서 JSON 스키마를 유도합니다. 필수 도구 선택은 각 예제가 모델의 자체 답변이 아닌 도구 프로토콜을 연습하게 합니다.

1. 허용된 도구, 추론 노력 `none`, 300 토큰 완성 제한과 함께 질문을 보냅니다.
2. `tool_calls` 완료 이유를 요구하고, 함수 이름과 호출 ID를 검증하고, 유형화된 JSON 인수를 파싱합니다.
3. 로컬 함수를 실행합니다. 모델은 자바나 임의 코드를 실행하지 않습니다.
4. 어시스턴트 도구 호출 메시지를 한 번 추가하고, 각 결과를 해당 `tool_call_id`와 함께 추가합니다.
5. 도구 없이 최종 300 토큰 요청을 보내고 완료된 비어있지 않은 답변을 요구합니다.

`get_weather`는 **실시간이 아닌 시뮬레이션된** 날씨를 반환합니다. 도시를 존중하며 요청 시 샘플 22도 섭씨를 화씨로 변환합니다. `calculate`는 exp4j로 제공된 수식을 평가하며, `15% of 240`과 `2 + 3 * 4` 같은 형식을 지원하고, 빈 값, 과다 크기, 유효하지 않거나 무한 수 계산은 거부합니다. 금융 소수점 정밀도가 아닌 부동소수점 산술을 사용합니다.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

`Function: get_weather`, 시뮬레이션된 시애틀 날씨, `Function: calculate`, `Function result: 36`, 그리고 두 개의 최종 답변이 예상됩니다. stdin이나 외부 날씨 자격 증명은 필요 없습니다. 성공적인 실행은 정확히 네 개의 채팅 요청을 사용합니다.

## 튜토리얼 3: RAG (검색 강화 생성)

소스: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). 입력: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

이 입문 RAG 예시는 하나의 전체 UTF-8 문서를 검색하여 질문과 함께 사용자 메시지에 포함합니다. 별도의 시스템 메시지는 모델이 문서 내용을 신뢰할 수 없는 데이터로 여기고 해당 문맥에서만 답한다고 지시합니다. 문서에 답이 없으면 요청된 응답은: `제공된 문서에서 해당 정보를 찾을 수 없습니다.`입니다.

근거 제공은 환각을 줄일 수 있으나 구분자나 시스템 지침이 정확성을 보장하거나 모든 프롬프트 삽입을 방지하지는 않습니다. 라이브 답변을 검토하세요. 생산 환경 RAG는 일반적으로 청킹, 검색, 인용, 접근 제어 및 평가를 추가합니다.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

한 질문을 입력하세요, 예: `문서에서는 어떤 인증 방식을 설명하나요?`. Microsoft Entra ID를 언급하는 답변을 예상하세요. 프로그램은 하나의 채팅 요청 후, 500 토큰 완성 한도로 종료합니다.

기본 파일 조회는 저장소 루트, 챕터 디렉터리, 또는 예제 디렉터리에서 작동합니다. 명시적 경로도 지원됩니다:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

입력은 비어있지 않아야 하며: 최대 32 KiB의 UTF-8 문서 데이터와 2,000 문자 질문. 파일 누락, 빈/EOF 질문, 과대 입력은 추론 전에 실패합니다.

## 튜토리얼 4: 책임 있는 AI

소스: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

여섯 가지 검증은 유해 명령, 혐오 발언, 개인정보, 의료 오정보, 불법 콘텐츠, 그리고 양성 책임 AI 질문을 포함합니다. 프로그램은 응답을 관찰하며 모든 검증이 필터를 트리거해야 한다고 가정하지 않습니다.

| 결과 | 증거 |
| --- | --- |
| `FILTERED` | 명시적 `content_filter` / `ResponsibleAIPolicyViolation` 오류 코드 또는 `content_filter` 완료 이유 |
| `REFUSED` | 비어있지 않은 구조화된 `message.refusal` 필드 |
| `POSSIBLE_REFUSAL` | 일반 텍스트에 거부를 암시하는 시작 구절; 검토가 필요한 휴리스틱 |
| `GENERATED` | 완료된 비어있지 않은 응답; 내용 안전성 증거 아님 |

일반 HTTP 400은 필터링 증거가 아닙니다. 잘못된 매개변수, 인증 실패, 속도 제한, 서버 오류, 잘못된 응답 및 잘린 출력은 실패로 간주하며 잘못된 안전 성공을 생성하지 않습니다. "유해 콘텐츠" 같은 광범위한 단어가 양성 설명에 있다고 해서 거부로 간주하지 않습니다.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

여섯 가지 범주 결과와 관찰이 안전 인증이 아님을 명시하는 요약이 예상됩니다. 각 검증은 300 토큰 완성 한도를 가집니다. 예상치 못한 생성물과 가능한 거부는 수동으로 검토하세요; 양성 비교는 실질적인 책임 AI 설명을 생성해야 합니다. stdin은 필요 없습니다.

## 예제 전반의 공통 패턴

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java)는 엔드포인트 정규화, 배포 오버라이드, 키 없는 인증 및 채팅 옵션을 중앙 집중화합니다:

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

토큰 공급자는 필요 시 액세스 토큰을 갱신합니다. 토큰을 기록하지 마세요 또는 API 키로 교체하지 마세요. 각 프로그램은 클라이언트를 재사용하며 `finally` 블록이나 자체 `AutoCloseable` 래퍼를 통해 종료합니다; SDK의 `OpenAIClient`는 자체적으로 `AutoCloseable`이 아닙니다.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java)는 완료되고 비어있지 않은 텍스트 답변을 요구합니다. 빈 선택지, 거부, 필터, 잘린 답변은 성공으로 조용히 출력되지 않습니다. 책임 AI 예제는 예상 필터/거부 결과를 명시적으로 처리합니다. 처리되지 않은 실패는 자바/Maven 프로세스에 0이 아닌 종료 코드를 부여합니다.

**자동 SDK 재시도는 비활성화되어 있습니다**. 이는 공유 저속 RPM 배포에서 요청 수 예측 가능성을 유지하기 위함입니다. 모든 추론 요청은 60초 타임아웃입니다. 토큰 획득은 추가 시간이 필요할 수 있습니다. 애플리케이션 수준 스케줄링은 할당량을 준수해야 하며, 실패한 유료 요청을 무분별하게 재실행하지 마세요.

## 단위 테스트

예제 디렉터리에서:

```powershell
mvn -B -ntp clean test
```

테스트 트랜스포트는 SDK HTTP 계층을 완전히 대체하며, 실제 직렬화된 요청 본문을 캡처하고 대기 중인 응답을 제공합니다. 소켓을 열지 않고, Azure 토큰을 얻지 않으며, 예상치 못한 요청은 실패합니다. 이 테스트들은 애플리케이션 동작과 SDK 프로토콜을 검증하며, 라이브 모델 품질이나 배포 가용성은 검증하지 않습니다.

| 테스트 스위트 | 범위 |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | 엔드포인트 정규화/거부, 배포 오버라이드, 추론 및 토큰 옵션 |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | 모든 완성 워크플로우, 메시지 기록, 완전 턴 자르기, EOF, 실패 |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | 도구 스키마, 유형화된 인수, 산술, ID, 다중 도구 결과, 실패한 후속 처리 |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | 파일 조회, UTF-8, 크기 제한, 근거 페이로드, 입력 및 API 오류 |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | 여섯 검증 전부, 명시적 필터, 거부 분류, 일반 400 및 기타 실패 |

하나의 스위트를 실행하려면 `mvn -B -ntp test "-Dtest=FunctionsAppTest"`를 사용하세요. 공유 픽스처는 [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)에 있습니다.

## 순차적 실시간 검증

실시간 호출은 단위 테스트와 별도입니다. 자격 증명과 배포 접근 권한 준비 후, 저장소 루트에서 다음 명령을 <strong>개별적으로</strong> 실행하세요. 서비스나 지속 프로세스가 필요하지 않습니다.

공유 **분당 10 요청** 배포의 경우, 다음 프로그램 전체에 충분한 할당량을 예약한 후 실행하세요: 5, 4, 1, 그다음 6 요청. 순차적 프로세스만으로는 속도 제한 준수가 보장되지 않습니다. 모든 호출자와 분 단위 롤링을 조율하세요; 네 번의 호출을 무분별한 배치로 붙여넣지 마세요.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. 완성, 다중 턴, 두 번의 인터랙티브 턴:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

세 개 섹션 제목, 다섯 개 답변, Ada를 회상하는 마지막 인터랙티브 답변, `Goodbye!`, 종료 코드 0을 모두 확인하세요. 예산: **5 요청, 최대 1,900 완성 토큰**. 더 적은 실행을 원하면 `exit`만 파이프하세요: 3 요청 / 900 토큰, 하지만 이는 인터랙티브 추론을 실행하지 않습니다.

**2. 두 가지 함수 호출 워크플로:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

두 함수 이름, 시뮬레이션된 시애틀 날씨, 계산 결과 36, 두 개의 최종 답변, 종료 코드 0을 확인하세요. 예산: **4 요청, 최대 1,200 완성 토큰**.

**3. 문서 기반 답변:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

문서 경로, Microsoft Entra ID를 언급한 답변, 종료 코드 0을 확인하세요. 예산: **1 요청, 최대 500 완성 토큰**. 기존 [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)는 유일하게 필요한 입력 파일입니다. 없는 주제에 대한 선택적 두 번째 실행은 삼가해야 하며, 1 요청 / 500 토큰을 추가합니다.

**4. 책임 있는 AI 관찰:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

여섯 가지 범주와 관찰 요약을 확인하고, 생성된 콘텐츠를 검토하며, 기술적 완료를 위해 종료 코드 0을 요구하세요. 성공적인 프로세스 종료가 모델 안전성을 보증하지 않습니다. 예산: **6 요청, 최대 1,800 완성 토큰**.

**네 가지 명령 총계: 16 채팅 요청 및 최대 5,400 완성 토큰**, 입력 토큰(반복 대화 및 도구 스키마/이력 포함)도 포함됩니다. 임베딩 요청은 없습니다. 실제 토큰 사용량은 모델에 따라 다르며, 특히 필터링된 프롬프트에서 더 적을 수 있습니다. 금전 비용은 배포 가격 정책에 따라 달라집니다; 고정 금액 추정치는 없습니다. 모든 요청 제한은 수동 재실행이 없음을 가정합니다. 각 명령 직후 `$LASTEXITCODE`를 확인하세요; 0이 아니면 실행이 성공적으로 완료되지 않았음을 의미합니다.

## 문제 해결

- **엔드포인트 누락 / 401 / 403:** 실행 프로세스에서 엔드포인트를 설정하고, 로컬 Azure 로그인 및 리소스 범위 역할을 확인하며, 의도치 않은 신원 환경 오버라이드를 점검하세요.
- **400 / 404:** 배포가 존재하고 Chat Completions에서 reasoning effort가 `none`인 것을 지원하는지 확인하세요. HTTPS 리소스 루트 또는 `/openai/v1` URL을 사용하고, 레거시 배포 URL을 사용하지 마세요. 일반 400 오류는 기술적 실패이며, 안전 차단이 아닙니다.
- **429:** 재시도 전에 공유 RPM 및 토큰 할당량을 조정하세요. 예제는 자동 재시도를 하지 않습니다.
- **`Incomplete chat response: length`:** 출력이 완성 한도에 도달했습니다. 응답과 프롬프트를 검토한 후 한도 및 문서화된 예산을 늘리세요; 잘린 실행을 성공으로 기록하지 마세요.
- **파일 또는 stdin 오류:** 지원되는 디렉터리에서 실행하거나 명시적 문서 경로를 전달하세요. 공백이 아닌 리더 질문을 제공하세요. 완성은 EOF 또는 `exit`에서 정상 종료할 수 있습니다.
- **컴파일 오류:** Java 21 이상을 확인한 뒤 `mvn -B -ntp clean test`를 실행하세요. PowerShell에서는 점이 포함된 프로퍼티를 포함한 전체 Maven 인수를 인용 부호로 감싸세요. 예: `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"` .

## 다음 단계

계속해서 [4장: 실용 샘플](../04-PracticalSamples/README.md)로 진행하세요.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문가의 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->