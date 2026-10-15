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

        TextField(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            label = { Text("Nome do paciente") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = convenio.value,
            onValueChange = { convenio.value = it },
            label = { Text("Convênio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = valorTotal.value,
            onValueChange = { valorTotal.value = it },
            label = { Text("Valor total") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = valorConvenio.value,
            onValueChange = { valorConvenio.value = it },
            label = { Text("Valor do convênio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = valorPaciente.value,
            onValueChange = { valorPaciente.value = it },
            label = { Text("Valor do paciente") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = formaPagamento.value,
            onValueChange = { formaPagamento.value = it },
            label = { Text("Forma de pagamento") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dataPagamento.value,
            onValueChange = { dataPagamento.value = it },
            label = { Text("Data do pagamento") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = status.value,
            onValueChange = { status.value = it },
            label = { Text("Status do pagamento") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

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
                paciente.value = ""
                convenio.value = ""
                valorTotal.value = ""
                valorConvenio.value = ""
                valorPaciente.value = ""
                formaPagamento.value = ""
                dataPagamento.value = ""
                status.value = ""
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar Campos")
            }
        }
    }
}