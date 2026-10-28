package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.BloqueioAgendaViewModel

@Composable
fun BloqueioAgendaFormulario(
    innerPadding: PaddingValues,
    vm: BloqueioAgendaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Bloqueio" else "Cadastro de Bloqueio de Agenda",
            subtitulo = if (isEdicao) "Altere os dados do bloqueio ID: ${vm.id.value}" else "Preencha os dados para criar um novo bloqueio.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.psicologo.value,
                onValueChange = { vm.psicologo.value = it },
                label = "Nome do psicólogo"
            )

            CampoTextoPadrao(
                value = vm.dataHoraInicio.value,
                onValueChange = { vm.dataHoraInicio.value = it },
                label = "Data e hora de início"
            )

            CampoTextoPadrao(
                value = vm.dataHoraFim.value,
                onValueChange = { vm.dataHoraFim.value = it },
                label = "Data e hora de fim"
            )

            CampoTextoPadrao(
                value = vm.motivo.value,
                onValueChange = { vm.motivo.value = it },
                label = "Motivo do bloqueio"
            )

            CampoTextoPadrao(
                value = vm.ativo.value,
                onValueChange = { vm.ativo.value = it },
                label = "Status",
                placeholder = "Ativo? Digite true ou false"
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}