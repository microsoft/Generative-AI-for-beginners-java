# Vodič za generiranje priče o ljubimcu za početnike

Prenesite fotografiju ljubimca, analizirajte je s GPT-5.6 Luna i generirajte priču iz dobivene opisne informacije. Za oba zahtjeva modela koristi se `reasoning_effort: none`.

| Komponenta | Verzija |
| --- | --- |
| Java | 21 ili noviji |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Sadržaj

- [Preduvjeti](#preduvjeti)
- [Razumijevanje strukture projekta](#razumijevanje-strukture-projekta)
- [Objašnjenje osnovnih komponenti](#objašnjenje-osnovnih-komponenti)
  - [1. Glavna aplikacija](#1-glavna-aplikacija)
  - [2. Web kontroler](#2-web-kontroler)
  - [3. Servis priče](#3-servis-priče)
  - [4. Web predlošci](#4-web-predlošci)
  - [5. Konfiguracija](#5-konfiguracija)
- [Pokretanje aplikacije](#pokretanje-aplikacije)
- [Offline testovi](#offline-testovi)
- [Kako sve funkcionira zajedno](#kako-sve-funkcionira-zajedno)
- [Razumijevanje AI integracije](#razumijevanje-ai-integracije)
- [Sljedeći koraci](#sljedeći-koraci)

## Preduvjeti

Prije početka provjerite imate li:
- Java 21 ili noviju verziju instaliranu
- Maven za upravljanje ovisnostima
- Azure AI Foundry implementaciju GPT-5.6 Luna naziva `gpt-5.6-luna` ili `AZURE_OPENAI_DEPLOYMENT` preklopnik koji pokazuje na tu implementaciju. Pogledajte [Poglavlje 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) za postavljanje i prijavu s `az login` za autentifikaciju bez ključa. Implementacija mora podržavati unos slike i `reasoning_effort: none`.
- Osnovno razumijevanje Jave, Spring Boota i web razvoja

## Razumijevanje strukture projekta

Projekt priče o ljubimcu ima nekoliko važnih datoteka:

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

## Objašnjenje osnovnih komponenti

### 1. Glavna aplikacija

**Datoteka:** `PetStoryApplication.java`

Ovo je ulazna točka za našu Spring Boot aplikaciju:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Što ovo radi:**
- `@SpringBootApplication` anotacija omogućuje automatsku konfiguraciju i skeniranje komponenti
- Pokreće ugrađeni web poslužitelj (Tomcat) na portu 8080
- Automatski stvara sve potrebne Spring beane i servise

### 2. Web kontroler

**Datoteka:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Zahtjev | Uspješan odgovor |
| --- | --- | --- |
| `GET /` | Bez tijela | HTML obrazac za prijenos s CSRF tokenom |
| `POST /analyze-image` | `multipart/form-data`, polje datoteke `image` | JSON: `{"description":"Razigrani ljubimac..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, polje `description` | HTML stranica s rezultatom s opisom i generiranom pričom |

Oba POST endpointa zahtijevaju kolačić sesije i CSRF token dobivene iz `GET /`. Skripta prijenosa šalje skrivenu vrijednost `_csrf` u zaglavlju `X-CSRF-TOKEN`; slanje priče šalje ga kao `_csrf` polje obrasca. API klijenti moraju sačuvati kolačić između zahtjeva. Ovo su endpointi za obrasce, a ne JSON zahtjevi.

Opisi moraju biti neprazni i ne dulji od 1000 znakova. Kontroler uklanja razmake sa započetka i kraja te uklanja `<`, `>`, dvostruke navodnike, apostrofe i `&` prije nego što ih proslijedi servisu. Predložak rezultata također escape-a izlaz modela s `th:text`.

Neuspjesi pri validaciji slike vraćaju HTTP 400 s poljem `error`; neuspjesi modela vraćaju HTTP 502 s poljem `error` i bez `description`. Nevaljani opisi priče ili neuspjesi modela preusmjeravaju na `/` s vidljivom porukom o pogrešci. Nedostajuća obavezna polja vraćaju HTTP 400, a nedostajući ili nevaljani CSRF tokeni vraćaju HTTP 403. Nema rezerviranih opisa ni priča prikazanih kao uspješni AI rezultati.

### 3. Servis priče

**Datoteka:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Službeni OpenAI Java SDK 4.63.1 poziva Azure AI Foundry OpenAI-kompatibilni Chat Completions API. Azure Identity 1.18.6 osigurava Microsoft Entra bearer token preko `DefaultAzureCredential`; nije potreban API ključ.

| Operacija | Ulaz | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bajtovi slike kodirani kao base64 data URL s uploadanim MIME tipom | 300 |
| `generateStory` | Opis ljubimca u korisničkoj poruci | 800 |

Oba zahtjeva koriste konfiguriranu implementaciju, prema zadanim postavkama `gpt-5.6-luna`, i eksplicitno postavljaju `ReasoningEffort.NONE` (`reasoning_effort: none`). Nijedan zahtjev ne šalje `temperature` ili zastarjeli parametar `max_tokens`.

Analiza slike prihvaća JPEG, PNG, GIF i WebP, odbacuje prazne slike i datoteke veće od 10 MB te ograničava opis na 1000 znakova. Upit za priču traži obiteljsku kratku priču. Prazni izbori ili sadržaj modela bez teksta su pogreške, a neuspjesi se evidentiraju za dijagnostiku na strani poslužitelja. SDK klijent se zatvara kad se aplikacija zaustavi.

### 4. Web predlošci

**Datoteka:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Obrazac za prijenos)

Stranica započinje izborom fotografije, a ne tekstualnim područjem za opis. **Analyze Image** prikazuje odabranu fotografiju i šalje je na `/analyze-image`. Uspješan odgovor prikazuje opis, popunjava skriveno polje `description` i otkriva **Generate Story**. Ta tipka šalje postojeći obrazac na `/generate-story`.

Nema preuzimanja modela u pregledniku niti ovisnosti o CDN-u. Analiza slike odvija se na poslužitelju preko konfigurirane Azure implementacije. Neuspjesi ostaju vidljivi i ne dopuštaju generiranje priče s izmišljenim opisom. Odabirom druge datoteke briše se prethodna analiza.

**Datoteka:** `result.html` (Prikaz priče)

Prikazuje generiranu priču:

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

**Značajke predloška:**

1. **Integracija Thymeleaf:** Koristi `th:` atribute za dinamički sadržaj
2. **Prilagodljiv dizajn:** CSS stilizacija za mobilne i desktop uređaje
3. **Rukovanje pogreškama:** Prikazuje validacijske pogreške korisnicima
4. **Rukovanje prijenosom:** JavaScript prikazuje pregled fotografije, šalje multipart zahtjev zaštićen CSRF-om i prikazuje vraćeni opis

### 5. Konfiguracija

**Datoteka:** `application.properties`

Postavke konfiguracije aplikacije:

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

**Objašnjenje konfiguracije:**

1. **Prijenos datoteka:** Maksimalna veličina kako za datoteku, tako i za cijeli multipart zahtjev je 10 MB; držite fotografije ispod tog ograničenja da ostavite mjesta za multipart zaglavlja
2. **Evidencija:** Kontrolira što se bilježi tijekom izvođenja
3. **Azure AI Foundry:** Definira krajnju točku i implementaciju modela za korištenje (autentifikacija bez ključa)
4. **Sigurnost:** CSRF zaštita ostaje uključena; dijagnostika modela bilježi se na poslužitelju, a kontroler prikazuje generičke poruke o pogreškama modela

## Pokretanje aplikacije

### Korak 1: Prijava i postavljanje krajnje točke

Autentifikacija je bez ključa (Microsoft Entra ID), pa nema API ključa. Prijavite se i postavite svoju Foundry krajnju točku:

**Windows (Command Prompt):**
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

**Zašto je ovo potrebno:**
- Azure AI Foundry koristi Microsoft Entra ID za autentifikaciju zahtjeva za zaključivanje
- Autentifikacija bez ključa znači da nema tajni u vašem izvornom kodu ili okruženju
- Vaš račun mora imati ulogu **Cognitive Services OpenAI User** na resursu

Zadani naziv implementacije je `gpt-5.6-luna`. Ako vaša implementacija GPT-5.6 Luna ima drugi naziv, postavite `AZURE_OPENAI_DEPLOYMENT` u istom terminalu prije pokretanja aplikacije. I analiza slike i generiranje priče koriste ovu postavku.

### Korak 2: Izgradnja i pokretanje

Idite u direktorij projekta:
```bash
cd 04-PracticalSamples/petstory
```

Izgradite samostalni izvršni JAR i pokrenite sve offline testove:
```bash
mvn clean package
```

Pokrenite poslužitelj:
```bash
mvn spring-boot:run
```

Aplikacija će se pokrenuti na `http://localhost:8080`.

Alternativno, pokrenite paketirani JAR na slobodnom portu, primjerice:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Za tu naredbu otvorite `http://localhost:8083/`. Isti su `/analyze-image` i `/generate-story` putevi dostupni na odabranom portu.

### Korak 3: Testirajte aplikaciju

1. **Otvorite** `http://localhost:8080` u vašem pregledniku
2. **Odaberite** jasnu fotografiju ljubimca u JPEG, PNG, GIF ili WebP formatu, manju od 10 MB
3. **Kliknite** "Analyze Image" i pričekajte opis ljubimca
4. **Kliknite** "Generate Story" nakon uspješne analize
5. **Pogledajte** priču i koristite poveznicu na stranici rezultata za povratak na obrazac za prijenos

Uspješan tijek od fotografije do priče ostvaruje dva poziva modelu, jedan po gumbu. Live inferencija troši kvotu vaše implementacije i može izazvati naknade; pokrećite testove uzastopno pri dijeljenju ograničene implementacije. Učitavanje početne stranice ne poziva model.

## Offline testovi

Iz uzorka direktorija pokrenite:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) snima stvarne OpenAI SDK zahtjeve pomoću loopback HTTP fiksture. Provjerava implementaciju oba zahtjeva, `reasoning_effort: none`, limite tokena, input slike, validaciju, prazne odgovore i greške uzlaznog toka.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) koristi MockMvc s mockiranim servisom modela za testiranje prikazanih Thymeleaf stranica, ugovora o prijenosu, CSRF-a, validacije, escape-a izlaza i vidljivih grešaka. Ti testovi ne zahtijevaju Azure vjerodajnice i nikada ne pozivaju plaćenu Azure inferenciju. Maven zapisuje Surefire izvještaje u `target/surefire-reports`.

## Kako sve funkcionira zajedno

Evo kompletnog tijeka kada generirate priču o ljubimcu:

1. **Odabir fotografije:** Birate sliku ljubimca u obrascu za prijenos
2. **Prijenos slike:** "Analyze Image" šalje multipart POST na `/analyze-image` s CSRF zaglavljem
3. **Analiza slike:** `StoryService` šalje sliku GPT-5.6 Luni s postavkom reasoning na `none`
4. **Prikaz opisa:** Preglednik prikazuje vraćeni opis i sprema ga u obrazac
5. **Slanje priče:** "Generate Story" šalje `description` i `_csrf` na `/generate-story`
6. **Generiranje priče:** Kontroler validira opis i poziva istu implementaciju s reasoningom postavljenim na `none`
7. **Renderiranje predloška:** Thymeleaf escape-a i prikazuje opis i priču na stranici rezultata

**Tijek rukovanja pogreškama:**
Ako model ne uspije, poslužitelj evidentira uzrok. Analiza slike vraća HTTP 502 i preglednik prikazuje pogrešku bez prikaza "Generate Story". Generiranje priče preusmjerava na obrazac s porukom o pogrešci. Nijedan put ne zamjenjuje tiho unaprijed napisani rezultat.

## Razumijevanje AI integracije

### Azure AI Foundry (bez ključa)
Servis konfigurira SDK s vašom resursnom `/openai/v1/` krajnjom točkom. `DefaultAzureCredential` i `AuthenticationUtil.getBearerTokenSupplier` opskrbljuju Microsoft Entra tokene za `https://ai.azure.com/.default`. Lokalni razvoj može koristiti vašu Azure CLI prijavu; Azure-hostana aplikacija može koristiti upravljani identitet s potrebnim dopuštenjima resursa.

### Inženjering promptova
Za analiza slike traži opažljive značajke ljubimca u kratkom odlomku i nalaže modelu da tretira tekst na slici kao podatke, a ne upute. Generiranje priče koristi vraćeni opis u zasebnom, obiteljskom pisanju. Nijedan poziv ne omogućuje reasoning niti postavlja override temperature.

### Obrada odgovora
Zajednički handler odgovora odbacuje nedostatak izbora i prazni ili samo razmaci sadržaj, uklanja razmake i čuva greške uzlaznog modela. Opisi slika su ograničeni na 1000 znakova da stanu u sljedeći obrazac priče. Izvorni neuspjeh modela se čuva za dijagnostiku, ali se ne prikazuje korisniku.

## Sljedeći koraci

Za više primjera, pogledajte [Poglavlje 04: Praktični primjeri](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati greške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->