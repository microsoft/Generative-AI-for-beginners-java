# Chat de base avec Azure AI Foundry - Exemple de bout en bout

Cet exemple est une application Spring Boot simple qui se connecte à un modèle **Azure AI Foundry** en utilisant **l'authentification sans clé** (Microsoft Entra ID) et teste votre configuration. Elle utilise le `ChatClient` de Spring AI, reposant sur le **SDK Java officiel OpenAI** et le point de terminaison **Azure OpenAI v1**.

Les versions dans [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) sont Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, et dotenv-java **3.2.0**. L'exemple utilise `spring-ai-starter-model-openai` et déclare explicitement `openai-java` et `azure-identity` ; Spring AI 2 a supprimé l'ancien starter Azure OpenAI.

## Table des matières

- [Prérequis](#prérequis)
- [Démarrage rapide](#démarrage-rapide)
- [Comment fonctionne l'authentification](#comment-fonctionne-lauthentification)
- [Exécution de l'application](#exécution-de-lapplication)
  - [Utilisation de Maven](#utilisation-de-maven)
  - [Utilisation de VS Code](#utilisation-de-vs-code)
  - [Sortie attendue](#sortie-attendue)
- [Référence de configuration](#référence-de-configuration)
  - [Variables d'environnement](#variables-denvironnement)
  - [Configuration Spring](#configuration-spring)
- [Dépannage](#dépannage)
  - [Problèmes courants](#problèmes-courants)
  - [Mode débogage](#mode-débogage)
- [Étapes suivantes](#étapes-suivantes)
- [Ressources](#ressources)

## Prérequis

Avant d'exécuter cet exemple, assurez-vous d'avoir :

- Une ressource Azure AI Foundry avec un déploiement `gpt-5.6-luna` - provisionnez-le avec `azd up` ou manuellement via le [guide de configuration Azure AI Foundry](../../getting-started-azure-openai.md)
- Le rôle **Cognitive Services OpenAI User** attribué sur cette ressource (les templates Bicep s'en chargent pour vous)
- L’[Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), connecté avec `az login`
- Java 21+ et Maven 3.9+

> **Pas de clé API requise** — l’authentification est sans clé via Microsoft Entra ID.

## Démarrage rapide

```bash
# 1. Naviguer vers le projet
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Se connecter afin que l'authentification sans clé puisse obtenir un jeton
az login

# 3. Configurer le point de terminaison
#    - Si vous avez exécuté `azd up`, .env a été écrit pour vous (sautez cette étape).
#    - Sinon, copiez le modèle et définissez AZURE_OPENAI_ENDPOINT :
cp .env.example .env

# 4. Exécuter l'application
mvn spring-boot:run
```

## Comment fonctionne l'authentification

Cet exemple s’authentifie avec **Microsoft Entra ID** — il n’y a pas de clé API.

L’application configure explicitement l'authentification dans [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) :

1. `azureCredential()` crée un `BearerTokenCredential` en utilisant `AuthenticationUtil.getBearerTokenSupplier` avec `DefaultAzureCredential` et la portée `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` construit un `OpenAIClient` avec `OpenAIOkHttpClient.builder()`, résout le point de terminaison de la ressource à `/openai/v1`, et fournit le credential bearer avec `.credential(...)`.
3. `azureChatModel()` fournit ce client à `OpenAiChatModel` de Spring AI qui alimente le `ChatClient` dans l’exemple.

Ces beans explicites empêchent une clé globale `OPENAI_API_KEY` de remplacer l'authentification Azure. Ne pas fournir de clé API uniquement dans le YAML ne suffit pas pour configurer l'authentification. `DefaultAzureCredential` peut utiliser localement votre session `az login` ou une identité gérée dans Azure ; quelle que soit l'identité sélectionnée, elle doit disposer du rôle sur la ressource mentionné ci-dessus.

## Exécution de l'application

### Utilisation de Maven

```bash
mvn spring-boot:run
```

### Utilisation de VS Code

1. Ouvrez le projet dans VS Code
2. Appuyez sur `F5` ou utilisez le panneau "Exécuter et déboguer"
3. Sélectionnez la configuration "Spring Boot-BasicChatApplication"

> **Note** : L'application charge le fichier `.env` depuis son répertoire de travail, y compris lorsqu'elle est lancée depuis VS Code.

### Sortie attendue

Exemple de sortie après un démarrage réussi (les journaux de démarrage sont omis ; le libellé de la réponse peut varier) :

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Référence de configuration

### Variables d'environnement

| Variable | Description | Obligatoire | Exemple |
|----------|-------------|-------------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL du point de terminaison Foundry (Azure OpenAI) | Oui | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nom du déploiement du modèle de chat | Non | `gpt-5.6-luna` (par défaut) |

> Il n’y a **pas** de variable clé API — l’authentification est sans clé (Microsoft Entra ID via `az login`).

### Configuration Spring

Les réglages dans [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) utilisent le préfixe `spring.ai.openai` et des propriétés de chat aplaties (pas de bloc `options`) :

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` correspond au **nom du déploiement Azure**. L’authentification provient des beans explicites décrits ci-dessus, pas d’un paramètre `api-key`. L’exemple désactive le raisonnement et limite les jetons de complétion à 500 ; il laisse `temperature` et l’ancienne propriété `max-tokens` non définis.

Microsoft recommande le [SDK OpenAI officiel avec Azure OpenAI v1 et l’API Responses pour les nouvelles applications](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions reste supporté pour cet exemple basé sur les messages. Pour GPT-5.6, les requêtes incluant des outils via Chat Completions doivent définir `reasoning_effort` à `none` ; utilisez Responses pour combiner raisonnement et outils. Voir [appel d’outils avec modèles de raisonnement](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Dépannage

### Problèmes courants

<details>
<summary><strong>Erreur : 401 / "PermissionDenied" / erreurs de jeton</strong></summary>

- Exécutez `az login` — l’authentification sans clé nécessite une connexion active pour obtenir un jeton
- Vérifiez que votre compte a le rôle **Cognitive Services OpenAI User** sur la ressource
- Si vous venez d’attribuer ce rôle, attendez une minute pour sa propagation
- Confirmez que vous êtes dans le bon locataire/abonnement (`az account show`)
</details>

<details>
<summary><strong>Erreur : "The endpoint is not valid" / erreurs de connexion</strong></summary>

- Assurez-vous que `AZURE_OPENAI_ENDPOINT` est l'URL complète de base (ex. `https://your-resource.openai.azure.com/`)
- Vérifiez la cohérence du slash final
- Vérifiez que le point de terminaison correspond à votre ressource provisionnée (`azd env get-values`)
</details>

<details>
<summary><strong>Erreur : "The deployment was not found"</strong></summary>

- Vérifiez que `AZURE_OPENAI_DEPLOYMENT` correspond à un nom de déploiement dans Azure
- Vérifiez que le modèle est déployé avec succès et actif
- Le nom de déploiement par défaut est `gpt-5.6-luna`
</details>

<details>
<summary><strong>Erreur : 429 / limite de débit dépassée</strong></summary>

- Le déploiement GPT-5.6 Luna par défaut dispose d’une capacité Standard globale 10 : 10 requêtes par minute et 10 000 jetons par minute
- Exécutez les exemples séquentiellement et attendez l’intervalle de retry du service avant de réessayer
- Cet exemple basique désactive les retry automatiques du SDK, donc une requête échouée est rapportée directement
</details>

<details>
<summary><strong>VS Code : variables d’environnement non chargées</strong></summary>

- Assurez-vous que votre fichier `.env` est dans le répertoire racine du projet (au même niveau que `pom.xml`)
- Essayez d’exécuter `mvn spring-boot:run` dans le terminal intégré de VS Code
- Vérifiez que l’extension Java de VS Code est bien installée
</details>

### Mode débogage

Pour activer la journalisation détaillée, décommentez ces lignes dans [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) :

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Étapes suivantes

**Configuration terminée !** Continuez votre parcours d’apprentissage :

[Chapitre 3 : Techniques fondamentales de l’IA générative](../../../03-CoreGenerativeAITechniques/README.md)

## Ressources

- [Transition Spring AI 2 SDK OpenAI Java](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK OpenAI Java officiel avec Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Authentification sans clé avec Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portail Azure AI Foundry](https://ai.azure.com/)
- [Documentation Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou erreurs d'interprétation découlant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->