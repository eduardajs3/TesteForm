package edu.front.psi.clin.utils

import kotlin.math.roundToLong

object MaskUtils {

    fun aplicarMascaraCpf(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }.take(11)
        val sb = StringBuilder()
        for (i in apenasDigitos.indices) {
            if (i == 3 || i == 6) sb.append('.')
            if (i == 9) sb.append('-')
            sb.append(apenasDigitos[i])
        }
        return sb.toString()
    }

    fun aplicarMascaraCnpj(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }.take(14)
        val sb = StringBuilder()
        for (i in apenasDigitos.indices) {
            if (i == 2 || i == 5) sb.append('.')
            if (i == 8) sb.append('/')
            if (i == 12) sb.append('-')
            sb.append(apenasDigitos[i])
        }
        return sb.toString()
    }

    fun aplicarMascaraTelefone(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }.take(11)
        val sb = StringBuilder()
        for (i in apenasDigitos.indices) {
            if (i == 0) sb.append('(')
            if (i == 2) sb.append(") ")
            val limiteTraco = if (apenasDigitos.length > 10) 7 else 6
            if (i == limiteTraco) sb.append('-')
            sb.append(apenasDigitos[i])
        }
        return sb.toString()
    }

    fun aplicarMascaraMoeda(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }
        val valorLong = apenasDigitos.toLongOrNull() ?: 0L
        if (valorLong == 0L) return ""

        val reais = valorLong / 100
        val centavos = (valorLong % 100).toString().padStart(2, '0')
        val reaisFormatados = reais
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()

        return "R$ $reaisFormatados,$centavos"
    }

    fun validarEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return regex.matches(email)
    }

    fun aplicarMascaraHora(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }.take(4)
        val sb = StringBuilder()

        for (i in apenasDigitos.indices) {
            if (i == 2) sb.append(':')
            sb.append(apenasDigitos[i])
        }
        return sb.toString()
    }

    fun aplicarMascaraCrp(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }.take(7)
        val sb = StringBuilder()

        for (i in apenasDigitos.indices) {
            if (i == 2) sb.append('/')
            sb.append(apenasDigitos[i])
        }
        return sb.toString()
    }

    fun aplicarMascaraPercentual(valor: String): String {
        val apenasDigitos = valor.filter { it.isDigit() }.take(3)
        if (apenasDigitos.isEmpty()) return ""
        val percentual = apenasDigitos.toIntOrNull()?.coerceAtMost(100) ?: return ""
        return "$percentual%"
    }

    fun converterPercentualParaDouble(valorFormatado: String): Double {
        val limpo = valorFormatado
            .replace("%", "")
            .replace(",", ".")
            .trim()
        return limpo.toDoubleOrNull() ?: 0.0
    }

    fun converterMoedaParaDouble(valorFormatado: String): Double {
        val apenasDigitos = valorFormatado.filter { it.isDigit() }
        if (apenasDigitos.isEmpty()) return 0.0
        return apenasDigitos.toDouble() / 100.0
    }

    fun moedaDoubleParaCentavos(valor: Double): String =
        (valor * 100.0).roundToLong().toString()
}