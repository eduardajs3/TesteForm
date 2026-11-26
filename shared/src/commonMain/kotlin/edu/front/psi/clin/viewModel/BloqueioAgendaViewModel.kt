package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.BloqueioAgendaAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.BloqueioAgenda
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import edu.front.psi.clin.utils.formatarDataBrasileiraParaIso
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class BloqueioAgendaViewModel(
    private val mandarMensagem: (String) -> Unit,
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val bloqueioApi = BloqueioAgendaAPI(httpClient)
    private val psicologoApi = PsicologoAPI(httpClient)

    private val _bloqueios = MutableStateFlow<List<BloqueioAgenda>>(emptyList())
    private val _psicologos = MutableStateFlow<List<Psicologo>>(emptyList())
    val psicologos = _psicologos.asStateFlow()

    var salvando by mutableStateOf(false)
        private set

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()
    private var carregamentoDadosFormulario: Job? = null

    init {
        carregarDadosFormulario()
    }

    fun carregarDadosFormulario() {
        if (carregamentoDadosFormulario?.isActive == true) return

        carregamentoDadosFormulario = viewModelScope.launch {
            try {
                val bloqueiosAtualizados = bloqueioApi.carregar()
                val psicologosAtualizados = psicologoApi.carregar()

                _bloqueios.value = bloqueiosAtualizados
                _psicologos.value = psicologosAtualizados

                val psicologoSelecionadoId = psicologo.valor.value.toLongOrNull()
                if (
                    id.value.isEmpty() &&
                    psicologoSelecionadoId != null &&
                    psicologosAtualizados.none {
                        it.id == psicologoSelecionadoId && it.ativo
                    }
                ) {
                    psicologo.limpar()
                }
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar bloqueios de agenda"))
            } finally {
                carregamentoDadosFormulario = null
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val bloqueiosFiltrados = combine(_bloqueios, _filtro, _psicologos) { bloqueios, filtro, psicologos ->
        if (filtro.isEmpty()) {
            bloqueios
        } else {
            bloqueios.filter { bloqueio ->
                nomePsicologo(bloqueio.psicologoId, psicologos).contains(filtro, ignoreCase = true) ||
                    bloqueio.motivo.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<BloqueioAgendaRotas>(
        BloqueioAgendaRotas.Listagem
    )

    val id = mutableStateOf("")

    val psicologo = CampoValidado()
    val dataInicio = CampoValidado()
    val horaInicio = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val dataFim = CampoValidado()
    val horaFim = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val motivo = CampoValidado(
        validadorExtra = { it.length <= 500 },
        mensagemErroPersonalizada = "O motivo deve ter no máximo 500 caracteres"
    )
    val ativo = CampoValidado("true")

    fun nomePsicologo(psicologoId: Long): String =
        nomePsicologo(psicologoId, _psicologos.value)

    fun salvar() {
        if (salvando) return

        val v1 = psicologo.validar()
        val v2 = dataInicio.validar()
        val v3 = horaInicio.validar()
        val v4 = dataFim.validar()
        val v5 = horaFim.validar()
        val v6 = motivo.validar()
        val v7 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        val psicologoId = psicologo.valor.value.toLongOrNull()
        val inicioDateTime = montarDataHoraIso(dataInicio.valor.value, horaInicio.valor.value)
        val fimDateTime = montarDataHoraIso(dataFim.valor.value, horaFim.valor.value)

        if (psicologoId == null || inicioDateTime == null || fimDateTime == null) {
            mandarMensagem("Informe psicólogo, datas e horários válidos.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val idAtual = id.value.toLongOrNull()
                val bloqueio = BloqueioAgenda(
                    id = idAtual,
                    psicologoId = psicologoId,
                    dataHoraInicio = inicioDateTime.toString(),
                    dataHoraFim = fimDateTime.toString(),
                    motivo = motivo.valor.value.trim(),
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    bloqueioApi.salvar(bloqueio)
                } else {
                    bloqueioApi.atualizar(idAtual!!, bloqueio)
                }

                _bloqueios.value = bloqueioApi.carregar()
                _psicologos.value = psicologoApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Bloqueio de agenda salvo com sucesso!"
                } else {
                    "Bloqueio de agenda atualizado com sucesso!"
                }

                mandarMensagem(mensagemSucesso)
                limparCampos()
                backStack.clear()
                backStack.add(BloqueioAgendaRotas.Listagem)
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
            } finally {
                salvando = false
            }
        }
    }

    fun apagar(bloqueio: BloqueioAgenda) {
        viewModelScope.launch {
            try {
                bloqueioApi.apagar(bloqueio)
                _bloqueios.value = bloqueioApi.carregar()
                mandarMensagem("Bloqueio apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
            }
        }
    }

    fun novoBloqueio() {
        backStack.clear()
        backStack.add(BloqueioAgendaRotas.Formulario)
        id.value = ""
        limparCampos()
    }

    fun editar(bloqueio: BloqueioAgenda) {
        backStack.clear()
        backStack.add(BloqueioAgendaRotas.Formulario)
        id.value = bloqueio.id?.toString() ?: ""
        psicologo.definir(bloqueio.psicologoId.toString())
        dataInicio.definir(formatarDataIsoParaBrasileira(bloqueio.dataHoraInicio))
        horaInicio.definir(bloqueio.dataHoraInicio.substringAfter('T', "").take(5))
        dataFim.definir(formatarDataIsoParaBrasileira(bloqueio.dataHoraFim))
        horaFim.definir(bloqueio.dataHoraFim.substringAfter('T', "").take(5))
        motivo.definir(bloqueio.motivo)
        ativo.definir(bloqueio.ativo.toString())
    }

    fun limparCampos() {
        psicologo.limpar()
        dataInicio.limpar()
        horaInicio.limpar()
        dataFim.limpar()
        horaFim.limpar()
        motivo.limpar()
        ativo.definir("true")
    }

    private fun nomePsicologo(psicologoId: Long, psicologos: List<Psicologo>): String =
        psicologos.firstOrNull { it.id == psicologoId }?.nome
            ?: "Psicólogo #$psicologoId"

    private fun montarDataHoraIso(data: String, hora: String): LocalDateTime? {
        val dataIso = formatarDataBrasileiraParaIso(data.trim())
        val partesHora = hora.trim().split(':')
        if (partesHora.size != 2) return null

        val horaInt = partesHora[0].toIntOrNull() ?: return null
        val minutoInt = partesHora[1].toIntOrNull() ?: return null
        if (horaInt !in 0..23 || minutoInt !in 0..59) return null

        val horaFormatada = "${horaInt.toString().padStart(2, '0')}:${minutoInt.toString().padStart(2, '0')}:00"
        return runCatching { LocalDateTime.parse("${dataIso}T$horaFormatada") }.getOrNull()
    }
}