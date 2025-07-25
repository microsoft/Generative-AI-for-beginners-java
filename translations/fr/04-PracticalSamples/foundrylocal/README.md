<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T10:34:35+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "fr"
}
-->
# Tutoriel Foundry Local Spring Boot

## Table des matières

- [Prérequis](../../../../04-PracticalSamples/foundrylocal)
- [Aperçu du projet](../../../../04-PracticalSamples/foundrylocal)
- [Comprendre le code](../../../../04-PracticalSamples/foundrylocal)
  - [1. Configuration de l'application (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Classe principale de l'application (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Couche de service AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Dépendances du projet (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Comment tout fonctionne ensemble](../../../../04-PracticalSamples/foundrylocal)
- [Configurer Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Exécuter l'application](../../../../04-PracticalSamples/foundrylocal)
- [Résultat attendu](../../../../04-PracticalSamples/foundrylocal)
- [Prochaines étapes](../../../../04-PracticalSamples/foundrylocal)
- [Dépannage](../../../../04-PracticalSamples/foundrylocal)

## Prérequis

Avant de commencer ce tutoriel, assurez-vous d'avoir :

- **Java 21 ou supérieur** installé sur votre système
- **Maven 3.6+** pour construire le projet
- **Foundry Local** installé et en cours d'exécution

### **Installer Foundry Local :**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Aperçu du projet

Ce projet se compose de quatre composants principaux :

1. **Application.java** - Le point d'entrée principal de l'application Spring Boot
2. **FoundryLocalService.java** - Couche de service qui gère la communication avec l'IA
3. **application.properties** - Configuration pour la connexion à Foundry Local
4. **pom.xml** - Dépendances Maven et configuration du projet

## Comprendre le code

### 1. Configuration de l'application (application.properties)

**Fichier :** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Ce que cela fait :**
- **base-url** : Spécifie où Foundry Local est en cours d'exécution (port par défaut 5273)
- **model** : Indique le nom du modèle AI à utiliser pour la génération de texte

**Concept clé :** Spring Boot charge automatiquement ces propriétés et les rend disponibles pour votre application via l'annotation `@Value`.

### 2. Classe principale de l'application (Application.java)

**Fichier :** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Ce que cela fait :**
- `@SpringBootApplication` active la configuration automatique de Spring Boot
- `WebApplicationType.NONE` indique à Spring qu'il s'agit d'une application en ligne de commande, et non d'un serveur web
- La méthode principale démarre l'application Spring

**Le démonstrateur :**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**Ce que cela fait :**
- `@Bean` crée un composant géré par Spring
- `CommandLineRunner` exécute du code après le démarrage de Spring Boot
- `foundryLocalService` est injecté automatiquement par Spring (injection de dépendances)
- Envoie un message de test à l'IA et affiche la réponse

### 3. Couche de service AI (FoundryLocalService.java)

**Fichier :** `src/main/java/com/example/FoundryLocalService.java`

#### Injection de configuration :
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Ce que cela fait :**
- `@Service` indique à Spring que cette classe fournit la logique métier
- `@Value` injecte les valeurs de configuration depuis application.properties
- La syntaxe `:default-value` fournit des valeurs par défaut si les propriétés ne sont pas définies

#### Initialisation du client :
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Ce que cela fait :**
- `@PostConstruct` exécute cette méthode après que Spring a créé le service
- Crée un client OpenAI pointant vers votre instance locale de Foundry Local
- Le chemin `/v1` est requis pour la compatibilité avec l'API OpenAI
- La clé API est "unused" car le développement local ne nécessite pas d'authentification

#### Méthode de chat :
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**Ce que cela fait :**
- **ChatCompletionCreateParams** : Configure la requête AI
  - `model` : Spécifie le modèle AI à utiliser
  - `addUserMessage` : Ajoute votre message à la conversation
  - `maxCompletionTokens` : Limite la longueur de la réponse (économie de ressources)
  - `temperature` : Contrôle l'aléatoire (0.0 = déterministe, 1.0 = créatif)
- **Appel API** : Envoie la requête à Foundry Local
- **Traitement de la réponse** : Extrait la réponse textuelle de l'IA de manière sécurisée
- **Gestion des erreurs** : Enveloppe les exceptions avec des messages d'erreur utiles

### 4. Dépendances du projet (pom.xml)

**Dépendances clés :**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Ce qu'elles font :**
- **spring-boot-starter** : Fournit les fonctionnalités de base de Spring Boot
- **openai-java** : SDK officiel OpenAI pour la communication avec l'API
- **jackson-databind** : Gère la sérialisation/désérialisation JSON pour les appels API

## Comment tout fonctionne ensemble

Voici le flux complet lorsque vous exécutez l'application :

1. **Démarrage** : Spring Boot démarre et lit `application.properties`
2. **Création du service** : Spring crée `FoundryLocalService` et injecte les valeurs de configuration
3. **Configuration du client** : `@PostConstruct` initialise le client OpenAI pour se connecter à Foundry Local
4. **Exécution du démonstrateur** : `CommandLineRunner` s'exécute après le démarrage
5. **Appel AI** : Le démonstrateur appelle `foundryLocalService.chat()` avec un message de test
6. **Requête API** : Le service construit et envoie une requête compatible OpenAI à Foundry Local
7. **Traitement de la réponse** : Le service extrait et renvoie la réponse de l'IA
8. **Affichage** : L'application imprime la réponse et se termine

## Configurer Foundry Local

Pour configurer Foundry Local, suivez ces étapes :

1. **Installez Foundry Local** en suivant les instructions de la section [Prérequis](../../../../04-PracticalSamples/foundrylocal).
2. **Téléchargez le modèle AI** que vous souhaitez utiliser, par exemple, `phi-3.5-mini`, avec la commande suivante :
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configurez le fichier application.properties** pour correspondre à vos paramètres Foundry Local, surtout si vous utilisez un port ou un modèle différent.

## Exécuter l'application

### Étape 1 : Démarrez Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Étape 2 : Construisez et exécutez l'application
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Résultat attendu

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Prochaines étapes

Pour plus d'exemples, consultez [Chapitre 04 : Exemples pratiques](../README.md)

## Dépannage

### Problèmes courants

**"Connexion refusée" ou "Service indisponible"**
- Assurez-vous que Foundry Local est en cours d'exécution : `foundry model list`
- Vérifiez que le service est sur le port 5273 : Consultez `application.properties`
- Essayez de redémarrer Foundry Local : `foundry model run phi-3.5-mini`

**Erreurs "Modèle introuvable"**
- Vérifiez les modèles disponibles : `foundry model list`
- Mettez à jour le nom du modèle dans `application.properties` pour qu'il corresponde exactement
- Téléchargez le modèle si nécessaire : `foundry model run phi-3.5-mini`

**Erreurs de compilation Maven**
- Assurez-vous d'avoir Java 21 ou supérieur : `java -version`
- Nettoyez et reconstruisez : `mvn clean compile`
- Vérifiez la connexion Internet pour télécharger les dépendances

**L'application démarre mais aucun résultat n'est affiché**
- Vérifiez que Foundry Local répond : Ouvrez un navigateur sur `http://localhost:5273`
- Consultez les journaux de l'application pour des messages d'erreur spécifiques
- Assurez-vous que le modèle est entièrement chargé et prêt

**Avertissement** :  
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d'origine doit être considéré comme la source faisant autorité. Pour des informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou d'interprétations erronées résultant de l'utilisation de cette traduction.