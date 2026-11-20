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
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import frontpsiclinic.shared.generated.resources.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Instant
import org.jetbrains.compose.resources.painterResource

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
        val dataInicialMillis = remember(value) {
            runCatching {
                val data = if (value.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                    val (dia, mes, ano) = value.split("/").map(String::toInt)
                    LocalDate(ano, mes, dia)
                } else {
                    LocalDate.parse(value.substringBefore('T'))
                }
                data.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
            }.getOrNull()
        }
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dataInicialMillis)

        DatePickerDialog(
            onDismissRequest = { mostrarPicker = false },
            shape = RoundedCornerShape(28.dp),
            colors = DatePickerDefaults.colors(
                containerColor = Branco  
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val data = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            onValueChange(formatarDataIsoParaBrasileira(data.toString()))
                        }
                        mostrarPicker = false
                    }
                ) {
                    Text(
                        text = "Confirmar",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
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
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = CinzaTextoData 
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Branco
                )
            )
        }
    }
}
