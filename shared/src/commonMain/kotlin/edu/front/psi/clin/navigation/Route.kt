package edu.front.psi.clin.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Login : Route

    @Serializable
    data object Dashboard : Route

    @Serializable
    data object Paciente : Route

    @Serializable
    data object Pagamento : Route

    @Serializable
    data object Convenio : Route

    @Serializable
    data object HorarioDisponivel : Route

    @Serializable
    data object SalaAtendimento : Route

    @Serializable
    data object TipoAtendimento : Route

    @Serializable
    data object BloqueioAgenda : Route

    @Serializable
    data object EvolucaoClinica : Route

    @Serializable
    data object Prontuario : Route

    @Serializable
    data object Recepcionista : Route

    @Serializable
    data object Funcionario : Route

    @Serializable
    data object Psicologo : Route

    @Serializable
    data object Agenda : Route
}
