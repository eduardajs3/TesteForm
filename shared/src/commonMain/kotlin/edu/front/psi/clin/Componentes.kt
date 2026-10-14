package edu.front.psi.clin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Campo de texto no padrão do exemplo da aula, estilizado com a paleta clean da PsiClin.
 */
@Composable
fun MeuTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    placeholder: String = "",
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = if (label.isNotBlank()) { { Text(label, color = TextoSecundario) } } else null,
        placeholder = if (placeholder.isNotBlank()) { { Text(placeholder, color = TextoSecundario.copy(alpha = 0.6f)) } } else null,
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeSalvia,
            unfocusedBorderColor = CremeBorda,
            focusedContainerColor = CremeCard,
            unfocusedContainerColor = VerdePastel.copy(alpha = 0.45f),
            focusedTextColor = TextoPrincipal,
            unfocusedTextColor = TextoPrincipal,
            cursorColor = VerdeSalvia
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    )
}

/**
 * Botão primário com fundo Verde Sálvia e texto branco.
 */
@Composable
fun BotaoPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VerdeSalvia,
            contentColor = Color.White
        ),
        modifier = modifier.height(48.dp)
    ) {
        Text(texto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

/**
 * Botão secundário estilo outlined, para ações como "Cancelar" e "Limpar".
 */
@Composable
fun BotaoSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextoSecundario
        ),
        modifier = modifier
            .height(48.dp)
            .border(1.dp, CremeBorda, RoundedCornerShape(10.dp))
    ) {
        Text(texto, fontWeight = FontWeight.Medium, fontSize = 15.sp)
    }
}

/**
 * Exibe mensagem de feedback (sucesso ou validação) em card discreto.
 */
@Composable
fun MensagemFeedback(mensagem: String) {
    if (mensagem.isNotBlank()) {
        val ehSucesso = mensagem.contains("sucesso", ignoreCase = true) || mensagem.contains("gravad", ignoreCase = true)
        val corFundo = if (ehSucesso) VerdePastel else Color(0xFFFDE8E8)
        val corTexto = if (ehSucesso) VerdeSucesso else Color(0xFFC53030)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .background(corFundo, shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = mensagem,
                color = corTexto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Card de formulário clean com cabeçalho.
 */
@Composable
fun CardFormulario(
    titulo: String,
    subtitulo: String = "Preencha as informações abaixo:",
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CremeCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CremeBorda, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            if (subtitulo.isNotBlank()) {
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = TextoSecundario,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
