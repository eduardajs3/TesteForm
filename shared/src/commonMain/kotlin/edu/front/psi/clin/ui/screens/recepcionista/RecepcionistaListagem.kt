package edu.front.psi.clin.ui.screen

import edu.front.psi.clin.ui.theme.CoresApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.Recepcionista
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

@Composable
fun RecepcionistaListagem(
    innerPadding: PaddingValues,
    vm: RecepcionistaViewModel
) {
    val lista = vm.recepcionistasFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val recepcionistaSelecionada = remember { mutableStateOf<Recepcionista?>(null) }

    // Mapeamento das colunas da tabela com suas proporções de largura
    val colunas = listOf(
        "Nome" to 1.5f,
        "CPF" to 1.3f,
        "Telefone" to 1.2f,
        "Setor" to 1.2f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Recepcionistas",
            textoBotaoNovo = "Nova Recepcionista",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novaRecepcionista,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { recepcionista -> recepcionistaSelecionada.value = recepcionista },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por nome, CPF ou setor..."
        ) { recepcionista ->
            Text(
                text = recepcionista.nome,
                fontWeight = FontWeight.Bold,
                color = CoresApp.TextoPrincipal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recepcionista.cpf,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.3f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recepcionista.telefone,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recepcionista.setor,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Modal de Visualização de Detalhes da Recepcionista
        recepcionistaSelecionada.value?.let { recepcionista ->
            AlertDialog(
                onDismissRequest = { recepcionistaSelecionada.value = null },
                containerColor = CoresApp.Branco,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = recepcionista.nome,
                        fontWeight = FontWeight.Bold,
                        color = CoresApp.Primaria,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemDetalhe("CPF", recepcionista.cpf)
                        ItemDetalhe("Setor", recepcionista.setor)
                        ItemDetalhe("Telefone", recepcionista.telefone)
                        ItemDetalhe("E-mail", recepcionista.email)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { recepcionistaSelecionada.value = null }) {
                        Text(
                            text = "Fechar",
                            color = CoresApp.Primaria,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun ItemDetalhe(rotulo: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            fontWeight = FontWeight.Bold,
            color = CoresApp.Primaria,
            fontSize = 14.sp
        )
        Text(
            text = valor.ifEmpty { "-" },
            color = CoresApp.TextoPrincipal,
            fontSize = 14.sp
        )
    }
}