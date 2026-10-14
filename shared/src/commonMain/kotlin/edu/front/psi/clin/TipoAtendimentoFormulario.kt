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

val tiposAtendimento = mutableListOf<TipoAtendimento>()

@Composable
fun TipoAtendimentoFormulario() {
    val nome = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val duracao = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    CardFormulario(
        titulo = "Cadastro de Tipo de Atendimento",
        subtitulo = "Defina modalidades de consulta, terapias e durações"
    ) {
        MeuTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = "Nome da Modalidade",
            placeholder = "Ex: Psicoterapia Individual"
        )

        MeuTextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            label = "Descrição Detalhada",
            placeholder = "Ex: Atendimento focado em acolhimento e desenvolvimento pessoal"
        )

        MeuTextField(
            value = duracao.value,
            onValueChange = { duracao.value = it },
            label = "Duração Padrão",
            placeholder = "Ex: 50 minutos"
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
                    duracao.value = ""
                    ativo.value = "true"
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Tipo",
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
                            dataCadastro = obterDataHoraAtual()
                        )

                        tiposAtendimento.add(tipo)
                        mensagem.value = "Tipo de atendimento '${tipo.nome}' gravado com sucesso!"
                    } else {
                        mensagem.value =
                            "Confira os campos. O nome deve ter pelo menos 2 caracteres e todos os campos devem ser preenchidos."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}
