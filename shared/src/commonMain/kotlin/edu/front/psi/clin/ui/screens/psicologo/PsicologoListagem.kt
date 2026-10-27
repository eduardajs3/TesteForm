package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel

@Composable
fun PsicologoListagem(
    innerPadding: PaddingValues,
    vm: PsicologoViewModel
) {
    val lista = vm.psicologosFiltrados.collectAsState()

    Column(modifier = Modifier.padding(innerPadding)) {
        for (psicologo in lista.value) {
            PsicologoItem(
                psicologo = psicologo,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}