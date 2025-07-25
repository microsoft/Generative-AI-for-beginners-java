<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:04:21+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "ko"
}
-->
# MCP 계산기 튜토리얼 초보자용

## 목차

- [배울 내용](../../../../../04-PracticalSamples/mcp/calculator)
- [사전 준비](../../../../../04-PracticalSamples/mcp/calculator)
- [프로젝트 구조 이해하기](../../../../../04-PracticalSamples/mcp/calculator)
- [핵심 구성 요소 설명](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. 메인 애플리케이션](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. 계산기 서비스](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. 직접 MCP 클라이언트](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI 기반 클라이언트](../../../../../04-PracticalSamples/mcp/calculator)
- [예제 실행하기](../../../../../04-PracticalSamples/mcp/calculator)
- [전체 작동 방식](../../../../../04-PracticalSamples/mcp/calculator)
- [다음 단계](../../../../../04-PracticalSamples/mcp/calculator)

## 배울 내용

이 튜토리얼은 Model Context Protocol (MCP)을 사용하여 계산기 서비스를 구축하는 방법을 설명합니다. 다음을 이해할 수 있습니다:

- AI가 도구로 사용할 수 있는 서비스를 만드는 방법
- MCP 서비스와 직접 통신을 설정하는 방법
- AI 모델이 어떤 도구를 사용할지 자동으로 선택하는 방법
- 직접 프로토콜 호출과 AI 지원 상호작용의 차이점

## 사전 준비

시작하기 전에 다음을 준비하세요:
- Java 21 이상 설치
- Maven을 사용한 의존성 관리
- 개인 액세스 토큰(PAT)이 있는 GitHub 계정
- Java와 Spring Boot에 대한 기본적인 이해

## 프로젝트 구조 이해하기

계산기 프로젝트에는 몇 가지 중요한 파일이 포함되어 있습니다:

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

## 핵심 구성 요소 설명

### 1. 메인 애플리케이션

**파일:** `McpServerApplication.java`

이 파일은 계산기 서비스의 진입점입니다. 표준 Spring Boot 애플리케이션에 특별한 기능이 추가되어 있습니다:

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

**이 파일의 역할:**
- 포트 8080에서 Spring Boot 웹 서버를 시작
- 계산기 메서드를 MCP 도구로 사용할 수 있도록 `ToolCallbackProvider` 생성
- `@Bean` 애노테이션을 사용하여 Spring이 다른 구성 요소에서 사용할 수 있도록 관리

### 2. 계산기 서비스

**파일:** `CalculatorService.java`

모든 수학 연산이 이루어지는 곳입니다. 각 메서드는 `@Tool`로 표시되어 MCP를 통해 사용할 수 있습니다:

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

**주요 특징:**

1. **`@Tool` 애노테이션**: 이 메서드가 외부 클라이언트에서 호출 가능하다는 것을 MCP에 알림
2. **명확한 설명**: 각 도구는 AI 모델이 언제 사용할지 이해할 수 있도록 설명을 포함
3. **일관된 반환 형식**: 모든 연산은 "5.00 + 3.00 = 8.00"과 같은 사람이 읽을 수 있는 문자열을 반환
4. **에러 처리**: 0으로 나누기와 음수 제곱근은 에러 메시지를 반환

**사용 가능한 연산:**
- `add(a, b)` - 두 숫자를 더함
- `subtract(a, b)` - 첫 번째 숫자에서 두 번째 숫자를 뺌
- `multiply(a, b)` - 두 숫자를 곱함
- `divide(a, b)` - 첫 번째 숫자를 두 번째 숫자로 나눔(0 체크 포함)
- `power(base, exponent)` - base를 exponent만큼 제곱
- `squareRoot(number)` - 제곱근 계산(음수 체크 포함)
- `modulus(a, b)` - 나머지 반환
- `absolute(number)` - 절대값 반환
- `help()` - 모든 연산에 대한 정보 반환

### 3. 직접 MCP 클라이언트

**파일:** `SDKClient.java`

이 클라이언트는 AI를 사용하지 않고 MCP 서버와 직접 통신하며 특정 계산기 기능을 호출합니다:

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

**이 파일의 역할:**
1. **계산기 서버**에 `http://localhost:8080`으로 연결
2. **사용 가능한 도구**(계산기 기능) 목록 표시
3. **특정 기능**을 정확한 매개변수로 호출
4. **결과**를 직접 출력

**사용 시점:** 수행하려는 계산을 정확히 알고 있고 프로그래밍 방식으로 호출하려는 경우.

### 4. AI 기반 클라이언트

**파일:** `LangChain4jClient.java`

이 클라이언트는 AI 모델(GPT-4o-mini)을 사용하여 어떤 계산기 도구를 사용할지 자동으로 결정합니다:

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

**이 파일의 역할:**
1. **GitHub 토큰**을 사용하여 AI 모델 연결 생성
2. **AI**를 계산기 MCP 서버에 연결
3. **AI**가 계산기 도구에 접근 가능하도록 설정
4. **자연어 요청**을 처리(예: "24.5와 17.3의 합을 계산해줘")

**AI가 자동으로 수행:**
- 요청을 분석하여 덧셈이 필요하다는 것을 이해
- `add` 도구를 선택
- `add(24.5, 17.3)` 호출
- 자연스러운 응답으로 결과 반환

## 예제 실행하기

### 1단계: 계산기 서버 시작

먼저 GitHub 토큰을 설정하세요(AI 클라이언트에 필요):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

서버 시작:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

서버는 `http://localhost:8080`에서 시작됩니다. 다음과 같은 메시지가 표시됩니다:
```
Started McpServerApplication in X.XXX seconds
```

### 2단계: 직접 클라이언트로 테스트

새 터미널에서:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

다음과 같은 출력이 표시됩니다:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### 3단계: AI 클라이언트로 테스트

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

AI가 도구를 자동으로 사용하는 것을 볼 수 있습니다:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## 전체 작동 방식

"5 + 3은 얼마야?"라고 AI에게 물어보면 다음과 같은 흐름으로 작동합니다:

1. **사용자**가 자연어로 AI에게 질문
2. **AI**가 요청을 분석하여 덧셈이 필요하다는 것을 이해
3. **AI**가 MCP 서버에 `add(5.0, 3.0)` 호출
4. **계산기 서비스**가 `5.0 + 3.0 = 8.0` 수행
5. **계산기 서비스**가 `"5.00 + 3.00 = 8.00"` 반환
6. **AI**가 결과를 받아 자연스러운 응답으로 포맷
7. **사용자**가 "5와 3의 합은 8입니다"라는 응답을 받음

## 다음 단계

더 많은 예제를 보려면 [Chapter 04: Practical samples](../../README.md)를 참조하세요.

**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있습니다. 원본 문서의 원어 버전을 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 책임을 지지 않습니다.