package edu.front.psi.clin.ui.screens.funcionario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.navigation.FuncionarioRotas
import edu.front.psi.clin.ui.componentes.AuthLayout
import edu.front.psi.clin.ui.componentes.AuthTextField
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.BotaoPadrao
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.Res
import frontpsiclinic.shared.generated.resources.arrow_back
import frontpsiclinic.shared.generated.resources.visibility
import frontpsiclinic.shared.generated.resources.visibility_off

@Composable
fun FuncionarioFormulario(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()
    val senhaVisivel = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        AuthLayout {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        vm.backStack.clear()
                        vm.backStack.add(FuncionarioRotas.Listagem)
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_back),
                        contentDescription = "Voltar para funcionários",
                        tint = VerdeClinica
                    )
                }
                Column {
                    Text(
                        text = if (isEdicao) "Editar Funcionário" else "Cadastro de Funcionário",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextoPrincipal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isEdicao) "Altere os dados do funcionário selecionado." else "Preencha os dados abaixo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AuthTextField(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome completo *",
                placeholder = "Digite o nome completo",
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = vm.cpf.valor.value,
                onValueChange = { vm.cpf.definir(it.filter(Char::isDigit)) },
                label = "CPF *",
                placeholder = "Digite o CPF",
                isError = vm.cpf.temErro.value,
                errorMessage = if (vm.cpf.temErro.value) "Campo obrigatório" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = vm.email.valor.value,
                onValueChange = { vm.email.definir(it) },
                label = "E-mail *",
                placeholder = "Digite o e-mail",
                isError = vm.email.temErro.value,
                errorMessage = if (vm.email.temErro.value) vm.email.mensagemErro.value else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = vm.telefone.valor.value,
                onValueChange = { vm.telefone.definir(it.filter(Char::isDigit)) },
                label = "Telefone *",
                placeholder = "Digite o telefone",
                isError = vm.telefone.temErro.value,
                errorMessage = if (vm.telefone.temErro.value) "Campo obrigatório" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = vm.senha.valor.value,
                onValueChange = { vm.senha.definir(it) },
                label = if (isEdicao) "Nova senha (opcional)" else "Senha *",
                placeholder = if (isEdicao) "Digite para alterar a senha" else "Digite a senha",
                visualTransformation = if (senhaVisivel.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel.value = !senhaVisivel.value }) {
                        Icon(
                            painter = painterResource(
                                if (senhaVisivel.value) Res.drawable.visibility else Res.drawable.visibility_off
                            ),
                            contentDescription = if (senhaVisivel.value) "Ocultar senha" else "Mostrar senha",
                            tint = VerdeClinica,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                isError = !isEdicao && vm.senha.temErro.value,
                errorMessage = if (!isEdicao && vm.senha.temErro.value) "Campo obrigatório" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.ativo.definir(valorBoolean)
                },
                isError = vm.ativo.temErro.value,
                errorMessage = if (vm.ativo.temErro.value) "Campo obrigatório" else null
            )

            if (vm.mensagem.value.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = vm.mensagem.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BotaoPadrao(
                texto = if (isEdicao) "Salvar Alterações" else "Cadastrar Funcionário",
                onClick = vm::salvar,
                containerColor = VerdeClinica,
                contentColor = Branco,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            if (!isEdicao) {
                TextButton(
                    onClick = vm::limparCampos,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Limpar campos",
                        color = VerdeClinica,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}