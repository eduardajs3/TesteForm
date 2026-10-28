package edu.front.psi.clin.ui.screen

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
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.RecepcionistaViewModel

private val CorTextoLinha = Color(0xFF1F2937)

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
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recepcionista.cpf,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.3f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recepcionista.telefone,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recepcionista.setor,
                color = CorTextoLinha,
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
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = recepcionista.nome,
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica,
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
                            color = VerdeClinica,
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
            color = VerdeClinica,
            fontSize = 14.sp
        )
        Text(
            text = valor.ifEmpty { "-" },
            color = CorTextoLinha,
            fontSize = 14.sp
        )
    }
}