package edu.front.psi.clin.ui.componentes

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.FundoSucesso
import edu.front.psi.clin.ui.theme.VerdeSuave
import edu.front.psi.clin.ui.theme.VerdeSucesso
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.Res
import frontpsiclinic.shared.generated.resources.check_circle

@Composable
fun NotificacaoFlutuante(
    mensagem: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = !mensagem.isNullOrEmpty(),
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = FundoSucesso,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, VerdeSuave)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.check_circle),
                        contentDescription = null,
                        tint = VerdeSucesso,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = mensagem ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = VerdeSucesso
                    )
                }
            }
        }
    }
}