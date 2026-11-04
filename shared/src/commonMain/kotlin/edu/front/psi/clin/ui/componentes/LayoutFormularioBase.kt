package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.theme.CoresApp

@Composable
fun LayoutFormularioBase(
    titulo: String,
    subtitulo: String = "Preencha os dados abaixo.",
    mensagemFeedback: String = "",
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = titulo,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = CoresApp.Primaria
        )
        Text(
            text = subtitulo,
            fontSize = 14.sp,
            color = CoresApp.TextoSecundario
        )

        Spacer(modifier = Modifier.height(16.dp))

        content()

        if (mensagemFeedback.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensagemFeedback,
                color = CoresApp.Primaria,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}