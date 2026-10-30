package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.Convenio
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun ConvenioListagem(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel
) {
    val lista = vm.conveniosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    // Estado para controlar qual convênio vai abrir o modal de exclusão
    val convenioParaExcluir = remember { mutableStateOf<Convenio?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Nome do Convênio" to 1.8f,
        "CNPJ" to 1.4f,
        "Cobertura" to 1.0f
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
            onEditar = vm::editar,
            // Guarda o item para abrir o modal de exclusão personalizado
            onApagar = { convenio ->
                convenioParaExcluir.value = convenio
            },
            placeholderPesquisa = "Buscar por nome ou CNPJ..."
        ) { convenio ->
            CelulaTabelaTexto(
                text = convenio.nome,
                weight = 1.8f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = convenio.cnpj,
                weight = 1.4f
            )
            CelulaTabelaTexto(
                text = "${convenio.percentualCobertura}%",
                weight = 1.0f
            )
        }

        // Modal de Exclusão Reutilizável
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