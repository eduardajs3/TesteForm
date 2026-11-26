package edu.front.psi.clin.ui.screens.prontuario

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.model.enums.ProntuarioStatus
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
    val nomePaciente = pacientes.firstOrNull { it.id == vm.pacienteId }?.nome.orEmpty()

    val statusRegistroOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = ProntuarioStatus.deValorApi(vm.status.valor.value).rotulo
    val ativoExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Prontuário" else "Cadastro de Prontuário",
            subtitulo = if (nomePaciente.isNotEmpty()) "Paciente: $nomePaciente" else "Preencha os dados do prontuário.",
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(ProntuarioRotas.Listagem)
            }
        ) {
            CampoDataPadrao(
                value = vm.dataAbertura.valor.value,
                onValueChange = { vm.dataAbertura.definir(it) },
                label = "Data de abertura",
                obrigatorio = true,
                isError = vm.dataAbertura.temErro.value,
                errorMessage = if (vm.dataAbertura.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.queixaPrincipal.valor.value,
                onValueChange = { vm.queixaPrincipal.definir(it) },
                label = "Queixa principal",
                obrigatorio = true,
                isError = vm.queixaPrincipal.temErro.value,
                errorMessage = if (vm.queixaPrincipal.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Status do prontuário",
                valorSelecionado = statusExibicao,
                opcoes = ProntuarioStatus.entries.map { it.rotulo },
                onOpcaoSelecionada = { selecao ->
                    val selecionado = ProntuarioStatus.entries.firstOrNull { it.rotulo == selecao }
                    vm.status.definir(selecionado?.name ?: ProntuarioStatus.ATIVO.name)
                },
                isError = vm.status.temErro.value,
                errorMessage = if (vm.status.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = ativoExibicao,
                opcoes = statusRegistroOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.ativo.definir(valorBoolean)
                },
                isError = vm.ativo.temErro.value,
                errorMessage = if (vm.ativo.temErro.value) "Campo obrigatório" else null
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