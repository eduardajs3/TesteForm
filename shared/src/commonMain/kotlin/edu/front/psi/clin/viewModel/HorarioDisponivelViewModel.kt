package edu.front.psi.clin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.api.HorarioDisponivelAPI
import edu.front.psi.clin.api.PsicologoAPI
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.api.httpClientManager
import edu.front.psi.clin.api.mensagemParaUsuario
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.model.enums.DiaSemana
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.security.PermissoesAcesso
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

    val backStack = NavBackStack<HorarioDisponivelRotas>(
        HorarioDisponivelRotas.Listagem
    )

    val id = mutableStateOf("")

    var psicologoId by mutableStateOf<Long?>(null)
        private set
    var erroPsicologo by mutableStateOf(false)
        private set

    val diaSemana = CampoValidado()
    val horaInicio = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val horaFim = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val descricao = CampoValidado()
    val ativo = CampoValidado("true")

    var salvando by mutableStateOf(false)
        private set

    private var carregamentoDadosFormulario: Job? = null
    private var ultimaSessao: String? = null

    init {
        SessionManager.aoAlterar {
            prepararParaSessaoAtual()
        }
        prepararParaSessaoAtual()
        carregarDadosFormulario(forcarAtualizacao = true)
    }

    private fun chaveSessao(): String =
        "${SessionManager.role.orEmpty()}|${SessionManager.email.orEmpty()}|${SessionManager.token}"

    fun prepararParaSessaoAtual() {
        val atual = chaveSessao()
        if (ultimaSessao != atual) {
            ultimaSessao = atual
            carregamentoDadosFormulario?.cancel()
            carregamentoDadosFormulario = null
            _horarios.value = emptyList()
            _psicologos.value = emptyList()
            _filtro.value = ""
            if (id.value.isEmpty()) {
                psicologoId = null
                erroPsicologo = false
            }
        }
    }

    fun carregarDadosFormulario(forcarAtualizacao: Boolean = false) {
        prepararParaSessaoAtual()
        if (!forcarAtualizacao && carregamentoDadosFormulario?.isActive == true) return

        carregamentoDadosFormulario?.cancel()
        carregamentoDadosFormulario = viewModelScope.launch {
            try {
                val horariosAtualizados = horarioApi.carregar()
                _horarios.value = horariosAtualizados
            } catch (e: Exception) {
                mandarMensagem(e.mensagemParaUsuario("Erro ao carregar horários disponíveis do servidor"))
            }

            try {
                val psicologosAtualizados = psicologoApi.carregar()
                _psicologos.value = psicologosAtualizados

                val psicologoSelecionadoId = psicologoId
                if (
                    id.value.isEmpty() &&
                    psicologoSelecionadoId != null &&
                    psicologosAtualizados.none {
                        it.id == psicologoSelecionadoId && it.ativo
                    }
                ) {
                    psicologoId = null
                }

                if (id.value.isEmpty() && psicologoId == null) {
                    preencherPsicologoSeNecessario()
                }
            } catch (_: Exception) {
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
                val rotuloDia = DiaSemana.deValorApi(horario.diaSemana).rotulo
                (nomePsicologo?.contains(filtro, ignoreCase = true) == true) ||
                    rotuloDia.contains(filtro, ignoreCase = true) ||
                    horario.diaSemana.contains(filtro, ignoreCase = true) ||
                    horario.horaInicio.contains(filtro, ignoreCase = true) ||
                    horario.horaFim.contains(filtro, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selecionarPsicologo(idSelecionado: Long?) {
        if (PermissoesAcesso.ehPsicologo(SessionManager.role) && id.value.isEmpty()) {
            preencherPsicologoSeNecessario()
            return
        }
        psicologoId = idSelecionado
        erroPsicologo = idSelecionado == null
    }

    fun salvar() {
        if (salvando) return

        val psicologoSelecionado = psicologoId
        erroPsicologo = psicologoSelecionado == null

        val v2 = diaSemana.validar()
        val v3 = horaInicio.validar()
        val v4 = horaFim.validar()
        val v6 = ativo.validar()

        if (erroPsicologo || !(v2 && v3 && v4 && v6)) {
            mandarMensagem(
                if (erroPsicologo && v2 && v3 && v4 && v6) {
                    "Selecione um psicólogo."
                } else {
                    "Preencha todos os campos obrigatórios."
                }
            )
            return
        }

        viewModelScope.launch {
            salvando = true
            try {
                val diaEnum = DiaSemana.deValorApi(diaSemana.valor.value)
                val horario = HorarioDisponivel(
                    id = id.value.toLongOrNull(),
                    psicologoId = psicologoSelecionado!!,
                    diaSemana = diaEnum.name,
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
        preencherPsicologoSeNecessario()
    }

    fun editar(horario: HorarioDisponivel) {
        backStack.clear()
        backStack.add(HorarioDisponivelRotas.Formulario)
        id.value = horario.id?.toString() ?: ""
        psicologoId = horario.psicologoId
        erroPsicologo = false
        diaSemana.definir(DiaSemana.deValorApi(horario.diaSemana).name)
        horaInicio.definir(horario.horaInicio.take(5))
        horaFim.definir(horario.horaFim.take(5))
        descricao.definir(horario.descricao ?: "")
        ativo.definir(horario.ativo.toString())
    }

    private fun preencherPsicologoSeNecessario() {
        if (!PermissoesAcesso.ehPsicologo(SessionManager.role)) return
        val emailSessao = SessionManager.email?.trim().orEmpty()
        if (emailSessao.isEmpty()) return
        val idEncontrado = _psicologos.value
            .firstOrNull { it.email.trim().equals(emailSessao, ignoreCase = true) }
            ?.id
        if (idEncontrado != null) {
            psicologoId = idEncontrado
            erroPsicologo = false
        }
    }

    fun limparCampos() {
        psicologoId = null
        erroPsicologo = false
        diaSemana.limpar()
        horaInicio.limpar()
        horaFim.limpar()
        descricao.limpar()
        ativo.definir("true")
    }
}