package edu.front.psi.clin.ui.screens.horarioDisponivel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import edu.front.psi.clin.model.enums.DiaSemana
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
    LaunchedEffect(vm) {
        vm.carregarDadosFormulario()
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        vm.carregarDadosFormulario()
    }

    val isEdicao = vm.id.value.isNotEmpty()
    val salvando = vm.salvando
    val psicologosLista = vm.psicologos.collectAsState().value
    val psicologosFiltrados = psicologosLista.filter {
        it.ativo || (isEdicao && it.id == vm.psicologoId)
    }
    val psicologoOpcoes = psicologosFiltrados.map { it.nome }

    val statusOpcoes = listOf("Ativo", "Inativo")

    val psicologoAtualObj = psicologosFiltrados.firstOrNull { it.id == vm.psicologoId }
    val psicologoAtualNome = psicologoAtualObj?.nome.orEmpty()

    val diaSelecionado = if (vm.diaSemana.valor.value.isBlank()) {
        null
    } else {
        DiaSemana.deValorApi(vm.diaSemana.valor.value)
    }
    val diaAtualRotulo = diaSelecionado?.rotulo.orEmpty()

    val statusExibicao = if (vm.ativo.valor.value == "true" || vm.ativo.valor.value.isEmpty()) "Ativo" else "Inativo"

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar Horário Disponível" else "Cadastro de Horário Disponível",
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(HorarioDisponivelRotas.Listagem)
            }
        ) {
            CampoDropdownPadrao(
                label = "Nome do psicólogo *",
                valorSelecionado = psicologoAtualNome,
                opcoes = psicologoOpcoes,
                onOpcaoSelecionada = { nomeSelecionado ->
                    val psicologoEscolhido = psicologosFiltrados.firstOrNull { it.nome == nomeSelecionado }
                    vm.selecionarPsicologo(psicologoEscolhido?.id)
                },
                isError = vm.erroPsicologo,
                errorMessage = if (vm.erroPsicologo) "Campo obrigatório" else null
            )

            CampoDropdownPadrao(
                label = "Dia da semana *",
                valorSelecionado = diaAtualRotulo,
                opcoes = DiaSemana.entries.map { it.rotulo },
                onOpcaoSelecionada = { rotuloSelecionado ->
                    val diaEscolhido = DiaSemana.entries.firstOrNull { it.rotulo == rotuloSelecionado }
                    vm.diaSemana.definir(diaEscolhido?.name.orEmpty())
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
                apenasNumeros = true,
                isError = vm.horaInicio.temErro.value,
                errorMessage = if (vm.horaInicio.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.horaFim.valor.value,
                onValueChange = { vm.horaFim.definir(it) },
                label = "Hora de fim",
                placeholder = "HH:mm (ex.: 09:00)",
                obrigatorio = true,
                apenasNumeros = true,
                isError = vm.horaFim.temErro.value,
                errorMessage = if (vm.horaFim.temErro.value) "Campo obrigatório" else null
            )

            CampoTextoPadrao(
                value = vm.descricao.valor.value,
                onValueChange = { vm.descricao.definir(it) },
                label = "Descrição",
                obrigatorio = false,
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