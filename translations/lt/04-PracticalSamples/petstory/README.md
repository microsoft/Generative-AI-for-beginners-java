# Augintinių istorijų generatoriaus pamoka pradedantiesiems

Įkelkite augintinio nuotrauką, ją analizuokite naudodami GPT-5.6 Luna ir sugeneruokite istoriją iš gauto aprašymo. Abu modelio užklausimai naudoja `reasoning_effort: none`.

| Komponentas | Versija |
| --- | --- |
| Java | 21 arba naujesnė |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Turinys

- [Reikalavimai](#reikalavimai)
- [Projekto struktūros supratimas](#projekto-struktūros-supratimas)
- [Pagrindinių komponentų paaiškinimas](#pagrindinių-komponentų-paaiškinimas)
  - [1. Pagrindinė programa](#1-pagrindinė-programa)
  - [2. Web valdiklis](#2-web-valdiklis)
  - [3. Istorijų servisas](#3-istorijų-servisas)
  - [4. Web šablonai](#4-web-šablonai)
  - [5. Konfigūracija](#5-konfigūracija)
- [Programos paleidimas](#programos-paleidimas)
- [Offline testai](#offline-testai)
- [Kaip visa tai veikia kartu](#kaip-visa-tai-veikia-kartu)
- [AI integracijos supratimas](#ai-integracijos-supratimas)
- [Kiti žingsniai](#kiti-žingsniai)

## Reikalavimai

Prieš pradėdami įsitikinkite, kad turite:
- Įdiegtą Java 21 arba naujesnę versiją
- Maven priklausomybių valdymui
- Azure AI Foundry GPT-5.6 Luna diegimą pavadinimu `gpt-5.6-luna`, arba `AZURE_OPENAI_DEPLOYMENT` pakeitimą, nukreipiantį į tą diegimą. Žr. [2 skyrių](../../02-SetupDevEnvironment/getting-started-azure-openai.md) dėl diegimo ir prisijunkite naudodami `az login` be raktų autentifikacijos. Diegimas turi palaikyti vaizdo įkėlimą ir `reasoning_effort: none`.
- Pagrindines žinias apie Java, Spring Boot ir žiniatinklio kūrimą

## Projekto struktūros supratimas

Augintinių istorijų projekte yra keletas svarbių failų:

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

## Pagrindinių komponentų paaiškinimas

### 1. Pagrindinė programa

**Failas:** `PetStoryApplication.java`

Tai mūsų Spring Boot programos įėjimo taškas:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ką tai daro:**
- `@SpringBootApplication` anotacija leidžia automatinį konfigūravimą ir komponentų nuskaitymą
- Paleidžia integruotą žiniatinklio serverį (Tomcat) prievade 8080
- Automatiškai sukuria visus reikalingus Spring beans ir servisas

### 2. Web valdiklis

**Failas:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Galinis taškas | Užklausa | Sėkmingas atsakymas |
| --- | --- | --- |
| `GET /` | Be turinio | HTML įkėlimo forma su CSRF žetonu |
| `POST /analyze-image` | `multipart/form-data`, bylų laukas `image` | JSON: `{"description":"Žaismingas augintinis..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, laukas `description` | HTML rezultatų puslapis su aprašymu ir sugeneruota istorija |

Abu POST galiniai taškai reikalauja sesijos slapuko ir CSRF žetono, gauto iš `GET /`. Įkėlimo skriptas siunčia paslėptą `_csrf` reikšmę `X-CSRF-TOKEN` antraštėje; istorijos pateikimas siunčia kaip `_csrf` formos lauką. API klientai privalo saugoti slapuką užklausų metu. Tai formos galiniai taškai, o ne JSON užklausų galiniai taškai.

Aprašymai turi būti ne tušti ir ne ilgesni nei 1000 simbolių. Valdiklis apkarpo aprašymą ir pašalina `<`, `>`, dvigubas kabutes, apostrofius ir `&` prieš perduodamas servisu. Rezultato šablonas taip pat apsaugo modelio išvestį naudodamas `th:text`.

Vaizdo validavimo klaidos grąžina HTTP 400 su lauku `error`; modelio klaidos grąžina HTTP 502 su lauku `error` ir be `description`. Netinkami istorijos aprašymai arba modelio klaidos nukreipia į `/` su matoma klaida. Privalomų laukų trūkumai grąžina HTTP 400, o trūkstami arba neteisingi CSRF žetonai grąžina HTTP 403. Nepateikiamos jokios pakaitinės aprašymo ar istorijos versijos kaip sėkmingi AI rezultatai.

### 3. Istorijų servisas

**Failas:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Oficialus OpenAI Java SDK 4.63.1 kviečia Azure AI Foundry OpenAI suderinamą Chat Completions API. Azure Identity 1.18.6 suteikia Microsoft Entra prieigos žetoną per `DefaultAzureCredential`; API raktas nereikalingas.

| Veiksmas | Įvestis | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Vaizdo baitai, užkoduoti kaip base64 duomenų URL su įkeltu MIME tipu | 300 |
| `generateStory` | Augintinio aprašymas vartotojo pranešime | 800 |

Abu užklausimai naudoja konfigūruotą diegimą, pagal nutylėjimą `gpt-5.6-luna`, ir aiškiai nustato `ReasoningEffort.NONE` (`reasoning_effort: none`). Nė viena užklausa nesiunčia `temperature` ar senojo `max_tokens` parametro.

Vaizdo analizė priima JPEG, PNG, GIF ir WebP formatus, atmeta tuščius vaizdus ir failus, viršijančius 10MB, bei riboja galutinį aprašymą iki 1000 simbolių. Istorijos užklausa reikalauja šeimai tinkamos trumpą istoriją. Tušti pasirinkimai arba tuščias modelio turinys laikomi klaidomis, o klaidų priežastis išlaikoma serverio diagnostikai. SDK klientas uždaromas, kai programa sustabdyta.

### 4. Web šablonai

**Failas:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Įkėlimo forma)

Puslapis prasideda nuo nuotraukų pasirinkimo, o ne aprašymo teksto lauko. **Analizuoti vaizdą** parodo pasirinktą nuotrauką ir išsiunčia į `/analyze-image`. Sėkmingas atsakymas rodo aprašymą, užpildo paslėptą `description` lauką ir atgaivina mygtuką **Sugeneruoti istoriją**. Šis mygtukas pateikia esamą formą į `/generate-story`.

Naršyklėje nesiunčiama jokia modelio kopija ar CDN priklausomybė. Vaizdo analizė vyksta serveryje per konfigūruotą Azure diegimą. Klaidos lieka matomos ir neleidžia generuoti istorijos su sukurta fiktyvia aprašymo versija. Pasirinkus kitą failą ankstesnė analizė išvaloma.

**Failas:** `result.html` (Istorijos rodymas)

Rodo sugeneruotą istoriją:

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

**Šablono ypatybės:**

1. **Thymeleaf integracija**: Naudoja `th:` atributus dinaminei turinio valdymui
2. **Reaguojantis dizainas**: CSS stilizavimas mobiliesiems ir stacionariems įrenginiams
3. **Klaidų tvarkymas**: Rodo validavimo klaidas vartotojams
4. **Įkėlimo tvarkymas**: JavaScript peržiūri nuotrauką, siunčia CSRF apsaugotą multipart užklausą ir rodo grąžintą aprašymą

### 5. Konfigūracija

**Failas:** `application.properties`

Programos konfigūracijos nustatymai:

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

**Konfigūracijos paaiškinimas:**

1. **Failų įkėlimas**: Tiek failas, tiek visas multipart užklausa ribojami iki 10MB; laikykitės mažesnių nuotraukų, kad liktų vietos multipart antraštėms
2. **Logavimas**: Valdo, kokią informaciją fiksuoti vykdymo metu
3. **Azure AI Foundry**: Nurodo naudotiną galutinį tašką ir modelio diegimą (be raktų autentifikacijos)
4. **Saugumas**: CSRF apsauga išlieka įjungta; modelio diagnostika loguojama serveryje, valdiklis rodo bendras modelio klaidų žinutes

## Programos paleidimas

### 1 veiksmas: Prisijungimas ir galutinio taško nustatymas

Autentifikacija be raktų (Microsoft Entra ID), todėl API rakto nereikia. Prisijunkite ir nustatykite Foundry galutinį tašką:

**Windows (Komandų eilutė):**
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

**Kodėl tai reikalinga:**
- Azure AI Foundry naudoja Microsoft Entra ID autentifikuoti užklausas
- Autentifikacija be raktų reiškia, kad jūsų šaltinio kode ar aplinkoje nėra slapčių duomenų
- Jūsų paskyra turi turėti **Cognitive Services OpenAI User** rolę prie ištekliaus

Pagal nutylėjimą diegimo pavadinimas yra `gpt-5.6-luna`. Jei jūsų GPT-5.6 Luna diegimas turi kitą pavadinimą, nustatykite `AZURE_OPENAI_DEPLOYMENT` tame pačiame terminale prieš paleidžiant programą. Tiek vaizdo analizė, tiek istorijos generavimas naudoja šį nustatymą.

### 2 veiksmas: Sukurkite ir paleiskite

Eikite į projekto katalogą:
```bash
cd 04-PracticalSamples/petstory
```

Sukurkite savarankišką JAR vykdomąjį failą ir paleiskite visus offline testus:
```bash
mvn clean package
```

Paleiskite serverį:
```bash
mvn spring-boot:run
```

Programa bus pasiekiama adresu `http://localhost:8080`.

Arba paleiskite supakuotą JAR laisvame prievade, pavyzdžiui:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Šiai komandai atidaromas `http://localhost:8083/`. Tie patys `/analyze-image` ir `/generate-story` maršrutai veikia pasirinktu prievadu.

### 3 veiksmas: Išbandykite programą

1. **Atidarykite** `http://localhost:8080` savo naršyklėje
2. **Pasirinkite** aiškią augintinio nuotrauką JPEG, PNG, GIF arba WebP formatu, iki 10MB
3. **Spustelėkite** "Analizuoti vaizdą" ir palaukite augintinio aprašymo
4. **Spustelėkite** "Sugeneruoti istoriją" po sėkmingos analizės
5. **Peržiūrėkite** istoriją ir naudokite rezultatų puslapio nuorodą, norėdami grįžti į įkėlimo formą

Sėkmingas nuotraukos į istoriją srautas atlieka du modelio kvietimus, po vieną kiekvienam mygtukui. Gyva inferencija naudoja jūsų diegimo kvotą ir gali sukelti mokesčius; vykdykite pagrindinius testus paeiliui, jei dalijatės ribota diegimo spartą. Pagrindinio puslapio įkėlimas nekviečia modelio.

## Offline testai

Iš pavyzdžių katalogo vykdykite:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) fiksuoja tikras OpenAI SDK užklausas naudodamas lokalų HTTP fixturą. Tikrina abu užklausų diegimus, `reasoning_effort: none`, žetonų ribas, vaizdo įkėlimą, įvesties validaciją, tuščius atsakymus ir viršutines klaidas.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) naudoja MockMvc su imituotu modelio servisu, kad testuotų pateiktų Thymeleaf puslapių atvaizdavimą, įkėlimo sutartį, CSRF, validaciją, išvesties apsaugą ir matomas klaidas. Šiems testams nereikia Azure kredencialų ir jie niekada nekviečia mokamos Azure inferencijos. Maven rašo Surefire ataskaitas į `target/surefire-reports`.

## Kaip visa tai veikia kartu

Štai visas srautas, kai generuojate augintinio istoriją:

1. **Nuotraukos pasirinkimas**: Pasirenkate augintinio vaizdą įkėlimo formoje
2. **Vaizdo įkėlimas**: "Analizuoti vaizdą" siunčia multipart POST į `/analyze-image` su CSRF antrašte
3. **Vaizdo analizė**: `StoryService` siunčia vaizdą GPT-5.6 Luna su reasoning nustatytu į `none`
4. **Aprašymo rodymas**: Naršyklė parodo grąžintą aprašymą ir saugo jį formoje
5. **Istorijos pateikimas**: "Sugeneruoti istoriją" pateikia `description` ir `_csrf` į `/generate-story`
6. **Istorijos generavimas**: Valdiklis tikrina aprašymą ir kviečia tą patį diegimą su reasoning `none`
7. **Šablono atvaizdavimas**: Thymeleaf apsaugo ir parodo aprašymą ir istoriją rezultatų puslapyje

**Klaidų valdymo srautas:**
Jei modelis nepavyksta, serveris užfiksuoja priežastį. Vaizdo analizė grąžina HTTP 502 ir naršyklė rodo klaidą, nerodant "Sugeneruoti istoriją". Istorijos generavimas nukreipia atgal į formą su klaidos pranešimu. Jokia iš dviejų krypčių tyliai nekeičia rezultato iš anksto parengtu.

## AI integracijos supratimas

### Azure AI Foundry (be raktų)
Servisas konfigūruoja SDK su jūsų ištekliaus `/openai/v1/` galutiniu tašku. `DefaultAzureCredential` ir `AuthenticationUtil.getBearerTokenSupplier` tiekia Microsoft Entra žetonus `https://ai.azure.com/.default`. Vietiniam kūrimui galima naudoti jūsų Azure CLI prisijungimą; Azure talpinama programa gali naudoti valdomą tapatybę su reikalingomis išteklių teisėmis.

### Užklausų konstravimas (Prompt Engineering)
Vaizdo analizė prašo pastebimų augintinio savybių trumpame pastraipoje ir įspėja modelį laikyti tekstą vaizde kaip duomenis, o ne instrukcijas. Istorijos generavimas naudoja grąžintą aprašymą atskiroje, šeimai tinkamoje rašymo užklausoje. Nei viena užklausa neįjungia reasoning ar nenustato temperatūros pakeitimo.

### Atsakymo apdorojimas
Bendras atsakymų apdorotojas atmeta trūkstamus pasirinkimus ir tuščią arba tik tarpus turinį, apkarpo galiojantį turinį ir išsaugo viršunines klaidas. Vaizdo aprašymai ribojami iki 1000 simbolių, kad tilptų vėlesnėje istorijos formoje. Originali modelio klaida išlaikoma diagnostikai, bet nerodoma vartotojui.

## Kiti žingsniai

Daugiau pavyzdžių žr. [4 skyrių: Praktiniai pavyzdžiai](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kilusią naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->