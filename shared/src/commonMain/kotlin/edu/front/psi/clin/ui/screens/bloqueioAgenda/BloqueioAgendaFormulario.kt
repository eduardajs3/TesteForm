package edu.front.psi.clin.ui.screens.bloqueioAgenda

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.BloqueioAgendaViewModel

@Composable
fun BloqueioAgendaFormulario(
    innerPadding: PaddingValues,
    vm: BloqueioAgendaViewModel
) {
    LaunchedEffect(vm) {
        vm.carregarDadosFormulario()
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        vm.carregarDadosFormulario()
    }

    val isEdicao = vm.id.value.isNotEmpty()
    val salvando = vm.salvando
    val isPsicologoLogado = PermissoesAcesso.ehPsicologo(SessionManager.role)
    val psicologos = vm.psicologos.collectAsState().value
    val psicologoSelecionado = psicologos.firstOrNull {
        it.id == vm.psicologoId
    }
    val psicologoOpcoes = if (isPsicologoLogado && !isEdicao && psicologoSelecionado != null) {
        listOf(psicologoSelecionado.nome)
    } else {
        psicologos.filter {
            it.ativo || (isEdicao && it.id == vm.psicologoId)
        }.map { it.nome }
    }

    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Bloqueio" else "Cadastro de Bloqueio de Agenda",
            subtitulo = if (isEdicao) "Altere os dados do bloqueio ID: ${vm.id.value}" else "Preencha os dados para criar um novo bloqueio.",
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(BloqueioAgendaRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Nome do psicólogo *",
                valorSelecionado = psicologoSelecionado?.nome.orEmpty(),
                opcoes = psicologoOpcoes,
                onOpcaoSelecionada = { nome ->
                    if (!isPsicologoLogado || isEdicao) {
                        val psicologo = psicologos.firstOrNull { it.nome == nome }
                        vm.selecionarPsicologo(psicologo?.id)
                    }
                },
                isError = vm.erroPsicologo,
                errorMessage = if (vm.erroPsicologo) "Campo obrigatório" else null
            )

            CampoDataPadrao(
                value = vm.dataInicio.valor.value,
                onValueChange = { vm.dataInicio.definir(it) },
                label = "Data de início",
                obrigatorio = true,
                isError = vm.dataInicio.temErro.value,
                errorMessage = if (vm.dataInicio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaInicio.valor.value,
                onValueChange = { vm.horaInicio.definir(it) },
                label = "Hora de início",
                placeholder = "HH:mm",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.horaInicio.temErro.value,
                errorMessage = if (vm.horaInicio.temErro.value) "Campo obrigatório" else null
            )

            CampoDataPadrao(
                value = vm.dataFim.valor.value,
                onValueChange = { vm.dataFim.definir(it) },
                label = "Data de fim",
                obrigatorio = true,
                isError = vm.dataFim.temErro.value,
                errorMessage = if (vm.dataFim.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaFim.valor.value,
                onValueChange = { vm.horaFim.definir(it) },
                label = "Hora de fim",
                placeholder = "HH:mm",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.horaFim.temErro.value,
                errorMessage = if (vm.horaFim.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.motivo.valor.value,
                onValueChange = { vm.motivo.definir(it) },
                label = "Motivo do bloqueio",
                obrigatorio = true,
                isError = vm.motivo.temErro.value,
                errorMessage = if (vm.motivo.temErro.value) vm.motivo.mensagemErro.value else null
            )

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.ativo.definir(valorBoolean)
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