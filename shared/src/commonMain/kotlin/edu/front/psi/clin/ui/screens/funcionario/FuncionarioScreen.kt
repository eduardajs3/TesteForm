package edu.front.psi.clin.ui.screens.funcionario

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.FuncionarioRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel

@Composable
fun FuncionarioScreen(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.FUNCIONARIOS
    )

    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == FuncionarioRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(FuncionarioRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<FuncionarioRotas.Listagem> {
                FuncionarioListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<FuncionarioRotas.Formulario> {
                if (podeGerenciar) {
                    FuncionarioFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    FuncionarioListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}