package edu.front.psi.clin.ui.screens.pagamento

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.Pagamento
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoListagem(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    val lista = vm.pagamentosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    val pagamentoSelecionado = remember { mutableStateOf<Pagamento?>(null) }
    val pagamentoParaExcluir = remember { mutableStateOf<Pagamento?>(null) }

    val colunas = listOf(
        "Paciente" to 1.5f,
        "Valor Total" to 1.1f,
        "Data" to 1.1f,
        "Status" to 0.9f
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
            onVisualizar = { pagamento -> pagamentoSelecionado.value = pagamento },
            onEditar = vm::editar,
            onApagar = { pagamento ->
                pagamentoParaExcluir.value = pagamento
            },
            placeholderPesquisa = "Buscar por paciente, status ou data..."
        ) { pagamento ->
            val isPago = pagamento.status

            CelulaTabelaTexto(
                text = pagamento.paciente,
                weight = 1.5f,
                isBold = true
            )
            CelulaTabelaTexto(
                text = "R$ ${pagamento.valorTotal}",
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = pagamento.dataPagamento,
                weight = 1.1f
            )

            Box(modifier = Modifier.weight(0.9f)) {
                BadgeStatus(
                    texto = if (isPago) "Pago" else "Pendente",
                    isAtivo = isPago
                )
            }
        }

        pagamentoSelecionado.value?.let { pagamento ->
            val isPago = pagamento.status
            ModalDetalhesBase(
                titulo = "Detalhes do Pagamento",
                onDismiss = { pagamentoSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Paciente", valor = pagamento.paciente)
                    ItemDetalhe(rotulo = "Convênio", valor = pagamento.convenio)
                    ItemDetalhe(rotulo = "Valor total", valor = "R$ ${pagamento.valorTotal}")
                    ItemDetalhe(rotulo = "Valor do convênio", valor = "R$ ${pagamento.valorConvenio}")
                    ItemDetalhe(rotulo = "Valor do paciente", valor = "R$ ${pagamento.valorPaciente}")
                    ItemDetalhe(rotulo = "Forma de pagamento", valor = pagamento.formaPagamento)
                    ItemDetalhe(rotulo = "Data do pagamento", valor = pagamento.dataPagamento)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isPago) "Pago" else "Pendente",
                    )
                }
            )
        }

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