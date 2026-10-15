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
fun BloqueioAgendaFormulario() {

    val psicologo = remember { mutableStateOf("") }
    val dataHoraInicio = remember { mutableStateOf("") }
    val dataHoraFim = remember { mutableStateOf("") }
    val motivo = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Bloqueio de Agenda")

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            placeholder = { Text("Nome do psicólogo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = dataHoraInicio.value,
            onValueChange = { dataHoraInicio.value = it },
            placeholder = { Text("Data e hora de início") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = dataHoraFim.value,
            onValueChange = { dataHoraFim.value = it },
            placeholder = { Text("Data e hora de fim") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = motivo.value,
            onValueChange = { motivo.value = it },
            placeholder = { Text("Motivo do bloqueio") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {},
            onClear = {
                psicologo.value = ""
                dataHoraInicio.value = ""
                dataHoraFim.value = ""
                motivo.value = ""
            }
        )
    }
}
