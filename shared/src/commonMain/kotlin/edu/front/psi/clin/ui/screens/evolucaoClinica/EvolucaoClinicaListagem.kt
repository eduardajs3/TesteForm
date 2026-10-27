package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel

@Composable
fun EvolucaoClinicaListagem(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel
) {
    val lista = vm.evolucoesFiltradas.collectAsState()

    Column(modifier = Modifier.padding(innerPadding)) {
        for (evolucao in lista.value) {
            EvolucaoClinicaItem(
                evolucao = evolucao,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}