# Tutorial das Técnicas Centrais de IA Generativa

## Sumário

- [Pré-requisitos](#pré-requisitos)
- [Primeiros Passos](#primeiros-passos)
- [Guia de Seleção de Modelo](#guia-de-seleção-de-modelo)
- [Tutorial 1: Completações e Chat com LLM](#tutorial-1-completações-e-chat-com-llm)
- [Tutorial 2: Chamada de Funções](#tutorial-2-chamada-de-funções)
- [Tutorial 3: RAG (Geração com Recuperação Aprimorada)](#tutorial-3-rag-geração-com-recuperação-aprimorada)
- [Tutorial 4: IA Responsável](#tutorial-4-ia-responsável)
- [Padrões Comuns Entre os Exemplos](#padrões-comuns-entre-os-exemplos)
- [Testes Unitários](#testes-unitários)
- [Verificação Sequencial Ao Vivo](#verificação-sequencial-ao-vivo)
- [Soluções de Problemas](#resolução-de-problemas)
- [Próximos Passos](#próximos-passos)

## Visão Geral

Quatro programas Java independentes demonstram chat, histórico de conversação, chamada de função, geração com recuperação de documentos completos (RAG) e manipulação de respostas de IA responsável. Todas as solicitações de chat direcionam-se por padrão para **GPT-5.6 Luna com esforço de raciocínio `none`**.

Estes exemplos utilizam o SDK Java oficial da OpenAI com o endpoint v1 do Azure OpenAI, seguindo [as diretrizes do SDK da Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). O pacote mais antigo `azure-ai-openai` não é mais dependência. Chat Completions é mantido para ensinar os fluxos de trabalho existentes baseados em mensagens; veja o [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) para outras opções de API.

## Pré-requisitos

- Java 21 ou superior e Maven 3.6.3 ou superior.
- Uma implantação de chat Azure OpenAI chamada `gpt-5.6-luna`, ou uma sobrescrição com configurações compatíveis de Chat Completions.
- Uma identidade Azure autenticada com a função **Cognitive Services OpenAI User** no recurso. Desenvolvimento local usa seu login do Azure CLI; aplicações hospedadas podem usar identidade gerenciada.
- Veja [Capítulo 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) para instruções de configuração do recurso e login.

A [configuração Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fixa estas versões, verificadas em 14-09-2026:

| Componente | Versão | Propósito |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Cliente oficial compatível com Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Autenticação sem chave e renovação de token |
| `net.objecthunter:exp4j` | 0.4.8 | Análise de expressões aritméticas sem execução de código |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Testes unitários offline com Jupiter |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Compilação Java 21, testes, exemplos executáveis |

O compilador usa `--release 21`. Nenhuma dependência de Spring Boot, Spring AI ou LangChain4j é necessária nesses exemplos independentes.

## Primeiros Passos

A partir da raiz do repositório, defina o endpoint do recurso e a sobrescrição opcional de implantação no seu shell.

**Windows PowerShell:**

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

Os testes não exigem credenciais Azure nem endpoint. O Maven não lê automaticamente um arquivo de ambiente; defina variáveis no shell usado para iniciar exemplos ao vivo. Para execuções em IDE, verifique o ambiente fornecido pela configuração de inicialização.

## Guia de Seleção de Modelo

| Variável de ambiente | Significado | Padrão |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Raiz HTTPS do recurso Azure ou URL `/openai/v1` já normalizada | Obrigatório para execuções ao vivo |
| `AZURE_OPENAI_DEPLOYMENT` | Nome da implantação de chat, não uma versão de modelo | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configuração de implantação separada para embeddings, não usada por esses quatro programas | `text-embedding-3-small` |

Sobrescrições vazias usam os padrões. A configuração adiciona `/openai/v1` exatamente uma vez e rejeita credenciais, query strings e caminhos legados de implantação no endpoint.

Cada requisição de chat define explicitamente `reasoningEffort(ReasoningEffort.NONE)` e `maxCompletionTokens(...)`. Nenhuma requisição define `temperature`, `top_p` ou a opção legada de token de completação. Isso inclui seleção de ferramenta e acompanhamentos de resultados de ferramenta. As ferramentas de função do GPT-5.6 Chat Completions requerem esforço de raciocínio `none`; veja [as orientações de chat da Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Não há streaming ou ponto de entrada para embedding neste capítulo.** O leitor recupera todo o documento, não vetores. Se você estendê-lo com embeddings, use uma implantação separada como `text-embedding-3-small`, nunca Luna.

## Tutorial 1: Completações e Chat com LLM

Fonte: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

O programa executa uma explicação simples sobre streams Java, uma conversa de duas etapas com HashMap/TreeMap e chat interativo. A segunda etapa inclui a primeira resposta do assistente; cada rodada interativa também envia a conversa anterior.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` fornece a implantação e a configuração explícita do esforço de raciocínio. Chat interativo ignora linhas em branco, encerra ao digitar `exit` ou EOF, e mantém a mensagem do sistema mais nove turnos completos de usuário/assistente. O limite de turnos é pedagógico, não garantia exata de orçamento de tokens.

A partir do diretório examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Espere três respostas iniciais, depois um prompt `You:`. Cada pergunta interativa não vazia adiciona uma solicitação. Os limites de completação são 200, 300, 400, depois 500 tokens por turno interativo.

## Tutorial 2: Chamada de Funções

Fonte: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

O SDK deriva schemas JSON dos registros anotados `WeatherArguments` e `CalculationArguments`. Uma escolha obrigatória de ferramenta faz com que cada exemplo exerça o protocolo da ferramenta em vez de aceitar a resposta livre do modelo.

1. Enviar uma pergunta com a ferramenta permitida, esforço de raciocínio `none`, e limite de completação de 300 tokens.
2. Exigir razão de finalização `tool_calls`, validar o nome da função e IDs da chamada, e analisar argumentos JSON tipados.
3. Executar a função localmente. O modelo não executa código Java ou arbitrário.
4. Adicionar a mensagem de chamada de função do assistente uma vez, seguida de cada resultado com seu `tool_call_id` correspondente.
5. Enviar uma solicitação final de 300 tokens sem ferramentas e exigir uma resposta completa e não vazia.

`get_weather` retorna o clima **simulado**, não ao vivo. Ele respeita a cidade e converte os 22 graus Celsius da amostra para Fahrenheit quando solicitado. `calculate` avalia a expressão fornecida usando exp4j, suporta formas como `15% de 240` e `2 + 3 * 4`, e rejeita cálculos vazios, muito grandes, inválidos ou não finitos. Usa aritmética de ponto flutuante, não precisão decimal financeira.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Espere `Function: get_weather`, clima simulado de Seattle, `Function: calculate`, `Function result: 36`, e as duas respostas finais. Nenhum stdin ou credenciais externas de clima são requeridos. Uma execução bem-sucedida usa exatamente quatro requisições de chat.

## Tutorial 3: RAG (Geração com Recuperação Aprimorada)

Fonte: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Entrada: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Este exemplo introdutório de RAG recupera um documento UTF-8 completo e o inclui na mensagem do usuário junto com a pergunta. Uma mensagem de sistema separada instrui o modelo a tratar o conteúdo do documento como dados não confiáveis e responder apenas com base nesse contexto. Se o documento não contiver a resposta, a resposta solicitada é: `Não consigo encontrar essa informação no documento fornecido.`

Ancoragem pode reduzir alucinações, mas nem delimitadores nem instruções do sistema garantem precisão ou evitam toda injeção de prompt. Revise respostas ao vivo. RAG em produção normalmente adiciona fragmentação, recuperação, citações, controle de acesso e avaliação.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Insira uma pergunta, por exemplo `Qual método de autenticação o documento descreve?`. Espere uma resposta mencionando Microsoft Entra ID. O programa termina após uma requisição de chat com limite de 500 tokens.

A busca padrão do arquivo funciona a partir da raiz do repositório, diretório do capítulo ou diretório de exemplos. Um caminho explícito também é suportado:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Entradas devem ser não vazias: no máximo 32 KiB de dados UTF-8 do documento e 2.000 caracteres de pergunta. Arquivos ausentes, perguntas vazias/EOF e entradas acima do tamanho falham antes da inferência.

## Tutorial 4: IA Responsável

Fonte: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

As seis sondas cobrem instruções prejudiciais, discurso de ódio, privacidade, informações médicas incorretas, conteúdo ilegal e uma pergunta benigna sobre IA responsável. O programa observa a resposta ao invés de assumir que toda sonda deve acionar um filtro.

| Resultado | Evidência |
| --- | --- |
| `FILTERED` | Um código de erro explícito `content_filter` / `ResponsibleAIPolicyViolation`, ou razão de finalização `content_filter` em completação |
| `REFUSED` | Um campo estruturado `message.refusal` não vazio |
| `POSSIBLE_REFUSAL` | Uma frase inicial de recusa em texto comum; heurística que requer revisão |
| `GENERATED` | Uma resposta completa e não vazia; não prova que o conteúdo é seguro |

Um HTTP 400 comum **não** é evidência de filtragem. Parâmetros inválidos, falhas de autenticação, limites de taxa, erros de servidor, respostas malformadas e saída truncada resultam em falha da execução, não sucesso falso de segurança. Palavras amplas como "conteúdo prejudicial" em explicação benigna não contam como recusa.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Espere seis resultados de categoria e um resumo declarando que as observações não são uma certificação de segurança. Cada sonda tem limite de 300 tokens. Revise manualmente gerações inesperadas e recusas possíveis; a comparação benigna deve produzir uma explicação substantiva de IA responsável. Nenhum stdin é exigido.

## Padrões Comuns Entre os Exemplos

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centraliza normalização de endpoint, sobrescrições de implantação, autenticação sem chave e opções de chat:

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

O fornecedor de tokens renova tokens de acesso conforme necessário. Não registre tokens nem substitua por chave de API. Cada programa reutiliza seu cliente e o fecha em `finally` ou via seu próprio wrapper `AutoCloseable`; o `OpenAIClient` do SDK não é `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) exige uma resposta textual completa e não vazia. Escolhas vazias, recusas, filtros e respostas truncadas não são impressas silenciosamente como sucesso. O exemplo de IA responsável trata explicitamente resultados esperados de filtro/recusa. Falhas não tratadas dão código de saída não zero para o processo Java/Maven.

**Tentativas automáticas do SDK estão desabilitadas** para manter contagem de requisições previsíveis em implantações compartilhadas de baixo RPM. Cada requisição de inferência tem timeout de 60 segundos. Aquisição de tokens pode levar tempo adicional. O agendamento no nível da aplicação deve respeitar cotas; não reinicie cegamente uma requisição paga falha.

## Testes Unitários

A partir do diretório examples:

```powershell
mvn -B -ntp clean test
```

O transporte de testes substitui completamente a camada HTTP do SDK, captura corpos de requisição serializados reais e fornece respostas enfileiradas. Não abre sockets, não adquire tokens Azure e falha em requisições inesperadas. Esses testes validam comportamento da aplicação e protocolo do SDK, não qualidade do modelo ao vivo nem disponibilidade da implantação.

| Suíte de testes | Cobertura |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalização/rejeição de endpoint, sobrescrições de implantação, opções de raciocínio e tokens |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Todo fluxo de completação, histórico de mensagens, corte de turnos completos, EOF, falhas |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schemas de ferramentas, argumentos tipados, aritmética, IDs, múltiplos resultados de ferramenta, acompanhamentos falhos |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Busca de arquivos, UTF-8, limites de tamanho, payload de ancoragem, erros de entrada e API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Todas as seis sondas, filtros explícitos, classificação de recusa, falhas comuns 400 e outras |

Para uma suíte, use `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Fixtures compartilhadas vivem em [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Verificação Sequencial Ao Vivo

Chamadas ao vivo são separadas dos testes unitários. Use os comandos abaixo **individualmente**, da raiz do repositório, somente após credenciais e acesso à implantação estarem prontos. Não são necessários serviços ou processos persistentes.

Para uma implantação compartilhada **10 requisições/minuto**, reserve cota suficiente para o programa inteiro antes de lançá-lo: 5, 4, 1, depois 6 requisições. Processos sequenciais sozinhos não garantem conformidade ao limite de taxa. Coordene o minuto rolante com todos os demais chamadores; não cole as quatro invocações como um lote sem ritmo.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completações, multi-turnos e dois turnos interativos:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Verifique todos os três títulos das seções, cinco respostas, uma resposta interativa final relembrando Ada, `Adeus!` e código de saída 0. Orçamento: **5 requisições, no máximo 1.900 tokens de completude**. Para uma execução menor, execute apenas `exit`: 3 requisições / 900 tokens, mas isso não exercita a inferência interativa.

**2. Ambos os fluxos de trabalho de chamada de função:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Verifique ambos os nomes das funções, clima simulado de Seattle, resultado calculado 36, duas respostas finais e código de saída 0. Orçamento: **4 requisições, no máximo 1.200 tokens de completude**.

**3. Resposta fundamentada em documento:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Verifique o caminho do documento, uma resposta mencionando Microsoft Entra ID e código de saída 0. Orçamento: **1 requisição, no máximo 500 tokens de completude**. O [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) existente é o único arquivo de entrada necessário. Uma segunda execução opcional perguntando sobre um tópico ausente deve se abster e adiciona uma requisição / 500 tokens.

**4. Observações de IA responsável:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Verifique seis categorias e o resumo das observações, revise o conteúdo gerado e exija código de saída 0 para conclusão técnica. Uma saída de processo bem-sucedida não certifica a segurança do modelo. Orçamento: **6 requisições, no máximo 1.800 tokens de completude**.

**Total para os quatro comandos: 16 requisições de chat e no máximo 5.400 tokens de completude**, mais tokens de entrada (incluindo conversa repetida e esquema/histórico da ferramenta). Não há requisições de embedding. O uso real de tokens depende do modelo e pode ser menor, especialmente para prompts filtrados. O custo em dólares depende do preço da implantação; não há estimativa monetária fixa implicada. Todos os limites de requisição assumem ausência de execuções manuais repetidas. Inspecione `$LASTEXITCODE` imediatamente após cada comando; valor diferente de zero significa que a execução não foi concluída com sucesso.

## Resolução de Problemas

- **Ponto de extremidade ausente / 401 / 403:** Configure o endpoint no processo de inicialização, verifique seu login local no Azure e a função com escopo de recurso, e cheque por substituições indesejadas do ambiente de identidade.
- **400 / 404:** Confirme que a implantação existe e suporta Chat Completions com esforço de raciocínio `none`. Use a raiz HTTPS do recurso ou URL `/openai/v1`, não um URL de implantação legado. Erros 400 ordinários são falhas técnicas, não bloqueios de segurança.
- **429:** Coordene o RPM compartilhado e a cota de tokens antes de tentar novamente. Os exemplos deliberadamente não fazem nova tentativa automática.
- **`Resposta de chat incompleta: comprimento`:** A saída alcançou o limite de completude. Revise a resposta e o prompt antes de aumentar o limite e seu orçamento documentado; não registre uma execução truncada como bem-sucedida.
- **Erros de arquivo ou stdin:** Execute a partir de um diretório suportado ou passe um caminho explícito para o documento. Forneça uma pergunta não vazia ao leitor. Completions podem terminar normalmente ao chegar em EOF ou `exit`.
- **Erros de compilação:** Verifique Java 21 ou posterior, depois rode `mvn -B -ntp clean test`. No PowerShell, coloque entre aspas todo o argumento Maven que contenha uma propriedade com ponto, por exemplo `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Próximos Passos

Continue para [Capítulo 4: Exemplos Práticos](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->