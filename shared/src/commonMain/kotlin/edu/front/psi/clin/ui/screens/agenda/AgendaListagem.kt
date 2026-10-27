package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

@Composable
fun AgendaListagem(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val lista = vm.agendamentosFiltrados.collectAsState()

    Column(modifier = Modifier.padding(innerPadding)) {
        for (agendamento in lista.value) {
            AgendaItem(
                agendamento = agendamento,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}