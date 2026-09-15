# Tutorial das Técnicas Centrais de IA Generativa

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Começar](#começar)
- [Guia de Seleção de Modelo](#guia-de-seleção-de-modelo)
- [Tutorial 1: Completações e Chat LLM](#tutorial-1-completações-e-chat-llm)
- [Tutorial 2: Chamada de Funções](#tutorial-2-chamada-de-funções)
- [Tutorial 3: RAG (Geração com Recuperação Aumentada)](#tutorial-3-rag-geração-com-recuperação-aumentada)
- [Tutorial 4: IA Responsável](#tutorial-4-ia-responsável)
- [Padrões Comuns nos Exemplos](#padrões-comuns-nos-exemplos)
- [Testes Unitários](#testes-unitários)
- [Verificação Sequencial em Tempo Real](#verificação-sequencial-em-tempo-real)
- [Resolução de Problemas](#resolução-de-problemas)
- [Próximos Passos](#próximos-passos)

## Visão Geral

Quatro programas Java autónomos demonstram chat, histórico de conversa, chamada de funções, geração com recuperação aumentando o documento completo (RAG) e gestão responsável de respostas de IA. Todas as requisições de chat direcionam-se por padrão para **GPT-5.6 Luna com esforço de raciocínio `none`**.

Estes exemplos usam o SDK oficial OpenAI Java com o endpoint v1 do Azure OpenAI, seguindo a [orientação do SDK da Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). O pacote antigo `azure-ai-openai` deixou de ser dependência. Chat Completions é mantido para ensinar os fluxos de trabalho baseados em mensagens existentes; consulte o [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) para outras opções de API.

## Pré-requisitos

- Java 21 ou posterior e Maven 3.6.3 ou posterior.
- Uma implementação de chat Azure OpenAI chamada `gpt-5.6-luna`, ou uma substituição com configurações compatíveis de Chat Completions.
- Uma identidade Azure autenticada com o papel **Cognitive Services OpenAI User** no recurso. O desenvolvimento local usa a sua autenticação Azure CLI; aplicações alojadas podem usar identidade gerida.
- Consulte o [Capítulo 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) para instruções de configuração de recurso e autenticação.

A [configuração Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fixa estas versões, verificadas em 2026-09-14:

| Componente | Versão | Propósito |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Cliente oficial compatível com Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Autenticação sem chave e renovação de tokens |
| `net.objecthunter:exp4j` | 0.4.8 | Análise de expressões aritméticas sem avaliação de código |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Testes unitários Jupiter offline |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Compilação Java 21, testes, exemplos executáveis |

O compilador usa `--release 21`. Nenhuma dependência de Spring Boot, Spring AI ou LangChain4j é necessária nestes exemplos autónomos.

## Começar

A partir da raiz do repositório, defina o endpoint do recurso e a possível substituição de implementação na sua shell.

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

Os testes não requerem credenciais Azure nem endpoint. O Maven não lê automaticamente um ficheiro ambiente; defina as variáveis na shell usada para lançar exemplos ao vivo. Para lançamentos em IDE, verifique o ambiente fornecido pela configuração de lançamento.

## Guia de Seleção de Modelo

| Variável de ambiente | Significado | Predefinição |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Raiz do recurso Azure HTTPS ou URL `/openai/v1` já normalizada | Obrigatório para execuções ao vivo |
| `AZURE_OPENAI_DEPLOYMENT` | Nome da implementação de chat, não uma versão de modelo | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configuração separada para implementação de embedding, não usada nestes quatro programas | `text-embedding-3-small` |

Substituições de implementação vazias usam os predefinidos. A configuração acrescenta `/openai/v1` exatamente uma vez e rejeita credenciais, strings de consulta e caminhos de implementação legados no endpoint.

Cada pedido de chat define explicitamente `reasoningEffort(ReasoningEffort.NONE)` e `maxCompletionTokens(...)`. Nenhum pedido define `temperature`, `top_p` ou a opção legada de token de completamento. Isto inclui selecção de ferramentas e seguimento de resultados. Ferramentas de função Chat Completions GPT-5.6 requerem esforço de raciocínio `none`; veja a [orientação de chat da Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Não existe ponto de entrada para streaming ou embeddings neste capítulo.** O leitor recupera o documento inteiro, não vetores. Se o expandir com embeddings, use uma implementação separada como `text-embedding-3-small`, nunca Luna.

## Tutorial 1: Completações e Chat LLM

Origem: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

O programa executa uma explicação simples sobre streams Java, uma conversa de duas voltas com HashMap/TreeMap, e chat interativo. A segunda volta inclui a primeira resposta do assistente; cada volta interativa também envia a conversa anterior.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` fornece a implementação e a definição explícita do esforço de raciocínio. O chat interativo ignora linhas em branco, termina em `exit` ou EOF, e retém a mensagem do sistema mais nove voltas completas de utilizador/assistente. O limite do número de voltas é educacional, não uma garantia exata de orçamento de tokens.

A partir da diretoria de exemplos:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Espere três respostas iniciais, depois um prompt `You:`. Cada pergunta interativa não vazia adiciona uma requisição. Limites de completamento são 200, 300, 400 e depois 500 tokens por volta interativa.

## Tutorial 2: Chamada de Funções

Origem: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

O SDK deriva esquemas JSON dos registos anotados `WeatherArguments` e `CalculationArguments`. Uma escolha de ferramenta obrigatória torna cada exercício um protocolo de ferramenta em vez de aceitar uma resposta sem auxílio do modelo.

1. Enviar uma pergunta com a ferramenta permitida, esforço de raciocínio `none`, e limite de 300 tokens para completamento.
2. Exigir razão de término `tool_calls`, validar nome da função e IDs de chamada, e analisar argumentos JSON tipados.
3. Executar a função local. O modelo não executa código Java ou arbitrário.
4. Adicionar a mensagem de chamada de ferramenta do assistente uma vez, seguida de cada resultado com o seu correspondendo `tool_call_id`.
5. Enviar uma requisição final de 300 tokens sem ferramentas e requerer uma resposta concluída e não vazia.

`get_weather` retorna o tempo **simulado**, não ao vivo. Respeita a cidade e converte os 22 graus Celsius de exemplo para Fahrenheit se solicitado. `calculate` avalia a expressão fornecida através do exp4j, suporta formas como `15% de 240` e `2 + 3 * 4`, e rejeita cálculos vazios, demasiado grandes, inválidos ou não finitos. Usa aritmética em ponto flutuante, não precisão decimal financeira.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Espere `Function: get_weather`, tempo simulado de Seattle, `Function: calculate`, `Function result: 36`, e as duas respostas finais. Não são necessários stdin ou credenciais externas para o tempo. Uma execução bem-sucedida utiliza exactamente quatro pedidos de chat.

## Tutorial 3: RAG (Geração com Recuperação Aumentada)

Origem: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Entrada: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Este exemplo introdutório RAG recupera um documento UTF-8 completo e inclui-o na mensagem do utilizador com a pergunta. Uma mensagem de sistema separada instrui o modelo a tratar o conteúdo do documento como dados não confiáveis e a responder apenas com base nesse contexto. Se o documento não contiver a resposta, a resposta solicitada é: `Não consigo encontrar essa informação no documento fornecido.`

Fundamentação pode reduzir alucinações, mas nem delimitadores nem instruções do sistema garantem precisão ou previnem todas as injeções de prompt. Reveja respostas ao vivo. A RAG para produção normalmente adiciona fragmentação, recuperação, citações, controlo de acesso e avaliação.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Introduza uma pergunta, por exemplo `Qual é o método de autenticação descrito no documento?`. Espere uma resposta mencionando Microsoft Entra ID. O programa termina após um pedido de chat com limite de 500 tokens para completamento.

A procura padrão de ficheiros funciona a partir da raiz do repositório, diretoria do capítulo ou diretoria de exemplos. Um caminho explícito também é suportado:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

As entradas devem não estar vazias: até 32 KiB de dados do documento UTF-8 e 2.000 caracteres para a pergunta. Ficheiros em falta, perguntas vazias/EOF, e entradas demasiado grandes falham antes da inferência.

## Tutorial 4: IA Responsável

Origem: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

As seis sondagens abrangem instruções nocivas, discurso de ódio, privacidade, desinformação médica, conteúdo ilegal, e uma questão benigna de IA responsável. O programa observa a resposta em vez de assumir que cada sondagem deve acionar um filtro.

| Resultado | Evidência |
| --- | --- |
| `FILTRADO` | Código de erro explícito `content_filter` / `ResponsibleAIPolicyViolation`, ou razão de término `content_filter` na completamento |
| `RECUSADO` | Um campo estruturado `message.refusal` não vazio |
| `POSSÍVEL_RECUSA` | Uma frase inicial de recusa em texto comum; uma heurística que requer revisão |
| `GERADO` | Uma resposta completa e não vazia; não prova que o conteúdo é seguro |

Um HTTP 400 normal **não** é evidência de filtragem. Parâmetros inválidos, falhas de autenticação, limites de taxa, erros do servidor, respostas malformadas e saída truncada falham a execução em vez de produzirem um falso sucesso em segurança. Palavras genéricas como "conteúdo nocivo" numa explicação benigna não contam como recusa.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Espere seis resultados de categoria e um sumário afirmando que as observações não são uma certificação de segurança. Cada sondagem tem um limite de completamento de 300 tokens. Reveja gerações inesperadas e possíveis recusas manualmente; a comparação benigna deverá produzir uma explicação substantiva de IA responsável. Nenhum stdin é requerido.

## Padrões Comuns nos Exemplos

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centraliza normalização de endpoint, substituições de implementação, autenticação sem chave, e opções de chat:

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

O fornecedor de tokens renova tokens de acesso conforme necessário. Não faça registo de tokens nem substitua isto com uma chave API. Cada programa reutiliza o seu cliente e fecha-o em `finally` ou através de um seu próprio wrapper `AutoCloseable`; o SDK `OpenAIClient` não é `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) requer uma resposta textual completa e não vazia. Escolhas vazias, recusas, filtros e respostas truncadas não são silenciosamente consideradas sucesso. O exemplo de IA responsável trata explicitamente resultados esperados de filtro/recusa. Falhas não tratadas dão ao processo Java/Maven um código de saída diferente de zero.

**As tentativas automáticas do SDK estão desativadas** para manter previsibilidade do número de pedidos em implementações partilhadas com baixa taxa de pedidos por minuto. Cada pedido de inferência tem um timeout de 60 segundos. A aquisição de tokens pode levar tempo adicional. A programação a nível de aplicação deve respeitar quotas; não relance cegamente um pedido pago falhado.

## Testes Unitários

A partir da diretoria de exemplos:

```powershell
mvn -B -ntp clean test
```

O transporte de teste substitui completamente a camada HTTP do SDK, capta os corpos de pedidos serializados reais, e fornece respostas enfileiradas. Não abre ligações, não adquire tokens Azure, e falha em pedidos inesperados. Estes testes validam o comportamento da aplicação e o protocolo do SDK, não a qualidade ao vivo do modelo ou disponibilidade da implementação.

| Suite de testes | Cobertura |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalização/rejeição de endpoint, substituições de implementação, opções de raciocínio e tokens |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Todo o fluxo de trabalho de completamento, histórico de mensagens, aparar voltas completas, EOF, falhas |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Esquemas de ferramenta, argumentos tipados, aritmética, IDs, múltiplos resultados de ferramenta, falhas de seguimento |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Procura de ficheiros, UTF-8, limites de tamanho, carga de fundamentação, erros de entrada e API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | As seis sondagens, filtros explícitos, classificação de recusa, 400 HTTP comum e outras falhas |

Para uma suite, use `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Fixtures partilhados vivem em [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Verificação Sequencial em Tempo Real

Chamadas ao vivo são separadas dos testes unitários. Use os seguintes comandos **individualmente**, a partir da raiz do repositório, apenas depois das credenciais e acesso à implementação estarem prontos. Não são necessários serviços nem processos persistentes.

Para uma implementação partilhada **10 pedidos/minuto**, reserve quota suficiente para todo o próximo programa antes de o lançar: 5, 4, 1, depois 6 pedidos. Apenas processos sequenciais não garantem conformidade com limite de taxa. Coordene o minuto rolante com os outros chamadores; não cole as quatro invocações como um lote sem ritmo.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completações, múltiplas voltas e duas voltas interativas:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Verifique todos os três títulos de secção, cinco respostas, uma resposta interativa final a recordar Ada, `Adeus!`, e código de saída 0. Orçamento: **5 pedidos, no máximo 1,900 tokens de completamento**. Para uma execução menor, canalize apenas `exit`: 3 pedidos / 900 tokens, mas isso não exercita a inferência interativa.

**2. Ambos os fluxos de trabalho com chamada de funções:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Verifique ambos os nomes das funções, o tempo simulado de Seattle, resultado calculado 36, duas respostas finais e código de saída 0. Orçamento: **4 pedidos, no máximo 1,200 tokens de completamento**.

**3. Resposta baseada em documento:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Verifique o caminho do documento, uma resposta mencionando Microsoft Entra ID e código de saída 0. Orçamento: **1 pedido, no máximo 500 tokens de completamento**. O existente [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) é o único ficheiro de entrada necessário. Uma segunda execução opcional a perguntar sobre um tópico ausente deve abster-se e adiciona um pedido / 500 tokens.

**4. Observações de IA responsável:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Verifique seis categorias e o resumo das observações, reveja o conteúdo gerado e exija código de saída 0 para conclusão técnica. Uma saída de processo bem-sucedida não certifica a segurança do modelo. Orçamento: **6 pedidos, no máximo 1,800 tokens de completamento**.

**Total para os quatro comandos: 16 pedidos de chat e no máximo 5,400 tokens de completamento**, mais tokens de entrada (incluindo conversa repetida e esquema/histórico da ferramenta). Não há pedidos de incorporação. O uso real de tokens depende do modelo e pode ser inferior, especialmente para prompts filtrados. O custo em dólares depende dos preços de implantação; não é implícita uma estimativa monetária fixa. Todos os limites de pedidos assumem que não há repetições manuais. Inspecione `$LASTEXITCODE` imediatamente após cada comando; diferente de zero significa que a execução não foi concluída com sucesso.

## Resolução de Problemas

- **Endpoint em falta / 401 / 403:** Defina o endpoint no processo de lançamento, verifique o seu início de sessão local no Azure e a função com escopo de recurso, e verifique se não há substituições indesejadas do ambiente de identidade.
- **400 / 404:** Confirme que a implantação existe e suporta Chat Completions com esforço de raciocínio `none`. Use a raiz do recurso HTTPS ou a URL `/openai/v1`, não uma URL de implantação antiga. Erros 400 normais são falhas técnicas, não bloqueios de segurança.
- **429:** Coordene o RPM compartilhado e a quota de tokens antes de tentar novamente. Os exemplos deliberadamente não fazem reintentos automáticos.
- **`Resposta de chat incompleta: length`:** A saída atingiu o limite de completamento. Reveja a resposta e o prompt antes de aumentar o limite e o orçamento documentado; não registe uma execução truncada como bem-sucedida.
- **Erros de ficheiros ou stdin:** Lance a partir de um diretório suportado ou passe um caminho de documento explícito. Forneça uma pergunta do leitor não vazia. As completions podem terminar normalmente com EOF ou `exit`.
- **Erros de compilação:** Verifique se tem Java 21 ou posterior, depois execute `mvn -B -ntp clean test`. No PowerShell, envolva todo o argumento Maven contendo uma propriedade com pontos, por exemplo `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Próximos Passos

Continue para [Capítulo 4: Exemplos Práticos](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->