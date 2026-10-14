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

    CardFormulario(
        titulo = "Cadastro de Pagamento",
        subtitulo = "Lançamento financeiro de sessões, coparticipações e repasses"
    ) {
        MeuTextField(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            label = "Nome do Paciente",
            placeholder = "Ex: Ana Clara Souza"
        )

        MeuTextField(
            value = convenio.value,
            onValueChange = { convenio.value = it },
            label = "Convênio / Particular",
            placeholder = "Ex: Unimed ou Particular"
        )

        MeuTextField(
            value = valorTotal.value,
            onValueChange = { valorTotal.value = it },
            label = "Valor Total (R$)",
            placeholder = "Ex: 200.00"
        )

        MeuTextField(
            value = valorConvenio.value,
            onValueChange = { valorConvenio.value = it },
            label = "Valor Coberto pelo Convênio (R$)",
            placeholder = "Ex: 150.00"
        )

        MeuTextField(
            value = valorPaciente.value,
            onValueChange = { valorPaciente.value = it },
            label = "Coparticipação do Paciente (R$)",
            placeholder = "Ex: 50.00"
        )

        MeuTextField(
            value = formaPagamento.value,
            onValueChange = { formaPagamento.value = it },
            label = "Forma de Pagamento",
            placeholder = "Ex: PIX, Cartão ou Boleto"
        )

        MeuTextField(
            value = dataPagamento.value,
            onValueChange = { dataPagamento.value = it },
            label = "Data do Pagamento",
            placeholder = "DD/MM/AAAA"
        )

        MeuTextField(
            value = status.value,
            onValueChange = { status.value = it },
            label = "Status do Pagamento",
            placeholder = "Ex: Pago, Pendente ou Faturado"
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
                    paciente.value = ""
                    convenio.value = ""
                    valorTotal.value = ""
                    valorConvenio.value = ""
                    valorPaciente.value = ""
                    formaPagamento.value = ""
                    dataPagamento.value = ""
                    status.value = ""
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Pagamento",
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
                        mensagem.value = "Pagamento do paciente '${pagamento.paciente}' registrado com sucesso!"
                    } else {
                        mensagem.value = "Preencha todos os campos corretamente (valores numéricos válidos)."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}
