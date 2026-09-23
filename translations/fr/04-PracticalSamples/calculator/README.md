# Tutoriel Calculatrice MCP pour Débutants

## Table des Matières

- [Ce que vous apprendrez](#ce-que-vous-apprendrez)
- [Prérequis](#prérequis)
- [Versions des dépendances](#versions-des-dépendances)
- [Comprendre la structure du projet](#comprendre-la-structure-du-projet)
- [Composants principaux expliqués](#composants-principaux-expliqués)
  - [1. Application principale](#1-application-principale)
  - [2. Service Calculatrice](#2-service-calculatrice)
  - [3. Client MCP direct](#3-client-mcp-direct)
  - [4. Client propulsé par IA](#4-client-propulsé-par-ia)
- [Exécution des exemples](#exécution-des-exemples)
- [Tests hors ligne](#tests-hors-ligne)
- [Comment tout fonctionne ensemble](#comment-tout-fonctionne-ensemble)
- [Étapes suivantes](#étapes-suivantes)

## Ce que vous apprendrez

Ce tutoriel explique comment construire un service calculatrice utilisant le Protocole de Contexte de Modèle (MCP). Vous comprendrez :

- Comment créer un service qu'une IA peut utiliser comme outil
- Comment configurer une communication directe avec des services MCP
- Comment les modèles d'IA peuvent choisir automatiquement quels outils utiliser
- La différence entre les appels directs au protocole et les interactions assistées par IA

## Prérequis

Avant de commencer, assurez-vous d'avoir :
- Java 21 ou supérieur installé
- Maven pour la gestion des dépendances
- Une compréhension basique de Java et Spring Boot

Seuls les clients IA nécessitent un déploiement Azure OpenAI et un `DefaultAzureCredential` authentifié,
comme une connexion existante Azure CLI locale ou une identité gérée dans Azure. L'identité doit
avoir le rôle Utilisateur Cognitive Services OpenAI sur la ressource. Voir [Chapitre 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Le serveur, le client SDK direct et tous les tests automatisés n'ont pas besoin de compte Azure ou d'accès à un modèle.

## Versions des dépendances

Dépendances de la version vérifiées le 14-09-2026 :

| Dépendance | Version |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (géré par Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Adaptateur officiel LangChain4j OpenAI | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (géré par Boot) | 6.0.3 |

Les adaptateurs MCP et officiel OpenAI sont des versions beta publiées sur Maven Central, sans snapshots.
Leurs versions diffèrent de LangChain4j core. Aucun dépôt snapshot ou milestone n'est nécessaire.
Les dépendances client seulement ont un scope test car les exemples exécutables vivent sous `src/test/java`.

## Comprendre la structure du projet

Le projet calculatrice contient plusieurs fichiers importants :

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Composants principaux expliqués

### 1. Application principale

**Fichier :** `McpServerApplication.java`

C'est le point d'entrée de notre service calculatrice. C'est une application standard Spring Boot avec une addition spéciale :

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
- Crée un `ToolCallbackProvider` qui rend nos méthodes calculatrice disponibles comme outils MCP
- L'annotation `@Bean` indique à Spring de gérer ce composant que d'autres parties peuvent utiliser

### 2. Service Calculatrice

**Fichier :** `CalculatorService.java`

C'est là que tout le calcul se passe. Chaque méthode est marquée avec `@Tool` pour la rendre disponible via MCP :

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
    
    // Plus d'opérations de calculatrice...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Caractéristiques clés :**

1. **Annotation `@Tool`** : Cela indique au MCP que cette méthode peut être appelée par des clients externes
2. **Descriptions claires** : Chaque outil a une description qui aide les modèles IA à comprendre quand l'utiliser
3. **Format de retour cohérent** : Toutes les opérations retournent des chaînes lisibles comme "5.00 + 3.00 = 8.00"
4. **Gestion des erreurs** : La division par zéro ou la racine carrée négative retournent des messages d’erreur

**Opérations disponibles :**
- `add(a, b)` - Additionne deux nombres
- `subtract(a, b)` - Soustrait le second du premier
- `multiply(a, b)` - Multiplie deux nombres
- `divide(a, b)` - Divise le premier par le second (avec contrôle zéro)
- `power(base, exponent)` - Élève la base à la puissance exposant
- `squareRoot(number)` - Calcule la racine carrée (avec contrôle négatif)
- `modulus(a, b)` - Retourne le reste de la division
- `absolute(number)` - Retourne la valeur absolue
- `help()` - Retourne des informations sur toutes les opérations

### 3. Client MCP direct

Voir [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Ce client utilise `HttpClientStreamableHttpTransport` à `/mcp`, initialise la connexion,
ping le serveur et gère la pagination de la liste des outils. Il vérifie que les neuf outils
attendus existent et appelle chacun d’eux, y compris `modulus` et `help`, sans modèle IA.

Le constructeur de requête actuel ressemble à ceci :

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Les erreurs de protocole échouent le client au lieu d’afficher un succès trompeur. Le client MCP
est fermé avec try-with-resources, y compris lorsque la découverte ou un appel d’outil échoue.

### 4. Client propulsé par IA

Voir [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
et [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implémente l’API actuelle `ChatModel` de LangChain4j.
`StreamableHttpMcpTransport` le connecte au même point d’accès `/mcp` que le client SDK.
`AiServices` découvre les outils et gère la conversation d’appel/résultat d’outil.

Le déploiement par défaut est **GPT-5.6 Luna**, avec le raisonnement explicitement désactivé :

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ces paramètres par défaut s'appliquent à chaque complétion, y compris les suivis après exécution d’outil.
Le client utilise un `BearerTokenCredential` rafraîchissable avec `DefaultAzureCredential`
et le scope `https://ai.azure.com/.default`, pas un jeton unique passé comme clé API.
Les URLs de ressources et celles finissant déjà en `/openai/v1` sont acceptées.

Le bot garde un historique de conversation limité, affiche `Tool executed: ...` avec le résultat
réel MCP, et échoue si une réponse saute les outils. Les boucles d’outil sont limitées à quatre allers-retours.
Les erreurs d’authentification, de modèle, MCP et outil se propagent ; les réessais automatiques du modèle sont désactivés.
Les transports/clients MCP et OpenAI officiels sont fermés en cas de succès ou d’échec.

## Exécution des exemples

### Étape 1 : Démarrer le serveur Calculatrice

Aucune configuration Azure n’est nécessaire pour le serveur. Les commandes ci-dessous s’exécutent depuis le répertoire de cet exemple.
L'exemple utilise le port **18081** pour éviter les conflits avec un autre exemple ; le port par défaut reste 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Le point d'accès MCP est `http://localhost:18081/mcp`. Les informations de santé et découverte sont à
`http://localhost:18081/health` et `http://localhost:18081/info`.
Le HTTP Streamable remplace l’ancien transport SSE uniquement ; `/sse` et `/v1/tools` ne sont pas des points d’accès.

### Étape 2 : Tester avec le client direct

Dans un autre terminal PowerShell :

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Aucune saisie n’est nécessaire. Tous les neuf outils sont exercés. Les résultats arithmétiques attendus incluent
8, 6, 42, 5, 256, 4, 2 et 5,5, suivis du texte d’aide.

### Étape 3 : Tester avec le client IA

Après authentification comme décrit dans les prérequis, configurez le client IA dans le même terminal :

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Attendez une ligne `Tool executed: add` avec `41.80`, suivie de la réponse du modèle.
Le mode simple prompt se termine sans attendre d’entrée. Pour lancer la démo originale à quatre prompts :

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

La démo appelle `add`, `squareRoot`, `help`, puis la chaîne `power` puis `divide`.
Les réponses numériques attendues sont 41,8, 12 et 64. Omettre les arguments lance aussi cette démo.

### Étape 4 : Lancer le bot interactif

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Tapez `Multiply 6 by 7 using the calculator service`, puis `exit` ou `quit`.
Attendez un résultat réel d’outil `multiply` de 42. Les lignes vides sont ignorées ; EOF termine aussi la session.
Pour un test non interactif au niveau de ce point d'entrée :

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Les deux points d’entrée IA acceptent `--prompt "question"`, `--demo`, et `--interactive`.
Les options invalides échouent avant d’ouvrir une connexion. Chaque argument Maven `-D...` est entièrement cité
pour PowerShell. Sous Bash, utilisez `export NAME=value` au lieu de `$env:NAME = "value"`.

**Quota :** Exécutez les exemples IA séquentiellement. Un prompt simple nécessite normalement deux requêtes modèle ;
la démo complète en nécessite normalement neuf, incluant les suivis résultat d’outil. Avec un déploiement 10 RPM partagé,
laissez une nouvelle fenêtre de quota avant la prochaine exécution IA. Un 429 échoue visiblement sans
réessais automatiques ; suivez les instructions retry-after du service. Le nombre réel de requêtes dépend du modèle.
Les tests hors ligne ne consomment aucun quota et ne valident pas la disponibilité ou qualité de réponse Luna en direct.

### Configuration et arrêt

| Paramètre | Par défaut / comportement |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080` ; URL de base, sans `/mcp` |
| `-Dmcp.server.url=...` | Remplace `MCP_SERVER_URL` pour tous les clients |
| `AZURE_OPENAI_ENDPOINT` | Requis seulement pour clients IA ; URL ressource ou URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna` ; nom de déploiement Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024` ; entier positif |
| Effort de raisonnement | Toujours `none`, y compris suivi boucle outil |

Un déploiement personnalisé doit supporter `reasoning_effort=none` et `max_completion_tokens`.
Les clients ne lisent pas automatiquement un fichier `.env`. Arrêtez le serveur avec `Ctrl+C` après les tests.
Les clients retournent normalement sans `System.exit` ni temporisations d'arrêt.

## Tests hors ligne

```powershell
mvn -B -ntp clean verify
```

Tous les tests sont hors ligne vis-à-vis d’Azure : la suite protocole démarre un serveur Spring et
un stub compatible OpenAI sur des ports loopback aléatoires, puis les ferme. Maven peut toujours devoir
télécharger des dépendances. Aucune authentification, déploiement en direct ou serveur MCP préexistant n’est utilisé.

- Les tests unitaires calculatrice couvrent toutes les opérations arithmétiques, résultats décimaux, aide, et erreurs domaine.
- Les tests MCP couvrent l’initialisation, découverte, les neuf appels d’outil, échecs d’outil, et santé/info.
- Les tests protocole IA exécutent la démo complète et le Bot interactif contre la vraie calculatrice,
  vérifient que les résultats d’outil alimentent la complétion suivante, et inspectent chaque corps HTTP pour Luna,
  `reasoning_effort: "none"`, et `max_completion_tokens` sans `max_tokens` hérités.
- Les tests de configuration/saisie couvrent les remplacements de déploiement et d’endpoint, lignes vides, EOF, exit/quit,
  mode simple prompt, options invalides, et propagation d’erreur. Les tests de quota prouvent qu’un 429 n’est pas réessayé.

## Comment tout fonctionne ensemble

Voici le flux complet quand vous demandez à l’IA « Quel est le résultat de 5 + 3 ? » :

1. **Vous** posez la question à l’IA en langage naturel
2. **L’IA** analyse votre demande et comprend que vous souhaitez une addition
3. **L’IA** appelle le serveur MCP : `add(5.0, 3.0)`
4. **Le service calculatrice** effectue : `5.0 + 3.0 = 8.0`
5. **Le service calculatrice** retourne : `"5.00 + 3.00 = 8.00"`
6. **L’IA** reçoit le résultat et formate une réponse naturelle
7. **Vous** obtenez : « La somme de 5 et 3 est 8 »

## Étapes suivantes

Pour plus d’exemples, voir [Chapitre 04 : Exemples pratiques](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou erreurs d'interprétation découlant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->