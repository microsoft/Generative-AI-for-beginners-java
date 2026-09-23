# 초보자를 위한 반려동물 이야기 생성기 튜토리얼

반려동물 사진을 업로드하고, GPT-5.6 Luna로 분석한 후, 결과 설명에서 이야기를 생성합니다. 두 모델 요청 모두 `reasoning_effort: none`을 사용합니다.

| 구성 요소 | 버전 |
| --- | --- |
| Java | 21 이상 |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## 목차

- [사전 준비](#사전-준비)
- [프로젝트 구조 이해](#프로젝트-구조-이해)
- [핵심 구성 요소 설명](#핵심-구성-요소-설명)
  - [1. 메인 애플리케이션](#1-메인-애플리케이션)
  - [2. 웹 컨트롤러](#2-웹-컨트롤러)
  - [3. 이야기 서비스](#3-이야기-서비스)
  - [4. 웹 템플릿](#4-웹-템플릿)
  - [5. 구성](#5-구성)
- [애플리케이션 실행](#애플리케이션-실행)
- [오프라인 테스트](#오프라인-테스트)
- [전체 동작 과정](#전체-동작-과정)
- [AI 통합 이해](#ai-통합-이해)
- [다음 단계](#다음-단계)

## 사전 준비

시작하기 전에 다음을 준비하십시오:
- Java 21 이상 설치
- 의존성 관리를 위한 Maven
- `gpt-5.6-luna`라는 이름의 Azure AI Foundry의 GPT-5.6 Luna 배포 또는 해당 배포를 가리키는 `AZURE_OPENAI_DEPLOYMENT` 오버라이드. 프로비저닝과 `az login`으로 무키 인증에 대한 자세한 내용은 [2장](../../02-SetupDevEnvironment/getting-started-azure-openai.md) 참고. 배포는 이미지 입력과 `reasoning_effort: none`을 지원해야 합니다.
- Java, Spring Boot, 웹 개발에 대한 기본 이해

## 프로젝트 구조 이해

반려동물 이야기 프로젝트에는 몇 가지 중요한 파일이 있습니다:

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

## 핵심 구성 요소 설명

### 1. 메인 애플리케이션

**파일:** `PetStoryApplication.java`

이 파일은 Spring Boot 애플리케이션의 진입점입니다:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**작동 방식:**
- `@SpringBootApplication` 어노테이션으로 자동 설정과 컴포넌트 스캔 활성화
- 포트 8080에서 내장 웹 서버(Tomcat)를 시작
- 필요한 모든 Spring 빈과 서비스를 자동 생성

### 2. 웹 컨트롤러

**파일:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| 엔드포인트 | 요청 | 성공 응답 |
| --- | --- | --- |
| `GET /` | 본문 없음 | CSRF 토큰이 포함된 HTML 업로드 폼 |
| `POST /analyze-image` | `multipart/form-data`, 파일 필드 `image` | JSON: `{"description":"장난기 많은 반려동물..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, 필드 `description` | 설명과 생성된 이야기가 포함된 HTML 결과 페이지 |

두 POST 엔드포인트 모두 `GET /`에서 받은 세션 쿠키와 CSRF 토큰을 필요로 합니다. 업로드 스크립트는 숨겨진 `_csrf` 값을 `X-CSRF-TOKEN` 헤더로 보내고, 이야기 제출은 `_csrf` 폼 필드로 보냅니다. API 클라이언트는 요청 간 쿠키를 유지해야 합니다. 이들은 JSON 요청 엔드포인트가 아니라 폼 엔드포인트입니다.

설명은 비어 있으면 안 되며 1000자 이내여야 합니다. 컨트롤러는 설명을 트리밍하고 `<`, `>`, 큰따옴표, 작은따옴표, `&`를 제거한 후 서비스에 전달합니다. 결과 템플릿도 `th:text`로 모델 출력을 이스케이프합니다.

이미지 검증 실패 시 HTTP 400과 `error` 필드를 반환하며, 모델 실패 시 HTTP 502와 `error` 필드를 반환하고 `description`은 없습니다. 유효하지 않은 이야기 설명이나 모델 실패 시 `/`로 리다이렉트되며 오류가 표시됩니다. 필수 필드 누락 시 HTTP 400, CSRF 토큰 누락 또는 유효하지 않을 경우 HTTP 403입니다. 대체 설명이나 이야기는 성공한 AI 결과로 제공되지 않습니다.

### 3. 이야기 서비스

**파일:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

공식 OpenAI Java SDK 4.63.1은 Azure AI Foundry의 OpenAI 호환 Chat Completions API를 호출합니다. Azure Identity 1.18.6은 `DefaultAzureCredential`을 통해 Microsoft Entra 베어러 토큰을 제공합니다; API 키는 필요 없습니다.

| 작업 | 입력 | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | 업로드된 MIME 타입을 가진 base64 데이터 URL로 인코딩된 이미지 바이트 | 300 |
| `generateStory` | 사용자 메시지에 담긴 반려동물 설명 | 800 |

두 요청 모두 설정된 배포(기본값은 `gpt-5.6-luna`)를 사용하며, 명시적으로 `ReasoningEffort.NONE` (`reasoning_effort: none`)을 설정합니다. `temperature`나 레거시 `max_tokens` 매개변수는 전혀 보내지지 않습니다.

이미지 분석은 JPEG, PNG, GIF, WebP를 수락하며, 빈 이미지와 10MB 초과 파일을 거부합니다. 결과 설명 길이는 1000자로 제한합니다. 이야기 프롬프트는 가족 친화적인 짧은 이야기를 요청합니다. 빈 선택지 또는 빈 모델 콘텐츠는 오류이며, 실패 시 원인을 유지하여 서버 측 진단에 활용합니다. 애플리케이션 종료 시 SDK 클라이언트를 닫습니다.

### 4. 웹 템플릿

**파일:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (업로드 폼)

페이지는 설명 텍스트 영역이 아니라 사진 선택기부터 시작합니다. **이미지 분석** 버튼은 선택한 사진을 미리 보기로 표시하고 `/analyze-image`에 POST합니다. 성공 응답 시 설명이 표시되고 숨겨진 `description` 필드가 채워지며 **이야기 생성** 버튼이 나타납니다. 이 버튼은 현재 폼을 `/generate-story`에 제출합니다.

브라우저 모델 다운로드나 CDN 의존성이 없습니다. 이미지 분석은 구성된 Azure 배포를 통해 서버에서 실행됩니다. 실패 시 오류가 표시되고 조작된 설명으로 이야기를 생성하지 않습니다. 다른 파일 선택 시 이전 분석이 초기화됩니다.

**파일:** `result.html` (이야기 표시)

생성된 이야기를 보여줍니다:

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

**템플릿 기능:**

1. **타임리프 통합**: 동적 콘텐츠를 위한 `th:` 속성 사용
2. **반응형 디자인**: 모바일 및 데스크톱용 CSS 스타일링
3. **오류 처리**: 사용자에게 검증 오류 표시
4. **업로드 처리**: 자바스크립트로 사진 미리보기, CSRF 보호된 multipart 요청 전송, 반환된 설명 표시

### 5. 구성

**파일:** `application.properties`

애플리케이션 설정:

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

**구성 설명:**

1. **파일 업로드**: 파일 및 전체 multipart 요청 크기는 10MB로 제한; multipart 헤더 공간을 위해 사진 크기를 제한하십시오
2. <strong>로깅</strong>: 실행 중 기록할 정보를 제어
3. **Azure AI Foundry**: 사용할 엔드포인트 및 모델 배포 지정(키리스 인증)
4. <strong>보안</strong>: CSRF 보호 활성화 유지; 모델 진단은 서버에 기록, 컨트롤러는 일반적인 실패 메시지 표시

## 애플리케이션 실행

### 1단계: 로그인 및 엔드포인트 설정

인증은 키리스 방식(마이크로소프트 엔트라 ID)이므로 API 키가 필요 없습니다. 로그인하고 Foundry 엔드포인트를 설정하십시오:

**Windows (명령 프롬프트):**
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

**왜 필요한가:**
- Azure AI Foundry는 Microsoft Entra ID를 사용해 추론 요청을 인증
- 키리스 인증은 소스 코드나 환경에 비밀이 없음을 의미
- 계정에 대해 리소스에 **Cognitive Services OpenAI User** 역할 필요

기본 배포 이름은 `gpt-5.6-luna`입니다. 다른 이름인 경우, 애플리케이션 시작 전에 터미널에서 `AZURE_OPENAI_DEPLOYMENT`를 설정하십시오. 이미지 분석과 이야기 생성이 이 설정을 공유합니다.

### 2단계: 빌드 및 실행

프로젝트 디렉터리로 이동:
```bash
cd 04-PracticalSamples/petstory
```

독립 실행 실행 가능한 JAR 빌드 및 모든 오프라인 테스트 실행:
```bash
mvn clean package
```

서버 시작:
```bash
mvn spring-boot:run
```

애플리케이션은 `http://localhost:8080`에서 시작합니다.

또는 예를 들어 빈 포트에서 패키지된 JAR를 시작:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

이 명령으로 `http://localhost:8083/`를 엽니다. 선택한 포트에서도 같은 `/analyze-image`와 `/generate-story` 경로가 사용 가능합니다.

### 3단계: 애플리케이션 테스트

1. 브라우저에서 `http://localhost:8080` 열기
2. 최대 10MB 크기의 JPEG, PNG, GIF, WebP형 선명한 반려동물 사진 선택
3. "이미지 분석" 클릭 후 반려동물 설명 대기
4. 성공적인 분석 후 "이야기 생성" 클릭
5. 이야기를 확인하고 결과 페이지 링크로 업로드 폼으로 돌아가기

성공적인 사진-이야기 흐름은 버튼마다 한 번씩 두 번 모델을 호출합니다. 실시간 추론은 배포의 할당량을 소비하며 비용이 발생할 수 있으니, 속도 제한된 배포 공유 시 연속으로 연기 테스트를 실행하십시오. 홈 페이지 로딩은 모델 호출을 하지 않습니다.

## 오프라인 테스트

샘플 디렉터리에서 다음 실행:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java)는 루프백 HTTP 픽스처로 실제 OpenAI SDK 요청을 캡처합니다. 두 요청의 배포, `reasoning_effort: none`, 토큰 제한, 이미지 페이로드, 입력 검증, 빈 응답 및 업스트림 오류를 검사합니다.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java)는 모킹된 모델 서비스와 MockMvc를 사용해 Thymeleaf 페이지, 업로드 계약, CSRF, 검증, 출력 이스케이프, 가시적 실패를 테스트합니다. 이 테스트는 Azure 자격증명이 필요 없으며 비용이 발생하는 Azure 추론을 호출하지 않습니다. Maven은 보고서를 `target/surefire-reports`에 작성합니다.

## 전체 동작 과정

반려동물 이야기를 생성할 때의 전체 흐름은 다음과 같습니다:

1. **사진 선택**: 업로드 폼에서 반려동물 사진 선택
2. **이미지 업로드**: "이미지 분석" 버튼이 CSRF 헤더가 포함된 multipart POST를 `/analyze-image`에 전송
3. **이미지 분석**: `StoryService`가 reasoning을 `none`으로 설정하여 이미지를 GPT-5.6 Luna에 전송
4. **설명 표시**: 브라우저가 반환된 설명을 표시하고 폼에 저장
5. **이야기 제출**: "이야기 생성"이 `description`과 `_csrf`를 `/generate-story`에 POST
6. **이야기 생성**: 컨트롤러가 설명을 검증하고 같은 배포를 reasoning `none`으로 호출
7. **템플릿 렌더링**: 타임리프가 결과 페이지에 설명과 이야기를 이스케이프하여 표시

**오류 처리 흐름:**
모델 실패 시 서버가 원인을 로깅합니다. 이미지 분석 중 실패하면 HTTP 502를 반환하며, 브라우저는 오류를 표시하고 "이야기 생성" 버튼을 숨깁니다. 이야기 생성 실패 시 오류 메시지와 함께 폼으로 리다이렉트됩니다. 어느 경로도 미리 작성된 결과를 조용히 대신하지 않습니다.

## AI 통합 이해

### Azure AI Foundry (키리스)
서비스는 리소스의 `/openai/v1/` 엔드포인트로 SDK를 구성합니다. `DefaultAzureCredential`과 `AuthenticationUtil.getBearerTokenSupplier`는 Microsoft Entra 토큰을 `https://ai.azure.com/.default`용으로 제공합니다. 로컬 개발은 Azure CLI 로그인 사용, Azure 호스팅 앱은 필요한 리소스 권한이 있는 관리 ID 사용 가능.

### 프롬프트 엔지니어링
이미지 분석은 짧은 단락으로 관찰된 반려동물 특징을 요청하며, 모델에 이미지 내 텍스트를 명령이 아닌 데이터로 처리하라고 지시합니다. 이야기 생성은 반환된 설명을 별도의 가족 친화적 글쓰기 요청에 사용합니다. 두 호출 모두 추론을 활성화하지 않고 온도 오버라이드를 설정하지 않습니다.

### 응답 처리
공통 응답 핸들러는 누락된 선택지와 빈 또는 공백만 있는 콘텐츠를 거부하고, 유효한 콘텐츠를 트리밍하며 업스트림 실패를 보존합니다. 이미지 설명은 이후 이야기 폼에 맞게 1000자로 제한합니다. 원래 모델 실패는 진단용으로 유지하지만 사용자에게는 렌더링하지 않습니다.

## 다음 단계

추가 예제는 [4장: 실용 샘플](../README.md)을 참조하십시오.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문가의 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->