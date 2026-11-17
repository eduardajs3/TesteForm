package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BotaoPadrao
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira

@Composable
internal fun AgendaDetalhesDialog(
    agendamento: Agenda,
    pacienteNome: String,
    psicologoNome: String,
    salaNome: String,
    tipoNome: String,
    convenioNome: String,
    onDismiss: () -> Unit,
    onEditar: () -> Unit,
    onApagar: () -> Unit
) {
    var confirmarExclusao by remember(agendamento.id) { mutableStateOf(false) }

    ModalDetalhesBase(
        titulo = "Detalhes do agendamento",
        onDismiss = onDismiss,
        conteudo = {
            ItemDetalhe(rotulo = "Paciente", valor = pacienteNome)
            ItemDetalhe(rotulo = "Psicólogo(a)", valor = psicologoNome)
            ItemDetalhe(rotulo = "Sala", valor = salaNome)
            ItemDetalhe(rotulo = "Atendimento", valor = tipoNome)
            ItemDetalhe(rotulo = "Convênio", valor = convenioNome)
            ItemDetalhe(
                rotulo = "Início",
                valor = formatarDataHoraIsoParaBrasileira(agendamento.dataHoraInicio)
            )
            ItemDetalhe(
                rotulo = "Fim",
                valor = formatarDataHoraIsoParaBrasileira(agendamento.dataHoraFim)
            )
            ItemDetalhe(rotulo = "Status", valor = agendamento.status)
            ItemDetalhe(rotulo = "Observações", valor = agendamento.observacoes.orEmpty())
        },
        acoesExtras = {
            OutlinedButton(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar")
            }
            BotaoPadrao(
                texto = "Apagar",
                onClick = { confirmarExclusao = true },
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        }
    )

    if (confirmarExclusao) {
        AppDeleteModal(
            title = "Excluir agendamento",
            message = "Deseja realmente excluir este agendamento?",
            onDismiss = { confirmarExclusao = false },
            onConfirm = {
                confirmarExclusao = false
                onApagar()
            }
        )
    }
}
