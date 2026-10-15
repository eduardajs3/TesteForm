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
fun SalaAtendimentoFormulario() {

    val nome = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val capacidade = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Sala de Atendimento")

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = nome.value,
            onValueChange = { nome.value = it },
            placeholder = { Text("Nome da sala") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            placeholder = { Text("Descrição da sala") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = capacidade.value,
            onValueChange = { capacidade.value = it },
            placeholder = { Text("Capacidade de pessoas") },
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
            onSave = {},
            onClear = {
                nome.value = ""
                descricao.value = ""
                capacidade.value = ""
                ativo.value = "true"
            }
        )
    }
}
