package edu.front.psi.clin

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import edu.front.psi.clin.api.SessionManager

fun main() {
    SessionManager.configure(System.getenv("PSICLIN_API_TOKEN"))
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FrontPsiClinic",
        ) {
            App()
        }
    }
}