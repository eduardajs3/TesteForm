package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

@Composable
fun RecepcionistaListagem(
    innerPadding: PaddingValues,
    vm: RecepcionistaViewModel
) {
    val lista = vm.recepcionistasFiltrados.collectAsState()

    Column(modifier = Modifier.padding(innerPadding)) {
        for (recepcionista in lista.value) {
            RecepcionistaItem(
                recepcionista = recepcionista,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}