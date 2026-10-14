package edu.front.psi.clin

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
@JsFun("() => new Date().toISOString()")
private external fun jsCurrentIsoDate(): String

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun obterDataHoraAtual(): String = jsCurrentIsoDate()