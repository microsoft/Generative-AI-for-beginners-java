# Azure AI Foundry 개발 환경 설정

> 이 가이드는 이 강좌의 Java AI 앱용 **Azure AI Foundry** 모델을 <strong>키리스</strong> 인증(Microsoft Entra ID)을 사용하여 설정합니다 — 관리할 API 키가 없습니다. 도구가 처음이라면 [개발 환경 가이드](./README.md)부터 시작하세요.

이 가이드는 이 강좌의 Java AI 앱을 위해 **Azure AI Foundry** 모델을 설정합니다. 두 가지 방법이 있습니다:

- **옵션 A — `azd` + Bicep로 프로비저닝 (권장):** 한 명령어로 Foundry 계정과 모델을 코드로 배포합니다. 포털 클릭 불필요.
- **옵션 B — Azure AI Foundry 포털에서 리소스를 수동으로 생성합니다.**

두 가지 방법 모두 **키리스 인증**(Microsoft Entra ID)을 사용하므로 복사하거나 노출할 API 키가 없습니다.

## 목차

- [생성되는 항목](#생성되는-항목)
- [필수 조건](#필수-조건)
- [옵션 A: azd + Bicep로 프로비저닝 (권장)](#option-a-provision-with-azd--bicep-recommended)
- [옵션 B: 리소스를 수동으로 생성](#옵션-b-리소스-수동-생성)
- [환경 구성](#환경-구성)
- [설정 테스트](#설정-테스트)
- [다음 단계](#다음-단계)
- [리소스](#리소스)
- [추가 리소스](#추가-리소스)

## 생성되는 항목

[`infra/`](../../../02-SetupDevEnvironment/infra)의 Bicep 템플릿이 다음을 프로비저닝합니다:

- 프로젝트가 포함된 **Azure AI Foundry** 계정(`Microsoft.CognitiveServices/accounts`, 종류 `AIServices`)
- <strong>챗</strong> 배포 - GPT-5.6 Luna (`gpt-5.6-luna`), 버전 `2026-07-09`, `GlobalStandard` 용량 `10` (이 모델에 대해 분당 10 요청 및 분당 10,000 토큰)
- <strong>임베딩</strong> 배포 - `text-embedding-3-small`, 버전 `1` (후속 장에서 사용)
- `az login`으로 로그인할 수 있도록 하는 **키리스 역할 할당** (`Cognitive Services OpenAI User`) — 키 관리 불필요

## 필수 조건

- [Azure 구독](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) 및 [Maven 3.9+](https://maven.apache.org/download.cgi)

## 옵션 A: azd + Bicep로 프로비저닝 (권장)

`02-SetupDevEnvironment` 폴더에서:

```bash
cd 02-SetupDevEnvironment

# 로그인 (두 도구 모두)
azd auth login
az login

# Foundry 계정 및 모델 배포 준비
azd up
```

`azd`가 **환경 이름**(예: `genai-java`), <strong>구독</strong>, <strong>지역</strong>을 묻습니다. 직접 구독을 선택하고 `gpt-5.6-luna`와 `text-embedding-3-small`이 사용 가능한 지역을 선택하세요(예: `eastus2`). 해당 지역에서 모델과 배포 유형에 충분한 할당량이 있는지 확인하세요; 구독마다 가용성과 할당량이 다릅니다.

프로비저닝이 완료되면 azd는:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep)에 정의된 모든 것을 배포합니다.
2. 포스트 프로비저닝 훅을 실행하여 비밀 없이 끝점 및 배포 이름을 포함한 [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure)을 작성합니다.

> **팁:** 변경 사항을 적용하려면 언제든지 `azd up`을 다시 실행하세요. 모든 것을 삭제하고 비용 발생을 중단하려면 `azd down`을 실행하세요.

생성된 설정을 보려면:

```bash
azd env get-values
```

이제 [설정 테스트](#설정-테스트)로 건너뛰세요.

## 옵션 B: 리소스 수동 생성

포털을 선호한다면, 직접 리소스를 생성하세요:

1. [Azure AI Foundry 포털](https://ai.azure.com/)에 접속하여 로그인하세요.
2. **프로젝트 생성** (이 작업은 AI Foundry 리소스도 생성합니다). `GenAIJava` 같은 이름을 지정하세요.
3. 프로젝트에서 **Models + endpoints** → **모델 배포** → <strong>기본 모델 배포</strong>를 엽니다.
4. **GPT-5.6 Luna** (모델 및 배포 이름 `gpt-5.6-luna`, 버전 `2026-07-09`)를 **Global Standard** 용량 `10`으로 배포합니다. 임베딩 예제를 원하면 **text-embedding-3-small**, 버전 `1`도 반복 배포하세요.
5. <strong>개요</strong>에서 <strong>끝점</strong>(예: `https://<resource>.openai.azure.com/`)을 복사하세요.
6. 키리스 액세스 권한 부여: 리소스에서 **액세스 제어 (IAM)** → **역할 할당 추가** → 내 계정에 **Cognitive Services OpenAI User** 역할을 할당하세요.

> **여전히 문제가 있나요?** [Azure AI Foundry 문서](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)를 참고하세요.

## 환경 구성

**옵션 A (`azd up`)를 사용했으면**, 설정 파일이 이미 작성되어 있으므로 구성할 필요가 없습니다. [설정 테스트](#설정-테스트)로 건너뛰세요.

**옵션 B (수동)를 사용했다면**, 예제의 `.env` 파일을 직접 만드세요:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` 파일을 끝점으로 편집하세요 (키 없음 — 인증은 키리스):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

리소스의 Azure OpenAI 끝점을 사용하세요, 프로젝트 URL이 아닙니다. 기본 챗 앱이 이를 `/openai/v1`로 해석하며 명시적인 베어러 토큰 클라이언트를 구성합니다; API 키는 필요하지 않습니다.

> **보안 주의:** 저장할 API 키가 없습니다. Microsoft Entra ID로 `az login`(로컬) 또는 관리 ID(Azure 환경)로 인증합니다. `.env` 파일에는 비밀이 없는 설정만 포함되며 이미 `.gitignore`에 포함되어 있습니다.

## 설정 테스트

키리스 인증이 토큰을 받을 수 있도록 로그인했는지 확인 후 예제를 실행하세요:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # 아직 로그인하지 않은 경우
mvn clean spring-boot:run
```

`gpt-5.6-luna` 모델의 응답이 보여야 합니다. 기본 할당량이 작으므로 예제를 순차적으로 실행하세요; HTTP 429 오류가 발생하면 재시도 간격 후 다시 시도하세요.

> **VS Code 사용자:** `F5` 키를 눌러 실행하세요. 앱이 `.env` 파일을 자동으로 로드합니다.

> **전체 예제:** 자세한 내용 및 문제 해결은 [Azure AI Foundry와 기본 챗 예제](./examples/basic-chat-azure/README.md)를 참고하세요.

## 다음 단계

프로비저닝과 예제 실행이 성공하면 다음이 준비됩니다:
- `gpt-5.6-luna` 및 `text-embedding-3-small`가 배포된 Azure AI Foundry
- 키리스 인증(Microsoft Entra ID) — 관리할 키가 없습니다
- 끝점 및 배포 이름이 포함된 로컬 `.env` 파일
- 사용할 준비가 된 Java 개발 환경

**[3장: 핵심 생성 AI 기법](../03-CoreGenerativeAITechniques/README.md)으로 계속해서 AI 애플리케이션을 빌드하세요!**

## 리소스

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID를 이용한 키리스 인증](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 문서](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2에서 OpenAI Java SDK로 전환](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1용 공식 OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## 추가 리소스

- [VS Code 다운로드](https://code.visualstudio.com/Download)
- [Docker Desktop 받기](https://www.docker.com/products/docker-desktop)
- [개발 컨테이너 구성](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문가의 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->