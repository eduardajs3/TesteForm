package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.*
import edu.front.psi.clin.model.*
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PagamentoViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val pagamentoApi = PagamentoAPI(httpClient)
    private val pacienteApi = PacienteAPI(httpClient)
    private val convenioApi = ConvenioAPI(httpClient)
    private val agendaApi = AgendaAPI(httpClient)

    private val _pagamentos = MutableStateFlow<List<Pagamento>>(emptyList())
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    // Listas auxiliares para preencher os dropdowns
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes = _pacientes.asStateFlow()

    private val _convenios = MutableStateFlow<List<Convenio>>(emptyList())
    val convenios = _convenios.asStateFlow()

    private val _agendas = MutableStateFlow<List<Agenda>>(emptyList())
    val agendas = _agendas.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _pagamentos.value = pagamentoApi.carregar()
                _pacientes.value = pacienteApi.carregar()
                _convenios.value = convenioApi.carregar()
                _agendas.value = agendaApi.carregar()
                println("Dados de Pagamento e dependências carregados com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar pagamentos: ${e.message}")
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val pagamentosFiltrados = combine(_pagamentos, _filtro) { pagamentos, filtro ->
        if (filtro.isEmpty()) {
            pagamentos
        } else {
            pagamentos.filter { 
                it.formaPagamento.contains(filtro, ignoreCase = true) || 
                it.status.contains(filtro, ignoreCase = true) 
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<PagamentoRotas>(PagamentoRotas.Listagem)

    val id = mutableStateOf("")
    val agendaId = mutableStateOf<Long?>(null)
    val pacienteId = mutableStateOf<Long?>(null)
    val convenioId = mutableStateOf<Long?>(null)

    val valorTotal = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val valorConvenio = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val valorPaciente = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val formaPagamento = CampoValidado()
    val dataPagamento = CampoValidado()
    val status = CampoValidado("PENDENTE") 
    val mensagem = mutableStateOf("")

    fun salvar() {
        val v1 = valorTotal.validar()
        val v2 = valorConvenio.validar()
        val v3 = valorPaciente.validar()
        val v4 = formaPagamento.validar()
        val v5 = dataPagamento.validar()

        if (agendaId.value == null || pacienteId.value == null || convenioId.value == null) {
            mensagem.value = "Selecione Agenda, Paciente e Convênio."
            mandarMensagem("Selecione Agenda, Paciente e Convênio.")
            return
        }

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        try {
            fun converterMoedaParaDouble(valorStr: String): Double {
                val apenasDigitos = valorStr.filter { it.isDigit() }
                if (apenasDigitos.isEmpty()) return 0.0
                return apenasDigitos.toDouble() / 100.0
            }

            val dataFormatada = converterDataParaIso(dataPagamento.valor.value)

            val pagamento = Pagamento(
                id = if (id.value.isEmpty()) null else id.value.toLong(),
                agendaId = agendaId.value!!,
                pacienteId = pacienteId.value!!,
                convenioId = convenioId.value!!,
                valorTotal = converterMoedaParaDouble(valorTotal.valor.value),
                valorConvenio = converterMoedaParaDouble(valorConvenio.valor.value),
                valorPaciente = converterMoedaParaDouble(valorPaciente.valor.value),
                formaPagamento = formaPagamento.valor.value,
                dataPagamento = dataFormatada,
                status = status.valor.value.ifEmpty { "PENDENTE" }
            )

            viewModelScope.launch {
                try {
                    if (id.value.isEmpty()) {
                        pagamentoApi.salvar(pagamento)
                    } else {
                        pagamentoApi.atualizar(id.value.toLong(), pagamento)
                    }
                    _pagamentos.value = pagamentoApi.carregar()
                    mensagem.value = "Pagamento salvo com sucesso!"
                    mandarMensagem("Pagamento salvo com sucesso!")
                    limparCampos()

                    backStack.clear()
                    backStack.add(PagamentoRotas.Listagem)
                } catch (e: Exception) {
                    mensagem.value = "Erro ao salvar: ${e.message}"
                    mandarMensagem("Erro ao salvar: ${e.message}")
                }
            }
        } catch (e: Exception) {
            mensagem.value = "Erro ao processar valores: ${e.message}"
            mandarMensagem("Erro ao processar valores: ${e.message}")
        }
    }

    private fun converterDataParaIso(dataStr: String): String {
        return try {
            if (dataStr.contains("/")) {
                val partes = dataStr.split("/")
                if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}T00:00:00" else dataStr
            } else {
                dataStr
            }
        } catch (e: Exception) {
            dataStr
        }
    }

    fun apagar(pagamento: Pagamento) {
        viewModelScope.launch {
            try {
                pagamentoApi.apagar(pagamento)
                _pagamentos.value = pagamentoApi.carregar()
                mandarMensagem("Pagamento apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
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
        id.value = pagamento.id?.toString() ?: ""
        agendaId.value = pagamento.agendaId
        pacienteId.value = pagamento.pacienteId
        convenioId.value = pagamento.convenioId
        
        valorTotal.definir(pagamento.valorTotal.toString())
        valorConvenio.definir(pagamento.valorConvenio.toString())
        valorPaciente.definir(pagamento.valorPaciente.toString())
        formaPagamento.definir(pagamento.formaPagamento)
        
        try {
            val partes = pagamento.dataPagamento.split("T")[0].split("-")
            if (partes.size == 3) {
                dataPagamento.definir("${partes[2]}/${partes[1]}/${partes[0]}")
            } else {
                dataPagamento.definir(pagamento.dataPagamento)
            }
        } catch (e: Exception) {
            dataPagamento.definir(pagamento.dataPagamento)
        }

        status.definir(pagamento.status)
        mensagem.value = ""
    }

    fun limparCampos() {
        agendaId.value = null
        pacienteId.value = null
        convenioId.value = null
        valorTotal.limpar()
        valorConvenio.limpar()
        valorPaciente.limpar()
        formaPagamento.limpar()
        dataPagamento.limpar()
        status.definir("PENDENTE")
        mensagem.value = "Campos limpos."
    }
}