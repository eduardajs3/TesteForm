package edu.front.psi.clin

import edu.front.psi.clin.model.HorarioDisponivel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains

class HorarioDisponivelSerializationTest {
    private val json = Json { encodeDefaults = true }

    @Test
    fun serializaAtivoTrueEFalseExplicitamente() {
        val ativo = HorarioDisponivel(
            psicologoId = 1,
            diaSemana = "SEGUNDA",
            horaInicio = "08:00:00",
            horaFim = "09:00:00",
            ativo = true
        )
        val inativo = ativo.copy(ativo = false)

        assertContains(json.encodeToString(ativo), "\"ativo\":true")
        assertContains(json.encodeToString(inativo), "\"ativo\":false")
    }
}
