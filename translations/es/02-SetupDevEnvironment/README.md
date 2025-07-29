<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:15:44+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "es"
}
-->
# Configuración del Entorno de Desarrollo para Generative AI con Java

> **Inicio Rápido**: Programa en la nube en 2 minutos - Salta a [Configuración de GitHub Codespaces](../../../02-SetupDevEnvironment) - ¡sin necesidad de instalación local y usando modelos de GitHub!

> **¿Interesado en Azure OpenAI?** Consulta nuestra [Guía de Configuración de Azure OpenAI](getting-started-azure-openai.md) con pasos para crear un nuevo recurso de Azure OpenAI.

## Lo Que Aprenderás

- Configurar un entorno de desarrollo en Java para aplicaciones de IA
- Elegir y configurar tu entorno de desarrollo preferido (primero en la nube con Codespaces, contenedor de desarrollo local o configuración local completa)
- Probar tu configuración conectándote a los Modelos de GitHub

## Tabla de Contenidos

- [Lo Que Aprenderás](../../../02-SetupDevEnvironment)
- [Introducción](../../../02-SetupDevEnvironment)
- [Paso 1: Configura Tu Entorno de Desarrollo](../../../02-SetupDevEnvironment)
  - [Opción A: GitHub Codespaces (Recomendado)](../../../02-SetupDevEnvironment)
  - [Opción B: Contenedor de Desarrollo Local](../../../02-SetupDevEnvironment)
  - [Opción C: Usa Tu Instalación Local Existente](../../../02-SetupDevEnvironment)
- [Paso 2: Crea un Token de Acceso Personal de GitHub](../../../02-SetupDevEnvironment)
- [Paso 3: Prueba Tu Configuración](../../../02-SetupDevEnvironment)
- [Solución de Problemas](../../../02-SetupDevEnvironment)
- [Resumen](../../../02-SetupDevEnvironment)
- [Próximos Pasos](../../../02-SetupDevEnvironment)

## Introducción

Este capítulo te guiará en la configuración de un entorno de desarrollo. Usaremos **Modelos de GitHub** como nuestro ejemplo principal porque es gratuito, fácil de configurar con solo una cuenta de GitHub, no requiere tarjeta de crédito y proporciona acceso a múltiples modelos para experimentar.

**¡No se requiere configuración local!** Puedes comenzar a programar de inmediato usando GitHub Codespaces, que ofrece un entorno de desarrollo completo en tu navegador.

<img src="./images/models.webp" alt="Captura de pantalla: Modelos de GitHub" width="50%">

Recomendamos usar [**Modelos de GitHub**](https://github.com/marketplace?type=models) para este curso porque es:
- **Gratuito** para comenzar
- **Fácil** de configurar con solo una cuenta de GitHub
- **Sin tarjeta de crédito** requerida
- **Múltiples modelos** disponibles para experimentar

> **Nota**: Los Modelos de GitHub utilizados en este entrenamiento tienen estos límites gratuitos:
> - 15 solicitudes por minuto (150 por día)
> - ~8,000 palabras de entrada, ~4,000 palabras de salida por solicitud
> - 5 solicitudes concurrentes
> 
> Para uso en producción, actualiza a Azure AI Foundry Models con tu cuenta de Azure. Tu código no necesita cambiar. Consulta la [documentación de Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Paso 1: Configura Tu Entorno de Desarrollo

<a name="quick-start-cloud"></a>

Hemos creado un contenedor de desarrollo preconfigurado para minimizar el tiempo de configuración y asegurarnos de que tengas todas las herramientas necesarias para este curso de Generative AI con Java. Elige tu enfoque de desarrollo preferido:

### Opciones de Configuración del Entorno:

#### Opción A: GitHub Codespaces (Recomendado)

**¡Comienza a programar en 2 minutos, sin necesidad de configuración local!**

1. Haz un fork de este repositorio en tu cuenta de GitHub  
   > **Nota**: Si deseas editar la configuración básica, consulta la [Configuración del Contenedor de Desarrollo](../../../.devcontainer/devcontainer.json)
2. Haz clic en **Code** → pestaña **Codespaces** → **...** → **New with options...**
3. Usa los valores predeterminados: esto seleccionará la **Configuración del contenedor de desarrollo**: **Entorno de Desarrollo de Generative AI con Java** creado específicamente para este curso
4. Haz clic en **Create codespace**
5. Espera ~2 minutos para que el entorno esté listo
6. Continúa con [Paso 2: Crea un Token de GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Captura de pantalla: Submenú de Codespaces" width="50%">

<img src="./images/image.png" alt="Captura de pantalla: Nuevo con opciones" width="50%">

<img src="./images/codespaces-create.png" alt="Captura de pantalla: Opciones para crear codespace" width="50%">

> **Beneficios de Codespaces**:
> - No se requiere instalación local
> - Funciona en cualquier dispositivo con un navegador
> - Preconfigurado con todas las herramientas y dependencias
> - 60 horas gratuitas al mes para cuentas personales
> - Entorno consistente para todos los estudiantes

#### Opción B: Contenedor de Desarrollo Local

**Para desarrolladores que prefieren el desarrollo local con Docker**

1. Haz un fork y clona este repositorio en tu máquina local  
   > **Nota**: Si deseas editar la configuración básica, consulta la [Configuración del Contenedor de Desarrollo](../../../.devcontainer/devcontainer.json)
2. Instala [Docker Desktop](https://www.docker.com/products/docker-desktop/) y [VS Code](https://code.visualstudio.com/)
3. Instala la [extensión Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) en VS Code
4. Abre la carpeta del repositorio en VS Code
5. Cuando se te solicite, haz clic en **Reopen in Container** (o usa `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Espera a que el contenedor se construya e inicie
7. Continúa con [Paso 2: Crea un Token de GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Captura de pantalla: Configuración del contenedor de desarrollo" width="50%">

<img src="./images/image-3.png" alt="Captura de pantalla: Contenedor de desarrollo construido" width="50%">

#### Opción C: Usa Tu Instalación Local Existente

**Para desarrolladores con entornos Java ya configurados**

Requisitos previos:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) o tu IDE preferido

Pasos:
1. Clona este repositorio en tu máquina local
2. Abre el proyecto en tu IDE
3. Continúa con [Paso 2: Crea un Token de GitHub](../../../02-SetupDevEnvironment)

> **Consejo Pro**: Si tienes una máquina con pocos recursos pero quieres usar VS Code localmente, ¡usa GitHub Codespaces! Puedes conectar tu VS Code local a un Codespace alojado en la nube para lo mejor de ambos mundos.

<img src="./images/image-2.png" alt="Captura de pantalla: Instancia local del contenedor de desarrollo creada" width="50%">

## Paso 2: Crea un Token de Acceso Personal de GitHub

1. Ve a [Configuración de GitHub](https://github.com/settings/profile) y selecciona **Settings** desde el menú de tu perfil.
2. En la barra lateral izquierda, haz clic en **Developer settings** (generalmente al final).
3. Bajo **Personal access tokens**, haz clic en **Fine-grained tokens** (o sigue este [enlace directo](https://github.com/settings/personal-access-tokens)).
4. Haz clic en **Generate new token**.
5. En "Token name", proporciona un nombre descriptivo (por ejemplo, `GenAI-Java-Course-Token`).
6. Establece una fecha de expiración (recomendado: 7 días por seguridad).
7. En "Resource owner", selecciona tu cuenta de usuario.
8. En "Repository access", selecciona los repositorios que deseas usar con los Modelos de GitHub (o "All repositories" si es necesario).
9. En "Repository permissions", busca **Models** y configúralo en **Read and write**.
10. Haz clic en **Generate token**.
11. **Copia y guarda tu token ahora** – no podrás verlo nuevamente.

> **Consejo de Seguridad**: Usa el alcance mínimo requerido y el tiempo de expiración más corto posible para tus tokens de acceso.

## Paso 3: Prueba Tu Configuración con el Ejemplo de Modelos de GitHub

Una vez que tu entorno de desarrollo esté listo, probemos la integración con los Modelos de GitHub usando nuestra aplicación de ejemplo en [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Abre la terminal en tu entorno de desarrollo.
2. Navega al ejemplo de Modelos de GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Configura tu token de GitHub como una variable de entorno:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Ejecuta la aplicación:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Deberías ver una salida similar a:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Entendiendo el Código de Ejemplo

Primero, entendamos lo que acabamos de ejecutar. El ejemplo bajo `examples/github-models` utiliza el SDK de OpenAI para Java para conectarse a los Modelos de GitHub:

**Qué hace este código:**
- **Conecta** con los Modelos de GitHub usando tu token de acceso personal
- **Envía** un mensaje simple "Say Hello World!" al modelo de IA
- **Recibe** y muestra la respuesta de la IA
- **Valida** que tu configuración funciona correctamente

**Dependencia Clave** (en `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Código Principal** (`App.java`):
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

## Resumen

¡Genial! Ahora tienes todo configurado:

- Creaste un Token de Acceso Personal de GitHub con los permisos correctos para acceder a los modelos de IA
- Configuraste tu entorno de desarrollo en Java (ya sea con Codespaces, contenedores de desarrollo o localmente)
- Te conectaste a los Modelos de GitHub usando el SDK de OpenAI para Java para desarrollo de IA gratuito
- Probaste que todo funciona con un ejemplo simple que interactúa con modelos de IA

## Próximos Pasos

[Capítulo 3: Técnicas Básicas de Generative AI](../03-CoreGenerativeAITechniques/README.md)

## Solución de Problemas

¿Tienes problemas? Aquí hay problemas comunes y sus soluciones:

- **¿El token no funciona?** 
  - Asegúrate de haber copiado el token completo sin espacios adicionales
  - Verifica que el token esté configurado correctamente como una variable de entorno
  - Comprueba que tu token tenga los permisos correctos (Models: Read and write)

- **¿Maven no encontrado?** 
  - Si usas contenedores de desarrollo/Codespaces, Maven debería estar preinstalado
  - Para configuración local, asegúrate de que Java 21+ y Maven 3.9+ estén instalados
  - Prueba `mvn --version` para verificar la instalación

- **¿Problemas de conexión?** 
  - Verifica tu conexión a internet
  - Asegúrate de que GitHub sea accesible desde tu red
  - Comprueba que no estés detrás de un firewall que bloquee el endpoint de los Modelos de GitHub

- **¿El contenedor de desarrollo no inicia?** 
  - Asegúrate de que Docker Desktop esté ejecutándose (para desarrollo local)
  - Intenta reconstruir el contenedor: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **¿Errores de compilación de la aplicación?**
  - Asegúrate de estar en el directorio correcto: `02-SetupDevEnvironment/examples/github-models`
  - Intenta limpiar y reconstruir: `mvn clean compile`

> **¿Necesitas ayuda?**: ¿Sigues teniendo problemas? Abre un issue en el repositorio y te ayudaremos.

**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por garantizar la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse como la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas que puedan surgir del uso de esta traducción.