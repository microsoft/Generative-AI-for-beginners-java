# Azure AI Foundry를 사용한 기본 채팅 - 엔드 투 엔드 예제

이 예제는 **Azure AI Foundry** 모델에 **키 없는 인증**(Microsoft Entra ID)으로 연결하고 설정을 테스트하는 간단한 Spring Boot 애플리케이션입니다. 공식 OpenAI Java SDK와 **Azure OpenAI v1** 엔드포인트를 백업으로 사용하는 Spring AI의 `ChatClient`를 유지합니다.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)에 명시된 버전은 Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, dotenv-java <strong>3.2.0</strong>입니다. 샘플은 `spring-ai-starter-model-openai`를 사용하며 `openai-java`와 `azure-identity`를 명시적으로 선언합니다; Spring AI 2는 이전의 Azure OpenAI 스타터를 제거했습니다.

## 목차

- [사전 준비사항](#사전-준비사항)
- [빠른 시작](#빠른-시작)
- [인증 작동 방식](#인증-작동-방식)
- [애플리케이션 실행](#애플리케이션-실행)
  - [Maven 사용](#maven-사용)
  - [VS Code 사용](#vs-code-사용)
  - [예상 출력](#예상-출력)
- [구성 참조](#구성-참조)
  - [환경 변수](#환경-변수)
  - [Spring 구성](#spring-구성)
- [문제 해결](#문제-해결)
  - [일반 문제](#일반-문제)
  - [디버그 모드](#디버그-모드)
- [다음 단계](#다음-단계)
- [참고 자료](#참고-자료)

## 사전 준비사항

이 예제를 실행하기 전에 다음을 확인하세요:

- `gpt-5.6-luna` 배포가 포함된 Azure AI Foundry 리소스 - `azd up`으로 프로비저닝하거나 [Azure AI Foundry 설정 가이드](../../getting-started-azure-openai.md)를 통해 수동 생성
- 해당 리소스에 대한 **Cognitive Services OpenAI User** 역할 (Bicep 템플릿에서 자동 할당)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login`으로 로그인됨
- Java 21 이상 및 Maven 3.9 이상

> **API 키 필요 없음** — 인증은 Microsoft Entra ID를 통한 키 없는 방식입니다.

## 빠른 시작

```bash
# 1. 프로젝트로 이동
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. 키리스 인증에서 토큰을 받을 수 있도록 로그인하세요
az login

# 3. 엔드포인트를 구성하세요
#    - `azd up` 명령어를 실행했다면, .env가 자동으로 작성되어 있습니다 (이 단계는 건너뛰세요).
#    - 그렇지 않으면 템플릿을 복사하고 AZURE_OPENAI_ENDPOINT를 설정하세요.
cp .env.example .env

# 4. 애플리케이션을 실행하세요
mvn spring-boot:run
```

## 인증 작동 방식

이 예제는 <strong>Microsoft Entra ID</strong>로 인증하며 API 키는 없습니다.

애플리케이션은 [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java)에서 인증을 명시적으로 구성합니다:

1. `azureCredential()`은 `DefaultAzureCredential`과 `https://ai.azure.com/.default` 범위를 사용하여 `AuthenticationUtil.getBearerTokenSupplier`를 통해 `BearerTokenCredential`을 생성합니다.
2. `azureOpenAiClient()`는 `OpenAIOkHttpClient.builder()`로 `OpenAIClient`를 구성하고 리소스 엔드포인트를 `/openai/v1`으로 해결하며 `.credential(...)`로 베어러 자격 증명을 제공합니다.
3. `azureChatModel()`은 해당 클라이언트를 Spring AI의 `OpenAiChatModel`에 공급하며, 이는 이 강의의 `ChatClient`를 지원합니다.

이 명시적 빈들은 글로벌 `OPENAI_API_KEY`가 Azure 인증을 덮어쓰는 것을 방지합니다. YAML에서 API 키를 생략하는 것만으로는 인증 설정이 되지 않습니다. `DefaultAzureCredential`은 로컬에서 `az login` 세션이나 Azure의 관리 ID를 사용할 수 있으며, 선택된 ID는 반드시 위에 명시된 리소스 역할을 가져야 합니다.

## 애플리케이션 실행

### Maven 사용

```bash
mvn spring-boot:run
```

### VS Code 사용

1. VS Code에서 프로젝트를 엽니다
2. `F5`를 누르거나 "실행 및 디버그" 패널을 사용합니다
3. "Spring Boot-BasicChatApplication" 구성을 선택합니다

> <strong>참고</strong>: 애플리케이션은 작업 디렉터리에서 `.env`를 로드하며, VS Code에서 실행할 때도 마찬가지입니다.

### 예상 출력

성공적으로 실행한 후 예시 출력 (시작 로그 생략; 응답 문구는 다를 수 있음):

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

## 구성 참조

### 환경 변수

| 변수 | 설명 | 필수 | 예제 |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) 엔드포인트 URL | 예 | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | 채팅 모델 배포 이름 | 아니오 | `gpt-5.6-luna` (기본값) |

> API 키 변수는 <strong>없음</strong> — 인증은 키 없는 방식( `az login`을 사용하는 Microsoft Entra ID)입니다.

### Spring 구성

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 설정은 `spring.ai.openai` 접두사와 평탄화된 채팅 속성( `options` 블록 없음)을 사용합니다:

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

`model`은 <strong>Azure 배포 이름</strong>입니다. 인증은 위에 설명한 명시적 빈에서 오며, `api-key` 설정이 아닙니다. 이 강의에서는 추론을 비활성화하고 완료 토큰을 500으로 제한하며, `temperature`와 구식 `max-tokens`는 설정하지 않습니다.

Microsoft는 [새 애플리케이션용 공식 OpenAI SDK와 Azure OpenAI v1 및 Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)를 권장합니다. Chat Completions는 이 기존 메시지 기반 강의를 위해 계속 지원됩니다. GPT-5.6의 경우 도구가 포함된 Chat Completions 요청은 `reasoning_effort`를 `none`으로 설정해야 하며, 도구와 추론을 결합할 때는 Responses를 사용하세요. [추론 모델에서 도구 호출 참조](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## 문제 해결

### 일반 문제

<details>
<summary><strong>오류: 401 / "PermissionDenied" / 토큰 오류</strong></summary>

- `az login` 실행 — 키 없는 인증은 토큰을 얻기 위한 활성 로그인 필요
- 계정에 해당 리소스의 **Cognitive Services OpenAI User** 역할이 있는지 확인
- 역할을 방금 할당했다면 전파될 때까지 1분 정도 기다림
- 올바른 테넌트/구독에 있는지 확인 (`az account show`)
</details>

<details>
<summary><strong>오류: "엔드포인트가 유효하지 않음" / 연결 오류</strong></summary>

- `AZURE_OPENAI_ENDPOINT`가 전체 기본 URL인지 확인 (예: `https://your-resource.openai.azure.com/`)
- 슬래시 끝이 일관성 있는지 확인
- 엔드포인트가 프로비저닝된 리소스와 일치하는지 확인 (`azd env get-values`)
</details>

<details>
<summary><strong>오류: "배포를 찾을 수 없음"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT`가 Azure 배포 이름과 일치하는지 확인
- 모델이 성공적으로 배포되고 활성 상태인지 확인
- 기본 배포 이름은 `gpt-5.6-luna`입니다
</details>

<details>
<summary><strong>오류: 429 / 요청 한도 초과</strong></summary>

- 기본 GPT-5.6 Luna 배포는 글로벌 표준 용량 10: 분당 10 요청, 분당 10,000 토큰
- 예제를 순차적으로 실행하고 서비스의 재시도 간격을 기다린 후 다시 시도하세요
- 이 기본 예제는 자동 SDK 재시도를 비활성화하여 실패한 요청이 바로 보고됩니다
</details>

<details>
<summary><strong>VS Code: 환경 변수가 로드되지 않음</strong></summary>

- `.env` 파일이 프로젝트 루트 디렉터리( `pom.xml`과 같은 수준)에 있는지 확인
- VS Code 통합 터미널에서 `mvn spring-boot:run` 실행 시도
- VS Code Java 확장 기능이 제대로 설치되었는지 확인
</details>

### 디버그 모드

자세한 로깅을 활성화하려면 [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)에서 다음 줄의 주석을 해제하세요:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## 다음 단계

**설정 완료!** 학습 여정을 계속 진행하세요:

[3장: 핵심 생성 AI 기술](../../../03-CoreGenerativeAITechniques/README.md)

## 참고 자료

- [Spring AI 2 OpenAI Java SDK 전환](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1과 함께하는 공식 OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID를 사용하는 키 없는 인증](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 포털](https://ai.azure.com/)
- [Azure AI Foundry 문서](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문가의 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->