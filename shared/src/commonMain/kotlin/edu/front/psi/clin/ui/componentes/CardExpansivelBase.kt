package edu.front.psi.clin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

val CorTextoPrincipalGlobal = Color(0xFF1F2937)
val CorCardFundoGlobal = Color(0xFFF9FAFB)
val CorBordaCardGlobal = Color(0xFFE5E7EB)

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
        colors = CardDefaults.cardColors(containerColor = CorCardFundoGlobal),
        border = BorderStroke(1.dp, CorBordaCardGlobal),
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
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tituloPrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CorTextoPrincipalGlobal
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
                        painter = painterResource(Res.drawable.keyboard_arrow_down),
                        contentDescription = "Expandir",
                        tint = CorTextoPrincipalGlobal,
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