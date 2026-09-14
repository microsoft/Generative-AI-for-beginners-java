# Tutoriel du Générateur d'Histoires pour Animaux de Compagnie pour Débutants

Téléchargez une photo d'animal, analysez-la avec GPT-5.6 Luna, et générez une histoire à partir de la description résultante. Les deux requêtes au modèle utilisent `reasoning_effort: none`.

| Composant | Version |
| --- | --- |
| Java | 21 ou supérieur |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Table des matières

- [Prérequis](#prérequis)
- [Comprendre la structure du projet](#comprendre-la-structure-du-projet)
- [Explication des composants principaux](#explication-des-composants-principaux)
  - [1. Application principale](#1-application-principale)
  - [2. Contrôleur Web](#2-contrôleur-web)
  - [3. Service d'histoire](#3-service-dhistoire)
  - [4. Templates Web](#4-templates-web)
  - [5. Configuration](#5-configuration)
- [Exécuter l'application](#exécuter-lapplication)
- [Tests hors ligne](#tests-hors-ligne)
- [Comment tout fonctionne ensemble](#comment-tout-fonctionne-ensemble)
- [Comprendre l'intégration de l'IA](#comprendre-lintégration-de-lia)
- [Étapes suivantes](#étapes-suivantes)

## Prérequis

Avant de commencer, assurez-vous d'avoir :
- Java 21 ou version supérieure installé
- Maven pour la gestion des dépendances
- Un déploiement Azure AI Foundry de GPT-5.6 Luna nommé `gpt-5.6-luna`, ou une substitution `AZURE_OPENAI_DEPLOYMENT` pointant vers ce déploiement. Consultez [Chapitre 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) pour la mise en place et connectez-vous avec `az login` pour une authentification sans clé. Le déploiement doit supporter l'entrée d'image et `reasoning_effort: none`.
- Connaissances de base en Java, Spring Boot et développement web

## Comprendre la structure du projet

Le projet d'histoire pour animaux de compagnie comprend plusieurs fichiers importants :

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Explication des composants principaux

### 1. Application principale

**Fichier :** `PetStoryApplication.java`

C'est le point d'entrée de notre application Spring Boot :

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ce que cela fait :**
- L'annotation `@SpringBootApplication` active la configuration automatique et la recherche de composants
- Démarre un serveur web embarqué (Tomcat) sur le port 8080
- Crée automatiquement tous les beans et services Spring nécessaires

### 2. Contrôleur Web

**Fichier :** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Requête | Réponse réussie |
| --- | --- | --- |
| `GET /` | Pas de corps | Formulaire HTML de téléchargement avec un token CSRF |
| `POST /analyze-image` | `multipart/form-data`, champ fichier `image` | JSON : `{"description":"Un animal joueur..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, champ `description` | Page de résultat HTML avec la description et l'histoire générée |

Les deux endpoints POST requièrent le cookie de session et le token CSRF obtenus via `GET /`. Le script de téléchargement envoie la valeur cachée `_csrf` dans l'en-tête `X-CSRF-TOKEN`; la soumission de l'histoire l'envoie comme champ de formulaire `_csrf`. Les clients API doivent préserver le cookie entre les requêtes. Ce sont des endpoints de formulaire, pas des endpoints de requêtes JSON.

Les descriptions doivent être non vides et ne pas dépasser 1000 caractères. Le contrôleur tronque la description et supprime `<`, `>`, guillemets doubles, apostrophes et `&` avant de la transmettre au service. Le template de résultat échappe aussi la sortie du modèle avec `th:text`.

Les échecs de validation d'image renvoient HTTP 400 avec un champ `error`; les échecs du modèle renvoient HTTP 502 avec un champ `error` et sans `description`. Les descriptions d'histoire invalides ou les échecs du modèle redirigent vers `/` avec une erreur visible. Les champs requis manquants renvoient HTTP 400, et les tokens CSRF manquants ou invalides renvoient HTTP 403. Aucune description de secours ni histoire n'est présentée comme résultat AI réussi.

### 3. Service d'histoire

**Fichier :** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Le SDK officiel OpenAI Java 4.63.1 appelle l'API Chat Completions compatible OpenAI d'Azure AI Foundry. Azure Identity 1.18.6 fournit un token Microsoft Entra via `DefaultAzureCredential`; aucune clé API n'est nécessaire.

| Opération | Entrée | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Octets d'image encodés en URL de données base64 avec le type MIME téléchargé | 300 |
| `generateStory` | Une description d'animal dans un message utilisateur | 800 |

Les deux requêtes utilisent le déploiement configuré, par défaut `gpt-5.6-luna`, et définissent explicitement `ReasoningEffort.NONE` (`reasoning_effort: none`). Aucune requête n'envoie `temperature` ni l'ancien paramètre `max_tokens`.

L'analyse d'image accepte JPEG, PNG, GIF et WebP, rejette les images vides et les fichiers de plus de 10 Mo, et limite la description résultante à 1000 caractères. Le prompt de l'histoire demande une courte histoire familiale. Les choix vides ou contenu modèle vide sont des erreurs, et les échecs conservent la cause d'origine pour un diagnostic serveur. Le client SDK est fermé à l'arrêt de l'application.

### 4. Templates Web

**Fichier :** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formulaire de téléchargement)

La page commence par un sélecteur de photo, pas une zone de texte pour la description. **Analyze Image** prévisualise la photo sélectionnée et la poste à `/analyze-image`. Une réponse réussie affiche la description, remplit le champ caché `description`, et révèle **Generate Story**. Ce bouton soumet le formulaire existant à `/generate-story`.

Il n'y a pas de téléchargement de modèle navigateur ni dépendance CDN. L'analyse d'image s'exécute sur le serveur via le déploiement Azure configuré. Les échecs restent visibles et ne permettent pas la génération d'histoire avec une description fabriquée. La sélection d'un autre fichier efface l'analyse précédente.

**Fichier :** `result.html` (Affichage de l'histoire)

Affiche l'histoire générée :

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Fonctionnalités du template :**

1. **Intégration Thymeleaf** : Utilise les attributs `th:` pour du contenu dynamique
2. **Design Responsive** : Style CSS pour mobile et bureau
3. **Gestion des erreurs** : Affiche les erreurs de validation aux utilisateurs
4. **Gestion du téléchargement** : JavaScript prévisualise la photo, envoie une requête multipart protégée CSRF, et affiche la description renvoyée

### 5. Configuration

**Fichier :** `application.properties`

Paramètres de configuration de l'application :

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Explication de la configuration :**

1. **Téléchargement de fichiers** : Le fichier et la requête multipart complète sont limités à 10 Mo ; gardez les photos en dessous de cette limite pour laisser de la place aux entêtes multipart
2. **Journalisation** : Contrôle les informations enregistrées durant l'exécution
3. **Azure AI Foundry** : Spécifie le point de terminaison et le déploiement du modèle à utiliser (authentification sans clé)
4. **Sécurité** : Protection CSRF reste activée ; les diagnostics du modèle sont journalisés sur le serveur, tandis que le contrôleur affiche des messages génériques en cas d'échec du modèle

## Exécuter l'application

### Étape 1 : Connexion et configuration de votre point de terminaison

L'authentification est sans clé (Microsoft Entra ID), donc il n'y a pas de clé API. Connectez-vous et configurez votre point de terminaison Foundry :

**Windows (Invite de commandes) :**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell) :**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS :**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Pourquoi c'est nécessaire :**
- Azure AI Foundry utilise Microsoft Entra ID pour authentifier les requêtes d'inférence
- L'authentification sans clé signifie aucune clé secrète dans votre code source ou environnement
- Votre compte doit avoir le rôle **Cognitive Services OpenAI User** sur la ressource

Le nom de déploiement par défaut est `gpt-5.6-luna`. Si votre déploiement GPT-5.6 Luna porte un autre nom, configurez `AZURE_OPENAI_DEPLOYMENT` dans le même terminal avant de démarrer l'application. L'analyse d'image et la génération d'histoire utilisent ce paramètre.

### Étape 2 : Construire et exécuter

Naviguez vers le répertoire du projet :
```bash
cd 04-PracticalSamples/petstory
```

Construisez le JAR exécutable autonome et lancez tous les tests hors ligne :
```bash
mvn clean package
```

Démarrez le serveur :
```bash
mvn spring-boot:run
```

L'application démarrera sur `http://localhost:8080`.

Alternativement, lancez le JAR empaqueté sur un port libre, par exemple :

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Pour cette commande, ouvrez `http://localhost:8083/`. Les mêmes routes `/analyze-image` et `/generate-story` sont disponibles sur le port choisi.

### Étape 3 : Tester l'application

1. **Ouvrez** `http://localhost:8080` dans votre navigateur
2. **Sélectionnez** une photo claire d'animal au format JPEG, PNG, GIF, ou WebP, inférieure à 10 Mo
3. **Cliquez** sur "Analyze Image" et attendez la description de l'animal
4. **Cliquez** sur "Generate Story" après une analyse réussie
5. **Consultez** l'histoire et utilisez le lien de la page de résultat pour revenir au formulaire de téléchargement

Le flux réussi photo-vers-histoire effectue deux appels au modèle, un par bouton. L'inférence en direct consomme le quota de votre déploiement et peut engendrer des coûts ; exécutez les tests rapides en série lorsqu'on partage un déploiement à débit limité. Le chargement de la page d'accueil n'appelle pas le modèle.

## Tests hors ligne

Depuis le répertoire sample, exécutez :

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) capture les requêtes réelles du SDK OpenAI avec un fixture HTTP en boucle locale. Il vérifie à la fois le déploiement de la requête, `reasoning_effort: none`, les limites de tokens, la charge utile image, la validation des entrées, les réponses vides et les erreurs en amont.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) utilise MockMvc avec un service modèle simulé pour tester les pages Thymeleaf rendues, le contrat de téléchargement, CSRF, la validation, l'échappement de sortie, et les échecs visibles. Ces tests n'ont pas besoin de certificats Azure et n'appellent jamais l'inférence payante Azure. Maven écrit les rapports Surefire sous `target/surefire-reports`.

## Comment tout fonctionne ensemble

Voici le flux complet lorsque vous générez une histoire d'animal :

1. **Sélection de photo** : Vous choisissez une image d'animal dans le formulaire de téléchargement
2. **Téléchargement d'image** : "Analyze Image" envoie un POST multipart à `/analyze-image` avec l'en-tête CSRF
3. **Analyse d'image** : `StoryService` envoie l'image à GPT-5.6 Luna avec le raisonnement réglé sur `none`
4. **Affichage de la description** : Le navigateur affiche la description renvoyée et la stocke dans le formulaire
5. **Soumission de l'histoire** : "Generate Story" poste `description` et `_csrf` à `/generate-story`
6. **Génération de l'histoire** : Le contrôleur valide la description et appelle le même déploiement avec le raisonnement réglé sur `none`
7. **Rendu du template** : Thymeleaf échappe et affiche la description ainsi que l'histoire dans la page de résultat

**Gestion des erreurs :**
Si le modèle échoue, le serveur logue la cause. L'analyse d'image renvoie HTTP 502 et le navigateur affiche l'erreur sans révéler "Generate Story". La génération d'histoire redirige vers le formulaire avec un message d'erreur. Aucun chemin ne substitue silencieusement un résultat pré-écrit.

## Comprendre l'intégration de l'IA

### Azure AI Foundry (sans clé)
Le service configure le SDK avec le point d'accès `/openai/v1/` de votre ressource. `DefaultAzureCredential` et `AuthenticationUtil.getBearerTokenSupplier` fournissent des tokens Microsoft Entra pour `https://ai.azure.com/.default`. Le développement local peut utiliser votre connexion Azure CLI ; une application hébergée sur Azure peut utiliser une identité managée avec les permissions nécessaires sur la ressource.

### Conception du prompt
L'analyse d'image demande des caractéristiques observables de l'animal en un court paragraphe et dit au modèle de traiter le texte dans l'image comme des données, pas des instructions. La génération d'histoire utilise la description retournée dans une requête d'écriture séparée, familiale. Aucun appel n'active le raisonnement ni ne définit une température personnalisée.

### Traitement de la réponse
Le gestionnaire commun des réponses rejette les choix manquants et le contenu vide ou composé uniquement d'espaces, tronque le contenu valide et conserve les échecs en amont. Les descriptions d'image sont limitées à 1000 caractères pour tenir dans le formulaire d'histoire ultérieur. L'échec original du modèle est conservé pour le diagnostic mais n'est pas affiché à l'utilisateur.

## Étapes suivantes

Pour plus d'exemples, voir [Chapitre 04 : Exemples pratiques](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou erreurs d'interprétation découlant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->