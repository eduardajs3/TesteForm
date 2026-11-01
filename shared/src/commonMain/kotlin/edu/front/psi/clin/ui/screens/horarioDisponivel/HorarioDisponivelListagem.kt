package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.components.CelulaTabelaTexto
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

@Composable
fun HorarioDisponivelListagem(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel
) {
    val lista = vm.horariosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    // Configuração das colunas da tabela com suas proporções de largura
    val colunas = listOf(
        "Psicólogo(a)" to 1.5f,
        "Dia da Semana" to 1.2f,
        "Horário" to 1.3f,
        "Descrição" to 1.5f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Horários Disponíveis",
            textoBotaoNovo = "Novo Horário",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoHorario,
            colunas = colunas,
            itens = lista.value,
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por psicólogo, dia ou horário..."
        ) { horario ->
            CelulaTabelaTexto(
                text = horario.psicologo,
                weight = 1.5f,
                isBold = true,
                fontSize = 14
            )
            CelulaTabelaTexto(
                text = horario.diaSemana,
                weight = 1.2f
            )
            CelulaTabelaTexto(
                text = "${horario.horaInicio} às ${horario.horaFim}",
                weight = 1.3f
            )
            CelulaTabelaTexto(
                text = horario.descricao,
                weight = 1.5f
            )
        }
    }
}