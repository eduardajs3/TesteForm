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
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.security.PermissoesAcesso
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
    val rotaPermitida = PermissoesAcesso.podeAcessarRota(principalVm.role, rotaAtual)

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
                onBack = principalVm::voltar,
                entryProvider = entryProvider {
                    entry<Route.Dashboard> {
                        DashboardTela(
                            innerPadding = contentPadding,
                            vm = dashboardVm,
                            onNavegar = { rota ->
                                if (rota == Route.Paciente) {
                                    pacienteVm.novoPaciente()
                                } else if (rota == Route.Agenda) {
                                    agendaVm.backStack.clear()
                                    agendaVm.backStack.add(AgendaRotas.Listagem)
                                }

                                if (principalVm.backstack.lastOrNull() != rota) {
                                    principalVm.backstack.add(rota)
                                }
                            },
                            onAgendarConsulta = {
                                agendaVm.novoAgendamento()
                                if (principalVm.backstack.lastOrNull() != Route.Agenda) {
                                    principalVm.backstack.add(Route.Agenda)
                                }
                            }
                        )
                    }

                    entry<Route.Paciente> {
                        PacienteScreen(
                            innerPadding = contentPadding,
                            vm = pacienteVm,
                            onNavegarParaProntuario = { paciente ->
                                pacienteSelecionadoNome = paciente.nome
                                prontuarioVm.selecionarPaciente(paciente)
                                prontuarioVm.backStack.clear()
                                prontuarioVm.backStack.add(ProntuarioRotas.Listagem)
                                principalVm.backstack.add(Route.Prontuario)
                            },
                            onNavegarParaEvolucao = { paciente ->
                                pacienteSelecionadoNome = paciente.nome
                                evolucaoVm.selecionarPaciente(paciente)
                                evolucaoVm.backStack.clear()
                                evolucaoVm.backStack.add(EvolucaoClinicaRotas.Listagem)
                                principalVm.backstack.add(Route.EvolucaoClinica)
                            }
                        )
                    }

                    entry<Route.Pagamento> {
                        PagamentoScreen(
                            innerPadding = contentPadding,
                            vm = pagamentoVm
                        )
                    }

                    entry<Route.Convenio> {
                        ConvenioScreen(
                            innerPadding = contentPadding,
                            vm = convenioVm
                        )
                    }

                    entry<Route.HorarioDisponivel> {
                        HorarioDisponivelScreen(
                            innerPadding = contentPadding,
                            vm = horarioVm
                        )
                    }

                    entry<Route.SalaAtendimento> {
                        SalaAtendimentoScreen(
                            innerPadding = contentPadding,
                            vm = salaVm
                        )
                    }

                    entry<Route.TipoAtendimento> {
                        TipoAtendimentoScreen(
                            innerPadding = contentPadding,
                            vm = tipoVm
                        )
                    }

                    entry<Route.BloqueioAgenda> {
                        BloqueioAgendaScreen(
                            innerPadding = contentPadding,
                            vm = bloqueioVm
                        )
                    }

                    entry<Route.EvolucaoClinica> {
                        EvolucaoClinicaScreen(
                            innerPadding = contentPadding,
                            vm = evolucaoVm,
                            onVoltar = principalVm::voltar,
                            nomePaciente = pacienteSelecionadoNome.ifEmpty { "Paciente" }
                        )
                    }

                    entry<Route.Prontuario> {
                        ProntuarioScreen(
                            innerPadding = contentPadding,
                            vm = prontuarioVm,
                            onVoltar = principalVm::voltar,
                            nomePaciente = pacienteSelecionadoNome.ifEmpty { "Paciente" }
                        )
                    }

                    entry<Route.Recepcionista> {
                        RecepcionistaScreen(
                            innerPadding = contentPadding,
                            vm = recepcionistaVm
                        )
                    }

                    entry<Route.Funcionario> {
                        FuncionarioScreen(
                            innerPadding = contentPadding,
                            vm = funcionarioVm
                        )
                    }

                    entry<Route.Psicologo> {
                        PsicologoScreen(
                            innerPadding = contentPadding,
                            vm = psicologoVm
                        )
                    }

                    entry<Route.Agenda> {
                        AgendaScreen(
                            innerPadding = contentPadding,
                            vm = agendaVm
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
                telaAtual = if (rotaPermitida) rotaAtual else Route.Dashboard,
                onNavigate = { novaRota ->
                    if (novaRota == Route.Login) {
                        principalVm.logout()
                    } else if (!PermissoesAcesso.podeAcessarRota(principalVm.role, novaRota)) {
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