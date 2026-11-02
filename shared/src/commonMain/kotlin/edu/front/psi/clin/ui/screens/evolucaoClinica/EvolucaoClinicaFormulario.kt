package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.components.BotoesAcaoFormulario
import edu.front.psi.clin.ui.components.CampoTextoPadrao
import edu.front.psi.clin.ui.components.CampoDropdownPadrao
import edu.front.psi.clin.ui.components.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel

@Composable
fun EvolucaoClinicaFormulario(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    // Declaração limpa das opções e conversão de status no topo
    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.value == "true" || vm.ativo.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Evolução Clínica" else "Cadastro de Evolução Clínica",
            subtitulo = if (isEdicao) "Altere os dados do registro de evolução selecionado." else "Preencha os dados da evolução clínica.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.prontuario.value,
                onValueChange = { vm.prontuario.value = it },
                label = "Prontuário"
            )

            CampoTextoPadrao(
                value = vm.psicologo.value,
                onValueChange = { vm.psicologo.value = it },
                label = "Psicólogo"
            )

            CampoTextoPadrao(
                value = vm.agenda.value,
                onValueChange = { vm.agenda.value = it },
                label = "Agenda"
            )

            CampoTextoPadrao(
                value = vm.dataRegistro.value,
                onValueChange = { vm.dataRegistro.value = it },
                label = "Data e hora do registro"
            )

            CampoTextoPadrao(
                value = vm.descricao.value,
                onValueChange = { vm.descricao.value = it },
                label = "Descrição da evolução clínica"
            )

            // Dropdown padronizado para o Status
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
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}