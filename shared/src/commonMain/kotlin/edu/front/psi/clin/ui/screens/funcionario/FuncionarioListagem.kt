package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun FuncionarioListagem(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val lista = vm.funcionariosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

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
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por nome, CPF ou e-mail..."
        ) { funcionario ->
            Text(
                text = funcionario.nome,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = funcionario.cpf,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.3f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = funcionario.telefone,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = funcionario.email,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}