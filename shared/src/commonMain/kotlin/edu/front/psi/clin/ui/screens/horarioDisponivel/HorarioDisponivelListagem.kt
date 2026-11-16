package edu.front.psi.clin.ui.screens.horarioDisponivel

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

@Composable
fun HorarioDisponivelListagem(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel
) {
    val lista = vm.horariosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val psicologos = vm.psicologos.collectAsState().value

    val horarioSelecionado = remember { mutableStateOf<HorarioDisponivel?>(null) }
    val horarioParaExcluir = remember { mutableStateOf<HorarioDisponivel?>(null) }

    val colunas = listOf(
        "Psicólogo(a)" to 1.4f,
        "Dia da Semana" to 1.1f,
        "Horário" to 1.2f,
        "Descrição" to 1.3f,
        "Status" to 0.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Horários Disponíveis",
            textoBotaoNovo = "Novo Horário",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoHorario,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { horario -> horarioSelecionado.value = horario },
            onEditar = vm::editar,
            onApagar = { horario -> horarioParaExcluir.value = horario },
            textoCompacto = { horario ->
                horario.psicologo
                    ?: psicologos.firstOrNull { it.id == horario.psicologoId }?.nome
                    ?: "Psicólogo #${horario.psicologoId}"
            },
            placeholderPesquisa = "Buscar por psicólogo, dia ou horário..."
        ) { horario ->
            val isAtivo = horario.ativo
            val nomePsicologo = horario.psicologo
                ?: psicologos.firstOrNull { it.id == horario.psicologoId }?.nome
                ?: "Psicólogo #${horario.psicologoId}"

            CelulaTabelaTexto(
                text = nomePsicologo,
                weight = 1.4f,
                isBold = true
            )
            CelulaTabelaTexto(
                text = horario.diaSemana,
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = "${horario.horaInicio} às ${horario.horaFim}",
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = horario.descricao ?: "",
                weight = 1.3f
            )

            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        horarioSelecionado.value?.let { horario ->
            val isAtivo = horario.ativo
            val nomePsicologo = horario.psicologo
                ?: psicologos.firstOrNull { it.id == horario.psicologoId }?.nome
                ?: "Psicólogo #${horario.psicologoId}"
            ModalDetalhesBase(
                titulo = "Detalhes do Horário",
                onDismiss = { horarioSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Psicólogo(a)", valor = nomePsicologo)
                    ItemDetalhe(rotulo = "Dia da semana", valor = horario.diaSemana)
                    ItemDetalhe(rotulo = "Horário de início", valor = horario.horaInicio)
                    ItemDetalhe(rotulo = "Horário de fim", valor = horario.horaFim)
                    ItemDetalhe(rotulo = "Descrição", valor = horario.descricao ?: "Nenhuma")
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo"
                    )
                }
            )
        }

        horarioParaExcluir.value?.let { horario ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir este horário disponível? Esta ação não pode ser desfeita.",
                onDismiss = { horarioParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(horario)
                    horarioParaExcluir.value = null
                }
            )
        }
    }
}