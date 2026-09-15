# Tutorial de Calculadora MCP para Iniciantes

## Índice

- [O que você vai aprender](#o-que-você-vai-aprender)
- [Pré-requisitos](#pré-requisitos)
- [Versões das dependências](#versões-das-dependências)
- [Entendendo a Estrutura do Projeto](#entendendo-a-estrutura-do-projeto)
- [Componentes Principais Explicados](#componentes-principais-explicados)
  - [1. Aplicação Principal](#1-aplicação-principal)
  - [2. Serviço da Calculadora](#2-serviço-da-calculadora)
  - [3. Cliente MCP Direto](#3-cliente-mcp-direto)
  - [4. Cliente Movido a IA](#4-cliente-movido-a-ia)
- [Executando os Exemplos](#executando-os-exemplos)
- [Testes Offline](#testes-offline)
- [Como Tudo Funciona Junto](#como-tudo-funciona-junto)
- [Próximos Passos](#próximos-passos)

## O que você vai aprender

Este tutorial explica como construir um serviço de calculadora usando o Protocolo de Contexto do Modelo (MCP). Você vai entender:

- Como criar um serviço que a IA pode usar como ferramenta
- Como configurar comunicação direta com serviços MCP
- Como os modelos de IA podem escolher automaticamente quais ferramentas usar
- A diferença entre chamadas diretas ao protocolo e interações assistidas por IA

## Pré-requisitos

Antes de começar, certifique-se de que você tem:
- Java 21 ou superior instalado
- Maven para gerenciamento de dependências
- Entendimento básico de Java e Spring Boot

Apenas os clientes de IA requerem uma implantação Azure OpenAI e uma `DefaultAzureCredential` autenticada,
como uma sessão do Azure CLI existente localmente ou uma identidade gerenciada no Azure. A identidade precisa
do papel de Usuário do Cognitive Services OpenAI no recurso. Veja [Capítulo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
O servidor, cliente SDK direto e todos os testes automatizados não precisam de conta Azure ou acesso a modelo.

## Versões das dependências

Dependências da release verificadas em 2026-09-14:

| Dependência | Versão |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (gerenciado pelo Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Adaptador oficial LangChain4j OpenAI | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (gerenciado pelo Boot) | 6.0.3 |

Os adaptadores MCP e OpenAI oficiais são releases beta publicados no Maven Central, não snapshots.
Suas versões diferem do núcleo LangChain4j. Nenhum repositório snapshot ou milestone é necessário.
Dependências apenas para clientes têm escopo de teste porque os exemplos executáveis vivem sob `src/test/java`.

## Entendendo a Estrutura do Projeto

O projeto da calculadora tem vários arquivos importantes:

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

**Arquivo:** `McpServerApplication.java`

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

**O que isso faz:**
- Inicia um servidor web Spring Boot na porta 8080
- Cria um `ToolCallbackProvider` que torna nossos métodos da calculadora disponíveis como ferramentas MCP
- A anotação `@Bean` informa ao Spring para gerenciar isso como um componente que outras partes podem usar

### 2. Serviço da Calculadora

**Arquivo:** `CalculatorService.java`

Aqui é onde toda a matemática acontece. Cada método é marcado com `@Tool` para torná-lo disponível via MCP:

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

**Principais características:**

1. **Anotação `@Tool`**: Indica ao MCP que este método pode ser chamado por clientes externos
2. **Descrições Claras**: Cada ferramenta tem uma descrição que ajuda os modelos de IA a entender quando usá-la
3. **Formato de Retorno Consistente**: Todas as operações retornam strings legíveis como "5.00 + 3.00 = 8.00"
4. **Tratamento de Erros**: Divisão por zero e raiz quadrada negativa retornam mensagens de erro

**Operações Disponíveis:**
- `add(a, b)` - Soma dois números
- `subtract(a, b)` - Subtrai o segundo do primeiro
- `multiply(a, b)` - Multiplica dois números
- `divide(a, b)` - Divide o primeiro pelo segundo (com verificação de zero)
- `power(base, exponent)` - Eleva a base à potência do expoente
- `squareRoot(number)` - Calcula a raiz quadrada (com verificação de negativo)
- `modulus(a, b)` - Retorna o resto da divisão
- `absolute(number)` - Retorna o valor absoluto
- `help()` - Retorna informações sobre todas as operações

### 3. Cliente MCP Direto

Veja [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Este cliente usa `HttpClientStreamableHttpTransport` em `/mcp`, inicializa a conexão,
envia ping ao servidor e segue paginação da lista de ferramentas. Verifica que todas as nove ferramentas esperadas
existem e chama cada uma, incluindo `modulus` e `help`, sem modelo de IA.

O construtor de requisição atual é assim:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Erros de protocolo fazem o cliente falhar em vez de imprimir um sucesso enganoso. O cliente MCP
é fechado com try-with-resources, inclusive quando a descoberta ou uma chamada de ferramenta falha.

### 4. Cliente Movido a IA

Veja [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
e [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementa a atual API `ChatModel` do LangChain4j.
`StreamableHttpMcpTransport` conecta-o ao mesmo endpoint `/mcp` que o cliente SDK.
`AiServices` descobre as ferramentas e gerencia a conversa de chamada/resultado de ferramentas.

A implantação padrão é **GPT-5.6 Luna**, com raciocínio explicitamente desabilitado:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Esses padrões se aplicam a cada conclusão, incluindo respostas após execução de ferramentas.
O cliente usa um `BearerTokenCredential` atualizável suportado por `DefaultAzureCredential`
e o escopo `https://ai.azure.com/.default`, não um token único passado como chave de API.
URLs de recursos e URLs já terminando em `/openai/v1` são aceitos.

O bot mantém um histórico de conversação limitado, imprime `Tool executed: ...` com o resultado real
MCP, e falha se uma resposta pular ferramentas. Loops de ferramentas são limitados a quatro interações.
Autenticação, modelo, MCP e erros de ferramenta se propagam; reintentos automáticos do modelo são desativados.
Tanto o transporte/cliente MCP quanto o cliente oficial OpenAI são fechados em sucesso ou falha.

## Executando os Exemplos

### Passo 1: Iniciar o Servidor da Calculadora

Nenhuma configuração Azure é necessária para o servidor. Comandos abaixo executam-se a partir do diretório deste exemplo.
O exemplo usa a porta **18081** para evitar conflito com outro exemplo; o padrão permanece 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

O endpoint MCP é `http://localhost:18081/mcp`. Informações de saúde e descoberta estão em
`http://localhost:18081/health` e `http://localhost:18081/info`.
HTTP Streamable substitui o antigo transporte só SSE; `/sse` e `/v1/tools` não são endpoints.

### Passo 2: Testar com Cliente Direto

Em outro terminal PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Nenhuma entrada é necessária. Todas as nove ferramentas são testadas. Resultados aritméticos esperados incluem
8, 6, 42, 5, 256, 4, 2 e 5.5, seguidos pelo texto de ajuda.

### Passo 3: Testar com Cliente IA

Após autenticar conforme descrito nos pré-requisitos, configure o cliente IA no mesmo terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Espere uma linha `Tool executed: add` com `41.80`, seguida pela resposta do modelo.
O modo single-prompt sai sem esperar entrada. Para executar a demo original de quatro prompts:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

A demo chama `add`, `squareRoot`, `help` e a operação encadeada `power` então `divide`.
As respostas numéricas esperadas são 41.8, 12 e 64. Omissão de argumentos também executa esta demo.

### Passo 4: Rodar o Bot Interativo

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Digite `Multiply 6 by 7 using the calculator service`, depois `exit` ou `quit`.
Espere um resultado verdadeiro da ferramenta `multiply` igual a 42. Linhas em branco são ignoradas; EOF também encerra a sessão.
Para um teste simples não interativo deste ponto de entrada:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Ambos os pontos de entrada IA aceitam `--prompt "question"`, `--demo` e `--interactive`.
Opções inválidas falham antes de abrir conexão. Cada argumento Maven `-D...` é totalmente citado
para PowerShell. No Bash, use `export NAME=value` em vez de `$env:NAME = "value"`.

**Quota:** Execute as amostras IA sequencialmente. Um prompt simples normalmente precisa de duas requisições ao modelo;
a demo completa normalmente necessita nove, incluindo follow-ups de resultados de ferramentas. Com uma implantação 10 RPM compartilhada,
aguarde uma janela de quota fresca antes da próxima execução IA. Um 429 falha visivelmente sem
reintentos automáticos; siga a orientação retry-after do serviço. Contagens reais dependem do modelo.
Testes offline não consomem quota e não estabelecem disponibilidade ou qualidade de resposta da Luna.

### Configuração e Desligamento

| Configuração | Padrão / comportamento |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL base, sem `/mcp` |
| `-Dmcp.server.url=...` | Substitui `MCP_SERVER_URL` para todos os clientes |
| `AZURE_OPENAI_ENDPOINT` | Necessário só para clientes IA; URL do recurso ou URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nome de implantação no Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; inteiro positivo |
| Esforço de raciocínio | Sempre `none`, incluindo follow-ups de loop de ferramenta |

Uma implantação substituída deve suportar `reasoning_effort=none` e `max_completion_tokens`.
Os clientes não leem arquivos `.env` automaticamente. Pare o servidor com `Ctrl+C` após testar.
Clientes retornam normalmente sem `System.exit` ou esperas de desligamento.

## Testes Offline

```powershell
mvn -B -ntp clean verify
```

Todos os testes são offline em relação ao Azure: o conjunto de protocolos inicia um servidor Spring e
um stub compatível com OpenAI em portas de loopback aleatórias, depois os fecha. O Maven ainda pode precisar
baixar dependências. Nenhuma credencial, implantação ativa ou servidor MCP preexistente é usado.

- Testes unitários da calculadora cobrem todas as operações aritméticas, resultados decimais, ajuda e erros do domínio.
- Testes MCP cobrem inicialização, descoberta, todas as nove chamadas de ferramentas, falhas das ferramentas e saúde/info.
- Testes de protocolo IA executam a demo completa e o Bot interativo contra a calculadora real,
  verificam que resultados de ferramentas alimentam a próxima conclusão, e inspecionam todo corpo HTTP para Luna,
  `reasoning_effort: "none"`, e `max_completion_tokens` sem `max_tokens` legado.
- Testes de configuração/entrada cobrem substituições de implantação e endpoint, linhas em branco, EOF, saída/quit,
  modo single-prompt, opções inválidas e propagação de erro. Testes de quota provam que 429 não é reintentado.

## Como Tudo Funciona Junto

Aqui está o fluxo completo quando você pergunta à IA "Quanto é 5 + 3?":

1. **Você** pergunta à IA em linguagem natural
2. **IA** analisa seu pedido e percebe que você quer adição
3. **IA** chama o servidor MCP: `add(5.0, 3.0)`
4. **Serviço da Calculadora** realiza: `5.0 + 3.0 = 8.0`
5. **Serviço da Calculadora** retorna: `"5.00 + 3.00 = 8.00"`
6. **IA** recebe o resultado e formata uma resposta natural
7. **Você** recebe: "A soma de 5 e 3 é 8"

## Próximos Passos

Para mais exemplos, veja [Capítulo 04: Exemplos práticos](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->