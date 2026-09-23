# IA Generativa para Principiantes - Edición Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![IA Generativa para Principiantes - Edición Java](../../translated_images/es/beg-genai-series.8b48be9951cc574c.webp)

**Compromiso de Tiempo**: El taller completo se puede realizar en línea sin configuración local. La configuración del entorno toma 2 minutos, y explorar los ejemplos requiere entre 1 y 3 horas según la profundidad de la exploración.

> **Inicio Rápido** 

1. Haz un fork de este repositorio a tu cuenta de GitHub
2. Haz clic en **Code** → pestaña **Codespaces** → **...** → **Nuevo con opciones...**
3. Usa las opciones predeterminadas: esto seleccionará el contenedor de desarrollo creado para este curso
4. Haz clic en **Crear codespace**
5. Espera aproximadamente 2 minutos para que el entorno esté listo
6. Ve directamente a [Capítulo 2: Provisionar Azure AI Foundry](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## Soporte Multilingüe

### Compatible mediante GitHub Action (Automatizado y Siempre Actualizado)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Árabe](../ar/README.md) | [Bengalí](../bn/README.md) | [Búlgaro](../bg/README.md) | [Birmano (Myanmar)](../my/README.md) | [Chino (Simplificado)](../zh-CN/README.md) | [Chino (Tradicional, Hong Kong)](../zh-HK/README.md) | [Chino (Tradicional, Macao)](../zh-MO/README.md) | [Chino (Tradicional, Taiwán)](../zh-TW/README.md) | [Croata](../hr/README.md) | [Checo](../cs/README.md) | [Danés](../da/README.md) | [Neerlandés](../nl/README.md) | [Estonio](../et/README.md) | [Finlandés](../fi/README.md) | [Francés](../fr/README.md) | [Alemán](../de/README.md) | [Griego](../el/README.md) | [Hebreo](../he/README.md) | [Hindi](../hi/README.md) | [Húngaro](../hu/README.md) | [Indonesio](../id/README.md) | [Italiano](../it/README.md) | [Japonés](../ja/README.md) | [Kannada](../kn/README.md) | [Jemer](../km/README.md) | [Coreano](../ko/README.md) | [Lituano](../lt/README.md) | [Malayo](../ms/README.md) | [Malabar](../ml/README.md) | [Maratí](../mr/README.md) | [Nepalí](../ne/README.md) | [Pidgin Nigeriano](../pcm/README.md) | [Noruego](../no/README.md) | [Persa (Farsi)](../fa/README.md) | [Polaco](../pl/README.md) | [Portugués (Brasil)](../pt-BR/README.md) | [Portugués (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Rumano](../ro/README.md) | [Ruso](../ru/README.md) | [Serbio (Cirílico)](../sr/README.md) | [Eslovaco](../sk/README.md) | [Esloveno](../sl/README.md) | [Español](./README.md) | [Swahili](../sw/README.md) | [Sueco](../sv/README.md) | [Tagalo (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Tailandés](../th/README.md) | [Turco](../tr/README.md) | [Ucraniano](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamita](../vi/README.md)

> **¿Prefieres clonar localmente?**
>
> Este repositorio incluye más de 50 traducciones de idiomas, lo que aumenta significativamente el tamaño de la descarga. Para clonar sin traducciones, usa el checkout parcial:
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
> Esto te proporciona todo lo necesario para completar el curso con una descarga mucho más rápida.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Estructura del Curso y Ruta de Aprendizaje

### **Capítulo 1: Introducción a la IA Generativa**
- **Conceptos Básicos**: Comprender Modelos de Lenguaje Grandes, tokens, embeddings y capacidades de IA
- **Ecosistema de IA en Java**: Visión general de Spring AI y OpenAI SDKs
- **Protocolo de Contexto de Modelo (MCP)**: Introducción a MCP y su papel en la comunicación de agentes de IA
- **Aplicaciones Prácticas**: Escenarios del mundo real incluyendo chatbots y generación de contenido
- **[→ Comenzar Capítulo 1](./01-IntroToGenAI/README.md)**

### **Capítulo 2: Configuración del Entorno de Desarrollo**
- **Azure AI Foundry**: Provisionar chat GPT-5.6 Luna y embeddings text-embedding-3-small con Bicep y la Azure Developer CLI (azd)
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: Aprende con `ChatClient`, respaldado por el SDK oficial OpenAI Java y Azure OpenAI v1
- **Autenticación sin Claves**: Conéctate de forma segura con Microsoft Entra ID — sin claves API que gestionar
- **Herramientas de Desarrollo**: Contenedores Docker, VS Code y configuración de GitHub Codespaces
- **[→ Comenzar Capítulo 2](./02-SetupDevEnvironment/README.md)**

### **Capítulo 3: Técnicas Básicas de IA Generativa**
- **SDK oficial de OpenAI para Java**: Haz llamadas directas a Azure OpenAI v1 con autenticación sin claves
- **Ingeniería de Prompts**: Técnicas para obtener respuestas óptimas del modelo de IA
- **Embeddings y Operaciones Vectoriales**: Implementa búsqueda semántica y coincidencia de similitud
- **Generación Aumentada por Recuperación (RAG)**: Combina IA con tus propias fuentes de datos
- **Llamadas a Funciones**: Extiende las capacidades de IA con herramientas y plugins personalizados
- **[→ Comenzar Capítulo 3](./03-CoreGenerativeAITechniques/README.md)**

### **Capítulo 4: Aplicaciones Prácticas y Proyectos**
- **Generador de Historias de Mascotas** (`petstory/`): Generación creativa de contenido con Azure AI Foundry
- **Demo Local de Foundry** (`foundrylocal/`): Integración local de modelos IA con OpenAI Java SDK
- **Servicio Calculadora MCP** (`calculator/`): Implementación básica del Protocolo de Contexto de Modelo con Spring AI
- **[→ Comenzar Capítulo 4](./04-PracticalSamples/README.md)**

### **Capítulo 5: Desarrollo Responsable de IA**
- **Seguridad de Contenido en Azure AI Foundry**: Prueba filtros y mecanismos de seguridad integrados (bloqueos estrictos y rechazos suaves)
- **Demo de IA Responsable**: Ejemplo práctico que muestra cómo funcionan los sistemas modernos de seguridad en IA
- **Mejores Prácticas**: Guías esenciales para el desarrollo y despliegue ético de IA
- **[→ Comenzar Capítulo 5](./05-ResponsibleGenAI/README.md)**

## Recursos Adicionales

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j para Principiantes](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js para Principiantes](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain para Principiantes](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agentes
[![AZD para Principiantes](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI para Principiantes](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP para Principiantes](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Agentes de IA para Principiantes](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serie IA Generativa
[![IA Generativa para Principiantes](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![IA Generativa (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![IA Generativa (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![IA Generativa (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Aprendizaje Básico
[![ML para Principiantes](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Ciencia de Datos para Principiantes](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![IA para Principiantes](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Ciberseguridad para Principiantes](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Desarrollo Web para Principiantes](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT para Principiantes](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Desarrollo XR para Principiantes](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serie Copilot
[![Copilot para Programación Asistida por IA](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot para C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Aventura Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Obtener Ayuda

Si te quedas atascado o tienes preguntas sobre cómo construir aplicaciones de IA, únete a otros aprendices y desarrolladores experimentados en discusiones sobre MCP. Es una comunidad de apoyo donde las preguntas son bienvenidas y el conocimiento se comparte libremente.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Si tienes comentarios sobre el producto o encuentras errores durante la construcción, visita:

[![Foro de Desarrolladores Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional humana. No somos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->