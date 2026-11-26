package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.*
import edu.front.psi.clin.model.*
import edu.front.psi.clin.model.enums.PagamentoStatus
import edu.front.psi.clin.navigation.PagamentoRotas
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlinx.coroutines.Job
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

    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes = _pacientes.asStateFlow()

    private val _convenios = MutableStateFlow<List<Convenio>>(emptyList())
    val convenios = _convenios.asStateFlow()

    private val _agendas = MutableStateFlow<List<Agenda>>(emptyList())
    val agendas = _agendas.asStateFlow()
    private var carregamentoDadosFormulario: Job? = null

    var salvando by mutableStateOf(false)
        private set

    val agendasSemPagamento = combine(_agendas, _pagamentos) { agendas, pagamentos ->
        val agendasComPagamento = pagamentos.mapTo(mutableSetOf()) { it.agendaId }
        agendas.filter { it.id != null && it.id !in agendasComPagamento }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        carregarDadosFormulario()
    }

    fun carregarDadosFormulario() {
        if (!PermissoesAcesso.podeAcessar(SessionManager.role, RecursoSistema.PAGAMENTOS)) return
        if (carregamentoDadosFormulario?.isActive == true) return

        carregamentoDadosFormulario = viewModelScope.launch {
            try {
                val pagamentosAtualizados = pagamentoApi.carregar()
                val pacientesAtualizados = pacienteApi.carregar()
                val conveniosAtualizados = convenioApi.carregar()
                val agendasAtualizadas = agendaApi.carregar()

                _pagamentos.value = pagamentosAtualizados
                _pacientes.value = pacientesAtualizados
                _convenios.value = conveniosAtualizados
                _agendas.value = agendasAtualizadas

                if (id.value.isEmpty()) {
                    if (
                        pacienteId.value != null &&
                        pacientesAtualizados.none {
                            it.id == pacienteId.value && it.ativo
                        }
                    ) {
                        pacienteId.value = null
                    }
                    if (
                        convenioId.value != null &&
                        conveniosAtualizados.none {
                            it.id == convenioId.value && it.ativo
                        }
                    ) {
                        convenioId.value = null
                    }
                    val agendaSelecionadaId = agendaId.value
                    if (
                        agendaSelecionadaId != null &&
                        (
                            agendasAtualizadas.none {
                                it.id == agendaSelecionadaId && it.ativo
                            } ||
                                pagamentosAtualizados.any { it.agendaId == agendaSelecionadaId }
                            )
                    ) {
                        agendaId.value = null
                    }
                }

                println("Dados de Pagamento e dependências carregados com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar pagamentos"))
            } finally {
                carregamentoDadosFormulario = null
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
                    it.status.rotulo.contains(filtro, ignoreCase = true) ||
                    it.status.name.contains(filtro, ignoreCase = true)
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
    val status = CampoValidado(PagamentoStatus.PENDENTE.name)

    fun salvar() {
        if (salvando) return

        if (agendaId.value == null || pacienteId.value == null || convenioId.value == null) {
            mandarMensagem("Selecione Agenda, Paciente e Convênio.")
            return
        }

        val v1 = valorTotal.validar()
        val v2 = valorConvenio.validar()
        val v3 = valorPaciente.validar()
        val v4 = formaPagamento.validar()
        val v5 = dataPagamento.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        try {
            fun converterMoedaParaDouble(valorStr: String): Double {
                val apenasDigitos = valorStr.filter { it.isDigit() }
                if (apenasDigitos.isEmpty()) return 0.0
                return apenasDigitos.toDouble() / 100.0
            }

            val pagamento = Pagamento(
                id = if (id.value.isEmpty()) null else id.value.toLong(),
                agendaId = agendaId.value!!,
                pacienteId = pacienteId.value!!,
                convenioId = convenioId.value!!,
                valorTotal = converterMoedaParaDouble(valorTotal.valor.value),
                valorConvenio = converterMoedaParaDouble(valorConvenio.valor.value),
                valorPaciente = converterMoedaParaDouble(valorPaciente.valor.value),
                formaPagamento = formaPagamento.valor.value,
                dataPagamento = dataPagamento.valor.value,
                status = PagamentoStatus.deValorApi(status.valor.value)
            )

            viewModelScope.launch {
                salvando = true
                try {
                    if (id.value.isEmpty()) {
                        pagamentoApi.salvar(pagamento)
                    } else {
                        pagamentoApi.atualizar(id.value.toLong(), pagamento)
                    }
                    _pagamentos.value = pagamentoApi.carregar()

                    val mensagemSucesso = if (id.value.isEmpty()) {
                        "Pagamento salvo com sucesso!"
                    } else {
                        "Pagamento atualizado com sucesso!"
                    }

                    mandarMensagem(mensagemSucesso)
                    limparCampos()

                    backStack.clear()
                    backStack.add(PagamentoRotas.Listagem)
                } catch (e: Exception) {
                    mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
                } finally {
                    salvando = false
                }
            }
        } catch (e: Exception) {
            mandarMensagem("Erro ao processar valores: ${e.message}")
        }
    }

    fun apagar(pagamento: Pagamento) {
        viewModelScope.launch {
            try {
                pagamentoApi.apagar(pagamento)
                _pagamentos.value = pagamentoApi.carregar()
                mandarMensagem("Pagamento apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
            }
        }
    }

    fun novoPagamento() {
        backStack.clear()
        backStack.add(PagamentoRotas.Formulario)
        id.value = ""
        limparCampos()
    }

    fun editar(pagamento: Pagamento) {
        backStack.clear()
        backStack.add(PagamentoRotas.Formulario)
        id.value = pagamento.id?.toString() ?: ""
        agendaId.value = pagamento.agendaId
        pacienteId.value = pagamento.pacienteId
        convenioId.value = pagamento.convenioId

        valorTotal.definir(valorParaCentavos(pagamento.valorTotal))
        valorConvenio.definir(valorParaCentavos(pagamento.valorConvenio))
        valorPaciente.definir(valorParaCentavos(pagamento.valorPaciente))
        formaPagamento.definir(pagamento.formaPagamento)

        dataPagamento.definir(formatarDataIsoParaBrasileira(pagamento.dataPagamento))

        status.definir(pagamento.status.name)
    }

    private fun valorParaCentavos(valor: Double): String =
        (valor * 100.0).toLong().toString()

    fun limparCampos() {
        agendaId.value = null
        pacienteId.value = null
        convenioId.value = null
        valorTotal.limpar()
        valorConvenio.limpar()
        valorPaciente.limpar()
        formaPagamento.limpar()
        dataPagamento.limpar()
        status.definir(PagamentoStatus.PENDENTE.name)
    }
}