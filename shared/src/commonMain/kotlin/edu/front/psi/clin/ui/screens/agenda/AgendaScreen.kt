package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(
    innerPadding: PaddingValues,
    vm: AgendaViewModel,
    onVoltarModulo: () -> Unit
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.AGENDAS
    )

    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else {
                onVoltarModulo()
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<AgendaRotas.Listagem> {
                AgendaListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<AgendaRotas.Formulario> {
                if (podeGerenciar) {
                    AgendaFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    AgendaListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}