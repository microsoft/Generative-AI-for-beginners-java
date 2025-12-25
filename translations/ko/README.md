<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:18:33+00:00",
  "source_file": "README.md",
  "language_code": "ko"
}
-->
# 초보자를 위한 생성형 AI - Java 에디션
[![Microsoft Foundry 디스코드](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![초보자를 위한 생성형 AI - Java 에디션](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ko.png)

**소요 시간**: 전체 워크숍은 로컬 설치 없이 온라인에서 완료할 수 있습니다. 환경 설정에는 2분 정도가 걸리며, 샘플을 탐색하는 데는 탐색 깊이에 따라 1-3시간이 필요합니다.

> **빠른 시작** 

1. 이 저장소를 GitHub 계정으로 포크하세요
2. 클릭 **Code** → **Codespaces** 탭 → **...** → **New with options...**
3. 기본값을 사용하세요 – 이렇게 하면 이 과정용으로 생성된 Development 컨테이너가 선택됩니다
4. 클릭 **Create codespace**
5. 환경이 준비될 때까지 약 2분 정도 기다리세요
6. 바로 [첫 번째 예제](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)로 이동

> **로컬로 복제하시겠습니까?**
>
> 이 저장소는 50개 이상의 언어 번역을 포함하고 있어 다운로드 크기가 크게 증가합니다. 번역 없이 복제하려면 sparse checkout을 사용하세요:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> 이렇게 하면 과정을 완료하는 데 필요한 모든 것을 훨씬 더 빠르게 얻을 수 있습니다.


## 다국어 지원

### GitHub Action으로 지원됨 (자동화 및 항상 최신 상태)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[아랍어](../ar/README.md) | [벵골어](../bn/README.md) | [불가리아어](../bg/README.md) | [버마어 (미얀마)](../my/README.md) | [중국어 (간체)](../zh/README.md) | [중국어 (번체, 홍콩)](../hk/README.md) | [중국어 (번체, 마카오)](../mo/README.md) | [중국어 (번체, 대만)](../tw/README.md) | [크로아티아어](../hr/README.md) | [체코어](../cs/README.md) | [덴마크어](../da/README.md) | [네덜란드어](../nl/README.md) | [에스토니아어](../et/README.md) | [핀란드어](../fi/README.md) | [프랑스어](../fr/README.md) | [독일어](../de/README.md) | [그리스어](../el/README.md) | [히브리어](../he/README.md) | [힌디어](../hi/README.md) | [헝가리어](../hu/README.md) | [인도네시아어](../id/README.md) | [이탈리아어](../it/README.md) | [일본어](../ja/README.md) | [칸나다어](../kn/README.md) | [한국어](./README.md) | [리투아니아어](../lt/README.md) | [말레이어](../ms/README.md) | [말라얄람어](../ml/README.md) | [마라티어](../mr/README.md) | [네팔어](../ne/README.md) | [나이지리아 피진어](../pcm/README.md) | [노르웨이어](../no/README.md) | [페르시아어 (파르시)](../fa/README.md) | [폴란드어](../pl/README.md) | [포르투갈어 (브라질)](../br/README.md) | [포르투갈어 (포르투갈)](../pt/README.md) | [펀자브어 (구르무키)](../pa/README.md) | [루마니아어](../ro/README.md) | [러시아어](../ru/README.md) | [세르비아어 (키릴 문자)](../sr/README.md) | [슬로바키아어](../sk/README.md) | [슬로베니아어](../sl/README.md) | [스페인어](../es/README.md) | [스와힐리어](../sw/README.md) | [스웨덴어](../sv/README.md) | [타갈로그어 (필리핀)](../tl/README.md) | [타밀어](../ta/README.md) | [텔루구어](../te/README.md) | [태국어](../th/README.md) | [터키어](../tr/README.md) | [우크라이나어](../uk/README.md) | [우르두어](../ur/README.md) | [베트남어](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## 강의 구조 및 학습 경로

### **챕터 1: 생성형 AI 소개**
- **핵심 개념**: 대형 언어 모델, 토큰, 임베딩, 및 AI 기능 이해
- **Java AI 생태계**: Spring AI 및 OpenAI SDK 개요
- **모델 컨텍스트 프로토콜**: MCP 소개와 AI 에이전트 통신에서의 역할
- **실무 적용 사례**: 챗봇 및 콘텐츠 생성 등 실제 시나리오
- **[→ 챕터 1 시작](./01-IntroToGenAI/README.md)**

### **챕터 2: 개발 환경 설정**
- **다중 제공자 구성**: GitHub Models, Azure OpenAI 및 OpenAI Java SDK 통합 설정
- **Spring Boot + Spring AI**: 엔터프라이즈 AI 애플리케이션 개발을 위한 모범 사례
- **GitHub Models**: 프로토타이핑 및 학습을 위한 무료 AI 모델 액세스(신용카드 불필요)
- **개발 도구**: Docker 컨테이너, VS Code 및 GitHub Codespaces 구성
- **[→ 챕터 2 시작](./02-SetupDevEnvironment/README.md)**

### **챕터 3: 핵심 생성형 AI 기법**
- **프롬프트 엔지니어링**: AI 모델의 최적 응답을 위한 기법
- **임베딩 및 벡터 연산**: 의미 검색 및 유사도 매칭 구현
- **Retrieval-Augmented Generation (RAG)**: AI와 자체 데이터 소스 결합
- **함수 호출(Function Calling)**: 커스텀 도구 및 플러그인으로 AI 기능 확장
- **[→ 챕터 3 시작](./03-CoreGenerativeAITechniques/README.md)**

### **챕터 4: 실습 응용 및 프로젝트**
- **Pet Story Generator** (`petstory/`): GitHub Models로 창의적 콘텐츠 생성
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK를 사용한 로컬 AI 모델 통합
- **MCP Calculator Service** (`calculator/`): Spring AI를 사용한 기본 모델 컨텍스트 프로토콜 구현
- **[→ 챕터 4 시작](./04-PracticalSamples/README.md)**

### **챕터 5: 책임 있는 AI 개발**
- **GitHub Models 안전성**: 내장 콘텐츠 필터링 및 안전 메커니즘(강제 차단 및 소프트 거부) 테스트
- **책임 있는 AI 데모**: 최신 AI 안전 시스템이 실제로 어떻게 작동하는지 보여주는 실습 예제
- **모범 사례**: 윤리적인 AI 개발 및 배포를 위한 필수 지침
- **[→ 챕터 5 시작](./05-ResponsibleGenAI/README.md)**

## 추가 자료

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j 초보자용](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js 초보자용](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / 에이전트
[![AZD 초보자용](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI 초보자용](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP 초보자용](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 에이전트 초보자용](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 생성형 AI 시리즈
[![초보자를 위한 생성형 AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![생성형 AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![생성형 AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![생성형 AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 핵심 학습
[![머신러닝 초보자용](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![데이터 과학 초보자용](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 초보자용](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![사이버보안 초보자용](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![웹 개발 초보자용](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![초보자를 위한 IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 XR 개발](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot 시리즈
[![AI 페어 프로그래밍용 Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET용 Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot 어드벤처](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## 도움받기

AI 앱을 개발하다가 막히거나 궁금한 점이 있으면 MCP에 대해 다른 학습자와 경험 많은 개발자들이 참여하는 토론에 합류하세요. 질문을 환영하고 지식을 자유롭게 나누는 지원 커뮤니티입니다.

[![Microsoft Foundry 디스코드](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

제품 피드백이나 빌드 중 오류가 있으면 다음을 방문하세요:

[![Microsoft Foundry 개발자 포럼](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
면책 사항:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나 자동 번역에는 오류나 부정확성이 포함될 수 있음을 알려드립니다. 원문(원래 언어로 작성된 문서)을 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우 전문적인 인간 번역을 권장합니다. 이 번역의 사용으로 인해 발생하는 오해나 잘못된 해석에 대해서는 당사가 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->