package edu.front.psi.clin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta Clean - Verde Claro & Creme (Estilo PsyClin)
val VerdeSalvia = Color(0xFF5B8266)      // Primária: Verde sálvia refinado
val VerdeClaro = Color(0xFF8EB69B)       // Destaques e acentos
val VerdePastel = Color(0xFFE8EFEA)      // Fundo suave de inputs e realces
val VerdeSucesso = Color(0xFF2D6A4F)     // Mensagens de sucesso

val CremeFundo = Color(0xFFFAF7F2)       // Fundo geral acolhedor (off-white)
val CremeCard = Color(0xFFFFFFFF)        // Cartões / Superfícies
val CremeBorda = Color(0xFFD8E0DA)       // Bordas delicadas

val TextoPrincipal = Color(0xFF233D2F)   // Texto primário em tom verde-grafite
val TextoSecundario = Color(0xFF6B7F72)  // Texto auxiliar e rótulos
val CinzaCancelar = Color(0xFF8D99AE)    // Botão cancelar neutro

val PsiClinColorScheme = lightColorScheme(
    primary = VerdeSalvia,
    onPrimary = Color.White,
    primaryContainer = VerdePastel,
    onPrimaryContainer = TextoPrincipal,
    secondary = VerdeClaro,
    onSecondary = Color.White,
    background = CremeFundo,
    onBackground = TextoPrincipal,
    surface = CremeCard,
    onSurface = TextoPrincipal,
    outline = CremeBorda
)

@Composable
fun PsiClinTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PsiClinColorScheme,
        content = content
    )
}
