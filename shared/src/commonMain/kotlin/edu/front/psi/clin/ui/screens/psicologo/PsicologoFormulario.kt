package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel

@Composable
fun PsicologoFormulario(
    innerPadding: PaddingValues,
    vm: PsicologoViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Psicólogo" else "Cadastro de Psicólogo",
            subtitulo = if (isEdicao) "Altere os dados do psicólogo selecionado." else "Preencha os dados do psicólogo.",
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
                apenasNumeros = true,
                isError = vm.cpf.temErro.value,
                errorMessage = if (vm.cpf.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.email.valor.value,
                onValueChange = { vm.email.definir(it) },
                label = "E-mail",
                obrigatorio = true,
                isError = vm.email.temErro.value,
                errorMessage = if (vm.email.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.telefone.valor.value,
                onValueChange = { vm.telefone.definir(it) },
                label = "Telefone",
                obrigatorio = true,
                apenasNumeros = true,
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

            CampoTextoPadrao(
                value = vm.crp.valor.value,
                onValueChange = { vm.crp.definir(it) },
                label = "CRP",
                obrigatorio = true,
                isError = vm.crp.temErro.value,
                errorMessage = if (vm.crp.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.especialidade.valor.value,
                onValueChange = { vm.especialidade.definir(it) },
                label = "Especialidade",
                obrigatorio = true,
                isError = vm.especialidade.temErro.value,
                errorMessage = if (vm.especialidade.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.ativo.valor.value,
                onValueChange = { vm.ativo.definir(it) },
                label = "Status",
                placeholder = "Ativo? Digite true ou false",
                obrigatorio = true,
                isError = vm.ativo.temErro.value,
                errorMessage = if (vm.ativo.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}