package edu.front.psi.clin.ui.screens.paciente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.PacienteRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDataPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel

@Composable
fun PacienteFormulario(
    innerPadding: PaddingValues,
    vm: PacienteViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val statusOpcoes = listOf("Ativo", "Inativo")
    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar informações de Paciente" else "Cadastro de Paciente",
            subtitulo = if (isEdicao) "Altere os dados do paciente selecionado." else "Preencha os dados do paciente",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(PacienteRotas.Listagem)
            }
        ) {
            CampoTextoPadrao(
                value = vm.nome.valor.value,
                onValueChange = { vm.nome.definir(it) },
                label = "Nome completo",
                obrigatorio = true,
                isError = vm.nome.temErro.value,
                errorMessage = if (vm.nome.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.cpf.valor.value,
                onValueChange = { vm.cpf.definir(it) },
                label = "CPF",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.cpf.temErro.value,
                errorMessage = if (vm.cpf.temErro.value) "Campo obrigatório" else null
            )

            CampoDataPadrao(
                value = vm.dataNascimento.valor.value,
                onValueChange = { vm.dataNascimento.definir(it) },
                label = "Data de nascimento",
                obrigatorio = true,
                isError = vm.dataNascimento.temErro.value,
                errorMessage = if (vm.dataNascimento.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.email.valor.value,
                onValueChange = { vm.email.definir(it) },
                label = "E-mail",
                obrigatorio = true,
                isError = vm.email.temErro.value,
                errorMessage = if (vm.email.temErro.value) vm.email.mensagemErro.value else null
            )
            
            CampoTextoPadrao(
                value = vm.telefone.valor.value,
                onValueChange = { vm.telefone.definir(it) },
                label = "Telefone",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.telefone.temErro.value,
                errorMessage = if (vm.telefone.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.endereco.valor.value,
                onValueChange = { vm.endereco.definir(it) },
                label = "Endereço",
                obrigatorio = true,
                isError = vm.endereco.temErro.value,
                errorMessage = if (vm.endereco.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.observacoes.valor.value,
                onValueChange = { vm.observacoes.definir(it) },
                label = "Observações"
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