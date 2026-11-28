package edu.front.psi.clin.ui.screens.prontuario

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel

@Composable
fun ProntuarioScreen(
    innerPadding: PaddingValues,
    vm: ProntuarioViewModel,
    onVoltar: () -> Unit,
    nomePaciente: String
) {
    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else {
                onVoltar()
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<ProntuarioRotas.Listagem> {
                ProntuarioListagem(
                    innerPadding = innerPadding,
                    vm = vm,
                    onVoltar = onVoltar,
                    nomePaciente = nomePaciente
                )
            }
            entry<ProntuarioRotas.Formulario> {
                ProntuarioFormulario(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
        }
    )
}