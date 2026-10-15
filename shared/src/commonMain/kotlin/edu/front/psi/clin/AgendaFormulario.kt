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
fun AgendaFormulario() {

    val paciente = remember { mutableStateOf("") }
    val psicologo = remember { mutableStateOf("") }
    val sala = remember { mutableStateOf("") }
    val tipoAtendimento = remember { mutableStateOf("") }
    val convenio = remember { mutableStateOf("") }
    val dataHoraInicio = remember { mutableStateOf("") }
    val dataHoraFim = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }
    val observacoes = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Agenda")

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            placeholder = { Text("Paciente") },
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
            value = sala.value,
            onValueChange = { sala.value = it },
            placeholder = { Text("Sala de atendimento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = tipoAtendimento.value,
            onValueChange = { tipoAtendimento.value = it },
            placeholder = { Text("Tipo de atendimento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = convenio.value,
            onValueChange = { convenio.value = it },
            placeholder = { Text("Convênio") },
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
            value = status.value,
            onValueChange = { status.value = it },
            placeholder = { Text("Status da agenda") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = observacoes.value,
            onValueChange = { observacoes.value = it },
            placeholder = { Text("Observações") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {},
            onClear = {
                paciente.value = ""
                psicologo.value = ""
                sala.value = ""
                tipoAtendimento.value = ""
                convenio.value = ""
                dataHoraInicio.value = ""
                dataHoraFim.value = ""
                status.value = ""
                observacoes.value = ""
            }
        )
    }
}
