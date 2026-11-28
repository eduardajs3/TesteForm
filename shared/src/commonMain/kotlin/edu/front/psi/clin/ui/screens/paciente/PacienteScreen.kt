package edu.front.psi.clin.ui.screens.paciente

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.navigation.PacienteRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel

@Composable
fun PacienteScreen(
    innerPadding: PaddingValues,
    vm: PacienteViewModel,
    onNavegarParaProntuario: (Paciente) -> Unit,
    onNavegarParaEvolucao: (Paciente) -> Unit,
    onVoltarModulo: () -> Unit
) {
    val podeGerenciar = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.PACIENTES
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
            entry<PacienteRotas.Listagem> {
                PacienteListagem(
                    innerPadding = innerPadding,
                    vm = vm,
                    onNavegarParaProntuario = onNavegarParaProntuario,
                    onNavegarParaEvolucao = onNavegarParaEvolucao
                )
            }
            entry<PacienteRotas.Formulario> {
                if (podeGerenciar) {
                    PacienteFormulario(
                        innerPadding = innerPadding,
                        vm = vm
                    )
                } else {
                    PacienteListagem(
                        innerPadding = innerPadding,
                        vm = vm,
                        onNavegarParaProntuario = onNavegarParaProntuario,
                        onNavegarParaEvolucao = onNavegarParaEvolucao
                    )
                }
            }
        }
    )
}