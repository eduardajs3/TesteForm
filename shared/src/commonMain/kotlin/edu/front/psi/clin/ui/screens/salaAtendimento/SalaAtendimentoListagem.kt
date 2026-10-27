package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel

@Composable
fun SalaAtendimentoListagem(
    innerPadding: PaddingValues,
    vm: SalaAtendimentoViewModel
) {
    val lista = vm.salasFiltradas.collectAsState()
    
    Column(modifier = Modifier.padding(innerPadding)) {
        for (sala in lista.value) {
            SalaAtendimentoItem(
                sala = sala,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}