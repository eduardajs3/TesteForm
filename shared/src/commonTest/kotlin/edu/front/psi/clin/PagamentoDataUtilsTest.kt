package edu.front.psi.clin

import edu.front.psi.clin.api.formatarDataPagamentoParaIso
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PagamentoDataUtilsTest {
    @Test
    fun formataDataBrasileiraComoDataHoraIso() {
        assertEquals("2026-11-15T00:00:00", formatarDataPagamentoParaIso("15/11/2026"))
    }

    @Test
    fun adicionaHorarioZeradoParaDataIsoSemHorario() {
        assertEquals("2026-11-15T00:00:00", formatarDataPagamentoParaIso("2026-11-15"))
    }

    @Test
    fun preservaDataHoraIsoERejeitaDataInvalida() {
        val dataHora = "2026-11-15T10:30:00"
        assertEquals(dataHora, formatarDataPagamentoParaIso(dataHora))
        assertFailsWith<IllegalArgumentException> {
            formatarDataPagamentoParaIso("15/11/2026T00:00")
        }
    }
}
