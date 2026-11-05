package edu.front.psi.clin.ui.screens.psicologo

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.Psicologo
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel

@Composable
fun PsicologoListagem(
    innerPadding: PaddingValues,
    vm: PsicologoViewModel
) {
    val lista = vm.psicologosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val psicologoSelecionado = remember { mutableStateOf<Psicologo?>(null) }
    
    // Estado para controlar qual psicólogo vai abrir o modal de exclusão
    val psicologoParaExcluir = remember { mutableStateOf<Psicologo?>(null) }

    // Mapeamento das colunas da tabela (incluindo Status)
    val colunas = listOf(
        "Nome" to 1.5f,
        "CRP" to 1.1f,
        "Telefone" to 1.1f,
        "Especialidade" to 1.4f,
        "Status" to 0.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Psicólogos",
            textoBotaoNovo = "Novo Psicólogo",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoPsicologo,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { psicologo -> psicologoSelecionado.value = psicologo },
            onEditar = vm::editar,
            onApagar = { psicologo -> psicologoParaExcluir.value = psicologo },
            placeholderPesquisa = "Buscar por nome, CRP ou especialidade..."
        ) { psicologo ->
            val isAtivo = psicologo.ativo

            CelulaTabelaTexto(
                text = psicologo.nome,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = psicologo.crp,
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = psicologo.telefone,
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = psicologo.especialidade,
                weight = 1.4f
            )
            
            // Célula de Status com badge visual na tabela
            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        // Modal Reutilizável de Detalhes do Psicólogo
        psicologoSelecionado.value?.let { psicologo ->
            val isAtivo = psicologo.ativo
            ModalDetalhesBase(
                titulo = psicologo.nome,
                onDismiss = { psicologoSelecionado.value = null },
                conteudo = {
                    ItemDetalhe("Nome completo", psicologo.nome)
                    ItemDetalhe("CRP", psicologo.crp)
                    ItemDetalhe("CPF", psicologo.cpf)
                    ItemDetalhe("Especialidade", psicologo.especialidade)
                    ItemDetalhe("Telefone", psicologo.telefone)
                    ItemDetalhe("E-mail", psicologo.email)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo",
                        corTextoValor = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E)
                    )
                }
            )
        }

        // Modal de Exclusão Reutilizável
        psicologoParaExcluir.value?.let { psicologo ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir o(a) psicólogo(a) '${psicologo.nome}'? Esta ação não pode ser desfeita.",
                onDismiss = { psicologoParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(psicologo)
                    psicologoParaExcluir.value = null
                }
            )
        }
    }
}