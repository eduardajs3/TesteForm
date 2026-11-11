package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

@Composable
fun AgendaFormulario(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val pacientesLista = vm.pacientes.collectAsState().value
    val psicologosLista = vm.psicologos.collectAsState().value
    val salasLista = vm.salas.collectAsState().value
    val tiposAtendimentoLista = vm.tiposAtendimento.collectAsState().value
    val conveniosLista = vm.convenios.collectAsState().value

    val listaStatusAgenda = listOf("AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO", "FALTOU")

    // Nomes selecionados para exibição comparando explicitamente Long com Long?
    val pacienteAtualNome = pacientesLista.firstOrNull { it.id?.toLong() == vm.pacienteId.value }?.nome ?: ""
    val psicologoAtualNome = psicologosLista.firstOrNull { it.id?.toLong() == vm.psicologoId.value }?.nome ?: ""
    val salaAtualNome = salasLista.firstOrNull { it.id?.toLong() == vm.salaId.value }?.nome ?: ""
    val tipoAtendimentoAtualNome = tiposAtendimentoLista.firstOrNull { it.id?.toLong() == vm.tipoAtendimentoId.value }?.nome ?: ""
    val convenioAtualNome = conveniosLista.firstOrNull { it.id?.toLong() == vm.convenioId.value }?.nome ?: ""

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Agendamento" else "Cadastro de Agenda",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(AgendaRotas.Listagem)
            }
        ) {
            // Dropdown de Paciente
            CampoDropdownPadrao(
                label = "Paciente *",
                opcoes = pacientesLista.map { it.nome },
                valorSelecionado = pacienteAtualNome,
                onOpcaoSelecionada = { nome ->
                    val pacienteEscolhido = pacientesLista.firstOrNull { it.nome == nome }
                    vm.pacienteId.value = pacienteEscolhido?.id?.toLong()
                },
                isError = vm.pacienteId.value == null && vm.mensagem.value.isNotEmpty(),
                errorMessage = if (vm.pacienteId.value == null && vm.mensagem.value.isNotEmpty()) "Campo obrigatório" else null
            )

            // Dropdown de Psicólogo
            CampoDropdownPadrao(
                label = "Psicólogo *",
                opcoes = psicologosLista.map { it.nome },
                valorSelecionado = psicologoAtualNome,
                onOpcaoSelecionada = { nome ->
                    val psicologoEscolhido = psicologosLista.firstOrNull { it.nome == nome }
                    vm.psicologoId.value = psicologoEscolhido?.id?.toLong()
                },
                isError = vm.psicologoId.value == null && vm.mensagem.value.isNotEmpty(),
                errorMessage = if (vm.psicologoId.value == null && vm.mensagem.value.isNotEmpty()) "Campo obrigatório" else null
            )

            // Dropdown de Tipo de Atendimento
            CampoDropdownPadrao(
                label = "Tipo de Atendimento *",
                opcoes = tiposAtendimentoLista.map { it.nome },
                valorSelecionado = tipoAtendimentoAtualNome,
                onOpcaoSelecionada = { nome ->
                    val tipoEscolhido = tiposAtendimentoLista.firstOrNull { it.nome == nome }
                    vm.tipoAtendimentoId.value = tipoEscolhido?.id?.toLong()
                },
                isError = vm.tipoAtendimentoId.value == null && vm.mensagem.value.isNotEmpty(),
                errorMessage = if (vm.tipoAtendimentoId.value == null && vm.mensagem.value.isNotEmpty()) "Campo obrigatório" else null
            )

            // Dropdown de Convênio
            CampoDropdownPadrao(
                label = "Convênio",
                opcoes = conveniosLista.map { it.nome },
                valorSelecionado = convenioAtualNome,
                onOpcaoSelecionada = { nome ->
                    val convenioEscolhido = conveniosLista.firstOrNull { it.nome == nome }
                    vm.convenioId.value = convenioEscolhido?.id?.toLong()
                }
            )

            // 1. Campo de Data
            CampoDataPadrao(
                value = vm.dataAtendimento.valor.value,
                onValueChange = { vm.dataAtendimento.definir(it) },
                label = "Data do Atendimento *",
                obrigatorio = true,
                isError = vm.dataAtendimento.temErro.value,
                errorMessage = if (vm.dataAtendimento.temErro.value) "Campo obrigatório" else null
            )

            // 2. Campo de Hora de Início
            CampoTextoPadrao(
                value = vm.horaInicio.valor.value,
                onValueChange = { vm.horaInicio.definir(it) },
                label = "Hora de Início *",
                placeholder = "HH:mm (ex.: 09:00)",
                obrigatorio = true,
                isError = vm.horaInicio.temErro.value,
                errorMessage = if (vm.horaInicio.temErro.value) "Campo obrigatório" else null
            )

            // 3. Campo de Hora de Fim
            CampoTextoPadrao(
                value = vm.horaFim.valor.value,
                onValueChange = { vm.horaFim.definir(it) },
                label = "Hora de Fim *",
                placeholder = "HH:mm (ex.: 09:50)",
                obrigatorio = true,
                isError = vm.horaFim.temErro.value,
                errorMessage = if (vm.horaFim.temErro.value) "Campo obrigatório" else null
            )

            // Dropdown de Salas de Atendimento
            CampoDropdownPadrao(
                label = "Sala de Atendimento",
                opcoes = salasLista.map { it.nome },
                valorSelecionado = salaAtualNome,
                onOpcaoSelecionada = { nome ->
                    val salaEscolhida = salasLista.firstOrNull { it.nome == nome }
                    vm.salaId.value = salaEscolhida?.id?.toLong()
                }
            )

            // Dropdown de Status da Agenda
            CampoDropdownPadrao(
                label = "Status da Agenda",
                opcoes = listaStatusAgenda,
                valorSelecionado = vm.status.valor.value,
                onOpcaoSelecionada = { vm.status.definir(it) }
            )

            // Observações
            CampoTextoPadrao(
                value = vm.observacoes.valor.value,
                onValueChange = { vm.observacoes.definir(it) },
                label = "Observações"
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else null,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}