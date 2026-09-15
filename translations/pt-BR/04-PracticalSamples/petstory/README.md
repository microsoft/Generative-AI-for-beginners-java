# Tutorial de Gerador de Histórias de Pets para Iniciantes

Envie uma foto de um pet, analise-a com o GPT-5.6 Luna e gere uma história a partir da descrição resultante. Ambas as solicitações ao modelo usam `reasoning_effort: none`.

| Componente | Versão |
| --- | --- |
| Java | 21 ou superior |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Entendendo a Estrutura do Projeto](#entendendo-a-estrutura-do-projeto)
- [Explicação dos Componentes Principais](#explicação-dos-componentes-principais)
  - [1. Aplicação Principal](#1-aplicação-principal)
  - [2. Controlador Web](#2-controlador-web)
  - [3. Serviço de Histórias](#3-serviço-de-histórias)
  - [4. Templates Web](#4-templates-web)
  - [5. Configuração](#5-configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Testes Offline](#testes-offline)
- [Como Tudo Funciona Junto](#como-tudo-funciona-junto)
- [Entendendo a Integração com IA](#entendendo-a-integração-com-ia)
- [Próximos Passos](#próximos-passos)

## Pré-requisitos

Antes de começar, certifique-se de que você tem:
- Java 21 ou superior instalado
- Maven para gerenciamento de dependências
- Uma implantação Azure AI Foundry do GPT-5.6 Luna chamada `gpt-5.6-luna`, ou uma substituição de `AZURE_OPENAI_DEPLOYMENT` apontando para essa implantação. Veja [Capítulo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) para provisionamento e faça login com `az login` para autenticação sem chave. A implantação deve suportar entrada de imagem e `reasoning_effort: none`.
- Conhecimento básico de Java, Spring Boot e desenvolvimento web

## Entendendo a Estrutura do Projeto

O projeto da história do pet possui vários arquivos importantes:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Explicação dos Componentes Principais

### 1. Aplicação Principal

**Arquivo:** `PetStoryApplication.java`

Este é o ponto de entrada para nossa aplicação Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**O que isso faz:**
- A anotação `@SpringBootApplication` habilita auto-configuração e escaneamento de componentes
- Inicia um servidor web embutido (Tomcat) na porta 8080
- Cria automaticamente todos os beans e serviços Spring necessários

### 2. Controlador Web

**Arquivo:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Requisição | Resposta de sucesso |
| --- | --- | --- |
| `GET /` | Sem corpo | Formulário HTML de upload com token CSRF |
| `POST /analyze-image` | `multipart/form-data`, campo de arquivo `image` | JSON: `{"description":"Um pet brincalhão..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, campo `description` | Página HTML de resultado com a descrição e a história gerada |

Ambos os endpoints POST requerem o cookie de sessão e o token CSRF obtidos do `GET /`. O script de upload envia o valor oculto `_csrf` no cabeçalho `X-CSRF-TOKEN`; o envio da história o envia como campo de formulário `_csrf`. Clientes API devem preservar o cookie entre as requisições. Estes são endpoints para formulários, não endpoints de requisições JSON.

As descrições devem ser não vazias e não ultrapassar 1000 caracteres. O controlador faz o trim da descrição e remove `<`, `>`, aspas duplas, apóstrofos e `&` antes de passar para o serviço. O template do resultado também escapa a saída do modelo com `th:text`.

Falhas na validação da imagem retornam HTTP 400 com um campo `error`; falhas do modelo retornam HTTP 502 com campo `error` e sem `description`. Descrições inválidas de histórias ou falhas do modelo redirecionam para `/` com uma mensagem de erro visível. Campos obrigatórios ausentes retornam HTTP 400, e tokens CSRF ausentes ou inválidos retornam HTTP 403. Nenhuma descrição ou história de fallback são apresentadas como resultados bem-sucedidos da IA.

### 3. Serviço de Histórias

**Arquivo:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

O SDK oficial OpenAI Java 4.63.1 chama a API de Completions de Chat compatível com OpenAI do Azure AI Foundry. Azure Identity 1.18.6 fornece um token bearer Microsoft Entra através do `DefaultAzureCredential`; nenhuma chave de API é necessária.

| Operação | Entrada | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bytes da imagem codificados como URL de dados base64 com o tipo MIME enviado | 300 |
| `generateStory` | Uma descrição do pet na mensagem do usuário | 800 |

Ambas as requisições usam a implantação configurada, padrão para `gpt-5.6-luna`, e explicitamente definem `ReasoningEffort.NONE` (`reasoning_effort: none`). Nenhuma requisição envia `temperature` ou o parâmetro legado `max_tokens`.

A análise de imagem aceita JPEG, PNG, GIF e WebP, rejeita imagens vazias e arquivos acima de 10MB, e limita a descrição resultante a 1000 caracteres. O prompt da história pede uma narrativa breve e familiar. Escolhas vazias ou conteúdo em branco do modelo são erros, e falhas preservam a causa original para diagnóstico do servidor. O cliente SDK é fechado quando a aplicação é finalizada.

### 4. Templates Web

**Arquivo:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formulário de Upload)

A página começa com um seletor de foto, não uma área de texto para descrição. **Analisar Imagem** pré-visualiza a foto selecionada e a envia para `/analyze-image`. Uma resposta bem-sucedida exibe a descrição, preenche o campo oculto `description` e revela **Gerar História**. Esse botão envia o formulário existente para `/generate-story`.

Não há download do modelo no navegador nem dependência de CDN. A análise de imagem é executada no servidor através da implantação Azure configurada. Falhas permanecem visíveis e não habilitam a geração de história com uma descrição fabricada. Selecionar um arquivo diferente limpa a análise anterior.

**Arquivo:** `result.html` (Exibição da História)

Exibe a história gerada:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Características do template:**

1. **Integração com Thymeleaf**: Usa atributos `th:` para conteúdo dinâmico
2. **Design Responsivo**: Estilos CSS para mobile e desktop
3. **Tratamento de Erros**: Exibe erros de validação aos usuários
4. **Manipulação de Upload**: JavaScript faz preview da foto, envia requisição multipart protegida por CSRF e exibe a descrição retornada

### 5. Configuração

**Arquivo:** `application.properties`

Configurações para a aplicação:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Explicação da configuração:**

1. **Upload de Arquivo**: Tanto o arquivo quanto a requisição multipart completa são limitados a 10MB; mantenha as fotos abaixo desse limite para deixar espaço para os cabeçalhos multipart
2. **Logging**: Controla quais informações são registradas durante a execução
3. **Azure AI Foundry**: Especifica o endpoint e a implantação do modelo a serem usados (autenticação sem chave)
4. **Segurança**: Proteção CSRF permanece ativa; diagnósticos do modelo são registrados no servidor, enquanto o controlador exibe mensagens genéricas de falha do modelo

## Executando a Aplicação

### Passo 1: Faça Login e Defina seu Endpoint

A autenticação é sem chave (Microsoft Entra ID), portanto não há chave de API. Faça login e defina seu endpoint Foundry:

**Windows (Prompt de Comando):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Por que isso é necessário:**
- O Azure AI Foundry usa o Microsoft Entra ID para autenticar requisições de inferência
- A autenticação sem chave significa que não há segredos no código-fonte ou ambiente
- Sua conta precisa do papel **Cognitive Services OpenAI User** no recurso

O nome padrão da implantação é `gpt-5.6-luna`. Se sua implantação GPT-5.6 Luna tiver outro nome, defina `AZURE_OPENAI_DEPLOYMENT` no mesmo terminal antes de iniciar a aplicação. Tanto a análise de imagem quanto a geração de histórias usam essa configuração.

### Passo 2: Compile e Execute

Navegue até o diretório do projeto:
```bash
cd 04-PracticalSamples/petstory
```

Compile o JAR executável standalone e rode todos os testes offline:
```bash
mvn clean package
```

Inicie o servidor:
```bash
mvn spring-boot:run
```

A aplicação iniciará em `http://localhost:8080`.

Alternativamente, inicie o JAR empacotado em uma porta livre, por exemplo:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Para esse comando, abra `http://localhost:8083/`. As mesmas rotas `/analyze-image` e `/generate-story` estão disponíveis na porta selecionada.

### Passo 3: Teste a Aplicação

1. **Abra** `http://localhost:8080` no seu navegador
2. **Selecione** uma foto clara de um pet nos formatos JPEG, PNG, GIF ou WebP, abaixo de 10MB
3. **Clique** em "Analisar Imagem" e aguarde a descrição do pet
4. **Clique** em "Gerar História" após a análise bem-sucedida
5. **Veja** a história e use o link da página de resultado para voltar ao formulário de upload

O fluxo bem-sucedido de foto para história faz duas chamadas ao modelo, uma para cada botão. A inferência ao vivo consome a cota da sua implantação e pode gerar custos; rode testes rápidos em série quando compartilhar uma implantação com limite de taxa. Carregar a página inicial não chama o modelo.

## Testes Offline

A partir do diretório sample, execute:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) captura requisições reais do SDK OpenAI com um fixture HTTP de loopback. Verifica a implantação, `reasoning_effort: none`, limites de tokens, payload de imagem, validação de entrada, respostas vazias e erros upstream em ambas as requisições.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) usa MockMvc com um serviço modelo mockado para testar as páginas em Thymeleaf renderizadas, contrato de upload, CSRF, validação, escape de saída e falhas visíveis. Estes testes não precisam de credenciais Azure e nunca chamam a inferência Azure paga. O Maven escreve relatórios Surefire em `target/surefire-reports`.

## Como Tudo Funciona Junto

Aqui está o fluxo completo quando você gera uma história de pet:

1. **Seleção da Foto**: Você escolhe uma imagem do pet no formulário de upload
2. **Upload da Imagem**: "Analisar Imagem" envia um POST multipart para `/analyze-image` com o cabeçalho CSRF
3. **Análise da Imagem**: `StoryService` envia a imagem para o GPT-5.6 Luna com o raciocínio definido para `none`
4. **Exibição da Descrição**: O navegador exibe a descrição retornada e a armazena no formulário
5. **Envio da História**: "Gerar História" posta `description` e `_csrf` para `/generate-story`
6. **Geração da História**: O controlador valida a descrição e chama a mesma implantação com raciocínio definido para `none`
7. **Renderização do Template**: Thymeleaf escapa e exibe a descrição e a história na página de resultado

**Fluxo de Tratamento de Erros:**
Se o modelo falhar, o servidor registra a causa. A análise de imagem retorna HTTP 502 e o navegador mostra o erro sem revelar "Gerar História". A geração da história redireciona para o formulário com uma mensagem de erro. Nenhum dos caminhos substitui silenciosamente por um resultado pré-escrito.

## Entendendo a Integração com IA

### Azure AI Foundry (sem chave)
O serviço configura o SDK com o endpoint `/openai/v1/` do seu recurso. `DefaultAzureCredential` e `AuthenticationUtil.getBearerTokenSupplier` fornecem tokens Microsoft Entra para `https://ai.azure.com/.default`. O desenvolvimento local pode usar seu login Azure CLI; um app hospedado no Azure pode usar uma identidade gerenciada com as permissões necessárias no recurso.

### Engenharia de Prompt
A análise de imagem solicita características observáveis do pet em um parágrafo curto e instrui o modelo a tratar o texto na imagem como dados, não como instruções. A geração de histórias usa a descrição retornada em uma requisição de escrita separada e familiar. Nenhuma chamada habilita raciocínio ou define uma sobreposição de temperatura.

### Processamento da Resposta
O manipulador de resposta compartilhado rejeita escolhas ausentes e conteúdo vazio ou apenas com espaços, faz trim do conteúdo válido e preserva falhas upstream. As descrições de imagem são limitadas a 1000 caracteres para caber no formulário de história subsequente. A falha original do modelo é retida para diagnóstico, mas não exibida ao usuário.

## Próximos Passos

Para mais exemplos, veja [Capítulo 04: Exemplos Práticos](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->