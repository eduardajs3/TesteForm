package edu.front.psi.clin.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.CremeCampo
import edu.front.psi.clin.VerdeClinica
import edu.front.psi.clin.ui.componentes.AppDeleteModal // Importando o seu componente de modal reutilizável
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

import frontpsiclinic.shared.generated.resources.*

data class SubMenuItemData(
    val nome: String,
    val tela: String,
    val iconeRes: DrawableResource
)

data class MenuItemData(
    val nome: String,
    val iconeRes: DrawableResource,
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
        MenuItemData(
            nome = "Dashboard", 
            iconeRes = Res.drawable.dashboard, 
            tela = "dashboard"
        ),
        MenuItemData(
            nome = "Pacientes", 
            iconeRes = Res.drawable.group_24px, 
            tela = "paciente"
        ),
        MenuItemData(
            nome = "Cadastros",
            iconeRes = Res.drawable.assignment_24px,
            subItens = listOf(
                SubMenuItemData("Convênios", "convenio", Res.drawable.handshake_24px),
                SubMenuItemData("Salas de Atendimento", "sala", Res.drawable.door_open),
                SubMenuItemData("Tipos de Atendimento", "tipo", Res.drawable.stethoscope_24px)
            )
        ),
        MenuItemData(
            nome = "Agenda",
            iconeRes = Res.drawable.calendar_month,
            subItens = listOf(
                SubMenuItemData("Agenda Geral", "agenda", Res.drawable.calendar_month),
                SubMenuItemData("Bloqueio de Agenda", "bloqueio", Res.drawable.event_busy_24px),
                SubMenuItemData("Horários Disponíveis", "horario", Res.drawable.calendar_check_24px)
            )
        ),
        MenuItemData(
            nome = "Pagamentos", 
            iconeRes = Res.drawable.description_24px, 
            tela = "pagamento"
        ),
        MenuItemData(
            nome = "Funcionários",
            iconeRes = Res.drawable.badge_24px,
            subItens = listOf(
                SubMenuItemData("Funcionários", "funcionario", Res.drawable.badge_24px),
                SubMenuItemData("Psicólogos", "psicologo", Res.drawable.neurology_24px),
                SubMenuItemData("Recepcionistas", "recepcionista", Res.drawable.headset_mic_24px)
            )
        )
    )

    // Estado para controlar a exibição do modal de confirmação de saída
    var mostrarModalSair by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CremeCampo,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
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

                    // Lista de itens do menu rolável
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        itensMenu.forEach { item ->
                            if (item.subItens != null) {
                                var expandido by remember {
                                    mutableStateOf(item.subItens.any { it.tela == telaAtual })
                                }

                                NavigationDrawerItem(
                                    label = { Text(item.nome, color = VerdeClinica, fontWeight = FontWeight.Bold) },
                                    selected = false,
                                    onClick = { expandido = !expandido },
                                    icon = {
                                        Icon(
                                            painter = painterResource(item.iconeRes),
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
                                                    Icon(
                                                        painter = painterResource(subItem.iconeRes),
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
                                            painter = painterResource(item.iconeRes),
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

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = VerdeClinica.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Botão Sair fixo na parte inferior do menu lateral com ícone nativo
                    NavigationDrawerItem(
                        label = { Text("Sair", color = Color(0xFFB91C1C), fontWeight = FontWeight.Bold) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            mostrarModalSair = true
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Sair",
                                tint = Color(0xFFB91C1C),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        },
        content = content
    )

    // Utilizando o componente reutilizável AppDeleteModal para confirmar a saída
    if (mostrarModalSair) {
        AppDeleteModal(
            title = "Encerrar Sessão",
            message = "Tem certeza de que deseja sair do sistema?",
            onDismiss = { mostrarModalSair = false },
            onConfirm = {
                mostrarModalSair = false
                onNavigate("login")
            }
        )
    }
}