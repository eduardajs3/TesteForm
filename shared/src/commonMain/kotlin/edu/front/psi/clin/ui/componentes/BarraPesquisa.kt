package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CinzaBordaPesquisa
import edu.front.psi.clin.ui.theme.CinzaTextoPesquisa
import edu.front.psi.clin.ui.theme.TextoPrincipal
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

@Composable
fun BarraPesquisa(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "Buscar"
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp)
            .background(Branco, shape = RoundedCornerShape(50.dp)),
        placeholder = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodyMedium,
                color = CinzaTextoPesquisa
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.search_24px),
                contentDescription = "Buscar",
                tint = CinzaTextoPesquisa,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(
                        painter = painterResource(Res.drawable.close),
                        contentDescription = "Limpar",
                        tint = CinzaTextoPesquisa,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CinzaBordaPesquisa,
            unfocusedBorderColor = CinzaBordaPesquisa,
            focusedContainerColor = Branco,
            unfocusedContainerColor = Branco,
            focusedTextColor = TextoPrincipal,
            unfocusedTextColor = TextoPrincipal
        )
    )
}