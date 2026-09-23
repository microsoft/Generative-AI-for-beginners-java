# Configuración del Entorno de Desarrollo para Azure AI Foundry

> Esta guía configura los modelos de **Azure AI Foundry** para las aplicaciones Java de IA en este curso, usando autenticación **sin clave** (Microsoft Entra ID), sin claves API que administrar. ¿Nuevo en las herramientas? Comienza con la [guía del entorno de desarrollo](./README.md).

Esta guía configura los modelos de **Azure AI Foundry** para las aplicaciones Java de IA en este curso. Tienes dos opciones:

- **Opción A — Provisionar con `azd` + Bicep (recomendado):** un comando despliega la cuenta Foundry y los modelos como código. Sin clics en el portal.
- **Opción B — Crear recursos manualmente** en el portal de Azure AI Foundry.

Ambos caminos usan **autenticación sin clave** (Microsoft Entra ID), sin claves API que copiar o filtrar.

## Tabla de Contenidos

- [Qué se crea](#qué-se-crea)
- [Requisitos previos](#requisitos-previos)
- [Opción A: Provisionar con azd + Bicep (Recomendado)](#option-a-provision-with-azd--bicep-recommended)
- [Opción B: Crear Recursos Manualmente](#opción-b-crear-recursos-manualmente)
- [Configura tu entorno](#configura-tu-entorno)
- [Prueba tu configuración](#prueba-tu-configuración)
- [¿Qué sigue?](#qué-sigue)
- [Recursos](#recursos)
- [Recursos adicionales](#recursos-adicionales)

## Qué se crea

Las plantillas Bicep en [`infra/`](../../../02-SetupDevEnvironment/infra) aprovisionan:

- Una cuenta de **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, tipo `AIServices`) con un proyecto
- Un despliegue **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), versión `2026-07-09`, con capacidad `GlobalStandard` `10` (10 solicitudes/minuto y 10,000 tokens/minuto para este modelo)
- Un despliegue **embedding** - `text-embedding-3-small`, versión `1` (usado en capítulos posteriores)
- Una asignación de rol **sin clave** (`Cognitive Services OpenAI User`) para iniciar sesión con `az login` en lugar de gestionar claves

## Requisitos previos

- Una [suscripción de Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) y [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opción A: Provisionar con azd + Bicep (Recomendado)

Desde la carpeta `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Iniciar sesión (ambas herramientas)
azd auth login
az login

# Proveer la cuenta de Foundry + despliegues de modelos
azd up
```

`azd` solicita un **nombre de entorno** (por ejemplo `genai-java`), **suscripción** y **región**. Elige tu suscripción y una región donde `gpt-5.6-luna` y `text-embedding-3-small` estén disponibles, por ejemplo `eastus2`. Confirma que la suscripción tiene cuota suficiente para el modelo y tipo de despliegue en esa región; la disponibilidad y cuota varían según la suscripción.

Cuando finaliza el aprovisionamiento, azd:

1. Despliega todo lo definido en [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Ejecuta un gancho post aprovisionamiento que escribe [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) con tu endpoint y nombres de despliegue (sin secretos).

> **Consejo:** Vuelve a ejecutar `azd up` en cualquier momento para aplicar cambios. Ejecuta `azd down` para eliminar todo y dejar de incurrir en costos.

Para ver la configuración generada:

```bash
azd env get-values
```

Ahora ve a [Prueba tu configuración](#prueba-tu-configuración).

## Opción B: Crear recursos manualmente

¿Prefieres el portal? Crea tú mismo los recursos:

1. Ve al [portal de Azure AI Foundry](https://ai.azure.com/) e inicia sesión.
2. **Crea un proyecto** (esto también crea un recurso AI Foundry). Asígnale un nombre como `GenAIJava`.
3. En tu proyecto, abre **Modelos + endpoints** → **Desplegar modelo** → **Desplegar modelo base**.
4. Despliega **GPT-5.6 Luna** (nombre de modelo y despliegue `gpt-5.6-luna`, versión `2026-07-09`) con capacidad **Global Standard** `10`. Repite para **text-embedding-3-small**, versión `1`, si quieres los ejemplos embedding.
5. Desde **Resumen**, copia el **endpoint** (por ejemplo `https://<recurso>.openai.azure.com/`).
6. Concédate acceso sin clave: en el recurso, abre **Control de acceso (IAM)** → **Agregar asignación de rol** → asigna **Cognitive Services OpenAI User** a tu cuenta.

> **¿Aún tienes problemas?** Consulta la [documentación de Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configura tu entorno

**Si usaste la Opción A (`azd up`)**, tu archivo de configuración ya está escrito — no necesitas configurar nada. Salta a [Prueba tu configuración](#prueba-tu-configuración).

**Si usaste la Opción B (manual)**, crea el archivo `.env` del ejemplo tú mismo:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Edita `.env` con tu endpoint (sin clave — la autenticación es sin clave):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Usa el endpoint Azure OpenAI del recurso, no la URL del proyecto. La app basic-chat lo resuelve a `/openai/v1` y configura un cliente con token bearer explícito; no se requiere clave API.

> **Nota de seguridad:** No hay clave API que almacenar. Autenticas con Microsoft Entra ID vía `az login` (local) o identidad administrada (en Azure). El archivo `.env` contiene solo configuraciones no secretas y ya está cubierto por `.gitignore`.

## Prueba tu configuración

Asegúrate de haber iniciado sesión para que la autenticación sin clave pueda obtener un token, luego ejecuta el ejemplo:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # si aún no has iniciado sesión
mvn clean spring-boot:run
```

Deberías ver una respuesta del modelo `gpt-5.6-luna`. Ejecuta los ejemplos secuencialmente para mantenerte dentro de la pequeña cuota predeterminada; si recibes HTTP 429, espera el intervalo de reintento antes de volver a intentarlo.

> **Usuarios de VS Code:** Presiona `F5` para ejecutar. La aplicación carga tu `.env` automáticamente.

> **Ejemplo completo:** Consulta el [ejemplo básico de chat con Azure AI Foundry](./examples/basic-chat-azure/README.md) para detalles y solución de problemas.

## ¿Qué sigue?

Después de aprovisionar y ejecutar el ejemplo con éxito, tendrás:
- Azure AI Foundry con `gpt-5.6-luna` y `text-embedding-3-small` desplegados
- Autenticación sin clave (Microsoft Entra ID), sin claves que administrar
- Un `.env` local con tu endpoint y nombres de despliegue
- Un entorno de desarrollo Java listo para usar

**Continúa a** [Capítulo 3: Técnicas básicas de IA generativa](../03-CoreGenerativeAITechniques/README.md) para comenzar a construir aplicaciones de IA.

## Recursos

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autenticación sin clave con Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Documentación de Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Transición Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK oficial OpenAI Java con Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Recursos adicionales

- [Descargar VS Code](https://code.visualstudio.com/Download)
- [Obtener Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Configuración de Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional humana. No somos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->