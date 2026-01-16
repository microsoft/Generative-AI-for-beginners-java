<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "75bfb080ca725e8a9aa9c80cae25fba1",
  "translation_date": "2025-07-29T10:20:46+00:00",
  "source_file": "01-IntroToGenAI/README.md",
  "language_code": "hr"
}
-->
# Uvod u Generativnu AI - Java izdanje

## Što ćete naučiti

- **Osnove generativne AI** uključujući LLM-ove, inženjering upita, tokene, ugrađivanja i vektorske baze podataka
- **Usporedba alata za razvoj AI u Javi** uključujući Azure OpenAI SDK, Spring AI i OpenAI Java SDK
- **Otkrijte Model Context Protocol** i njegovu ulogu u komunikaciji AI agenata

## Sadržaj

- [Uvod](../../../01-IntroToGenAI)
- [Brzi pregled koncepata generativne AI](../../../01-IntroToGenAI)
- [Pregled inženjeringa upita](../../../01-IntroToGenAI)
- [Tokeni, ugrađivanja i agenti](../../../01-IntroToGenAI)
- [Alati i biblioteke za razvoj AI u Javi](../../../01-IntroToGenAI)
  - [OpenAI Java SDK](../../../01-IntroToGenAI)
  - [Spring AI](../../../01-IntroToGenAI)
  - [Azure OpenAI Java SDK](../../../01-IntroToGenAI)
- [Sažetak](../../../01-IntroToGenAI)
- [Sljedeći koraci](../../../01-IntroToGenAI)

## Uvod

Dobrodošli u prvo poglavlje Generativne AI za početnike - Java izdanje! Ova osnovna lekcija uvodi vas u ključne koncepte generativne AI i kako raditi s njima koristeći Javu. Naučit ćete o osnovnim građevnim blokovima AI aplikacija, uključujući Velike Jezične Modele (LLM-ove), tokene, ugrađivanja i AI agente. Također ćemo istražiti osnovne Java alate koje ćete koristiti tijekom ovog tečaja.

### Brzi pregled koncepata generativne AI

Generativna AI je vrsta umjetne inteligencije koja stvara novi sadržaj, poput teksta, slika ili koda, na temelju obrazaca i odnosa naučenih iz podataka. Generativni AI modeli mogu generirati odgovore slične ljudskima, razumjeti kontekst, a ponekad čak i stvoriti sadržaj koji djeluje kao da ga je stvorio čovjek.

Dok razvijate svoje Java AI aplikacije, radit ćete s **generativnim AI modelima** za stvaranje sadržaja. Neke od mogućnosti generativnih AI modela uključuju:

- **Generiranje teksta**: Pisanje teksta sličnog ljudskom za chatbotove, sadržaj i dovršavanje teksta.
- **Generiranje i analiza slika**: Stvaranje realističnih slika, poboljšanje fotografija i prepoznavanje objekata.
- **Generiranje koda**: Pisanje isječaka koda ili skripti.

Postoje specifične vrste modela optimizirane za različite zadatke. Na primjer, i **Mali Jezični Modeli (SLM-ovi)** i **Veliki Jezični Modeli (LLM-ovi)** mogu obraditi generiranje teksta, pri čemu LLM-ovi obično nude bolje performanse za složenije zadatke. Za zadatke vezane uz slike koristili biste specijalizirane modele za viziju ili multimodalne modele.

![Slika: Vrste generativnih AI modela i primjene.](../../../translated_images/hr/llms.225ca2b8a0d34473.webp)

Naravno, odgovori ovih modela nisu uvijek savršeni. Vjerojatno ste čuli za to da modeli "haluciniraju" ili generiraju netočne informacije na autoritativan način. No, možete pomoći modelu da generira bolje odgovore pružanjem jasnih uputa i konteksta. Ovdje dolazi do izražaja **inženjering upita**.

#### Pregled inženjeringa upita

Inženjering upita je praksa dizajniranja učinkovitih ulaza kako bi se AI modeli usmjerili prema željenim izlazima. To uključuje:

- **Jasnoću**: Činjenje uputa jasnima i nedvosmislenima.
- **Kontekst**: Pružanje potrebnih pozadinskih informacija.
- **Ograničenja**: Navođenje bilo kakvih ograničenja ili formata.

Neke od najboljih praksi za inženjering upita uključuju dizajn upita, jasne upute, razlaganje zadataka, učenje iz jednog ili nekoliko primjera i podešavanje upita. Testiranje različitih upita ključno je za pronalaženje onoga što najbolje funkcionira za vaš specifični slučaj.

Prilikom razvoja aplikacija radit ćete s različitim vrstama upita:
- **Sistemski upiti**: Postavljaju osnovna pravila i kontekst za ponašanje modela
- **Korisnički upiti**: Ulazni podaci od korisnika vaše aplikacije
- **Asistentski upiti**: Odgovori modela temeljeni na sistemskim i korisničkim upitima

> **Saznajte više**: Saznajte više o inženjeringu upita u [Poglavlju o inženjeringu upita tečaja GenAI za početnike](https://github.com/microsoft/generative-ai-for-beginners/tree/main/04-prompt-engineering-fundamentals)

#### Tokeni, ugrađivanja i agenti

Prilikom rada s generativnim AI modelima susrest ćete pojmove poput **tokena**, **ugrađivanja**, **agenata** i **Model Context Protocol (MCP)**. Evo detaljnog pregleda ovih koncepata:

- **Tokeni**: Tokeni su najmanje jedinice teksta u modelu. Oni mogu biti riječi, znakovi ili podriječi. Tokeni se koriste za predstavljanje tekstualnih podataka u formatu koji model može razumjeti. Na primjer, rečenica "The quick brown fox jumped over the lazy dog" može se tokenizirati kao ["The", " quick", " brown", " fox", " jumped", " over", " the", " lazy", " dog"] ili ["The", " qu", "ick", " br", "own", " fox", " jump", "ed", " over", " the", " la", "zy", " dog"] ovisno o strategiji tokenizacije.

![Slika: Primjer tokena generativne AI koji razlažu riječi na tokene](../../../01-IntroToGenAI/images/tokens.webp)

Tokenizacija je proces razbijanja teksta na ove manje jedinice. Ovo je ključno jer modeli rade s tokenima, a ne s izvornim tekstom. Broj tokena u upitu utječe na duljinu i kvalitetu odgovora modela, jer modeli imaju ograničenja na broj tokena u svom kontekstualnom prozoru (npr. 128K tokena za ukupni kontekst GPT-4, uključujući ulaz i izlaz).

  U Javi možete koristiti biblioteke poput OpenAI SDK-a za automatsko rukovanje tokenizacijom prilikom slanja zahtjeva AI modelima.

- **Ugrađivanja**: Ugrađivanja su vektorske reprezentacije tokena koje hvataju semantičko značenje. To su numeričke reprezentacije (obično nizovi brojeva s pomičnim zarezom) koje omogućuju modelima razumijevanje odnosa između riječi i generiranje kontekstualno relevantnih odgovora. Slične riječi imaju slična ugrađivanja, omogućujući modelu razumijevanje koncepata poput sinonima i semantičkih odnosa.

![Slika: Ugrađivanja](../../../translated_images/hr/embedding.398e50802c0037f9.webp)

  U Javi možete generirati ugrađivanja koristeći OpenAI SDK ili druge biblioteke koje podržavaju generiranje ugrađivanja. Ova ugrađivanja su ključna za zadatke poput semantičkog pretraživanja, gdje želite pronaći sličan sadržaj na temelju značenja, a ne točnih podudaranja teksta.

- **Vektorske baze podataka**: Vektorske baze podataka su specijalizirani sustavi za pohranu optimizirani za ugrađivanja. Omogućuju učinkovito pretraživanje sličnosti i ključne su za obrasce generiranja uz pomoć pretraživanja (RAG), gdje trebate pronaći relevantne informacije iz velikih skupova podataka na temelju semantičke sličnosti, a ne točnih podudaranja.

![Slika: Arhitektura vektorske baze podataka koja prikazuje kako se ugrađivanja pohranjuju i dohvaćaju za pretraživanje sličnosti.](../../../translated_images/hr/vector.f12f114934e223df.webp)

> **Napomena**: U ovom tečaju nećemo pokrivati vektorske baze podataka, ali smatramo da ih vrijedi spomenuti jer se često koriste u stvarnim aplikacijama.

- **Agenti i MCP**: AI komponente koje autonomno komuniciraju s modelima, alatima i vanjskim sustavima. Model Context Protocol (MCP) pruža standardizirani način za agente da sigurno pristupaju vanjskim izvorima podataka i alatima. Saznajte više u našem [MCP za početnike](https://github.com/microsoft/mcp-for-beginners) tečaju.

U Java AI aplikacijama koristit ćete tokene za obradu teksta, ugrađivanja za semantičko pretraživanje i RAG, vektorske baze podataka za dohvaćanje podataka te agente s MCP-om za izgradnju inteligentnih sustava koji koriste alate.

![Slika: kako upit postaje odgovor—tokeni, vektori, opcionalno RAG pretraživanje, razmišljanje LLM-a i MCP agent sve u jednom brzom tijeku.](../../../translated_images/hr/flow.f4ef62c3052d12a8.webp)

### Alati i biblioteke za razvoj AI u Javi

Java nudi izvrsne alate za razvoj AI. Tri glavne biblioteke koje ćemo istražiti tijekom ovog tečaja su OpenAI Java SDK, Azure OpenAI SDK i Spring AI.

Evo brze referentne tablice koja prikazuje koji se SDK koristi u primjerima svakog poglavlja:

| Poglavlje | Primjer | SDK |
|-----------|---------|-----|
| 02-PostavljanjeRazvojnogOkruženja | github-models | OpenAI Java SDK |
| 02-PostavljanjeRazvojnogOkruženja | basic-chat-azure | Spring AI Azure OpenAI |
| 03-OsnovneTehnikeGenerativneAI | primjeri | Azure OpenAI SDK |
| 04-PraktičniPrimjeri | petstory | OpenAI Java SDK |
| 04-PraktičniPrimjeri | foundrylocal | OpenAI Java SDK |
| 04-PraktičniPrimjeri | calculator | Spring AI MCP SDK + LangChain4j |

**Poveznice na dokumentaciju SDK-a:**
- [Azure OpenAI Java SDK](https://github.com/Azure/azure-sdk-for-java/tree/azure-ai-openai_1.0.0-beta.16/sdk/openai/azure-ai-openai)
- [Spring AI](https://docs.spring.io/spring-ai/reference/)
- [OpenAI Java SDK](https://github.com/openai/openai-java)
- [LangChain4j](https://docs.langchain4j.dev/)

#### OpenAI Java SDK

OpenAI SDK je službena Java biblioteka za OpenAI API. Pruža jednostavno i konzistentno sučelje za interakciju s OpenAI modelima, čineći integraciju AI mogućnosti u Java aplikacije jednostavnom. Primjeri GitHub modela iz Poglavlja 2, aplikacija Pet Story iz Poglavlja 4 i primjer Foundry Local demonstriraju pristup OpenAI SDK-a.

#### Spring AI

Spring AI je sveobuhvatan okvir koji donosi AI mogućnosti u Spring aplikacije, pružajući konzistentan sloj apstrakcije preko različitih AI pružatelja. Besprijekorno se integrira s Spring ekosustavom, čineći ga idealnim izborom za poslovne Java aplikacije koje trebaju AI mogućnosti.

Snaga Spring AI-a leži u njegovoj besprijekornoj integraciji sa Spring ekosustavom, čineći izgradnju produkcijski spremnih AI aplikacija jednostavnom uz poznate Spring obrasce poput ubrizgavanja ovisnosti, upravljanja konfiguracijom i testnih okvira. Koristit ćete Spring AI u Poglavljima 2 i 4 za izgradnju aplikacija koje koriste i OpenAI i Model Context Protocol (MCP) Spring AI biblioteke.

##### Model Context Protocol (MCP)

[Model Context Protocol (MCP)](https://modelcontextprotocol.io/) je novi standard koji omogućuje AI aplikacijama sigurnu interakciju s vanjskim izvorima podataka i alatima. MCP pruža standardizirani način za AI modele da pristupe kontekstualnim informacijama i izvršavaju radnje u vašim aplikacijama.

U Poglavlju 4 izgradit ćete jednostavnu MCP kalkulator uslugu koja demonstrira osnove Model Context Protocol-a sa Spring AI-om, pokazujući kako stvoriti osnovne integracije alata i arhitekture usluga.

#### Azure OpenAI Java SDK

Azure OpenAI klijentska biblioteka za Javu je prilagodba OpenAI REST API-ja koja pruža idiomatsko sučelje i integraciju s ostatkom Azure SDK ekosustava. U Poglavlju 3 izgradit ćete aplikacije koristeći Azure OpenAI SDK, uključujući chat aplikacije, pozivanje funkcija i RAG (Retrieval-Augmented Generation) obrasce.

> Napomena: Azure OpenAI SDK zaostaje za OpenAI Java SDK-om u pogledu značajki, pa za buduće projekte razmotrite korištenje OpenAI Java SDK-a.

## Sažetak

To je kraj osnova! Sada razumijete:

- Ključne koncepte iza generativne AI - od LLM-ova i inženjeringa upita do tokena, ugrađivanja i vektorskih baza podataka
- Vaše opcije alata za razvoj AI u Javi: Azure OpenAI SDK, Spring AI i OpenAI Java SDK
- Što je Model Context Protocol i kako omogućuje AI agentima rad s vanjskim alatima

## Sljedeći koraci

[Poglavlje 2: Postavljanje razvojnog okruženja](../02-SetupDevEnvironment/README.md)

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden koristeći AI uslugu za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati mjerodavnim izvorom. Za ključne informacije preporučuje se profesionalni prijevod od strane stručnjaka. Ne preuzimamo odgovornost za bilo kakve nesporazume ili pogrešne interpretacije proizašle iz korištenja ovog prijevoda.