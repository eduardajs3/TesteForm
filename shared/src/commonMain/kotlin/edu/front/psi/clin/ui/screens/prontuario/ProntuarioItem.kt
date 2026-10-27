package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.Prontuario

@Composable
fun ProntuarioItem(
    prontuario: Prontuario,
    apagar: (Prontuario) -> Unit = {},
    editar: (Prontuario) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
            .height(100.dp)
            .border(shape = RoundedCornerShape(20.dp), width = 2.dp, color = Color.Black)
            .padding(vertical = 4.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = prontuario.paciente,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Data: ${prontuario.dataAbertura} | Status: ${prontuario.status}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Queixa: ${prontuario.queixaPrincipal}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column {
            IconButton(onClick = { editar(prontuario) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar"
                )
            }
            IconButton(onClick = { apagar(prontuario) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover"
                )
            }
        }
    }
}