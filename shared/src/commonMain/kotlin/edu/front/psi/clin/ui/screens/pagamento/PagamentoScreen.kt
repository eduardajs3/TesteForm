package edu.front.psi.clin.ui.screens.pagamento

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoScreen(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel,
    onVoltarModulo: () -> Unit
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.PAGAMENTOS
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