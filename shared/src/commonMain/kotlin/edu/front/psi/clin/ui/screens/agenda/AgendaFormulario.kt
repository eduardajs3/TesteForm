package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    val listaPacientes = listOf(
        "Maria Clara da Silva",
        "João Pedro Santos",
        "Ana Beatriz Lima",
        "Lucas Oliveira"
    )

    val listaTiposAtendimento = listOf("Consulta Individual", "Terapia de Casal", "Avaliação Psicológica", "Anamnese")
    val listaConvenios = listOf("Particular", "Unimed", "Bradesco Saúde", "Amil", "SulAmérica")
    val listaStatusAgenda = listOf("Agendado", "Confirmado", "Em Atendimento", "Realizado", "Cancelado")

    var dataSelecionada by remember(vm.dataHoraInicio.valor.value) {
        val data = if (vm.dataHoraInicio.valor.value.contains(" ")) {
            vm.dataHoraInicio.valor.value.split(" ")[0]
        } else if (vm.dataHoraInicio.valor.value.isNotEmpty()) {
            vm.dataHoraInicio.valor.value
        } else {
            "16/03/2026"
        }
        mutableStateOf(data)
    }

    val horariosDisponiveisNoDia = remember(dataSelecionada) {
        if (dataSelecionada.isNotEmpty()) {
            listOf("08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00")
        } else {
            emptyList()
        }
    }

    val salasDisponiveisNoDia = remember(dataSelecionada) {
        if (dataSelecionada.isNotEmpty()) {
            listOf("Sala 01 - Bloco A", "Sala 02 - Bloco A", "Sala 03 - Bloco B")
        } else {
            emptyList()
        }
    }

    val psicologosDisponiveisNoDia = remember(dataSelecionada) {
        if (dataSelecionada.isNotEmpty()) {
            listOf("Dra. Amanda Silva", "Dr. Roberto Alves", "Dra. Juliana Costa")
        } else {
            emptyList()
        }
    }

    var horarioSelecionado by remember(vm.dataHoraInicio.valor.value) {
        val horaInicio = if (vm.dataHoraInicio.valor.value.contains(" ")) {
            vm.dataHoraInicio.valor.value.split(" ")[1]
        } else ""
        val encontrado = listOf(
            "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00",
            "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00"
        ).firstOrNull { it.startsWith(horaInicio) } ?: ""
        mutableStateOf(encontrado)
    }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Agendamento" else "Cadastro de Agenda",
            subtitulo = if (isEdicao) "Altere os dados do agendamento selecionado." else "Escolha a data para carregar os horários e recursos disponíveis.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoDropdownPadrao(
                label = "Paciente *",
                opcoes = listaPacientes,
                valorSelecionado = vm.paciente.valor.value,
                onOpcaoSelecionada = { vm.paciente.definir(it) },
                isError = vm.paciente.temErro.value,
                errorMessage = if (vm.paciente.temErro.value) vm.paciente.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = "Psicólogo *",
                opcoes = psicologosDisponiveisNoDia,
                valorSelecionado = vm.psicologo.valor.value,
                onOpcaoSelecionada = { vm.psicologo.definir(it) },
                isError = vm.psicologo.temErro.value,
                errorMessage = if (vm.psicologo.temErro.value) vm.psicologo.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = "Tipo de Atendimento *",
                opcoes = listaTiposAtendimento,
                valorSelecionado = vm.tipoAtendimento.valor.value,
                onOpcaoSelecionada = { vm.tipoAtendimento.definir(it) },
                isError = vm.tipoAtendimento.temErro.value,
                errorMessage = if (vm.tipoAtendimento.temErro.value) vm.tipoAtendimento.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = "Convênio",
                opcoes = listaConvenios,
                valorSelecionado = vm.convenio.valor.value,
                onOpcaoSelecionada = { vm.convenio.definir(it) }
            )

            CampoDataPadrao(
                value = dataSelecionada,
                onValueChange = { novaData ->
                    dataSelecionada = novaData
                    horarioSelecionado = "" 
                    vm.dataHoraInicio.definir("")
                    vm.dataHoraFim.definir("")
                },
                label = "Data do Atendimento",
                obrigatorio = true,
                isError = vm.dataHoraInicio.temErro.value,
                errorMessage = if (vm.dataHoraInicio.temErro.value) vm.dataHoraInicio.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = if (dataSelecionada.isEmpty()) "Selecione primeiro uma data *" else "Horário Disponível para $dataSelecionada *",
                opcoes = horariosDisponiveisNoDia,
                valorSelecionado = horarioSelecionado,
                onOpcaoSelecionada = { intervalo ->
                    horarioSelecionado = intervalo
                    val partes = intervalo.split(" - ")
                    if (partes.size == 2) {
                        vm.dataHoraInicio.definir("$dataSelecionada ${partes[0]}")
                        vm.dataHoraFim.definir("$dataSelecionada ${partes[1]}")
                    }
                },
                isError = vm.dataHoraInicio.temErro.value,
                errorMessage = if (vm.dataHoraInicio.temErro.value) vm.dataHoraInicio.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = "Sala de Atendimento Livre *",
                opcoes = salasDisponiveisNoDia,
                valorSelecionado = vm.sala.valor.value,
                onOpcaoSelecionada = { vm.sala.definir(it) },
                isError = vm.sala.temErro.value,
                errorMessage = if (vm.sala.temErro.value) vm.sala.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = "Status da Agenda",
                opcoes = listaStatusAgenda,
                valorSelecionado = vm.status.valor.value,
                onOpcaoSelecionada = { vm.status.definir(it) }
            )

            CampoTextoPadrao(
                value = vm.observacoes.valor.value,
                onValueChange = { vm.observacoes.definir(it) },
                label = "Observações"
            )

            BotoesAcaoFormulario(
                onLimpar = {
                    vm.limparCampos()
                    dataSelecionada = ""
                    horarioSelecionado = ""
                },
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}
