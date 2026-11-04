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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.Prontuario
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel

@Composable
fun ProntuarioListagem(
    innerPadding: PaddingValues,
    vm: ProntuarioViewModel
) {
    val lista = vm.prontuariosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val prontuarioSelecionado = remember { mutableStateOf<Prontuario?>(null) }

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Paciente" to 1.5f,
        "Abertura" to 1.1f,
        "Queixa Principal" to 1.8f,
        "Status" to 1.1f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Prontuários",
            textoBotaoNovo = "Novo Prontuário",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoProntuario,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { prontuario -> prontuarioSelecionado.value = prontuario },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por paciente, queixa ou status..."
        ) { prontuario ->
            Text(
                text = prontuario.paciente,
                fontWeight = FontWeight.Bold,
                color = CoresApp.TextoPrincipal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = prontuario.dataAbertura,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = prontuario.queixaPrincipal,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Badge de Status com cores dinâmicas (Ativo / Em Acompanhamento / Encerrado)
            Box(modifier = Modifier.weight(1.1f)) {
                val statusLower = prontuario.status.lowercase()
                val isAtivo = statusLower.contains("ativo") || statusLower.contains("em andamento") || statusLower.contains("aberto")
                val isEncerrado = statusLower.contains("encerrado") || statusLower.contains("inativo") || statusLower.contains("concluído")

                val corFundo = when {
                    isAtivo -> CoresApp.FundoBadgeAtivo
                    isEncerrado -> CoresApp.FundoBadgeAlerta
                    else -> CoresApp.BordaContainer
                }

                val corTexto = when {
                    isAtivo -> CoresApp.TextoBadgeAtivo
                    isEncerrado -> CoresApp.TextoBadgeAlerta
                    else -> CoresApp.TextoBadgeNeutro
                }

                Box(
                    modifier = Modifier
                        .background(color = corFundo, shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = prontuario.status,
                        color = corTexto,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Modal de Detalhes do Prontuário Clínico
        prontuarioSelecionado.value?.let { prontuario ->
            AlertDialog(
                onDismissRequest = { prontuarioSelecionado.value = null },
                containerColor = CoresApp.Branco,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "Prontuário - ${prontuario.paciente}",
                        fontWeight = FontWeight.Bold,
                        color = CoresApp.Primaria,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ItemDetalhe("Paciente", prontuario.paciente)
                        ItemDetalhe("Data de Abertura", prontuario.dataAbertura)
                        ItemDetalhe("Status", prontuario.status)
                        ItemDetalhe("Queixa Principal", prontuario.queixaPrincipal)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { prontuarioSelecionado.value = null }) {
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