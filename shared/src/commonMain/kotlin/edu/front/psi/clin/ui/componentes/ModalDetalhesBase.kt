package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun ModalDetalhesBase(
    titulo: String,
    onDismiss: () -> Unit,
    conteudo: @Composable ColumnScope.() -> Unit,
    acoesExtras: @Composable (ColumnScope.() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                color = VerdeClinica
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                conteudo()

                if (acoesExtras != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ações Rápidas",
                        style = MaterialTheme.typography.titleMedium,
                        color = VerdeClinica
                    )
                    acoesExtras()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Fechar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = VerdeClinica
                )
            }
        }
    )
}

@Composable
fun ItemDetalhe(
    rotulo: String,
    valor: String,
    corTextoValor: Color = TextoPrincipal
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            style = MaterialTheme.typography.bodyMedium,
            color = VerdeClinica
        )
        Text(
            text = valor.ifEmpty { "-" },
            style = MaterialTheme.typography.bodyMedium,
            color = corTextoValor
        )
    }
}