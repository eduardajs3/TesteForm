package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import edu.front.psi.clin.ui.theme.Branco
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.Res
import frontpsiclinic.shared.generated.resources.fundo_login

@Composable
fun AuthLayout(
    content: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val telaPequena = maxWidth < 600.dp

        Image(
            painter = painterResource(Res.drawable.fundo_login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = if (telaPequena) {
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            } else {
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 90.dp)
                    .width(470.dp)
            },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (telaPequena) 24.dp else 40.dp,
                        vertical = if (telaPequena) 24.dp else 36.dp
                    )
                    .then(
                        if (telaPequena) {
                            Modifier.verticalScroll(rememberScrollState())
                        } else {
                            Modifier
                        }
                    ),
                content = content
            )
        }
    }
}