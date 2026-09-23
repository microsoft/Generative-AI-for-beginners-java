# Tutorial Generator de Povești cu Animale de Companie pentru Începători

Încarcă o fotografie cu un animal de companie, analizeaz-o cu GPT-5.6 Luna și generează o poveste pornind de la descrierea rezultată. Ambele cereri către model folosesc `reasoning_effort: none`.

| Componentă | Versiune |
| --- | --- |
| Java | 21 sau mai nou |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Cuprins

- [Cerințe](#cerințe)
- [Înțelegerea Structurii Proiectului](#înțelegerea-structurii-proiectului)
- [Explicarea Componentelelor Cheie](#explicarea-componentelelor-cheie)
  - [1. Aplicația Principală](#1-aplicația-principală)
  - [2. Controler Web](#2-controler-web)
  - [3. Serviciul Povestirii](#3-serviciul-povestirii)
  - [4. Șabloanele Web](#4-șabloane-web)
  - [5. Configurarea](#5-configurare)
- [Pornirea Aplicației](#pornirea-aplicației)
- [Teste Offline](#teste-offline)
- [Cum Funcționează Totul Împreună](#cum-funcționează-totul-împreună)
- [Înțelegerea Integrării AI](#înțelegerea-integrării-ai)
- [Următorii Pași](#următorii-pași)

## Cerințe

Înainte de a începe, asigură-te că ai:
- Java 21 sau versiune mai nouă instalată
- Maven pentru gestionarea dependențelor
- O implementare Azure AI Foundry a GPT-5.6 Luna numită `gpt-5.6-luna`, sau o suprascriere `AZURE_OPENAI_DEPLOYMENT` care indică către acea implementare. Vezi [Capitolul 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) pentru aprovizionare și autentifică-te cu `az login` pentru autentificare fără cheie. Implementarea trebuie să suporte input de imagine și `reasoning_effort: none`.
- Cunoștințe de bază despre Java, Spring Boot și dezvoltare web

## Înțelegerea Structurii Proiectului

Proiectul poveștii cu animale de companie conține câteva fișiere importante:

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

## Explicarea Componentelelor Cheie

### 1. Aplicația Principală

**Fișier:** `PetStoryApplication.java`

Acesta este punctul de intrare pentru aplicația noastră Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ce face asta:**
- Anotarea `@SpringBootApplication` activează configurarea automată și scanarea componentelor
- Pornește un server web încorporat (Tomcat) pe portul 8080
- Creează automat toate bean-urile și serviciile Spring necesare

### 2. Controler Web

**Fișier:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Cerere | Răspuns cu succes |
| --- | --- | --- |
| `GET /` | Fără corp | Formular HTML de încărcare cu token CSRF |
| `POST /analyze-image` | `multipart/form-data`, câmp fișier `image` | JSON: `{"description":"Un animal jucăuș..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, câmp `description` | Pagină HTML cu rezultatul, descrierea și povestea generată |

Ambele endpoint-uri POST cer cookie-ul de sesiune și tokenul CSRF obținut de la `GET /`. Scriptul de încărcare trimite valoarea ascunsă `_csrf` în antetul `X-CSRF-TOKEN`; trimiterea poveștii îl trimite ca câmp de formular `_csrf`. Clienții API trebuie să păstreze cookie-ul între cereri. Acestea sunt endpoint-uri pentru formulare, nu pentru cereri JSON.

Descrierile trebuie să fie nevid și să nu depășească 1000 de caractere. Controlerul taie descrierea și elimină `<`, `>`, ghilimele duble, apostrof și `&` înainte de a o trimite către serviciu. Șablonul rezultat escapează și el ieșirea modelului cu `th:text`.

Eșecurile de validare a imaginii returnează HTTP 400 cu un câmp `error`; eșecurile modelului returnează HTTP 502 cu un câmp `error` și fără `description`. Descrierile de poveste invalidă sau eșecurile modelului redirecționează către `/` cu un mesaj de eroare vizibil. Câmpurile obligatorii lipsă returnează HTTP 400, iar tokenurile CSRF lipsă sau invalide returnează HTTP 403. Nu sunt prezentate descrieri sau povești de rezervă ca rezultate reușite ale AI-ului.

### 3. Serviciul Povestirii

**Fișier:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

SDK-ul oficial OpenAI Java 4.63.1 apelează API-ul de Chat Completions compatibil OpenAI al Azure AI Foundry. Azure Identity 1.18.6 furnizează un token de tip bearer Microsoft Entra prin `DefaultAzureCredential`; nu este necesară o cheie API.

| Operațiune | Intrare | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Octeți imagine codificați ca URL de date base64 cu tipul MIME încărcat | 300 |
| `generateStory` | O descriere a animalului într-un mesaj de utilizator | 800 |

Ambele cereri folosesc implementarea configurată, implicit `gpt-5.6-luna`, și setează explicit `ReasoningEffort.NONE` (`reasoning_effort: none`). Niciuna nu trimite parametrul `temperature` sau parametrul vechi `max_tokens`.

Analiza imaginii acceptă JPEG, PNG, GIF și WebP, respinge imagini goale și fișiere mai mari de 10MB și limitează descrierea rezultată la 1000 de caractere. Promptul pentru poveste cere o poveste scurtă potrivită pentru întreaga familie. Opțiuni goale sau conținutul modelului gol sunt erori, iar eșecurile păstrează cauza originală pentru diagnosticare pe server. Clientul SDK se închide la oprirea aplicației.

### 4. Șabloane Web

**Fișier:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formular de încărcare)

Pagina începe cu un selector de fotografie, nu cu o zonă de text pentru descriere. **Analyze Image** afișează previzualizarea fotografiei selectate și o postează la `/analyze-image`. Un răspuns cu succes afișează descrierea, completează câmpul ascuns `description` și dezvăluie butonul **Generate Story**. Acest buton trimite formularul existent către `/generate-story`.

Nu există niciun model descărcat în browser sau dependență CDN. Analiza imaginii rulează pe server prin implementarea Azure configurată. Eșecurile rămân vizibile și nu permit generarea poveștii cu o descriere fabricată. Selectarea unui fișier diferit șterge analiza anterioară.

**Fișier:** `result.html` (Afișarea Povestirii)

Afișează povestea generată:

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

**Caracteristici șablon:**

1. **Integrare Thymeleaf**: Folosește atribute `th:` pentru conținut dinamic
2. **Design Responsiv**: Stilizare CSS pentru mobil și desktop
3. **Gestionare Erori**: Afișează erori de validare utilizatorilor
4. **Manipulare Încărcare**: JavaScript previzualizează fotografia, trimite o cerere multipart protejată CSRF și afișează descrierea returnată

### 5. Configurare

**Fișier:** `application.properties`

Setări de configurare pentru aplicație:

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

**Explicație configurare:**

1. **Încărcare Fișier**: atât fișierul cât și cererea multipart completă sunt limitate la 10MB; păstrează fotografiile sub această limită pentru a lăsa spațiu pentru headere multipart
2. **Logare**: controlează ce informații se loghează în timpul execuției
3. **Azure AI Foundry**: specifică endpoint-ul și implementarea modelului de folosit (autentificare fără cheie)
4. **Securitate**: protecția CSRF rămâne activă; diagnostice model sunt logate pe server, iar controlerul afișează mesaje generice pentru eșecurile modelului

## Pornirea Aplicației

### Pasul 1: Autentifică-te și Setează Endpoint-ul

Autentificarea este fără cheie (Microsoft Entra ID), deci nu există o cheie API. Autentifică-te și setează endpoint-ul Foundry:

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

**De ce este necesar:**
- Azure AI Foundry folosește Microsoft Entra ID pentru autentificarea cererilor de inferență
- Autentificarea fără cheie înseamnă că nu există secrete în codul sursă sau în mediu
- Contul tău trebuie să aibă rolul **Cognitive Services OpenAI User** pe resursă

Numele implicit al implementării este `gpt-5.6-luna`. Dacă implementarea ta GPT-5.6 Luna are alt nume, setează `AZURE_OPENAI_DEPLOYMENT` în același terminal înainte de a porni aplicația. Atât analiza imaginii, cât și generarea poveștii folosesc această setare.

### Pasul 2: Compilare și Pornire

Navighează în directorul proiectului:
```bash
cd 04-PracticalSamples/petstory
```

Compilează JAR-ul executabil standalone și rulează toate testele offline:
```bash
mvn clean package
```

Pornește serverul:
```bash
mvn spring-boot:run
```

Aplicația va porni pe `http://localhost:8080`.

Alternativ, pornește JAR-ul ambalat pe un port liber, de exemplu:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Pentru această comandă, deschide `http://localhost:8083/`. Aceleași rute `/analyze-image` și `/generate-story` sunt disponibile pe portul selectat.

### Pasul 3: Testează Aplicația

1. **Deschide** `http://localhost:8080` în browserul tău
2. **Selectează** o fotografie clară a unui animal de companie în format JPEG, PNG, GIF sau WebP, sub 10MB
3. **Apasă** "Analyze Image" și așteaptă descrierea animalului
4. **Apasă** "Generate Story" după ce analiza reușește
5. **Vizualizează** povestea și folosește link-ul paginii de rezultat pentru a reveni la formularul de încărcare

Fluxul de succes foto-poveste face două apeluri către model, câte unul per buton. Inferența live consumă cota implementării tale și poate genera costuri; rulează testele de bază serial când împărți o implementare cu limită de rată. Încărcarea paginii principale nu apelează modelul.

## Teste Offline

Din directorul sample, rulează:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) capturează cererile reale SDK OpenAI cu un fixture HTTP loopback. Verifică implementarea ambelor cereri, `reasoning_effort: none`, limitele de tokeni, payload-ul de imagine, validarea inputului, răspunsurile goale și erorile upstream.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) folosește MockMvc cu un serviciu model mockuit pentru a testa paginile Thymeleaf renderizate, contractul de încărcare, CSRF, validarea, escape-ul ieșirii și eșecurile vizibile. Aceste teste nu necesită credențiale Azure și niciodată nu apelează inferența Azure plătită. Maven scrie rapoarte Surefire în `target/surefire-reports`.

## Cum Funcționează Totul Împreună

Iată fluxul complet când generezi o poveste cu animalul de companie:

1. **Selectare Fotografie**: Alegi o imagine cu animalul în formularul de încărcare
2. **Încărcare Imagine**: "Analyze Image" trimite un POST multipart către `/analyze-image` cu antetul CSRF
3. **Analiza Imagini**: `StoryService` trimite imaginea către GPT-5.6 Luna cu reasoning setat la `none`
4. **Afișarea Descrierii**: Browserul afișează descrierea primită și o stochează în formular
5. **Trimiterea Povestirii**: "Generate Story" trimite `description` și `_csrf` către `/generate-story`
6. **Generarea Povestirii**: Controlerul validează descrierea și apelează aceeași implementare cu reasoning setat la `none`
7. **Redarea Șablonului**: Thymeleaf escapează și afișează descrierea și povestea pe pagina de rezultat

**Fluxul de Gestionare Erori:**
Dacă modelul eșuează, serverul loghează cauza. Analiza imaginii returnează HTTP 502 și browserul afișează eroarea fără a dezvălui "Generate Story". Generarea poveștii redirecționează către formular cu un mesaj de eroare. Niciuna dintre căi nu substituie silențios un rezultat pre-scris.

## Înțelegerea Integrării AI

### Azure AI Foundry (fără cheie)
Serviciul configurează SDK-ul cu endpoint-ul `/openai/v1/` al resursei tale. `DefaultAzureCredential` și `AuthenticationUtil.getBearerTokenSupplier` furnizează tokenuri Microsoft Entra pentru `https://ai.azure.com/.default`. Dezvoltarea locală poate folosi autentificarea CLI Azure; o aplicație găzduită în Azure poate folosi o identitate gestionată cu permisiunile necesare asupra resursei.

### Ingineria Promptului
Analiza imaginii cere modelului observarea trăsăturilor animalului într-un paragraf scurt și îi spune să trateze textul din imagine ca date, nu ca instrucțiuni. Generarea poveștii folosește descrierea returnată într-o cerere separată, potrivită pentru familie. Niciun apel nu activează reasoning sau setează o valori pentru temperatură.

### Procesarea Răspunsului
Handlerul comun al răspunsului respinge alegerile lipsă și conținutul gol sau format doar din spații albe, taie conținutul valid și păstrează eșecurile upstream. Descrierile imaginii sunt limitate la 1000 de caractere pentru a se potrivi formularului de poveste ulterior. Eșecul original al modelului este păstrat pentru diagnosticare, dar nu este afișat utilizatorului.

## Următorii Pași

Pentru mai multe exemple, vezi [Capitolul 04: Exemple practice](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->