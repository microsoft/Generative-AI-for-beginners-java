# Tutorial de Calculadora MCP para Principiantes

## Tabla de Contenidos

- [Lo que Aprenderás](#lo-que-aprenderás)
- [Requisitos Previos](#requisitos-previos)
- [Versiones de Dependencias](#versiones-de-dependencias)
- [Entendiendo la Estructura del Proyecto](#entendiendo-la-estructura-del-proyecto)
- [Componentes Principales Explicados](#componentes-principales-explicados)
  - [1. Aplicación Principal](#1-aplicación-principal)
  - [2. Servicio de Calculadora](#2-servicio-de-calculadora)
  - [3. Cliente MCP Directo](#3-cliente-mcp-directo)
  - [4. Cliente Potenciado por AI](#4-cliente-potenciado-por-ia)
- [Ejecución de los Ejemplos](#ejecutando-los-ejemplos)
- [Pruebas Offline](#pruebas-offline)
- [Cómo Funciona Todo Junto](#cómo-funciona-todo-junto)
- [Próximos Pasos](#próximos-pasos)

## Lo que Aprenderás

Este tutorial explica cómo construir un servicio de calculadora usando el Protocolo de Contexto de Modelo (MCP). Entenderás:

- Cómo crear un servicio que la IA pueda usar como herramienta
- Cómo configurar comunicación directa con servicios MCP
- Cómo los modelos de IA pueden elegir automáticamente qué herramientas usar
- La diferencia entre llamadas directas al protocolo e interacciones asistidas por IA

## Requisitos Previos

Antes de comenzar, asegúrate de tener:
- Java 21 o superior instalado
- Maven para gestión de dependencias
- Conocimiento básico de Java y Spring Boot

Solo los clientes de IA requieren un despliegue de Azure OpenAI y un `DefaultAzureCredential` autenticado,
como un inicio de sesión existente en la CLI de Azure localmente o una identidad gestionada en Azure. La identidad necesita
el rol de Usuario de Cognitive Services OpenAI en el recurso. Ver [Capítulo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
El servidor, cliente SDK directo y todas las pruebas automatizadas no necesitan cuenta Azure ni acceso a modelos.

## Versiones de Dependencias

Dependencias verificadas en la versión del 2026-09-14:

| Dependencia | Versión |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (gestionado por Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Adaptador oficial OpenAI de LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (gestionado por Boot) | 6.0.3 |

Los adaptadores MCP y oficiales OpenAI son versiones beta publicadas en Maven Central, no snapshots.
Sus versiones difieren del núcleo LangChain4j. No se necesitan repositorios snapshot o milestone.
Las dependencias solo de cliente tienen alcance test porque los ejemplos ejecutables viven bajo `src/test/java`.

## Entendiendo la Estructura del Proyecto

El proyecto de la calculadora tiene varios archivos importantes:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Componentes Principales Explicados

### 1. Aplicación Principal

**Archivo:** `McpServerApplication.java`

Este es el punto de entrada de nuestro servicio de calculadora. Es una aplicación estándar Spring Boot con una adición especial:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Qué hace esto:**
- Inicia un servidor web Spring Boot en el puerto 8080
- Crea un `ToolCallbackProvider` que hace que nuestros métodos de calculadora estén disponibles como herramientas MCP
- La anotación `@Bean` indica a Spring que lo gestione como un componente que otras partes pueden usar

### 2. Servicio de Calculadora

**Archivo:** `CalculatorService.java`

Aquí es donde ocurre toda la matemática. Cada método está marcado con `@Tool` para hacerlo disponible a través de MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Más operaciones de calculadora...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Características clave:**

1. **Anotación `@Tool`**: Indica a MCP que este método puede ser llamado por clientes externos
2. **Descripciones Claras**: Cada herramienta tiene una descripción que ayuda a los modelos de IA a entender cuándo usarla
3. **Formato de Retorno Consistente**: Todas las operaciones devuelven cadenas legibles humanas como "5.00 + 3.00 = 8.00"
4. **Manejo de Errores**: División por cero y raíces negativas devuelven mensajes de error

**Operaciones Disponibles:**
- `add(a, b)` - Suma dos números
- `subtract(a, b)` - Resta el segundo del primero
- `multiply(a, b)` - Multiplica dos números
- `divide(a, b)` - Divide el primero por el segundo (con comprobación de cero)
- `power(base, exponent)` - Eleva la base a la potencia del exponente
- `squareRoot(number)` - Calcula la raíz cuadrada (con comprobación de negativos)
- `modulus(a, b)` - Devuelve el residuo de la división
- `absolute(number)` - Devuelve el valor absoluto
- `help()` - Devuelve información sobre todas las operaciones

### 3. Cliente MCP Directo

Ver [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Este cliente usa `HttpClientStreamableHttpTransport` en `/mcp`, inicializa la conexión,
hace ping al servidor y sigue la paginación de la lista de herramientas. Verifica que existan las nueve herramientas esperadas
y llama a cada una, incluyendo `modulus` y `help`, sin un modelo AI.

El constructor de solicitudes actual es así:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Los errores de protocolo fallan el cliente en vez de imprimir un éxito engañoso. El cliente MCP
se cierra con try-with-resources, incluso cuando falla el descubrimiento o la llamada a herramienta.

### 4. Cliente Potenciado por IA

Ver [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
y [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementa la API actual `ChatModel` de LangChain4j.
`StreamableHttpMcpTransport` lo conecta al mismo endpoint `/mcp` que el cliente SDK.
`AiServices` descubre las herramientas y gestiona la conversación de llamadas/resultados a herramientas.

El despliegue por defecto es **GPT-5.6 Luna**, con razonamiento explícitamente deshabilitado:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Estos valores predeterminados aplican a cada finalización, incluyendo seguimientos después de ejecución de herramientas.
El cliente usa un `BearerTokenCredential` renovable respaldado por `DefaultAzureCredential`
y el ámbito `https://ai.azure.com/.default`, no un token de una sola vez pasado como clave API.
Se aceptan URLs de recursos y URLs que ya terminan en `/openai/v1`.

El bot mantiene un historial de conversación acotado, imprime `Tool executed: ...` con el resultado real
MCP y falla si una respuesta omite herramientas. Los bucles de herramientas se limitan a cuatro viajes de ida y vuelta.
Se propagan errores de autenticación, modelo, MCP y herramienta; los reintentos automáticos de modelo están deshabilitados.
Tanto el transporte/cliente MCP como el cliente oficial OpenAI se cierran en éxito o fallo.

## Ejecutando los Ejemplos

### Paso 1: Iniciar el Servidor de Calculadora

No se necesita configuración Azure para el servidor. Los comandos abajo se ejecutan desde el directorio de esta muestra.
El ejemplo usa el puerto **18081** para evitar conflictos con otra muestra; el puerto predeterminado sigue siendo 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

El endpoint MCP es `http://localhost:18081/mcp`. La información de salud y descubrimiento está en
`http://localhost:18081/health` y `http://localhost:18081/info`.
El HTTP Streamable reemplaza el transporte solo SSE antiguo; `/sse` y `/v1/tools` no son endpoints.

### Paso 2: Prueba con Cliente Directo

En otra terminal PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

No se necesita entrada. Se ejercitan las nueve herramientas. Resultados aritméticos esperados incluyen
8, 6, 42, 5, 256, 4, 2 y 5.5, seguido del texto de ayuda.

### Paso 3: Prueba con Cliente de IA

Después de autenticarse como se describe en los requisitos previos, configura el cliente de IA en la misma terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Espera una línea `Tool executed: add` con `41.80`, seguida de la respuesta del modelo.
El modo prompt único sale sin esperar entrada. Para ejecutar la demostración original de cuatro prompts:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

La demo llama a `add`, `squareRoot`, `help` y las operaciones encadenadas `power` luego `divide`.
Las respuestas numéricas esperadas son 41.8, 12 y 64. Omitir argumentos también ejecuta esta demo.

### Paso 4: Ejecutar el Bot Interactivo

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Escribe `Multiply 6 by 7 using the calculator service`, luego `exit` o `quit`.
Espera un resultado real de herramienta `multiply` de 42. Las líneas en blanco se ignoran; EOF también termina la sesión.
Para una prueba rápida no interactiva de este punto de entrada:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Ambos puntos de entrada IA aceptan `--prompt "question"`, `--demo` y `--interactive`.
Opciones inválidas fallan antes de abrir conexión. Cada argumento Maven `-D...` se cita completamente
para PowerShell. En Bash, usa `export NAME=value` en lugar de `$env:NAME = "value"`.

**Cuota:** Ejecuta las muestras IA secuencialmente. Un prompt simple normalmente necesita dos solicitudes al modelo;
la demo completa normalmente necesita nueve, incluyendo seguimientos de resultados de herramientas. Con un despliegue compartido de 10 RPM,
permite una ventana de cuota fresca antes de la siguiente ejecución IA. Un 429 falla visiblemente sin
reintentos automáticos; sigue la guía retry-after del servicio. Los conteos reales dependen del modelo.
Las pruebas offline no consumen cuota y no establecen disponibilidad en vivo o calidad de respuesta de Luna.

### Configuración y Apagado

| Configuración | Predeterminado / comportamiento |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL base, sin `/mcp` |
| `-Dmcp.server.url=...` | Anula `MCP_SERVER_URL` para todos los clientes |
| `AZURE_OPENAI_ENDPOINT` | Requerido solo para clientes IA; URL del recurso o URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nombre del despliegue Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; entero positivo |
| Esfuerzo de razonamiento | Siempre `none`, incluyendo seguimientos de bucles de herramientas |

Un despliegue anulado debe soportar `reasoning_effort=none` y `max_completion_tokens`.
Los clientes no leen automáticamente un archivo `.env`. Detén el servidor con `Ctrl+C` tras probar.
Los clientes retornan normalmente sin `System.exit` o esperas de apagado.

## Pruebas Offline

```powershell
mvn -B -ntp clean verify
```

Todas las pruebas son offline con respecto a Azure: la suite de protocolo inicia un servidor Spring y
un stub compatible OpenAI en puertos locales aleatorios, luego los cierra. Maven puede necesitar
descargar dependencias. No se usan credenciales, despliegue en vivo ni servidor MCP preexistente.

- Las pruebas unitarias de calculadora cubren todas las operaciones aritméticas, resultados decimales, ayuda y errores de dominio.
- Las pruebas MCP cubren inicialización, descubrimiento, las nueve llamadas a herramientas, fallos de herramientas, y salud/info.
- Las pruebas de protocolo IA ejecutan la demo completa y Bot interactivo contra la calculadora real,
  verifican que los resultados de herramientas alimentan la siguiente finalización, e inspeccionan cada cuerpo HTTP para Luna,
  `reasoning_effort: "none"`, y `max_completion_tokens` sin `max_tokens` heredado.
- Las pruebas de configuración/entrada cubren anulación de despliegue y endpoint, líneas en blanco, EOF, exit/quit,
  modo prompt único, opciones inválidas y propagación de errores. Las pruebas de cuota prueban que 429 no se reintenta.

## Cómo Funciona Todo Junto

Este es el flujo completo cuando le preguntas a la IA "¿Cuánto es 5 + 3?":

1. **Tú** le haces la pregunta a la IA en lenguaje natural
2. **La IA** analiza tu solicitud y se da cuenta de que quieres una suma
3. **La IA** llama al servidor MCP: `add(5.0, 3.0)`
4. **El Servicio de Calculadora** realiza: `5.0 + 3.0 = 8.0`
5. **El Servicio de Calculadora** retorna: `"5.00 + 3.00 = 8.00"`
6. **La IA** recibe el resultado y formatea una respuesta natural
7. **Tú** recibes: "La suma de 5 y 3 es 8"

## Próximos Pasos

Para más ejemplos, ver [Capítulo 04: Ejemplos prácticos](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional humana. No somos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->