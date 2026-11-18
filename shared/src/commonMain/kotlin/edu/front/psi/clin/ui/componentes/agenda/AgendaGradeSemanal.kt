package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.model.TipoAtendimento
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.utils.DiaSemanaAgenda

@Composable
internal fun AgendaGradeSemanal(
    diasDaSemana: List<DiaSemanaAgenda>,
    diaSelecionadoIndex: Int,
    telaPequena: Boolean,
    horariosSessao: List<String>,
    agendamentos: List<Agenda>,
    pacientes: List<Paciente>,
    psicologos: List<Psicologo>,
    tiposAtendimento: List<TipoAtendimento>,
    horariosDisponiveis: List<HorarioDisponivel>,
    psicologoFiltro: String,
    onSelecionarDia: (Int) -> Unit,
    onSelecionarAgendamento: (Agenda) -> Unit
) {
    val diasExibidos = if (telaPequena) {
        listOf(diasDaSemana[diaSelecionadoIndex])
    } else {
        diasDaSemana
    }

    fun diaSemanaParaIso(nomeDia: String): String = when (nomeDia.lowercase()) {
        "seg" -> "SEGUNDA"
        "ter" -> "TERCA"
        "qua" -> "QUARTA"
        "qui" -> "QUINTA"
        "sex" -> "SEXTA"
        "sáb", "sab" -> "SABADO"
        else -> "DOMINGO"
    }

    val psicologoObj = psicologos.firstOrNull { it.nome == psicologoFiltro }
    val horariosDoProfissional = if (psicologoFiltro == "Todos") {
        horariosDisponiveis.filter { it.ativo }
    } else {
        horariosDisponiveis.filter { it.ativo && it.psicologoId == psicologoObj?.id }
    }

    if (telaPequena) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            diasDaSemana.forEachIndexed { index, dia ->
                val selecionado = index == diaSelecionadoIndex
                val diaIso = diaSemanaParaIso(dia.nomeDia)
                val temExpedienteNoDia = horariosDoProfissional.any { it.diaSemana.equals(diaIso, ignoreCase = true) }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selecionado) VerdeClinica else Branco
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (selecionado) VerdeClinica else CardBordaCinza
                    ),
                    modifier = Modifier
                        .alpha(if (selecionado || temExpedienteNoDia) 1f else 0.4f)
                        .then(
                            if (temExpedienteNoDia || psicologoFiltro == "Todos") {
                                Modifier.clickable { onSelecionarDia(index) }
                            } else {
                                Modifier 
                            }
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia.nomeDia,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (selecionado) Branco else TextoPrincipal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dia.dataFormatada,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selecionado) Branco else TextoSecundario
                        )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(60.dp))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                diasDaSemana.forEachIndexed { index, dia ->
                    val selecionado = index == diaSelecionadoIndex
                    val diaIso = diaSemanaParaIso(dia.nomeDia)
                    val temExpedienteNoDia = horariosDoProfissional.any { it.diaSemana.equals(diaIso, ignoreCase = true) }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selecionado) VerdeClinica else Branco
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selecionado) VerdeClinica else CardBordaCinza
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .alpha(if (selecionado || temExpedienteNoDia) 1f else 0.4f)
                            .then(
                                if (temExpedienteNoDia || psicologoFiltro == "Todos") {
                                    Modifier.clickable { onSelecionarDia(index) }
                                } else {
                                    Modifier
                                }
                            )
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
                                color = if (selecionado) Branco else TextoPrincipal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dia.dataFormatada,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selecionado) Branco else TextoSecundario
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

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
                    diasExibidos.forEach { dia ->
                        val agendamentosNoHorario = agendamentos.filter { item ->
                            val partesDataHora = item.dataHoraInicio.split("T")
                            if (partesDataHora.size != 2) return@filter false
                            val dataIsoItem = partesDataHora[0]
                            val horaItem = partesDataHora[1].take(5)

                            dataIsoItem == dia.dataIso && horaItem == hora
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (agendamentosNoHorario.isEmpty()) {
                                CardAgendamentoGrade(
                                    agendamento = null,
                                    nomePaciente = "",
                                    nomePsicologo = "",
                                    nomeTipo = "",
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = null
                                )
                            } else {
                                agendamentosNoHorario.forEach { agendamento ->
                                    val nomePaciente = pacientes.firstOrNull {
                                        it.id == agendamento.pacienteId
                                    }?.nome ?: "Paciente"
                                    val nomePsicologo = psicologos.firstOrNull {
                                        it.id == agendamento.psicologoId
                                    }?.nome ?: "Psicólogo"
                                    val nomeTipo = tiposAtendimento.firstOrNull {
                                        it.id == agendamento.tipoAtendimentoId
                                    }?.nome ?: "Consulta"

                                    CardAgendamentoGrade(
                                        agendamento = agendamento,
                                        nomePaciente = nomePaciente,
                                        nomePsicologo = nomePsicologo,
                                        nomeTipo = nomeTipo,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onSelecionarAgendamento(agendamento) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}