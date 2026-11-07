package edu.front.psi.clin.ui.screens.prontuario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel

@Composable
fun ProntuarioFormulario(
    innerPadding: PaddingValues,
    vm: ProntuarioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.value == "true" || vm.ativo.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Prontuário" else "Cadastro de Prontuário",
            subtitulo = if (isEdicao) "Altere os dados do prontuário selecionado." else "Preencha os dados do prontuário.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.paciente.value,
                onValueChange = { vm.paciente.value = it },
                label = "Paciente"
            )

            CampoDataPadrao(
                value = vm.dataAbertura.value,
                onValueChange = { vm.dataAbertura.value = it },
                label = "Data de abertura"
            )

            CampoTextoPadrao(
                value = vm.queixaPrincipal.value,
                onValueChange = { vm.queixaPrincipal.value = it },
                label = "Queixa principal"
            )

            CampoTextoPadrao(
                value = vm.status.value,
                onValueChange = { vm.status.value = it },
                label = "Status do prontuário"
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