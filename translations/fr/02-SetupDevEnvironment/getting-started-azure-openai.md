# Configuration de l'environnement de développement pour Azure AI Foundry

> Ce guide configure les modèles **Azure AI Foundry** pour les applications Java AI de ce cours, en utilisant une authentification **sans clé** (Microsoft Entra ID) — pas de clés API à gérer. Nouveau avec les outils ? Commencez par le [guide de l'environnement de développement](./README.md).

Ce guide configure les modèles **Azure AI Foundry** pour les applications Java AI de ce cours. Vous avez deux options :

- **Option A — Provision avec `azd` + Bicep (recommandé) :** une seule commande déploie le compte Foundry et les modèles en mode code. Aucun clic dans le portail.
- **Option B — Créer les ressources manuellement** dans le portail Azure AI Foundry.

Les deux options utilisent une **authentification sans clé** (Microsoft Entra ID) — il n’y a pas de clés API à copier ou exposer.

## Table des matières

- [Ce qui est créé](#ce-qui-est-créé)
- [Prérequis](#prérequis)
- [Option A : Provision avec azd + Bicep (recommandé)](#option-a-provision-with-azd--bicep-recommended)
- [Option B : Création manuelle des ressources](#option-b-création-manuelle-des-ressources)
- [Configurer votre environnement](#configurer-votre-environnement)
- [Tester votre configuration](#tester-votre-configuration)
- [Et ensuite ?](#et-ensuite)
- [Ressources](#ressources)
- [Ressources supplémentaires](#ressources-supplémentaires)

## Ce qui est créé

Les templates Bicep dans [`infra/`](../../../02-SetupDevEnvironment/infra) provisionnent :

- Un compte **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, type `AIServices`) avec un projet
- Un déploiement **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), version `2026-07-09`, avec une capacité `GlobalStandard` de `10` (10 requêtes/minute et 10 000 tokens/minute pour ce modèle)
- Un déploiement **embedding** - `text-embedding-3-small`, version `1` (utilisé dans les chapitres suivants)
- Une **attribution de rôle sans clé** (`Cognitive Services OpenAI User`) pour vous connecter avec `az login` au lieu de gérer des clés

## Prérequis

- Un [abonnement Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) et [Maven 3.9+](https://maven.apache.org/download.cgi)

## Option A : Provision avec azd + Bicep (recommandé)

Depuis le dossier `02-SetupDevEnvironment` :

```bash
cd 02-SetupDevEnvironment

# Se connecter (aux deux outils)
azd auth login
az login

# Provisionner le compte Foundry + les déploiements de modèles
azd up
```

`azd` vous demande un **nom d’environnement** (par exemple `genai-java`), un **abonnement** et une **région**. Choisissez votre abonnement et une région où `gpt-5.6-luna` et `text-embedding-3-small` sont disponibles, par exemple `eastus2`. Vérifiez que l’abonnement a le quota suffisant pour le modèle et le type de déploiement dans cette région ; la disponibilité et les quotas varient selon l’abonnement.

Quand le provisioning est terminé, azd :

1. Déploie tout ce qui est défini dans [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Exécute un hook post-provisionnement qui écrit [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) avec votre endpoint et les noms de déploiement (aucun secret).

> **Astuce :** relancez `azd up` à tout moment pour appliquer des modifications. Exécutez `azd down` pour tout supprimer et arrêter les coûts.

Pour voir les paramètres générés :

```bash
azd env get-values
```

Passez maintenant à [Tester votre configuration](#tester-votre-configuration).

## Option B : Création manuelle des ressources

Vous préférez le portail ? Créez les ressources manuellement :

1. Allez sur le [portail Azure AI Foundry](https://ai.azure.com/) et connectez-vous.
2. **Créez un projet** (cela crée aussi une ressource AI Foundry). Donnez-lui un nom comme `GenAIJava`.
3. Dans votre projet, ouvrez **Modèles + points de terminaison** → **Déployer un modèle** → **Déployer un modèle de base**.
4. Déployez **GPT-5.6 Luna** (nom modèle et déploiement `gpt-5.6-luna`, version `2026-07-09`) avec la capacité **Global Standard** `10`. Répétez pour **text-embedding-3-small**, version `1`, si vous souhaitez les exemples d’embeddings.
5. Depuis **Vue d’ensemble**, copiez le **point de terminaison** (par exemple `https://<resource>.openai.azure.com/`).
6. Accordez-vous un accès sans clé : sur la ressource, ouvrez **Contrôle d’accès (IAM)** → **Ajouter une attribution de rôle** → attribuez **Cognitive Services OpenAI User** à votre compte.

> **Vous avez toujours des problèmes ?** Consultez la [documentation Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configurer votre environnement

**Si vous avez utilisé l’option A (`azd up`)**, votre fichier de configuration est déjà écrit — rien à configurer. Passez à [Tester votre configuration](#tester-votre-configuration).

**Si vous avez utilisé l’option B (manuelle)**, créez vous-même le fichier `.env` de l’exemple :

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Modifiez `.env` avec votre endpoint (pas de clé — l’authentification est sans clé) :

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Utilisez le point de terminaison Azure OpenAI de la ressource, pas l’URL du projet. L’application basic-chat le résout vers `/openai/v1` et configure un client bearer-token explicite ; aucune clé API n’est requise.

> **Note de sécurité :** Il n’y a pas de clé API à stocker. Vous vous authentifiez avec Microsoft Entra ID via `az login` (localement) ou une identité managée (dans Azure). Le fichier `.env` contient uniquement des paramètres non secrets et est déjà couvert par `.gitignore`.

## Tester votre configuration

Assurez-vous d’être connecté afin que l’authentification sans clé puisse obtenir un jeton, puis exécutez l’exemple :

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # si vous n'êtes pas déjà connecté
mvn clean spring-boot:run
```

Vous devriez voir une réponse du modèle `gpt-5.6-luna`. Exécutez les exemples séquentiellement pour rester dans le quota par défaut réduit ; si vous obtenez une erreur HTTP 429, attendez l’intervalle de nouvelle tentative avant de réessayer.

> **Utilisateurs VS Code :** appuyez sur `F5` pour lancer. L’application charge automatiquement votre `.env`.

> **Exemple complet :** Consultez l'[exemple Basic Chat avec Azure AI Foundry](./examples/basic-chat-azure/README.md) pour les détails et dépannage.

## Et ensuite ?

Après le provisionnement et l’exécution réussie de l’exemple, vous aurez :
- Azure AI Foundry avec `gpt-5.6-luna` et `text-embedding-3-small` déployés
- Authentification sans clé (Microsoft Entra ID) — pas de clés à gérer
- Un `.env` local avec votre endpoint et noms de déploiement
- Un environnement de développement Java prêt à l’emploi

**Continuez vers** [Chapitre 3 : Techniques de base d’IA générative](../03-CoreGenerativeAITechniques/README.md) pour commencer à créer des applications IA !

## Ressources

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Authentification sans clé avec Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Documentation Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Transition Spring AI 2 vers OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java officiel OpenAI avec Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Ressources supplémentaires

- [Télécharger VS Code](https://code.visualstudio.com/Download)
- [Obtenir Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Configuration du conteneur de développement](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou erreurs d'interprétation découlant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->