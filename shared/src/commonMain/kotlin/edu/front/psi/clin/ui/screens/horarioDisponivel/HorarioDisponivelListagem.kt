package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.componentes.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

private val CorTextoLinha = Color(0xFF1F2937)

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
            Text(
                text = horario.psicologo,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = horario.diaSemana,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${horario.horaInicio} às ${horario.horaFim}",
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.3f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = horario.descricao,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}