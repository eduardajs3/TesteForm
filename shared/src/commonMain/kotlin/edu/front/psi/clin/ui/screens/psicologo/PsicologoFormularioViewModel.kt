package edu.front.psi.clin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel

@Composable
fun PsicologoFormulario(
    innerPadding: PaddingValues,
    vm: PsicologoViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(innerPadding)
    ) {
        Text(
            text = "Cadastro de Psicólogo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeClinica
        )
        Text(text = "Preencha os dados do psicólogo.")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.nome.value,
            onValueChange = { vm.nome.value = it },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.cpf.value,
            onValueChange = { vm.cpf.value = it },
            label = { Text("CPF") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.email.value,
            onValueChange = { vm.email.value = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.telefone.value,
            onValueChange = { vm.telefone.value = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.senha.value,
            onValueChange = { vm.senha.value = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.crp.value,
            onValueChange = { vm.crp.value = it },
            label = { Text("CRP") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.especialidade.value,
            onValueChange = { vm.especialidade.value = it },
            label = { Text("Especialidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = vm.ativo.value,
            onValueChange = { vm.ativo.value = it },
            placeholder = { Text("Ativo? Digite true ou false") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = vm::limparCampos,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF6F3EC),
                    contentColor = VerdeClinica
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar Campos")
            }
            Button(
                onClick = vm::salvar,
                modifier = Modifier.weight(1f)
            ) {
                Text("Gravar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(vm.mensagem.value)
    }
}