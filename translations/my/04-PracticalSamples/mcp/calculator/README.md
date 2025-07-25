<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:25:36+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "my"
}
-->
# MCP Calculator Tutorial for Beginners

## အကြောင်းအရာများ

- [သင်လေ့လာနိုင်မည့်အရာများ](../../../../../04-PracticalSamples/mcp/calculator)
- [လိုအပ်ချက်များ](../../../../../04-PracticalSamples/mcp/calculator)
- [ပရောဂျက်ဖွဲ့စည်းပုံကိုနားလည်ခြင်း](../../../../../04-PracticalSamples/mcp/calculator)
- [အဓိကအစိတ်အပိုင်းများရှင်းလင်းချက်](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. အဓိကအက်ပလီကေးရှင်း](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. ကိန်းဂဏန်းဝန်ဆောင်မှု](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. တိုက်ရိုက် MCP Client](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-အခြေခံ Client](../../../../../04-PracticalSamples/mcp/calculator)
- [ဥပမာများကိုအလုပ်လုပ်စေခြင်း](../../../../../04-PracticalSamples/mcp/calculator)
- [အားလုံးပေါင်းစည်းပြီးအလုပ်လုပ်ပုံ](../../../../../04-PracticalSamples/mcp/calculator)
- [နောက်တစ်ဆင့်များ](../../../../../04-PracticalSamples/mcp/calculator)

## သင်လေ့လာနိုင်မည့်အရာများ

ဒီလမ်းညွှန်မှာ Model Context Protocol (MCP) ကိုအသုံးပြုပြီး ကိန်းဂဏန်းဝန်ဆောင်မှုတစ်ခုကို ဘယ်လိုတည်ဆောက်ရမယ်ဆိုတာရှင်းပြထားပါတယ်။ သင်နားလည်နိုင်မယ့်အရာများမှာ:

- AI သုံးပြီး tools အဖြစ်အသုံးပြုနိုင်တဲ့ ဝန်ဆောင်မှုတစ်ခုကို ဘယ်လိုဖန်တီးရမလဲ
- MCP ဝန်ဆောင်မှုများနှင့် တိုက်ရိုက်ဆက်သွယ်ပုံ
- AI မော်ဒယ်များက ဘယ် tools ကိုအသုံးပြုရမလဲဆိုတာကို အလိုအလျောက်ရွေးချယ်ပုံ
- တိုက်ရိုက် protocol calls နဲ့ AI-အကူအညီ interactions ကြားက ကွာခြားချက်

## လိုအပ်ချက်များ

စတင်ရန်မတိုင်မီ သင်မှာအောက်ပါအရာများရှိရမည်:
- Java 21 သို့မဟုတ် အထက်ရှိထား
- Maven ကို dependency management အတွက်အသုံးပြုရန်
- GitHub အကောင့်နှင့် personal access token (PAT)
- Java နဲ့ Spring Boot အခြေခံနားလည်မှု

## ပရောဂျက်ဖွဲ့စည်းပုံကိုနားလည်ခြင်း

ကိန်းဂဏန်းပရောဂျက်မှာ အရေးကြီးတဲ့ဖိုင်များအချို့ပါဝင်ပါတယ်:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## အဓိကအစိတ်အပိုင်းများရှင်းလင်းချက်

### 1. အဓိကအက်ပလီကေးရှင်း

**ဖိုင်:** `McpServerApplication.java`

ဒီဟာက ကိန်းဂဏန်းဝန်ဆောင်မှုရဲ့ entry point ဖြစ်ပါတယ်။ ဒါဟာ ပုံမှန် Spring Boot application တစ်ခုဖြစ်ပြီး အထူး feature တစ်ခုပါဝင်ပါတယ်:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**ဒီဟာဘာလုပ်သလဲ:**
- Spring Boot web server ကို port 8080 မှာစတင်
- `ToolCallbackProvider` ကိုဖန်တီးပြီး ကိန်းဂဏန်းနည်းလမ်းများကို MCP tools အဖြစ်အသုံးပြုနိုင်အောင်လုပ်ဆောင်
- `@Bean` annotation က Spring ကို ဒီဟာကို component အဖြစ်စီမံခန့်ခွဲရန်ပြောပြ

### 2. ကိန်းဂဏန်းဝန်ဆောင်မှု

**ဖိုင်:** `CalculatorService.java`

ဒီနေရာမှာ ကိန်းဂဏန်းဆိုင်ရာ calculation တွေကိုလုပ်ဆောင်ပါတယ်။ `@Tool` နဲ့အမှတ်အသားပြုထားတဲ့ method တစ်ခုစီကို MCP မှတစ်ဆင့်အသုံးပြုနိုင်ပါတယ်:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**အဓိကအချက်များ:**

1. **`@Tool` Annotation**: ဒီဟာက MCP ကို ဒီ method ကို အပြင်ဘက် client တွေကခေါ်နိုင်တယ်လို့ပြောပြ
2. **ရှင်းလင်းတဲ့ဖော်ပြချက်များ**: tool တစ်ခုစီမှာ AI မော်ဒယ်တွေကို ဘယ်အခါအသုံးပြုရမလဲဆိုတာနားလည်စေဖို့ဖော်ပြချက်ပါဝင်
3. **အမြဲတမ်းတူညီတဲ့ပြန်လည်ပေးပုံ**: အားလုံးက "5.00 + 3.00 = 8.00" လိုမျိုး လူနားလည်နိုင်တဲ့ string format ပြန်ပေး
4. **အမှားကိုင်တွယ်မှု**: သုညနဲ့စားခြင်းနဲ့ အနုတ်ရင်းအမြစ်တွေမှာ error message ပြန်ပေး

**ရရှိနိုင်တဲ့လုပ်ဆောင်ချက်များ:**
- `add(a, b)` - နံပါတ်နှစ်ခုကိုပေါင်း
- `subtract(a, b)` - ဒုတိယနံပါတ်ကိုပထမနံပါတ်ကနုတ်
- `multiply(a, b)` - နံပါတ်နှစ်ခုကိုမြှောက်
- `divide(a, b)` - ပထမနံပါတ်ကို ဒုတိယနံပါတ်နဲ့စား (သုညစစ်ဆေးမှုပါဝင်)
- `power(base, exponent)` - base ကို exponent အတိုင်းမြှောက်
- `squareRoot(number)` - ရင်းအမြစ်တွက်ချက် (အနုတ်စစ်ဆေးမှုပါဝင်)
- `modulus(a, b)` - remainder ပြန်ပေး
- `absolute(number)` - absolute value ပြန်ပေး
- `help()` - လုပ်ဆောင်ချက်အားလုံးအကြောင်းအရာပြန်ပေး

### 3. တိုက်ရိုက် MCP Client

**ဖိုင်:** `SDKClient.java`

ဒီ client က MCP server နဲ့ AI မသုံးဘဲ တိုက်ရိုက်ဆက်သွယ်ပါတယ်။ အတိအကျ parameter တွေနဲ့ ကိန်းဂဏန်းလုပ်ဆောင်ချက်တွေကိုခေါ်ပါတယ်:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**ဒီဟာဘာလုပ်သလဲ:**
1. **ဆက်သွယ်**: `http://localhost:8080` မှာ calculator server နဲ့ဆက်သွယ်
2. **စာရင်းပြုစု**: ရရှိနိုင်တဲ့ tools (calculator functions) အားလုံးကိုစာရင်းပြုစု
3. **ခေါ်**: အတိအကျ parameter တွေနဲ့လုပ်ဆောင်ချက်တွေကိုခေါ်
4. **ရလဒ်ကိုပုံနှိပ်**: ရလဒ်တွေကိုတိုက်ရိုက်ပြ

**ဘယ်အခါအသုံးပြုမလဲ:** သင်ဘယ်လို calculation လုပ်ချင်တယ်ဆိုတာသိပြီး programmatically ခေါ်ချင်တဲ့အခါ

### 4. AI-အခြေခံ Client

**ဖိုင်:** `LangChain4jClient.java`

ဒီ client က AI မော်ဒယ် (GPT-4o-mini) ကိုအသုံးပြုပြီး calculator tools တွေကိုအလိုအလျောက်ရွေးချယ်နိုင်ပါတယ်:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**ဒီဟာဘာလုပ်သလဲ:**
1. **AI မော်ဒယ်ဆက်သွယ်မှု**: GitHub token ကိုအသုံးပြုပြီး AI မော်ဒယ်နဲ့ဆက်သွယ်
2. **Calculator MCP server နဲ့ဆက်သွယ်**: AI ကို calculator tools အားလုံးရရှိအောင်လုပ်ဆောင်
3. **သဘာဝဘာသာစကားတောင်းဆိုမှု**: "24.5 နဲ့ 17.3 ကိုပေါင်းပါ" လို request တွေကိုခွင့်ပြု

**AI အလိုအလျောက်:**
- သင်နံပါတ်တွေကိုပေါင်းချင်တယ်ဆိုတာနားလည်
- `add` tool ကိုရွေးချယ်
- `add(24.5, 17.3)` ကိုခေါ်
- ရလဒ်ကို သဘာဝအဖြေတစ်ခုအဖြစ်ပြန်ပေး

## ဥပမာများကိုအလုပ်လုပ်စေခြင်း

### အဆင့် 1: Calculator Server ကိုစတင်

အရင်ဆုံး GitHub token ကိုသတ်မှတ်ပါ (AI client အတွက်လိုအပ်):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Server ကိုစတင်ပါ:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Server က `http://localhost:8080` မှာစတင်ပါလိမ့်မယ်။ သင်မြင်ရမယ့်အရာက:
```
Started McpServerApplication in X.XXX seconds
```

### အဆင့် 2: Direct Client နဲ့စမ်းသပ်

Terminal အသစ်တစ်ခုမှာ:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

သင်မြင်ရမယ့် output က:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### အဆင့် 3: AI Client နဲ့စမ်းသပ်

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

AI က tools တွေကိုအလိုအလျောက်အသုံးပြုတာကိုသင်မြင်ရမယ်:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## အားလုံးပေါင်းစည်းပြီးအလုပ်လုပ်ပုံ

AI ကို "5 နဲ့ 3 ကိုပေါင်းရင်ဘယ်လောက်လဲ?" လို့မေးတဲ့အခါမှာ:

1. **သင်** AI ကို သဘာဝဘာသာစကားနဲ့မေး
2. **AI** သင့်တောင်းဆိုမှုကိုခွဲခြမ်းစိတ်ဖြာပြီး addition လုပ်ချင်တယ်ဆိုတာနားလည်
3. **AI** MCP server ကိုခေါ်: `add(5.0, 3.0)`
4. **Calculator Service** က `5.0 + 3.0 = 8.0` လုပ်ဆောင်
5. **Calculator Service** က `"5.00 + 3.00 = 8.00"` ပြန်ပေး
6. **AI** ရလဒ်ကိုရပြီး သဘာဝအဖြေတစ်ခုအဖြစ် format ပြုလုပ်
7. **သင်** "5 နဲ့ 3 ကိုပေါင်းရင် 8 ဖြစ်ပါတယ်" ဆိုတဲ့အဖြေကိုရ

## နောက်တစ်ဆင့်များ

ဥပမာများပိုမိုကြည့်ရန် [Chapter 04: Practical samples](../../README.md) ကိုကြည့်ပါ

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်ခြင်းတွင် အမှားများ သို့မဟုတ် မတိကျမှုများ ပါရှိနိုင်သည်ကို သတိပြုပါ။ မူရင်းဘာသာစကားဖြင့် ရေးသားထားသော စာရွက်စာတမ်းကို အာဏာတရ အရင်းအမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူ့ဘာသာပြန်ပညာရှင်များမှ ပရော်ဖက်ရှင်နယ် ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲအလွတ်များ သို့မဟုတ် အနားလွဲမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။