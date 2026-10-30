package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import edu.front.psi.clin.Funcionario
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
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

    // Estado para controlar qual funcionário vai abrir o modal de exclusão
    val funcionarioParaExcluir = remember { mutableStateOf<Funcionario?>(null) }

    // Definição das colunas da tabela com seus respetivos pesos de largura
    val colunas = listOf(
        "Nome" to 1.5f,
        "CPF" to 1.3f,
        "Telefone" to 1.2f,
        "E-mail" to 1.8f
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
            onEditar = vm::editar,
            // Guarda o item para abrir o modal de exclusão personalizado
            onApagar = { funcionario ->
                funcionarioParaExcluir.value = funcionario
            },
            placeholderPesquisa = "Buscar por nome, CPF ou e-mail..."
        ) { funcionario ->
            CelulaTabelaTexto(
                text = funcionario.nome,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = funcionario.cpf,
                weight = 1.3f
            )
            CelulaTabelaTexto(
                text = funcionario.telefone,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = funcionario.email,
                weight = 1.8f
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