package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.BloqueioAgendaAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.BloqueioAgenda
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class BloqueioAgendaViewModel(
    private val mandarMensagem: (String) -> Unit,
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val bloqueioApi = BloqueioAgendaAPI(httpClient)
    private val psicologoApi = PsicologoAPI(httpClient)
    private val _bloqueios = MutableStateFlow<List<BloqueioAgenda>>(emptyList())
    private val _psicologos = MutableStateFlow<List<Psicologo>>(emptyList())
    val psicologos = _psicologos.asStateFlow()

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _bloqueios.value = bloqueioApi.carregar()
                _psicologos.value = psicologoApi.carregar()
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar bloqueios de agenda: ${e.message}")
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

    val mensagem = mutableStateOf("")

    fun nomePsicologo(psicologoId: Long?): String =
        nomePsicologo(psicologoId, _psicologos.value)

    fun salvar() {
        val v1 = psicologo.validar()
        val v2 = dataInicio.validar()
        val v3 = horaInicio.validar()
        val v4 = dataFim.validar()
        val v5 = horaFim.validar()
        val v6 = motivo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        val psicologoId = psicologo.valor.value.toLongOrNull()
        val inicio = formatarDataHora(dataInicio.valor.value, horaInicio.valor.value)
        val fim = formatarDataHora(dataFim.valor.value, horaFim.valor.value)
        val bloqueioId = if (id.value.isEmpty()) 0L else id.value.toLongOrNull()

        if (psicologoId == null || inicio == null || fim == null || bloqueioId == null) {
            mensagem.value = "Informe psicólogo, datas e horários válidos."
            mandarMensagem(mensagem.value)
            return
        }
        if (fim <= inicio) {
            mensagem.value = "O fim do bloqueio deve ser posterior ao início."
            mandarMensagem(mensagem.value)
            return
        }

        viewModelScope.launch {
            try {
                val bloqueio = BloqueioAgenda(
                    id = bloqueioId,
                    psicologoId = psicologoId,
                    dataHoraInicio = inicio,
                    dataHoraFim = fim,
                    motivo = motivo.valor.value
                )

                if (id.value.isEmpty()) {
                    bloqueioApi.salvar(bloqueio)
                } else {
                    bloqueioApi.atualizar(bloqueioId, bloqueio)
                }

                _bloqueios.value = bloqueioApi.carregar()
                _psicologos.value = psicologoApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Bloqueio de agenda salvo com sucesso!"
                } else {
                    "Bloqueio de agenda atualizado com sucesso!"
                }
                mensagem.value = mensagemSucesso
                mandarMensagem(mensagemSucesso)
                limparCampos()
                backStack.clear()
                backStack.add(BloqueioAgendaRotas.Listagem)
            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem(mensagem.value)
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
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoBloqueio() {
        backStack.clear()
        backStack.add(BloqueioAgendaRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(bloqueio: BloqueioAgenda) {
        backStack.clear()
        backStack.add(BloqueioAgendaRotas.Formulario)
        id.value = bloqueio.id?.toString() ?: ""
        psicologo.definir(bloqueio.psicologoId?.toString() ?: "")
        val (dataInicioValor, horaInicioValor) = separarDataHora(bloqueio.dataHoraInicio)
        val (dataFimValor, horaFimValor) = separarDataHora(bloqueio.dataHoraFim)
        dataInicio.definir(dataInicioValor)
        horaInicio.definir(horaInicioValor)
        dataFim.definir(dataFimValor)
        horaFim.definir(horaFimValor)
        motivo.definir(bloqueio.motivo)
        mensagem.value = ""
    }

    fun limparCampos() {
        psicologo.limpar()
        dataInicio.limpar()
        horaInicio.limpar()
        dataFim.limpar()
        horaFim.limpar()
        motivo.limpar()
        mensagem.value = "Campos limpos."
    }

    private fun nomePsicologo(psicologoId: Long?, psicologos: List<Psicologo>): String =
        psicologos.firstOrNull { it.id == psicologoId }?.nome
            ?: psicologoId?.let { "Psicólogo #$it" }
            ?: "Psicólogo"

    private fun formatarDataHora(data: String, hora: String): String? {
        val partesData = data.split('/')
        if (partesData.size != 3) return null
        val date = runCatching {
            LocalDate(partesData[2].toInt(), partesData[1].toInt(), partesData[0].toInt())
        }.getOrNull() ?: return null

        val partesHora = hora.split(':')
        if (partesHora.size != 2) return null
        val horaInt = partesHora[0].toIntOrNull() ?: return null
        val minutoInt = partesHora[1].toIntOrNull() ?: return null
        if (horaInt !in 0..23 || minutoInt !in 0..59) return null

        return "$date" +
            "T${horaInt.toString().padStart(2, '0')}:${minutoInt.toString().padStart(2, '0')}:00"
    }

    private fun separarDataHora(valor: String?): Pair<String, String> {
        val partes = valor?.split('T', limit = 2) ?: return "" to ""
        if (partes.size != 2) return "" to ""
        val data = partes[0].split('-')
        if (data.size != 3) return "" to ""
        return "${data[2]}/${data[1]}/${data[0]}" to partes[1].take(5)
    }
}