# Tutoriel sur les techniques principales de l'IA générative

## Table des matières

- [Prérequis](#prérequis)
- [Premiers pas](#premiers-pas)
- [Guide de sélection de modèle](#guide-de-sélection-de-modèle)
- [Tutoriel 1 : Complétions LLM et chat](#tutoriel-1-complétions-llm-et-chat)
- [Tutoriel 2 : Appel de fonctions](#tutoriel-2-appel-de-fonctions)
- [Tutoriel 3 : RAG (Génération augmentée par récupération)](#tutoriel-3-rag-génération-augmentée-par-récupération)
- [Tutoriel 4 : IA responsable](#tutoriel-4-ia-responsable)
- [Modèles communs à travers les exemples](#modèles-communs-à-travers-les-exemples)
- [Tests unitaires](#tests-unitaires)
- [Vérification séquentielle en direct](#vérification-séquentielle-en-direct)
- [Dépannage](#dépannage)
- [Étapes suivantes](#étapes-suivantes)

## Aperçu

Quatre programmes Java autonomes démontrent le chat, l'historique de conversation, l'appel de fonctions, la génération augmentée par récupération de documents entiers (RAG) et la gestion des réponses en IA responsable. Toutes les requêtes de chat ciblent par défaut **GPT-5.6 Luna avec un effort de raisonnement `none`**.

Ces exemples utilisent le SDK Java officiel d'OpenAI avec le point de terminaison v1 d'Azure OpenAI, suivant les [directives SDK de Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). L'ancien paquet `azure-ai-openai` n'est plus une dépendance. Chat Completions est conservé pour enseigner les flux de travail basés sur les messages existants ; voir le [SDK Java OpenAI](https://github.com/openai/openai-java#microsoft-azure) pour d'autres options d'API.

## Prérequis

- Java 21 ou supérieur et Maven 3.6.3 ou supérieur.
- Un déploiement Azure OpenAI pour le chat nommé `gpt-5.6-luna`, ou une substitution avec des paramètres compatibles de Chat Completions.
- Une identité Azure connectée avec le rôle **Utilisateur Cognitive Services OpenAI** sur la ressource. Le développement local utilise votre connexion Azure CLI ; les applications hébergées peuvent utiliser une identité gérée.
- Voir le [Chapitre 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) pour la configuration de la ressource et les instructions de connexion.

La [configuration Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fixe ces versions, vérifiées au 14/09/2026 :

| Composant | Version | Usage |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Client officiel compatible Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Authentification sans clé et actualisation des jetons |
| `net.objecthunter:exp4j` | 0.4.8 | Analyse d'expressions arithmétiques sans évaluation de code |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Tests unitaires Jupiter hors ligne |
| Compilateur / Surefire / Exec Maven | 3.16.0 / 3.6.0 / 3.6.4 | Compilation Java 21, tests, exemples exécutables |

Le compilateur utilise `--release 21`. Aucun besoin de dépendance Spring Boot, Spring AI ou LangChain4j pour ces exemples autonomes.

## Premiers pas

Depuis la racine du dépôt, définissez le point de terminaison de la ressource et l'option de déploiement dans votre shell.

**Windows PowerShell :**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS :**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Les tests ne nécessitent ni identifiants Azure ni point de terminaison. Maven ne lit pas automatiquement un fichier d'environnement ; définissez les variables dans le shell utilisé pour lancer les exemples en direct. Pour les lancements IDE, vérifiez l'environnement fourni par votre configuration de lancement.

## Guide de sélection de modèle

| Variable d'environnement | Signification | Par défaut |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Racine de ressource Azure HTTPS ou URL `/openai/v1` déjà normalisée | Obligatoire pour les exécutions en direct |
| `AZURE_OPENAI_DEPLOYMENT` | Nom de déploiement de chat, pas une version de modèle | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configuration de déploiement d'embed séparée, non utilisée par ces quatre programmes | `text-embedding-3-small` |

Les substitutions de déploiement vides utilisent les valeurs par défaut. La configuration ajoute `/openai/v1` exactement une fois et rejette les identifiants, chaînes de requête et chemins de déploiement hérités dans le point de terminaison.

Chaque requête chat définit explicitement `reasoningEffort(ReasoningEffort.NONE)` et `maxCompletionTokens(...)`. Aucune requête ne définit `temperature`, `top_p` ou l'option héritée de jetons de complétion. Cela inclut la sélection d'outil et les suivis de résultats d'outil. Les outils Chat Completions de GPT-5.6 exigent un effort de raisonnement `none` ; voir les [directives de chat de Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Il n'y a pas de point d'entrée pour le streaming ou les embeddings dans ce chapitre.** Le lecteur récupère son document entier, pas des vecteurs. Si vous l'étendez avec des embeddings, utilisez un déploiement d'embeddings séparé tel que `text-embedding-3-small`, jamais Luna.

## Tutoriel 1 : Complétions LLM et chat

Source : [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Le programme exécute une explication simple des streams Java, une conversation en deux tours HashMap/TreeMap, et un chat interactif. Le second tour inclut la première réponse de l'assistant ; chaque tour interactif envoie aussi sa conversation antérieure.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` fournit le déploiement et le réglage explicite du raisonnement. Le chat interactif ignore les lignes vierges, se termine sur `exit` ou EOF, et conserve le message système plus neuf tours complétés utilisateur/assistant. La limitation du nombre de tours est une borne pédagogique, pas une garantie stricte de budget de jetons.

Depuis le répertoire des exemples :

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Attendez-vous à trois réponses initiales, puis une invite `You:`. Chaque question interactive non vide ajoute une requête. Les limites de complétion sont de 200, 300, 400 puis 500 jetons par tour interactif.

## Tutoriel 2 : Appel de fonctions

Source : [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

Le SDK dérive des schémas JSON à partir des records annotés `WeatherArguments` et `CalculationArguments`. Le choix d'un outil obligatoire fait que chaque exemple utilise le protocole de l'outil au lieu d'accepter une réponse autonome du modèle.

1. Envoyez une question avec l'outil autorisé, effort de raisonnement `none`, et limite de 300 jetons de complétion.
2. Exigez une raison de fin `tool_calls`, validez le nom de fonction et les IDs d'appel, et analysez les arguments JSON typés.
3. Exécutez la fonction locale. Le modèle n’exécute pas de code Java ni arbitraire.
4. Ajoutez le message d’appel d’outil de l’assistant une fois, suivi de chaque résultat avec son `tool_call_id` correspondant.
5. Envoyez une dernière requête de 300 jetons sans outils et exigez une réponse complète et non vide.

`get_weather` retourne une météo **simulée**, pas en direct. Elle respecte la ville et convertit l'exemple de 22 degrés Celsius en Fahrenheit si demandé. `calculate` évalue l'expression fournie via exp4j, supporte des formes comme `15% of 240` et `2 + 3 * 4`, et rejette les calculs vides, surdimensionnés, invalides ou non finis. Il utilise l’arithmétique en virgule flottante, pas la précision décimale financière.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Attendez-vous à `Function: get_weather`, météo simulée à Seattle, `Function: calculate`, `Function result: 36`, et les deux réponses finales. Aucun stdin ni identifiants météo externes ne sont nécessaires. Une exécution réussie utilise exactement quatre requêtes chat.

## Tutoriel 3 : RAG (Génération augmentée par récupération)

Source : [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Entrée : [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Cet exemple introductif de RAG récupère un document UTF-8 complet et l'inclut dans le message utilisateur avec la question. Un message système séparé demande au modèle de considérer le contenu du document comme non fiable et de répondre uniquement à partir de ce contexte. Si le document ne contient pas la réponse, la réponse demandée est : `Je ne peux pas trouver cette information dans le document fourni.`

Le grounding peut réduire les hallucinations, mais ni les délimiteurs ni les instructions système ne garantissent la précision ni ne préviennent toute injection de prompt. Vérifiez les réponses en direct. En production, le RAG ajoute normalement fragmentation, recherche, citations, contrôle d'accès et évaluation.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Entrez une question, par exemple `Quelle méthode d'authentification le document décrit-il ?`. Attendez une réponse mentionnant Microsoft Entra ID. Le programme se termine après une requête chat avec une limite de 500 jetons.

La recherche de fichier par défaut fonctionne depuis la racine du dépôt, le répertoire du chapitre ou le répertoire des exemples. Un chemin explicite est aussi supporté :

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Les entrées doivent être non vides : au plus 32 KiB de données UTF-8 du document et 2 000 caractères de question. Les fichiers manquants, questions vides/EOF et entrées surdimensionnées échouent avant l’inférence.

## Tutoriel 4 : IA responsable

Source : [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Les six sondes couvrent les instructions nuisibles, discours de haine, vie privée, désinformation médicale, contenu illégal, et une question bénigne d’IA responsable. Le programme observe la réponse plutôt que de supposer qu’une sonde déclenche toujours un filtre.

| Résultat | Preuve |
| --- | --- |
| `FILTERED` | Un code d’erreur explicite `content_filter` / `ResponsibleAIPolicyViolation`, ou une raison de fin `content_filter` de complétion |
| `REFUSED` | Un champ structuré `message.refusal` non vide |
| `POSSIBLE_REFUSAL` | Une phrase de refus en texte ordinaire ; une heuristique nécessitant examen |
| `GENERATED` | Une réponse terminée non vide ; pas une preuve que le contenu soit sûr |

Un HTTP 400 ordinaire n’est **pas** une preuve de filtrage. Paramètres invalides, échecs d’authentification, limites de débit, erreurs serveur, réponses mal formées et sortie tronquée échouent l’exécution plutôt que de produire un succès de sécurité faux. Des mots larges comme « contenu nuisible » dans une explication bénigne ne comptent pas comme refus.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Attendez six résultats de catégorie et un résumé indiquant que les observations ne sont pas une certification de sécurité. Chaque sonde a une limite de 300 jetons de complétion. Examinez manuellement les générations inattendues et refus possibles ; la comparaison bénigne devrait produire une explication substantielle d’IA responsable. Aucun stdin n’est requis.

## Modèles communs à travers les exemples

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralise la normalisation des points de terminaison, les substitutions de déploiement, l’authentification sans clé, et les options de chat :

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Le fournisseur de jetons actualise les tokens d’accès selon les besoins. Ne pas consigner les jetons ni remplacer par une clé API. Chaque programme réutilise son client et le ferme dans `finally` ou via son propre wrapper `AutoCloseable` ; le `OpenAIClient` du SDK n’est pas lui-même `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) exige une réponse textuelle complète et non vide. Les choix vides, refus, filtres et réponses tronquées ne sont pas affichés silencieusement comme succès. L’exemple d’IA responsable gère explicitement les résultats attendus de filtre/refus. Les échecs non gérés donnent un code de sortie non nul au processus Java/Maven.

**Les tentatives automatiques du SDK sont désactivées** pour garder le nombre de requêtes prévisible sur des déploiements partagés à faible RPM. Chaque requête d’inférence a un timeout de 60 secondes. L’obtention de jetons peut prendre plus de temps. La planification à niveau application doit respecter les quotas ; ne relancez pas aveuglément une requête payante échouée.

## Tests unitaires

Depuis le répertoire des exemples :

```powershell
mvn -B -ntp clean test
```

Le transport de test remplace entièrement la couche HTTP du SDK, capture le corps réel des requêtes sérialisées, et fournit les réponses en file d’attente. Il n’ouvre aucun socket, n’acquiert aucun jeton Azure et échoue sur des requêtes inattendues. Ces tests valident le comportement applicatif et le protocole du SDK, pas la qualité du modèle en direct ni la disponibilité du déploiement.

| Suite de tests | Couverture |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalisation/rejet de point de terminaison, substitutions de déploiement, options de raisonnement et de jetons |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Tous flux de complétion, historique des messages, réduction des tours complets, EOF, échecs |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schémas d’outil, arguments typés, arithmétique, IDs, multiples résultats d’outil, suivis échoués |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Recherche de fichier, UTF-8, limites de taille, charge de grounding, erreurs d’entrée et d’API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Les six sondes, filtres explicites, classification refus, erreurs 400 ordinaires et autres échecs |

Pour une suite, utilisez `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Les fixtures partagées vivent dans [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Vérification séquentielle en direct

Les appels en direct sont séparés des tests unitaires. Utilisez les commandes suivantes **individuellement**, depuis la racine du dépôt, seulement une fois que les identifiants et accès au déploiement sont prêts. Aucun service ni processus persistant n’est nécessaire.

Pour un déploiement partagé à **10 requêtes/minute**, réservez assez de quota pour le programme entier avant de le lancer : 5, 4, 1, puis 6 requêtes. Les processus séquentiels seuls ne garantissent pas le respect de la limite de débit. Coordonnez la minute roulante avec tous les autres appelants ; ne collez pas les quatre invocations en lots sans rythme.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Complétions, multi-tours, et deux tours interactifs :**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Vérifiez les titres des trois sections, cinq réponses, une réponse finale interactive rappelant Ada, `Au revoir !`, et le code de sortie 0. Budget : **5 requêtes, au maximum 1 900 jetons de complétion**. Pour une exécution plus courte, pipez uniquement `exit` : 3 requêtes / 900 jetons, mais cela ne teste pas l'inférence interactive.

**2. Les deux workflows d'appel de fonction :**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Vérifiez les deux noms de fonction, la météo simulée de Seattle, le résultat calculé 36, deux réponses finales, et le code de sortie 0. Budget : **4 requêtes, au maximum 1 200 jetons de complétion**.

**3. Réponse fondée sur un document :**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Vérifiez le chemin du document, une réponse mentionnant Microsoft Entra ID, et le code de sortie 0. Budget : **1 requête, au maximum 500 jetons de complétion**. Le fichier d'entrée requis unique est l'existant [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt). Une seconde exécution optionnelle posant une question sur un sujet absent doit s'abstenir et ajoute une requête / 500 jetons.

**4. Observations relatives à l'IA responsable :**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Vérifiez six catégories et le résumé des observations, passez en revue le contenu généré, et exigez le code de sortie 0 pour la complétion technique. Une sortie de processus réussie ne garantit pas la sécurité du modèle. Budget : **6 requêtes, au maximum 1 800 jetons de complétion**.

**Total pour les quatre commandes : 16 requêtes de chat et au maximum 5 400 jetons de complétion**, plus les jetons d'entrée (incluant la conversation répétée et le schéma/historique des outils). Il n’y a aucune requête d’intégration vectorielle. L’usage effectif des jetons dépend du modèle et peut être inférieur, en particulier pour les invites filtrées. Le coût en dollars dépend du tarif de déploiement ; aucune estimation monétaire fixe n’est implicite. Toutes les limites de requêtes supposent aucune réexécution manuelle. Inspectez `$LASTEXITCODE` immédiatement après chaque commande ; une valeur non nulle signifie que l’exécution n’a pas réussi.

## Dépannage

- **Endpoint manquant / 401 / 403 :** Définissez l’endpoint dans le processus de lancement, vérifiez votre connexion Azure locale et le rôle à périmètre de ressource, et vérifiez l’absence d’écrasements indésirables des identités dans l’environnement.
- **400 / 404 :** Confirmez que le déploiement existe et supporte Chat Completions avec l’effort de raisonnement `none`. Utilisez la racine HTTPS de la ressource ou l’URL `/openai/v1`, pas une URL de déploiement legacy. Les erreurs 400 ordinaires sont des échecs techniques, pas des barrières de sécurité.
- **429 :** Coordonnez le RPM partagé et le quota de jetons avant de réessayer. Les exemples ne font délibérément pas de réessaie automatique.
- **`Réponse de chat incomplète : longueur` :** La sortie a atteint la limite de complétion. Passez en revue la réponse et l’invite avant d’augmenter la limite et son budget documenté ; ne consignez pas une exécution tronquée comme réussie.
- **Erreurs de fichier ou stdin :** Lancez à partir d’un répertoire supporté ou fournissez un chemin de document explicite. Fournissez une question non vide pour la lecture. Les complétions peuvent normalement se terminer à EOF ou sur `exit`.
- **Erreurs de compilation :** Vérifiez Java 21 ou plus récent, puis exécutez `mvn -B -ntp clean test`. Dans PowerShell, citez tout l’argument Maven contenant une propriété avec un point, par exemple `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Étapes suivantes

Continuez vers [Chapitre 4 : Exemples Pratiques](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou erreurs d'interprétation découlant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->