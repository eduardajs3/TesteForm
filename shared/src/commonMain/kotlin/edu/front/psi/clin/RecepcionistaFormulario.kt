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
fun RecepcionistaFormulario() {

    val nome = remember { mutableStateOf("") }
    val cpf = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val telefone = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val dataCadastro = remember { mutableStateOf("") }
    val setor = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Cadastro de Recepcionista")

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = nome.value,
            onValueChange = { nome.value = it },
            placeholder = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = cpf.value,
            onValueChange = { cpf.value = it },
            placeholder = { Text("CPF") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            placeholder = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = senha.value,
            onValueChange = { senha.value = it },
            placeholder = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = dataCadastro.value,
            onValueChange = { dataCadastro.value = it },
            placeholder = { Text("Data de cadastro") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoClinica(
            value = setor.value,
            onValueChange = { setor.value = it },
            placeholder = { Text("Setor") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcoesFormulario(
            onSave = {},
            onClear = {
                nome.value = ""
                cpf.value = ""
                email.value = ""
                telefone.value = ""
                senha.value = ""
                dataCadastro.value = ""
                setor.value = ""
            }
        )
    }
}
