<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:06:47+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "hi"
}
-->
# MCP कैलकुलेटर ट्यूटोरियल शुरुआती लोगों के लिए

## सामग्री की सूची

- [आप क्या सीखेंगे](../../../../../04-PracticalSamples/mcp/calculator)
- [पूर्व आवश्यकताएँ](../../../../../04-PracticalSamples/mcp/calculator)
- [प्रोजेक्ट संरचना को समझना](../../../../../04-PracticalSamples/mcp/calculator)
- [मुख्य घटकों की व्याख्या](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. मुख्य एप्लिकेशन](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. कैलकुलेटर सेवा](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. डायरेक्ट MCP क्लाइंट](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. एआई-संचालित क्लाइंट](../../../../../04-PracticalSamples/mcp/calculator)
- [उदाहरण चलाना](../../../../../04-PracticalSamples/mcp/calculator)
- [यह सब कैसे एक साथ काम करता है](../../../../../04-PracticalSamples/mcp/calculator)
- [अगले कदम](../../../../../04-PracticalSamples/mcp/calculator)

## आप क्या सीखेंगे

यह ट्यूटोरियल समझाता है कि Model Context Protocol (MCP) का उपयोग करके कैलकुलेटर सेवा कैसे बनाई जाए। आप समझेंगे:

- एआई के लिए एक टूल के रूप में सेवा कैसे बनाई जाए
- MCP सेवाओं के साथ डायरेक्ट संचार कैसे सेट करें
- एआई मॉडल स्वचालित रूप से कौन से टूल का उपयोग करना है, यह कैसे चुनते हैं
- डायरेक्ट प्रोटोकॉल कॉल और एआई-सहायता प्राप्त इंटरैक्शन के बीच का अंतर

## पूर्व आवश्यकताएँ

शुरू करने से पहले, सुनिश्चित करें कि आपके पास निम्नलिखित हैं:
- Java 21 या उससे ऊपर का संस्करण इंस्टॉल हो
- Maven डिपेंडेंसी प्रबंधन के लिए
- GitHub अकाउंट और एक व्यक्तिगत एक्सेस टोकन (PAT)
- Java और Spring Boot की बुनियादी समझ

## प्रोजेक्ट संरचना को समझना

कैलकुलेटर प्रोजेक्ट में कई महत्वपूर्ण फाइलें हैं:

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

## मुख्य घटकों की व्याख्या

### 1. मुख्य एप्लिकेशन

**फाइल:** `McpServerApplication.java`

यह हमारी कैलकुलेटर सेवा का प्रवेश बिंदु है। यह एक मानक Spring Boot एप्लिकेशन है जिसमें एक विशेष जोड़ है:

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

**यह क्या करता है:**
- पोर्ट 8080 पर एक Spring Boot वेब सर्वर शुरू करता है
- एक `ToolCallbackProvider` बनाता है जो हमारे कैलकुलेटर मेथड्स को MCP टूल्स के रूप में उपलब्ध कराता है
- `@Bean` एनोटेशन Spring को इसे एक घटक के रूप में प्रबंधित करने के लिए कहता है जिसे अन्य भाग उपयोग कर सकते हैं

### 2. कैलकुलेटर सेवा

**फाइल:** `CalculatorService.java`

यह वह जगह है जहां सभी गणितीय कार्य होते हैं। प्रत्येक मेथड को `@Tool` के साथ चिह्नित किया गया है ताकि इसे MCP के माध्यम से उपलब्ध कराया जा सके:

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

**मुख्य विशेषताएँ:**

1. **`@Tool` एनोटेशन**: यह MCP को बताता है कि इस मेथड को बाहरी क्लाइंट्स द्वारा कॉल किया जा सकता है
2. **स्पष्ट विवरण**: प्रत्येक टूल का विवरण होता है जो एआई मॉडल को समझने में मदद करता है कि इसे कब उपयोग करना है
3. **सुसंगत रिटर्न फॉर्मेट**: सभी ऑपरेशन्स मानव-पढ़ने योग्य स्ट्रिंग्स लौटाते हैं जैसे "5.00 + 3.00 = 8.00"
4. **त्रुटि हैंडलिंग**: शून्य से विभाजन और नकारात्मक वर्गमूल त्रुटि संदेश लौटाते हैं

**उपलब्ध ऑपरेशन्स:**
- `add(a, b)` - दो संख्याओं को जोड़ता है
- `subtract(a, b)` - दूसरी संख्या को पहली से घटाता है
- `multiply(a, b)` - दो संख्याओं को गुणा करता है
- `divide(a, b)` - पहली संख्या को दूसरी से विभाजित करता है (शून्य-जांच के साथ)
- `power(base, exponent)` - बेस को एक्सपोनेंट की शक्ति तक बढ़ाता है
- `squareRoot(number)` - वर्गमूल की गणना करता है (नकारात्मक जांच के साथ)
- `modulus(a, b)` - विभाजन का शेष लौटाता है
- `absolute(number)` - परिमाण मान लौटाता है
- `help()` - सभी ऑपरेशन्स के बारे में जानकारी लौटाता है

### 3. डायरेक्ट MCP क्लाइंट

**फाइल:** `SDKClient.java`

यह क्लाइंट एआई का उपयोग किए बिना MCP सर्वर से सीधे बात करता है। यह विशेष कैलकुलेटर फंक्शन्स को मैन्युअली कॉल करता है:

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

**यह क्या करता है:**
1. **कनेक्ट करता है** कैलकुलेटर सर्वर से `http://localhost:8080` पर
2. **सूचीबद्ध करता है** सभी उपलब्ध टूल्स (हमारे कैलकुलेटर फंक्शन्स)
3. **कॉल करता है** विशिष्ट फंक्शन्स को सटीक पैरामीटर्स के साथ
4. **परिणाम सीधे प्रिंट करता है**

**कब उपयोग करें:** जब आपको ठीक-ठीक पता हो कि कौन सा गणना करना है और इसे प्रोग्रामेटिक रूप से कॉल करना हो।

### 4. एआई-संचालित क्लाइंट

**फाइल:** `LangChain4jClient.java`

यह क्लाइंट एक एआई मॉडल (GPT-4o-mini) का उपयोग करता है जो स्वचालित रूप से तय कर सकता है कि कौन से कैलकुलेटर टूल्स का उपयोग करना है:

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

**यह क्या करता है:**
1. **एक एआई मॉडल कनेक्शन बनाता है** आपके GitHub टोकन का उपयोग करके
2. **हमारे कैलकुलेटर MCP सर्वर से कनेक्ट करता है**
3. **एआई को हमारे सभी कैलकुलेटर टूल्स तक पहुंच देता है**
4. **प्राकृतिक भाषा अनुरोधों की अनुमति देता है** जैसे "24.5 और 17.3 का योग निकालें"

**एआई स्वचालित रूप से:**
- समझता है कि आप संख्याओं को जोड़ना चाहते हैं
- `add` टूल चुनता है
- `add(24.5, 17.3)` कॉल करता है
- परिणाम को प्राकृतिक प्रतिक्रिया में लौटाता है

## उदाहरण चलाना

### चरण 1: कैलकुलेटर सर्वर शुरू करें

पहले, अपना GitHub टोकन सेट करें (एआई क्लाइंट के लिए आवश्यक):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

सर्वर शुरू करें:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

सर्वर `http://localhost:8080` पर शुरू होगा। आपको यह दिखाई देगा:
```
Started McpServerApplication in X.XXX seconds
```

### चरण 2: डायरेक्ट क्लाइंट के साथ परीक्षण करें

एक नए टर्मिनल में:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

आपको ऐसा आउटपुट दिखाई देगा:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### चरण 3: एआई क्लाइंट के साथ परीक्षण करें

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

आप देखेंगे कि एआई स्वचालित रूप से टूल्स का उपयोग कर रहा है:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## यह सब कैसे एक साथ काम करता है

जब आप एआई से पूछते हैं "5 + 3 क्या है?" तो पूरा फ्लो इस प्रकार होता है:

1. **आप** एआई से प्राकृतिक भाषा में पूछते हैं
2. **एआई** आपके अनुरोध का विश्लेषण करता है और समझता है कि आप जोड़ना चाहते हैं
3. **एआई** MCP सर्वर को कॉल करता है: `add(5.0, 3.0)`
4. **कैलकुलेटर सेवा** प्रदर्शन करती है: `5.0 + 3.0 = 8.0`
5. **कैलकुलेटर सेवा** लौटाती है: `"5.00 + 3.00 = 8.00"`
6. **एआई** परिणाम प्राप्त करता है और एक प्राकृतिक प्रतिक्रिया में फॉर्मेट करता है
7. **आपको** मिलता है: "5 और 3 का योग 8 है"

## अगले कदम

अधिक उदाहरणों के लिए, देखें [Chapter 04: Practical samples](../../README.md)

**अस्वीकरण**:  
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता सुनिश्चित करने का प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल भाषा में उपलब्ध मूल दस्तावेज़ को आधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।