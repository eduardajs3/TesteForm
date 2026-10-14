
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Funcionário")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            placeholder = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = cpf.value,
            onValueChange = { cpf.value = it },
            placeholder = { Text("CPF") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            placeholder = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = senha.value,
            onValueChange = { senha.value = it },
            placeholder = { Text("Senha") },
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
                        dataCadastro = LocalDateTime.now().toString()
                    )

                    funcionarios.add(funcionario)
                    mensagem.value = "Funcionário gravado com sucesso!"
                } else {
                    mensagem.value = "Preencha todos os campos corretamente."
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
                cpf.value = ""
                email.value = ""
                telefone.value = ""
                senha.value = ""
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
