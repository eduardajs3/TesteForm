package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

private val CorTextoPadrao = Color(0xFF1F2937)

@Composable
fun RowScope.CelulaTabelaTexto(
    text: String,
    weight: Float,
    isBold: Boolean = false,
    color: Color = CorTextoPadrao,
    fontSize: Int = 13
) {
    Text(
        text = text,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        fontSize = fontSize.sp,
        modifier = Modifier.weight(weight),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}