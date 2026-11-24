package edu.front.psi.clin.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.CardFundoCinza
import edu.front.psi.clin.ui.theme.CinzaBordaCampo
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun CardExpansivelBase(
    tituloTopo: String,
    tituloPrincipal: String,
    isExpandido: Boolean,
    onClickToggle: () -> Unit,
    onEditarClick: () -> Unit = {},
    onExcluirClick: () -> Unit = {},
    mostrarAcoesTopo: Boolean = true,
    modifier: Modifier = Modifier,
    conteudoExpandido: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 4.dp else 0.dp,
        animationSpec = tween(durationMillis = 200)
    )

    val containerColor by animateColorAsState(
        targetValue = if (isHovered) Branco else CardFundoCinza,
        animationSpec = tween(durationMillis = 200)
    )

    val borderColor by animateColorAsState(
        targetValue = if (isHovered) CinzaBordaCampo else CardBordaCinza,
        animationSpec = tween(durationMillis = 200)
    )

    val rotacaoSeta by animateFloatAsState(
        targetValue = if (isExpandido) 180f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        modifier = modifier
            .fillMaxWidth()
            .hoverable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClickToggle() }
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
                    Text(
                        text = tituloTopo,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tituloPrincipal,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrincipal
                    )
                }

                if (isExpandido && mostrarAcoesTopo) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onEditarClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.edit_square_24px),
                                contentDescription = "Editar",
                                tint = VerdeClinica,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = onExcluirClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.delete_24px),
                                contentDescription = "Excluir",
                                tint = TextoErro,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.keyboard_arrow_down),
                        contentDescription = if (isExpandido) "Recolher" else "Expandir",
                        tint = TextoPrincipal,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotacaoSeta)
                    )
                }
            }

            if (isExpandido) {
                Spacer(modifier = Modifier.height(16.dp))
                conteudoExpandido()
            }
        }
    }
}