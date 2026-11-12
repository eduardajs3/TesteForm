package edu.front.psi.clin

import edu.front.psi.clin.utils.MensagemFeedbackUtils
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MensagemFeedbackUtilsTest {
    @Test
    fun reconheceMensagensDeErro() {
        assertTrue(MensagemFeedbackUtils.ehErro("Erro ao salvar"))
        assertTrue(MensagemFeedbackUtils.ehErro("Preencha todos os campos obrigatórios."))
        assertTrue(MensagemFeedbackUtils.ehErro("Selecione Paciente, Psicólogo e Tipo de Atendimento."))
        assertTrue(MensagemFeedbackUtils.ehErro("Por favor, corrija os erros no formulário."))
        assertTrue(MensagemFeedbackUtils.ehErro("O horário de término deve ser posterior ao início."))
        assertTrue(MensagemFeedbackUtils.ehErro("A descrição da evolução é obrigatória!"))
    }

    @Test
    fun naoTrataMensagensDeSucessoComoErro() {
        assertFalse(MensagemFeedbackUtils.ehErro("Pagamento atualizado com sucesso!"))
        assertFalse(MensagemFeedbackUtils.ehErro("Consulta confirmada!"))
        assertFalse(MensagemFeedbackUtils.ehErro("Campos limpos."))
    }
}
