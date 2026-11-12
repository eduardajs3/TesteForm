package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.HorarioDisponivelAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HorarioDisponivelViewModel(
    private val mandarMensagem: (String) -> Unit
) : ViewModel() {

    private val httpClient = httpClientManager()
    private val horarioApi = HorarioDisponivelAPI(httpClient)
    private val psicologoApi = PsicologoAPI(httpClient)

    private val _horarios = MutableStateFlow<List<HorarioDisponivel>>(listOf())
    private val _psicologos = MutableStateFlow<List<Psicologo>>(listOf())
    val psicologos = _psicologos.asStateFlow()

    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _horarios.value = horarioApi.carregar()
                _psicologos.value = psicologoApi.carregar()
                println("Horários disponíveis carregados no init: ${_horarios.value}")
            } catch (e: Exception) {
                mandarMensagem("Erro ao carregar horários disponíveis do servidor: ${e.message}")
            }
        }
    }

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val horariosFiltrados = combine(_horarios, _filtro, _psicologos) { horarios, filtro, psicologos ->
        if (filtro.isEmpty()) {
            horarios
        } else {
            horarios.filter { horario ->
                val nomePsicologo = psicologos
                    .firstOrNull { it.id == horario.psicologoId }
                    ?.nome
                (nomePsicologo?.contains(filtro, ignoreCase = true) == true) ||
                    (horario.psicologo?.contains(filtro, ignoreCase = true) == true) ||
                    horario.diaSemana.contains(filtro, ignoreCase = true) ||
                    horario.horaInicio.contains(filtro, ignoreCase = true) ||
                    horario.horaFim.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val backStack = NavBackStack<HorarioDisponivelRotas>(
        HorarioDisponivelRotas.Listagem
    )

    val id = mutableStateOf("")

    val psicologo = CampoValidado()
    val diaSemana = CampoValidado()
    val horaInicio = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val horaFim = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val descricao = CampoValidado()
    val ativo = CampoValidado("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        val v1 = psicologo.validar()
        val v2 = diaSemana.validar()
        val v3 = horaInicio.validar()
        val v4 = horaFim.validar()
        val v5 = descricao.validar()
        val v6 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            try {
                val horario = HorarioDisponivel(
                    id = if (id.value.isEmpty()) null else id.value.toLong(),
                    psicologoId = psicologo.valor.value.toLongOrNull() ?: 1L,
                    diaSemana = diaSemana.valor.value,
                    horaInicio = if (horaInicio.valor.value.length == 5) "${horaInicio.valor.value}:00" else horaInicio.valor.value,
                    horaFim = if (horaFim.valor.value.length == 5) "${horaFim.valor.value}:00" else horaFim.valor.value,
                    descricao = descricao.valor.value.ifEmpty { null },
                    ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
                )

                if (id.value.isEmpty()) {
                    horarioApi.salvar(horario)
                } else {
                    val idNumber = id.value.toLong()
                    horarioApi.atualizar(idNumber, horario)
                }


                _horarios.value = horarioApi.carregar()
                println("Horários após salvar: ${_horarios.value}")

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Horário disponível salvo com sucesso!"
                } else {
                    "Horário disponível atualizado com sucesso!"
                }
                mensagem.value = mensagemSucesso
                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(HorarioDisponivelRotas.Listagem)

            } catch (e: Exception) {
                mensagem.value = "Erro ao salvar: ${e.message}"
                mandarMensagem("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun apagar(horario: HorarioDisponivel) {
        viewModelScope.launch {
            try {
                horarioApi.apagar(horario)
                _horarios.value = horarioApi.carregar()
                println("Horários após apagar: ${_horarios.value}")
                mandarMensagem("Horário apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem("Erro ao apagar: ${e.message}")
            }
        }
    }

    fun novoHorario() {
        backStack.clear()
        backStack.add(HorarioDisponivelRotas.Formulario)
        id.value = ""
        limparCampos()
        mensagem.value = ""
    }

    fun editar(horario: HorarioDisponivel) {
        backStack.clear()
        backStack.add(HorarioDisponivelRotas.Formulario)
        id.value = horario.id?.toString() ?: ""
        psicologo.definir(horario.psicologoId.toString())
        diaSemana.definir(horario.diaSemana)
        horaInicio.definir(horario.horaInicio)
        horaFim.definir(horario.horaFim)
        descricao.definir(horario.descricao ?: "")
        ativo.definir(horario.ativo.toString())
        mensagem.value = ""
    }

    fun limparCampos() {
        psicologo.limpar()
        diaSemana.limpar()
        horaInicio.limpar()
        horaFim.limpar()
        descricao.limpar()
        ativo.definir("true")
        mensagem.value = "Campos limpos."
    }
}