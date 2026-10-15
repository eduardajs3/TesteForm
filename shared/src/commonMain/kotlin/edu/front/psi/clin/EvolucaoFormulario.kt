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

        CampoClinica(
            value = prontuario.value,
            onValueChange = { prontuario.value = it },
            placeholder = { Text("Prontuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            placeholder = { Text("Psicólogo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = agenda.value,
            onValueChange = { agenda.value = it },
            placeholder = { Text("Agenda") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = dataRegistro.value,
            onValueChange = { dataRegistro.value = it },
            placeholder = { Text("Data e hora do registro") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            placeholder = { Text("Descrição da evolução clínica") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {},
            onClear = {
                prontuario.value = ""
                psicologo.value = ""
                agenda.value = ""
                dataRegistro.value = ""
                descricao.value = ""
            }
        )
    }
}
