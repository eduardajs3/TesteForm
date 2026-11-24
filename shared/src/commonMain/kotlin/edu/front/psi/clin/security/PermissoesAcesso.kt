package edu.front.psi.clin.security

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
            RecursoSistema.PSICOLOGOS -> perfil in setOf("ADMIN", "RECEPCIONISTA")
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

    fun recursoDaTela(tela: String): RecursoSistema? = when (tela) {
        "funcionario" -> RecursoSistema.FUNCIONARIOS
        "psicologo" -> RecursoSistema.PSICOLOGOS
        "recepcionista" -> RecursoSistema.RECEPCIONISTAS
        "prontuario" -> RecursoSistema.PRONTUARIOS
        "evolucao" -> RecursoSistema.EVOLUCOES_CLINICAS
        "horario" -> RecursoSistema.HORARIOS_DISPONIVEIS
        "bloqueio" -> RecursoSistema.BLOQUEIOS_AGENDA
        "pagamento" -> RecursoSistema.PAGAMENTOS
        "agenda" -> RecursoSistema.AGENDAS
        "paciente" -> RecursoSistema.PACIENTES
        "convenio" -> RecursoSistema.CONVENIOS
        "sala" -> RecursoSistema.SALAS_ATENDIMENTO
        "tipo" -> RecursoSistema.TIPOS_ATENDIMENTO
        else -> null
    }

    fun podeAcessarTela(role: String?, tela: String): Boolean {
        if (tela == "dashboard") return role?.let(::roleConhecida) == true
        return recursoDaTela(tela)?.let { podeAcessar(role, it) } ?: false
    }
}