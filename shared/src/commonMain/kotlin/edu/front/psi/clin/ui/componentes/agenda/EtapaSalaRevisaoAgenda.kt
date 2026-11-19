package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.*
import edu.front.psi.clin.ui.componentes.BotaoPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
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
        }
    }

    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onVoltar,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Voltar")
        }
        BotaoPadrao(
            texto = if (isEdicao) "Salvar alterações" else "Agendar",
            onClick = {
                if (vm.validarSalaEHorario(salasDisponiveis, horarioSelecionado)) {
                    exibirConfirmacao = true
                }
            },
            modifier = Modifier.weight(1f),
            containerColor = VerdeClinica
        )
    }

    if (exibirConfirmacao) {
        ModalDetalhesBase(
            titulo = if (isEdicao) "Confirmar alterações" else "Confirmar agendamento",
            onDismiss = { exibirConfirmacao = false },
            conteudo = {
                Text(
                    text = "Confira os detalhes antes de confirmar:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPrincipal
                )
                HorizontalDivider(color = CardBordaCinza.copy(alpha = 0.5f))

                ItemDetalhe(rotulo = "Paciente", valor = pacienteSelecionado?.nome)
                ItemDetalhe(rotulo = "Psicólogo", valor = psicologoSelecionado?.nome)
                ItemDetalhe(rotulo = "Atendimento", valor = tipoSelecionado?.nome)
                ItemDetalhe(rotulo = "Convênio", valor = convenioSelecionado?.nome)
                ItemDetalhe(
                    rotulo = "Data",
                    valor = dataSelecionada?.let {
                        "${it.day.toString().padStart(2, '0')}/${it.monthNumber.toString().padStart(2, '0')}/${it.year}"
                    }
                )
                ItemDetalhe(
                    rotulo = "Horário",
                    valor = horarioSelecionado?.let { "${it.inicio}–${it.fim}" }
                )
                ItemDetalhe(rotulo = "Sala", valor = salaSelecionada?.nome)
                ItemDetalhe(rotulo = "Status", valor = vm.status.valor.value)
                ItemDetalhe(rotulo = "Observações", valor = vm.observacoes.valor.value.ifEmpty { null })
            },
            acoesExtras = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { exibirConfirmacao = false },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Revisar")
                    }
                    BotaoPadrao(
                        texto = if (isEdicao) "Salvar alterações" else "Agendar",
                        onClick = {
                            if (vm.validarSalaEHorario(salasDisponiveis, horarioSelecionado)) {
                                exibirConfirmacao = false
                                onAgendar()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        containerColor = VerdeClinica
                    )
                }
            }
        )
    }
}