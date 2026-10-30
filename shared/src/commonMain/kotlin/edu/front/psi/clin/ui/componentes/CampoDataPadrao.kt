package edu.front.psi.clin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.VerdeClinica
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDataPadrao(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    obrigatorio: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var mostrarPicker by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Envolvemos o campo num Box clicável que aciona o DatePicker ao toque/clique
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    mostrarPicker = true
                }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                enabled = false, // Mantido false para o utilizador não digitar manualmente via teclado
                label = {
                    Text(if (obrigatorio) "$label *" else label)
                },
                isError = isError,
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.calendar_check_24px),
                        contentDescription = "Selecionar data",
                        tint = VerdeClinica,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledBorderColor = if (isError) Color(0xFFEF4444) else Color(0xFFE5E7EB),
                    disabledLabelColor = if (isError) Color(0xFFEF4444) else VerdeClinica,
                    disabledTextColor = Color(0xFF1F2937),
                    disabledTrailingIconColor = VerdeClinica
                )
            )
        }

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color(0xFFEF4444),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

    if (mostrarPicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { mostrarPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            onValueChange(formatter.format(Date(millis)))
                        }
                        mostrarPicker = false
                    }
                ) {
                    Text(text = "OK", color = VerdeClinica)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarPicker = false }
                ) {
                    Text(text = "Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}