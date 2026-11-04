package edu.front.psi.clin.ui.screen

import edu.front.psi.clin.ui.theme.CoresApp

import androidx.compose.foundation.layout.*
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
import edu.front.psi.clin.EvolucaoClinica
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel

@Composable
fun EvolucaoClinicaListagem(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel
) {
    val lista = vm.evolucoesFiltradas.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val evolucaoSelecionada = remember { mutableStateOf<EvolucaoClinica?>(null) }

    // Configuração das colunas da tabela
    val colunas = listOf(
        "Prontuário" to 1.2f,
        "Psicólogo(a)" to 1.5f,
        "Data" to 1.1f,
        "Descrição" to 1.8f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Evoluções Clínicas",
            textoBotaoNovo = "Nova Evolução",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novaEvolucao,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { evolucao -> evolucaoSelecionada.value = evolucao },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por prontuário, psicólogo ou descrição..."
        ) { evolucao ->
            Text(
                text = evolucao.prontuario,
                fontWeight = FontWeight.Bold,
                color = CoresApp.TextoPrincipal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = evolucao.psicologo,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = evolucao.dataRegistro,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = evolucao.descricao,
                color = CoresApp.TextoPrincipal,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        evolucaoSelecionada.value?.let { evolucao ->
            AlertDialog(
                onDismissRequest = { evolucaoSelecionada.value = null },
                title = { Text("Evolução clínica") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Prontuário: ${evolucao.prontuario}")
                        Text("Psicólogo(a): ${evolucao.psicologo}")
                        Text("Agenda: ${evolucao.agenda}")
                        Text("Data do registro: ${evolucao.dataRegistro}")
                        Text("Descrição: ${evolucao.descricao}")
                        Text("Status: ${if (evolucao.ativo) "Ativa" else "Inativa"}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { evolucaoSelecionada.value = null }) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}