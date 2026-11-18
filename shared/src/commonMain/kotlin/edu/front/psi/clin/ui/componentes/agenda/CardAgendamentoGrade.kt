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
    val status = agendamento?.status?.uppercase() ?: ""

    val corFundo = when (status) {
        "CONFIRMADO" -> FundoStatusConfirmado  
        "REALIZADO" -> FundoStatusRealizado   
        "AGENDADO" -> FundoStatusAgendado    
        "CANCELADO", "FALTOU" -> FundoStatusNegativo 
        else -> Branco 
    }

    val corTexto = when (status) {
        "CONFIRMADO" -> TextoStatusConfirmado
        "REALIZADO" -> TextoStatusRealizado
        "AGENDADO" -> TextoStatusAgendado
        "CANCELADO", "FALTOU" -> TextoStatusNegativo
        else -> TextoSecundario.copy(alpha = 0.45f)
    }
    val corBorda = when (status) {
        "CONFIRMADO" -> BordaStatusConfirmado
        "REALIZADO" -> BordaStatusRealizado
        "AGENDADO" -> BordaStatusAgendado
        "CANCELADO", "FALTOU" -> TextoStatusNegativo.copy(alpha = 0.5f)
        else -> CardBordaCinza.copy(alpha = 0.5f)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = corFundo),
        modifier = modifier
            .height(78.dp)
            .border(
                width = 1.dp,
                color = corBorda,
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
                    color = corTexto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = nomePsicologo,
                    style = MaterialTheme.typography.bodySmall,
                    color = corTexto.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$nomeTipo • ${agendamento.status}",
                    style = MaterialTheme.typography.labelSmall,
                    color = corTexto.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "Livre",
                    style = MaterialTheme.typography.bodySmall,
                    color = corTexto,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}