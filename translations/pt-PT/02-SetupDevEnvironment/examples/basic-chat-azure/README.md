# Chat Básico com Azure AI Foundry - Exemplo de Ponta a Ponta

Este exemplo é uma aplicação simples Spring Boot que conecta a um modelo **Azure AI Foundry** usando **autenticação sem chave** (Microsoft Entra ID) e testa a sua configuração. Mantém o `ChatClient` do Spring AI, suportado pelo **SDK oficial OpenAI Java** e pelo endpoint **Azure OpenAI v1**.

As versões em [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) são Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** e dotenv-java **3.2.0**. O exemplo usa `spring-ai-starter-model-openai` e declara explicitamente `openai-java` e `azure-identity`; o Spring AI 2 removeu o antigo starter Azure OpenAI.

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Início Rápido](#início-rápido)
- [Como a Autenticação Funciona](#como-a-autenticação-funciona)
- [Executar a Aplicação](#executar-a-aplicação)
  - [Usando Maven](#usando-maven)
  - [Usando VS Code](#usando-vs-code)
  - [Saída Esperada](#saída-esperada)
- [Referência de Configuração](#referência-de-configuração)
  - [Variáveis de Ambiente](#variáveis-de-ambiente)
  - [Configuração Spring](#configuração-spring)
- [Resolução de Problemas](#resolução-de-problemas)
  - [Problemas Comuns](#problemas-comuns)
  - [Modo Debug](#modo-debug)
- [Próximos Passos](#próximos-passos)
- [Recursos](#recursos)

## Pré-requisitos

Antes de executar este exemplo, confirme que tem:

- Um recurso Azure AI Foundry com um deployment `gpt-5.6-luna` - providencie-o com `azd up` ou manualmente via o [guia de configuração Azure AI Foundry](../../getting-started-azure-openai.md)
- O papel **Cognitive Services OpenAI User** nesse recurso (os templates Bicep atribuem-no automaticamente)
- A [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), autenticada com `az login`
- Java 21+ e Maven 3.9+

> **Não é necessária uma chave de API** — a autenticação é sem chave via Microsoft Entra ID.

## Início Rápido

```bash
# 1. Navegue até ao projeto
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Inicie sessão para que a autenticação sem chave possa obter um token
az login

# 3. Configure o endpoint
#    - Se executou `azd up`, o ficheiro .env foi criado automaticamente para si (pule este passo).
#    - Caso contrário, copie o modelo e defina AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Execute a aplicação
mvn spring-boot:run
```

## Como a Autenticação Funciona

Este exemplo autentica-se com **Microsoft Entra ID** — não há chave de API.

A aplicação configura a autenticação explicitamente em [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` cria um `BearerTokenCredential` usando `AuthenticationUtil.getBearerTokenSupplier` com `DefaultAzureCredential` e o escopo `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` constrói um `OpenAIClient` com `OpenAIOkHttpClient.builder()`, resolve o endpoint do recurso para `/openai/v1` e fornece o token bearer com `.credential(...)`.
3. `azureChatModel()` fornece esse cliente ao `OpenAiChatModel` do Spring AI, que suporta o `ChatClient` da lição.

Estes beans explícitos evitam que uma variável global `OPENAI_API_KEY` sobrescreva a autenticação Azure. Omissão da chave API só no YAML não configura a autenticação. `DefaultAzureCredential` pode usar a sessão local do `az login` ou uma identidade gerida no Azure; a identidade selecionada deve ter o papel no recurso listado acima.

## Executar a Aplicação

### Usando Maven

```bash
mvn spring-boot:run
```

### Usando VS Code

1. Abra o projeto no VS Code
2. Pressione `F5` ou utilize o painel "Run and Debug"
3. Selecione a configuração "Spring Boot-BasicChatApplication"

> **Nota**: A aplicação carrega o `.env` do diretório de trabalho, incluindo quando iniciada a partir do VS Code.

### Saída Esperada

Saída ilustrativa após execução bem-sucedida (logs de arranque omitidos; texto da resposta pode variar):

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

| Variável | Descrição | Obrigatória | Exemplo |
|----------|-----------|-------------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL do endpoint Foundry (Azure OpenAI) | Sim | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nome do deployment do modelo de chat | Não | `gpt-5.6-luna` (padrão) |

> Não existe nenhuma variável para chave API — a autenticação é sem chave (Microsoft Entra ID via `az login`).

### Configuração Spring

As definições em [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) usam o prefixo `spring.ai.openai` e propriedades de chat aplanadas (sem bloco `options`):

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

`model` é o **nome do deployment Azure**. A autenticação vem dos beans explícitos descritos acima, não de uma configuração `api-key`. A lição desativa o raciocínio e limita tokens de completamento a 500; deixa `temperature` e o legado `max-tokens` por definir.

A Microsoft recomenda o [SDK oficial OpenAI com Azure OpenAI v1 e API Responses para novas aplicações](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions continua suportado para esta lição existente baseada em mensagens. Para GPT-5.6, pedidos que incluem ferramentas em Chat Completions devem definir `reasoning_effort` para `none`; use Responses para conjugar raciocínio com ferramentas. Veja [chamada de ferramentas com modelos de raciocínio](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Resolução de Problemas

### Problemas Comuns

<details>
<summary><strong>Erro: 401 / "PermissionDenied" / erros de token</strong></summary>

- Execute `az login` — a autenticação sem chave precisa de sessão ativa para obter token
- Verifique se a sua conta tem o papel **Cognitive Services OpenAI User** no recurso
- Se acabou de atribuir o papel, espere um minuto para propagar
- Confirme que está no tenant/subscrição correta (`az account show`)
</details>

<details>
<summary><strong>Erro: "The endpoint is not valid" / erros de ligação</strong></summary>

- Garanta que `AZURE_OPENAI_ENDPOINT` é a URL base completa (ex.: `https://your-resource.openai.azure.com/`)
- Verifique a consistência da barra final
- Confirme que o endpoint corresponde ao seu recurso providenciado (`azd env get-values`)
</details>

<details>
<summary><strong>Erro: "The deployment was not found"</strong></summary>

- Verifique que `AZURE_OPENAI_DEPLOYMENT` corresponde a um nome de deployment no Azure
- Confirme que o modelo está implantado com sucesso e ativo
- O nome de deployment padrão é `gpt-5.6-luna`
</details>

<details>
<summary><strong>Erro: 429 / limite de taxa excedido</strong></summary>

- O deployment padrão GPT-5.6 Luna tem capacidade Global Standard 10: 10 pedidos/minuto e 10.000 tokens/minuto
- Execute exemplos sequencialmente e espere o intervalo de retry do serviço antes de tentar novamente
- Este exemplo básico desativa retries automáticos do SDK, por isso um pedido falhado é reportado diretamente
</details>

<details>
<summary><strong>VS Code: Variáveis de ambiente não carregam</strong></summary>

- Garanta que o ficheiro `.env` está no diretório raiz do projeto (ao mesmo nível que `pom.xml`)
- Tente executar `mvn spring-boot:run` no terminal integrado do VS Code
- Confirme que a extensão Java do VS Code está instalada corretamente
</details>

### Modo Debug

Para ativar logs detalhados, descomente estas linhas em [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Próximos Passos

**Configuração Completa!** Continue a sua jornada de aprendizagem:

[Capítulo 3: Técnicas Básicas de IA Generativa](../../../03-CoreGenerativeAITechniques/README.md)

## Recursos

- [Transição Spring AI 2 SDK OpenAI Java](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Oficial OpenAI Java com Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autenticação sem chave com Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Documentação Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->