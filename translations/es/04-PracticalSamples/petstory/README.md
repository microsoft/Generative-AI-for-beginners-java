# Tutorial generador de historias de mascotas para principiantes

Sube una foto de una mascota, analízala con GPT-5.6 Luna y genera una historia a partir de la descripción resultante. Ambas solicitudes al modelo usan `reasoning_effort: none`.

| Componente | Versión |
| --- | --- |
| Java | 21 o superior |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Índice

- [Requisitos previos](#requisitos-previos)
- [Comprendiendo la estructura del proyecto](#comprendiendo-la-estructura-del-proyecto)
- [Componentes principales explicados](#componentes-principales-explicados)
  - [1. Aplicación principal](#1-aplicación-principal)
  - [2. Controlador web](#2-controlador-web)
  - [3. Servicio de historias](#3-servicio-de-historias)
  - [4. Plantillas web](#4-plantillas-web)
  - [5. Configuración](#5-configuración)
- [Ejecutando la aplicación](#ejecutando-la-aplicación)
- [Pruebas fuera de línea](#pruebas-fuera-de-línea)
- [Cómo funciona todo junto](#cómo-funciona-todo-junto)
- [Comprendiendo la integración con IA](#comprendiendo-la-integración-con-ia)
- [Próximos pasos](#próximos-pasos)

## Requisitos previos

Antes de comenzar, asegúrate de tener:
- Java 21 o superior instalado
- Maven para la gestión de dependencias
- Una implementación de Azure AI Foundry de GPT-5.6 Luna llamada `gpt-5.6-luna`, o un anulación `AZURE_OPENAI_DEPLOYMENT` que apunte a esa implementación. Consulta el [Capítulo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) para el aprovisionamiento e inicia sesión con `az login` para autenticación sin claves. La implementación debe soportar entrada de imágenes y `reasoning_effort: none`.
- Conocimientos básicos de Java, Spring Boot y desarrollo web

## Comprendiendo la estructura del proyecto

El proyecto de historia de mascotas tiene varios archivos importantes:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Componentes principales explicados

### 1. Aplicación Principal

**Archivo:** `PetStoryApplication.java`

Este es el punto de entrada de nuestra aplicación Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Qué hace esto:**
- La anotación `@SpringBootApplication` habilita la configuración automática y el escaneo de componentes
- Inicia un servidor web embebido (Tomcat) en el puerto 8080
- Crea todos los beans y servicios necesarios de Spring automáticamente

### 2. Controlador web

**Archivo:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Solicitud | Respuesta exitosa |
| --- | --- | --- |
| `GET /` | Sin cuerpo | Formulario HTML de subida con un token CSRF |
| `POST /analyze-image` | `multipart/form-data`, campo de archivo `image` | JSON: `{"description":"Una mascota juguetona..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, campo `description` | Página HTML con la descripción y la historia generada |

Ambos endpoints POST requieren la cookie de sesión y el token CSRF obtenidos de `GET /`. El script de subida envía el valor oculto `_csrf` en el encabezado `X-CSRF-TOKEN`; el envío de la historia lo envía como campo de formulario `_csrf`. Los clientes API deben conservar la cookie entre solicitudes. Estos son endpoints de formulario, no de solicitudes JSON.

Las descripciones deben no estar vacías y tener un máximo de 1000 caracteres. El controlador recorta la descripción y elimina `<`, `>`, comillas dobles, apóstrofes y `&` antes de pasarla al servicio. La plantilla de resultado también escapa la salida del modelo con `th:text`.

Las fallas en la validación de imágenes retornan HTTP 400 con un campo `error`; las fallas del modelo retornan HTTP 502 con campo `error` y sin `description`. Las descripciones de historia inválidas o fallas del modelo redirigen a `/` con un error visible. La falta de campos requeridos retorna HTTP 400, y la falta o invalidez del token CSRF retorna HTTP 403. No se presentan descripciones o historias alternativas como resultados exitosos de IA.

### 3. Servicio de historias

**Archivo:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

El SDK oficial de OpenAI Java 4.63.1 llama a la API Chat Completions compatible con OpenAI de Azure AI Foundry. Azure Identity 1.18.6 provee un token portador Microsoft Entra a través de `DefaultAzureCredential`; no se requiere clave API.

| Operación | Entrada | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bytes de imagen codificados como URL de datos base64 con el tipo MIME subido | 300 |
| `generateStory` | Una descripción de mascota en un mensaje de usuario | 800 |

Ambas solicitudes usan la implementación configurada, por defecto `gpt-5.6-luna`, y establecen explícitamente `ReasoningEffort.NONE` (`reasoning_effort: none`). Ninguna solicitud envía `temperature` o el parámetro heredado `max_tokens`.

El análisis de imágenes acepta JPEG, PNG, GIF y WebP, rechaza imágenes vacías y archivos mayores a 10MB, y limita la descripción resultante a 1000 caracteres. El prompt para la historia solicita un cuento corto familiar. Opciones vacías o contenido en blanco del modelo son errores, y las fallas conservan la causa original para diagnósticos del servidor. El cliente SDK se cierra al detener la aplicación.

### 4. Plantillas web

**Archivo:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formulario de subida)

La página empieza con un selector de fotos, no un área de texto para descripción. **Analizar Imagen** previsualiza la foto seleccionada y la envía a `/analyze-image`. Una respuesta exitosa muestra la descripción, llena el campo oculto `description` y revela **Generar Historia**. Ese botón envía el formulario existente a `/generate-story`.

No hay descarga de modelo en el navegador ni dependencia CDN. El análisis de imagen se ejecuta en el servidor mediante la implementación Azure configurada. Las fallas permanecen visibles y no habilitan la generación de historias con descripciones fabricadas. Seleccionar otro archivo borra el análisis previo.

**Archivo:** `result.html` (Visualización de la historia)

Muestra la historia generada:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Características de la plantilla:**

1. **Integración Thymeleaf**: Usa atributos `th:` para contenido dinámico
2. **Diseño adaptable**: Estilos CSS para móvil y escritorio
3. **Manejo de errores**: Muestra errores de validación a los usuarios
4. **Manejo de subida**: JavaScript previsualiza la foto, envía una solicitud multipart protegida con CSRF y muestra la descripción retornada

### 5. Configuración

**Archivo:** `application.properties`

Configuraciones para la aplicación:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Configuración explicada:**

1. **Subida de archivos**: Tanto el archivo como la solicitud multipart completa están limitados a 10MB; mantén las fotos por debajo de ese límite para dejar espacio para encabezados multipart
2. **Registro**: Controla qué información se registra durante la ejecución
3. **Azure AI Foundry**: Especifica el endpoint y la implementación del modelo a usar (autenticación sin clave)
4. **Seguridad**: La protección CSRF permanece habilitada; los diagnósticos del modelo se registran en el servidor, mientras que el controlador muestra mensajes genéricos de fallo de modelo

## Ejecutando la aplicación

### Paso 1: Inicia sesión y configura tu endpoint

La autenticación es sin clave (Microsoft Entra ID), así que no hay clave API. Inicia sesión y configura tu endpoint Foundry:

**Windows (Símbolo del sistema):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Por qué es necesario:**
- Azure AI Foundry usa Microsoft Entra ID para autenticar las solicitudes de inferencia
- La autenticación sin clave significa que no hay secretos en tu código fuente o entorno
- Tu cuenta debe tener el rol **Usuario de OpenAI de Servicios Cognitivos** en el recurso

El nombre de implementación predeterminado es `gpt-5.6-luna`. Si tu implementación GPT-5.6 Luna tiene otro nombre, configura `AZURE_OPENAI_DEPLOYMENT` en la misma terminal antes de iniciar la aplicación. Tanto el análisis de imagen como la generación de historias usan esta configuración.

### Paso 2: Compilar y ejecutar

Navega al directorio del proyecto:
```bash
cd 04-PracticalSamples/petstory
```

Compila el JAR ejecutable independiente y ejecuta todas las pruebas fuera de línea:
```bash
mvn clean package
```

Inicia el servidor:
```bash
mvn spring-boot:run
```

La aplicación iniciará en `http://localhost:8080`.

Alternativamente, inicia el JAR empaquetado en un puerto libre, por ejemplo:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Para ese comando, abre `http://localhost:8083/`. Las mismas rutas `/analyze-image` y `/generate-story` están disponibles en el puerto seleccionado.

### Paso 3: Prueba la aplicación

1. **Abre** `http://localhost:8080` en tu navegador
2. **Selecciona** una foto clara de mascota en formato JPEG, PNG, GIF o WebP, menor a 10MB
3. **Haz clic** en "Analizar Imagen" y espera la descripción de la mascota
4. **Haz clic** en "Generar Historia" después del análisis exitoso
5. **Visualiza** la historia y usa el enlace en la página de resultado para volver al formulario de subida

El flujo exitoso de foto a historia realiza dos llamadas al modelo, una por botón. La inferencia en vivo consume la cuota de tu implementación y puede generar cargos; ejecuta pruebas básicas en serie cuando compartas una implementación con límite de tasa. Cargar la página principal no llama al modelo.

## Pruebas fuera de línea

Desde el directorio sample, ejecuta:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) captura solicitudes reales al SDK OpenAI con un fixture HTTP de loopback. Verifica el despliegue en ambas solicitudes, `reasoning_effort: none`, límites de tokens, carga útil de imagen, validación de entrada, respuestas vacías y errores ascendentes.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) usa MockMvc con un servicio de modelo simulado para probar las páginas Thymeleaf renderizadas, contrato de subida, CSRF, validación, escapado de salida y fallas visibles. Estas pruebas no necesitan credenciales de Azure y nunca hacen llamadas a inferencia paga de Azure. Maven escribe reportes Surefire bajo `target/surefire-reports`.

## Cómo funciona todo junto

Aquí está el flujo completo cuando generas una historia de mascota:

1. **Selección de foto**: Eliges una imagen de mascota en el formulario de subida
2. **Subida de imagen**: "Analizar Imagen" envía un POST multipart a `/analyze-image` con el encabezado CSRF
3. **Análisis de imagen**: `StoryService` envía la imagen a GPT-5.6 Luna con razonamiento establecido en `none`
4. **Visualización de descripción**: El navegador muestra la descripción retornada y la almacena en el formulario
5. **Envío de historia**: "Generar Historia" envía `description` y `_csrf` a `/generate-story`
6. **Generación de historia**: El controlador valida la descripción y llama a la misma implementación con razonamiento en `none`
7. **Renderizado de plantilla**: Thymeleaf escapa y muestra la descripción y la historia en la página de resultados

**Flujo de manejo de errores:**
Si falla el modelo, el servidor registra la causa. El análisis de imagen retorna HTTP 502 y el navegador muestra el error sin revelar "Generar Historia". La generación de historia redirige al formulario con un mensaje de error. Ningún camino sustituye silenciosamente un resultado preescrito.

## Comprendiendo la integración con IA

### Azure AI Foundry (sin claves)
El servicio configura el SDK con el endpoint `/openai/v1/` de tu recurso. `DefaultAzureCredential` y `AuthenticationUtil.getBearerTokenSupplier` proveen tokens Microsoft Entra para `https://ai.azure.com/.default`. El desarrollo local puede usar tu inicio de sesión CLI de Azure; una app alojada en Azure puede usar identidad administrada con permisos necesarios.

### Ingeniería de prompts
El análisis de imagen solicita características observables de la mascota en un párrafo corto e indica al modelo tratar el texto en la imagen como datos, no instrucciones. La generación de historia usa la descripción retornada en una solicitud separada de escritura familiar. Ninguna llamada habilita razonamiento ni establece una temperatura distinta.

### Procesamiento de respuesta
El manejador compartido de respuestas rechaza elecciones faltantes y contenido vacío o solo espacios en blanco, recorta contenido válido y conserva fallas ascendentes. Las descripciones de imagen se limitan a 1000 caracteres para ajustarse al formulario de historia siguiente. La falla original del modelo se mantiene para diagnósticos, pero no se muestra al usuario.

## Próximos pasos

Para más ejemplos, consulta [Capítulo 04: Ejemplos prácticos](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional humana. No somos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->