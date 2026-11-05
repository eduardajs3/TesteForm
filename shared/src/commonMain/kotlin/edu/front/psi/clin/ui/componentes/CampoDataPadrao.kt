package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CinzaBordaCampo
import edu.front.psi.clin.ui.theme.CinzaTextoData
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.VerdeClinica
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
                enabled = false,
                label = {
                    Text(
                        text = if (obrigatorio) "$label *" else label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                isError = isError,
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.calendar_month),
                        contentDescription = "Selecionar data",
                        tint = VerdeClinica,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Branco,
                    disabledBorderColor = if (isError) TextoErro else CinzaBordaCampo,
                    disabledLabelColor = if (isError) TextoErro else VerdeClinica,
                    disabledTextColor = TextoPrincipal,
                    disabledTrailingIconColor = VerdeClinica
                )
            )
        }

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = TextoErro,
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
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge,
                        color = VerdeClinica
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarPicker = false }
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = CinzaTextoData
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}