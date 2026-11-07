package edu.front.psi.clin.ui.screens.evolucaoClinica

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel

@Composable
fun EvolucaoClinicaFormulario(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

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
                onLimpar = if (!isEdicao) vm::limparCampos else null,
                onSalvar = vm::salvar,
                textoBotaoSalvar =
                    if (isEdicao)
                        "Salvar Alterações"
                    else
                        "Gravar"
            )
        }
    }
}