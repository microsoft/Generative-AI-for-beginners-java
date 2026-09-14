# Návod na generovanie príbehu o zvieratku pre začiatočníkov

Nahrajte fotografiu zvieratka, analyzujte ju pomocou GPT-5.6 Luna a vygenerujte príbeh z výsledného popisu. Obe požiadavky na model používajú `reasoning_effort: none`.

| Komponent | Verzia |
| --- | --- |
| Java | 21 alebo vyššia |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Obsah

- [Predpoklady](#predpoklady)
- [Pochopenie štruktúry projektu](#pochopenie-štruktúry-projektu)
- [Vysvetlenie hlavných komponentov](#vysvetlenie-hlavných-komponentov)
  - [1. Hlavná aplikácia](#1-hlavná-aplikácia)
  - [2. Webový kontrolér](#2-webový-kontrolér)
  - [3. Služba príbehu](#3-služba-príbehu)
  - [4. Webové šablóny](#4-webové-šablóny)
  - [5. Konfigurácia](#5-konfigurácia)
- [Spustenie aplikácie](#spustenie-aplikácie)
- [Offline testy](#offline-testy)
- [Ako to všetko spolu funguje](#ako-to-všetko-spolu-funguje)
- [Pochopenie AI integrácie](#pochopenie-ai-integrácie)
- [Ďalšie kroky](#ďalšie-kroky)

## Predpoklady

Pred začatím sa uistite, že máte:
- Nainštalovanú Javu 21 alebo vyššiu
- Maven pre správu závislostí
- Nasadenie Azure AI Foundry GPT-5.6 Luna nazvané `gpt-5.6-luna`, alebo prepísanie `AZURE_OPENAI_DEPLOYMENT` ukazujúce na toto nasadenie. Pozrite si [Kapitolu 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) pre vytvorenie a prihlásenie cez `az login` pre autentifikáciu bez kľúčov. Nasadenie musí podporovať vstup obrazu a `reasoning_effort: none`.
- Základné znalosti Javy, Spring Bootu a webového vývoja

## Pochopenie štruktúry projektu

Projekt príbehu o zvieratku obsahuje niekoľko dôležitých súborov:

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

## Vysvetlenie hlavných komponentov

### 1. Hlavná aplikácia

**Súbor:** `PetStoryApplication.java`

Toto je vstupný bod našej Spring Boot aplikácie:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Čo to robí:**
- Anotácia `@SpringBootApplication` umožňuje automatickú konfiguráciu a vyhľadávanie komponentov
- Spúšťa zabudovaný webový server (Tomcat) na porte 8080
- Automaticky vytvára všetky potrebné Spring beany a služby

### 2. Webový kontrolér

**Súbor:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Požiadavka | Úspešná odpoveď |
| --- | --- | --- |
| `GET /` | Bez tela | HTML formulár na nahranie s CSRF tokenom |
| `POST /analyze-image` | `multipart/form-data`, pole súboru `image` | JSON: `{"description":"Hravo zvieratko..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, pole `description` | HTML výsledková stránka s popisom a vygenerovaným príbehom |

Obidve POST požiadavky vyžadujú session cookie a CSRF token získané z `GET /`. Nahrávací skript posiela skrytú hodnotu `_csrf` v hlavičke `X-CSRF-TOKEN`; odoslanie príbehu ju posiela ako formulárové pole `_csrf`. API klienti musia zachovať cookie medzi požiadavkami. Toto sú formulárové endpointy, nie JSON požiadavky.

Popisy musia byť neprázdne a nesmú presiahnuť 1000 znakov. Kontrolér upraví popis odstránením medzier a vyčistí `<`, `>`, dvojité úvodzovky, apostrofy a `&` pred poslaním službe. Výsledná šablóna tiež escapuje výstup modelu s `th:text`.

Neúspešná validácia obrázkov vráti HTTP 400 s poľom `error`; zlyhanie modelu vráti HTTP 502 s poľom `error` a bez `description`. Nesprávne popisy príbehu alebo zlyhania modelu presmerujú na `/` s viditeľnou chybou. Chýbajúce povinné polia vracajú HTTP 400, chýbajúce alebo neplatné CSRF tokeny vracajú HTTP 403. Neexistujú žiadne náhradné popisy alebo príbehy ako úspešné AI výsledky.

### 3. Služba príbehu

**Súbor:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Oficiálny OpenAI Java SDK 4.63.1 volá Azure AI Foundry kompatibilné API Chat Completions. Azure Identity 1.18.6 poskytuje Microsoft Entra bearer token cez `DefaultAzureCredential`; API kľúč nie je potrebný.

| Operácia | Vstup | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bajty obrázka zakódované ako base64 data URL s nahratým MIME typom | 300 |
| `generateStory` | Popis zvieratka v užívateľskej správe | 800 |

Obe požiadavky používajú nakonfigurované nasadenie, predvolene `gpt-5.6-luna`, a explicitne nastavujú `ReasoningEffort.NONE` (`reasoning_effort: none`). Žiadna požiadavka neposiela `temperature` ani starší parameter `max_tokens`.

Analýza obrázku akceptuje JPEG, PNG, GIF a WebP, odmieta prázdne obrázky a súbory nad 10MB, a limituje výsledný popis na 1000 znakov. Prompt na príbeh žiada krátky rodinne priateľský príbeh. Prázdne voľby alebo prázdny obsah modelu sú chyby a zlyhania si uchovávajú pôvodnú príčinu pre diagnostiku na strane servera. SDK klient je zatvorený pri vypnutí aplikácie.

### 4. Webové šablóny

**Súbor:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formulár na nahrávanie)

Stránka začína výberom fotky, nie textovým poľom na popis. **Analyze Image** zobrazí náhľad vybranej fotky a odošle ju na `/analyze-image`. Úspešná odpoveď zobrazí popis, vyplní skryté pole `description` a odhalí tlačidlo **Generate Story**. Toto tlačidlo odošle aktuálny formulár na `/generate-story`.

Neexistuje žiadne sťahovanie modelu v prehliadači alebo CDN závislosti. Analýza obrázkov prebieha na serveri cez konfigurované Azure nasadenie. Zlyhania zostávajú viditeľné a neumožňujú generovanie príbehu s vyfabrikovaným popisom. Výber iného súboru vymaže predchádzajúcu analýzu.

**Súbor:** `result.html` (Zobrazenie príbehu)

Zobrazuje vygenerovaný príbeh:

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

**Funkcie šablóny:**

1. **Integrácia Thymeleaf**: Používa atribúty `th:` pre dynamický obsah
2. **Responzívny dizajn**: CSS štýly pre mobil a desktop
3. **Spracovanie chýb**: Zobrazuje validačné chyby používateľom
4. **Spracovanie nahrávania**: JavaScript zobrazuje náhľad fotky, posiela multipart požiadavku chránenú CSRF a zobrazuje vrátený popis

### 5. Konfigurácia

**Súbor:** `application.properties`

Nastavenia konfigurácie aplikácie:

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

**Vysvetlenie konfigurácie:**

1. **Nahrávanie súborov**: Súbor aj celý multipart požiadavok majú limit 10MB; udržiavajte fotografie pod týmto limitom, aby zostal priestor na hlavičky multipart
2. **Logovanie**: Riadi, aké informácie sa logujú počas vykonávania
3. **Azure AI Foundry**: Špecifikuje endpoint a nasadenie modelu na použitie (autentifikácia bez kľúča)
4. **Bezpečnosť**: CSRF ochrana zostáva zapnutá; diagnostika modelu sa loguje na serveri, zatiaľ čo kontrolér zobrazuje všeobecné chybové hlásenia modelu

## Spustenie aplikácie

### Krok 1: Prihlásenie a nastavenie endpointu

Autentifikácia je bez kľúča (Microsoft Entra ID), takže nie je potrebný API kľúč. Prihláste sa a nastavte svoj Foundry endpoint:

**Windows (Príkazový riadok):**
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

**Prečo je to potrebné:**
- Azure AI Foundry používa Microsoft Entra ID na autentifikáciu inferenčných požiadaviek
- Autentifikácia bez kľúča znamená žiadne tajomstvá v zdrojovom kóde alebo prostredí
- Váš účet potrebuje rolu **Cognitive Services OpenAI User** na danom zdroji

Predvolené meno nasadenia je `gpt-5.6-luna`. Ak máte iný názov nasadenia GPT-5.6 Luna, nastavte `AZURE_OPENAI_DEPLOYMENT` v tom istom termináli pred spustením aplikácie. Analýza obrázkov i generovanie príbehov používajú toto nastavenie.

### Krok 2: Build a spustenie

Prejdite do adresára projektu:
```bash
cd 04-PracticalSamples/petstory
```

Vytvorte samostatný spustiteľný JAR a spustite všetky offline testy:
```bash
mvn clean package
```

Spustite server:
```bash
mvn spring-boot:run
```

Aplikácia začne na `http://localhost:8080`.

Alternatívne spustite zabalený JAR na voľnom porte, napríklad:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Pri tomto príkaze otvorte `http://localhost:8083/`. Tie isté trasy `/analyze-image` a `/generate-story` sú dostupné na zvolenom porte.

### Krok 3: Otestovanie aplikácie

1. **Otvorte** `http://localhost:8080` vo svojom prehliadači
2. **Vyberte** jasnú fotografiu zvieratka vo formáte JPEG, PNG, GIF alebo WebP, pod 10MB
3. **Kliknite** na "Analyze Image" a počkajte na popis zvieratka
4. **Kliknite** na "Generate Story" po úspešnej analýze
5. **Zobrazte** príbeh a použite odkaz na výsledkovej stránke pre návrat na nahrávací formulár

Úspešný tok foto-príbeh vyvolá dva modelové volania, jedno na každý tlačidlo. Živé inferencie spotrebúvajú vašu kvótu nasadenia a môžu spôsobiť poplatky; spúšťajte testy postupne, ak zdieľate limitované nasadenie. Načítanie domovskej stránky nevyvoláva model.

## Offline testy

V adresári sample spustite:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) zachytáva reálne OpenAI SDK požiadavky so spätnou HTTP slučkou. Kontroluje nasadenie oboch požiadaviek, `reasoning_effort: none`, limity tokenov, obrazový payload, validáciu vstupu, prázdne odpovede a upstream chyby.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) používa MockMvc s mockovanou službou modelu na testovanie vykreslených Thymeleaf stránok, kontraktu nahrávania, CSRF, validácie, escapovania výstupu a viditeľných zlyhaní. Tieto testy nepotrebujú Azure poverenia a nikdy nevolajú platené inferencie Azure. Maven zapisuje Surefire reporty do `target/surefire-reports`.

## Ako to všetko spolu funguje

Tu je kompletný tok pri generovaní príbehu o zvieratku:

1. **Výber fotografie**: Vyberiete obrázok zvieratka vo formulári na nahrávanie
2. **Nahratie obrázka**: "Analyze Image" posiela multipart POST na `/analyze-image` s CSRF hlavičkou
3. **Analýza obrázka**: `StoryService` odošle obrázok GPT-5.6 Luna so zadaným reasoning na `none`
4. **Zobrazenie popisu**: Prehliadač zobrazí vrátený popis a uloží ho do formulára
5. **Odoslanie príbehu**: "Generate Story" odošle `description` a `_csrf` na `/generate-story`
6. **Generovanie príbehu**: Kontrolér validuje popis a volá rovnaké nasadenie so zadaným reasoning `none`
7. **Vykreslenie šablóny**: Thymeleaf escapuje a zobrazuje popis a príbeh na výslednej stránke

**Spracovanie chýb:**
Ak model zlyhá, server zaznamená príčinu do logu. Analýza obrázka vráti HTTP 502 a prehliadač zobrazí chybu bez zobrazenia "Generate Story". Generovanie príbehu presmeruje na formulár s chybovým hlásením. Žiadna cesta ticho nenahradí výsledok predpísaným textom.

## Pochopenie AI integrácie

### Azure AI Foundry (bez kľúča)
Služba konfiguruje SDK s `/openai/v1/` endpointom vášho zdroja. `DefaultAzureCredential` a `AuthenticationUtil.getBearerTokenSupplier` poskytujú Microsoft Entra tokeny pre `https://ai.azure.com/.default`. Lokálny vývoj môže použiť vaše Azure CLI prihlásenie; Azure hosťovaná aplikácia môže použiť spravovanú identitu s potrebnými povoleniami zdroja.

### Inžinierstvo promptu
Analýza obrázka vyžaduje pozorovateľné vlastnosti zvieratka v krátkom odseku a hovorí modelu, aby text na obrázku považoval za dáta, nie za pokyny. Generovanie príbehu používa vrátený popis v samostatnej, rodinne priateľskej požiadavke na písanie. Žiadne volanie nezapína reasoning ani nenastavuje teplotu.

### Spracovanie odpovede
Spoločný spracovateľ odpovede odmieta chýbajúce voľby a prázdny alebo len prázdny obsah, oreže platný obsah a zachováva upstream zlyhania. Popisy obrázkov sú obmedzené na 1000 znakov, aby sa zmestili do ďalšieho formulára príbehu. Pôvodné zlyhanie modelu zostáva pre diagnostiku, ale nie je zobrazené užívateľovi.

## Ďalšie kroky

Pre viac príkladov pozrite [Kapitolu 04: Praktické ukážky](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->