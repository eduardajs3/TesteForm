package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun BotaoFiltroDia(
    titulo: String,
    quantidade: Int,
    selecionado: Boolean,
    temAtendimento: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (selecionado) VerdeClinica else Branco,
        border = BorderStroke(
            width = 1.dp,
            color = if (selecionado) VerdeClinica else CardBordaCinza
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            selecionado -> Branco
                            temAtendimento -> VerdeClinica
                            else -> CardBordaCinza
                        }
                    )
            )
            Text(
                text = if (quantidade > 0) "$titulo ($quantidade)" else titulo,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selecionado) FontWeight.SemiBold else FontWeight.Normal,
                color = when {
                    selecionado -> Branco
                    temAtendimento -> TextoPrincipal
                    else -> TextoSecundario
                }
            )
        }
    }
}