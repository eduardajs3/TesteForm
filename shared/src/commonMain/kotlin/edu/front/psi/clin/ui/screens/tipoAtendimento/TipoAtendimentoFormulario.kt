package edu.front.psi.clin.ui.screens.tipoAtendimento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.TipoAtendimentoRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.TipoAtendimentoViewModel

@Composable
fun TipoAtendimentoFormulario(
    innerPadding: PaddingValues,
    vm: TipoAtendimentoViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Tipo de Atendimento" else "Cadastro de Tipo de Atendimento",
            subtitulo = if (isEdicao) "Altere os dados do tipo de atendimento selecionado." else "Preencha os dados do tipo de atendimento.",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(TipoAtendimentoRotas.Listagem)
            }
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome do atendimento",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.descricao.valor.value,
                onValueChange = { vm.descricao.definir(it) },
                label = "Descrição do atendimento",
                obrigatorio = true,
                isError = vm.descricao.temErro.value,
                errorMessage = if (vm.descricao.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.duracao.valor.value,
                onValueChange = { vm.duracao.definir(it) },
                label = "Duração",
                placeholder = "HH:mm (ex.: 00:50)",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.duracao.temErro.value,
                errorMessage = if (vm.duracao.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Status",
                valorSelecionado = statusExibicao,
                opcoes = statusOpcoes,
                onOpcaoSelecionada = { selecao ->
                    val valorBoolean = (selecao == "Ativo").toString()
                    vm.ativo.definir(valorBoolean)
                },
                isError = vm.ativo.temErro.value,
                errorMessage = if (vm.ativo.temErro.value) "Campo obrigatório" else null
            )

            CampoDataPadrao(
                value = vm.dataCadastro.valor.value,
                onValueChange = { vm.dataCadastro.definir(it) },
                label = "Data de cadastro",
                obrigatorio = true,
                isError = vm.dataCadastro.temErro.value,
                errorMessage = if (vm.dataCadastro.temErro.value) "Campo obrigatório" else null
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