package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.EvolucaoClinica
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*


private val CorTextoPrincipal = Color(0xFF1F2937)
private val CorCardFundo = Color(0xFFF9FAFB)
private val CorBordaCard = Color(0xFFE5E7EB)

@Composable
fun EvolucaoClinicaListagem(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel,
    nomePaciente: String = vm.filtro.collectAsState().value.ifEmpty { "Paciente" }
) {
    val listaEvolucoes by vm.evolucoesFiltradas.collectAsState()

    val listaOrdenada = remember(listaEvolucoes) {
        listaEvolucoes.sortedByDescending { it.id }
    }

    var itemExpandidoId by remember { mutableStateOf<Int?>(null) }
    var itemParaExcluir by remember { mutableStateOf<EvolucaoClinica?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabeçalho da Tela
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Evolução Clínica",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CorTextoPrincipal
                )

                Button(
                    onClick = { vm.novaEvolucao(nomePaciente) },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Nova Evolução",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nova Evolução", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card do Paciente no Topo (Exibe o nome correto recebido)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CorCardFundo),
                border = BorderStroke(1.dp, CorBordaCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Paciente",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CorTextoPrincipal
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = nomePaciente,
                        fontSize = 16.sp,
                        color = CorTextoPrincipal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de Cards de Evoluções Clínicas
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listaOrdenada) { item ->
                    val isExpansivel = itemExpandidoId == item.id

                    CardEvolucao(
                        item = item,
                        isExpandido = isExpansivel,
                        onClickToggle = {
                            itemExpandidoId = if (isExpansivel) null else item.id
                        },
                        onEditarClick = { vm.editar(item) },
                        onExcluirClick = { itemParaExcluir = item }
                    )
                }
            }
        }

        // Modal de Exclusão
        itemParaExcluir?.let { item ->
            AppDeleteModal(
                title = "Exclusão de Evolução Clínica",
                message = "Tem certeza que deseja excluir o registro de ${item.dataRegistro}?",
                onDismiss = { itemParaExcluir = null },
                onConfirm = {
                    vm.apagar(item)
                    itemParaExcluir = null
                }
            )
        }
    }
}

@Composable
private fun CardEvolucao(
    item: EvolucaoClinica,
    isExpandido: Boolean,
    onClickToggle: () -> Unit,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CorCardFundo),
        border = BorderStroke(1.dp, CorBordaCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickToggle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Data do registro em destaque no topo do card
                    Text(
                        text = item.dataRegistro,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.descricao,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CorTextoPrincipal
                    )
                }

                if (isExpandido) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = onEditarClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.edit_square_24px),
                                contentDescription = "Editar",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = onExcluirClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.delete_24px),
                                contentDescription = "Excluir",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expandir",
                        tint = CorTextoPrincipal
                    )
                }
            }

            if (isExpandido) {
                Spacer(modifier = Modifier.height(16.dp))

                if (item.psicologo.isNotEmpty()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Psicólogo(a): ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CorTextoPrincipal
                        )
                        Text(
                            text = item.psicologo,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (item.agenda.isNotEmpty()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Sessão / Agenda: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CorTextoPrincipal
                        )
                        Text(
                            text = item.agenda,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Situação: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CorTextoPrincipal
                    )
                    Text(
                        text = if (item.ativo) "Ativo" else "Inativo",
                        fontSize = 14.sp,
                        color = if (item.ativo) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )
                }
            }
        }
    }
}