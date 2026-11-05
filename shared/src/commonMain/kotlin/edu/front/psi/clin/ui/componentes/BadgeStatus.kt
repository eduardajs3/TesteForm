package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BadgeStatus(
    texto: String,
    isAtivo: Boolean,
    modifier: Modifier = Modifier
) {
    val corFundo = if (isAtivo) Color(0xFFA2C99B) else Color(0xFFFEF3C7)
    val corTexto = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E)

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .background(
                    color = corFundo,
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = texto,
                color = corTexto,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}