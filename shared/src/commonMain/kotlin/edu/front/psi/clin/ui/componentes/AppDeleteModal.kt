package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource

// Importe o Res do seu projeto (ajuste o pacote caso necessário, ex: frontpsiclinic...)
import frontpsiclinic.shared.generated.resources.*
// Exemplo: import frontpsiclinic.shared.generated.resources.ic_fechar

private val CorTextoTitulo = Color(0xFF1F2937)
private val CorTextoSubtitulo = Color(0xFF9CA3AF)
private val CorBotaoExcluir = Color(0xFFA30D18)

@Composable
fun AppDeleteModal(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String = "Excluir"
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CorTextoTitulo
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            // Substitua 'ic_fechar' pelo nome exato do seu arquivo XML na pasta drawable
                            painter = painterResource(Res.drawable.close), 
                            contentDescription = "Fechar modal",
                            tint = Color.Gray // Se quiser manter a cor original do XML, basta apagar o parâmetro 'tint'
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mensagem de confirmação
                Text(
                    text = message,
                    fontSize = 15.sp,
                    color = CorTextoSubtitulo
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botão Único Vermelho Arredondado (Pill)
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CorBotaoExcluir
                    ),
                    shape = CircleShape
                ) {
                    Text(
                        text = confirmText,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}