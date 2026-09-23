# មេរៀនបច្ចេកទេស AI ជីវិតបង្កើតស្នូល

## តារាងមាតិកា

- [តម្រូវការជាមុន](#តម្រូវការជាមុន)
- [ការចាប់ផ្តើម](#ការចាប់ផ្តើម)
- [មគ្គុទេសក៍ជ្រើសរើសម៉ូដែល](#មគ្គុទេសក៍ជ្រើសរើសម៉ូដែល)
- [មេរៀនទី១៖ ការបញ្ចប់ និងការជជែក LLM](#មេរៀនទី១៖-ការបញ្ចប់-និងការជជែក-llm)
- [មេរៀនទី២៖ ការហៅមុខងារ](#មេរៀនទី២៖-ការហៅមុខងារ)
- [មេរៀនទី៣៖ RAG (ការបង្កើតបន្ថែមដោយការស្តារឡើងវិញ)](#មេរៀនទី៣៖-rag-ការបង្កើតបន្ថែមដោយការស្តារឡើងវិញ)
- [មេរៀនទី៤៖ AI ដែលមានការទទួលខុសត្រូវ](#មេរៀនទី៤៖-ai-ដែលមានការទទួលខុសត្រូវ)
- [លំនាំទូទៅជុំវិញឧទាហរណ៍](#លំនាំទូទៅជុំវិញឧទាហរណ៍)
- [ការធ្វើតេស្តផ្នែក](#ការធ្វើតេស្តផ្នែក)
- [ការត្រួតពិនិត្យបន្តបន្ទាប់ផ្ទាល់](#ការត្រួតពិនិត្យបន្តបន្ទាប់ផ្ទាល់)
- [ដោះស្រាយបញ្ហា](#ការដោះស្រាយបញ្ហា)
- [ជំហានបន្ទាប់](#ជំហានបន្ទាប់)

## សេចក្ដីសង្ខេប

កម្មវិធី Java ដាច់ដោយឡែកបួននេះបង្ហាញពីការជជែក, ប្រវត្តិការសន្ទនា, ការហៅមុខងារ, ការ​បង្កើត​បន្ថែម​បែប​ការ​ស្ទង់យក​ឯកសារ​ទាំងមូល (RAG) និងការគ្រប់គ្រងចម្លើយ AI ដែលមានការទទួលខុសត្រូវ។ ការស្នើរសុំជជែកទាំងអស់មានគោលដៅទៅកាន់ **GPT-5.6 Luna ដោយប្រែប្រួលការគិត `none` ដោយលំនាំដើម**។

ឧទាហរណ៍ទាំងនេះប្រើ OpenAI Java SDK ផ្លូវការជាមួយចុងផ្លូវ Azure OpenAI's v1 ដោយគោលការណ៍ [Microsoft's SDK guidance](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages)។ កញ្ចប់ចាស់ `azure-ai-openai` មិនមែនជាអាស្រ័យភាពទៀតទេ។ Chat Completions ត្រូវរក្សាទុកដើម្បីបង្រៀនក្រុមការងារផ្អែកលើសារដែលមានស្រាប់; សូមមើល [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) សម្រាប់ជម្រើស API ផ្សេងទៀត។

## តម្រូវការជាមុន

- Java 21 ឬក្រោមហើយ Maven 3.6.3 ឬក្រោម។
- ការបញ្ជូន Azure OpenAI chat ឈ្មោះ `gpt-5.6-luna` ឬការជំនួសជាមួយការកំណត់ Chat Completions ដែលសមស្រប។
- អត្តសញ្ញាណ Azure ដែលបានចុះឈ្មោះជាមួយតួនាទី **Cognitive Services OpenAI User** នៅលើធនធាន។ ការអភិវឌ្ឍផ្ទាល់ប្រើការចុះឈ្មោះ Azure CLI របស់អ្នក; កម្មវិធីរត់លើម៉ាស៊ីនបម្រើអាចប្រើអត្តសញ្ញាណគ្រប់គ្រង។
- សូមមើល [ជំពូក 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) សម្រាប់ការដំឡើងធនធាន និងការណែនាំចុះឈ្មោះ។

ការកំណត់ Maven](examples/pom.xml) បានកំណត់ជាប់កំណែទាំងនេះ ដែលបានត្រួតពិនិត្យនៅថ្ងៃទី 2026-09-14:

| ធាតុ | កំណែ | គោលបំណង |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | អ្នកអតិថិជនផ្លូវការសមស្រប Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | ការផ្ទៀងផ្ទាត់គ្មានកូនសោ និងបច្ចុប្បន្នភាពសញ្ញាប័ត្រ |
| `net.objecthunter:exp4j` | 0.4.8 | ការវិភាគបញ្ចាក់លេខគណិតវិទ្យា ដោយគ្មានការប៉ាន់ប្រមាណកូដ |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | ការធ្វើតេស្ត Jupiter ផ្ទាល់មិនត្រូវការអ៊ីនធើណិត |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | ការបំលែង Java 21, ការធ្វើតេស្ត, ឧទាហរណ៍ដែលអាចបញ្ជា |

អ្នកបំលែងកូដប្រើ `--release 21`។ មិនត្រូវការឲ្យមាន Spring Boot, Spring AI, ឬ LangChain4j នៅក្នុងឧទាហរណ៍ដាច់ដោយឡែកទាំងនេះទេ។

## ការចាប់ផ្តើម

ចាប់ផ្តើមពីឫស្សទRepositories ដាក់ចង្អុលទីតាំងធនធាននិងការជំនួស deployment មួយក្នុង shell របស់អ្នក។

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

ការធ្វើតេស្តមិនត្រូវការសមត្ថភាព Azure ឬចុងផ្លូវ។ Maven មិនអានបណ្ណាល័យបរិយាកាសដោយស្វ័យប្រវត្តិ; ចំណូលអថេរ ក្នុង shell ដែលបានប្រើដើម្បីចាប់ផ្តើមឧទាហរណ៍ផ្ទាល់។ សម្រាប់ការចាប់ផ្តើមក្នុង IDE សូមបញ្ជាក់បរិយាកាសដែលផ្គត់ផ្គង់ដោយការរៀបចំធ្វើចាប់ផ្តើមរបស់អ្នក។

## មគ្គុទេសក៍ជ្រើសរើសម៉ូដែល

| អថេរបរិយាកាស | មានន័យ | លំនាំដើម |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | មូលដ្ឋានធនធាន HTTPS Azure ឬ URL `/openai/v1` ដែលបានធ្វើស្តង់ដាររួចហើយ | តម្រូវការសម្រាប់រត់ផ្ទាល់ |
| `AZURE_OPENAI_DEPLOYMENT` | ឈ្មោះការបញ្ជូន chat មិនមែនជាកំណែម៉ូដែល | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | ការកំណត់ embedding ដាច់ៗដែលកម្មវិធីទាំងបួននេះមិនប្រើ | `text-embedding-3-small` |

ការជំនួស deployment ទំនេរប្រើលំនាំដើម។ ការកំណត់បញ្ចូល `/openai/v1` ម្តងតែមួយ និងបដិសេធសមត្ថភាព, សំណួរ query និងផ្លូវ deployment ហោចណាស់នៅក្នុងចុងផ្លូវ។

ការស្នើរសុំជជែកគ្រប់គ្រាន់បានកំណត់ `reasoningEffort(ReasoningEffort.NONE)` និង `maxCompletionTokens(...)` ជាក់លាក់។ គ្មានស្នើរសុំណាមួយកំណត់ `temperature`, `top_p` ឬជម្រើស legacy លើ token completion ទេ។ នេះរួមបញ្ចូលការជ្រើសរើសឧបករណ៍ និងការតាមដានលទ្ធផល។ ឧបករណ៍ GPT-5.6 Chat Completions ត្រូវការការយកចិត្តទុកដាក់ reasoning effort `none`; សូមមើល [Microsoft's chat guidance](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)។

**មិនមានច្រកចូលស្ទ្រីម ឬ embedding នៅជំពូកនេះទេ។** អ្នកអានយកឯកសារទាំងមូល អត់មែនវ៉ិចទ័រទេ។ ប្រសិនបើអ្នកបន្ថែម embedding សូមប្រើ deployment embedding ដាច់ពីគ្នាដូចជា `text-embedding-3-small` មិនដែល Luna ទេ។

## មេរៀនទី១៖ ការបញ្ចប់ និងការជជែក LLM

ប្រភព៖ [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)។

កម្មវិធីនេះរត់ការពន្យល់ Java streams សាមញ្ញ, ការសន្ទនាគូ HashMap/TreeMap ពីរជុំ និងជជែកផ្ទាល់ខ្លួន។ ជុំទីពីររួមបញ្ចូលចម្លើយជំនួយការដំបូង; ជុំផ្ទាល់ខ្លួននីមួយៗផ្ញើប្រវត្តិការសន្ទនាមុនរបស់វាដែរ។

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` ផ្គត់ផ្គង់ការបញ្ជូន និងកំណត់ reasoning ជាក់លាក់។ ជជែកផ្ទាល់ខ្លួន ធ្វើអោយបាត់បង់ខ្សែបន្ទាត់ទេ ហើយបញ្ចប់នៅពេល `exit` ឬ EOF និងរក្សាសារប្រព័ន្ធបូកជាមួយកំណត់ត្រានៃប្រញាប់ចម្លើយអ្នកប្រើ និងជំនួយការជាច្រើនបានបញ្ចប់ទាំងអស់។ ការកាត់បន្ថយចំនួនជុំគឺជាការកំណត់សម្រាប់ការសិក្សា មិនមែនធានាគម្រូតូតូខ្មែរ token-budget ទេ។

ពីថតឧទាហរណ៍:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

រំពឹងចម្លើយដំបូងបីចម្លើយ បន្ទាប់មកសារបង្ហាញ `You:`។ សំណួរជជែកមិនទាន់បញ្ចប់នីមួយៗបន្ថែមការស្នើរសុំមួយ។ កំណត់កំណត់បញ្ចប់គឺ 200, 300, 400, បន្ទាប់មក 500 token គ្រប់ជុំជជែក។

## មេរៀនទី២៖ ការហៅមុខងារ

ប្រភព៖ [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)។

SDK ដកស្រង់ schema JSON ពីកំណត់ត្រា `WeatherArguments` និង `CalculationArguments` ដែលបានធ្វើសម្គាល់។ ការជ្រើសរើសឧបករណ៍ត្រូវបានទាមទារបង្កើតឱ្យឧទាហរណ៍ដោយប្រើពិធីការឧបករណ៍ ជំនួសចម្លើយមូលដ្ឋានដែលមិនមានជំនួយ។

1. ផ្ញើសំណួរជាមួយឧបករណ៍ដែលបានអនុញ្ញាត, reasoning effort `none`, និងកំណត់ដែនកំណត់បញ្ចប់ 300 token។
2. ត្រូវការហេតុផល `tool_calls` សម្រេច, ផ្ទៀងផ្ទាត់ឈ្មោះមុខងារ និង ID ហៅ, និងបម្រែបម្រួល arguments JSON typed។
3. ប្រតិបត្តិមុខងារផ្ទាល់តំបន់។ ម៉ូដែលមិនអនុវត្តន៍ Java ឬកូដគ្មានការកំណត់ទេ។
4. បន្ថែមសារហៅឧបករណ៍ជំនួយតែមួយ ដោយបន្ទាប់មកលទ្ធផលនីមួយៗជាមួយ `tool_call_id` ត្រូវគ្នា។
5. ផ្ញើសំណើចុងក្រោយមួយ 300 token ដោយគ្មានឧបករណ៍ និងត្រូវការ​ចម្លើយដែលបានបញ្ចប់ មិនទទេ។

`get_weather` ត្រឡប់ចេញជា​​​ពត៌មានអាកាសធាតុ **ដែលបានចម្លង**, មិនមែនផ្ទាល់។ វាគោរពទីក្រុង និងបំលែង 22 អង្សាសេលស្យუსទៅអង់តុល្យហ្វារេនកខណៈត្រូវបានស្នើ។ `calculate` វាយតម្លៃប្រៀបធៀបដែលបានផ្តល់តាម exp4j, គាំទ្ររូបមន្តដូចជា `15% នៃ 240` និង `2 + 3 * 4`, និងបដិសេធការគណនារឹងទទេ, ធំប្រេ, មិនត្រឹមត្រូវ ឬមិនដាច់ដោយឡែក។ វាប្រើគណិតវិទ្យាតំណក់ទឹក មិនមែនជាការត្រឹមត្រូវលេខសុទ្ធនៃហិរញ្ញវត្ថុទេ។

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

រំពឹង `Function: get_weather`, ពត៌មានអាកាសធាតុ Seattle ដែលបានចម្លង, `Function: calculate`, `Function result: 36`, និងចម្លើយចុងបី។ មិនតម្រូវ stdio ឬសមត្ថភាពអាកាសធាតុខាងក្រៅ។ ការរត់ជោគជ័យប្រើការស្នើសុំជជែកជាចំណុចបួនពេញលេញ។

## មេរៀនទី៣៖ RAG (ការបង្កើតបន្ថែមដោយការស្តារឡើងវិញ)

ប្រភព៖ [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)។ ទិន្នន័យបញ្ចូល៖ [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)។

ឧទាហរណ៍ RAG ទីបញ្ចូលនេះយកឯកសារ UTF-8 មួយទាំងមូល ហើយបញ្ចូលវាទៅក្នុងសារអ្នកប្រើជាមួយសំណួរ។ សារប្រព័ន្ធដាច់ពីគ្នារួចណែនាំម៉ូដែលឲ្យយកមាតិកាឯកសារជាទិន្នន័យមិនប្រាកដ ហើយឆ្លើយតែពីបរិបទនោះតែប៉ុណ្ណោះ។ ប្រសិនបើឯកសារមិនមានចម្លើយដែលស្នើ សំណូមបទទួលបានគឺ៖ `ខ្ញុំមិនអាចរកឃើញ ព័តមាននោះនៅក្នុងឯកសារដែលបានផ្តល់កាន់ខ្ញុំទេ។`

ការបញ្ជាក់មូលដ្ឋានអាចកាត់បន្ថយការស្រមោល, ប៉ុន្តែមិនមាន delimiter ឬសេចក្តីណែនាំប្រព័ន្ធណាមួយធានាបានពីភាពត្រឹមត្រូវ ឬការការពារសំណើរ prompt injection ទាំងអស់ឡើយ។ សូមពិនិត្យចម្លើយផ្ទាល់។ RAG ផលិតក្នុងវិស័យធម្មតាគឺបន្ថែមការចែកធ្លាយ, ការស្រាវជ្រាវ, ឯកសារយោង, ការត្រួតពិនិត្យចូល និងការវាយតម្លៃ។

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

បញ្ចូលសំណួរមួយ ឧទាហរណ៍ `របៀបផ្ទៀងផ្ទាត់អត្តសញ្ញាណណាដែលឯកសារបានពណ៌នា?`។ រំពឹងចម្លើយ​ដែលមានការបញ្ជាក់ Microsoft Entra ID។ កម្មវិធីចាកចេញបន្ទាប់ពីស្នើរសុំជជែកមួយម្តងដែលមានដែនកំណត់ 500 token។

ការស្វែងរកឯកសារលំនាំដើមដំណើរការពីឫស្សៈហ្គារី root, ថតជំពូក ឬថតឧទាហរណ៍។ ផ្លូវថតបញ្ជាក់ជាក់លាក់ក៏គាំទ្រ:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

បញ្ចូលត្រូវមិនទទេ: អតិបរិមាណ 32 KiB នៃទិន្នន័យឯកសារ UTF-8 និង 2,000 តួអក្សរសំណួរ។ ឯកសារមិនមាន, សំណួរទទេ/ចប់ EOF, និងបញ្ចូលធំលើសត្រូវបរាជ័យមុនការព្យាករណ៍។

## មេរៀនទី៤៖ AI ដែលមានការទទួលខុសត្រូវ

ប្រភព៖ [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)។

សោភ័ណភាពប្រាំមួយគ្របដណ្តប់ការណែនាំគំរាម, ពាក្យជនយូរ, គូលំអន់, ព័ត៌មានវេជ្ជសាស្រ្តមិនត្រឹមត្រូវ, មាតិកាស្របច្បាប់, និងសំណួរ AI មានការទទួលខុសត្រូវម្យ៉ាងល្អ។ កម្មវិធីសង្កេតការឆ្លើយតបដោយមិនសន្មត់ថាសោភ័ណភាពរាល់ករណីត្រូវសកម្មរហ័ស។

| លទ្ធផល | ភស្ដុតាង |
| --- | --- |
| `FILTERED` | កូដកំហុស `content_filter` / `ResponsibleAIPolicyViolation` ឬហេតុផលបញ្ចប់ `content_filter` |
| `REFUSED` | វាល `message.refusal` ដែលមានរចនាសម្ព័ន្ធមិនទទេ |
| `POSSIBLE_REFUSAL` | ប្រយោគបដិសេធដំបូង ដែលមានក្នុងអត្ថបទធម្មតា; គន្លឹះតម្រូវការត្រួតពិនិត្យ |
| `GENERATED` | ចម្លើយបញ្ចប់មិនទទេ; មិនមែនភស្តុតាងថាមាតិកាគឺសុវត្ថិភាពទេ |

HTTP 400 ធម្មតាមិនមែនជា ភស្ដុតាងនៃការត្រួតពិនិត្យទេ។ ពីរបៀបទ្រូបកំហុស ការផ្ទៀងផ្ទាត់បរាជ័យ, ការកំណត់កំណត់ពីកម្រិត, កំហុសម៉ាស៊ីនមេ, ចម្លើយមិនបញ្ចេញបិទលំអាទេ ធ្វើឲ្យរត់បរាជ័យ ដោយមិនបង្កើតជោគជ័យសុវត្ថិភាពស្មានមិនបាន។ ពាក្យទូលំទូលាយដូចជា "មាតិកាអាក្រក់" ក្នុងការពន្យល់ល្អមិនត្រូវបានណែនាំជាបដិសេធ។

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

រំពឹងលទ្ធផលប្រាំមួយថ្នាក់ និងសេចក្តី​សង្ខេបបញ្ជាក់ថាការសង្កេតមិនមែនជាសញ្ញាប័ត្រសុវត្ថិភាព។ សោភ័ណភាពមួយៗមានដែនកំណត់បញ្ចប់ 300 token។ សូមពិនិត្យការបង្កើតមិនរំពឹងទុក និងបដិសេធអាចកើតមានដោយដៃគូ; ការប្រៀបធៀបសោភ័ណភាពគួរតែលើកឡើងការពន្យល់ AI ដែលមានភាពទទួលខុសត្រូវ។ មិនតម្រូវ stdio ទេ។

## លំនាំទូទៅជុំវិញឧទាហរណ៍

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) ប្រមូលផ្តុំការធ្វើស្តង់ដារចុងផ្លូវ, ការជំនួស deployment, ការផ្ទៀងផ្ទាត់គ្មានសោ, និងជម្រើសជជែក:

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

អ្នកផ្តល់សញ្ញាប័ត្រមានការរត់បន្តបន្ទាប់ដល់ការយកសញ្ញាប័ត្រផ្ទាល់ខ្លួនតាមតម្រូវការ។ កុំកត់ត្រាសញ្ញាប័ត្រ ឬជំនួសវាមួយជាមួយកូនសោ API។ កម្មវិធីនីមួយៗប្រើ client របស់ខ្លួនឡើងវិញ ហើយបិទវា តាមរយៈ `finally` ឬរបាំង `AutoCloseable` ផ្ទាល់ខ្លួន; SDK `OpenAIClient` មិនមែនជា `AutoCloseable` ទេ។

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) តម្រូវឲ្យមានចម្លើយបញ្ចប់ មិនទទេ។ ជម្រើសទទេ, បដិសេធ, ការត្រួតពិនិត្យ, និងចម្លើយត្រូវបានកាត់បន្ថយ មិនត្រូវបានបោះពុម្ភជោគជ័យដោយស្ងាត់។ ឧទាហរណ៍ AI ដែលមានការទទួលខុសត្រូវ ដំណើរការវិលត្រឡប់បដិសេធ និងត្រួតពិនិត្យយ៉ាងច្បាស់។ ការបរាជ័យមិនទទួលបាន ដំណើរការ Java/Maven មានកូដចេញមិនសូន្យ។

**ការបញ្ចូន SDK វិញដោយស្វ័យប្រវត្តិត្រូវបានបិទចោល** ដើម្បីរក្សាលេខស្នើរសុំឲ្យអាចទាយព្យាករណ៍បានលើ deployment RPM ទាបចែករួម។ ស្នើរសុំការព្យាករណ៍រាល់ករណីមានពេលវេលាអស់កំណត់ 60 វិនាទី។ ការទទួលបាន token យកពេលបន្ថែម។ ការកំណត់កាលវិភាគកម្រិតកម្មវិធីត្រូវគោរពគណនាមតិ; កុំបញ្ចម្លែងការប្រតិបត្តិការណ៍ដែលបរាជ័យ។

## ការធ្វើតេស្តផ្នែក

ពីថតឧទាហរណ៍:

```powershell
mvn -B -ntp clean test
```

ការដឹកជញ្ជូនតេស្តជំនួសស្រទាប់ HTTP SDK ពេញលេញ, ចាប់យករាងកាយសំណើ serialized ជាក់លាក់, និងផ្គត់ផ្គង់ចម្លើយក្នុងជួរ។ វាមិនបើកសុកខណៈពេល, មិនទទួលសញ្ញាប័ត្រ Azure, ហើយបរាជ័យនៅលើសំណើដែលមិនរំពឹងទុក។ ការធ្វើតេស្តទាំងនេះបញ្ជាក់លទ្ធផលកម្មវិធី និងពិធីការ SDK មិនមែនគុណភាពម៉ូដែលផ្ទាល់ ឬភាពអាចប្រើបាននៃ deployment ទេ។

| សំណុំតេស្ត | ការគ្របដណ្តប់ |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | ការធ្វើស្តង់ដារចុងផ្លូវ/បដិសេធ, ការជំនួស deployment, ការគិត និងជម្រើស token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | រាល់ដំណើរការបញ្ចប់, ប្រវត្តិសារសារ, ការកាត់បន្ថយជុំបញ្ចប់, EOF, ការបរាជ័យ |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schema ឧបករណ៍, arguments typed, គណិតវិទ្យា, ID, លទ្ធផលឧបករណ៍ច្រើន, ការតាមដានបរាជ័យ |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | ស្វែងរកឯកសារ, UTF-8, កំណត់ទំហំ, បញ្ជាក់ទិន្នន័យ grounding, បញ្ហាបញ្ចូល និង API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | សោភ័ណភាពប្រាំមួយ, ប្រតិបត្តិការត្រួតពិនិត្យច្បាស់, ចំណាត់ថ្នាក់បដិសេធ, កំហុស 400 និងបញ្ហាផ្សេងទៀត |

សម្រាប់សំណុំតេស្តមួយ ប្រើ `mvn -B -ntp test "-Dtest=FunctionsAppTest"`។ ផ្នែកចែករំលែកមាននៅក្នុង [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)។

## ការត្រួតពិនិត្យបន្តបន្ទាប់ផ្ទាល់

ការហៅផ្ទាល់ខ្លួនបំបែកពីការធ្វើតេស្តផ្នែក។ ប្រើពាក្យបញ្ជាដូចខាងក្រោម **ម្យ៉ាងទៅម្យ៉ាង**, ពីឫស្សៈហ្គារី root, បន្ទាប់ពីការចូលប្រើ និងការចូលដំណើរការបានរួចរាល់។ មិនតម្រូវសេវាកម្ម ឬដំណើរការអចិន្រ្តៃយ៍ជានិច្ចទេ។

សម្រាប់ deployment ចែករំលែក **១០ ស្នើរសុំ/នាទី** សូមកក់កំណត់គណនបរិមាណគ្រប់គ្រាន់សម្រាប់កម្មវិធីបន្ទាប់មួយទាំងមូលមុនចាប់ផ្តើមវា: 5, 4, 1, បន្ទាប់ពី 6 ស្នើរសុំ។ ដំណើរការបន្តបន្ទាប់មួយៗមិនធានាទាន់សព្វថ្ងៃ rate-limit ទេ។ សម្របសម្រួលនាទីរំលងជាមួយអ្នកហៅដទៃទៀត; កុំបិទការហៅចំនួនបួនដូចជាជួរមេរៀងគ្នា។

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**១. ការបញ្ចប់, ជុំច្រើន និងជុំជជែកពីរជុំ:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

ពិនិត្យមើលចំណងជើងផ្នែកទាំងបី ចម្លើយប្រាំ, ចម្លើយបន្ទាប់បន្ដដែលអន្តរកម្មដែលរំលឹកដល់ Ada, `Goodbye!`, និងកូដបញ្ចេញ 0។ ថវិកា៖ **ការស្នើសុំ 5 ដង, មិនលើស 1,900 សញ្ញាទម្រង់បញ្ចប់**។ សម្រាប់ការប្រតិបត្តិការតូចចុងបញ្ចប់ បូមតែ `exit`: 3 សំណើ / 900 សញ្ញា, ប៉ុន្តែមិនអនុវត្តការប៉ាន់ប្រមាណអន្តរកម្មទេ។

**2. វិធីសាស្រ្តដែលហៅមុខងារ​ទាំងពីរ:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

ពិនិត្យមើលឈ្មោះមុខងារទាំងពីរ, អាកាសធាតុងាយនៃទីក្រុង Seattle, លទ្ធផលគណនា 36, ចម្លើយចុងក្រោយពីរគន្លង និងកូដបញ្ចេញ 0។ ថវិកា៖ **សំណើ 4 ដង, មិនលើស 1,200 សញ្ញាទម្រង់បញ្ចប់**។

**3. ចម្លើយអាស្រ័យលើឯកសារ:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

ពិនិត្យផ្លូវឯកសារ, ចម្លើយដែលគូរពណ៌នា Microsoft Entra ID និងកូដបញ្ចេញ 0។ ថវិកា៖ **សំណើ 1 ដង, មិនលើស 500 សញ្ញាទម្រង់បញ្ចប់**។ ឯកសារ [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) មានស្រាប់ គឺជាឯកសារបញ្ចូលតែមួយត្រូវការ។ ការប្រតិបត្តិការថ្មីថែមទៀតដែលសួរអំពីប្រធានបទទាល់តែអវត្តមានគួរត្រូវបានទទួលស្គាល់ថាមិនគួរចូលរួម និងបន្ថែមសំណើ ១ និង ៥០០ សញ្ញា។

**4. ការសង្កេតទំនួលខុសត្រូវ AI៖**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

ពិនិត្យមើលជំពូកប្រាំមួយ និងសេចក្តីសង្ខេបពិនិត្យសង្កេត, ពិនិត្យមើលមាតិកាដែលបានបង្កើត, ហើយទាមទារកូដបញ្ចេញ 0 សម្រាប់បញ្ចប់បច្ចេកទេស។ ការចាកចេញជាសំណង់ជោគជ័យមិនអាចធានាថាគំរូមានសុវត្ថិភាពទេ។ ថវិកា៖ **សំណើ 6 ដង, មិនលើស 1,800 សញ្ញាទម្រង់បញ្ចប់**។

**សរុបសម្រាប់បញ្ជារការបួនទៀត៖ សំណើជជែក 16 ដង និងមិនលើស 5,400 សញ្ញាទម្រង់បញ្ចប់**, បូកបញ្ចូលសញ្ញាបញ្ចូល (រួមទាំងការជជែកសំរាប់ការចម្លងហើយ និងស្កីមាគ្រា/ប្រវត្តិឧបករណ៍)។ មានសំណើ embedding សូន្យ។ ការប្រើប្រាស់សញ្ញាពិតប្រាកដអាស្រ័យលើគំរូ និងអាចតិចជាងនេះ, ជាពិសេសសម្រាប់ការផ្ទុកសំណើកាត់ត។ តម្លៃលុយអាស្រ័យលើតម្លៃប្រតិបត្តិការ; មិនមានការប៉ាន់ប្រមាណលុយទេ។ ការកំណត់ជាអ្នកដំណើរការ ច្បាស់ថាគ្មានការរត់ជាស្ដង់ដារឱ្យទៅមុន។ ពិនិត្យ $LASTEXITCODE បន្ទាប់ពីបញ្ជារការតែមួយសំណុំ; មិនមែនសូន្យមានន័យថាការប្រតិបត្តិការមិនបានបញ្ចប់ជោគជ័យ។

## ការដោះស្រាយបញ្ហា

- **គ្មានចំណុចចប់ / 401 / 403:** កំណត់ចំណុចចប់នៅក្នុងដំណើរការចាប់ផ្ដើម, បញ្ជាក់ការចុះឈ្មោះ Azure នៅក្នុងក្នុងផ្ទះ និងតួនាទីដែលមានដែនកំណត់ធនធាន, ពិនិត្យការបំប្លែងអត្តសញ្ញាណដែលមិនគាប់សងខាងក្នុងបរិបទបរិយាកាស។
- **400 / 404:** បញ្ជាក់ថាការចេញផ្សាយមាន និងគាំទ្រការបញ្ចប់ជជែកជាមួយការប្រើប្រាស់សេចក្តីធ្វើការ `none`។ ប្រើ HTTPS resource root ឬ URL `/openai/v1`, មិនប្រើ URL ពីការចេញផ្សាយចាស់ទេ។ កំហុសទូទៅ 400 គឺជាការបរាជ័យបច្ចេកទេស មិនមែនជាឧបសគ្គសុវត្ថិភាពទេ។
- **429:** សម្របសម្រួល RPM ជាភាគចំណែក និងកំណត់ខ្ទង់សញ្ញាក្នុងការប្តូរវិញម្តងទៀត។ ឧទាហរណ៍មិនបានប្តូរវិញដោយស្វ័យប្រវត្តិ។
- **`Incomplete chat response: length`:** ផលបញ្ចប់ទល់ពណ៌នារបស់ប្រភេទបញ្ចប់។ ពិនិត្យបម្លាស់បម្លែងនៃការឆ្លើយတនិងកំណត់ឡើងវិញកំណត់ខ្ទង់និងថវិកា របស់វា; កុំរក្សាការបញ្ចប់ព្រាត់ជោគជ័យ។
- **កំហុសឯកសារឬ stdin:** ចាប់ផ្ដើមពីថតគាំទ្រ ឬផ្តល់ផ្លូវឯកសារពិតប្រាកដ។ ផ្តល់សំនួរអ្នកអានមិនទទេ។ ការបញ្ចប់អាចបញ្ចប់ធម្មតានៅ EOF ឬ `exit`។
- **កំហុសការបញ្ចប់កូដ:** ពិនិត្យ Java 21 ឬក្រោយទៀត, បន្ទាប់មករត់ `mvn -B -ntp clean test`។ នៅ PowerShell ច្រកអ្វីៗទាំងអស់ក្នុងអាគុយម៉ង់ Maven ដែលមានគុណលក្ខណៈចំណុច, ឧទាហរណ៍ `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`។

## ជំហានបន្ទាប់

បន្តទៅកាន់ [ជំពូក 4: គំរូអនុវត្ត](../04-PracticalSamples/README.md)។

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->