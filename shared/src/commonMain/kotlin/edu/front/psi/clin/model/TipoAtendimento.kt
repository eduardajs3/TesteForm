package edu.front.psi.clin.model
import kotlinx.serialization.Serializable

@Serializable
enum class ModalidadeAtendimento(val rotulo: String) {
    PRESENCIAL("Presencial"),
    ONLINE("Online"),
    HIBRIDO("Híbrido");

    companion object {
        fun deValorApi(valor: String): ModalidadeAtendimento =
            entries.firstOrNull { it.name.equals(valor, ignoreCase = true) } ?: PRESENCIAL
    }
}

@Serializable
data class TipoAtendimento(
    val id: Long? = null,
    val nome: String,
    val descricao: String,
    val duracao: String,
    val ativo: Boolean,
    val dataCadastro: String = "",
    val modalidade: ModalidadeAtendimento = ModalidadeAtendimento.PRESENCIAL
)