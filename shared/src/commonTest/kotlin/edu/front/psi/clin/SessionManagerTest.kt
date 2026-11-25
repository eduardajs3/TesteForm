package edu.front.psi.clin

import edu.front.psi.clin.api.SessionManager
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionManagerTest {
    @Test
    fun removePrefixoBearerQuandoPresente() {
        SessionManager.iniciarSessao("  Bearer eyJhbGciOi.test  ", "ADMIN", "admin@example.com")

        assertEquals("eyJhbGciOi.test", SessionManager.token)

        SessionManager.encerrarSessao()
    }

    @Test
    fun preservaTokenJwtSemPrefixoBearer() {
        SessionManager.iniciarSessao("eyJhbGciOi.test", "ADMIN", "admin@example.com")

        assertEquals("eyJhbGciOi.test", SessionManager.token)

        SessionManager.encerrarSessao()
    }
}