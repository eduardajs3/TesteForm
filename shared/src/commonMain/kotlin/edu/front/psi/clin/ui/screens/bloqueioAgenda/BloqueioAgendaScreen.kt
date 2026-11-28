package edu.front.psi.clin.ui.screens.bloqueioAgenda

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.BloqueioAgendaViewModel

@Composable
fun BloqueioAgendaScreen(
    innerPadding: PaddingValues,
    vm: BloqueioAgendaViewModel,
    onVoltarModulo: () -> Unit
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.BLOQUEIOS_AGENDA
    )

    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else{
                onVoltarModulo()
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<BloqueioAgendaRotas.Listagem> {
                BloqueioAgendaListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<BloqueioAgendaRotas.Formulario> {
                if (podeGerenciar) {
                    BloqueioAgendaFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    BloqueioAgendaListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}