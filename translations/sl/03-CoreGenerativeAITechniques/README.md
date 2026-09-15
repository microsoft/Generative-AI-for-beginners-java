# Osnove učnega tečaja generativne umetne inteligence

## Kazalo vsebine

- [Predpogoji](#predpogoji)
- [Začetek](#začetek)
- [Vodnik za izbiro modela](#vodnik-za-izbiro-modela)
- [Tečaj 1: Dokončanja in klepet z LLM](#tečaj-1-dokončanja-in-klepet-z-llm)
- [Tečaj 2: Klic funkcij](#tečaj-2-klic-funkcij)
- [Tečaj 3: RAG (Ustvarjanje z iskalnim izboljšanjem)](#tečaj-3-rag-ustvarjanje-z-iskalnim-izboljšanjem)
- [Tečaj 4: Odgovorna umetna inteligenca](#tečaj-4-odgovorna-umetna-inteligenca)
- [Pogosti vzorci v primerih](#pogosti-vzorci-v-primerih)
- [Enote testov](#enotni-testi)
- [Zaporedna sprotna verifikacija](#zaporedna-sprotna-verifikacija)
- [Reševanje težav](#reševanje-težav)
- [Naslednji koraki](#naslednji-koraki)

## Pregled

Štirje samostojni Java programi pokažejo klepet, zgodovino pogovora, klic funkcij, generacijo z iskalnim izboljšanjem (RAG) za celoten dokument ter odgovorno ravnanje z umetno inteligenco. Vseh klepetalnih zahtevkov je privzeto usmerjenih na **GPT-5.6 Luna z racionalizacijskim naporom `none`**.

Ti primeri uporabljajo uradni OpenAI Java SDK z Azure OpenAI vmesnikom v1, sledijo [Microsoftovim navodilom za SDK](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Starejši paket `azure-ai-openai` ni več odvisnost. Chat Completions ostaja za učenje obstoječih potekov dela na osnovi sporočil; poglejte [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) za druge API možnosti.

## Predpogoji

- Java 21 ali novejši in Maven 3.6.3 ali novejši.
- Azure OpenAI klepetalna namestitev z imenom `gpt-5.6-luna` ali preglasitev z združljivimi nastavitvami Chat Completions.
- Prijavljena Azure identiteta z vlogo **Cognitive Services OpenAI User** na višku. Lokalni razvoj uporablja vašo Azure CLI prijavo; gostovane aplikacije lahko uporabljajo upravljano identiteto.
- Za nastavitev virov in navodila za prijavo glejte [Poglavje 2](../02-SetupDevEnvironment/getting-started-azure-openai.md).

[Maven konfiguracija](../../../03-CoreGenerativeAITechniques/examples/pom.xml) določa te različice, preverjene 2026-09-14:

| Komponenta | Različica | Namen |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Uradni odjemalec združljiv z Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Avtorizacija brez ključa in osvežitev žetonov |
| `net.objecthunter:exp4j` | 0.4.8 | Parsiranje aritmetičnih izrazov brez evalvacije kode |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter enotni testi |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Prevajanje Java 21, testi, izvajalni primeri |

Prevajalnik uporablja `--release 21`. Za te samostojne primere ni potrebna odvisnost Spring Boot, Spring AI ali LangChain4j.

## Začetek

Iz korena repozitorija nastavite končno točko vira in po potrebi preglasitev namestitve v lupini.

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

Testi ne potrebujejo Azure poverilnic ali končne točke. Maven samodejno ne bere datotek okolja; spremenljivke nastavite v lupini, ki sproži žive primere. Za zagon v IDE preverite okolje v konfiguraciji zagona.

## Vodnik za izbiro modela

| Okoljska spremenljivka | Pomen | Privzeto |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS koren Azure vira ali že normaliziran URL `/openai/v1` | Potrebno za žive zagon |
| `AZURE_OPENAI_DEPLOYMENT` | Ime namestitve za klepet, ne različica modela | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Konfiguracija ločene namestitve za vdelave, ne uporablja se v teh štirih programih | `text-embedding-3-small` |

Prazne preglasitve namestitev uporabijo privzete vrednosti. Konfiguracija pripne `/openai/v1` točno enkrat in zavrne poverilnice, poizvedbene nize ter stare poti namestitev v končni točki.

Vsak klepetalni zahtevek izrecno nastavi `reasoningEffort(ReasoningEffort.NONE)` in `maxCompletionTokens(...)`. Noben zahtevek ne nastavi `temperature`, `top_p` ali stare možnosti za tokene dokončanja. To vključuje izbiro orodja in spremljajoče rezultate. Funkcijska orodja GPT-5.6 Chat Completions zahtevajo racionalizacijski napor `none`; glej [Microsoftova navodila za klepet](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**V tem poglavju ni vhoda za pretočni ali vdelani način.** Bralec pridobi celoten dokument, ne vektorjev. Če razširite z vdelavami, uporabite ločeno namestitev vdelave, na primer `text-embedding-3-small`, nikoli Luno.

## Tečaj 1: Dokončanja in klepet z LLM

Vir: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program prikazuje preprosto razlago Java tokov, dvotarni pogovor z HashMap/TreeMap in interaktivni klepet. Drugi krog vključuje prvi odgovor pomočnika; vsak interaktivni krog pošlje tudi predhodni pogovor.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` posreduje namestitev in izrecno nastavitev racionalizacije. Interaktivni klepet preskoči prazne vrstice, konča na `exit` ali EOF in obdrži sistemsko sporočilo ter devet zaključenih uporabniško/pomočniških krogov. Omejitev števila krogov je izobraževalna, ne točna zagotovitev proračuna za tokene.

Iz imenika primerov:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Pričakujte tri začetne odgovore, nato poziv `You:`. Vsako interaktivno vprašanje, ki ni prazno, doda en zahtevek. Omejitve dokončanja so 200, 300, 400 nato 500 tokenov na interaktivni krog.

## Tečaj 2: Klic funkcij

Vir: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK izpelje JSON sheme iz označenih zapisov `WeatherArguments` in `CalculationArguments`. Izbrano orodje zakonsko zahteva vsak primer, da vadi protokol orodja namesto sprejema modelovega odgovora brez pomoči.

1. Pošlji vprašanje z dovolenim orodjem, racionalizacija ni potrebna, omejitev dokončanja 300 tokenov.
2. Zahtevaj zaključni razlog `tool_calls`, preveri ime funkcije in ID-je klicev ter razčleni tipizirane JSON argumente.
3. Izvedi lokalno funkcijo. Model ne izvaja Jave ali poljubne kode.
4. Doda sporočilo z orodjem pomočnika enkrat, nato vsak rezultat z ustreznim `tool_call_id`.
5. Pošlji nazadnje en zahtevek 300 tokenov brez orodij in zahteva dokončan, ne prazen odgovor.

`get_weather` vrača **simulirano**, ne trenutno, vreme. Upošteva mesto in pretvori vzorec 22 stopinj Celzija v Fahrenheite, če je zahtevano. `calculate` oceni dano enačbo preko exp4j, podpira oblike kot `15% of 240` in `2 + 3 * 4`, zavrne prazne, prevelike, neveljavne ali neskončne izračune. Uporablja plavajočo aritmetiko, ne finančno decimalno natančnost.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Pričakujte `Function: get_weather`, simulirano vreme v Seattlu, `Function: calculate`, `Function result: 36` in dva zaključna odgovora. Ne potrebujete vhodnih podatkov iz stdin ali zunanjih poverilnic za vreme. Uspešen zagon ima natančno štiri klepetalne zahtevke.

## Tečaj 3: RAG (Ustvarjanje z iskalnim izboljšanjem)

Vir: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Vnos: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Ta uvodni RAG primer pridobi en celoten UTF-8 dokument in ga vključi v uporabniško sporočilo z vprašanjem. Ločeno sistemsko sporočilo modelu naroči, da obravnava vsebino dokumenta kot nezanesljive podatke in odgovarja samo iz tega konteksta. Če dokument ne vsebuje odgovora, je zahtevan odgovor: `Ne morem najti te informacije v predloženem dokumentu.`

Utemeljevanje lahko zmanjša halucinacije, a ne omejevalniki niti sistemska navodila ne zagotavljajo pravilnosti ali ne preprečijo vseh vdorov v poziv. Preglejte žive odgovore. Proizvodna RAG običajno dodaja razdeljevanje, iskanje, navedbe, nadzor dostopa in ocenjevanje.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Vnesite eno vprašanje, na primer `Katero metodo avtentikacije dokument opisuje?`. Pričakujte odgovor z omembo Microsoft Entra ID. Program po enem klepetalnem zahtevku z omejitvijo 500 tokenov izstopi.

Privzeto iskanje datotek deluje iz korena repozitorija, poglavjskega imenika ali imenika primerov. Podprt je tudi izrecen pot.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Vnosi morajo biti ne-prazni: največ 32 KiB podatkov UTF-8 dokumenta in 2000 znakov vprašanja. Manjkajoče datoteke, prazna/vloga EOF vprašanja in preveliki vnosi ne uspejo pred sklepanjem.

## Tečaj 4: Odgovorna umetna inteligenca

Vir: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Šest sond pokriva škodljiva navodila, sovražni govor, zasebnost, medicinske dezinformacije, nezakonito vsebino in benigno odgovorno vprašanje umetne inteligence. Program opazuje odziv namesto, da bi predpostavil, da mora vsaka sonda sprožiti filter.

| Izid | Dokaz |
| --- | --- |
| `FILTERED` | Izrecna napaka kode `content_filter` / `ResponsibleAIPolicyViolation` ali zaključni razlog `content_filter` pri dokončanju |
| `REFUSED` | Ne-prazno strukturirano polje `message.refusal` |
| `POSSIBLE_REFUSAL` | Uvodna fraza zavrnitve v običajnem tekstu; hevristika, ki zahteva pregled |
| `GENERATED` | Dokončan ne-prazen odgovor; ni dokaz, da je vsebina varna |

Običajen HTTP 400 **ni** dokaz filtriranja. Neveljavne parametre, neuspehe avtentikacije, omejitve hitrosti, napake strežnika, napačne odzive in skrajšane izhode ne uspejo izvajanja, namesto da bi ustvarili lažen varnostni uspeh. Splošne besede kot "škodljiva vsebina" v benignem pojasnilu niso štete kot zavrnitev.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Pričakujte šest rezultatov kategorij in povzetek, da opazovanja niso varnostna certifikacija. Vsaka sonda ima omejitev dokončanja 300 tokenov. Ročno preglejte nepričakovane generacije in možne zavrnitve; benigni primer naj proizvede vsebinsko odgovorno AI razlago. Ni potrebnega stdin.

## Pogosti vzorci v primerih

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizira normalizacijo končnih točk, preglasitve nastavitev, avtorizacijo brez ključa in možnosti klepeta:

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

Ponudnik tokenov po potrebi osveži dostopne žetone. Ne beležite tokenov in ne nadomeščajte tega z API ključem. Vsak program znova uporablja svoj odjemalec in ga zapre v `finally` ali s svojo ovojnico `AutoCloseable`; sam SDK `OpenAIClient` ni `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) zahteva dokončan, ne-prazen besedilni odgovor. Prazne izbire, zavrnitve, filtri in skrajšani odgovori niso tiho izpisani kot uspeh. Primer odgovorne AI izrecno upravlja pričakovane izide filtriranja/zavrnitve. Neobdelane napake dajo Java/Maven procesu nenicelno izhodno kodo.

**Samodejni ponovni poskusi SDK so onemogočeni** za ohranjanje predvidljivega števila zahtevkov pri skupnih nizko RPM namestitvah. Vsak zahtevek za sklepanje ima 60 sekundni časovni limit. Pridobitev tokenov lahko vzame dodatni čas. Načrtovanje na ravni aplikacije mora spoštovati kvote; ne ponavljajte slepo neuspelih plačljivih zahtev.

## Enotni testi

Iz imenika primerov:

```powershell
mvn -B -ntp clean test
```

Testni transport popolnoma nadomesti SDK HTTP plast, zajema dejanska serializirana telesa zahtevkov in nudi vrsto odgovorov v čakalni vrsti. Ne odpira vtičnic, ne pridobiva Azure žetonov in ne uspe pri nepričakovanih zahtevkih. Ti testi preverjajo vedenje aplikacije in SDK protokol, ne pa kakovost modelov v živo ali razpoložljivost namestitve.

| Testni paket | Pokritost |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizacija/zavrnitev končnih točk, preglasitve namestitev, možnosti racionalizacije in tokenov |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Vsak potek dokončanja, zgodovina sporočil, obrezovanje celotnih krogov, EOF, napake |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Sheme orodij, tipizirani argumenti, aritmetika, ID-ji, več rezultatov orodij, neuspešni nadaljnji pozivi |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Iskanje datotek, UTF-8, prostorske omejitve, vsebina ozemljevanja, vnosi in API napake |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Vse šest sond, izrecni filtri, klasifikacija zavrnitev, običajne napake 400 in druge |

Za en paket uporabite `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Skupni pripomočki so v [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Zaporedna sprotna verifikacija

Živi pozivi so ločeni od enotnih testov. Uporabite naslednje ukaze **posamezno**, iz korena repozitorija, šele ko so poverilnice in dostop do namestitve pripravljeni. Ni potrebnih storitev ali trajnih procesov.

Za skupno **10 zahtevkov/minuto** namestitev, pred zagonom zagotovite dovolj kvote za celoten naslednji program: 5, 4, 1 ter nato 6 zahtevkov. Zaporedni procesi sami ne zagotavljajo spoštovanja omejitev hitrosti. Uskladite tekoči minutek z vsemi drugimi povabljenci; ne prilepite štirih klicev kot neporavnan kup.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Dokončanja, večkrožni ter dva interaktivna kroga:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Preverite vse tri naslove odsekov, pet odgovorov, zadnji interaktivni odgovor, ki omenja Ado, `Nasvidenje!` in izhodno kodo 0. Proračun: **5 zahtev, največ 1.900 zaključnih žetonov**. Za manjši zagon uporabite samo ukaz `exit`: 3 zahteve / 900 žetonov, vendar to ne preizkuša interaktivnega sklepanja.

**2. Oba poteka klicev funkcij:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Preverite obe imeni funkcij, simulirano vreme v Seattlu, izračunan rezultat 36, dva zaključna odgovora in izhodno kodo 0. Proračun: **4 zahteve, največ 1.200 zaključnih žetonov**.

**3. Odgovor na podlagi dokumenta:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Preverite pot do dokumenta, odgovor, ki omenja Microsoft Entra ID, in izhodno kodo 0. Proračun: **1 zahteva, največ 500 zaključnih žetonov**. Obstoječa datoteka [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) je edina zahtevana vhodna datoteka. Neobvezni drugi zagon, ki sprašuje o neobstoječi temi, naj se vzdrži in doda eno zahtevo / 500 žetonov.

**4. Opazovanja odgovorne umetne inteligence:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Preverite šest kategorij in povzetek opazovanj, preglejte ustvarjeno vsebino in zahtevajte izhodno kodo 0 za tehnično dokončanje. Uspešen izhodni proces ne zagotavlja varnosti modela. Proračun: **6 zahtev, največ 1.800 zaključnih žetonov**.

**Skupaj za štiri ukaze: 16 zahtev klepeta in največ 5.400 zaključnih žetonov**, plus vhodni žetoni (vključno z ponovljenim pogovorom in shemo/zgodovino orodja). Ni zahtev za vdelave. Dejanska poraba žetonov je odvisna od modela in je lahko nižja, še posebej pri filtriranih pozivih. Stroški v dolarjih so odvisni od cen za uvajanje; ni podanega fiksnega denarnega ocenjevanja. Vse omejitve zahtev predvidevajo, da ni ročnih ponovitev. Takoj po vsakem ukazu preverite `$LASTEXITCODE`; če ni nič, zagon ni bil uspešen.

## Reševanje težav

- **Manjkajoča končna točka / 401 / 403:** Nastavite končno točko v procesu zagona, preverite lokalni prijavni podatki Azure in vlogo z obsegom vira ter preverite nenamerne preglasitve identitete v okolju.
- **400 / 404:** Preverite, da uvajanje obstaja in podpira Chat Completions z zahtevkom "none". Uporabite korensko HTTPS pot vir ali URL `/openai/v1`, ne pa URL za staro uvajanje. Navadna napaka 400 je tehnična napaka, ne preprečitev varnosti.
- **429:** Uskladite skupni RPM in kvoto žetonov pred ponovnim poskusom. Primeri namerno ne izvajajo samodejnih ponovitev.
- **`Nedokončan odgovor klepeta: dolžina`:** Izhod je dosegel omejitev dokončanja. Preverite odgovor in poziv pred povišanjem omejitve in z njo povezanim proračunom; skrajšan zagon ne sme biti zabeležen kot uspešen.
- **Napake z datotekami ali stdin:** Zaženite iz podprte mape ali navedite eksplicitno pot do dokumenta. Zagotovite ne-prazno vprašanje bralca. Dokončanja se lahko končajo normalno na EOF ali `exit`.
- **Napake pri prevajanju:** Preverite Java 21 ali novejšo različico, nato zaženite `mvn -B -ntp clean test`. V PowerShellu navedite celoten argument Maven, ki vsebuje piko, na primer `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Naslednji koraki

Nadaljujte na [Poglavje 4: Praktični primeri](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba obravnavati kot avtoritativni vir. Za kritične informacije je priporočljiv strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->