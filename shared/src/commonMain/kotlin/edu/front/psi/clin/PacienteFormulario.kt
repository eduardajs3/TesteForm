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

val pacientes = mutableListOf<Paciente>()

@Composable
fun PacienteFormulario() {
    val nome = remember { mutableStateOf("") }
    val cpf = remember { mutableStateOf("") }
    val nascimento = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val telefone = remember { mutableStateOf("") }
    val endereco = remember { mutableStateOf("") }
    val observacoes = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    CardFormulario(
        titulo = "Cadastro de Paciente",
        subtitulo = "Informações cadastrais e de contato do paciente"
    ) {
        MeuTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = "Nome Completo",
            placeholder = "Ex: Ana Clara Souza"
        )

        MeuTextField(
            value = cpf.value,
            onValueChange = { cpf.value = it },
            label = "CPF",
            placeholder = "000.000.000-00"
        )

        MeuTextField(
            value = nascimento.value,
            onValueChange = { nascimento.value = it },
            label = "Data de Nascimento",
            placeholder = "DD/MM/AAAA"
        )

        MeuTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = "E-mail",
            placeholder = "anaclara@email.com"
        )

        MeuTextField(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            label = "Telefone",
            placeholder = "(11) 99999-8888"
        )

        MeuTextField(
            value = endereco.value,
            onValueChange = { endereco.value = it },
            label = "Endereço Completo",
            placeholder = "Rua das Flores, 123 - Apto 4"
        )

        MeuTextField(
            value = observacoes.value,
            onValueChange = { observacoes.value = it },
            label = "Observações Clínicas",
            placeholder = "Histórico, recomendações ou queixas principais"
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
                    cpf.value = ""
                    nascimento.value = ""
                    email.value = ""
                    telefone.value = ""
                    endereco.value = ""
                    observacoes.value = ""
                    ativo.value = "true"
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Paciente",
                onClick = {
                    if (
                        nome.value.isNotBlank() &&
                        cpf.value.isNotBlank() &&
                        nascimento.value.isNotBlank() &&
                        email.value.isNotBlank() &&
                        telefone.value.isNotBlank() &&
                        endereco.value.isNotBlank() &&
                        (ativo.value == "true" || ativo.value == "false")
                    ) {
                        val paciente = Paciente(
                            id = pacientes.size + 1,
                            nome = nome.value,
                            cpf = cpf.value,
                            dataNascimento = nascimento.value,
                            email = email.value,
                            telefone = telefone.value,
                            endereco = endereco.value,
                            observacoes = observacoes.value,
                            ativo = ativo.value == "true"
                        )

                        pacientes.add(paciente)
                        mensagem.value = "Paciente '${paciente.nome}' gravado com sucesso!"
                    } else {
                        mensagem.value = "Preencha todos os campos obrigatórios."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}