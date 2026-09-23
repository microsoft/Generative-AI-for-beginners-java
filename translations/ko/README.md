# 초보자를 위한 생성 AI - 자바 에디션
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![초보자를 위한 생성 AI - 자바 에디션](../../translated_images/ko/beg-genai-series.8b48be9951cc574c.webp)

**소요 시간**: 전체 워크숍은 로컬 설치 없이 온라인으로 완료할 수 있습니다. 환경 설정은 2분 정도 소요되며, 샘플 탐색에는 탐색 깊이에 따라 1~3시간이 필요합니다.

> **빠른 시작** 

1. 이 저장소를 GitHub 계정으로 포크합니다.
2. **Code** → **Codespaces** 탭 → **...** → **옵션 선택 후 새로 만들기...** 클릭
3. 기본 설정 사용 – 이 과정용으로 만들어진 개발 컨테이너가 선택됩니다.
4. **codespace 만들기** 클릭
5. 환경 준비까지 약 2분 대기
6. 바로 [2장: Azure AI Foundry 프로비저닝](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)으로 이동

## 다국어 지원

### GitHub Action을 통한 지원 (자동 및 항상 최신 상태)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[아랍어](../ar/README.md) | [벵골어](../bn/README.md) | [불가리아어](../bg/README.md) | [버마어 (미얀마)](../my/README.md) | [중국어 (간체)](../zh-CN/README.md) | [중국어 (번체, 홍콩)](../zh-HK/README.md) | [중국어 (번체, 마카오)](../zh-MO/README.md) | [중국어 (번체, 대만)](../zh-TW/README.md) | [크로아티아어](../hr/README.md) | [체코어](../cs/README.md) | [덴마크어](../da/README.md) | [네덜란드어](../nl/README.md) | [에스토니아어](../et/README.md) | [핀란드어](../fi/README.md) | [프랑스어](../fr/README.md) | [독일어](../de/README.md) | [그리스어](../el/README.md) | [히브리어](../he/README.md) | [힌디어](../hi/README.md) | [헝가리어](../hu/README.md) | [인도네시아어](../id/README.md) | [이탈리아어](../it/README.md) | [일본어](../ja/README.md) | [칸나다어](../kn/README.md) | [크메르어](../km/README.md) | [한국어](./README.md) | [리투아니아어](../lt/README.md) | [말레이어](../ms/README.md) | [말라얄람어](../ml/README.md) | [마라티어](../mr/README.md) | [네팔어](../ne/README.md) | [나이지리아 피진](../pcm/README.md) | [노르웨이어](../no/README.md) | [페르시아어 (파르시)](../fa/README.md) | [폴란드어](../pl/README.md) | [포르투갈어 (브라질)](../pt-BR/README.md) | [포르투갈어 (포르투갈)](../pt-PT/README.md) | [펀자브어 (구르무키)](../pa/README.md) | [루마니아어](../ro/README.md) | [러시아어](../ru/README.md) | [세르비아어 (키릴)](../sr/README.md) | [슬로바키아어](../sk/README.md) | [슬로베니아어](../sl/README.md) | [스페인어](../es/README.md) | [스와힐리어](../sw/README.md) | [스웨덴어](../sv/README.md) | [따갈로그어 (필리핀)](../tl/README.md) | [타밀어](../ta/README.md) | [텔루구어](../te/README.md) | [태국어](../th/README.md) | [터키어](../tr/README.md) | [우크라이나어](../uk/README.md) | [우르두어](../ur/README.md) | [베트남어](../vi/README.md)

> **로컬에 클론하기를 선호하나요?**
>
> 이 저장소는 50개 이상의 언어 번역을 포함하여 다운로드 크기가 크게 증가합니다. 번역 없이 클론하려면 스패어 체크아웃을 사용하세요:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> 이렇게 하면 훨씬 빠른 다운로드로 코스를 완료하는 데 필요한 모든 것을 얻을 수 있습니다.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## 과정 구조 및 학습 경로

### **1장: 생성 AI 소개**
- **핵심 개념**: 대형 언어 모델, 토큰, 임베딩 및 AI 기능 이해
- **자바 AI 생태계**: Spring AI와 OpenAI SDK 개관
- **모델 컨텍스트 프로토콜**: MCP 소개 및 AI 에이전트 통신에서의 역할
- **실용적 응용**: 채팅봇 및 콘텐츠 생성 등 실제 시나리오
- **[→ 1장 시작](./01-IntroToGenAI/README.md)**

### **2장: 개발 환경 설정**
- **Azure AI Foundry**: Bicep 및 Azure Developer CLI(azd)를 사용하여 GPT-5.6 Luna 채팅과 text-embedding-3-small 임베딩 프로비저닝
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: 공식 OpenAI 자바 SDK 및 Azure OpenAI v1 기반 `ChatClient` 학습
- **키리스 인증**: 관리할 API 키 없이 Microsoft Entra ID로 안전하게 연결
- **개발 도구**: Docker 컨테이너, VS 코드 및 GitHub Codespaces 설정
- **[→ 2장 시작](./02-SetupDevEnvironment/README.md)**

### **3장: 핵심 생성 AI 기법**
- **공식 OpenAI 자바 SDK**: 키리스 인증으로 Azure OpenAI v1 직통 호출
- **프롬프트 엔지니어링**: 최적의 AI 모델 응답 기법
- **임베딩 및 벡터 연산**: 의미 검색 및 유사도 매칭 구현
- **검색 보강 생성(RAG)**: AI를 자체 데이터 소스와 결합
- **함수 호출**: 맞춤 도구 및 플러그인으로 AI 기능 확장
- **[→ 3장 시작](./03-CoreGenerativeAITechniques/README.md)**

### **4장: 실용적 응용 및 프로젝트**
- **애완동물 이야기 생성기** (`petstory/`): Azure AI Foundry와 창의적 콘텐츠 생성
- **Foundry 로컬 데모** (`foundrylocal/`): OpenAI 자바 SDK와 로컬 AI 모델 통합
- **MCP 계산기 서비스** (`calculator/`): Spring AI를 사용한 기본 모델 컨텍스트 프로토콜 구현
- **[→ 4장 시작](./04-PracticalSamples/README.md)**

### **5장: 책임 있는 AI 개발**
- **Azure AI Foundry 콘텐츠 안전**: 내장된 콘텐츠 필터링 및 안전 메커니즘 테스트 (엄격 차단 및 부드러운 거부)
- **책임 있는 AI 데모**: 현대 AI 안전 시스템의 실습 예제
- **모범 사례**: 윤리적 AI 개발 및 배포를 위한 필수 지침
- **[→ 5장 시작](./05-ResponsibleGenAI/README.md)**

## 추가 자료

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j 초보자용](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js 초보자용](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain 초보자용](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![초보자를 위한 AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 AI 에이전트](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 생성 AI 시리즈
[![초보자를 위한 생성 AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![생성 AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![생성 AI (자바)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![생성 AI (자바스크립트)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 핵심 학습
[![초보자를 위한 ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 데이터 과학](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 사이버보안](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![초보자를 위한 웹 개발](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![초보자를 위한 XR 개발](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 코파일럿 시리즈
[![AI 페어 프로그래밍용 코파일럿](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET용 코파일럿](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![코파일럿 어드벤처](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## 도움 받기

AI 앱 개발 중에 막히거나 질문이 있으면 MCP에 대해 배우는 동료 학습자와 경험 많은 개발자들이 모인 커뮤니티에 참여하세요. 질문을 환영하고 지식을 자유롭게 나누는 지원 환경입니다.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

제품 피드백이나 빌드 중 오류가 있으면 다음을 방문하세요:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 기하기 위해 노력하고 있으나, 자동 번역은 오류나 부정확한 부분이 있을 수 있음을 유의하시기 바랍니다. 원본 문서의 원어본이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문가의 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->