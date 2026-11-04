package edu.front.psi.clin.ui.screen

import edu.front.psi.clin.ui.theme.CoresApp

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
import edu.front.psi.clin.SalaAtendimento
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.SalaAtendimentoViewModel

@Composable
fun SalaAtendimentoListagem(
    innerPadding: PaddingValues,
    vm: SalaAtendimentoViewModel
) {
    val lista = vm.salasFiltradas.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val salaSelecionada = remember { mutableStateOf<SalaAtendimento?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Nome da Sala" to 1.5f,
        "Capacidade" to 1.0f,
        "Descrição" to 2.0f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Salas de Atendimento",
            textoBotaoNovo = "Nova Sala",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novaSala,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { sala -> salaSelecionada.value = sala },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por nome ou descrição..."
        ) { sala ->
            Text(
                text = sala.nome,
                fontWeight = FontWeight.Bold,
                color = CoresApp.TextoPrincipal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${sala.capacidade} pessoas",
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.0f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sala.descricao,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(2.0f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Modal de Visualização de Detalhes da Sala de Atendimento
        salaSelecionada.value?.let { sala ->
            AlertDialog(
                onDismissRequest = { salaSelecionada.value = null },
                containerColor = CoresApp.Branco,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = sala.nome,
                        fontWeight = FontWeight.Bold,
                        color = CoresApp.Primaria,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemDetalhe("Capacidade", "${sala.capacidade} pessoas")
                        ItemDetalhe("Descrição", sala.descricao)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { salaSelecionada.value = null }) {
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