package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.HorarioDisponivelAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.ui.componentes.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.Job
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
    private var carregamentoDadosFormulario: Job? = null

    var salvando by mutableStateOf(false)
        private set

    init {
        carregarDadosFormulario()
    }

    fun carregarDadosFormulario() {
        if (carregamentoDadosFormulario?.isActive == true) return

        carregamentoDadosFormulario = viewModelScope.launch {
            try {
                val horariosAtualizados = horarioApi.carregar()
                val psicologosAtualizados = psicologoApi.carregar()

                _horarios.value = horariosAtualizados
                _psicologos.value = psicologosAtualizados
                val psicologoSelecionadoId = psicologo.valor.value.toLongOrNull()
                if (
                    id.value.isEmpty() &&
                    psicologoSelecionadoId != null &&
                    psicologosAtualizados.none {
                        it.id == psicologoSelecionadoId && it.ativo
                    }
                ) {
                    psicologo.limpar()
                }
                println("Horários disponíveis carregados no init: ${_horarios.value}")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar horários disponíveis do servidor"))
            } finally {
                carregamentoDadosFormulario = null
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
                    horario.psicologo.contains(filtro, ignoreCase = true) ||
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

    fun salvar() {
        if (salvando) return
        val v1 = psicologo.validar()
        val v2 = diaSemana.validar()
        val v3 = horaInicio.validar()
        val v4 = horaFim.validar()
        val v6 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v6)) {
            mandarMensagem("Preencha todos os campos obrigatórios.")
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val ativoValido = ativo.valor.value.toBooleanStrictOrNull()
                if (ativoValido == null) {
                    mandarMensagem("Selecione um status válido para o horário.")
                    return@launch
                }

                val psicologoIdValor = psicologo.valor.value.toLongOrNull() ?: 1L
                val nomePsicologoSelecionado = _psicologos.value
                    .firstOrNull { it.id == psicologoIdValor }
                    ?.nome
                    ?: "Psicólogo #$psicologoIdValor"

                val horario = HorarioDisponivel(
                    id = if (id.value.isEmpty()) null else id.value.toLong(),
                    psicologoId = psicologoIdValor,
                    psicologo = nomePsicologoSelecionado,
                    diaSemana = diaSemana.valor.value,
                    horaInicio = if (horaInicio.valor.value.length == 5) "${horaInicio.valor.value}:00" else horaInicio.valor.value,
                    horaFim = if (horaFim.valor.value.length == 5) "${horaFim.valor.value}:00" else horaFim.valor.value,
                    descricao = descricao.valor.value.ifEmpty { null },
                    ativo = ativoValido
                )

                if (id.value.isEmpty()) {
                    horarioApi.salvar(horario)
                } else {
                    val idNumber = id.value.toLong()
                    horarioApi.atualizar(idNumber, horario)
                }

                _horarios.value = horarioApi.carregar()

                val mensagemSucesso = if (id.value.isEmpty()) {
                    "Horário disponível salvo com sucesso!"
                } else {
                    "Horário disponível atualizado com sucesso!"
                }

                mandarMensagem(mensagemSucesso)
                limparCampos()

                backStack.clear()
                backStack.add(HorarioDisponivelRotas.Listagem)

            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao salvar"))
            } finally {
                salvando = false
            }
        }
    }

    fun apagar(horario: HorarioDisponivel) {
        viewModelScope.launch {
            try {
                horarioApi.apagar(horario)
                _horarios.value = horarioApi.carregar()
                mandarMensagem("Horário apagado com sucesso!")
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao apagar"))
            }
        }
    }

    fun novoHorario() {
        backStack.clear()
        backStack.add(HorarioDisponivelRotas.Formulario)
        id.value = ""
        limparCampos()
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
    }

    fun limparCampos() {
        psicologo.limpar()
        diaSemana.limpar()
        horaInicio.limpar()
        horaFim.limpar()
        descricao.limpar()
        ativo.definir("true")
    }
}