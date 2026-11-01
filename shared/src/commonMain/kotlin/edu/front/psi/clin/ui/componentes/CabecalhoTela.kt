package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.VerdeClinica

@Composable
fun CabecalhoTela(
    titulo: String,
    subtitulo: String? = null,
    textoBotao: String,
    iconeBotao: Painter,
    onClickBotao: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = titulo,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            if (!subtitulo.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Button(
            onClick = onClickBotao,
            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = iconeBotao,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(textoBotao, color = Color.White)
        }
    }
}