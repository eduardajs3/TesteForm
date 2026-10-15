
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

val convenios = mutableListOf<Convenio>()

@Composable
fun ConvenioFormulario() {

    val nome = remember { mutableStateOf("") }
    val cnpj = remember { mutableStateOf("") }
    val telefone = remember { mutableStateOf("") }
    val percentualCobertura = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Convênio")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = { Text("Nome do convênio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = cnpj.value,
            onValueChange = { cnpj.value = it },
            label = { Text("CNPJ") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CremeCampo,
                focusedContainerColor = CremeCampo
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = percentualCobertura.value,
            onValueChange = { percentualCobertura.value = it },
            label = { Text("Percentual de cobertura (ex.: 80)") },
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
            label = { Text("Ativo? Digite true ou false") },
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
                onClick = {
                val convenio = Convenio(
                    id = convenios.size + 1,
                    nome = nome.value,
                    cnpj = cnpj.value,
                    telefone = telefone.value,
                    percentualCobertura = percentualCobertura.value,
                    ativo = ativo.value
                )

                convenios.add(convenio)
                mensagem.value = "Convênio gravado com sucesso!"
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Gravar")
            }
            Button(
                onClick = {
                nome.value = ""
                cnpj.value = ""
                telefone.value = ""
                percentualCobertura.value = ""
                ativo.value = "true"
                mensagem.value = ""
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar Campos")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(mensagem.value)
    }
}
