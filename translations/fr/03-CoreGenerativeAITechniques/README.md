<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T10:33:55+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "fr"
}
-->
# Tutoriel sur les techniques fondamentales de l'IA générative

## Table des matières

- [Prérequis](../../../03-CoreGenerativeAITechniques)
- [Commencer](../../../03-CoreGenerativeAITechniques)
  - [Étape 1 : Définir votre variable d'environnement](../../../03-CoreGenerativeAITechniques)
  - [Étape 2 : Accéder au répertoire des exemples](../../../03-CoreGenerativeAITechniques)
- [Tutoriel 1 : Complétions et Chat avec LLM](../../../03-CoreGenerativeAITechniques)
- [Tutoriel 2 : Appels de fonctions](../../../03-CoreGenerativeAITechniques)
- [Tutoriel 3 : RAG (Génération augmentée par récupération)](../../../03-CoreGenerativeAITechniques)
- [Tutoriel 4 : IA responsable](../../../03-CoreGenerativeAITechniques)
- [Modèles communs dans les exemples](../../../03-CoreGenerativeAITechniques)
- [Étapes suivantes](../../../03-CoreGenerativeAITechniques)
- [Dépannage](../../../03-CoreGenerativeAITechniques)
  - [Problèmes courants](../../../03-CoreGenerativeAITechniques)

## Aperçu

Ce tutoriel propose des exemples pratiques des techniques fondamentales de l'IA générative en utilisant Java et les modèles GitHub. Vous apprendrez à interagir avec les modèles de langage étendu (LLM), à implémenter des appels de fonctions, à utiliser la génération augmentée par récupération (RAG) et à appliquer des pratiques d'IA responsable.

## Prérequis

Avant de commencer, assurez-vous d'avoir :
- Java 21 ou une version supérieure installée
- Maven pour la gestion des dépendances
- Un compte GitHub avec un jeton d'accès personnel (PAT)

## Commencer

### Étape 1 : Définir votre variable d'environnement

Tout d'abord, vous devez définir votre jeton GitHub comme une variable d'environnement. Ce jeton vous permet d'accéder gratuitement aux modèles GitHub.

**Windows (Invite de commandes) :**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell) :**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS :**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Étape 2 : Accéder au répertoire des exemples

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutoriel 1 : Complétions et Chat avec LLM

**Fichier :** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Ce que cet exemple enseigne

Cet exemple montre les mécanismes fondamentaux d'interaction avec les modèles de langage étendu (LLM) via l'API OpenAI, notamment l'initialisation du client avec les modèles GitHub, les structures de messages pour les invites système et utilisateur, la gestion de l'état de la conversation grâce à l'accumulation de l'historique des messages, et le réglage des paramètres pour contrôler la longueur des réponses et les niveaux de créativité.

### Concepts clés du code

#### 1. Configuration du client
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Cela crée une connexion aux modèles GitHub en utilisant votre jeton.

#### 2. Complétion simple
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Mémoire de conversation
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

L'IA se souvient des messages précédents uniquement si vous les incluez dans les requêtes suivantes.

### Exécuter l'exemple
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Ce qui se passe lorsque vous l'exécutez

1. **Complétion simple** : L'IA répond à une question sur Java avec des directives d'invite système.
2. **Chat multi-tours** : L'IA conserve le contexte à travers plusieurs questions.
3. **Chat interactif** : Vous pouvez avoir une vraie conversation avec l'IA.

## Tutoriel 2 : Appels de fonctions

**Fichier :** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Ce que cet exemple enseigne

Les appels de fonctions permettent aux modèles d'IA de demander l'exécution d'outils externes et d'API via un protocole structuré où le modèle analyse les requêtes en langage naturel, détermine les appels de fonctions nécessaires avec les paramètres appropriés en utilisant des définitions de schéma JSON, et traite les résultats retournés pour générer des réponses contextuelles, tandis que l'exécution réelle des fonctions reste sous le contrôle du développeur pour garantir la sécurité et la fiabilité.

### Concepts clés du code

#### 1. Définition des fonctions
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Cela indique à l'IA quelles fonctions sont disponibles et comment les utiliser.

#### 2. Flux d'exécution des fonctions
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Implémentation des fonctions
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Exécuter l'exemple
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Ce qui se passe lorsque vous l'exécutez

1. **Fonction météo** : L'IA demande des données météo pour Seattle, vous les fournissez, l'IA formate une réponse.
2. **Fonction calculatrice** : L'IA demande un calcul (15 % de 240), vous le réalisez, l'IA explique le résultat.

## Tutoriel 3 : RAG (Génération augmentée par récupération)

**Fichier :** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Ce que cet exemple enseigne

La génération augmentée par récupération (RAG) combine la récupération d'informations avec la génération de langage en injectant le contexte de documents externes dans les invites de l'IA, permettant aux modèles de fournir des réponses précises basées sur des sources de connaissances spécifiques plutôt que sur des données d'entraînement potentiellement obsolètes ou inexactes, tout en maintenant des limites claires entre les requêtes utilisateur et les sources d'informations autorisées grâce à une ingénierie stratégique des invites.

### Concepts clés du code

#### 1. Chargement des documents
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Injection de contexte
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Les triples guillemets aident l'IA à distinguer le contexte de la question.

#### 3. Gestion sécurisée des réponses
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Toujours valider les réponses de l'API pour éviter les plantages.

### Exécuter l'exemple
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Ce qui se passe lorsque vous l'exécutez

1. Le programme charge `document.txt` (contient des informations sur les modèles GitHub).
2. Vous posez une question sur le document.
3. L'IA répond uniquement en se basant sur le contenu du document, et non sur ses connaissances générales.

Essayez de demander : "Qu'est-ce que les modèles GitHub ?" vs "Quel temps fait-il ?"

## Tutoriel 4 : IA responsable

**Fichier :** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Ce que cet exemple enseigne

L'exemple d'IA responsable met en avant l'importance de mettre en œuvre des mesures de sécurité dans les applications d'IA. Il montre des filtres de sécurité qui détectent les catégories de contenu nuisible, notamment les discours haineux, le harcèlement, l'automutilation, le contenu sexuel et la violence, démontrant comment les applications d'IA en production doivent gérer avec élégance les violations des politiques de contenu grâce à une gestion appropriée des exceptions, des mécanismes de retour d'information utilisateur et des stratégies de réponse de secours.

### Concepts clés du code

#### 1. Cadre de test de sécurité
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Catégories de sécurité testées
- Instructions de violence/auto-harm
- Discours haineux
- Violations de la vie privée
- Désinformation médicale
- Activités illégales

### Exécuter l'exemple
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Ce qui se passe lorsque vous l'exécutez

Le programme teste diverses invites nuisibles et montre comment le système de sécurité de l'IA :
1. **Bloque les requêtes dangereuses** avec des erreurs HTTP 400.
2. **Permet le contenu sûr** d'être généré normalement.
3. **Protège les utilisateurs** contre les sorties nuisibles de l'IA.

## Modèles communs dans les exemples

### Modèle d'authentification
Tous les exemples utilisent ce modèle pour s'authentifier auprès des modèles GitHub :

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Modèle de gestion des erreurs
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Modèle de structure des messages
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Étapes suivantes

[Chapitre 04 : Exemples pratiques](../04-PracticalSamples/README.md)

## Dépannage

### Problèmes courants

**"GITHUB_TOKEN non défini"**
- Assurez-vous de définir la variable d'environnement.
- Vérifiez que votre jeton a l'étendue `models:read`.

**"Pas de réponse de l'API"**
- Vérifiez votre connexion Internet.
- Assurez-vous que votre jeton est valide.
- Vérifiez si vous avez atteint les limites de taux.

**Erreurs de compilation Maven**
- Assurez-vous d'avoir Java 21 ou une version supérieure.
- Exécutez `mvn clean compile` pour actualiser les dépendances.

**Avertissement** :  
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d'origine doit être considéré comme la source faisant autorité. Pour des informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou d'interprétations erronées résultant de l'utilisation de cette traduction.