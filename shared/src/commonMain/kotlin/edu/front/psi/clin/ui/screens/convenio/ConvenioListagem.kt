package edu.front.psi.clin.ui.screens.convenio

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.Convenio
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

@Composable
fun ConvenioListagem(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel
) {
    val lista = vm.conveniosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    val convenioSelecionado = remember { mutableStateOf<Convenio?>(null) }
    val convenioParaExcluir = remember { mutableStateOf<Convenio?>(null) }

    val colunas = listOf(
        "Nome do Convênio" to 1.6f,
        "CNPJ" to 1.2f,
        "Cobertura" to 0.8f,
        "Status" to 0.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Convênios",
            textoBotaoNovo = "Novo Convênio",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoConvenio,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { convenio -> convenioSelecionado.value = convenio },
            onEditar = vm::editar,
            onApagar = { convenio -> convenioParaExcluir.value = convenio },
            placeholderPesquisa = "Buscar por nome ou CNPJ..."
        ) { convenio ->
            val isAtivo = convenio.ativo

            CelulaTabelaTexto(
                text = convenio.nome,
                weight = 1.6f,
                isBold = true
            )
            CelulaTabelaTexto(
                text = convenio.cnpj,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = "${convenio.percentualCobertura}%",
                weight = 0.8f
            )
            
            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        convenioSelecionado.value?.let { convenio ->
            val isAtivo = convenio.ativo
            ModalDetalhesBase(
                titulo = "Detalhes do Convênio",
                onDismiss = { convenioSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Nome do convênio", valor = convenio.nome)
                    ItemDetalhe(rotulo = "CNPJ", valor = convenio.cnpj)
                    ItemDetalhe(rotulo = "Telefone", valor = convenio.telefone)
                    ItemDetalhe(rotulo = "Percentual de cobertura", valor = "${convenio.percentualCobertura}%")
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo"
                    )
                }
            )
        }

        convenioParaExcluir.value?.let { convenio ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir o convênio '${convenio.nome}'? Esta ação não pode ser desfeita.",
                onDismiss = { convenioParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(convenio)
                    convenioParaExcluir.value = null
                }
            )
        }
    }
}