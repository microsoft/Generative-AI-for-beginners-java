<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T16:14:17+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "sk"
}
-->
# Praktické aplikácie a projekty

## Čo sa naučíte
V tejto sekcii si ukážeme tri praktické aplikácie, ktoré predstavujú vzory vývoja generatívnej AI s použitím Java:
- Vytvorenie multimodálneho generátora príbehov o domácich miláčikoch, ktorý kombinuje AI na strane klienta a servera
- Implementácia integrácie lokálneho AI modelu pomocou ukážky Foundry Local Spring Boot
- Vývoj služby Model Context Protocol (MCP) s príkladom kalkulačky

## Obsah

- [Úvod](../../../04-PracticalSamples)
  - [Ukážka Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Generátor príbehov o domácich miláčikoch](../../../04-PracticalSamples)
  - [MCP Kalkulačková služba (Jednoduchá ukážka MCP)](../../../04-PracticalSamples)
- [Postup učenia](../../../04-PracticalSamples)
- [Zhrnutie](../../../04-PracticalSamples)
- [Ďalšie kroky](../../../04-PracticalSamples)

## Úvod

Táto kapitola predstavuje **ukážkové projekty**, ktoré demonštrujú vzory vývoja generatívnej AI s použitím Java. Každý projekt je plne funkčný a ukazuje konkrétne AI technológie, architektonické vzory a osvedčené postupy, ktoré môžete prispôsobiť pre svoje vlastné aplikácie.

### Ukážka Foundry Local Spring Boot

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** ukazuje, ako integrovať lokálne AI modely pomocou **OpenAI Java SDK**. Demonštruje pripojenie k modelu **Phi-3.5-mini**, ktorý beží na Foundry Local, čo vám umožňuje spúšťať AI aplikácie bez závislosti na cloudových službách.

### Generátor príbehov o domácich miláčikoch

**[Generátor príbehov o domácich miláčikoch](petstory/README.md)** je pútavá webová aplikácia Spring Boot, ktorá demonštruje **multimodálne AI spracovanie** na generovanie kreatívnych príbehov o domácich miláčikoch. Kombinuje AI na strane klienta a servera pomocou transformer.js pre interakcie v prehliadači a OpenAI SDK pre spracovanie na strane servera.

### MCP Kalkulačková služba (Jednoduchá ukážka MCP)

**[MCP Kalkulačková služba](calculator/README.md)** je jednoduchá ukážka **Model Context Protocol (MCP)** s použitím Spring AI. Poskytuje priateľský úvod do konceptov MCP, pričom ukazuje, ako vytvoriť základný MCP server, ktorý komunikuje s MCP klientmi.

## Postup učenia

Tieto projekty sú navrhnuté tak, aby nadväzovali na koncepty z predchádzajúcich kapitol:

1. **Začnite jednoducho**: Začnite s ukážkou Foundry Local Spring Boot, aby ste pochopili základnú integráciu AI s lokálnymi modelmi
2. **Pridajte interaktivitu**: Pokračujte s Generátorom príbehov o domácich miláčikoch pre multimodálne AI a interakcie na webe
3. **Naučte sa základy MCP**: Vyskúšajte MCP Kalkulačkovú službu, aby ste pochopili základy Model Context Protocol

## Zhrnutie

Skvelá práca! Teraz ste preskúmali niekoľko reálnych aplikácií:

- Multimodálne AI zážitky, ktoré fungujú v prehliadači aj na serveri
- Integráciu lokálnych AI modelov pomocou moderných Java frameworkov a SDK
- Vašu prvú službu Model Context Protocol, aby ste videli, ako nástroje integrujú AI

## Ďalšie kroky

[Kap. 5: Zodpovedná generatívna AI](../05-ResponsibleGenAI/README.md)

**Zrieknutie sa zodpovednosti**:  
Tento dokument bol preložený pomocou služby AI prekladu [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, prosím, berte na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nenesieme zodpovednosť za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.