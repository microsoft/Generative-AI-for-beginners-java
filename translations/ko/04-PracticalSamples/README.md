<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T14:50:30+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "ko"
}
-->
# 실용적인 응용 및 프로젝트

## 학습 내용
이 섹션에서는 Java를 활용한 생성형 AI 개발 패턴을 보여주는 세 가지 실용적인 응용 프로그램을 시연합니다:
- 클라이언트와 서버 측 AI를 결합한 다중 모달 반려동물 이야기 생성기 만들기
- Foundry Local Spring Boot 데모를 통해 로컬 AI 모델 통합 구현
- 계산기 예제를 활용한 Model Context Protocol (MCP) 서비스 개발

## 목차

- [소개](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot 데모](../../../04-PracticalSamples)
  - [반려동물 이야기 생성기](../../../04-PracticalSamples)
  - [MCP 계산기 서비스 (초보자용 MCP 데모)](../../../04-PracticalSamples)
- [학습 진행 단계](../../../04-PracticalSamples)
- [요약](../../../04-PracticalSamples)
- [다음 단계](../../../04-PracticalSamples)

## 소개

이 장에서는 Java를 활용한 생성형 AI 개발 패턴을 보여주는 **샘플 프로젝트**를 소개합니다. 각 프로젝트는 완전한 기능을 갖추고 있으며, 특정 AI 기술, 아키텍처 패턴, 그리고 모범 사례를 시연합니다. 이를 통해 여러분의 응용 프로그램에 적합한 방식을 적용할 수 있습니다.

### Foundry Local Spring Boot 데모

**[Foundry Local Spring Boot 데모](foundrylocal/README.md)**는 **OpenAI Java SDK**를 사용하여 로컬 AI 모델과 통합하는 방법을 보여줍니다. 이 데모는 Foundry Local에서 실행되는 **Phi-3.5-mini** 모델에 연결하여 클라우드 서비스에 의존하지 않고 AI 애플리케이션을 실행할 수 있는 방법을 시연합니다.

### 반려동물 이야기 생성기

**[반려동물 이야기 생성기](petstory/README.md)**는 창의적인 반려동물 이야기를 생성하기 위해 **다중 모달 AI 처리**를 시연하는 흥미로운 Spring Boot 웹 애플리케이션입니다. 이 프로젝트는 브라우저 기반 AI 상호작용을 위한 transformer.js와 서버 측 처리를 위한 OpenAI SDK를 결합하여 클라이언트와 서버 측 AI 기능을 통합합니다.

### MCP 계산기 서비스 (초보자용 MCP 데모)

**[MCP 계산기 서비스](calculator/README.md)**는 Spring AI를 사용하여 **Model Context Protocol (MCP)**을 간단히 시연하는 프로젝트입니다. 초보자도 쉽게 MCP 개념을 이해할 수 있도록 설계되었으며, 기본 MCP 서버를 생성하고 MCP 클라이언트와 상호작용하는 방법을 보여줍니다.

## 학습 진행 단계

이 프로젝트들은 이전 장에서 다룬 개념을 기반으로 설계되었습니다:

1. **기본부터 시작**: Foundry Local Spring Boot 데모를 통해 로컬 모델과의 기본 AI 통합을 이해하세요.
2. **상호작용 추가**: 반려동물 이야기 생성기를 통해 다중 모달 AI와 웹 기반 상호작용을 경험하세요.
3. **MCP 기초 배우기**: MCP 계산기 서비스를 통해 Model Context Protocol의 기본 개념을 익히세요.

## 요약

잘하셨습니다! 이제 다음과 같은 실제 응용 프로그램을 탐구했습니다:

- 브라우저와 서버에서 모두 작동하는 다중 모달 AI 경험
- 최신 Java 프레임워크와 SDK를 활용한 로컬 AI 모델 통합
- AI와 도구를 통합하는 방법을 보여주는 첫 번째 Model Context Protocol 서비스

## 다음 단계

[5장: 책임 있는 생성형 AI](../05-ResponsibleGenAI/README.md)

**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있지만, 자동 번역에는 오류나 부정확성이 포함될 수 있습니다. 원본 문서를 해당 언어로 작성된 상태에서 권위 있는 자료로 간주해야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 책임을 지지 않습니다.