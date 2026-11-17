package edu.front.psi.clin.utils

data class DiaSemanaAgenda(
    val nomeDia: String,
    val dataFormatada: String,
    val dataIso: String
)

fun diasNoMes(mes: Int, ano: Int): Int = when (mes) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) 29 else 28
    else -> 30
}

fun diaDaSemanaSakamoto(ano: Int, mes: Int, dia: Int): Int {
    val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    var y = ano
    if (mes < 3) y -= 1
    return (y + y / 4 - y / 100 + y / 400 + t[mes - 1] + dia) % 7
}

data class DataSimples(val dia: Int, val mes: Int, val ano: Int) {
    fun formatarDiaMes(): String = "${dia.toString().padStart(2, '0')}/${mes.toString().padStart(2, '0')}"
    fun formatarIso(): String = "$ano-${mes.toString().padStart(2, '0')}-${dia.toString().padStart(2, '0')}"

    fun adicionarDias(qtd: Int): DataSimples {
        var d = dia + qtd
        var m = mes
        var a = ano
        while (d > diasNoMes(m, a)) {
            d -= diasNoMes(m, a)
            m++
            if (m > 12) {
                m = 1
                a++
            }
        }
        while (d < 1) {
            m--
            if (m < 1) {
                m = 12
                a--
            }
            d += diasNoMes(m, a)
        }
        return DataSimples(d, m, a)
    }
}

fun obterPrimeiraSegundaDoMes(ano: Int, mes: Int): DataSimples {
    val dw = diaDaSemanaSakamoto(ano, mes, 1)
    val diasParaSegunda = when (dw) {
        1 -> 0
        0 -> 1
        else -> 8 - dw
    }
    return DataSimples(1 + diasParaSegunda, mes, ano)
}

val nomesDosMeses = listOf(
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
)

fun criarDiasDaSemana(segunda: DataSimples): List<DiaSemanaAgenda> {
    val nomesDosDias = listOf("Seg", "Ter", "Qua", "Qui", "Sex")
    return nomesDosDias.mapIndexed { indice, nome ->
        val data = segunda.adicionarDias(indice)
        DiaSemanaAgenda(
            nomeDia = nome,
            dataFormatada = data.formatarDiaMes(),
            dataIso = data.formatarIso()
        )
    }
}

fun tituloSemanaAgenda(inicio: DataSimples, fim: DataSimples): String {
    val nomeMesInicio = nomesDosMeses[inicio.mes - 1]
    val nomeMesFim = nomesDosMeses[fim.mes - 1]
    return when {
        inicio.ano != fim.ano ->
            "${inicio.dia} de $nomeMesInicio de ${inicio.ano} - ${fim.dia} de $nomeMesFim de ${fim.ano}"
        inicio.mes != fim.mes ->
            "${inicio.dia} de $nomeMesInicio - ${fim.dia} de $nomeMesFim de ${fim.ano}"
        else ->
            "${inicio.dia} - ${fim.dia} de $nomeMesInicio de ${fim.ano}"
    }
}