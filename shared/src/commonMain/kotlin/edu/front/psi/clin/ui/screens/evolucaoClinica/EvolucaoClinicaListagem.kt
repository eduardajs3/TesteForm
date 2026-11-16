package edu.front.psi.clin.ui.screens.evolucaoClinica

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.EvolucaoClinica
import edu.front.psi.clin.ui.componentes.*
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.viewmodel.EvolucaoClinicaViewModel
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

@Composable
fun EvolucaoClinicaListagem(
    innerPadding: PaddingValues,
    vm: EvolucaoClinicaViewModel,
    nomePaciente: String = vm.filtro.collectAsState().value.ifEmpty { "Paciente" }
) {
    val listaEvolucoes by vm.evolucoesFiltradas.collectAsState()
    val listaOrdenada = remember(listaEvolucoes) { listaEvolucoes.sortedByDescending { it.id } }
    val psicologos = vm.psicologos.collectAsState().value
    val agendas = vm.agendas.collectAsState().value
    val pacientes = vm.pacientes.collectAsState().value

    var itemExpandidoId by remember { mutableStateOf<Long?>(null) }
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
            CabecalhoTela(
                titulo = "Evolução Clínica",
                textoBotao = "Nova Evolução",
                iconeBotao = painterResource(Res.drawable.add),
                onClickBotao = { vm.novaEvolucao(nomePaciente) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            CardCabecalhoInfo(
                rotulo = "Paciente",
                valor = nomePaciente
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listaOrdenada) { item ->
                    val isExpansivel = itemExpandidoId == item.id
                    val psicologoNome = psicologos.firstOrNull { it.id == item.psicologoId }?.nome
                        ?: "Psicólogo #${item.psicologoId}"
                    val agenda = agendas.firstOrNull { it.id == item.agendaId }
                    val pacienteNome = agenda?.let { agendamento ->
                        pacientes.firstOrNull { it.id == agendamento.pacienteId }?.nome
                            ?: agendamento.paciente
                    } ?: "Paciente"
                    val agendaDescricao = agenda?.let {
                        "$pacienteNome - ${formatarDataHoraIsoParaBrasileira(it.dataHoraInicio)}"
                    } ?: "Agenda #${item.agendaId}"

                    CardExpansivelBase(
                        tituloTopo = formatarDataIsoParaBrasileira(item.dataRegistro),
                        tituloPrincipal = item.descricao.orEmpty(),
                        isExpandido = isExpansivel,
                        onClickToggle = {
                            itemExpandidoId = if (isExpansivel) null else item.id
                        },
                        onEditarClick = { vm.editar(item) },
                        onExcluirClick = { itemParaExcluir = item }
                    ) {
                        LinhaDetalheCard(rotulo = "Psicólogo(a): ", valor = psicologoNome)
                        LinhaDetalheCard(rotulo = "Sessão / Agenda: ", valor = agendaDescricao)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Situação: ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
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

        itemParaExcluir?.let { item ->
            AppDeleteModal(
                title = "Exclusão de Evolução Clínica",
                message = "Tem certeza que deseja excluir o registro de ${formatarDataIsoParaBrasileira(item.dataRegistro)}?",
                onDismiss = { itemParaExcluir = null },
                onConfirm = {
                    vm.apagar(item)
                    itemParaExcluir = null
                }
            )
        }
    }
}