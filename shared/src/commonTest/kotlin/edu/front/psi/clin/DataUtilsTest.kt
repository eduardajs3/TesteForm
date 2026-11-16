package edu.front.psi.clin

import edu.front.psi.clin.utils.formatarDataBrasileiraOuIsoParaDataHoraIso
import edu.front.psi.clin.utils.formatarDataBrasileiraParaIso
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DataUtilsTest {
    @Test
    fun formataDataBrasileiraComoIso() {
        assertEquals("2026-11-15", formatarDataBrasileiraParaIso("15/11/2026"))
    }

    @Test
    fun formataDataIsoComoBrasileira() {
        assertEquals("27/09/2022", formatarDataIsoParaBrasileira("2022-09-27T18:00:00Z"))
    }

    @Test
    fun formataDataHoraIsoComoBrasileiraPreservandoHorario() {
        assertEquals(
            "27/09/2022 18:00",
            formatarDataHoraIsoParaBrasileira("2022-09-27T18:00:00Z")
        )
    }

    @Test
    fun converteDataBrasileiraOuIsoParaDataHoraIso() {
        assertEquals(
            "2026-11-15T00:00:00",
            formatarDataBrasileiraOuIsoParaDataHoraIso("15/11/2026")
        )
        assertEquals(
            "2026-11-15T00:00:00",
            formatarDataBrasileiraOuIsoParaDataHoraIso("2026-11-15")
        )
        val dataHora = "2026-11-15T10:30:00"
        assertEquals(dataHora, formatarDataBrasileiraOuIsoParaDataHoraIso(dataHora))
        assertFailsWith<IllegalArgumentException> {
            formatarDataBrasileiraOuIsoParaDataHoraIso("15/11/2026T00:00")
        }
    }
}
