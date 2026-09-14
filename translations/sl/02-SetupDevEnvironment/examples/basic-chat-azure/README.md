# Osnovni klepet z Azure AI Foundry - primer od začetka do konca

Ta primer je enostavna aplikacija Spring Boot, ki se poveže z modelom **Azure AI Foundry** z uporabo **avtentikacije brez ključa** (Microsoft Entra ID) in preizkusi vašo nastavitev. Uporablja Spring AI-jeve `ChatClient`, ki temelji na **uradnem OpenAI Java SDK** in na končni točki **Azure OpenAI v1**.

Verzije v [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) so Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** in dotenv-java **3.2.0**. Primer uporablja `spring-ai-starter-model-openai` in izrecno navaja `openai-java` in `azure-identity`; Spring AI 2 je odstranil stari Azure OpenAI starter.

## Kazalo

- [Pogoji](#pogoji)
- [Hiter začetek](#hiter-začetek)
- [Kako deluje avtentikacija](#kako-deluje-avtentikacija)
- [Zagon aplikacije](#zagon-aplikacije)
  - [Uporaba Mavena](#uporaba-mavena)
  - [Uporaba VS Code](#uporaba-vs-code)
  - [Pričakovan izhod](#pričakovan-izhod)
- [Referenca konfiguracije](#referenca-konfiguracije)
  - [Spremenljivke okolja](#spremenljivke-okolja)
  - [Spring konfiguracija](#spring-konfiguracija)
- [Reševanje težav](#reševanje-težav)
  - [Pogoste težave](#pogoste-težave)
  - [Način razhroščevanja](#način-razhroščevanja)
- [Naslednji koraki](#naslednji-koraki)
- [Viri](#viri)

## Pogoji

Pred izvajanjem tega primera zagotovite:

- Sredstvo Azure AI Foundry z razmestitvijo `gpt-5.6-luna` - zagotovite ga z `azd up` ali ročno preko [Azure AI Foundry vodnika za nastavitev](../../getting-started-azure-openai.md)
- Vlogo **Cognitive Services OpenAI User** na tem viru (Bicep predloge to samodejno dodelijo)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), prijavljen z `az login`
- Java 21+ in Maven 3.9+

> **Brez API ključa** — avtentikacija je brezključno preko Microsoft Entra ID.

## Hiter začetek

```bash
# 1. Pojdi do projekta
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Prijavi se, da lahko keyless avtentikacija pridobi žeton
az login

# 3. Konfiguriraj končno točko
#    - Če ste zagnali `azd up`, je bila datoteka .env ustvarjena za vas (to preskoči).
#    - V nasprotnem primeru kopiraj predlogo in nastavi AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Zaženi aplikacijo
mvn spring-boot:run
```

## Kako deluje avtentikacija

Ta primer se avtenticira z **Microsoft Entra ID** — ni potrebnega API ključa.

Aplikacija izrecno nastavi avtentikacijo v [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` ustvari `BearerTokenCredential` z uporabo `AuthenticationUtil.getBearerTokenSupplier` s `DefaultAzureCredential` in dovoljenjem `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` zgradi `OpenAIClient` z `OpenAIOkHttpClient.builder()`, določi končno točko vira na `/openai/v1` in doda žeton za avtentikacijo z `.credential(...)`.
3. `azureChatModel()` poda tega klienta Spring AI-jevemu `OpenAiChatModel`, ki podpira `ChatClient` v lekciji.

Ti izrecni komponenti preprečujejo, da bi globalni `OPENAI_API_KEY` prekril Azure avtentikacijo. Samo izpustitev API ključa v YAML ni nastavitve avtentikacije. `DefaultAzureCredential` lahko lokalno uporabi vašo sejo `az login` ali upravljano identiteto v Azure; izbrana identiteta mora imeti zgoraj navedeno vlogo.

## Zagon aplikacije

### Uporaba Mavena

```bash
mvn spring-boot:run
```

### Uporaba VS Code

1. Odprite projekt v VS Code
2. Pritisnite `F5` ali uporabite panel "Zaženi in razhrošči"
3. Izberite konfiguracijo "Spring Boot-BasicChatApplication"

> **Opomba**: Aplikacija naloži `.env` iz svojega delovnega imenika, tudi če jo zaženete iz VS Code.

### Pričakovan izhod

Primer izhoda po uspešnem zagonu (izpusti zagonske zapise; besedilo odziva se razlikuje):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Referenca konfiguracije

### Spremenljivke okolja

| Spremenljivka | Opis | Obvezno | Primer |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL končne točke Foundry (Azure OpenAI) | Da | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Ime razmestitve klepetalnega modela | Ne | `gpt-5.6-luna` (privzeto) |

> Ni spremenljivke za API ključ — avtentikacija je brezključno (Microsoft Entra ID preko `az login`).

### Spring konfiguracija

Nastavitve v [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) uporabljajo predpono `spring.ai.openai` in poenostavljene lastnosti klepeta (brez bloka `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` je **ime Azure razmestitve**. Avtentikacija prihaja iz zgoraj opisanih izrecnih komponent, ne iz nastavitve `api-key`. Lekcija izključi sklepanje in omeji število zaključenih žetonov na 500; pusti `temperature` in staro `max-tokens` nespremenjeno.

Microsoft priporoča [uradni OpenAI SDK z Azure OpenAI v1 in API za odgovore za nove aplikacije](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Funkcija Chat Completions ostaja podprta za to obstoječo lekcijo na osnovi sporočil. Za GPT-5.6 morajo zahteve z orodji na Chat Completions nastaviti `reasoning_effort` na `none`; pri kombinaciji sklepanja in orodij uporabite Responses. Oglejte si [klic orodij z modeli za sklepanje](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Reševanje težav

### Pogoste težave

<details>
<summary><strong>Napaka: 401 / "PermissionDenied" / napake s tokenom</strong></summary>

- Zaženite `az login` — avtentikacija brez ključa potrebuje aktivno prijavo za pridobitev žetona
- Preverite, da ima vaš račun vlogo **Cognitive Services OpenAI User** na viru
- Če ste pravkar dodelili vlogo, počakajte minuto, da se razširi
- Potrdite, da ste v pravem najemniku/računu (`az account show`)
</details>

<details>
<summary><strong>Napaka: "Končna točka ni veljavna" / težave s povezavo</strong></summary>

- Prepričajte se, da je `AZURE_OPENAI_ENDPOINT` popoln osnovni URL (npr. `https://your-resource.openai.azure.com/`)
- Preverite skladnost zaključnega poševnika
- Potrdite, da končna točka ustreza vašemu viri (`azd env get-values`)
</details>

<details>
<summary><strong>Napaka: "Razmestitev ni bila najdena"</strong></summary>

- Preverite, da `AZURE_OPENAI_DEPLOYMENT` ustreza imenu razmestitve v Azure
- Preverite, da je model uspešno razmeščen in aktiven
- Privzeto ime razmestitve je `gpt-5.6-luna`
</details>

<details>
<summary><strong>Napaka: 429 / presežena omejitev hitrosti</strong></summary>

- Privzeta razmestitev GPT-5.6 Luna ima Global Standard kapaciteto 10: 10 zahtev/minuto in 10.000 žetonov/minuto
- Zaženite primere zaporedno in počakajte interval ponovnega poizkusa storitve pred ponovitvijo
- Ta osnovni primer izključi samodejne ponovitve v SDK, tako da se neuspešna zahteva takoj prijavi
</details>

<details>
<summary><strong>VS Code: Spremenljivke okolja se ne nalagajo</strong></summary>

- Prepričajte se, da je vaša `.env` datoteka v korenski mapi projekta (na isti ravni kot `pom.xml`)
- Poskusite zagnati `mvn spring-boot:run` v integriranem terminalu VS Code
- Preverite, da je razširitev za Java v VS Code pravilno nameščena
</details>

### Način razhroščevanja

Za omogočanje podrobnega zapisovanja odkomentirajte te vrstice v [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Naslednji koraki

**Namestitev končana!** Nadaljujte svojo učno pot:

[Poglavje 3: Osnovne tehnike generativne umetne inteligence](../../../03-CoreGenerativeAITechniques/README.md)

## Viri

- [Prehod na Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Uradni OpenAI Java SDK z Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Avtentikacija brez ključa z Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Dokumentacija Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba obravnavati kot avtoritativni vir. Za kritične informacije je priporočljiv strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->