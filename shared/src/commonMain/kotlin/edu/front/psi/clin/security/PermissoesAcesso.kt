package edu.front.psi.clin.security

import edu.front.psi.clin.navigation.Route

enum class RecursoSistema {
    FUNCIONARIOS,
    PSICOLOGOS,
    RECEPCIONISTAS,
    PRONTUARIOS,
    EVOLUCOES_CLINICAS,
    HORARIOS_DISPONIVEIS,
    BLOQUEIOS_AGENDA,
    PAGAMENTOS,
    AGENDAS,
    PACIENTES,
    CONVENIOS,
    SALAS_ATENDIMENTO,
    TIPOS_ATENDIMENTO
}

object PermissoesAcesso {
    fun normalizarRole(role: String): String = role
        .trim()
        .uppercase()
        .removePrefix("ROLE_")

    fun roleConhecida(role: String): Boolean = normalizarRole(role) in setOf(
        "ADMIN",
        "RECEPCIONISTA",
        "PSICOLOGO"
    )

    fun podeAcessar(role: String?, recurso: RecursoSistema): Boolean {
        val perfil = role?.let(::normalizarRole) ?: return false
        return when (recurso) {
            RecursoSistema.FUNCIONARIOS -> perfil == "ADMIN"
            RecursoSistema.PSICOLOGOS -> perfil in setOf("ADMIN", "RECEPCIONISTA", "PSICOLOGO")
            RecursoSistema.RECEPCIONISTAS -> perfil == "ADMIN"
            RecursoSistema.PRONTUARIOS,
            RecursoSistema.EVOLUCOES_CLINICAS -> perfil in setOf("ADMIN", "PSICOLOGO")
            RecursoSistema.HORARIOS_DISPONIVEIS,
            RecursoSistema.BLOQUEIOS_AGENDA,
            RecursoSistema.AGENDAS,
            RecursoSistema.PACIENTES,
            RecursoSistema.CONVENIOS,
            RecursoSistema.SALAS_ATENDIMENTO,
            RecursoSistema.TIPOS_ATENDIMENTO -> perfil in setOf("ADMIN", "RECEPCIONISTA", "PSICOLOGO")
            RecursoSistema.PAGAMENTOS -> perfil in setOf("ADMIN", "RECEPCIONISTA")
        }
    }

    fun podeGerenciar(role: String?, recurso: RecursoSistema): Boolean {
        val perfil = role?.let(::normalizarRole) ?: return false
        if (perfil == "ADMIN") return true

        return when (perfil) {
            "RECEPCIONISTA" -> recurso in setOf(
                RecursoSistema.PAGAMENTOS,
                RecursoSistema.AGENDAS,
                RecursoSistema.PACIENTES,
                RecursoSistema.CONVENIOS,
                RecursoSistema.SALAS_ATENDIMENTO,
                RecursoSistema.TIPOS_ATENDIMENTO
            )
            "PSICOLOGO" -> recurso in setOf(
                RecursoSistema.PRONTUARIOS,
                RecursoSistema.EVOLUCOES_CLINICAS,
                RecursoSistema.HORARIOS_DISPONIVEIS,
                RecursoSistema.BLOQUEIOS_AGENDA
            )
            else -> false
        }
    }

    fun recursoDaRota(rota: Route): RecursoSistema? = when (rota) {
        Route.Funcionario -> RecursoSistema.FUNCIONARIOS
        Route.Psicologo -> RecursoSistema.PSICOLOGOS
        Route.Recepcionista -> RecursoSistema.RECEPCIONISTAS
        Route.Prontuario -> RecursoSistema.PRONTUARIOS
        Route.EvolucaoClinica -> RecursoSistema.EVOLUCOES_CLINICAS
        Route.HorarioDisponivel -> RecursoSistema.HORARIOS_DISPONIVEIS
        Route.BloqueioAgenda -> RecursoSistema.BLOQUEIOS_AGENDA
        Route.Pagamento -> RecursoSistema.PAGAMENTOS
        Route.Agenda -> RecursoSistema.AGENDAS
        Route.Paciente -> RecursoSistema.PACIENTES
        Route.Convenio -> RecursoSistema.CONVENIOS
        Route.SalaAtendimento -> RecursoSistema.SALAS_ATENDIMENTO
        Route.TipoAtendimento -> RecursoSistema.TIPOS_ATENDIMENTO
        else -> null
    }

    fun podeAcessarRota(role: String?, rota: Route): Boolean {
        if (rota == Route.Dashboard) return role?.let(::roleConhecida) == true
        return recursoDaRota(rota)?.let { podeAcessar(role, it) } ?: false
    }
}