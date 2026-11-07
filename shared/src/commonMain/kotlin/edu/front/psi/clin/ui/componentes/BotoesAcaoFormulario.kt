package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CinzaFundoBotao
import edu.front.psi.clin.ui.theme.CinzaTextoBotao
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun BotoesAcaoFormulario(
    onLimpar: (() -> Unit)? = null,
    onSalvar: () -> Unit,
    textoBotaoSalvar: String = "Gravar",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (onLimpar != null) {
                BotaoPadrao(
                    texto = "Limpar Campos",
                    onClick = onLimpar,
                    containerColor = CinzaFundoBotao,
                    contentColor = CinzaTextoBotao,
                    modifier = Modifier.weight(1f)
                )
            }

            BotaoPadrao(
                texto = textoBotaoSalvar,
                onClick = onSalvar,
                containerColor = VerdeClinica,
                contentColor = Branco,
                modifier = Modifier.weight(1f)
            )
        }
    }
}