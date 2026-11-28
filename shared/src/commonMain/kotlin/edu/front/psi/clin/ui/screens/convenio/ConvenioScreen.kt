package edu.front.psi.clin.ui.screens.convenio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.ConvenioRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

@Composable
fun ConvenioScreen(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel,
    onVoltarModulo: () -> Unit
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.CONVENIOS
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
            entry<ConvenioRotas.Listagem> {
                ConvenioListagem(innerPadding = innerPadding, vm = vm)
            }
            entry<ConvenioRotas.Formulario> {
                if (podeGerenciar) {
                    ConvenioFormulario(innerPadding = innerPadding, vm = vm)
                } else {
                    ConvenioListagem(innerPadding = innerPadding, vm = vm)
                }
            }
        }
    )
}