package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.Pagamento
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.ui.components.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*

class PagamentoViewModel(
    private val mandarMensagem: (String, SnackbarDuration) -> Unit
) : ViewModel() {

    private var idCounter: Int = 0
    private val _pagamentos = MutableStateFlow<List<Pagamento>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val pagamentosFiltrados = combine(_pagamentos, _filtro) { pagamentos, filtro ->
        if (filtro.isEmpty()) {
            pagamentos
        } else {
            pagamentos.filter { it.paciente.contains(filtro, ignoreCase = true) || it.convenio.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<PagamentoRotas>(
        PagamentoRotas.Listagem
    )

    val id = mutableStateOf("")
    
    // Campos geridos pelo validador (valores com máscara de moeda aplicada automaticamente)
    val paciente = CampoValidado()
    val convenio = CampoValidado()
    val valorTotal = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val valorConvenio = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val valorPaciente = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val formaPagamento = CampoValidado()
    val dataPagamento = CampoValidado()
    val status = CampoValidado()
    val mensagem = mutableStateOf("")

    fun salvar() {
        val v1 = paciente.validar()
        val v2 = convenio.validar()
        val v3 = valorTotal.validar()
        val v4 = valorConvenio.validar()
        val v5 = valorPaciente.validar()
        val v6 = formaPagamento.validar()
        val v7 = dataPagamento.validar()
        val v8 = status.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.", SnackbarDuration.Short)
            return
        }

        try {
            // Função auxiliar para converter o formato monetário "R$ 1.234,56" de volta para Double
            fun converterMoedaParaDouble(valorStr: String): Double {
                val apenasDigitos = valorStr.filter { it.isDigit() }
                if (apenasDigitos.isEmpty()) return 0.0
                return apenasDigitos.toDouble() / 100.0
            }

            val pagamento = Pagamento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                paciente = paciente.valor.value,
                convenio = convenio.valor.value,
                valorTotal = converterMoedaParaDouble(valorTotal.valor.value),
                valorConvenio = converterMoedaParaDouble(valorConvenio.valor.value),
                valorPaciente = converterMoedaParaDouble(valorPaciente.valor.value),
                formaPagamento = formaPagamento.valor.value,
                dataPagamento = dataPagamento.valor.value,
                status = status.valor.value.toBooleanStrictOrNull()
                    ?: throw IllegalArgumentException("Status deve ser true ou false.")
            )

            if (id.value.isEmpty()) {
                idCounter++
                _pagamentos.update { lista -> lista + pagamento.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _pagamentos.update { lista ->
                    lista.map { p -> if (p.id == idNumber) pagamento.copy(id = idNumber) else p }
                }
            }
            mensagem.value = "Pagamento salvo com sucesso!"
            mandarMensagem("Pagamento salvo com sucesso!", SnackbarDuration.Short)
            limparCampos()
            
            backStack.clear()
            backStack.add(PagamentoRotas.Listagem)

        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
            mandarMensagem("Erro ao salvar: ${e.message}", SnackbarDuration.Long)
        }
    }

    fun apagar(pagamento: Pagamento) {
        _pagamentos.update { lista -> lista - pagamento }
        mandarMensagem("Pagamento apagado com sucesso!", SnackbarDuration.Short)
    }

    fun novoPagamento() {
        backStack.clear()
        backStack.add(PagamentoRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(pagamento: Pagamento) {
        backStack.clear()
        backStack.add(PagamentoRotas.Formulario)
        id.value = pagamento.id.toString()
        paciente.definir(pagamento.paciente)
        convenio.definir(pagamento.convenio)
        
        // Atribui diretamente convertendo o valor numérico para string
        valorTotal.definir(pagamento.valorTotal.toString())
        valorConvenio.definir(pagamento.valorConvenio.toString())
        valorPaciente.definir(pagamento.valorPaciente.toString())
        
        formaPagamento.definir(pagamento.formaPagamento)
        dataPagamento.definir(pagamento.dataPagamento)
        status.definir(pagamento.status.toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        paciente.limpar()
        convenio.limpar()
        valorTotal.limpar()
        valorConvenio.limpar()
        valorPaciente.limpar()
        formaPagamento.limpar()
        dataPagamento.limpar()
        status.limpar()
        mensagem.value = "Campos limpos."
    }
}