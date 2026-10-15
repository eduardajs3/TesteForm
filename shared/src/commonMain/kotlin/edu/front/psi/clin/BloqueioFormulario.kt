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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults

@Composable
fun BloqueioAgendaFormulario() {

    val psicologo = remember { mutableStateOf("") }
    val dataHoraInicio = remember { mutableStateOf("") }
    val dataHoraFim = remember { mutableStateOf("") }
    val motivo = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Cadastro de Bloqueio de Agenda",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeClinica
        )
        Text(text = "Preencha os dados do bloqueio de agenda.")

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            label = { Text("Nome do psicólogo") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dataHoraInicio.value,
            onValueChange = { dataHoraInicio.value = it },
            label = { Text("Data e hora de início") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dataHoraFim.value,
            onValueChange = { dataHoraFim.value = it },
            label = { Text("Data e hora de fim") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = motivo.value,
            onValueChange = { motivo.value = it },
            label = { Text("Motivo do bloqueio") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )            )

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
