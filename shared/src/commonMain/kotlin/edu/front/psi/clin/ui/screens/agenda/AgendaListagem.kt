package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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

import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.ui.componentes.CabecalhoTela
import edu.front.psi.clin.ui.theme.*
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
    val psicologoFiltroSelecionado by vm.psicologoFiltro.collectAsState()
    var agendamentoSelecionado by remember { mutableStateOf<Agenda?>(null) }

    // Controle de semanas
    var semanaOffset by remember { mutableStateOf(0) }

    val diasDaSemana = remember(semanaOffset) {
        when (semanaOffset) {
            -1 -> listOf(
                DiaSemanaAgenda("Segunda", "09/03"),
                DiaSemanaAgenda("Terça", "10/03"),
                DiaSemanaAgenda("Quarta", "11/03"),
                DiaSemanaAgenda("Quinta", "12/03"),
                DiaSemanaAgenda("Sexta", "13/03")
            )
            1 -> listOf(
                DiaSemanaAgenda("Segunda", "23/03"),
                DiaSemanaAgenda("Terça", "24/03"),
                DiaSemanaAgenda("Quarta", "25/03"),
                DiaSemanaAgenda("Quinta", "26/03"),
                DiaSemanaAgenda("Sexta", "27/03")
            )
            else -> listOf(
                DiaSemanaAgenda("Segunda", "16/03"),
                DiaSemanaAgenda("Terça", "17/03"),
                DiaSemanaAgenda("Quarta", "18/03"),
                DiaSemanaAgenda("Quinta", "19/03"),
                DiaSemanaAgenda("Sexta", "20/03")
            )
        }
    }

    val tituloSemana = remember(semanaOffset) {
        when (semanaOffset) {
            -1 -> "Semana de 09 a 13 de Março de 2026"
            1 -> "Semana de 23 a 27 de Março de 2026"
            else -> "Semana de 16 a 20 de Março de 2026"
        }
    }

    val listaPsicologos = listOf("Todos", "Dra. Ana Costa", "Dr. Roberto Alves", "Dra. Juliana Costa")
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

            Spacer(modifier = Modifier.height(16.dp))

            // Barra de Filtros e Navegação da Semana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card de Navegação de Semanas
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Branco),
                    modifier = Modifier.weight(1.4f),
                    border = BorderStroke(1.dp, CardBordaCinza)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { semanaOffset-- }) {
                            Icon(
                                painter = painterResource(Res.drawable.keyboard_arrow_left),
                                contentDescription = "Semana Anterior",
                                tint = VerdeClinica,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = tituloSemana,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )

                        IconButton(onClick = { semanaOffset++ }) {
                            Icon(
                                painter = painterResource(Res.drawable.keyboard_arrow_right),
                                contentDescription = "Próxima Semana",
                                tint = VerdeClinica,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Filtro rápido por Psicólogo
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Branco),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, CardBordaCinza)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Psicólogo:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = VerdeClinica
                        )

                        listaPsicologos.forEach { psi ->
                            val selecionado = psi == psicologoFiltroSelecionado
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (selecionado) VerdeClinica else LinhaDivisoria,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { vm.filtrarPorPsicologo(psi) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (psi == "Todos") "Todos" else psi.split(" ").getOrNull(1) ?: psi,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selecionado) Branco else TextoPrincipal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cabeçalho com os Dias da Semana
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Espaçamento para alinhar com a coluna de horários (60.dp)
                Spacer(modifier = Modifier.width(60.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    diasDaSemana.forEach { dia ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Branco),
                            border = BorderStroke(1.dp, CardBordaCinza),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = dia.nomeDia,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = dia.dataFormatada,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundario
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Grade de Horários e Consultas
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                .padding(top = 16.dp)
                        )

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            diasDaSemana.forEach { dia ->
                                // Busca precisa por data específica E horário
                                val agendamentoNoHorario = agendamentos.firstOrNull {
                                    it.dataHoraInicio.contains(dia.dataFormatada) && it.dataHoraInicio.contains(hora)
                                }

                                CardAgendamentoGrade(
                                    dia = dia,
                                    agendamento = agendamentoNoHorario,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (agendamentoNoHorario != null) {
                                            agendamentoSelecionado = agendamentoNoHorario
                                        } else {
                                            // Pré-preenche o formulário com o dia e o horário clicados!
                                            vm.novoAgendamentoComDataHora("${dia.dataFormatada}/2026", hora)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Modal de Detalhes da Consulta
        agendamentoSelecionado?.let { agendamento ->
            AlertDialog(
                onDismissRequest = { agendamentoSelecionado = null },
                containerColor = Branco,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Consulta Agendada",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = VerdeClinica
                        )

                        // Badge de Status
                        val isConfirmado = agendamento.status.equals("Confirmado", ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isConfirmado) FundoSucesso else Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = agendamento.status,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isConfirmado) VerdeSucesso else TextoInativo
                            )
                        }
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ItemDetalhe("Paciente", agendamento.paciente)
                        ItemDetalhe("Psicólogo", agendamento.psicologo)
                        ItemDetalhe("Data e Horário", agendamento.dataHoraInicio)
                        ItemDetalhe("Sala", agendamento.sala)
                        ItemDetalhe("Tipo de Atendimento", agendamento.tipoAtendimento)
                        ItemDetalhe("Convênio", agendamento.convenio)
                        ItemDetalhe("Observações", agendamento.observacoes)
                    }
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão de Confirmar / Alternar Status Rápido
                        if (!agendamento.status.equals("Confirmado", ignoreCase = true)) {
                            TextButton(onClick = {
                                vm.alterarStatus(agendamento, "Confirmado")
                                agendamentoSelecionado = null
                            }) {
                                Text("Confirmar Presença", color = VerdeSucesso, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Botão Editar
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

                        // Botão Excluir
                        IconButton(onClick = {
                            val item = agendamento
                            agendamentoSelecionado = null
                            vm.apagar(item)
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.delete_24px),
                                contentDescription = "Excluir",
                                tint = TextoErro,
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
        colors = CardDefaults.cardColors(containerColor = corStatus),
        modifier = modifier
            .height(78.dp)
            .border(
                width = 1.dp,
                color = if (agendamento != null) corTextoStatus.copy(alpha = 0.35f) else CardBordaCinza,
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
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = agendamento.psicologo,
                    style = MaterialTheme.typography.bodySmall,
                    color = corTextoStatus.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${agendamento.tipoAtendimento} • ${agendamento.status}",
                    style = MaterialTheme.typography.labelSmall,
                    color = corTextoStatus.copy(alpha = 0.7f),
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
