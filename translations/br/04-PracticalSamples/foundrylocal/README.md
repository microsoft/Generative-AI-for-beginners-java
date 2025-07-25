<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:22:31+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "br"
}
-->
# Tutorial do Foundry Local com Spring Boot

## Índice

- [Pré-requisitos](../../../../04-PracticalSamples/foundrylocal)
- [Visão Geral do Projeto](../../../../04-PracticalSamples/foundrylocal)
- [Entendendo o Código](../../../../04-PracticalSamples/foundrylocal)
  - [1. Configuração da Aplicação (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Classe Principal da Aplicação (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Camada de Serviço de IA (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Dependências do Projeto (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Como Tudo Funciona Junto](../../../../04-PracticalSamples/foundrylocal)
- [Configurando o Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Executando a Aplicação](../../../../04-PracticalSamples/foundrylocal)
- [Saída Esperada](../../../../04-PracticalSamples/foundrylocal)
- [Próximos Passos](../../../../04-PracticalSamples/foundrylocal)
- [Solução de Problemas](../../../../04-PracticalSamples/foundrylocal)

## Pré-requisitos

Antes de começar este tutorial, certifique-se de ter:

- **Java 21 ou superior** instalado no seu sistema
- **Maven 3.6+** para construir o projeto
- **Foundry Local** instalado e em execução

### **Instale o Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Visão Geral do Projeto

Este projeto consiste em quatro componentes principais:

1. **Application.java** - O ponto de entrada principal da aplicação Spring Boot
2. **FoundryLocalService.java** - Camada de serviço que gerencia a comunicação com a IA
3. **application.properties** - Configuração para a conexão com o Foundry Local
4. **pom.xml** - Dependências do Maven e configuração do projeto

## Entendendo o Código

### 1. Configuração da Aplicação (application.properties)

**Arquivo:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**O que isso faz:**
- **base-url**: Especifica onde o Foundry Local está sendo executado (porta padrão 5273)
- **model**: Nomeia o modelo de IA a ser usado para geração de texto

**Conceito-chave:** O Spring Boot carrega automaticamente essas propriedades e as disponibiliza para sua aplicação usando a anotação `@Value`.

### 2. Classe Principal da Aplicação (Application.java)

**Arquivo:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**O que isso faz:**
- `@SpringBootApplication` habilita a configuração automática do Spring Boot
- `WebApplicationType.NONE` informa ao Spring que esta é uma aplicação de linha de comando, não um servidor web
- O método principal inicia a aplicação Spring

**O Runner de Demonstração:**
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

**O que isso faz:**
- `@Bean` cria um componente gerenciado pelo Spring
- `CommandLineRunner` executa o código após o Spring Boot ser iniciado
- `foundryLocalService` é injetado automaticamente pelo Spring (injeção de dependência)
- Envia uma mensagem de teste para a IA e exibe a resposta

### 3. Camada de Serviço de IA (FoundryLocalService.java)

**Arquivo:** `src/main/java/com/example/FoundryLocalService.java`

#### Injeção de Configuração:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**O que isso faz:**
- `@Service` informa ao Spring que esta classe fornece lógica de negócios
- `@Value` injeta valores de configuração do application.properties
- A sintaxe `:default-value` fornece valores padrão caso as propriedades não estejam definidas

#### Inicialização do Cliente:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**O que isso faz:**
- `@PostConstruct` executa este método após o Spring criar o serviço
- Cria um cliente OpenAI que aponta para sua instância local do Foundry Local
- O caminho `/v1` é necessário para compatibilidade com a API OpenAI
- A chave de API é "unused" porque o desenvolvimento local não requer autenticação

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

**O que isso faz:**
- **ChatCompletionCreateParams**: Configura a solicitação para a IA
  - `model`: Especifica qual modelo de IA usar
  - `addUserMessage`: Adiciona sua mensagem à conversa
  - `maxCompletionTokens`: Limita o tamanho da resposta (economiza recursos)
  - `temperature`: Controla a aleatoriedade (0.0 = determinístico, 1.0 = criativo)
- **Chamada de API**: Envia a solicitação para o Foundry Local
- **Tratamento de Resposta**: Extrai a resposta de texto da IA de forma segura
- **Tratamento de Erros**: Envolve exceções com mensagens de erro úteis

### 4. Dependências do Projeto (pom.xml)

**Dependências Principais:**

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

**O que elas fazem:**
- **spring-boot-starter**: Fornece funcionalidades principais do Spring Boot
- **openai-java**: SDK oficial da OpenAI para comunicação com a API
- **jackson-databind**: Gerencia a serialização/deserialização JSON para chamadas de API

## Como Tudo Funciona Junto

Aqui está o fluxo completo quando você executa a aplicação:

1. **Inicialização**: O Spring Boot inicia e lê o `application.properties`
2. **Criação do Serviço**: O Spring cria o `FoundryLocalService` e injeta os valores de configuração
3. **Configuração do Cliente**: `@PostConstruct` inicializa o cliente OpenAI para conectar ao Foundry Local
4. **Execução da Demonstração**: `CommandLineRunner` é executado após a inicialização
5. **Chamada de IA**: A demonstração chama `foundryLocalService.chat()` com uma mensagem de teste
6. **Solicitação de API**: O serviço constrói e envia uma solicitação compatível com OpenAI para o Foundry Local
7. **Processamento de Resposta**: O serviço extrai e retorna a resposta da IA
8. **Exibição**: A aplicação imprime a resposta e encerra

## Configurando o Foundry Local

Para configurar o Foundry Local, siga estas etapas:

1. **Instale o Foundry Local** usando as instruções na seção [Pré-requisitos](../../../../04-PracticalSamples/foundrylocal).
2. **Baixe o modelo de IA** que você deseja usar, por exemplo, `phi-3.5-mini`, com o seguinte comando:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configure o arquivo application.properties** para corresponder às configurações do seu Foundry Local, especialmente se você estiver usando uma porta ou modelo diferente.

## Executando a Aplicação

### Etapa 1: Inicie o Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Etapa 2: Construa e Execute a Aplicação
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Saída Esperada

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

## Próximos Passos

Para mais exemplos, veja [Capítulo 04: Exemplos práticos](../README.md)

## Solução de Problemas

### Problemas Comuns

**"Connection refused" ou "Service unavailable"**
- Certifique-se de que o Foundry Local está em execução: `foundry model list`
- Verifique se o serviço está na porta 5273: Confira o `application.properties`
- Tente reiniciar o Foundry Local: `foundry model run phi-3.5-mini`

**Erros de "Model not found"**
- Verifique os modelos disponíveis: `foundry model list`
- Atualize o nome do modelo no `application.properties` para corresponder exatamente
- Baixe o modelo, se necessário: `foundry model run phi-3.5-mini`

**Erros de compilação no Maven**
- Certifique-se de que o Java 21 ou superior está instalado: `java -version`
- Limpe e reconstrua: `mvn clean compile`
- Verifique a conexão com a internet para baixar dependências

**A aplicação inicia, mas não exibe saída**
- Verifique se o Foundry Local está respondendo: Abra o navegador em `http://localhost:5273`
- Confira os logs da aplicação para mensagens de erro específicas
- Certifique-se de que o modelo está totalmente carregado e pronto

**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se a tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.