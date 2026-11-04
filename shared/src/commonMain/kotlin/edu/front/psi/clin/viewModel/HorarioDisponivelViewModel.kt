package edu.front.psi.clin.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import edu.front.psi.clin.HorarioDisponivel
import edu.front.psi.clin.navigation.HorarioDisponivelRotas
import edu.front.psi.clin.ui.components.CampoValidado
import edu.front.psi.clin.utils.MaskUtils
import kotlinx.coroutines.flow.*

class HorarioDisponivelViewModel(
    private val mandarMensagem: (String, SnackbarDuration) -> Unit
) : ViewModel() {

    private var idCounter: Int = 0
    private val _horarios = MutableStateFlow<List<HorarioDisponivel>>(listOf())
    
    private val _filtro = MutableStateFlow("")
    val filtro = _filtro.asStateFlow()

    fun atualizarFiltro(novoFiltro: String) {
        _filtro.value = novoFiltro
    }

    val horariosFiltrados = combine(_horarios, _filtro) { horarios, filtro ->
        if (filtro.isEmpty()) {
            horarios
        } else {
            horarios.filter { it.psicologo.contains(filtro, ignoreCase = true) || it.diaSemana.contains(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val backStack = NavBackStack<HorarioDisponivelRotas>(
        HorarioDisponivelRotas.Listagem
    )

    val id = mutableStateOf("")
    
    // Campos do formulário com as máscaras de hora aplicadas na hora de início e de fim
    val psicologo = CampoValidado()
    val diaSemana = CampoValidado()
    val horaInicio = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val horaFim = CampoValidado(formatador = { MaskUtils.aplicarMascaraHora(it) })
    val descricao = CampoValidado()
    val ativo = CampoValidado("true")
    val mensagem = mutableStateOf("")

    fun salvar() {
        // Validação de todos os campos obrigatórios
        val v1 = psicologo.validar()
        val v2 = diaSemana.validar()
        val v3 = horaInicio.validar()
        val v4 = horaFim.validar()
        val v5 = descricao.validar()
        val v6 = ativo.validar()

        if (!(v1 && v2 && v3 && v4 && v5 && v6)) {
            mensagem.value = "Preencha todos os campos obrigatórios."
            mandarMensagem("Preencha todos os campos obrigatórios.", SnackbarDuration.Short)
            return
        }

        try {
            val horario = HorarioDisponivel(
                id = if (id.value.isEmpty()) 0 else id.value.toInt(),
                psicologo = psicologo.valor.value,
                diaSemana = diaSemana.valor.value,
                horaInicio = horaInicio.valor.value,
                horaFim = horaFim.valor.value,
                descricao = descricao.valor.value,
                ativo = ativo.valor.value.toBooleanStrictOrNull() ?: true
            )

            if (id.value.isEmpty()) {
                idCounter++
                _horarios.update { lista -> lista + horario.copy(id = idCounter) }
            } else {
                val idNumber = id.value.toInt()
                _horarios.update { lista ->
                    lista.map { h -> if (h.id == idNumber) horario.copy(id = idNumber) else h }
                }
            }
            mensagem.value = "Horário disponível salvo com sucesso!"
            mandarMensagem("Horário disponível salvo com sucesso!", SnackbarDuration.Short)
            limparCampos()
            
            backStack.clear()
            backStack.add(HorarioDisponivelRotas.Listagem)

        } catch (e: Exception) {
            mensagem.value = "Erro ao salvar: ${e.message}"
            mandarMensagem("Erro ao salvar: ${e.message}", SnackbarDuration.Long)
        }
    }

    fun apagar(horario: HorarioDisponivel) {
        _horarios.update { lista -> lista - horario }
        mandarMensagem("Horário apagado com sucesso!", SnackbarDuration.Short)
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
        id.value = horario.id.toString()
        psicologo.definir(horario.psicologo)
        diaSemana.definir(horario.diaSemana)
        horaInicio.definir(horario.horaInicio)
        horaFim.definir(horario.horaFim)
        descricao.definir(horario.descricao)
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