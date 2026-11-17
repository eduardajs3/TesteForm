package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.CabecalhoTela
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.theme.TextoErro
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import edu.front.psi.clin.utils.DataSimples
import edu.front.psi.clin.utils.criarDiasDaSemana
import edu.front.psi.clin.utils.formatarDataHoraIsoParaBrasileira
import edu.front.psi.clin.utils.obterPrimeiraSegundaDoMes
import edu.front.psi.clin.utils.tituloSemanaAgenda
import frontpsiclinic.shared.generated.resources.Res
import frontpsiclinic.shared.generated.resources.add
import frontpsiclinic.shared.generated.resources.delete_24px
import frontpsiclinic.shared.generated.resources.edit_square_24px
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
    var exibirModalExclusao by remember { mutableStateOf(false) } // Estado para controlar o modal de exclusão
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

        // Modal de Detalhes
        agendamentoSelecionado?.let { agendamento ->
            val pacienteNome = pacientes.firstOrNull { it.id == agendamento.pacienteId }?.nome ?: "-"
            val psicologoNome = psicologos.firstOrNull { it.id == agendamento.psicologoId }?.nome ?: "-"
            val salaNome = salas.firstOrNull { it.id == agendamento.salaId }?.nome ?: "-"
            val tipoNome = tiposAtendimento.firstOrNull { it.id == agendamento.tipoAtendimentoId }?.nome ?: "-"
            val convenioNome = convenios.firstOrNull { it.id == agendamento.convenioId }?.nome ?: "-"

            ModalDetalhesBase(
                titulo = "Detalhes do Agendamento",
                onDismiss = { agendamentoSelecionado = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Paciente", valor = pacienteNome)
                    ItemDetalhe(rotulo = "Psicólogo", valor = psicologoNome)
                    ItemDetalhe(rotulo = "Sala", valor = salaNome)
                    ItemDetalhe(rotulo = "Tipo de Atendimento", valor = tipoNome)
                    ItemDetalhe(rotulo = "Convênio", valor = convenioNome)
                    ItemDetalhe(
                        rotulo = "Data e Horário",
                        valor = formatarDataHoraIsoParaBrasileira(agendamento.dataHoraInicio)
                    )
                    ItemDetalhe(rotulo = "Status", valor = agendamento.status)
                    ItemDetalhe(rotulo = "Observações", valor = agendamento.observacoes?.ifEmpty { null })
                },
                acoesTopo = {
                    IconButton(
                        onClick = {
                            // Ativa o modal de confirmação de exclusão
                            exibirModalExclusao = true
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.delete_24px),
                            contentDescription = "Excluir",
                            tint = TextoErro,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            val item = agendamento
                            agendamentoSelecionado = null
                            vm.editar(item)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.edit_square_24px),
                            contentDescription = "Editar",
                            tint = VerdeClinica,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }

        // Modal de Confirmação de Exclusão (AppDeleteModal)
        if (exibirModalExclusao && agendamentoSelecionado != null) {
            AppDeleteModal(
                title = "Excluir agendamento",
                message = "Deseja realmente excluir este agendamento?",
                onDismiss = { exibirModalExclusao = false },
                onConfirm = {
                    val item = agendamentoSelecionado
                    exibirModalExclusao = false
                    agendamentoSelecionado = null
                    if (item != null) {
                        vm.apagar(item)
                    }
                }
            )
        }
    }
}