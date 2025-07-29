<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:13:53+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "br"
}
-->
# Configurando o Ambiente de Desenvolvimento para IA Generativa em Java

> **Início Rápido**: Codifique na nuvem em 2 minutos - Vá para [Configuração do GitHub Codespaces](../../../02-SetupDevEnvironment) - sem necessidade de instalação local e utilizando modelos do GitHub!

> **Interessado no Azure OpenAI?** Veja nosso [Guia de Configuração do Azure OpenAI](getting-started-azure-openai.md) com etapas para criar um novo recurso do Azure OpenAI.

## O Que Você Vai Aprender

- Configurar um ambiente de desenvolvimento Java para aplicações de IA
- Escolher e configurar seu ambiente de desenvolvimento preferido (priorizando a nuvem com Codespaces, contêiner local ou configuração totalmente local)
- Testar sua configuração conectando-se aos Modelos do GitHub

## Índice

- [O Que Você Vai Aprender](../../../02-SetupDevEnvironment)
- [Introdução](../../../02-SetupDevEnvironment)
- [Passo 1: Configure Seu Ambiente de Desenvolvimento](../../../02-SetupDevEnvironment)
  - [Opção A: GitHub Codespaces (Recomendado)](../../../02-SetupDevEnvironment)
  - [Opção B: Contêiner Local](../../../02-SetupDevEnvironment)
  - [Opção C: Usar Sua Instalação Local Existente](../../../02-SetupDevEnvironment)
- [Passo 2: Crie um Token de Acesso Pessoal no GitHub](../../../02-SetupDevEnvironment)
- [Passo 3: Teste Sua Configuração](../../../02-SetupDevEnvironment)
- [Solução de Problemas](../../../02-SetupDevEnvironment)
- [Resumo](../../../02-SetupDevEnvironment)
- [Próximos Passos](../../../02-SetupDevEnvironment)

## Introdução

Este capítulo irá guiá-lo na configuração de um ambiente de desenvolvimento. Usaremos **Modelos do GitHub** como nosso exemplo principal porque é gratuito, fácil de configurar com apenas uma conta do GitHub, não exige cartão de crédito e oferece acesso a vários modelos para experimentação.

**Sem necessidade de configuração local!** Você pode começar a codificar imediatamente usando o GitHub Codespaces, que fornece um ambiente de desenvolvimento completo no seu navegador.

<img src="./images/models.webp" alt="Captura de tela: Modelos do GitHub" width="50%">

Recomendamos usar [**Modelos do GitHub**](https://github.com/marketplace?type=models) para este curso porque:
- É **gratuito** para começar
- **Fácil** de configurar com apenas uma conta do GitHub
- **Não exige cartão de crédito**
- **Vários modelos** disponíveis para experimentação

> **Nota**: Os Modelos do GitHub usados neste treinamento possuem os seguintes limites gratuitos:
> - 15 solicitações por minuto (150 por dia)
> - ~8.000 palavras de entrada, ~4.000 palavras de saída por solicitação
> - 5 solicitações simultâneas
> 
> Para uso em produção, atualize para Modelos do Azure AI Foundry com sua conta do Azure. Seu código não precisa mudar. Veja a [documentação do Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Passo 1: Configure Seu Ambiente de Desenvolvimento

<a name="quick-start-cloud"></a>

Criamos um contêiner de desenvolvimento pré-configurado para minimizar o tempo de configuração e garantir que você tenha todas as ferramentas necessárias para este curso de IA Generativa em Java. Escolha sua abordagem de desenvolvimento preferida:

### Opções de Configuração do Ambiente:

#### Opção A: GitHub Codespaces (Recomendado)

**Comece a codificar em 2 minutos - sem necessidade de configuração local!**

1. Faça um fork deste repositório para sua conta do GitHub
   > **Nota**: Se você quiser editar a configuração básica, veja a [Configuração do Contêiner de Desenvolvimento](../../../.devcontainer/devcontainer.json)
2. Clique em **Code** → aba **Codespaces** → **...** → **New with options...**
3. Use os padrões – isso selecionará a **Configuração do Contêiner de Desenvolvimento**: **Ambiente de Desenvolvimento Java para IA Generativa** criado para este curso
4. Clique em **Create codespace**
5. Aguarde ~2 minutos para o ambiente estar pronto
6. Prossiga para [Passo 2: Criar Token do GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Captura de tela: submenu Codespaces" width="50%">

<img src="./images/image.png" alt="Captura de tela: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Captura de tela: opções de criação de codespace" width="50%">

> **Benefícios do Codespaces**:
> - Sem necessidade de instalação local
> - Funciona em qualquer dispositivo com um navegador
> - Pré-configurado com todas as ferramentas e dependências
> - 60 horas gratuitas por mês para contas pessoais
> - Ambiente consistente para todos os alunos

#### Opção B: Contêiner Local

**Para desenvolvedores que preferem desenvolvimento local com Docker**

1. Faça um fork e clone este repositório para sua máquina local
   > **Nota**: Se você quiser editar a configuração básica, veja a [Configuração do Contêiner de Desenvolvimento](../../../.devcontainer/devcontainer.json)
2. Instale [Docker Desktop](https://www.docker.com/products/docker-desktop/) e [VS Code](https://code.visualstudio.com/)
3. Instale a extensão [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) no VS Code
4. Abra a pasta do repositório no VS Code
5. Quando solicitado, clique em **Reopen in Container** (ou use `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Aguarde o contêiner ser construído e iniciado
7. Prossiga para [Passo 2: Criar Token do GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Captura de tela: configuração do contêiner de desenvolvimento" width="50%">

<img src="./images/image-3.png" alt="Captura de tela: contêiner de desenvolvimento concluído" width="50%">

#### Opção C: Usar Sua Instalação Local Existente

**Para desenvolvedores com ambientes Java existentes**

Pré-requisitos:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ou seu IDE preferido

Passos:
1. Clone este repositório para sua máquina local
2. Abra o projeto no seu IDE
3. Prossiga para [Passo 2: Criar Token do GitHub](../../../02-SetupDevEnvironment)

> **Dica Pro**: Se você tem uma máquina com especificações baixas, mas quer usar o VS Code localmente, utilize o GitHub Codespaces! Você pode conectar seu VS Code local a um Codespace hospedado na nuvem para o melhor dos dois mundos.

<img src="./images/image-2.png" alt="Captura de tela: instância local do contêiner de desenvolvimento criada" width="50%">

## Passo 2: Crie um Token de Acesso Pessoal no GitHub

1. Navegue até [Configurações do GitHub](https://github.com/settings/profile) e selecione **Settings** no menu do seu perfil.
2. Na barra lateral esquerda, clique em **Developer settings** (geralmente no final).
3. Em **Personal access tokens**, clique em **Fine-grained tokens** (ou siga este [link direto](https://github.com/settings/personal-access-tokens)).
4. Clique em **Generate new token**.
5. Em "Token name", forneça um nome descritivo (ex.: `GenAI-Java-Course-Token`).
6. Defina uma data de expiração (recomendado: 7 dias para melhores práticas de segurança).
7. Em "Resource owner", selecione sua conta de usuário.
8. Em "Repository access", selecione os repositórios que você deseja usar com os Modelos do GitHub (ou "All repositories", se necessário).
9. Em "Repository permissions", encontre **Models** e defina como **Read and write**.
10. Clique em **Generate token**.
11. **Copie e salve seu token agora** – você não poderá vê-lo novamente!

> **Dica de Segurança**: Use o escopo mínimo necessário e o menor tempo de expiração prático para seus tokens de acesso.

## Passo 3: Teste Sua Configuração com o Exemplo dos Modelos do GitHub

Depois que seu ambiente de desenvolvimento estiver pronto, vamos testar a integração com os Modelos do GitHub usando nosso aplicativo de exemplo em [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Abra o terminal no seu ambiente de desenvolvimento.
2. Navegue até o exemplo dos Modelos do GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Defina seu token do GitHub como uma variável de ambiente:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Execute o aplicativo:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Você deverá ver uma saída semelhante a:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Entendendo o Código de Exemplo

Primeiro, vamos entender o que acabamos de executar. O exemplo em `examples/github-models` utiliza o SDK Java do OpenAI para conectar-se aos Modelos do GitHub:

**O que este código faz:**
- **Conecta-se** aos Modelos do GitHub usando seu token de acesso pessoal
- **Envia** uma mensagem simples "Say Hello World!" para o modelo de IA
- **Recebe** e exibe a resposta da IA
- **Valida** que sua configuração está funcionando corretamente

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

Ótimo! Agora você tem tudo configurado:

- Criou um Token de Acesso Pessoal no GitHub com as permissões corretas para acesso aos modelos de IA
- Configurou seu ambiente de desenvolvimento Java (seja Codespaces, contêineres ou local)
- Conectou-se aos Modelos do GitHub usando o SDK Java do OpenAI para desenvolvimento de IA gratuito
- Testou tudo com um exemplo simples que interage com modelos de IA

## Próximos Passos

[Capítulo 3: Técnicas Fundamentais de IA Generativa](../03-CoreGenerativeAITechniques/README.md)

## Solução de Problemas

Está enfrentando problemas? Aqui estão problemas comuns e soluções:

- **Token não funciona?** 
  - Certifique-se de copiar o token inteiro sem espaços extras
  - Verifique se o token foi definido corretamente como uma variável de ambiente
  - Confirme que seu token tem as permissões corretas (Models: Read and write)

- **Maven não encontrado?** 
  - Se estiver usando contêineres/Codespaces, o Maven deve estar pré-instalado
  - Para configuração local, certifique-se de que Java 21+ e Maven 3.9+ estão instalados
  - Tente `mvn --version` para verificar a instalação

- **Problemas de conexão?** 
  - Verifique sua conexão com a internet
  - Confirme que o GitHub está acessível na sua rede
  - Certifique-se de que não está atrás de um firewall bloqueando o endpoint dos Modelos do GitHub

- **Contêiner de desenvolvimento não inicia?** 
  - Certifique-se de que o Docker Desktop está em execução (para desenvolvimento local)
  - Tente reconstruir o contêiner: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Erros de compilação do aplicativo?**
  - Certifique-se de estar no diretório correto: `02-SetupDevEnvironment/examples/github-models`
  - Tente limpar e reconstruir: `mvn clean compile`

> **Precisa de ajuda?**: Ainda enfrentando problemas? Abra uma issue no repositório e nós ajudaremos você.

**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se a tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.