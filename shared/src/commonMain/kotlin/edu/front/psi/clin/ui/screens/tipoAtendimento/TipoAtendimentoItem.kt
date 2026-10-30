package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.TipoAtendimento
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
import edu.front.psi.clin.ui.components.ItemDetalhe
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.components.ModalDetalhesBase
import edu.front.psi.clin.ui.viewmodel.TipoAtendimentoViewModel

@Composable
fun TipoAtendimentoListagem(
    innerPadding: PaddingValues,
    vm: TipoAtendimentoViewModel
) {
    val lista = vm.tiposFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val tipoSelecionado = remember { mutableStateOf<TipoAtendimento?>(null) }
    
    // Estado para controlar qual tipo de atendimento vai abrir o modal de exclusão
    val tipoParaExcluir = remember { mutableStateOf<TipoAtendimento?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Tipo de Atendimento" to 1.6f,
        "Duração" to 1.1f,
        "Descrição" to 1.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Tipos de Atendimento",
            textoBotaoNovo = "Novo Tipo",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoTipo,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { tipo -> tipoSelecionado.value = tipo },
            onEditar = vm::editar,
            onApagar = { tipo -> tipoParaExcluir.value = tipo },
            placeholderPesquisa = "Buscar por tipo, duração ou descrição..."
        ) { tipo ->
            CelulaTabelaTexto(
                text = tipo.nome,
                weight = 1.6f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = "${tipo.duracao} min",
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = tipo.descricao,
                weight = 1.8f
            )
        }

        // Modal Reutilizável de Detalhes do Tipo de Atendimento
        tipoSelecionado.value?.let { tipo ->
            ModalDetalhesBase(
                titulo = tipo.nome,
                onDismiss = { tipoSelecionado.value = null },
                conteudo = {
                    ItemDetalhe("Duração", "${tipo.duracao} minutos")
                    ItemDetalhe("Descrição", tipo.descricao)
                }
            )
        }

        // Modal de Exclusão Reutilizável
        tipoParaExcluir.value?.let { tipo ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir o tipo '${tipo.nome}'? Esta ação não pode ser desfeita.",
                onDismiss = { tipoParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(tipo)
                    tipoParaExcluir.value = null
                }
            )
        }
    }
}