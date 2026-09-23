# Tutorial MCP Calculator pentru Începători

## Cuprins

- [Ce Veți Învăța](#ce-veți-învăța)
- [Precondiții](#precondiții)
- [Versiuni Dependințe](#versiuni-dependințe)
- [Înțelegerea Structurii Proiectului](#înțelegerea-structurii-proiectului)
- [Componentele de Bază Explicate](#componentele-de-bază-explicate)
  - [1. Aplicația Principală](#1-aplicația-principală)
  - [2. Serviciul Calculatorului](#2-serviciul-calculatorului)
  - [3. Client MCP Direct](#3-client-mcp-direct)
  - [4. Client Alimentat de AI](#4-client-alimentat-de-ai)
- [Rularea Exemplului](#rularea-exemplului)
- [Teste Offline](#teste-offline)
- [Cum Funcționează Totul Împreună](#cum-funcționează-totul-împreună)
- [Pașii Următori](#pașii-următori)

## Ce Veți Învăța

Acest tutorial explică cum să construiți un serviciu de calculator folosind Protocolul Contextului Modelului (MCP). Veți înțelege:

- Cum să creați un serviciu pe care AI îl poate folosi ca un instrument
- Cum să configurați o comunicare directă cu serviciile MCP
- Cum modelele AI pot alege automat ce instrumente să folosească
- Diferența dintre apelurile directe de protocol și interacțiunile asistate de AI

## Precondiții

Înainte de a începe, asigurați-vă că aveți:
- Java 21 sau o versiune superioară instalată
- Maven pentru gestionarea dependințelor
- Cunoștințe de bază despre Java și Spring Boot

Doar clienții AI necesită o implementare Azure OpenAI și un `DefaultAzureCredential` autentificat,
cum ar fi un login existent Azure CLI local sau o identitate gestionată în Azure. Identitatea necesită
rolul Cognitive Services OpenAI User pe resursă. Consultați [Capitolul 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Serverul, clientul direct SDK și toate testele automate nu au nevoie de un cont Azure sau acces la model.

## Versiuni Dependințe

Dependințe verificate la lansare pe 2026-09-14:

| Dependență | Versiune |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (gestionat de Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j adaptor oficial OpenAI | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (gestionat de Boot) | 6.0.3 |

Adaptatorii MCP și OpenAI oficiali sunt versiuni beta publicate în Maven Central, nu snapshot-uri.
Versiunile lor diferă de LangChain4j core. Nu sunt necesare depozite de tip snapshot sau milestone.
Dependențele doar pentru client au scop de test deoarece exemplele executabile se află în `src/test/java`.

## Înțelegerea Structurii Proiectului

Proiectul calculator are mai multe fișiere importante:

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

## Componentele de Bază Explicate

### 1. Aplicația Principală

**Fișier:** `McpServerApplication.java`

Acesta este punctul de intrare al serviciului nostru de calculator. Este o aplicație standard Spring Boot cu o adăugare specială:

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

**Ce face asta:**
- Pornește un server web Spring Boot pe portul 8080
- Creează un `ToolCallbackProvider` care face metodele calculatorului disponibile ca instrumente MCP
- Anotarea `@Bean` spune Spring să gestioneze acest element ca un component pe care alte părți îl pot folosi

### 2. Serviciul Calculatorului

**Fișier:** `CalculatorService.java`

Aici are loc toată matematica. Fiecare metodă este marcată cu `@Tool` pentru a fi disponibilă prin MCP:

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
    
    // Mai multe operații calculator...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Caracteristici cheie:**

1. **Anotarea `@Tool`**: Aceasta spune MCP că metoda poate fi apelată de clienți externi
2. **Descrieri Clare**: Fiecare instrument are o descriere care ajută modelele AI să înțeleagă când să-l folosească
3. **Format Consistent de Returnare**: Toate operațiile returnează șiruri lizibile de oameni precum "5.00 + 3.00 = 8.00"
4. **Gestionarea Erorilor**: Împărțirea la zero și rădăcinile pătrate negative returnează mesaje de eroare

**Operații Disponibile:**
- `add(a, b)` - Adună două numere
- `subtract(a, b)` - Scade al doilea număr din primul
- `multiply(a, b)` - Înmulțește două numere
- `divide(a, b)` - Împarte primul la al doilea (cu verificare zero)
- `power(base, exponent)` - Ridică baza la puterea exponentului
- `squareRoot(number)` - Calculează rădăcina pătrată (cu verificare negativă)
- `modulus(a, b)` - Returnează restul împărțirii
- `absolute(number)` - Returnează valoarea absolută
- `help()` - Returnează informații despre toate operațiile

### 3. Client MCP Direct

Vezi [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Acest client folosește `HttpClientStreamableHttpTransport` la `/mcp`, inițializează conexiunea,
trimite un ping serverului și urmărește paginarea listei de instrumente. Verifică că toate cele nouă instrumente așteptate
există și apelează fiecare, inclusiv `modulus` și `help`, fără un model AI.

Constructorul actual de cereri arată astfel:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Erorile de protocol determină eșuarea clientului în loc de afișarea unui succes înșelător. Clientul MCP
este închis cu try-with-resources, inclusiv când descoperirea sau un apel la instrument eșuează.

### 4. Client Alimentat de AI

Vezi [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
și [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementează API-ul curent LangChain4j `ChatModel`.
`StreamableHttpMcpTransport` îl conectează la același punct final `/mcp` ca și clientul SDK.
`AiServices` descoperă instrumentele și gestionează conversația apel-resultat a instrumentelor.

Implementarea implicită este **GPT-5.6 Luna**, cu raționamentul dezactivat explicit:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Aceste valori implicite se aplică fiecărei completări, inclusiv urmăriri după execuția instrumentului.
Clientul folosește un `BearerTokenCredential` reîmprospătabil susținut de `DefaultAzureCredential`
și domeniul `https://ai.azure.com/.default`, nu un token unic transmis ca cheie API.
Se acceptă atât URL-urile resurselor, cât și URL-urile care se termină deja în `/openai/v1`.

Botul păstrează o istorie limitată a conversației, afișează `Tool executed: ...` cu rezultatul
efectiv MCP și eșuează dacă un răspuns ocolește instrumentele. Bucla instrumentelor este limitată la patru ture.
Erorile de autentificare, model, MCP și instrument sunt propagate; retry-urile automate ale modelului sunt dezactivate.
Atât transportul/clientul MCP, cât și clientul oficial OpenAI sunt închise la succes sau eșec.

## Rularea Exemplului

### Pasul 1: Pornește Serverul Calculator

Nu este necesară configurarea Azure pentru server. Comenzile de mai jos se rulează din directorul acestui exemplu.
Exemplul folosește portul **18081** pentru a evita conflictele cu alt exemplu; implicit rămâne 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Punctul final MCP este `http://localhost:18081/mcp`. Informații despre sănătate și descoperire sunt la
`http://localhost:18081/health` și `http://localhost:18081/info`.
Streamable HTTP înlocuiește vechiul transport exclusiv SSE; `/sse` și `/v1/tools` nu sunt endpoint-uri.

### Pasul 2: Testare cu Client Direct

Într-un alt terminal PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Nu este necesară nicio intrare. Toate cele nouă instrumente sunt testate. Rezultatele aritmetice așteptate includ
8, 6, 42, 5, 256, 4, 2 și 5.5, urmate de textul de ajutor.

### Pasul 3: Testare cu Client AI

După autentificare conform celor din precondiții, configurați clientul AI în același terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Așteptați o linie `Tool executed: add` cu `41.80`, urmată de răspunsul modelului.
Modul cu un singur prompt iese fără să aștepte input. Pentru a rula demo-ul inițial cu patru prompturi:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo-ul apelează `add`, `squareRoot`, `help` și operația în lanț `power` apoi `divide`.
Răspunsurile numerice așteptate sunt 41.8, 12 și 64. Omiterea argumentelor execută și acest demo.

### Pasul 4: Rulați Botul Interactiv

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Introduceți `Multiply 6 by 7 using the calculator service`, apoi `exit` sau `quit`.
Așteptați un rezultat real `multiply` de 42. Liniile goale sunt ignorate; EOF oprește și sesiunea.
Pentru un test simplu neinteractiv al acestui punct de intrare:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Ambele puncte de intrare AI acceptă `--prompt "question"`, `--demo` și `--interactive`.
Opțiunile invalide eșuează înainte de deschiderea conexiunii. Fiecare argument Maven `-D...` este complet între ghilimele
pentru PowerShell. În Bash, folosiți `export NAME=value` în loc de `$env:NAME = "value"`.

**Cota:** Rulați exemplele AI secvențial. Un prompt simplu necesită de obicei două cereri către model;
demo-ul complet necesită de obicei nouă, inclusiv urmăriri după rezultatul instrumentului. Cu o implementare 10 RPM
partajată, așteptați o fereastră de cotă proaspătă înainte de următoarea rulare AI. Un 429 eșuează vizibil fără
retry-uri automate; urmați indicațiile serviciului de retry-after. Numărul real de cereri depinde de model.
Testele offline nu consumă cotă și nu stabilesc disponibilitatea sau calitatea răspunsurilor Luna live.

### Configurare și Închidere

| Setare | Implicit / comportament |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL de bază, fără `/mcp` |
| `-Dmcp.server.url=...` | Suprascrie `MCP_SERVER_URL` pentru toți clienții |
| `AZURE_OPENAI_ENDPOINT` | Necesitar doar pentru clienții AI; URL-ul resursei sau URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; numele unei implementări Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; număr întreg pozitiv |
| Efort de raționare | Întotdeauna `none`, inclusiv urmăriri de bucle instrument |

O implementare suprascrisă trebuie să suporte `reasoning_effort=none` și `max_completion_tokens`.
Clienții nu citesc automat un fișier `.env`. Opriți serverul cu `Ctrl+C` după testare.
Clienții se termină normal fără `System.exit` sau pauze de închidere.

## Teste Offline

```powershell
mvn -B -ntp clean verify
```

Toate testele sunt offline în raport cu Azure: suita de protocol pornește un server Spring și
un stub compatibil OpenAI pe porturi loopback aleatoare, apoi le închide. Maven poate avea totuși nevoie
să descarce dependențe. Nu sunt folosite credențiale, implementări live sau server MCP preexistent.

- Testele unitare ale calculatorului acoperă toate operațiile aritmetice, rezultatele zecimale, ajutorul și erorile de domeniu.
- Testele MCP acoperă inițializarea, descoperirea, toate cele nouă apeluri de instrument, eșecurile instrumentelor și sănătate/info.
- Testele protocolului AI execută demo-ul complet și Bot-ul interactiv împotriva calculatorului real,
  verifică că rezultatele instrumentelor alimentează următoarea completare și inspectează fiecare corp HTTP pentru Luna,
  `reasoning_effort: "none"` și `max_completion_tokens` fără `max_tokens` moștenit.
- Testele de configurare/input acoperă suprascrierile de implementare și endpoint, liniile goale, EOF, exit/quit,
  modul cu un singur prompt, opțiunile invalide și propagarea erorilor. Testele de cotă dovedesc că 429 nu este reîncercat.

## Cum Funcționează Totul Împreună

Iată fluxul complet când întrebați AI "Cât face 5 + 3?":

1. **Tu** îi pui întrebarea AI în limbaj natural
2. **AI** analizează cererea și își dă seama că vrei adunare
3. **AI** apelează serverul MCP: `add(5.0, 3.0)`
4. **Serviciul Calculator** efectuează: `5.0 + 3.0 = 8.0`
5. **Serviciul Calculator** returnează: `"5.00 + 3.00 = 8.00"`
6. **AI** primește rezultatul și formulează un răspuns natural
7. **Tu** primești: "Suma lui 5 și 3 este 8"

## Pașii Următori

Pentru mai multe exemple, vezi [Capitolul 04: Exemple practice](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->