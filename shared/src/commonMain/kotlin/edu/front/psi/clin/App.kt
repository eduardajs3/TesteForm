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
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import edu.front.psi.clin.ui.viewmodel.BloqueioAgendaViewModel
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel
import edu.front.psi.clin.ui.viewmodel.TipoAtendimentoViewModel
import kotlinx.coroutines.launch

private val Branco = Color.White
val CremeCampo = Color(0xFFF6F3EC)
val VerdeClinica = Color(0xFF3B5760)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Pilha de navegação (backstack) baseada nas rotas tipadas ensinadas na aula
    val backstack = remember { mutableStateListOf<Route>(Route.Dashboard) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estado para guardar o nome do paciente selecionado entre telas
    var pacienteSelecionadoNome by remember { mutableStateOf("") }

    // ViewModels instanciados
    val pacienteVm: PacienteViewModel = viewModel(factory = viewModelFactory { initializer { PacienteViewModel() } })
    val pagamentoVm: PagamentoViewModel = viewModel(factory = viewModelFactory { initializer { PagamentoViewModel() } })
    val convenioVm: ConvenioViewModel = viewModel(factory = viewModelFactory { initializer { ConvenioViewModel() } })
    val horarioVm: HorarioDisponivelViewModel = viewModel(factory = viewModelFactory { initializer { HorarioDisponivelViewModel() } })
    val salaVm: SalaAtendimentoViewModel = viewModel(factory = viewModelFactory { initializer { SalaAtendimentoViewModel() } })
    val tipoVm: TipoAtendimentoViewModel = viewModel(factory = viewModelFactory { initializer { TipoAtendimentoViewModel() } })
    val bloqueioVm: BloqueioAgendaViewModel = viewModel(factory = viewModelFactory { initializer { BloqueioAgendaViewModel() } })
    val evolucaoVm: EvolucaoClinicaViewModel = viewModel(factory = viewModelFactory { initializer { EvolucaoClinicaViewModel() } })
    val prontuarioVm: ProntuarioViewModel = viewModel(factory = viewModelFactory { initializer { ProntuarioViewModel() } })
    val recepcionistaVm: RecepcionistaViewModel = viewModel(factory = viewModelFactory { initializer { RecepcionistaViewModel() } })
    val funcionarioVm: FuncionarioViewModel = viewModel(factory = viewModelFactory { initializer { FuncionarioViewModel() } })
    val psicologoVm: PsicologoViewModel = viewModel(factory = viewModelFactory { initializer { PsicologoViewModel() } })
    val agendaVm: AgendaViewModel = viewModel(factory = viewModelFactory { initializer { AgendaViewModel() } })

    // Rota atualmente visível no topo da pilha
    val rotaAtual = backstack.lastOrNull() ?: Route.Dashboard

    // Identifica se está em modo de formulário em alguma das telas
    fun estaEmFormulario(): Boolean {
        return when (rotaAtual) {
            is Route.Paciente -> pacienteVm.tela.value == "form"
            is Route.Pagamento -> pagamentoVm.tela.value == "form"
            is Route.Convenio -> convenioVm.tela.value == "form"
            is Route.HorarioDisponivel -> horarioVm.tela.value == "form"
            is Route.SalaAtendimento -> salaVm.tela.value == "form"
            is Route.TipoAtendimento -> tipoVm.tela.value == "form"
            is Route.BloqueioAgenda -> bloqueioVm.tela.value == "form"
            is Route.EvolucaoClinica -> evolucaoVm.tela.value == "form"
            is Route.Prontuario -> prontuarioVm.tela.value == "form"
            is Route.Recepcionista -> recepcionistaVm.tela.value == "form"
            is Route.Funcionario -> funcionarioVm.tela.value == "form"
            is Route.Psicologo -> psicologoVm.tela.value == "form"
            is Route.Agenda -> agendaVm.tela.value == "form"
            else -> false
        }
    }

    fun tratarVoltar() {
        if (estaEmFormulario()) {
            when (rotaAtual) {
                is Route.Paciente -> pacienteVm.tela.value = "list"
                is Route.Pagamento -> pagamentoVm.tela.value = "list"
                is Route.Convenio -> convenioVm.tela.value = "list"
                is Route.HorarioDisponivel -> horarioVm.tela.value = "list"
                is Route.SalaAtendimento -> salaVm.tela.value = "list"
                is Route.TipoAtendimento -> tipoVm.tela.value = "list"
                is Route.BloqueioAgenda -> bloqueioVm.tela.value = "list"
                is Route.EvolucaoClinica -> evolucaoVm.tela.value = "list"
                is Route.Prontuario -> prontuarioVm.tela.value = "list"
                is Route.Recepcionista -> recepcionistaVm.tela.value = "list"
                is Route.Funcionario -> funcionarioVm.tela.value = "list"
                is Route.Psicologo -> psicologoVm.tela.value = "list"
                is Route.Agenda -> agendaVm.tela.value = "list"
                else -> {}
            }
        } else {
            if (backstack.size > 1) {
                backstack.removeAt(backstack.lastIndex)
            } else {
                backstack.clear()
                backstack.add(Route.Dashboard)
            }
        }
    }

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
        MenuLateral(
            drawerState = drawerState,
            scope = scope,
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
                    "pagamento" -> Route.Pagamento
                    "funcionario" -> Route.Funcionario
                    "psicologo" -> Route.Psicologo
                    "recepcionista" -> Route.Recepcionista
                    "agenda" -> Route.Agenda
                    "login" -> Route.Login
                    else -> Route.Dashboard
                }

                if (novaRota == Route.Login) {
                    backstack.clear()
                    backstack.add(Route.Login)
                } else {
                    backstack.clear()
                    backstack.add(Route.Dashboard)
                    if (novaRota != Route.Dashboard) {
                        backstack.add(novaRota)
                    }
                }
            }
        ) {
            // Se estiver na rota de Login, exibe apenas a tela de login sem o Scaffold do Dashboard
            if (rotaAtual is Route.Login) {
                LoginScreen(
                    onNavigate = { rota ->
                        backstack.clear()
                        backstack.add(rota)
                    }
                )
            } else {
                Scaffold(
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
                                val mostrarVoltar = estaEmFormulario() || backstack.size > 1
                                if (mostrarVoltar) {
                                    IconButton(onClick = { tratarVoltar() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Voltar",
                                            tint = Color.White
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Abrir Menu Lateral",
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeClinica)
                        )
                    },
                    containerColor = Branco
                ) { contentPadding ->
                    // NavDisplay gerenciando as entradas do Navigation 3 com base no backstack
                    NavDisplay(
                        backStack = backstack,
                        onBack = { tratarVoltar() },
                        entryProvider = entryProvider {
                            entry<Route.Dashboard> {
                                DashboardTela(contentPadding)
                            }
                            entry<Route.Paciente> {
                                if (pacienteVm.tela.value == "form") {
                                    PacienteFormulario(contentPadding, pacienteVm)
                                } else {
                                    PacienteListagem(
                                        innerPadding = contentPadding,
                                        vm = pacienteVm,
                                        onNavegarParaProntuario = { paciente ->
                                            pacienteSelecionadoNome = paciente.nome
                                            prontuarioVm.paciente.value = paciente.nome
                                            prontuarioVm.atualizarFiltro(paciente.nome)
                                            prontuarioVm.tela.value = "list"
                                            backstack.add(Route.Prontuario)
                                        },
                                        onNavegarParaEvolucao = { paciente ->
                                            pacienteSelecionadoNome = paciente.nome
                                            evolucaoVm.prontuario.value = paciente.nome
                                            evolucaoVm.atualizarFiltro(paciente.nome)
                                            evolucaoVm.tela.value = "list"
                                            backstack.add(Route.EvolucaoClinica)
                                        }
                                    )
                                }
                            }
                            entry<Route.Pagamento> {
                                if (pagamentoVm.tela.value == "form") PagamentoFormulario(contentPadding, pagamentoVm)
                                else PagamentoListagem(contentPadding, pagamentoVm)
                            }
                            entry<Route.Convenio> {
                                if (convenioVm.tela.value == "form") ConvenioFormulario(contentPadding, convenioVm)
                                else ConvenioListagem(contentPadding, convenioVm)
                            }
                            entry<Route.HorarioDisponivel> {
                                if (horarioVm.tela.value == "form") HorarioDisponivelFormulario(contentPadding, horarioVm)
                                else HorarioDisponivelListagem(contentPadding, horarioVm)
                            }
                            entry<Route.SalaAtendimento> {
                                if (salaVm.tela.value == "form") SalaAtendimentoFormulario(contentPadding, salaVm)
                                else SalaAtendimentoListagem(contentPadding, salaVm)
                            }
                            entry<Route.TipoAtendimento> {
                                if (tipoVm.tela.value == "form") TipoAtendimentoFormulario(contentPadding, tipoVm)
                                else TipoAtendimentoListagem(contentPadding, tipoVm)
                            }
                            entry<Route.BloqueioAgenda> {
                                if (bloqueioVm.tela.value == "form") BloqueioAgendaFormulario(contentPadding, bloqueioVm)
                                else BloqueioAgendaListagem(contentPadding, bloqueioVm)
                            }
                            entry<Route.EvolucaoClinica> {
                                if (evolucaoVm.tela.value == "form") {
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
                                if (prontuarioVm.tela.value == "form") {
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
                                if (recepcionistaVm.tela.value == "form") RecepcionistaFormulario(contentPadding, recepcionistaVm)
                                else RecepcionistaListagem(contentPadding, recepcionistaVm)
                            }
                            entry<Route.Funcionario> {
                                if (funcionarioVm.tela.value == "form") FuncionarioFormulario(contentPadding, funcionarioVm)
                                else FuncionarioListagem(contentPadding, funcionarioVm)
                            }
                            entry<Route.Psicologo> {
                                if (psicologoVm.tela.value == "form") PsicologoFormulario(contentPadding, psicologoVm)
                                else PsicologoListagem(contentPadding, psicologoVm)
                            }
                            entry<Route.Agenda> {
                                if (agendaVm.tela.value == "form") AgendaFormulario(contentPadding, agendaVm)
                                else AgendaListagem(contentPadding, agendaVm)
                            }
                        }
                    )
                }
            }
        }
    }
}