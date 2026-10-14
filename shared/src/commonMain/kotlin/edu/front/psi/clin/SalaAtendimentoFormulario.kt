
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
import java.time.LocalDateTime

val salasAtendimento = mutableListOf<SalaAtendimento>()

@Composable
fun SalaAtendimentoFormulario() {

    val nome = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val capacidade = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Sala de Atendimento")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            placeholder = { Text("Nome da sala") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            placeholder = { Text("Descrição da sala") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = capacidade.value,
            onValueChange = { capacidade.value = it },
            placeholder = { Text("Capacidade de pessoas") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = ativo.value,
            onValueChange = { ativo.value = it },
            placeholder = { Text("Ativo? Digite true ou false") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val capacidadeNumero = capacidade.value.toIntOrNull()

                if (
                    nome.value.isNotBlank() &&
                    descricao.value.isNotBlank() &&
                    capacidadeNumero != null &&
                    capacidadeNumero > 0 &&
                    (ativo.value == "true" || ativo.value == "false")
                ) {
                    val sala = SalaAtendimento(
                        id = salasAtendimento.size + 1,
                        nome = nome.value,
                        descricao = descricao.value,
                        capacidade = capacidadeNumero,
                        ativo = ativo.value == "true",
                        dataCadastro = LocalDateTime.now().toString()
                    )

                    salasAtendimento.add(sala)
                    mensagem.value = "Sala gravada com sucesso!"
                } else {
                    mensagem.value =
                        "Preencha os campos corretamente. A capacidade deve ser maior que zero."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gravar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                nome.value = ""
                descricao.value = ""
                capacidade.value = ""
                ativo.value = "true"
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
