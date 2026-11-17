package edu.front.psi.clin.ui.componentes.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.ConsultaHojeItem

@Composable
fun CardConsultaHoje(
    consulta: ConsultaHojeItem,
    onConfirmar: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val telaPequena = maxWidth < 600.dp

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
            border = BorderStroke(1.dp, CardBordaCinza),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (telaPequena) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CremeCampo,
                            border = BorderStroke(1.dp, CardBordaCinza)
                        ) {
                            Text(
                                text = consulta.horario,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = VerdeClinica
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = consulta.paciente,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            BadgeStatus(
                                texto = consulta.status,
                                isAtivo = consulta.status == "Agendado" || consulta.status == "Confirmado"
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${consulta.psicologo} • ${consulta.sala} • ${consulta.tipo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario
                            )
                        }
                    }

                    if (consulta.status == "Agendado") {
                        OutlinedButton(
                            onClick = onConfirmar,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, VerdeClinica),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = VerdeClinica
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "Confirmar",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CremeCampo,
                            border = BorderStroke(1.dp, CardBordaCinza)
                        ) {
                            Text(
                                text = consulta.horario,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = VerdeClinica
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = consulta.paciente,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal
                                )
                                BadgeStatus(
                                    texto = consulta.status,
                                    isAtivo = consulta.status == "Agendado" || consulta.status == "Confirmado"
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${consulta.psicologo} • ${consulta.sala} • ${consulta.tipo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario
                            )
                        }
                    }

                    if (consulta.status == "Agendado") {
                        OutlinedButton(
                            onClick = onConfirmar,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, VerdeClinica),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = VerdeClinica
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Confirmar",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}