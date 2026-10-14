package edu.front.psi.clin

import java.time.LocalDateTime

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun obterDataHoraAtual(): String = LocalDateTime.now().toString()