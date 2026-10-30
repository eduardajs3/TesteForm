package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.BloqueioAgenda
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
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
    
    // Estado para controlar qual item vai abrir o modal de exclusão
    val itemParaExcluir = remember { mutableStateOf<BloqueioAgenda?>(null) }

    // Definindo as colunas e as proporções de largura na tabela
    val colunas = listOf(
        "Psicólogo(a)" to 1.5f,
        "Início" to 1.2f,
        "Fim" to 1.2f,
        "Motivo" to 1.5f
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
            onEditar = vm::editar,
            // Guarda o item para abrir o modal de exclusão personalizado
            onApagar = { bloqueio -> 
                itemParaExcluir.value = bloqueio 
            },
            placeholderPesquisa = "Buscar por psicólogo ou motivo..."
        ) { bloqueio ->
            CelulaTabelaTexto(
                text = bloqueio.psicologo,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = bloqueio.dataHoraInicio,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = bloqueio.dataHoraFim,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = bloqueio.motivo,
                weight = 1.5f
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