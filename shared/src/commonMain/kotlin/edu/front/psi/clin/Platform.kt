package edu.front.psi.clin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun obterDataHoraAtual(): String