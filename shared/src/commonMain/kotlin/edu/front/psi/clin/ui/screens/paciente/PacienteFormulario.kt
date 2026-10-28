package edu.front.psi.clin.ui.screens.paciente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoDataPadrao
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel

@Composable
fun PacienteFormulario(
    innerPadding: PaddingValues,
    vm: PacienteViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar informações de Paciente" else "Cadastro de Paciente",
            subtitulo = if (isEdicao) "Altere os dados do paciente selecionado." else "Preencha os dados do paciente",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.value,
                onValueChange = { vm.nome.value = it },
                label = "Nome completo"
            )

            CampoTextoPadrao(
                value = vm.cpf.value,
                onValueChange = { vm.cpf.value = it },
                label = "CPF",
            )

            CampoDataPadrao(
                value = vm.dataNascimento.value,
                onValueChange = { vm.dataNascimento.value = it },
                label = "Data de nascimento"
            )

            CampoTextoPadrao(
                value = vm.email.value,
                onValueChange = { vm.email.value = it },
                label = "E-mail"
            )

            CampoTextoPadrao(
                value = vm.telefone.value,
                onValueChange = { vm.telefone.value = it },
                label = "Telefone"
            )

            CampoTextoPadrao(
                value = vm.endereco.value,
                onValueChange = { vm.endereco.value = it },
                label = "Endereço"
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