# Navodila za ustvarjanje zgodbe o hišnem ljubljenčku za začetnike

Naložite fotografijo hišnega ljubljenčka, jo analizirajte z GPT-5.6 Luna in ustvarite zgodbo na podlagi dobljenega opisa. Oba modela uporabljata `reasoning_effort: none`.

| Komponenta | Verzija |
| --- | --- |
| Java | 21 ali višja |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Vsebina

- [Pogoji](#pogoji)
- [Razumevanje strukture projekta](#razumevanje-strukture-projekta)
- [Pojasnilo osnovnih komponent](#pojasnilo-osnovnih-komponent)
  - [1. Glavna aplikacija](#1-glavna-aplikacija)
  - [2. Web kontroler](#2-web-kontroler)
  - [3. Storitvena zgodba](#3-storitvena-zgodba)
  - [4. Spletne predloge](#4-spletne-predloge)
  - [5. Konfiguracija](#5-konfiguracija)
- [Zagon aplikacije](#zagon-aplikacije)
- [Offline testi](#offline-testi)
- [Kako vse deluje skupaj](#kako-vse-deluje-skupaj)
- [Razumevanje AI integracije](#razumevanje-ai-integracije)
- [Nadaljnji koraki](#nadaljnji-koraki)

## Pogoji

Pred začetkom zagotovite, da imate:
- Java 21 ali višjo različico nameščeno
- Maven za upravljanje odvisnosti
- Azure AI Foundry namestitev GPT-5.6 Luna z imenom `gpt-5.6-luna` ali uporabo nastavka `AZURE_OPENAI_DEPLOYMENT`, ki kaže na to namestitev. Glejte [Poglavje 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) za namestitev in prijavo z `az login` za brezključni dostop. Namestitev mora podpirati vhodne slike in `reasoning_effort: none`.
- Osnovno razumevanje Jave, Spring Boota in spletnega razvoja

## Razumevanje strukture projekta

Projekt zgodbe o hišnem ljubljenčku vsebuje več pomembnih datotek:

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

## Pojasnilo osnovnih komponent

### 1. Glavna aplikacija

**Datoteka:** `PetStoryApplication.java`

To je začetna točka naše Spring Boot aplikacije:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Kaj to počne:**
- `@SpringBootApplication` anotacija omogoči samodejno konfiguracijo in iskanje komponent
- Zažene vgrajen spletni strežnik (Tomcat) na vratih 8080
- Samodejno ustvari vse potrebne Spring "bean"-e in storitve

### 2. Web kontroler

**Datoteka:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Končni naslov | Zahteva | Uspešen odgovor |
| --- | --- | --- |
| `GET /` | Brez telesa | HTML obrazec za nalaganje s CSRF žetonom |
| `POST /analyze-image` | `multipart/form-data`, polje datoteke `image` | JSON: `{"description":"Igriv hišni ljubljenček..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, polje `description` | HTML stran z rezultatom, opisom in ustvarjeno zgodbo |

Oba POST končna naslova zahtevata piškotek seje in CSRF žeton, pridobljena z `GET /`. Skripta nalaganja pošlje skrito `_csrf` vrednost v glavi `X-CSRF-TOKEN`; oddaja zgodbe jo pošlje kot polje `_csrf`. API odjemalci morajo ohraniti piškotek med zahtevami. To so končne točke za obrazce, ne pa JSON zahtev.

Opisi morajo biti ne-prazni in ne daljši od 1000 znakov. Kontroler odstrani odvečne presledke in odstrani `<`, `>`, dvojne narekovaje, apostrofe in `&` pred posredovanjem storitvi. Tudi predloga rezultatov izogne morebitnim varnostnim težavam z `th:text`.

Napake pri preverjanju slike vračajo HTTP 400 z poljem `error`; napake modela vračajo HTTP 502 z `error` in brez `description`. Neveljavni opisi zgodb ali napake modela preusmerijo na `/` z vidno napako. Manjkanje obveznih polj vrača HTTP 400, manjkajoči ali neveljavni CSRF žetoni pa HTTP 403. Rezervirani opisi ali zgodbe kot uspešni AI rezultati niso prikazani.

### 3. Storitvena zgodba

**Datoteka:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Uradni OpenAI Java SDK 4.63.1 kliče Azure AI Foundry OpenAI združljiv API za klepet. Azure Identity 1.18.6 zagotavlja Microsoft Entra žeton preko `DefaultAzureCredential`; API ključ ni potreben.

| Operacija | Vhod | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bajti slike kodirani kot base64 podatkovni URL z naloženim MIME tipom | 300 |
| `generateStory` | Opis hišnega ljubljenčka v uporabniškem sporočilu | 800 |

Obe zahtevi uporabljata nastavljeno namestitev, po privzetku `gpt-5.6-luna`, in izrecno nastavljeno `ReasoningEffort.NONE` (`reasoning_effort: none`). Niti ena zahteva ne pošilja `temperature` ali starega parametra `max_tokens`.

Analiza slike sprejema JPEG, PNG, GIF in WebP, zavrača prazne slike in datoteke nad 10MB ter omejuje rezultat opisa na 1000 znakov. Spodbuda za zgodbo zahteva družinsko prijazno kratko zgodbo. Prazne izbire ali prazna vsebina so napake, napake pa ohranijo izvirni vzrok za diagnostiko na strežniku. SDK odjemalec se zapre ob zaustavitvi aplikacije.

### 4. Spletne predloge

**Datoteka:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Obrazec za nalaganje)

Stran se začne s fotoselektorjem, ne z besedilnim področjem za opis. **Analiziraj sliko** prikaže izbrano fotografijo in jo pošlje na `/analyze-image`. Uspešen odgovor prikaže opis, izpolni skrito polje `description` in prikaže **Ustvari zgodbo**. Ta gumb pošlje obstoječi obrazec na `/generate-story`.

Ni prenosa modela v brskalnik ali CDN odvisnosti. Analiza slike poteka na strežniku preko nastavljene Azure namestitve. Napake ostanejo vidne in ne omogočajo ustvarjanja zgodbe z izmišljanim opisom. Izbira druge datoteke počisti prejšnjo analizo.

**Datoteka:** `result.html` (Prikaz zgodbe)

Prikaže ustvarjeno zgodbo:

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

**Značilnosti predloge:**

1. **Integracija Thymeleaf**: uporablja `th:` atribute za dinamično vsebino
2. **Prilagodljiv dizajn**: CSS slogovanje za mobilne naprave in namizje
3. **Obravnava napak**: prikazuje napake preverjanja uporabnikom
4. **Obravnava nalaganja**: JavaScript predogleda fotografijo, pošlje CSRF zavarovano multipart zahtevo in prikaže prejeti opis

### 5. Konfiguracija

**Datoteka:** `application.properties`

Nastavitve konfiguracije za aplikacijo:

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

**Pojasnilo konfiguracije:**

1. **Nalaganje datotek**: tako datoteka kot celotna multipart zahteva sta omejeni na 10MB; fotografije naj bodo pod to mejo, da ostane prostor za multipart glave
2. **Zapisovanje**: nadzoruje, katere informacije se beležijo med izvajanjem
3. **Azure AI Foundry**: določa končni naslov in model namestitve za uporabo (brezključna avtentikacija)
4. **Varnost**: CSRF zaščita ostaja omogočena; diagnostika modela se beleži na strežniku, medtem ko kontroler prikazuje generične napake modela

## Zagon aplikacije

### Korak 1: Prijava in nastavitev končnega naslova

Avtentikacija je brezklučna (Microsoft Entra ID), zato ni potrebnega API ključa. Prijavite se in nastavite vaš Foundry končni naslov:

**Windows (ukazna vrstica):**
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

**Zakaj je to potrebno:**
- Azure AI Foundry uporablja Microsoft Entra ID za avtentikacijo zahtev po sklepih
- Brezključna avtentikacija pomeni, da ni skrivnosti v vaši izvorni kodi ali okolju
- Vaš račun mora imeti vlogo **Cognitive Services OpenAI User** za ta vir

Privzeto ime namestitve je `gpt-5.6-luna`. Če vaša namestitev GPT-5.6 Luna nosi drugo ime, nastavite `AZURE_OPENAI_DEPLOYMENT` v isti terminal pred zagonom aplikacije. Tako analiza slike kot ustvarjanje zgodbe uporabljata to nastavitev.

### Korak 2: Build in zagon

Pojdite v projektno mapo:
```bash
cd 04-PracticalSamples/petstory
```

Sestavite samostojno JAR datoteko in zaženite vse offline teste:
```bash
mvn clean package
```

Zaženite strežnik:
```bash
mvn spring-boot:run
```

Aplikacija bo dostopna na `http://localhost:8080`.

Lahko tudi zaženete pakirano JAR datoteko na prostih vratih, na primer:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Za ta ukaz odprite `http://localhost:8083/`. Enaki poti `/analyze-image` in `/generate-story` so na voljo na izbranih vratih.

### Korak 3: Testiranje aplikacije

1. **Odprite** `http://localhost:8080` v brskalniku
2. **Izberite** jasno fotografijo hišnega ljubljenčka v formatu JPEG, PNG, GIF ali WebP, pod 10MB
3. **Kliknite** "Analyze Image" in počakajte na opis hišnega ljubljenčka
4. **Kliknite** "Generate Story" po uspešni analizi
5. **Ogledajte** zgodbo in uporabite povezavo na strani z rezultatom za vrnitev na obrazec za nalaganje

Uspešen potek od fotografije do zgodbe naredi dva modelska klica, enega na gumb. Živa izvedba porabi kvoto vaše namestitve in lahko povzroči stroške; pri skupni rabi omejene namestitve izvajajte teste zaporedno. Nalaganje domače strani ne kliče modela.

## Offline testi

Iz mape vzorcev zaženite:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) zajema resnične zahtevke OpenAI SDK z zanke HTTP s simulacijo. Preverja namestitev obeh zahtev, `reasoning_effort: none`, omejitve žetonov, podatke slike, validacijo vhodnih podatkov, prazne odzive in napake na strani zgornjega sistema.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) uporablja MockMvc z izmišljenim modelom storitve za testiranje prikazanih Thymeleaf strani, pogodbe nalaganja, CSRF, preverjanja, izogibanja izhodu in vidnih napak. Ti testi ne potrebujejo Azure poverilnic in nikoli ne kličejo plačljive Azure izvedbe. Maven piše Surefire poročila v `target/surefire-reports`.

## Kako vse deluje skupaj

Tukaj je celoten potek, ko ustvarite zgodbo o hišnem ljubljenčku:

1. **Izbor fotografije**: Izberete sliko hišnega ljubljenčka v obrazcu za nalaganje
2. **Nalaganje slike**: "Analyze Image" pošlje multipart POST na `/analyze-image` s CSRF glavo
3. **Analiza slike**: `StoryService` pošlje sliko GPT-5.6 Luna z nastavitvijo reasoning na `none`
4. **Prikaz opisa**: Brskalnik prikaže vrnjeni opis in shrani v obrazec
5. **Oddaja zgodbe**: "Generate Story" pošlje `description` in `_csrf` na `/generate-story`
6. **Ustvarjanje zgodbe**: Kontroler preveri opis in kliče isto namestitev z reasoning nastavljeno na `none`
7. **Prikaz predloge**: Thymeleaf izogne in prikaže opis in zgodbo na strani z rezultatom

**Potek obravnave napak:**
Če model ne uspe, strežnik zabeleži vzrok. Analiza slike vrne HTTP 502 in brskalnik prikaže napako brez razkritja gumba "Generate Story". Ustvarjanje zgodbe preusmeri na obrazec z obvestilom o napaki. Nobena pot ne nadomesti rezultata z vnaprej pripravljeno vsebino.

## Razumevanje AI integracije

### Azure AI Foundry (brezključni dostop)
Storitev konfigurira SDK z URL končne točke vašega vira `/openai/v1/`. `DefaultAzureCredential` in `AuthenticationUtil.getBearerTokenSupplier` zagotavljata Microsoft Entra žetone za `https://ai.azure.com/.default`. Lokalni razvoj lahko uporablja vašo prijavo v Azure CLI; aplikacija gostovana na Azure lahko uporabi upravljano identiteto z ustreznimi dovoljenji.

### Oblikovanje poziva
Analiza slike zahteva opazne lastnosti ljubljenčka v kratkem odstavku in pove modelu, naj besedilo na sliki obravnava kot podatke, ne kot navodila. Ustvarjanje zgodbe uporabi vrnjeni opis v ločeni, družinsko prijazni pisni zahtevi. Nobena zahteva ne omogoča reasoning ali nastavi temperaturo.

### Obdelava odziva
Skupni obdelovalec odziva zavrne manjkajoče izbire in prazno oziroma samo presledke vsebino, odstrani odvečen prostor in ohrani napake zgornjega sloja. Opisi slike so omejeni na 1000 znakov, da ustrezajo obrazcu zgodbe. Izvirna napaka modela se shrani za diagnostiko, vendar se ne prikaže uporabniku.

## Nadaljnji koraki

Za več primerov glejte [Poglavje 04: Praktični primeri](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba obravnavati kot avtoritativni vir. Za kritične informacije je priporočljiv strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->