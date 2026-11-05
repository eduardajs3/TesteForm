package com.psiclin.front.ui.screenss.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.ui.componentes.AuthLayout
import edu.front.psi.clin.ui.componentes.AuthTextField
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica

@Composable
fun LoginScreen(
    onNavigate: (Route) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AuthLayout {
        Text(
            text = "Acesse sua conta",
            style = MaterialTheme.typography.titleLarge,
            color = TextoPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Informe seu e-mail e sua senha para entrar",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(28.dp))

        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "Digite seu e-mail"
        )

        Spacer(modifier = Modifier.height(20.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Senha",
            placeholder = "Digite sua senha",
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = VerdeClinica
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Esqueci a senha",
                modifier = Modifier.clickable {
                    onNavigate(Route.RecoverPassword)
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                onNavigate(Route.Dashboard)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClinica
            )
        ) {
            Text(
                text = "Entrar",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Branco
            )
        }
    }
}