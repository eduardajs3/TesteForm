package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.Paciente
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteListagem(
    innerPadding: PaddingValues,
    vm: PacienteViewModel,
    onNavegarParaProntuario: (Paciente) -> Unit = {},
    onNavegarParaEvolucao: (Paciente) -> Unit = {}
) {
    val lista = vm.pacientesFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val pacienteSelecionado = remember { mutableStateOf<Paciente?>(null) }
    var pacienteParaExcluir by remember { mutableStateOf<Paciente?>(null) }
    var exibirDetalhesCadastrais by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Cabeçalho e Botão de Novo Paciente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pacientes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica
                    )
                    Text(
                        text = "Gerencie os pacientes cadastrados na clínica",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = vm::novoPaciente,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Novo Paciente",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Novo Paciente", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo de Busca
            OutlinedTextField(
                value = filtro.value,
                onValueChange = vm::atualizarFiltro,
                placeholder = { Text("Buscar por nome ou CPF...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = VerdeClinica,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de Cards de Pacientes
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lista.value) { paciente ->
                    CardPaciente(
                        paciente = paciente,
                        onClick = { pacienteSelecionado.value = paciente },
                        onEditarClick = { vm.editar(paciente) },
                        onExcluirClick = { pacienteParaExcluir = paciente }
                    )
                }
            }
        }

        // Modal de Seleção de Ações do Paciente
        pacienteSelecionado.value?.let { paciente ->
            if (!exibirDetalhesCadastrais) {
                AlertDialog(
                    onDismissRequest = { pacienteSelecionado.value = null },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Text(
                            text = paciente.nome,
                            fontWeight = FontWeight.Bold,
                            color = VerdeClinica,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Selecione a opção desejada:",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            Button(
                                onClick = {
                                    val targetPaciente = paciente
                                    pacienteSelecionado.value = null
                                    onNavegarParaProntuario(targetPaciente)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Prontuário", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    val targetPaciente = paciente
                                    pacienteSelecionado.value = null
                                    onNavegarParaEvolucao(targetPaciente)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Evolução Clínica", color = Color.White)
                            }

                            OutlinedButton(
                                onClick = { exibirDetalhesCadastrais = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Informações Cadastrais", color = VerdeClinica)
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { pacienteSelecionado.value = null }) {
                            Text("Cancelar", color = Color.Gray)
                        }
                    }
                )
            } else {
                // Modal de Dados Cadastrais
                AlertDialog(
                    onDismissRequest = {
                        exibirDetalhesCadastrais = false
                        pacienteSelecionado.value = null
                    },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Text(
                            text = "Informações Cadastrais",
                            fontWeight = FontWeight.Bold,
                            color = VerdeClinica,
                            fontSize = 18.sp
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ItemInfo("Nome", paciente.nome)
                            ItemInfo("CPF", paciente.cpf)
                            ItemInfo("Data de nascimento", paciente.dataNascimento)
                            ItemInfo("E-mail", paciente.email)
                            ItemInfo("Telefone", paciente.telefone)
                            ItemInfo("Endereço", paciente.endereco)
                            ItemInfo("Observações", paciente.observacoes)
                            ItemInfo("Status", if (paciente.ativo) "Ativo" else "Inativo")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { exibirDetalhesCadastrais = false }) {
                            Text("Voltar", color = VerdeClinica, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }

        // Modal de Exclusão de Paciente
        pacienteParaExcluir?.let { paciente ->
            AppDeleteModal(
                title = "Exclusão de paciente",
                message = "Tem certeza que deseja excluir o cadastro de ${paciente.nome}?",
                onDismiss = { pacienteParaExcluir = null },
                onConfirm = {
                    vm.apagar(paciente)
                    pacienteParaExcluir = null
                }
            )
        }
    }
}

@Composable
private fun CardPaciente(
    paciente: Paciente,
    onClick: () -> Unit,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = paciente.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CorTextoLinha
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CPF: ${paciente.cpf}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Badge com Status (Ativo / Inativo)
                val isAtivo = paciente.ativo
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isAtivo) Color(0xFFA2C99B) else Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isAtivo) "Ativo" else "Inativo",
                        color = if (isAtivo) Color(0xFF2E5927) else Color(0xFF92400E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botão Editar
                IconButton(
                    onClick = onEditarClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Paciente",
                        tint = Color.Gray
                    )
                }

                // Botão Excluir
                IconButton(
                    onClick = onExcluirClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir Paciente",
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemInfo(rotulo: String, valor: String) {
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