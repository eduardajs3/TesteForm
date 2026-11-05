package edu.front.psi.clin.ui.screens.evolucaoClinica

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.EvolucaoClinica
import edu.front.psi.clin.ui.componentes.*
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

private val CorTextoPrincipal = Color(0xFF1F2937)

@Composable
fun EvolucaoClinicaListagem(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel,
    nomePaciente: String = vm.filtro.collectAsState().value.ifEmpty { "Paciente" }
) {
    val listaEvolucoes by vm.evolucoesFiltradas.collectAsState()
    val listaOrdenada = remember(listaEvolucoes) { listaEvolucoes.sortedByDescending { it.id } }

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
            // Cabeçalho da Tela padronizado
            CabecalhoTela(
                titulo = "Evolução Clínica",
                textoBotao = "Nova Evolução",
                iconeBotao = painterResource(Res.drawable.add),
                onClickBotao = { vm.novaEvolucao(nomePaciente) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card do Paciente no Topo padronizado
            CardCabecalhoInfo(
                rotulo = "Paciente",
                valor = nomePaciente
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de Cards de Evoluções Clínicas
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listaOrdenada) { item ->
                    val isExpansivel = itemExpandidoId == item.id

                    CardExpansivelBase(
                        tituloTopo = item.dataRegistro,
                        tituloPrincipal = item.descricao,
                        isExpandido = isExpansivel,
                        onClickToggle = {
                            itemExpandidoId = if (isExpansivel) null else item.id
                        },
                        onEditarClick = { vm.editar(item) },
                        onExcluirClick = { itemParaExcluir = item }
                    ) {
                        LinhaDetalheCard(rotulo = "Psicólogo(a): ", valor = item.psicologo)
                        LinhaDetalheCard(rotulo = "Sessão / Agenda: ", valor = item.agenda)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Situação: ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CorTextoPrincipal
                            )

                            BadgeStatus(
                                texto = if (item.ativo) "Ativo" else "Inativo",
                                isAtivo = item.ativo
                            )
                        }
                    }
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