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
                value = vm.psicologo.valor.value,
                onValueChange = { vm.psicologo.definir(it) },
                label = "Nome do psicólogo",
                isError = vm.psicologo.temErro.value,
                errorMessage = if (vm.psicologo.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.dataHoraInicio.valor.value,
                onValueChange = { vm.dataHoraInicio.definir(it) },
                label = "Data e hora de início",
                isError = vm.dataHoraInicio.temErro.value,
                errorMessage = if (vm.dataHoraInicio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.dataHoraFim.valor.value,
                onValueChange = { vm.dataHoraFim.definir(it) },
                label = "Data e hora de fim",
                isError = vm.dataHoraFim.temErro.value,
                errorMessage = if (vm.dataHoraFim.temErro.value) "Campo obrigatório" else null,
            
            )

            CampoTextoPadrao(
                value = vm.motivo.valor.value,
                onValueChange = { vm.motivo.definir(it) },
                label = "Motivo do bloqueio",
                isError = vm.motivo.temErro.value,
                errorMessage = if (vm.motivo.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.ativo.valor.value,
                onValueChange = { vm.ativo.definir(it) },
                label = "Status",
                placeholder = "Ativo? Digite true ou false",
                isError = vm.ativo.temErro.value,
                errorMessage = if (vm.ativo.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}