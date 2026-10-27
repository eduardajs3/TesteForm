package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.HorarioDisponivel
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel

@Composable
fun HorarioDisponivelItem(
    horario: HorarioDisponivel,
    apagar: (HorarioDisponivel) -> Unit = {},
    editar: (HorarioDisponivel) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(shape = RoundedCornerShape(20.dp), color = Color.LightGray)
            .height(100.dp)
            .border(shape = RoundedCornerShape(20.dp), width = 2.dp, color = Color.Black)
            .padding(vertical = 4.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${horario.psicologo} - ${horario.diaSemana}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Horário: ${horario.horaInicio} às ${horario.horaFim}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = horario.descricao,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column {
            IconButton(onClick = { editar(horario) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar"
                )
            }
            IconButton(onClick = { apagar(horario) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover"
                )
            }
        }
    }
}

@Composable
fun HorarioDisponivelListagem(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel
) {
    val lista = vm.horariosFiltrados.collectAsState()
    
    Column(modifier = Modifier.padding(innerPadding)) {
        for (horario in lista.value) {
            HorarioDisponivelItem(
                horario = horario,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}