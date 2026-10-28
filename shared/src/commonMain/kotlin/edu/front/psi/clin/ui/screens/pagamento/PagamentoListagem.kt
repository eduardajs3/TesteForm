package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.PagamentoViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun PagamentoListagem(
    innerPadding: PaddingValues,
    vm: PagamentoViewModel
) {
    val lista = vm.pagamentosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    // Configuração das colunas e seus pesos de largura
    val colunas = listOf(
        "Paciente" to 1.5f,
        "Valor Total" to 1.2f,
        "Data" to 1.2f,
        "Status" to 1.2f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Pagamentos",
            textoBotaoNovo = "Novo Pagamento",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoPagamento,
            colunas = colunas,
            itens = lista.value,
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por paciente, status ou data..."
        ) { pagamento ->
            Text(
                text = pagamento.paciente,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "R$ ${pagamento.valorTotal}",
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = pagamento.dataPagamento,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Badge de Status financeiro com cores apropriadas
            Box(modifier = Modifier.weight(1.2f)) {
                val isPago = pagamento.status

                val corFundo = when {
                    isPago -> Color(0xFFA2C99B)
                    else -> Color(0xFFFEF3C7)
                }

                val corTexto = when {
                    isPago -> Color(0xFF2E5927)
                    else -> Color(0xFF92400E)
                }

                Box(
                    modifier = Modifier
                        .background(color = corFundo, shape = RoundedCornerShape(50.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPago) "Pago" else "Pendente",
                        color = corTexto,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}