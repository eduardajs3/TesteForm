package edu.front.psi.clin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val pacientes = mutableListOf<Paciente>()

@Composable
fun PacienteFormulario() {
    val nome = remember { mutableStateOf("") }
    val cpf = remember { mutableStateOf("") }
    val nascimento = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val telefone = remember { mutableStateOf("") }
    val endereco = remember { mutableStateOf("") }
    val observacoes = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
       Text("Cadastro de Paciente")

    TextField(
        value = nome.value,
        onValueChange = { nome.value = it },
        placeholder = { Text("Nome completo") }
    )

    TextField(
        value = cpf.value,
        onValueChange = { cpf.value = it },
        placeholder = { Text("CPF") }
    )

    TextField(
        value = nascimento.value,
        onValueChange = { nascimento.value = it },
        placeholder = { Text("Data de nascimento") }
    )

    TextField(
        value = email.value,
        onValueChange = { email.value = it },
        placeholder = { Text("E-mail") }
    )

    TextField(
        value = telefone.value,
        onValueChange = { telefone.value = it },
        placeholder = { Text("Telefone") }
    )

    TextField(
        value = endereco.value,
        onValueChange = { endereco.value = it },
        placeholder = { Text("Endereço") }
    )

    TextField(
        value = observacoes.value,
        onValueChange = { observacoes.value = it },
        placeholder = { Text("Observações") }
    )

    TextField(
        value = ativo.value,
        onValueChange = { ativo.value = it },
        placeholder = { Text("Ativo: true ou false") }
    )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (
                    nome.value.isNotBlank() &&
                    cpf.value.isNotBlank() &&
                    nascimento.value.isNotBlank() &&
                    email.value.isNotBlank() &&
                    telefone.value.isNotBlank() &&
                    endereco.value.isNotBlank() &&
                    (ativo.value == "true" || ativo.value == "false")
                ) {
                    val paciente = Paciente(
                        id = pacientes.size + 1,
                        nome = nome.value,
                        cpf = cpf.value,
                        dataNascimento = nascimento.value,
                        email = email.value,
                        telefone = telefone.value,
                        endereco = endereco.value,
                        observacoes = observacoes.value,
                        ativo = ativo.value == "true"
                    )

                    pacientes.add(paciente)
                    mensagem.value = "Paciente gravado no protótipo!"
                } else {
                    mensagem.value = "Preencha os campos obrigatórios."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gravar")
        }

        Button(
            onClick = {
                nome.value = ""
                cpf.value = ""
                nascimento.value = ""
                email.value = ""
                telefone.value = ""
                endereco.value = ""
                observacoes.value = ""
                ativo.value = "true"
                mensagem.value = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpar Campos")
        }

        Text(mensagem.value)
    }
}