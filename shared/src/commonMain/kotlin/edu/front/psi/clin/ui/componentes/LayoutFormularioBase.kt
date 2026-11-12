package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.utils.MensagemFeedbackUtils
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

@Composable
fun LayoutFormularioBase(
    titulo: String,
    subtitulo: String = "Preencha os dados abaixo.",
    mensagemFeedback: String = "",
    onVoltar: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onVoltar != null) {
                IconButton(onClick = onVoltar) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_back),
                        contentDescription = "Voltar",
                        tint = VerdeClinica,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = VerdeClinica
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        content()

        if (mensagemFeedback.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensagemFeedback,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (MensagemFeedbackUtils.ehErro(mensagemFeedback)) TextoErro else VerdeClinica
            )
        }
    }
}