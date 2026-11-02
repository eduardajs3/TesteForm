package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.CampoDataPadrao
import edu.front.psi.clin.ui.components.CampoDropdownPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoFormulario(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    // Declaração limpa das opções e conversão de status no topo
    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.status.valor.value == "true" || vm.status.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Pagamento" else "Cadastro de Pagamento",
            subtitulo = if (isEdicao) "Altere os dados do pagamento selecionado." else "Preencha os dados do pagamento.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.paciente.valor.value,
                onValueChange = { vm.paciente.definir(it) },
                label = "Nome do paciente",
                obrigatorio = true,
                isError = vm.paciente.temErro.value,
                errorMessage = if (vm.paciente.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.convenio.valor.value,
                onValueChange = { vm.convenio.definir(it) },
                label = "Convênio",
                obrigatorio = true,
                isError = vm.convenio.temErro.value,
                errorMessage = if (vm.convenio.temErro.value) "Campo obrigatório" else null
            )

            // Valor total com máscara de moeda
            CampoTextoPadrao(
                value = vm.valorTotal.valor.value,
                onValueChange = { vm.valorTotal.definir(it) },
                label = "Valor total",
                obrigatorio = true,
                isError = vm.valorTotal.temErro.value,
                errorMessage = if (vm.valorTotal.temErro.value) "Campo obrigatório" else null
            )

            // Valor do convênio com máscara de moeda
            CampoTextoPadrao(
                value = vm.valorConvenio.valor.value,
                onValueChange = { vm.valorConvenio.definir(it) },
                label = "Valor do convênio",
                obrigatorio = true,
                isError = vm.valorConvenio.temErro.value,
                errorMessage = if (vm.valorConvenio.temErro.value) "Campo obrigatório" else null
            )

            // Valor do paciente com máscara de moeda
            CampoTextoPadrao(
                value = vm.valorPaciente.valor.value,
                onValueChange = { vm.valorPaciente.definir(it) },
                label = "Valor do paciente",
                obrigatorio = true,
                isError = vm.valorPaciente.temErro.value,
                errorMessage = if (vm.valorPaciente.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.formaPagamento.valor.value,
                onValueChange = { vm.formaPagamento.definir(it) },
                label = "Forma de pagamento",
                obrigatorio = true,
                isError = vm.formaPagamento.temErro.value,
                errorMessage = if (vm.formaPagamento.temErro.value) "Campo obrigatório" else null
            )

            CampoDataPadrao(
                value = vm.dataPagamento.valor.value,
                onValueChange = { vm.dataPagamento.definir(it) },
                label = "Data do pagamento",
                obrigatorio = true,
                isError = vm.dataPagamento.temErro.value,
                errorMessage = if (vm.dataPagamento.temErro.value) "Campo obrigatório" else null
            )

            // Dropdown padronizado para o Status do pagamento
            CampoDropdownPadrao(
                label = "Status do pagamento",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.status.definir(valorBoolean)
                },
                isError = vm.status.temErro.value,
                errorMessage = if (vm.status.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}