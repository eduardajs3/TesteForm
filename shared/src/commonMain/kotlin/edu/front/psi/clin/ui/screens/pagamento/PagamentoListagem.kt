package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.Pagamento
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.BadgeStatus
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoListagem(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    val lista = vm.pagamentosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    // Estado para controlar qual pagamento vai abrir o modal de exclusão
    val pagamentoParaExcluir = remember { mutableStateOf<Pagamento?>(null) }

    // Configuração das colunas e seus pesos de largura
    val colunas = listOf(
        "Paciente" to 1.5f,
        "Valor Total" to 1.2f,
        "Data" to 1.2f,
        "Status" to 1.2f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Pagamentos",
            textoBotaoNovo = "Novo Pagamento",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoPagamento,
            colunas = colunas,
            itens = lista.value,
            onEditar = vm::editar,
            // Guarda o item para abrir o modal de exclusão personalizado
            onApagar = { pagamento ->
                pagamentoParaExcluir.value = pagamento
            },
            placeholderPesquisa = "Buscar por paciente, status ou data..."
        ) { pagamento ->
            CelulaTabelaTexto(
                text = pagamento.paciente,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = "R$ ${pagamento.valorTotal}",
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = pagamento.dataPagamento,
                weight = 1.2f
            )

            // Utilizando o componente reutilizável BadgeStatus para o estado financeiro
            Box(modifier = Modifier.weight(1.2f)) {
                BadgeStatus(
                    texto = if (pagamento.status) "Pago" else "Pendente",
                    isAtivo = pagamento.status
                )
            }
        }

        // Modal de Exclusão Reutilizável
        pagamentoParaExcluir.value?.let { pagamento ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir este registo de pagamento? Esta ação não pode ser desfeita.",
                onDismiss = { pagamentoParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(pagamento)
                    pagamentoParaExcluir.value = null
                }
            )
        }
    }
}