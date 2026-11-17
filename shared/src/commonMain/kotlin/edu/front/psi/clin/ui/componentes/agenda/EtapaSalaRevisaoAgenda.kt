package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.*
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import edu.front.psi.clin.ui.viewmodel.HorarioAgendamento
import kotlinx.datetime.LocalDate

@Composable
internal fun EtapaSalaRevisaoAgenda(
    vm: AgendaViewModel,
    isEdicao: Boolean,
    salasDisponiveis: List<SalaAtendimento>,
    salaSelecionada: SalaAtendimento?,
    pacienteSelecionado: Paciente?,
    psicologoSelecionado: Psicologo?,
    tipoSelecionado: TipoAtendimento?,
    convenioSelecionado: Convenio?,
    dataSelecionada: LocalDate?,
    horarioSelecionado: HorarioAgendamento?,
    onVoltar: () -> Unit,
    onAgendar: () -> Unit
) {
    var exibirConfirmacao by remember(vm.id.value) { mutableStateOf(false) }

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
                text = "Sala e confirmação",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            CampoDropdownPadrao(
                label = "Sala disponível *",
                valorSelecionado = salaSelecionada?.nome.orEmpty(),
                opcoes = salasDisponiveis.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    vm.salaId.value = salasDisponiveis
                        .firstOrNull { it.nome == nome }?.id
                }
            )
            if (horarioSelecionado == null || salasDisponiveis.isEmpty()) {
                Text(
                    text = if (horarioSelecionado == null) {
                        "Este horário não está mais disponível. Volte e selecione outro."
                    } else {
                        "Não há salas disponíveis neste horário. Volte e selecione outro horário."
                    },
                    color = TextoErro,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = vm.status.valor.value,
                opcoes = if (isEdicao) {
                    listOf("AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO", "FALTOU")
                } else {
                    listOf("AGENDADO", "CONFIRMADO")
                },
                onOpcaoSelecionada = { vm.status.definir(it) }
            )

            CampoTextoPadrao(
                value = vm.observacoes.valor.value,
                onValueChange = { vm.observacoes.definir(it) },
                label = "Observações (opcional)"
            )

            HorizontalDivider()
            Text(
                text = "Revise os dados do agendamento",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica
            )
            ResumoAgendamento("Paciente", pacienteSelecionado?.nome)
            ResumoAgendamento("Psicólogo", psicologoSelecionado?.nome)
            ResumoAgendamento("Atendimento", tipoSelecionado?.nome)
            ResumoAgendamento("Convênio", convenioSelecionado?.nome)
            ResumoAgendamento(
                "Data",
                dataSelecionada?.let {
                    "${it.dayOfMonth.toString().padStart(2, '0')}/${it.monthNumber.toString().padStart(2, '0')}/${it.year}"
                }
            )
            ResumoAgendamento(
                "Horário",
                horarioSelecionado?.let { "${it.inicio}–${it.fim}" }
            )
            ResumoAgendamento("Sala", salaSelecionada?.nome)
            ResumoAgendamento("Status", vm.status.valor.value)
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
            onClick = {
                if (
                    horarioSelecionado == null ||
                    salasDisponiveis.none { it.id == vm.salaId.value }
                ) {
                    vm.mensagem.value = if (horarioSelecionado == null) {
                        "Este horário não está mais disponível. Selecione outro."
                    } else {
                        "Não há sala disponível neste horário. Selecione outro horário."
                    }
                } else {
                    vm.mensagem.value = ""
                    exibirConfirmacao = true
                }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica)
        ) {
            Text(if (isEdicao) "Confirmar alterações" else "Confirmar agendamento")
        }
    }

    if (exibirConfirmacao) {
        AlertDialog(
            onDismissRequest = { exibirConfirmacao = false },
            title = { Text(if (isEdicao) "Confirmar alterações" else "Confirmar agendamento") },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Confira os detalhes antes de confirmar:")
                    HorizontalDivider()
                    ResumoAgendamento("Paciente", pacienteSelecionado?.nome)
                    ResumoAgendamento("Psicólogo", psicologoSelecionado?.nome)
                    ResumoAgendamento("Atendimento", tipoSelecionado?.nome)
                    ResumoAgendamento("Convênio", convenioSelecionado?.nome)
                    ResumoAgendamento(
                        "Data",
                        dataSelecionada?.let {
                            "${it.dayOfMonth.toString().padStart(2, '0')}/${it.monthNumber.toString().padStart(2, '0')}/${it.year}"
                        }
                    )
                    ResumoAgendamento(
                        "Horário",
                        horarioSelecionado?.let { "${it.inicio}–${it.fim}" }
                    )
                    ResumoAgendamento("Sala", salaSelecionada?.nome)
                    ResumoAgendamento("Status", vm.status.valor.value)
                    ResumoAgendamento(
                        "Observações",
                        vm.observacoes.valor.value.ifEmpty { null }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        exibirConfirmacao = false
                        onAgendar()
                    }
                ) {
                    Text(if (isEdicao) "Salvar alterações" else "Agendar")
                }
            },
            dismissButton = {
                TextButton(onClick = { exibirConfirmacao = false }) {
                    Text("Revisar")
                }
            }
        )
    }
}

@Composable
private fun ResumoAgendamento(rotulo: String, valor: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            fontWeight = FontWeight.SemiBold,
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = valor ?: "—",
            color = TextoPrincipal,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

 