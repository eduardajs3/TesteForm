package edu.front.psi.clin.ui.screens.evolucaoClinica

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel

@Composable
fun EvolucaoClinicaScreen(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel,
    onVoltar: () -> Unit,
    nomePaciente: String
) {
    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == EvolucaoClinicaRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(EvolucaoClinicaRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<EvolucaoClinicaRotas.Listagem> {
                EvolucaoClinicaListagem(
                    innerPadding = innerPadding,
                    vm = vm,
                    onVoltar = onVoltar,
                    nomePaciente = nomePaciente
                )
            }
            entry<EvolucaoClinicaRotas.Formulario> {
                EvolucaoClinicaFormulario(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
        }
    )
}