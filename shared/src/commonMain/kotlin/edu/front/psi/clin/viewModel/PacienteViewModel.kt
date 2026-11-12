package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.PacienteAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.navigation.PacienteRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PacienteViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val pacienteApi = PacienteAPI(httpClient)

    private val _pacientes = MutableStateFlow<List<Paciente>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _pacientes.value = pacienteApi.carregar()
                println("Pacientes carregados no init: ${_pacientes.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar pacientes do servidor: ${e.message}")
            }
        }
    }

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

    val backStack = NavBackStack<PacienteRotas>(
        PacienteRotas.Listagem
    )
     
    // Campos do formulário
    val id = mutableStateOf("")
    val nome = CampoValidado()
    val cpf = CampoValidado(formatador = { MaskUtils.aplicarMascaraCpf(it) })
    val dataNascimento = CampoValidado()
    
    val email = CampoValidado(
        validadorExtra = { MaskUtils.validarEmail(it) },
        mensagemErroPersonalizada = "E-mail inválido"
    )
    
    val telefone = CampoValidado(formatador = { MaskUtils.aplicarMascaraTelefone(it) })
    val endereco = CampoValidado()
    val observacoes = CampoValidado() // Opcional
    val ativo = CampoValidado("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = dataNascimento.validar()
        val v4 = email.validar()
        val v5 = telefone.validar()
        val v6 = endereco.validar()
        val v7 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7)) {
            mensagem.value = "Por favor, corrija os erros no formulário."
            mandarMensagem("Por favor, corrija os erros no formulário.")
            return
        }
        val partes = dataNascimento.valor.value.split("/")

        val dataFormatada = if (partes.size == 3) {
            "${partes[2]}-${partes[1]}-${partes[0]}"
        } else {
            dataNascimento.valor.value
        }
        viewModelScope.launch {
            try {
                val paciente = Paciente(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    cpf = cpf.valor.value,
                    dataNascimento = dataFormatada,
                    email = email.valor.value,
                    telefone = telefone.valor.value,
                    endereco = endereco.valor.value,
                    observacoes = observacoes.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    pacienteApi.salvar(paciente)
                } else {
                    val idNumber = id.value.toLong()
                    pacienteApi.atualizar(idNumber, paciente)
                }

                // Recarrega a lista direto da API do backend
                _pacientes.value = pacienteApi.carregar()
                
                mensagem.value = "Paciente salvo com sucesso!"
                mandarMensagem("Paciente salvo com sucesso!")
                limparCampos()
                
                backStack.clear()
                backStack.add(PacienteRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(paciente: Paciente) {
        viewModelScope.launch {
            try {
                pacienteApi.apagar(paciente)
                _pacientes.value = pacienteApi.carregar()
                mandarMensagem("Paciente apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoPaciente() {
        backStack.clear()
        backStack.add(PacienteRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(paciente: Paciente) {
        backStack.clear()
        backStack.add(PacienteRotas.Formulario)
        id.value = paciente.id.toString()
        nome.definir(paciente.nome)
        cpf.definir(paciente.cpf)
        dataNascimento.definir(paciente.dataNascimento)
        email.definir(paciente.email)
        telefone.definir(paciente.telefone)
        endereco.definir(paciente.endereco)
        observacoes.definir(
            paciente.observacoes
        )
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