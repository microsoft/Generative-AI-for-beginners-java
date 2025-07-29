<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:10:55+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "pt"
}
-->
# Configurar o Ambiente de Desenvolvimento para IA Generativa com Java

> **Início Rápido**: Codifique na Nuvem em 2 minutos - Vá para [Configuração do GitHub Codespaces](../../../02-SetupDevEnvironment) - sem necessidade de instalação local e utiliza modelos do GitHub!

> **Interessado no Azure OpenAI?** Consulte o nosso [Guia de Configuração do Azure OpenAI](getting-started-azure-openai.md) com os passos para criar um novo recurso do Azure OpenAI.

## O Que Vai Aprender

- Configurar um ambiente de desenvolvimento Java para aplicações de IA
- Escolher e configurar o seu ambiente de desenvolvimento preferido (nuvem com Codespaces, contentor local ou configuração local completa)
- Testar a sua configuração conectando-se aos Modelos do GitHub

## Índice

- [O Que Vai Aprender](../../../02-SetupDevEnvironment)
- [Introdução](../../../02-SetupDevEnvironment)
- [Passo 1: Configurar o Seu Ambiente de Desenvolvimento](../../../02-SetupDevEnvironment)
  - [Opção A: GitHub Codespaces (Recomendado)](../../../02-SetupDevEnvironment)
  - [Opção B: Contentor de Desenvolvimento Local](../../../02-SetupDevEnvironment)
  - [Opção C: Usar a Sua Instalação Local Existente](../../../02-SetupDevEnvironment)
- [Passo 2: Criar um Token de Acesso Pessoal do GitHub](../../../02-SetupDevEnvironment)
- [Passo 3: Testar a Sua Configuração](../../../02-SetupDevEnvironment)
- [Resolução de Problemas](../../../02-SetupDevEnvironment)
- [Resumo](../../../02-SetupDevEnvironment)
- [Próximos Passos](../../../02-SetupDevEnvironment)

## Introdução

Este capítulo irá guiá-lo na configuração de um ambiente de desenvolvimento. Usaremos os **Modelos do GitHub** como exemplo principal porque são gratuitos, fáceis de configurar com apenas uma conta GitHub, não exigem cartão de crédito e oferecem acesso a vários modelos para experimentação.

**Sem necessidade de configuração local!** Pode começar a programar imediatamente usando o GitHub Codespaces, que fornece um ambiente de desenvolvimento completo no seu navegador.

<img src="./images/models.webp" alt="Captura de ecrã: Modelos do GitHub" width="50%">

Recomendamos usar os [**Modelos do GitHub**](https://github.com/marketplace?type=models) para este curso porque são:
- **Gratuitos** para começar
- **Fáceis** de configurar com apenas uma conta GitHub
- **Sem necessidade de cartão de crédito**
- **Vários modelos** disponíveis para experimentação

> **Nota**: Os Modelos do GitHub usados neste treino têm os seguintes limites gratuitos:
> - 15 pedidos por minuto (150 por dia)
> - ~8.000 palavras de entrada, ~4.000 palavras de saída por pedido
> - 5 pedidos simultâneos
> 
> Para uso em produção, atualize para os Modelos do Azure AI Foundry com a sua conta Azure. O seu código não precisa de alterações. Consulte a [documentação do Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Passo 1: Configurar o Seu Ambiente de Desenvolvimento

<a name="quick-start-cloud"></a>

Criámos um contentor de desenvolvimento pré-configurado para minimizar o tempo de configuração e garantir que tem todas as ferramentas necessárias para este curso de IA Generativa com Java. Escolha a abordagem de desenvolvimento que preferir:

### Opções de Configuração do Ambiente:

#### Opção A: GitHub Codespaces (Recomendado)

**Comece a programar em 2 minutos - sem necessidade de configuração local!**

1. Faça um fork deste repositório para a sua conta GitHub
   > **Nota**: Se quiser editar a configuração básica, consulte a [Configuração do Contentor de Desenvolvimento](../../../.devcontainer/devcontainer.json)
2. Clique em **Code** → separador **Codespaces** → **...** → **New with options...**
3. Use as predefinições – isto selecionará a **Configuração do Contentor de Desenvolvimento**: **Ambiente de Desenvolvimento Java para IA Generativa** criado para este curso
4. Clique em **Create codespace**
5. Aguarde ~2 minutos para o ambiente estar pronto
6. Prossiga para [Passo 2: Criar Token do GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Captura de ecrã: submenu Codespaces" width="50%">

<img src="./images/image.png" alt="Captura de ecrã: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Captura de ecrã: opções para criar codespace" width="50%">

> **Vantagens do Codespaces**:
> - Sem necessidade de instalação local
> - Funciona em qualquer dispositivo com um navegador
> - Pré-configurado com todas as ferramentas e dependências
> - 60 horas gratuitas por mês para contas pessoais
> - Ambiente consistente para todos os participantes

#### Opção B: Contentor de Desenvolvimento Local

**Para programadores que preferem desenvolvimento local com Docker**

1. Faça um fork e clone este repositório para a sua máquina local
   > **Nota**: Se quiser editar a configuração básica, consulte a [Configuração do Contentor de Desenvolvimento](../../../.devcontainer/devcontainer.json)
2. Instale o [Docker Desktop](https://www.docker.com/products/docker-desktop/) e o [VS Code](https://code.visualstudio.com/)
3. Instale a extensão [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) no VS Code
4. Abra a pasta do repositório no VS Code
5. Quando solicitado, clique em **Reopen in Container** (ou use `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Aguarde a construção e inicialização do contentor
7. Prossiga para [Passo 2: Criar Token do GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Captura de ecrã: configuração do contentor de desenvolvimento" width="50%">

<img src="./images/image-3.png" alt="Captura de ecrã: construção do contentor de desenvolvimento concluída" width="50%">

#### Opção C: Usar a Sua Instalação Local Existente

**Para programadores com ambientes Java já configurados**

Pré-requisitos:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ou o seu IDE preferido

Passos:
1. Clone este repositório para a sua máquina local
2. Abra o projeto no seu IDE
3. Prossiga para [Passo 2: Criar Token do GitHub](../../../02-SetupDevEnvironment)

> **Dica Pro**: Se tem uma máquina com especificações baixas mas quer usar o VS Code localmente, utilize o GitHub Codespaces! Pode conectar o seu VS Code local a um Codespace hospedado na nuvem para obter o melhor dos dois mundos.

<img src="./images/image-2.png" alt="Captura de ecrã: instância local do contentor de desenvolvimento criada" width="50%">

## Passo 2: Criar um Token de Acesso Pessoal do GitHub

1. Aceda a [Configurações do GitHub](https://github.com/settings/profile) e selecione **Settings** no menu do seu perfil.
2. Na barra lateral esquerda, clique em **Developer settings** (geralmente no final).
3. Em **Personal access tokens**, clique em **Fine-grained tokens** (ou siga este [link direto](https://github.com/settings/personal-access-tokens)).
4. Clique em **Generate new token**.
5. Em "Token name", forneça um nome descritivo (ex.: `GenAI-Java-Course-Token`).
6. Defina uma data de expiração (recomendado: 7 dias para melhores práticas de segurança).
7. Em "Resource owner", selecione a sua conta de utilizador.
8. Em "Repository access", selecione os repositórios que deseja usar com os Modelos do GitHub (ou "All repositories", se necessário).
9. Em "Repository permissions", encontre **Models** e defina como **Read and write**.
10. Clique em **Generate token**.
11. **Copie e guarde o seu token agora** – não poderá vê-lo novamente!

> **Dica de Segurança**: Use o escopo mínimo necessário e o menor tempo de expiração prático para os seus tokens de acesso.

## Passo 3: Testar a Sua Configuração com o Exemplo dos Modelos do GitHub

Depois de o ambiente de desenvolvimento estar pronto, vamos testar a integração com os Modelos do GitHub usando a aplicação de exemplo em [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Abra o terminal no seu ambiente de desenvolvimento.
2. Navegue até ao exemplo dos Modelos do GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Defina o seu token do GitHub como uma variável de ambiente:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Execute a aplicação:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Deverá ver uma saída semelhante a:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Compreendendo o Código de Exemplo

Primeiro, vamos entender o que acabámos de executar. O exemplo em `examples/github-models` utiliza o OpenAI Java SDK para conectar-se aos Modelos do GitHub:

**O que este código faz:**
- **Conecta-se** aos Modelos do GitHub usando o seu token de acesso pessoal
- **Envia** uma mensagem simples "Say Hello World!" ao modelo de IA
- **Recebe** e exibe a resposta da IA
- **Valida** que a sua configuração está a funcionar corretamente

**Dependência Principal** (em `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Código Principal** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Resumo

Parabéns! Agora tem tudo configurado:

- Criou um Token de Acesso Pessoal do GitHub com as permissões corretas para acesso aos modelos de IA
- Configurou o seu ambiente de desenvolvimento Java (seja com Codespaces, contentores de desenvolvimento ou localmente)
- Conectou-se aos Modelos do GitHub usando o OpenAI Java SDK para desenvolvimento de IA gratuito
- Testou que tudo funciona com um exemplo simples que interage com modelos de IA

## Próximos Passos

[Capítulo 3: Técnicas Fundamentais de IA Generativa](../03-CoreGenerativeAITechniques/README.md)

## Resolução de Problemas

Está com problemas? Aqui estão problemas comuns e soluções:

- **Token não funciona?** 
  - Certifique-se de que copiou o token completo sem espaços extras
  - Verifique se o token está definido corretamente como uma variável de ambiente
  - Confirme que o token tem as permissões corretas (Models: Read and write)

- **Maven não encontrado?** 
  - Se estiver a usar contentores de desenvolvimento/Codespaces, o Maven deve estar pré-instalado
  - Para configuração local, certifique-se de que o Java 21+ e o Maven 3.9+ estão instalados
  - Tente `mvn --version` para verificar a instalação

- **Problemas de conexão?** 
  - Verifique a sua conexão à internet
  - Confirme que o GitHub é acessível a partir da sua rede
  - Certifique-se de que não está atrás de um firewall que bloqueie o endpoint dos Modelos do GitHub

- **Contentor de desenvolvimento não inicia?** 
  - Certifique-se de que o Docker Desktop está em execução (para desenvolvimento local)
  - Tente reconstruir o contentor: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Erros de compilação da aplicação?**
  - Certifique-se de que está no diretório correto: `02-SetupDevEnvironment/examples/github-models`
  - Tente limpar e reconstruir: `mvn clean compile`

> **Precisa de ajuda?**: Ainda com problemas? Abra um issue no repositório e ajudaremos.

**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte autoritária. Para informações críticas, recomenda-se uma tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes do uso desta tradução.