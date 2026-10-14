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

val funcionarios = mutableListOf<Funcionario>()

@Composable
fun FuncionarioFormulario() {
    val nome = remember { mutableStateOf("") }
    val cpf = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val telefone = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    CardFormulario(
        titulo = "Cadastro de Funcionário",
        subtitulo = "Cadastre recepcionistas, administradores e equipe de apoio"
    ) {
        MeuTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = "Nome Completo",
            placeholder = "Ex: Mariana Silva"
        )

        MeuTextField(
            value = cpf.value,
            onValueChange = { cpf.value = it },
            label = "CPF",
            placeholder = "000.000.000-00"
        )

        MeuTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = "E-mail",
            placeholder = "mariana@clinica.com"
        )

        MeuTextField(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            label = "Telefone",
            placeholder = "(11) 98765-4321"
        )

        MeuTextField(
            value = senha.value,
            onValueChange = { senha.value = it },
            label = "Senha de Acesso",
            placeholder = "Digite uma senha segura"
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
                    email.value = ""
                    telefone.value = ""
                    senha.value = ""
                    ativo.value = "true"
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Funcionário",
                onClick = {
                    if (
                        nome.value.isNotBlank() &&
                        cpf.value.isNotBlank() &&
                        email.value.isNotBlank() &&
                        telefone.value.isNotBlank() &&
                        senha.value.isNotBlank() &&
                        (ativo.value == "true" || ativo.value == "false")
                    ) {
                        val funcionario = Funcionario(
                            id = funcionarios.size + 1,
                            nome = nome.value,
                            cpf = cpf.value,
                            email = email.value,
                            telefone = telefone.value,
                            senha = senha.value,
                            ativo = ativo.value == "true",
                            dataCadastro = obterDataHoraAtual()
                        )

                        funcionarios.add(funcionario)
                        mensagem.value = "Funcionário '${funcionario.nome}' gravado com sucesso!"
                    } else {
                        mensagem.value = "Preencha todos os campos obrigatórios corretamente."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}
