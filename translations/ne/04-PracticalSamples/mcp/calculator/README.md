<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:14:49+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "ne"
}
-->
# MCP क्याल्कुलेटर ट्युटोरियल शुरुवातकर्ताहरूका लागि

## सामग्री सूची

- [तपाईंले के सिक्नुहुनेछ](../../../../../04-PracticalSamples/mcp/calculator)
- [पूर्वआवश्यकताहरू](../../../../../04-PracticalSamples/mcp/calculator)
- [प्रोजेक्ट संरचना बुझ्दै](../../../../../04-PracticalSamples/mcp/calculator)
- [मुख्य कम्पोनेन्टहरूको व्याख्या](../../../../../04-PracticalSamples/mcp/calculator)
  - [१. मुख्य एप्लिकेसन](../../../../../04-PracticalSamples/mcp/calculator)
  - [२. क्याल्कुलेटर सेवा](../../../../../04-PracticalSamples/mcp/calculator)
  - [३. प्रत्यक्ष MCP क्लाइन्ट](../../../../../04-PracticalSamples/mcp/calculator)
  - [४. एआई-संचालित क्लाइन्ट](../../../../../04-PracticalSamples/mcp/calculator)
- [उदाहरणहरू चलाउँदै](../../../../../04-PracticalSamples/mcp/calculator)
- [सबै कुरा कसरी सँगै काम गर्छ](../../../../../04-PracticalSamples/mcp/calculator)
- [अर्को चरणहरू](../../../../../04-PracticalSamples/mcp/calculator)

## तपाईंले के सिक्नुहुनेछ

यो ट्युटोरियलले मोडेल कन्टेक्स्ट प्रोटोकल (MCP) प्रयोग गरेर क्याल्कुलेटर सेवा कसरी निर्माण गर्ने भनेर व्याख्या गर्दछ। तपाईंले बुझ्नुहुनेछ:

- एआईले उपकरणको रूपमा प्रयोग गर्न सक्ने सेवा कसरी बनाउने
- MCP सेवाहरूसँग प्रत्यक्ष सञ्चार कसरी सेटअप गर्ने
- एआई मोडेलहरूले कुन उपकरण प्रयोग गर्ने भनेर स्वचालित रूपमा कसरी निर्णय गर्छन्
- प्रत्यक्ष प्रोटोकल कलहरू र एआई-सहायता अन्तर्क्रियाहरू बीचको भिन्नता

## पूर्वआवश्यकताहरू

सुरु गर्नुअघि, सुनिश्चित गर्नुहोस् कि तपाईंले यी चीजहरू तयार पार्नुभएको छ:
- Java 21 वा उच्च संस्करण स्थापना गरिएको
- Maven निर्भरता व्यवस्थापनका लागि
- व्यक्तिगत पहुँच टोकन (PAT) भएको GitHub खाता
- Java र Spring Boot को आधारभूत ज्ञान

## प्रोजेक्ट संरचना बुझ्दै

क्याल्कुलेटर प्रोजेक्टमा केही महत्त्वपूर्ण फाइलहरू छन्:

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

## मुख्य कम्पोनेन्टहरूको व्याख्या

### १. मुख्य एप्लिकेसन

**फाइल:** `McpServerApplication.java`

यो हाम्रो क्याल्कुलेटर सेवाको प्रवेश बिन्दु हो। यो एउटा मानक Spring Boot एप्लिकेसन हो जसमा एउटा विशेष थप छ:

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

**यसले के गर्छ:**
- पोर्ट 8080 मा Spring Boot वेब सर्भर सुरु गर्छ
- `ToolCallbackProvider` सिर्जना गर्छ जसले हाम्रो क्याल्कुलेटर विधिहरू MCP उपकरणको रूपमा उपलब्ध गराउँछ
- `@Bean` एनोटेसनले Spring लाई यसलाई अन्य भागहरूले प्रयोग गर्न सक्ने कम्पोनेन्टको रूपमा व्यवस्थापन गर्न भन्छ

### २. क्याल्कुलेटर सेवा

**फाइल:** `CalculatorService.java`

यहाँ सबै गणितीय कार्यहरू हुन्छन्। प्रत्येक विधि `@Tool` ले चिन्हित गरिएको छ ताकि यो MCP मार्फत उपलब्ध होस्:

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

**मुख्य विशेषताहरू:**

1. **`@Tool` एनोटेसन**: यसले MCP लाई यो विधि बाह्य क्लाइन्टहरूले कल गर्न सक्ने बताउँछ
2. **स्पष्ट विवरणहरू**: प्रत्येक उपकरणसँग विवरण हुन्छ जसले एआई मोडेलहरूलाई यो कहिले प्रयोग गर्ने भनेर बुझ्न मद्दत गर्छ
3. **सुसंगत प्रतिफल ढाँचा**: सबै अपरेसनहरूले "5.00 + 3.00 = 8.00" जस्ता मानव-पढ्न मिल्ने स्ट्रिङहरू फिर्ता गर्छन्
4. **त्रुटि ह्यान्डलिङ**: शून्यद्वारा भाग र नकारात्मक वर्गमूलले त्रुटि सन्देशहरू फिर्ता गर्छ

**उपलब्ध अपरेसनहरू:**
- `add(a, b)` - दुई संख्याहरू जोड्छ
- `subtract(a, b)` - दोस्रोलाई पहिलोबाट घटाउँछ
- `multiply(a, b)` - दुई संख्याहरू गुणा गर्छ
- `divide(a, b)` - पहिलोलाई दोस्रोले भाग गर्छ (शून्य-जाँच सहित)
- `power(base, exponent)` - आधारलाई घातांकमा उठाउँछ
- `squareRoot(number)` - वर्गमूल गणना गर्छ (नकारात्मक जाँच सहित)
- `modulus(a, b)` - भागको बाँकी फिर्ता गर्छ
- `absolute(number)` - पूर्ण मान फिर्ता गर्छ
- `help()` - सबै अपरेसनहरूको जानकारी फिर्ता गर्छ

### ३. प्रत्यक्ष MCP क्लाइन्ट

**फाइल:** `SDKClient.java`

यो क्लाइन्टले एआई प्रयोग नगरी MCP सर्भरसँग प्रत्यक्ष कुरा गर्छ। यसले विशिष्ट क्याल्कुलेटर कार्यहरू म्यानुअली कल गर्छ:

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

**यसले के गर्छ:**
1. **जोड्छ** क्याल्कुलेटर सर्भरमा `http://localhost:8080`
2. **सूचीबद्ध गर्छ** सबै उपलब्ध उपकरणहरू (हाम्रो क्याल्कुलेटर कार्यहरू)
3. **कल गर्छ** विशिष्ट कार्यहरू ठ्याक्कै प्यारामिटरहरूसँग
4. **प्रत्यक्ष प्रिन्ट गर्छ** नतिजाहरू

**यो कहिले प्रयोग गर्ने:** जब तपाईंलाई ठ्याक्कै कुन गणना गर्नुपर्छ थाहा छ र यसलाई प्रोग्राममार्फत कल गर्न चाहनुहुन्छ।

### ४. एआई-संचालित क्लाइन्ट

**फाइल:** `LangChain4jClient.java`

यो क्लाइन्टले एआई मोडेल (GPT-4o-mini) प्रयोग गर्छ जसले स्वचालित रूपमा कुन क्याल्कुलेटर उपकरण प्रयोग गर्ने निर्णय गर्न सक्छ:

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

**यसले के गर्छ:**
1. **एआई मोडेल कनेक्शन** तपाईंको GitHub टोकन प्रयोग गरेर सिर्जना गर्छ
2. **कनेक्ट गर्छ** एआईलाई हाम्रो क्याल्कुलेटर MCP सर्भरसँग
3. **एआईलाई दिन्छ** हाम्रो सबै क्याल्कुलेटर उपकरणहरूको पहुँच
4. **प्राकृतिक भाषा अनुरोधहरू अनुमति दिन्छ** जस्तै "24.5 र 17.3 को योगफल निकाल"

**एआई स्वचालित रूपमा:**
- तपाईंले संख्या जोड्न चाहनुहुन्छ भनेर बुझ्छ
- `add` उपकरण चयन गर्छ
- `add(24.5, 17.3)` कल गर्छ
- प्राकृतिक उत्तरमा नतिजा फिर्ता गर्छ

## उदाहरणहरू चलाउँदै

### चरण १: क्याल्कुलेटर सर्भर सुरु गर्नुहोस्

पहिले, तपाईंको GitHub टोकन सेट गर्नुहोस् (एआई क्लाइन्टका लागि आवश्यक):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

सर्भर सुरु गर्नुहोस्:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

सर्भर `http://localhost:8080` मा सुरु हुनेछ। तपाईंले देख्नुहुनेछ:
```
Started McpServerApplication in X.XXX seconds
```

### चरण २: प्रत्यक्ष क्लाइन्टसँग परीक्षण गर्नुहोस्

नयाँ टर्मिनलमा:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

तपाईंले यस्तो आउटपुट देख्नुहुनेछ:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### चरण ३: एआई क्लाइन्टसँग परीक्षण गर्नुहोस्

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

तपाईंले एआईले उपकरणहरू स्वचालित रूपमा प्रयोग गरेको देख्नुहुनेछ:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## सबै कुरा कसरी सँगै काम गर्छ

जब तपाईं एआईलाई "५ + ३ कति हो?" भनेर सोध्नुहुन्छ, यो सम्पूर्ण प्रक्रिया हुन्छ:

1. **तपाईं** एआईलाई प्राकृतिक भाषामा सोध्नुहुन्छ
2. **एआई** तपाईंको अनुरोध विश्लेषण गर्छ र तपाईंलाई जोड्न चाहिएको बुझ्छ
3. **एआई** MCP सर्भरलाई कल गर्छ: `add(5.0, 3.0)`
4. **क्याल्कुलेटर सेवा** प्रदर्शन गर्छ: `5.0 + 3.0 = 8.0`
5. **क्याल्कुलेटर सेवा** फिर्ता गर्छ: `"5.00 + 3.00 = 8.00"`
6. **एआई** नतिजा प्राप्त गर्छ र प्राकृतिक उत्तरमा ढाल्छ
7. **तपाईं** पाउनुहुन्छ: "५ र ३ को योगफल ८ हो"

## अर्को चरणहरू

थप उदाहरणहरूको लागि, [अध्याय ०४: व्यावहारिक नमूनाहरू](../../README.md) हेर्नुहोस्।

**अस्वीकरण**:  
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको छ। हामी यथासम्भव शुद्धता सुनिश्चित गर्न प्रयास गर्छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छ। मूल दस्तावेज़ यसको मातृभाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याको लागि हामी जिम्मेवार हुने छैनौं।