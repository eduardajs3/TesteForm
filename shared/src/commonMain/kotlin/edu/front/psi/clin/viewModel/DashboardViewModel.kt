package edu.front.psi.clin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.api.AgendaAPI
import edu.front.psi.clin.api.PacienteAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.SalaAtendimentoAPI
import edu.front.psi.clin.api.TipoAtendimentoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.model.SalaAtendimento
import edu.front.psi.clin.model.TipoAtendimento
import edu.front.psi.clin.model.enums.AgendaStatus
import frontpsiclinic.shared.generated.resources.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import org.jetbrains.compose.resources.DrawableResource

data class MetricaCardItem(
    val id: String,
    val titulo: String,
    val valor: String,
    val subtitulo: String,
    val iconeRes: DrawableResource,
    val variacaoPositiva: Boolean = true
)

data class ConsultaHojeItem(
    val id: Long,
    val paciente: String,
    val psicologo: String,
    val sala: String,
    val horario: String,
    val tipo: String,
    val status: String
)

class DashboardViewModel(
    private val mandarMensagem: (String) -> Unit = {}
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val agendaApi = AgendaAPI(httpClient)
    private val pacienteApi = PacienteAPI(httpClient)
    private val salaApi = SalaAtendimentoAPI(httpClient)
    private val psicologoApi = PsicologoAPI(httpClient)
    private val tipoAtendimentoApi = TipoAtendimentoAPI(httpClient)

    private val _agendamentos = MutableStateFlow<List<Agenda>>(emptyList())
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    private val _salas = MutableStateFlow<List<SalaAtendimento>>(emptyList())
    private val _psicologos = MutableStateFlow<List<Psicologo>>(emptyList())
    private val _tiposAtendimento = MutableStateFlow<List<TipoAtendimento>>(emptyList())

    private val _metricas = MutableStateFlow<List<MetricaCardItem>>(emptyList())
    val metricas: StateFlow<List<MetricaCardItem>> = _metricas.asStateFlow()

    private val _consultas = MutableStateFlow<List<ConsultaHojeItem>>(emptyList())
    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    private val _filtroStatus = MutableStateFlow("Todas")
    val filtroStatus: StateFlow<String> = _filtroStatus.asStateFlow()

    val consultasFiltradas: StateFlow<List<ConsultaHojeItem>> =
        combine(_consultas, _filtroStatus) { consultas, filtro ->
            if (filtro == "Todas") consultas
            else consultas.filter { it.status.equals(filtro, ignoreCase = true) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun carregarDados() {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val agendamentos = agendaApi.carregar()
                val pacientes = pacienteApi.carregar()
                val salas = salaApi.carregar()
                val psicologos = psicologoApi.carregar()
                val tiposAtendimento = tipoAtendimentoApi.carregar()

                _agendamentos.value = agendamentos
                _pacientes.value = pacientes
                _salas.value = salas
                _psicologos.value = psicologos
                _tiposAtendimento.value = tiposAtendimento
                atualizarApresentacao()
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar dados do dashboard"))
            } finally {
                _carregando.value = false
            }
        }
    }

    fun selecionarFiltroStatus(novoFiltro: String) {
        _filtroStatus.value = novoFiltro
    }

    fun confirmarConsulta(consultaId: Long) {
        val agendamento = _agendamentos.value.firstOrNull { it.id == consultaId } ?: return
        if (agendamento.status != AgendaStatus.AGENDADO) return

        viewModelScope.launch {
            try {
                val atualizado = agendamento.copy(status = AgendaStatus.CONFIRMADO)
                agendaApi.atualizar(consultaId, atualizado)
                _agendamentos.update { lista ->
                    lista.map { if (it.id == consultaId) atualizado else it }
                }
                atualizarApresentacao()
                mandarMensagem("Consulta de ${agendamento.pacienteNome()} confirmada!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao confirmar consulta"))
            }
        }
    }

    private fun atualizarApresentacao() {
        val agora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val hoje = agora.date.toString()
        val minutoAtual = agora.hour * 60 + agora.minute
        val consultasHoje = _agendamentos.value.filter {
            it.ativo &&
                it.dataHoraInicio.substringBefore('T') == hoje &&
                it.status != AgendaStatus.CANCELADO
        }
        val consultasVisiveis = consultasHoje
            .filter {
                it.status == AgendaStatus.AGENDADO ||
                    it.status == AgendaStatus.CONFIRMADO
            }
            .mapNotNull { agenda ->
                val id = agenda.id ?: return@mapNotNull null
                val inicio = agenda.dataHoraInicio.substringAfter('T', "").take(5)
                val fim = agenda.dataHoraFim.substringAfter('T', "").take(5)
                val minutoFim = fim.toMinutos()
                if (minutoFim != null && minutoFim <= minutoAtual) return@mapNotNull null

                ConsultaHojeItem(
                    id = id,
                    paciente = agenda.pacienteNome(),
                    psicologo = agenda.psicologoNome(),
                    sala = agenda.salaNome(),
                    horario = "$inicio - $fim",
                    tipo = agenda.tipoAtendimentoNome(),
                    status = agenda.status.rotulo
                )
            }
            .sortedBy { it.horario.substringBefore(" - ") }

        _consultas.value = consultasVisiveis

        val pacientesAtivos = _pacientes.value.count { it.ativo }
        val salasAtivas = _salas.value.filter { it.ativo }
        val salasEmUso = consultasHoje
            .asSequence()
            .filter { agenda ->
                agenda.status != AgendaStatus.REALIZADO &&
                    agenda.status != AgendaStatus.FALTOU &&
                    agenda.dataHoraInicio.substringBefore('T') == hoje &&
                    agenda.dataHoraInicio.substringAfter('T', "").take(5).toMinutos()
                        ?.let { it <= minutoAtual } == true &&
                    agenda.dataHoraFim.substringAfter('T', "").take(5).toMinutos()
                        ?.let { minutoAtual < it } == true
            }
            .map { it.salaId }
            .toSet()
            .size
        val consultasAgendadasOuConfirmadas = consultasHoje.count {
            it.status == AgendaStatus.AGENDADO ||
                it.status == AgendaStatus.CONFIRMADO
        }
        val consultasFinalizadas = consultasHoje.count { it.status == AgendaStatus.REALIZADO }

        _metricas.value = listOf(
            MetricaCardItem(
                id = "consultas",
                titulo = "Consultas Hoje",
                valor = consultasHoje.size.toString(),
                subtitulo = "$consultasAgendadasOuConfirmadas agendadas/confirmadas • $consultasFinalizadas finalizadas",
                iconeRes = Res.drawable.calendar_month
            ),
            MetricaCardItem(
                id = "pacientes",
                titulo = "Pacientes Ativos",
                valor = pacientesAtivos.toString(),
                subtitulo = "de ${_pacientes.value.size} cadastrados",
                iconeRes = Res.drawable.group_24px
            ),
            MetricaCardItem(
                id = "salas",
                titulo = "Salas em Uso",
                valor = "${salasEmUso.coerceAtMost(salasAtivas.size)} / ${salasAtivas.size}",
                subtitulo = "${(salasAtivas.size - salasEmUso).coerceAtLeast(0)} disponíveis agora",
                iconeRes = Res.drawable.door_open
            )
        )
    }

    private fun Agenda.pacienteNome(): String =
        _pacientes.value.firstOrNull { it.id == pacienteId }?.nome
            ?: "Paciente"

    private fun Agenda.psicologoNome(): String =
        _psicologos.value.firstOrNull { it.id == psicologoId }?.nome
            ?: "Psicólogo"

    private fun Agenda.salaNome(): String =
        _salas.value.firstOrNull { it.id == salaId }?.nome
            ?: "Sala"

    private fun Agenda.tipoAtendimentoNome(): String =
        _tiposAtendimento.value.firstOrNull { it.id == tipoAtendimentoId }?.nome
            ?: "Atendimento"

    private fun String.toMinutos(): Int? {
        val partes = split(':')
        if (partes.size < 2) return null
        val hora = partes[0].toIntOrNull() ?: return null
        val minuto = partes[1].take(2).toIntOrNull() ?: return null
        if (hora !in 0..23 || minuto !in 0..59) return null
        return hora * 60 + minuto
    }
}