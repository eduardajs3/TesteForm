package edu.front.psi.clin.ui.screens.salaAtendimento

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.SalaAtendimento
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel

@Composable
fun SalaAtendimentoListagem(
    innerPadding: PaddingValues,
    vm: SalaAtendimentoViewModel
) {
    val lista = vm.salasFiltradas.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val salaSelecionada = remember { mutableStateOf<SalaAtendimento?>(null) }
    
    val salaParaExcluir = remember { mutableStateOf<SalaAtendimento?>(null) }

    val colunas = listOf(
        "Nome da Sala" to 1.5f,
        "Capacidade" to 0.9f,
        "Descrição" to 1.6f,
        "Status" to 0.8f
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
            val isAtivo = sala.ativo

            CelulaTabelaTexto(
                text = sala.nome,
                weight = 1.5f,
                isBold = true
            )
            CelulaTabelaTexto(
                text = "${sala.capacidade} pessoas",
                weight = 0.9f
            )
            CelulaTabelaTexto(
                text = sala.descricao,
                weight = 1.6f
            )
            
            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        salaSelecionada.value?.let { sala ->
            val isAtivo = sala.ativo
            ModalDetalhesBase(
                titulo = sala.nome,
                onDismiss = { salaSelecionada.value = null },
                conteudo = {
                    ItemDetalhe("Nome da sala", sala.nome)
                    ItemDetalhe("Capacidade", "${sala.capacidade} pessoas")
                    ItemDetalhe("Descrição", sala.descricao)
                    ItemDetalhe("Data de cadastro", sala.dataCadastro)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo",
                    )
                }
            )
        }

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