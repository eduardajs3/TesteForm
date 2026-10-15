package edu.front.psi.clin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

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
        label = { Text("Nome completo") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = cpf.value,
        onValueChange = { cpf.value = it },
        label = { Text("CPF") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = nascimento.value,
        onValueChange = { nascimento.value = it },
        label = { Text("Data de nascimento") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = email.value,
        onValueChange = { email.value = it },
        label = { Text("E-mail") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = telefone.value,
        onValueChange = { telefone.value = it },
        label = { Text("Telefone") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = endereco.value,
        onValueChange = { endereco.value = it },
        label = { Text("Endereço") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = observacoes.value,
        onValueChange = { observacoes.value = it },
        label = { Text("Observações") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

    TextField(
        value = ativo.value,
        onValueChange = { ativo.value = it },
        label = { Text("Ativo: true ou false") },
        modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                val paciente = Paciente(
                    id = pacientes.size + 1,
                    nome = nome.value,
                    cpf = cpf.value,
                    dataNascimento = nascimento.value,
                    email = email.value,
                    telefone = telefone.value,
                    endereco = endereco.value,
                    observacoes = observacoes.value,
                    ativo = ativo.value
                )

                pacientes.add(paciente)
                mensagem.value = "Paciente gravado no protótipo!"
            },
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar Campos")
            }
        }

        Text(mensagem.value)
    }
}