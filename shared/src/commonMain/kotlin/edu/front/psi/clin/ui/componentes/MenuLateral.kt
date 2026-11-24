package edu.front.psi.clin.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.navigation.Route
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.ui.theme.CremeCampo
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.viewmodel.PrincipalViewModel
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class SubMenuItemData(
    val nome: String,
    val rota: Route,
    val iconeRes: DrawableResource
)

data class MenuItemData(
    val nome: String,
    val iconeRes: DrawableResource,
    val rota: Route? = null,
    val subItens: List<SubMenuItemData>? = null
)

@Composable
fun MenuLateral(
    viewModel: PrincipalViewModel,
    telaAtual: Route,
    onNavigate: (Route) -> Unit,
    content: @Composable () -> Unit
) {
    val todosItensMenu = listOf(
        MenuItemData(
            nome = "Dashboard",
            iconeRes = Res.drawable.dashboard,
            rota = Route.Dashboard
        ),
        MenuItemData(
            nome = "Pacientes",
            iconeRes = Res.drawable.group_24px,
            rota = Route.Paciente
        ),
        MenuItemData(
            nome = "Cadastros",
            iconeRes = Res.drawable.assignment_24px,
            subItens = listOf(
                SubMenuItemData("Convênios", Route.Convenio, Res.drawable.handshake_24px),
                SubMenuItemData("Salas de Atendimento", Route.SalaAtendimento, Res.drawable.door_open),
                SubMenuItemData("Tipos de Atendimento", Route.TipoAtendimento, Res.drawable.stethoscope_24px)
            )
        ),
        MenuItemData(
            nome = "Agenda",
            iconeRes = Res.drawable.calendar_month,
            subItens = listOf(
                SubMenuItemData("Agenda Geral", Route.Agenda, Res.drawable.calendar_month),
                SubMenuItemData("Bloqueio de Agenda", Route.BloqueioAgenda, Res.drawable.event_busy_24px),
                SubMenuItemData("Horários Disponíveis", Route.HorarioDisponivel, Res.drawable.calendar_check_24px)
            )
        ),
        MenuItemData(
            nome = "Pagamentos",
            iconeRes = Res.drawable.description_24px,
            rota = Route.Pagamento
        ),
        MenuItemData(
            nome = "Funcionários",
            iconeRes = Res.drawable.badge_24px,
            subItens = listOf(
                SubMenuItemData("Funcionários", Route.Funcionario, Res.drawable.badge_24px),
                SubMenuItemData("Psicólogos", Route.Psicologo, Res.drawable.neurology_24px),
                SubMenuItemData("Recepcionistas", Route.Recepcionista, Res.drawable.headset_mic_24px)
            )
        )
    )

    val itensMenu = todosItensMenu.mapNotNull { item ->
        if (item.subItens != null) {
            val subItensPermitidos = item.subItens.filter {
                PermissoesAcesso.podeAcessarRota(viewModel.role, it.rota)
            }
            item.copy(subItens = subItensPermitidos).takeIf { subItensPermitidos.isNotEmpty() }
        } else {
            item.rota?.let { rotaItem ->
                item.takeIf { PermissoesAcesso.podeAcessarRota(viewModel.role, rotaItem) }
            }
        }
    }

    var mostrarModalSair by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = viewModel.drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CremeCampo,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.psiclin_logo),
                        contentDescription = "Logotipo PsiClin",
                        modifier = Modifier
                            .height(80.dp)
                            .padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = VerdeClinica.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        itensMenu.forEach { item ->
                            if (item.subItens != null) {
                                var expandido by remember {
                                    mutableStateOf(item.subItens.any { it.rota == telaAtual })
                                }

                                NavigationDrawerItem(
                                    label = {
                                        Text(
                                            text = item.nome,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = VerdeClinica
                                        )
                                    },
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
                                            painter = painterResource(if (expandido) Res.drawable.keyboard_arrow_up else Res.drawable.keyboard_arrow_down),
                                            contentDescription = "Expandir/Recolher",
                                            tint = VerdeClinica,
                                            modifier = Modifier.size(20.dp)
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
                                            val selecionado = telaAtual == subItem.rota
                                            NavigationDrawerItem(
                                                label = {
                                                    Text(
                                                        text = subItem.nome,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = VerdeClinica
                                                    )
                                                },
                                                selected = selecionado,
                                                onClick = {
                                                    onNavigate(subItem.rota)
                                                    viewModel.fecharGaveta()
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
                                val selecionado = telaAtual == item.rota
                                NavigationDrawerItem(
                                    label = {
                                        Text(
                                            text = item.nome,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = VerdeClinica
                                        )
                                    },
                                    selected = selecionado,
                                    onClick = {
                                        item.rota?.let { onNavigate(it) }
                                        viewModel.fecharGaveta()
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

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.account_circle),
                            contentDescription = "Perfil conectado",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = viewModel.usuarioConectado?.substringBefore('@')
                                    ?.replaceFirstChar { it.uppercase() }
                                    ?: "Usuário conectado",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = VerdeClinica
                            )
                            Text(
                                text = when (viewModel.role?.uppercase()) {
                                    "ADMIN" -> "Administrador"
                                    "RECEPCIONISTA" -> "Recepcionista"
                                    "PSICOLOGO" -> "Psicólogo"
                                    else -> "Perfil não identificado"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = VerdeClinica.copy(alpha = 0.75f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Sair",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextoErro
                            )
                        },
                        selected = false,
                        onClick = {
                            viewModel.fecharGaveta()
                            mostrarModalSair = true
                        },
                        icon = {
                            Icon(
                                painter = painterResource(Res.drawable.logout),
                                contentDescription = "Sair",
                                tint = TextoErro,
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

    if (mostrarModalSair) {
        ModalLogout(
            titulo = "Encerrar Sessão",
            mensagem = "Tem certeza de que deseja sair do sistema?",
            textoBotaoConfirmar = "Sair",
            onDismiss = { mostrarModalSair = false },
            onConfirm = {
                mostrarModalSair = false
                onNavigate(Route.Login)
            }
        )
    }
}