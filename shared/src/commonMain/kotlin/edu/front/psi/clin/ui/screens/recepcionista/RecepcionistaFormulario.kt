package edu.front.psi.clin.ui.screens.recepcionista

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
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

@Composable
fun RecepcionistaFormulario(
    innerPadding: PaddingValues,
    vm: RecepcionistaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    // Declaração limpa das opções e conversão de status no topo
    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Recepcionista" else "Cadastro de Recepcionista",
            subtitulo = if (isEdicao) "Altere os dados da recepcionista selecionada." else "Preencha os dados da recepcionista.",
            mensagemFeedback = vm.mensagem.value
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome completo",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            // CPF com máscara automática (sem apenasNumeros)
            CampoTextoPadrao(
                value = vm.cpf.valor.value,
                onValueChange = { vm.cpf.definir(it) },
                label = "CPF",
                obrigatorio = true,
                isError = vm.cpf.temErro.value,
                errorMessage = if (vm.cpf.temErro.value) "Campo obrigatório" else null
            )

            // E-mail com suporte a validação dinâmica de formato
            CampoTextoPadrao(
                value = vm.email.valor.value,
                onValueChange = { vm.email.definir(it) },
                label = "E-mail",
                obrigatorio = true,
                isError = vm.email.temErro.value,
                errorMessage = if (vm.email.temErro.value) vm.email.mensagemErro.value else null
            )

            // Telefone com máscara automática (sem apenasNumeros)
            CampoTextoPadrao(
                value = vm.telefone.valor.value,
                onValueChange = { vm.telefone.definir(it) },
                label = "Telefone",
                obrigatorio = true,
                isError = vm.telefone.temErro.value,
                errorMessage = if (vm.telefone.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.senha.valor.value,
                onValueChange = { vm.senha.definir(it) },
                label = "Senha",
                obrigatorio = true,
                isError = vm.senha.temErro.value,
                errorMessage = if (vm.senha.temErro.value) "Campo obrigatório" else null
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

            CampoDataPadrao(
                value = vm.dataCadastro.valor.value,
                onValueChange = { vm.dataCadastro.definir(it) },
                label = "Data de cadastro",
                obrigatorio = true,
                isError = vm.dataCadastro.temErro.value,
                errorMessage = if (vm.dataCadastro.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.setor.valor.value,
                onValueChange = { vm.setor.definir(it) },
                label = "Setor",
                obrigatorio = true,
                isError = vm.setor.temErro.value,
                errorMessage = if (vm.setor.temErro.value) "Campo obrigatório" else null
            )

            BotoesAcaoFormulario(
                onLimpar = vm::limparCampos,
                onSalvar = vm::salvar,
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}