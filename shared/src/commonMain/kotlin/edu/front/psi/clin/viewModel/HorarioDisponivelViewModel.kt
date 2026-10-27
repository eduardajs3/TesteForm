package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.HorarioDisponivel // Ajuste para o pacote correto do seu Model
import kotlinx.coroutines.flow.*

class HorarioDisponivelViewModel : ViewModel() {

    private var idCounter: Int = 0
    private val _horarios = MutableStateFlow<List<HorarioDisponivel>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val horariosFiltrados = combine(_horarios, _filtro) { horarios, filtro ->
        if (filtro.isEmpty()) {
            horarios
        } else {
            horarios.filter { it.psicologo.contains(filtro, ignoreCase = true) || it.diaSemana.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    val id = mutableStateOf("")
    val psicologo = mutableStateOf("")
    val diaSemana = mutableStateOf("")
    val horaInicio = mutableStateOf("")
    val horaFim = mutableStateOf("")
    val descricao = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val horario = HorarioDisponivel(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                psicologo = psicologo.value,
                diaSemana = diaSemana.value,
                horaInicio = horaInicio.value,
                horaFim = horaFim.value,
                descricao = descricao.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _horarios.update { lista -> lista + horario.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _horarios.update { lista ->
                    lista.map { h -> if (h.id == idNumber) horario.copy(id = idNumber) else h }
                }
            }
            mensagem.value = "Horário disponível salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(horario: HorarioDisponivel) {
        _horarios.update { lista -> lista - horario }
    }

    fun novoHorario() {
        tela.value = "form"
        id.value = ""
        psicologo.value = ""
        diaSemana.value = ""
        horaInicio.value = ""
        horaFim.value = ""
        descricao.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(horario: HorarioDisponivel) {
        tela.value = "form"
        id.value = horario.id.toString()
        psicologo.value = horario.psicologo
        diaSemana.value = horario.diaSemana
        horaInicio.value = horario.horaInicio
        horaFim.value = horario.horaFim
        descricao.value = horario.descricao
        ativo.value = horario.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        psicologo.value = ""
        diaSemana.value = ""
        horaInicio.value = ""
        horaFim.value = ""
        descricao.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}