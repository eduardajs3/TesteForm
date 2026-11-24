package edu.front.psi.clin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.psiclin.front.ui.screens.auth.LoginScreen
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.navigation.ConvenioRotas
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.navigation.FuncionarioRotas
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.navigation.PacienteRotas
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.navigation.PsicologoRotas
import edu.front.psi.clin.navigation.RecepcionistaRotas
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.navigation.SalaAtendimentoRotas
import edu.front.psi.clin.navigation.TipoAtendimentoRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.componentes.MenuLateral
import edu.front.psi.clin.ui.componentes.NotificacaoFlutuante
import edu.front.psi.clin.ui.screens.DashboardTela
import edu.front.psi.clin.ui.screens.agenda.AgendaScreen
import edu.front.psi.clin.ui.screens.bloqueioAgenda.BloqueioAgendaScreen
import edu.front.psi.clin.ui.screens.convenio.ConvenioScreen
import edu.front.psi.clin.ui.screens.evolucaoClinica.EvolucaoClinicaScreen
import edu.front.psi.clin.ui.screens.funcionario.FuncionarioScreen
import edu.front.psi.clin.ui.screens.horarioDisponivel.HorarioDisponivelScreen
import edu.front.psi.clin.ui.screens.paciente.PacienteScreen
import edu.front.psi.clin.ui.screens.pagamento.PagamentoScreen
import edu.front.psi.clin.ui.screens.prontuario.ProntuarioScreen
import edu.front.psi.clin.ui.screens.psicologo.PsicologoScreen
import edu.front.psi.clin.ui.screens.recepcionista.RecepcionistaScreen
import edu.front.psi.clin.ui.screens.salaAtendimento.SalaAtendimentoScreen
import edu.front.psi.clin.ui.screens.tipoAtendimento.TipoAtendimentoScreen
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.PsiClinTheme
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val scope = rememberCoroutineScope()

    val principalVm: PrincipalViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PrincipalViewModel(scope) }
        }
    )

    if (!principalVm.autenticado) {
        PsiClinTheme {
            LoginScreen(
                onNavigate = { rota ->
                    principalVm.backstack.clear()
                    principalVm.backstack.add(rota)
                },
                onLogin = principalVm::login,
                isLoading = principalVm.autenticando,
                errorMessage = principalVm.erroLogin
            )
        }
        return
    }

    var pacienteSelecionadoNome by remember { mutableStateOf("") }

    val pacienteVm: PacienteViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                PacienteViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val pagamentoVm: PagamentoViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                PagamentoViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val convenioVm: ConvenioViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ConvenioViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val funcionarioVm: FuncionarioViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                FuncionarioViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val horarioVm: HorarioDisponivelViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HorarioDisponivelViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val salaVm: SalaAtendimentoViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                SalaAtendimentoViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val tipoVm: TipoAtendimentoViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                TipoAtendimentoViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val bloqueioVm: BloqueioAgendaViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                BloqueioAgendaViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val evolucaoVm: EvolucaoClinicaViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                EvolucaoClinicaViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val prontuarioVm: ProntuarioViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ProntuarioViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val recepcionistaVm: RecepcionistaViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                RecepcionistaViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val psicologoVm: PsicologoViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                PsicologoViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val agendaVm: AgendaViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AgendaViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val dashboardVm: DashboardViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                DashboardViewModel(
                    mandarMensagem = { msg -> principalVm.mostrarNotificacao(msg) }
                )
            }
        }
    )

    val rotaAtual = principalVm.backstack.lastOrNull() ?: Route.Dashboard

    val voltarParaListagemInterna: () -> Boolean = {
        when (rotaAtual) {
            Route.Paciente -> {
                if (pacienteVm.backStack.lastOrNull() == PacienteRotas.Formulario) {
                    pacienteVm.backStack.clear()
                    pacienteVm.backStack.add(PacienteRotas.Listagem)
                    true
                } else false
            }

            Route.Pagamento -> {
                if (pagamentoVm.backStack.lastOrNull() == PagamentoRotas.Formulario) {
                    pagamentoVm.backStack.clear()
                    pagamentoVm.backStack.add(PagamentoRotas.Listagem)
                    true
                } else false
            }

            Route.Convenio -> {
                if (convenioVm.backStack.lastOrNull() == ConvenioRotas.Formulario) {
                    convenioVm.backStack.clear()
                    convenioVm.backStack.add(ConvenioRotas.Listagem)
                    true
                } else false
            }

            Route.HorarioDisponivel -> {
                if (horarioVm.backStack.lastOrNull() == HorarioDisponivelRotas.Formulario) {
                    horarioVm.backStack.clear()
                    horarioVm.backStack.add(HorarioDisponivelRotas.Listagem)
                    true
                } else false
            }

            Route.SalaAtendimento -> {
                if (salaVm.backStack.lastOrNull() == SalaAtendimentoRotas.Formulario) {
                    salaVm.backStack.clear()
                    salaVm.backStack.add(SalaAtendimentoRotas.Listagem)
                    true
                } else false
            }

            Route.TipoAtendimento -> {
                if (tipoVm.backStack.lastOrNull() == TipoAtendimentoRotas.Formulario) {
                    tipoVm.backStack.clear()
                    tipoVm.backStack.add(TipoAtendimentoRotas.Listagem)
                    true
                } else false
            }

            Route.BloqueioAgenda -> {
                if (bloqueioVm.backStack.lastOrNull() == BloqueioAgendaRotas.Formulario) {
                    bloqueioVm.backStack.clear()
                    bloqueioVm.backStack.add(BloqueioAgendaRotas.Listagem)
                    true
                } else false
            }

            Route.EvolucaoClinica -> {
                if (evolucaoVm.backStack.lastOrNull() == EvolucaoClinicaRotas.Formulario) {
                    evolucaoVm.backStack.clear()
                    evolucaoVm.backStack.add(EvolucaoClinicaRotas.Listagem)
                    true
                } else false
            }

            Route.Prontuario -> {
                if (prontuarioVm.backStack.lastOrNull() == ProntuarioRotas.Formulario) {
                    prontuarioVm.backStack.clear()
                    prontuarioVm.backStack.add(ProntuarioRotas.Listagem)
                    true
                } else false
            }

            Route.Recepcionista -> {
                if (recepcionistaVm.backStack.lastOrNull() == RecepcionistaRotas.Formulario) {
                    recepcionistaVm.backStack.clear()
                    recepcionistaVm.backStack.add(RecepcionistaRotas.Listagem)
                    true
                } else false
            }

            Route.Funcionario -> {
                if (funcionarioVm.backStack.lastOrNull() == FuncionarioRotas.Formulario) {
                    funcionarioVm.backStack.clear()
                    funcionarioVm.backStack.add(FuncionarioRotas.Listagem)
                    true
                } else false
            }

            Route.Psicologo -> {
                if (psicologoVm.backStack.lastOrNull() == PsicologoRotas.Formulario) {
                    psicologoVm.backStack.clear()
                    psicologoVm.backStack.add(PsicologoRotas.Listagem)
                    true
                } else false
            }

            Route.Agenda -> {
                if (agendaVm.backStack.lastOrNull() == AgendaRotas.Formulario) {
                    agendaVm.backStack.clear()
                    agendaVm.backStack.add(AgendaRotas.Listagem)
                    true
                } else false
            }

            else -> false
        }
    }

    fun telaDaRota(rota: Route) = when (rota) {
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
        else -> "dashboard"
    }

    val stringTelaAtual = telaDaRota(rotaAtual)
    val rotaPermitida = PermissoesAcesso.podeAcessarTela(principalVm.role, stringTelaAtual)

    val voltarParaModuloAnterior: () -> Unit = {
        if (principalVm.backstack.size > 1) {
            principalVm.backstack.removeAt(principalVm.backstack.lastIndex)
        }
    }

    val conteudoInterno: @Composable (PaddingValues) -> Unit = { contentPadding ->
        if (!rotaPermitida) {
            LaunchedEffect(rotaAtual, principalVm.role) {
                principalVm.mostrarNotificacao("Acesso negado para este perfil.")
                principalVm.backstack.clear()
                principalVm.backstack.add(Route.Dashboard)
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Acesso não autorizado")
            }
        } else {
            NavDisplay(
                backStack = principalVm.backstack,
                onBack = {
                    if (!voltarParaListagemInterna() && principalVm.backstack.size > 1) {
                        principalVm.backstack.removeAt(principalVm.backstack.lastIndex)
                    }
                },
                entryProvider = entryProvider {
                    entry<Route.Dashboard> {
                        DashboardTela(
                            innerPadding = contentPadding,
                            vm = dashboardVm,
                            podeCriarAgendamento = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.AGENDAS
                            ),
                            podeCriarPaciente = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.PACIENTES
                            ),
                            podeAcessarFinanceiro = PermissoesAcesso.podeAcessarTela(
                                principalVm.role,
                                "pagamento"
                            ),
                            podeConfirmarAgendamento = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.AGENDAS
                            ),
                            onNavegar = { rota ->
                                val recursoParaCriar = when (rota) {
                                    Route.Paciente -> RecursoSistema.PACIENTES
                                    else -> null
                                }
                                val podeAcessarRota = PermissoesAcesso.podeAcessarTela(
                                    principalVm.role,
                                    telaDaRota(rota)
                                )
                                val podeCriarRecurso = recursoParaCriar?.let {
                                    PermissoesAcesso.podeGerenciar(principalVm.role, it)
                                } ?: true
                                if (podeAcessarRota && podeCriarRecurso) {
                                    if (rota == Route.Paciente) {
                                        pacienteVm.novoPaciente()
                                    } else if (rota == Route.Agenda) {
                                        agendaVm.backStack.clear()
                                        agendaVm.backStack.add(AgendaRotas.Listagem)
                                    }

                                    if (principalVm.backstack.lastOrNull() != rota) {
                                        principalVm.backstack.add(rota)
                                    }
                                } else {
                                    principalVm.mostrarNotificacao("Acesso negado para este perfil.")
                                }
                            },
                            onAgendarConsulta = {
                                if (PermissoesAcesso.podeGerenciar(principalVm.role, RecursoSistema.AGENDAS)) {
                                    agendaVm.novoAgendamento()
                                    if (principalVm.backstack.lastOrNull() != Route.Agenda) {
                                        principalVm.backstack.add(Route.Agenda)
                                    }
                                } else {
                                    principalVm.mostrarNotificacao("Acesso negado para este perfil.")
                                }
                            }
                        )
                    }

                    entry<Route.Paciente> {
                        PacienteScreen(
                            innerPadding = contentPadding,
                            vm = pacienteVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.PACIENTES
                            ),
                            onNavegarParaProntuario = { paciente ->
                                if (PermissoesAcesso.podeAcessar(principalVm.role, RecursoSistema.PRONTUARIOS)) {
                                    pacienteSelecionadoNome = paciente.nome
                                    prontuarioVm.selecionarPaciente(paciente)
                                    prontuarioVm.backStack.clear()
                                    prontuarioVm.backStack.add(ProntuarioRotas.Listagem)
                                    principalVm.backstack.add(Route.Prontuario)
                                }
                            },
                            onNavegarParaEvolucao = { paciente ->
                                if (PermissoesAcesso.podeAcessar(principalVm.role, RecursoSistema.EVOLUCOES_CLINICAS)) {
                                    pacienteSelecionadoNome = paciente.nome
                                    evolucaoVm.selecionarPaciente(paciente)
                                    evolucaoVm.backStack.clear()
                                    evolucaoVm.backStack.add(EvolucaoClinicaRotas.Listagem)
                                    principalVm.backstack.add(Route.EvolucaoClinica)
                                }
                            }
                        )
                    }

                    entry<Route.Pagamento> {
                        PagamentoScreen(
                            innerPadding = contentPadding,
                            vm = pagamentoVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.PAGAMENTOS
                            )
                        )
                    }

                    entry<Route.Convenio> {
                        ConvenioScreen(
                            innerPadding = contentPadding,
                            vm = convenioVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.CONVENIOS
                            )
                        )
                    }

                    entry<Route.HorarioDisponivel> {
                        HorarioDisponivelScreen(
                            innerPadding = contentPadding,
                            vm = horarioVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.HORARIOS_DISPONIVEIS
                            )
                        )
                    }

                    entry<Route.SalaAtendimento> {
                        SalaAtendimentoScreen(
                            innerPadding = contentPadding,
                            vm = salaVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.SALAS_ATENDIMENTO
                            )
                        )
                    }

                    entry<Route.TipoAtendimento> {
                        TipoAtendimentoScreen(
                            innerPadding = contentPadding,
                            vm = tipoVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.TIPOS_ATENDIMENTO
                            )
                        )
                    }

                    entry<Route.BloqueioAgenda> {
                        BloqueioAgendaScreen(
                            innerPadding = contentPadding,
                            vm = bloqueioVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.BLOQUEIOS_AGENDA
                            )
                        )
                    }

                    entry<Route.EvolucaoClinica> {
                        EvolucaoClinicaScreen(
                            innerPadding = contentPadding,
                            vm = evolucaoVm,
                            onVoltar = voltarParaModuloAnterior,
                            nomePaciente = pacienteSelecionadoNome.ifEmpty { "Paciente" }
                        )
                    }

                    entry<Route.Prontuario> {
                        ProntuarioScreen(
                            innerPadding = contentPadding,
                            vm = prontuarioVm,
                            onVoltar = voltarParaModuloAnterior,
                            nomePaciente = pacienteSelecionadoNome.ifEmpty { "Paciente" }
                        )
                    }

                    entry<Route.Recepcionista> {
                        RecepcionistaScreen(
                            innerPadding = contentPadding,
                            vm = recepcionistaVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.RECEPCIONISTAS
                            )
                        )
                    }

                    entry<Route.Funcionario> {
                        FuncionarioScreen(
                            innerPadding = contentPadding,
                            vm = funcionarioVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.FUNCIONARIOS
                            )
                        )
                    }

                    entry<Route.Psicologo> {
                        PsicologoScreen(
                            innerPadding = contentPadding,
                            vm = psicologoVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.PSICOLOGOS
                            )
                        )
                    }

                    entry<Route.Agenda> {
                        AgendaScreen(
                            innerPadding = contentPadding,
                            vm = agendaVm,
                            podeGerenciar = PermissoesAcesso.podeGerenciar(
                                principalVm.role,
                                RecursoSistema.AGENDAS
                            )
                        )
                    }
                }
            )
        }
    }

    PsiClinTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MenuLateral(
                viewModel = principalVm,
                telaAtual = if (rotaPermitida) stringTelaAtual else "dashboard",
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
                        principalVm.logout()
                    } else if (!PermissoesAcesso.podeAcessarTela(principalVm.role, telaDaRota(novaRota))) {
                        principalVm.mostrarNotificacao("Acesso negado para este perfil.")
                    } else if (novaRota != rotaAtual) {
                        principalVm.backstack.clear()
                        principalVm.backstack.add(Route.Dashboard)

                        if (novaRota != Route.Dashboard) {
                            principalVm.backstack.add(novaRota)
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "PsiClin",
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
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = VerdeClinica
                            )
                        )
                    },
                    containerColor = Branco,
                    content = conteudoInterno
                )
            }

            NotificacaoFlutuante(
                mensagem = principalVm.mensagemNotificacao
            )
        }
    }
}