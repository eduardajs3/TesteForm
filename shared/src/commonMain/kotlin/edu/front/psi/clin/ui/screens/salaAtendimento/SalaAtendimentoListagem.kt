package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.SalaAtendimento
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
import edu.front.psi.clin.ui.components.ItemDetalhe
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.components.ModalDetalhesBase
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel

@Composable
fun SalaAtendimentoListagem(
    innerPadding: PaddingValues,
    vm: SalaAtendimentoViewModel
) {
    val lista = vm.salasFiltradas.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val salaSelecionada = remember { mutableStateOf<SalaAtendimento?>(null) }
    
    // Estado para controlar qual sala vai abrir o modal de exclusão
    val salaParaExcluir = remember { mutableStateOf<SalaAtendimento?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Nome da Sala" to 1.5f,
        "Capacidade" to 1.0f,
        "Descrição" to 2.0f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Salas de Atendimento",
            textoBotaoNovo = "Nova Sala",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novaSala,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { sala -> salaSelecionada.value = sala },
            onEditar = vm::editar,
            onApagar = { sala -> salaParaExcluir.value = sala },
            placeholderPesquisa = "Buscar por nome ou descrição..."
        ) { sala ->
            CelulaTabelaTexto(
                text = sala.nome,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = "${sala.capacidade} pessoas",
                weight = 1.0f
            )
            CelulaTabelaTexto(
                text = sala.descricao,
                weight = 2.0f
            )
        }

        // Modal Reutilizável de Detalhes da Sala de Atendimento
        salaSelecionada.value?.let { sala ->
            ModalDetalhesBase(
                titulo = sala.nome,
                onDismiss = { salaSelecionada.value = null },
                conteudo = {
                    ItemDetalhe("Capacidade", "${sala.capacidade} pessoas")
                    ItemDetalhe("Descrição", sala.descricao)
                }
            )
        }

        // Modal de Exclusão Reutilizável
        salaParaExcluir.value?.let { sala ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir a sala '${sala.nome}'? Esta ação não pode ser desfeita.",
                onDismiss = { salaParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(sala)
                    salaParaExcluir.value = null
                }
            )
        }
    }
}