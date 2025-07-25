<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:37:25+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "es"
}
-->
# Tutorial de la Calculadora MCP para Principiantes

## Tabla de Contenidos

- [Qué Aprenderás](../../../../../04-PracticalSamples/mcp/calculator)
- [Requisitos Previos](../../../../../04-PracticalSamples/mcp/calculator)
- [Entendiendo la Estructura del Proyecto](../../../../../04-PracticalSamples/mcp/calculator)
- [Componentes Principales Explicados](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Aplicación Principal](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Servicio de Calculadora](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Cliente MCP Directo](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Cliente Potenciado por IA](../../../../../04-PracticalSamples/mcp/calculator)
- [Ejecutando los Ejemplos](../../../../../04-PracticalSamples/mcp/calculator)
- [Cómo Funciona Todo Junto](../../../../../04-PracticalSamples/mcp/calculator)
- [Próximos Pasos](../../../../../04-PracticalSamples/mcp/calculator)

## Qué Aprenderás

Este tutorial explica cómo construir un servicio de calculadora utilizando el Protocolo de Contexto de Modelo (MCP). Aprenderás:

- Cómo crear un servicio que la IA pueda usar como herramienta
- Cómo configurar la comunicación directa con servicios MCP
- Cómo los modelos de IA pueden elegir automáticamente qué herramientas usar
- La diferencia entre llamadas directas al protocolo y las interacciones asistidas por IA

## Requisitos Previos

Antes de comenzar, asegúrate de tener:
- Java 21 o superior instalado
- Maven para la gestión de dependencias
- Una cuenta de GitHub con un token de acceso personal (PAT)
- Conocimientos básicos de Java y Spring Boot

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
    └── Bot.java                          # Simple chat interface
```

## Componentes Principales Explicados

### 1. Aplicación Principal

**Archivo:** `McpServerApplication.java`

Este es el punto de entrada de nuestro servicio de calculadora. Es una aplicación estándar de Spring Boot con una adición especial:

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

**Qué hace:**
- Inicia un servidor web de Spring Boot en el puerto 8080
- Crea un `ToolCallbackProvider` que hace que nuestros métodos de calculadora estén disponibles como herramientas MCP
- La anotación `@Bean` indica a Spring que gestione esto como un componente que otras partes pueden usar

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Características clave:**

1. **Anotación `@Tool`**: Indica a MCP que este método puede ser llamado por clientes externos
2. **Descripciones Claras**: Cada herramienta tiene una descripción que ayuda a los modelos de IA a entender cuándo usarla
3. **Formato Consistente de Retorno**: Todas las operaciones devuelven cadenas legibles como "5.00 + 3.00 = 8.00"
4. **Manejo de Errores**: La división por cero y las raíces cuadradas negativas devuelven mensajes de error

**Operaciones Disponibles:**
- `add(a, b)` - Suma dos números
- `subtract(a, b)` - Resta el segundo del primero
- `multiply(a, b)` - Multiplica dos números
- `divide(a, b)` - Divide el primero por el segundo (con verificación de cero)
- `power(base, exponent)` - Eleva la base a la potencia del exponente
- `squareRoot(number)` - Calcula la raíz cuadrada (con verificación de negativos)
- `modulus(a, b)` - Devuelve el resto de la división
- `absolute(number)` - Devuelve el valor absoluto
- `help()` - Devuelve información sobre todas las operaciones

### 3. Cliente MCP Directo

**Archivo:** `SDKClient.java`

Este cliente se comunica directamente con el servidor MCP sin usar IA. Llama manualmente a funciones específicas de la calculadora:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Qué hace:**
1. **Se conecta** al servidor de calculadora en `http://localhost:8080`
2. **Lista** todas las herramientas disponibles (nuestras funciones de calculadora)
3. **Llama** a funciones específicas con parámetros exactos
4. **Imprime** los resultados directamente

**Cuándo usar esto:** Cuando sabes exactamente qué cálculo quieres realizar y deseas llamarlo programáticamente.

### 4. Cliente Potenciado por IA

**Archivo:** `LangChain4jClient.java`

Este cliente utiliza un modelo de IA (GPT-4o-mini) que puede decidir automáticamente qué herramientas de calculadora usar:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Qué hace:**
1. **Crea** una conexión con el modelo de IA utilizando tu token de GitHub
2. **Conecta** la IA a nuestro servidor MCP de calculadora
3. **Da** acceso a la IA a todas nuestras herramientas de calculadora
4. **Permite** solicitudes en lenguaje natural como "Calcula la suma de 24.5 y 17.3"

**La IA automáticamente:**
- Entiende que quieres sumar números
- Elige la herramienta `add`
- Llama a `add(24.5, 17.3)`
- Devuelve el resultado en una respuesta natural

## Ejecutando los Ejemplos

### Paso 1: Inicia el Servidor de Calculadora

Primero, configura tu token de GitHub (necesario para el cliente de IA):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Inicia el servidor:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

El servidor se iniciará en `http://localhost:8080`. Deberías ver:
```
Started McpServerApplication in X.XXX seconds
```

### Paso 2: Prueba con el Cliente Directo

En una nueva terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Verás una salida como:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Paso 3: Prueba con el Cliente de IA

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Verás que la IA utiliza automáticamente las herramientas:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Cómo Funciona Todo Junto

Aquí está el flujo completo cuando le preguntas a la IA "¿Cuánto es 5 + 3?":

1. **Tú** le preguntas a la IA en lenguaje natural
2. **La IA** analiza tu solicitud y se da cuenta de que quieres sumar
3. **La IA** llama al servidor MCP: `add(5.0, 3.0)`
4. **El Servicio de Calculadora** realiza: `5.0 + 3.0 = 8.0`
5. **El Servicio de Calculadora** devuelve: `"5.00 + 3.00 = 8.00"`
6. **La IA** recibe el resultado y lo formatea en una respuesta natural
7. **Tú** recibes: "La suma de 5 y 3 es 8"

## Próximos Pasos

Para más ejemplos, consulta [Capítulo 04: Ejemplos prácticos](../../README.md)

**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por garantizar la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse como la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas que puedan surgir del uso de esta traducción.