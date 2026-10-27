package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel

@Composable
fun ProntuarioListagem(
    innerPadding: PaddingValues,
    vm: ProntuarioViewModel
) {
    val lista = vm.prontuariosFiltrados.collectAsState()

    Column(modifier = Modifier.padding(innerPadding)) {
        for (prontuario in lista.value) {
            ProntuarioItem(
                prontuario = prontuario,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}