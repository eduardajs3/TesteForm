package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun CabecalhoTela(
    titulo: String,
    subtitulo: String? = null,
    textoBotao: String,
    iconeBotao: Painter,
    onClickBotao: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                color = TextoPrincipal
            )
            if (!subtitulo.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        BotaoPadrao(
            texto = textoBotao,
            onClick = onClickBotao,
            icone = iconeBotao,
            containerColor = VerdeClinica,
            contentColor = Branco,
            modifier = Modifier.wrapContentWidth()
        )
    }
}