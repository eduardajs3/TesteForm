package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao // <-- Importado do seu componente global
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

@Composable
fun AgendaFormulario(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    // 1. Lista global de pacientes cadastrados na clínica
    val listaPacientes = listOf(
        "Maria Clara da Silva",
        "João Pedro Santos",
        "Ana Beatriz Lima",
        "Lucas Oliveira"
    )

    // 2. Opções genéricas
    val listaTiposAtendimento = listOf("Consulta Individual", "Terapia de Casal", "Avaliação Psicológica", "Anamnese")
    val listaConvenios = listOf("Particular", "Unimed", "Bradesco Saúde", "Amil", "SulAmérica")
    val listaStatusAgenda = listOf("Agendado", "Confirmado", "Em Atendimento", "Realizado", "Cancelado")

    // Estado da data escolhida
    var dataSelecionada by remember { mutableStateOf("26/03/2026") }

    // 3. Horários, Salas e Psicólogos filtrados dinamicamente com base no dia escolhido
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

    var horarioSelecionado by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Agendamento" else "Cadastro de Agenda",
            subtitulo = if (isEdicao) "Altere os dados do agendamento selecionado." else "Escolha a data para carregar os horários e recursos disponíveis.",
            mensagemFeedback = vm.mensagem.value
        ) {
            // Passo 1: Selecionar o Paciente
            CampoDropdownPadrao(
                label = "Paciente",
                opcoes = listaPacientes,
                valorSelecionado = vm.paciente.value,
                onOpcaoSelecionada = { vm.paciente.value = it }
            )

            // Passo 2: Selecionar o Dia do Atendimento
            CampoTextoPadrao(
                value = dataSelecionada,
                onValueChange = { novaData ->
                    dataSelecionada = novaData
                    horarioSelecionado = "" 
                    vm.dataHoraInicio.value = ""
                    vm.dataHoraFim.value = ""
                },
                label = "Data do Atendimento",
                placeholder = "Ex: 26/03/2026"
            )

            // Passo 3: Seleção do Horário Disponível
            CampoDropdownPadrao(
                label = if (dataSelecionada.isEmpty()) "Selecione primeiro uma data" else "Horário Disponível para $dataSelecionada",
                opcoes = horariosDisponiveisNoDia,
                valorSelecionado = horarioSelecionado,
                onOpcaoSelecionada = { intervalo ->
                    horarioSelecionado = intervalo
                    val partes = intervalo.split(" - ")
                    if (partes.size == 2) {
                        vm.dataHoraInicio.value = "$dataSelecionada ${partes[0]}"
                        vm.dataHoraFim.value = "$dataSelecionada ${partes[1]}"
                    }
                }
            )

            // Passo 4: Seleção do Psicólogo
            CampoDropdownPadrao(
                label = "Psicólogo Disponível",
                opcoes = psicologosDisponiveisNoDia,
                valorSelecionado = vm.psicologo.value,
                onOpcaoSelecionada = { vm.psicologo.value = it }
            )

            // Passo 5: Seleção da Sala
            CampoDropdownPadrao(
                label = "Sala de Atendimento Livre",
                opcoes = salasDisponiveisNoDia,
                valorSelecionado = vm.sala.value,
                onOpcaoSelecionada = { vm.sala.value = it }
            )

            // Passo 6: Tipo de Atendimento e Convênio
            CampoDropdownPadrao(
                label = "Tipo de Atendimento",
                opcoes = listaTiposAtendimento,
                valorSelecionado = vm.tipoAtendimento.value,
                onOpcaoSelecionada = { vm.tipoAtendimento.value = it }
            )

            CampoDropdownPadrao(
                label = "Convênio",
                opcoes = listaConvenios,
                valorSelecionado = vm.convenio.value,
                onOpcaoSelecionada = { vm.convenio.value = it }
            )

            // Passo 7: Status do Agendamento
            CampoDropdownPadrao(
                label = "Status da Agenda",
                opcoes = listaStatusAgenda,
                valorSelecionado = vm.status.value,
                onOpcaoSelecionada = { vm.status.value = it }
            )

            CampoTextoPadrao(
                value = vm.observacoes.value,
                onValueChange = { vm.observacoes.value = it },
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