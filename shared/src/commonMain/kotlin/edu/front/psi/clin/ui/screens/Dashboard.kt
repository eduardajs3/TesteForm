package edu.front.psi.clin.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.ui.componentes.dashboard.CardAtalho
import edu.front.psi.clin.ui.componentes.dashboard.CardConsultaHoje
import edu.front.psi.clin.ui.componentes.dashboard.CardMetrica
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.DashboardViewModel
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun DashboardTela(
    innerPadding: PaddingValues,
    vm: DashboardViewModel,
    onNavegar: (Route) -> Unit = {},
    onAgendarConsulta: () -> Unit = { onNavegar(Route.Agenda) },
    podeCriarAgendamento: Boolean = true,
    podeCriarPaciente: Boolean = true,
    podeAcessarFinanceiro: Boolean = true,
    podeConfirmarAgendamento: Boolean = true
) {
    LaunchedEffect(vm) {
        vm.carregarDados()
    }

    val metricas by vm.metricas.collectAsState()
    val consultas by vm.consultasFiltradas.collectAsState()
    val filtroStatus by vm.filtroStatus.collectAsState()
    val carregando by vm.carregando.collectAsState()
    val exibirAcoesRapidas = podeCriarAgendamento || podeCriarPaciente || podeAcessarFinanceiro

    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        val telaPequena = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(if (telaPequena) 16.dp else 24.dp)
        ) {
            
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

            Spacer(modifier = Modifier.height(24.dp))

            val linhasMetricas = metricas.chunked(if (telaPequena) 1 else 2)
            linhasMetricas.forEachIndexed { index, linha ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    linha.forEach { metrica ->
                        CardMetrica(item = metrica, modifier = Modifier.weight(1f))
                    }
                    if (!telaPequena && linha.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                if (index < linhasMetricas.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (exibirAcoesRapidas) {
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

                if (telaPequena) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (podeCriarAgendamento) {
                            CardAtalho("Agendar Consulta", "Preencher dados", Res.drawable.calendar_month, Modifier.fillMaxWidth(), onAgendarConsulta)
                        }
                        if (podeCriarPaciente) {
                            CardAtalho("Novo Paciente", "Cadastrar ou consultar", Res.drawable.group_24px, Modifier.fillMaxWidth()) { onNavegar(Route.Paciente) }
                        }
                        if (podeAcessarFinanceiro) {
                            CardAtalho("Financeiro", "Pagamentos e recibos", Res.drawable.description_24px, Modifier.fillMaxWidth()) { onNavegar(Route.Pagamento) }
                        }
                    }
                }
                else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (podeCriarAgendamento) {
                            CardAtalho("Agendar Consulta", "Preencher dados", Res.drawable.calendar_month, Modifier.weight(1f), onAgendarConsulta)
                        }
                        if (podeCriarPaciente) {
                            CardAtalho("Novo Paciente", "Cadastrar ou consultar", Res.drawable.group_24px, Modifier.weight(1f)) { onNavegar(Route.Paciente) }
                        }
                        if (podeAcessarFinanceiro) {
                            CardAtalho("Financeiro", "Pagamentos e recibos", Res.drawable.description_24px, Modifier.weight(1f)) { onNavegar(Route.Pagamento) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            Text(
                text = "Próximas Consultas de Hoje",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (carregando && metricas.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VerdeClinica)
                }
            } else if (consultas.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Nenhuma consulta encontrada.", color = TextoSecundario)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    consultas.forEach { consulta ->
                        CardConsultaHoje(
                            consulta = consulta,
                            onConfirmar = { vm.confirmarConsulta(consulta.id) },
                            permitirConfirmacao = podeConfirmarAgendamento
                        )
                    }
                }
            }
        }
    }
}