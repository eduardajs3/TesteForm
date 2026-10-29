package edu.front.psi.clin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.VerdeClinica

@Composable
fun CampoTextoPadrao(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    obrigatorio: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = VerdeClinica,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedLabelColor = VerdeClinica
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}