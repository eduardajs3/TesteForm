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
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoFormulario(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Pagamento" else "Cadastro de Pagamento",
            subtitulo = if (isEdicao) "Altere os dados do pagamento selecionado." else "Preencha os dados do pagamento.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.paciente.value,
                onValueChange = { vm.paciente.value = it },
                label = "Nome do paciente"
            )

            CampoTextoPadrao(
                value = vm.convenio.value,
                onValueChange = { vm.convenio.value = it },
                label = "Convênio"
            )

            CampoTextoPadrao(
                value = vm.valorTotal.value,
                onValueChange = { vm.valorTotal.value = it },
                label = "Valor total"
            )

            CampoTextoPadrao(
                value = vm.valorConvenio.value,
                onValueChange = { vm.valorConvenio.value = it },
                label = "Valor do convênio"
            )

            CampoTextoPadrao(
                value = vm.valorPaciente.value,
                onValueChange = { vm.valorPaciente.value = it },
                label = "Valor do paciente"
            )

            CampoTextoPadrao(
                value = vm.formaPagamento.value,
                onValueChange = { vm.formaPagamento.value = it },
                label = "Forma de pagamento"
            )

            CampoDataPadrao(
                value = vm.dataPagamento.value,
                onValueChange = { vm.dataPagamento.value = it },
                label = "Data do pagamento"
            )

            CampoTextoPadrao(
                value = vm.status.value,
                onValueChange = { vm.status.value = it },
                label = "Status do pagamento"
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}