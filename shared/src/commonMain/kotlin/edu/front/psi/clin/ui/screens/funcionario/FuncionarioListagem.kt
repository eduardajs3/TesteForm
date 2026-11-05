package edu.front.psi.clin.ui.screens.funcionario

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.Funcionario
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BadgeStatus
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.componentes.CelulaTabelaTexto
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun FuncionarioListagem(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val lista = vm.funcionariosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    // Estados para controlar o funcionário selecionado para detalhes (botão do olho) e exclusão
    val funcionarioSelecionado = remember { mutableStateOf<Funcionario?>(null) }
    val funcionarioParaExcluir = remember { mutableStateOf<Funcionario?>(null) }

    // Definição das colunas da tabela com seus respetivos pesos de largura (incluindo Status)
    val colunas = listOf(
        "Nome" to 1.5f,
        "CPF" to 1.2f,
        "Telefone" to 1.1f,
        "E-mail" to 1.6f,
        "Status" to 0.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Funcionários",
            textoBotaoNovo = "Novo Funcionário",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoFuncionario,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { funcionario -> funcionarioSelecionado.value = funcionario },
            onEditar = vm::editar,
            onApagar = { funcionario ->
                funcionarioParaExcluir.value = funcionario
            },
            placeholderPesquisa = "Buscar por nome, CPF ou e-mail..."
        ) { funcionario ->
            val isAtivo = funcionario.ativo

            CelulaTabelaTexto(
                text = funcionario.nome,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = funcionario.cpf,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = funcionario.telefone,
                weight = 1.1f
            )
            CelulaTabelaTexto(
                text = funcionario.email,
                weight = 1.6f
            )
            
            // Célula de Status com badge visual na tabela
            Box(modifier = Modifier.weight(0.8f)) {
                BadgeStatus(
                    texto = if (isAtivo) "Ativo" else "Inativo",
                    isAtivo = isAtivo
                )
            }
        }

        // Modal de Detalhes (ativado pelo botão do olho)
        funcionarioSelecionado.value?.let { funcionario ->
            val isAtivo = funcionario.ativo
            ModalDetalhesBase(
                titulo = funcionario.nome,
                onDismiss = { funcionarioSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Nome completo", valor = funcionario.nome)
                    ItemDetalhe(rotulo = "CPF", valor = funcionario.cpf)
                    ItemDetalhe(rotulo = "E-mail", valor = funcionario.email)
                    ItemDetalhe(rotulo = "Telefone", valor = funcionario.telefone)
                    ItemDetalhe(rotulo = "Data de cadastro", valor = funcionario.dataCadastro)
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (isAtivo) "Ativo" else "Inativo",
                        corTextoValor = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E)
                    )
                }
            )
        }

        // Modal de Exclusão Reutilizável
        funcionarioParaExcluir.value?.let { funcionario ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir o funcionário '${funcionario.nome}'? Esta ação não pode ser desfeita.",
                onDismiss = { funcionarioParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(funcionario)
                    funcionarioParaExcluir.value = null
                }
            )
        }
    }
}