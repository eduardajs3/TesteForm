package edu.front.psi.clin.ui.screens.tipoAtendimento

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.navigation.TipoAtendimentoRotas
import edu.front.psi.clin.ui.viewmodel.TipoAtendimentoViewModel

@Composable
fun TipoAtendimentoScreen(
    innerPadding: PaddingValues,
    vm: TipoAtendimentoViewModel,
    podeGerenciar: Boolean
) {
    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == TipoAtendimentoRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(TipoAtendimentoRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<TipoAtendimentoRotas.Listagem> {
                TipoAtendimentoListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<TipoAtendimentoRotas.Formulario> {
                if (podeGerenciar) {
                    TipoAtendimentoFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    TipoAtendimentoListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}