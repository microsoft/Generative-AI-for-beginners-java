<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T12:11:39+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "ro"
}
-->
# Aplicații Practice & Proiecte

## Ce Vei Învăța
În această secțiune vom prezenta trei aplicații practice care ilustrează modele de dezvoltare AI generativă cu Java:
- Crearea unui Generator de Povești pentru Animale de Companie multi-modal, combinând AI pe partea client și server
- Implementarea integrării modelelor AI locale cu demo-ul Foundry Local Spring Boot
- Dezvoltarea unui serviciu Model Context Protocol (MCP) cu exemplul Calculatorului

## Cuprins

- [Introducere](../../../04-PracticalSamples)
  - [Demo Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Generator de Povești pentru Animale de Companie](../../../04-PracticalSamples)
  - [Serviciul MCP Calculator (Demo MCP pentru Începători)](../../../04-PracticalSamples)
- [Progresul Învățării](../../../04-PracticalSamples)
- [Rezumat](../../../04-PracticalSamples)
- [Pași Următori](../../../04-PracticalSamples)

## Introducere

Acest capitol prezintă **proiecte exemplu** care demonstrează modele de dezvoltare AI generativă cu Java. Fiecare proiect este complet funcțional și ilustrează tehnologii AI specifice, modele arhitecturale și bune practici pe care le poți adapta pentru propriile aplicații.

### Demo Foundry Local Spring Boot

**[Demo Foundry Local Spring Boot](foundrylocal/README.md)** demonstrează cum să integrezi modele AI locale folosind **OpenAI Java SDK**. Acesta prezintă conectarea la modelul **Phi-3.5-mini** care rulează pe Foundry Local, permițând rularea aplicațiilor AI fără a depinde de servicii cloud.

### Generator de Povești pentru Animale de Companie

**[Generatorul de Povești pentru Animale de Companie](petstory/README.md)** este o aplicație web captivantă Spring Boot care demonstrează **procesarea AI multi-modală** pentru generarea de povești creative despre animale de companie. Acesta combină capabilități AI pe partea client și server, utilizând transformer.js pentru interacțiuni AI în browser și OpenAI SDK pentru procesare pe server.

### Serviciul MCP Calculator (Demo MCP pentru Începători)

**[Serviciul MCP Calculator](mcp/calculator/README.md)** este o demonstrație simplă a **Model Context Protocol (MCP)** folosind Spring AI. Acesta oferă o introducere prietenoasă în conceptele MCP, arătând cum să creezi un MCP Server de bază care interacționează cu clienți MCP.

## Progresul Învățării

Aceste proiecte sunt concepute pentru a construi pe baza conceptelor din capitolele anterioare:

1. **Începe Simplu**: Începe cu Demo-ul Foundry Local Spring Boot pentru a înțelege integrarea de bază a AI cu modele locale
2. **Adaugă Interactivitate**: Continuă cu Generatorul de Povești pentru Animale de Companie pentru AI multi-modal și interacțiuni bazate pe web
3. **Învață Bazele MCP**: Încearcă Serviciul MCP Calculator pentru a înțelege fundamentele Model Context Protocol

## Rezumat

**Felicitări!** Ai reușit să:

- **Creezi experiențe AI multi-modale** combinând procesarea AI pe partea client și server
- **Implementezi integrarea modelelor AI locale** folosind framework-uri și SDK-uri moderne Java
- **Dezvolți servicii Model Context Protocol** care demonstrează modele de integrare a instrumentelor

## Pași Următori

[Capitolul 5: AI Generativă Responsabilă](../05-ResponsibleGenAI/README.md)

**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să fiți conștienți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa natală ar trebui considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea din utilizarea acestei traduceri.