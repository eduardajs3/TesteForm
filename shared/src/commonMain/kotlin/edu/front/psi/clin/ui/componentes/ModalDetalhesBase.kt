package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun ModalDetalhesBase(
    titulo: String,
    onDismiss: () -> Unit,
    conteudo: @Composable ColumnScope.() -> Unit,
    acoesExtras: @Composable (ColumnScope.() -> Unit)? = null,
    acoesTopo: @Composable (RowScope.() -> Unit)? = null 
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Branco,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = VerdeClinica,
                    modifier = Modifier.weight(1f)
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (acoesTopo != null) {
                        acoesTopo()
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(Res.drawable.close),
                            contentDescription = "Fechar",
                            tint = TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                conteudo()

                if (acoesExtras != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    acoesExtras()
                }
            }
        },
        confirmButton = {}
    )
}