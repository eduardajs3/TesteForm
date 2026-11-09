package edu.front.psi.clin.ui.screens.horarioDisponivel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.ui.componentes.BotoesAcaoFormulario
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

@Composable
fun HorarioDisponivelFormulario(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()

    val psicologoOpcoes = listOf(
        "Dra. Ana Silva",
        "Dr. Carlos Souza",
        "Dra. Mariana Oliveira"
    )

    val diasDaSemana = listOf(
        "Segunda-feira",
        "Terça-feira",
        "Quarta-feira",
        "Quinta-feira",
        "Sexta-feira",
        "Sábado",
        "Domingo"
    )

    val statusOpcoes = listOf("Ativo", "Inativo")

    val psicologoAtual = vm.psicologo.valor.value
    val psicologoExibicao = if (psicologoOpcoes.contains(psicologoAtual)) psicologoAtual else (psicologoOpcoes.firstOrNull() ?: "")

    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Horário Disponível" else "Cadastro de Horário Disponível",
            subtitulo = if (isEdicao) "Altere os dados do horário selecionado." else "Preencha os dados do horário disponível.",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(HorarioDisponivelRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Nome do psicólogo",
                valorSelecionado = psicologoExibicao,
                opcoes = psicologoOpcoes,
                onOpcaoSelecionada = { selecao ->
                    vm.psicologo.definir(selecao)
                },
                isError = vm.psicologo.temErro.value,
                errorMessage = if (vm.psicologo.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Dia da semana",
                valorSelecionado = vm.diaSemana.valor.value,
                opcoes = diasDaSemana,
                onOpcaoSelecionada = { vm.diaSemana.definir(it) },
                isError = vm.diaSemana.temErro.value,
                errorMessage = if (vm.diaSemana.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaInicio.valor.value,
                onValueChange = { vm.horaInicio.definir(it) },
                label = "Hora de início",
                placeholder = "HH:mm (ex.: 08:00)",
                obrigatorio = true,
                isError = vm.horaInicio.temErro.value,
                errorMessage = if (vm.horaInicio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaFim.valor.value,
                onValueChange = { vm.horaFim.definir(it) },
                label = "Hora de fim",
                placeholder = "HH:mm (ex.: 09:00)",
                obrigatorio = true,
                isError = vm.horaFim.temErro.value,
                errorMessage = if (vm.horaFim.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.descricao.valor.value,
                onValueChange = { vm.descricao.definir(it) },
                label = "Descrição",
                obrigatorio = true,
                isError = vm.descricao.temErro.value,
                errorMessage = if (vm.descricao.temErro.value) "Campo obrigatório" else null
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