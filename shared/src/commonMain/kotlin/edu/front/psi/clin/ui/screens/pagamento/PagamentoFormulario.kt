package edu.front.psi.clin.ui.screens.pagamento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import edu.front.psi.clin.model.enums.PagamentoStatus
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira

@Composable
fun PagamentoFormulario(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    LaunchedEffect(vm) {
        vm.carregarDadosFormulario()
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        vm.carregarDadosFormulario()
    }

    val isEdicao = vm.id.value.isNotEmpty()
    val salvando = vm.salvando

    val agendaIdSelecionado = vm.agendaId.valor.value.toLongOrNull()
    val pacienteIdSelecionado = vm.pacienteId.valor.value.toLongOrNull()
    val convenioIdSelecionado = vm.convenioId.valor.value.toLongOrNull()

    val pacientesLista = vm.pacientes.collectAsState().value
    val conveniosLista = vm.convenios.collectAsState().value

    val pacientesDisponiveis = pacientesLista.filter {
        it.ativo || (isEdicao && it.id == pacienteIdSelecionado)
    }
    val conveniosDisponiveis = conveniosLista.filter {
        it.ativo || (isEdicao && it.id == convenioIdSelecionado)
    }

    val agendasLista = vm.agendas.collectAsState().value
    val agendasSemPagamento = vm.agendasSemPagamento.collectAsState().value
    val agendaAtual = agendasLista.firstOrNull { it.id == agendaIdSelecionado }
    val agendasParaSelecionar = if (isEdicao && agendaAtual != null) {
        (agendasSemPagamento + agendaAtual).distinctBy { it.id }
    } else {
        agendasSemPagamento
    }

    val formaPagamentoOpcoes = listOf("PIX", "DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "BOLETO")

    val pacienteAtualNome = pacientesLista.firstOrNull { it.id == pacienteIdSelecionado }?.nome ?: ""
    val convenioAtualNome = conveniosLista.firstOrNull { it.id == convenioIdSelecionado }?.nome ?: ""
    val agendaAtualDescricao = agendasLista.firstOrNull { it.id == agendaIdSelecionado }
        ?.let { "Agenda #${it.id} - ${formatarDataHoraIsoParaBrasileira(it.dataHoraInicio)}" }
        ?: ""

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Pagamento" else "Cadastro de Pagamento",
            subtitulo = if (isEdicao) "Altere os dados do pagamento selecionado." else "Preencha os dados do pagamento.",
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(PagamentoRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Agenda / Consulta *",
                valorSelecionado = agendaAtualDescricao,
                opcoes = agendasParaSelecionar.map {
                    "Agenda #${it.id} - ${formatarDataHoraIsoParaBrasileira(it.dataHoraInicio)}"
                },
                onOpcaoSelecionada = { selecao ->
                    val idStr = selecao.substringAfter("#").substringBefore(" -")
                    val agendaEscolhida = agendasLista.firstOrNull { it.id.toString() == idStr }
                    vm.agendaId.definir(agendaEscolhida?.id?.toString().orEmpty())
                    vm.pacienteId.definir(agendaEscolhida?.pacienteId?.toString().orEmpty())
                    vm.convenioId.definir(agendaEscolhida?.convenioId?.toString().orEmpty())
                },
                isError = vm.agendaId.temErro.value,
                errorMessage = if (vm.agendaId.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Paciente *",
                valorSelecionado = pacienteAtualNome,
                opcoes = pacientesDisponiveis.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val pacienteEscolhido = pacientesDisponiveis.firstOrNull { it.nome == nome }
                    vm.pacienteId.definir(pacienteEscolhido?.id?.toString().orEmpty())
                },
                isError = vm.pacienteId.temErro.value,
                errorMessage = if (vm.pacienteId.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Convênio *",
                valorSelecionado = convenioAtualNome,
                opcoes = conveniosDisponiveis.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val convenioEscolhido = conveniosDisponiveis.firstOrNull { it.nome == nome }
                    vm.convenioId.definir(convenioEscolhido?.id?.toString().orEmpty())
                },
                isError = vm.convenioId.temErro.value,
                errorMessage = if (vm.convenioId.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.valorTotal.valor.value,
                onValueChange = { vm.valorTotal.definir(it) },
                label = "Valor total",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.valorTotal.temErro.value,
                errorMessage = if (vm.valorTotal.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.valorConvenio.valor.value,
                onValueChange = { vm.valorConvenio.definir(it) },
                label = "Valor do convênio",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.valorConvenio.temErro.value,
                errorMessage = if (vm.valorConvenio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.valorPaciente.valor.value,
                onValueChange = { vm.valorPaciente.definir(it) },
                label = "Valor do paciente",
                obrigatorio = true,
                apenasNumeros = true,
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
                valorSelecionado = PagamentoStatus.deValorApi(vm.status.valor.value).rotulo,
                opcoes = PagamentoStatus.entries.map { it.rotulo },
                onOpcaoSelecionada = { selecao ->
                    val selecionado = PagamentoStatus.entries.firstOrNull { it.rotulo == selecao }
                    vm.status.definir(selecionado?.name.orEmpty())
                },
                isError = vm.status.temErro.value,
                errorMessage = if (vm.status.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao && !salvando) vm::limparCampos else null,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (salvando) {
                    "Salvando..."
                } else {
                    if (isEdicao) "Salvar Alterações" else "Gravar"
                }
            )
        }
    }
}