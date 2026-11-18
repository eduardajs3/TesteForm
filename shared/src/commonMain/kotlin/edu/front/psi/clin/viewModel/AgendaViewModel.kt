package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.*
import edu.front.psi.clin.model.*
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import edu.front.psi.clin.utils.formatarDataIsoParaBrasileira
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class HorarioAgendamento(
    val inicio: String,
    val fim: String
)

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
    private val horarioDisponivelApi = HorarioDisponivelAPI(httpClient)

    private val _agendamentos = MutableStateFlow<List<Agenda>>(emptyList())
    val agendamentos = _agendamentos.asStateFlow()
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
    
    private val _horariosDisponiveis = MutableStateFlow<List<HorarioDisponivel>>(emptyList())
    val horariosDisponiveis = _horariosDisponiveis.asStateFlow()
    
    private val _psicologoFiltro = MutableStateFlow("Todos")
    val psicologoFiltro = _psicologoFiltro.asStateFlow()
    
    private var carregamentoDadosFormulario: Job? = null
    private val _statusFiltro = MutableStateFlow("Todos")
    val statusFiltro = _statusFiltro.asStateFlow()

    var salvando by mutableStateOf(false)
        private set

    fun filtrarPorStatus(status: String) {
        _statusFiltro.value = status
    }
    
    fun filtrarPorPsicologo(nome: String) {
        _psicologoFiltro.value = nome
    }

    fun atualizarDisponibilidade() {
        viewModelScope.launch {
            try {
                _horariosDisponiveis.value = horarioDisponivelApi.carregar()
                _agendamentos.value = agendaApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao atualizar a disponibilidade"))
            }
        }
    }

    init {
        carregarDadosFormulario()
    }

    fun carregarDadosFormulario() {
        if (carregamentoDadosFormulario?.isActive == true) return

        viewModelScope.launch {
            try {
                _agendamentos.value = agendaApi.carregar()
                val pacientesAtualizados = pacienteApi.carregar()
                _pacientes.value = pacientesAtualizados
                if (
                    id.value.isEmpty() &&
                    pacienteId.value != null &&
                    pacientesAtualizados.none { it.id == pacienteId.value && it.ativo }
                ) {
                    pacienteId.value = null
                    pacienteIdValidado.limpar()
                }
                _psicologos.value = psicologoApi.carregar()
                _salas.value = salaApi.carregar()
                _tiposAtendimento.value = tipoAtendimentoApi.carregar()
                _convenios.value = convenioAPI.carregar()
                _horariosDisponiveis.value = horarioDisponivelApi.carregar()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar dados do servidor"))
            } finally {
                carregamentoDadosFormulario = null
            }
        }.also { carregamentoDadosFormulario = it }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val agendamentosFiltrados = combine(
        _agendamentos,
        _filtro,
        _psicologoFiltro,
        _statusFiltro
    ) { agendamentos, filtro, psicologoFiltro, statusFiltro ->

        agendamentos.filter { item ->
            val termo = filtro.trim()
            val digitosFiltro = termo.filter { it.isDigit() }
            val pacienteObj = _pacientes.value.firstOrNull { it.id == item.pacienteId }
            val nomePac = item.paciente ?: pacienteObj?.nome.orEmpty()
            val psicologoObj = _psicologos.value.firstOrNull { it.id == item.psicologoId }
            val nomePsi = item.psicologo ?: psicologoObj?.nome.orEmpty()
            val salaObj = _salas.value.firstOrNull { it.id == item.salaId }
            val nomeSl = item.sala ?: salaObj?.nome.orEmpty()

            val filtroTexto =
                termo.isBlank() ||
                nomePac.contains(termo, ignoreCase = true) ||
                (pacienteObj?.cpf?.contains(termo, ignoreCase = true) == true) ||
                (digitosFiltro.isNotEmpty() && pacienteObj?.cpf?.filter { it.isDigit() }?.contains(digitosFiltro) == true) ||
                nomePsi.contains(termo, ignoreCase = true) ||
                (psicologoObj?.cpf?.contains(termo, ignoreCase = true) == true) ||
                nomeSl.contains(termo, ignoreCase = true) ||
                item.observacoes?.contains(termo, ignoreCase = true) == true ||
                item.status.contains(termo, ignoreCase = true)

            val filtroPsicologo =
                psicologoFiltro == "Todos" ||
                _psicologos.value.firstOrNull {
                    it.nome == psicologoFiltro
                }?.id == item.psicologoId

            val filtroStatus =
                statusFiltro == "Todos" ||
                item.status.equals(statusFiltro, true)

            filtroTexto && filtroPsicologo && filtroStatus
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val backStack = NavBackStack<AgendaRotas>(
        AgendaRotas.Listagem
    )

    val id = mutableStateOf("")
    val pacienteId = mutableStateOf<Long?>(null)
    val psicologoId = mutableStateOf<Long?>(null)
    val salaId = mutableStateOf<Long?>(null)
    val tipoAtendimentoId = mutableStateOf<Long?>(null)
    val convenioId = mutableStateOf<Long?>(null)

    val pacienteIdValidado = CampoValidado()
    val psicologoIdValidado = CampoValidado()
    val tipoAtendimentoIdValidado = CampoValidado()
    val convenioIdValidado = CampoValidado()

    val dataAtendimento = CampoValidado()
    val horaInicio = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val horaFim = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })

    val status = CampoValidado("AGENDADO")
    val observacoes = CampoValidado()
    val ativo = CampoValidado("true")

    fun validarDadosAtendimento(): Boolean {
        val v1 = pacienteIdValidado.validar()
        val v2 = psicologoIdValidado.validar()
        val v3 = tipoAtendimentoIdValidado.validar()
        val v4 = convenioIdValidado.validar()

        if (!(v1 && v2 && v3 && v4)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return false
        }
        return true
    }

    fun validarDataHorario(
        datasDisponiveis: List<LocalDate>,
        dataSelecionada: LocalDate?,
        horariosDisponiveis: List<HorarioAgendamento>,
        horarioSelecionado: HorarioAgendamento?
    ): Boolean {
        val v1 = datasDisponiveis.isNotEmpty()
        val v2 = dataSelecionada != null
        val v3 = horariosDisponiveis.isNotEmpty()
        val v4 = horarioSelecionado != null

        if (!v1) {
            mandarMensagem("Não há datas disponíveis. Selecione outro psicólogo ou atendimento.")
            return false
        }
        if (!v2) {
            mandarMensagem("Selecione uma data para continuar.")
            return false
        }
        if (!v3) {
            mandarMensagem("Não há horários livres para esta data. Selecione outra data.")
            return false
        }
        if (!v4) {
            mandarMensagem("Selecione um horário para continuar.")
            return false
        }
        return true
    }

    fun validarSalaEHorario(
        salasDisponiveis: List<SalaAtendimento>,
        horarioSelecionado: HorarioAgendamento?
    ): Boolean {
        val v1 = horarioSelecionado != null
        val v2 = salasDisponiveis.isNotEmpty() && salasDisponiveis.any { it.id == salaId.value }

        if (!v1) {
            mandarMensagem("Este horário não está mais disponível. Selecione outro.")
            return false
        }
        if (!v2) {
            mandarMensagem("Não há sala disponível neste horário. Selecione outro horário.")
            return false
        }
        return true
    }

    fun diasDisponiveis(psicologoId: Long, tipoAtendimentoId: Long): List<LocalDate> {
        val hoje = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return (0..60)
            .map { hoje.plus(it, DateTimeUnit.DAY) }
            .filter { data -> buscarHorariosDisponiveis(data, psicologoId, tipoAtendimentoId).isNotEmpty() }
    }

    fun buscarHorariosDisponiveis(
        data: LocalDate,
        psicologoId: Long,
        tipoAtendimentoId: Long
    ): List<HorarioAgendamento> {
        val diaSemana = when (data.dayOfWeek.ordinal + 1) {
            1 -> "SEGUNDA"
            2 -> "TERCA"
            3 -> "QUARTA"
            4 -> "QUINTA"
            5 -> "SEXTA"
            6 -> "SABADO"
            else -> "DOMINGO"
        }
        val duracao = _tiposAtendimento.value
            .firstOrNull { it.id == tipoAtendimentoId }
            ?.duracao
            ?.let(::duracaoEmMinutos)
            ?: return emptyList()
        if (duracao <= 0) return emptyList()

        val agora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val minutosAgora = agora.hour * 60 + agora.minute

        return _horariosDisponiveis.value
            .asSequence()
            .filter {
                it.ativo &&
                    it.psicologoId == psicologoId &&
                    it.diaSemana.equals(diaSemana, ignoreCase = true)
            }
            .flatMap { horario ->
                val inicioFaixa = horaEmMinutos(horario.horaInicio) ?: return@flatMap emptySequence()
                val fimFaixa = horaEmMinutos(horario.horaFim) ?: return@flatMap emptySequence()
                if (fimFaixa <= inicioFaixa) return@flatMap emptySequence()

                generateSequence(inicioFaixa) { inicio ->
                    (inicio + duracao).takeIf { fim -> fim <= fimFaixa }?.let { inicio + duracao }
                }
                    .takeWhile { it + duracao <= fimFaixa }
                    .filter { inicio ->
                        val fim = inicio + duracao
                        val horarioPassou = !(data > agora.date || data == agora.date && inicio > minutosAgora)
                        val eHorarioAtualNaEdicao = id.value.isNotEmpty() &&
                            inicio == horaEmMinutos(horaInicio.valor.value) &&
                            data.toString() == dataAtendimento.valor.value.toLocalDateOrNull()?.toString()

                        (!horarioPassou || eHorarioAtualNaEdicao) &&
                            !temConflitoComAgendamento(
                                agendamentos = _agendamentos.value,
                                data = data,
                                inicio = inicio,
                                fim = fim,
                                psicologoId = psicologoId
                            )
                    }
                    .map { inicio ->
                        HorarioAgendamento(
                            inicio = formatarHora(inicio),
                            fim = formatarHora(inicio + duracao)
                        )
                    }
            }
            .distinctBy { it.inicio }
            .sortedBy { it.inicio }
            .toList()
    }

    fun salasDisponiveis(data: LocalDate, horario: HorarioAgendamento): List<SalaAtendimento> {
        val inicio = horaEmMinutos(horario.inicio) ?: return emptyList()
        val fim = horaEmMinutos(horario.fim) ?: return emptyList()
        return _salas.value.filter { sala ->
            sala.ativo && !temConflitoComAgendamento(
                agendamentos = _agendamentos.value,
                data = data,
                inicio = inicio,
                fim = fim,
                salaId = sala.id
            )
        }
    }

    fun salvar(onDisponibilidadeIndisponivel: () -> Unit = {}) {
        if (salvando) return

        if (
            pacienteId.value == null ||
            psicologoId.value == null ||
            tipoAtendimentoId.value == null ||
            convenioId.value == null ||
            salaId.value == null
        ) {
            mandarMensagem("Selecione paciente, psicólogo, atendimento, convênio e sala.")
            return
        }

        val vData = dataAtendimento.validar()
        val vInicio = horaInicio.validar()
        val vFim = horaFim.validar()

        if (!(vData && vInicio && vFim)) {
            mandarMensagem("Por favor, preencha a data e os horários de início e fim.")
            return
        }

        val data = dataAtendimento.valor.value.toLocalDateOrNull()
        val inicioMinutos = horaEmMinutos(horaInicio.valor.value)
        val fimMinutos = horaEmMinutos(horaFim.valor.value)
        val tipoAtendimento = _tiposAtendimento.value.firstOrNull { it.id == tipoAtendimentoId.value }
        val duracao = tipoAtendimento?.duracao?.let(::duracaoEmMinutos)
        val hoje = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        if (data == null || inicioMinutos == null || fimMinutos == null || duracao == null) {
            mandarMensagem("Data, horários ou duração do atendimento inválidos.")
            return
        }
        if (id.value.isEmpty()) {
            if (data < hoje) {
                mandarMensagem("Não é possível agendar em uma data passada.")
                return
            }
            val agora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            if (data == hoje && inicioMinutos <= agora.hour * 60 + agora.minute) {
                mandarMensagem("Não é possível agendar em um horário que já passou.")
                return
            }
        }
        if (fimMinutos <= inicioMinutos || fimMinutos - inicioMinutos != duracao) {
            mandarMensagem("O horário deve respeitar a duração do tipo de atendimento.")
            return
        }
        if (id.value.isEmpty() && status.valor.value !in setOf("AGENDADO", "CONFIRMADO")) {
            mandarMensagem("Para novos agendamentos, selecione Agendado ou Confirmado.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val agendamentosAtualizados = agendaApi.carregar()
                _agendamentos.value = agendamentosAtualizados
                _horariosDisponiveis.value = horarioDisponivelApi.carregar()
                _salas.value = salaApi.carregar()

                val horariosValidos = buscarHorariosDisponiveis(
                    data,
                    psicologoId.value!!,
                    tipoAtendimentoId.value!!
                )
                val horarioPresente = horariosValidos.any { it.inicio == horaInicio.valor.value && it.fim == horaFim.valor.value }
                val eEdicaoMesmoHorario = id.value.isNotEmpty()
                if (!horarioPresente && !eEdicaoMesmoHorario) {
                    horaInicio.limpar()
                    horaFim.limpar()
                    salaId.value = null
                    onDisponibilidadeIndisponivel()
                    return@launch
                }

                val salasValidas = salasDisponiveis(
                    data,
                    HorarioAgendamento(horaInicio.valor.value, horaFim.valor.value)
                )
                if (salasValidas.none { it.id == salaId.value }) {
                    salaId.value = null
                    onDisponibilidadeIndisponivel()
                    return@launch
                }

                val dataFormatada = data.toString()
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
                    observacoes = observacoes.valor.value.ifEmpty { null },
                    ativo = ativo.valor.value.toBoolean()
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
                
                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(AgendaRotas.Listagem)

            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
            } finally {
                salvando = false
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
                    status = novoStatus.uppercase(),
                    paciente = null,
                    psicologo = null,
                    sala = null,
                    tipoAtendimento = null,
                    convenio = null
                )
                agendaApi.atualizar(
                    agenda.id!!,
                    atualizado
                )
                _agendamentos.value = agendaApi.carregar()
                mandarMensagem("Status atualizado para ${novoStatus.uppercase()} com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao alterar status"))
            }
        }
    }

    private fun temConflitoComAgendamento(
        agendamentos: List<Agenda>,
        data: LocalDate,
        inicio: Int,
        fim: Int,
        psicologoId: Long? = null,
        salaId: Long? = null
    ): Boolean = agendamentos.any { agendamento ->
        val statusAtual = agendamento.status.uppercase()
        val ocupaHorario = agendamento.ativo && statusAtual !in setOf("CANCELADO", "CANCELED")
        val mesmoRecurso = when {
            psicologoId != null -> agendamento.psicologoId == psicologoId
            salaId != null -> agendamento.salaId == salaId
            else -> false
        }
        val inicioExistente = horaEmMinutos(agendamento.dataHoraInicio.substringAfter('T', "").take(5))
        val fimExistente = horaEmMinutos(agendamento.dataHoraFim.substringAfter('T', "").take(5))

        agendamento.id?.toString() != id.value &&
            ocupaHorario &&
            mesmoRecurso &&
            agendamento.dataHoraInicio.substringBefore('T') == data.toString() &&
            inicioExistente != null &&
            fimExistente != null &&
            inicio < fimExistente &&
            fim > inicioExistente
    }

    private fun horaEmMinutos(valor: String): Int? {
        val partes = valor.take(5).split(":")
        if (partes.size != 2) return null
        val hora = partes[0].toIntOrNull() ?: return null
        val minuto = partes[1].toIntOrNull() ?: return null
        if (hora !in 0..23 || minuto !in 0..59) return null
        return hora * 60 + minuto
    }

    private fun duracaoEmMinutos(valor: String): Int? {
        val partes = valor.split(":")
        if (partes.size != 2) return valor.toIntOrNull()
        val hora = partes[0].toIntOrNull() ?: return null
        val minuto = partes[1].take(2).toIntOrNull() ?: return null
        if (hora < 0 || minuto !in 0..59) return null
        return hora * 60 + minuto
    }

    private fun formatarHora(minutos: Int): String =
        "${(minutos / 60).toString().padStart(2, '0')}:${(minutos % 60).toString().padStart(2, '0')}"

    private fun String.toLocalDateOrNull(): LocalDate? {
        val partes = if (contains('/')) split('/') else split('-')
        if (partes.size != 3) return null
        return runCatching {
            if (contains('/')) {
                LocalDate(partes[2].toInt(), partes[1].toInt(), partes[0].toInt())
            } else {
                LocalDate(partes[0].toInt(), partes[1].toInt(), partes[2].toInt())
            }
        }.getOrNull()
    }

    fun limparCampos() {
        pacienteId.value = null
        psicologoId.value = null
        salaId.value = null
        tipoAtendimentoId.value = null
        convenioId.value = null
        
        pacienteIdValidado.limpar()
        psicologoIdValidado.limpar()
        tipoAtendimentoIdValidado.limpar()
        convenioIdValidado.limpar()

        dataAtendimento.limpar()
        horaInicio.limpar()
        horaFim.limpar()
        if (id.value.isEmpty()) {
            status.definir("AGENDADO")
        }
        observacoes.limpar()
        ativo.definir("true")
    }

    fun apagar(agenda: Agenda) {
        viewModelScope.launch {
            try {
                agendaApi.apagar(agenda)
                _agendamentos.value = agendaApi.carregar()
                mandarMensagem("Agendamento apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
            }
        }
    }

    fun novoAgendamento() {
        backStack.clear()
        backStack.add(AgendaRotas.Formulario)
        id.value = ""
        limparCampos()
    }

    fun editar(agenda: Agenda) {
        backStack.clear()
        backStack.add(AgendaRotas.Formulario)
        id.value = agenda.id?.toString() ?: ""
        
        pacienteId.value = agenda.pacienteId
        pacienteIdValidado.definir(agenda.pacienteId?.toString() ?: "")
        
        psicologoId.value = agenda.psicologoId
        psicologoIdValidado.definir(agenda.psicologoId?.toString() ?: "")
        
        salaId.value = agenda.salaId
        
        tipoAtendimentoId.value = agenda.tipoAtendimentoId
        tipoAtendimentoIdValidado.definir(agenda.tipoAtendimentoId?.toString() ?: "")
        
        convenioId.value = agenda.convenioId
        convenioIdValidado.definir(agenda.convenioId?.toString() ?: "")

        val partesDataHora = agenda.dataHoraInicio.split("T")
        if (partesDataHora.size == 2) {
            if (partesDataHora[0].split("-").size == 3) {
                dataAtendimento.definir(formatarDataIsoParaBrasileira(partesDataHora[0]))
            }
            horaInicio.definir(partesDataHora[1].take(5))
        }
        val partesFim = agenda.dataHoraFim.split("T")
        if (partesFim.size == 2) {
            horaFim.definir(partesFim[1].take(5))
        }

        status.definir(agenda.status)
        observacoes.definir(agenda.observacoes ?: "")
        ativo.definir(agenda.ativo.toString())
    }
}