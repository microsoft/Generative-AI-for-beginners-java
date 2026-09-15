# Tutorial do Calculador MCP para Iniciantes

## Índice

- [O Que Vai Aprender](#o-que-vai-aprender)
- [Pré-requisitos](#pré-requisitos)
- [Versões das Dependências](#versões-das-dependências)
- [Compreender a Estrutura do Projeto](#compreender-a-estrutura-do-projeto)
- [Componentes Principais Explicados](#componentes-principais-explicados)
  - [1. Aplicação Principal](#1-aplicação-principal)
  - [2. Serviço de Calculadora](#2-serviço-de-calculadora)
  - [3. Cliente MCP Direto](#3-cliente-mcp-direto)
  - [4. Cliente Potenciado por IA](#4-cliente-potenciado-por-ia)
- [Executar os Exemplos](#executar-os-exemplos)
- [Testes Offline](#testes-offline)
- [Como Tudo Funciona em Conjunto](#como-tudo-funciona-em-conjunto)
- [Próximos Passos](#próximos-passos)

## O Que Vai Aprender

Este tutorial explica como construir um serviço de calculadora usando o Protocolo de Contexto de Modelo (MCP). Vai entender:

- Como criar um serviço que a IA pode usar como uma ferramenta
- Como configurar comunicação direta com serviços MCP
- Como os modelos de IA podem escolher automaticamente quais ferramentas usar
- A diferença entre chamadas diretas ao protocolo e interações assistidas por IA

## Pré-requisitos

Antes de começar, certifique-se de que tem:
- Java 21 ou superior instalado
- Maven para gestão de dependências
- Conhecimentos básicos de Java e Spring Boot

Só os clientes IA requerem um deployment Azure OpenAI e um `DefaultAzureCredential` autenticado,
como um login existente do Azure CLI localmente ou uma identidade gerida no Azure. A identidade precisa
do papel Utilizador de OpenAI nos Serviços Cognitivos no recurso. Veja o [Capítulo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
O servidor, cliente SDK direto, e todos os testes automáticos não precisam de conta Azure nem acesso a modelos.

## Versões das Dependências

Dependências da release verificadas a 2026-09-14:

| Dependência | Versão |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (gerido pelo Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Adaptador oficial LangChain4j OpenAI | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (gerido pelo Boot) | 6.0.3 |

Os adaptadores MCP e oficiais OpenAI são lançamentos beta publicados no Maven Central, não snapshots.
As suas versões diferem do core LangChain4j. Não são necessários repositórios snapshot ou milestone.
As dependências exclusivas do cliente têm escopo de teste porque os exemplos executáveis vivem em `src/test/java`.

## Compreender a Estrutura do Projeto

O projeto da calculadora tem vários ficheiros importantes:

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

## Componentes Principais Explicados

### 1. Aplicação Principal

**Ficheiro:** `McpServerApplication.java`

Este é o ponto de entrada do nosso serviço de calculadora. É uma aplicação padrão Spring Boot com uma adição especial:

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

**O que isto faz:**
- Inicia um servidor web Spring Boot na porta 8080
- Cria um `ToolCallbackProvider` que torna os nossos métodos de calculadora disponíveis como ferramentas MCP
- A anotação `@Bean` diz ao Spring para gerir isto como um componente que outras partes podem usar

### 2. Serviço de Calculadora

**Ficheiro:** `CalculatorService.java`

Aqui é onde toda a matemática acontece. Cada método é marcado com `@Tool` para ficar disponível via MCP:

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
    
    // Mais operações da calculadora...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Características principais:**

1. **Anotação `@Tool`**: Diz ao MCP que este método pode ser chamado por clientes externos
2. **Descrições Claras**: Cada ferramenta tem uma descrição que ajuda os modelos de IA a perceber quando a usar
3. **Formato Consistente de Retorno**: Todas as operações retornam strings legíveis por humanos como "5.00 + 3.00 = 8.00"
4. **Gestão de Erros**: Divisão por zero e raízes quadradas negativas retornam mensagens de erro

**Operações Disponíveis:**
- `add(a, b)` - Soma dois números
- `subtract(a, b)` - Subtrai o segundo do primeiro
- `multiply(a, b)` - Multiplica dois números
- `divide(a, b)` - Divide o primeiro pelo segundo (com verificação de zero)
- `power(base, exponent)` - Eleva a base à potência do expoente
- `squareRoot(number)` - Calcula raiz quadrada (com verificação de negativos)
- `modulus(a, b)` - Retorna o resto da divisão
- `absolute(number)` - Retorna valor absoluto
- `help()` - Retorna informações sobre todas as operações

### 3. Cliente MCP Direto

Veja [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Este cliente usa `HttpClientStreamableHttpTransport` em `/mcp`, inicializa a ligação,
envia ping ao servidor, e segue a paginação da lista de ferramentas. Verifica que as nove ferramentas esperadas
existem e chama cada uma delas, incluindo `modulus` e `help`, sem um modelo IA.

O construtor de pedido atual é assim:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Erros de protocolo falham o cliente em vez de apresentar um sucesso enganador. O cliente MCP
é fechado com try-with-resources, inclusive quando a descoberta ou uma chamada de ferramenta falha.

### 4. Cliente Potenciado por IA

Veja [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
e [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementa a API atual LangChain4j `ChatModel`.
`StreamableHttpMcpTransport` liga-o ao mesmo endpoint `/mcp` do cliente SDK.
`AiServices` descobre as ferramentas e gere a conversa de chamada/resultado de ferramentas.

O deployment padrão é **GPT-5.6 Luna**, com raciocínio explicitamente desativado:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Estes padrões aplicam-se a cada completamento, incluindo seguimentos após execução de ferramenta.
O cliente usa um `BearerTokenCredential` refrescável suportado por `DefaultAzureCredential`
e o escopo `https://ai.azure.com/.default`, não um token único passado como chave API.
URLs de recursos e URLs que já terminam em `/openai/v1` são ambos aceites.

O bot mantém um histórico de conversação limitado, imprime `Tool executed: ...` com o resultado real
MCP, e falha se uma resposta saltar ferramentas. Ciclos de ferramentas são limitados a quatro trocas.
Autenticação, modelo, MCP, e erros de ferramenta propagam; tentativas automáticas de modelo estão desativadas.
Tanto o transporte/cliente MCP como o cliente oficial OpenAI fecham-se em sucesso ou falha.

## Executar os Exemplos

### Passo 1: Iniciar o Servidor da Calculadora

Não é necessária configuração Azure para o servidor. Os comandos abaixo correm a partir da diretoria deste exemplo.
O exemplo usa a porta **18081** para evitar conflito com outro exemplo; o padrão continua a ser 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

O endpoint MCP é `http://localhost:18081/mcp`. Informação de saúde e descoberta está em
`http://localhost:18081/health` e `http://localhost:18081/info`.
HTTP Streamable substitui o antigo transporte só SSE; `/sse` e `/v1/tools` não são endpoints.

### Passo 2: Testar com Cliente Direto

Noutro terminal PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Não é necessário input. Todas as nove ferramentas são usadas. Resultados aritméticos esperados incluem
8, 6, 42, 5, 256, 4, 2, e 5.5, seguidos do texto de ajuda.

### Passo 3: Testar com Cliente IA

Depois de autenticar conforme descrito nos pré-requisitos, configure o cliente IA no mesmo terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Espere a linha `Tool executed: add` com `41.80`, seguida da resposta do modelo.
O modo prompt único sai sem esperar por input. Para correr a demo original de quatro prompts:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

A demo chama `add`, `squareRoot`, `help`, e a operação encadeada `power` e depois `divide`.
Respostas numéricas esperadas são 41.8, 12, e 64. Omitir argumentos também executa esta demo.

### Passo 4: Executar o Bot Interativo

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Insira `Multiply 6 by 7 using the calculator service`, depois `exit` ou `quit`.
Espere um resultado real da ferramenta `multiply` de 42. Linhas em branco são ignoradas; EOF também termina a sessão.
Para um teste não interativo deste ponto de entrada:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Ambos os pontos de entrada IA aceitam `--prompt "question"`, `--demo`, e `--interactive`.
Opções inválidas falham antes de abrir ligação. Cada argumento `-D...` do Maven é totalmente citado
para PowerShell. No Bash, use `export NAME=value` em vez de `$env:NAME = "value"`.

**Quota:** Execute os exemplos IA sequencialmente. Um prompt simples normalmente precisa de dois pedidos ao modelo;
a demo completa normalmente precisa de nove, incluindo seguimentos pós resultado da ferramenta. Com um deployment partilhado de 10 RPM,
aguarde a abertura de uma nova janela de quota antes da próxima execução IA. Um erro 429 falha visivelmente sem
tentativas automáticas; siga as indicações retry-after do serviço. Contagens reais de pedidos dependem do modelo.
Testes offline não consomem quota e não estabelecem disponibilidade ou qualidade de respostas em Luna.

### Configuração e Encerramento

| Configuração | Padrão / comportamento |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL base, sem `/mcp` |
| `-Dmcp.server.url=...` | Substitui o `MCP_SERVER_URL` para todos os clientes |
| `AZURE_OPENAI_ENDPOINT` | Apenas requerido para clientes IA; URL do recurso ou URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nome do deployment Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; inteiro positivo |
| Esforço de raciocínio | Sempre `none`, incluindo seguimentos de ciclos de ferramenta |

Um deployment substituído deve suportar `reasoning_effort=none` e `max_completion_tokens`.
Os clientes não leem automaticamente ficheiros `.env`. Pare o servidor com `Ctrl+C` após o teste.
Os clientes retornam normalmente sem `System.exit` ou pausas no encerramento.

## Testes Offline

```powershell
mvn -B -ntp clean verify
```

Todos os testes são offline relativamente ao Azure: a suite de protocolo inicia um servidor Spring e
um stub compatível com OpenAI em portas loopback aleatórias, depois fecha-os. O Maven pode ainda precisar
de descarregar dependências. Não são usadas credenciais, deployments live, ou servidor MCP pré-existente.

- Testes unitários da calculadora cobrem todas as operações aritméticas, resultados decimais, ajuda, e erros de domínio.
- Testes MCP cobrem inicialização, descoberta, as nove chamadas de ferramenta, falhas de ferramentas, e saúde/info.
- Testes do protocolo IA executam a demo completa e Bot interativo contra a calculadora real,
  verificam que resultados de ferramentas alimentam o próximo completamento, e inspecionam todos os corpos HTTP para Luna,
  `reasoning_effort: "none"`, e `max_completion_tokens` sem `max_tokens` legado.
- Testes de configuração/input cobrem overrides de deployment e endpoint, linhas em branco, EOF, exit/quit,
  modo prompt único, opções inválidas, e propagação de erro. Testes de quota provam que 429 não é refeito.

## Como Tudo Funciona em Conjunto

Aqui está o fluxo completo quando pergunta à IA "Quanto é 5 + 3?":

1. **Você** pergunta à IA em linguagem natural
2. **IA** analisa o pedido e percebe que quer adição
3. **IA** chama o servidor MCP: `add(5.0, 3.0)`
4. **Serviço de Calculadora** executa: `5.0 + 3.0 = 8.0`
5. **Serviço de Calculadora** retorna: `"5.00 + 3.00 = 8.00"`
6. **IA** recebe o resultado e formata uma resposta natural
7. **Você** obtém: "A soma de 5 e 3 é 8"

## Próximos Passos

Para mais exemplos, veja o [Capítulo 04: Exemplos práticos](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->