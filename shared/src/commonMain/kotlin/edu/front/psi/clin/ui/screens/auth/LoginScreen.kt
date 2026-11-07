package com.psiclin.front.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun LoginScreen(
    onNavigate: (Route) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

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
            value = senha,
            onValueChange = { senha = it },
            label = "Senha",
            placeholder = "Digite sua senha",
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

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Esqueci a senha",
                modifier = Modifier.clickable {
                    onNavigate(Route.RecuperarSenha)
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        BotaoPadrao(
            texto = "Entrar",
            onClick = {
                onNavigate(Route.Dashboard)
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Não tem uma conta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Text(
                text = "Cadastre-se",
                modifier = Modifier.clickable {
                    onNavigate(Route.Cadastro)
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = VerdeClinica
            )
        }
    }
}