package edu.front.psi.clin

import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PermissoesAcessoTest {
    @Test
    fun adminPodeLerEGerenciarTodosOsRecursos() {
        RecursoSistema.entries.forEach { recurso ->
            assertTrue(PermissoesAcesso.podeAcessar("ADMIN", recurso), "$recurso deve ser visível para ADMIN")
            assertTrue(PermissoesAcesso.podeGerenciar("ADMIN", recurso), "$recurso deve ser editável para ADMIN")
        }
    }

    @Test
    fun recepcionistaTemAcessoOperacionalMasNaoClinicoOuAdministrativo() {
        assertFalse(PermissoesAcesso.podeAcessar("RECEPCIONISTA", RecursoSistema.FUNCIONARIOS))
        assertFalse(PermissoesAcesso.podeAcessar("RECEPCIONISTA", RecursoSistema.RECEPCIONISTAS))
        assertFalse(PermissoesAcesso.podeAcessar("RECEPCIONISTA", RecursoSistema.PRONTUARIOS))
        assertFalse(PermissoesAcesso.podeAcessar("RECEPCIONISTA", RecursoSistema.EVOLUCOES_CLINICAS))
        assertTrue(PermissoesAcesso.podeGerenciar("RECEPCIONISTA", RecursoSistema.PAGAMENTOS))
        assertTrue(PermissoesAcesso.podeGerenciar("RECEPCIONISTA", RecursoSistema.AGENDAS))
        assertTrue(PermissoesAcesso.podeGerenciar("RECEPCIONISTA", RecursoSistema.PACIENTES))
        assertFalse(PermissoesAcesso.podeGerenciar("RECEPCIONISTA", RecursoSistema.HORARIOS_DISPONIVEIS))
        assertFalse(PermissoesAcesso.podeGerenciar("RECEPCIONISTA", RecursoSistema.BLOQUEIOS_AGENDA))
    }

    @Test
    fun psicologoConsultaOperacaoMasSoGerenciaAreaClinicaEDisponibilidade() {
        assertFalse(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.FUNCIONARIOS))
        assertFalse(PermissoesAcesso.podeAcessarTela("PSICOLOGO", "funcionario"))
        assertFalse(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.RECEPCIONISTAS))
        assertTrue(PermissoesAcesso.podeGerenciar("PSICOLOGO", RecursoSistema.PRONTUARIOS))
        assertTrue(PermissoesAcesso.podeGerenciar("PSICOLOGO", RecursoSistema.EVOLUCOES_CLINICAS))
        assertTrue(PermissoesAcesso.podeGerenciar("PSICOLOGO", RecursoSistema.HORARIOS_DISPONIVEIS))
        assertTrue(PermissoesAcesso.podeGerenciar("PSICOLOGO", RecursoSistema.BLOQUEIOS_AGENDA))
        assertTrue(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.AGENDAS))
        assertFalse(PermissoesAcesso.podeGerenciar("PSICOLOGO", RecursoSistema.AGENDAS))
        assertTrue(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.PACIENTES))
        assertFalse(PermissoesAcesso.podeGerenciar("PSICOLOGO", RecursoSistema.PACIENTES))
    }

    @Test
    fun roleComPrefixoSpringEComparadaSemDiferenciarMaiusculas() {
        assertTrue(PermissoesAcesso.podeAcessar("ROLE_ADMIN", RecursoSistema.FUNCIONARIOS))
        assertTrue(PermissoesAcesso.podeAcessar(" role_admin ", RecursoSistema.FUNCIONARIOS))
        assertTrue(PermissoesAcesso.podeAcessar("psicologo", RecursoSistema.PRONTUARIOS))
        assertFalse(PermissoesAcesso.podeAcessar("OUTRO", RecursoSistema.PACIENTES))
        assertFalse(PermissoesAcesso.podeAcessar(null, RecursoSistema.PACIENTES))
    }

    @Test
    fun psicologoNaoPodeAcessarModuloFuncionarios() {
        assertFalse(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.FUNCIONARIOS))
        assertFalse(PermissoesAcesso.podeAcessarTela("PSICOLOGO", "funcionario"))
        assertFalse(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.PSICOLOGOS))
        assertFalse(PermissoesAcesso.podeAcessarTela("PSICOLOGO", "psicologo"))
        assertFalse(PermissoesAcesso.podeAcessar("PSICOLOGO", RecursoSistema.RECEPCIONISTAS))
        assertFalse(PermissoesAcesso.podeAcessarTela("PSICOLOGO", "recepcionista"))
    }
}