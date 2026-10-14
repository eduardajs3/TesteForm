package edu.front.psi.clin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    val tela = remember { mutableStateOf("menu") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Clínica de Psicologia")
                    }
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (tela.value == "menu") {
                    Text("Escolha um formulário")

                    Button(
                        onClick = { tela.value = "paciente" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Paciente")
                    }

                    Button(
                        onClick = { tela.value = "psicologo" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Psicólogo")
                    }
                    Button(
                        onClick = { tela.value = "pagamento" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Pagamento")
                    }
                    Button(
                        onClick = { tela.value = "horario" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Horário Disponível")
                    }
                    Button(
                        onClick = { tela.value = "sala" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Sala de Atendimento")
                    }
                    Button(
                        onClick = { tela.value = "tipo" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Tipo de Atendimento")
                    }
                    Button(
                        onClick = { tela.value = "funcionario" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Funcionário")
                    }
                    Button(
                        onClick = { tela.value = "convenio" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar Convênio")
                    }
                } else if (tela.value == "paciente") {
                    Button(
                        onClick = { tela.value = "menu" }
                    ) {
                        Text("Voltar ao menu")
                    }

                    PacienteFormulario()
                } else if (tela.value == "psicologo") {
                    Button(
                        onClick = { tela.value = "menu" }
                    ) {
                        Text("Voltar ao menu")
                    }

                    PsicologoFormulario()
                }
                else if (tela.value == "pagamento") {
                    Button(onClick = { tela.value = "menu" }) {
                        Text("Voltar ao menu")
                    }

                    PagamentoFormulario()
                }
                else if (tela.value == "horario") {
                    Button(onClick = { tela.value = "menu" }) {
                        Text("Voltar ao menu")
                    }

                    HorarioDisponivelFormulario()
                }
                else if (tela.value == "sala") {
                    Button(onClick = { tela.value = "menu" }) {
                        Text("Voltar ao menu")
                    }

                    SalaAtendimentoFormulario()
                }
                else if (tela.value == "tipo") {
                    Button(onClick = { tela.value = "menu" }) {
                        Text("Voltar ao menu")
                    }

                    TipoAtendimentoFormulario()
                }
                else if (tela.value == "funcionario") {
                    Button(onClick = { tela.value = "menu" }) {
                        Text("Voltar ao menu")
                    }

                    FuncionarioFormulario()
                }
                else if (tela.value == "convenio") {
                    Button(onClick = { tela.value = "menu" }) {
                        Text("Voltar ao menu")
                    }

                    ConvenioFormulario()
                }
            } 
        }
    }
}