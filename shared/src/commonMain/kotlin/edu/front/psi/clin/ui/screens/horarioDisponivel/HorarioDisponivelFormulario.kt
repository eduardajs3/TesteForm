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
                value = vm.psicologo.valor.value,
                onValueChange = { vm.psicologo.definir(it) },
                label = "Nome do psicólogo",
                obrigatorio = true,
                isError = vm.psicologo.temErro.value,
                errorMessage = if (vm.psicologo.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.diaSemana.valor.value,
                onValueChange = { vm.diaSemana.definir(it) },
                label = "Dia da semana",
                obrigatorio = true,
                isError = vm.diaSemana.temErro.value,
                errorMessage = if (vm.diaSemana.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaInicio.valor.value,
                onValueChange = { vm.horaInicio.definir(it) },
                label = "Hora de início (ex.: 08:00)",
                obrigatorio = true,
                isError = vm.horaInicio.temErro.value,
                errorMessage = if (vm.horaInicio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaFim.valor.value,
                onValueChange = { vm.horaFim.definir(it) },
                label = "Hora de fim (ex.: 09:00)",
                obrigatorio = true,
                isError = vm.horaFim.temErro.value,
                errorMessage = if (vm.horaFim.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.descricao.valor.value,
                onValueChange = { vm.descricao.definir(it) },
                label = "Descrição",
                obrigatorio = true,
                isError = vm.descricao.temErro.value,
                errorMessage = if (vm.descricao.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.ativo.valor.value,
                onValueChange = { vm.ativo.definir(it) },
                label = "Status",
                placeholder = "Ativo? Digite true ou false",
                obrigatorio = true,
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