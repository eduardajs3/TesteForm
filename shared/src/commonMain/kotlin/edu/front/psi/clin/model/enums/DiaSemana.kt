package edu.front.psi.clin.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class DiaSemana(
    val num: Int,
    val rotulo: String
) {
    DOMINGO(1, "Domingo"),
    SEGUNDA(2, "Segunda-feira"),
    TERCA(3, "Terça-feira"),
    QUARTA(4, "Quarta-feira"),
    QUINTA(5, "Quinta-feira"),
    SEXTA(6, "Sexta-feira"),
    SABADO(7, "Sábado");

    companion object {
        fun deValorApi(valor: String): DiaSemana =
            entries.firstOrNull {
                it.name.equals(valor.trim(), ignoreCase = true) ||
                    it.rotulo.equals(valor.trim(), ignoreCase = true)
            } ?: when (valor.trim().uppercase()) {
                "1" -> DOMINGO
                "2" -> SEGUNDA
                "3" -> TERCA
                "4" -> QUARTA
                "5" -> QUINTA
                "6" -> SEXTA
                "7" -> SABADO
                else -> SEGUNDA
            }

        fun deNumero(numero: Int): DiaSemana =
            entries.firstOrNull { it.num == numero } ?: SEGUNDA
    }
}