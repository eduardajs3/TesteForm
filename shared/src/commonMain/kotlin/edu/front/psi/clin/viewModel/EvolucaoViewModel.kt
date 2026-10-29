package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.EvolucaoClinica
import kotlinx.coroutines.flow.*

class EvolucaoClinicaViewModel : ViewModel() {

    private var idCounter: Int = 1

    private val _evolucoes = MutableStateFlow<List<EvolucaoClinica>>(
        listOf(
            EvolucaoClinica(
                id = 1,
                prontuario = "Maria Clara da Silva",
                psicologo = "Dra. Ana Costa",
                agenda = "Sessão 10/10/2023 - 14:00",
                dataRegistro = "10/10/2023 15:00",
                descricao = "Paciente apresentou melhora nos quadros de ansiedade social e relatou melhora na qualidade do sono.",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

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
            if (descricao.value.isBlank()) {
                mensagem.value = "A descrição da evolução é obrigatória!"
                return
            }

            val ehNovo = id.value.isEmpty()
            val novoId = if (ehNovo) ++idCounter else id.value.toInt()

            val statusBooleano = when (ativo.value.trim().lowercase()) {
                "false", "0", "inativo", "não", "nao" -> false
                else -> true
            }

            val evolucaoItem = EvolucaoClinica(
                id = novoId,
                prontuario = prontuario.value.trim().ifEmpty { "Paciente não informado" },
                psicologo = psicologo.value.trim().ifEmpty { "Dra. Ana Costa" },
                agenda = agenda.value.trim(),
                dataRegistro = dataRegistro.value.trim().ifEmpty { "Hoje" },
                descricao = descricao.value.trim(),
                ativo = statusBooleano
            )

            if (ehNovo) {
                _evolucoes.update { lista -> lista + evolucaoItem }
            } else {
                _evolucoes.update { lista ->
                    lista.map { item -> if (item.id == novoId) evolucaoItem else item }
                }
            }

            _filtro.value = "" // Limpa busca para o novo item aparecer
            limparCampos()
            mensagem.value = "Evolução clínica salva com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(evolucao: EvolucaoClinica) {
        _evolucoes.update { lista -> lista.filterNot { it.id == evolucao.id } }
    }

    fun novaEvolucao(nomePaciente: String = "") {
        limparCampos()
        if (nomePaciente.isNotEmpty()) {
            prontuario.value = nomePaciente
        }
        tela.value = "form"
    }

    fun editar(evolucao: EvolucaoClinica) {
        id.value = evolucao.id.toString()
        prontuario.value = evolucao.prontuario
        psicologo.value = evolucao.psicologo
        agenda.value = evolucao.agenda
        dataRegistro.value = evolucao.dataRegistro
        descricao.value = evolucao.descricao
        ativo.value = evolucao.ativo.toString()
        mensagem.value = ""
        tela.value = "form"
    }

    fun limparCampos() {
        id.value = ""
        prontuario.value = ""
        psicologo.value = ""
        agenda.value = ""
        dataRegistro.value = ""
        descricao.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }
}