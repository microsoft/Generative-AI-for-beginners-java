<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T16:34:29+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "my"
}
-->
# လက်တွေ့အသုံးချမှုများနှင့် ပရောဂျက်များ

## သင်လေ့လာမည့်အရာများ
ဤအပိုင်းတွင် Java ဖြင့် Generative AI ဖွံ့ဖြိုးတိုးတက်မှု ပုံစံများကို ပြသသည့် လက်တွေ့အသုံးချမှု သုံးခုကို ပြသပါမည်။
- Client-side နှင့် Server-side AI ကိုပေါင်းစပ်ထားသော Multi-modal Pet Story Generator တစ်ခု ဖန်တီးခြင်း
- Foundry Local Spring Boot demo ဖြင့် ဒေသခံ AI မော်ဒယ်များကို ပေါင်းစည်းခြင်း
- Calculator ဥပမာဖြင့် Model Context Protocol (MCP) ဝန်ဆောင်မှု တစ်ခု ဖွံ့ဖြိုးတိုးတက်ခြင်း

## အကြောင်းအရာများ

- [နိဒါန်း](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Pet Story Generator](../../../04-PracticalSamples)
  - [MCP Calculator Service (Beginner-Friendly MCP Demo)](../../../04-PracticalSamples)
- [လေ့လာမှုအဆင့်ဆင့်](../../../04-PracticalSamples)
- [အကျဉ်းချုပ်](../../../04-PracticalSamples)
- [နောက်တစ်ဆင့်](../../../04-PracticalSamples)

## နိဒါန်း

ဤအခန်းတွင် **နမူနာပရောဂျက်များ** ကို ပြသထားပြီး၊ Java ဖြင့် Generative AI ဖွံ့ဖြိုးတိုးတက်မှု ပုံစံများကို ပြသထားသည်။ ပရောဂျက်တစ်ခုစီသည် လုံးဝအလုပ်လုပ်နိုင်ပြီး သင့်ကိုယ်ပိုင် အက်ပလီကေးရှင်းများအတွက် အသုံးချနိုင်သော AI နည်းပညာများ၊ ဖွဲ့စည်းပုံ ပုံစံများနှင့် အကောင်းဆုံး လုပ်ထုံးလုပ်နည်းများကို ပြသထားသည်။

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** သည် **OpenAI Java SDK** ကို အသုံးပြု၍ ဒေသခံ AI မော်ဒယ်များနှင့် ပေါင်းစည်းပုံကို ပြသထားသည်။ Foundry Local တွင် လည်ပတ်နေသော **Phi-3.5-mini** မော်ဒယ်နှင့် ချိတ်ဆက်ခြင်းကို ပြသထားပြီး၊ Cloud ဝန်ဆောင်မှုများမလိုအပ်ဘဲ AI အက်ပလီကေးရှင်းများကို လည်ပတ်နိုင်စေသည်။

### Pet Story Generator

**[Pet Story Generator](petstory/README.md)** သည် စိတ်ဝင်စားဖွယ် Spring Boot Web Application တစ်ခုဖြစ်ပြီး **multi-modal AI processing** ကို အသုံးပြု၍ ဖန်တီးမှုဆန်းသစ်သော အိမ်မွေးတိရစ္ဆာန်ပုံပြင်များ ဖန်တီးပေးသည်။ ၎င်းသည် Browser-based AI အပြန်အလှန်များအတွက် transformer.js နှင့် Server-side အတွက် OpenAI SDK ကို ပေါင်းစပ်အသုံးပြုထားသည်။

### MCP Calculator Service (Beginner-Friendly MCP Demo)

**[MCP Calculator Service](calculator/README.md)** သည် Spring AI ကို အသုံးပြု၍ **Model Context Protocol (MCP)** ကို ပြသထားသည့် ရိုးရှင်းသော နမူနာတစ်ခုဖြစ်သည်။ ၎င်းသည် MCP အကြောင်းအရာများကို စတင်လေ့လာရန် အဆင်ပြေသော နည်းလမ်းတစ်ခုဖြစ်ပြီး၊ MCP Server တစ်ခုကို ဖန်တီးပြီး MCP Clients များနှင့် အပြန်အလှန် ဆက်သွယ်ပုံကို ပြသထားသည်။

## လေ့လာမှုအဆင့်ဆင့်

ဤပရောဂျက်များကို အခြေခံအပိုင်းများမှစ၍ အဆင့်ဆင့် တိုးတက်စေရန် ဒီဇိုင်းထုတ်ထားသည်။

1. **ရိုးရှင်းစွာ စတင်ပါ**: Foundry Local Spring Boot Demo ဖြင့် ဒေသခံ မော်ဒယ်များနှင့် AI ပေါင်းစည်းပုံကို နားလည်ပါ
2. **အပြန်အလှန် ပေါင်းစည်းမှု ထည့်သွင်းပါ**: Pet Story Generator ကို စမ်းသပ်ပြီး Multi-modal AI နှင့် Web-based အပြန်အလှန်များကို လေ့လာပါ
3. **MCP အခြေခံများကို လေ့လာပါ**: MCP Calculator Service ကို စမ်းသပ်ပြီး Model Context Protocol အခြေခံများကို နားလည်ပါ

## အကျဉ်းချုပ်

ကောင်းမွန်ပါတယ်! သင်သည် ယခုအခါ လက်တွေ့အသုံးချမှုအချို့ကို လေ့လာပြီးဖြစ်သည်။

- Browser နှင့် Server နှစ်ခုလုံးတွင် အလုပ်လုပ်နိုင်သော Multi-modal AI အတွေ့အကြုံများ
- ခေတ်မီ Java Frameworks နှင့် SDK များကို အသုံးပြု၍ ဒေသခံ AI မော်ဒယ်များ ပေါင်းစည်းခြင်း
- AI နှင့် Tools များ ပေါင်းစည်းပုံကို မြင်နိုင်စေရန် Model Context Protocol ဝန်ဆောင်မှုကို စတင်လေ့လာခြင်း

## နောက်တစ်ဆင့်

[အခန်း ၅: တာဝန်ရှိသော Generative AI](../05-ResponsibleGenAI/README.md)

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်ခြင်းတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို ၎င်း၏ မူရင်းဘာသာစကားဖြင့် အာဏာတရားရှိသော အရင်းအမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူက ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲသုံးစားမှုများ သို့မဟုတ် အနားလွဲမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။