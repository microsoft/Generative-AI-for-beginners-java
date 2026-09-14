# 초보자를 위한 MCP 계산기 튜토리얼

## 목차

- [학습 내용](#학습-내용)
- [필수 조건](#필수-조건)
- [의존성 버전](#의존성-버전)
- [프로젝트 구조 이해](#프로젝트-구조-이해)
- [핵심 구성 요소 설명](#핵심-구성-요소-설명)
  - [1. 메인 애플리케이션](#1-메인-애플리케이션)
  - [2. 계산기 서비스](#2-계산기-서비스)
  - [3. 직접 MCP 클라이언트](#3-직접-mcp-클라이언트)
  - [4. AI 기반 클라이언트](#4-ai-기반-클라이언트)
- [예제 실행하기](#예제-실행하기)
- [오프라인 테스트](#오프라인-테스트)
- [전체 동작 원리](#전체-작동-방식)
- [다음 단계](#다음-단계)

## 학습 내용

이 튜토리얼에서는 모델 컨텍스트 프로토콜(MCP)을 사용하여 계산기 서비스를 구축하는 방법을 설명합니다. 다음을 이해하게 됩니다:

- AI가 도구로 사용할 수 있는 서비스 생성 방법
- MCP 서비스와 직접 통신 설정 방법
- AI 모델이 자동으로 사용할 도구를 선택하는 방법
- 직접 프로토콜 호출과 AI 지원 상호작용의 차이점

## 필수 조건

시작하기 전에 다음이 준비되어 있는지 확인하세요:
- Java 21 이상 설치
- 의존성 관리를 위한 Maven
- Java 및 Spring Boot에 대한 기본 이해

AI 클라이언트만 Azure OpenAI 배포 및 인증된 `DefaultAzureCredential`이 필요합니다,
예를 들어 로컬의 기존 Azure CLI 로그인이나 Azure 관리 ID가 있습니다. 이 아이덴티티는
Cognitive Services OpenAI 사용자 역할을 해당 리소스에 가져야 합니다. 자세한 내용은 [2장](../../02-SetupDevEnvironment/getting-started-azure-openai.md)을 참조하세요.
서버, 직접 SDK 클라이언트 및 모든 자동화 테스트는 Azure 계정이나 모델 액세스가 필요 없습니다.

## 의존성 버전

2026-09-14에 검증된 릴리스 의존성:

| 의존성 | 버전 |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI 관리) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j 공식 OpenAI 어댑터 | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot 관리) | 6.0.3 |

MCP 및 공식 OpenAI 어댑터는 Maven Central에 게시된 베타 릴리스이며 스냅샷이 아닙니다.
이들의 버전은 LangChain4j core와 다릅니다. 스냅샷이나 마일스톤 저장소는 필요하지 않습니다.
클라이언트 전용 의존성은 테스트 범위를 가지며 실행 가능한 예제는 `src/test/java` 아래에 있습니다.

## 프로젝트 구조 이해

계산기 프로젝트에는 몇 가지 중요한 파일이 있습니다:

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

## 핵심 구성 요소 설명

### 1. 메인 애플리케이션

**파일:** `McpServerApplication.java`

이것은 계산기 서비스의 진입점입니다. 표준 Spring Boot 애플리케이션이며 하나의 특별한 추가 기능이 있습니다:

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

**이 기능의 역할:**
- 8080 포트에서 Spring Boot 웹 서버를 시작합니다
- 계산기 메서드를 MCP 도구로 사용할 수 있게 하는 `ToolCallbackProvider`를 생성합니다
- `@Bean` 애노테이션은 Spring이 이 컴포넌트를 관리하여 다른 부분에서 사용할 수 있게 합니다

### 2. 계산기 서비스

**파일:** `CalculatorService.java`

이곳에서 모든 수학 연산이 이루어집니다. 각 메서드는 MCP를 통해 사용할 수 있도록 `@Tool`로 표시되어 있습니다:

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
    
    // 더 많은 계산기 연산...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**주요 기능:**

1. **`@Tool` 애노테이션**: 이 메서드가 외부 클라이언트에 의해 호출될 수 있음을 MCP에 알립니다
2. **명확한 설명**: 각 도구는 AI 모델이 언제 사용할지 이해할 수 있도록 설명이 포함되어 있습니다
3. **일관된 반환 형식**: 모든 연산은 "5.00 + 3.00 = 8.00"과 같은 사람이 읽기 쉬운 문자열을 반환합니다
4. **오류 처리**: 0으로 나누기 및 음수 제곱근에 대한 오류 메시지를 반환합니다

**사용 가능한 연산:**
- `add(a, b)` - 두 숫자를 더합니다
- `subtract(a, b)` - 두 번째 숫자를 첫 번째에서 뺍니다
- `multiply(a, b)` - 두 숫자를 곱합니다
- `divide(a, b)` - 첫 번째를 두 번째로 나눕니다 (0 체크 포함)
- `power(base, exponent)` - base를 exponent만큼 거듭제곱합니다
- `squareRoot(number)` - 제곱근을 계산합니다 (음수 체크 포함)
- `modulus(a, b)` - 나머지를 반환합니다
- `absolute(number)` - 절댓값을 반환합니다
- `help()` - 모든 연산에 대한 정보를 반환합니다

### 3. 직접 MCP 클라이언트

[SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)을 참조하세요.

이 클라이언트는 `/mcp`에서 `HttpClientStreamableHttpTransport`를 사용하여 연결을 초기화하고,
서버에 핑을 보내며 도구 목록 페이징을 따릅니다. 아홉 개의 예상 도구가 모두 존재하는지 확인하고,
AI 모델 없이 `modulus`와 `help`를 포함한 각각을 호출합니다.

현재 요청 빌더는 다음과 같습니다:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

프로토콜 오류는 잘못된 성공 메시지 출력 대신 클라이언트를 실패하게 만듭니다. MCP 클라이언트는
발견 또는 도구 호출 실패 시에도 try-with-resources로 닫힙니다.

### 4. AI 기반 클라이언트

[LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
및 [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)를 참조하세요.

`OpenAiOfficialChatModel`은 현재 LangChain4j `ChatModel` API를 구현합니다.
`StreamableHttpMcpTransport`는 SDK 클라이언트와 동일한 `/mcp` 엔드포인트에 연결합니다.
`AiServices`는 도구를 발견하고 도구 호출/결과 대화를 관리합니다.

기본 배포는 <strong>GPT-5.6 Luna</strong>이며, 추론이 명시적으로 비활성화되어 있습니다:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

이러한 기본값은 도구 실행 후 후속 질문을 포함한 모든 완료에 적용됩니다.
클라이언트는 `DefaultAzureCredential`로 뒷받침되는 갱신 가능한 `BearerTokenCredential`
및 `https://ai.azure.com/.default` 범위를 사용하며, API 키로 전달되는 일회성 토큰이 아닙니다.
리소스 URL과 이미 `/openai/v1`로 끝나는 URL 모두 허용됩니다.

봇은 제한된 대화 기록을 유지하며, 실제 MCP 결과와 함께 `Tool executed: ...`를 출력하고,
도구를 건너뛰는 응답이 있으면 실패합니다. 도구 루프는 4회 왕복으로 제한됩니다.
인증, 모델, MCP, 도구 오류는 모두 전파되며, 자동 모델 재시도는 비활성화되어 있습니다.
MCP 트랜스포트/클라이언트와 공식 OpenAI 클라이언트는 성공 또는 실패 시 모두 닫힙니다.

## 예제 실행하기

### 1단계: 계산기 서버 시작

서버에는 Azure 구성이 필요 없습니다. 아래 명령은 이 샘플의 디렉터리에서 실행됩니다.
예제는 다른 샘플과 충돌을 피하기 위해 포트 <strong>18081</strong>을 사용합니다; 기본값은 여전히 8080입니다.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP 엔드포인트는 `http://localhost:18081/mcp`입니다. 상태 및 검색 정보는
`http://localhost:18081/health` 및 `http://localhost:18081/info`에서 확인할 수 있습니다.
스트리밍 가능한 HTTP가 이전 SSE 전용 전송을 대체합니다; `/sse` 및 `/v1/tools`는 엔드포인트가 아닙니다.

### 2단계: 직접 클라이언트로 테스트

다른 PowerShell 터미널에서:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

입력이 필요 없습니다. 모든 아홉 개 툴을 실행합니다. 예상 산술 결과는
8, 6, 42, 5, 256, 4, 2, 5.5이며, 도움말 텍스트가 뒤따릅니다.

### 3단계: AI 클라이언트로 테스트

사전 요구 조건대로 인증 후, 같은 터미널에서 AI 클라이언트를 구성합니다:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

`Tool executed: add` 문장과 `41.80`이 표시되고, 모델의 답변이 뒤따릅니다.
단일 프롬프트 모드는 입력을 기다리지 않고 종료됩니다. 원래의 네 프롬프트 데모를 실행하려면:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

데모는 `add`, `squareRoot`, `help`, 그리고 연결된 `power` 후에 `divide` 작업을 호출합니다.
예상 숫자 답변은 41.8, 12, 64입니다. 인수를 생략해도 이 데모가 실행됩니다.

### 4단계: 인터랙티브 봇 실행

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

`Multiply 6 by 7 using the calculator service`를 입력한 후, `exit` 또는 `quit`를 입력합니다.
실제 `multiply` 도구 결과인 42가 예상됩니다. 빈 줄은 무시되며, EOF도 세션을 종료합니다.
이 진입점의 비대화식 스모크 테스트는 다음과 같습니다:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

두 AI 진입점 모두 `--prompt "question"`, `--demo`, `--interactive`를 허용합니다.
잘못된 옵션은 연결을 열기 전에 실패합니다. 각 Maven `-D...` 인수는 PowerShell에서 완전히 인용됩니다.
Bash에서는 `$env:NAME = "value"` 대신 `export NAME=value`를 사용하십시오.

**쿼터:** AI 샘플은 순차적으로 실행하십시오. 간단한 프롬프트는 보통 두 번의 모델 요청이 필요하며,
전체 데모는 도구 결과 후속 포함 총 아홉 번을 필요로 합니다. 공유 10 RPM 배포 환경에서는
다음 AI 실행 전 쿼터 기간이 새로워지도록 하십시오. 429 오류는 자동 재시도 없이 가시적으로 실패하며,
서비스의 재시도 후 안내를 따르십시오. 실제 요청 수는 모델에 따라 다릅니다.
오프라인 테스트는 쿼터를 소모하지 않고 라이브 Luna 가용성이나 답변 품질을 측정하지 않습니다.

### 구성 및 종료

| 설정 | 기본값 / 동작 |
| --- | --- |
| `MCP_SERVER_URL` | `/mcp` 없이 기본 URL `http://localhost:8080` |
| `-Dmcp.server.url=...` | 모든 클라이언트에 대해 `MCP_SERVER_URL`을 재정의 |
| `AZURE_OPENAI_ENDPOINT` | AI 클라이언트에만 필요; 리소스 URL 또는 `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure 배포 이름 |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; 양의 정수 |
| 추론 노력 | 항상 `none`, 도구 루프 후속 포함 |

재정의된 배포는 `reasoning_effort=none` 및 `max_completion_tokens`를 지원해야 합니다.
클라이언트는 `.env` 파일을 자동으로 읽지 않습니다. 테스트 후 서버를 `Ctrl+C`로 중지하십시오.
클라이언트는 `System.exit` 또는 종료 대기 없이 정상적으로 반환됩니다.

## 오프라인 테스트

```powershell
mvn -B -ntp clean verify
```

모든 테스트는 Azure와 관련하여 오프라인입니다: 프로토콜 모음은 스프링 서버와
OpenAI 호환 스텁을 무작위 루프백 포트에서 시작한 후 닫습니다. Maven은 여전히
의존성을 다운로드해야 할 수 있습니다. 자격 증명, 라이브 배포, 기존 MCP 서버는 사용하지 않습니다.

- 계산기 단위 테스트는 모든 산술 연산, 소수 결과, 도움말 및 도메인 오류를 다룹니다.
- MCP 테스트는 초기화, 검색, 아홉 개 도구 호출, 도구 실패, 상태/정보를 다룹니다.
- AI 프로토콜 테스트는 진짜 계산기를 대상으로 전체 데모와 인터랙티브 봇을 실행하며,
  도구 결과가 다음 완성 요청에 전달되는지 확인하고, Luna,
  `reasoning_effort: "none"`, `max_completion_tokens` 및 이전 `max_tokens`가 없는지 모든 HTTP 본문을 검사합니다.
- 구성/입력 테스트는 배포 및 엔드포인트 재정의, 빈 줄, EOF, 종료/종료 명령,
  단일 프롬프트 모드, 잘못된 옵션, 오류 전파를 다룹니다. 쿼터 테스트는 429 오류가 재시도되지 않음을 증명합니다.

## 전체 작동 방식

AI에 "5 + 3은 얼마인가요?"라고 물었을 때의 전체 흐름은 다음과 같습니다:

1. <strong>사용자</strong>가 자연어로 AI에 질문합니다
2. <strong>AI</strong>가 요청을 분석하고 덧셈을 원한다는 것을 인식합니다
3. <strong>AI</strong>가 MCP 서버를 호출합니다: `add(5.0, 3.0)`
4. <strong>계산기 서비스</strong>가 실행합니다: `5.0 + 3.0 = 8.0`
5. <strong>계산기 서비스</strong>가 결과를 반환합니다: `"5.00 + 3.00 = 8.00"`
6. <strong>AI</strong>가 결과를 받아 자연스러운 응답을 만듭니다
7. <strong>사용자</strong>가 받는 답변: "5와 3의 합은 8입니다"

## 다음 단계

더 많은 예제는 [4장: 실용 샘플](../README.md)을 참조하세요

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문가의 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->