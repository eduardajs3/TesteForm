package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel

@Composable
fun PacienteListagem(
    innerPadding: PaddingValues,
    vm: PacienteViewModel
) {
    val lista = vm.pacientesFiltrados.collectAsState()
    
    Column(modifier = Modifier.padding(innerPadding)) {
        for (paciente in lista.value) {
            PacienteItem(
                paciente = paciente,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}