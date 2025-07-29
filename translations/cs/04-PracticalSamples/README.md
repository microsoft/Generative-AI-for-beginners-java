<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T16:11:14+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "cs"
}
-->
# Praktické aplikace a projekty

## Co se naučíte
V této části si ukážeme tři praktické aplikace, které představují vzory vývoje generativní AI s využitím Javy:
- Vytvoření multimodálního generátoru příběhů o mazlíčcích kombinujícího AI na straně klienta i serveru
- Implementace integrace lokálního AI modelu pomocí ukázky Foundry Local Spring Boot
- Vývoj služby Model Context Protocol (MCP) na příkladu kalkulačky

## Obsah

- [Úvod](../../../04-PracticalSamples)
  - [Ukázka Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Generátor příběhů o mazlíčcích](../../../04-PracticalSamples)
  - [MCP Kalkulačka (Ukázka MCP pro začátečníky)](../../../04-PracticalSamples)
- [Postup učení](../../../04-PracticalSamples)
- [Shrnutí](../../../04-PracticalSamples)
- [Další kroky](../../../04-PracticalSamples)

## Úvod

Tato kapitola představuje **ukázkové projekty**, které demonstrují vzory vývoje generativní AI s využitím Javy. Každý projekt je plně funkční a ukazuje konkrétní AI technologie, architektonické vzory a osvědčené postupy, které můžete přizpůsobit pro své vlastní aplikace.

### Ukázka Foundry Local Spring Boot

**[Ukázka Foundry Local Spring Boot](foundrylocal/README.md)** ukazuje, jak integrovat lokální AI modely pomocí **OpenAI Java SDK**. Demonstruje připojení k modelu **Phi-3.5-mini** běžícímu na Foundry Local, což umožňuje provozovat AI aplikace bez závislosti na cloudových službách.

### Generátor příběhů o mazlíčcích

**[Generátor příběhů o mazlíčcích](petstory/README.md)** je poutavá webová aplikace Spring Boot, která demonstruje **multimodální AI zpracování** pro generování kreativních příběhů o mazlíčcích. Kombinuje AI na straně klienta i serveru pomocí transformer.js pro interakce v prohlížeči a OpenAI SDK pro zpracování na straně serveru.

### MCP Kalkulačka (Ukázka MCP pro začátečníky)

**[MCP Kalkulačka](calculator/README.md)** je jednoduchá ukázka **Model Context Protocol (MCP)** s využitím Spring AI. Poskytuje přívětivý úvod do konceptů MCP a ukazuje, jak vytvořit základní MCP Server, který komunikuje s MCP klienty.

## Postup učení

Tyto projekty jsou navrženy tak, aby stavěly na konceptech z předchozích kapitol:

1. **Začněte jednoduše**: Začněte s ukázkou Foundry Local Spring Boot, abyste pochopili základní integraci AI s lokálními modely
2. **Přidejte interaktivitu**: Pokračujte s Generátorem příběhů o mazlíčcích pro multimodální AI a interakce na webu
3. **Seznamte se se základy MCP**: Vyzkoušejte MCP Kalkulačku, abyste pochopili základy Model Context Protocol

## Shrnutí

Dobrá práce! Nyní jste prozkoumali několik reálných aplikací:

- Multimodální AI zážitky, které fungují jak v prohlížeči, tak na serveru
- Integraci lokálních AI modelů s využitím moderních Java frameworků a SDK
- Vaši první službu Model Context Protocol, abyste viděli, jak nástroje spolupracují s AI

## Další kroky

[5. kapitola: Odpovědná generativní AI](../05-ResponsibleGenAI/README.md)

**Prohlášení:**  
Tento dokument byl přeložen pomocí služby pro automatický překlad [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli se snažíme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Neodpovídáme za žádná nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.