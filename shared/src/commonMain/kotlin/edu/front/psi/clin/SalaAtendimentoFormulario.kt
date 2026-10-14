package edu.front.psi.clin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val salasAtendimento = mutableListOf<SalaAtendimento>()

@Composable
fun SalaAtendimentoFormulario() {
    val nome = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val capacidade = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    CardFormulario(
        titulo = "Cadastro de Sala de Atendimento",
        subtitulo = "Gerencie os consultórios e espaços disponíveis para terapia"
    ) {
        MeuTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = "Nome da Sala / Consultório",
            placeholder = "Ex: Sala 01 - Terapia Infantil"
        )

        MeuTextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            label = "Descrição dos Recursos",
            placeholder = "Ex: Poltronas reclináveis, brinquedoteca e ar-condicionado"
        )

        MeuTextField(
            value = capacidade.value,
            onValueChange = { capacidade.value = it },
            label = "Capacidade de Pessoas",
            placeholder = "Ex: 4"
        )

        MeuTextField(
            value = ativo.value,
            onValueChange = { ativo.value = it },
            label = "Status Ativo",
            placeholder = "true ou false"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            BotaoSecundario(
                texto = "Limpar Campos",
                onClick = {
                    nome.value = ""
                    descricao.value = ""
                    capacidade.value = ""
                    ativo.value = "true"
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Sala",
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
                            dataCadastro = obterDataHoraAtual()
                        )

                        salasAtendimento.add(sala)
                        mensagem.value = "Sala '${sala.nome}' gravada com sucesso!"
                    } else {
                        mensagem.value =
                            "Preencha os campos corretamente. A capacidade deve ser um número maior que zero."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}
