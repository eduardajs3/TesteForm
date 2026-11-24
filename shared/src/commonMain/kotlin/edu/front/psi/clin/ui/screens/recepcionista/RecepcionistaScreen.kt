package edu.front.psi.clin.ui.screens.recepcionista

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.RecepcionistaRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

@Composable
fun RecepcionistaScreen(
    innerPadding: PaddingValues,
    vm: RecepcionistaViewModel
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.RECEPCIONISTAS
    )

    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == RecepcionistaRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(RecepcionistaRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<RecepcionistaRotas.Listagem> {
                RecepcionistaListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<RecepcionistaRotas.Formulario> {
                if (podeGerenciar) {
                    RecepcionistaFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    RecepcionistaListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}