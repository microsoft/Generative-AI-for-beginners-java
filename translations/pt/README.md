<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:38:31+00:00",
  "source_file": "README.md",
  "language_code": "pt"
}
-->
# IA Generativa para Principiantes - Edição Java
[![Discord do Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![IA Generativa para Principiantes - Edição Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.pt.png)

**Compromisso de Tempo**: Todo o workshop pode ser concluído online sem configuração local. A configuração do ambiente demora 2 minutos, sendo que explorar os exemplos requer entre 1–3 horas, dependendo da profundidade da exploração.

> **Início Rápido** 

1. Faça fork deste repositório para a sua conta GitHub
2. Clique em **Code** → separador **Codespaces** → **...** → **New with options...**
3. Use os padrões – isto irá selecionar o contentor de desenvolvimento criado para este curso
4. Clique em **Create codespace**
5. Aguarde ~2 minutos até o ambiente ficar pronto
6. Vá diretamente para [→ Iniciar Capítulo 2](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Prefere Clonar Localmente?**
>
> Este repositório inclui mais de 50 traduções de idiomas, o que aumenta significativamente o tamanho do download. Para clonar sem traduções, use sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Isto dá-lhe tudo o que precisa para concluir o curso com um download muito mais rápido.


## Suporte Multilingue

### Suportado através do GitHub Action (Automatizado e Sempre Atualizado)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Árabe](../ar/README.md) | [Bengalês](../bn/README.md) | [Búlgaro](../bg/README.md) | [Birmanês (Myanmar)](../my/README.md) | [Chinês (Simplificado)](../zh/README.md) | [Chinês (Tradicional, Hong Kong)](../hk/README.md) | [Chinês (Tradicional, Macau)](../mo/README.md) | [Chinês (Tradicional, Taiwan)](../tw/README.md) | [Croata](../hr/README.md) | [Checo](../cs/README.md) | [Dinamarquês](../da/README.md) | [Holandês](../nl/README.md) | [Estónio](../et/README.md) | [Finlandês](../fi/README.md) | [Francês](../fr/README.md) | [Alemão](../de/README.md) | [Grego](../el/README.md) | [Hebraico](../he/README.md) | [Hindi](../hi/README.md) | [Húngaro](../hu/README.md) | [Indonésio](../id/README.md) | [Italiano](../it/README.md) | [Japonês](../ja/README.md) | [Kannada](../kn/README.md) | [Coreano](../ko/README.md) | [Lituano](../lt/README.md) | [Malaio](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepalês](../ne/README.md) | [Pidgin Nigeriano](../pcm/README.md) | [Norueguês](../no/README.md) | [Persa (Farsi)](../fa/README.md) | [Polaco](../pl/README.md) | [Português (Brasil)](../br/README.md) | [Português (Portugal)](./README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romeno](../ro/README.md) | [Russo](../ru/README.md) | [Sérvio (Cirílico)](../sr/README.md) | [Eslovaco](../sk/README.md) | [Esloveno](../sl/README.md) | [Espanhol](../es/README.md) | [Suaíli](../sw/README.md) | [Sueco](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Tailandês](../th/README.md) | [Turco](../tr/README.md) | [Ucraniano](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamita](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Estrutura do Curso e Percurso de Aprendizagem

### **Capítulo 1: Introdução à IA Generativa**
- **Conceitos Principais**: Compreensão de Modelos de Linguagem de Grande Escala, tokens, embeddings e capacidades de IA
- **Ecossistema de IA em Java**: Visão geral do Spring AI e dos SDKs OpenAI
- **Protocolo de Contexto do Modelo**: Introdução ao MCP e ao seu papel na comunicação de agentes de IA
- **Aplicações Práticas**: Cenários do mundo real incluindo chatbots e geração de conteúdo
- **[→ Iniciar Capítulo 1](./01-IntroToGenAI/README.md)**

### **Capítulo 2: Configuração do Ambiente de Desenvolvimento**
- **Configuração Multi-Provedor**: Configure GitHub Models, Azure OpenAI e integrações com o OpenAI Java SDK
- **Spring Boot + Spring AI**: Melhores práticas para desenvolvimento de aplicações de IA em ambiente empresarial
- **GitHub Models**: Acesso gratuito a modelos de IA para prototipagem e aprendizagem (não é necessário cartão de crédito)
- **Ferramentas de Desenvolvimento**: Contentores Docker, VS Code e configuração de GitHub Codespaces
- **[→ Iniciar Capítulo 2](./02-SetupDevEnvironment/README.md)**

### **Capítulo 3: Técnicas Principais de IA Generativa**
- **Engenharia de Prompts**: Técnicas para obter respostas ótimas dos modelos de IA
- **Embeddings e Operações Vetoriais**: Implementar pesquisa semântica e correspondência por similaridade
- **Geração com Recuperação Aumentada (RAG)**: Combinar IA com as suas próprias fontes de dados
- **Chamada de Funções**: Expandir as capacidades da IA com ferramentas e plugins personalizados
- **[→ Iniciar Capítulo 3](./03-CoreGenerativeAITechniques/README.md)**

### **Capítulo 4: Aplicações Práticas e Projetos**
- **Pet Story Generator** (`petstory/`): Geração criativa de conteúdo com GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): Integração de modelo de IA local com o OpenAI Java SDK
- **Serviço de Calculadora MCP** (`calculator/`): Implementação básica do Protocolo de Contexto do Modelo com Spring AI
- **[→ Iniciar Capítulo 4](./04-PracticalSamples/README.md)**

### **Capítulo 5: Desenvolvimento Responsável de IA**
- **Segurança dos GitHub Models**: Teste dos filtros de conteúdo incorporados e mecanismos de segurança (bloqueios rígidos e recusas suaves)
- **Demonstração de IA Responsável**: Exemplo prático que mostra como os sistemas de segurança modernos de IA funcionam na prática
- **Melhores Práticas**: Diretrizes essenciais para desenvolvimento e implantação ética de IA
- **[→ Iniciar Capítulo 5](./05-ResponsibleGenAI/README.md)**

## Recursos Adicionais

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j para Principiantes](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js para Principiantes](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agentes
[![AZD para Principiantes](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI para Principiantes](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP para Principiantes](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Agentes de IA para Principiantes](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Série IA Generativa
[![IA Generativa para Principiantes](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![IA Generativa (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![IA Generativa (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![IA Generativa (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Aprendizagem Fundamental
[![ML para Principiantes](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Ciência de Dados para Principiantes](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![IA para Principiantes](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cibersegurança para Principiantes](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Desenvolvimento Web para Principiantes](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT para Iniciantes](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Desenvolvimento XR para Iniciantes](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Série Copilot
[![Copilot para Programação Emparelhada com IA](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot para C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Aventura Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Obter Ajuda

Se ficar bloqueado ou tiver alguma dúvida sobre como criar aplicações de IA, junte-se a outros aprendizes e a desenvolvedores experientes nas discussões sobre o MCP. É uma comunidade de apoio onde as perguntas são bem-vindas e o conhecimento é partilhado livremente.

[![Discord do Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Se tiver feedback do produto ou encontrar erros durante a construção, visite:

[![Fórum de Desenvolvedores Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Isenção de responsabilidade**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela exatidão, por favor note que traduções automáticas podem conter erros ou imprecisões. A versão original no seu idioma nativo deve ser considerada a fonte autoritativa. Para informações críticas, recomenda-se tradução humana profissional. Não nos responsabilizamos por quaisquer mal‑entendidos ou interpretações incorretas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->