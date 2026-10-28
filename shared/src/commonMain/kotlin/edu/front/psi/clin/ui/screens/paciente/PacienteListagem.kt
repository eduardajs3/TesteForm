package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.Paciente
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun PacienteListagem(
    innerPadding: PaddingValues,
    vm: PacienteViewModel
) {
    val lista = vm.pacientesFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val pacienteSelecionado = remember { androidx.compose.runtime.mutableStateOf<Paciente?>(null) }

    // 1. Definição das colunas e seus pesos de largura
    val colunas = listOf(
        "Nome" to 1.2f,
        "CPF" to 1.3f,
        "Celular" to 1.2f,
        "Convênio" to 1.1f,
        "Status" to 1.1f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Pacientes",
            textoBotaoNovo = "Novo Paciente",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoPaciente,
            colunas = colunas,
            itens = lista.value,
            onVisualizar = { paciente -> pacienteSelecionado.value = paciente },
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por nome ou CPF..."
        ) { paciente ->
            // 2. Mapeamento dos dados do paciente para cada coluna
            Text(
                text = paciente.nome,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = paciente.cpf,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.3f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = paciente.telefone,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Unimed",
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Badge pill de Status (Ativo / Inativo)
            Box(modifier = Modifier.weight(1.1f)) {
                val isAtivo = paciente.ativo
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isAtivo) Color(0xFFA2C99B) else Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isAtivo) "Ativo" else "Inativo",
                        color = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        pacienteSelecionado.value?.let { paciente ->
            AlertDialog(
                onDismissRequest = { pacienteSelecionado.value = null },
                title = { Text(paciente.nome) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("CPF: ${paciente.cpf}")
                        Text("Data de nascimento: ${paciente.dataNascimento}")
                        Text("E-mail: ${paciente.email}")
                        Text("Telefone: ${paciente.telefone}")
                        Text("Endereço: ${paciente.endereco}")
                        Text("Observações: ${paciente.observacoes}")
                        Text("Status: ${if (paciente.ativo) "Ativo" else "Inativo"}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { pacienteSelecionado.value = null }) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}