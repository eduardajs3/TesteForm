package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Paciente
import kotlinx.coroutines.flow.*

class PacienteViewModel : ViewModel() {

    private var idCounter: Int = 2
    
    private val _pacientes = MutableStateFlow<List<Paciente>>(listOf(
        Paciente(1, "João Silva", "111.222.333-44", "01/01/1990", "joao@email.com", "(11) 99999-9999", "Rua A, 123", "Paciente estável", true)
    ))
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val pacientesFiltrados = combine(_pacientes, _filtro) { pacientes, filtro ->
        if (filtro.isEmpty()) {
            pacientes
        } else {
            pacientes.filter { it.nome.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tela = mutableStateOf("list") // "list" ou "form"

    // Campos do formulário
    val id = mutableStateOf("")
    val nome = mutableStateOf("")
    val cpf = mutableStateOf("")
    val dataNascimento = mutableStateOf("")
    val email = mutableStateOf("")
    val telefone = mutableStateOf("")
    val endereco = mutableStateOf("")
    val observacoes = mutableStateOf("")
    val ativo = mutableStateOf("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação: apenas observacoes não é obrigatório
        if (nome.value.isBlank() ||
            cpf.value.isBlank() ||
            dataNascimento.value.isBlank() ||
            email.value.isBlank() ||
            telefone.value.isBlank() ||
            endereco.value.isBlank() ||
            ativo.value.isBlank()
        ) {
            mensagem.value = "Por favor, preencha todos os campos obrigatórios."
            return
        }

        try {
            val paciente = Paciente(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.value,
                cpf = cpf.value,
                dataNascimento = dataNascimento.value,
                email = email.value,
                telefone = telefone.value,
                endereco = endereco.value,
                observacoes = observacoes.value,
                ativo = ativo.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _pacientes.update { lista -> lista + paciente.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _pacientes.update { lista ->
                    lista.map { prj -> if (prj.id == idNumber) paciente.copy(id = idNumber) else prj }
                }
            }
            mensagem.value = "Paciente salvo com sucesso!"
            tela.value = "list"
        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
        }
    }

    fun apagar(paciente: Paciente) {
        _pacientes.update { lista -> lista - paciente }
    }

    fun novoPaciente() {
        tela.value = "form"
        id.value = ""
        nome.value = ""
        cpf.value = ""
        dataNascimento.value = ""
        email.value = ""
        telefone.value = ""
        endereco.value = ""
        observacoes.value = ""
        ativo.value = "true"
        mensagem.value = ""
    }

    fun editar(paciente: Paciente) {
        tela.value = "form"
        id.value = paciente.id.toString()
        nome.value = paciente.nome
        cpf.value = paciente.cpf
        dataNascimento.value = paciente.dataNascimento
        email.value = paciente.email
        telefone.value = paciente.telefone
        endereco.value = paciente.endereco
        observacoes.value = paciente.observacoes
        ativo.value = paciente.ativo.toString()
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.value = ""
        cpf.value = ""
        dataNascimento.value = ""
        email.value = ""
        telefone.value = ""
        endereco.value = ""
        observacoes.value = ""
        ativo.value = "true"
        mensagem.value = "Campos limpos."
    }
}