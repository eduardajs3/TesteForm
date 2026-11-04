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
import edu.front.psi.clin.TipoAtendimento
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.TipoAtendimentoViewModel

@Composable
fun TipoAtendimentoListagem(
    innerPadding: PaddingValues,
    vm: TipoAtendimentoViewModel
) {
    val lista = vm.tiposFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val tipoSelecionado = remember { mutableStateOf<TipoAtendimento?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Tipo de Atendimento" to 1.6f,
        "Duração" to 1.1f,
        "Descrição" to 1.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Tipos de Atendimento",
            textoBotaoNovo = "Novo Tipo",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoTipo,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { tipo -> tipoSelecionado.value = tipo },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por tipo, duração ou descrição..."
        ) { tipo ->
            Text(
                text = tipo.nome,
                fontWeight = FontWeight.Bold,
                color = CoresApp.TextoPrincipal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${tipo.duracao} min",
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = tipo.descricao,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Modal de Visualização de Detalhes do Tipo de Atendimento
        tipoSelecionado.value?.let { tipo ->
            AlertDialog(
                onDismissRequest = { tipoSelecionado.value = null },
                containerColor = CoresApp.Branco,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = tipo.nome,
                        fontWeight = FontWeight.Bold,
                        color = CoresApp.Primaria,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemDetalhe("Duração", "${tipo.duracao} minutos")
                        ItemDetalhe("Descrição", tipo.descricao)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { tipoSelecionado.value = null }) {
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