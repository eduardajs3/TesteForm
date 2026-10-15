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
fun ProntuarioFormulario() {

    val paciente = remember { mutableStateOf("") }
    val dataAbertura = remember { mutableStateOf("") }
    val queixaPrincipal = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Prontuário")

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            placeholder = { Text("Paciente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = dataAbertura.value,
            onValueChange = { dataAbertura.value = it },
            placeholder = { Text("Data de abertura") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = queixaPrincipal.value,
            onValueChange = { queixaPrincipal.value = it },
            placeholder = { Text("Queixa principal") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = status.value,
            onValueChange = { status.value = it },
            placeholder = { Text("Status do prontuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {},
            onClear = {
                paciente.value = ""
                dataAbertura.value = ""
                queixaPrincipal.value = ""
                status.value = ""
            }
        )
    }
}
