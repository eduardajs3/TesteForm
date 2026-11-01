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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.components.CabecalhoTela
import org.jetbrains.compose.resources.painterResource
import frontpsiclinic.shared.generated.resources.*

val CorBordaContainer = Color(0xFFE5E7EB)
val CorLinhaDivisoria = Color(0xFFF3F4F6)
val CorTextoCabecalho = Color(0xFF9CA3AF)

@Composable
fun <T> LayoutListagemBase(
    titulo: String,
    textoBotaoNovo: String,
    textoFiltro: String,
    onFiltroChange: (String) -> Unit,
    onNovoClick: () -> Unit,
    colunas: List<Pair<String, Float>>,
    itens: List<T>,
    onVisualizar: ((T) -> Unit)? = null, // Callback opcional para o olho
    onEditar: (T) -> Unit,
    onApagar: (T) -> Unit,
    placeholderPesquisa: String = "Buscar...",
    linhaContent: @Composable RowScope.(T) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 1. Cabeçalho Padronizado (CabecalhoTela)
        CabecalhoTela(
            titulo = titulo,
            textoBotao = textoBotaoNovo,
            iconeBotao = painterResource(Res.drawable.add),
            onClickBotao = onNovoClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Barra de Pesquisa
        BarraPesquisa(
            text = textoFiltro,
            onTextChange = onFiltroChange,
            placeholderText = placeholderPesquisa
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Tabela
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CorBordaContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Largura ajustada da coluna de ações conforme a presença do botão visualizar
                val larguraAcoes = if (onVisualizar != null) 108.dp else 72.dp

                // Cabeçalho da Tabela
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colunas.forEach { (nome, peso) ->
                        Text(
                            text = nome,
                            fontWeight = FontWeight.Bold,
                            color = CorTextoCabecalho,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(peso)
                        )
                    }
                    Spacer(modifier = Modifier.width(larguraAcoes))
                }

                HorizontalDivider(color = CorLinhaDivisoria, thickness = 1.dp)

                // Verifica se a lista está vazia para exibir a mensagem informativa
                if (itens.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Não há registros cadastrados.",
                            color = CorTextoCabecalho,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Linhas de dados
                    itens.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Renderização das colunas customizadas
                            linhaContent(item)

                            // Ações (Visualizar + Editar + Excluir) com ícones XML
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
                                            painter = painterResource(Res.drawable.badge_24px),
                                            contentDescription = "Visualizar",
                                            tint = CorTextoCabecalho,
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
                                        tint = CorTextoCabecalho,
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
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        if (index < itens.size - 1) {
                            HorizontalDivider(color = CorLinhaDivisoria, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}