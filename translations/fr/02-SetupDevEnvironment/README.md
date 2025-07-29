<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:13:05+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "fr"
}
-->
# Configuration de l'environnement de développement pour l'IA générative en Java

> **Démarrage rapide** : Codez dans le cloud en 2 minutes - Passez directement à [Configuration de GitHub Codespaces](../../../02-SetupDevEnvironment) - aucune installation locale requise et utilise les modèles GitHub !

> **Intéressé par Azure OpenAI ?** Consultez notre [Guide de configuration Azure OpenAI](getting-started-azure-openai.md) avec les étapes pour créer une nouvelle ressource Azure OpenAI.

## Ce que vous allez apprendre

- Configurer un environnement de développement Java pour les applications d'IA
- Choisir et configurer votre environnement de développement préféré (priorité au cloud avec Codespaces, conteneur de développement local ou configuration locale complète)
- Tester votre configuration en vous connectant aux modèles GitHub

## Table des matières

- [Ce que vous allez apprendre](../../../02-SetupDevEnvironment)
- [Introduction](../../../02-SetupDevEnvironment)
- [Étape 1 : Configurer votre environnement de développement](../../../02-SetupDevEnvironment)
  - [Option A : GitHub Codespaces (Recommandé)](../../../02-SetupDevEnvironment)
  - [Option B : Conteneur de développement local](../../../02-SetupDevEnvironment)
  - [Option C : Utiliser votre installation locale existante](../../../02-SetupDevEnvironment)
- [Étape 2 : Créer un jeton d'accès personnel GitHub](../../../02-SetupDevEnvironment)
- [Étape 3 : Tester votre configuration](../../../02-SetupDevEnvironment)
- [Dépannage](../../../02-SetupDevEnvironment)
- [Résumé](../../../02-SetupDevEnvironment)
- [Prochaines étapes](../../../02-SetupDevEnvironment)

## Introduction

Ce chapitre vous guidera dans la configuration d'un environnement de développement. Nous utiliserons **GitHub Models** comme exemple principal car il est gratuit, facile à configurer avec un simple compte GitHub, ne nécessite pas de carte de crédit et offre l'accès à plusieurs modèles pour l'expérimentation.

**Aucune configuration locale requise !** Vous pouvez commencer à coder immédiatement en utilisant GitHub Codespaces, qui fournit un environnement de développement complet dans votre navigateur.

<img src="./images/models.webp" alt="Capture d'écran : Modèles GitHub" width="50%">

Nous recommandons d'utiliser [**GitHub Models**](https://github.com/marketplace?type=models) pour ce cours car il est :
- **Gratuit** pour commencer
- **Facile** à configurer avec un simple compte GitHub
- **Sans carte de crédit** requise
- **Plusieurs modèles** disponibles pour l'expérimentation

> **Note** : Les modèles GitHub utilisés dans cette formation ont les limites gratuites suivantes :
> - 15 requêtes par minute (150 par jour)
> - ~8 000 mots en entrée, ~4 000 mots en sortie par requête
> - 5 requêtes simultanées
> 
> Pour une utilisation en production, passez aux modèles Azure AI Foundry avec votre compte Azure. Votre code n'a pas besoin de changer. Consultez la [documentation Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Étape 1 : Configurer votre environnement de développement

<a name="quick-start-cloud"></a>

Nous avons créé un conteneur de développement préconfiguré pour minimiser le temps de configuration et garantir que vous disposez de tous les outils nécessaires pour ce cours sur l'IA générative en Java. Choisissez votre approche de développement préférée :

### Options de configuration de l'environnement :

#### Option A : GitHub Codespaces (Recommandé)

**Commencez à coder en 2 minutes - aucune configuration locale requise !**

1. Forkez ce dépôt sur votre compte GitHub
   > **Note** : Si vous souhaitez modifier la configuration de base, consultez le [Fichier de configuration du conteneur de développement](../../../.devcontainer/devcontainer.json)
2. Cliquez sur **Code** → Onglet **Codespaces** → **...** → **Nouveau avec options...**
3. Utilisez les paramètres par défaut – cela sélectionnera la **Configuration du conteneur de développement** : **Environnement de développement Java pour l'IA générative** créé sur mesure pour ce cours
4. Cliquez sur **Créer un codespace**
5. Attendez ~2 minutes que l'environnement soit prêt
6. Passez à [Étape 2 : Créer un jeton GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Capture d'écran : sous-menu Codespaces" width="50%">

<img src="./images/image.png" alt="Capture d'écran : Nouveau avec options" width="50%">

<img src="./images/codespaces-create.png" alt="Capture d'écran : Options de création de codespace" width="50%">

> **Avantages de Codespaces** :
> - Aucune installation locale requise
> - Fonctionne sur tout appareil avec un navigateur
> - Préconfiguré avec tous les outils et dépendances
> - Gratuit 60 heures par mois pour les comptes personnels
> - Environnement cohérent pour tous les apprenants

#### Option B : Conteneur de développement local

**Pour les développeurs qui préfèrent le développement local avec Docker**

1. Forkez et clonez ce dépôt sur votre machine locale
   > **Note** : Si vous souhaitez modifier la configuration de base, consultez le [Fichier de configuration du conteneur de développement](../../../.devcontainer/devcontainer.json)
2. Installez [Docker Desktop](https://www.docker.com/products/docker-desktop/) et [VS Code](https://code.visualstudio.com/)
3. Installez l'extension [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) dans VS Code
4. Ouvrez le dossier du dépôt dans VS Code
5. Lorsque vous y êtes invité, cliquez sur **Reopen in Container** (ou utilisez `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Attendez que le conteneur soit construit et démarré
7. Passez à [Étape 2 : Créer un jeton GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Capture d'écran : Configuration du conteneur de développement" width="50%">

<img src="./images/image-3.png" alt="Capture d'écran : Construction du conteneur de développement terminée" width="50%">

#### Option C : Utiliser votre installation locale existante

**Pour les développeurs disposant déjà d'environnements Java**

Prérequis :
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ou votre IDE préféré

Étapes :
1. Clonez ce dépôt sur votre machine locale
2. Ouvrez le projet dans votre IDE
3. Passez à [Étape 2 : Créer un jeton GitHub](../../../02-SetupDevEnvironment)

> **Astuce Pro** : Si vous avez une machine peu performante mais souhaitez utiliser VS Code localement, utilisez GitHub Codespaces ! Vous pouvez connecter votre VS Code local à un Codespace hébergé dans le cloud pour profiter des avantages des deux mondes.

<img src="./images/image-2.png" alt="Capture d'écran : instance locale du conteneur de développement créée" width="50%">

## Étape 2 : Créer un jeton d'accès personnel GitHub

1. Accédez à [Paramètres GitHub](https://github.com/settings/profile) et sélectionnez **Paramètres** dans le menu de votre profil.
2. Dans la barre latérale gauche, cliquez sur **Paramètres développeur** (généralement en bas).
3. Sous **Jetons d'accès personnel**, cliquez sur **Jetons à portée fine** (ou suivez ce [lien direct](https://github.com/settings/personal-access-tokens)).
4. Cliquez sur **Générer un nouveau jeton**.
5. Sous "Nom du jeton", fournissez un nom descriptif (par ex., `GenAI-Java-Course-Token`).
6. Définissez une date d'expiration (recommandé : 7 jours pour les bonnes pratiques de sécurité).
7. Sous "Propriétaire de la ressource", sélectionnez votre compte utilisateur.
8. Sous "Accès au dépôt", sélectionnez les dépôts que vous souhaitez utiliser avec les modèles GitHub (ou "Tous les dépôts" si nécessaire).
9. Sous "Autorisations du dépôt", trouvez **Modèles** et définissez-le sur **Lecture et écriture**.
10. Cliquez sur **Générer un jeton**.
11. **Copiez et sauvegardez votre jeton maintenant** – vous ne pourrez plus le voir !

> **Conseil de sécurité** : Utilisez le champ d'application minimum requis et la durée d'expiration la plus courte possible pour vos jetons d'accès.

## Étape 3 : Tester votre configuration avec l'exemple des modèles GitHub

Une fois votre environnement de développement prêt, testons l'intégration des modèles GitHub avec notre application exemple dans [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Ouvrez le terminal dans votre environnement de développement.
2. Accédez à l'exemple des modèles GitHub :
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Définissez votre jeton GitHub comme variable d'environnement :
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Exécutez l'application :
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Vous devriez voir une sortie similaire à :
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Comprendre le code exemple

Tout d'abord, comprenons ce que nous venons d'exécuter. L'exemple sous `examples/github-models` utilise le SDK Java OpenAI pour se connecter aux modèles GitHub :

**Ce que fait ce code :**
- **Se connecte** aux modèles GitHub en utilisant votre jeton d'accès personnel
- **Envoie** un simple message "Say Hello World!" au modèle d'IA
- **Reçoit** et affiche la réponse de l'IA
- **Valide** que votre configuration fonctionne correctement

**Dépendance clé** (dans `pom.xml`) :
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Code principal** (`App.java`) :
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Résumé

Bravo ! Vous avez maintenant tout configuré :

- Créé un jeton d'accès personnel GitHub avec les bonnes autorisations pour accéder aux modèles d'IA
- Mis en place votre environnement de développement Java (que ce soit Codespaces, conteneurs de développement ou local)
- Connecté aux modèles GitHub en utilisant le SDK Java OpenAI pour le développement d'IA gratuit
- Testé que tout fonctionne avec un exemple simple qui communique avec les modèles d'IA

## Prochaines étapes

[Chapitre 3 : Techniques fondamentales de l'IA générative](../03-CoreGenerativeAITechniques/README.md)

## Dépannage

Vous rencontrez des problèmes ? Voici les problèmes courants et leurs solutions :

- **Le jeton ne fonctionne pas ?** 
  - Assurez-vous d'avoir copié l'intégralité du jeton sans espaces supplémentaires
  - Vérifiez que le jeton est correctement défini comme variable d'environnement
  - Vérifiez que votre jeton dispose des bonnes autorisations (Modèles : Lecture et écriture)

- **Maven introuvable ?** 
  - Si vous utilisez des conteneurs de développement/Codespaces, Maven devrait être préinstallé
  - Pour une configuration locale, assurez-vous que Java 21+ et Maven 3.9+ sont installés
  - Essayez `mvn --version` pour vérifier l'installation

- **Problèmes de connexion ?** 
  - Vérifiez votre connexion Internet
  - Assurez-vous que GitHub est accessible depuis votre réseau
  - Vérifiez que vous n'êtes pas derrière un pare-feu bloquant le point d'accès des modèles GitHub

- **Le conteneur de développement ne démarre pas ?** 
  - Assurez-vous que Docker Desktop est en cours d'exécution (pour le développement local)
  - Essayez de reconstruire le conteneur : `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Erreurs de compilation de l'application ?**
  - Assurez-vous d'être dans le bon répertoire : `02-SetupDevEnvironment/examples/github-models`
  - Essayez de nettoyer et de reconstruire : `mvn clean compile`

> **Besoin d'aide ?** : Vous avez encore des problèmes ? Ouvrez une issue dans le dépôt et nous vous aiderons.

**Avertissement** :  
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d'origine doit être considéré comme la source faisant autorité. Pour des informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou d'interprétations erronées résultant de l'utilisation de cette traduction.