package edu.front.psi.clin.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.theme.CoresApp

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
    ) {
        // 1. Cabeçalho: Título + Botão Novo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CoresApp.Primaria
            )

            Button(
                onClick = onNovoClick,
                colors = ButtonDefaults.buttonColors(containerColor = CoresApp.Primaria),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = textoBotaoNovo,
                    tint = CoresApp.Branco,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = textoBotaoNovo,
                    color = CoresApp.Branco,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // 2. Barra de Pesquisa
        BarraPesquisa(
            text = textoFiltro,
            onTextChange = onFiltroChange,
            placeholderText = placeholderPesquisa
        )

        // 3. Tabela
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CoresApp.Branco),
            border = androidx.compose.foundation.BorderStroke(1.dp, CoresApp.BordaContainer)
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
                            color = CoresApp.TextoCabecalho,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(peso)
                        )
                    }
                    Spacer(modifier = Modifier.width(larguraAcoes))
                }

                HorizontalDivider(color = CoresApp.LinhaDivisoria, thickness = 1.dp)

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

                        // Ações (Olho + Editar + Excluir)
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
                                        imageVector = Icons.Outlined.Visibility,
                                        contentDescription = "Visualizar",
                                        tint = CoresApp.TextoCabecalho,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { onEditar(item) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Editar",
                                    tint = CoresApp.TextoCabecalho,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(
                                onClick = { onApagar(item) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Remover",
                                    tint = CoresApp.Erro,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    if (index < itens.size - 1) {
                        HorizontalDivider(color = CoresApp.LinhaDivisoria, thickness = 1.dp)
                    }
                }
            }
        }
    }
}