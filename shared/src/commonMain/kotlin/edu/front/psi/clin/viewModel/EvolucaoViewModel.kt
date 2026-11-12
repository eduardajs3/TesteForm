package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.model.EvolucaoClinica
import edu.front.psi.clin.navigation.EvolucaoClinicaRotas
import kotlinx.coroutines.flow.*

class EvolucaoClinicaViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

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

    val backStack = NavBackStack<EvolucaoClinicaRotas>(
        EvolucaoClinicaRotas.Listagem
    )


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
                mandarMensagem("A descrição da evolução é obrigatória!")
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

            _filtro.value = ""
            val mensagemSucesso = if (ehNovo) {
                "Evolução clínica salva com sucesso!"
            } else {
                "Evolução clínica atualizada com sucesso!"
            }
            mensagem.value = mensagemSucesso
            mandarMensagem(mensagemSucesso)
            limparCampos()

            backStack.clear()
            backStack.add(EvolucaoClinicaRotas.Listagem)

        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
            mandarMensagem("Erro ao salvar: ${e.message}")
        }
    }

    fun apagar(evolucao: EvolucaoClinica) {
        _evolucoes.update { lista -> lista.filterNot { it.id == evolucao.id } }
        mandarMensagem("Evolução clínica apagada com sucesso!")
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