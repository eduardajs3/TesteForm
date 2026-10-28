package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

@Composable
fun HorarioDisponivelFormulario(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Horário Disponível" else "Cadastro de Horário Disponível",
            subtitulo = if (isEdicao) "Altere os dados do horário selecionado." else "Preencha os dados do horário disponível.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.psicologo.value,
                onValueChange = { vm.psicologo.value = it },
                label = "Nome do psicólogo"
            )

            CampoTextoPadrao(
                value = vm.diaSemana.value,
                onValueChange = { vm.diaSemana.value = it },
                label = "Dia da semana"
            )

            CampoTextoPadrao(
                value = vm.horaInicio.value,
                onValueChange = { vm.horaInicio.value = it },
                label = "Hora de início (ex.: 08:00)"
            )

            CampoTextoPadrao(
                value = vm.horaFim.value,
                onValueChange = { vm.horaFim.value = it },
                label = "Hora de fim (ex.: 09:00)"
            )

            CampoTextoPadrao(
                value = vm.descricao.value,
                onValueChange = { vm.descricao.value = it },
                label = "Descrição"
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