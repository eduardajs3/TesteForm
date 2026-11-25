package edu.front.psi.clin.ui.screens.prontuario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val salvando = vm.salvando
    val pacientes = vm.pacientes.collectAsState().value
    val pacienteIdFiltro = vm.pacienteIdFiltro.collectAsState().value
    val pacientesFiltrados = pacientes.filter {
        pacienteIdFiltro == null || it.id == pacienteIdFiltro
    }
    val pacienteOpcoes = pacientesFiltrados.map { it.nome }.distinct()

    val statusProntuarioOpcoes = listOf(
        "Ativo",
        "Arquivado",
        "Encerrado"
    )

    val statusRegistroOpcoes = listOf("Ativo", "Inativo")

    val pacienteAtual = vm.paciente.value
    val pacienteExibicao = pacienteAtual

    val statusAtual = vm.status.value
    val statusExibicao = when {
        statusProntuarioOpcoes.contains(statusAtual) -> statusAtual
        statusAtual.equals("ATIVO", ignoreCase = true) || statusAtual.lowercase() == "true" -> "Ativo"
        statusAtual.equals("ARQUIVADO", ignoreCase = true) -> "Arquivado"
        statusAtual.equals("ENCERRADO", ignoreCase = true) || statusAtual.lowercase() == "false" -> "Encerrado"
        else -> "Ativo"
    }

    val ativoExibicao = if (vm.ativo.value == "true" || vm.ativo.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Prontuário" else "Cadastro de Prontuário",
            subtitulo = if (isEdicao) "Altere os dados do prontuário selecionado." else "Preencha os dados do prontuário.",
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
                opcoes = statusProntuarioOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.status.value = selecao
                }
            )

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = ativoExibicao,
                opcoes = statusRegistroOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.ativo.value = valorBoolean
                }
            )

            BotoesAcaoFormulario(
                onLimpar = if (!isEdicao && !salvando) vm::limparCampos else null,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (salvando) {
                    "Salvando..."
                } else {
                    if (isEdicao) "Salvar Alterações" else "Gravar"
                }
            )
        }
    }
}