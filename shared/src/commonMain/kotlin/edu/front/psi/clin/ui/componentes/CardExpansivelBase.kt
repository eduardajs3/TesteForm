package edu.front.psi.clin.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.CardFundoCinza
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.TextoPrincipal
import edu.front.psi.clin.ui.theme.TextoSecundario
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun CardExpansivelBase(
    tituloTopo: String,
    tituloPrincipal: String,
    isExpandido: Boolean,
    onClickToggle: () -> Unit,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit,
    conteudoExpandido: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
        border = BorderStroke(1.dp, CardBordaCinza),
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

                if (isExpandido) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = onEditarClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.edit_square_24px),
                                contentDescription = "Editar",
                                tint = TextoSecundario,
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
                                tint = TextoErro,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.keyboard_arrow_down),
                        contentDescription = "Expandir",
                        tint = TextoPrincipal,
                        modifier = Modifier.size(24.dp)
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