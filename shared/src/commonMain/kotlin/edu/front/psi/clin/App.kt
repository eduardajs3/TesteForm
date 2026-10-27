package edu.front.psi.clin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.front.psi.clin.ui.screen.ConvenioListagem
import edu.front.psi.clin.ui.screen.HorarioDisponivelListagem
import edu.front.psi.clin.ui.screen.PacienteListagem
import edu.front.psi.clin.ui.screen.PagamentoListagem
import edu.front.psi.clin.ui.screen.SalaAtendimentoListagem
import edu.front.psi.clin.ui.screen.TipoAtendimentoListagem
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel
import edu.front.psi.clin.ui.viewmodel.TipoAtendimentoViewModel

private val Branco = Color.White
val CremeCampo = Color(0xFFF6F3EC)
val VerdeClinica = Color(0xFF3B5760)

private data class MenuItem(
    val nome: String,
    val icone: String,
    val tela: String
)

@Composable
private fun MenuCard(
    item: MenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
            .background(CremeCampo, RoundedCornerShape(12.dp))
            .height(70.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.icone == "pessoa") {
                Icon(imageVector = Icons.Filled.Person, contentDescription = item.nome, tint = VerdeClinica)
            } else if (item.icone == "agenda") {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = item.nome, tint = VerdeClinica)
            } else {
                Icon(imageVector = Icons.Filled.Description, contentDescription = item.nome, tint = VerdeClinica)
            }

            Text(
                text = item.nome,
                color = VerdeClinica,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )

            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = "Abrir", tint = VerdeClinica)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val tela = remember { mutableStateOf("menu") }
    
    // Instanciando o ViewModel do Paciente (seguindo o padrão do professor)
    val pacienteVm: PacienteViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PacienteViewModel() }
        }
    )
    val pagamentoVm: PagamentoViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PagamentoViewModel() }
        }
    )
    val convenioVm: ConvenioViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ConvenioViewModel() }
        }
    )
    val horarioVm: HorarioDisponivelViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HorarioDisponivelViewModel() }
        }
    )
    val salaVm: SalaAtendimentoViewModel = viewModel(
        factory = viewModelFactory {
            initializer { SalaAtendimentoViewModel() }
        }
    )
    val tipoVm: TipoAtendimentoViewModel = viewModel(
        factory = viewModelFactory {
            initializer { TipoAtendimentoViewModel() }
        }
    )
    val filtroPaciente = pacienteVm.filtro.collectAsState()
    val filtroPagamento = pagamentoVm.filtro.collectAsState()
    val filtroConvenio = convenioVm.filtro.collectAsState()
    val filtroHorario = horarioVm.filtro.collectAsState()
    val filtroSala = salaVm.filtro.collectAsState()
    val filtroTipo = tipoVm.filtro.collectAsState()
    val telaComFiltro = tela.value in setOf("paciente", "pagamento", "convenio", "horario", "sala", "tipo")

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = VerdeClinica,
            onPrimary = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (telaComFiltro) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                            ) {
                                Text(
                                    when (tela.value) {
                                        "paciente" -> "Pacientes"
                                        "pagamento" -> "Pagamentos"
                                        "convenio" -> "Convênios"
                                        "horario" -> "Horários disponíveis"
                                        "sala" -> "Salas de atendimento"
                                        else -> "Tipos de atendimento"
                                    },
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                TextField(
                                    modifier = Modifier.width(150.dp),
                                    value = when (tela.value) {
                                        "paciente" -> filtroPaciente.value
                                        "pagamento" -> filtroPagamento.value
                                        "convenio" -> filtroConvenio.value
                                        "horario" -> filtroHorario.value
                                        "sala" -> filtroSala.value
                                        else -> filtroTipo.value
                                    },
                                    onValueChange = {
                                        when (tela.value) {
                                            "paciente" -> pacienteVm.atualizarFiltro(it)
                                            "pagamento" -> pagamentoVm.atualizarFiltro(it)
                                            "convenio" -> convenioVm.atualizarFiltro(it)
                                            "horario" -> horarioVm.atualizarFiltro(it)
                                            "sala" -> salaVm.atualizarFiltro(it)
                                            "tipo" -> tipoVm.atualizarFiltro(it)
                                        }
                                    },
                                    placeholder = { Text("Filtro") }
                                )
                            }
                        } else {
                            Text(
                                text = if (tela.value == "menu") "Clínica de Psicologia" else "Gerenciamento",
                                color = Color.White
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (tela.value == "paciente" && pacienteVm.tela.value == "form") {
                                    pacienteVm.tela.value = "list"
                                } else if (tela.value == "pagamento" && pagamentoVm.tela.value == "form") {
                                    pagamentoVm.tela.value = "list"
                                } else if (tela.value == "convenio" && convenioVm.tela.value == "form") {
                                    convenioVm.tela.value = "list"
                                } else if (tela.value == "horario" && horarioVm.tela.value == "form") {
                                    horarioVm.tela.value = "list"
                                } else if (tela.value == "sala" && salaVm.tela.value == "form") {
                                    salaVm.tela.value = "list"
                                } else if (tela.value == "tipo" && tipoVm.tela.value == "form") {
                                    tipoVm.tela.value = "list"
                                } else {
                                    tela.value = "menu"
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (tela.value == "menu") {
                                    Icons.Filled.Psychology
                                } else {
                                    Icons.AutoMirrored.Filled.ArrowBack
                                },
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = VerdeClinica
                    )
                )
            },
            floatingActionButton = {
                // Botão flutuante de Adicionar (+) igual ao do professor, exibido apenas na listagem de pacientes
                if ((tela.value == "paciente" && pacienteVm.tela.value == "list") ||
                    (tela.value == "pagamento" && pagamentoVm.tela.value == "list") ||
                    (tela.value == "convenio" && convenioVm.tela.value == "list") ||
                    (tela.value == "horario" && horarioVm.tela.value == "list") ||
                    (tela.value == "sala" && salaVm.tela.value == "list") ||
                    (tela.value == "tipo" && tipoVm.tela.value == "list")
                ) {
                    androidx.compose.material3.Button(
                        onClick = {
                            when (tela.value) {
                                "paciente" -> pacienteVm.novoPaciente()
                                "pagamento" -> pagamentoVm.novoPagamento()
                                "convenio" -> convenioVm.novoConvenio()
                                "horario" -> horarioVm.novoHorario()
                                "sala" -> salaVm.novaSala()
                                "tipo" -> tipoVm.novoTipo()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.AddCircle,
                            contentDescription = "Adicionar",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            containerColor = Branco
        ) { contentPadding ->
            if (tela.value == "menu") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Selecione um formulário",
                            color = VerdeClinica,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Escolha uma das opções abaixo para cadastrar ou gerenciar as informações da clínica.",
                            color = VerdeClinica,
                            fontSize = 14.sp
                        )
                    }

                    val itensMenu = listOf(
                        MenuItem("Cadastrar Paciente", "pessoa", "paciente"),
                        MenuItem("Cadastrar Psicólogo", "pessoa", "psicologo"),
                        MenuItem("Cadastrar Funcionário", "pessoa", "funcionario"),
                        MenuItem("Cadastrar Recepcionista", "pessoa", "recepcionista"),
                        MenuItem("Cadastrar Convênio", "documento", "convenio"),
                        MenuItem("Cadastrar Sala de Atendimento", "documento", "sala"),
                        MenuItem("Cadastrar Tipo de Atendimento", "documento", "tipo"),
                        MenuItem("Cadastrar Agenda", "agenda", "agenda"),
                        MenuItem("Cadastrar Pagamento", "documento", "pagamento"),
                        MenuItem("Cadastrar Prontuário", "documento", "prontuario"),
                        MenuItem("Cadastrar Evolução Clínica", "documento", "evolucao"),
                        MenuItem("Bloqueio de Agenda", "agenda", "bloqueio"),
                        MenuItem("Horário Disponível", "agenda", "horario")
                    )

                    for (item in itensMenu) {
                        MenuCard(
                            item = item,
                            onClick = {
                                tela.value = item.tela
                            }
                        )
                    }
                }
            } else {
                // Tratamento das telas específicas
                if (tela.value == "paciente") {
                    if (pacienteVm.tela.value == "form") PacienteFormulario(contentPadding, pacienteVm)
                    else PacienteListagem(contentPadding, pacienteVm)
                } else if (tela.value == "pagamento") {
                    if (pagamentoVm.tela.value == "form") PagamentoFormulario(contentPadding, pagamentoVm)
                    else PagamentoListagem(contentPadding, pagamentoVm)
                } else if (tela.value == "convenio") {
                    if (convenioVm.tela.value == "form") ConvenioFormulario(contentPadding, convenioVm)
                    else ConvenioListagem(contentPadding, convenioVm)
                } else if (tela.value == "horario") {
                    if (horarioVm.tela.value == "form") HorarioDisponivelFormulario(contentPadding, horarioVm)
                    else HorarioDisponivelListagem(contentPadding, horarioVm)
                } else if (tela.value == "sala") {
                    if (salaVm.tela.value == "form") SalaAtendimentoFormulario(contentPadding, salaVm)
                    else SalaAtendimentoListagem(contentPadding, salaVm)
                } else if (tela.value == "tipo") {
                    if (tipoVm.tela.value == "form") TipoAtendimentoFormulario(contentPadding, tipoVm)
                    else TipoAtendimentoListagem(contentPadding, tipoVm)
                } else {
                    // Demais telas do seu sistema mantidas no layout original
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        if (tela.value == "agenda") {
                            AgendaFormulario()
                        } else if (tela.value == "prontuario") {
                            ProntuarioFormulario()
                        } else if (tela.value == "recepcionista") {
                            RecepcionistaFormulario()
                        } else if (tela.value == "evolucao") {
                            EvolucaoClinicaFormulario()
                        } else if (tela.value == "bloqueio") {
                            BloqueioAgendaFormulario()
                        } else if (tela.value == "funcionario") {
                            FuncionarioFormulario()
                        } else if (tela.value == "psicologo") {
                            PsicologoFormulario()
                        }
                    }
                }
            }
        }
    }
}