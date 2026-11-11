package edu.front.psi.clin.ui.screens.pagamento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.PagamentoRotas
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

    val pacientesLista = vm.pacientes.collectAsState().value
    val conveniosLista = vm.convenios.collectAsState().value
    val agendasLista = vm.agendas.collectAsState().value

    val formaPagamentoOpcoes = listOf("PIX", "DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "BOLETO")
    val statusOpcoes = listOf("PENDENTE", "PAGO", "CANCELADO")

    // Nomes selecionados para exibição nos dropdowns com base nos IDs do ViewModel
    val pacienteAtualNome = pacientesLista.firstOrNull { it.id?.toLong() == vm.pacienteId.value }?.nome ?: ""
    val convenioAtualNome = conveniosLista.firstOrNull { it.id?.toLong() == vm.convenioId.value }?.nome ?: ""
    val agendaAtualDescricao = agendasLista.firstOrNull { it.id?.toLong() == vm.agendaId.value }?.let { "Agenda #${it.id} - ${it.dataHoraInicio}" } ?: ""

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Pagamento" else "Cadastro de Pagamento",
            subtitulo = if (isEdicao) "Altere os dados do pagamento selecionado." else "Preencha os dados do pagamento.",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(PagamentoRotas.Listagem)
            }
        ) {
            // Dropdown de Agenda
            CampoDropdownPadrao(
                label = "Agenda / Consulta *",
                valorSelecionado = agendaAtualDescricao,
                opcoes = agendasLista.map { "Agenda #${it.id} - ${it.dataHoraInicio}" },
                onOpcaoSelecionada = { selecao ->
                    val idStr = selecao.substringAfter("#").substringBefore(" -")
                    val agendaEscolhida = agendasLista.firstOrNull { it.id.toString() == idStr }
                    vm.agendaId.value = agendaEscolhida?.id?.toLong()
                },
                isError = vm.agendaId.value == null && vm.mensagem.value.isNotEmpty(),
                errorMessage = if (vm.agendaId.value == null && vm.mensagem.value.isNotEmpty()) "Campo obrigatório" else null
            )

            // Dropdown de Paciente
            CampoDropdownPadrao(
                label = "Paciente *",
                valorSelecionado = pacienteAtualNome,
                opcoes = pacientesLista.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val pacienteEscolhido = pacientesLista.firstOrNull { it.nome == nome }
                    vm.pacienteId.value = pacienteEscolhido?.id?.toLong()
                },
                isError = vm.pacienteId.value == null && vm.mensagem.value.isNotEmpty(),
                errorMessage = if (vm.pacienteId.value == null && vm.mensagem.value.isNotEmpty()) "Campo obrigatório" else null
            )

            // Dropdown de Convênio
            CampoDropdownPadrao(
                label = "Convênio *",
                valorSelecionado = convenioAtualNome,
                opcoes = conveniosLista.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val convenioEscolhido = conveniosLista.firstOrNull { it.nome == nome }
                    vm.convenioId.value = convenioEscolhido?.id?.toLong()
                },
                isError = vm.convenioId.value == null && vm.mensagem.value.isNotEmpty(),
                errorMessage = if (vm.convenioId.value == null && vm.mensagem.value.isNotEmpty()) "Campo obrigatório" else null
            )

            // Valores
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

            CampoDropdownPadrao(
                label = "Forma de pagamento",
                valorSelecionado = vm.formaPagamento.valor.value,
                opcoes = formaPagamentoOpcoes,
                onOpcaoSelecionada = { vm.formaPagamento.definir(it) },
                isError = vm.formaPagamento.temErro.value,
                errorMessage = if (vm.formaPagamento.temErro.value) "Campo obrigatório" else null
            )

            // Data do Pagamento
            CampoDataPadrao(
                value = vm.dataPagamento.valor.value,
                onValueChange = { vm.dataPagamento.definir(it) },
                label = "Data do pagamento",
                obrigatorio = true,
                isError = vm.dataPagamento.temErro.value,
                errorMessage = if (vm.dataPagamento.temErro.value) "Campo obrigatório" else null
            )

            // Status do Pagamento
            CampoDropdownPadrao(
                label = "Status do pagamento",
                valorSelecionado = vm.status.valor.value,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { vm.status.definir(it) },
                isError = vm.status.temErro.value,
                errorMessage = if (vm.status.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else null,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}