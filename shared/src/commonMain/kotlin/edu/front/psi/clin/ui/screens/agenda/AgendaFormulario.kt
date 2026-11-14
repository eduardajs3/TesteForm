package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.*
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.componentes.CampoTextoPadrao
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import edu.front.psi.clin.ui.viewmodel.HorarioAgendamento
import kotlinx.datetime.LocalDate

@Composable
fun AgendaFormulario(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    val isEdicao = vm.id.value.isNotEmpty()
    var etapa by remember(vm.id.value) { mutableIntStateOf(1) }

    val pacientes = vm.pacientes.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value
    val salas = vm.salas.collectAsState().value
    val tipos = vm.tiposAtendimento.collectAsState().value
    val convenios = vm.convenios.collectAsState().value
    val horariosCadastrados = vm.horariosDisponiveis.collectAsState().value

    val pacientesDisponiveis = pacientes.filter {
        it.ativo || it.id == vm.pacienteId.value
    }
    val psicologosDisponiveis = psicologos.filter {
        it.ativo || it.id == vm.psicologoId.value
    }
    val tiposDisponiveis = tipos.filter {
        it.ativo || it.id == vm.tipoAtendimentoId.value
    }
    val conveniosDisponiveis = convenios.filter {
        it.ativo || it.id == vm.convenioId.value
    }

    val pacienteSelecionado = pacientesDisponiveis.firstOrNull {
        it.id == vm.pacienteId.value
    }
    val psicologoSelecionado = psicologosDisponiveis.firstOrNull {
        it.id == vm.psicologoId.value
    }
    val tipoSelecionado = tiposDisponiveis.firstOrNull {
        it.id == vm.tipoAtendimentoId.value
    }
    val convenioSelecionado = conveniosDisponiveis.firstOrNull {
        it.id == vm.convenioId.value
    }

    val datasDisponiveis = if (vm.psicologoId.value != null && vm.tipoAtendimentoId.value != null) {
        vm.diasDisponiveis(vm.psicologoId.value!!, vm.tipoAtendimentoId.value!!)
    } else {
        emptyList()
    }
    val dataSelecionada = parseDataAgenda(vm.dataAtendimento.valor.value)
    val horariosDisponiveis = if (
        dataSelecionada != null &&
        vm.psicologoId.value != null &&
        vm.tipoAtendimentoId.value != null
    ) {
        vm.buscarHorariosDisponiveis(
            dataSelecionada,
            vm.psicologoId.value!!,
            vm.tipoAtendimentoId.value!!
        )
    } else {
        emptyList()
    }
    val horarioSelecionado = horariosDisponiveis.firstOrNull {
        it.inicio == vm.horaInicio.valor.value && it.fim == vm.horaFim.valor.value
    }
    val salasDisponiveis = if (dataSelecionada != null && horarioSelecionado != null) {
        vm.salasDisponiveis(dataSelecionada, horarioSelecionado)
    } else {
        emptyList()
    }
    val salaSelecionada = salas.firstOrNull { it.id == vm.salaId.value }

    Box(modifier = Modifier.padding(innerPadding)) {
        LayoutFormularioBase(
            titulo = if (isEdicao) "Editar agendamento" else "Novo agendamento",
            subtitulo = "Selecione os dados, confira a disponibilidade e revise antes de confirmar.",
            mensagemFeedback = vm.mensagem.value,
            onVoltar = {
                vm.backStack.clear()
                vm.backStack.add(AgendaRotas.Listagem)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Atendimento", "Data e horário", "Sala e revisão").forEachIndexed { index, titulo ->
                    val numeroEtapa = index + 1
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (etapa == numeroEtapa) VerdeClinica else CremeCampo,
                        border = BorderStroke(
                            1.dp,
                            if (etapa == numeroEtapa) VerdeClinica else CardBordaCinza
                        )
                    ) {
                        Text(
                            text = "$numeroEtapa. $titulo",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (etapa == numeroEtapa) Branco else TextoSecundario
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            when (etapa) {
                1 -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Branco),
                        border = BorderStroke(1.dp, CardBordaCinza),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Dados do atendimento",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            CampoDropdownPadrao(
                                label = "Paciente *",
                                valorSelecionado = pacienteSelecionado?.nome.orEmpty(),
                                opcoes = pacientesDisponiveis.map { it.nome },
                                onOpcaoSelecionada = { nome ->
                                    vm.pacienteId.value = pacientesDisponiveis
                                        .firstOrNull { it.nome == nome }?.id
                                }
                            )
                            CampoDropdownPadrao(
                                label = "Psicólogo *",
                                valorSelecionado = psicologoSelecionado?.nome.orEmpty(),
                                opcoes = psicologosDisponiveis.map { it.nome },
                                onOpcaoSelecionada = { nome ->
                                    val selecionado = psicologosDisponiveis.firstOrNull { it.nome == nome }
                                    vm.psicologoId.value = selecionado?.id
                                    vm.dataAtendimento.limpar()
                                    vm.horaInicio.limpar()
                                    vm.horaFim.limpar()
                                    vm.salaId.value = null
                                }
                            )
                            CampoDropdownPadrao(
                                label = "Tipo de atendimento *",
                                valorSelecionado = tipoSelecionado?.nome.orEmpty(),
                                opcoes = tiposDisponiveis.map { it.nome },
                                onOpcaoSelecionada = { nome ->
                                    val selecionado = tiposDisponiveis.firstOrNull { it.nome == nome }
                                    vm.tipoAtendimentoId.value = selecionado?.id
                                    vm.dataAtendimento.limpar()
                                    vm.horaInicio.limpar()
                                    vm.horaFim.limpar()
                                    vm.salaId.value = null
                                }
                            )
                            CampoDropdownPadrao(
                                label = "Convênio *",
                                valorSelecionado = convenioSelecionado?.nome.orEmpty(),
                                opcoes = conveniosDisponiveis.map { it.nome },
                                onOpcaoSelecionada = { nome ->
                                    vm.convenioId.value = conveniosDisponiveis
                                        .firstOrNull { it.nome == nome }?.id
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (
                                vm.pacienteId.value == null ||
                                vm.psicologoId.value == null ||
                                vm.tipoAtendimentoId.value == null ||
                                vm.convenioId.value == null
                            ) {
                                vm.mensagem.value = "Selecione paciente, psicólogo, atendimento e convênio."
                            } else {
                                vm.mensagem.value = ""
                                vm.atualizarDisponibilidade()
                                etapa = 2
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica)
                    ) {
                        Text("Ver disponibilidade")
                    }
                }

                2 -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Branco),
                        border = BorderStroke(1.dp, CardBordaCinza),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Escolha uma data disponível",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            Text(
                                text = "Os dias exibidos consideram a agenda semanal do psicólogo e a duração do atendimento.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario
                            )

                            if (horariosCadastrados.isEmpty()) {
                                Text(
                                    text = "Não há horários semanais cadastrados para consultar.",
                                    color = TextoErro,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else if (datasDisponiveis.isEmpty()) {
                                Text(
                                    text = "Não há datas com horários disponíveis nos próximos 60 dias.",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    datasDisponiveis.chunked(4).forEach { linhaDatas ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            linhaDatas.forEach { data ->
                                                val selecionada = data == dataSelecionada
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = if (selecionada) VerdeClinica else CremeCampo,
                                                    border = BorderStroke(
                                                        1.dp,
                                                        if (selecionada) VerdeClinica else CardBordaCinza
                                                    ),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            vm.dataAtendimento.definir(data.toString())
                                                            vm.horaInicio.limpar()
                                                            vm.horaFim.limpar()
                                                            vm.salaId.value = null
                                                        }
                                                ) {
                                                    Column(
                                                        modifier = Modifier.padding(8.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text(
                                                            text = nomeDia(data),
                                                            color = if (selecionada) Branco else TextoPrincipal,
                                                            style = MaterialTheme.typography.labelMedium,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Text(
                                                            text = "${data.dayOfMonth.toString().padStart(2, '0')}/${data.monthNumber.toString().padStart(2, '0')}",
                                                            color = if (selecionada) Branco else TextoSecundario,
                                                            style = MaterialTheme.typography.labelSmall
                                                        )
                                                    }
                                                }
                                            }
                                            repeat(4 - linhaDatas.size) {
                                                Spacer(Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }

                            if (dataSelecionada != null) {
                                Text(
                                    text = "Horários disponíveis",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal
                                )
                                if (horariosDisponiveis.isEmpty()) {
                                    Text(
                                        text = "Não há horários livres para esta data.",
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        horariosDisponiveis.chunked(3).forEach { linhaHorarios ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                linhaHorarios.forEach { horario ->
                                                    val selecionado = horario == horarioSelecionado
                                                    Surface(
                                                        shape = RoundedCornerShape(10.dp),
                                                        color = if (selecionado) VerdeClinica else CremeCampo,
                                                        border = BorderStroke(
                                                            1.dp,
                                                            if (selecionado) VerdeClinica else CardBordaCinza
                                                        ),
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .clickable {
                                                                vm.horaInicio.definir(horario.inicio)
                                                                vm.horaFim.definir(horario.fim)
                                                                vm.salaId.value = null
                                                            }
                                                    ) {
                                                        Text(
                                                            text = "${horario.inicio}–${horario.fim}",
                                                            modifier = Modifier.padding(10.dp),
                                                            color = if (selecionado) Branco else TextoPrincipal,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }
                                                repeat(3 - linhaHorarios.size) {
                                                    Spacer(Modifier.weight(1f))
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Text(
                                text = "Os bloqueios de agenda não são verificados porque não há uma fonte persistida disponível no app.",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { etapa = 1 },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Voltar")
                        }
                        Button(
                            onClick = {
                                if (horarioSelecionado == null) {
                                    vm.mensagem.value = "Selecione uma data e um horário disponível."
                                } else if (salasDisponiveis.isEmpty()) {
                                    vm.mensagem.value = "Não há salas disponíveis para este horário."
                                } else {
                                    vm.mensagem.value = ""
                                    etapa = 3
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica)
                        ) {
                            Text("Escolher sala")
                        }
                    }
                }

                else -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Branco),
                        border = BorderStroke(1.dp, CardBordaCinza),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Sala e confirmação",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            CampoDropdownPadrao(
                                label = "Sala disponível *",
                                valorSelecionado = salaSelecionada?.nome.orEmpty(),
                                opcoes = salasDisponiveis.map { it.nome },
                                onOpcaoSelecionada = { nome ->
                                    vm.salaId.value = salasDisponiveis
                                        .firstOrNull { it.nome == nome }?.id
                                }
                            )

                            CampoDropdownPadrao(
                                label = "Status",
                                valorSelecionado = vm.status.valor.value,
                                opcoes = if (isEdicao) {
                                    listOf("AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO", "FALTOU")
                                } else {
                                    listOf("AGENDADO", "CONFIRMADO")
                                },
                                onOpcaoSelecionada = { vm.status.definir(it) }
                            )

                            CampoTextoPadrao(
                                value = vm.observacoes.valor.value,
                                onValueChange = { vm.observacoes.definir(it) },
                                label = "Observações (opcional)"
                            )

                            HorizontalDivider()
                            Text(
                                text = "Revise os dados do agendamento",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = VerdeClinica
                            )
                            ResumoAgendamento("Paciente", pacienteSelecionado?.nome)
                            ResumoAgendamento("Psicólogo", psicologoSelecionado?.nome)
                            ResumoAgendamento("Atendimento", tipoSelecionado?.nome)
                            ResumoAgendamento("Convênio", convenioSelecionado?.nome)
                            ResumoAgendamento(
                                "Data",
                                dataSelecionada?.let {
                                    "${it.dayOfMonth.toString().padStart(2, '0')}/${it.monthNumber.toString().padStart(2, '0')}/${it.year}"
                                }
                            )
                            ResumoAgendamento(
                                "Horário",
                                horarioSelecionado?.let { "${it.inicio}–${it.fim}" }
                            )
                            ResumoAgendamento("Sala", salaSelecionada?.nome)
                            ResumoAgendamento("Status", vm.status.valor.value)
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { etapa = 2 },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Voltar")
                        }
                        Button(
                            onClick = vm::salvar,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica)
                        ) {
                            Text(if (isEdicao) "Confirmar alterações" else "Confirmar agendamento")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumoAgendamento(rotulo: String, valor: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$rotulo: ",
            fontWeight = FontWeight.SemiBold,
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = valor ?: "—",
            color = TextoPrincipal,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun parseDataAgenda(valor: String): LocalDate? {
    if (valor.isBlank()) return null
    return runCatching {
        if (valor.contains('/')) {
            val partes = valor.split('/')
            LocalDate(partes[2].toInt(), partes[1].toInt(), partes[0].toInt())
        } else {
            LocalDate.parse(valor)
        }
    }.getOrNull()
}

private fun nomeDia(data: LocalDate): String = when (data.dayOfWeek.ordinal + 1) {
    1 -> "Seg"
    2 -> "Ter"
    3 -> "Qua"
    4 -> "Qui"
    5 -> "Sex"
    6 -> "Sáb"
    else -> "Dom"
}
