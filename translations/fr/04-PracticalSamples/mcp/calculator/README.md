<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:35:06+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "fr"
}
-->
# Tutoriel du Calculateur MCP pour Débutants

## Table des Matières

- [Ce que Vous Allez Apprendre](../../../../../04-PracticalSamples/mcp/calculator)
- [Prérequis](../../../../../04-PracticalSamples/mcp/calculator)
- [Comprendre la Structure du Projet](../../../../../04-PracticalSamples/mcp/calculator)
- [Explication des Composants Principaux](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Application Principale](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Service de Calculatrice](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Client MCP Direct](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Client Alimenté par l'IA](../../../../../04-PracticalSamples/mcp/calculator)
- [Exécution des Exemples](../../../../../04-PracticalSamples/mcp/calculator)
- [Comment Tout Fonctionne Ensemble](../../../../../04-PracticalSamples/mcp/calculator)
- [Prochaines Étapes](../../../../../04-PracticalSamples/mcp/calculator)

## Ce que Vous Allez Apprendre

Ce tutoriel explique comment créer un service de calculatrice en utilisant le protocole Model Context Protocol (MCP). Vous comprendrez :

- Comment créer un service que l'IA peut utiliser comme outil
- Comment configurer une communication directe avec les services MCP
- Comment les modèles d'IA peuvent choisir automatiquement quels outils utiliser
- La différence entre les appels directs au protocole et les interactions assistées par l'IA

## Prérequis

Avant de commencer, assurez-vous d'avoir :
- Java 21 ou une version supérieure installée
- Maven pour la gestion des dépendances
- Un compte GitHub avec un jeton d'accès personnel (PAT)
- Une compréhension de base de Java et Spring Boot

## Comprendre la Structure du Projet

Le projet de calculatrice contient plusieurs fichiers importants :

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

## Explication des Composants Principaux

### 1. Application Principale

**Fichier :** `McpServerApplication.java`

C'est le point d'entrée de notre service de calculatrice. Il s'agit d'une application Spring Boot standard avec une addition spéciale :

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

**Ce que cela fait :**
- Démarre un serveur web Spring Boot sur le port 8080
- Crée un `ToolCallbackProvider` qui rend nos méthodes de calculatrice disponibles en tant qu'outils MCP
- L'annotation `@Bean` indique à Spring de gérer cela comme un composant utilisable par d'autres parties

### 2. Service de Calculatrice

**Fichier :** `CalculatorService.java`

C'est ici que tous les calculs sont effectués. Chaque méthode est marquée avec `@Tool` pour la rendre accessible via MCP :

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

**Caractéristiques principales :**

1. **Annotation `@Tool` :** Cela indique à MCP que cette méthode peut être appelée par des clients externes
2. **Descriptions Claires :** Chaque outil a une description qui aide les modèles d'IA à comprendre quand l'utiliser
3. **Format de Retour Cohérent :** Toutes les opérations renvoient des chaînes lisibles comme "5.00 + 3.00 = 8.00"
4. **Gestion des Erreurs :** La division par zéro et les racines carrées négatives renvoient des messages d'erreur

**Opérations Disponibles :**
- `add(a, b)` - Additionne deux nombres
- `subtract(a, b)` - Soustrait le second du premier
- `multiply(a, b)` - Multiplie deux nombres
- `divide(a, b)` - Divise le premier par le second (avec vérification de zéro)
- `power(base, exponent)` - Élève la base à la puissance de l'exposant
- `squareRoot(number)` - Calcule la racine carrée (avec vérification des négatifs)
- `modulus(a, b)` - Renvoie le reste de la division
- `absolute(number)` - Renvoie la valeur absolue
- `help()` - Renvoie des informations sur toutes les opérations

### 3. Client MCP Direct

**Fichier :** `SDKClient.java`

Ce client communique directement avec le serveur MCP sans utiliser l'IA. Il appelle manuellement des fonctions spécifiques de la calculatrice :

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

**Ce que cela fait :**
1. **Se Connecte** au serveur de calculatrice à `http://localhost:8080`
2. **Liste** tous les outils disponibles (nos fonctions de calculatrice)
3. **Appelle** des fonctions spécifiques avec des paramètres exacts
4. **Affiche** directement les résultats

**Quand l'utiliser :** Lorsque vous savez exactement quel calcul vous voulez effectuer et que vous souhaitez l'appeler de manière programmatique.

### 4. Client Alimenté par l'IA

**Fichier :** `LangChain4jClient.java`

Ce client utilise un modèle d'IA (GPT-4o-mini) qui peut décider automatiquement quels outils de calculatrice utiliser :

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

**Ce que cela fait :**
1. **Crée** une connexion au modèle d'IA en utilisant votre jeton GitHub
2. **Connecte** l'IA à notre serveur MCP de calculatrice
3. **Donne** à l'IA accès à tous nos outils de calculatrice
4. **Permet** des requêtes en langage naturel comme "Calcule la somme de 24,5 et 17,3"

**L'IA fait automatiquement :**
- Comprend que vous voulez additionner des nombres
- Choisit l'outil `add`
- Appelle `add(24.5, 17.3)`
- Renvoie le résultat dans une réponse naturelle

## Exécution des Exemples

### Étape 1 : Démarrer le Serveur de Calculatrice

D'abord, configurez votre jeton GitHub (nécessaire pour le client IA) :

**Windows :**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS :**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Démarrez le serveur :
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Le serveur démarrera sur `http://localhost:8080`. Vous devriez voir :
```
Started McpServerApplication in X.XXX seconds
```

### Étape 2 : Tester avec le Client Direct

Dans un nouveau terminal :
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Vous verrez une sortie comme :
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Étape 3 : Tester avec le Client IA

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Vous verrez l'IA utiliser automatiquement les outils :
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Comment Tout Fonctionne Ensemble

Voici le flux complet lorsque vous demandez à l'IA "Combien font 5 + 3 ?" :

1. **Vous** posez la question à l'IA en langage naturel
2. **L'IA** analyse votre requête et comprend que vous voulez une addition
3. **L'IA** appelle le serveur MCP : `add(5.0, 3.0)`
4. **Le Service de Calculatrice** effectue : `5.0 + 3.0 = 8.0`
5. **Le Service de Calculatrice** renvoie : `"5.00 + 3.00 = 8.00"`
6. **L'IA** reçoit le résultat et le formate dans une réponse naturelle
7. **Vous** recevez : "La somme de 5 et 3 est 8"

## Prochaines Étapes

Pour plus d'exemples, consultez [Chapitre 04 : Exemples pratiques](../../README.md)

**Avertissement** :  
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d'origine doit être considéré comme la source faisant autorité. Pour des informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous ne sommes pas responsables des malentendus ou des interprétations erronées résultant de l'utilisation de cette traduction.