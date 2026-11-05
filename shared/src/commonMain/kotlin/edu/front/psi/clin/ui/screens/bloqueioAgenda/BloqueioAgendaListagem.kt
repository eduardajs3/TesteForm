package edu.front.psi.clin.ui.screens.bloqueioAgenda

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.BloqueioAgenda
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.BloqueioAgendaViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun BloqueioAgendaListagem(
    innerPadding: PaddingValues,
    vm: BloqueioAgendaViewModel
) {
    val lista = vm.bloqueiosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    
    // Estados para controlar o item selecionado para detalhes (botão do olho) e exclusão
    val itemSelecionado = remember { mutableStateOf<BloqueioAgenda?>(null) }
    val itemParaExcluir = remember { mutableStateOf<BloqueioAgenda?>(null) }

    // Definindo as colunas da tabela (Psicólogo, Início, Fim e Status)
    val colunas = listOf(
        "Psicólogo(a)" to 1.8f,
        "Início" to 1.3f,
        "Fim" to 1.3f,
        "Status" to 0.9f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Bloqueios de Agenda",
            textoBotaoNovo = "Novo Bloqueio",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoBloqueio,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { item -> itemSelecionado.value = item },
            onEditar = vm::editar,
            onApagar = { item -> 
                itemParaExcluir.value = item 
            },
            placeholderPesquisa = "Buscar por psicólogo..."
        ) { bloqueio ->
            val isAtivo = bloqueio.ativo

            CelulaTabelaTexto(
                text = bloqueio.psicologo,
                weight = 1.8f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = bloqueio.dataHoraInicio,
                weight = 1.3f
            )
            CelulaTabelaTexto(
                text = bloqueio.dataHoraFim,
                weight = 1.3f
            )
            
            // Célula de Status com badge visual na tabela
            Box(modifier = Modifier.weight(0.9f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        // Modal de Detalhes (ativado pelo botão do olho)
        itemSelecionado.value?.let { bloqueio ->
            val isAtivo = bloqueio.ativo
            ModalDetalhesBase(
                titulo = "Detalhes do Bloqueio",
                onDismiss = { itemSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Psicólogo(a)", valor = bloqueio.psicologo)
                    ItemDetalhe(rotulo = "Data e hora de início", valor = bloqueio.dataHoraInicio)
                    ItemDetalhe(rotulo = "Data e hora de fim", valor = bloqueio.dataHoraFim)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo",
                        corTextoValor = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E)
                    )
                }
            )
        }

        // Modal de Exclusão Reutilizável
        itemParaExcluir.value?.let { item ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir este registo? Esta ação não pode ser desfeita.",
                onDismiss = { itemParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(item)
                    itemParaExcluir.value = null
                }
            )
        }
    }
}