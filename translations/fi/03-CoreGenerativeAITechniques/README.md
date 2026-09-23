# Keskeiset Generatiivisen tekoälyn tekniikat -opas

## Sisällysluettelo

- [Esivaatimukset](#esivaatimukset)
- [Aloitus](#aloitus)
- [Mallin valintaopas](#mallin-valintaopas)
- [Opetus 1: LLM-päätteet ja chat](#opetus-1-llm-päätteet-ja-chat)
- [Opetus 2: Funktiokutsu](#opetus-2-funktiokutsu)
- [Opetus 3: RAG (hakuun perustuva generointi)](#opetus-3-rag-hakuun-perustuva-generointi)
- [Opetus 4: Vastuullinen tekoäly](#opetus-4-vastuullinen-tekoäly)
- [Yleisiä malleja esimerkeissä](#yleisiä-malleja-esimerkeissä)
- [Yksikkötestit](#yksikkötestit)
- [Peräkkäinen reaaliaikavarmistus](#peräkkäinen-reaaliaikavarmistus)
- [Vianetsintä](#vianetsintä)
- [Seuraavat vaiheet](#seuraavat-vaiheet)

## Yleiskatsaus

Neljä erillistä Java-ohjelmaa demonstroivat chatin, keskusteluhistorian, funktiokutsun, koko dokumentin hakuun perustuvan generoinnin (RAG) ja vastuullisen tekoälyn vastauskäsittelyn. Kaikki chatpyynnöt kohdistuvat oletuksena **GPT-5.6 Lunaan ilman perustelupyrkimystä (`reasoning effort none`)**.

Nämä esimerkit käyttävät virallista OpenAI Java SDK:ta Azure OpenAI:n v1-päätepisteellä Microsoftin SDK-ohjeiden mukaisesti [Microsoftin SDK-ohje](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Vanha `azure-ai-openai`-paketti ei enää ole riippuvuus. Chat completions -toimintoa pidetään mukana opettaakseen olemassa olevia viestipohjaisia työnkulkuja; katso [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) muita API-vaihtoehtoja varten.

## Esivaatimukset

- Java 21 tai uudempi ja Maven 3.6.3 tai uudempi.
- Azure OpenAI chat -asennus nimeltä `gpt-5.6-luna` tai yhteensopiva Chat Completions -asetuksilla korvaava asennus.
- Kirjautunut Azure-tunnus, jolla on **Cognitive Services OpenAI User** -rooli resurssissa. Paikallisessa kehityksessä käytetään Azure CLI -kirjautumista; isännöidyissä sovelluksissa voi käyttää hallittua identiteettiä.
- Katso [Luku 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) resurssin asetusta ja kirjautumisohjeita varten.

[Maven-konfiguraatio](../../../03-CoreGenerativeAITechniques/examples/pom.xml) lukitsee nämä versiot, tarkastettu 2026-09-14:

| Komponentti | Versio | Tarkoitus |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Virallinen Azure v1 -yhteensopiva asiakas |
| `com.azure:azure-identity` | 1.18.6 | Avaineton autentikointi ja token-päivitys |
| `net.objecthunter:exp4j` | 0.4.8 | Aritmeettisten lausekkeiden jäsentäminen ilman koodin suorittamista |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter -yksikkötestit |
| Maven-kääntäjä / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 käännös, testit, suoritettavat esimerkit |

Kääntäjä käyttää `--release 21` -asetusta. Nämä itsenäiset esimerkit eivät tarvitse Spring Bootia, Spring AI:ta tai LangChain4j-riippuvuuksia.

## Aloitus

Aseta resurssin päätepiste ja tarvittaessa asennuksen ylikirjoitus juurihakemistosta kuoreen.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Testit eivät tarvitse Azure-tunnuksia tai päätepistettä. Maven ei lue automaattisesti ympäristömuuttujatiedostoa; aseta muuttujat kuoreen, jolla käynnistät live-esimerkit. IDE-käynnistyksissä tarkista käyttämäsi käynnistyskonfiguraation ympäristöasetukset.

## Mallin valintaopas

| Ympäristömuuttuja | Merkitys | Oletus |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS-osoitteinen Azure-resurssin juuri tai valmiiksi normalisoitu `/openai/v1`-URL | Vaaditaan live-suorituksissa |
| `AZURE_OPENAI_DEPLOYMENT` | Chat-asennuksen nimi, ei malliversio | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Eri asennus upotuksia varten, ei käytössä näissä neljässä ohjelmassa | `text-embedding-3-small` |

Tyhjät asennuksen ylikirjoitukset käyttävät oletuksia. Konfiguraatio lisää `/openai/v1` -polun tasan kerran ja hylkää tunnukset, kyselymerkkijonot ja perinteiset asennuspolut päätepisteessä.

Jokaiseen chat-pyyntöön asetetaan eksplisiittisesti `reasoningEffort(ReasoningEffort.NONE)` ja `maxCompletionTokens(...)`. Kukaan pyyntö ei anna arvoa `temperature`, `top_p` tai perinteiselle päätteiden token-asetukselle. Tämä koskee myös työkalujen valintaa ja tulostapahtumien jatkoja. GPT-5.6 Chat Completions -toimintotyökalut vaativat perustelupyrkimyksen `none`; katso [Microsoftin chat-ohje](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Tässä luvussa ei ole suoratoisto- tai upotusliitäntää.** Lukija hakee koko dokumenttinsa, ei vektoreita. Jos lisäät upotuksia, käytä erillistä upotusasennusta kuten `text-embedding-3-small`, älä koskaan Lunaa.

## Opetus 1: LLM-päätteet ja chat

Lähde: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Ohjelma suorittaa yksinkertaisen Java streams -selityksen, kahden vaiheen HashMap/TreeMap-keskustelun, ja interaktiivisen chatin. Toinen vaihe sisältää ensimmäisen avustajan vastauksen; jokainen interaktiivinen vaihe lähettää myös aiemman keskustelunsa.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` antaa asennuksen ja eksplisiittisen perusteluasetuksen. Interaktiivinen chat ohittaa tyhjät rivit, päättyy `exit`-komentoon tai tiedoston loppuun ja säilyttää järjestelmäviestin sekä yhdeksän valmista käyttäjä/avustajavaihdosta. Vaiheiden rajoittaminen on opetusmääräys, ei tarkka token-budjetin takuu.

Esimerkkihakemistosta:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Odota kolme alkuaikaista vastausta, sitten `You:`-kehotetta. Jokainen ei-tyhjä kysymys lisää yhden pyynnön. Päätteiden rajat ovat 200, 300, 400 ja lopulta 500 tokenia per interaktiivinen vaihe.

## Opetus 2: Funktiokutsu

Lähde: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK muodostaa JSON-skeemat merkityistä `WeatherArguments` ja `CalculationArguments`-tietueista. Pakollinen työkalun valinta ohjaa jokaista esimerkkiä käyttämään työkaluprotokollaa mallin avustamattoman vastauksen sijaan.

1. Lähetä kysymys sallitulla työkalulla, perustelu pyrkimys `none`, ja 300 tokenin rajalla.
2. Vaadi `tool_calls` päättymisperustetta, validoi funktio nimi ja kutsu-ID:t, ja jäsennä tyypitetyt JSON-argumentit.
3. Suorita paikallinen funktio. Malli ei suorita Java- tai muuta mielivaltaista koodia.
4. Lisää avustajan työkalukutsuviesti kerran, sitten tulokset jokaisella vastaavalla `tool_call_id`:llä.
5. Lähetä yksi lopullinen 300 tokenin pyyntö ilman työkaluja ja vaadi täytetty, ei-tyhjä vastaus.

`get_weather` palauttaa **simuloidun**, ei reaaliaikaisen, sään. Se huomioi kaupungin ja muuntaa esimerkkilämpötilan 22 Celsius-astetta Fahrenheitiksi pyydettäessä. `calculate` arvioi lausekkeen exp4j:llä, tukee muotoja kuten `15% of 240` ja `2 + 3 * 4`, ja hylkää tyhjät, liian suuret, virheelliset tai ei-pätevät laskut. Se käyttää liukulukulaskuja, ei rahoituksen desimaalien tarkkuutta.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Odota `Function: get_weather`, simuloitu Seattle-sää, `Function: calculate`, `Function result: 36`, ja kaksi lopullista vastausta. Ei stdin:ää tai ulkoisia säätunnuksia tarvita. Onnistunut suoritus käyttää täsmälleen neljää chat-pyyntöä.

## Opetus 3: RAG (hakuun perustuva generointi)

Lähde: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Syöte: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Tämä aloittelijan RAG-esimerkki hakee yhden kokonaisen UTF-8-dokumentin ja sisällyttää sen käyttäjäviestiin kysymyksen kanssa. Erillinen järjestelmäviesti ohjeistaa mallia käsittelemään dokumentin sisältö epäluotettavana tietona ja vastaamaan vain tuon kontekstin perusteella. Jos dokumentista ei löydy vastausta, pyydetty vastaus on: `En löydä kyseistä tietoa annetusta dokumentista.`

Perustelu voi vähentää harhaluentoja, mutta rajaukset tai järjestelmäohjeet eivät takaa tarkkuutta tai estä kaikkia kehotepohjaisia hyökkäyksiä. Tarkista live-vastaukset. Tuotannossa RAG lisää yleensä palojen pilkkomisen, haun, lähteiden merkinnät, käyttöoikeuden valvonnan ja arvioinnin.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Anna yksi kysymys, esimerkiksi `Mitä todennustapaa dokumentti kuvaa?`. Odota vastaus, joka mainitsee Microsoft Entra ID:n. Ohjelma lopettaa yhdellä chat-pyynnöllä, jonka päätteiden raja on 500 tokenia.

Oletustiedostohaku toimii arkiston juuressa, lukuhakemistossa tai esimerkeissä. Myös eksplisiittinen polku on tuettu:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Syötteet eivät saa olla tyhjiä: enintään 32 KiB UTF-8-dokumenttitietoa ja 2000 kysymysmerkkiä. Puuttuvat tiedostot, tyhjät/EOF-kysymykset ja liian suuret syötteet epäonnistuvat ennen päättelyä.

## Opetus 4: Vastuullinen tekoäly

Lähde: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Kuusi koeajoa käsittelevät haitallisia ohjeita, vihapuhetta, yksityisyyttä, lääketieteellistä disinformaatiota, laitonta sisältöä ja hyvänlaatuisen vastuullisen tekoälyn kysymyksen. Ohjelma tarkkailee vastauksia eikä vaadi kaikkien koejyvien laukaisua.

| Tulos | Todiste |
| --- | --- |
| `FILTERED` | Eksplisiittinen `content_filter` / `ResponsibleAIPolicyViolation` -virhekoodi tai päätteessä `content_filter` -päättymisperuste |
| `REFUSED` | Ei-tyhjä rakenteellinen `message.refusal`-kenttä |
| `POSSIBLE_REFUSAL` | Alkava kieltäytymislause tavanomaisessa tekstissä; heuristinen merkki, joka vaatii tarkistusta |
| `GENERATED` | Valmis, ei-tyhjä vastaus; ei todiste sisällön turvallisuudesta |

Tavallinen HTTP 400 EI OLE todiste suodatuksesta. Virheelliset parametrit, todennusvirheet, nopeusrajat, palvelinvirheet, väärinmuodostetut vastaukset ja katkennut tuloste epäonnistuvat eikä luo väärää turvallisuushyvkäystä. Laajat termit kuten "haitallinen sisältö" hyvänlaatuisessa selityksessä eivät ole kieltäytymistä.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Odota kuusi kategoriatulosta ja yhteenveto, jossa todetaan, ettei havainnoista saa turvallisuussertifikaattia. Jokaisen koejyvän päätteiden raja on 300 tokenia. Tarkista odottamattomat vastaukset ja mahdolliset kieltäytymiset käsin; hyväntahtoinen vertailu tuottaa asiaankuuluvan vastuullisen tekoälyn selityksen. Stdin:iä ei tarvita.

## Yleisiä malleja esimerkeissä

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) keskittää päätepisteen normalisoinnin, asennuksen ylikirjoitukset, avaintomattoman autentikoinnin ja chat-asetukset:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Tokenien hankkija päivittää käyttöoikeustokenit tarpeen mukaan. Älä lokita tokeneita äläkä korvaa niitä API-avaimella. Kukin ohjelma käyttää uudelleen omaa asiakastaan ja sulkee sen `finally`-lohkossa tai oman `AutoCloseable`-kääreen kautta; SDK:n `OpenAIClient` ei itse ole `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) vaatii valmiin, ei-tyhjän tekstivastauksen. Tyhjät vaihtoehdot, kieltäytymiset, suodattimet ja rajatut vastaukset eivät tulostu hiljaisesti onnistumisena. Vastuullisen tekoälyn esimerkki käsittelee vaaditut suodatin/kieltäytymis-tulokset eksplisiittisesti. Käsittelemättömät virheet aiheuttavat Java/Maven-prosessille nollasta poikkeavan poistumiskoodin.

**SDK:n automaattiset uudelleenyritykset on pois päältä** pyynnön laskennan ennustettavuuden vuoksi jaettujen matalan RPM:n asennusten kohdalla. Jokaisella päättelypyynnöllä on 60 sekunnin aikakatkaisu. Tokenien hankinta voi kestää pidempään. Sovellustason ajoituksen on noudatettava kiintiöitä; älä automaattisesti uudelleenyrity pahoiteltua maksullista pyyntöä.

## Yksikkötestit

Esimerkeistä:

```powershell
mvn -B -ntp clean test
```

Testikuljetus korvaa SDK:n HTTP-kerroksen kokonaan, tallentaa todelliset sarjatut pyyntöjen rungot ja tarjoaa jonotetut vastaukset. Se ei avaa mitään yhteyksiä, hanki Azure-tokeneita eikä hyväksy odottamattomia pyyntöjä. Nämä testit validoivat sovellustason käyttäytymistä ja SDK:n protokollaa, eivät live-mallin laatua tai asennuksen saatavuutta.

| Testipaketti | Kattavuus |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Päätepisteen normalisointi/hylkäys, asennuksen ylikirjoitus, perustelun ja tokenien asetukset |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Kaikki päätteiden työnkulut, viestihistoria, vaihelaskenta, EOF, virheet |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Työkaluskeemat, tyypitetyt argumentit, aritmetiikka, ID:t, useat työkalutulokset, epäonnistuneet jatkopyynnöt |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Tiedostohaku, UTF-8, kokorajoitukset, perustelu, syöte- ja API-virheet |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Kaikki kuusi koejyvää, eksplisiittiset suodattimet, kieltäytymisten luokitus, tavalliset 400-virheet ja muut |

Yhden paketin suorittamiseen: `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Jaetut testivarusteet sijaitsevat [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Peräkkäinen reaaliaikavarmistus

Live-kutsut ovat erillisiä yksikkötesteistä. Käytä seuraavia komentoja **yksittäin** arkiston juurista vain, kun tunnukset ja asennusoikeudet ovat valmiina. Ei tarvita palveluita tai pysyviä prosesseja.

Jaetussa **10 pyynnön/minuutti** asennuksessa varaa tarpeeksi kiintiötä koko seuraavaan ohjelmaan ennen käynnistystä: 5, 4, 1, sitten 6 pyyntöä. Peräkkäiset prosessit eivät yksin takaa nopeusrajan noudattamista. Koordinoi kulkeva minuutti kaikkien muiden kutsujien kanssa; älä liitä neljää kutsua yhtenä mitoitettuna eränä.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Päätteet, monivaiheiset ja kaksi interaktiivista vaihetta:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Tarkista kaikki kolme osion otsikkoa, viisi vastausta, lopullinen vuorovaikutteinen vastaus, jossa muistellaan Adaa, `Goodbye!` ja poistumiskoodi 0. Budjetti: **5 pyyntöä, enintään 1900 valmistumissanaa**. Pienempään ajoon putkita vain `exit`: 3 pyyntöä / 900 sanaa, mutta se ei harjoita vuorovaikutteista päättelyä.

**2. Molemmat funktiokutsutyönkulut:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Tarkista molemmat funktioiden nimet, simuloitu Seattlen sää, laskettu tulos 36, kaksi lopullista vastausta ja poistumiskoodi 0. Budjetti: **4 pyyntöä, enintään 1200 valmistumissanaa**.

**3. Dokumenttipohjainen vastaus:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Tarkista dokumentin polku, vastaus, jossa mainitaan Microsoft Entra ID, ja poistumiskoodi 0. Budjetti: **1 pyyntö, enintään 500 valmistumissanaa**. Käytettävissä oleva [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) on ainoa vaadittu syötetiedosto. Vapaaehtoinen toinen ajo, jossa kysytään puuttuvasta aiheesta, tulee pidättäytyä ja lisää yhden pyynnön / 500 sanaa.

**4. Vastuullisen tekoälyn havainnot:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Tarkista kuusi kategoriaa ja havaintoyhteenveto, tarkista luotu sisältö ja vaadi poistumiskoodi 0 teknisen suorituksen varmistamiseksi. Onnistunut prosessin poistuminen ei takaa mallin turvallisuutta. Budjetti: **6 pyyntöä, enintään 1800 valmistumissanaa**.

**Yhteensä neljälle komennolle: 16 chat-pyyntöä ja enintään 5400 valmistumissanaa**, sekä syötesanat (sisältäen toistetun keskustelun ja työkalukaavion/historian). Upotuspyrkimyksiä ei ole ollenkaan. Todellinen sanankäyttö riippuu mallista ja voi olla pienempi, erityisesti suodatettujen kehotteiden tapauksessa. Dollarikustannukset riippuvat käyttöönoton hinnoittelusta; kiinteää rahallista arviota ei anneta. Kaikki pyyntörajoitukset edellyttävät, ettei manuaalisia toistoja ole. Tarkista `$LASTEXITCODE` heti jokaisen komennon jälkeen; nollasta poikkeava tarkoittaa, ettei ajo onnistunut täydellisesti.

## Vianetsintä

- **Puutteellinen päätepiste / 401 / 403:** Aseta päätepiste käynnistysprosessiin, varmista paikallinen Azure-kirjautuminen ja resurssikohtainen rooli sekä tarkista ei-toivotut identiteetin ympäristöylikirjoitukset.
- **400 / 404:** Varmista, että käyttöönotto on olemassa ja tukee Chat Completioneja ilman päättelyä (`none`). Käytä HTTPS-resurssin juurta tai `/openai/v1` -URL:ää, ei vanhentunutta käyttöönotto-URL:ää. Tavalliset 400-virheet ovat teknisiä vikoja, eivät turvallisuusblokkeja.
- **429:** Koordinoi jaettu RPM ja tokenkiquota ennen uudelleenyrittämistä. Esimerkeissä ei ole automaattista uudelleenyrittämistä.
- **`Kesken jäänyt chat-vastaus: pituus`:** Tulostus on saavuttanut valmistumisrajan. Tarkista vastaus ja kehotus ennen rajan ja siihen dokumentoidun budjetin nostamista; älä merkitse katkaistua ajoa onnistuneeksi.
- **Tiedosto- tai stdin-virheet:** Käynnistä tuetusta hakemistosta tai anna selkeä dokumentin polku. Anna ei-tyhjä lukijakysymys. Valmistumiset voivat loppua normaalisti EOF:ään tai `exit`-komentoon.
- **Käännösvirheet:** Varmista Java 21 tai uudempi, aja `mvn -B -ntp clean test`. PowerShellissä lainaa koko Maven-parametri, joka sisältää pisteellä erotetun ominaisuuden, esimerkiksi `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Seuraavat vaiheet

Jatka [Luku 4: Käytännön esimerkit](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->