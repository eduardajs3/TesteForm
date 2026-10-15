package edu.front.psi.clin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val CremeClaro = Color(0xFFF6F3EC)

@Composable
private fun MenuCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = VerdeClinica)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Text(
                text = title,
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
                        Text("Clínica de Psicologia", color = Color.White)
                    },
                    navigationIcon = {
                        IconButton(onClick = { tela.value = "menu" }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
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

                    MenuCard("Cadastrar Paciente", Icons.Filled.Person) {
                        tela.value = "paciente"
                    }
                    MenuCard("Cadastrar Psicólogo", Icons.Filled.Person) {
                        tela.value = "psicologo"
                    }
                    MenuCard("Cadastrar Funcionário", Icons.Filled.Person) {
                        tela.value = "funcionario"
                    }
                    MenuCard("Cadastrar Recepcionista", Icons.Filled.Person) {
                        tela.value = "recepcionista"
                    }
                    MenuCard("Cadastrar Convênio", Icons.Filled.Description) {
                        tela.value = "convenio"
                    }
                    MenuCard("Cadastrar Sala de Atendimento", Icons.Filled.Description) {
                        tela.value = "sala"
                    }
                    MenuCard("Cadastrar Tipo de Atendimento", Icons.Filled.Description) {
                        tela.value = "tipo"
                    }
                    MenuCard("Cadastrar Agenda", Icons.Filled.DateRange) {
                        tela.value = "agenda"
                    }
                    MenuCard("Cadastrar Pagamento", Icons.Filled.Description) {
                        tela.value = "pagamento"
                    }
                    MenuCard("Cadastrar Prontuário", Icons.Filled.Description) {
                        tela.value = "prontuario"
                    }
                    MenuCard("Cadastrar Evolução Clínica", Icons.Filled.Description) {
                        tela.value = "evolucao"
                    }
                    MenuCard("Bloqueio de Agenda", Icons.Filled.DateRange) {
                        tela.value = "bloqueio"
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
                    when (tela.value) {
                        "paciente" -> PacienteFormulario()
                        "agenda" -> AgendaFormulario()
                        "prontuario" -> ProntuarioFormulario()
                        "recepcionista" -> RecepcionistaFormulario()
                        "evolucao" -> EvolucaoClinicaFormulario()
                        "bloqueio" -> BloqueioAgendaFormulario()
                        "convenio" -> ConvenioFormulario()
                        "funcionario" -> FuncionarioFormulario()
                        "psicologo" -> PsicologoFormulario()
                        "sala" -> SalaAtendimentoFormulario()
                        "tipo" -> TipoAtendimentoFormulario()
                        "pagamento" -> PagamentoFormulario()
                    }

                    Button(
                        onClick = { tela.value = "menu" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Voltar ao Menu")
                    }
                }
            }
        }
    }
}
