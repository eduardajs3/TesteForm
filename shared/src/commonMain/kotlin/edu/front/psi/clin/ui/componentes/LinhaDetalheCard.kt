package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

@Composable
fun LinhaDetalheCard(
    rotulo: String,
    valor: String,
    corTexto: Color = Color.Gray
) {
    if (valor.isNotEmpty()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = rotulo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1F2937)
            )
            Text(
                text = valor,
                fontSize = 14.sp,
                color = corTexto
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}