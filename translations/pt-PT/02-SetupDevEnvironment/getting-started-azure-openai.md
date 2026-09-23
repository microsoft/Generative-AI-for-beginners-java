# Configurar o Ambiente de Desenvolvimento para Azure AI Foundry

> Este guia configura modelos **Azure AI Foundry** para as aplicações Java AI deste curso, utilizando autenticação **sem chaves** (Microsoft Entra ID) — sem chaves API para gerir. Novo nas ferramentas? Comece com o [guia do ambiente de desenvolvimento](./README.md).

Este guia configura modelos **Azure AI Foundry** para as aplicações Java AI deste curso. Tem dois caminhos:

- **Opção A — Provisionar com `azd` + Bicep (recomendado):** um comando implanta a conta Foundry e os modelos como código. Sem cliques no portal.
- **Opção B — Criar recursos manualmente** no portal Azure AI Foundry.

Ambos os caminhos usam **autenticação sem chaves** (Microsoft Entra ID) — não há chaves API para copiar ou vazar.

## Índice

- [O que é criado](#o-que-é-criado)
- [Pré-requisitos](#pré-requisitos)
- [Opção A: Provisionar com azd + Bicep (Recomendado)](#option-a-provision-with-azd--bicep-recommended)
- [Opção B: Criar Recursos Manualmente](#opção-b-criar-recursos-manualmente)
- [Configurar o seu Ambiente](#configurar-o-seu-ambiente)
- [Testar a sua Configuração](#testar-a-sua-configuração)
- [E depois?](#e-depois)
- [Recursos](#recursos)
- [Recursos Adicionais](#recursos-adicionais)

## O que é criado

Os templates Bicep em [`infra/`](../../../02-SetupDevEnvironment/infra) provisionam:

- Uma conta **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, tipo `AIServices`) com um projeto
- Uma implantação **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), versão `2026-07-09`, com capacidade `GlobalStandard` `10` (10 pedidos/minuto e 10.000 tokens/minuto para este modelo)
- Uma implantação **embedding** - `text-embedding-3-small`, versão `1` (usada em capítulos posteriores)
- Uma **atribuição de função sem chaves** (`Cognitive Services OpenAI User`) para que se autentique com `az login` em vez de gerir chaves

## Pré-requisitos

- Uma [subscrição Azure](https://azure.microsoft.com/free/)
- [CLI do Azure Developer (`azd`)](https://aka.ms/azure-dev/install)
- [CLI do Azure (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) e [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opção A: Provisionar com azd + Bicep (Recomendado)

A partir da pasta `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Iniciar sessão (ambas as ferramentas)
azd auth login
az login

# Providenciar a conta Foundry + implementações de modelos
azd up
```

`azd` pede um **nome de ambiente** (por exemplo `genai-java`), **subscrição** e **região**. Escolha a sua subscrição e uma região onde `gpt-5.6-luna` e `text-embedding-3-small` estejam disponíveis, por exemplo `eastus2`. Confirme que a subscrição tem quota suficiente para o modelo e tipo de implantação nessa região; disponibilidade e quota variam por subscrição.

Quando o provisionamento terminar, azd:

1. Implanta tudo definido em [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Executa um post-hook que escreve [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) com os nomes do seu endpoint e implantação (sem segredos).

> **Sugestão:** Execute `azd up` sempre que quiser aplicar alterações. Execute `azd down` para apagar tudo e parar de gerar custos.

Para ver as definições geradas:

```bash
azd env get-values
```

Agora avance para [Testar a sua Configuração](#testar-a-sua-configuração).

## Opção B: Criar Recursos Manualmente

Prefere o portal? Crie os recursos manualmente:

1. Vá ao [portal Azure AI Foundry](https://ai.azure.com/) e inicie sessão.
2. **Crie um projeto** (isto também cria um recurso AI Foundry). Dê-lhe um nome como `GenAIJava`.
3. No seu projeto, abra **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Implemente **GPT-5.6 Luna** (nome do modelo e implantação `gpt-5.6-luna`, versão `2026-07-09`) com capacidade **Global Standard** `10`. Repita para **text-embedding-3-small**, versão `1`, se quiser os exemplos de embedding.
5. De **Overview**, copie o **endpoint** (por exemplo `https://<resource>.openai.azure.com/`).
6. Conceda acesso sem chave: no recurso, abra **Gestão de acesso (IAM)** → **Adicionar atribuição de função** → atribua **Cognitive Services OpenAI User** à sua conta.

> **Continuar com dificuldades?** Consulte a [documentação do Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configurar o seu Ambiente

**Se usou a Opção A (`azd up`)**, o ficheiro de definições já está criado — não é necessário configurar mais nada. Avance para [Testar a sua Configuração](#testar-a-sua-configuração).

**Se usou a Opção B (manual)**, crie você próprio o ficheiro `.env` do exemplo:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Edite o `.env` com o seu endpoint (sem chave — autenticação sem chaves):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Utilize o endpoint Azure OpenAI do recurso, não uma URL de projeto. A app basic-chat resolve isso para `/openai/v1` e configura um cliente com token bearer explícito; não é necessária chave API.

> **Nota de segurança:** Não há chave API para armazenar. Autentica-se com Microsoft Entra ID via `az login` (localmente) ou uma identidade gerida (em Azure). O ficheiro `.env` contém apenas definições não secretas e já está coberto pelo `.gitignore`.

## Testar a sua Configuração

Certifique-se de que está autenticado para que a autenticação sem chaves possa obter um token, depois execute o exemplo:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # se ainda não estiveres iniciado sessão
mvn clean spring-boot:run
```

Deve ver uma resposta do modelo `gpt-5.6-luna`. Execute os exemplos sequencialmente para permanecer dentro da pequena quota padrão; se receber HTTP 429, aguarde o intervalo de retentativa antes de tentar novamente.

> **Utilizadores VS Code:** Prima `F5` para executar. A app carrega automaticamente o seu `.env`.

> **Exemplo completo:** Consulte o [exemplo Basic Chat com Azure AI Foundry](./examples/basic-chat-azure/README.md) para detalhes e resolução de problemas.

## E depois?

Depois do provisionamento e execução bem-sucedida do exemplo, terá:
- Azure AI Foundry com `gpt-5.6-luna` e `text-embedding-3-small` implementados
- Autenticação sem chave (Microsoft Entra ID) — sem chaves para gerir
- Um ficheiro local `.env` com o seu endpoint e nomes de implementação
- Ambiente de desenvolvimento Java pronto a usar

**Continue para** [Capítulo 3: Técnicas Básicas de IA Generativa](../03-CoreGenerativeAITechniques/README.md) para começar a construir aplicações de IA!

## Recursos

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autenticação sem chave com Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Documentação Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Transição para Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java OpenAI oficial com Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Recursos Adicionais

- [Descarregar VS Code](https://code.visualstudio.com/Download)
- [Obter Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Configuração do Contentor de Desenvolvimento](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->