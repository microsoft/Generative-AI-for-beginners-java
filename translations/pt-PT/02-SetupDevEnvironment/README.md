# Configurar o Ambiente de Desenvolvimento para IA Generativa em Java

> **Início Rápido:** Providencie os seus modelos de IA no **Azure AI Foundry** como código com Bicep + `azd` em poucos minutos — veja o [Guia de Configuração do Azure AI Foundry](getting-started-azure-openai.md). A autenticação é **sem chave** (Microsoft Entra ID), pelo que não há chaves API para gerir.

## O que Vai Aprender

- Configurar um ambiente de desenvolvimento Java para aplicações de IA
- Escolher e configurar o seu ambiente de desenvolvimento preferido (cloud-first com Codespaces, contentor de desenvolvimento local, ou configuração local completa)
- Testar a sua configuração ligando-se a um modelo Azure AI Foundry

## Índice

- [O que Vai Aprender](#o-que-vai-aprender)
- [Introdução](#introdução)
- [Passo 1: Configure o Seu Ambiente de Desenvolvimento](#passo-1-configure-o-seu-ambiente-de-desenvolvimento)
  - [Opção A: GitHub Codespaces (Recomendado)](#opção-a-github-codespaces-recomendado)
  - [Opção B: Contentor de Desenvolvimento Local](#opção-b-contentor-de-desenvolvimento-local)
  - [Opção C: Utilize a Sua Instalação Local Existente](#opção-c-utilize-a-sua-instalação-local-existente)
- [Passo 2: Providencie o Azure AI Foundry](#passo-2-providencie-o-azure-ai-foundry)
- [Passo 3: Teste a Sua Configuração](#passo-3-teste-a-sua-configuração)
- [Resolução de Problemas](#resolução-de-problemas)
- [Resumo](#resumo)
- [Próximos Passos](#próximos-passos)

## Introdução

Este capítulo irá guiar você na configuração de um ambiente de desenvolvimento. Vamos usar o **Azure AI Foundry** para os modelos ao longo deste curso. Você providencia os modelos como código com Bicep e a Azure Developer CLI (`azd`), e depois liga-se com **autenticação sem chave** (Microsoft Entra ID) — sem necessidade de copiar ou expor chaves API.

**Não é necessário configuração local!** Pode usar o GitHub Codespaces, que fornece um ambiente de desenvolvimento completo no seu navegador, e providenciar o Foundry a partir daí.

Usamos o **Azure AI Foundry** neste curso porque é:
- **Providenciado como código** — um único `azd up` implementa a conta e os deployments dos modelos
- **Sem chave** — autentique com o seu login Azure ou uma identidade gerida
- **Pronto para produção** — o mesmo código corre localmente e na Azure
- **Flexível** — troque modelos alterando o nome do deployment, não o seu código

> **Nota**: Os deployments do Azure AI Foundry são faturados por token (pague conforme usa). Veja o [guia de configuração do Azure AI Foundry](getting-started-azure-openai.md) para detalhes sobre provisão, região e custos.


## Passo 1: Configure o Seu Ambiente de Desenvolvimento

<a name="quick-start-cloud"></a>

Criámos um contentor de desenvolvimento pré-configurado para minimizar o tempo de configuração e garantir que tem todas as ferramentas necessárias para este curso de IA Generativa em Java. Escolha a abordagem de desenvolvimento preferida:

### Opções de Configuração do Ambiente:

#### Opção A: GitHub Codespaces (Recomendado)

**Comece a codificar em 2 minutos - sem necessidade de configuração local!**

1. Faça fork deste repositório para a sua conta GitHub
   > **Nota**: Se quiser editar a configuração básica, consulte a [Configuração do Contentor de Desenvolvimento](../../../.devcontainer/devcontainer.json)
2. Clique em **Code** → separador **Codespaces** → **...** → **New with options...**
3. Use as predefinições – isto irá selecionar a **configuração do contentor de desenvolvimento**: **Ambiente de Desenvolvimento Java para IA Generativa**, contentor personalizado criado para este curso
4. Clique em **Create codespace**
5. Espere cerca de 2 minutos para o ambiente estar pronto
6. Prossiga para o [Passo 2: Providencie o Azure AI Foundry](#passo-2-providencie-o-azure-ai-foundry)

<img src="../../../translated_images/pt-PT/codespaces.9945ded8ceb431a5.webp" alt="Captura de ecrã: submenu Codespaces" width="50%">

<img src="../../../translated_images/pt-PT/image.833552b62eee7766.webp" alt="Captura de ecrã: Novo com opções" width="50%">

<img src="../../../translated_images/pt-PT/codespaces-create.b44a36f728660ab7.webp" alt="Captura de ecrã: opções para criar codespace" width="50%">


> **Vantagens dos Codespaces**:
> - Não requer instalação local
> - Funciona em qualquer dispositivo com um navegador
> - Pré-configurado com todas as ferramentas e dependências
> - 60 horas grátis por mês para contas pessoais
> - Ambiente consistente para todos os formandos

#### Opção B: Contentor de Desenvolvimento Local

**Para desenvolvedores que preferem desenvolvimento local com Docker**

1. Faça fork e clone este repositório para a sua máquina local
   > **Nota**: Se quiser editar a configuração básica, consulte a [Configuração do Contentor de Desenvolvimento](../../../.devcontainer/devcontainer.json)
2. Instale o [Docker Desktop](https://www.docker.com/products/docker-desktop/) e o [VS Code](https://code.visualstudio.com/)
3. Instale a [extensão Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) no VS Code
4. Abra a pasta do repositório no VS Code
5. Quando solicitado, clique em **Reopen in Container** (ou use `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Espere que o contentor seja construído e iniciado
7. Prossiga para o [Passo 2: Providencie o Azure AI Foundry](#passo-2-providencie-o-azure-ai-foundry)

<img src="../../../translated_images/pt-PT/devcontainer.21126c9d6de64494.webp" alt="Captura de ecrã: configuração do contentor de desenvolvimento" width="50%">

<img src="../../../translated_images/pt-PT/image-3.bf93d533bbc84268.webp" alt="Captura de ecrã: contentor de desenvolvimento construído" width="50%">

#### Opção C: Utilize a Sua Instalação Local Existente

**Para desenvolvedores com ambientes Java já existentes**

Pré-requisitos:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ou o seu IDE preferido

Passos:
1. Clone este repositório para a sua máquina local
2. Abra o projeto no seu IDE
3. Prossiga para o [Passo 2: Providencie o Azure AI Foundry](#passo-2-providencie-o-azure-ai-foundry)

> **Dica Profissional**: Se tem uma máquina com especificações baixas mas quer usar VS Code localmente, use o GitHub Codespaces! Pode ligar o seu VS Code local a um Codespace alojado na cloud para tirar o melhor dos dois mundos.

<img src="../../../translated_images/pt-PT/image-2.fc0da29a6e4d2aff.webp" alt="Captura de ecrã: instância local devcontainer criada" width="50%">


## Passo 2: Providencie o Azure AI Foundry

Implemente os modelos IA do curso no Azure AI Foundry como código. A partir da raiz do repositório:

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` solicita o nome do ambiente, subscrição e região, providencia uma conta Azure AI Foundry com os deployments `gpt-5.6-luna` e `text-embedding-3-small`, e escreve o endpoint no `.env` do exemplo - tudo com autenticação **sem chave** (sem chaves API).

> **Tutorial completo:** Veja o [Guia de Configuração do Azure AI Foundry](getting-started-azure-openai.md) para pré-requisitos, alternativa manual (portal), orientação de região, e notas sobre custos/limpeza.

## Passo 3: Teste a Sua Configuração

Assim que os seus modelos Foundry estiverem providenciados, teste a ligação com a aplicação exemplo em [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure).

1. Abra o terminal no seu ambiente de desenvolvimento.
2. Navegue para o exemplo:
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. Certifique-se de que está autenticado (a autenticação sem chave necessita de um token):
   ```bash
   az login
   ```
   > Se executou `azd up`, o ficheiro `.env` com o seu endpoint já terá sido escrito para si.
4. Execute a aplicação:
   ```bash
   mvn clean spring-boot:run
   ```

Deve ver uma resposta do modelo `gpt-5.6-luna`.

### Compreender o Código Exemplo

O [exemplo basic-chat](./examples/basic-chat-azure/README.md) usa **Spring Boot 4.1.1** e **Spring AI 2.0.1**. O `ChatClient` do Spring AI é suportado pelo SDK oficial OpenAI Java, ligando ao endpoint Azure OpenAI **v1** com autenticação sem chave.

**O que este código faz:**
- **Liga-se** ao Azure AI Foundry usando o seu login Azure (Microsoft Entra ID) — sem chave API
- **Envia** um prompt para o modelo `gpt-5.6-luna`
- **Recebe** e mostra a resposta da IA
- **Valida** que a sua configuração está a funcionar corretamente

**Dependências Chave** (excerto do [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)):
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

O POM gere o OpenAI Java **4.63.1** e define explicitamente Azure Identity **1.18.6**. O Spring AI 2 removeu o starter Azure-specific; o Azure Identity é ainda necessário para o bean de credenciais.

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

A autenticação sem chave é configurada explicitamente em [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java), não inferida a partir da ausência de chave API. A sua credencial bearer usa `DefaultAzureCredential` com o âmbito `https://ai.azure.com/.default`, e o seu `OpenAIClient` aponta para `/openai/v1`. A aplicação fornece esse cliente ao modelo de chat do Spring AI, pelo que uma variável global `OPENAI_API_KEY` não pode sobrepor a autenticação Azure.

As definições do chat estão diretamente sob `spring.ai.openai.chat`, sem bloco `options`. A lição mantém as Chat Completions com `reasoning-effort: none` e um limite de 500 tokens na completion; não define `temperature` nem `max-tokens`. Veja a [referência de configuração do exemplo](./examples/basic-chat-azure/README.md#spring-configuration) para orientação sobre a escolha da API e chamadas a ferramentas.

## Resumo

Após completar os passos acima, terá:

- Modelos Azure AI Foundry providenciados como código com Bicep + `azd`
- O seu ambiente de desenvolvimento Java configurado (seja Codespaces, contentores de desenvolvimento, ou local)
- Ligação ao Azure AI Foundry com autenticação sem chave (Microsoft Entra ID) — sem chaves API
- Testado que tudo funciona com um exemplo simples que comunica com o seu modelo

## Próximos Passos

[Capítulo 3: Técnicas Core de IA Generativa](../03-CoreGenerativeAITechniques/README.md)

## Resolução de Problemas

Está a ter problemas? Aqui estão os problemas comuns e soluções:

- **Autenticação falha (401/403)?** 
  - Execute `az login` — a autenticação é sem chave, portanto deve estar autenticado
  - Verifique se a sua conta tem o papel **Cognitive Services OpenAI User** no recurso
  - Se acabou de providenciar, espere um minuto para a atribuição do papel propagar

- **Maven não encontrado?** 
  - Se estiver a usar contentores de desenvolvimento/Codespaces, o Maven deve estar pré-instalado
  - Para configuração local, certifique-se que Java 21+ e Maven 3.9+ estão instalados
  - Experimente `mvn --version` para verificar a instalação

- **`azd` não encontrado ou provisão falha?** 
  - Instale a [Azure Developer CLI](https://aka.ms/azure-dev/install) e execute `azd auth login`
  - Escolha uma região onde `gpt-5.6-luna` e `text-embedding-3-small` estejam disponíveis (ex.: `eastus2`), com quota suficiente na subscrição selecionada
  - Veja o [guia de configuração do Azure AI Foundry](getting-started-azure-openai.md) para detalhes

- **Contentor de desenvolvimento não inicia?** 
  - Certifique-se que o Docker Desktop está a correr (para desenvolvimento local)
  - Tente reconstruir o contentor: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Erros na compilação da aplicação?**
  - Certifique-se que está no diretório correto: `02-SetupDevEnvironment/examples/basic-chat-azure`
  - Experimente limpar e reconstruir: `mvn clean compile`

> **Precisa de ajuda?**: Continua com problemas? Abra uma issue no repositório e ajudaremos.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->