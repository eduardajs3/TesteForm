package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.BloqueioAgenda
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.ui.components.CampoValidado
import kotlinx.coroutines.flow.*

class BloqueioAgendaViewModel(
    private val mandarMensagem: (String, SnackbarDuration) -> Unit
) : ViewModel() {

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

    val backStack = NavBackStack<BloqueioAgendaRotas>(
        BloqueioAgendaRotas.Listagem
    )

    val id = mutableStateOf("")
    
    // Campos geridos pelo validador
    val psicologo = CampoValidado()
    val dataHoraInicio = CampoValidado()
    val dataHoraFim = CampoValidado()
    val motivo = CampoValidado()
    val ativo = CampoValidado("true")
    
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Valida todos os campos de uma vez utilizando a função da classe
        val v1 = psicologo.validar()
        val v2 = dataHoraInicio.validar()
        val v3 = dataHoraFim.validar()
        val v4 = motivo.validar()
        val v5 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.", SnackbarDuration.Short)
            return
        }

        try {
            val bloqueio = BloqueioAgenda(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                psicologo = psicologo.valor.value,
                dataHoraInicio = dataHoraInicio.valor.value,
                dataHoraFim = dataHoraFim.valor.value,
                motivo = motivo.valor.value,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
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
            mandarMensagem("Bloqueio de agenda salvo com sucesso!", SnackbarDuration.Short)
            limparCampos()
            
            backStack.clear()
            backStack.add(BloqueioAgendaRotas.Listagem)

        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
            mandarMensagem("Erro ao salvar: ${e.message}", SnackbarDuration.Long)
        }
    }

    fun apagar(bloqueio: BloqueioAgenda) {
        _bloqueios.update { lista -> lista - bloqueio }
        mandarMensagem("Bloqueio apagado com sucesso!", SnackbarDuration.Short)
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
        id.value = bloqueio.id.toString()
        psicologo.definir(bloqueio.psicologo)
        dataHoraInicio.definir(bloqueio.dataHoraInicio)
        dataHoraFim.definir(bloqueio.dataHoraFim)
        motivo.definir(bloqueio.motivo)
        ativo.definir(bloqueio.ativo.toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        psicologo.limpar()
        dataHoraInicio.limpar()
        dataHoraFim.limpar()
        motivo.limpar()
        ativo.definir("true")
        mensagem.value = "Campos limpos."
    }
}