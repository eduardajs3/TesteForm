# Explicação do Login JWT e da URL da API

Este documento explica as alterações feitas no cliente PsiClin para autenticar com JWT e configurar o endereço do backend por meio de `.env`. O foco é nas linhas alteradas e nos métodos envolvidos: o restante do código não foi reescrito.

## Visão geral do fluxo

1. O Gradle lê `API_BASE_URL` do `.env` (ou de `.env.example`, se `.env` não existir) e gera a classe `ApiConfig`.
2. A pessoa informa e-mail e senha na tela de login.
3. `PrincipalViewModel.login` chama `AuthAPI.login`.
4. `AuthAPI` envia `POST {API_BASE_URL}/auth/signin` com `email` e `senha`.
5. O backend retorna um JSON com `token`. A anotação `@SerialName` mapeia esse nome para a propriedade Kotlin `accessToken`.
6. O ViewModel guarda o token em `SessionManager`, marca a sessão como autenticada e navega para o Dashboard.
7. As APIs de dados usam `SessionManager.token` como Bearer token e `ApiConfig.BASE_URL` para formar os endpoints.
8. No logout, o token é apagado e o app volta à tela de login.

Contrato confirmado pela chamada ao backend:

```http
POST /auth/signin
Content-Type: application/json

{"email":"...","senha":"..."}
```

Resposta esperada:

```json
{"token":"<JWT>"}
```

## Configuração da URL

### `.env`

Arquivo local, ignorado pelo Git, com o endereço usado nesta máquina. Atualmente:

```env
API_BASE_URL=https://psiclin-backend.onrender.com
```

O Gradle lê esse valor durante a compilação. Alterar o arquivo não muda um app já compilado: é necessário compilar de novo. A URL é configuração pública do cliente; não coloque senhas, chaves de assinatura JWT ou outros segredos nela.

### [.env.example](../.env.example)

Modelo versionado da variável necessária. Ele permite que outra pessoa saiba qual configuração criar, sem depender de um `.env` local. O endereço de exemplo atual é o mesmo backend hospedado; pode ser trocado pelo endereço correto do ambiente usado.

### [.gitignore](../.gitignore)

Foi adicionada a entrada `.env`. **Causa:** valores locais não devem ser incluídos em commits por acidente. **Efeito:** o `.env` continua disponível para o build local, mas não aparece como arquivo a versionar; `.env.example` permanece versionável.

### [buildSrc/build.gradle.kts](../buildSrc/build.gradle.kts)

O plugin `java` habilita a compilação da tarefa Gradle escrita em Java. Essa tarefa é código de build, não código executado pelo aplicativo.

### [GenerateApiConfigTask.java](../buildSrc/src/main/java/edu/front/psi/build/GenerateApiConfigTask.java)

A classe `GenerateApiConfigTask` implementa a leitura e validação da configuração:

- `getConfigFiles`, anotado com `@InputFiles`, declara `.env` e `.env.example` como entradas da tarefa. O Gradle usa essas entradas para saber quando a tarefa precisa ser executada novamente.
- `getOutputDirectory`, anotado com `@OutputDirectory`, declara onde será gerado o código Kotlin.
- `generate()` escolhe `.env` quando ele existe; caso contrário, usa `.env.example`. Se nenhum estiver disponível, interrompe o build com uma mensagem explícita.
- `readBaseUrl()` percorre o arquivo, ignora linhas vazias e comentários, encontra a chave `API_BASE_URL`, separa a chave do valor no primeiro `=` e remove aspas externas. Se a chave não existir, o build falha em vez de gerar uma URL vazia.
- `replaceAll("/+$", "")` remove barras finais. Assim, um valor como `https://api.exemplo/` não gera endpoints com `//`.
- A validação exige `http://` ou `https://`. Isso detecta configuração incompleta durante o build, em vez de produzir falha de rede mais tarde.
- O método cria o diretório e escreve `ApiConfig.kt` com `ApiConfig.BASE_URL`. O valor é escapado para continuar sendo uma string Kotlin válida.

**Causa e efeito:** a tarefa transforma uma configuração externa em uma constante compilável comum a Android, JVM e web; as classes de API não precisam ler arquivos do dispositivo em tempo de execução.

### [shared/build.gradle.kts](../shared/build.gradle.kts)

- O `import GenerateApiConfigTask` disponibiliza a tarefa personalizada ao script do módulo.
- `envFile` e `envExampleFile` apontam para os arquivos na raiz do repositório.
- `generatedApiConfigDir` define o diretório de saída dentro de `build/`, que é artefato gerado e não código-fonte editado manualmente.
- `tasks.register<GenerateApiConfigTask>("generateApiConfig")` registra a tarefa, informa os arquivos de entrada e o diretório de saída.
- `commonMain.kotlin.srcDir(generatedApiConfigDir)` inclui `ApiConfig.kt` no código compartilhado.
- A dependência das tarefas cujo nome começa com `compile` garante que a configuração seja gerada antes da compilação. A tarefa tipada, com entradas e saídas declaradas, também permite ao Gradle controlar corretamente incrementalidade e configuration cache.

## Autenticação

### [AuthAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/AuthAPI.kt)

- `LoginRequest` representa o JSON enviado. Suas propriedades `email` e `senha` correspondem ao contrato confirmado do backend.
- `LoginResponse` representa a resposta. `@SerialName("token")` diz ao Kotlin Serialization para preencher `accessToken` a partir da chave JSON `token`. Isso permite manter um nome descritivo no app sem exigir que o backend renomeie a resposta.
- `AuthAPI` recebe um `HttpClient`, reutilizando a infraestrutura Ktor já usada nas outras APIs.
- `login(email, senha)` envia um POST para `${ApiConfig.BASE_URL}/auth/signin`, define `Content-Type: application/json` e serializa `LoginRequest` no corpo.
- Para qualquer status fora de `200..299`, lança uma exceção. O texto atualmente diz que e-mail ou senha são inválidos; por isso, outros status HTTP também acabam apresentados dessa maneira.
- Em status de sucesso, desserializa a resposta como `LoginResponse` e rejeita token em branco. Só então devolve o resultado ao ViewModel.

**Causa e efeito da correção do endpoint:** a primeira versão do cliente chamava `/auth/login` e esperava `accessToken`; a evidência do backend mostrou `/auth/signin` e `{ "token": ... }`. A divergência de caminho podia resultar em falha de autenticação, e a divergência do nome do campo impediria ler uma resposta de sucesso. As duas partes agora seguem o contrato real.

### [SessionManager.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/SessionManager.kt)

- `token` começa vazio, em vez de conter um JWT fixo no código-fonte.
- `private set` impede que qualquer classe substitua o token diretamente; a sessão só muda pelos métodos do objeto.
- `iniciarSessao(accessToken)` guarda o token recebido do servidor.
- `encerrarSessao()` apaga o token.

O token é mantido somente em memória: fechar ou reiniciar o app encerra a sessão. Ainda não há persistência segura nem renovação automática de token.

### [PrincipalViewModel.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/viewModel/PrincipalViewModel.kt)

- `authAPI` é criado com o `HttpClient` provido por `httpClientManager()`.
- `autenticado`, `autenticando` e `erroLogin` são estados observáveis pelo Compose. `private set` deixa o ViewModel como único responsável por alterá-los.
- `login(email, senha)` ignora cliques repetidos enquanto uma tentativa está em andamento. Em `viewModelScope`, marca carregamento, limpa erro anterior e chama a API com o e-mail sem espaços no início ou no fim.
- No sucesso, armazena o token, marca como autenticado e substitui a pilha de navegação por `Route.Dashboard`.
- No erro, limpa qualquer token e publica uma mensagem para a tela. O `finally` sempre desativa o estado de carregamento, inclusive quando a chamada falha.
- `logout()` apaga o token, zera estado de autenticação/erro e restaura a pilha para `Route.Login`.

Os métodos preexistentes de gaveta e notificação não mudaram; o `CoroutineScope` que já era usado por eles continua sendo usado para essas ações. O login usa `viewModelScope`, que acompanha o ciclo de vida do ViewModel.

### [LoginScreen.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/ui/screens/auth/LoginScreen.kt)

- A assinatura agora recebe `onLogin`, `isLoading` e `errorMessage`, além de `onNavigate`.
- `onNavigate` continua servindo aos links de cadastro e recuperação de senha; não é mais usado para fingir que o login foi bem-sucedido.
- `isLoading` altera o texto do botão para “Entrando...” e o desabilita. Campos vazios também desabilitam o botão.
- `errorMessage`, quando presente, aparece com a cor de erro do tema.
- O clique do botão chama `onLogin(email, senha)`. A tela coleta os dados e delega a chamada de rede ao ViewModel, em vez de navegar diretamente para o Dashboard.
- Os campos, a máscara de senha e o controle de visibilidade continuam com a mesma função anterior.

### [App.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/App.kt)

- Logo após criar `PrincipalViewModel`, `if (!principalVm.autenticado)` seleciona a tela de autenticação atual.
- Cadastro e recuperação de senha continuam acessíveis por suas rotas. Nos demais casos, aparece `LoginScreen` ligada a `principalVm.login`, `autenticando` e `erroLogin`.
- O `return` encerra essa composição antes da criação dos ViewModels de agenda, pacientes, pagamentos e demais dados protegidos. Como alguns carregam dados em sua inicialização, isso evita requisições antes de existir token.
- Depois do login, `autenticado` muda e o Compose recompõe `App`; o fluxo passa a criar os ViewModels protegidos.
- A opção de menu que leva a `Route.Login` agora chama `logout()`, para limpar a sessão em vez de apenas mudar a rota.
- Os pontos de composição já existentes que criam `LoginScreen` recebem os novos parâmetros de login, carregamento e erro, mantendo as chamadas compatíveis com a nova assinatura.

## APIs que usam a URL centralizada

Em todos os arquivos abaixo, a mudança funcional na URL é a mesma: o endereço completo fixo foi substituído por `${ApiConfig.BASE_URL}` mais o caminho específico. Os métodos `carregar`, `salvar`, `atualizar` e `apagar` continuam usando seus verbos HTTP e Bearer token como antes.

| Arquivo | Endpoint depois da mudança |
| --- | --- |
| [AgendaAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/AgendaAPI.kt) | `${ApiConfig.BASE_URL}/agendas` |
| [ConvenioAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/ConvenioAPI.kt) | `${ApiConfig.BASE_URL}/convenios` |
| [HorarioDisponivel.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/HorarioDisponivel.kt) | `${ApiConfig.BASE_URL}/horarios-disponiveis` |
| [PacienteAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/PacienteAPI.kt) | `${ApiConfig.BASE_URL}/pacientes` |
| [PagamentoAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/PagamentoAPI.kt) | `${ApiConfig.BASE_URL}/pagamentos` |
| [PsicologoAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/PsicologoAPI.kt) | `${ApiConfig.BASE_URL}/psicologos` |
| [RecepcionistaAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/RecepcionistaAPI.kt) | `${ApiConfig.BASE_URL}/recepcionistas` |
| [SalaAtendimentoAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/SalaAtendimentoAPI.kt) | `${ApiConfig.BASE_URL}/salas-atendimento` |
| [TipoAtendimento.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/TipoAtendimento.kt) | `${ApiConfig.BASE_URL}/tipos-atendimento` |

Em todos os casos, a parte anterior ao caminho era o host fixo `https://psiclin-backend.onrender.com`. Por exemplo, a linha de agenda mudou de `"https://psiclin-backend.onrender.com/agendas"` para `"${ApiConfig.BASE_URL}/agendas"`; as demais substituem apenas o caminho final mostrado na tabela. O efeito de cada substituição é permitir que o mesmo código use qualquer host configurado sem alterar a rota específica do recurso.

A propriedade `endPoint` é compartilhada pelos métodos de cada classe, então a troca dessa linha altera o destino de todas as operações daquela API:

- `carregar()` faz `GET` em `endPoint`, inclui o Bearer token e converte o JSON em uma lista do modelo correspondente.
- `salvar(modelo)` faz `POST` em `endPoint`, envia o token e serializa o objeto como JSON.
- `atualizar(id, modelo)` faz `PUT` em `endPoint/{id}`, mantendo token e corpo JSON.
- `apagar(modelo)` faz `DELETE` em `endPoint/{id}`, mantendo o token.

Esses métodos não tiveram seu comportamento CRUD alterado; agora todos apontam para o host escolhido em `.env`. Assim, mudar de servidor não exige editar cada método nem cada arquivo de API.

Em [AgendaAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/AgendaAPI.kt) e [PacienteAPI.kt](../shared/src/commonMain/kotlin/edu/front/psi/clin/api/PacienteAPI.kt), também foram removidos logs que imprimiam o JWT, o objeto enviado ou o texto completo da resposta. Esses dados podem conter credenciais ou informações pessoais. Os logs de status HTTP foram mantidos.

## Causa e efeito, resumidos

- **Antes:** o login era apenas navegação local; o botão Entrar não consultava o backend.
- **Agora:** o botão envia credenciais, espera uma resposta real e só abre o Dashboard se houver token.
- **Antes:** havia um JWT fixo no cliente e endereços de backend repetidos em cada API.
- **Agora:** o token vem da resposta do login e o endereço é mantido em `.env`, centralizado durante o build.
- **Antes:** ViewModels que carregam dados podiam ser criados na tela de login.
- **Agora:** sua criação fica depois da verificação de estado autenticado.
- **Antes:** o cliente usava caminho e nome de campo diferentes do contrato mostrado pelo backend.
- **Agora:** usa `/auth/signin` e converte a chave `token` da resposta para `accessToken`.

## Limites atuais e verificação

- O backend não está incluído neste workspace; a implementação do servidor, cadastro de contas e validação BCrypt não foram alterados.
- A sessão não sobrevive ao fechamento do app e não há refresh token.
- A mensagem de erro de `AuthAPI.login` associa qualquer status não-2xx a credenciais inválidas. Se o servidor puder retornar outros erros, é melhor ler e tratar o corpo/status com mensagens específicas.
- A compilação Android foi validada com `:shared:compileAndroidMain`. A compilação JS do projeto já falha por referências a `java.*` em arquivos de `commonMain` não relacionados a estas mudanças.
- Um JWT compartilhado em captura de tela deve ser considerado exposto e invalidado ou deixado expirar.
