# Configurando o Ambiente de Desenvolvimento para Azure AI Foundry

> Este guia configura modelos do **Azure AI Foundry** para os aplicativos de IA em Java deste curso, usando autenticação **sem chave** (Microsoft Entra ID) — sem chaves de API para gerenciar. Novo nas ferramentas? Comece com o [guia do ambiente de desenvolvimento](./README.md).

Este guia configura modelos do **Azure AI Foundry** para os aplicativos de IA em Java deste curso. Você tem duas opções:

- **Opção A — Provisionar com `azd` + Bicep (recomendado):** um comando implanta a conta Foundry e modelos como código. Sem clicar no portal.
- **Opção B — Criar recursos manualmente** no portal Azure AI Foundry.

Ambos os caminhos usam **autenticação sem chave** (Microsoft Entra ID) — não há chaves de API para copiar ou vazar.

## Sumário

- [O que é criado](#o-que-é-criado)
- [Pré-requisitos](#pré-requisitos)
- [Opção A: Provisionar com azd + Bicep (Recomendado)](#option-a-provision-with-azd--bicep-recommended)
- [Opção B: Criar Recursos Manualmente](#opção-b-criar-recursos-manualmente)
- [Configure Seu Ambiente](#configure-seu-ambiente)
- [Teste Sua Configuração](#teste-sua-configuração)
- [O Que Vem Depois?](#o-que-vem-depois)
- [Recursos](#recursos)
- [Recursos Adicionais](#recursos-adicionais)

## O que é criado

Os templates Bicep em [`infra/`](../../../02-SetupDevEnvironment/infra) provisionam:

- Uma conta do **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, tipo `AIServices`) com um projeto
- Um deployment de **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), versão `2026-07-09`, com capacidade `GlobalStandard` `10` (10 requisições/minuto e 10.000 tokens/minuto para este modelo)
- Um deployment de **embedding** - `text-embedding-3-small`, versão `1` (usado nos capítulos seguintes)
- Uma **atribuição de função sem chave** (`Cognitive Services OpenAI User`) para que você faça login com `az login` em vez de gerenciar chaves

## Pré-requisitos

- Uma [assinatura do Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) e [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opção A: Provisionar com azd + Bicep (Recomendado)

Na pasta `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Entrar (ambas as ferramentas)
azd auth login
az login

# Provisionar a conta Foundry + implantações de modelos
azd up
```

`azd` solicitará um **nome do ambiente** (por exemplo `genai-java`), **assinatura** e **região**. Escolha sua assinatura e uma região onde `gpt-5.6-luna` e `text-embedding-3-small` estejam disponíveis, por exemplo `eastus2`. Confirme se a assinatura tem cota suficiente para o modelo e tipo de implantação nessa região; disponibilidade e cota variam por assinatura.

Quando o provisionamento terminar, azd:

1. Implanta tudo definido em [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Executa um hook pós-provisionamento que escreve [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) com seu endpoint e nomes de implantação (sem segredos).

> **Dica:** Reexecute `azd up` a qualquer momento para aplicar mudanças. Execute `azd down` para deletar tudo e parar de gerar custos.

Para ver as configurações geradas:

```bash
azd env get-values
```

Agora pule para [Teste Sua Configuração](#teste-sua-configuração).

## Opção B: Criar Recursos Manualmente

Prefere o portal? Crie os recursos manualmente:

1. Vá para o [portal Azure AI Foundry](https://ai.azure.com/) e faça login.
2. **Crie um projeto** (isso também cria um recurso AI Foundry). Dê um nome como `GenAIJava`.
3. No seu projeto, abra **Modelos + endpoints** → **Implantar modelo** → **Implantar modelo base**.
4. Faça o deploy do **GPT-5.6 Luna** (nome do modelo e deployment `gpt-5.6-luna`, versão `2026-07-09`) com capacidade **Global Standard** `10`. Repita para o **text-embedding-3-small**, versão `1`, se desejar os exemplos de embedding.
5. Em **Visão geral**, copie o **endpoint** (por exemplo `https://<resource>.openai.azure.com/`).
6. Conceda a si mesmo acesso sem chave: no recurso, abra **Controle de acesso (IAM)** → **Adicionar atribuição de função** → atribua **Cognitive Services OpenAI User** à sua conta.

> **Ainda com dificuldades?** Veja a [documentação do Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configure Seu Ambiente

**Se você usou a Opção A (`azd up`)**, seu arquivo de configurações já está escrito — não há nada para configurar. Pule para [Teste Sua Configuração](#teste-sua-configuração).

**Se você usou a Opção B (manual)**, crie o arquivo `.env` do exemplo você mesmo:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Edite `.env` com seu endpoint (sem chave — a autenticação é sem chave):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Use o endpoint Azure OpenAI do recurso, não a URL do projeto. O app basic-chat resolve isso para `/openai/v1` e configura um cliente com token bearer explícito; uma chave de API não é necessária.

> **Nota de segurança:** Não há chave de API para armazenar. Você se autentica com Microsoft Entra ID via `az login` (localmente) ou identidade gerenciada (no Azure). O arquivo `.env` contém apenas configurações não secretas e já está protegido por `.gitignore`.

## Teste Sua Configuração

Certifique-se de que você está logado para que a autenticação sem chave possa obter um token, então execute o exemplo:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # se você ainda não estiver conectado
mvn clean spring-boot:run
```

Você deve ver uma resposta do modelo `gpt-5.6-luna`. Execute os exemplos sequencialmente para manter-se dentro da pequena cota padrão; se receber HTTP 429, espere o intervalo de tentativa antes de tentar novamente.

> **Usuários do VS Code:** Pressione `F5` para executar. O app carrega seu `.env` automaticamente.

> **Exemplo completo:** Veja o [Exemplo de Chat Básico com Azure AI Foundry](./examples/basic-chat-azure/README.md) para detalhes e solução de problemas.

## O Que Vem Depois?

Após o provisionamento e a execução bem-sucedida do exemplo, você terá:
- Azure AI Foundry com `gpt-5.6-luna` e `text-embedding-3-small` implantados
- Autenticação sem chave (Microsoft Entra ID) — sem chaves para gerenciar
- Um `.env` local com seu endpoint e nomes de implantação
- Um ambiente de desenvolvimento Java pronto para uso

**Continue para** [Capítulo 3: Técnicas Centrais de IA Generativa](../03-CoreGenerativeAITechniques/README.md) para começar a construir aplicativos de IA!

## Recursos

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autenticação sem chave com Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Documentação do Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Transição Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java OpenAI oficial com Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Recursos Adicionais

- [Baixar VS Code](https://code.visualstudio.com/Download)
- [Obter Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Configuração do Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->