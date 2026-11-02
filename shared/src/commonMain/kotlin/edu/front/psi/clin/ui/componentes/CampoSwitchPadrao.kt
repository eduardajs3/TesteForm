package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CampoSwitchPadrao(
    label: String,
    valorAtual: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textoAtivo: String = "Ativo",
    textoInativo: String = "Inativo"
) {
    val isChecked = valorAtual.toBooleanStrictOrNull() ?: true

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ${if (isChecked) textoAtivo else textoInativo}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Switch(
                checked = isChecked,
                onCheckedChange = { novoEstado ->
                    onValueChange(novoEstado.toString())
                }
            )
        }
    }
}