package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.CardFundoCinza
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.TextoPrincipal

@Composable
fun CardCabecalhoInfo(
    rotulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
        border = BorderStroke(1.dp, CardBordaCinza),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rotulo,
                style = MaterialTheme.typography.titleMedium,
                color = TextoPrincipal
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                color = TextoPrincipal
            )
        }
    }
}