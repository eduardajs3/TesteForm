package edu.front.psi.clin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.ui.componentes.MenuLateral
import edu.front.psi.clin.ui.viewmodel.PrincipalViewModel

// Imports das telas de Formulário
import edu.front.psi.clin.ui.screen.AgendaFormulario
import edu.front.psi.clin.ui.screen.BloqueioAgendaFormulario
import edu.front.psi.clin.ui.screen.ConvenioFormulario
import edu.front.psi.clin.ui.screen.EvolucaoClinicaFormulario
import edu.front.psi.clin.ui.screen.FuncionarioFormulario
import edu.front.psi.clin.ui.screen.HorarioDisponivelFormulario
import edu.front.psi.clin.ui.screens.paciente.PacienteFormulario 
import edu.front.psi.clin.ui.screen.PagamentoFormulario
import edu.front.psi.clin.ui.screen.ProntuarioFormulario
import edu.front.psi.clin.ui.screen.PsicologoFormulario
import edu.front.psi.clin.ui.screen.RecepcionistaFormulario
import edu.front.psi.clin.ui.screen.SalaAtendimentoFormulario
import edu.front.psi.clin.ui.screen.TipoAtendimentoFormulario

// Imports das telas de Listagem e Dashboard
import com.psiclin.front.ui.screens.auth.LoginScreen
import com.psiclin.front.ui.screens.auth.RecoverPasswordScreen
import edu.front.psi.clin.ui.screen.AgendaListagem
import edu.front.psi.clin.ui.screen.BloqueioAgendaListagem
import edu.front.psi.clin.ui.screen.ConvenioListagem
import edu.front.psi.clin.ui.screen.DashboardTela
import edu.front.psi.clin.ui.screen.EvolucaoClinicaListagem
import edu.front.psi.clin.ui.screen.FuncionarioListagem
import edu.front.psi.clin.ui.screen.HorarioDisponivelListagem
import edu.front.psi.clin.ui.screen.PacienteListagem
import edu.front.psi.clin.ui.screen.PagamentoListagem
import edu.front.psi.clin.ui.screens.prontuario.ProntuarioListagem
import edu.front.psi.clin.ui.screen.PsicologoListagem
import edu.front.psi.clin.ui.screen.RecepcionistaListagem
import edu.front.psi.clin.ui.screen.SalaAtendimentoListagem
import edu.front.psi.clin.ui.screen.TipoAtendimentoListagem

// Imports das ViewModels
import edu.front.psi.clin.ui.viewmodel.*
import kotlinx.coroutines.launch
import edu.front.psi.clin.navigation.ConvenioRotas
import edu.front.psi.clin.navigation.FuncionarioRotas
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.navigation.PsicologoRotas
import edu.front.psi.clin.navigation.RecepcionistaRotas
import edu.front.psi.clin.navigation.SalaAtendimentoRotas
import edu.front.psi.clin.navigation.TipoAtendimentoRotas
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.navigation.PacienteRotas
import edu.front.psi.clin.navigation.AgendaRotas

private val Branco = Color.White
val CremeCampo = Color(0xFFF6F3EC)
val VerdeClinica = Color(0xFF3B5760)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val scope = rememberCoroutineScope()

    // 1. ViewModel Principal centralizando a navegação global, gaveta e snackbar
    val principalVm: PrincipalViewModel = viewModel(
        factory = viewModelFactory { initializer { PrincipalViewModel(scope) } }
    )

    // Estado para guardar o nome do paciente selecionado entre telas
    var pacienteSelecionadoNome by remember { mutableStateOf("") }

    // 2. ViewModels instanciados
    val pacienteVm: PacienteViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                PacienteViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val pagamentoVm: PagamentoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                PagamentoViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    
    // ConvenioViewModel atualizado recebendo o callback de mensagem global do principalVm
    val convenioVm: ConvenioViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                ConvenioViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val funcionarioVm: FuncionarioViewModel =
    viewModel(
        factory = viewModelFactory {
            initializer {
                FuncionarioViewModel(
                    mandarMensagem = { msg, dur ->
                        principalVm.mostrarMensagem(msg, dur)
                    }
                )
            }
        }
    )

    val horarioVm: HorarioDisponivelViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                HorarioDisponivelViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val salaVm: SalaAtendimentoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                SalaAtendimentoViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val tipoVm: TipoAtendimentoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                TipoAtendimentoViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val bloqueioVm: BloqueioAgendaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                BloqueioAgendaViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val evolucaoVm: EvolucaoClinicaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                EvolucaoClinicaViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val prontuarioVm: ProntuarioViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                ProntuarioViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val recepcionistaVm: RecepcionistaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                RecepcionistaViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    
    val psicologoVm: PsicologoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                PsicologoViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )
    val agendaVm: AgendaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                AgendaViewModel(mandarMensagem = { msg, dur -> principalVm.mostrarMensagem(msg, dur) }) 
            } 
        }
    )

    val rotaAtual = principalVm.backstack.lastOrNull() ?: Route.Dashboard

    // String identificadora da tela atual para o MenuLateral
    val stringTelaAtual = when (rotaAtual) {
        is Route.Dashboard -> "dashboard"
        is Route.Paciente -> "paciente"
        is Route.Pagamento -> "pagamento"
        is Route.Convenio -> "convenio"
        is Route.HorarioDisponivel -> "horario"
        is Route.SalaAtendimento -> "sala"
        is Route.TipoAtendimento -> "tipo"
        is Route.BloqueioAgenda -> "bloqueio"
        is Route.EvolucaoClinica -> "evolucao"
        is Route.Prontuario -> "prontuario"
        is Route.Recepcionista -> "recepcionista"
        is Route.Funcionario -> "funcionario"
        is Route.Psicologo -> "psicologo"
        is Route.Agenda -> "agenda"
        else -> "dashboard"
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = VerdeClinica,
            onPrimary = Color.White
        )
    ) {
        // MenuLateral consumindo diretamente o PrincipalViewModel
        MenuLateral(
            viewModel = principalVm,
            telaAtual = stringTelaAtual,
            onNavigate = { destino ->
                val novaRota = when (destino) {
                    "dashboard" -> Route.Dashboard
                    "paciente" -> Route.Paciente
                    "pagamento" -> Route.Pagamento
                    "convenio" -> Route.Convenio
                    "horario" -> Route.HorarioDisponivel
                    "sala" -> Route.SalaAtendimento
                    "tipo" -> Route.TipoAtendimento
                    "bloqueio" -> Route.BloqueioAgenda
                    "funcionario" -> Route.Funcionario
                    "psicologo" -> Route.Psicologo
                    "recepcionista" -> Route.Recepcionista
                    "agenda" -> Route.Agenda
                    "login" -> Route.Login
                    else -> Route.Dashboard
                }

                if (novaRota == Route.Login) {
                    principalVm.backstack.clear()
                    principalVm.backstack.add(Route.Login)
                } else {
                    principalVm.backstack.clear()
                    principalVm.backstack.add(Route.Dashboard)
                    if (novaRota != Route.Dashboard) {
                        principalVm.backstack.add(novaRota)
                    }
                }
            }
        ) {
            if (rotaAtual is Route.Login) {

                    LoginScreen(
                        onNavigate = { rota ->
                            principalVm.backstack.clear()
                            principalVm.backstack.add(rota)
                        }
                    )

                } else if (rotaAtual is Route.RecoverPassword) {

                    RecoverPasswordScreen(
                        onNavigate = { rota ->
                            principalVm.backstack.clear()
                            principalVm.backstack.add(rota)
                        }
                    )

                } else {
                Scaffold(
                    snackbarHost = { SnackbarHost(principalVm.snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (rotaAtual) {
                                        is Route.Dashboard -> "Dashboard"
                                        is Route.Paciente -> "Pacientes"
                                        is Route.Pagamento -> "Pagamentos"
                                        is Route.Convenio -> "Convênios"
                                        is Route.HorarioDisponivel -> "Horários Disponíveis"
                                        is Route.SalaAtendimento -> "Salas de Atendimento"
                                        is Route.TipoAtendimento -> "Tipos de Atendimento"
                                        is Route.BloqueioAgenda -> "Bloqueios"
                                        is Route.EvolucaoClinica -> "Evoluções"
                                        is Route.Prontuario -> "Prontuários"
                                        is Route.Recepcionista -> "Recepcionistas"
                                        is Route.Funcionario -> "Funcionários"
                                        is Route.Psicologo -> "Psicólogos"
                                        is Route.Agenda -> "Agendas"
                                        else -> "PsiClin"
                                    },
                                    color = Color.White
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { principalVm.abrirGaveta() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Abrir Menu Lateral",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeClinica)
                        )
                    },
                    containerColor = Branco
                ) { contentPadding ->
                    NavDisplay(
                        backStack = principalVm.backstack,
                        onBack = {
                            if (principalVm.backstack.size > 1) {
                                principalVm.backstack.removeAt(principalVm.backstack.lastIndex)
                            }
                        },
                        entryProvider = entryProvider {
                            entry<Route.Dashboard> {
                                DashboardTela(contentPadding)
                            }
                            entry<Route.Paciente> {
                                val subRota = pacienteVm.backStack.lastOrNull() ?: PacienteRotas.Listagem
                                if (subRota is PacienteRotas.Formulario) {
                                    PacienteFormulario(contentPadding, pacienteVm)
                                } else {
                                    PacienteListagem(
                                        innerPadding = contentPadding,
                                        vm = pacienteVm,
                                        onNavegarParaProntuario = { paciente ->
                                            pacienteSelecionadoNome = paciente.nome
                                            prontuarioVm.paciente.value = paciente.nome
                                            prontuarioVm.atualizarFiltro(paciente.nome)
                                            prontuarioVm.backStack.clear()
                                            prontuarioVm.backStack.add(ProntuarioRotas.Listagem)
                                            principalVm.backstack.add(Route.Prontuario)
                                        },
                                        onNavegarParaEvolucao = { paciente ->
                                            pacienteSelecionadoNome = paciente.nome
                                            evolucaoVm.prontuario.value = paciente.nome
                                            evolucaoVm.atualizarFiltro(paciente.nome)
                                            evolucaoVm.backStack.clear()
                                            evolucaoVm.backStack.add(EvolucaoClinicaRotas.Listagem)
                                            principalVm.backstack.add(Route.EvolucaoClinica)
                                        }
                                    )
                                }
                            }
                            entry<Route.Pagamento> {
                                val subRota = pagamentoVm.backStack.lastOrNull() ?: PagamentoRotas.Listagem
                                if (subRota is PagamentoRotas.Formulario) {
                                    PagamentoFormulario(contentPadding, pagamentoVm)
                                } else {
                                    PagamentoListagem(contentPadding, pagamentoVm)
                                }
                            }
                            
                            // Módulo de Convênio adaptado para usar o backStack interno do ViewModel
                            entry<Route.Convenio> {
                                val subRota =
                                    convenioVm.backStack.lastOrNull()
                                        ?: ConvenioRotas.Listagem

                                if (subRota is ConvenioRotas.Formulario) { 
                                    ConvenioFormulario(contentPadding, convenioVm)
                                } else {
                                    ConvenioListagem(contentPadding, convenioVm)
                                }
                            }

                            entry<Route.HorarioDisponivel> {
                                val subRota = horarioVm.backStack.lastOrNull() ?: HorarioDisponivelRotas.Listagem
                                if (subRota is HorarioDisponivelRotas.Formulario) {
                                    HorarioDisponivelFormulario(contentPadding, horarioVm)
                                } else {
                                    HorarioDisponivelListagem(contentPadding, horarioVm)
                                }
                            }
                            entry<Route.SalaAtendimento> {
                                val subRota = salaVm.backStack.lastOrNull() ?: SalaAtendimentoRotas.Listagem
                                if (subRota is SalaAtendimentoRotas.Formulario) {
                                    SalaAtendimentoFormulario(contentPadding, salaVm)
                                } else {
                                    SalaAtendimentoListagem(contentPadding, salaVm)
                                }
                            }
                            entry<Route.TipoAtendimento> {
                                val subRota = tipoVm.backStack.lastOrNull() ?: TipoAtendimentoRotas.Listagem
                                if (subRota is TipoAtendimentoRotas.Formulario) {
                                    TipoAtendimentoFormulario(contentPadding, tipoVm)
                                } else {
                                    TipoAtendimentoListagem(contentPadding, tipoVm)
                                }
                            }
                            entry<Route.BloqueioAgenda> {
                                val subRota = bloqueioVm.backStack.lastOrNull() ?: BloqueioAgendaRotas.Listagem
                                if (subRota is BloqueioAgendaRotas.Formulario) {
                                    BloqueioAgendaFormulario(contentPadding, bloqueioVm)
                                } else {
                                    BloqueioAgendaListagem(contentPadding, bloqueioVm)
                                }
                            }
                            entry<Route.EvolucaoClinica> {
                                val subRota =
                                    evolucaoVm.backStack.lastOrNull()
                                        ?: EvolucaoClinicaRotas.Listagem

                                if (subRota is EvolucaoClinicaRotas.Formulario) {
                                    EvolucaoClinicaFormulario(contentPadding, evolucaoVm)
                                } else {
                                    EvolucaoClinicaListagem(
                                        innerPadding = contentPadding,
                                        vm = evolucaoVm,
                                        nomePaciente = pacienteSelecionadoNome.ifEmpty { "Paciente" }
                                    )
                                }
                            }
                            entry<Route.Prontuario> {
                                val subRota = prontuarioVm.backStack.lastOrNull() ?: ProntuarioRotas.Listagem
                                if (subRota is ProntuarioRotas.Formulario) {
                                    ProntuarioFormulario(contentPadding, prontuarioVm)
                                } else {
                                    ProntuarioListagem(
                                        innerPadding = contentPadding,
                                        vm = prontuarioVm,
                                        nomePaciente = pacienteSelecionadoNome.ifEmpty { "Paciente" }
                                    )
                                }
                            }
                            entry<Route.Recepcionista> {
                                val subRota = recepcionistaVm.backStack.lastOrNull() ?: RecepcionistaRotas.Listagem
                                if (subRota is RecepcionistaRotas.Formulario) {
                                    RecepcionistaFormulario(contentPadding, recepcionistaVm)
                                } else {
                                    RecepcionistaListagem(contentPadding, recepcionistaVm)
                                }
                            }
                            entry<Route.Funcionario> {
                                val subRota = funcionarioVm.backStack.lastOrNull() ?: FuncionarioRotas.Listagem

                                if (subRota is FuncionarioRotas.Formulario) {
                                    FuncionarioFormulario(contentPadding, funcionarioVm)
                                } else {
                                    FuncionarioListagem(contentPadding, funcionarioVm)
                                }
                            }
                            entry<Route.Psicologo> {
                                val subRota = psicologoVm.backStack.lastOrNull() ?: PsicologoRotas.Listagem
                                if (subRota is PsicologoRotas.Formulario) {
                                    PsicologoFormulario(contentPadding, psicologoVm)
                                } else {
                                    PsicologoListagem(contentPadding, psicologoVm)
                                }
                            }
                            entry<Route.Agenda> {
                                val subRota = agendaVm.backStack.lastOrNull() ?: AgendaRotas.Listagem
                                if (subRota is AgendaRotas.Formulario) {
                                    AgendaFormulario(contentPadding, agendaVm)
                                } else {
                                    AgendaListagem(contentPadding, agendaVm)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}