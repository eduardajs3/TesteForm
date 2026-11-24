package edu.front.psi.clin.ui.screens.pagamento

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoScreen(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel,
    podeGerenciar: Boolean
) {
    NavDisplay(
        backStack = vm.backStack,
        onBack = {
            if (vm.backStack.size > 1) {
                vm.backStack.removeAt(vm.backStack.lastIndex)
            } else if (vm.backStack.lastOrNull() == PagamentoRotas.Formulario) {
                vm.backStack.clear()
                vm.backStack.add(PagamentoRotas.Listagem)
            }
        },
        entryProvider = entryProvider<NavKey> {
            entry<PagamentoRotas.Listagem> {
                PagamentoListagem(
                    innerPadding = innerPadding,
                    vm = vm
                )
            }
            entry<PagamentoRotas.Formulario> {
                if (podeGerenciar) {
                    PagamentoFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    PagamentoListagem(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                }
            }
        }
    )
}