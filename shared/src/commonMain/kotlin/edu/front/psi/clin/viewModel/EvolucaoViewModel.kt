package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.EvolucaoClinica
import kotlinx.coroutines.flow.*

class EvolucaoClinicaViewModel : ViewModel() {

    private var idCounter: Int = 2

    private val _evolucoes = MutableStateFlow<List<EvolucaoClinica>>(
        listOf(
            EvolucaoClinica(
                id = 1,
                prontuario = "Prontuário #001",
                psicologo = "Dra. Ana Costa",
                agenda = "Sessão 10/10/2023 - 14:00",
                dataRegistro = "10/10/2023 15:00",
                descricao = "Paciente apresentou melhora nos quadros de ansiedade.",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
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

    val tela = mutableStateOf("list") // "list" ou "form"

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
        try {
            val evolucao = EvolucaoClinica(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                prontuario = prontuario.value,
                psicologo = psicologo.value,
                agenda = agenda.value,
                dataRegistro = dataRegistro.value,
                descricao = descricao.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _evolucoes.update { lista -> lista + evolucao.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _evolucoes.update { lista ->
                    lista.map { item -> if (item.id == idNumber) evolucao.copy(id = idNumber) else item }
                }
            }
            mensagem.value = "Evolução clínica salva com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(evolucao: EvolucaoClinica) {
        _evolucoes.update { lista -> lista - evolucao }
    }

    fun novaEvolucao() {
        tela.value = "form"
        id.value = ""
        prontuario.value = ""
        psicologo.value = ""
        agenda.value = ""
        dataRegistro.value = ""
        descricao.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(evolucao: EvolucaoClinica) {
        tela.value = "form"
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
        prontuario.value = ""
        psicologo.value = ""
        agenda.value = ""
        dataRegistro.value = ""
        descricao.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}