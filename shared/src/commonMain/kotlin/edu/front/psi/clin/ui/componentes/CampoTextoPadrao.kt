package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.VerdeClinica

@Composable
fun CampoTextoPadrao(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    obrigatorio: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    apenasNumeros: Boolean = false, // Novo parâmetro
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { novoTexto ->
                // Se apenasNumeros for true, filtra para manter apenas dígitos (0-9)
                if (apenasNumeros) {
                    onValueChange(novoTexto.filter { it.isDigit() })
                } else {
                    onValueChange(novoTexto)
                }
            },
            label = { Text(if (obrigatorio) "$label *" else label) },
            placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            // Exibe o teclado numérico no telemóvel/dispositivo se apenasNumeros for verdadeiro
            keyboardOptions = KeyboardOptions(
                keyboardType = if (apenasNumeros) KeyboardType.Number else KeyboardType.Text
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = VerdeClinica,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedLabelColor = VerdeClinica,
                errorBorderColor = Color(0xFFEF4444),
                errorLabelColor = Color(0xFFEF4444)
            )
        )
        
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color(0xFFEF4444),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}