package edu.front.psi.clin.ui.screens.tipoAtendimento

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.TipoAtendimento
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
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

    // Mapeamento das colunas da tabela e suas proporções de largura (incluindo Status)
    val colunas = listOf(
        "Tipo de Atendimento" to 1.5f,
        "Duração" to 1.0f,
        "Descrição" to 1.7f,
        "Status" to 0.8f
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
            val isAtivo = tipo.ativo

            CelulaTabelaTexto(
                text = tipo.nome,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = "${tipo.duracao} min",
                weight = 1.0f
            )
            CelulaTabelaTexto(
                text = tipo.descricao,
                weight = 1.7f
            )
            
            // Célula de Status com badge visual na tabela
            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        // Modal Reutilizável de Detalhes do Tipo de Atendimento
        tipoSelecionado.value?.let { tipo ->
            val isAtivo = tipo.ativo
            ModalDetalhesBase(
                titulo = tipo.nome,
                onDismiss = { tipoSelecionado.value = null },
                conteudo = {
                    ItemDetalhe("Nome do atendimento", tipo.nome)
                    ItemDetalhe("Duração", "${tipo.duracao} minutos")
                    ItemDetalhe("Descrição", tipo.descricao)
                    ItemDetalhe("Data de cadastro", tipo.dataCadastro)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo",
                        corTextoValor = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E)
                    )
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