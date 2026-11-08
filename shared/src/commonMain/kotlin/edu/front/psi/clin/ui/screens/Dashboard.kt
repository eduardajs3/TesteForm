package edu.front.psi.clin.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.ConsultaHojeItem
import edu.front.psi.clin.ui.viewmodel.DashboardViewModel
import edu.front.psi.clin.ui.viewmodel.MetricaCardItem
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DashboardTela(
    innerPadding: PaddingValues,
    vm: DashboardViewModel,
    onNavegar: (Route) -> Unit = {}
) {
    // Coleta reativa dos estados expostos pela ViewModel (StateFlow -> Compose State)
    val metricas by vm.metricas.collectAsState()
    val consultas by vm.consultasFiltradas.collectAsState()
    val filtroStatus by vm.filtroStatus.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // --- 1. CABEÇALHO DO PAINEL GERAL ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Painel Geral",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Visão geral e indicadores em tempo real da PsiClin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            // Tag elegante de data / status
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = VerdeClinica.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, VerdeClinica.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.calendar_month),
                        contentDescription = null,
                        tint = VerdeClinica,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Hoje • Atendimento Clínico",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdeClinica
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. GRID DE CARDS DE MÉTRICAS (2x2) ---
        if (metricas.size >= 4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardMetrica(item = metricas[0], modifier = Modifier.weight(1f))
                CardMetrica(item = metricas[1], modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardMetrica(item = metricas[2], modifier = Modifier.weight(1f))
                CardMetrica(item = metricas[3], modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 3. AÇÕES RÁPIDAS (ATALHOS OPERACIONAIS) ---
        Text(
            text = "Ações Rápidas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextoPrincipal
        )
        Text(
            text = "Acesse com um clique os principais módulos do sistema",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CardAtalho(
                titulo = "Agendar Consulta",
                subtitulo = "Visualizar grade semanal",
                iconeRes = Res.drawable.calendar_month,
                modifier = Modifier.weight(1f),
                onClick = { onNavegar(Route.Agenda) }
            )
            CardAtalho(
                titulo = "Novo Paciente",
                subtitulo = "Cadastrar ou consultar",
                iconeRes = Res.drawable.group_24px,
                modifier = Modifier.weight(1f),
                onClick = { onNavegar(Route.Paciente) }
            )
            CardAtalho(
                titulo = "Evoluções",
                subtitulo = "Registros de atendimento",
                iconeRes = Res.drawable.assignment_24px,
                modifier = Modifier.weight(1f),
                onClick = { onNavegar(Route.EvolucaoClinica) }
            )
            CardAtalho(
                titulo = "Financeiro",
                subtitulo = "Pagamentos e recibos",
                iconeRes = Res.drawable.description_24px,
                modifier = Modifier.weight(1f),
                onClick = { onNavegar(Route.Pagamento) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. SEÇÃO: CONSULTAS DE HOJE ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Próximas Consultas de Hoje",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Text(
                    text = "${consultas.size} atendimento(s) listado(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }

            // Filtros rápidos por status
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Todas", "Confirmadas", "Pendentes").forEach { filtro ->
                    val selecionado = filtroStatus == filtro
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (selecionado) VerdeClinica else CremeCampo,
                        border = BorderStroke(1.dp, if (selecionado) VerdeClinica else CardBordaCinza),
                        modifier = Modifier.clickable { vm.selecionarFiltroStatus(filtro) }
                    ) {
                        Text(
                            text = filtro,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Medium,
                            color = if (selecionado) Branco else TextoPrincipal
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = { onNavegar(Route.Agenda) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Ver Agenda Completa",
                        color = VerdeClinica,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(Res.drawable.keyboard_arrow_right),
                        contentDescription = null,
                        tint = VerdeClinica,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (consultas.isEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
                border = BorderStroke(1.dp, CardBordaCinza),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma consulta encontrada para o filtro selecionado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                consultas.forEach { consulta ->
                    CardConsultaHoje(
                        consulta = consulta,
                        onAlternarConfirmacao = { vm.alternarConfirmacao(consulta.id) }
                    )
                }
            }
        }
    }
}

// --- SUBCOMPONENTES REUTILIZÁVEIS DO DASHBOARD ---

@Composable
private fun CardMetrica(
    item: MetricaCardItem,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
        border = BorderStroke(1.dp, CardBordaCinza),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextoSecundario
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VerdeClinica.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(item.iconeRes),
                        contentDescription = null,
                        tint = VerdeClinica,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = if (item.variacaoPositiva) VerdeSucesso else TextoSecundario,
                fontWeight = if (item.variacaoPositiva) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun CardAtalho(
    titulo: String,
    subtitulo: String,
    iconeRes: DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
        border = BorderStroke(1.dp, CardBordaCinza),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VerdeClinica.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconeRes),
                    contentDescription = null,
                    tint = VerdeClinica,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun CardConsultaHoje(
    consulta: ConsultaHojeItem,
    onAlternarConfirmacao: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
        border = BorderStroke(1.dp, CardBordaCinza),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
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
                // Horário em destaque
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

                // Detalhes do paciente e consulta
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
                            isAtivo = consulta.isConfirmada
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

            // Ação de confirmar / alterar status
            OutlinedButton(
                onClick = onAlternarConfirmacao,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    1.dp,
                    if (consulta.isConfirmada) CardBordaCinza else VerdeClinica
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (consulta.isConfirmada) TextoSecundario else VerdeClinica
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (consulta.isConfirmada) "Desmarcar" else "Confirmar",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
