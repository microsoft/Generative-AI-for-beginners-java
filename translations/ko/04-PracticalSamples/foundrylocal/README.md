<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:03:55+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "ko"
}
-->
# Foundry Local Spring Boot 튜토리얼

## 목차

- [사전 준비](../../../../04-PracticalSamples/foundrylocal)
- [프로젝트 개요](../../../../04-PracticalSamples/foundrylocal)
- [코드 이해하기](../../../../04-PracticalSamples/foundrylocal)
  - [1. 애플리케이션 설정 (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. 메인 애플리케이션 클래스 (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI 서비스 계층 (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. 프로젝트 의존성 (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [전체 동작 원리](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local 설정하기](../../../../04-PracticalSamples/foundrylocal)
- [애플리케이션 실행하기](../../../../04-PracticalSamples/foundrylocal)
- [예상 출력](../../../../04-PracticalSamples/foundrylocal)
- [다음 단계](../../../../04-PracticalSamples/foundrylocal)
- [문제 해결](../../../../04-PracticalSamples/foundrylocal)

## 사전 준비

이 튜토리얼을 시작하기 전에 다음을 준비하세요:

- **Java 21 이상**이 시스템에 설치되어 있어야 합니다.
- **Maven 3.6+**이 프로젝트 빌드를 위해 필요합니다.
- **Foundry Local**이 설치 및 실행 중이어야 합니다.

### **Foundry Local 설치:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## 프로젝트 개요

이 프로젝트는 다음 네 가지 주요 구성 요소로 이루어져 있습니다:

1. **Application.java** - Spring Boot 애플리케이션의 메인 진입점
2. **FoundryLocalService.java** - AI 통신을 처리하는 서비스 계층
3. **application.properties** - Foundry Local 연결을 위한 설정 파일
4. **pom.xml** - Maven 의존성과 프로젝트 설정

## 코드 이해하기

### 1. 애플리케이션 설정 (application.properties)

**파일:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**이 설정의 역할:**
- **base-url**: Foundry Local이 실행 중인 위치를 지정 (기본 포트 5273)
- **model**: 텍스트 생성을 위해 사용할 AI 모델 이름

**핵심 개념:** Spring Boot는 이 설정을 자동으로 로드하고 `@Value` 어노테이션을 통해 애플리케이션에서 사용할 수 있도록 합니다.

### 2. 메인 애플리케이션 클래스 (Application.java)

**파일:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**이 코드의 역할:**
- `@SpringBootApplication`은 Spring Boot의 자동 설정을 활성화합니다.
- `WebApplicationType.NONE`은 이 애플리케이션이 웹 서버가 아닌 커맨드라인 애플리케이션임을 나타냅니다.
- 메인 메서드는 Spring 애플리케이션을 시작합니다.

**데모 실행기:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**이 코드의 역할:**
- `@Bean`은 Spring이 관리하는 컴포넌트를 생성합니다.
- `CommandLineRunner`는 Spring Boot가 시작된 후 코드를 실행합니다.
- `foundryLocalService`는 Spring에 의해 자동으로 주입됩니다 (의존성 주입).
- 테스트 메시지를 AI에 보내고 응답을 출력합니다.

### 3. AI 서비스 계층 (FoundryLocalService.java)

**파일:** `src/main/java/com/example/FoundryLocalService.java`

#### 설정 주입:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**이 코드의 역할:**
- `@Service`는 이 클래스가 비즈니스 로직을 제공한다고 Spring에 알립니다.
- `@Value`는 application.properties에서 설정 값을 주입합니다.
- `:default-value` 구문은 설정 값이 없을 경우 기본값을 제공합니다.

#### 클라이언트 초기화:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**이 코드의 역할:**
- `@PostConstruct`는 Spring이 서비스를 생성한 후 이 메서드를 실행합니다.
- OpenAI 클라이언트를 생성하여 로컬 Foundry Local 인스턴스에 연결합니다.
- `/v1` 경로는 OpenAI API 호환성을 위해 필요합니다.
- API 키는 로컬 개발에서는 인증이 필요 없으므로 "unused"로 설정됩니다.

#### 채팅 메서드:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**이 코드의 역할:**
- **ChatCompletionCreateParams**: AI 요청을 구성합니다.
  - `model`: 사용할 AI 모델을 지정합니다.
  - `addUserMessage`: 대화에 사용자의 메시지를 추가합니다.
  - `maxCompletionTokens`: 응답 길이를 제한하여 리소스를 절약합니다.
  - `temperature`: 응답의 무작위성을 제어합니다 (0.0 = 결정적, 1.0 = 창의적).
- **API 호출**: Foundry Local에 요청을 보냅니다.
- **응답 처리**: AI의 텍스트 응답을 안전하게 추출합니다.
- **에러 처리**: 유용한 에러 메시지로 예외를 래핑합니다.

### 4. 프로젝트 의존성 (pom.xml)

**주요 의존성:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**이 의존성의 역할:**
- **spring-boot-starter**: Spring Boot의 핵심 기능을 제공합니다.
- **openai-java**: OpenAI API 통신을 위한 공식 Java SDK입니다.
- **jackson-databind**: API 호출을 위한 JSON 직렬화/역직렬화를 처리합니다.

## 전체 동작 원리

애플리케이션을 실행하면 다음과 같은 흐름으로 동작합니다:

1. **시작**: Spring Boot가 시작되고 `application.properties`를 읽습니다.
2. **서비스 생성**: Spring이 `FoundryLocalService`를 생성하고 설정 값을 주입합니다.
3. **클라이언트 설정**: `@PostConstruct`가 OpenAI 클라이언트를 초기화합니다.
4. **데모 실행**: `CommandLineRunner`가 시작 후 실행됩니다.
5. **AI 호출**: 데모가 `foundryLocalService.chat()`을 호출하여 테스트 메시지를 보냅니다.
6. **API 요청**: 서비스가 OpenAI 호환 요청을 Foundry Local에 보냅니다.
7. **응답 처리**: 서비스가 AI의 응답을 추출하고 반환합니다.
8. **출력**: 애플리케이션이 응답을 출력하고 종료합니다.

## Foundry Local 설정하기

Foundry Local을 설정하려면 다음 단계를 따르세요:

1. [사전 준비](../../../../04-PracticalSamples/foundrylocal) 섹션의 지침에 따라 Foundry Local을 설치하세요.
2. 사용하려는 AI 모델(예: `phi-3.5-mini`)을 다운로드하세요. 다음 명령어를 사용합니다:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. `application.properties` 파일을 열어 Foundry Local 설정(특히 다른 포트나 모델을 사용하는 경우)을 업데이트하세요.

## 애플리케이션 실행하기

### 1단계: Foundry Local 시작
```bash
foundry model run phi-3.5-mini
```

### 2단계: 애플리케이션 빌드 및 실행
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## 예상 출력

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## 다음 단계

더 많은 예제를 보려면 [Chapter 04: Practical samples](../README.md)를 참조하세요.

## 문제 해결

### 일반적인 문제

**"Connection refused" 또는 "Service unavailable"**
- Foundry Local이 실행 중인지 확인하세요: `foundry model list`
- 서비스가 포트 5273에서 실행 중인지 확인하세요: `application.properties`를 점검하세요.
- Foundry Local을 다시 시작해 보세요: `foundry model run phi-3.5-mini`

**"Model not found" 오류**
- 사용 가능한 모델을 확인하세요: `foundry model list`
- `application.properties`에서 모델 이름을 정확히 업데이트하세요.
- 필요한 경우 모델을 다운로드하세요: `foundry model run phi-3.5-mini`

**Maven 컴파일 오류**
- Java 21 이상이 설치되었는지 확인하세요: `java -version`
- 클린 빌드 후 다시 시도하세요: `mvn clean compile`
- 의존성 다운로드를 위해 인터넷 연결을 확인하세요.

**애플리케이션이 시작되었지만 출력이 없음**
- Foundry Local이 응답하는지 확인하세요: 브라우저에서 `http://localhost:5273`를 열어보세요.
- 애플리케이션 로그에서 특정 에러 메시지를 확인하세요.
- 모델이 완전히 로드되고 준비되었는지 확인하세요.

**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있지만, 자동 번역에는 오류나 부정확성이 포함될 수 있습니다. 원본 문서의 원어 버전을 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 책임을 지지 않습니다.