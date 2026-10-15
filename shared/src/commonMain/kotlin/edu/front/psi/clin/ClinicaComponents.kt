package edu.front.psi.clin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val VerdeClinica = Color(0xFF3B5760)
private val CremeCampo = Color(0xFFF6F3EC)

@Composable
fun CampoClinica(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    minLines: Int = 1
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = placeholder,
        modifier = modifier,
        minLines = minLines,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = CremeCampo,
            focusedContainerColor = CremeCampo
        )
    )
}

@Composable
fun AcoesFormulario(
    onSave: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f)
        ) {
            Text("Gravar")
        }

        Button(
            onClick = onClear,
            modifier = Modifier.weight(1f)
        ) {
            Text("Limpar Campos")
        }
    }
}
