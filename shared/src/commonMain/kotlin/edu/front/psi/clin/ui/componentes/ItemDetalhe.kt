package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun ItemDetalhe(
    rotulo: String,
    valor: String?,
    corTextoValor: Color = TextoPrincipal,
    corTextoRotulo: Color = VerdeClinica,
    pesoRotulo: FontWeight? = null,
    valorVazio: String = "-"
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            style = MaterialTheme.typography.bodyMedium,
            color = corTextoRotulo,
            fontWeight = pesoRotulo
        )
        Text(
            text = valor?.ifEmpty { valorVazio } ?: valorVazio,
            style = MaterialTheme.typography.bodyMedium,
            color = corTextoValor
        )
    }
}