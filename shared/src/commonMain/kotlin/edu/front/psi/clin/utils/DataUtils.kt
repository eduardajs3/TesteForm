package edu.front.psi.clin.utils

fun formatarDataBrasileiraParaIso(data: String): String {
    val partes = data.split("/")
    return if (partes.size == 3) {
        "${partes[2]}-${partes[1]}-${partes[0]}"
    } else {
        data
    }
}

fun formatarDataIsoParaBrasileira(data: String): String {
    val partes = data.substringBefore('T').split("-")
    return if (partes.size == 3) {
        "${partes[2]}/${partes[1]}/${partes[0]}"
    } else {
        data
    }
}

fun formatarDataHoraIsoParaBrasileira(dataHora: String): String {
    val partes = dataHora.split('T', limit = 2)
    if (partes.size != 2 || !partes[0].matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
        return formatarDataIsoParaBrasileira(dataHora)
    }

    val hora = partes[1].take(5)
    if (!hora.matches(Regex("\\d{2}:\\d{2}"))) return dataHora

    return "${formatarDataIsoParaBrasileira(partes[0])} $hora"
}

fun formatarDataBrasileiraOuIsoParaDataHoraIso(
    data: String,
    mensagemErro: String = "Data inválida: use dd/MM/yyyy, yyyy-MM-dd ou uma data e hora ISO."
): String =
    when {
        data.matches(Regex("\\d{2}/\\d{2}/\\d{4}")) -> {
            val (dia, mes, ano) = data.split("/")
            "$ano-$mes-${dia}T00:00:00"
        }
        data.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> "${data}T00:00:00"
        data.matches(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2}(\\.\\d{1,9})?)?")) -> data
        else -> throw IllegalArgumentException(mensagemErro)
    }
