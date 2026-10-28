package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.CampoDataPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.FuncionarioViewModel

@Composable
fun FuncionarioFormulario(
    innerPadding: PaddingValues,
    vm: FuncionarioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Funcionário" else "Cadastro de Funcionário",
            subtitulo = if (isEdicao) "Altere os dados do funcionário selecionado." else "Preencha os dados do funcionário.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.value,
                onValueChange = { vm.nome.value = it },
                label = "Nome completo"
            )

            CampoTextoPadrao(
                value = vm.cpf.value,
                onValueChange = { vm.cpf.value = it },
                label = "CPF"
            )

            CampoTextoPadrao(
                value = vm.email.value,
                onValueChange = { vm.email.value = it },
                label = "E-mail"
            )

            CampoTextoPadrao(
                value = vm.telefone.value,
                onValueChange = { vm.telefone.value = it },
                label = "Telefone"
            )

            CampoTextoPadrao(
                value = vm.senha.value,
                onValueChange = { vm.senha.value = it },
                label = "Senha"
            )

            CampoTextoPadrao(
                value = vm.ativo.value,
                onValueChange = { vm.ativo.value = it },
                label = "Status",
                placeholder = "Ativo? Digite true ou false"
            )

            CampoDataPadrao(
                value = vm.dataCadastro.value,
                onValueChange = { vm.dataCadastro.value = it },
                label = "Data de cadastro"
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}