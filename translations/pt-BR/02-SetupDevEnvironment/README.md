# Configurando o Ambiente de Desenvolvimento para IA Generativa para Java

> **Início Rápido:** Providencie seus modelos de IA no **Azure AI Foundry** como código com Bicep + `azd` em poucos minutos — veja o [Guia de Configuração do Azure AI Foundry](getting-started-azure-openai.md). A autenticação é **sem chaves** (Microsoft Entra ID), então não há chaves de API para gerenciar.

## O que você vai aprender

- Configurar um ambiente de desenvolvimento Java para aplicações de IA
- Escolher e configurar seu ambiente de desenvolvimento preferido (priorizando nuvem com Codespaces, contêiner de desenvolvimento local ou configuração totalmente local)
- Testar sua configuração conectando-se a um modelo do Azure AI Foundry

## Índice

- [O que você vai aprender](#o-que-você-vai-aprender)
- [Introdução](#introdução)
- [Passo 1: Configure Seu Ambiente de Desenvolvimento](#passo-1-configure-seu-ambiente-de-desenvolvimento)
  - [Opção A: GitHub Codespaces (Recomendado)](#opção-a-github-codespaces-recomendado)
  - [Opção B: Contêiner de Desenvolvimento Local](#opção-b-contêiner-de-desenvolvimento-local)
  - [Opção C: Use Sua Instalação Local Existente](#opção-c-use-sua-instalação-local-existente)
- [Passo 2: Provisionar Azure AI Foundry](#passo-2-provisionar-azure-ai-foundry)
- [Passo 3: Teste Sua Configuração](#passo-3-teste-sua-configuração)
- [Solução de Problemas](#solução-de-problemas)
- [Resumo](#resumo)
- [Próximos Passos](#próximos-passos)

## Introdução

Este capítulo irá guiá-lo na configuração de um ambiente de desenvolvimento. Usaremos o **Azure AI Foundry** para os modelos ao longo deste curso. Você provisiona os modelos como código com Bicep e a Azure Developer CLI (`azd`), depois se conecta com **autenticação sem chave** (Microsoft Entra ID) — sem chaves de API para copiar ou vazar.

**Nenhuma configuração local necessária!** Você pode usar o GitHub Codespaces, que oferece um ambiente completo de desenvolvimento no seu navegador, e provisionar o Foundry de lá.

Usamos o **Azure AI Foundry** neste curso porque:
- **Provisionado como código** — um único `azd up` implanta a conta e os deployments dos modelos
- **Sem chaves** — autentique com seu login Azure ou identidade gerenciada
- **Pronto para produção** — o mesmo código roda localmente e no Azure
- **Flexível** — troque modelos mudando o nome do deployment, sem alterar seu código

> **Nota**: Os deployments do Azure AI Foundry são cobrados por token (pague pelo uso). Veja o [guia de configuração do Azure AI Foundry](getting-started-azure-openai.md) para detalhes sobre provisionamento, região e custos.


## Passo 1: Configure Seu Ambiente de Desenvolvimento

<a name="quick-start-cloud"></a>

Criamos um contêiner de desenvolvimento pré-configurado para minimizar o tempo de configuração e garantir que você tenha todas as ferramentas necessárias para este curso de IA Generativa para Java. Escolha sua abordagem de desenvolvimento preferida:

### Opções de Configuração do Ambiente:

#### Opção A: GitHub Codespaces (Recomendado)

**Comece a programar em 2 minutos - sem necessidade de configuração local!**

1. Faça um fork deste repositório para sua conta do GitHub
   > **Nota**: Se quiser editar a configuração básica, veja a [Configuração do Dev Container](../../../.devcontainer/devcontainer.json)
2. Clique em **Code** → aba **Codespaces** → **...** → **Novo com opções...**
3. Use os padrões – isso selecionará a **Configuração do Dev Container**: **Ambiente de Desenvolvimento Java para IA Generativa** devcontainer personalizado criado para este curso
4. Clique em **Create codespace**
5. Aguarde aproximadamente 2 minutos para o ambiente ficar pronto
6. Prossiga para [Passo 2: Provisionar Azure AI Foundry](#passo-2-provisionar-azure-ai-foundry)

<img src="../../../translated_images/pt-BR/codespaces.9945ded8ceb431a5.webp" alt="Screenshot: submenu Codespaces" width="50%">

<img src="../../../translated_images/pt-BR/image.833552b62eee7766.webp" alt="Screenshot: Novo com opções" width="50%">

<img src="../../../translated_images/pt-BR/codespaces-create.b44a36f728660ab7.webp" alt="Screenshot: Opções de criação de codespace" width="50%">


> **Benefícios dos Codespaces**:
> - Sem necessidade de instalação local
> - Funciona em qualquer dispositivo com navegador
> - Pré-configurado com todas as ferramentas e dependências
> - 60 horas grátis por mês para contas pessoais
> - Ambiente consistente para todos os aprendizes

#### Opção B: Contêiner de Desenvolvimento Local

**Para desenvolvedores que preferem desenvolvimento local com Docker**

1. Faça um fork e clone este repositório para sua máquina local
   > **Nota**: Se quiser editar a configuração básica, veja a [Configuração do Dev Container](../../../.devcontainer/devcontainer.json)
2. Instale o [Docker Desktop](https://www.docker.com/products/docker-desktop/) e o [VS Code](https://code.visualstudio.com/)
3. Instale a [extensão Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) no VS Code
4. Abra a pasta do repositório no VS Code
5. Quando solicitado, clique em **Reopen in Container** (ou use `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Aguarde enquanto o contêiner é construído e iniciado
7. Prossiga para [Passo 2: Provisionar Azure AI Foundry](#passo-2-provisionar-azure-ai-foundry)

<img src="../../../translated_images/pt-BR/devcontainer.21126c9d6de64494.webp" alt="Screenshot: configuração do dev container" width="50%">

<img src="../../../translated_images/pt-BR/image-3.bf93d533bbc84268.webp" alt="Screenshot: build do dev container completo" width="50%">

#### Opção C: Use Sua Instalação Local Existente

**Para desenvolvedores com ambientes Java existentes**

Pré-requisitos:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ou seu IDE preferido

Passos:
1. Clone este repositório para sua máquina local
2. Abra o projeto no seu IDE
3. Prossiga para [Passo 2: Provisionar Azure AI Foundry](#passo-2-provisionar-azure-ai-foundry)

> **Dica Profissional**: Se sua máquina é de baixa especificação mas você quer usar VS Code localmente, use GitHub Codespaces! Você pode conectar seu VS Code local a um Codespace hospedado na nuvem para o melhor dos dois mundos.

<img src="../../../translated_images/pt-BR/image-2.fc0da29a6e4d2aff.webp" alt="Screenshot: instância local do devcontainer criada" width="50%">


## Passo 2: Provisionar Azure AI Foundry

Implante os modelos de IA do curso no Azure AI Foundry como código. A partir da raiz do repositório:

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` solicita o nome do ambiente, assinatura e região, provisiona uma conta Azure AI Foundry com os deployments `gpt-5.6-luna` e `text-embedding-3-small`, e grava o endpoint no `.env` do exemplo — tudo com autenticação **sem chave** (sem chaves de API).

> **Tutorial completo:** Veja o [Guia de Configuração do Azure AI Foundry](getting-started-azure-openai.md) para pré-requisitos, alternativa manual (portal), orientação de região e notas sobre custo/limpeza.

## Passo 3: Teste Sua Configuração

Uma vez que seus modelos Foundry estejam provisionados, teste a conexão com o app de exemplo em [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure).

1. Abra o terminal no seu ambiente de desenvolvimento.
2. Navegue até o exemplo:
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. Certifique-se de estar logado (a autenticação sem chave precisa de token):
   ```bash
   az login
   ```
   > Se você executou `azd up`, o arquivo `.env` com seu endpoint já foi escrito para você.
4. Execute o aplicativo:
   ```bash
   mvn clean spring-boot:run
   ```

Você deverá ver uma resposta do modelo `gpt-5.6-luna`.

### Entendendo o Código do Exemplo

O [exemplo basic-chat](./examples/basic-chat-azure/README.md) usa **Spring Boot 4.1.1** e **Spring AI 2.0.1**. O `ChatClient` do Spring AI é suportado pelo SDK Java oficial do OpenAI, conectando ao endpoint Azure OpenAI **v1** com autenticação sem chave.

**O que este código faz:**
- **Conecta** ao Azure AI Foundry usando seu login Azure (Microsoft Entra ID) — sem chave de API
- **Envia** uma solicitação ao modelo `gpt-5.6-luna`
- **Recebe** e exibe a resposta da IA
- **Valida** que sua configuração está funcionando corretamente

**Dependências Chave** (trecho do [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)):
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
</dependency>
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>${azure-identity.version}</version>
</dependency>
```

O POM gerencia o OpenAI Java **4.63.1** e define explicitamente o Azure Identity **1.18.6**. O Spring AI 2 removeu o starter específico do Azure; o Azure Identity ainda é necessário para o bean de credenciais.

**Configuração** ([application.yml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)):
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

A autenticação sem chave é configurada explicitamente em [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java), não inferida pela ausência de chave de API. Suas credenciais de portador usam `DefaultAzureCredential` com escopo `https://ai.azure.com/.default`, e seu `OpenAIClient` aponta para `/openai/v1`. O app fornece esse cliente ao modelo de chat do Spring AI, então uma variável global `OPENAI_API_KEY` não pode sobrescrever a autenticação Azure.

As configurações do chat estão diretamente sob `spring.ai.openai.chat`, sem um bloco `options`. A lição mantém Chat Completions com `reasoning-effort: none` e um limite de 500 tokens; não define `temperature` nem `max-tokens`. Veja a [referência de configuração do exemplo](./examples/basic-chat-azure/README.md#spring-configuration) para escolha de API e orientação de chamadas de ferramentas.

## Resumo

Após completar os passos acima, você terá:

- Provisionado modelos Azure AI Foundry como código com Bicep + `azd`
- Seu ambiente de desenvolvimento Java rodando (seja Codespaces, contêineres dev ou local)
- Conectado ao Azure AI Foundry com autenticação sem chave (Microsoft Entra ID) — sem chaves de API
- Testado tudo funcionando com um exemplo simples que conversa com seu modelo

## Próximos Passos

[Capítulo 3: Técnicas Básicas de IA Generativa](../03-CoreGenerativeAITechniques/README.md)

## Solução de Problemas

Está enfrentando problemas? Aqui estão problemas comuns e soluções:

- **Falha na autenticação (401/403)?** 
  - Execute `az login` — autenticação é sem chave, então você deve estar logado
  - Verifique se sua conta tem o papel **Cognitive Services OpenAI User** no recurso
  - Se acabou de provisionar, espere um minuto para a propagação da atribuição do papel

- **Maven não encontrado?** 
  - Se usar dev containers/Codespaces, o Maven já deve estar pré-instalado
  - Para configuração local, assegure que Java 21+ e Maven 3.9+ estejam instalados
  - Tente `mvn --version` para verificar a instalação

- **`azd` não encontrado ou provisionamento falha?** 
  - Instale a [Azure Developer CLI](https://aka.ms/azure-dev/install) e execute `azd auth login`
  - Escolha uma região onde `gpt-5.6-luna` e `text-embedding-3-small` estejam disponíveis (ex: `eastus2`), com cota suficiente na assinatura selecionada
  - Veja o [guia de configuração do Azure AI Foundry](getting-started-azure-openai.md) para detalhes

- **Contêiner dev não inicia?** 
  - Verifique se o Docker Desktop está rodando (para desenvolvimento local)
  - Tente reconstruir o container: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Erros de compilação da aplicação?**
  - Certifique-se de estar no diretório correto: `02-SetupDevEnvironment/examples/basic-chat-azure`
  - Tente limpar e compilar: `mvn clean compile`

> **Precisa de ajuda?**: Ainda com problemas? Abra uma issue no repositório e vamos ajudá-lo.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->