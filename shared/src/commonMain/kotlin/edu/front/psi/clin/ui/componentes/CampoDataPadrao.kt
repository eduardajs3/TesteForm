package edu.front.psi.clin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.VerdeClinica
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
    modifier: Modifier = Modifier
) {
    var mostrarPicker by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true, // Impede a digitação manual via teclado
                enabled = false, // Garante foco limpo para o clique abrir o picker
                label = { Text(label) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar data",
                        tint = VerdeClinica
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledBorderColor = Color(0xFFE5E7EB),
                    disabledLabelColor = VerdeClinica,
                    disabledTextColor = Color(0xFF1F2937),
                    disabledTrailingIconColor = VerdeClinica
                )
            )

            // Camada transparente por cima para capturar o clique em qualquer parte do campo
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { mostrarPicker = true }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

    // Modal de Calendário
    if (mostrarPicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { mostrarPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Formata o timestamp em milissegundos para DD/MM/AAAA no fuso local
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            val dataFormatada = formatter.format(Date(millis))
                            onValueChange(dataFormatada)
                        }
                        mostrarPicker = false
                    }
                ) {
                    Text("OK", color = VerdeClinica)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarPicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}