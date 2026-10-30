package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

@Composable
fun ConvenioFormulario(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Convênio" else "Cadastro de Convênio",
            subtitulo = if (isEdicao) "Altere os dados do convênio selecionado." else "Preencha os dados do convênio.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome do convênio",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.cnpj.valor.value,
                onValueChange = { vm.cnpj.definir(it) },
                label = "CNPJ",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.cnpj.temErro.value,
                errorMessage = if (vm.cnpj.temErro.value) "Campo obrigatório" else null
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
                value = vm.percentualCobertura.valor.value,
                onValueChange = { vm.percentualCobertura.definir(it) },
                label = "Percentual de cobertura (ex.: 80)",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.percentualCobertura.temErro.value,
                errorMessage = if (vm.percentualCobertura.temErro.value) "Campo obrigatório" else null
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