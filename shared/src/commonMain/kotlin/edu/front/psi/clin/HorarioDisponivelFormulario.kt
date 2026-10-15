package edu.front.psi.clin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults

@Composable
fun HorarioDisponivelFormulario() {

    val psicologo = remember { mutableStateOf("") }
    val diaSemana = remember { mutableStateOf("") }
    val horaInicio = remember { mutableStateOf("") }
    val horaFim = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Cadastro de Horário Disponível",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeClinica
        )
        Text(text = "Preencha os dados do horário disponível.")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            placeholder = { Text("Nome do psicólogo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = diaSemana.value,
            onValueChange = { diaSemana.value = it },
            placeholder = { Text("Dia da semana") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = horaInicio.value,
            onValueChange = { horaInicio.value = it },
            placeholder = { Text("Hora de início (ex.: 08:00)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = horaFim.value,
            onValueChange = { horaFim.value = it },
            placeholder = { Text("Hora de fim (ex.: 09:00)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            placeholder = { Text("Descrição") },
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
