package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import edu.front.psi.clin.ui.viewmodel.HorarioAgendamento
import kotlinx.datetime.LocalDate

@Composable
internal fun EtapaDataHorarioAgenda(
    vm: AgendaViewModel,
    horariosCadastrados: List<HorarioDisponivel>,
    datasDisponiveis: List<LocalDate>,
    dataSelecionada: LocalDate?,
    horariosDisponiveis: List<HorarioAgendamento>,
    horarioSelecionado: HorarioAgendamento?,
    onVoltar: () -> Unit,
    onEscolherSala: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Branco),
        border = BorderStroke(1.dp, CardBordaCinza),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Escolha uma data disponível",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                text = "Os dias exibidos consideram a agenda semanal do psicólogo e a duração do atendimento.",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )

            if (horariosCadastrados.isEmpty()) {
                Text(
                    text = "Não há horários semanais cadastrados. Selecione outro psicólogo ou atendimento.",
                    color = TextoErro,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (datasDisponiveis.isEmpty()) {
                Text(
                    text = "Não há datas disponíveis nos próximos 60 dias. Selecione outro psicólogo ou atendimento.",
                    color = TextoErro,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    datasDisponiveis.chunked(4).forEach { linhaDatas ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            linhaDatas.forEach { data ->
                                val selecionada = data == dataSelecionada
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selecionada) VerdeClinica else CremeCampo,
                                    border = BorderStroke(
                                        1.dp,
                                        if (selecionada) VerdeClinica else CardBordaCinza
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            vm.dataAtendimento.definir(data.toString())
                                            vm.horaInicio.limpar()
                                            vm.horaFim.limpar()
                                            vm.salaId.value = null
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = nomeDia(data),
                                            color = if (selecionada) Branco else TextoPrincipal,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${data.dayOfMonth.toString().padStart(2, '0')}/${data.monthNumber.toString().padStart(2, '0')}",
                                            color = if (selecionada) Branco else TextoSecundario,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                            repeat(4 - linhaDatas.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            if (dataSelecionada != null) {
                Text(
                    text = "Horários disponíveis",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                if (horariosDisponiveis.isEmpty()) {
                    Text(
                        text = "Não há horários livres para esta data. Selecione outra data.",
                        color = TextoErro,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        horariosDisponiveis.chunked(3).forEach { linhaHorarios ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                linhaHorarios.forEach { horario ->
                                    val selecionado = horario == horarioSelecionado
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (selecionado) VerdeClinica else CremeCampo,
                                        border = BorderStroke(
                                            1.dp,
                                            if (selecionado) VerdeClinica else CardBordaCinza
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                vm.horaInicio.definir(horario.inicio)
                                                vm.horaFim.definir(horario.fim)
                                                vm.salaId.value = null
                                            }
                                    ) {
                                        Text(
                                            text = "${horario.inicio}–${horario.fim}",
                                            modifier = Modifier.padding(10.dp),
                                            color = if (selecionado) Branco else TextoPrincipal,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                repeat(3 - linhaHorarios.size) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = "Os bloqueios de agenda não são verificados porque não há uma fonte persistida disponível no app.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onVoltar,
            modifier = Modifier.weight(1f)
        ) {
            Text("Voltar")
        }
        Button(
            onClick = onEscolherSala,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica)
        ) {
            Text("Escolher sala")
        }
    }
}

private fun nomeDia(data: LocalDate): String = when (data.dayOfWeek.ordinal + 1) {
    1 -> "Seg"
    2 -> "Ter"
    3 -> "Qua"
    4 -> "Qui"
    5 -> "Sex"
    6 -> "Sáb"
    else -> "Dom"
}
 