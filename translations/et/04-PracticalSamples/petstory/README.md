# Lemmikloolooja juhend algajatele

Laadige üles lemmiklooma foto, analüüsige seda GPT-5.6 Lunaga ja genereerige saadud kirjelduse põhjal lugu. Mõlemas mudelipäringus kasutatakse `reasoning_effort: none`.

| Komponent | Versioon |
| --- | --- |
| Java | 21 või uuem |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Sisukord

- [Eeltingimused](#eeltingimused)
- [Projekti struktuuri mõistmine](#projekti-struktuuri-mõistmine)
- [Põhikomponentide selgitus](#põhikomponentide-selgitus)
  - [1. Põhirakendus](#1-põhirakendus)
  - [2. Veebikontroller](#2-veebikontroller)
  - [3. Lugude teenus](#3-lugude-teenus)
  - [4. Veebimallid](#4-veebimallid)
  - [5. Konfiguratsioon](#5-konfiguratsioon)
- [Rakenduse käivitamine](#rakenduse-käivitamine)
- [Võrgust väljaspool testimine](#võrgust-väljaspool-testimine)
- [Kuidas see kõik töötab](#kuidas-see-kõik-töötab)
- [Tehisintellekti integreerimise mõistmine](#tehisintellekti-integreerimise-mõistmine)
- [Järgmised sammud](#järgmised-sammud)

## Eeltingimused

Enne alustamist veenduge, et teil on:
- Java 21 või uuem installitud
- Maven sõltuvuste haldamiseks
- Azure AI Foundry GPT-5.6 Luna juurutus nimega `gpt-5.6-luna` või `AZURE_OPENAI_DEPLOYMENT` asend, mis osutab sellele juurutusele. Vaadake [2. peatükki](../../02-SetupDevEnvironment/getting-started-azure-openai.md) kasutuselevõtuks ja logige sisse `az login` abil võtmepõhise autentimise asemel. Juurutus peab toetama pildi sisendit ja `reasoning_effort: none`.
- Põhitõdede mõistmine Java, Spring Booti ja veebiarenduse kohta

## Projekti struktuuri mõistmine

Lemmikloolooja projektis on mitu olulist faili:

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

## Põhikomponentide selgitus

### 1. Põhirakendus

**Fail:** `PetStoryApplication.java`

See on meie Spring Boot rakenduse sisenemispunkt:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Mis see teeb:**
- `@SpringBootApplication` annotatsioon lubab automaatse konfiguratsiooni ja komponentide skaneerimise
- Käivitab sisseehitatud veebiserveri (Tomcat) pordil 8080
- Loob automaatselt kõik vajalikud Spring bean’id ja teenused

### 2. Veebikontroller

**Fail:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| End-point | Päring | Edukas vastus |
| --- | --- | --- |
| `GET /` | Ilma kehata | HTML-üleslaadimisvorm CSRF tokeniga |
| `POST /analyze-image` | `multipart/form-data`, faili väli `image` | JSON: `{"description":"Mänguhimuline lemmikloom..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, väli `description` | HTML tulemusleht koos kirjelduse ja loodud looga |

Mõlemad POST lõpp-punktid nõuavad sessiooniküpsise ja CSRF tokeni, mis saadakse `GET /`. Üleslaadimisskript saadab peidetud `_csrf` väärtuse päises `X-CSRF-TOKEN`; loo esitamine saadab selle vormi väljana `_csrf`. API kliendid peavad säilitama küpsise päringute vahel. Need on vormipäringud, mitte JSON päringud.

Kirjeldused peavad olema mitte-tühjad ja kuni 1000 tähemärki. Kontroller lõikab kirjelduse ning eemaldab enne teenusele edastamist `<`, `>`, topeltjutumärgid, apostroofsõnad ja `&`. Tulemust mallil kuvatakse mudeli väljund turvaliselt `th:text` abil.

Pildi valideerimise ebaõnnestumised tagastavad HTTP 400 koos `error` väljadega; mudeli tõrked tagastavad HTTP 502 koos `error` väljadega ja ilma `description` väljata. Vigased kirjeldused või mudeli tõrked suunavad `/` lehele koos nähtava veateatega. Puuduvad nõutud väljad tagastavad HTTP 400 ja puuduvad või vigased CSRF tokenid HTTP 403. Edukate AI tulemustena ei esitata varukoopiaid kirjeldustest ega lugudest.

### 3. Lugude teenus

**Fail:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Ametlik OpenAI Java SDK 4.63.1 kutsub Azure AI Foundry OpenAI ühilduvat Chat Completions API-d. Azure Identity 1.18.6 tarnib Microsoft Entra pääsme `DefaultAzureCredential` kaudu; API-võtit pole vaja.

| Operatsioon | Sisend | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Pildi bajtid baasis64 andmeurlina koos üleslaaditud MIME-tüübiga | 300 |
| `generateStory` | Lemmiklooma kirjeldus kasutajateates | 800 |

Mõlemad päringud kasutavad konfigureeritud juurutust, vaikimisi `gpt-5.6-luna`, ja määravad selgesõnaliselt `ReasoningEffort.NONE` (`reasoning_effort: none`). Ükski päring ei saada `temperature` ega pärandatud `max_tokens` parameetrit.

Pildi analüüs aktsepteerib JPEG, PNG, GIF ja WebP, lükkab tagasi tühjad pildid ja üle 10MB failid ning piirab saadud kirjelduse 1000 tähemärgi piiresse. Loo prompt palub pere­sõbralikku lühilugu. Tühjad valikud või tühjad mudeli vastused on vead, mis säilitavad algse põhjuse serveripoolseks diagnostikaks. SDK klient sulgub rakenduse sulgemisel.

### 4. Veebimallid

**Fail:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Üleslaadimisvorm)

Leht algab foto valikuga, mitte tekstipiirkonnaga kirjelduseks. **Analüüsi pilt** eeltutvustab valitud foto ja postitab selle `/analyze-image`. Edukas vastus kuvab kirjelduse, täidab peidetud `description` välja ning paljastab **Genereeri lugu** nupu. See nupp esitab olemasoleva vormi `/generate-story`.

Brauseris mudeli allalaadimist ega CDN sõltuvust pole. Pildi analüüs töötab serveris Azure juurutuse kaudu. Tõrked jäävad nähtavaks ja ei võimalda loo genereerimist valemiis kirjelduse põhjal. Teise faili valimine tühjendab eelneva analüüsi.

**Fail:** `result.html` (Loo kuvamine)

Kuvab loodud loo:

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

**Malli omadused:**

1. **Thymeleafi integratsioon**: Kasutab `th:` atribuute dünaamilise sisu jaoks
2. **Reageeriv kujundus**: CSS stiil mobiilile ja lauaarvutile
3. **Vigade käsitlemine**: Kuvab valideerimisvead kasutajatele
4. **Üleslaadimise käsitlemine**: JavaScript eeltutvustab foto, saadab CSRF-ga kaitstud multipart päringu ja kuvab tagastatud kirjelduse

### 5. Konfiguratsioon

**Fail:** `application.properties`

Rakenduse konfiguratsiooni sätted:

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

**Konfiguratsiooni selgitus:**

1. **Faili üleslaadimine**: Faili ja kogu multipart päringu maksimaalne suurus on 10MB; hoidke fotod nende piiride all, et oleks ruumi multipart päistele
2. **Logimine**: Kontrollib, mis teave logitakse käivitamise ajal
3. **Azure AI Foundry**: Määrab kasutatava lõpp-punkti ja mudelijuurutuse (võtmepõhine autentimine puudub)
4. **Turvalisus**: CSRF kaitse on lubatud; mudeli diagnostika logitakse serveris, kontroller kuvab põhilisi mudelitõrgete teateid

## Rakenduse käivitamine

### 1. samm: Sisselogimine ja lõpp-punkti seadistamine

Autentimine on võtmepõhine (Microsoft Entra ID), seega API võtit pole. Logige sisse ja seadistage Foundry lõpp-punkt:

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

**Miks see on vajalik:**
- Azure AI Foundry kasutab Microsoft Entra ID autentimaks päringuid mudelile
- Võtmepõhine autentimine tähendab, et lähtekoodis või keskkonnas pole salajasi võtmeid
- Teie konto vajab ressursil rolli **Cognitive Services OpenAI User**

Vaikimisi juurutuse nimi on `gpt-5.6-luna`. Kui teie GPT-5.6 Luna juurutusel on teine nimi, seadistage sama terminalis enne rakenduse käivitamist `AZURE_OPENAI_DEPLOYMENT`. Nii pildi analüüs kui loo genereerimine kasutavad seda seadistust.

### 2. samm: Koostamine ja käivitamine

Minge projekti kataloogi:
```bash
cd 04-PracticalSamples/petstory
```

Koostage iseseisev käivitatav JAR ja käivitage kõik võrgust väljaspool testid:
```bash
mvn clean package
```

Käivitage server:
```bash
mvn spring-boot:run
```

Rakendus käivitub aadressil `http://localhost:8080`.

Või käivitage pakendatud JAR vabal pordil, näiteks:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Selle käsu puhul avage `http://localhost:8083/`. Samad `/analyze-image` ja `/generate-story` rajad on valitud pordil saadaval.

### 3. samm: Rakenduse testimine

1. **Avage** `http://localhost:8080` oma brauseris
2. **Valige** selge lemmiklooma foto JPEG, PNG, GIF või WebP vormingus, alla 10MB
3. **Klõpsake** "Analyze Image" ja oodake lemmiklooma kirjeldust
4. **Klõpsake** "Generate Story" pärast edukat analüüsi
5. **Vaadake** lugu ja kasutage tulemuslehe linki, et naasta üleslaadimisvormile

Edukas foto-loo voog teeb kaks mudelikõnet, ühe nupu võrra. Otsepäringud tarbivad juurutuse kvota ja võivad põhjustada kulusid; piiratud juurutuse jagamisel käitage põhilisi testid seeriaviisiliselt. Avalehe laadimine mudelit ei kutsu.

## Võrgust väljaspool testimine

Näidiskataloogist käivitage:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) jäädvustab reaalsete OpenAI SDK päringute loopback HTTP fikstuuri. Kontrollib päringute juurutust, `reasoning_effort: none`, tokeni limiite, pildiparameetrit, sisendi valideerimist, tühje vastuseid ja ülemise teenuse tõrkeid.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) kasutab MockMvc-d koos mudeli teenuse mociga, et testida Thymeleaf renderingut, üleslaadimislepingut, CSRF, valideerimist, väljundi turvamist ja nähtavaid tõrkeid. Need testid ei vaja Azure mandaatide ja ei kutsu tasulist Azure mudelit. Maven kirjutab Surefire raportid kataloogi `target/surefire-reports`.

## Kuidas see kõik töötab

Siin on täielik voog, kui genereerite lemmikloolooja abil loo:

1. **Foto valik**: valite lemmiklooma foto üleslaadimisvormil
2. **Pildi üleslaadimine**: "Analyze Image" saadab multipart POST päringu `/analyze-image` koos CSRF päisega
3. **Pildi analüüs**: `StoryService` saadab pildi GPT-5.6 Lunale koos `reasoning_effort` seatud `none` väärtusele
4. **Kirjelduse kuvamine**: brauser kuvab tagastatud kirjelduse ja salvestab selle vormi
5. **Loo esitamine**: "Generate Story" postitab `description` ja `_csrf` `/generate-story`
6. **Loo genereerimine**: kontroller valideerib kirjelduse ja kutsub sama juurutust reasoning` effort`-ita
7. **Malli renderdamine**: Thymeleaf turvab ja kuvab kirjelduse ning loo tulemuslehel

**Vigade käsitlemise voog:**
Kui mudel ebaõnnestub, logib server põhjuse. Pildi analüüs tagastab HTTP 502 ja brauser kuvab vea ilma "Generate Story" näitamiseta. Loo genereerimine suunab vormile veateatega. Ükski tee ei asenda vaikselt eelkirjutud tulemust.

## Tehisintellekti integreerimise mõistmine

### Azure AI Foundry (võtmepõhine autentimine puudub)
Teenus konfigureerib SDK teie ressursi `/openai/v1/` lõpp-punktiga. `DefaultAzureCredential` ja `AuthenticationUtil.getBearerTokenSupplier` tarnivad Microsoft Entra pääsme `https://ai.azure.com/.default` jaoks. Kohalik areng saab kasutada Azure CLI sisselogimist; Azure-is hostitud rakendus saab kasutada haldatud identiteeti vajalike õigustega ressursi jaoks.

### Prompt insenerimine
Pildi analüüs palub lühikeses lõigus kirjeldada täheldatavaid lemmiklooma omadusi ja käsib mudelil käsitleda pildil olevat teksti andmetena, mitte juhistena. Loo genereerimine kasutab tagastatud kirjeldust eraldi, pere­sõbralikus kirjutamise päringus. Ükski kõne ei luba reasoningut ega sea temperatuuriparameetrit.

### Vastuse töötlemine
Ühine vastuse töötleja lükkab tagasi puuduvad valikud ja tühjad või ainult tühikut sisaldavad sisud, lõikab kehtiva sisu ning säilitab ülemise tõrke. Pildikirjeldused on piiratud 1000 tähemärgiga, et need sobituksid loo vormi. Originaalne mudelitõrge säilitatakse diagnostikaks, kuid see ei kuvata kasutajale.

## Järgmised sammud

Lisadeks vaadake [4. peatükk: Praktilised näited](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades AI tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun pange tähele, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega seotud eksimustest või valesti mõistmistest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->