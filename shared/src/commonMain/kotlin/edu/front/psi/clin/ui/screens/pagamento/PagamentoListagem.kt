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
    val pacientesLista = vm.pacientes.collectAsState().value
    val conveniosLista = vm.convenios.collectAsState().value

    val pagamentoSelecionado = remember { mutableStateOf<Pagamento?>(null) }
    val pagamentoParaExcluir = remember { mutableStateOf<Pagamento?>(null) }

    val colunas = listOf(
        "Paciente" to 1.5f,
        "Valor Total" to 1.1f,
        "Forma Pagto" to 1.1f,
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
            textoCompacto = { pagamento ->
                pacientesLista.firstOrNull { it.id?.toLong() == pagamento.pacienteId }?.nome
                    ?: "Paciente #${pagamento.pacienteId}"
            },
            placeholderPesquisa = "Buscar por forma de pagamento ou status..."
        ) { pagamento ->
            val pacienteNome = pacientesLista.firstOrNull { it.id?.toLong() == pagamento.pacienteId }?.nome ?: "Paciente #${pagamento.pacienteId}"
            val isPago = pagamento.status.equals("PAGO", ignoreCase = true) || pagamento.status.equals("true", ignoreCase = true)

            CelulaTabelaTexto(
                text = pacienteNome,
                weight = 1.5f,
                isBold = true
            )
            CelulaTabelaTexto(
                text = "R$ ${pagamento.valorTotal}",
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = pagamento.formaPagamento,
                weight = 1.1f
            )   
            

            Box(modifier = Modifier.weight(0.9f)) {
                BadgeStatus(
                    texto = if (isPago) "PAGO" else pagamento.status,
                    isAtivo = isPago
                )
            }
        }

        pagamentoSelecionado.value?.let { pagamento ->
            val pacienteNome = pacientesLista.firstOrNull { it.id?.toLong() == pagamento.pacienteId }?.nome ?: "-"
            val convenioNome = conveniosLista.firstOrNull { it.id?.toLong() == pagamento.convenioId }?.nome ?: "-"
            val isPago = pagamento.status.equals("PAGO", ignoreCase = true) || pagamento.status.equals("true", ignoreCase = true)

            ModalDetalhesBase(
                titulo = "Detalhes do Pagamento",
                onDismiss = { pagamentoSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Paciente", valor = pacienteNome)
                    ItemDetalhe(rotulo = "Convênio", valor = convenioNome)
                    ItemDetalhe(rotulo = "Valor total", valor = "R$ ${pagamento.valorTotal}")
                    ItemDetalhe(rotulo = "Valor do convênio", valor = "R$ ${pagamento.valorConvenio}")
                    ItemDetalhe(rotulo = "Valor do paciente", valor = "R$ ${pagamento.valorPaciente}")
                    ItemDetalhe(rotulo = "Forma de pagamento", valor = pagamento.formaPagamento)
                    ItemDetalhe(rotulo = "Data do pagamento", valor = pagamento.dataPagamento)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isPago) "Pago" else pagamento.status,
                    )
                }
            )
        }

        pagamentoParaExcluir.value?.let { pagamento ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir este registro de pagamento? Esta ação não pode ser desfeita.",
                onDismiss = { pagamentoParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(pagamento)
                    pagamentoParaExcluir.value = null
                }
            )
        }
    }
}