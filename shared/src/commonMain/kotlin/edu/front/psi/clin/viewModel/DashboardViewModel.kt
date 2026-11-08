package edu.front.psi.clin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import frontpsiclinic.shared.generated.resources.*
import kotlinx.coroutines.flow.*
import org.jetbrains.compose.resources.DrawableResource

data class MetricaCardItem(
    val id: String,
    val titulo: String,
    val valor: String,
    val subtitulo: String,
    val iconeRes: DrawableResource,
    val variacaoPositiva: Boolean = true
)

data class ConsultaHojeItem(
    val id: Int,
    val paciente: String,
    val psicologo: String,
    val sala: String,
    val horario: String,
    val tipo: String,
    val status: String
)

class DashboardViewModel(
    private val mandarMensagem: (String) -> Unit = {}
) : ViewModel() {

    // 1. Estados mutáveis privados (boa prática do ViewModel)
    private val _metricas = MutableStateFlow<List<MetricaCardItem>>(
        listOf(
            MetricaCardItem(
                id = "consultas",
                titulo = "Consultas Hoje",
                valor = "8",
                subtitulo = "2 finalizadas • 6 em andamento",
                iconeRes = Res.drawable.calendar_month,
                variacaoPositiva = true
            ),
            MetricaCardItem(
                id = "pacientes",
                titulo = "Pacientes Ativos",
                valor = "24",
                subtitulo = "+3 novos este mês",
                iconeRes = Res.drawable.group_24px,
                variacaoPositiva = true
            ),
            MetricaCardItem(
                id = "salas",
                titulo = "Salas em Uso",
                valor = "4 / 5",
                subtitulo = "Sala 02 disponível agora",
                iconeRes = Res.drawable.door_open,
                variacaoPositiva = true
            )
        )
    )
    // 2. StateFlow somente leitura exposto publicamente via asStateFlow()
    val metricas: StateFlow<List<MetricaCardItem>> = _metricas.asStateFlow()

    // 3. Consultas agendadas para o dia
    private val _consultas = MutableStateFlow<List<ConsultaHojeItem>>(
        listOf(
            ConsultaHojeItem(
                id = 1,
                paciente = "João Silva",
                psicologo = "Dra. Ana Costa",
                sala = "Sala 02 - Bloco A",
                horario = "09:00 - 10:00",
                tipo = "Presencial",
                status = "Confirmado"
            ),
            ConsultaHojeItem(
                id = 2,
                paciente = "Maria Clara da Silva",
                psicologo = "Dr. Roberto Alves",
                sala = "Sala 01 - Bloco A",
                horario = "10:30 - 11:30",
                tipo = "Presencial",
                status = "Agendado"
            ),
            ConsultaHojeItem(
                id = 3,
                paciente = "Lucas Oliveira",
                psicologo = "Dra. Ana Costa",
                sala = "Atendimento Remoto",
                horario = "14:00 - 15:00",
                tipo = "Online",
                status = "Cancelado"
            ),
            ConsultaHojeItem(
                id = 4,
                paciente = "Beatriz Souza",
                psicologo = "Dra. Juliana Costa",
                sala = "Sala 03 - Bloco B",
                horario = "15:30 - 16:30",
                tipo = "Presencial",
                status = "Faltou"
            ),
            ConsultaHojeItem(
                id = 5,
                paciente = "Carlos Eduardo Lima",
                psicologo = "Dr. Roberto Alves",
                sala = "Sala 01 - Bloco A",
                horario = "17:00 - 18:00",
                tipo = "Presencial",
                status = "Confirmado"
            )
        )
    )

    // 4. Filtro por status
    private val _filtroStatus = MutableStateFlow("Todas")
    val filtroStatus: StateFlow<String> = _filtroStatus.asStateFlow()

    fun selecionarFiltroStatus(novoFiltro: String) {
        _filtroStatus.value = novoFiltro
    }

    // 5. combine() + filter() + stateIn() com SharingStarted.WhileSubscribed(5_000)
    val consultasFiltradas: StateFlow<List<ConsultaHojeItem>> = combine(_consultas, _filtroStatus) { consultas, filtro ->
        if (filtro == "Todas") consultas else consultas.filter { it.status == filtro }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 6. Ação reativa para confirmar presença
    fun alternarConfirmacao(consultaId: Int) {
        _consultas.update { lista ->
            lista.map { consulta ->
                if (consulta.id == consultaId && consulta.status in listOf("Agendado", "Confirmado")) {
                    val novoStatus = if (consulta.status == "Agendado") "Confirmado" else "Agendado"
                    mandarMensagem(
                        if (novoStatus == "Confirmado") "Consulta de ${consulta.paciente} confirmada!"
                        else "Status da consulta de ${consulta.paciente} alterado para Agendado."
                    )
                    consulta.copy(status = novoStatus)
                } else {
                    consulta
                }
            }
        }
    }
}
