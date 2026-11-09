package edu.front.psi.clin.ui.screens.prontuario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.ProntuarioRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel

@Composable
fun ProntuarioFormulario(
    innerPadding: PaddingValues,
    vm: ProntuarioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val pacienteOpcoes = listOf(
        "Ana Clara Souza",
        "Carlos Eduardo Lima",
        "Mariana Ribeiro",
        "João Pedro Santos"
    )

    val statusOpcoes = listOf(
        "Ativo",
        "Arquivado",
        "Encerrado"
    )

    val pacienteAtual = vm.paciente.value
    val pacienteExibicao = if (pacienteOpcoes.contains(pacienteAtual)) pacienteAtual else (pacienteOpcoes.firstOrNull() ?: "")

    val statusAtual = vm.status.value
    val statusExibicao = when {
        statusOpcoes.contains(statusAtual) -> statusAtual
        statusAtual.lowercase() == "true" -> "Ativo"
        statusAtual.lowercase() == "false" -> "Encerrado"
        else -> "Ativo" 
    }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Prontuário" else "Cadastro de Prontuário",
            subtitulo = if (isEdicao) "Altere os dados do prontuário selecionado." else "Preencha os dados do prontuário.",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(ProntuarioRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Paciente",
                valorSelecionado = pacienteExibicao,
                opcoes = pacienteOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.paciente.value = selecao
                }
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

            CampoDropdownPadrao(
                label = "Status do prontuário",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.status.value = selecao
                }
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao) vm::limparCampos else { {} },
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}