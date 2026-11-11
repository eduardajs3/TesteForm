package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.EvolucaoClinicaAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.EvolucaoClinica
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EvolucaoClinicaViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val evolucaoApi = EvolucaoClinicaAPI(httpClientManager())
    private val _evolucoes = MutableStateFlow<List<EvolucaoClinica>>(emptyList())

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        recarregar()
    }

    fun recarregar() {
        viewModelScope.launch {
            try {
                _evolucoes.value = evolucaoApi.carregar()
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar evoluções do servidor: ${e.message}")
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro.trim()
    }

    val evolucoesFiltradas = combine(_evolucoes, _filtro) { evolucoes, filtro ->
        if (filtro.isEmpty()) {
            evolucoes
        } else {
            evolucoes.filter {
                it.prontuario.contains(filtro, ignoreCase = true) ||
                it.psicologo.contains(filtro, ignoreCase = true) ||
                it.descricao.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<EvolucaoClinicaRotas>(
        EvolucaoClinicaRotas.Listagem
    )

    // Campos do formulário
    val id = mutableStateOf("")
    val prontuario = mutableStateOf("")
    val psicologo = mutableStateOf("")
    val agenda = mutableStateOf("")
    val dataRegistro = mutableStateOf("")
    val descricao = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        if (descricao.value.isBlank()) {
            mensagem.value = "A descrição da evolução é obrigatória!"
            mandarMensagem("A descrição da evolução é obrigatória!")
            return
        }

        viewModelScope.launch {
            try {
                val evolucaoItem = EvolucaoClinica(
                    id = id.value.toIntOrNull() ?: 0,
                    prontuario = prontuario.value.trim().ifEmpty { "Paciente não informado" },
                    psicologo = psicologo.value.trim().ifEmpty { "Dra. Ana Costa" },
                    agenda = agenda.value.trim(),
                    dataRegistro = dataRegistro.value.trim().ifEmpty { "Hoje" },
                    descricao = descricao.value.trim(),
                    ativo = ativo.value.trim().lowercase() !in listOf("false", "0", "inativo", "não", "nao")
                )

                if (id.value.isEmpty()) {
                    evolucaoApi.salvar(evolucaoItem)
                } else {
                    evolucaoApi.atualizar(id.value.toLong(), evolucaoItem)
                }
                _evolucoes.value = evolucaoApi.carregar()

                _filtro.value = ""
                mensagem.value = "Evolução clínica salva com sucesso!"
                mandarMensagem("Evolução clínica salva com sucesso!")
                limparCampos()
                backStack.clear()
                backStack.add(EvolucaoClinicaRotas.Listagem)
            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(evolucao: EvolucaoClinica) {
        viewModelScope.launch {
            try {
                evolucaoApi.apagar(evolucao)
                _evolucoes.value = evolucaoApi.carregar()
                mandarMensagem("Evolução clínica apagada com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar evolução: ${e.message}")
            }
        }
    }

    fun novaEvolucao(nomePaciente: String = "") {
        backStack.clear()
        backStack.add(EvolucaoClinicaRotas.Formulario)
        limparCampos()
        if (nomePaciente.isNotEmpty()) {
            prontuario.value = nomePaciente
        }
    }

    fun editar(evolucao: EvolucaoClinica) {
        backStack.clear()
        backStack.add(EvolucaoClinicaRotas.Formulario)
        id.value = evolucao.id.toString()
        prontuario.value = evolucao.prontuario
        psicologo.value = evolucao.psicologo
        agenda.value = evolucao.agenda
        dataRegistro.value = evolucao.dataRegistro
        descricao.value = evolucao.descricao
        ativo.value = evolucao.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        id.value = ""
        prontuario.value = ""
        psicologo.value = ""
        agenda.value = ""
        dataRegistro.value = ""
        descricao.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}