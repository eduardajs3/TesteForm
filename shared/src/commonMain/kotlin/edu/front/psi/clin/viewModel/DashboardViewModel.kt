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
    val status: String,
    val isConfirmada: Boolean
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
            ),
            MetricaCardItem(
                id = "evolucoes",
                titulo = "Evoluções Pendentes",
                valor = "2",
                subtitulo = "Aguardando preenchimento",
                iconeRes = Res.drawable.assignment_24px,
                variacaoPositiva = false
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
                status = "Confirmada",
                isConfirmada = true
            ),
            ConsultaHojeItem(
                id = 2,
                paciente = "Maria Clara da Silva",
                psicologo = "Dr. Roberto Alves",
                sala = "Sala 01 - Bloco A",
                horario = "10:30 - 11:30",
                tipo = "Presencial",
                status = "Pendente",
                isConfirmada = false
            ),
            ConsultaHojeItem(
                id = 3,
                paciente = "Lucas Oliveira",
                psicologo = "Dra. Ana Costa",
                sala = "Atendimento Remoto",
                horario = "14:00 - 15:00",
                tipo = "Online",
                status = "Confirmada",
                isConfirmada = true
            ),
            ConsultaHojeItem(
                id = 4,
                paciente = "Beatriz Souza",
                psicologo = "Dra. Juliana Costa",
                sala = "Sala 03 - Bloco B",
                horario = "15:30 - 16:30",
                tipo = "Presencial",
                status = "Pendente",
                isConfirmada = false
            ),
            ConsultaHojeItem(
                id = 5,
                paciente = "Carlos Eduardo Lima",
                psicologo = "Dr. Roberto Alves",
                sala = "Sala 01 - Bloco A",
                horario = "17:00 - 18:00",
                tipo = "Presencial",
                status = "Confirmada",
                isConfirmada = true
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
        when (filtro) {
            "Confirmadas" -> consultas.filter { it.isConfirmada }
            "Pendentes" -> consultas.filter { !it.isConfirmada }
            else -> consultas
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 6. Ação reativa para confirmar presença
    fun alternarConfirmacao(consultaId: Int) {
        _consultas.update { lista ->
            lista.map { consulta ->
                if (consulta.id == consultaId) {
                    val novoStatus = !consulta.isConfirmada
                    mandarMensagem(
                        if (novoStatus) "Presença de ${consulta.paciente} confirmada!"
                        else "Presença de ${consulta.paciente} alterada para Pendente."
                    )
                    consulta.copy(
                        isConfirmada = novoStatus,
                        status = if (novoStatus) "Confirmada" else "Pendente"
                    )
                } else {
                    consulta
                }
            }
        }
    }
}

