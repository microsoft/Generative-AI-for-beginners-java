# MCP Kalkulaatori juhend algajatele

## Sisukord

- [Mida te õpite](#mida-te-õpite)
- [Eeltingimused](#eeltingimused)
- [Sõltuvuste versioonid](#sõltuvuste-versioonid)
- [Projekti struktuuri mõistmine](#projekti-struktuuri-mõistmine)
- [Põhikomponentide selgitus](#põhikomponentide-selgitus)
  - [1. Peamine rakendus](#1-peamine-rakendus)
  - [2. Kalkulaatori teenus](#2-kalkulaatori-teenus)
  - [3. Otsene MCP klient](#3-otsene-mcp-klient)
  - [4. Tehisintellektil põhinev klient](#4-tehisintellektil-põhinev-klient)
- [Näidete käivitamine](#näidete-käivitamine)
- [Võrguühenduseta testid](#võrguühenduseta-testid)
- [Kuidas see kõik kokku töötab](#kuidas-see-kõik-kokku-töötab)
- [Järgmised sammud](#järgmised-sammud)

## Mida te õpite

See juhend selgitab, kuidas luua kalkulaatori teenus, kasutades Mudeli konteksti protokolli (MCP). Te mõistate:

- Kuidas luua teenus, mida tehisintellekt saab tööriistana kasutada
- Kuidas seadistada otsene suhtlus MCP teenustega
- Kuidas tehisintellekti mudelid saavad automaatselt valida, milliseid tööriistu kasutada
- Erinevust otseste protokolli kutsede ja tehisintellekti abil tehtavate interaktsioonide vahel

## Eeltingimused

Enne alustamist veenduge, et teil on:
- Paigaldatud Java 21 või uuem versioon
- Maven sõltuvuste haldamiseks
- Põhilised teadmised Java ja Spring Booti kohta

Ainult AI kliendid vajavad Azure OpenAI juurutust ja autentitud `DefaultAzureCredential`i,
näiteks olemasolevat Azure CLI sisselogimist lokaalselt või hallatavat identiteeti Azures. Identiteedil on vaja
Cognitive Services OpenAI kasutaja rolli ressursil. Vaata [2. peatükk](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Server, otsene SDK klient ja kõik automatiseeritud testid ei vaja Azure kontot ega mudeli ligipääsu.

## Sõltuvuste versioonid

Väljalaske sõltuvused kontrolliti 2026-09-14:

| Sõltuvus | Versioon |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI haldab) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j ametlik OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-haldus) | 6.0.3 |

MCP ja ametlikud OpenAI adapterid on avaldatud beta väljaanded Maven Centralis, mitte snapshotid.
Nende versioonid erinevad LangChain4j core'ist. Snapshot- või milestone-repositsioone ei ole vaja.
Ainult kliendipoolsed sõltuvused on testide ulatuses, kuna käivitatavad näited asuvad kataloogis `src/test/java`.

## Projekti struktuuri mõistmine

Kalkulaatori projektis on mitmeid olulisi faile:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Põhikomponentide selgitus

### 1. Peamine rakendus

**Fail:** `McpServerApplication.java`

See on meie kalkulaatori teenuse sisenemispunkt. See on standardne Spring Boot rakendus ühe erilise lisandiga:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Mida see teeb:**
- Käivitab Spring Boot veebiserveri pordil 8080
- Loob `ToolCallbackProvider`i, mis teeb meie kalkulaatori meetodid MCP tööriistadena kättesaadavaks
- `@Bean` annotatsioon ütleb Springile, et see on komponent, mida teised osad saavad kasutada

### 2. Kalkulaatori teenus

**Fail:** `CalculatorService.java`

Siin toimub kogu matemaatika. Iga meetod on märgitud `@Tool`-ga, et see oleks MCP kaudu kättesaadav:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Rohkem kalkulaatori toiminguid...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Põhifunktsioonid:**

1. **`@Tool` annotatsioon**: See ütleb MCP-le, et seda meetodit saavad kutsuda välised kliendid
2. **Selged kirjeldused**: Igal tööriistal on kirjeldus, mis aitab AI mudelitel mõista, millal seda kasutada
3. **Ühtne tagastuse formaat**: Kõik operatsioonid tagastavad inimesele loetavaid stringe nagu "5.00 + 3.00 = 8.00"
4. **Veeavigade käitlemine**: Nulliga jagamine ja negatiivsed ruutjuured tagastavad veateateid

**Saadaval olevad operatsioonid:**
- `add(a, b)` - Liidab kaks arvu
- `subtract(a, b)` - Lahutab teise esimesest
- `multiply(a, b)` - Korrutab kaks arvu
- `divide(a, b)` - Jagab esimese teisega (nulli kontrolliga)
- `power(base, exponent)` - Tõstab aluse astendajani
- `squareRoot(number)` - Arvutab ruutjuure (negatiivse kontrolliga)
- `modulus(a, b)` - Tagastab jagamise jäägi
- `absolute(number)` - Tagastab absoluutväärtuse
- `help()` - Tagastab info kõigi operatsioonide kohta

### 3. Otsene MCP klient

Vaata [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

See klient kasutab `HttpClientStreamableHttpTransport` aadressil `/mcp`, algatab ühenduse,
pingi serverile ja järgib tööriistade nimekirja lehekülgede kaupa laadimist. Kontrollib, et kõiki üheksat oodatud tööriista
on olemas ja kutsub igaüht neist, sealhulgas `modulus` ja `help`, ilma AI mudelita.

Praegune päringu koostaja näeb välja selline:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokollivead põhjustavad kliendi nurjumise, selle asemel et kuvada eksitav edu. MCP klient
suletakse koos katkestusega, sealhulgas avastamise või tööriista kutsest ebaõnnestumisel.

### 4. Tehisintellektil põhinev klient

Vaata [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
ja [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` rakendab kehtivat LangChain4j `ChatModel` API-t.
`StreamableHttpMcpTransport` ühendab selle samale `/mcp` lõpp-punktile nagu SDK klient.
`AiServices` avastab tööriistad ja haldab tööriista-kutse/tulemuse vestlust.

Vaikimisi juurutus on **GPT-5.6 Luna**, mille puhul mõtlemine on otseselt keelatud:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Need vaikeseaded kehtivad iga lõpetamise kohta, sealhulgas tööriista täitmiste järgselt.
Klient kasutab värskendatavat `BearerTokenCredential`i, mille tugiks on `DefaultAzureCredential`
ja ulatus `https://ai.azure.com/.default`, mitte ühekordset märgendit API võtmena.
Ressursi URL-id ja URL-id, mis juba lõpevad `/openai/v1`, on mõlemad aktsepteeritavad.

Bot hoiab piiratud vestlusajaloo, prindib `Tool executed: ...` koos tegeliku
MCP tulemusega ning nurjub, kui vastus jätab tööriistad välja. Tööriistasilmused on piiratud nelja ringkäiguga.
Autentimis-, mudeli-, MCP- ja tööriistavigad levivad; automaatsed mudeli uuesti katsed on keelatud.
Nii MCP transpordiklient kui ametlik OpenAI klient suletakse eduka või ebaeduka tulemuse korral.

## Näidete käivitamine

### Samm 1: Käivita kalkulaatori server

Serveri jaoks ei ole vajalik mingit Azure konfiguratsiooni. Alljärgnevad käsud käivitatakse selle näidise kataloogist.
Näide kasutab porti **18081**, et vältida konflikte teise näitega; vaikimisi jääb port 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP lõpp-punkt on `http://localhost:18081/mcp`. Tervise ja avastusteave on aadressidel
`http://localhost:18081/health` ja `http://localhost:18081/info`.
Streamable HTTP asendab vana ainult SSE transpordimehhanismi; `/sse` ja `/v1/tools` ei ole lõpp-punktid.

### Samm 2: Testi otsese kliendiga

Teises PowerShell terminalis:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Sisendit pole vaja. Kõik üheksa tööriista on testitud. Oodatavad aritmeetilised tulemused hõlmavad
8, 6, 42, 5, 256, 4, 2 ja 5.5, millele järgneb abitekst.

### Samm 3: Testi AI kliendiga

Pärast autentimist nagu eeltingimustes kirjeldatud, seadista AI klient samas terminalis:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Oota rida `Tool executed: add` väärtusega `41.80`, millele järgneb mudeli vastus.
Ühe sisendi režiim väljub ilma sisendit oodamata. Originaalse nelja sisendi demo käivitamiseks:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo kutsub `add`, `squareRoot`, `help` ning ahelas `power` ja `divide` toiminguid.
Oodatavad numbrilised vastused on 41.8, 12 ja 64. Argumentide jätmine vahele käivitab samuti selle demo.

### Samm 4: Käivita interaktiivne bott

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Sisesta `Multiply 6 by 7 using the calculator service`, seejärel `exit` või `quit`.
Oota tegelikku `multiply` tööriista tulemust väärtusega 42. Tühjad read ignoreeritakse; ka EOF lõpetab sessiooni.
Mitteinteraktiivseks tõrketestiks selle sisenemispunkti jaoks:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Mõlemad AI sisenemispunktid aktsepteerivad `--prompt "question"`, `--demo` ja `--interactive` parameetreid.
Vigased valikud põhjustavad ebaõnnestumise enne ühenduse loomist. Iga Maven-i `-D...` argument tuleb PowerShellis täielikult ümbritseda jutumärkidesse.
Bashis kasuta `export NAME=value` asemel `$env:NAME = "value"`.

**Kvoot:** Käivita AI näited järjestikku. Lihtsa palve puhul on tavaliselt kaks mudelipäringut;
täielik demo vajab tavaliselt üheksa, sealhulgas tööriistatulemuste järelpäringud. Jagatud 10 RPM juurutusega
lase kvoodi värske akna avaneda enne järgmise AI jooksu algust. 429 tõrked ebaõnnestuvad nähtavalt ilma
automaatsete kordusteta; järgi teenuse retry-after juhiseid. Tegelikud päringute arvud sõltuvad mudelist.
Võrguühenduseta testid ei kasuta kvooti ega määra Luna saadavust või vastuste kvaliteeti.

### Konfiguratsioon ja seiskamine

| Seade | Vaikimisi / käitumine |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; baasaadress, ilma `/mcp`-osata |
| `-Dmcp.server.url=...` | Üle kirjutab `MCP_SERVER_URL` kõikide klientide jaoks |
| `AZURE_OPENAI_ENDPOINT` | Vajalik ainult AI klientidele; ressursi URL või `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure juurutuse nimi |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positiivne täisarv |
| Mõtlemisjõupingutus | Alati `none`, sh tööriistasilmuste järelpäringud |

Üle kirjutatud juurutus peab toetama `reasoning_effort=none` ja `max_completion_tokens`.
Kliendid ei loe `.env` faili automaatselt. Peata server testi lõpus `Ctrl+C`-ga.
Kliendid naasevad normaalselt ilma `System.exit` või sulgemise viivitusteta.

## Võrguühenduseta testid

```powershell
mvn -B -ntp clean verify
```

Kõik testid toimuvad võrguühenduseta Azure suhtes: protokollikomplekt alustab Spring serverit ja
OpenAI-sobivat varianti suvalistel loopback portidel, seejärel sulgeb need. Maven võib siiski vajada
sõltuvuste allalaadimist. Mitte mingit autentimist, elavat juurutust ega olemasolevat MCP serverit ei kasutata.

- Kalkulaatori üksustestid katavad kõik aritmeetilised tehteid, kümnendvastuseid, abi ja domeenivead.
- MCP testid katavad initsialiseerimise, avastuse, üheksa tööriista kutsed, tööriistavigad ja tervise/info kontrolli.
- AI protokolli testid täidavad täieliku demo ja interaktiivse botti päris kalkulaatori vastu,
  kontrollivad, et tööriistatulemused jõuavad järgmise täitmiseni ning uurivad iga Luna,
  `reasoning_effort: "none"` ja `max_completion_tokens` HTTP-päringu sisu ilma vananenud `max_tokens` viisata.
- Konfiguratsiooni/sisendi testid hõlmavad juurutuse ja lõpp-punkti üle kirjutamist, tühje ridu, EOF, väljapääsu/quit,
  ühe sisendiga režiimi, vigaseid valikuid ja vigade levitamist. Kvooti testid tõestavad, et 429 ei kordu.

## Kuidas see kõik kokku töötab

Siin on täielik voog, kui te tehisintellektile küsida "Mis on 5 + 3?":

1. **Teie** küsitlete AI-d loomulikus keeles
2. **AI** analüüsib teie päringut ja mõistab, et soovite liitmist
3. **AI** kutsub MCP serverit: `add(5.0, 3.0)`
4. **Kalkulaatori teenus** teeb: `5.0 + 3.0 = 8.0`
5. **Kalkulaatori teenus** tagastab: `"5.00 + 3.00 = 8.00"`
6. **AI** saab tulemuse ja vormindab loomuliku vastuse
7. **Teie** saate vastuseks: "Arvude 5 ja 3 summa on 8"

## Järgmised sammud

Täiendavate näidete jaoks vaata [Peatükk 04: Praktilised näited](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades AI tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun pange tähele, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega seotud eksimustest või valesti mõistmistest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->