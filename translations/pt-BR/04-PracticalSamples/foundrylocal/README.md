# Tutorial Local Foundry Spring Boot

Execute um pequeno modelo de linguagem na sua própria máquina e chame seu endpoint REST compatível com OpenAI
a partir de uma aplicação console Java. Nenhum deployment na Azure, login na Azure,
chave API da nuvem ou inferência em nuvem são utilizados. **GPT-5.6 Luna é somente para Azure; não
configure-o como um modelo Foundry Local.**

## Versões e pré-requisitos

| Componente | Versão |
| --- | --- |
| Java | 21 ou superior |
| Maven | 3.6.3 ou superior |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (servidor REST local) | 2.0.1 |
| Node.js (servidor REST local) | 20 ou superior |
| Foundry Local CLI (opcional, release separada) | 0.10.3 prévia |

O Spring Boot gerencia o Spring Framework, Jackson, JUnit e versões do plugin Maven.
Este exemplo usa o OpenAI Java SDK diretamente, não Spring AI. A antiga propriedade milestone
e repositório do Spring AI, que não são usados, foram removidos.

O modelo inicial recomendado é **Qwen 2.5 0.5B**, variante CPU
`qwen2.5-0.5b-instruct-generic-cpu:4` (aproximadamente 822 MB no catálogo).
Evita a necessidade de provedores de execução GPU. Outros pequenos modelos suportados e em cache
podem ser selecionados explicitamente. A instalação do modelo e do runtime requer acesso à rede;
prompts e inferências permanecem locais. O Foundry Local pode ainda emitir diagnósticos mínimos de runtime
mesmo com a telemetria desabilitada.

Rode os seguintes comandos a partir deste diretório de exemplo.

## Construir e testar Java

```powershell
mvn clean verify
```

Os testes de contrato HTTP iniciam um servidor loopback efêmero e exercitam o OpenAI Java SDK real.
Eles cobrem a serialização de requisição, descoberta de modelo, seleção explícita de modelo,
listas ambíguas ou mal formadas de modelos, falhas HTTP, respostas em branco,
URLs locais somente, e propagação de falhas via linha de comando. Não precisam de modelo ou
acesso à rede além da instalação de dependências Maven. O teste ao vivo é opt-in.

## Iniciar o modelo local

### Recomendado: servidor SDK fixo

Não há SDK Java nativo Foundry Local. O pequeno help Node.js hospeda o
servidor REST oficial do SDK; o aplicativo e requisição de chat permanecem Java.

Instale as dependências fixas do runtime:

```powershell
npm ci
```

Se o Windows x64 não conseguir acessar o NuGet durante a instalação nativa do SDK, use o fallback fornecido.
Ele baixa o arquivo arquivado oficial do runtime no GitHub compatível, checa o
resumo SHA-256 do release, e posiciona suas DLLs ao lado do addon nativo. Ele não
desativa validação TLS, não exige privilégio elevado, nem modifica a fonte do SDK.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Liste modelos já em cache nessa máquina:

```powershell
npm run start:foundry -- --list
```

Na primeira execução, permita explicitamente o download do pequeno modelo CPU:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Nas execuções seguintes, omita `--download` para exigir modelo em cache:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

O helper prefere um modelo em cache equivalente, aceita um alias ou ID exato da variante,
e recusa modelo ausente a menos que `--download` seja fornecido. Ele registra somente o
provedor de execução do modelo selecionado quando um é requerido. Variantes GPU em cache ainda podem
precisar de pacotes e drivers compatíveis do provedor de execução.

Se a porta 5273 estiver ocupada, passe `--port 0` para porta disponível. O helper imprime
`FOUNDRY_LOCAL_BASE_URL`, o ID exato `FOUNDRY_LOCAL_MODEL` e seu PID quando estiver pronto.
Use o endpoint impresso no Java. Deixe este terminal aberto enquanto executar Java;
**Ctrl+C** para o servidor REST e libera o modelo.

O cache padrão é `~/.foundry/cache/models`. Configure `FOUNDRY_LOCAL_CACHE_DIR` para
outro cache existente. Logs e estado do helper são escritos sob o diretório `target/foundry-local`
deste exemplo. Pare o helper antes de rodar `mvn clean`.

### Opcional: Foundry Local CLI

A CLI e o SDK têm releases independentes: CLI **0.10.3** inclui SDK **1.2.4**;
o helper acima usa SDK **2.0.1**. Instalar a última CLI não instala a
última SDK para linguagem. Veja as [notas de release da CLI](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

No Windows, use o comando de instalação por usuário se a CLI estiver ausente:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Ou atualize uma instalação existente:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

A CLI 0.10.x substitui antigos comandos `foundry service` por `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` necessita de modelo já baixado. Consulte `foundry model --help` para
comandos de download. Use o endpoint real da saída de status; caso contrário, a CLI
usa uma porta atribuída automaticamente. Não inicie a CLI e o helper SDK
na mesma porta. Quando terminar:

```powershell
foundry server stop
```

## Execute a aplicação Java

Em um segundo terminal, configure o endpoint e ID exato do modelo impressos pelo seu servidor:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Ou execute o aplicativo empacotado:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

O ponto único de entrada Java é `com.example.Application`. Ele imprime o endpoint selecionado,
ID real do modelo, prompt e resposta gerada, então fecha seu contexto Spring
e cliente HTTP. Falha na inferência ou ausência de texto na resposta resulta em
saída de falha ao invés de placeholder de sucesso.

### Configuração

| Variável ambiente | Padrão | Propósito |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Endpoint HTTP loopback, incluindo `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Vazio | ID exato do modelo; caso contrário seleciona o único modelo anunciado |
| `FOUNDRY_LOCAL_PROMPT` | Uma pergunta em uma frase sobre modelos locais | Prompt enviado pelo console runner |

Argumentos Spring equivalentes são `--foundry.local.base-url=...`,
`--foundry.local.model=...`, e `--foundry.local.prompt=...`.
Somente endpoints HTTP de loopback são aceitos. Endpoints remotos/na nuvem,
credenciais embutidas, query strings e caminhos sem `/v1` são rejeitados.

Configuração de modelo vazia funciona somente quando `/v1/models` anuncia exatamente um modelo.
Modelo anunciado não necessariamente está carregado. Se múltiplos modelos são anunciados,
configure o ID exato carregado ao invés de confiar na ordenação do catálogo.

Requisições usam `temperature=0`, limite de 150 tokens de saída, timeout de 120 segundos e
sem tentativas automáticas. O campo `max_tokens` na requisição é intencional: é
suportado pelo contrato REST Foundry Local, embora o OpenAI Java o deprecie
para modelos mais recentes na nuvem. A identidade do modelo vem da configuração ou
descoberta, não das reivindicações do próprio modelo.

## Validação ao vivo

Com o servidor local rodando, execute todos os testes incluindo o teste ao vivo opt-in.
Substitua a porta do endpoint pelo valor impresso pelo seu servidor. Coloque entre aspas propriedades
Maven pontuadas no PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

O teste ao vivo chama `Application.main`, fornece o fato "A capital da
França é Paris," pergunta pela cidade, e confirma que o texto gerado é
`Paris`. Ele verifica um resultado semântico, não apenas status HTTP de sucesso.

Este é um cheque de integração, não um benchmark de acurácia. Durante a validação, este
modelo 0.5B respondeu a prompt "2 + 2" separado com `3` via Java e REST direto.
Não confie nele para precisão aritmética ou factual sem verificação independente;
use ferramentas determinísticas para cálculos.

## Solução de problemas

| Sintoma | Verifique |
| --- | --- |
| Conexão recusada | Aguarde a mensagem de pronto; use a porta impressa e caminho `/v1`. |
| Múltiplos modelos anunciados | Configure `FOUNDRY_LOCAL_MODEL` para ID exato do modelo carregado. |
| Modelo ausente | Use `--list`, ou permita explicitamente download com `--download`. |
| Provedor GPU falha ou trava | Use o modelo CPU pequeno. Um modelo GPU em cache ainda precisa do provedor. |
| CLI permanece em `initializing` | Leia `foundry server logs --lines 80`; pare o daemon e use o helper SDK. |
| Falha TLS/download NuGet | Corrija acesso à rede ou use fallback Windows x64 verificado acima. Não desative TLS. |
| Porta ocupada | Use `--port 0` e configure Java com endpoint impresso. |
| Sem escolhas ou texto em branco | O app falha propositadamente; inspecione logs do modelo e runtime. |

## Código fonte e referências

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): executor Spring Boot one-shot.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): descoberta tipada e chats locais.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): contrato HTTP, runner e testes ao vivo.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): servidor REST SDK oficial com seleção e limpeza de modelo em cache.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): fallback nativo Windows x64 verificado.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), e [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): configuração e dependências.
- [Integração REST Foundry Local](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Release e notas de migração Foundry Local 2.0.1](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Capítulo 04: exemplos práticos](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->