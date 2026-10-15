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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults

@Composable
fun ProntuarioFormulario() {

    val paciente = remember { mutableStateOf("") }
    val dataAbertura = remember { mutableStateOf("") }
    val queixaPrincipal = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Cadastro de Prontuário",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeClinica
        )
        Text(text = "Preencha os dados do prontuário.")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = paciente.value,
            onValueChange = { paciente.value = it },
            label = { Text("Paciente") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dataAbertura.value,
            onValueChange = { dataAbertura.value = it },
            label = { Text("Data de abertura") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = queixaPrincipal.value,
            onValueChange = { queixaPrincipal.value = it },
            label = { Text("Queixa principal") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ))

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = status.value,
            onValueChange = { status.value = it },
            label = { Text("Status do prontuário") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        
        TextField(
            value = ativo.value,
            onValueChange = { ativo.value = it },
            label = { Text("Ativo? Digite true ou false") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
           
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF6F3EC),
                    contentColor = VerdeClinica
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpar Campos")
            }
             Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("Gravar")
            }
        }
    }
}
