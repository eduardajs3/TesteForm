package edu.front.psi.clin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.ui.componentes.MenuLateral
import edu.front.psi.clin.ui.componentes.NotificacaoFlutuante
import edu.front.psi.clin.ui.theme.PsiClinTheme
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.VerdeClinica

import edu.front.psi.clin.ui.screens.agenda.AgendaFormulario
import edu.front.psi.clin.ui.screens.bloqueioAgenda.BloqueioAgendaFormulario
import edu.front.psi.clin.ui.screens.convenio.ConvenioFormulario
import edu.front.psi.clin.ui.screens.evolucaoClinica.EvolucaoClinicaFormulario
import edu.front.psi.clin.ui.screens.funcionario.FuncionarioFormulario
import edu.front.psi.clin.ui.screens.horarioDisponivel.HorarioDisponivelFormulario
import edu.front.psi.clin.ui.screens.paciente.PacienteFormulario
import edu.front.psi.clin.ui.screens.pagamento.PagamentoFormulario
import edu.front.psi.clin.ui.screens.prontuario.ProntuarioFormulario
import edu.front.psi.clin.ui.screens.psicologo.PsicologoFormulario
import edu.front.psi.clin.ui.screens.recepcionista.RecepcionistaFormulario
import edu.front.psi.clin.ui.screens.salaAtendimento.SalaAtendimentoFormulario
import edu.front.psi.clin.ui.screens.tipoAtendimento.TipoAtendimentoFormulario

// Imports das telas de Autenticação, Listagem e Dashboard
import com.psiclin.front.ui.screens.auth.LoginScreen
import com.psiclin.front.ui.screens.auth.RecuperarSenhaScreen
import com.psiclin.front.ui.screens.auth.RegistroScreen
import edu.front.psi.clin.ui.screens.agenda.AgendaListagem
import edu.front.psi.clin.ui.screens.bloqueioAgenda.BloqueioAgendaListagem
import edu.front.psi.clin.ui.screens.convenio.ConvenioListagem
import edu.front.psi.clin.ui.screens.DashboardTela
import edu.front.psi.clin.ui.screens.evolucaoClinica.EvolucaoClinicaListagem
import edu.front.psi.clin.ui.screens.funcionario.FuncionarioListagem
import edu.front.psi.clin.ui.screens.horarioDisponivel.HorarioDisponivelListagem
import edu.front.psi.clin.ui.screens.paciente.PacienteListagem
import edu.front.psi.clin.ui.screens.pagamento.PagamentoListagem
import edu.front.psi.clin.ui.screens.prontuario.ProntuarioListagem
import edu.front.psi.clin.ui.screens.psicologo.PsicologoListagem
import edu.front.psi.clin.ui.screens.recepcionista.RecepcionistaListagem
import edu.front.psi.clin.ui.screens.salaAtendimento.SalaAtendimentoListagem
import edu.front.psi.clin.ui.screens.tipoAtendimento.TipoAtendimentoListagem

// Imports das ViewModels
import edu.front.psi.clin.ui.viewmodel.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val scope = rememberCoroutineScope()

    val principalVm: PrincipalViewModel = viewModel(
        factory = viewModelFactory { initializer { PrincipalViewModel(scope) } }
    )

    var pacienteSelecionadoNome by remember { mutableStateOf("") }

    val pacienteVm: PacienteViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                PacienteViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val pagamentoVm: PagamentoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                PagamentoViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val convenioVm: ConvenioViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                ConvenioViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val funcionarioVm: FuncionarioViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                FuncionarioViewModel(
                    mandarMensagem = { msg-> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )
    val horarioVm: HorarioDisponivelViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                HorarioDisponivelViewModel(mandarMensagem = { msg-> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val salaVm: SalaAtendimentoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                SalaAtendimentoViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val tipoVm: TipoAtendimentoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                TipoAtendimentoViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val bloqueioVm: BloqueioAgendaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                BloqueioAgendaViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val evolucaoVm: EvolucaoClinicaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                EvolucaoClinicaViewModel(mandarMensagem = { msg-> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val prontuarioVm: ProntuarioViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                ProntuarioViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val recepcionistaVm: RecepcionistaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                RecepcionistaViewModel(mandarMensagem = { msg  -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val psicologoVm: PsicologoViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                PsicologoViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val agendaVm: AgendaViewModel = viewModel(
        factory = viewModelFactory { 
            initializer { 
                AgendaViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }) 
            } 
        }
    )
    val dashboardVm: DashboardViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                DashboardViewModel(mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) })
            }
        }
    )

    val rotaAtual = principalVm.backstack.lastOrNull() ?: Route.Login

    val stringTelaAtual = when (rotaAtual) {
        Route.Dashboard -> "dashboard"
        Route.Paciente -> "paciente"
        Route.Pagamento -> "pagamento"
        Route.Convenio -> "convenio"
        Route.HorarioDisponivel -> "horario"
        Route.SalaAtendimento -> "sala"
        Route.TipoAtendimento -> "tipo"
        Route.BloqueioAgenda -> "bloqueio"
        Route.EvolucaoClinica -> "evolucao"
        Route.Prontuario -> "prontuario"
        Route.Recepcionista -> "recepcionista"
        Route.Funcionario -> "funcionario"
        Route.Psicologo -> "psicologo"
        Route.Agenda -> "agenda"
        else -> "login"
    }

    PsiClinTheme {
        Box(modifier = Modifier.fillMaxSize()) {
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
                // Tratamento das telas de autenticação que não possuem Scaffold com menu lateral
                when (rotaAtual) {
                    Route.Login -> {
                        LoginScreen(
                            onNavigate = { rota ->
                                principalVm.backstack.clear()
                                principalVm.backstack.add(rota)
                            }
                        )
                    }
                    Route.Cadastro -> {
                        RegistroScreen(
                            onNavigate = { rota ->
                                principalVm.backstack.clear()
                                principalVm.backstack.add(rota)
                            }
                        )
                    }
                    Route.RecuperarSenha -> {
                        RecuperarSenhaScreen(
                            onNavigate = { rota ->
                                principalVm.backstack.clear()
                                principalVm.backstack.add(rota)
                            }
                        )
                    }
                    else -> {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text(
                                            text = when (rotaAtual) {
                                                Route.Dashboard -> "Dashboard"
                                                Route.Paciente -> "Pacientes"
                                                Route.Pagamento -> "Pagamentos"
                                                Route.Convenio -> "Convênios"
                                                Route.HorarioDisponivel -> "Horários Disponíveis"
                                                Route.SalaAtendimento -> "Salas de Atendimento"
                                                Route.TipoAtendimento -> "Tipos de Atendimento"
                                                Route.BloqueioAgenda -> "Bloqueios"
                                                Route.EvolucaoClinica -> "Evoluções"
                                                Route.Prontuario -> "Prontuários"
                                                Route.Recepcionista -> "Recepcionistas"
                                                Route.Funcionario -> "Funcionários"
                                                Route.Psicologo -> "Psicólogos"
                                                Route.Agenda -> "Agendas"
                                                else -> "PsiClin"
                                            },
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Branco
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { principalVm.abrirGaveta() }) {
                                            Icon(
                                                imageVector = Icons.Filled.Menu,
                                                contentDescription = "Abrir Menu Lateral",
                                                tint = Branco
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
                                    entry<Route.Login> {
                                        LoginScreen(onNavigate = { rota ->
                                            principalVm.backstack.clear()
                                            principalVm.backstack.add(rota)
                                        })
                                    }
                                    entry<Route.Cadastro> {
                                        RegistroScreen(onNavigate = { rota ->
                                            principalVm.backstack.clear()
                                            principalVm.backstack.add(rota)
                                        })
                                    }
                                    entry<Route.RecuperarSenha> {
                                        RecuperarSenhaScreen(
                                            onNavigate = { rota ->
                                                principalVm.backstack.clear()
                                                principalVm.backstack.add(rota)
                                            }
                                        )
                                    }
                                    entry<Route.Dashboard> {
                                        DashboardTela(
                                            innerPadding = contentPadding,
                                            vm = dashboardVm,
                                            onNavegar = { rota ->
                                                if (rota == Route.Paciente) {
                                                    pacienteVm.novoPaciente()
                                                }
                                                principalVm.backstack.clear()
                                                principalVm.backstack.add(Route.Dashboard)
                                                if (rota != Route.Dashboard) {
                                                    principalVm.backstack.add(rota)
                                                }
                                            }
                                        )
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
                                    entry<Route.Convenio> {
                                        val subRota = convenioVm.backStack.lastOrNull() ?: ConvenioRotas.Listagem
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
                                        val subRota = evolucaoVm.backStack.lastOrNull() ?: EvolucaoClinicaRotas.Listagem
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

            NotificacaoFlutuante(mensagem = principalVm.mensagemNotificacao)
        }
    }
}