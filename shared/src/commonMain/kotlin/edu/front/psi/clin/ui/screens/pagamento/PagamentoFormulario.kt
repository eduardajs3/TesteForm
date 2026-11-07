package edu.front.psi.clin.ui.screens.pagamento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

@Composable
fun PagamentoFormulario(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val pacienteOpcoes = listOf(
        "Ana Clara Souza",
        "Carlos Eduardo Lima",
        "Mariana Ribeiro",
        "João Pedro Santos"
    )
    val convenioOpcoes = listOf(
        "Particular",
        "Unimed",
        "Amil",
        "Bradesco Saúde",
        "SulAmérica"
    )
    val statusOpcoes = listOf(
        "Pendente",
        "Pago",
        "Cancelado"
    )

    val pacienteAtual = vm.paciente.valor.value
    val pacienteExibicao = if (pacienteOpcoes.contains(pacienteAtual)) pacienteAtual else (pacienteOpcoes.firstOrNull() ?: "")

    val convenioAtual = vm.convenio.valor.value
    val convenioExibicao = if (convenioOpcoes.contains(convenioAtual)) convenioAtual else (convenioOpcoes.firstOrNull() ?: "")

    val statusAtual = vm.status.valor.value
    val statusExibicao = when {
        statusOpcoes.contains(statusAtual) -> statusAtual
        statusAtual.lowercase() == "true" -> "Pago"
        statusAtual.lowercase() == "false" -> "Pendente"
        else -> "Pendente"
    }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Pagamento" else "Cadastro de Pagamento",
            subtitulo = if (isEdicao) "Altere os dados do pagamento selecionado." else "Preencha os dados do pagamento.",
            mensagemFeedback = vm.mensagem.value
        ) {
    
            CampoDropdownPadrao(
                label = "Nome do paciente",
                valorSelecionado = pacienteExibicao,
                opcoes = pacienteOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.paciente.definir(selecao)
                },
                isError = vm.paciente.temErro.value,
                errorMessage = if (vm.paciente.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Convênio",
                valorSelecionado = convenioExibicao,
                opcoes = convenioOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.convenio.definir(selecao)
                },
                isError = vm.convenio.temErro.value,
                errorMessage = if (vm.convenio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.valorTotal.valor.value,
                onValueChange = { vm.valorTotal.definir(it) },
                label = "Valor total",
                obrigatorio = true,
                isError = vm.valorTotal.temErro.value,
                errorMessage = if (vm.valorTotal.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.valorConvenio.valor.value,
                onValueChange = { vm.valorConvenio.definir(it) },
                label = "Valor do convênio",
                obrigatorio = true,
                isError = vm.valorConvenio.temErro.value,
                errorMessage = if (vm.valorConvenio.temErro.value) "Campo obrigatório" else null
            )

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

            CampoDropdownPadrao(
                label = "Status do pagamento",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.status.definir(selecao)
                },
                isError = vm.status.temErro.value,
                errorMessage = if (vm.status.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else { {} },
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}