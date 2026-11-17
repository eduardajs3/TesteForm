package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Agenda
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
    onSelecionarDia: (Int) -> Unit,
    onSelecionarAgendamento: (Agenda) -> Unit
) {
    val diasExibidos = if (telaPequena) {
        listOf(diasDaSemana[diaSelecionadoIndex])
    } else {
        diasDaSemana
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
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selecionado) VerdeClinica else Branco
                    ),
                    border = BorderStroke(1.dp, if (selecionado) VerdeClinica else CardBordaCinza),
                    modifier = Modifier.clickable { onSelecionarDia(index) }
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
                        val agendamentoNoHorario = agendamentos.firstOrNull { item ->
                            val partesDataHora = item.dataHoraInicio.split("T")
                            if (partesDataHora.size != 2) return@firstOrNull false
                            val dataIsoItem = partesDataHora[0]
                            val horaItem = partesDataHora[1].take(5)

                            dataIsoItem == dia.dataIso && horaItem == hora
                        }

                        // Removidos os .toLong() redundantes nas linhas 164, 167 e 170
                        val nomePaciente = pacientes.firstOrNull {
                            it.id == agendamentoNoHorario?.pacienteId
                        }?.nome ?: "Paciente"
                        val nomePsicologo = psicologos.firstOrNull {
                            it.id == agendamentoNoHorario?.psicologoId
                        }?.nome ?: "Psicólogo"
                        val nomeTipo = tiposAtendimento.firstOrNull {
                            it.id == agendamentoNoHorario?.tipoAtendimentoId
                        }?.nome ?: "Consulta"

                        CardAgendamentoGrade(
                            agendamento = agendamentoNoHorario,
                            nomePaciente = nomePaciente,
                            nomePsicologo = nomePsicologo,
                            nomeTipo = nomeTipo,
                            modifier = Modifier.weight(1f),
                            onClick = agendamentoNoHorario?.let {
                                { onSelecionarAgendamento(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}