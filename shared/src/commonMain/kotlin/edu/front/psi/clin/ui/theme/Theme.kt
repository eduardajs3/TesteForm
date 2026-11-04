package edu.front.psi.clin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CoresApp {
    val Primaria = Color(0xFF3B5760)
    val FundoFormulario = Color(0xFFF6F3EC)
    val Branco = Color.White
    val Preto = Color.Black

    val TextoPrincipal = Color(0xFF1F2937)
    val TextoSecundario = Color(0xFF6E6E6E)
    val TextoCabecalho = Color(0xFF9CA3AF)
    val BordaPesquisa = Color(0xFFD0D0D0)
    val BordaContainer = Color(0xFFE5E7EB)
    val LinhaDivisoria = Color(0xFFF3F4F6)
    val FundoBotaoSecundario = Color(0xFFF3F4F6)
    val TextoBotaoSecundario = Color(0xFF4B5563)
    val Erro = Color(0xFFEF4444)

    val FundoBadgeAtivo = Color(0xFFA2C99B)
    val TextoBadgeAtivo = Color(0xFF2E5927)
    val FundoBadgeAlerta = Color(0xFFFEF3C7)
    val TextoBadgeAlerta = Color(0xFF92400E)
    val TextoBadgeNeutro = Color(0xFF374151)
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = CoresApp.Primaria,
            onPrimary = CoresApp.Branco
        ),
        content = content
    )
}
