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
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel

@Composable
fun SalaAtendimentoFormulario(
    innerPadding: PaddingValues,
    vm: SalaAtendimentoViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Sala de Atendimento" else "Cadastro de Sala de Atendimento",
            subtitulo = if (isEdicao) "Altere os dados da sala selecionada." else "Preencha os dados da sala de atendimento.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome da sala",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.descricao.valor.value,
                onValueChange = { vm.descricao.definir(it) },
                label = "Descrição da sala",
                obrigatorio = true,
                isError = vm.descricao.temErro.value,
                errorMessage = if (vm.descricao.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.capacidade.valor.value,
                onValueChange = { vm.capacidade.definir(it) },
                label = "Capacidade de pessoas",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.capacidade.temErro.value,
                errorMessage = if (vm.capacidade.temErro.value) "Campo obrigatório" else null
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

            CampoDataPadrao(
                value = vm.dataCadastro.valor.value,
                onValueChange = { vm.dataCadastro.definir(it) },
                label = "Data de cadastro",
                obrigatorio = true,
                isError = vm.dataCadastro.temErro.value,
                errorMessage = if (vm.dataCadastro.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}