package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.Recepcionista
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
import edu.front.psi.clin.ui.components.ItemDetalhe
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.components.ModalDetalhesBase
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

@Composable
fun RecepcionistaListagem(
    innerPadding: PaddingValues,
    vm: RecepcionistaViewModel
) {
    val lista = vm.recepcionistasFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val recepcionistaSelecionada = remember { mutableStateOf<Recepcionista?>(null) }
    
    // Estado para controlar qual recepcionista vai abrir o modal de exclusão
    val recepcionistaParaExcluir = remember { mutableStateOf<Recepcionista?>(null) }

    // Mapeamento das colunas da tabela com suas proporções de largura
    val colunas = listOf(
        "Nome" to 1.5f,
        "CPF" to 1.3f,
        "Telefone" to 1.2f,
        "Setor" to 1.2f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Recepcionistas",
            textoBotaoNovo = "Nova Recepcionista",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novaRecepcionista,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { recepcionista -> recepcionistaSelecionada.value = recepcionista },
            onEditar = vm::editar,
            onApagar = { recepcionista -> recepcionistaParaExcluir.value = recepcionista },
            placeholderPesquisa = "Buscar por nome, CPF ou setor..."
        ) { recepcionista ->
            CelulaTabelaTexto(
                text = recepcionista.nome,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = recepcionista.cpf,
                weight = 1.3f
            )
            CelulaTabelaTexto(
                text = recepcionista.telefone,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = recepcionista.setor,
                weight = 1.2f
            )
        }

        // Modal Reutilizável de Detalhes da Recepcionista
        recepcionistaSelecionada.value?.let { recepcionista ->
            ModalDetalhesBase(
                titulo = recepcionista.nome,
                onDismiss = { recepcionistaSelecionada.value = null },
                conteudo = {
                    ItemDetalhe("CPF", recepcionista.cpf)
                    ItemDetalhe("Setor", recepcionista.setor)
                    ItemDetalhe("Telefone", recepcionista.telefone)
                    ItemDetalhe("E-mail", recepcionista.email)
                }
            )
        }

        // Modal de Exclusão Reutilizável
        recepcionistaParaExcluir.value?.let { recepcionista ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir a recepcionista '${recepcionista.nome}'? Esta ação não pode ser desfeita.",
                onDismiss = { recepcionistaParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(recepcionista)
                    recepcionistaParaExcluir.value = null
                }
            )
        }
    }
}