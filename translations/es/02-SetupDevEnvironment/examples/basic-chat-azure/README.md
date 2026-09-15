# Chat básico con Azure AI Foundry - Ejemplo de principio a fin

Este ejemplo es una aplicación simple de Spring Boot que se conecta a un modelo de **Azure AI Foundry** usando **autenticación sin clave** (Microsoft Entra ID) y prueba tu configuración. Utiliza el `ChatClient` de Spring AI, respaldado por el **SDK oficial de OpenAI para Java** y el endpoint **Azure OpenAI v1**.

Las versiones en [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) son Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** y dotenv-java **3.2.0**. El ejemplo usa `spring-ai-starter-model-openai` y declara explícitamente `openai-java` y `azure-identity`; Spring AI 2 eliminó el antiguo starter de Azure OpenAI.

## Tabla de Contenidos

- [Requisitos previos](#requisitos-previos)
- [Inicio rápido](#inicio-rápido)
- [Cómo funciona la autenticación](#cómo-funciona-la-autenticación)
- [Ejecutar la aplicación](#ejecutando-la-aplicación)
  - [Usando Maven](#usando-maven)
  - [Usando VS Code](#usando-vs-code)
  - [Salida esperada](#salida-esperada)
- [Referencia de configuración](#referencia-de-configuración)
  - [Variables de entorno](#variables-de-entorno)
  - [Configuración de Spring](#configuración-de-spring)
- [Solución de problemas](#solución-de-problemas)
  - [Problemas comunes](#problemas-comunes)
  - [Modo de depuración](#modo-de-depuración)
- [Próximos pasos](#próximos-pasos)
- [Recursos](#recursos)

## Requisitos previos

Antes de ejecutar este ejemplo, asegúrate de tener:

- Un recurso de Azure AI Foundry con un despliegue `gpt-5.6-luna` - aprovisionado con `azd up` o manualmente vía la [guía de configuración de Azure AI Foundry](../../getting-started-azure-openai.md)
- El rol **Cognitive Services OpenAI User** en ese recurso (las plantillas Bicep asignan esto por ti)
- La [CLI de Azure (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), ingresada con `az login`
- Java 21+ y Maven 3.9+

> **No se requiere clave API** — la autenticación es sin clave a través de Microsoft Entra ID.

## Inicio rápido

```bash
# 1. Navega al proyecto
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Inicia sesión para que la autenticación sin clave pueda obtener un token
az login

# 3. Configura el endpoint
#    - Si ejecutaste `azd up`, el archivo .env fue creado para ti (omite este paso).
#    - De lo contrario, copia la plantilla y establece AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Ejecuta la aplicación
mvn spring-boot:run
```

## Cómo funciona la autenticación

Este ejemplo se autentica con **Microsoft Entra ID** — no hay clave API.

La aplicación configura la autenticación explícitamente en [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` crea un `BearerTokenCredential` usando `AuthenticationUtil.getBearerTokenSupplier` con `DefaultAzureCredential` y el alcance `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` construye un `OpenAIClient` con `OpenAIOkHttpClient.builder()`, resuelve el endpoint del recurso a `/openai/v1`, y provee la credencial bearer con `.credential(...)`.
3. `azureChatModel()` provee ese cliente al `OpenAiChatModel` de Spring AI, que respalda el `ChatClient` de la lección.

Estos beans explícitos evitan que una clave global `OPENAI_API_KEY` anule la autenticación de Azure. Omitir una clave API solo en YAML no configura la autenticación. `DefaultAzureCredential` puede usar tu sesión `az login` localmente o una identidad administrada en Azure; la identidad seleccionada debe tener el rol del recurso listado arriba.

## Ejecutando la aplicación

### Usando Maven

```bash
mvn spring-boot:run
```

### Usando VS Code

1. Abre el proyecto en VS Code
2. Presiona `F5` o usa el panel "Run and Debug"
3. Selecciona la configuración "Spring Boot-BasicChatApplication"

> **Nota**: La aplicación carga `.env` desde su directorio de trabajo, incluso cuando se lanza desde VS Code.

### Salida esperada

Salida ilustrativa tras una ejecución exitosa (logs de inicio omitidos; el texto de la respuesta puede variar):

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

## Referencia de configuración

### Variables de entorno

| Variable | Descripción | Requerida | Ejemplo |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL del endpoint de Foundry (Azure OpenAI) | Sí | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nombre del despliegue del modelo de chat | No | `gpt-5.6-luna` (predeterminado) |

> No hay variable de clave API — la autenticación es sin clave (Microsoft Entra ID vía `az login`).

### Configuración de Spring

La configuración en [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) usa el prefijo `spring.ai.openai` y propiedades de chat planas (sin bloque `options`):

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

`model` es el **nombre del despliegue en Azure**. La autenticación proviene de los beans explícitos descritos arriba, no de un ajuste `api-key`. La lección desactiva el razonamiento y limita los tokens de completado a 500; deja `temperature` y el legado `max-tokens` sin configurar.

Microsoft recomienda usar el [SDK oficial de OpenAI con Azure OpenAI v1 y la API de Respuestas para nuevas aplicaciones](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions sigue siendo soportado para esta lección basada en mensajes. Para GPT-5.6, las solicitudes que incluyen herramientas en Chat Completions deben configurar `reasoning_effort` a `none`; usa Respuestas al combinar razonamiento con herramientas. Consulta [llamadas a herramientas con modelos de razonamiento](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Solución de problemas

### Problemas comunes

<details>
<summary><strong>Error: 401 / "PermissionDenied" / errores de token</strong></summary>

- Ejecuta `az login` — la autenticación sin clave necesita una sesión activa para obtener un token
- Verifica que tu cuenta tenga el rol **Cognitive Services OpenAI User** en el recurso
- Si acabas de asignar el rol, espera un minuto para que se propague
- Confirma que estás en el inquilino/suscripción correcta (`az account show`)
</details>

<details>
<summary><strong>Error: "El endpoint no es válido" / errores de conexión</strong></summary>

- Asegúrate de que `AZURE_OPENAI_ENDPOINT` sea la URL base completa (p.ej., `https://your-resource.openai.azure.com/`)
- Verifica la consistencia de la barra diagonal final
- Confirma que el endpoint coincide con tu recurso aprovisionado (`azd env get-values`)
</details>

<details>
<summary><strong>Error: "No se encontró el despliegue"</strong></summary>

- Verifica que `AZURE_OPENAI_DEPLOYMENT` coincida con un nombre de despliegue en Azure
- Asegúrate que el modelo esté desplegado y activo correctamente
- El nombre de despliegue por defecto es `gpt-5.6-luna`
</details>

<details>
<summary><strong>Error: 429 / límite de tasa excedido</strong></summary>

- El despliegue predeterminado GPT-5.6 Luna tiene capacidad Global Standard 10: 10 solicitudes/minuto y 10,000 tokens/minuto
- Ejecuta los ejemplos secuencialmente y espera el intervalo de reintentos del servicio antes de volver a intentar
- Este ejemplo básico desactiva los reintentos automáticos del SDK, por lo que una solicitud fallida se reporta directamente
</details>

<details>
<summary><strong>VS Code: Variables de entorno no cargan</strong></summary>

- Asegúrate que el archivo `.env` esté en el directorio raíz del proyecto (al mismo nivel que `pom.xml`)
- Intenta ejecutar `mvn spring-boot:run` en el terminal integrado de VS Code
- Verifica que la extensión Java de VS Code esté correctamente instalada
</details>

### Modo de depuración

Para habilitar registros detallados, descomenta estas líneas en [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Próximos pasos

**¡Configuración completa!** Continúa tu viaje de aprendizaje:

[Capítulo 3: Técnicas centrales de IA generativa](../../../03-CoreGenerativeAITechniques/README.md)

## Recursos

- [Transición de Spring AI 2 a OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK oficial OpenAI Java con Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autenticación sin clave con Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Documentación de Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional humana. No somos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->