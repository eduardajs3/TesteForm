package edu.front.psi.clin.ui.screens.bloqueioAgenda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
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
    val isEdicao = vm.id.value.isNotEmpty()
    val psicologos = vm.psicologos.collectAsState().value
    val psicologoOpcoes = psicologos.map { it.nome }
    val psicologoSelecionado = psicologos.firstOrNull {
        it.id?.toString() == vm.psicologo.valor.value
    }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Bloqueio" else "Cadastro de Bloqueio de Agenda",
            subtitulo = if (isEdicao) "Altere os dados do bloqueio ID: ${vm.id.value}" else "Preencha os dados para criar um novo bloqueio.",
            mensagemFeedback = vm.mensagem.value,
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
                    val psicologo = psicologos.firstOrNull { it.nome == nome }
                    vm.psicologo.definir(psicologo?.id?.toString().orEmpty())
                },
                isError = vm.psicologo.temErro.value,
                errorMessage = if (vm.psicologo.temErro.value) "Campo obrigatório" else null
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

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else { {} },
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}