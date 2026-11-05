package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario

@Composable
fun LinhaDetalheCard(
    rotulo: String,
    valor: String,
    corTexto: Color = TextoSecundario
) {
    if (valor.isNotEmpty()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = rotulo,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoPrincipal
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                color = corTexto
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}