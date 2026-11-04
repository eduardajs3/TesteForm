package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.Agenda
import edu.front.psi.clin.navigation.AgendaRotas
import kotlinx.coroutines.flow.*

class AgendaViewModel(
    private val mandarMensagem: (String, SnackbarDuration) -> Unit
) : ViewModel() {

    private var idCounter: Int = 2

    private val _agendamentos = MutableStateFlow<List<Agenda>>(
        listOf(
            Agenda(
                id = 1,
                paciente = "João Silva",
                psicologo = "Dra. Ana Costa",
                sala = "Sala 02",
                tipoAtendimento = "Presencial",
                convenio = "Unimed",
                dataHoraInicio = "10/10/2023 14:00",
                dataHoraFim = "10/10/2023 15:00",
                status = "Confirmado",
                observacoes = "Primeira consulta",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val agendamentosFiltrados = combine(_agendamentos, _filtro) { agendamentos, filtro ->
        if (filtro.isEmpty()) {
            agendamentos
        } else {
            agendamentos.filter {
                it.paciente.contains(filtro, ignoreCase = true) ||
                it.psicologo.contains(filtro, ignoreCase = true) ||
                it.status.contains(filtro, ignoreCase = true)
            }
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
            mandarMensagem("Agendamento salvo com sucesso!", SnackbarDuration.Short)
            limparCampos()
            
            backStack.clear()
            backStack.add(AgendaRotas.Listagem)

        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
            mandarMensagem("Erro ao salvar: ${e.message}", SnackbarDuration.Long)
        }
    }

    fun apagar(agendamento: Agenda) {
        _agendamentos.update { lista -> lista - agendamento }
        mandarMensagem("Agendamento apagado com sucesso!", SnackbarDuration.Short)
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
        status.value = ""
        observacoes.value = ""
        ativo.value = "true"
        mensagem.value = ""
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