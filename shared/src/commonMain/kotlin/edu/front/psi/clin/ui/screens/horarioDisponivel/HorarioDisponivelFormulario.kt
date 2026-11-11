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

    // Mapeamento correto dos IDs dos psicólogos cadastrados no backend (ex: ID 2, 3, 4)
    val psicologosMap = mapOf(
        "Dra. Ana Silva" to 2L,
        "Dr. Carlos Souza" to 3L,
        "Dra. Mariana Oliveira" to 4L
    )
    val psicologoOpcoes = psicologosMap.keys.toList()

    // Mapeamento correto para os Enums esperados pelo backend Spring Boot
    val diasDaSemanaMap = mapOf(
        "Segunda-feira" to "SEGUNDA",
        "Terça-feira" to "TERCA",
        "Quarta-feira" to "QUARTA",
        "Quinta-feira" to "QUINTA",
        "Sexta-feira" to "SEXTA",
        "Sábado" to "SABADO",
        "Domingo" to "DOMINGO"
    )
    val diasOpcoes = diasDaSemanaMap.keys.toList()

    val statusOpcoes = listOf("Ativo", "Inativo")

    // Descobre o nome do psicólogo com base no ID armazenado no ViewModel
    val psicologoAtualNome = psicologosMap.entries.firstOrNull { it.value.toString() == vm.psicologo.valor.value }?.key ?: psicologoOpcoes.first()
    
    // Descobre o rótulo amigável do dia da semana com base no Enum do ViewModel
    val diaAtualRotulo = diasDaSemanaMap.entries.firstOrNull { it.value == vm.diaSemana.valor.value }?.key ?: diasOpcoes.first()

    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Horário Disponível" else "Cadastro de Horário Disponível",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(HorarioDisponivelRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Nome do psicólogo",
                valorSelecionado = psicologoAtualNome,
                opcoes = psicologoOpcoes,
                onOpcaoSelecionada = { nomeSelecionado ->
                    // Salva o ID correspondente no campo do ViewModel
                    val idPsicologo = psicologosMap[nomeSelecionado] ?: 2L
                    vm.psicologo.definir(idPsicologo.toString())
                },
                isError = vm.psicologo.temErro.value,
                errorMessage = if (vm.psicologo.temErro.value) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Dia da semana",
                valorSelecionado = diaAtualRotulo,
                opcoes = diasOpcoes,
                onOpcaoSelecionada = { rotuloSelecionado ->
                    // Salva o Enum correspondente (ex: "SEGUNDA") no ViewModel
                    val enumValor = diasDaSemanaMap[rotuloSelecionado] ?: "SEGUNDA"
                    vm.diaSemana.definir(enumValor)
                },
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
                textoBotaoSalvar = if (isEdicao) "Salvar Alterações" else "Gravar"
            )
        }
    }
}