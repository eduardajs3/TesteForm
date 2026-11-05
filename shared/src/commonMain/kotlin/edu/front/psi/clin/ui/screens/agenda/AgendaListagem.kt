package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.BorderStroke // <-- Import correto da borda
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
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

import frontpsiclinic.shared.generated.resources.*

import edu.front.psi.clin.Agenda
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.components.CabecalhoTela
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

private val CorTextoPrincipal = Color(0xFF1F2937)
private val CorBordaCard = Color(0xFFE5E7EB)

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
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, CorBordaCard)
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CorTextoPrincipal
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
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Gray,
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
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "Consulta Agendada",
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica,
                        fontSize = 20.sp
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
                                painter = painterResource(Res.drawable.edit_square_24px), // Verifique se o XML chama 'edit' ou 'edit_24px'
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
                                painter = painterResource(Res.drawable.delete_24px), // Verifique se o XML chama 'delete' ou 'delete_24px'
                                contentDescription = "Excluir",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { agendamentoSelecionado = null }) {
                        Text("Fechar", color = Color.Gray)
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
        "confirmado", "realizado" -> Color(0xFFDCFCE7)
        "pendente" -> Color(0xFFFEF3C7)
        "cancelado" -> Color(0xFFFEE2E2)
        else -> Color(0xFFF3F4F6)
    }

    val corTextoStatus = when (agendamento?.status?.lowercase()) {
        "confirmado", "realizado" -> Color(0xFF166534)
        "pendente" -> Color(0xFF92400E)
        "cancelado" -> Color(0xFF991B1B)
        else -> Color.Gray
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (agendamento != null) corStatus else Color.White
        ),
        modifier = modifier
            .height(72.dp)
            .border(
                width = 1.dp,
                color = if (agendamento != null) corTextoStatus.copy(alpha = 0.3f) else CorBordaCard,
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = corTextoStatus,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = agendamento.psicologo,
                    fontSize = 11.sp,
                    color = corTextoStatus.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "+ Livre",
                    fontSize = 11.sp,
                    color = Color.LightGray,
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
            fontWeight = FontWeight.Bold,
            color = VerdeClinica,
            fontSize = 14.sp
        )
        Text(
            text = valor.ifEmpty { "-" },
            color = CorTextoPrincipal,
            fontSize = 14.sp
        )
    }
}