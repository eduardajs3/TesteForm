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

@Composable
fun EvolucaoClinicaFormulario() {

    val prontuario = remember { mutableStateOf("") }
    val psicologo = remember { mutableStateOf("") }
    val agenda = remember { mutableStateOf("") }
    val dataRegistro = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Evolução Clínica")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = prontuario.value,
            onValueChange = { prontuario.value = it },
            label = { Text("Prontuário") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            label = { Text("Psicólogo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = agenda.value,
            onValueChange = { agenda.value = it },
            label = { Text("Agenda") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dataRegistro.value,
            onValueChange = { dataRegistro.value = it },
            label = { Text("Data e hora do registro") },
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
            label = { Text("Descrição da evolução clínica") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            ))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("Gravar")
            }
            Button(
                onClick = {
                prontuario.value = ""
                psicologo.value = ""
                agenda.value = ""
                dataRegistro.value = ""
                descricao.value = ""
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar Campos")
            }
        }
    }
}
