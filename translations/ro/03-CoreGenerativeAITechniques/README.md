# Tutorial Tehnici Generative AI de Bază

## Cuprins

- [Cerințe preliminare](#cerințe-preliminare)
- [Început](#început)
- [Ghid de selecție a modelului](#ghid-de-selecție-a-modelului)
- [Tutorial 1: Completări LLM și Chat](#tutorial-1-completări-llm-și-chat)
- [Tutorial 2: Apelare funcții](#tutorial-2-apelare-funcții)
- [Tutorial 3: RAG (Generare augmentată prin recuperare)](#tutorial-3-rag-generare-augmentată-prin-recuperare)
- [Tutorial 4: AI responsabil](#tutorial-4-ai-responsabil)
- [Tipare comune în exemple](#tipare-comune-în-exemple)
- [Teste unitare](#teste-unitare)
- [Verificare live secvențială](#verificare-live-secvențială)
- [Depanare](#depanare)
- [Pașii următori](#pașii-următori)

## Prezentare generală

Patru programe Java independente demonstrează chat, istoric conversații, apelarea funcțiilor, generarea augmentată prin recuperare (RAG) la nivel de document întreg și gestionarea răspunsurilor AI responsabile. Toate cererile de chat vizează implicit **GPT-5.6 Luna cu efortul de raționare `none`**.

Aceste exemple folosesc SDK-ul Java oficial OpenAI cu endpointul Azure OpenAI v1, urmând [ghidul SDK Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Pachetul mai vechi `azure-ai-openai` nu mai este o dependență. Chat Completions este păstrat pentru a învăța fluxurile existente bazate pe mesaje; vedeți [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) pentru alte opțiuni API.

## Cerințe preliminare

- Java 21 sau versiuni ulterioare și Maven 3.6.3 sau versiuni ulterioare.
- O implementare chat Azure OpenAI numită `gpt-5.6-luna`, sau o suprascriere compatibilă cu setările Chat Completions.
- O identitate Azure autentificată cu rolul **Cognitive Services OpenAI User** pe resursă. Dezvoltarea locală folosește autentificarea dvs. Azure CLI; aplicațiile găzduite pot folosi identitate gestionată.
- Consultați [Capitolul 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) pentru configurarea resursei și instrucțiuni de autentificare.

[Configurarea Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fixează aceste versiuni, verificate la 2026-09-14:

| Componentă | Versiune | Scop |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Client oficial compatibil Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Autentificare fără cheie și reîmprospătare token |
| `net.objecthunter:exp4j` | 0.4.8 | Parsare expresii aritmetice fără evaluare de cod |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Teste unitare Jupiter offline |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Compilare Java 21, teste, exemple rulabile |

Compilatorul folosește `--release 21`. Nu este necesară dependența Spring Boot, Spring AI sau LangChain4j pentru aceste exemple independente.

## Început

Din rădăcina depozitului, setați endpointul resursei și suprascrierea opțională a implementării în shell.

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

Testele nu necesită nici credențiale Azure, nici un endpoint. Maven nu citește automat un fișier de mediu; setați variabilele în shell-ul folosit pentru a porni exemplele live. Pentru lansările din IDE, verificați mediul furnizat de configurația de lansare.

## Ghid de selecție a modelului

| Variabilă de mediu | Semnificație | Implicit |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Rădăcina resursei Azure HTTPS sau URL deja normalizat cu `/openai/v1` | Necesare pentru rulare live |
| `AZURE_OPENAI_DEPLOYMENT` | Numele implementării chat, nu o versiune model | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configurare implementare embedding separată, nefolosită de aceste patru programe | `text-embedding-3-small` |

Suprascrierile goale folosesc valorile implicite. Configurația adaugă `/openai/v1` exact o dată și respinge credențiale, șiruri de interogare și căi legacy în endpoint.

Fiecare cerere de chat setează explicit `reasoningEffort(ReasoningEffort.NONE)` și `maxCompletionTokens(...)`. Nicio cerere nu setează `temperature`, `top_p` sau opțiunea legacy de tokeni la completare. Aceasta include selecția instrumentului și follow-upurile asupra rezultatului instrumentului. Instrumentele funcției Chat Completions GPT-5.6 necesită efort de raționare `none`; vezi [ghidul chat Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Nu există punct de intrare pentru streaming sau embedding în acest capitol.** Cititorul recuperează întreg documentul, nu vectori. Dacă extindeți cu embeddings, folosiți o implementare embedding separată precum `text-embedding-3-small`, niciodată Luna.

## Tutorial 1: Completări LLM și Chat

Sursă: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programul rulează o explicație simplă a fluxurilor Java, o conversație în două ture HashMap/TreeMap și un chat interactiv. Turul al doilea include primul răspuns al asistentului; fiecare tur interactiv trimite și conversația anterioară.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` furnizează implementarea și setarea explicită a raționării. Chat-ul interactiv sare peste linii goale, se încheie la `exit` sau EOF și păstrează mesajul de sistem plus nouă ture completate utilizator/asistent. Limitarea numărului de ture este o măsură educațională, nu o garanție exactă pentru bugetul de tokeni.

Din directorul examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Așteptați trei răspunsuri inițiale, apoi un prompt `You:`. Fiecare întrebare interactivă nenulă adaugă o cerere. Limitele completării sunt de 200, 300, 400 și apoi 500 tokeni pe tur interactiv.

## Tutorial 2: Apelare funcții

Sursă: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK-ul derivă scheme JSON din înregistrările annotate `WeatherArguments` și `CalculationArguments`. O alegere obligatorie de instrument face ca fiecare exemplu să exerseze protocolul instrumentului în loc să accepte un răspuns nesupravegheat al modelului.

1. Trimiteți o întrebare cu instrumentul permis, efort de raționare `none` și o limită de 300 tokeni pentru completare.
2. Solicitați un motiv de terminare `tool_calls`, validați numele funcției și ID-urile apelului și parsați argumentele JSON tipate.
3. Executați funcția locală. Modelul nu execută Java sau cod arbitrar.
4. Adăugați mesajul de apel al instrumentului al asistentului o singură dată, urmat de fiecare rezultat cu `tool_call_id` corespunzător.
5. Trimiteți o cerere finală de 300 tokeni fără instrumente și solicitați un răspuns complet, nenul.

`get_weather` returnează vreme **simulată**, nu live. Respectă orașul și convertește cei 22 de grade Celsius de test în Fahrenheit când se cere. `calculate` evaluează expresia furnizată prin exp4j, suportă forme ca `15% of 240` și `2 + 3 * 4`, și respinge calcule goale, supradimensionate, invalide sau infinite. Folosește aritmetică în virgulă mobilă, nu precizie decimală financiară.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Așteptați `Function: get_weather`, vreme simulată în Seattle, `Function: calculate`, `Function result: 36` și cele două răspunsuri finale. Nu sunt necesare stdin sau credențiale externe pentru vreme. O rulare reușită folosește exact patru cereri chat.

## Tutorial 3: RAG (Generare augmentată prin recuperare)

Sursă: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Intrare: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Acest exemplu introductiv RAG preia un întreg document UTF-8 și îl include în mesajul utilizatorului împreună cu întrebarea. Un mesaj de sistem separat instrucționează modelul să trateze conținutul documentului ca date neîncrezătoare și să răspundă doar din acel context. Dacă documentul nu conține răspunsul, răspunsul cerut este: `Nu pot găsi acea informație în documentul furnizat.`

Fundamentarea poate reduce halucinațiile, dar delimitatoarele sau instrucțiunile sistemului nu garantează acuratețea sau previn orice injectare de prompt. Revizuiți răspunsurile live. RAG-ul de producție adaugă normal împărțire în segmente, recuperare, citări, control acces și evaluare.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Introduceți o întrebare, de exemplu `Care metodă de autentificare descrie documentul?`. Așteptați un răspuns ce menționează Microsoft Entra ID. Programul se închide după o cerere chat cu o limită de 500 tokeni.

Căutarea implicită a fișierului funcționează din rădăcina depozitului, directorul capitolului sau directorul examples. Este suportată și o cale explicită:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Intrările trebuie să fie nenule: cel mult 32 KiB de date document UTF-8 și 2.000 de caractere întrebări. Lipsa fișierelor, întrebări nule/EOF și intrări supradimensionate eșuează înainte de inferență.

## Tutorial 4: AI responsabil

Sursă: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Cele șase probe acoperă instrucțiuni dăunătoare, discurs instigator la ură, confidențialitate, dezinformare medicală, conținut ilegal și o întrebare benignă de AI responsabil. Programul observă răspunsul fără a considera că fiecare probă trebuie să declanșeze un filtru.

| Rezultat | Dovezi |
| --- | --- |
| `FILTRAT` | Un cod explicit de eroare `content_filter` / `ResponsibleAIPolicyViolation`, sau un motiv de terminare `content_filter` la completare |
| `REFUZAT` | Un câmp structurat `message.refusal` nenul |
| `POSIBIL_REFUZ` | O frază de refuz în text obișnuit; o euristică ce necesită revizuire |
| `GENERAT` | Un răspuns complet, nenul; nu dovadă că conținutul este sigur |

Un HTTP 400 obișnuit **nu** este dovadă a filtrării. Parametrii invalizi, eșecurile de autentificare, limitele de rată, erorile serverului, răspunsurile necorespunzătoare și ieșirile tăiate eșuează rularea în loc să genereze un succes fals de siguranță. Cuvintele largi precum "conținut dăunător" într-o explicație benignă nu contează ca refuz.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Așteptați șase rezultate pe categorii și un sumar care afirmă că observațiile nu sunt o certificare de siguranță. Fiecare probă are o limită de completare de 300 tokeni. Examinați manual generațiile neașteptate și posibilele refuzuri; comparația benignă ar trebui să ducă la o explicație relevantă de AI responsabil. Nu este necesar stdin.

## Tipare comune în exemple

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizează normalizarea endpointului, suprascrierile de implementare, autentificarea fără cheie și opțiunile chat:

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

Furnizorul tokenilor reîmprospătează tokeni de acces după nevoie. Nu înregistrați tokeni și nu înlocuiți cu o cheie API. Fiecare program reutilizează clientul său și îl închide în `finally` sau prin propriul wrapper `AutoCloseable`; SDK-ul `OpenAIClient` însuși nu este `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) solicită un răspuns textual complet și nenul. Opțiunile goale, refuzurile, filtrele și răspunsurile trunchiate nu sunt tipărite silențios ca succes. Exemplul de AI responsabil gestionează explicit rezultatele așteptate de filtre/refuz. Eșecurile neprevăzute dau procesului Java/Maven un cod de ieșire nenul.

**Retrial-urile automate ale SDK sunt dezactivate** pentru a menține predictibil numărul de cereri pe implementările cu RPM scăzut partajat. Fiecare cerere de inferență are un timeout de 60 de secunde. Obținerea tokenilor poate dura mai mult. Programarea la nivel de aplicație trebuie să respecte cotelor; nu relansați automat o cerere plătită eșuată.

## Teste unitare

Din directorul examples:

```powershell
mvn -B -ntp clean test
```

Transportul de test înlocuiește complet stratul HTTP al SDK, capturează corpurile cererilor serializate reale și furnizează răspunsuri în coadă. Nu deschide socket-uri, nu dobândește tokeni Azure și eșuează la cereri neașteptate. Aceste teste validează comportamentul aplicației și protocolul SDK, nu calitatea modelului live sau disponibilitatea implementării.

| Suita de teste | Acoperire |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizarea/respingeri endpoint, suprascrieri implementare, opțiuni raționare și tokeni |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Fiecare flux completare, istoric mesaje, trunchiere tur complet, EOF, eșecuri |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Scheme instrument, argumente tipate, aritmetică, ID-uri, multiple rezultate instrument, follow-up eșuate |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Căutare fișier, UTF-8, limite de mărime, payload fundamentare, erori intrare și API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Toate cele șase probe, filtre explicite, clasificare refuz, HTTP 400 obișnuit și alte eșecuri |

Pentru o suită, folosiți `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Fixture-urile partajate trăiesc în [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Verificare live secvențială

Apelurile live sunt separate de testele unitare. Folosiți următoarele comenzi **individual**, din rădăcina depozitului, doar după ce credențialele și accesul la implementare sunt pregătite. Nu sunt necesare servicii sau procese persistente.

Pentru o implementare comună **10 cereri/minut**, rezervați cotă suficientă pentru întreg programul următor înainte de lansare: 5, 4, 1, apoi 6 cereri. Procesele secvențiale singure nu garantează conformitatea cu limita de rată. Coordonați minutul rulant cu toți ceilalți apelanți; nu lipiți cele patru invocări ca un batch nepauzat.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completări, multi-turn, și două ture interactive:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Verificați toate cele trei titluri de secțiuni, cinci răspunsuri, un răspuns final interactiv care îl amintește pe Ada, `La revedere!` și codul de ieșire 0. Buget: **5 cereri, cel mult 1.900 tokenuri pentru completare**. Pentru o rulare mai mică, redirecționați doar `exit`: 3 cereri / 900 tokenuri, dar aceasta nu testează inferența interactivă.

**2. Ambele fluxuri de lucru call-function:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Verificați ambele nume de funcții, vremea simulată din Seattle, rezultatul calculat 36, două răspunsuri finale și codul de ieșire 0. Buget: **4 cereri, cel mult 1.200 tokenuri pentru completare**.

**3. Răspuns bazat pe document:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Verificați calea documentului, un răspuns care menționează Microsoft Entra ID și codul de ieșire 0. Buget: **1 cerere, cel mult 500 tokenuri pentru completare**. Fișierul existent [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) este singurul fișier de intrare necesar. O rulare opțională a doua care întreabă despre un subiect absent trebuie să se abțină și adaugă o cerere / 500 tokenuri.

**4. Observații Responsible-AI:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Verificați șase categorii și rezumatul observațional, revizuiți conținutul generat și solicitați cod de ieșire 0 pentru finalizarea tehnică. O ieșire de proces reușită nu certifică siguranța modelului. Buget: **6 cereri, cel mult 1.800 tokenuri pentru completare**.

**Total pentru cele patru comenzi: 16 cereri de chat și cel mult 5.400 tokenuri pentru completare**, plus tokenuri de intrare (inclusiv conversația repetată și schema/istoricul de unelte). Nu există cereri de embedding. Utilizarea reală a tokenurilor depinde de model și poate fi mai mică, în special pentru prompturi filtrate. Costul în dolari depinde de prețurile de implementare; nu se implică o estimare fixă în bani. Toate limitele de cereri presupun absenta rerulărilor manuale. Inspectați `$LASTEXITCODE` imediat după fiecare comandă; nonzero înseamnă că rularea nu s-a finalizat cu succes.

## Depanare

- **Endpoint lipsă / 401 / 403:** Setați endpoint-ul în procesul de lansare, verificați autentificarea locală Azure și rolul restricționat la resurse, și verificați dacă nu există suprascrieri de mediu de identitate neintenționate.
- **400 / 404:** Confirmați că implementarea există și suportă Chat Completions cu efort de raționament `none`. Folosiți rădăcina resursei HTTPS sau URL-ul `/openai/v1`, nu un URL vechi de implementare. Erorile obișnuite 400 sunt eșecuri tehnice, nu blocaje de siguranță.
- **429:** Coordonați RPM-ul și cota de tokenuri împărțite înainte de a reîncerca. Exemplele nu reîncercă automat intenționat.
- **`Răspuns chat incomplet: lungime`:** Outputul a atins limita de completare. Revizuiți răspunsul și promptul înainte de a crește limita și bugetul documentat; nu înregistrați ca reușită o rulare tăiată.
- **Erori de fișier sau stdin:** Lansați dintr-un director suportat sau transmiteți o cale explicită a documentului. Oferiți o întrebare nonblankă pentru reader. Completările se pot încheia normal la EOF sau `exit`.
- **Erori de compilare:** Verificați Java 21 sau versiuni ulterioare, apoi rulați `mvn -B -ntp clean test`. În PowerShell, puneți între ghilimele întregul argument Maven care conține o proprietate cu punct, de exemplu `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Pașii următori

Continuați la [Capitolul 4: Exemple practice](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->