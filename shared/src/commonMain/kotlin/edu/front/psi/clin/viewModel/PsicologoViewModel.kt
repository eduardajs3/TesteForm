package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Psicologo
import kotlinx.coroutines.flow.*

class PsicologoViewModel : ViewModel() {

    private var idCounter: Int = 2

    private val _psicologos = MutableStateFlow<List<Psicologo>>(
        listOf(
            Psicologo(
                id = 1,
                nome = "Dra. Ana Costa",
                cpf = "987.654.321-00",
                email = "ana.costa@email.com",
                telefone = "(11) 97777-6666",
                senha = "123",
                crp = "06/123456",
                especialidade = "Terapia Cognitivo-Comportamental",
                ativo = true
            )
        )
    )

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val psicologosFiltrados = combine(_psicologos, _filtro) { psicologos, filtro ->
        if (filtro.isEmpty()) {
            psicologos
        } else {
            psicologos.filter {
                it.nome.contains(filtro, ignoreCase = true) ||
                it.crp.contains(filtro, ignoreCase = true) ||
                it.especialidade.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    // Campos do formulário
    val id = mutableStateOf("")
    val nome = mutableStateOf("")
    val cpf = mutableStateOf("")
    val email = mutableStateOf("")
    val telefone = mutableStateOf("")
    val senha = mutableStateOf("")
    val crp = mutableStateOf("")
    val especialidade = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        try {
            val psicologo = Psicologo(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                cpf = cpf.value,
                email = email.value,
                telefone = telefone.value,
                senha = senha.value,
                crp = crp.value,
                especialidade = especialidade.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _psicologos.update { lista -> lista + psicologo.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _psicologos.update { lista ->
                    lista.map { item -> if (item.id == idNumber) psicologo.copy(id = idNumber) else item }
                }
            }
            mensagem.value = "Psicólogo salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(psicologo: Psicologo) {
        _psicologos.update { lista -> lista - psicologo }
    }

    fun novoPsicologo() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        cpf.value = ""
        email.value = ""
        telefone.value = ""
        senha.value = ""
        crp.value = ""
        especialidade.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(psicologo: Psicologo) {
        tela.value = "form"
        id.value = psicologo.id.toString()
        nome.value = psicologo.nome
        cpf.value = psicologo.cpf
        email.value = psicologo.email
        telefone.value = psicologo.telefone
        senha.value = psicologo.senha
        crp.value = psicologo.crp
        especialidade.value = psicologo.especialidade
        ativo.value = psicologo.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.value = ""
        cpf.value = ""
        email.value = ""
        telefone.value = ""
        senha.value = ""
        crp.value = ""
        especialidade.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}