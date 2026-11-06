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

// Estrutura pura em Kotlin para manipulação de calendário (100% KMP compatível)
private fun diasNoMes(mes: Int, ano: Int): Int = when (mes) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) 29 else 28
    else -> 30
}

private fun diaDaSemanaSakamoto(ano: Int, mes: Int, dia: Int): Int {
    // 0 = Domingo, 1 = Segunda, 2 = Terça, 3 = Quarta, 4 = Quinta, 5 = Sexta, 6 = Sábado
    val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    var y = ano
    if (mes < 3) y -= 1
    return (y + y / 4 - y / 100 + y / 400 + t[mes - 1] + dia) % 7
}

private data class DataSimples(val dia: Int, val mes: Int, val ano: Int) {
    fun formatarDiaMes(): String = "${dia.toString().padStart(2, '0')}/${mes.toString().padStart(2, '0')}"

    fun adicionarDias(qtd: Int): DataSimples {
        var d = dia + qtd
        var m = mes
        var a = ano
        while (d > diasNoMes(m, a)) {
            d -= diasNoMes(m, a)
            m++
            if (m > 12) {
                m = 1
                a++
            }
        }
        while (d < 1) {
            m--
            if (m < 1) {
                m = 12
                a--
            }
            d += diasNoMes(m, a)
        }
        return DataSimples(d, m, a)
    }
}

private fun obterPrimeiraSegundaDoMes(ano: Int, mes: Int): DataSimples {
    val dw = diaDaSemanaSakamoto(ano, mes, 1)
    val diasParaSegunda = when (dw) {
        1 -> 0
        0 -> 1
        else -> 8 - dw
    }
    return DataSimples(1 + diasParaSegunda, mes, ano)
}

private val nomesDosMeses = listOf(
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
)

@Composable
fun AgendaListagem(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val agendamentos by vm.agendamentosFiltrados.collectAsState()
    val psicologoFiltroSelecionado by vm.psicologoFiltro.collectAsState()
    var agendamentoSelecionado by remember { mutableStateOf<Agenda?>(null) }

    // Inicia na semana de 16/03/2026 (Segunda-feira)
    var segundaAtual by remember { mutableStateOf(DataSimples(16, 3, 2026)) }
    var menuMesesAberto by remember { mutableStateOf(false) }

    // Calcula os 5 dias da semana (Segunda a Sexta)
    val sextaAtual = remember(segundaAtual) { segundaAtual.adicionarDias(4) }

    val diasDaSemana = remember(segundaAtual) {
        listOf(
            DiaSemanaAgenda("Segunda", segundaAtual.formatarDiaMes()),
            DiaSemanaAgenda("Terça", segundaAtual.adicionarDias(1).formatarDiaMes()),
            DiaSemanaAgenda("Quarta", segundaAtual.adicionarDias(2).formatarDiaMes()),
            DiaSemanaAgenda("Quinta", segundaAtual.adicionarDias(3).formatarDiaMes()),
            DiaSemanaAgenda("Sexta", segundaAtual.adicionarDias(4).formatarDiaMes())
        )
    }

    val tituloSemana = remember(segundaAtual, sextaAtual) {
        val nomeMesInicio = nomesDosMeses[segundaAtual.mes - 1]
        val nomeMesFim = nomesDosMeses[sextaAtual.mes - 1]
        if (segundaAtual.mes == sextaAtual.mes) {
            "Semana de ${segundaAtual.dia.toString().padStart(2, '0')} a ${sextaAtual.dia.toString().padStart(2, '0')} de $nomeMesInicio de ${segundaAtual.ano}"
        } else {
            "Semana de ${segundaAtual.dia.toString().padStart(2, '0')} de $nomeMesInicio a ${sextaAtual.dia.toString().padStart(2, '0')} de $nomeMesFim de ${segundaAtual.ano}"
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
                // Card de Navegação de Semanas com Seletor de Mês
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Branco),
                    modifier = Modifier.weight(1.5f),
                    border = BorderStroke(1.dp, CardBordaCinza)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { segundaAtual = segundaAtual.adicionarDias(-7) }) {
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

                            // Menu Dropdown para Escolher o Mês Diretamente
                            Box {
                                OutlinedButton(
                                    onClick = { menuMesesAberto = true },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = VerdeClinica
                                    ),
                                    border = BorderStroke(1.dp, CardBordaCinza),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = nomesDosMeses[segundaAtual.mes - 1],
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = VerdeClinica
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        painter = painterResource(Res.drawable.keyboard_arrow_down),
                                        contentDescription = "Selecionar Mês",
                                        tint = VerdeClinica,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuMesesAberto,
                                    onDismissRequest = { menuMesesAberto = false },
                                    containerColor = Branco
                                ) {
                                    nomesDosMeses.forEachIndexed { index, mesNome ->
                                        val isMesAtual = (index + 1) == segundaAtual.mes
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "$mesNome ${segundaAtual.ano}",
                                                    fontWeight = if (isMesAtual) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isMesAtual) VerdeClinica else TextoPrincipal
                                                )
                                            },
                                            onClick = {
                                                menuMesesAberto = false
                                                segundaAtual = obterPrimeiraSegundaDoMes(segundaAtual.ano, index + 1)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        IconButton(onClick = { segundaAtual = segundaAtual.adicionarDias(7) }) {
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
                                val agendamentoNoHorario = agendamentos.firstOrNull {
                                    it.dataHoraInicio.contains(dia.dataFormatada) && it.dataHoraInicio.contains(hora)
                                }

                                // Somente as consultas agendadas possuem ação de clique.
                                // Dias e slots vagos são desabilitados, sem ação de clique.
                                CardAgendamentoGrade(
                                    dia = dia,
                                    agendamento = agendamentoNoHorario,
                                    modifier = Modifier.weight(1f),
                                    onClick = if (agendamentoNoHorario != null) {
                                        { agendamentoSelecionado = agendamentoNoHorario }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }
        }

        // Modal de Detalhes da Consulta Marcada
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
                        // Botão de Confirmar Presença
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
    onClick: (() -> Unit)? = null
) {
    val isOcupado = agendamento != null

    val corStatus = when (agendamento?.status?.lowercase()) {
        "confirmado", "realizado" -> FundoSucesso
        "pendente" -> Color(0xFFFEF3C7)
        "cancelado" -> Color(0xFFFEE2E2)
        else -> LinhaDivisoria.copy(alpha = 0.4f)
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
                // Slot vago: completamente desabilitado, sem função de clique
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
