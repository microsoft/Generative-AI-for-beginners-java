<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T10:36:19+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "es"
}
-->
# Tutorial de Técnicas Fundamentales de IA Generativa

## Tabla de Contenidos

- [Requisitos previos](../../../03-CoreGenerativeAITechniques)
- [Primeros pasos](../../../03-CoreGenerativeAITechniques)
  - [Paso 1: Configura tu variable de entorno](../../../03-CoreGenerativeAITechniques)
  - [Paso 2: Navega al directorio de ejemplos](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: Completaciones y Chat con LLM](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Llamadas a funciones](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Generación Aumentada por Recuperación)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: IA Responsable](../../../03-CoreGenerativeAITechniques)
- [Patrones comunes en los ejemplos](../../../03-CoreGenerativeAITechniques)
- [Próximos pasos](../../../03-CoreGenerativeAITechniques)
- [Resolución de problemas](../../../03-CoreGenerativeAITechniques)
  - [Problemas comunes](../../../03-CoreGenerativeAITechniques)

## Descripción general

Este tutorial ofrece ejemplos prácticos de técnicas fundamentales de IA generativa utilizando Java y GitHub Models. Aprenderás a interactuar con Modelos de Lenguaje Extenso (LLMs), implementar llamadas a funciones, usar generación aumentada por recuperación (RAG) y aplicar prácticas de IA responsable.

## Requisitos previos

Antes de comenzar, asegúrate de tener:
- Java 21 o superior instalado
- Maven para la gestión de dependencias
- Una cuenta de GitHub con un token de acceso personal (PAT)

## Primeros pasos

### Paso 1: Configura tu variable de entorno

Primero, necesitas configurar tu token de GitHub como una variable de entorno. Este token te permite acceder a GitHub Models de forma gratuita.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Paso 2: Navega al directorio de ejemplos

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: Completaciones y Chat con LLM

**Archivo:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Qué enseña este ejemplo

Este ejemplo demuestra los mecanismos fundamentales de interacción con Modelos de Lenguaje Extenso (LLM) a través de la API de OpenAI, incluyendo la inicialización del cliente con GitHub Models, patrones de estructura de mensajes para indicaciones del sistema y del usuario, gestión del estado de la conversación mediante la acumulación de historial de mensajes, y ajuste de parámetros para controlar la longitud de las respuestas y los niveles de creatividad.

### Conceptos clave del código

#### 1. Configuración del cliente
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Esto crea una conexión con GitHub Models utilizando tu token.

#### 2. Completación simple
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Memoria de conversación
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

La IA recuerda mensajes previos solo si los incluyes en solicitudes posteriores.

### Ejecuta el ejemplo
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Qué sucede cuando lo ejecutas

1. **Completación simple**: La IA responde una pregunta sobre Java con orientación del sistema.
2. **Chat de múltiples turnos**: La IA mantiene el contexto a lo largo de varias preguntas.
3. **Chat interactivo**: Puedes tener una conversación real con la IA.

## Tutorial 2: Llamadas a funciones

**Archivo:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Qué enseña este ejemplo

Las llamadas a funciones permiten que los modelos de IA soliciten la ejecución de herramientas externas y APIs mediante un protocolo estructurado donde el modelo analiza solicitudes en lenguaje natural, determina las llamadas a funciones necesarias con parámetros adecuados utilizando definiciones de JSON Schema, y procesa los resultados devueltos para generar respuestas contextuales, mientras que la ejecución real de las funciones permanece bajo control del desarrollador para garantizar seguridad y confiabilidad.

### Conceptos clave del código

#### 1. Definición de funciones
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Esto indica a la IA qué funciones están disponibles y cómo usarlas.

#### 2. Flujo de ejecución de funciones
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Implementación de funciones
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Ejecuta el ejemplo
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Qué sucede cuando lo ejecutas

1. **Función de clima**: La IA solicita datos meteorológicos para Seattle, tú los proporcionas, y la IA formatea una respuesta.
2. **Función de calculadora**: La IA solicita un cálculo (15% de 240), tú lo realizas, y la IA explica el resultado.

## Tutorial 3: RAG (Generación Aumentada por Recuperación)

**Archivo:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Qué enseña este ejemplo

La Generación Aumentada por Recuperación (RAG) combina recuperación de información con generación de lenguaje al inyectar contexto de documentos externos en las indicaciones de la IA, permitiendo que los modelos proporcionen respuestas precisas basadas en fuentes de conocimiento específicas en lugar de datos de entrenamiento potencialmente desactualizados o inexactos, mientras se mantienen límites claros entre las consultas del usuario y las fuentes de información autorizadas mediante ingeniería estratégica de indicaciones.

### Conceptos clave del código

#### 1. Carga de documentos
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Inyección de contexto
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Las comillas triples ayudan a la IA a distinguir entre contexto y pregunta.

#### 3. Manejo seguro de respuestas
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Siempre valida las respuestas de la API para evitar fallos.

### Ejecuta el ejemplo
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Qué sucede cuando lo ejecutas

1. El programa carga `document.txt` (contiene información sobre GitHub Models).
2. Haces una pregunta sobre el documento.
3. La IA responde basándose únicamente en el contenido del documento, no en su conocimiento general.

Prueba preguntar: "¿Qué son GitHub Models?" vs "¿Cómo está el clima?"

## Tutorial 4: IA Responsable

**Archivo:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Qué enseña este ejemplo

El ejemplo de IA Responsable muestra la importancia de implementar medidas de seguridad en aplicaciones de IA. Demuestra filtros de seguridad que detectan categorías de contenido dañino, incluyendo discurso de odio, acoso, autolesiones, contenido sexual y violencia, mostrando cómo las aplicaciones de IA en producción deben manejar de manera adecuada las violaciones de políticas de contenido mediante manejo de excepciones, mecanismos de retroalimentación al usuario y estrategias de respuesta alternativa.

### Conceptos clave del código

#### 1. Marco de pruebas de seguridad
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Categorías de seguridad probadas
- Instrucciones de violencia/daño
- Discurso de odio
- Violaciones de privacidad
- Desinformación médica
- Actividades ilegales

### Ejecuta el ejemplo
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Qué sucede cuando lo ejecutas

El programa prueba varios mensajes dañinos y muestra cómo el sistema de seguridad de IA:
1. **Bloquea solicitudes peligrosas** con errores HTTP 400.
2. **Permite contenido seguro** para ser generado normalmente.
3. **Protege a los usuarios** de salidas dañinas de la IA.

## Patrones comunes en los ejemplos

### Patrón de autenticación
Todos los ejemplos utilizan este patrón para autenticarse con GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Patrón de manejo de errores
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Patrón de estructura de mensajes
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Próximos pasos

[Capítulo 04: Ejemplos prácticos](../04-PracticalSamples/README.md)

## Resolución de problemas

### Problemas comunes

**"GITHUB_TOKEN no configurado"**
- Asegúrate de configurar la variable de entorno.
- Verifica que tu token tenga el alcance `models:read`.

**"Sin respuesta de la API"**
- Revisa tu conexión a internet.
- Verifica que tu token sea válido.
- Comprueba si has alcanzado los límites de uso.

**Errores de compilación con Maven**
- Asegúrate de tener Java 21 o superior.
- Ejecuta `mvn clean compile` para actualizar las dependencias.

**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por garantizar la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse como la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas que puedan surgir del uso de esta traducción.