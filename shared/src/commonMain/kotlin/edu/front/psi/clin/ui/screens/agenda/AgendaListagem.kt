package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.ui.componentes.CabecalhoTela
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import edu.front.psi.clin.utils.DataSimples
import edu.front.psi.clin.utils.criarDiasDaSemana
import edu.front.psi.clin.utils.obterPrimeiraSegundaDoMes
import edu.front.psi.clin.utils.tituloSemanaAgenda
import frontpsiclinic.shared.generated.resources.Res
import frontpsiclinic.shared.generated.resources.add
import org.jetbrains.compose.resources.painterResource

private val horariosBase = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00")

@Composable
fun AgendaListagem(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val agendamentos by vm.agendamentosFiltrados.collectAsState()
    val pacientes = vm.pacientes.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value
    val salas = vm.salas.collectAsState().value
    val tiposAtendimento = vm.tiposAtendimento.collectAsState().value
    val convenios = vm.convenios.collectAsState().value

    var agendamentoSelecionado by remember { mutableStateOf<Agenda?>(null) }
    var segundaAtual by remember { mutableStateOf(DataSimples(16, 11, 2026)) }
    var menuMesesAberto by remember { mutableStateOf(false) }
    var diaSelecionadoIndex by remember { mutableIntStateOf(0) }

    val sextaAtual = remember(segundaAtual) { segundaAtual.adicionarDias(4) }
    val diasDaSemana = remember(segundaAtual) { criarDiasDaSemana(segundaAtual) }
    val tituloSemana = remember(segundaAtual, sextaAtual) {
        tituloSemanaAgenda(segundaAtual, sextaAtual)
    }
    val horariosSessao = remember(agendamentos, diasDaSemana) {
        val datasDaSemana = diasDaSemana.map { it.dataIso }.toSet()
        (
            horariosBase + agendamentos.mapNotNull { agendamento ->
                val partes = agendamento.dataHoraInicio.split('T', limit = 2)
                val hora = partes.getOrNull(1)?.take(5)
                hora?.takeIf {
                    partes[0] in datasDaSemana && it.matches(Regex("\\d{2}:\\d{2}"))
                }
            }
        ).distinct().sorted()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        val telaPequena = maxWidth < 600.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (telaPequena) 12.dp else 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CabecalhoTela(
                    titulo = "Agenda de Atendimentos",
                    subtitulo = "Acompanhe e organize as consultas da semana",
                    textoBotao = "Novo Agendamento",
                    iconeBotao = painterResource(Res.drawable.add),
                    onClickBotao = vm::novoAgendamento
                )

                Spacer(modifier = Modifier.height(16.dp))

                AgendaNavegacaoSemana(
                    segundaAtual = segundaAtual,
                    tituloSemana = tituloSemana,
                    telaPequena = telaPequena,
                    menuMesesAberto = menuMesesAberto,
                    onSemanaAnterior = { segundaAtual = segundaAtual.adicionarDias(-7) },
                    onProximaSemana = { segundaAtual = segundaAtual.adicionarDias(7) },
                    onAbrirMeses = { menuMesesAberto = true },
                    onFecharMeses = { menuMesesAberto = false },
                    onSelecionarMes = { mes ->
                        segundaAtual = obterPrimeiraSegundaDoMes(segundaAtual.ano, mes)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AgendaGradeSemanal(
                    diasDaSemana = diasDaSemana,
                    diaSelecionadoIndex = diaSelecionadoIndex,
                    telaPequena = telaPequena,
                    horariosSessao = horariosSessao,
                    agendamentos = agendamentos,
                    pacientes = pacientes,
                    psicologos = psicologos,
                    tiposAtendimento = tiposAtendimento,
                    onSelecionarDia = { diaSelecionadoIndex = it },
                    onSelecionarAgendamento = { agendamentoSelecionado = it }
                )
            }
        }

        agendamentoSelecionado?.let { agendamento ->
    
            val pacienteNome = pacientes.firstOrNull { it.id == agendamento.pacienteId }?.nome ?: "-"
            val psicologoNome = psicologos.firstOrNull { it.id == agendamento.psicologoId }?.nome ?: "-"
            val salaNome = salas.firstOrNull { it.id == agendamento.salaId }?.nome ?: "-"
            val tipoNome = tiposAtendimento.firstOrNull { it.id == agendamento.tipoAtendimentoId }?.nome ?: "-"
            val convenioNome = convenios.firstOrNull { it.id == agendamento.convenioId }?.nome ?: "-"

            AgendaDetalhesDialog(
                agendamento = agendamento,
                pacienteNome = pacienteNome,
                psicologoNome = psicologoNome,
                salaNome = salaNome,
                tipoNome = tipoNome,
                convenioNome = convenioNome,
                onDismiss = { agendamentoSelecionado = null },
                onEditar = {
                    val item = agendamento
                    agendamentoSelecionado = null
                    vm.editar(item)
                },
                onApagar = {
                    val item = agendamento
                    agendamentoSelecionado = null
                    vm.apagar(item)
                }
            )
        }
    }
}