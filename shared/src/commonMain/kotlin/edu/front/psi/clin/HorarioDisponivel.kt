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
import androidx.compose.material3.TextFieldDefaults

@Composable
fun HorarioDisponivelFormulario() {

    val psicologo = remember { mutableStateOf("") }
    val diaSemana = remember { mutableStateOf("") }
    val horaInicio = remember { mutableStateOf("") }
    val horaFim = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Horário Disponível")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            placeholder = { Text("Nome do psicólogo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = diaSemana.value,
            onValueChange = { diaSemana.value = it },
            placeholder = { Text("Dia da semana") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = horaInicio.value,
            onValueChange = { horaInicio.value = it },
            placeholder = { Text("Hora de início (ex.: 08:00)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = horaFim.value,
            onValueChange = { horaFim.value = it },
            placeholder = { Text("Hora de fim (ex.: 09:00)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            placeholder = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = ativo.value,
            onValueChange = { ativo.value = it },
            placeholder = { Text("Ativo? Digite true ou false") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gravar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                psicologo.value = ""
                diaSemana.value = ""
                horaInicio.value = ""
                horaFim.value = ""
                descricao.value = ""
                ativo.value = "true"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpar Campos")
        }
    }
}
