<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:23:03+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "br"
}
-->
# Tutorial do MCP Calculator para Iniciantes

## Índice

- [O Que Você Vai Aprender](../../../../../04-PracticalSamples/mcp/calculator)
- [Pré-requisitos](../../../../../04-PracticalSamples/mcp/calculator)
- [Entendendo a Estrutura do Projeto](../../../../../04-PracticalSamples/mcp/calculator)
- [Componentes Principais Explicados](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Aplicação Principal](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Serviço de Calculadora](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Cliente MCP Direto](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Cliente com IA](../../../../../04-PracticalSamples/mcp/calculator)
- [Executando os Exemplos](../../../../../04-PracticalSamples/mcp/calculator)
- [Como Tudo Funciona Junto](../../../../../04-PracticalSamples/mcp/calculator)
- [Próximos Passos](../../../../../04-PracticalSamples/mcp/calculator)

## O Que Você Vai Aprender

Este tutorial explica como construir um serviço de calculadora usando o Model Context Protocol (MCP). Você vai entender:

- Como criar um serviço que pode ser usado como ferramenta por IA
- Como configurar comunicação direta com serviços MCP
- Como modelos de IA podem escolher automaticamente quais ferramentas usar
- A diferença entre chamadas diretas de protocolo e interações assistidas por IA

## Pré-requisitos

Antes de começar, certifique-se de ter:
- Java 21 ou superior instalado
- Maven para gerenciamento de dependências
- Uma conta no GitHub com um token de acesso pessoal (PAT)
- Conhecimento básico de Java e Spring Boot

## Entendendo a Estrutura do Projeto

O projeto da calculadora possui vários arquivos importantes:

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

## Componentes Principais Explicados

### 1. Aplicação Principal

**Arquivo:** `McpServerApplication.java`

Este é o ponto de entrada do nosso serviço de calculadora. É uma aplicação padrão do Spring Boot com uma adição especial:

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

**O que isso faz:**
- Inicia um servidor web Spring Boot na porta 8080
- Cria um `ToolCallbackProvider` que torna nossos métodos de calculadora disponíveis como ferramentas MCP
- A anotação `@Bean` informa ao Spring para gerenciar isso como um componente que outras partes podem usar

### 2. Serviço de Calculadora

**Arquivo:** `CalculatorService.java`

Aqui é onde todos os cálculos acontecem. Cada método é marcado com `@Tool` para torná-lo disponível via MCP:

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

**Principais características:**

1. **Anotação `@Tool`**: Indica ao MCP que este método pode ser chamado por clientes externos
2. **Descrições Claras**: Cada ferramenta tem uma descrição que ajuda modelos de IA a entenderem quando usá-la
3. **Formato de Retorno Consistente**: Todas as operações retornam strings legíveis como "5.00 + 3.00 = 8.00"
4. **Tratamento de Erros**: Divisão por zero e raízes quadradas negativas retornam mensagens de erro

**Operações Disponíveis:**
- `add(a, b)` - Soma dois números
- `subtract(a, b)` - Subtrai o segundo do primeiro
- `multiply(a, b)` - Multiplica dois números
- `divide(a, b)` - Divide o primeiro pelo segundo (com verificação de zero)
- `power(base, exponent)` - Eleva a base ao expoente
- `squareRoot(number)` - Calcula a raiz quadrada (com verificação de negativo)
- `modulus(a, b)` - Retorna o resto da divisão
- `absolute(number)` - Retorna o valor absoluto
- `help()` - Retorna informações sobre todas as operações

### 3. Cliente MCP Direto

**Arquivo:** `SDKClient.java`

Este cliente se comunica diretamente com o servidor MCP sem usar IA. Ele chama manualmente funções específicas da calculadora:

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

**O que isso faz:**
1. **Conecta-se** ao servidor da calculadora em `http://localhost:8080`
2. **Lista** todas as ferramentas disponíveis (nossas funções da calculadora)
3. **Chama** funções específicas com parâmetros exatos
4. **Imprime** os resultados diretamente

**Quando usar:** Quando você sabe exatamente qual cálculo deseja realizar e quer chamá-lo programaticamente.

### 4. Cliente com IA

**Arquivo:** `LangChain4jClient.java`

Este cliente usa um modelo de IA (GPT-4o-mini) que pode decidir automaticamente quais ferramentas da calculadora usar:

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

**O que isso faz:**
1. **Cria** uma conexão com o modelo de IA usando seu token do GitHub
2. **Conecta** a IA ao nosso servidor MCP da calculadora
3. **Dá** à IA acesso a todas as ferramentas da calculadora
4. **Permite** solicitações em linguagem natural como "Calcule a soma de 24.5 e 17.3"

**A IA automaticamente:**
- Entende que você quer somar números
- Escolhe a ferramenta `add`
- Chama `add(24.5, 17.3)`
- Retorna o resultado em uma resposta natural

## Executando os Exemplos

### Passo 1: Inicie o Servidor da Calculadora

Primeiro, configure seu token do GitHub (necessário para o cliente de IA):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Inicie o servidor:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

O servidor será iniciado em `http://localhost:8080`. Você deve ver:
```
Started McpServerApplication in X.XXX seconds
```

### Passo 2: Teste com o Cliente Direto

Em um novo terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Você verá uma saída como:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Passo 3: Teste com o Cliente de IA

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Você verá a IA usando as ferramentas automaticamente:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Como Tudo Funciona Junto

Aqui está o fluxo completo quando você pergunta à IA "Quanto é 5 + 3?":

1. **Você** pergunta à IA em linguagem natural
2. **IA** analisa sua solicitação e percebe que você quer somar
3. **IA** chama o servidor MCP: `add(5.0, 3.0)`
4. **Serviço de Calculadora** realiza: `5.0 + 3.0 = 8.0`
5. **Serviço de Calculadora** retorna: `"5.00 + 3.00 = 8.00"`
6. **IA** recebe o resultado e formata uma resposta natural
7. **Você** recebe: "A soma de 5 e 3 é 8"

## Próximos Passos

Para mais exemplos, veja [Capítulo 04: Exemplos práticos](../../README.md)

**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se a tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.