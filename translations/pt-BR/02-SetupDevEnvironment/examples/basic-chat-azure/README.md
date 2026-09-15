# Chat Básico com Azure AI Foundry - Exemplo de Ponta a Ponta

Este exemplo é uma aplicação simples Spring Boot que se conecta a um modelo **Azure AI Foundry** usando **autenticação sem chave** (Microsoft Entra ID) e testa sua configuração. Ele mantém o `ChatClient` do Spring AI, suportado pelo **SDK oficial OpenAI Java** e o endpoint **Azure OpenAI v1**.

As versões em [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) são Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** e dotenv-java **3.2.0**. O exemplo usa `spring-ai-starter-model-openai` e declara explicitamente `openai-java` e `azure-identity`; o Spring AI 2 removeu o starter antigo do Azure OpenAI.

## Sumário

- [Pré-requisitos](#pré-requisitos)
- [Início Rápido](#início-rápido)
- [Como a Autenticação Funciona](#como-a-autenticação-funciona)
- [Executando a Aplicação](#executando-a-aplicação)
  - [Usando Maven](#usando-maven)
  - [Usando VS Code](#usando-vs-code)
  - [Saída Esperada](#saída-esperada)
- [Referência de Configuração](#referência-de-configuração)
  - [Variáveis de Ambiente](#variáveis-de-ambiente)
  - [Configuração Spring](#configuração-spring)
- [Solução de Problemas](#solução-de-problemas)
  - [Problemas Comuns](#problemas-comuns)
  - [Modo de Depuração](#modo-de-depuração)
- [Próximos Passos](#próximos-passos)
- [Recursos](#recursos)

## Pré-requisitos

Antes de executar este exemplo, certifique-se de que você tem:

- Um recurso Azure AI Foundry com um deployment `gpt-5.6-luna` - provisionado com `azd up` ou manualmente via o [guia de configuração do Azure AI Foundry](../../getting-started-azure-openai.md)
- A função **Cognitive Services OpenAI User** atribuída a esse recurso (os templates Bicep atribuem isso para você)
- A [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), autenticado com `az login`
- Java 21+ e Maven 3.9+

> **Sem necessidade de chave de API** — a autenticação é sem chave via Microsoft Entra ID.

## Início Rápido

```bash
# 1. Navegue até o projeto
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Faça login para que a autenticação sem chave possa obter um token
az login

# 3. Configure o endpoint
#    - Se você executou `azd up`, o arquivo .env foi criado para você (pule esta etapa).
#    - Caso contrário, copie o modelo e defina AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Execute a aplicação
mvn spring-boot:run
```

## Como a Autenticação Funciona

Este exemplo autentica com **Microsoft Entra ID** — não há chave de API.

A aplicação configura a autenticação explicitamente em [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` cria um `BearerTokenCredential` usando `AuthenticationUtil.getBearerTokenSupplier` com `DefaultAzureCredential` e o escopo `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` constrói um `OpenAIClient` com `OpenAIOkHttpClient.builder()`, resolve o endpoint do recurso para `/openai/v1`, e fornece a credencial bearer com `.credential(...)`.
3. `azureChatModel()` fornece esse cliente para o `OpenAiChatModel` do Spring AI, que suporta o `ChatClient` do exemplo.

Esses beans explícitos impedem que uma variável global `OPENAI_API_KEY` sobrescreva a autenticação do Azure. Omitir uma chave API só no YAML não configura a autenticação. O `DefaultAzureCredential` pode usar sua sessão `az login` localmente ou uma identidade gerenciada no Azure; a identidade selecionada deve ter o papel de recurso listado acima.

## Executando a Aplicação

### Usando Maven

```bash
mvn spring-boot:run
```

### Usando VS Code

1. Abra o projeto no VS Code
2. Pressione `F5` ou use o painel "Executar e Depurar"
3. Selecione a configuração "Spring Boot-BasicChatApplication"

> **Nota**: A aplicação carrega `.env` do diretório de trabalho, inclusive quando iniciada pelo VS Code.

### Saída Esperada

Saída ilustrativa após uma execução bem-sucedida (logs de inicialização omitidos; o texto da resposta pode variar):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Referência de Configuração

### Variáveis de Ambiente

| Variável | Descrição | Obrigatório | Exemplo |
|----------|-----------|------------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL do endpoint Foundry (Azure OpenAI) | Sim | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nome do deployment do modelo de chat | Não | `gpt-5.6-luna` (padrão) |

> Não existe variável de chave API — a autenticação é sem chave (Microsoft Entra ID via `az login`).

### Configuração Spring

As configurações em [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) usam o prefixo `spring.ai.openai` e propriedades flattenadas para chat (sem bloco `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` é o **nome do deployment Azure**. A autenticação vem dos beans explícitos descritos acima, não de uma configuração `api-key`. O exemplo desativa raciocínio (reasoning) e limita tokens de completions a 500; deixa `temperature` e o legacy `max-tokens` sem configuração.

A Microsoft recomenda o [SDK oficial OpenAI com Azure OpenAI v1 e a API Responses para novas aplicações](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions continua suportado para esta lição baseada em mensagens existente. Para GPT-5.6, solicitações que incluem ferramentas em Chat Completions devem definir `reasoning_effort` como `none`; use Responses para combinar raciocínio com ferramentas. Veja [chamadas de ferramenta com modelos de raciocínio](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Solução de Problemas

### Problemas Comuns

<details>
<summary><strong>Erro: 401 / "PermissionDenied" / erros de token</strong></summary>

- Execute `az login` — autenticação sem chave requer login ativo para obter token
- Verifique se sua conta tem o papel **Cognitive Services OpenAI User** no recurso
- Se você acabou de atribuir o papel, espere um minuto para propagação
- Confirme que está no tenant/assinatura correta (`az account show`)
</details>

<details>
<summary><strong>Erro: "The endpoint is not valid" / erros de conexão</strong></summary>

- Garanta que `AZURE_OPENAI_ENDPOINT` seja a URL base completa (ex.: `https://your-resource.openai.azure.com/`)
- Verifique a consistência de barra final
- Confirme se o endpoint corresponde ao seu recurso provisionado (`azd env get-values`)
</details>

<details>
<summary><strong>Erro: "The deployment was not found"</strong></summary>

- Verifique se `AZURE_OPENAI_DEPLOYMENT` corresponde ao nome de deployment no Azure
- Confirme que o modelo está implantado com sucesso e ativo
- O nome padrão da implantação é `gpt-5.6-luna`
</details>

<details>
<summary><strong>Erro: 429 / limite de taxa excedido</strong></summary>

- A implantação padrão GPT-5.6 Luna tem capacidade Global Standard 10: 10 solicitações/minuto e 10.000 tokens/minuto
- Execute exemplos sequencialmente e aguarde o intervalo de tentativa do serviço antes de tentar novamente
- Este exemplo básico desativa as tentativas automáticas do SDK, então uma solicitação falhada é relatada diretamente
</details>

<details>
<summary><strong>VS Code: Variáveis de ambiente não estão sendo carregadas</strong></summary>

- Assegure que seu arquivo `.env` esteja no diretório raiz do projeto (mesmo nível do `pom.xml`)
- Tente executar `mvn spring-boot:run` no terminal integrado do VS Code
- Verifique se a extensão Java do VS Code está instalada corretamente
</details>

### Modo de Depuração

Para habilitar o log detalhado, descomente estas linhas em [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Próximos Passos

**Configuração Completa!** Continue sua jornada de aprendizado:

[Capítulo 3: Técnicas Centrais de IA Generativa](../../../03-CoreGenerativeAITechniques/README.md)

## Recursos

- [Transição do Spring AI 2 para OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java oficial do OpenAI com Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autenticação sem chave com Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Documentação Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->