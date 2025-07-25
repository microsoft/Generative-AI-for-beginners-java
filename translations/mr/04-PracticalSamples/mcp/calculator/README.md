<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:11:57+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "mr"
}
-->
# MCP कॅल्क्युलेटर ट्यूटोरियल नवशिक्यांसाठी

## विषय सूची

- [तुम्ही काय शिकाल](../../../../../04-PracticalSamples/mcp/calculator)
- [पूर्वतयारी](../../../../../04-PracticalSamples/mcp/calculator)
- [प्रोजेक्टची रचना समजून घेणे](../../../../../04-PracticalSamples/mcp/calculator)
- [मुख्य घटकांचे स्पष्टीकरण](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. मुख्य अॅप्लिकेशन](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. कॅल्क्युलेटर सेवा](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. डायरेक्ट MCP क्लायंट](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-सक्षम क्लायंट](../../../../../04-PracticalSamples/mcp/calculator)
- [उदाहरण चालवणे](../../../../../04-PracticalSamples/mcp/calculator)
- [सर्व काही एकत्र कसे कार्य करते](../../../../../04-PracticalSamples/mcp/calculator)
- [पुढील पायऱ्या](../../../../../04-PracticalSamples/mcp/calculator)

## तुम्ही काय शिकाल

या ट्यूटोरियलमध्ये Model Context Protocol (MCP) वापरून कॅल्क्युलेटर सेवा कशी तयार करायची हे समजावले आहे. तुम्ही शिकाल:

- AI साठी टूल म्हणून सेवा कशी तयार करायची
- MCP सेवांसोबत थेट संवाद कसा सेट करायचा
- AI मॉडेल्स आपोआप कोणती टूल्स वापरायची ते कसे निवडतात
- डायरेक्ट प्रोटोकॉल कॉल्स आणि AI-सह संवाद यामधील फरक

## पूर्वतयारी

सुरुवात करण्यापूर्वी, तुमच्याकडे खालील गोष्टी असणे आवश्यक आहे:
- Java 21 किंवा त्याहून अधिक आवृत्ती इंस्टॉल केलेली
- Maven डिपेंडन्सी व्यवस्थापनासाठी
- GitHub खाते आणि वैयक्तिक प्रवेश टोकन (PAT)
- Java आणि Spring Boot चा मूलभूत समज

## प्रोजेक्टची रचना समजून घेणे

कॅल्क्युलेटर प्रोजेक्टमध्ये काही महत्त्वाचे फाइल्स आहेत:

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

## मुख्य घटकांचे स्पष्टीकरण

### 1. मुख्य अॅप्लिकेशन

**फाइल:** `McpServerApplication.java`

हे आमच्या कॅल्क्युलेटर सेवेसाठी प्रवेश बिंदू आहे. हे एक मानक Spring Boot अॅप्लिकेशन आहे ज्यामध्ये एक विशेष जोड आहे:

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

**याचा उपयोग:**
- Spring Boot वेब सर्व्हर पोर्ट 8080 वर सुरू करतो
- `ToolCallbackProvider` तयार करतो ज्यामुळे आमच्या कॅल्क्युलेटर पद्धती MCP टूल्स म्हणून उपलब्ध होतात
- `@Bean` अॅनोटेशन Spring ला हे घटक म्हणून व्यवस्थापित करण्यास सांगते जे इतर भाग वापरू शकतात

### 2. कॅल्क्युलेटर सेवा

**फाइल:** `CalculatorService.java`

इथे सर्व गणितीय क्रिया होतात. प्रत्येक पद्धती `@Tool` ने चिन्हांकित केलेली आहे ज्यामुळे ती MCP द्वारे उपलब्ध होते:

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

**महत्त्वाचे वैशिष्ट्ये:**

1. **`@Tool` अॅनोटेशन**: MCP ला सांगते की ही पद्धत बाह्य क्लायंट्सद्वारे कॉल केली जाऊ शकते
2. **स्पष्ट वर्णन**: प्रत्येक टूलमध्ये वर्णन असते जे AI मॉडेल्सना ते कधी वापरायचे ते समजण्यास मदत करते
3. **सुसंगत परतावा स्वरूप**: सर्व ऑपरेशन्स मानवी-वाचनीय स्ट्रिंग्स परत करतात जसे "5.00 + 3.00 = 8.00"
4. **त्रुटी हाताळणी**: शून्याने विभागणी आणि नकारात्मक वर्गमूळ त्रुटी संदेश परत करतात

**उपलब्ध ऑपरेशन्स:**
- `add(a, b)` - दोन संख्यांची बेरीज करते
- `subtract(a, b)` - दुसऱ्या संख्येने पहिली संख्या वजा करते
- `multiply(a, b)` - दोन संख्यांचा गुणाकार करते
- `divide(a, b)` - पहिली संख्या दुसऱ्याने विभागते (शून्य तपासणीसह)
- `power(base, exponent)` - बेसला एक्सपोनेंटने उचलते
- `squareRoot(number)` - वर्गमूळ काढते (नकारात्मक तपासणीसह)
- `modulus(a, b)` - विभागणीचा शिल्लक परत करते
- `absolute(number)` - संख्येचे परिपूर्ण मूल्य परत करते
- `help()` - सर्व ऑपरेशन्सबद्दल माहिती परत करते

### 3. डायरेक्ट MCP क्लायंट

**फाइल:** `SDKClient.java`

हा क्लायंट AI वापरल्याशिवाय MCP सर्व्हरशी थेट संवाद साधतो. तो विशिष्ट कॅल्क्युलेटर फंक्शन्स मॅन्युअली कॉल करतो:

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

**याचा उपयोग:**
1. **कनेक्ट** कॅल्क्युलेटर सर्व्हरशी `http://localhost:8080` वर
2. **लिस्ट** सर्व उपलब्ध टूल्स (आमचे कॅल्क्युलेटर फंक्शन्स)
3. **कॉल** विशिष्ट फंक्शन्स अचूक पॅरामीटर्ससह
4. **प्रिंट** परिणाम थेट

**कधी वापरायचे:** जेव्हा तुम्हाला अचूक गणना करायची आहे आणि ती प्रोग्रामॅटिकली कॉल करायची आहे.

### 4. AI-सक्षम क्लायंट

**फाइल:** `LangChain4jClient.java`

हा क्लायंट AI मॉडेल (GPT-4o-mini) वापरतो जे आपोआप कोणती कॅल्क्युलेटर टूल्स वापरायची ते ठरवते:

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

**याचा उपयोग:**
1. **AI मॉडेल कनेक्शन तयार करते** तुमच्या GitHub टोकन वापरून
2. **AI ला कनेक्ट करते** आमच्या कॅल्क्युलेटर MCP सर्व्हरशी
3. **AI ला देते** आमच्या सर्व कॅल्क्युलेटर टूल्सचा प्रवेश
4. **नैसर्गिक भाषा विनंत्या सक्षम करते** जसे "24.5 आणि 17.3 ची बेरीज करा"

**AI आपोआप:**
- तुमची विनंती समजते की तुम्हाला संख्यांची बेरीज करायची आहे
- `add` टूल निवडते
- `add(24.5, 17.3)` कॉल करते
- नैसर्गिक प्रतिसादात परिणाम परत करते

## उदाहरण चालवणे

### पायरी 1: कॅल्क्युलेटर सर्व्हर सुरू करा

प्रथम, तुमचे GitHub टोकन सेट करा (AI क्लायंटसाठी आवश्यक):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

सर्व्हर सुरू करा:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

सर्व्हर `http://localhost:8080` वर सुरू होईल. तुम्हाला दिसेल:
```
Started McpServerApplication in X.XXX seconds
```

### पायरी 2: डायरेक्ट क्लायंटसह चाचणी करा

नवीन टर्मिनलमध्ये:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

तुम्हाला असे आउटपुट दिसेल:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### पायरी 3: AI क्लायंटसह चाचणी करा

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

तुम्हाला AI टूल्स आपोआप वापरताना दिसेल:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## सर्व काही एकत्र कसे कार्य करते

जेव्हा तुम्ही AI ला विचारता "5 + 3 किती?" तेव्हा संपूर्ण प्रवाह असा असतो:

1. **तुम्ही** AI ला नैसर्गिक भाषेत विचारता
2. **AI** तुमची विनंती विश्लेषित करते आणि समजते की तुम्हाला बेरीज करायची आहे
3. **AI** MCP सर्व्हरला कॉल करते: `add(5.0, 3.0)`
4. **कॅल्क्युलेटर सेवा** कार्य करते: `5.0 + 3.0 = 8.0`
5. **कॅल्क्युलेटर सेवा** परत करते: `"5.00 + 3.00 = 8.00"`
6. **AI** परिणाम प्राप्त करते आणि नैसर्गिक प्रतिसाद तयार करते
7. **तुम्हाला** मिळते: "5 आणि 3 ची बेरीज 8 आहे"

## पुढील पायऱ्या

अधिक उदाहरणांसाठी, पहा [Chapter 04: Practical samples](../../README.md)

**अस्वीकरण**:  
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेचा अभाव असू शकतो. मूळ भाषेतील दस्तऐवज हा अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराचा वापर करून उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थासाठी आम्ही जबाबदार राहणार नाही.