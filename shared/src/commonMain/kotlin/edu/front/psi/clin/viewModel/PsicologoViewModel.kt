package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.navigation.PsicologoRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PsicologoViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val psicologoApi = PsicologoAPI(httpClient)

    private val _psicologos = MutableStateFlow<List<Psicologo>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _psicologos.value = psicologoApi.carregar()
                println("Psicólogos carregados no init: ${_psicologos.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar psicólogos do servidor: ${e.message}")
            }
        }
    }

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

    val backStack = NavBackStack<PsicologoRotas>(
        PsicologoRotas.Listagem
    )
    val id = mutableStateOf("")
    val nome = CampoValidado()
    val cpf = CampoValidado(formatador = { MaskUtils.aplicarMascaraCpf(it) })
    val email = CampoValidado(
        validadorExtra = { MaskUtils.validarEmail(it) },
        mensagemErroPersonalizada = "E-mail inválido"
    )
    val telefone = CampoValidado(formatador = { MaskUtils.aplicarMascaraTelefone(it) })
    val senha = CampoValidado()
    val crp = CampoValidado(formatador = { MaskUtils.aplicarMascaraCrp(it) })
    val especialidade = CampoValidado()
    val ativo = CampoValidado("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        val v1 = nome.validar()
        val v2 = cpf.validar()
        val v3 = email.validar()
        val v4 = telefone.validar()
        val v5 = senha.validar()
        val v6 = crp.validar()
        val v7 = especialidade.validar()
        val v8 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6 && v7 && v8)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val psicologo = Psicologo(
                    id = id.value.toLongOrNull(),
                    nome = nome.valor.value,
                    cpf = cpf.valor.value,
                    email = email.valor.value,
                    telefone = telefone.valor.value,
                    senha = senha.valor.value,
                    crp = crp.valor.value,
                    especialidade = especialidade.valor.value,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    psicologoApi.salvar(psicologo)
                } else {
                    val idNumber = id.value.toLong()
                    psicologoApi.atualizar(idNumber, psicologo)
                }

                // Recarrega a lista direto da API
                _psicologos.value = psicologoApi.carregar()
                println("Psicólogos após salvar: ${_psicologos.value}")

                mensagem.value = "Psicólogo salvo com sucesso!"
                mandarMensagem("Psicólogo salvo com sucesso!")
                limparCampos()
                
                backStack.clear()
                backStack.add(PsicologoRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(psicologo: Psicologo) {
        viewModelScope.launch {
            try {
                psicologoApi.apagar(psicologo)
                _psicologos.value = psicologoApi.carregar()
                println("Psicólogos após apagar: ${_psicologos.value}")
                mandarMensagem("Psicólogo apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoPsicologo() {
        backStack.clear()
        backStack.add(PsicologoRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(psicologo: Psicologo) {
        backStack.clear()
        backStack.add(PsicologoRotas.Formulario)
        id.value = psicologo.id.toString()
        nome.definir(psicologo.nome)
        cpf.definir(psicologo.cpf)
        email.definir(psicologo.email)
        telefone.definir(psicologo.telefone)
        senha.definir(psicologo.senha ?: "")
        crp.definir(psicologo.crp)
        especialidade.definir(psicologo.especialidade)
        ativo.definir(psicologo.ativo.toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        nome.limpar()
        cpf.limpar()
        email.limpar()
        telefone.limpar()
        senha.limpar()
        crp.limpar()
        especialidade.limpar()
        ativo.definir("true")
        mensagem.value = "Campos limpos."
    }
}