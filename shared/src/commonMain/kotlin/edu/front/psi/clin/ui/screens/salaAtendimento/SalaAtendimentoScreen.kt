package edu.front.psi.clin.ui.screens.salaAtendimento

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.SalaAtendimentoRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel

@Composable
fun SalaAtendimentoScreen(
    innerPadding: PaddingValues,
    vm: SalaAtendimentoViewModel
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.SALAS_ATENDIMENTO
    )

    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == SalaAtendimentoRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(SalaAtendimentoRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<SalaAtendimentoRotas.Listagem> {
                SalaAtendimentoListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<SalaAtendimentoRotas.Formulario> {
                if (podeGerenciar) {
                    SalaAtendimentoFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    SalaAtendimentoListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}