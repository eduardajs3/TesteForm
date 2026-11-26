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
import edu.front.psi.clin.utils.formatarDataBrasileiraParaIso
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

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
                    val pacienteIdAtual = pacienteId.valor.value.toLongOrNull()
                    if (
                        pacienteIdAtual != null &&
                        pacientesAtualizados.none {
                            it.id == pacienteIdAtual && it.ativo
                        }
                    ) {
                        pacienteId.limpar()
                    }

                    val convenioIdAtual = convenioId.valor.value.toLongOrNull()
                    if (
                        convenioIdAtual != null &&
                        conveniosAtualizados.none {
                            it.id == convenioIdAtual && it.ativo
                        }
                    ) {
                        convenioId.limpar()
                    }

                    val agendaSelecionadaId = agendaId.valor.value.toLongOrNull()
                    if (
                        agendaSelecionadaId != null &&
                        (
                            agendasAtualizadas.none {
                                it.id == agendaSelecionadaId && it.ativo
                            } ||
                                pagamentosAtualizados.any { it.agendaId == agendaSelecionadaId }
                            )
                    ) {
                        agendaId.limpar()
                    }
                }
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
    val agendaId = CampoValidado()
    val pacienteId = CampoValidado()
    val convenioId = CampoValidado()

    val valorTotal = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val valorConvenio = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val valorPaciente = CampoValidado(formatador = { MaskUtils.aplicarMascaraMoeda(it) })
    val formaPagamento = CampoValidado()
    val dataPagamento = CampoValidado()
    val status = CampoValidado(PagamentoStatus.PENDENTE.name)

    fun salvar() {
        if (salvando) return

        val vAgenda = agendaId.validar()
        val vPaciente = pacienteId.validar()
        val vConvenio = convenioId.validar()
        val v1 = valorTotal.validar()
        val v2 = valorConvenio.validar()
        val v3 = valorPaciente.validar()
        val v4 = formaPagamento.validar()
        val v5 = dataPagamento.validar()
        val v6 = status.validar()

        val agendaSelecionada = agendaId.valor.value.toLongOrNull()
        val pacienteSelecionado = pacienteId.valor.value.toLongOrNull()
        val convenioSelecionado = convenioId.valor.value.toLongOrNull()

        if (!(vAgenda && vPaciente && vConvenio && v1 && v2 && v3 && v4 && v5 && v6)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        if (agendaSelecionada == null || pacienteSelecionado == null || convenioSelecionado == null) {
            mandarMensagem("Selecione Agenda, Paciente e Convênio válidos.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val idAtual = id.value.toLongOrNull()
                val pagamento = Pagamento(
                    id = idAtual,
                    agendaId = agendaSelecionada,
                    pacienteId = pacienteSelecionado,
                    convenioId = convenioSelecionado,
                    valorTotal = converterMoedaParaDouble(valorTotal.valor.value),
                    valorConvenio = converterMoedaParaDouble(valorConvenio.valor.value),
                    valorPaciente = converterMoedaParaDouble(valorPaciente.valor.value),
                    formaPagamento = formaPagamento.valor.value.trim(),
                    dataPagamento = formatarDataBrasileiraParaIso(dataPagamento.valor.value.trim()),
                    status = PagamentoStatus.deValorApi(status.valor.value)
                )

                if (id.value.isEmpty()) {
                    pagamentoApi.salvar(pagamento)
                } else {
                    pagamentoApi.atualizar(idAtual!!, pagamento)
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
        agendaId.definir(pagamento.agendaId.toString())
        pacienteId.definir(pagamento.pacienteId.toString())
        convenioId.definir(pagamento.convenioId.toString())

        valorTotal.definir(valorParaCentavos(pagamento.valorTotal))
        valorConvenio.definir(valorParaCentavos(pagamento.valorConvenio))
        valorPaciente.definir(valorParaCentavos(pagamento.valorPaciente))
        formaPagamento.definir(pagamento.formaPagamento)
        dataPagamento.definir(formatarDataIsoParaBrasileira(pagamento.dataPagamento))
        status.definir(pagamento.status.name)
    }

    private fun valorParaCentavos(valor: Double): String =
        (valor * 100.0).roundToLong().toString()

    private fun converterMoedaParaDouble(valorStr: String): Double {
        val apenasDigitos = valorStr.filter { it.isDigit() }
        if (apenasDigitos.isEmpty()) return 0.0
        return apenasDigitos.toDouble() / 100.0
    }

    fun limparCampos() {
        agendaId.limpar()
        pacienteId.limpar()
        convenioId.limpar()
        valorTotal.limpar()
        valorConvenio.limpar()
        valorPaciente.limpar()
        formaPagamento.limpar()
        dataPagamento.limpar()
        status.definir(PagamentoStatus.PENDENTE.name)
    }
}