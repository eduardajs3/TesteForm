
package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.utils.nomesDosMeses
import frontpsiclinic.shared.generated.resources.Res
import frontpsiclinic.shared.generated.resources.keyboard_arrow_down
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun SeletorMesAgenda(
    mesAtual: Int,
    anoAtual: Int,
    menuAberto: Boolean,
    onAbrirMenu: () -> Unit,
    onFecharMenu: () -> Unit,
    onSelecionarMes: (Int) -> Unit
) {
    Box {
        OutlinedButton(
            onClick = onAbrirMenu,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeClinica),
            border = BorderStroke(1.dp, CardBordaCinza),
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = nomesDosMeses[mesAtual - 1],
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(Res.drawable.keyboard_arrow_down),
                contentDescription = "Selecionar Mês",
                tint = VerdeClinica,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = menuAberto,
            onDismissRequest = onFecharMenu,
            containerColor = Branco
        ) {
            nomesDosMeses.forEachIndexed { index, mesNome ->
                val isMesAtual = index + 1 == mesAtual
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$mesNome $anoAtual",
                            fontWeight = if (isMesAtual) FontWeight.Bold else FontWeight.Normal,
                            color = if (isMesAtual) VerdeClinica else TextoPrincipal
                        )
                    },
                    onClick = {
                        onFecharMenu()
                        onSelecionarMes(index + 1)
                    }
                )
            }
        }
    }
}
 