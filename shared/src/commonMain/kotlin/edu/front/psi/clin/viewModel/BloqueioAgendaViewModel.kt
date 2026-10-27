package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.BloqueioAgenda // Ajuste para o pacote correto do seu Model
import kotlinx.coroutines.flow.*

class BloqueioAgendaViewModel : ViewModel() {

    private var idCounter: Int = 0
    private val _bloqueios = MutableStateFlow<List<BloqueioAgenda>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val bloqueiosFiltrados = combine(_bloqueios, _filtro) { bloqueios, filtro ->
        if (filtro.isEmpty()) {
            bloqueios
        } else {
            bloqueios.filter { it.psicologo.contains(filtro, ignoreCase = true) || it.motivo.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    val id = mutableStateOf("")
    val psicologo = mutableStateOf("")
    val dataHoraInicio = mutableStateOf("")
    val dataHoraFim = mutableStateOf("")
    val motivo = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val bloqueio = BloqueioAgenda(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                psicologo = psicologo.value,
                dataHoraInicio = dataHoraInicio.value,
                dataHoraFim = dataHoraFim.value,
                motivo = motivo.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _bloqueios.update { lista -> lista + bloqueio.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _bloqueios.update { lista ->
                    lista.map { b -> if (b.id == idNumber) bloqueio.copy(id = idNumber) else b }
                }
            }
            mensagem.value = "Bloqueio de agenda salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(bloqueio: BloqueioAgenda) {
        _bloqueios.update { lista -> lista - bloqueio }
    }

    fun novoBloqueio() {
        tela.value = "form"
        id.value = ""
        psicologo.value = ""
        dataHoraInicio.value = ""
        dataHoraFim.value = ""
        motivo.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(bloqueio: BloqueioAgenda) {
        tela.value = "form"
        id.value = bloqueio.id.toString()
        psicologo.value = bloqueio.psicologo
        dataHoraInicio.value = bloqueio.dataHoraInicio
        dataHoraFim.value = bloqueio.dataHoraFim
        motivo.value = bloqueio.motivo
        ativo.value = bloqueio.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        psicologo.value = ""
        dataHoraInicio.value = ""
        dataHoraFim.value = ""
        motivo.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}