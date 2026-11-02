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
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

@Composable
fun ConvenioFormulario(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    // Declaração limpa das opções e conversão de status no topo
    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Convênio" else "Cadastro de Convênio",
            subtitulo = if (isEdicao) "Altere os dados do convênio selecionado." else "Preencha os dados do convênio.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome do convênio",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            // CNPJ com máscara automática tratada no ViewModel (sem apenasNumeros)
            CampoTextoPadrao(
                value = vm.cnpj.valor.value,
                onValueChange = { vm.cnpj.definir(it) },
                label = "CNPJ",
                obrigatorio = true,
                isError = vm.cnpj.temErro.value,
                errorMessage = if (vm.cnpj.temErro.value) "Campo obrigatório" else null
            )

            // Telefone com máscara automática tratada no ViewModel (sem apenasNumeros)
            CampoTextoPadrao(
                value = vm.telefone.valor.value,
                onValueChange = { vm.telefone.definir(it) },
                label = "Telefone",
                obrigatorio = true,
                isError = vm.telefone.temErro.value,
                errorMessage = if (vm.telefone.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.percentualCobertura.valor.value,
                onValueChange = { vm.percentualCobertura.definir(it) },
                label = "Percentual de cobertura (ex.: 80)",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.percentualCobertura.temErro.value,
                errorMessage = if (vm.percentualCobertura.temErro.value) "Campo obrigatório" else null
            )

            // Dropdown padronizado para o Status
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

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}