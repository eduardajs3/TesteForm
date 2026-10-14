
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

val pagamentos = mutableListOf<Pagamento>()

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
    val mensagem = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Pagamento")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            placeholder = { Text("Nome do paciente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = convenio.value,
            onValueChange = { convenio.value = it },
            placeholder = { Text("Convênio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = valorTotal.value,
            onValueChange = { valorTotal.value = it },
            placeholder = { Text("Valor total") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = valorConvenio.value,
            onValueChange = { valorConvenio.value = it },
            placeholder = { Text("Valor do convênio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = valorPaciente.value,
            onValueChange = { valorPaciente.value = it },
            placeholder = { Text("Valor do paciente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = formaPagamento.value,
            onValueChange = { formaPagamento.value = it },
            placeholder = { Text("Forma de pagamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dataPagamento.value,
            onValueChange = { dataPagamento.value = it },
            placeholder = { Text("Data do pagamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = status.value,
            onValueChange = { status.value = it },
            placeholder = { Text("Status do pagamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val total = valorTotal.value.replace(",", ".").toDoubleOrNull()
                val valorDoConvenio = valorConvenio.value.replace(",", ".").toDoubleOrNull()
                val valorDoPaciente = valorPaciente.value.replace(",", ".").toDoubleOrNull()

                if (
                    paciente.value.isNotBlank() &&
                    convenio.value.isNotBlank() &&
                    total != null &&
                    valorDoConvenio != null &&
                    valorDoPaciente != null &&
                    formaPagamento.value.isNotBlank() &&
                    dataPagamento.value.isNotBlank() &&
                    status.value.isNotBlank()
                ) {
                    val pagamento = Pagamento(
                        id = pagamentos.size + 1,
                        paciente = paciente.value,
                        convenio = convenio.value,
                        valorTotal = total,
                        valorConvenio = valorDoConvenio,
                        valorPaciente = valorDoPaciente,
                        formaPagamento = formaPagamento.value,
                        dataPagamento = dataPagamento.value,
                        status = status.value
                    )

                    pagamentos.add(pagamento)
                    mensagem.value = "Pagamento gravado com sucesso!"
                } else {
                    mensagem.value = "Preencha todos os campos corretamente."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gravar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                paciente.value = ""
                convenio.value = ""
                valorTotal.value = ""
                valorConvenio.value = ""
                valorPaciente.value = ""
                formaPagamento.value = ""
                dataPagamento.value = ""
                status.value = ""
                mensagem.value = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpar Campos")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(mensagem.value)
    }
}
