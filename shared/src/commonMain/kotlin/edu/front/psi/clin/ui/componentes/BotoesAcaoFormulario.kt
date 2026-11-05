package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CinzaFundoBotao
import edu.front.psi.clin.ui.theme.CinzaTextoBotao
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun BotoesAcaoFormulario(
    onLimpar: () -> Unit,
    onSalvar: () -> Unit,
    textoBotaoSalvar: String = "Gravar",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onLimpar,
            colors = ButtonDefaults.buttonColors(
                containerColor = CinzaFundoBotao,
                contentColor = CinzaTextoBotao
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Limpar Campos",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = onSalvar,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClinica,
                contentColor = Branco
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = textoBotaoSalvar,
                style = MaterialTheme.typography.bodyLarge,
                color = Branco
            )
        }
    }
}