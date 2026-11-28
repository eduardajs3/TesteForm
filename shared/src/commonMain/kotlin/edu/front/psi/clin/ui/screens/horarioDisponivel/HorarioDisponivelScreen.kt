package edu.front.psi.clin.ui.screens.horarioDisponivel

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

@Composable
fun HorarioDisponivelScreen(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel,
    onVoltarModulo: () -> Unit
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.HORARIOS_DISPONIVEIS
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
            entry<HorarioDisponivelRotas.Listagem> {
                HorarioDisponivelListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }

            entry<HorarioDisponivelRotas.Formulario> {
                if (podeGerenciar) {
                    HorarioDisponivelFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    HorarioDisponivelListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}