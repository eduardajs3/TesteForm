package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.ui.theme.*

@Composable
fun CardAgendamentoGrade(
    agendamento: Agenda?,
    nomePaciente: String,
    nomePsicologo: String,
    nomeTipo: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val isOcupado = agendamento != null

    val corStatus = when (agendamento?.status?.uppercase()) {
        "CONFIRMADO", "REALIZADO", "AGENDADO" -> FundoSucesso
        "CANCELADO", "FALTOU" -> FundoStatusNegativo
        else -> LinhaDivisoria.copy(alpha = 0.4f)
    }

    val corTextoStatus = when (agendamento?.status?.uppercase()) {
        "CONFIRMADO", "REALIZADO", "AGENDADO" -> VerdeSucesso
        "CANCELADO", "FALTOU" -> TextoStatusNegativo
        else -> TextoSecundario
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = corStatus),
        modifier = modifier
            .height(78.dp)
            .border(
                width = 1.dp,
                color = if (isOcupado) corTextoStatus.copy(alpha = 0.35f) else CardBordaCinza.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (isOcupado && onClick != null) Modifier.clickable { onClick() } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (agendamento != null) {
                Text(
                    text = nomePaciente,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = corTextoStatus,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = nomePsicologo,
                    style = MaterialTheme.typography.bodySmall,
                    color = corTextoStatus.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$nomeTipo • ${agendamento.status}",
                    style = MaterialTheme.typography.labelSmall,
                    color = corTextoStatus.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "Livre",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario.copy(alpha = 0.45f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}