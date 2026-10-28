package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.components.LayoutListagemBase
import edu.front.psi.clin.ui.viewmodel.ConvenioViewModel

private val CorTextoLinha = Color(0xFF1F2937)

@Composable
fun ConvenioListagem(
    innerPadding: PaddingValues,
    vm: ConvenioViewModel
) {
    val lista = vm.conveniosFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()

    // Mapeamento das colunas da tabela e suas proporções de largura
    val colunas = listOf(
        "Nome do Convênio" to 1.8f,
        "CNPJ" to 1.4f,
        "Cobertura" to 1.0f
    )

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutListagemBase(
            titulo = "Convênios",
            textoBotaoNovo = "Novo Convênio",
            textoFiltro = filtro.value,
            onFiltroChange = vm::atualizarFiltro,
            onNovoClick = vm::novoConvenio,
            colunas = colunas,
            itens = lista.value,
            onEditar = vm::editar,
            onApagar = vm::apagar,
            placeholderPesquisa = "Buscar por nome ou CNPJ..."
        ) { convenio ->
            // Mapeamento das propriedades do objeto Convenio para as colunas
            Text(
                text = convenio.nome,
                fontWeight = FontWeight.Bold,
                color = CorTextoLinha,
                fontSize = 14.sp,
                modifier = Modifier.weight(1.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = convenio.cnpj,
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.4f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${convenio.percentualCobertura}%",
                color = CorTextoLinha,
                fontSize = 13.sp,
                modifier = Modifier.weight(1.0f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}