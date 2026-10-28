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
                value = vm.nome.value,
                onValueChange = { vm.nome.value = it },
                label = "Nome do convênio"
            )

            CampoTextoPadrao(
                value = vm.cnpj.value,
                onValueChange = { vm.cnpj.value = it },
                label = "CNPJ"
            )

            CampoTextoPadrao(
                value = vm.telefone.value,
                onValueChange = { vm.telefone.value = it },
                label = "Telefone"
            )

            CampoTextoPadrao(
                value = vm.percentualCobertura.value,
                onValueChange = { vm.percentualCobertura.value = it },
                label = "Percentual de cobertura (ex.: 80)"
            )

            CampoTextoPadrao(
                value = vm.ativo.value,
                onValueChange = { vm.ativo.value = it },
                label = "Status",
                placeholder = "Ativo? Digite true ou false"
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}