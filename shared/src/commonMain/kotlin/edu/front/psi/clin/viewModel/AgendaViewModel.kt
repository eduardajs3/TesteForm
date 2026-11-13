package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.*
import edu.front.psi.clin.model.*
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AgendaViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val agendaApi = AgendaAPI(httpClient)
    private val pacienteApi = PacienteAPI(httpClient)
    private val psicologoApi = PsicologoAPI(httpClient)
    private val salaApi = SalaAtendimentoAPI(httpClient)
    private val tipoAtendimentoApi = TipoAtendimentoAPI(httpClient)
    private val convenioAPI = ConvenioAPI(httpClient)


    private val _agendamentos = MutableStateFlow<List<Agenda>>(emptyList())
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes = _pacientes.asStateFlow()

    private val _psicologos = MutableStateFlow<List<Psicologo>>(emptyList())
    val psicologos = _psicologos.asStateFlow()

    private val _salas = MutableStateFlow<List<SalaAtendimento>>(emptyList())
    val salas = _salas.asStateFlow()

    private val _tiposAtendimento = MutableStateFlow<List<TipoAtendimento>>(emptyList())
    val tiposAtendimento = _tiposAtendimento.asStateFlow()

    private val _convenios = MutableStateFlow<List<Convenio>>(emptyList())
    val convenios = _convenios.asStateFlow()
    private val _psicologoFiltro = MutableStateFlow("Todos")
    val psicologoFiltro = _psicologoFiltro.asStateFlow()

    fun filtrarPorPsicologo(nome: String) {
        _psicologoFiltro.value = nome
    }
    init {
        viewModelScope.launch {
            try {
                _agendamentos.value = agendaApi.carregar()
                _pacientes.value = pacienteApi.carregar()
                _psicologos.value = psicologoApi.carregar()
                _salas.value = salaApi.carregar()
                _tiposAtendimento.value = tipoAtendimentoApi.carregar()
                _convenios.value = convenioAPI.carregar()
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar dados do servidor: ${e.message}")
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val agendamentosFiltrados = combine(_agendamentos, _filtro) { agendamentos, filtro ->
        if (filtro.isEmpty()) {
            agendamentos
        } else {
            agendamentos.filter { item ->
                item.observacoes?.contains(filtro, ignoreCase = true) == true ||
                item.status.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<AgendaRotas>(
    AgendaRotas.Listagem
)

    val id = mutableStateOf("")
    val pacienteId = mutableStateOf<Long?>(null)
    val psicologoId = mutableStateOf<Long?>(null)
    val salaId = mutableStateOf<Long?>(null)
    val tipoAtendimentoId = mutableStateOf<Long?>(null)
    val convenioId = mutableStateOf<Long?>(null)

    val dataAtendimento = CampoValidado()
    val horaInicio = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val horaFim = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })

    val status = CampoValidado("CONFIRMADO")
    val observacoes = CampoValidado()
    val ativo = CampoValidado("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        val vData = dataAtendimento.validar()
        val vInicio = horaInicio.validar()
        val vFim = horaFim.validar()

        if (pacienteId.value == null || psicologoId.value == null || tipoAtendimentoId.value == null) {
            mensagem.value = "Selecione Paciente, Psicólogo e Tipo de Atendimento."
            mandarMensagem("Selecione Paciente, Psicólogo e Tipo de Atendimento.")
            return
        }

        if (!(vData && vInicio && vFim)) {
            mensagem.value = "Por favor, preencha a data e os horários de início e fim."
            mandarMensagem("Por favor, preencha a data e os horários de início e fim.")
            return
        }


        if (horaFim.valor.value <= horaInicio.valor.value) {
            mensagem.value = "O horário de término deve ser obrigatoriamente posterior ao início."
            mandarMensagem("O horário de término deve ser obrigatoriamente posterior ao início.")
            return
        }

        viewModelScope.launch {
            try {
                val dataFormatada = converterDataParaIso(dataAtendimento.valor.value)
                val inicioFormatado = "${dataFormatada}T${horaInicio.valor.value}:00"
                val fimFormatado = "${dataFormatada}T${horaFim.valor.value}:00"

                val agenda = Agenda(
                    id = id.value.toLongOrNull(),

                    pacienteId = pacienteId.value!!,
                    psicologoId = psicologoId.value!!,
                    salaId = salaId.value!!,
                    tipoAtendimentoId = tipoAtendimentoId.value!!,
                    convenioId = convenioId.value!!,

                    paciente = null,
                    psicologo = null,
                    sala = null,
                    tipoAtendimento = null,
                    convenio = null,

                    dataHoraInicio = inicioFormatado,
                    dataHoraFim = fimFormatado,
                    status = status.valor.value,
                    observacoes = observacoes.valor.value.ifEmpty { null }
                )

                if (id.value.isEmpty()) {
                    agendaApi.salvar(agenda)
                } else {
                    agendaApi.atualizar(id.value.toLong(), agenda)
                }

                _agendamentos.value = agendaApi.carregar()
                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Agendamento salvo com sucesso!"
                } else {
                    "Agendamento atualizado com sucesso!"
                }
                mensagem.value = mensagemSucesso
                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(AgendaRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }
    fun alterarStatus(
        agenda: Agenda,
        novoStatus: String
    ) {
        viewModelScope.launch {
            try {
                val atualizado = agenda.copy(
                    status = novoStatus.uppercase()
                )

                agendaApi.atualizar(
                    agenda.id!!,
                    atualizado
                )

                _agendamentos.value = agendaApi.carregar()

            } catch (e: Exception) {
                mandarMensagem("Erro ao alterar status: ${e.message}")
            }
        }
    }

    private fun converterDataParaIso(dataStr: String): String {
        return try {
            if (dataStr.contains("/")) {
                val partes = dataStr.split("/")
                if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}" else dataStr
            } else {
                dataStr
            }
        } catch (e: Exception) {
            dataStr
        }
    }

    fun limparCampos() {
        pacienteId.value = null
        psicologoId.value = null
        salaId.value = null
        tipoAtendimentoId.value = null
        convenioId.value = null
        dataAtendimento.limpar()
        horaInicio.limpar()
        horaFim.limpar()
        status.definir("CONFIRMADO")
        observacoes.limpar()
        ativo.definir("true")
        mensagem.value = "Campos limpos."
    }

    fun apagar(agenda: Agenda) {
        viewModelScope.launch {
            try {
                agendaApi.apagar(agenda)
                _agendamentos.value = agendaApi.carregar()
                mandarMensagem("Agendamento apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoAgendamento() {
        backStack.clear()
        backStack.add(AgendaRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(agenda: Agenda) {
        backStack.clear()
        backStack.add(AgendaRotas.Formulario)
        id.value = agenda.id?.toString() ?: ""
        pacienteId.value = agenda.pacienteId
        psicologoId.value = agenda.psicologoId
        salaId.value = agenda.salaId
        tipoAtendimentoId.value = agenda.tipoAtendimentoId
        convenioId.value = agenda.convenioId

        try {
            val partesDataHora = agenda.dataHoraInicio.split("T")
            if (partesDataHora.size == 2) {
                val partesData = partesDataHora[0].split("-")
                if (partesData.size == 3) {
                    dataAtendimento.definir("${partesData[2]}/${partesData[1]}/${partesData[0]}")
                }
                horaInicio.definir(partesDataHora[1].take(5))
            }
            val partesFim = agenda.dataHoraFim.split("T")
            if (partesFim.size == 2) {
                horaFim.definir(partesFim[1].take(5))
            }
        } catch (e: Exception) {

        }

        status.definir(agenda.status)
        observacoes.definir(agenda.observacoes ?: "")
        ativo.definir(agenda.ativo.toString())
        mensagem.value = ""
    }
}