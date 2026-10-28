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
import edu.front.psi.clin.Psicologo
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.PsicologoViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun PsicologoListagem(
    innerPadding: PaddingValues,
    vm: PsicologoViewModel
) {
    val lista = vm.psicologosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val psicologoSelecionado = remember { mutableStateOf<Psicologo?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Nome" to 1.5f,
        "CRP" to 1.2f,
        "Telefone" to 1.2f,
        "Especialidade" to 1.6f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Psicólogos",
            textoBotaoNovo = "Novo Psicólogo",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoPsicologo,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { psicologo -> psicologoSelecionado.value = psicologo },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por nome, CRP ou especialidade..."
        ) { psicologo ->
            Text(
                text = psicologo.nome,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = psicologo.crp,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = psicologo.telefone,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = psicologo.especialidade,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Modal de Visualização de Detalhes do Psicólogo
        psicologoSelecionado.value?.let { psicologo ->
            AlertDialog(
                onDismissRequest = { psicologoSelecionado.value = null },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = psicologo.nome,
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemDetalhe("CRP", psicologo.crp)
                        ItemDetalhe("Especialidade", psicologo.especialidade)
                        ItemDetalhe("Telefone", psicologo.telefone)
                        ItemDetalhe("E-mail", psicologo.email)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { psicologoSelecionado.value = null }) {
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