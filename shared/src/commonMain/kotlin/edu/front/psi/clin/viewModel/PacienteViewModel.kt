package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.front.psi.clin.Paciente
import edu.front.psi.clin.ui.components.CampoValidado
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

    // Campos do formulário geridos pelo validador
    val id = mutableStateOf("")
    val nome = CampoValidado()
    val cpf = CampoValidado()
    val dataNascimento = CampoValidado()
    val email = CampoValidado()
    val telefone = CampoValidado()
    val endereco = CampoValidado()
    val observacoes = CampoValidado() // Opcional
    val ativo = CampoValidado("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios (observações é opcional)
        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = dataNascimento.validar()
        val v4 = email.validar()
        val v5 = telefone.validar()
        val v6 = endereco.validar()
        val v7 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7)) {
            mensagem.value = "Por favor, preencha todos os campos obrigatórios."
            return
        }

        try {
            val paciente = Paciente(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                nome = nome.valor.value,
                cpf = cpf.valor.value,
                dataNascimento = dataNascimento.valor.value,
                email = email.valor.value,
                telefone = telefone.valor.value,
                endereco = endereco.valor.value,
                observacoes = observacoes.valor.value,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
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
            limparCampos()
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
        limparCampos()
        mensagem.value = ""
    }

    fun editar(paciente: Paciente) {
        tela.value = "form"
        id.value = paciente.id.toString()
        nome.definir(paciente.nome)
        cpf.definir(paciente.cpf)
        dataNascimento.definir(paciente.dataNascimento)
        email.definir(paciente.email)
        telefone.definir(paciente.telefone)
        endereco.definir(paciente.endereco)
        observacoes.definir(paciente.observacoes)
        ativo.definir(paciente.ativo.toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        cpf.limpar()
        dataNascimento.limpar()
        email.limpar()
        telefone.limpar()
        endereco.limpar()
        observacoes.limpar()
        ativo.definir("true")
        mensagem.value = "Campos limpos."
    }
}