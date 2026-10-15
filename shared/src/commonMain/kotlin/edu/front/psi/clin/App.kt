package edu.front.psi.clin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val CremeClaro = Color(0xFFF6F3EC)
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
            .padding(8.dp)
            .background(VerdeClinica, RoundedCornerShape(12.dp))
            .height(70.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.icone == "pessoa") {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = item.nome,
                    tint = Color.White
                )
            } else if (item.icone == "agenda") {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = item.nome,
                    tint = Color.White
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = item.nome,
                    tint = Color.White
                )
            }

            Text(
                text = item.nome,
                color = Color.White,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val tela = remember { mutableStateOf("menu") }

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
                        Text(
                            "Clínica de Psicologia",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { tela.value = "menu" }
                        ) {
                            Icon(
                                imageVector = if (tela.value == "menu") {
                                    Icons.Filled.Menu
                                } else {
                                    Icons.Filled.ArrowBack
                                },
                                contentDescription = if (tela.value == "menu") {
                                    "Menu"
                                } else {
                                    "Voltar"
                                },
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = VerdeClinica
                    )
                )
            },
            containerColor = CremeClaro
        ) { contentPadding ->

            if (tela.value == "menu") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Menu Principal",
                        color = VerdeClinica,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

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
                        MenuItem("Bloqueio de Agenda", "agenda", "bloqueio")
                    )

                    for (item in itensMenu) {
                        MenuCard(
                            item = item,
                            onClick = { tela.value = item.tela }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (tela.value == "paciente") {
                        PacienteFormulario()
                    } else if (tela.value == "agenda") {
                        AgendaFormulario()
                    } else if (tela.value == "prontuario") {
                        ProntuarioFormulario()
                    } else if (tela.value == "recepcionista") {
                        RecepcionistaFormulario()
                    } else if (tela.value == "evolucao") {
                        EvolucaoClinicaFormulario()
                    } else if (tela.value == "bloqueio") {
                        BloqueioAgendaFormulario()
                    } else if (tela.value == "convenio") {
                        ConvenioFormulario()
                    } else if (tela.value == "funcionario") {
                        FuncionarioFormulario()
                    } else if (tela.value == "psicologo") {
                        PsicologoFormulario()
                    } else if (tela.value == "sala") {
                        SalaAtendimentoFormulario()
                    } else if (tela.value == "tipo") {
                        TipoAtendimentoFormulario()
                    } else if (tela.value == "pagamento") {
                        PagamentoFormulario()
                    }
                }
            }
        }
    }
}
