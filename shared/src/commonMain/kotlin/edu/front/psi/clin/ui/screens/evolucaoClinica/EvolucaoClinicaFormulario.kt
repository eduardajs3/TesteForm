package edu.front.psi.clin.ui.screens.evolucaoClinica

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel

@Composable
fun EvolucaoClinicaFormulario(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val pacientes = vm.pacientes.collectAsState().value
    val prontuarios = vm.prontuarios.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value
    val agendas = vm.agendas.collectAsState().value
    val prontuarioAtual = prontuarios.firstOrNull { it.id == vm.prontuarioId.value }
    val agendasDoPaciente = prontuarioAtual?.let { prontuario ->
        agendas.filter { it.pacienteId == prontuario.pacienteId }
    }.orEmpty()
    val agendaAtual = agendasDoPaciente.firstOrNull { it.id == vm.agendaId.value }
    val prontuarioOpcoes = prontuarios.mapNotNull { prontuario ->
        prontuario.id?.let { id ->
            val paciente = pacientes.firstOrNull { it.id == prontuario.pacienteId }?.nome
                ?: "Paciente #${prontuario.pacienteId}"
            id to "$paciente - ${prontuario.queixaPrincipal} (#$id)"
        }
    }
    val psicologoOpcoes = psicologos.filter {
        agendaAtual != null && it.id == agendaAtual.psicologoId
    }.mapNotNull { psicologo ->
        psicologo.id?.let { it to "${psicologo.nome} (#$it)" }
    }
    val agendaOpcoes = agendasDoPaciente.mapNotNull { agenda ->
        agenda.id?.let { id ->
            val paciente = pacientes.firstOrNull { it.id == agenda.pacienteId }?.nome
                ?: agenda.paciente
                ?: "Paciente"
            id to "$paciente - ${agenda.dataHoraInicio} (#$id)"
        }
    }

    val statusOpcoes = listOf("Ativo", "Inativo")

    val prontuarioExibicao = prontuarioOpcoes.firstOrNull {
        it.first == vm.prontuarioId.value
    }?.second.orEmpty()
    val psicologoExibicao = psicologoOpcoes.firstOrNull {
        it.first == vm.psicologoId.value
    }?.second.orEmpty()
    val agendaExibicao = agendaOpcoes.firstOrNull {
        it.first == vm.agendaId.value
    }?.second.orEmpty()

    val statusExibicao = if (vm.ativo.value == "true" || vm.ativo.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Evolução Clínica" else "Cadastro de Evolução Clínica",
            subtitulo = if (isEdicao) "Altere os dados do registro de evolução selecionado." else "Preencha os dados da evolução clínica.",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(EvolucaoClinicaRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Prontuário",
                valorSelecionado = prontuarioExibicao,
                opcoes = prontuarioOpcoes.map { it.second },
                onOpcaoSelecionada = { selecao ->
                    val prontuarioId = prontuarioOpcoes.firstOrNull {
                        it.second == selecao
                    }?.first
                    prontuarios.firstOrNull { it.id == prontuarioId }?.let(vm::selecionarProntuario)
                }
            )

            CampoDropdownPadrao(
                label = "Agenda",
                valorSelecionado = agendaExibicao,
                opcoes = agendaOpcoes.map { it.second },
                onOpcaoSelecionada = { selecao ->
                    val agendaId = agendaOpcoes.firstOrNull {
                        it.second == selecao
                    }?.first
                    agendasDoPaciente.firstOrNull { it.id == agendaId }?.let(vm::selecionarAgenda)
                }
            )

            CampoDropdownPadrao(
                label = "Psicólogo da agenda",
                valorSelecionado = psicologoExibicao,
                opcoes = psicologoOpcoes.map { it.second },
                onOpcaoSelecionada = { selecao ->
                    vm.psicologoId.value = psicologoOpcoes.firstOrNull {
                        it.second == selecao
                    }?.first
                }
            )

            CampoDataPadrao(
                value = vm.dataRegistro.value,
                onValueChange = { vm.dataRegistro.value = it },
                label = "Data e hora do registro"
            )

            CampoTextoPadrao(
                value = vm.descricao.value,
                onValueChange = { vm.descricao.value = it },
                label = "Descrição da evolução clínica"
            )

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.ativo.value = valorBoolean
                }
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else { {} },
                onSalvar = vm::salvar,
                textoBotaoSalvar =
                    if (isEdicao)
                        "Salvar Alterações"
                    else
                        "Gravar"
            )
        }
    }
}