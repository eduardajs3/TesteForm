package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.model.Agenda
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.CabecalhoTela
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import org.jetbrains.compose.resources.painterResource

private val horariosBase = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00")

@Composable
fun AgendaListagem(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
   
    LaunchedEffect(Unit) {
        vm.carregarDadosFormulario()
    }

    val agendamentos by vm.agendamentosFiltrados.collectAsState()
    val pacientes = vm.pacientes.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value
    val salas = vm.salas.collectAsState().value
    val tiposAtendimento = vm.tiposAtendimento.collectAsState().value
    val convenios = vm.convenios.collectAsState().value
    val podeGerenciarAgenda = PermissoesAcesso.podeGerenciar(
        SessionManager.role,
        RecursoSistema.AGENDAS
    )
    
    val psicologoFiltroAtual by vm.psicologoFiltro.collectAsState()
    val horariosDisponiveisState = vm.horariosDisponiveis.collectAsState().value

    var agendamentoSelecionado by remember { mutableStateOf<Agenda?>(null) }
    var exibirModalExclusao by remember { mutableStateOf(false) }
    var menuMesesAberto by remember { mutableStateOf(false) }

 
    val hoje = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

   
    val segundaAtualState = remember {
        val diaDaSemanaOrdinal = hoje.dayOfWeek.ordinal // 0 = Segunda, ..., 6 = Domingo
        val segunda = hoje.minus(diaDaSemanaOrdinal, DateTimeUnit.DAY)
        DataSimples(segunda.day, segunda.monthNumber, segunda.year)
    }
    var segundaAtual by remember { mutableStateOf(segundaAtualState) }

    val diaHojeIndex = remember(segundaAtual, hoje) {
        val dias = criarDiasDaSemana(segundaAtual)
        val index = dias.indexOfFirst { it.dataIso == hoje.toString() }
        if (index != -1) index else 0
    }

    var diaSelecionadoIndex by remember(segundaAtual) { mutableIntStateOf(diaHojeIndex) }

    LaunchedEffect(psicologoFiltroAtual, segundaAtual, horariosDisponiveisState) {
        val dias = criarDiasDaSemana(segundaAtual)
        val psicologoObj = psicologos.firstOrNull { it.nome == psicologoFiltroAtual }
        
        val horariosProfissional = if (psicologoFiltroAtual == "Todos") {
            horariosDisponiveisState.filter { it.ativo }
        } else {
            horariosDisponiveisState.filter { it.ativo && it.psicologoId == psicologoObj?.id }
        }

        val diaAtualSelecionado = dias.getOrNull(diaSelecionadoIndex)
        val diaIsoAtual = diaAtualSelecionado?.let { 
            when (it.nomeDia.lowercase()) {
                "seg" -> "SEGUNDA"
                "ter" -> "TERCA"
                "qua" -> "QUARTA"
                "qui" -> "QUINTA"
                "sex" -> "SEXTA"
                "sáb", "sab" -> "SABADO"
                else -> "DOMINGO"
            }
        }

        val temExpedienteNoDiaAtual = diaIsoAtual != null && 
            horariosProfissional.any { it.diaSemana.equals(diaIsoAtual, ignoreCase = true) }

        if (!temExpedienteNoDiaAtual && psicologoFiltroAtual != "Todos" && dias.isNotEmpty()) {
            val primeiroDiaValidoIndex = dias.indexOfFirst { dia ->
                val iso = when (dia.nomeDia.lowercase()) {
                    "seg" -> "SEGUNDA"
                    "ter" -> "TERCA"
                    "qua" -> "QUARTA"
                    "qui" -> "QUINTA"
                    "sex" -> "SEXTA"
                    "sáb", "sab" -> "SABADO"
                    else -> "DOMINGO"
                }
                horariosProfissional.any { it.diaSemana.equals(iso, ignoreCase = true) }
            }

            if (primeiroDiaValidoIndex != -1) {
                diaSelecionadoIndex = primeiroDiaValidoIndex
            }
        }
    }

    val domingoAtual = remember(segundaAtual) { segundaAtual.adicionarDias(6) }
    val diasDaSemana = remember(segundaAtual) { criarDiasDaSemana(segundaAtual) }
    val tituloSemana = remember(segundaAtual, domingoAtual) {
        tituloSemanaAgenda(segundaAtual, domingoAtual)
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
                    mostrarBotao = podeGerenciarAgenda,
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

                Spacer(modifier = Modifier.height(12.dp))

                if (telaPequena) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        CampoDropdownPadrao(
                            label = "Psicólogo",
                            valorSelecionado = psicologoFiltroAtual,
                            opcoes = listOf("Todos") + psicologos.map { it.nome },
                            onOpcaoSelecionada = { nome -> vm.filtrarPorPsicologo(nome) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CampoDropdownPadrao(
                            label = "Status",
                            valorSelecionado = vm.statusFiltro.collectAsState().value,
                            opcoes = listOf("Todos", "AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO", "FALTOU"),
                            onOpcaoSelecionada = { status -> vm.filtrarPorStatus(status) }
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            CampoDropdownPadrao(
                                label = "Psicólogo",
                                valorSelecionado = psicologoFiltroAtual,
                                opcoes = listOf("Todos") + psicologos.map { it.nome },
                                onOpcaoSelecionada = { nome -> vm.filtrarPorPsicologo(nome) }
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            CampoDropdownPadrao(
                                label = "Status",
                                valorSelecionado = vm.statusFiltro.collectAsState().value,
                                opcoes = listOf("Todos", "AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO", "FALTOU"),
                                onOpcaoSelecionada = { status -> vm.filtrarPorStatus(status) }
                            )
                        }
                    }
                }

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
                    horariosDisponiveis = horariosDisponiveisState, 
                    psicologoFiltro = psicologoFiltroAtual,         
                    onSelecionarDia = { diaSelecionadoIndex = it },
                    onSelecionarAgendamento = { agendamentoSelecionado = it }
                )
            }
        }

        agendamentoSelecionado?.let { agendamento ->
            val pacienteNome = pacientes.firstOrNull { it.id == agendamento.pacienteId }?.nome ?: "-"
            val psicologoNome = psicologos.firstOrNull { it.id == agendamento.psicologoId }?.nome ?: "-"
            val salaNome = salas.firstOrNull { it.id == agendamento.salaId }?.nome ?: "-"
            val tipoAtendimento = tiposAtendimento.firstOrNull { it.id == agendamento.tipoAtendimentoId }
            val tipoNome = tipoAtendimento?.nome ?: "-"
            val convenioNome = convenios.firstOrNull { it.id == agendamento.convenioId }?.nome ?: "-"

            ModalDetalhesBase(
                titulo = "Detalhes do Agendamento",
                onDismiss = { agendamentoSelecionado = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Paciente", valor = pacienteNome)
                    ItemDetalhe(rotulo = "Psicólogo", valor = psicologoNome)
                    ItemDetalhe(rotulo = "Sala", valor = salaNome)
                    ItemDetalhe(rotulo = "Tipo de Atendimento", valor = tipoNome)
                    ItemDetalhe(rotulo = "Modalidade", valor = tipoAtendimento?.modalidade?.rotulo ?: "-")
                    ItemDetalhe(rotulo = "Convênio", valor = convenioNome)
                    ItemDetalhe(
                        rotulo = "Data",
                        valor = formatarDataHoraIsoParaBrasileira(agendamento.dataHoraInicio)
                            .substringBefore(" ")
                    )
                    ItemDetalhe(
                        rotulo = "Horário",
                        valor = "${agendamento.dataHoraInicio.substringAfter("T").take(5)} às ${
                            agendamento.dataHoraFim.substringAfter("T").take(5)
                        }"
                    )
                    ItemDetalhe(rotulo = "Status", valor = agendamento.status)
                    ItemDetalhe(rotulo = "Observações", valor = agendamento.observacoes?.ifEmpty { null })
                },
                acoesTopo = if (podeGerenciarAgenda) {
                    {
                        IconButton(
                            onClick = { exibirModalExclusao = true },
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
                } else {
                    null
                }
            )
        }

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