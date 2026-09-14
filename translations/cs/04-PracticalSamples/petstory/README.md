# Tutoriál generátoru příběhů o mazlíčcích pro začátečníky

Nahrajte fotografii mazlíčka, analyzujte ji pomocí GPT-5.6 Luna a na základě vzniklého popisu vygenerujte příběh. Oba modelové požadavky používají `reasoning_effort: none`.

| Komponenta | Verze |
| --- | --- |
| Java | 21 nebo vyšší |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Obsah

- [Požadavky](#požadavky)
- [Pochopení struktury projektu](#pochopení-struktury-projektu)
- [Vysvětlení hlavních komponent](#vysvětlení-hlavních-komponent)
  - [1. Hlavní aplikace](#1-hlavní-aplikace)
  - [2. Webový kontroler](#2-webový-kontroler)
  - [3. Služba pro příběh](#3-služba-pro-příběh)
  - [4. Webové šablony](#4-webové-šablony)
  - [5. Konfigurace](#5-konfigurace)
- [Spuštění aplikace](#spuštění-aplikace)
- [Offline testy](#offline-testy)
- [Jak vše funguje dohromady](#jak-vše-funguje-dohromady)
- [Pochopení integrace AI](#pochopení-integrace-ai)
- [Další kroky](#další-kroky)

## Požadavky

Než začnete, ujistěte se, že máte:
- Java 21 nebo vyšší nainstalovanou
- Maven pro správu závislostí
- Azure AI Foundry nasazení GPT-5.6 Luna pojmenované `gpt-5.6-luna`, nebo překrytí `AZURE_OPENAI_DEPLOYMENT` směrující na toto nasazení. Viz [Kapitola 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) pro provisioning a přihlášení pomocí `az login` pro autentizaci bez klíče. Nasazení musí podporovat vstup obrázku a `reasoning_effort: none`.
- Základní znalosti Javy, Spring Bootu a webového vývoje

## Pochopení struktury projektu

Projekt příběhu o mazlíčkovi obsahuje několik důležitých souborů:

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

## Vysvětlení hlavních komponent

### 1. Hlavní aplikace

**Soubor:** `PetStoryApplication.java`

Toto je vstupní bod naší Spring Boot aplikace:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Co to dělá:**
- Anotace `@SpringBootApplication` povoluje auto-konfiguraci a skenování komponent
- Spouští vestavěný webový server (Tomcat) na portu 8080
- Automaticky vytváří všechny potřebné Spring beany a služby

### 2. Webový kontroler

**Soubor:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Požadavek | Úspěšná odpověď |
| --- | --- | --- |
| `GET /` | Žádné tělo | HTML formulář pro nahrání s CSRF tokenem |
| `POST /analyze-image` | `multipart/form-data`, pole souboru `image` | JSON: `{"description":"Hravé zvířátko..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, pole `description` | HTML stránka s popisem a vygenerovaným příběhem |

Oba POST endpointy vyžadují session cookie a CSRF token získané z `GET /`. Skript pro upload odesílá skrytou hodnotu `_csrf` v hlavičce `X-CSRF-TOKEN`; odeslání příběhu ho posílá jako formulářové pole `_csrf`. API klienti musí mezi požadavky zachovávat cookie. Jedná se o formulářové endpointy, ne o JSON požadavky.

Popisy musí být neprazdné a dlouhé maximálně 1000 znaků. Kontroler text zkrátí a odstraní `<`, `>`, uvozovky, apostrofy a `&` před předáním do služby. Výsledná šablona také escapuje výstup modelu pomocí `th:text`.

Chyby validace obrázku vrací HTTP 400 s polem `error`; chyby modelu vrací HTTP 502 s polem `error` a bez `description`. Neplatné popisy příběhů nebo chyby modelu přesměrují na `/` s viditelnou chybou. Chybějící povinná pole vrací HTTP 400 a chybějící nebo neplatné CSRF tokeny HTTP 403. Žádné náhradní popisy nebo příběhy nejsou předkládány jako úspěšné AI výsledky.

### 3. Služba pro příběh

**Soubor:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Oficiální OpenAI Java SDK 4.63.1 volá Azure AI Foundry OpenAI-kompatibilní Chat Completions API. Azure Identity 1.18.6 poskytuje Microsoft Entra bearer token přes `DefaultAzureCredential`; API klíč není potřeba.

| Operace | Vstup | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bajty obrázku zakódované jako base64 data URL s nahraným MIME typem | 300 |
| `generateStory` | Popis mazlíčka v uživatelské zprávě | 800 |

Oba požadavky používají nakonfigurované nasazení, ve výchozím nastavení `gpt-5.6-luna`, a explicitně nastavují `ReasoningEffort.NONE` (`reasoning_effort: none`). Ani jeden požadavek neposílá `temperature` ani starší parametr `max_tokens`.

Analýza obrázku přijímá JPEG, PNG, GIF a WebP, odmítá prázdné obrázky a soubory větší než 10MB a omezuje popis na 1000 znaků. Výzva k příběhu vyžaduje krátký příběh vhodný pro rodiny. Prázdné volby nebo prázdný obsah modelu jsou chybou a chyby uchovávají původní příčinu pro diagnostiku na straně serveru. SDK klient se uzavírá při vypnutí aplikace.

### 4. Webové šablony

**Soubor:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formulář pro nahrání)

Stránka začíná výběrem fotky, nikoli textovým polem pro popis. **Analyze Image** zobrazí náhled vybraného obrázku a pošle ho na `/analyze-image`. Úspěšná odpověď zobrazí popis, vyplní skryté pole `description` a zobrazí tlačítko **Generate Story**. To odešle formulář na `/generate-story`.

Neexistuje žádné stahování modelu v prohlížeči ani závislost na CDN. Analýza obrázku běží na serveru přes nakonfigurované Azure nasazení. Chyby zůstávají viditelné a neumožní generování příběhu s vymyšleným popisem. Výběr jiného souboru vymaže předchozí analýzu.

**Soubor:** `result.html` (Zobrazení příběhu)

Zobrazuje vygenerovaný příběh:

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

**Vlastnosti šablony:**

1. **Integrace Thymleaf**: Používá atributy `th:` pro dynamický obsah
2. **Responzivní design**: CSS stylování pro mobily i desktop
3. **Zpracování chyb**: Zobrazuje uživateli validační chyby
4. **Zpracování uploadu**: JavaScript náhled obrázku, odesílá CSRF chráněný multipart požadavek a zobrazuje vrácený popis

### 5. Konfigurace

**Soubor:** `application.properties`

Konfigurační nastavení aplikace:

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

**Vysvětlení konfigurace:**

1. **Nahrávání souborů**: Limit 10MB platí jak pro soubor, tak pro celý multipart požadavek; udržujte fotografie pod tímto limitem, aby bylo místo pro multipart hlavičky
2. **Logování**: Ovládá, jaké informace se logují během běhu
3. **Azure AI Foundry**: Určuje endpoint a nasazení modelu, které se použije (autentizace bez klíče)
4. **Bezpečnost**: Ochrana CSRF zůstává aktivní; diagnostika modelu je logována na serveru, kontroler zobrazuje obecné zprávy o chybách modelu

## Spuštění aplikace

### Krok 1: Přihlášení a nastavení endpointu

Autentizace je bez klíče (Microsoft Entra ID), takže není třeba API klíč. Přihlaste se a nastavte svůj Foundry endpoint:

**Windows (Příkazový řádek):**
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

**Proč je to potřeba:**
- Azure AI Foundry využívá Microsoft Entra ID k autentizaci inference požadavků
- Autentizace bez klíče znamená žádná tajemství ve vašem kódu nebo prostředí
- Váš účet potřebuje roli **Cognitive Services OpenAI User** na zdroji

Výchozí název nasazení je `gpt-5.6-luna`. Pokud má vaše nasazení GPT-5.6 Luna jiný název, nastavte `AZURE_OPENAI_DEPLOYMENT` ve stejném terminálu před spuštěním aplikace. Analýza obrázku i generování příběhu používají toto nastavení.

### Krok 2: Sestavení a spuštění

Přejděte do adresáře projektu:
```bash
cd 04-PracticalSamples/petstory
```

Sestavte spustitelný samostatný JAR a spusťte všechny offline testy:
```bash
mvn clean package
```

Spusťte server:
```bash
mvn spring-boot:run
```

Aplikace bude dostupná na `http://localhost:8080`.

Alternativně spusťte zabalený JAR na volném portu, například:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Pro tento příkaz otevřete `http://localhost:8083/`. Totéž `/analyze-image` a `/generate-story` jsou dostupné na zvoleném portu.

### Krok 3: Testování aplikace

1. **Otevřete** `http://localhost:8080` ve svém prohlížeči
2. **Vyberte** jasnou fotografii mazlíčka ve formátu JPEG, PNG, GIF nebo WebP, pod 10MB
3. **Klikněte** na „Analyze Image“ a počkejte na popis mazlíčka
4. **Klikněte** na „Generate Story“ po úspěšné analýze
5. **Prohlédněte** si příběh a použijte odkaz na výsledné stránce pro návrat k formuláři uploadu

Úspěšný tok od fotografie k příběhu volá model dvakrát, jednou za tlačítko. Živá inference spotřebovává kvótu vašeho nasazení a může způsobit náklady; při sdíleném nasazení s omezením rychlosti spouštějte smoke testy sériově. Načítání domovské stránky znamená volání modelu.

## Offline testy

Ze složky sample spusťte:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) zachycuje skutečné požadavky SDK OpenAI v rámci loopback HTTP fixture. Kontroluje nasazení obou požadavků, `reasoning_effort: none`, limit tokenů, payload obrázku, validaci vstupu, prázdné odpovědi a chyby upstream.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) využívá MockMvc s mockovanou službou modelu k testování vykreslených Thymeleaf stránek, upload kontraktu, CSRF, validace, escapingu výstupu a viditelných chyb. Tyto testy nevyžadují Azure přihlašovací údaje a nikdy nevolají placenou Azure inference. Maven ukládá Surefire reporty do `target/surefire-reports`.

## Jak vše funguje dohromady

Zde je kompletní postup při generování příběhu o mazlíčkovi:

1. **Výběr fotky**: Vyberete obrázek mazlíčka ve formuláři pro nahrání
2. **Nahrání obrázku**: „Analyze Image“ odešle multipart POST na `/analyze-image` s CSRF hlavičkou
3. **Analýza obrázku**: `StoryService` pošle obrázek na GPT-5.6 Luna s nastaveným reasoning na `none`
4. **Zobrazení popisu**: Prohlížeč zobrazí vrácený popis a uloží ho do formuláře
5. **Odeslání příběhu**: „Generate Story“ odešle `description` a `_csrf` na `/generate-story`
6. **Generování příběhu**: Kontroler ověří popis a zavolá stejné nasazení s reasoning nastaveným na `none`
7. **Vykreslení šablony**: Thymeleaf escapuje a zobrazuje popis a příběh na výsledné stránce

**Zpracování chyb:**
Pokud model selže, server zaloguje příčinu. Analýza obrázku vrací HTTP 502 a prohlížeč zobrazuje chybu bez zobrazení tlačítka „Generate Story“. Generování příběhu přesměruje na formulář s chybovou zprávou. Žádná cesta tichým způsobem nenahrazuje předem napsaný výsledek.

## Pochopení integrace AI

### Azure AI Foundry (bez klíče)
Služba konfiguruje SDK s endpointem `/openai/v1/` vašeho zdroje. `DefaultAzureCredential` a `AuthenticationUtil.getBearerTokenSupplier` poskytují Microsoft Entra tokeny pro `https://ai.azure.com/.default`. Lokální vývoj může využít vaše přihlášení v Azure CLI; aplikace hostovaná v Azure může používat managed identity s potřebnými oprávněními na zdroj.

### Prompt engineering
Analýza obrázku žádá o pozorovatelné vlastnosti mazlíčka v krátkém odstavci a říká modelu, aby text na obrázku považoval za data, ne za instrukce. Generování příběhu využívá vrácený popis v samostatném, rodinně přátelském psaní. Ani jeden hovor nezapíná reasoning ani nenastavuje přepis teploty.

### Zpracování odpovědi
Sdílený handler odpovědí odmítá chybějící volby a prázdný či pouze bílé znaky obsah, ořezává platný obsah a uchovává chyby upstream. Popisy obrázků jsou limitovány na 1000 znaků, aby vyhověly následnému formuláři příběhu. Původní chyba modelu se uchovává pro diagnostiku, ale uživateli se nezobrazuje.

## Další kroky

Pro více příkladů viz [Kapitola 04: Praktické příklady](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o co největší přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->