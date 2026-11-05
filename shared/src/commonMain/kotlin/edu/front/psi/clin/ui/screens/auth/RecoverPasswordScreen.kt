package com.psiclin.front.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.components.AuthLayout
import edu.front.psi.clin.ui.components.AuthTextField
@Composable
fun RecoverPasswordScreen(
    onNavigate: (Route) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    AuthLayout {
        Text(
            text = "Recuperar senha",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Informe seus dados e defina sua nova senha",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            value = nome,
            onValueChange = { nome = it },
            label = "Nome completo",
            placeholder = "Digite seu nome completo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            placeholder = "Digite seu e-mail"
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = senha,
            onValueChange = { senha = it },
            label = "Nova senha",
            placeholder = "Digite sua nova senha",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = "Confirmar senha",
            placeholder = "Confirme sua nova senha",
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(
                        imageVector = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = VerdeClinica
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onNavigate(Route.Login)
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
                text = "Redefinir Senha",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Lembrou a senha? ",
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )
            Text(
                text = "Entrar na conta",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica,
                modifier = Modifier.clickable {
                    onNavigate(Route.Login)
                }
            )
        }
    }
}