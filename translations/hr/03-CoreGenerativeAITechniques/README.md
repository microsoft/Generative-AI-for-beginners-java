# Vodič za osnovne tehnike generativne umjetne inteligencije

## Sadržaj

- [Preduvjeti](#preduvjeti)
- [Početak](#početak)
- [Vodič za odabir modela](#vodič-za-odabir-modela)
- [Vodič 1: LLM dovršetci i chat](#vodič-1-llm-dovršetci-i-chat)
- [Vodič 2: Pozivanje funkcija](#vodič-2-pozivanje-funkcija)
- [Vodič 3: RAG (generacija uz nadopunu pretraživanjem)](#vodič-3-rag-generacija-uz-nadopunu-pretraživanjem)
- [Vodič 4: Odgovorno AI](#vodič-4-odgovorno-ai)
- [Uobičajeni obrasci u primjerima](#uobičajeni-obrasci-u-primjerima)
- [Jedinični testovi](#jedinični-testovi)
- [Sekvencijalna provjera uživo](#sekvencijalna-provjera-uživo)
- [Rješavanje problema](#rješavanje-problema)
- [Sljedeći koraci](#sljedeći-koraci)

## Pregled

Četiri samostalna Java programa demonstriraju chat, povijest razgovora, pozivanje funkcija, RAG (generaciju s nadopunom pretraživanjem čitavog dokumenta) i odgovorno upravljanje odgovorima AI-a. Sve chat zahtjeve prema zadanim postavkama usmjeravaju se na **GPT-5.6 Luna s naporom rezoniranja `none`**.

Ovi primjeri koriste službeni OpenAI Java SDK s Azure OpenAI v1 krajnjom točkom, slijedeći [Microsoftove smjernice za SDK](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Stari paket `azure-ai-openai` više nije potreban. Chat dovršeci se zadržavaju radi poučavanja postojećih tijekova rada baziranih na porukama; za druge API opcije vidi [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure).

## Preduvjeti

- Java 21 ili noviji i Maven 3.6.3 ili noviji.
- Azure OpenAI chat implementacija nazvana `gpt-5.6-luna`, ili preklapanje s kompatibilnim postavkama Chat dovršetaka.
- Prijavljena Azure identifikacija s ulogom **Cognitive Services OpenAI User** na resursu. Lokalni razvoj koristi vašu Azure CLI prijavu; hostirane aplikacije mogu koristiti upravljani identitet.
- Vidi [Poglavlje 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) za upute o postavljanju resursa i prijavi.

[Maven konfiguracija](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fiksira ove verzije, provjerene 2026-09-14:

| Komponenta | Verzija | Namjena |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Službeni Azure v1-kompatibilni klijent |
| `com.azure:azure-identity` | 1.18.6 | Autentifikacija bez ključa i osvježavanje tokena |
| `net.objecthunter:exp4j` | 0.4.8 | Parsiranje aritmetičkih izraza bez izvođenja koda |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter jedinični testovi |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Kompajliranje Java 21, testovi, pokretni primjeri |

Kompajler koristi `--release 21`. Za ove samostalne primjere nije potrebna ovisnost o Spring Boot, Spring AI ili LangChain4j.

## Početak

Iz korijena repozitorija postavite krajnju točku resursa i opcionalnu primjenu preklapanja u vašem ljusci.

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

Testovi ne zahtijevaju Azure vjerodajnice niti krajnju točku. Maven automatski ne učitava datoteku okoline; postavite varijable u ljusci kojom pokrećete žive primjere. Za pokretanja iz IDE-a provjerite okruženje isporučeno konfiguracijom pokretanja.

## Vodič za odabir modela

| Varijabla okoline | Značenje | Zadano |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS korijen Azure resursa ili već normalizirani `/openai/v1` URL | Potrebno za živa pokretanja |
| `AZURE_OPENAI_DEPLOYMENT` | Naziv chat implementacije, ne verzija modela | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Konfiguracija zasebne implementacije za embedding, ne koristi se u ovim četiri programa | `text-embedding-3-small` |

Prazni preklopi implementacija koriste zadane vrijednosti. Konfiguracija dodaje `/openai/v1` točno jednom i odbacuje vjerodajnice, upitne nizove i zastarjele putove implementacije u krajnjoj točki.

Svaki chat zahtjev eksplicitno postavlja `reasoningEffort(ReasoningEffort.NONE)` i `maxCompletionTokens(...)`. Ni jedan zahtjev ne postavlja `temperature`, `top_p` ili zastarjelu opciju tokena dovršetka. To uključuje odabir alata i praćenje rezultata alata. GPT-5.6 alati za Chat Completions zahtijevaju napor rezoniranja `none`; vidi [Microsoftove smjernice za chat](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**U ovom poglavlju nema streaming ili ulazne točke za embedding.** Čitatelj dohvaća cijeli dokument, ne vektore. Ako ga proširite embeddingom, koristite zasebnu implementaciju za embedding poput `text-embedding-3-small`, nikad Lunu.

## Vodič 1: LLM dovršetci i chat

Izvor: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program izvodi jednostavno objašnjenje Java streamova, dvo-okretni razgovor HashMap/TreeMap i interaktivni chat. Drugi okret uključuje prvi odgovor asistenta; svaki interaktivni okret također šalje prethodni razgovor.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` osigurava implementaciju i eksplicitno postavljanje rezoniranja. Interaktivni chat preskače prazne retke, završava na `exit` ili EOF i čuva sistemsku poruku plus devet dovršenih korisničkih/asistentskih okreta. Ograničavanje broja okreta je obrazovna granica, ne točna garancija token budžeta.

Iz direktorija primjera:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Očekujte tri početna odgovora, zatim upit `You:`. Svako interaktivno pitanje koje nije prazno dodaje jedan zahtjev. Granice dovršetka su 200, 300, 400, zatim 500 tokena po interaktivnom okretu.

## Vodič 2: Pozivanje funkcija

Izvor: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK izvlači JSON sheme iz anotiranih zapisa `WeatherArguments` i `CalculationArguments`. Obavezni odabir alata čini svaki primjer vježbom protokola alata umjesto prihvaćanja samostalnog modelskog odgovora.

1. Pošaljite pitanje dopuštenim alatom, napor rezoniranja `none` i ograničenje dovršetka od 300 tokena.
2. Zahtijevajte razlog završetka `tool_calls`, provjerite ime funkcije i ID-jeve poziva te parsirajte tipizirane JSON argumente.
3. Izvršite lokalnu funkciju. Model ne izvršava Java ili proizvoljni kod.
4. Dodajte poruku poziva alata asistenta jednom, zatim svaki rezultat s odgovarajućim `tool_call_id`.
5. Pošaljite jedan završni zahtjev od 300 tokena bez alata i zahtijevajte dovršeni, neprazni odgovor.

`get_weather` vraća **simulirano**, ne stvarno, vrijeme. Poštuje grad i pretvara uzorak od 22 stupnja Celzija u Fahrenheit po zahtjevu. `calculate` ocjenjuje predani izraz preko exp4j, podržava oblike kao što su `15% od 240` i `2 + 3 * 4`, te odbacuje prazne, prevelike, neispravne ili nedefinirane izračune. Koristi računarstvo s pokretnim zarezom, ne financijsku decimalnu preciznost.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Očekujte `Function: get_weather`, simulirano vrijeme u Seattleu, `Function: calculate`, `Function result: 36` i dva završna odgovora. Nisu potrebni stdin ili vanjske vjerodajnice za vrijeme. Uspješan pokret koristi točno četiri chat zahtjeva.

## Vodič 3: RAG (generacija uz nadopunu pretraživanjem)

Izvor: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Ulaz: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Ovaj uvodni RAG primjer dohvaća cijeli UTF-8 dokument i uključuje ga u korisničku poruku s pitanjem. Posebna sistemska poruka uči model da tretira sadržaj dokumenta kao nepouzdane podatke i odgovara samo iz tog konteksta. Ako dokument ne sadrži odgovor, zatraženi odgovor je: `Ne mogu pronaći tu informaciju u dostavljenom dokumentu.`

Utemeljenje može smanjiti halucinacije, ali ni razdjelnici ni sistemske upute ne garantiraju točnost niti sprječavaju svaku injekciju prompta. Pregledajte žive odgovore. Produkcijski RAG obično dodaje razdvajanje na dijelove, dohvat, citate, kontrolu pristupa i evaluaciju.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Unesite jedno pitanje, na primjer `Koju metodu autentifikacije dokument opisuje?`. Očekujte odgovor koji spominje Microsoft Entra ID. Program izlazi nakon jednog chat zahtjeva s ograničenjem dovršetka od 500 tokena.

Zadani pronalazak datoteke radi iz korijena repozitorija, direktorija poglavlja ili direktorija primjera. Podržana je i eksplicitna putanja:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Ulazi moraju biti ne prazni: najviše 32 KiB UTF-8 podataka dokumenta i 2.000 znakova pitanja. Nedostajuće datoteke, prazna/prazna pitanja i preveliki ulazi ne prolaze prije izvođenja zaključivanja.

## Vodič 4: Odgovorno AI

Izvor: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Šest testova pokriva štetne upute, govor mržnje, privatnost, medicinske dezinformacije, nezakoniti sadržaj i benigno odgovorno AI pitanje. Program promatra odgovor umjesto da pretpostavlja da svaki test mora izazvati filter.

| Ishod | Dokaz |
| --- | --- |
| `FILTERED` | Izričit `content_filter` / `ResponsibleAIPolicyViolation` kod pogreške ili razlog završetka dovršetka `content_filter` |
| `REFUSED` | Neprazno strukturirano polje `message.refusal` |
| `POSSIBLE_REFUSAL` | Uvodna fraza odbijanja u običnom tekstu; heuristika koja zahtijeva pregled |
| `GENERATED` | Dovršen neprazni odgovor; nije dokaz da je sadržaj siguran |

Obični HTTP 400 **nije** dokaz filtriranja. Nevaljali parametri, neuspjesi autentifikacije, ograničenja brzine, greške poslužitelja, nepravilni odgovori i skraćeni izlaz ne uspijevaju pokretanje umjesto da proizvode lažni sigurnosni uspjeh. Opće riječi poput "štetan sadržaj" u benignom objašnjenju ne računaju se kao odbijanje.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Očekujte šest rezultata kategorija i sažetak koji navodi da opažanja nisu certifikat o sigurnosti. Svaki test ima ograničenje dovršetka od 300 tokena. Pregledajte neočekivane generacije i moguća odbijanja ručno; benigni usporedbeni bi trebao proizvesti sadržajno odgovorno AI objašnjenje. Nije potreban stdin.

## Uobičajeni obrasci u primjerima

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizira normalizaciju krajnje točke, preklapanje implementacija, autentifikaciju bez ključa i chat postavke:

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

Dobavljač tokena osvježava pristupne tokene prema potrebi. Ne zapisujte tokene niti ih zamjenjujte API ključem. Svaki program ponovno koristi svog klijenta i zatvara ga u `finally` ili kroz vlastiti `AutoCloseable` omotač; SDK-jev `OpenAIClient` nije `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) zahtijeva dovršen, neprazni tekstualni odgovor. Prazni izbori, odbijanja, filteri i skraćeni odgovori se ne ispisuju tiho kao uspjeh. Primjer odgovornog AI-a eksplicitno obrađuje očekivane ishode filtera/odbijanja. Nepodržani neuspjesi daju Java/Maven procesu nenulti izlazni kod.

**Automatski ponovni pokušaji SDK-a su onemogućeni** kako bi se broj zahtjeva učinio predvidljivim na zajedničkim implementacijama s niskim brojem zahtjeva po minuti. Svaki zahtjev zaključivanja ima vremenski limit od 60 sekundi. Nabava tokena može potrajati dodatno. Raspoređivanje na razini aplikacije mora poštivati kvote; nemojte nasumično ponavljati neuspjeli plaćeni zahtjev.

## Jedinični testovi

Iz direktorija primjera:

```powershell
mvn -B -ntp clean test
```

Testni transport u potpunosti zamjenjuje SDK HTTP sloj, bilježi stvarna serijalizirana tijela zahtjeva i isporučuje redoslijed odgovora. Ne otvara utičnice, ne pribavlja Azure tokene i ne uspijeva na neočekivanim zahtjevima. Ovi testovi potvrđuju ponašanje aplikacije i SDK protokol, ne kvalitetu živog modela ili dostupnost implementacije.

| Testni paket | Pokrivenost |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizacija/odbacivanje krajnje točke, preklapanje implementacija, opcije rezoniranja i tokena |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Svaki tijek dovršetka, povijest poruka, uređivanje dovršenih okreta, EOF, neuspjesi |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Sheme alata, tipizirani argumenti, aritmetika, ID-jevi, višestruki rezultati alata, neuspjeli nastavci |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Pronalaženje datoteka, UTF-8, veličinske granice, osnovni teret, ulazne i API pogreške |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Sve šest testova, eksplicitni filteri, klasifikacija odbijanja, obične 400 i druge pogreške |

Za jedan paket koristite `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Zajedničke pripreme nalaze se u [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekvencijalna provjera uživo

Živi pozivi su odvojeni od jediničnih testova. Koristite sljedeće naredbe **pojedinačno**, iz korijena repozitorija, tek nakon što su vjerodajnice i pristup implementaciji spremni. Nije potrebna nijedna usluga ili trajni proces.

Za zajedničku implementaciju **10 zahtjeva/minutu** rezervirajte dovoljno kvote za cijeli sljedeći program prije njegovog pokretanja: 5, 4, 1, zatim 6 zahtjeva. Sekvencijalni procesi sami ne jamče poštovanje ograničenja brzine. Usuglasite tekuću minutu sa svim ostalim pozivateljima; nemojte lijepiti četiri poziva kao neodmjerenu skupinu.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Dovršetci, višestruki okreti i dva interaktivna okreta:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Provjerite sva tri naslova odjeljaka, pet odgovora, završni interaktivni odgovor koji spominje Adu, `Doviđenja!`, i izlazni kod 0. Proračun: **5 upita, najviše 1.900 tokena dovršetka**. Za manji pokret, proslijedite samo `exit`: 3 upita / 900 tokena, ali to ne pokreće interaktivno zaključivanje.

**2. Oba tijeka rada s pozivanjem funkcija:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Provjerite oba naziva funkcija, simulirano vrijeme u Seattleu, izračunati rezultat 36, dva konačna odgovora i izlazni kod 0. Proračun: **4 upita, najviše 1.200 tokena dovršetka**.

**3. Odgovor temeljen na dokumentu:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Provjerite put dokumenta, odgovor koji spominje Microsoft Entra ID i izlazni kod 0. Proračun: **1 upit, najviše 500 tokena dovršetka**. Postojeći [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) je jedini potrebni ulazni dokument. Neobavezni drugi pokret koji pita o odsutnoj temi treba suzdržati se i dodaje jedan upit / 500 tokena.

**4. Promatranja odgovornog AI:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Provjerite šest kategorija i sažetak promatranja, pregledajte generirani sadržaj i zahtijevajte izlazni kod 0 za tehničko dovršenje. Uspješan završetak procesa ne potvrđuje sigurnost modela. Proračun: **6 upita, najviše 1.800 tokena dovršetka**.

**Ukupno za četiri naredbe: 16 chat upita i najviše 5.400 tokena dovršetka**, plus ulazni tokeni (uključujući ponovljene razgovore i shemu/povijest alata). Nema zahtjeva za ugrađene prikaze. Stvarna upotreba tokena ovisi o modelu i može biti manja, osobito za filtrirane upite. Trošak u dolarima ovisi o cijenama implementacije; nije impliciran fiksni novčani iznos. Svi limiti upita pretpostavljaju da nema ručnih ponovnih pokretanja. Odmah nakon svake naredbe provjerite `$LASTEXITCODE`; vrijednost različita od nule znači da pokret nije uspješno dovršen.

## Rješavanje problema

- **Nedostaje endpoint / 401 / 403:** Postavite endpoint u procesu pokretanja, provjerite lokalnu Azure prijavu i ulogu na razini resursa, te provjerite neželjene zamjene identiteta u okruženju.
- **400 / 404:** Potvrdite da implementacija postoji i podržava Chat Completions s razinom napora `none`. Koristite HTTPS root resursa ili URL `/openai/v1`, a ne URL zastarjele implementacije. Uobičajeni 400 errori su tehnički neuspjesi, a ne sigurnosne blokade.
- **429:** Koordinirajte zajednički RPM i kvotu tokena prije pokušaja ponovo. Primjeri namjerno ne rade automatski ponovni pokušaj.
- **`Nepotpuni odgovor na chat: duljina`:** Izlaz je dosegao ograničenje dovršetka. Pregledajte odgovor i prompt prije povećanja ograničenja i njegovog dokumentiranog proračuna; ne bilježite skraćeni pokret kao uspješan.
- **Pogreške datoteke ili stdin-a:** Pokrenite iz podržanog direktorija ili navedite jasan put do dokumenta. Postavite ne-prazno pitanje čitaču. Dovršetci se mogu normalno završiti na EOF ili `exit`.
- **Pogreške pri kompajliranju:** Provjerite imate li Java 21 ili noviju, zatim pokrenite `mvn -B -ntp clean test`. U PowerShellu navodite cijeli Maven argument koji sadrži točkastu svojinu, primjerice `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Sljedeći koraci

Nastavite na [Poglavlje 4: Praktični primjeri](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati greške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->