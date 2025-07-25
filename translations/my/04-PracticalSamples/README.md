<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T12:24:39+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "my"
}
-->
# လက်တွေ့အသုံးချမှုများနှင့် ပရောဂျက်များ

## သင်လေ့လာမည့်အရာများ
ဤအပိုင်းတွင် Java ဖြင့် Generative AI ဖွံ့ဖြိုးတိုးတက်မှု ပုံစံများကို ပြသသည့် လက်တွေ့အသုံးချမှု သုံးခုကို ပြသပါမည်။
- Client-side နှင့် Server-side AI ပေါင်းစပ်ထားသော Multi-modal Pet Story Generator တစ်ခု ဖန်တီးခြင်း
- Foundry Local Spring Boot demo ဖြင့် ဒေသခံ AI မော်ဒယ် ပေါင်းစည်းမှုကို အကောင်အထည်ဖော်ခြင်း
- Calculator ဥပမာဖြင့် Model Context Protocol (MCP) ဝန်ဆောင်မှု တီထွင်ခြင်း

## အကြောင်းအရာများ

- [နိဒါန်း](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Pet Story Generator](../../../04-PracticalSamples)
  - [MCP Calculator Service (Beginner-Friendly MCP Demo)](../../../04-PracticalSamples)
- [လေ့လာမှု အဆင့်ဆင့်](../../../04-PracticalSamples)
- [အကျဉ်းချုပ်](../../../04-PracticalSamples)
- [နောက်တစ်ဆင့်](../../../04-PracticalSamples)

## နိဒါန်း

ဤအခန်းတွင် **နမူနာပရောဂျက်များ** ကို ပြသထားပြီး၊ Java ဖြင့် Generative AI ဖွံ့ဖြိုးတိုးတက်မှု ပုံစံများကို ပြသထားသည်။ ပရောဂျက်တိုင်းသည် အပြည့်အဝ လုပ်ဆောင်နိုင်ပြီး၊ သင့်ပရောဂျက်များအတွက် အသုံးချနိုင်သော AI နည်းပညာများ၊ ဖွဲ့စည်းပုံ ပုံစံများနှင့် အကောင်းဆုံး လုပ်ထုံးလုပ်နည်းများကို ပြသထားသည်။

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** သည် **OpenAI Java SDK** ကို အသုံးပြု၍ ဒေသခံ AI မော်ဒယ်များနှင့် ပေါင်းစည်းပုံကို ပြသထားသည်။ Foundry Local တွင် လည်ပတ်နေသော **Phi-3.5-mini** မော်ဒယ်နှင့် ချိတ်ဆက်ခြင်းကို ပြသပြီး၊ Cloud ဝန်ဆောင်မှုများမလိုအပ်ဘဲ AI အက်ပ်များကို လည်ပတ်နိုင်စေသည်။

### Pet Story Generator

**[Pet Story Generator](petstory/README.md)** သည် စိတ်ဝင်စားဖွယ် Spring Boot Web Application တစ်ခုဖြစ်ပြီး၊ **multi-modal AI processing** ကို အသုံးပြု၍ ဖန်တီးမှုဆန်းသစ်သော အိမ်မွေးတိရစ္ဆာန်ပုံပြင်များ ဖန်တီးပေးသည်။ ၎င်းသည် Browser-based AI အပြန်အလှန်လုပ်ဆောင်မှုများအတွက် transformer.js ကို အသုံးပြုပြီး၊ Server-side အတွက် OpenAI SDK ကို အသုံးပြုထားသည်။

### MCP Calculator Service (Beginner-Friendly MCP Demo)

**[MCP Calculator Service](mcp/calculator/README.md)** သည် Spring AI ကို အသုံးပြု၍ **Model Context Protocol (MCP)** ကို ပြသထားသည့် နမူနာတစ်ခုဖြစ်သည်။ ၎င်းသည် MCP အကြောင်းအရာများကို စတင်လေ့လာလိုသူများအတွက် လွယ်ကူစွာ နားလည်နိုင်စေရန် MCP Server တစ်ခုကို ဖန်တီးပြီး၊ MCP Clients နှင့် အပြန်အလှန်လုပ်ဆောင်ပုံကို ပြသထားသည်။

## လေ့လာမှု အဆင့်ဆင့်

ဤပရောဂျက်များကို အခြေခံအဆင့်မှ စတင်၍ အဆင့်ဆင့် တိုးတက်စေရန် ဒီဇိုင်းဆွဲထားသည်။

1. **ရိုးရှင်းစွာ စတင်ပါ**: Foundry Local Spring Boot Demo ကို စတင်လေ့လာပြီး ဒေသခံ AI မော်ဒယ်များနှင့် ပေါင်းစည်းပုံကို နားလည်ပါ
2. **အပြန်အလှန်လုပ်ဆောင်မှု ထည့်သွင်းပါ**: Pet Story Generator ကို လေ့လာပြီး Multi-modal AI နှင့် Web-based အပြန်အလှန်လုပ်ဆောင်မှုများကို နားလည်ပါ
3. **MCP အခြေခံများကို လေ့လာပါ**: MCP Calculator Service ကို စမ်းသပ်ပြီး Model Context Protocol အခြေခံများကို နားလည်ပါ

## အကျဉ်းချုပ်

**ဂုဏ်ယူပါတယ်!** သင်အောင်မြင်စွာ:

- **Client-side နှင့် Server-side AI ပေါင်းစပ်မှု** ဖြင့် Multi-modal AI အတွေ့အကြုံများ ဖန်တီးနိုင်ခဲ့ပါသည်
- **ဒေသခံ AI မော်ဒယ် ပေါင်းစည်းမှု** ကို ခေတ်မီ Java Frameworks နှင့် SDK များ အသုံးပြု၍ အကောင်အထည်ဖော်နိုင်ခဲ့ပါသည်
- **Model Context Protocol ဝန်ဆောင်မှုများ ဖွံ့ဖြိုးတိုးတက်မှု** ကို နားလည်နိုင်ခဲ့ပါသည်

## နောက်တစ်ဆင့်

[အခန်း ၅: တာဝန်ရှိသော Generative AI](../05-ResponsibleGenAI/README.md)

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို ၎င်း၏ မူရင်းဘာသာစကားဖြင့် အာဏာတရ အရင်းအမြစ်အဖြစ် ရှုလေ့လာသင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူ့ဘာသာပြန်ပညာရှင်များမှ ပရော်ဖက်ရှင်နယ် ဘာသာပြန်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲအမှားများ သို့မဟုတ် အနားလွဲမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။