# Tutorial do Gerador de Histórias para Animais de Estimação para Iniciantes

Carregue uma foto de um animal de estimação, analise-a com o GPT-5.6 Luna e gere uma história a partir da descrição resultante. Ambos os pedidos ao modelo usam `reasoning_effort: none`.

| Componente | Versão |
| --- | --- |
| Java | 21 ou superior |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Compreender a Estrutura do Projeto](#compreender-a-estrutura-do-projeto)
- [Componentes Principais Explicados](#componentes-principais-explicados)
  - [1. Aplicação Principal](#1-aplicação-principal)
  - [2. Controlador Web](#2-controlador-web)
  - [3. Serviço de História](#3-serviço-de-história)
  - [4. Modelos Web](#4-modelos-web)
  - [5. Configuração](#5-configuração)
- [Executar a Aplicação](#executar-a-aplicação)
- [Testes Offline](#testes-offline)
- [Como Tudo Funciona em Conjunto](#como-tudo-funciona-em-conjunto)
- [Compreender a Integração de IA](#compreender-a-integração-de-ia)
- [Próximos Passos](#próximos-passos)

## Pré-requisitos

Antes de começar, certifique-se de que tem:
- Java 21 ou superior instalado
- Maven para gestão de dependências
- Uma implementação Azure AI Foundry do GPT-5.6 Luna chamada `gpt-5.6-luna`, ou uma substituição `AZURE_OPENAI_DEPLOYMENT` que aponte para essa implementação. Veja [Capítulo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) para provisionar e inicie sessão com `az login` para autenticação sem chave. A implementação deve suportar entrada de imagem e `reasoning_effort: none`.
- Compreensão básica de Java, Spring Boot e desenvolvimento web

## Compreender a Estrutura do Projeto

O projeto da história para animais de estimação tem vários ficheiros importantes:

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

## Componentes Principais Explicados

### 1. Aplicação Principal

**Ficheiro:** `PetStoryApplication.java`

Este é o ponto de entrada da nossa aplicação Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**O que isto faz:**
- A anotação `@SpringBootApplication` ativa a configuração automática e a deteção de componentes
- Inicia um servidor web embutido (Tomcat) na porta 8080
- Cria automaticamente todos os beans e serviços Spring necessários

### 2. Controlador Web

**Ficheiro:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Pedido | Resposta bem-sucedida |
| --- | --- | --- |
| `GET /` | Sem corpo | Formulário HTML de carregamento com token CSRF |
| `POST /analyze-image` | `multipart/form-data`, campo de ficheiro `image` | JSON: `{"description":"Um animal brincalhão..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, campo `description` | Página HTML com o resultado, mostrando a descrição e a história gerada |

Ambos os endpoints POST requerem o cookie de sessão e o token CSRF obtidos a partir do `GET /`. O script de carregamento envia o valor oculto `_csrf` no cabeçalho `X-CSRF-TOKEN`; a submissão da história envia-o como o campo de formulário `_csrf`. Clientes de API devem preservar o cookie entre pedidos. Estes são endpoints de formulário, não endpoints de pedidos JSON.

As descrições devem não estar vazias e ter no máximo 1000 caracteres. O controlador remove espaços em excesso da descrição e elimina `<`, `>`, aspas duplas, apóstrofos e `&` antes de passá-la ao serviço. O modelo do resultado também escapa a saída do modelo com `th:text`.

Falhas na validação da imagem retornam HTTP 400 com o campo `error`; falhas do modelo retornam HTTP 502 com o campo `error` e sem `description`. Descrições de histórias inválidas ou falhas do modelo redirecionam para `/` com um erro visível. Campos obrigatórios em falta retornam HTTP 400, e tokens CSRF em falta ou inválidos retornam HTTP 403. Não são apresentadas descrições ou histórias alternativas como resultados de IA bem-sucedidos.

### 3. Serviço de História

**Ficheiro:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

O SDK oficial OpenAI Java 4.63.1 chama a API de Chat Completions compatível com OpenAI da Azure AI Foundry. O Azure Identity 1.18.6 fornece um token Microsoft Entra via `DefaultAzureCredential`; não é necessária nenhuma chave API.

| Operação | Entrada | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bytes da imagem codificados como uma URL de dados base64 com o tipo MIME carregado | 300 |
| `generateStory` | Uma descrição do animal numa mensagem do utilizador | 800 |

Ambos os pedidos usam a implementação configurada, por padrão `gpt-5.6-luna`, e definem explicitamente `ReasoningEffort.NONE` (`reasoning_effort: none`). Nenhum pedido envia `temperature` ou o parâmetro legado `max_tokens`.

A análise de imagens aceita JPEG, PNG, GIF e WebP, rejeita imagens vazias e ficheiros superiores a 10MB, e limita a descrição resultante a 1000 caracteres. O prompt da história pede uma narrativa curta e familiar. Escolhas vazias ou conteúdo do modelo em branco são erros, e falhas mantêm a causa original para diagnósticos no servidor. O cliente SDK é fechado quando a aplicação é encerrada.

### 4. Modelos Web

**Ficheiro:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formulário de Carregamento)

A página começa com um seletor de fotos, não uma caixa de texto para descrição. **Analisar Imagem** pré-visualiza a foto selecionada e envia-a para `/analyze-image`. Uma resposta bem-sucedida mostra a descrição, preenche o campo oculto `description` e revela **Gerar História**. Esse botão submete o formulário existente para `/generate-story`.

Não existe download de modelo no browser nem dependência de CDN. A análise de imagem corre no servidor através da implementação Azure configurada. Falhas permanecem visíveis e não permitem geração de história com descrição fabricada. Selecionar um ficheiro diferente limpa a análise anterior.

**Ficheiro:** `result.html` (Exibição da História)

Mostra a história gerada:

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

**Funcionalidades do modelo:**

1. **Integração Thymeleaf**: Usa atributos `th:` para conteúdo dinâmico
2. **Design Responsivo**: Estilos CSS para dispositivos móveis e desktops
3. **Gestão de Erros**: Mostra erros de validação aos utilizadores
4. **Gestão de Carregamento**: JavaScript pré-visualiza a foto, envia um pedido multipart protegido por CSRF e exibe a descrição retornada

### 5. Configuração

**Ficheiro:** `application.properties`

Configurações da aplicação:

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

**Configuração explicada:**

1. **Carregamento de Ficheiros**: Tanto o ficheiro como o pedido multipart completo são limitados a 10MB; mantenha as fotos abaixo desse limite para levar em conta os cabeçalhos multipart
2. **Logging**: Controla que informações são registadas durante a execução
3. **Azure AI Foundry**: Especifica o endpoint e a implementação do modelo a usar (autenticação sem chave)
4. **Segurança**: A proteção CSRF permanece ativa; diagnósticos do modelo são registados no servidor, enquanto o controlador mostra mensagens genéricas de falha de modelo

## Executar a Aplicação

### Passo 1: Iniciar Sessão e Definir o Endpoint

A autenticação é sem chave (Microsoft Entra ID), por isso não existe chave API. Inicie sessão e defina o seu endpoint Foundry:

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

**Por que isto é necessário:**
- Azure AI Foundry usa Microsoft Entra ID para autenticar pedidos de inferência
- Autenticação sem chave significa sem segredos no seu código-fonte ou ambiente
- A sua conta precisa da função **Cognitive Services OpenAI User** no recurso

O nome da implementação por defeito é `gpt-5.6-luna`. Se a sua implementação GPT-5.6 Luna tiver outro nome, defina `AZURE_OPENAI_DEPLOYMENT` no mesmo terminal antes de iniciar a aplicação. Tanto a análise de imagem como a geração de histórias usam esta configuração.

### Passo 2: Construir e Executar

Navegue para o diretório do projeto:
```bash
cd 04-PracticalSamples/petstory
```

Construa o JAR executável independente e execute todos os testes offline:
```bash
mvn clean package
```

Inicie o servidor:
```bash
mvn spring-boot:run
```

A aplicação começará em `http://localhost:8080`.

Alternativamente, inicie o JAR empacotado numa porta livre, por exemplo:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Com esse comando, aceda a `http://localhost:8083/`. As mesmas rotas `/analyze-image` e `/generate-story` estão disponíveis na porta selecionada.

### Passo 3: Testar a Aplicação

1. **Abra** `http://localhost:8080` no seu browser
2. **Selecione** uma foto clara de um animal em formato JPEG, PNG, GIF ou WebP, com menos de 10MB
3. **Clique** em "Analisar Imagem" e aguarde a descrição do animal
4. **Clique** em "Gerar História" após análise bem-sucedida
5. **Veja** a história e use o link na página de resultado para regressar ao formulário de carregamento

O fluxo foto-para-história bem-sucedido faz duas chamadas ao modelo, uma por botão. A inferência em tempo real consome a quota da sua implementação e pode acarretar custos; execute testes básicos em série ao partilhar uma implementação com limitação de taxa. Carregar a página principal não chama o modelo.

## Testes Offline

A partir do diretório sample, execute:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) captura pedidos reais do SDK OpenAI com um fixture HTTP loopback. Verifica a implementação, `reasoning_effort: none`, limites de tokens, payload da imagem, validação de entrada, respostas vazias e erros upstream em ambos os pedidos.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) usa MockMvc com um serviço modelo simulado para testar as páginas Thymeleaf renderizadas, o contrato de upload, CSRF, validação, escapamento de saída e falhas visíveis. Estes testes não precisam de credenciais Azure e nunca chamam a inferência Azure paga. O Maven escreve relatórios Surefire em `target/surefire-reports`.

## Como Tudo Funciona em Conjunto

Aqui está o fluxo completo quando gera uma história para animal de estimação:

1. **Seleção da Foto**: Escolhe uma imagem do animal de estimação no formulário de carregamento
2. **Carregamento da Imagem**: "Analisar Imagem" envia um POST multipart para `/analyze-image` com o cabeçalho CSRF
3. **Análise da Imagem**: `StoryService` envia a imagem para o GPT-5.6 Luna com o raciocínio definido para `none`
4. **Exibição da Descrição**: O browser mostra a descrição retornada e armazena-a no formulário
5. **Submissão da História**: "Gerar História" publica `description` e `_csrf` para `/generate-story`
6. **Geração da História**: O controlador valida a descrição e chama a mesma implementação com raciocínio definido para `none`
7. **Renderização do Modelo**: Thymeleaf escapa e mostra a descrição e a história na página de resultado

**Fluxo de Tratamento de Erros:**
Se o modelo falhar, o servidor regista a causa. A análise de imagem retorna HTTP 502 e o browser mostra o erro sem revelar "Gerar História". A geração da história redireciona para o formulário com uma mensagem de erro. Nenhum dos caminhos substitui silenciosamente por um resultado pré-escrito.

## Compreender a Integração de IA

### Azure AI Foundry (sem chave)
O serviço configura o SDK com o endpoint `/openai/v1/` do seu recurso. `DefaultAzureCredential` e `AuthenticationUtil.getBearerTokenSupplier` fornecem tokens Microsoft Entra para `https://ai.azure.com/.default`. O desenvolvimento local pode usar a sua sessão do Azure CLI; uma aplicação hospedada no Azure pode usar uma identidade gerida com permissões necessárias no recurso.

### Engenharia do Prompt
A análise de imagem pede características observáveis do animal num parágrafo curto e indica ao modelo para tratar o texto na imagem como dados, não como instruções. A geração da história usa a descrição retornada numa requisição de escrita separada e familiar. Nenhuma chamada permite raciocínio nem define um override de temperatura.

### Processamento da Resposta
O manipulador de respostas partilhado rejeita escolhas em falta e conteúdos vazios ou só com espaços, remove espaços do conteúdo válido e preserva falhas upstream. As descrições das imagens são limitadas a 1000 caracteres para caber no formulário subsequente da história. A falha original do modelo é mantida para diagnósticos, mas não é mostrada ao utilizador.

## Próximos Passos

Para mais exemplos, veja [Capítulo 04: Exemplos práticos](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->