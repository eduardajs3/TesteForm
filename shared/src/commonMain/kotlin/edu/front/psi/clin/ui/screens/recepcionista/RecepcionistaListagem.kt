package edu.front.psi.clin.ui.screens.recepcionista

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.Recepcionista
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

@Composable
fun RecepcionistaListagem(
    innerPadding: PaddingValues,
    vm: RecepcionistaViewModel
) {
    val lista = vm.recepcionistasFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val recepcionistaSelecionada = remember { mutableStateOf<Recepcionista?>(null) }
    
    val recepcionistaParaExcluir = remember { mutableStateOf<Recepcionista?>(null) }

    val colunas = listOf(
        "Nome" to 1.5f,
        "CPF" to 1.2f,
        "Telefone" to 1.1f,
        "Setor" to 1.1f,
        "Status" to 0.8f
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
            val isAtivo = recepcionista.ativo

            CelulaTabelaTexto(
                text = recepcionista.nome,
                weight = 1.5f,
                isBold = true
            )
            CelulaTabelaTexto(
                text = recepcionista.cpf,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = recepcionista.telefone,
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = recepcionista.setor,
                weight = 1.1f
            )
            
            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        recepcionistaSelecionada.value?.let { recepcionista ->
            val isAtivo = recepcionista.ativo
            ModalDetalhesBase(
                titulo = recepcionista.nome,
                onDismiss = { recepcionistaSelecionada.value = null },
                conteudo = {
                    ItemDetalhe("Nome completo", recepcionista.nome)
                    ItemDetalhe("CPF", recepcionista.cpf)
                    ItemDetalhe("Setor", recepcionista.setor)
                    ItemDetalhe("Telefone", recepcionista.telefone)
                    ItemDetalhe("E-mail", recepcionista.email)
                    ItemDetalhe("Data de cadastro", recepcionista.dataCadastro)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo",
                    )
                }
            )
        }

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