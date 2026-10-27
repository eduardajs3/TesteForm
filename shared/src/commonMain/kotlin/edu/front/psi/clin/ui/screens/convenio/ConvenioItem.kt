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
import edu.front.psi.clin.Convenio
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

@Composable
fun ConvenioItem(
    convenio: Convenio,
    apagar: (Convenio) -> Unit = {},
    editar: (Convenio) -> Unit = {}
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
                text = convenio.nome,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "CNPJ: ${convenio.cnpj} | Cobertura: ${convenio.percentualCobertura}%",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column {
            IconButton(onClick = { editar(convenio) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar"
                )
            }
            IconButton(onClick = { apagar(convenio) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover"
                )
            }
        }
    }
}

@Composable
fun ConvenioListagem(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel
) {
    val lista = vm.conveniosFiltrados.collectAsState()
    
    Column(modifier = Modifier.padding(innerPadding)) {
        for (convenio in lista.value) {
            ConvenioItem(
                convenio = convenio,
                apagar = vm::apagar,
                editar = vm::editar
            )
        }
    }
}