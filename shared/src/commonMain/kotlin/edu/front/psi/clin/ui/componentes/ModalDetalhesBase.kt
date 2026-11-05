package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.VerdeClinica

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
                fontWeight = FontWeight.Bold,
                color = VerdeClinica,
                fontSize = 18.sp
            ) 
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               

                if (acoesExtras != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ações Rápidas",
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica,
                        fontSize = 15.sp
                    )
                    acoesExtras()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = VerdeClinica)
            }
        }
    )
}


@Composable
fun ItemDetalhe(
    rotulo: String,
    valor: String,
    corTextoValor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color(0xFF1F2937)
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            fontWeight = FontWeight.Bold,
            color = VerdeClinica,
            fontSize = 14.sp
        )
        Text(
            text = valor.ifEmpty { "-" },
            color = corTextoValor,
            fontSize = 14.sp
        )
    }
}