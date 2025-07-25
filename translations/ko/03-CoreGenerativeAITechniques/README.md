<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:03:13+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "ko"
}
-->
# 핵심 생성 AI 기술 튜토리얼

## 목차

- [사전 준비](../../../03-CoreGenerativeAITechniques)
- [시작하기](../../../03-CoreGenerativeAITechniques)
  - [1단계: 환경 변수 설정](../../../03-CoreGenerativeAITechniques)
  - [2단계: 예제 디렉토리로 이동](../../../03-CoreGenerativeAITechniques)
- [튜토리얼 1: LLM 완성과 채팅](../../../03-CoreGenerativeAITechniques)
- [튜토리얼 2: 함수 호출](../../../03-CoreGenerativeAITechniques)
- [튜토리얼 3: RAG (검색 증강 생성)](../../../03-CoreGenerativeAITechniques)
- [튜토리얼 4: 책임 있는 AI](../../../03-CoreGenerativeAITechniques)
- [예제 전반에 걸친 공통 패턴](../../../03-CoreGenerativeAITechniques)
- [다음 단계](../../../03-CoreGenerativeAITechniques)
- [문제 해결](../../../03-CoreGenerativeAITechniques)
  - [일반적인 문제](../../../03-CoreGenerativeAITechniques)

## 개요

이 튜토리얼은 Java와 GitHub Models를 사용하여 핵심 생성 AI 기술을 실습할 수 있는 예제를 제공합니다. 대규모 언어 모델(LLM)과 상호작용하는 방법, 함수 호출 구현, 검색 증강 생성(RAG) 사용, 책임 있는 AI 실천 방법을 배우게 됩니다.

## 사전 준비

시작하기 전에 다음을 준비하세요:
- Java 21 이상 설치
- 의존성 관리를 위한 Maven
- 개인 액세스 토큰(PAT)이 있는 GitHub 계정

## 시작하기

### 1단계: 환경 변수 설정

먼저 GitHub 토큰을 환경 변수로 설정해야 합니다. 이 토큰은 GitHub Models에 무료로 액세스할 수 있도록 합니다.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### 2단계: 예제 디렉토리로 이동

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## 튜토리얼 1: LLM 완성과 채팅

**파일:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### 이 예제가 가르치는 것

이 예제는 OpenAI API를 통해 대규모 언어 모델(LLM)과 상호작용하는 핵심 메커니즘을 보여줍니다. 여기에는 GitHub Models를 사용한 클라이언트 초기화, 시스템 및 사용자 프롬프트를 위한 메시지 구조 패턴, 메시지 기록 축적을 통한 대화 상태 관리, 응답 길이와 창의성 수준을 제어하기 위한 매개변수 조정이 포함됩니다.

### 주요 코드 개념

#### 1. 클라이언트 설정
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

GitHub Models에 연결을 생성합니다.

#### 2. 간단한 완성
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. 대화 메모리
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI는 이전 메시지를 후속 요청에 포함시킬 때만 기억합니다.

### 예제 실행
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### 실행 결과

1. **간단한 완성**: 시스템 프롬프트 지침에 따라 AI가 Java 질문에 답변합니다.
2. **다중 턴 채팅**: AI가 여러 질문에 걸쳐 문맥을 유지합니다.
3. **대화형 채팅**: AI와 실제 대화를 나눌 수 있습니다.

## 튜토리얼 2: 함수 호출

**파일:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### 이 예제가 가르치는 것

함수 호출은 AI 모델이 JSON Schema 정의를 사용하여 자연어 요청을 분석하고 적절한 매개변수를 가진 함수 호출을 결정하며, 반환된 결과를 처리하여 문맥에 맞는 응답을 생성하는 구조화된 프로토콜을 통해 외부 도구와 API 실행을 요청할 수 있도록 합니다. 실제 함수 실행은 보안과 신뢰성을 위해 개발자가 제어합니다.

### 주요 코드 개념

#### 1. 함수 정의
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

AI에게 사용 가능한 함수와 사용 방법을 알려줍니다.

#### 2. 함수 실행 흐름
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. 함수 구현
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### 예제 실행
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### 실행 결과

1. **날씨 함수**: AI가 시애틀의 날씨 데이터를 요청하고, 사용자가 제공하면 AI가 응답을 형식화합니다.
2. **계산기 함수**: AI가 계산(240의 15%)을 요청하고, 사용자가 계산하면 AI가 결과를 설명합니다.

## 튜토리얼 3: RAG (검색 증강 생성)

**파일:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### 이 예제가 가르치는 것

검색 증강 생성(RAG)은 외부 문서 컨텍스트를 AI 프롬프트에 주입하여 정보 검색과 언어 생성을 결합합니다. 이를 통해 모델이 잠재적으로 오래되거나 부정확한 학습 데이터 대신 특정 지식 소스를 기반으로 정확한 답변을 제공할 수 있도록 하며, 전략적 프롬프트 엔지니어링을 통해 사용자 질문과 권위 있는 정보 소스 간의 명확한 경계를 유지합니다.

### 주요 코드 개념

#### 1. 문서 로딩
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. 컨텍스트 주입
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

트리플 따옴표는 AI가 컨텍스트와 질문을 구분하도록 돕습니다.

#### 3. 안전한 응답 처리
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API 응답을 항상 검증하여 충돌을 방지합니다.

### 예제 실행
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### 실행 결과

1. 프로그램이 `document.txt`를 로드합니다 (GitHub Models에 대한 정보 포함).
2. 문서에 대한 질문을 합니다.
3. AI는 일반 지식이 아닌 문서 내용만을 기반으로 답변합니다.

다음 질문을 시도해 보세요: "GitHub Models란 무엇인가요?" vs "오늘 날씨는 어떤가요?"

## 튜토리얼 4: 책임 있는 AI

**파일:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### 이 예제가 가르치는 것

책임 있는 AI 예제는 AI 애플리케이션에서 안전 조치를 구현하는 중요성을 보여줍니다. 혐오 발언, 괴롭힘, 자해, 성적 콘텐츠, 폭력 등 유해 콘텐츠 범주를 감지하는 안전 필터를 시연하며, 콘텐츠 정책 위반을 적절히 처리하기 위한 예외 처리, 사용자 피드백 메커니즘, 대체 응답 전략을 통해 프로덕션 AI 애플리케이션이 어떻게 작동해야 하는지 보여줍니다.

### 주요 코드 개념

#### 1. 안전 테스트 프레임워크
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. 테스트된 안전 범주
- 폭력/자해 지침
- 혐오 발언
- 개인정보 침해
- 의료 정보 오도
- 불법 활동

### 예제 실행
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### 실행 결과

프로그램은 다양한 유해 프롬프트를 테스트하며 AI 안전 시스템이:
1. **위험한 요청 차단**: HTTP 400 오류로 처리
2. **안전한 콘텐츠 허용**: 정상적으로 생성
3. **사용자 보호**: 유해한 AI 출력 방지

## 예제 전반에 걸친 공통 패턴

### 인증 패턴
모든 예제는 GitHub Models 인증에 이 패턴을 사용합니다:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### 오류 처리 패턴
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### 메시지 구조 패턴
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## 다음 단계

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## 문제 해결

### 일반적인 문제

**"GITHUB_TOKEN not set"**
- 환경 변수를 설정했는지 확인하세요.
- 토큰에 `models:read` 범위가 있는지 확인하세요.

**"No response from API"**
- 인터넷 연결을 확인하세요.
- 토큰이 유효한지 확인하세요.
- 속도 제한에 도달했는지 확인하세요.

**Maven 컴파일 오류**
- Java 21 이상이 설치되어 있는지 확인하세요.
- `mvn clean compile`을 실행하여 의존성을 새로 고치세요.

**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있지만, 자동 번역에는 오류나 부정확성이 포함될 수 있습니다. 원본 문서의 원어 버전을 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 책임을 지지 않습니다.