package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel

@Composable
fun FuncionarioListagem(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val lista = vm.funcionariosFiltrados.collectAsState()

    Column(modifier = Modifier.padding(innerPadding)) {
        for (funcionario in lista.value) {
            FuncionarioItem(
                funcionario = funcionario,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}