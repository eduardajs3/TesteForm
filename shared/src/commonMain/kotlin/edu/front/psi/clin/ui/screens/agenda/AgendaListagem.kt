package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.Agenda
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun AgendaListagem(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val lista = vm.agendamentosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val agendamentoSelecionado = remember { mutableStateOf<Agenda?>(null) }

    // Proporções das colunas na tabela
    val colunas = listOf(
        "Paciente" to 1.5f,
        "Psicólogo" to 1.5f,
        "Início" to 1.2f,
        "Status" to 1.0f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Agenda",
            textoBotaoNovo = "Novo Agendamento",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoAgendamento,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { agendamento -> agendamentoSelecionado.value = agendamento },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por paciente, psicólogo ou status..."
        ) { agendamento ->
            Text(
                text = agendamento.paciente,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = agendamento.psicologo,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = agendamento.dataHoraInicio,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = agendamento.status,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.0f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Modal de Visualização de Detalhes do Agendamento
        agendamentoSelecionado.value?.let { agendamento ->
            AlertDialog(
                onDismissRequest = { agendamentoSelecionado.value = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "Detalhes do Agendamento",
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemDetalhe("Paciente", agendamento.paciente)
                        ItemDetalhe("Psicólogo", agendamento.psicologo)
                        ItemDetalhe("Sala", agendamento.sala)
                        ItemDetalhe("Tipo de Atendimento", agendamento.tipoAtendimento)
                        ItemDetalhe("Convênio", agendamento.convenio)
                        ItemDetalhe("Início", agendamento.dataHoraInicio)
                        ItemDetalhe("Fim", agendamento.dataHoraFim)
                        ItemDetalhe("Status", agendamento.status)
                        ItemDetalhe("Observações", agendamento.observacoes)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { agendamentoSelecionado.value = null }) {
                        Text(
                            text = "Fechar",
                            color = VerdeClinica,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
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
            color = CorTextoLinha,
            fontSize = 14.sp
        )
    }
}