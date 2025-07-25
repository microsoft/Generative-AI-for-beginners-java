<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T10:36:54+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "es"
}
-->
# Tutorial de Foundry Local con Spring Boot

## Tabla de Contenidos

- [Requisitos Previos](../../../../04-PracticalSamples/foundrylocal)
- [Descripción del Proyecto](../../../../04-PracticalSamples/foundrylocal)
- [Entendiendo el Código](../../../../04-PracticalSamples/foundrylocal)
  - [1. Configuración de la Aplicación (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Clase Principal de la Aplicación (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Capa de Servicio de IA (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Dependencias del Proyecto (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Cómo Funciona Todo Junto](../../../../04-PracticalSamples/foundrylocal)
- [Configurando Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Ejecutando la Aplicación](../../../../04-PracticalSamples/foundrylocal)
- [Salida Esperada](../../../../04-PracticalSamples/foundrylocal)
- [Próximos Pasos](../../../../04-PracticalSamples/foundrylocal)
- [Resolución de Problemas](../../../../04-PracticalSamples/foundrylocal)

## Requisitos Previos

Antes de comenzar este tutorial, asegúrate de tener:

- **Java 21 o superior** instalado en tu sistema
- **Maven 3.6+** para compilar el proyecto
- **Foundry Local** instalado y en ejecución

### **Instalar Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Descripción del Proyecto

Este proyecto consta de cuatro componentes principales:

1. **Application.java** - El punto de entrada principal de la aplicación Spring Boot
2. **FoundryLocalService.java** - Capa de servicio que maneja la comunicación con la IA
3. **application.properties** - Configuración para la conexión con Foundry Local
4. **pom.xml** - Dependencias de Maven y configuración del proyecto

## Entendiendo el Código

### 1. Configuración de la Aplicación (application.properties)

**Archivo:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Qué hace:**
- **base-url**: Especifica dónde está ejecutándose Foundry Local (puerto predeterminado 5273)
- **model**: Indica el modelo de IA que se utilizará para la generación de texto

**Concepto clave:** Spring Boot carga automáticamente estas propiedades y las pone a disposición de tu aplicación usando la anotación `@Value`.

### 2. Clase Principal de la Aplicación (Application.java)

**Archivo:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Qué hace:**
- `@SpringBootApplication` habilita la configuración automática de Spring Boot
- `WebApplicationType.NONE` indica que esta es una aplicación de línea de comandos, no un servidor web
- El método principal inicia la aplicación Spring

**El Ejecutor de Demostración:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**Qué hace:**
- `@Bean` crea un componente que Spring gestiona
- `CommandLineRunner` ejecuta código después de que Spring Boot se inicia
- `foundryLocalService` se inyecta automáticamente por Spring (inyección de dependencias)
- Envía un mensaje de prueba a la IA y muestra la respuesta

### 3. Capa de Servicio de IA (FoundryLocalService.java)

**Archivo:** `src/main/java/com/example/FoundryLocalService.java`

#### Inyección de Configuración:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Qué hace:**
- `@Service` indica a Spring que esta clase proporciona lógica de negocio
- `@Value` inyecta valores de configuración desde application.properties
- La sintaxis `:default-value` proporciona valores predeterminados si las propiedades no están configuradas

#### Inicialización del Cliente:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Qué hace:**
- `@PostConstruct` ejecuta este método después de que Spring crea el servicio
- Crea un cliente OpenAI que apunta a tu instancia local de Foundry Local
- La ruta `/v1` es necesaria para la compatibilidad con la API de OpenAI
- La clave de API es "unused" porque el desarrollo local no requiere autenticación

#### Método de Chat:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**Qué hace:**
- **ChatCompletionCreateParams**: Configura la solicitud de IA
  - `model`: Especifica qué modelo de IA usar
  - `addUserMessage`: Agrega tu mensaje a la conversación
  - `maxCompletionTokens`: Limita la longitud de la respuesta (ahorra recursos)
  - `temperature`: Controla la aleatoriedad (0.0 = determinista, 1.0 = creativo)
- **Llamada a la API**: Envía la solicitud a Foundry Local
- **Manejo de Respuesta**: Extrae de manera segura la respuesta de texto de la IA
- **Manejo de Errores**: Envuelve excepciones con mensajes de error útiles

### 4. Dependencias del Proyecto (pom.xml)

**Dependencias Clave:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Qué hacen:**
- **spring-boot-starter**: Proporciona funcionalidad básica de Spring Boot
- **openai-java**: SDK oficial de OpenAI para comunicación con la API
- **jackson-databind**: Maneja la serialización/deserialización JSON para llamadas a la API

## Cómo Funciona Todo Junto

Aquí está el flujo completo cuando ejecutas la aplicación:

1. **Inicio**: Spring Boot se inicia y lee `application.properties`
2. **Creación del Servicio**: Spring crea `FoundryLocalService` e inyecta valores de configuración
3. **Configuración del Cliente**: `@PostConstruct` inicializa el cliente OpenAI para conectarse a Foundry Local
4. **Ejecución de Demostración**: `CommandLineRunner` se ejecuta después del inicio
5. **Llamada a la IA**: La demostración llama a `foundryLocalService.chat()` con un mensaje de prueba
6. **Solicitud a la API**: El servicio construye y envía una solicitud compatible con OpenAI a Foundry Local
7. **Procesamiento de Respuesta**: El servicio extrae y devuelve la respuesta de la IA
8. **Visualización**: La aplicación imprime la respuesta y se cierra

## Configurando Foundry Local

Para configurar Foundry Local, sigue estos pasos:

1. **Instala Foundry Local** usando las instrucciones en la sección [Requisitos Previos](../../../../04-PracticalSamples/foundrylocal).
2. **Descarga el modelo de IA** que deseas usar, por ejemplo, `phi-3.5-mini`, con el siguiente comando:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configura el archivo application.properties** para que coincida con la configuración de tu Foundry Local, especialmente si estás usando un puerto o modelo diferente.

## Ejecutando la Aplicación

### Paso 1: Inicia Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Paso 2: Compila y Ejecuta la Aplicación
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Salida Esperada

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Próximos Pasos

Para más ejemplos, consulta [Capítulo 04: Ejemplos prácticos](../README.md)

## Resolución de Problemas

### Problemas Comunes

**"Conexión rechazada" o "Servicio no disponible"**
- Asegúrate de que Foundry Local esté ejecutándose: `foundry model list`
- Verifica que el servicio esté en el puerto 5273: Revisa `application.properties`
- Intenta reiniciar Foundry Local: `foundry model run phi-3.5-mini`

**Errores de "Modelo no encontrado"**
- Verifica los modelos disponibles: `foundry model list`
- Actualiza el nombre del modelo en `application.properties` para que coincida exactamente
- Descarga el modelo si es necesario: `foundry model run phi-3.5-mini`

**Errores de compilación con Maven**
- Asegúrate de tener Java 21 o superior: `java -version`
- Limpia y recompila: `mvn clean compile`
- Verifica la conexión a internet para descargar dependencias

**La aplicación se inicia pero no muestra salida**
- Verifica que Foundry Local esté respondiendo: Abre el navegador en `http://localhost:5273`
- Revisa los registros de la aplicación para mensajes de error específicos
- Asegúrate de que el modelo esté completamente cargado y listo

**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por garantizar la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse como la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas que puedan surgir del uso de esta traducción.