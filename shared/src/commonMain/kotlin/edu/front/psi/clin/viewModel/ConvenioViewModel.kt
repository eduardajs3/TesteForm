package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.ConvenioAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.Convenio
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import edu.front.psi.clin.navigation.ConvenioRotas

class ConvenioViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val convenioApi = ConvenioAPI(httpClient)

    private val _convenios = MutableStateFlow<List<Convenio>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()
    val mensagem = mutableStateOf("")

    init {
        viewModelScope.launch {
            try {
                _convenios.value = convenioApi.carregar()
                println("Lista carregada no init: ${_convenios.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar convênios do servidor: ${e.message}")
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val conveniosFiltrados = combine(_convenios, _filtro) { convenios, filtro ->
        if (filtro.isEmpty()) {
            convenios
        } else {
            convenios.filter { it.nome.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack =
        NavBackStack<ConvenioRotas>(
            ConvenioRotas.Listagem
        )

    val id = mutableStateOf("")
    
    val nome = CampoValidado()
    val cnpj = CampoValidado(formatador = { MaskUtils.aplicarMascaraCnpj(it) })
    val telefone = CampoValidado(formatador = { MaskUtils.aplicarMascaraTelefone(it) })
    val percentualCobertura = CampoValidado(
        formatador = { MaskUtils.aplicarMascaraPercentual(it) }
    )
    val ativo = CampoValidado("true")

    fun salvar() {
        val v1 = nome.validar()
        val v2 = cnpj.validar()
        val v3 = telefone.validar()
        val v4 = percentualCobertura.validar()
        val v5 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val percentualDouble = percentualCobertura.valor.value
                    .replace("%", "")
                    .replace(",", ".")
                    .trim()
                    .toDoubleOrNull() ?: 0.0

                val convenio = Convenio(
                    id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                    nome = nome.valor.value,
                    cnpj = cnpj.valor.value,
                    telefone = telefone.valor.value,
                    percentualCobertura = percentualDouble,
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    convenioApi.salvar(convenio)
                } else {
                    val idNumber = id.value.toLong()
                    convenioApi.atualizar(idNumber, convenio)
                }
                _convenios.value = convenioApi.carregar()
                println("Lista após salvar: ${_convenios.value}")

                mensagem.value = "Convênio salvo com sucesso!"
                mandarMensagem("Convênio salvo com sucesso!")
                limparCampos()
         
                backStack.clear()
                backStack.add(ConvenioRotas.Listagem)
                
            } catch (e: Exception) {
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(convenio: Convenio) {
        viewModelScope.launch {
            try {
                convenioApi.apagar(convenio)
                _convenios.value = convenioApi.carregar()
                println("Lista após apagar: ${_convenios.value}")
                mandarMensagem("Convênio apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoConvenio() {
        id.value = ""
        limparCampos()
        backStack.clear()
        backStack.add(ConvenioRotas.Formulario)
    }

    fun editar(convenio: Convenio) {
        id.value = convenio.id.toString()
        nome.definir(convenio.nome)
        cnpj.definir(convenio.cnpj)
        telefone.definir(convenio.telefone)
        percentualCobertura.definir(convenio.percentualCobertura.toString())
        ativo.definir(convenio.ativo.toString())
        
        backStack.clear()
        backStack.add(ConvenioRotas.Formulario)
    }

    fun limparCampos() {
        nome.limpar()
        cnpj.limpar()
        telefone.limpar()
        percentualCobertura.limpar()
        ativo.definir("true")
    }
}