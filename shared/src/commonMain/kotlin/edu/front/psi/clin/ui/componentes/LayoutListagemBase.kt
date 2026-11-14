package edu.front.psi.clin.ui.componentes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.CardBordaCinza
import edu.front.psi.clin.ui.theme.LinhaDivisoria
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.TextoSecundario
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

@Composable
fun <T> LayoutListagemBase(
    titulo: String,
    textoBotaoNovo: String,
    textoFiltro: String,
    onFiltroChange: (String) -> Unit,
    onNovoClick: () -> Unit,
    colunas: List<Pair<String, Float>>,
    itens: List<T>,
    onVisualizar: ((T) -> Unit)? = null,
    onEditar: (T) -> Unit,
    onApagar: (T) -> Unit,
    placeholderPesquisa: String = "Buscar...",
    textoCompacto: (T) -> String,
    linhaContent: @Composable RowScope.(T) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val telaPequena = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            CabecalhoTela(
                titulo = titulo,
                textoBotao = textoBotaoNovo,
                iconeBotao = painterResource(Res.drawable.add),
                onClickBotao = onNovoClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            BarraPesquisa(
                text = textoFiltro,
                onTextChange = onFiltroChange,
                placeholderText = placeholderPesquisa
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Branco),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBordaCinza)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val larguraAcoes = if (onVisualizar != null) 108.dp else 72.dp

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (telaPequena) {
                            Text(
                                text = colunas.firstOrNull()?.first.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoSecundario,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            colunas.forEach { (nome, peso) ->
                                Text(
                                    text = nome,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoSecundario,
                                    modifier = Modifier.weight(peso)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(larguraAcoes))
                    }

                    HorizontalDivider(color = LinhaDivisoria, thickness = 1.dp)

                    if (itens.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Não há registros cadastrados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoSecundario,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        itens.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (telaPequena) {
                                    Text(
                                        text = textoCompacto(item),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    linhaContent(item)
                                }

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.width(larguraAcoes)
                                ) {
                                    if (onVisualizar != null) {
                                        IconButton(
                                            onClick = { onVisualizar(item) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(Res.drawable.visibility),
                                                contentDescription = "Visualizar",
                                                tint = TextoSecundario,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { onEditar(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.edit_square_24px),
                                            contentDescription = "Editar",
                                            tint = TextoSecundario,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { onApagar(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.delete_24px),
                                            contentDescription = "Remover",
                                            tint = TextoErro,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            if (index < itens.size - 1) {
                                HorizontalDivider(color = LinhaDivisoria, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}