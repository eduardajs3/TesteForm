package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.navigation.AgendaRotas
import kotlinx.coroutines.flow.*

class AgendaViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private var idCounter: Int = 6

    private val _agendamentos = MutableStateFlow<List<Agenda>>(
        listOf(
            Agenda(
                id = 1,
                paciente = "João Silva",
                psicologo = "Dra. Ana Costa",
                sala = "Sala 02 - Bloco A",
                tipoAtendimento = "Presencial",
                convenio = "Unimed",
                dataHoraInicio = "16/03/2026 14:00",
                dataHoraFim = "16/03/2026 15:00",
                status = "Confirmado",
                observacoes = "Primeira consulta",
                ativo = true
            ),
            Agenda(
                id = 2,
                paciente = "Maria Clara da Silva",
                psicologo = "Dr. Roberto Alves",
                sala = "Sala 01 - Bloco A",
                tipoAtendimento = "Presencial",
                convenio = "Bradesco Saúde",
                dataHoraInicio = "17/03/2026 09:00",
                dataHoraFim = "17/03/2026 10:00",
                status = "Confirmado",
                observacoes = "Retorno quinzenal",
                ativo = true
            ),
            Agenda(
                id = 3,
                paciente = "Lucas Oliveira",
                psicologo = "Dra. Ana Costa",
                sala = "Sala 02 - Bloco A",
                tipoAtendimento = "Online",
                convenio = "Particular",
                dataHoraInicio = "18/03/2026 10:00",
                dataHoraFim = "18/03/2026 11:00",
                status = "Pendente",
                observacoes = "Avaliação inicial",
                ativo = true
            ),
            Agenda(
                id = 4,
                paciente = "Ana Beatriz Lima",
                psicologo = "Dra. Juliana Costa",
                sala = "Sala 03 - Bloco B",
                tipoAtendimento = "Presencial",
                convenio = "SulAmérica",
                dataHoraInicio = "19/03/2026 15:00",
                dataHoraFim = "19/03/2026 16:00",
                status = "Confirmado",
                observacoes = "Sessão de terapia",
                ativo = true
            ),
            Agenda(
                id = 5,
                paciente = "João Pedro Santos",
                psicologo = "Dr. Roberto Alves",
                sala = "Sala 01 - Bloco A",
                tipoAtendimento = "Presencial",
                convenio = "Amil",
                dataHoraInicio = "20/03/2026 08:00",
                dataHoraFim = "20/03/2026 09:00",
                status = "Realizado",
                observacoes = "Encaminhamento concluído",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    private val _psicologoFiltro = MutableStateFlow("Todos")
    val psicologoFiltro = _psicologoFiltro.asStateFlow()

    fun filtrarPorPsicologo(psicologo: String) {
        _psicologoFiltro.value = psicologo
    }

    val agendamentosFiltrados = combine(_agendamentos, _filtro, _psicologoFiltro) { agendamentos, filtro, psicologo ->
        agendamentos.filter { item ->
            val matchTexto = filtro.isEmpty() ||
                item.paciente.contains(filtro, ignoreCase = true) ||
                item.status.contains(filtro, ignoreCase = true) ||
                item.sala.contains(filtro, ignoreCase = true)
            val matchPsicologo = psicologo == "Todos" || item.psicologo.contains(psicologo, ignoreCase = true)
            matchTexto && matchPsicologo
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<AgendaRotas>(
        AgendaRotas.Listagem
    )

    // Campos do formulário
    val id = mutableStateOf("")
    val paciente = mutableStateOf("")
    val psicologo = mutableStateOf("")
    val sala = mutableStateOf("")
    val tipoAtendimento = mutableStateOf("")
    val convenio = mutableStateOf("")
    val dataHoraInicio = mutableStateOf("")
    val dataHoraFim = mutableStateOf("")
    val status = mutableStateOf("")
    val observacoes = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val agendamento = Agenda(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                paciente = paciente.value,
                psicologo = psicologo.value,
                sala = sala.value,
                tipoAtendimento = tipoAtendimento.value,
                convenio = convenio.value,
                dataHoraInicio = dataHoraInicio.value,
                dataHoraFim = dataHoraFim.value,
                status = status.value,
                observacoes = observacoes.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _agendamentos.update { lista -> lista + agendamento.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _agendamentos.update { lista ->
                    lista.map { item -> if (item.id == idNumber) agendamento.copy(id = idNumber) else item }
                }
            }
            mensagem.value = "Agendamento salvo com sucesso!"
            mandarMensagem("Agendamento salvo com sucesso!")
            limparCampos()
            
            backStack.clear()
            backStack.add(AgendaRotas.Listagem)

        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
            mandarMensagem("Erro ao salvar: ${e.message}")
        }
    }

    fun apagar(agendamento: Agenda) {
        _agendamentos.update { lista -> lista - agendamento }
        mandarMensagem("Agendamento apagado com sucesso!")
    }

    fun alterarStatus(agendamento: Agenda, novoStatus: String) {
        _agendamentos.update { lista ->
            lista.map { item ->
                if (item.id == agendamento.id) item.copy(status = novoStatus) else item
            }
        }
        mandarMensagem("Status alterado para $novoStatus!")
    }

    fun novoAgendamento() {
        backStack.clear()
        backStack.add(AgendaRotas.Formulario)
        id.value = ""
        paciente.value = ""
        psicologo.value = ""
        sala.value = ""
        tipoAtendimento.value = ""
        convenio.value = ""
        dataHoraInicio.value = ""
        dataHoraFim.value = ""
        status.value = "Confirmado"
        observacoes.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun novoAgendamentoComDataHora(data: String, hora: String) {
        novoAgendamento()
        val horaFim = when (hora) {
            "08:00" -> "09:00"
            "09:00" -> "10:00"
            "10:00" -> "11:00"
            "11:00" -> "12:00"
            "14:00" -> "15:00"
            "15:00" -> "16:00"
            "16:00" -> "17:00"
            "17:00" -> "18:00"
            else -> hora
        }
        dataHoraInicio.value = "$data $hora"
        dataHoraFim.value = "$data $horaFim"
    }

    fun editar(agendamento: Agenda) {
        backStack.clear()
        backStack.add(AgendaRotas.Formulario)
        id.value = agendamento.id.toString()
        paciente.value = agendamento.paciente
        psicologo.value = agendamento.psicologo
        sala.value = agendamento.sala
        tipoAtendimento.value = agendamento.tipoAtendimento
        convenio.value = agendamento.convenio
        dataHoraInicio.value = agendamento.dataHoraInicio
        dataHoraFim.value = agendamento.dataHoraFim
        status.value = agendamento.status
        observacoes.value = agendamento.observacoes
        ativo.value = agendamento.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        paciente.value = ""
        psicologo.value = ""
        sala.value = ""
        tipoAtendimento.value = ""
        convenio.value = ""
        dataHoraInicio.value = ""
        dataHoraFim.value = ""
        status.value = ""
        observacoes.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}