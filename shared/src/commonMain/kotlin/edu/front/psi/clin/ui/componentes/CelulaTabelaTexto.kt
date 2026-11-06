package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import edu.front.psi.clin.ui.theme.TextoPrincipal

@Composable
fun RowScope.CelulaTabelaTexto(
    text: String,
    weight: Float,
    isBold: Boolean = false,
    color: Color = TextoPrincipal
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        modifier = Modifier.weight(weight),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}