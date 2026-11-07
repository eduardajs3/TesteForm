package edu.front.psi.clin.ui.screens.funcionario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel

@Composable
fun FuncionarioFormulario(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Funcionário" else "Cadastro de Funcionário",
            subtitulo = if (isEdicao) "Altere os dados do funcionário selecionado." else "Preencha os dados do funcionário.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome completo",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.cpf.valor.value,
                onValueChange = { vm.cpf.definir(it) },
                label = "CPF",
                obrigatorio = true,
                isError = vm.cpf.temErro.value,
                errorMessage = if (vm.cpf.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.email.valor.value,
                onValueChange = { vm.email.definir(it) },
                label = "E-mail",
                obrigatorio = true,
                isError = vm.email.temErro.value,
                errorMessage = if (vm.email.temErro.value) vm.email.mensagemErro.value else null
            )

            CampoTextoPadrao(
                value = vm.telefone.valor.value,
                onValueChange = { vm.telefone.definir(it) },
                label = "Telefone",
                obrigatorio = true,
                isError = vm.telefone.temErro.value,
                errorMessage = if (vm.telefone.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.senha.valor.value,
                onValueChange = { vm.senha.definir(it) },
                label = "Senha",
                obrigatorio = true,
                isError = vm.senha.temErro.value,
                errorMessage = if (vm.senha.temErro.value) "Campo obrigatório" else null
            )

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

            CampoDataPadrao(
                value = vm.dataCadastro.valor.value,
                onValueChange = { vm.dataCadastro.definir(it) },
                label = "Data de cadastro",
                obrigatorio = true,
                isError = vm.dataCadastro.temErro.value,
                errorMessage = if (vm.dataCadastro.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else null,
                onSalvar = vm::salvar,
                textoBotaoSalvar =
                    if (isEdicao)
                        "Salvar Alterações"
                    else
                        "Gravar"
            )
        }
    }
}