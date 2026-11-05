package edu.front.psi.clin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = VerdeClinica,
    onPrimary = Branco,
    background = CremeCampo,
    surface = Branco,
    onSurface = TextoPrincipal,
    error = TextoErro,
    onError = Branco
)

@Composable
fun PsiClinTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}