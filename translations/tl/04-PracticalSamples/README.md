<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:58:50+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "tl"
}
-->
# Praktikal na Aplikasyon at mga Proyekto

## Ano ang Iyong Matututunan
Sa seksyong ito, ipapakita namin ang tatlong praktikal na aplikasyon na nagtatampok ng mga pattern sa pag-develop ng generative AI gamit ang Java:
- Gumawa ng multi-modal na Pet Story Generator na pinagsasama ang AI sa client-side at server-side
- Magpatupad ng lokal na integrasyon ng AI model gamit ang Foundry Local Spring Boot demo
- Bumuo ng Model Context Protocol (MCP) service gamit ang Calculator na halimbawa

## Talaan ng Nilalaman

- [Introduksyon](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Pet Story Generator](../../../04-PracticalSamples)
  - [MCP Calculator Service (Demo ng MCP para sa mga Baguhan)](../../../04-PracticalSamples)
- [Pag-unlad ng Pagkatuto](../../../04-PracticalSamples)
- [Buod](../../../04-PracticalSamples)
- [Susunod na Hakbang](../../../04-PracticalSamples)

## Introduksyon

Ang kabanatang ito ay nagtatampok ng **mga sample na proyekto** na nagpapakita ng mga pattern sa pag-develop ng generative AI gamit ang Java. Ang bawat proyekto ay ganap na gumagana at nagpapakita ng partikular na teknolohiya ng AI, mga pattern ng arkitektura, at mga pinakamahusay na kasanayan na maaari mong iangkop para sa iyong sariling mga aplikasyon.

### Foundry Local Spring Boot Demo

Ang **[Foundry Local Spring Boot Demo](foundrylocal/README.md)** ay nagpapakita kung paano mag-integrate sa mga lokal na AI model gamit ang **OpenAI Java SDK**. Ipinapakita nito ang koneksyon sa **Phi-3.5-mini** model na tumatakbo sa Foundry Local, na nagbibigay-daan sa iyo na magpatakbo ng mga AI application nang hindi umaasa sa mga cloud service.

### Pet Story Generator

Ang **[Pet Story Generator](petstory/README.md)** ay isang nakaka-engganyong Spring Boot web application na nagpapakita ng **multi-modal AI processing** para makabuo ng malikhaing kwento tungkol sa mga alagang hayop. Pinagsasama nito ang kakayahan ng AI sa client-side at server-side gamit ang transformer.js para sa browser-based na AI interactions at ang OpenAI SDK para sa server-side na pagproseso.

### MCP Calculator Service (Demo ng MCP para sa mga Baguhan)

Ang **[MCP Calculator Service](mcp/calculator/README.md)** ay isang simpleng demonstrasyon ng **Model Context Protocol (MCP)** gamit ang Spring AI. Nagbibigay ito ng madaling maunawaang pagpapakilala sa mga konsepto ng MCP, na nagpapakita kung paano gumawa ng basic na MCP Server na nakikipag-ugnayan sa mga MCP client.

## Pag-unlad ng Pagkatuto

Ang mga proyektong ito ay idinisenyo upang magtuloy-tuloy ang pagkatuto mula sa mga naunang kabanata:

1. **Magsimula sa Simple**: Simulan sa Foundry Local Spring Boot Demo upang maunawaan ang pangunahing integrasyon ng AI sa mga lokal na modelo
2. **Magdagdag ng Interaktibidad**: Magpatuloy sa Pet Story Generator para sa multi-modal na AI at mga web-based na interaksyon
3. **Matutunan ang Batayan ng MCP**: Subukan ang MCP Calculator Service upang maunawaan ang mga pangunahing konsepto ng Model Context Protocol

## Buod

**Binabati kita!** Matagumpay mong:

- **Nakapagbuo ng multi-modal na karanasan sa AI** na pinagsasama ang client-side at server-side na pagproseso ng AI
- **Nakapagpatupad ng integrasyon ng lokal na AI model** gamit ang modernong Java frameworks at SDKs
- **Nakapag-develop ng Model Context Protocol services** na nagpapakita ng mga pattern sa integrasyon ng mga tool

## Susunod na Hakbang

[Chapter 5: Responsible Generative AI](../05-ResponsibleGenAI/README.md)

**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't sinisikap naming maging tumpak, pakitandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa kanyang katutubong wika ang dapat ituring na opisyal na sanggunian. Para sa mahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.