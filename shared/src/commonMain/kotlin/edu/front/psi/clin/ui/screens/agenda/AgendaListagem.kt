package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import frontpsiclinic.shared.generated.resources.*

import edu.front.psi.clin.Agenda
import edu.front.psi.clin.ui.componentes.CabecalhoTela
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.FundoSucesso
import edu.front.psi.clin.ui.theme.TextoInativo
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.theme.VerdeSucesso
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

data class DiaSemanaAgenda(
    val nomeDia: String,
    val dataFormatada: String
)

@Composable
fun AgendaListagem(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val agendamentos by vm.agendamentosFiltrados.collectAsState()
    var agendamentoSelecionado by remember { mutableStateOf<Agenda?>(null) }

    val diasDaSemana = listOf(
        DiaSemanaAgenda("Segunda", "16/03"),
        DiaSemanaAgenda("Terça", "17/03"),
        DiaSemanaAgenda("Quarta", "18/03"),
        DiaSemanaAgenda("Quinta", "19/03"),
        DiaSemanaAgenda("Sexta", "20/03")
    )

    val horariosSessao = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CabecalhoTela(
                titulo = "Agenda de Atendimentos",
                subtitulo = "Acompanhe e organize as consultas da semana",
                textoBotao = "Novo Agendamento",
                iconeBotao = painterResource(Res.drawable.add), 
                onClickBotao = vm::novoAgendamento
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Branco),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, CardBordaCinza)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Navegar semana anterior */ }) {
                        Icon(
                            painter = painterResource(Res.drawable.keyboard_arrow_left),
                            contentDescription = "Semana Anterior",
                            tint = VerdeClinica,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Semana de 16 a 20 de Março de 2026",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrincipal
                    )

                    IconButton(onClick = { /* Navegar próxima semana */ }) {
                        Icon(
                            painter = painterResource(Res.drawable.keyboard_arrow_right),
                            contentDescription = "Próxima Semana",
                            tint = VerdeClinica,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(horariosSessao) { hora ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = hora,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextoSecundario,
                            modifier = Modifier
                                .width(60.dp)
                                .padding(top = 12.dp)
                        )

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            diasDaSemana.forEach { dia ->
                                val agendamentoNoHorario = agendamentos.firstOrNull {
                                    it.dataHoraInicio.contains(hora) || it.dataHoraInicio.contains(dia.nomeDia)
                                }

                                CardAgendamentoGrade(
                                    dia = dia,
                                    agendamento = agendamentoNoHorario,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (agendamentoNoHorario != null) {
                                            agendamentoSelecionado = agendamentoNoHorario
                                        } else {
                                            vm.novoAgendamento()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        agendamentoSelecionado?.let { agendamento ->
            AlertDialog(
                onDismissRequest = { agendamentoSelecionado = null },
                containerColor = Branco,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "Consulta Agendada",
                        style = MaterialTheme.typography.titleLarge,
                        color = VerdeClinica
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ItemDetalhe("Paciente", agendamento.paciente)
                        ItemDetalhe("Psicólogo", agendamento.psicologo)
                        ItemDetalhe("Horário", agendamento.dataHoraInicio)
                        ItemDetalhe("Sala", agendamento.sala)
                        ItemDetalhe("Tipo", agendamento.tipoAtendimento)
                        ItemDetalhe("Status", agendamento.status)
                        ItemDetalhe("Observações", agendamento.observacoes)
                    }
                },
                confirmButton = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = {
                            val item = agendamento
                            agendamentoSelecionado = null
                            vm.editar(item)
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.edit_square_24px),
                                contentDescription = "Editar",
                                tint = VerdeClinica,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        IconButton(onClick = {
                            val item = agendamento
                            agendamentoSelecionado = null
                            vm.apagar(item)
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.delete_24px),
                                contentDescription = "Excluir",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { agendamentoSelecionado = null }) {
                        Text(
                            text = "Fechar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun CardAgendamentoGrade(
    dia: DiaSemanaAgenda,
    agendamento: Agenda?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val corStatus = when (agendamento?.status?.lowercase()) {
        "confirmado", "realizado" -> FundoSucesso
        "pendente" -> Color(0xFFFEF3C7)
        "cancelado" -> Color(0xFFFEE2E2)
        else -> Branco
    }

    val corTextoStatus = when (agendamento?.status?.lowercase()) {
        "confirmado", "realizado" -> VerdeSucesso
        "pendente" -> TextoInativo
        "cancelado" -> Color(0xFF991B1B)
        else -> TextoSecundario
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = corStatus
        ),
        modifier = modifier
            .height(72.dp)
            .border(
                width = 1.dp,
                color = if (agendamento != null) corTextoStatus.copy(alpha = 0.3f) else CardBordaCinza,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (agendamento != null) {
                Text(
                    text = agendamento.paciente,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = corTextoStatus,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = agendamento.psicologo,
                    style = MaterialTheme.typography.bodySmall,
                    color = corTextoStatus.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "+ Livre",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun ItemDetalhe(rotulo: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = VerdeClinica
        )
        Text(
            text = valor.ifEmpty { "-" },
            style = MaterialTheme.typography.bodyMedium,
            color = TextoPrincipal
        )
    }
}