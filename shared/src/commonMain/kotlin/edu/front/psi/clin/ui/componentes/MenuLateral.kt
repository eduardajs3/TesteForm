package edu.front.psi.clin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.CremeCampo
import edu.front.psi.clin.VerdeClinica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class SubMenuItemData(
    val nome: String,
    val tela: String
)

data class MenuItemData(
    val nome: String,
    val icone: String,
    val tela: String? = null,
    val subItens: List<SubMenuItemData>? = null
)

@Composable
fun MenuLateral(
    drawerState: DrawerState,
    scope: CoroutineScope,
    telaAtual: String,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val itensMenu = listOf(
        MenuItemData("Dashboard", "dashboard", tela = "dashboard"),
        MenuItemData("Pacientes", "pessoa", tela = "paciente"),
        MenuItemData(
            nome = "Funcionários",
            icone = "pessoa",
            subItens = listOf(
                SubMenuItemData("Funcionários", "funcionario"),
                SubMenuItemData("Psicólogos", "psicologo"),
                SubMenuItemData("Recepcionistas", "recepcionista")
            )
        ),
        MenuItemData(
            nome = "Cadastros",
            icone = "cadastros",
            subItens = listOf(
                SubMenuItemData("Convênios", "convenio"),
                SubMenuItemData("Salas de Atendimento", "sala"),
                SubMenuItemData("Tipos de Atendimento", "tipo")
            )
        ),
        MenuItemData(
            nome = "Agenda",
            icone = "agenda",
            subItens = listOf(
                SubMenuItemData("Agenda Geral", "agenda"),
                SubMenuItemData("Bloqueio de Agenda", "bloqueio"),
                SubMenuItemData("Horários Disponíveis", "horario")
            )
        ),
        MenuItemData("Pagamentos", "documento", tela = "pagamento"),
        MenuItemData("Prontuários", "documento", tela = "prontuario"),
        MenuItemData("Evolução Clínica", "documento", tela = "evolucao")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CremeCampo,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "PsiClin",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeClinica,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Text(
                        text = "Gestão de Psicologia",
                        fontSize = 12.sp,
                        color = VerdeClinica.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = VerdeClinica.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        itensMenu.forEach { item ->
                            if (item.subItens != null) {
                                // Item Pai Expansível (Funcionários, Cadastros e Agenda)
                                var expandido by remember {
                                    mutableStateOf(item.subItens.any { it.tela == telaAtual })
                                }

                                NavigationDrawerItem(
                                    label = { Text(item.nome, color = VerdeClinica, fontWeight = FontWeight.Bold) },
                                    selected = false,
                                    onClick = { expandido = !expandido },
                                    icon = {
                                        Icon(
                                            imageVector = when (item.icone) {
                                                "pessoa" -> Icons.Filled.Person
                                                "agenda" -> Icons.Filled.DateRange
                                                "cadastros" -> Icons.Filled.ListAlt
                                                else -> Icons.Filled.Description
                                            },
                                            contentDescription = item.nome,
                                            tint = VerdeClinica
                                        )
                                    },
                                    badge = {
                                        Icon(
                                            imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                            contentDescription = "Expandir/Recolher",
                                            tint = VerdeClinica
                                        )
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedContainerColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )

                                // Sub-itens
                                AnimatedVisibility(
                                    visible = expandido,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    Column(modifier = Modifier.padding(start = 16.dp)) {
                                        item.subItens.forEach { subItem ->
                                            val selecionado = telaAtual == subItem.tela
                                            NavigationDrawerItem(
                                                label = { Text(subItem.nome, color = VerdeClinica, fontSize = 13.sp) },
                                                selected = selecionado,
                                                onClick = {
                                                    onNavigate(subItem.tela)
                                                    scope.launch { drawerState.close() }
                                                },
                                                colors = NavigationDrawerItemDefaults.colors(
                                                    selectedContainerColor = VerdeClinica.copy(alpha = 0.15f),
                                                    unselectedContainerColor = Color.Transparent
                                                ),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.padding(vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Item Direto
                                val selecionado = telaAtual == item.tela
                                NavigationDrawerItem(
                                    label = { Text(item.nome, color = VerdeClinica) },
                                    selected = selecionado,
                                    onClick = {
                                        item.tela?.let { onNavigate(it) }
                                        scope.launch { drawerState.close() }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = when (item.icone) {
                                                "dashboard" -> Icons.Filled.Dashboard
                                                "pessoa" -> Icons.Filled.Person
                                                "agenda" -> Icons.Filled.DateRange
                                                else -> Icons.Filled.Description
                                            },
                                            contentDescription = item.nome,
                                            tint = VerdeClinica
                                        )
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = VerdeClinica.copy(alpha = 0.15f),
                                        unselectedContainerColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        content = content
    )
}