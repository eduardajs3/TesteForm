package edu.front.psi.clin

import edu.front.psi.clin.api.mensagemDeErroApi
import edu.front.psi.clin.api.BackendApiException
import edu.front.psi.clin.api.mensagemParaUsuario
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiErrorHandlerTest {
    @Test
    fun extraiMensagemDoCampoErro() {
        assertEquals(
            "O psicólogo já possui um agendamento neste horário",
            mensagemDeErroApi("""{"erro":"O psicólogo já possui um agendamento neste horário"}""")
        )
    }

    @Test
    fun combinaErrosDeValidacao() {
        assertEquals(
            "O e-mail deve ser válido\nO CPF não pode estar vazio",
            mensagemDeErroApi("""{"email":"O e-mail deve ser válido","cpf":"O CPF não pode estar vazio"}""")
        )
    }

    @Test
    fun usaMensagemGenericaParaRespostaSemMensagem() {
        assertEquals(
            "Não foi possível concluir a solicitação. Tente novamente.",
            mensagemDeErroApi("""{"timestamp":"2026-11-15T10:00:00"}""")
        )
    }

    @Test
    fun mensagemDoBackendNaoRecebePrefixoDaOperacao() {
        val erro = BackendApiException(400, "Já existe um paciente cadastrado com este CPF")

        assertEquals(
            "Já existe um paciente cadastrado com este CPF",
            erro.mensagemParaUsuario("Erro ao salvar")
        )
    }
}
