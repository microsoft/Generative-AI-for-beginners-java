<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:49:59+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "ko"
}
-->
# Java를 위한 생성형 AI 개발 환경 설정

> **빠른 시작**: 클라우드에서 2분 만에 코딩 시작 - [GitHub Codespaces 설정](../../../02-SetupDevEnvironment)으로 이동하세요 - 로컬 설치가 필요 없으며 GitHub 모델을 사용합니다!

> **Azure OpenAI에 관심 있으신가요?** [Azure OpenAI 설정 가이드](getting-started-azure-openai.md)를 확인하여 새로운 Azure OpenAI 리소스를 생성하는 단계를 알아보세요.

## 학습 목표

- AI 애플리케이션을 위한 Java 개발 환경 설정
- 선호하는 개발 환경 선택 및 구성 (Codespaces를 활용한 클라우드 우선 방식, 로컬 개발 컨테이너, 또는 완전한 로컬 설정)
- GitHub 모델에 연결하여 설정 테스트

## 목차

- [학습 목표](../../../02-SetupDevEnvironment)
- [소개](../../../02-SetupDevEnvironment)
- [1단계: 개발 환경 설정](../../../02-SetupDevEnvironment)
  - [옵션 A: GitHub Codespaces (추천)](../../../02-SetupDevEnvironment)
  - [옵션 B: 로컬 개발 컨테이너](../../../02-SetupDevEnvironment)
  - [옵션 C: 기존 로컬 설치 사용](../../../02-SetupDevEnvironment)
- [2단계: GitHub 개인 액세스 토큰 생성](../../../02-SetupDevEnvironment)
- [3단계: 설정 테스트](../../../02-SetupDevEnvironment)
- [문제 해결](../../../02-SetupDevEnvironment)
- [요약](../../../02-SetupDevEnvironment)
- [다음 단계](../../../02-SetupDevEnvironment)

## 소개

이 장에서는 개발 환경 설정 방법을 안내합니다. **GitHub 모델**을 주요 예제로 사용하며, 이는 무료이고 GitHub 계정만 있으면 간단히 설정할 수 있으며 신용카드가 필요 없고 여러 모델을 실험할 수 있는 접근성을 제공합니다.

**로컬 설정이 필요 없습니다!** 브라우저에서 완전한 개발 환경을 제공하는 GitHub Codespaces를 사용하여 즉시 코딩을 시작할 수 있습니다.

<img src="./images/models.webp" alt="스크린샷: GitHub 모델" width="50%">

이 과정에서는 [**GitHub 모델**](https://github.com/marketplace?type=models)을 사용하는 것을 추천합니다. 이유는 다음과 같습니다:
- **무료**로 시작 가능
- **간단한** 설정 (GitHub 계정만 필요)
- **신용카드 불필요**
- **다양한 모델**을 실험 가능

> **참고**: 이 교육에서 사용하는 GitHub 모델은 다음과 같은 무료 제한이 있습니다:
> - 분당 15회 요청 (하루 150회)
> - 요청당 약 8,000 단어 입력, 약 4,000 단어 출력
> - 동시 요청 5회
> 
> 프로덕션 사용을 위해 Azure AI Foundry 모델로 업그레이드하세요. 코드 변경 없이 Azure 계정을 사용하여 전환할 수 있습니다. [Azure AI Foundry 문서](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models)를 참조하세요.

## 1단계: 개발 환경 설정

<a name="quick-start-cloud"></a>

이 Java 생성형 AI 과정에 필요한 모든 도구를 포함한 사전 구성된 개발 컨테이너를 준비했습니다. 선호하는 개발 방식을 선택하세요:

### 환경 설정 옵션:

#### 옵션 A: GitHub Codespaces (추천)

**2분 만에 코딩 시작 - 로컬 설정 불필요!**

1. 이 저장소를 GitHub 계정으로 포크하세요.
   > **참고**: 기본 설정을 편집하려면 [Dev 컨테이너 구성](../../../.devcontainer/devcontainer.json)을 확인하세요.
2. **Code** → **Codespaces** 탭 → **...** → **New with options...**를 클릭하세요.
3. 기본값을 사용하세요 – 이 과정에 맞춘 **Generative AI Java Development Environment** 커스텀 devcontainer가 선택됩니다.
4. **Create codespace**를 클릭하세요.
5. 환경이 준비될 때까지 약 2분 기다리세요.
6. [2단계: GitHub 토큰 생성](../../../02-SetupDevEnvironment)으로 진행하세요.

<img src="./images/codespaces.png" alt="스크린샷: Codespaces 서브메뉴" width="50%">

<img src="./images/image.png" alt="스크린샷: 옵션으로 새로 만들기" width="50%">

<img src="./images/codespaces-create.png" alt="스크린샷: Codespace 생성 옵션" width="50%">

> **Codespaces의 장점**:
> - 로컬 설치 불필요
> - 브라우저가 있는 모든 기기에서 작동
> - 모든 도구와 종속성이 사전 구성됨
> - 개인 계정에 대해 월 60시간 무료
> - 모든 학습자에게 일관된 환경 제공

#### 옵션 B: 로컬 개발 컨테이너

**Docker를 활용한 로컬 개발을 선호하는 개발자를 위한 옵션**

1. 이 저장소를 로컬 머신으로 포크하고 클론하세요.
   > **참고**: 기본 설정을 편집하려면 [Dev 컨테이너 구성](../../../.devcontainer/devcontainer.json)을 확인하세요.
2. [Docker Desktop](https://www.docker.com/products/docker-desktop)과 [VS Code](https://code.visualstudio.com/)를 설치하세요.
3. VS Code에서 [Dev Containers 확장](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)을 설치하세요.
4. VS Code에서 저장소 폴더를 엽니다.
5. 메시지가 표시되면 **Reopen in Container**를 클릭하세요 (또는 `Ctrl+Shift+P` → "Dev Containers: Reopen in Container"를 사용).
6. 컨테이너가 빌드되고 시작될 때까지 기다리세요.
7. [2단계: GitHub 토큰 생성](../../../02-SetupDevEnvironment)으로 진행하세요.

<img src="./images/devcontainer.png" alt="스크린샷: Dev 컨테이너 설정" width="50%">

<img src="./images/image-3.png" alt="스크린샷: Dev 컨테이너 빌드 완료" width="50%">

#### 옵션 C: 기존 로컬 설치 사용

**기존 Java 환경을 사용하는 개발자를 위한 옵션**

필수 조건:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) 또는 선호하는 IDE

단계:
1. 이 저장소를 로컬 머신으로 클론하세요.
2. 프로젝트를 IDE에서 엽니다.
3. [2단계: GitHub 토큰 생성](../../../02-SetupDevEnvironment)으로 진행하세요.

> **프로 팁**: 저사양 머신을 사용하면서 VS Code를 로컬로 사용하고 싶다면 GitHub Codespaces를 활용하세요! 로컬 VS Code를 클라우드 호스팅 Codespace에 연결하여 두 가지 장점을 모두 누릴 수 있습니다.

<img src="./images/image-2.png" alt="스크린샷: 로컬 devcontainer 인스턴스 생성 완료" width="50%">

## 2단계: GitHub 개인 액세스 토큰 생성

1. [GitHub 설정](https://github.com/settings/profile)으로 이동하여 프로필 메뉴에서 **Settings**를 선택하세요.
2. 왼쪽 사이드바에서 **Developer settings**를 클릭하세요 (보통 하단에 위치).
3. **Personal access tokens** 아래에서 **Fine-grained tokens**를 클릭하세요 (또는 이 [링크](https://github.com/settings/personal-access-tokens)를 따르세요).
4. **Generate new token**을 클릭하세요.
5. "Token name"에 설명이 포함된 이름을 입력하세요 (예: `GenAI-Java-Course-Token`).
6. 만료 날짜를 설정하세요 (보안 모범 사례로 7일 추천).
7. "Resource owner"에서 사용자 계정을 선택하세요.
8. "Repository access"에서 GitHub 모델을 사용할 저장소를 선택하세요 (필요한 경우 "All repositories").
9. "Repository permissions"에서 **Models**를 찾아 **Read and write**로 설정하세요.
10. **Generate token**을 클릭하세요.
11. **지금 토큰을 복사하고 저장하세요** – 다시 볼 수 없습니다!

> **보안 팁**: 최소한의 필요한 범위와 가장 짧은 실용적인 만료 시간을 사용하세요.

## 3단계: GitHub 모델 예제로 설정 테스트

개발 환경이 준비되면 [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models)에 있는 예제 애플리케이션으로 GitHub 모델 통합을 테스트해 봅시다.

1. 개발 환경에서 터미널을 엽니다.
2. GitHub 모델 예제로 이동하세요:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. GitHub 토큰을 환경 변수로 설정하세요:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. 애플리케이션을 실행하세요:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

다음과 유사한 출력이 표시됩니다:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### 예제 코드 이해하기

먼저 실행한 내용을 이해해 봅시다. `examples/github-models` 아래의 예제는 OpenAI Java SDK를 사용하여 GitHub 모델에 연결합니다:

**이 코드가 하는 일:**
- 개인 액세스 토큰을 사용하여 GitHub 모델에 **연결**
- AI 모델에 간단한 "Say Hello World!" 메시지를 **전송**
- AI의 응답을 **수신**하고 표시
- 설정이 올바르게 작동하는지 **검증**

**핵심 종속성** (`pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**주요 코드** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## 요약

축하합니다! 이제 모든 설정이 완료되었습니다:

- AI 모델 액세스를 위한 올바른 권한으로 GitHub 개인 액세스 토큰 생성
- Codespaces, 개발 컨테이너, 또는 로컬 설정을 통해 Java 개발 환경 실행
- OpenAI Java SDK를 사용하여 GitHub 모델에 연결하여 무료 AI 개발
- AI 모델과 대화하는 간단한 예제를 통해 모든 것이 작동하는지 테스트

## 다음 단계

[3장: 생성형 AI 핵심 기술](../03-CoreGenerativeAITechniques/README.md)

## 문제 해결

문제가 발생했나요? 다음은 일반적인 문제와 해결 방법입니다:

- **토큰이 작동하지 않나요?** 
  - 토큰 전체를 복사했는지 확인하세요 (공백 없이)
  - 환경 변수로 올바르게 설정되었는지 확인하세요
  - 토큰에 올바른 권한이 있는지 확인하세요 (Models: Read and write)

- **Maven을 찾을 수 없나요?** 
  - Dev 컨테이너/Codespaces를 사용하는 경우 Maven이 사전 설치되어 있어야 합니다.
  - 로컬 설정의 경우 Java 21+ 및 Maven 3.9+가 설치되어 있는지 확인하세요.
  - `mvn --version`을 실행하여 설치를 확인하세요.

- **연결 문제?** 
  - 인터넷 연결을 확인하세요.
  - GitHub이 네트워크에서 접근 가능한지 확인하세요.
  - GitHub 모델 엔드포인트를 차단하는 방화벽이 없는지 확인하세요.

- **Dev 컨테이너가 시작되지 않나요?** 
  - Docker Desktop이 실행 중인지 확인하세요 (로컬 개발의 경우).
  - 컨테이너를 다시 빌드해 보세요: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **애플리케이션 컴파일 오류?**
  - 올바른 디렉토리에 있는지 확인하세요: `02-SetupDevEnvironment/examples/github-models`
  - 클린 및 재빌드 시도: `mvn clean compile`

> **도움이 필요하신가요?**: 여전히 문제가 있나요? 저장소에 이슈를 열어주시면 도움을 드리겠습니다.

**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있습니다. 원본 문서의 원어 버전을 신뢰할 수 있는 권위 있는 자료로 간주해야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.