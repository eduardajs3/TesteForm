package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CinzaBordaCampo
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun CampoTextoPadrao(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    obrigatorio: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    apenasNumeros: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { novoTexto ->
                if (apenasNumeros) {
                    onValueChange(novoTexto.filter { it.isDigit() })
                } else {
                    onValueChange(novoTexto)
                }
            },
            label = {
                Text(
                    text = if (obrigatorio) "$label *" else label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (apenasNumeros) KeyboardType.Number else KeyboardType.Text
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Branco,
                unfocusedContainerColor = Branco,
                focusedBorderColor = VerdeClinica,
                unfocusedBorderColor = if (isError) TextoErro else CinzaBordaCampo,
                focusedLabelColor = VerdeClinica,
                unfocusedLabelColor = if (isError) TextoErro else VerdeClinica,
                focusedTextColor = TextoPrincipal,
                unfocusedTextColor = TextoPrincipal,
                errorBorderColor = TextoErro,
                errorLabelColor = TextoErro,
                errorTextColor = TextoPrincipal
            )
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = TextoErro,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}