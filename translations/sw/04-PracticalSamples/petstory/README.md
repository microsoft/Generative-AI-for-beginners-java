# Mafunzo ya Kizazi cha Hadithi za Wanyama wa Kipenzi kwa Waanzilishi

Pakia picha ya mnyama wa kipenzi, ichanganue na GPT-5.6 Luna, na tengeneza hadithi kutokana na maelezo yanayotokana. Maombi yote ya mfano hutumia `reasoning_effort: none`.

| Sehemu | Toleo |
| --- | --- |
| Java | 21 au zaidi |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Jedwali la Yaliyomo

- [Mahitaji ya Awali](#mahitaji-ya-awali)
- [Kuelewa Muundo wa Mradi](#kuelewa-muundo-wa-mradi)
- [Ufafanuzi wa Sehemu Muhimu](#ufafanuzi-wa-sehemu-muhimu)
  - [1. Programu Kuu](#1-programu-kuu)
  - [2. Kidhibiti Tovuti](#2-kidhibiti-tovuti)
  - [3. Huduma ya Hadithi](#3-huduma-ya-hadithi)
  - [4. Violezo vya Tovuti](#4-violezo-vya-tovuti)
  - [5. Usanidi](#5-usanidi)
- [Kukimbia Programu](#kukimbia-programu)
- [Majaribio ya Nje ya Mtandao](#majaribio-ya-nje-ya-mtandao)
- [Jinsi Yote Hufanya Kazi Pamoja](#jinsi-yote-hufanya-kazi-pamoja)
- [Kuelewa Muungano wa AI](#kuelewa-muungano-wa-ai)
- [Hatua Zinazo Fuata](#hatua-zinazo-fuata)

## Mahitaji ya Awali

Kabla ya kuanza, hakikisha una:
- Java 21 au zaidi imewekwa
- Maven kwa usimamizi wa utegemezi
- Utekelezaji wa Azure AI Foundry wa GPT-5.6 Luna unaoitwa `gpt-5.6-luna`, au toleo la `AZURE_OPENAI_DEPLOYMENT` linaloelekeza kwa utekelezaji huo. Angalia [Sura ya 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) kwa utoaji na ingia kwa kutumia `az login` kwa uthibitishaji bila funguo. Utekelezaji lazima uunge mkono ingizo la picha na `reasoning_effort: none`.
- Uelewa wa msingi wa Java, Spring Boot, na maendeleo ya mtandao

## Kuelewa Muundo wa Mradi

Mradi wa hadithi za wanyama wa kipenzi una faili kadhaa muhimu:

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

## Ufafanuzi wa Sehemu Muhimu

### 1. Programu Kuu

**Faili:** `PetStoryApplication.java`

Huu ni mlango wa kuingia wa programu yetu ya Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Hili hufanya nini:**
- Kiambatisho cha `@SpringBootApplication` kinawezesha usanidi wa moja kwa moja na kusaka sehemu
- Huanza seva ya wavuti iliyojengwa ndani (Tomcat) kwenye bandari 8080
- Huunda vitambaa vyote na huduma za Spring kwa njia ya moja kwa moja

### 2. Kidhibiti Tovuti

**Faili:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Kituo cha mwisho | Ombi | Jibu lenye mafanikio |
| --- | --- | --- |
| `GET /` | Hakuna mwili | Fomu ya kupakia ya HTML yenye tokeni ya CSRF |
| `POST /analyze-image` | `multipart/form-data`, sehemu ya faili `image` | JSON: `{"description":"Mnyama wa kucheza..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, sehemu `description` | Ukurasa wa matokeo wa HTML wenye maelezo na hadithi iliyotengenezwa |

Sehemu zote za POST zinahitaji keki ya kikao na tokeni ya CSRF iliyopewa kutoka `GET /`. Skripti ya kupakia hutuma thamani iliyofichwa ya `_csrf` kwenye kichwa cha `X-CSRF-TOKEN`; usambazaji wa hadithi hutumia kama sehemu ya fomu ya `_csrf`. Wateja wa API lazima wahifadhi keki kati ya maombi. Hizi ni sehemu za fomu, si sehemu za maombi ya JSON.

Maelezo lazima yasikosewe na yasizidi herufi 1000. Kidhibiti hugata maelezo na kuondoa `<`, `>`, alama za nukuu mbili, alama za nukuu moja, na `&` kabla ya kuyatuma kwa huduma. Kiolezo cha matokeo pia hugandisha matokeo ya mfano kwa kutumia `th:text`.

Kushindwa kuthibitisha picha hurudisha HTTP 400 na sehemu ya `error`; kushindwa kwa mfano hurudisha HTTP 502 na sehemu ya `error` bila `description`. Maelezo ya hadithi yasiyo halali au kushindwa kwa mfano hupeleka tena `/` na kosa linaloonekana. Sehemu zilizohitaji zinazokosekana hurudisha HTTP 400, na tokeni za CSRF zilizokosekana au batili hurudisha HTTP 403. Hakuna maelezo ya kivunjika au hadithi huonyeshwa kama matokeo ya AI yenye mafanikio.

### 3. Huduma ya Hadithi

**Faili:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

SDK rasmi ya Java ya OpenAI 4.63.1 huwasiliana na API ya Chat Completions inayolingana na OpenAI ya Azure AI Foundry. Azure Identity 1.18.6 hutumia tokeni ya Microsoft Entra kupitia `DefaultAzureCredential`; hakuna funguo la API linalohitajika.

| Operesheni | Ingizo | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bytes za picha zilizochapishwa kama URL ya data ya base64 na aina ya MIME iliyopakiwa | 300 |
| `generateStory` | Maelezo ya mnyama wa kipenzi katika ujumbe wa mtumiaji | 800 |

Maombi yote hutumia utekelezaji uliowekwa, kwa kawaida `gpt-5.6-luna`, na kwa wazi kuweka `ReasoningEffort.NONE` (`reasoning_effort: none`). Hakuna maombi yanayotuma `temperature` au vigezo vya zamani vya `max_tokens`.

Uchambuzi wa picha unakubali JPEG, PNG, GIF, na WebP, hukataa picha tupu na faili zenye ukubwa zaidi ya 10MB, na huweka maelezo yanayotokana yasizidi herufi 1000. Omba la hadithi linahitaji hadithi fupi inayofaa familia. Chaguo tupu au maudhui ya mfano tupu ni makosa, na kushindwa huhifadhi sababu ya awali kwa uchunguzi wa seva. Mteja wa SDK huzimwa wakati programu inakufa.

### 4. Violezo vya Tovuti

**Faili:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Fomu ya Kupakia)

Ukurasa unaanza na chaguo la picha, si eneo la maandishi ya maelezo. **Chambua Picha** huonyesha awali picha iliyochaguliwa na kuituma kwa `/analyze-image`. Jibu lenye mafanikio linaonyesha maelezo, limemaliza sehemu ya `description` iliyofichwa, na huonyesha **Tengeneza Hadithi**. Kitufe hicho hutuma fomu iliyopo kwa `/generate-story`.

Hakuna pakizi ya mfano wa kivinjari au utegemezi wa CDN. Uchambuzi wa picha hufanyika kwenye seva kupitia utekelezaji wa Azure uliowekwa. Kushindwa huonekana na hakuwezeshi utengenezaji wa hadithi na maelezo yaliyotengenezwa. Kuchagua faili tofauti hufuta uchambuzi uliopita.

**Faili:** `result.html` (Onyesho la Hadithi)

Inaonyesha hadithi iliyotengenezwa:

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

**Sifa za kiolezo:**

1. **Ushirikiano na Thymeleaf**: Inatumia sifa za `th:` kwa maudhui yanayobadilika
2. **Mchoro Unaojibadilisha**: Mtindo wa CSS kwa simu za mkononi na kompyuta
3. **Ushughulikiaji wa Makosa**: Inaonyesha makosa ya uthibitishaji kwa watumiaji
4. **Ushughulikiaji wa Kupakia**: JavaScript inaonyesha awali ya picha, inatuma ombi la multipart lililohifadhiwa dhidi ya CSRF, na inaonyesha maelezo yaliyorejeshwa

### 5. Usanidi

**Faili:** `application.properties`

Mipangilio ya usanidi kwa programu:

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

**Ufafanuzi wa usanidi:**

1. **Upakiaji wa Faili**: Faili zote na ombi zima la multipart hupunguzwa hadi 10MB; weka picha chini ya kikomo hicho ili kuwa na nafasi kwa kichwa cha multipart
2. **Ufuatiliaji**: Hudhibiti taarifa zinazoandikwa wakati wa utekelezaji
3. **Azure AI Foundry**: Huweka kiungo na utekelezaji wa mfano wa kutumia (uthibitishaji bila funguo)
4. **Usalama**: Ulinzi wa CSRF unabaki ukiwa hai; uchunguzi wa mfano unaandikwa kwenye seva, huku kidhibiti kinaonyesha ujumbe wa makosa ya mfano kwa jumla

## Kukimbia Programu

### Hatua ya 1: Ingia na Weka Kiungo Chako

Uthibitishaji haufungi funguo (Microsoft Entra ID), hivyo hakuna funguo la API. Ingia na weka kiungo cha Foundry:

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

**Kwa nini hii inahitajika:**
- Azure AI Foundry hutumia Microsoft Entra ID kuthibitisha maombi ya inferensi
- Uthibitishaji bila funguo maana yake hakuna siri katika kanuni zako au mazingira
- Akaunti yako inahitaji nafasi ya **Cognitive Services OpenAI User** kwenye rasilimali

Jina la utekelezaji wa kawaida ni `gpt-5.6-luna`. Ikiwa utekelezaji wako wa GPT-5.6 Luna una jina jingine, weka `AZURE_OPENAI_DEPLOYMENT` kwenye terminal ile ile kabla ya kuanzisha programu. Uchambuzi wa picha na utengenezaji wa hadithi hutumia mpangilio huu.

### Hatua ya 2: Jenga na Endesha

Elekea kwenye saraka ya mradi:
```bash
cd 04-PracticalSamples/petstory
```

Tengeneza jarida huru la JAR na endesha majaribio yote ya nje ya mtandao:
```bash
mvn clean package
```

Anzisha seva:
```bash
mvn spring-boot:run
```

Programu itaenda kuanza kwenye `http://localhost:8080`.

Mbali na hilo, anzisha JAR iliyobinadariwa kwenye bandari huru, kwa mfano:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Kwa amri hiyo, fungua `http://localhost:8083/`. Njia za `/analyze-image` na `/generate-story` zinapatikana kwenye bandari iliyochaguliwa.

### Hatua ya 3: Jaribu Programu

1. **Fungua** `http://localhost:8080` kwenye kivinjari chako
2. **Chagua** picha wazi ya mnyama wa kipenzi katika muundo wa JPEG, PNG, GIF, au WebP, chini ya 10MB
3. **Bonyeza** "Analyze Image" na subiri maelezo ya mnyama
4. **Bonyeza** "Generate Story" baada ya uchambuzi kufanyika kwa mafanikio
5. **Tazama** hadithi na tumia kiungo cha ukurasa wa matokeo kurudi kwenye fomu ya kupakia

Mtiririko wa picha-kwa-hadithi una mafanikio hufanya maombi mawili ya mfano, moja kwa kila kitufe. Inferensi ya moja kwa moja hutumia msamaha wa utekelezaji wako na inaweza kusababisha malipo; fanya majaribio ya haraka kwa mfululizo wakati wa kugawana utekelezaji ulio na kikomo cha kiwango. Kupakia ukurasa wa nyumba hakuiti mfano.

## Majaribio ya Nje ya Mtandao

Kutoka kwa saraka ya sampuli, endesha:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) hutumia maombi halisi ya SDK ya OpenAI kwa kifaa cha HTTP cha mzunguko wa nyuma. Inakagua utekelezaji wa maombi yote mawili, `reasoning_effort: none`, vizingiti vya tokeni, mzigo wa picha, uthibitishaji wa ingizo, majibu tupu, na makosa ya juu.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) hutumia MockMvc na huduma ya mfano iliyopigwa simu kujaribu kurasa za Thymeleaf zilizochorwa, mkataba wa kupakia, CSRF, uthibitishaji, usagaji wa matokeo, na kushindwa kuonekana. Majaribio haya hayaitegi cheti cha Azure na hayaweziita inferensi ya Azure iliyolipishwa. Maven hutoa ripoti za Surefire chini ya `target/surefire-reports`.

## Jinsi Yote Hufanya Kazi Pamoja

Huu ni mtiririko kamili wakati unapotengeneza hadithi ya mnyama wa kipenzi:

1. **Uchaguzi wa Picha**: Unachagua picha ya mnyama wa kipenzi kwenye fomu ya kupakia
2. **Ukipakia Picha**: "Analyze Image" hutuma ombi la multipart POST kwa `/analyze-image` na kichwa cha CSRF
3. **Uchambuzi wa Picha**: `StoryService` hutuma picha kwa GPT-5.6 Luna na reasoning imewekwa `none`
4. **Onyesho la Maelezo**: Kivinjari kinaonyesha maelezo yaliyorejeshwa na kuyahifadhi kwenye fomu
5. **Usambazaji wa Hadithi**: "Generate Story" hutuma `description` na `_csrf` kwa `/generate-story`
6. **Utengenezaji wa Hadithi**: Kidhibiti kinathibitisha maelezo na kuitisha utekelezaji ule ule na reasoning imewekwa `none`
7. **Uchoraji wa Kiolezo**: Thymeleaf hugandisha na kuonyesha maelezo na hadithi kwenye ukurasa wa matokeo

**Mtiririko wa Ushughulikiaji wa Makosa:**
Ikiwa mfano unashindwa, seva inaandika sababu. Uchambuzi wa picha hurudisha HTTP 502 na kivinjari kinaonyesha kosa bila kuonyesha "Generate Story". Utengenezaji wa hadithi hurejeleza kwenye fomu na ujumbe wa kosa. Njia yoyote haisemi matokeo yaliyotayarishwa mapema kimya.

## Kuelewa Muungano wa AI

### Azure AI Foundry (bila funguo)
Huduma huweka SDK na kiungo cha `/openai/v1/` cha rasilimali yako. `DefaultAzureCredential` na `AuthenticationUtil.getBearerTokenSupplier` hutoa tokeni za Microsoft Entra kwa `https://ai.azure.com/.default`. Maendeleo ya ndani yanaweza kutumia ingia yako ya Azure CLI; programu inayoorodheshwa kwenye Azure inaweza kutumia kitambulisho kilichosimamiwa na ruhusa muhimu za rasilimali.

### Uhandisi wa Ombi
Uchambuzi wa picha unadai sifa zinazoweza kuonekana za mnyama wa kipenzi katika aya fupi na lugha mfano atuchukue maandishi ya picha kama data, si maagizo. Utengenezaji wa hadithi hutumia maelezo yaliyorejeshwa katika ombi tofauti, salama kwa familia. Hakuna maombi yanayotumia reasoning au kubadilisha joto.

### Usindikaji wa Majibu
Msimamizi wa majibu wa pamoja hukataa chaguo zilizokosekana na maudhui tupu au yaliyomo kwa nafasi tu, hugata maudhui halali, na huhifadhi makosa ya juu. Maelezo ya picha hugawanyika hadi herufi 1000 ili kufaa fomu ya hadithi iliyofuata. Kosa la awali la mfano huhifadhiwa kwa uchunguzi lakini halionyeshwi kwa mtumiaji.

## Hatua Zinazo Fuata

Kwa mifano zaidi, ona [Sura ya 04: Sampuli za Kivitendo](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kionyozo**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati ya asili katika lugha yake halisi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatutojibu kwa kuelewa vibaya au tafsiri potofu zinazotokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->