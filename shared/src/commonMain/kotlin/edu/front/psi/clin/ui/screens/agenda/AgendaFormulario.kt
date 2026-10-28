package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

@Composable
fun AgendaFormulario(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Agendamento" else "Cadastro de Agenda",
            subtitulo = if (isEdicao) "Altere os dados do agendamento selecionado." else "Preencha os dados do agendamento.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.paciente.value,
                onValueChange = { vm.paciente.value = it },
                label = "Paciente"
            )

            CampoTextoPadrao(
                value = vm.psicologo.value,
                onValueChange = { vm.psicologo.value = it },
                label = "Psicólogo"
            )

            CampoTextoPadrao(
                value = vm.sala.value,
                onValueChange = { vm.sala.value = it },
                label = "Sala de atendimento"
            )

            CampoTextoPadrao(
                value = vm.tipoAtendimento.value,
                onValueChange = { vm.tipoAtendimento.value = it },
                label = "Tipo de atendimento"
            )

            CampoTextoPadrao(
                value = vm.convenio.value,
                onValueChange = { vm.convenio.value = it },
                label = "Convênio"
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
                value = vm.status.value,
                onValueChange = { vm.status.value = it },
                label = "Status da agenda"
            )

            CampoTextoPadrao(
                value = vm.observacoes.value,
                onValueChange = { vm.observacoes.value = it },
                label = "Observações"
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