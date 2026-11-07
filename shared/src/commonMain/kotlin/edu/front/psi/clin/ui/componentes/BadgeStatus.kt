package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.theme.TextoInativo
import edu.front.psi.clin.ui.theme.CinzaFundoBotao

@Composable
fun BadgeStatus(
    texto: String,
    isAtivo: Boolean,
    modifier: Modifier = Modifier
) {
    val corFundo = if (isAtivo) VerdeClinica.copy(alpha = 0.15f) else CinzaFundoBotao
    val corTexto = if (isAtivo) VerdeClinica else TextoInativo

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