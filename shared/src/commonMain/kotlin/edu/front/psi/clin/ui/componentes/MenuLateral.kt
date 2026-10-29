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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import org.jetbrains.compose.resources.painterResource
import testform.generated.resources.*

data class SubMenuItemData(
    val nome: String,
    val tela: String,
    val icone: String = "description"
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
        MenuItemData("Pacientes", "pacientes", tela = "paciente"),
        
        MenuItemData(
            nome = "Cadastros",
            icone = "cadastros",
            subItens = listOf(
                SubMenuItemData("Convênios", "convenio", icone = "badge"),
                SubMenuItemData("Salas de Atendimento", "sala", icone = "neurology"),
                SubMenuItemData("Tipos de Atendimento", "tipo", icone = "stethoscope")
            )
        ),
        MenuItemData(
            nome = "Agenda",
            icone = "agenda",
            subItens = listOf(
                SubMenuItemData("Agenda Geral", "agenda", icone = "calendar_check"),
                SubMenuItemData("Bloqueio de Agenda", "bloqueio", icone = "event_busy"),
                SubMenuItemData("Horários Disponíveis", "horario", icone = "assignment")
            )
        ),
        MenuItemData("Pagamentos", "pagamentos", tela = "pagamento"),
        MenuItemData(
            nome = "Funcionários",
            icone = "funcionarios",
            subItens = listOf(
                SubMenuItemData("Funcionários", "funcionario", icone = "group"),
                SubMenuItemData("Psicólogos", "psicologo", icone = "headset_mic"),
                SubMenuItemData("Recepcionistas", "recepcionista", icone = "handshake")
            )
        ),
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
                                // Item Pai Expansível
                                var expandido by remember {
                                    mutableStateOf(item.subItens.any { it.tela == telaAtual })
                                }

                                NavigationDrawerItem(
                                    label = { Text(item.nome, color = VerdeClinica, fontWeight = FontWeight.Bold) },
                                    selected = false,
                                    onClick = { expandido = !expandido },
                                    icon = {
                                        val painterIcon = when (item.icone) {
                                            "cadastros" -> painterResource(Res.drawable.badge_24px)[cite: 2]
                                            "agenda" -> painterResource(Res.drawable.calendar_check_24px)[cite: 2]
                                            "funcionarios" -> painterResource(Res.drawable.group_24px)[cite: 2]
                                            else -> painterResource(Res.drawable.description_24px)[cite: 2]
                                        }
                                        Icon(
                                            painter = painterIcon,
                                            contentDescription = item.nome,
                                            tint = VerdeClinica,
                                            modifier = Modifier.size(24.dp)
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

                                // Sub-itens com ícones personalizados
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
                                                icon = {
                                                    val painterSubIcon = when (subItem.icone) {
                                                        "badge" -> painterResource(Res.drawable.badge_24px)[cite: 2]
                                                        "neurology" -> painterResource(Res.drawable.neurology_24px)[cite: 2]
                                                        "stethoscope" -> painterResource(Res.drawable.stethoscope_24px)[cite: 2]
                                                        "calendar_check" -> painterResource(Res.drawable.calendar_check_24px)[cite: 2]
                                                        "event_busy" -> painterResource(Res.drawable.event_busy_24px)[cite: 2]
                                                        "assignment" -> painterResource(Res.drawable.assignment_24px)[cite: 2]
                                                        "group" -> painterResource(Res.drawable.group_24px)[cite: 2]
                                                        "headset_mic" -> painterResource(Res.drawable.headset_mic_24px)[cite: 2]
                                                        "handshake" -> painterResource(Res.drawable.handshake_24px)[cite: 2]
                                                        else -> painterResource(Res.drawable.description_24px)[cite: 2]
                                                    }
                                                    Icon(
                                                        painter = painterSubIcon,
                                                        contentDescription = subItem.nome,
                                                        tint = VerdeClinica,
                                                        modifier = Modifier.size(20.dp)
                                                    )
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
                                // Item Direto (Dashboard, Pacientes, Pagamentos)
                                val selecionado = telaAtual == item.tela
                                NavigationDrawerItem(
                                    label = { Text(item.nome, color = VerdeClinica) },
                                    selected = selecionado,
                                    onClick = {
                                        item.tela?.let { onNavigate(it) }
                                        scope.launch { drawerState.close() }
                                    },
                                    icon = {
                                        val painterIcon = when (item.icone) {
                                            "dashboard" -> painterResource(Res.drawable.edit_square_24px)[cite: 2]
                                            "pacientes" -> painterResource(Res.drawable.group_24px)[cite: 2]
                                            "pagamentos" -> painterResource(Res.drawable.assignment_24px)[cite: 2]
                                            else -> painterResource(Res.drawable.description_24px)[cite: 2]
                                        }
                                        Icon(
                                            painter = painterIcon,
                                            contentDescription = item.nome,
                                            tint = VerdeClinica,
                                            modifier = Modifier.size(24.dp)
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
