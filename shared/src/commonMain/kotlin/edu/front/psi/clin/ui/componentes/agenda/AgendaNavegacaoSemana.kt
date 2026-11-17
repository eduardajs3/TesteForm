package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.utils.DataSimples
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AgendaNavegacaoSemana(
    segundaAtual: DataSimples,
    tituloSemana: String,
    telaPequena: Boolean,
    menuMesesAberto: Boolean,
    onSemanaAnterior: () -> Unit,
    onProximaSemana: () -> Unit,
    onAbrirMeses: () -> Unit,
    onFecharMeses: () -> Unit,
    onSelecionarMes: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Branco),
            modifier = Modifier.weight(1.5f),
            border = BorderStroke(1.dp, CardBordaCinza)
        ) {
            if (telaPequena) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onSemanaAnterior) {
                            Icon(
                                painter = painterResource(Res.drawable.keyboard_arrow_left),
                                contentDescription = "Semana Anterior",
                                tint = VerdeClinica,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = tituloSemana,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal,
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = onProximaSemana) {
                            Icon(
                                painter = painterResource(Res.drawable.keyboard_arrow_right),
                                contentDescription = "Próxima Semana",
                                tint = VerdeClinica,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    SeletorMesAgenda(
                        mesAtual = segundaAtual.mes,
                        anoAtual = segundaAtual.ano,
                        menuAberto = menuMesesAberto,
                        onAbrirMenu = onAbrirMeses,
                        onFecharMenu = onFecharMeses,
                        onSelecionarMes = onSelecionarMes
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onSemanaAnterior) {
                        Icon(
                            painter = painterResource(Res.drawable.keyboard_arrow_left),
                            contentDescription = "Semana Anterior",
                            tint = VerdeClinica,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = tituloSemana,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )
                        SeletorMesAgenda(
                            mesAtual = segundaAtual.mes,
                            anoAtual = segundaAtual.ano,
                            menuAberto = menuMesesAberto,
                            onAbrirMenu = onAbrirMeses,
                            onFecharMenu = onFecharMeses,
                            onSelecionarMes = onSelecionarMes
                        )
                    }

                    IconButton(onClick = onProximaSemana) {
                        Icon(
                            painter = painterResource(Res.drawable.keyboard_arrow_right),
                            contentDescription = "Próxima Semana",
                            tint = VerdeClinica,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
