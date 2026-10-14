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

val convenios = mutableListOf<Convenio>()

@Composable
fun ConvenioFormulario() {
    val nome = remember { mutableStateOf("") }
    val cnpj = remember { mutableStateOf("") }
    val telefone = remember { mutableStateOf("") }
    val percentualCobertura = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    CardFormulario(
        titulo = "Cadastro de Convênio",
        subtitulo = "Parcerias com operadoras de planos de saúde e seguradoras"
    ) {
        MeuTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = "Nome da Operadora / Plano",
            placeholder = "Ex: Unimed Saúde"
        )

        MeuTextField(
            value = cnpj.value,
            onValueChange = { cnpj.value = it },
            label = "CNPJ",
            placeholder = "00.000.000/0001-00"
        )

        MeuTextField(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            label = "Telefone de Suporte / Credenciamento",
            placeholder = "0800 123 4567"
        )

        MeuTextField(
            value = percentualCobertura.value,
            onValueChange = { percentualCobertura.value = it },
            label = "Percentual de Cobertura Padrão (%)",
            placeholder = "Ex: 80"
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
                    cnpj.value = ""
                    telefone.value = ""
                    percentualCobertura.value = ""
                    ativo.value = "true"
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Convênio",
                onClick = {
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
                        mensagem.value = "Convênio '${convenio.nome}' gravado com sucesso!"
                    } else {
                        mensagem.value =
                            "Confira os campos e informe um percentual de cobertura válido."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}
