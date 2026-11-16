package edu.front.psi.clin

import edu.front.psi.clin.model.EvolucaoClinica
import edu.front.psi.clin.model.Prontuario
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ClinicalApiResponseTest {
    @Test
    fun desserializaProntuarioComPacienteIdDoBackend() {
        val prontuarios = Json.decodeFromString<List<Prontuario>>(
            """[{"id":7,"pacienteId":12,"dataAbertura":"2026-11-15T10:00:00","queixaPrincipal":"Ansiedade","status":"ATIVO","ativo":true}]"""
        )

        assertEquals(12L, prontuarios.single().pacienteId)
        assertEquals("ATIVO", prontuarios.single().status)
    }

    @Test
    fun desserializaEvolucaoComIdsDoBackend() {
        val evolucoes = Json.decodeFromString<List<EvolucaoClinica>>(
            """[{"id":3,"prontuarioId":7,"psicologoId":9,"agendaId":15,"dataRegistro":"2026-11-15T10:30:00","descricao":"Registro clínico","ativo":true}]"""
        )
        val evolucao = evolucoes.single()

        assertEquals(7L, evolucao.prontuarioId)
        assertEquals(9L, evolucao.psicologoId)
        assertEquals(15L, evolucao.agendaId)
    }
}
