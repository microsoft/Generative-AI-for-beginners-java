# Tutorial de Técnicas Básicas de IA Generativa

## Tabla de Contenidos

- [Prerrequisitos](#prerrequisitos)
- [Primeros Pasos](#primeros-pasos)
- [Guía de Selección de Modelos](#guía-de-selección-de-modelo)
- [Tutorial 1: Completaciones y Chat con LLM](#tutorial-1-completaciones-y-chat-con-llm)
- [Tutorial 2: Llamada a Funciones](#tutorial-2-llamada-a-funciones)
- [Tutorial 3: RAG (Generación Aumentada por Recuperación)](#tutorial-3-rag-generación-aumentada-por-recuperación)
- [Tutorial 4: IA Responsable](#tutorial-4-ia-responsable)
- [Patrones Comunes en los Ejemplos](#patrones-comunes-en-los-ejemplos)
- [Pruebas Unitarias](#pruebas-unitarias)
- [Verificación Secuencial en Vivo](#verificación-secuencial-en-vivo)
- [Solución de Problemas](#solución-de-problemas)
- [Próximos Pasos](#próximos-pasos)

## Resumen

Cuatro programas Java independientes demuestran chat, historial de conversación, llamadas a funciones, generación aumentada por recuperación (RAG) de documentos completos y manejo responsable de IA. Todas las solicitudes de chat se dirigen por defecto a **GPT-5.6 Luna con esfuerzo de razonamiento `none`**.

Estos ejemplos usan el SDK oficial de OpenAI Java con el endpoint v1 de Azure OpenAI, siguiendo la [guía del SDK de Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). El paquete anterior `azure-ai-openai` ya no es una dependencia. Chat Completions se mantiene para enseñar los flujos de trabajo basados en mensajes existentes; consulte el [SDK Java de OpenAI](https://github.com/openai/openai-java#microsoft-azure) para otras opciones de API.

## Prerrequisitos

- Java 21 o superior y Maven 3.6.3 o superior.
- Un despliegue de chat Azure OpenAI llamado `gpt-5.6-luna`, o una anulación con configuraciones compatibles para Chat Completions.
- Una identidad Azure con sesión activa y el rol **Usuario de Servicios Cognitivos OpenAI** en el recurso. El desarrollo local usa la sesión del Azure CLI; las aplicaciones hospedadas pueden usar identidad administrada.
- Consulte el [Capítulo 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) para instrucciones de configuración del recurso e inicio de sesión.

La [configuración Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fija estas versiones, comprobadas el 14-09-2026:

| Componente | Versión | Propósito |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Cliente oficial compatible con Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Autenticación sin clave y renovación de tokens |
| `net.objecthunter:exp4j` | 0.4.8 | Análisis de expresiones aritméticas sin evaluación de código |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Pruebas unitarias offline con Jupiter |
| Compilador Maven / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Compilación Java 21, pruebas, ejemplos ejecutables |

El compilador usa `--release 21`. Estos ejemplos independientes no requieren Spring Boot, Spring AI ni LangChain4j.

## Primeros Pasos

Desde la raíz del repositorio, configure el endpoint del recurso y la posible anulación del despliegue en su shell.

**PowerShell en Windows:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Las pruebas no requieren credenciales de Azure ni endpoint. Maven no lee automáticamente un archivo de entorno; configure las variables en el shell que use para lanzar los ejemplos en vivo. Para lanzamientos en IDE, verifique el entorno que provee su configuración.

## Guía de Selección de Modelo

| Variable de entorno | Significado | Valor por defecto |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Raíz HTTPS del recurso Azure o URL `/openai/v1` ya normalizada | Requerido para ejecuciones en vivo |
| `AZURE_OPENAI_DEPLOYMENT` | Nombre del despliegue de chat, no una versión del modelo | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configuración separada para despliegue de embedding, no usada por estos cuatro programas | `text-embedding-3-small` |

Las anulaciones de despliegue vacías usan los valores por defecto. La configuración añade exactamente una vez `/openai/v1` y rechaza credenciales, cadenas de consulta y rutas de despliegue heredadas en el endpoint.

Cada solicitud de chat establece explícitamente `reasoningEffort(ReasoningEffort.NONE)` y `maxCompletionTokens(...)`. Ninguna solicitud configura `temperature`, `top_p` ni la opción antigua de tokens de completación. Esto incluye la selección y resultados de herramientas. Las herramientas de función de Chat Completions GPT-5.6 requieren esfuerzo de razonamiento `none`; consulte la [guía de chat de Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**No hay punto de entrada de streaming o embedding en este capítulo.** El lector recupera todo el documento, no vectores. Si lo amplía con embeddings, use un despliegue separado como `text-embedding-3-small`, nunca Luna.

## Tutorial 1: Completaciones y Chat con LLM

Fuente: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

El programa ejecuta una explicación simple de flujos Java, una conversación de dos turnos con HashMap/TreeMap y chat interactivo. El segundo turno incluye la primera respuesta del asistente; cada turno interactivo también envía la conversación previa.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` suministra el despliegue y el ajuste explícito de razonamiento. El chat interactivo omite líneas vacías, termina con `exit` o EOF, y mantiene el mensaje del sistema más nueve turnos completos de usuario/asistente. Limitar el conteo de turnos es un límite educativo, no una garantía exacta de presupuesto de tokens.

Desde el directorio de ejemplos:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Espere tres respuestas iniciales, luego un prompt `You:`. Cada pregunta interactiva no vacía añade una solicitud. Los límites de completación son 200, 300, 400 y luego 500 tokens por turno interactivo.

## Tutorial 2: Llamada a Funciones

Fuente: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

El SDK deriva esquemas JSON desde los registros anotados `WeatherArguments` y `CalculationArguments`. La selección obligatoria de herramienta hace que cada ejemplo ejecute el protocolo de herramienta en vez de aceptar una respuesta no asistida del modelo.

1. Enviar una pregunta con la herramienta permitida, esfuerzo de razonamiento `none` y límite de completación de 300 tokens.
2. Requerir una razón de fin `tool_calls`, validar el nombre de función y los IDs de llamada, y analizar argumentos JSON tipados.
3. Ejecutar la función localmente. El modelo no ejecuta código Java ni arbitrario.
4. Añadir un mensaje de llamada a herramienta del asistente una vez, seguido de cada resultado con su `tool_call_id` correspondiente.
5. Enviar una solicitud final de 300 tokens sin herramientas y requerir una respuesta completada y no vacía.

`get_weather` devuelve un clima **simulado**, no en vivo. Respeta la ciudad y convierte los 22 grados Celsius del ejemplo a Fahrenheit cuando se requiere. `calculate` evalúa la expresión suministrada mediante exp4j, soporta formas como `15% of 240` y `2 + 3 * 4`, y rechaza cálculos vacíos, sobredimensionados, inválidos o no finitos. Usa aritmética de coma flotante, no precisión decimal financiera.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Espere `Function: get_weather`, clima simulado de Seattle, `Function: calculate`, `Function result: 36` y las dos respuestas finales. No se requiere stdin ni credenciales externas para clima. Una ejecución exitosa usa exactamente cuatro solicitudes de chat.

## Tutorial 3: RAG (Generación Aumentada por Recuperación)

Fuente: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Entrada: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Este ejemplo introductorio de RAG recupera un documento UTF-8 completo y lo incluye en el mensaje de usuario con la pregunta. Un mensaje de sistema separado instruye al modelo a tratar el contenido del documento como datos no confiables y responder solo desde ese contexto. Si el documento no contiene la respuesta, la respuesta solicitada es: `No puedo encontrar esa información en el documento proporcionado.`

La fundamentación puede reducir alucinaciones, pero ni delimitadores ni instrucciones del sistema garantizan exactitud ni previenen toda inyección de prompt. Revise respuestas en vivo. RAG en producción normalmente añade fragmentación, recuperación, citas, control de acceso y evaluación.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Ingrese una pregunta, por ejemplo `¿Qué método de autenticación describe el documento?`. Espere una respuesta que mencione Microsoft Entra ID. El programa termina tras una solicitud de chat con límite de completación de 500 tokens.

La búsqueda de archivo por defecto funciona desde la raíz del repositorio, el directorio del capítulo o el directorio de ejemplos. También se admite una ruta explícita:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Las entradas no deben estar vacías: máximo 32 KiB de datos UTF-8 del documento y 2.000 caracteres en la pregunta. Los archivos faltantes, preguntas vacías/EOF y entradas sobredimensionadas fallan antes de la inferencia.

## Tutorial 4: IA Responsable

Fuente: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Las seis pruebas cubren instrucciones dañinas, discurso de odio, privacidad, desinformación médica, contenido ilegal y una pregunta benignamente responsable. El programa observa la respuesta en lugar de asumir que cada prueba debe activar un filtro.

| Resultado | Evidencia |
| --- | --- |
| `FILTRADO` | Código de error explícito `content_filter` / `ResponsibleAIPolicyViolation`, o razón de fin `content_filter` en completación |
| `RECHAZADO` | Campo `message.refusal` estructurado y no vacío |
| `POSIBLE_RECHAZO` | Frase inicial de rechazo en texto común; heurística que requiere revisión |
| `GENERADO` | Respuesta completa no vacía; no prueba que el contenido sea seguro |

Un HTTP 400 común **no** es evidencia de filtrado. Parámetros inválidos, fallas de autenticación, límites de tasa, errores de servidor, respuestas mal formadas y salida truncada fallan la ejecución en lugar de producir un falso éxito de seguridad. Palabras generales como "contenido dañino" en una explicación benigna no cuentan como rechazo.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Espere seis resultados de categoría y un resumen que indique que las observaciones no son una certificación de seguridad. Cada prueba tiene un límite de completación de 300 tokens. Revise manualmente las generaciones inesperadas y posibles rechazos; la comparación benigna debería producir una explicación sustantiva de IA responsable. No se requiere stdin.

## Patrones Comunes en los Ejemplos

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centraliza la normalización del endpoint, anulaciones de despliegue, autenticación sin clave y opciones de chat:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

El proveedor de tokens renueva tokens de acceso según sea necesario. No registre tokens ni reemplace esto con una clave API. Cada programa reutiliza su cliente y lo cierra en un bloque `finally` o mediante su propio envoltorio `AutoCloseable`; el `OpenAIClient` del SDK no es `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) requiere una respuesta textual completa y no vacía. Elecciones vacías, rechazos, filtros y respuestas truncadas no se imprimen silenciosamente como éxito. El ejemplo de IA responsable maneja explícitamente los resultados esperados de filtro/rechazo. Fallas no manejadas dan un código de salida distinto de cero al proceso Java/Maven.

**Los reintentos automáticos del SDK están deshabilitados** para mantener predecible el conteo de solicitudes en despliegues compartidos con baja RPM. Cada solicitud de inferencia tiene un tiempo de espera de 60 segundos. La adquisición de tokens puede tomar tiempo adicional. La programación a nivel de aplicación debe respetar cuotas; no repita ciegamente una solicitud pagada fallida.

## Pruebas Unitarias

Desde el directorio de ejemplos:

```powershell
mvn -B -ntp clean test
```

El transporte de prueba reemplaza completamente la capa HTTP del SDK, captura los cuerpos de petición serializados reales y suministra respuestas en cola. No abre sockets, no adquiere tokens de Azure y falla ante solicitudes inesperadas. Estas pruebas validan el comportamiento de la aplicación y el protocolo SDK, no la calidad del modelo en vivo ni la disponibilidad del despliegue.

| Suite de pruebas | Cobertura |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalización/rechazo de endpoint, anulaciones de despliegue, opciones de razonamiento y tokens |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Todos los flujos de completación, historial de mensajes, recorte de turnos completos, EOF, fallas |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Esquemas de herramientas, argumentos tipados, aritmética, IDs, múltiples resultados de herramienta, seguimientos fallidos |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Búsqueda de archivos, UTF-8, límites de tamaño, carga de fundamentación, errores de entrada y API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Las seis pruebas, filtros explícitos, clasificación de rechazos, HTTP 400 común y otras fallas |

Para una suite, use `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Los fixtures compartidos están en [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Verificación Secuencial en Vivo

Las llamadas en vivo son separadas de las pruebas unitarias. Use los siguientes comandos **individualmente**, desde la raíz del repositorio, solo cuando las credenciales y el acceso al despliegue estén listos. No se requieren servicios ni procesos persistentes.

Para un despliegue compartido de **10 solicitudes/minuto**, reserve suficiente cuota para todo el programa siguiente antes de lanzarlo: 5, 4, 1 y luego 6 solicitudes. Los procesos secuenciales por sí solos no garantizan el cumplimiento del límite de tasa. Coordine el minuto rodante con todos los demás llamadores; no pegue las cuatro invocaciones como un lote sin pausa.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completaciones, múltiples turnos y dos turnos interactivos:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Verifique los tres encabezados de sección, cinco respuestas, una respuesta interactiva final que recuerda a Ada, `¡Adiós!` y el código de salida 0. Presupuesto: **5 solicitudes, como máximo 1,900 tokens de finalización**. Para una ejecución más pequeña, canalice solo `exit`: 3 solicitudes / 900 tokens, pero eso no ejercita la inferencia interactiva.

**2. Ambos flujos de trabajo de llamada a funciones:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Verifique ambos nombres de función, el clima simulado de Seattle, el resultado calculado 36, dos respuestas finales y el código de salida 0. Presupuesto: **4 solicitudes, como máximo 1,200 tokens de finalización**.

**3. Respuesta basada en documentos:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Verifique la ruta del documento, una respuesta que mencione Microsoft Entra ID y el código de salida 0. Presupuesto: **1 solicitud, como máximo 500 tokens de finalización**. El archivo de entrada único requerido es el existente [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt). Una segunda ejecución opcional que pregunte sobre un tema ausente debe abstenerse y agrega una solicitud / 500 tokens.

**4. Observaciones de IA responsable:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Verifique seis categorías y el resumen de observaciones, revise el contenido generado y requiera código de salida 0 para la finalización técnica. Una salida exitosa del proceso no certifica la seguridad del modelo. Presupuesto: **6 solicitudes, como máximo 1,800 tokens de finalización**.

**Total para los cuatro comandos: 16 solicitudes de chat y como máximo 5,400 tokens de finalización**, más tokens de entrada (incluyendo conversación repetida y esquema/historial de herramientas). No hay solicitudes de embeddings. El uso real de tokens depende del modelo y puede ser menor, especialmente para prompts filtrados. El costo en dólares depende del precio de implementación; no se implica una estimación monetaria fija. Todos los límites de solicitud asumen que no hay reejecuciones manuales. Inspeccione `$LASTEXITCODE` inmediatamente después de cada comando; distinto de cero significa que la ejecución no se completó con éxito.

## Solución de problemas

- **Falta endpoint / 401 / 403:** Configure el endpoint en el proceso de lanzamiento, verifique su inicio de sesión local de Azure y el rol con alcance de recurso, y revise que no haya anulaciones no deseadas del entorno de identidad.
- **400 / 404:** Confirme que la implementación exista y admita Chat Completions con esfuerzo de razonamiento `none`. Use la raíz HTTPS del recurso o la URL `/openai/v1`, no una URL de implementación legacy. Los errores 400 habituales son fallos técnicos, no bloqueos de seguridad.
- **429:** Coordine el RPM compartido y la cuota de tokens antes de reintentar. Los ejemplos deliberadamente no reintentan automáticamente.
- **`Respuesta de chat incompleta: longitud`:** La salida alcanzó el límite de finalización. Revise la respuesta y el prompt antes de aumentar el límite y su presupuesto documentado; no registre una ejecución truncada como exitosa.
- **Errores de archivo o entrada estándar:** Inicie desde un directorio compatible o pase una ruta de documento explícita. Proporcione una pregunta lectora no vacía. Las completaciones pueden terminar normalmente con EOF o `exit`.
- **Errores de compilación:** Verifique Java 21 o posterior, luego ejecute `mvn -B -ntp clean test`. En PowerShell, cite todo el argumento Maven que contenga una propiedad con puntos, por ejemplo `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Próximos pasos

Continúe en [Capítulo 4: Ejemplos Prácticos](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional humana. No somos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->