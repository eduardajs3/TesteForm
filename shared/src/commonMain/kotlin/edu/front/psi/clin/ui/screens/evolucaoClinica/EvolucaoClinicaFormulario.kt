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
import edu.front.psi.clin.utils.MaskUtils
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira

@Composable
fun EvolucaoClinicaFormulario(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()
    val salvando = vm.salvando

    val prontuarioIdSelecionado = vm.prontuarioId
    val agendaIdSelecionado = vm.agendaId
    val psicologoIdSelecionado = vm.psicologoId

    val pacientes = vm.pacientes.collectAsState().value
    val prontuarios = vm.prontuarios.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value
    val agendas = vm.agendas.collectAsState().value
    val pacienteIdFiltro = vm.pacienteIdFiltro.collectAsState().value
    val prontuarioAtual = prontuarios.firstOrNull { it.id == prontuarioIdSelecionado }
    val agendasDoPaciente = prontuarioAtual?.let { prontuario ->
        agendas.filter { it.pacienteId == prontuario.pacienteId }
    }.orEmpty()
    val agendaAtual = agendas.firstOrNull { it.id == agendaIdSelecionado }
    val agendasParaOpcoes = (agendasDoPaciente + listOfNotNull(agendaAtual))
        .distinctBy { it.id }

    val prontuarioOpcoes = prontuarios.filter {
        pacienteIdFiltro == null || it.pacienteId == pacienteIdFiltro
    }.mapNotNull { prontuario ->
        prontuario.id?.let { id ->
            val paciente = pacientes.firstOrNull { it.id == prontuario.pacienteId }?.nome
                ?: "Paciente #${prontuario.pacienteId}"
            id to "$paciente - ${prontuario.queixaPrincipal} (#$id)"
        }
    }

    val psicologoAssociadoId = agendaAtual?.psicologoId ?: psicologoIdSelecionado
    val psicologoAssociado = psicologos.firstOrNull { it.id == psicologoAssociadoId }
    val psicologoOpcoes = listOfNotNull(
        psicologoAssociadoId?.let { id ->
            id to "${psicologoAssociado?.nome ?: "Psicólogo #$id"} (#$id)"
        }
    )

    val agendaOpcoes = agendasParaOpcoes.mapNotNull { agenda ->
        agenda.id?.let { id ->
            val paciente = pacientes.firstOrNull { it.id == agenda.pacienteId }?.nome
                ?: "Paciente #${agenda.pacienteId}"
            id to "$paciente - ${formatarDataHoraIsoParaBrasileira(agenda.dataHoraInicio)} (#$id)"
        }
    } + listOfNotNull(
        agendaIdSelecionado
            ?.takeIf { id -> agendasParaOpcoes.none { it.id == id } }
            ?.let { it to "Agenda #$it" }
    )

    val statusOpcoes = listOf("Ativo", "Inativo")

    val prontuarioExibicao = prontuarioOpcoes.firstOrNull {
        it.first == prontuarioIdSelecionado
    }?.second.orEmpty()

    val psicologoExibicao = psicologoOpcoes.firstOrNull {
        it.first == psicologoAssociadoId
    }?.second.orEmpty()

    val agendaExibicao = agendaOpcoes.firstOrNull {
        it.first == agendaIdSelecionado
    }?.second.orEmpty()

    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) {
        "Ativo"
    } else {
        "Inativo"
    }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Evolução Clínica" else "Cadastro de Evolução Clínica",
            subtitulo = if (isEdicao) {
                "Altere os dados do registro de evolução selecionado."
            } else {
                "Preencha os dados da evolução clínica."
            },
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(EvolucaoClinicaRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Prontuário *",
                valorSelecionado = prontuarioExibicao,
                opcoes = prontuarioOpcoes.map { it.second },
                onOpcaoSelecionada = { selecao ->
                    val prontuarioId = prontuarioOpcoes.firstOrNull {
                        it.second == selecao
                    }?.first
                    prontuarios.firstOrNull { it.id == prontuarioId }?.let(vm::selecionarProntuario)
                },
                isError = vm.erroProntuario,
                errorMessage = if (vm.erroProntuario) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Agenda *",
                valorSelecionado = agendaExibicao,
                opcoes = agendaOpcoes.map { it.second },
                onOpcaoSelecionada = { selecao ->
                    val agendaId = agendaOpcoes.firstOrNull {
                        it.second == selecao
                    }?.first
                    agendas.firstOrNull { it.id == agendaId }?.let(vm::selecionarAgenda)
                },
                isError = vm.erroAgenda,
                errorMessage = if (vm.erroAgenda) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Psicólogo da agenda *",
                valorSelecionado = psicologoExibicao,
                opcoes = psicologoOpcoes.map { it.second },
                onOpcaoSelecionada = { selecao ->
                    val idSelecionado = psicologoOpcoes.firstOrNull {
                        it.second == selecao
                    }?.first
                    vm.selecionarPsicologo(idSelecionado)
                },
                isError = vm.erroPsicologo,
                errorMessage = if (vm.erroPsicologo) "Campo obrigatório" else null
            )

            CampoDataPadrao(
                value = vm.dataRegistro.valor.value,
                onValueChange = { vm.dataRegistro.definir(it) },
                label = "Data do registro",
                obrigatorio = true,
                isError = vm.dataRegistro.temErro.value,
                errorMessage = if (vm.dataRegistro.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaRegistro.valor.value,
                onValueChange = { vm.horaRegistro.definir(MaskUtils.aplicarMascaraHora(it)) },
                label = "Horário do registro",
                placeholder = "HH:mm",
                isError = vm.horaRegistro.temErro.value,
                errorMessage = if (vm.horaRegistro.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.descricao.valor.value,
                onValueChange = { vm.descricao.definir(it) },
                label = "Descrição da evolução clínica",
                obrigatorio = true,
                isError = vm.descricao.temErro.value,
                errorMessage = if (vm.descricao.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.ativo.definir((selecao == "Ativo").toString())
                },
                isError = vm.ativo.temErro.value,
                errorMessage = if (vm.ativo.temErro.value) "Campo obrigatório" else null
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