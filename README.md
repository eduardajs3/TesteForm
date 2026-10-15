# FrontPsiClinic

## Identificação do projeto

- **Projeto:** FrontPsiClinic — protótipo de interface para uma clínica de psicologia.
- **Grupo:** [Agostinho Benigno Monteiro Gutierrez, Afonso Rocha Sprocatti, Eduarda Josileide da Silva, Lindalva Carlos da Silva, Westes Mesquita da Silva Oliveira].
- **Repositório Git:** [inserir o link HTTPS ou SSH do repositório remoto].

> O diretório de trabalho usado para desenvolver este projeto não possui um repositório Git configurado. Substitua o campo acima pelo endereço remoto oficial do grupo antes da entrega.

## Integrantes e entidades

Preencha esta tabela com os nomes dos integrantes e distribua as entidades de acordo com a responsabilidade de cada pessoa. Cada integrante deve ficar responsável por pelo menos duas entidades e pelos respectivos formulários.

| Integrante | Entidades sob sua responsabilidade |
|---|---|
| [Agostinho Benigno Monteiro Gutierrez] | [Agenda], [BloqueioAgenda] |
| [Afonso Rocha Sprocatti] | [Entidade 1], [Entidade 2] |
| [Adicionar linhas conforme o grupo] | |

### Entidades do projeto

O projeto contém modelos para:

- Agenda
- Bloqueio de agenda
- Convênio
- Evolução clínica
- Funcionário
- Horário disponível
- Paciente
- Pagamento
- Prontuário
- Psicólogo
- Recepcionista
- Sala de atendimento
- Tipo de atendimento

Cada modelo está em `shared/src/commonMain/kotlin/edu/front/psi/clin/model/`, e os formulários correspondentes estão no pacote `edu.front.psi.clin`, em `shared/src/commonMain/kotlin/edu/front/psi/clin/`.

## Descrição da aplicação

O FrontPsiClinic é um protótipo multiplataforma de interface para organizar cadastros e informações de uma clínica de psicologia. A aplicação reúne formulários de pacientes, profissionais, agenda, prontuários, pagamentos e outros dados relacionados ao atendimento.

A centralização dessas telas busca facilitar a visualização das áreas funcionais da clínica e demonstrar como seus dados podem ser inseridos. O projeto ainda é uma interface demonstrativa: não possui backend, banco de dados ou persistência. Os botões **Gravar** não armazenam os dados; **Limpar Campos** limpa os campos do formulário atual.

## Tecnologias utilizadas

- Kotlin Multiplatform
- Compose Multiplatform
- Material 3
- Gradle 9.5.1 com Gradle Wrapper
- Android, Desktop (JVM), JavaScript e WebAssembly como alvos configurados

Versões principais configuradas em `gradle/libs.versions.toml`: Kotlin 2.4.20, Compose Multiplatform 1.12.1 e Material 3 1.12.0-alpha03.

## Arquitetura e organização

O projeto não possui backend implementado. Portanto, não há camadas de API, serviço ou persistência a documentar. A arquitetura existente é a do frontend multiplataforma:

| Camada/área | Implementação |
|---|---|
| Interface e navegação | `shared/src/commonMain/kotlin/edu/front/psi/clin/` — telas de formulário, menu e componentes visuais Compose |
| Modelos de domínio | `shared/src/commonMain/kotlin/edu/front/psi/clin/model/` — entidades Kotlin compartilhadas |
| Recursos compartilhados | `shared/src/commonMain/composeResources/` |
| Inicialização Android | `androidApp/src/main/` |
| Inicialização Desktop | `desktopApp/src/main/kotlin/` |
| Inicialização Web | `webApp/src/webMain/kotlin/` e `webApp/src/webMain/resources/` |

Os formulários e modelos ficam no módulo `shared` para serem reutilizados pelos alvos Android, Desktop e Web. Não há separação em pastas de repositórios, serviços ou banco de dados porque essas partes ainda não fazem parte do protótipo.

## Requisitos

- Git para clonar o repositório remoto.
- JDK 21 (o Gradle Wrapper está configurado com toolchain Azul 21).
- Android Studio com Android SDK para executar no Android.
- Navegador compatível para executar as versões Web.

O Gradle Wrapper baixa a versão necessária do Gradle na primeira execução. A compilação também pode baixar dependências, portanto é necessário acesso à internet na configuração inicial.

## Instalação

Clone o repositório remoto do grupo:

```powershell
git clone <https://github.com/sistema-psicologo-dsm-6/PsiClin_Frontend.git>
cd FrontPsiClinic
```

Abra a pasta do projeto no Android Studio ou use o terminal com o Gradle Wrapper incluído.

## Execução

Execute os comandos a partir da raiz do projeto. No Windows, use `.\gradlew.bat`; no macOS/Linux, use `./gradlew`.

### Desktop

```powershell
.\gradlew.bat :desktopApp:run
```

O aplicativo é aberto em uma janela Desktop.

### WebAssembly

```powershell
.\gradlew.bat :webApp:wasmJsBrowserDevelopmentRun
```

O Gradle inicia o servidor de desenvolvimento e abre a aplicação no navegador. O endereço local é informado no terminal.

### JavaScript

```powershell
.\gradlew.bat :webApp:jsBrowserDevelopmentRun
```

### Android

Conecte um dispositivo Android ou inicie um emulador configurado no Android Studio. Para gerar o APK de depuração:

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

O APK é gerado em `androidApp/build/outputs/apk/debug/`. Também é possível abrir o projeto no Android Studio, selecionar a configuração Android e executar no dispositivo ou emulador.

## Acesso à aplicação

- **Desktop:** a aplicação é acessada na janela aberta pelo comando `:desktopApp:run`.
- **Web:** acesse no navegador o endereço local exibido pelo Gradle ao iniciar o servidor de desenvolvimento.
- **Android:** instale e abra o APK gerado ou execute a aplicação diretamente pelo Android Studio em um dispositivo/emulador.

## Testes

Para executar os testes disponíveis do módulo compartilhado:

```powershell
.\gradlew.bat :shared:jvmTest
```
