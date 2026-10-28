package edu.front.psi.clin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.front.psi.clin.ui.components.MenuLateral

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
import edu.front.psi.clin.ui.screen.AgendaListagem
import edu.front.psi.clin.ui.screen.BloqueioAgendaListagem
import edu.front.psi.clin.ui.screen.ConvenioListagem
import edu.front.psi.clin.ui.screen.DashboardTela
import edu.front.psi.clin.ui.screen.EvolucaoClinicaListagem
import edu.front.psi.clin.ui.screen.FuncionarioListagem
import edu.front.psi.clin.ui.screen.HorarioDisponivelListagem
import edu.front.psi.clin.ui.screen.PacienteListagem
import edu.front.psi.clin.ui.screen.PagamentoListagem
import edu.front.psi.clin.ui.screen.ProntuarioListagem
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
    val tela = remember { mutableStateOf("dashboard") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ViewModels
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

    fun tratarVoltar() {
        val estaEmForm = when (tela.value) {
            "paciente" -> pacienteVm.tela.value == "form"
            "pagamento" -> pagamentoVm.tela.value == "form"
            "convenio" -> convenioVm.tela.value == "form"
            "horario" -> horarioVm.tela.value == "form"
            "sala" -> salaVm.tela.value == "form"
            "tipo" -> tipoVm.tela.value == "form"
            "bloqueio" -> bloqueioVm.tela.value == "form"
            "evolucao" -> evolucaoVm.tela.value == "form"
            "prontuario" -> prontuarioVm.tela.value == "form"
            "recepcionista" -> recepcionistaVm.tela.value == "form"
            "funcionario" -> funcionarioVm.tela.value == "form"
            "psicologo" -> psicologoVm.tela.value == "form"
            "agenda" -> agendaVm.tela.value == "form"
            else -> false
        }

        if (estaEmForm) {
            when (tela.value) {
                "paciente" -> pacienteVm.tela.value = "list"
                "pagamento" -> pagamentoVm.tela.value = "list"
                "convenio" -> convenioVm.tela.value = "list"
                "horario" -> horarioVm.tela.value = "list"
                "sala" -> salaVm.tela.value = "list"
                "tipo" -> tipoVm.tela.value = "list"
                "bloqueio" -> bloqueioVm.tela.value = "list"
                "evolucao" -> evolucaoVm.tela.value = "list"
                "prontuario" -> prontuarioVm.tela.value = "list"
                "recepcionista" -> recepcionistaVm.tela.value = "list"
                "funcionario" -> funcionarioVm.tela.value = "list"
                "psicologo" -> psicologoVm.tela.value = "list"
                "agenda" -> agendaVm.tela.value = "list"
            }
        } else {
            tela.value = "dashboard"
        }
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
            telaAtual = tela.value,
            onNavigate = { novaTela -> tela.value = novaTela }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (tela.value) {
                                    "dashboard" -> "Dashboard"
                                    "paciente" -> "Pacientes"
                                    "pagamento" -> "Pagamentos"
                                    "convenio" -> "Convênios"
                                    "horario" -> "Horários Disponíveis"
                                    "sala" -> "Salas de Atendimento"
                                    "tipo" -> "Tipos de Atendimento"
                                    "bloqueio" -> "Bloqueios"
                                    "evolucao" -> "Evoluções"
                                    "prontuario" -> "Prontuários"
                                    "recepcionista" -> "Recepcionistas"
                                    "funcionario" -> "Funcionários"
                                    "psicologo" -> "Psicólogos"
                                    "agenda" -> "Agendas"
                                    else -> "PsiClin"
                                },
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            val estaEmForm = when (tela.value) {
                                "paciente" -> pacienteVm.tela.value == "form"
                                "pagamento" -> pagamentoVm.tela.value == "form"
                                "convenio" -> convenioVm.tela.value == "form"
                                "horario" -> horarioVm.tela.value == "form"
                                "sala" -> salaVm.tela.value == "form"
                                "tipo" -> tipoVm.tela.value == "form"
                                "bloqueio" -> bloqueioVm.tela.value == "form"
                                "evolucao" -> evolucaoVm.tela.value == "form"
                                "prontuario" -> prontuarioVm.tela.value == "form"
                                "recepcionista" -> recepcionistaVm.tela.value == "form"
                                "funcionario" -> funcionarioVm.tela.value == "form"
                                "psicologo" -> psicologoVm.tela.value == "form"
                                "agenda" -> agendaVm.tela.value == "form"
                                else -> false
                            }

                            if (estaEmForm) {
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
                when (tela.value) {
                    "dashboard" -> DashboardTela(contentPadding)
                    "paciente" -> if (pacienteVm.tela.value == "form") PacienteFormulario(contentPadding, pacienteVm) else PacienteListagem(contentPadding, pacienteVm)
                    "pagamento" -> if (pagamentoVm.tela.value == "form") PagamentoFormulario(contentPadding, pagamentoVm) else PagamentoListagem(contentPadding, pagamentoVm)
                    "convenio" -> if (convenioVm.tela.value == "form") ConvenioFormulario(contentPadding, convenioVm) else ConvenioListagem(contentPadding, convenioVm)
                    "horario" -> if (horarioVm.tela.value == "form") HorarioDisponivelFormulario(contentPadding, horarioVm) else HorarioDisponivelListagem(contentPadding, horarioVm)
                    "sala" -> if (salaVm.tela.value == "form") SalaAtendimentoFormulario(contentPadding, salaVm) else SalaAtendimentoListagem(contentPadding, salaVm)
                    "tipo" -> if (tipoVm.tela.value == "form") TipoAtendimentoFormulario(contentPadding, tipoVm) else TipoAtendimentoListagem(contentPadding, tipoVm)
                    "bloqueio" -> if (bloqueioVm.tela.value == "form") BloqueioAgendaFormulario(contentPadding, bloqueioVm) else BloqueioAgendaListagem(contentPadding, bloqueioVm)
                    "evolucao" -> if (evolucaoVm.tela.value == "form") EvolucaoClinicaFormulario(contentPadding, evolucaoVm) else EvolucaoClinicaListagem(contentPadding, evolucaoVm)
                    "prontuario" -> if (prontuarioVm.tela.value == "form") ProntuarioFormulario(contentPadding, prontuarioVm) else ProntuarioListagem(contentPadding, prontuarioVm)
                    "recepcionista" -> if (recepcionistaVm.tela.value == "form") RecepcionistaFormulario(contentPadding, recepcionistaVm) else RecepcionistaListagem(contentPadding, recepcionistaVm)
                    "funcionario" -> if (funcionarioVm.tela.value == "form") FuncionarioFormulario(contentPadding, funcionarioVm) else FuncionarioListagem(contentPadding, funcionarioVm)
                    "psicologo" -> if (psicologoVm.tela.value == "form") PsicologoFormulario(contentPadding, psicologoVm) else PsicologoListagem(contentPadding, psicologoVm)
                    "agenda" -> if (agendaVm.tela.value == "form") AgendaFormulario(contentPadding, agendaVm) else AgendaListagem(contentPadding, agendaVm)
                }
            }
        }
    }
}