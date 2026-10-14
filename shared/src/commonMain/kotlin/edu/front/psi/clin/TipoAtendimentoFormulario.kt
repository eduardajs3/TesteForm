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

val tiposAtendimento = mutableListOf<TipoAtendimento>()

@Composable
fun TipoAtendimentoFormulario() {

    val nome = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val duracao = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Tipo de Atendimento")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            placeholder = { Text("Nome do atendimento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            placeholder = { Text("Descrição do atendimento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = duracao.value,
            onValueChange = { duracao.value = it },
            placeholder = { Text("Duração (ex.: 50 minutos)") },
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
                if (
                    nome.value.trim().length >= 2 &&
                    descricao.value.isNotBlank() &&
                    descricao.value.length <= 500 &&
                    duracao.value.isNotBlank() &&
                    duracao.value.length <= 20 &&
                    (ativo.value == "true" || ativo.value == "false")
                ) {
                    val tipo = TipoAtendimento(
                        id = tiposAtendimento.size + 1,
                        nome = nome.value.trim(),
                        descricao = descricao.value,
                        duracao = duracao.value,
                        ativo = ativo.value == "true",
                        dataCadastro = LocalDateTime.now().toString()
                    )

                    tiposAtendimento.add(tipo)
                    mensagem.value = "Tipo de atendimento gravado com sucesso!"
                } else {
                    mensagem.value =
                        "Confira os campos. O nome deve ter pelo menos 2 caracteres."
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
                duracao.value = ""
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
