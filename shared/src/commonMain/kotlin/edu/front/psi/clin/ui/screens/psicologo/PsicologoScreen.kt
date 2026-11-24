package edu.front.psi.clin.ui.screens.psicologo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.PsicologoRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel

@Composable
fun PsicologoScreen(
    innerPadding: PaddingValues,
    vm: PsicologoViewModel
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.PSICOLOGOS
    )

    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == PsicologoRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(PsicologoRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<PsicologoRotas.Listagem> {
                PsicologoListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<PsicologoRotas.Formulario> {
                if (podeGerenciar) {
                    PsicologoFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    PsicologoListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}