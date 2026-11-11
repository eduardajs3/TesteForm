package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.BloqueioAgendaAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.BloqueioAgenda
import edu.front.psi.clin.navigation.BloqueioAgendaRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BloqueioAgendaViewModel(
    private val mandarMensagem: (String) -> Unit,
) : ViewModel() {

    private val bloqueioApi = BloqueioAgendaAPI(httpClientManager())
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

    init {
        recarregar()
    }

    fun recarregar() {
        viewModelScope.launch {
            try {
                _bloqueios.value = bloqueioApi.carregar()
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar bloqueios do servidor: ${e.message}")
            }
        }
    }

    fun salvar() {
        // Valida todos os campos de uma vez utilizando a função da classe
        val v1 = psicologo.validar()
        val v2 = dataHoraInicio.validar()
        val v3 = dataHoraFim.validar()
        val v4 = motivo.validar()
        val v5 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val bloqueio = BloqueioAgenda(
                    id = id.value.toIntOrNull() ?: 0,
                    psicologo = psicologo.valor.value,
                    dataHoraInicio = dataHoraInicio.valor.value,
                    dataHoraFim = dataHoraFim.valor.value,
                    motivo = motivo.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    bloqueioApi.salvar(bloqueio)
                } else {
                    bloqueioApi.atualizar(id.value.toLong(), bloqueio)
                }
                _bloqueios.value = bloqueioApi.carregar()

                mensagem.value = "Bloqueio de agenda salvo com sucesso!"
                mandarMensagem("Bloqueio de agenda salvo com sucesso!")
                limparCampos()
                backStack.clear()
                backStack.add(BloqueioAgendaRotas.Listagem)
            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
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
                mandarMensagem("Erro ao apagar bloqueio: ${e.message}")
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