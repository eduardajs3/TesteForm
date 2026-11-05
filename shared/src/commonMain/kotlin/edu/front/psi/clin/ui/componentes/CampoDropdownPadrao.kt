package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CinzaBordaCampo
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.VerdeClinica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDropdownPadrao(
    label: String,
    valorSelecionado: String,
    opcoes: List<String>,
    onOpcaoSelecionada: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = valorSelecionado,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            isError = isError,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Branco,
                unfocusedContainerColor = Branco,
                focusedBorderColor = VerdeClinica,
                unfocusedBorderColor = if (isError) TextoErro else CinzaBordaCampo,
                errorBorderColor = TextoErro,
                focusedLabelColor = VerdeClinica,
                unfocusedLabelColor = if (isError) TextoErro else TextoPrincipal,
                errorLabelColor = TextoErro,
                focusedTextColor = TextoPrincipal,
                unfocusedTextColor = TextoPrincipal,
                focusedTrailingIconColor = VerdeClinica,
                unfocusedTrailingIconColor = VerdeClinica,
                errorTrailingIconColor = TextoErro
            ),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Branco, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 4.dp)
        ) {
            opcoes.forEach { opcao ->
                val isSelecionado = opcao == valorSelecionado
                DropdownMenuItem(
                    text = {
                        Text(
                            text = opcao,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelecionado) VerdeClinica else TextoPrincipal,
                            fontWeight = if (isSelecionado) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onOpcaoSelecionada(opcao)
                        expanded = false
                    },
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelecionado) VerdeClinica.copy(alpha = 0.08f) else Branco)
                )
            }
        }
    }

    if (isError && !errorMessage.isNullOrEmpty()) {
        Text(
            text = errorMessage,
            color = TextoErro,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}