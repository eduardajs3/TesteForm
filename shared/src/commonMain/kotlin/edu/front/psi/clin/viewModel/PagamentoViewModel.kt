package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Pagamento // Ajuste para o pacote correto do seu Model
import kotlinx.coroutines.flow.*

class PagamentoViewModel : ViewModel() {

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

    val tela = mutableStateOf("list") // "list" ou "form"

    val id = mutableStateOf("")
    val paciente = mutableStateOf("")
    val convenio = mutableStateOf("")
    val valorTotal = mutableStateOf("")
    val valorConvenio = mutableStateOf("")
    val valorPaciente = mutableStateOf("")
    val formaPagamento = mutableStateOf("")
    val dataPagamento = mutableStateOf("")
    val status = mutableStateOf("")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val pagamento = Pagamento(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                paciente = paciente.value,
                convenio = convenio.value,
                valorTotal = valorTotal.value.toDoubleOrNull() ?: 0.0,
                valorConvenio = valorConvenio.value.toDoubleOrNull() ?: 0.0,
                valorPaciente = valorPaciente.value.toDoubleOrNull() ?: 0.0,
                formaPagamento = formaPagamento.value,
                dataPagamento = dataPagamento.value,
                status = status.value.toBooleanStrictOrNull()
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
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(pagamento: Pagamento) {
        _pagamentos.update { lista -> lista - pagamento }
    }

    fun novoPagamento() {
        tela.value = "form"
        id.value = ""
        paciente.value = ""
        convenio.value = ""
        valorTotal.value = ""
        valorConvenio.value = ""
        valorPaciente.value = ""
        formaPagamento.value = ""
        dataPagamento.value = ""
        status.value = ""
        mensagem.value = ""
    }

    fun editar(pagamento: Pagamento) {
        tela.value = "form"
        id.value = pagamento.id.toString()
        paciente.value = pagamento.paciente
        convenio.value = pagamento.convenio
        valorTotal.value = pagamento.valorTotal.toString()
        valorConvenio.value = pagamento.valorConvenio.toString()
        valorPaciente.value = pagamento.valorPaciente.toString()
        formaPagamento.value = pagamento.formaPagamento
        dataPagamento.value = pagamento.dataPagamento
        status.value = pagamento.status.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        paciente.value = ""
        convenio.value = ""
        valorTotal.value = ""
        valorConvenio.value = ""
        valorPaciente.value = ""
        formaPagamento.value = ""
        dataPagamento.value = ""
        status.value = ""
        mensagem.value = "Campos limpos."
    }
}