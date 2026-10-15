
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

        CampoClinica(
            value = nome.value,
            onValueChange = { nome.value = it },
            placeholder = { Text("Nome do convênio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = cnpj.value,
            onValueChange = { cnpj.value = it },
            placeholder = { Text("CNPJ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            placeholder = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = percentualCobertura.value,
            onValueChange = { percentualCobertura.value = it },
            placeholder = { Text("Percentual de cobertura (ex.: 80)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = ativo.value,
            onValueChange = { ativo.value = it },
            placeholder = { Text("Ativo? Digite true ou false") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {
                val percentual = percentualCobertura.value
                    .replace(",", ".")
                    .toDoubleOrNull()

                if (
                    nome.value.trim().length >= 2 &&
                    cnpj.value.isNotBlank() &&
                    telefone.value.isNotBlank() &&
                    percentual != null &&
                    percentual >= 0 &&
                    (ativo.value == "true" || ativo.value == "false")
                ) {
                    val convenio = Convenio(
                        id = convenios.size + 1,
                        nome = nome.value.trim(),
                        cnpj = cnpj.value,
                        telefone = telefone.value,
                        percentualCobertura = percentual,
                        ativo = ativo.value == "true"
                    )

                    convenios.add(convenio)
                    mensagem.value = "Convênio gravado com sucesso!"
                } else {
                    mensagem.value =
                        "Confira os campos e informe um percentual válido."
                }
            },
            onClear = {
                nome.value = ""
                cnpj.value = ""
                telefone.value = ""
                percentualCobertura.value = ""
                ativo.value = "true"
                mensagem.value = ""
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(mensagem.value)
    }
}
