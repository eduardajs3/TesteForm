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
fun PagamentoFormulario() {

    val paciente = remember { mutableStateOf("") }
    val convenio = remember { mutableStateOf("") }
    val valorTotal = remember { mutableStateOf("") }
    val valorConvenio = remember { mutableStateOf("") }
    val valorPaciente = remember { mutableStateOf("") }
    val formaPagamento = remember { mutableStateOf("") }
    val dataPagamento = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Pagamento")

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            placeholder = { Text("Nome do paciente") },
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
            value = valorTotal.value,
            onValueChange = { valorTotal.value = it },
            placeholder = { Text("Valor total") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = valorConvenio.value,
            onValueChange = { valorConvenio.value = it },
            placeholder = { Text("Valor do convênio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = valorPaciente.value,
            onValueChange = { valorPaciente.value = it },
            placeholder = { Text("Valor do paciente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = formaPagamento.value,
            onValueChange = { formaPagamento.value = it },
            placeholder = { Text("Forma de pagamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = dataPagamento.value,
            onValueChange = { dataPagamento.value = it },
            placeholder = { Text("Data do pagamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = status.value,
            onValueChange = { status.value = it },
            placeholder = { Text("Status do pagamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {},
            onClear = {
                paciente.value = ""
                convenio.value = ""
                valorTotal.value = ""
                valorConvenio.value = ""
                valorPaciente.value = ""
                formaPagamento.value = ""
                dataPagamento.value = ""
                status.value = ""
            }
        )
    }
}