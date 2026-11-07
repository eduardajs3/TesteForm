package com.psiclin.front.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import edu.front.psi.clin.ui.componentes.BotaoPadrao
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

@Composable
fun RecuperarSenhaScreen(
    onNavigate: (Route) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    var senhaVisivel by remember { mutableStateOf(false) }
    var confirmarSenhaVisivel by remember { mutableStateOf(false) }

    AuthLayout {
        Text(
            text = "Recuperar senha",
            style = MaterialTheme.typography.titleLarge,
            color = TextoPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Informe seus dados e defina sua nova senha",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
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
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(
                        painter = painterResource(
                            if (senhaVisivel) Res.drawable.visibility else Res.drawable.visibility_off
                        ),
                        contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                        tint = VerdeClinica,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = "Confirmar senha",
            placeholder = "Confirme sua nova senha",
            visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmarSenhaVisivel = !confirmarSenhaVisivel }) {
                    Icon(
                        painter = painterResource(
                            if (confirmarSenhaVisivel) Res.drawable.visibility else Res.drawable.visibility_off
                        ),
                        contentDescription = if (confirmarSenhaVisivel) "Ocultar senha" else "Mostrar senha",
                        tint = VerdeClinica,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BotaoPadrao(
            texto = "Redefinir Senha",
            onClick = {
                onNavigate(Route.Login)
            },
            containerColor = VerdeClinica,
            contentColor = Branco,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Lembrou a senha? ",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Text(
                text = "Entrar na conta",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica,
                modifier = Modifier.clickable {
                    onNavigate(Route.Login)
                }
            )
        }
    }
}