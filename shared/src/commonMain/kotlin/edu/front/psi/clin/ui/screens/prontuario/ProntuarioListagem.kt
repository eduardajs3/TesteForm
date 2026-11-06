package edu.front.psi.clin.ui.screens.prontuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Prontuario
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.*
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.viewmodel.ProntuarioViewModel
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProntuarioListagem(
    innerPadding: PaddingValues,
    vm: ProntuarioViewModel,
    nomePaciente: String = vm.paciente.value.ifEmpty { vm.filtro.collectAsState().value.ifEmpty { "Todos os Pacientes" } }
) {
    val listaProntuarios by vm.prontuariosFiltrados.collectAsState()
    val listaOrdenada = remember(listaProntuarios) { listaProntuarios.sortedByDescending { it.id } }

    var itemExpandidoId by remember { mutableStateOf<Int?>(null) }
    var itemParaExcluir by remember { mutableStateOf<Prontuario?>(null) }

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
                titulo = "Prontuário",
                textoBotao = "Novo Prontuário",
                iconeBotao = painterResource(Res.drawable.add),
                onClickBotao = { vm.novoProntuario(nomePaciente) }
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

                    CardExpansivelBase(
                        tituloTopo = item.dataAbertura,
                        tituloPrincipal = item.queixaPrincipal,
                        isExpandido = isExpansivel,
                        onClickToggle = {
                            itemExpandidoId = if (isExpansivel) null else item.id
                        },
                        onEditarClick = { vm.editar(item) },
                        onExcluirClick = { itemParaExcluir = item }
                    ) {
                        LinhaDetalheCard(rotulo = "Status: ", valor = item.status)

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
                title = "Exclusão de prontuário",
                message = "Tem certeza que deseja excluir o registro de ${item.dataAbertura}?",
                onDismiss = { itemParaExcluir = null },
                onConfirm = {
                    vm.apagar(item)
                    itemParaExcluir = null
                }
            )
        }
    }
}