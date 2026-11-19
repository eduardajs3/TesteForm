package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.*
import edu.front.psi.clin.navigation.AgendaRotas
import edu.front.psi.clin.ui.componentes.LayoutFormularioBase
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel
import kotlinx.datetime.LocalDate

@Composable
fun AgendaFormulario(
    innerPadding: PaddingValues,
    vm: AgendaViewModel
) {
    LaunchedEffect(vm) {
        vm.carregarDadosFormulario()
    }

    val isEdicao = vm.id.value.isNotEmpty()
    var etapa by remember(vm.id.value) { mutableIntStateOf(1) }

    val pacientes = vm.pacientes.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value
    val salas = vm.salas.collectAsState().value
    val tipos = vm.tiposAtendimento.collectAsState().value
    val convenios = vm.convenios.collectAsState().value
    val horariosCadastrados = vm.horariosDisponiveis.collectAsState().value

    val pacientesDisponiveis = pacientes.filter { it.ativo }
    val psicologosDisponiveis = psicologos.filter { it.ativo }
    val tiposDisponiveis = tipos.filter { it.ativo }
    val conveniosDisponiveis = convenios.filter { it.ativo }

    val pacienteSelecionado = pacientes.firstOrNull {
        it.id == vm.pacienteId.value
    }
    val psicologoSelecionado = psicologos.firstOrNull {
        it.id == vm.psicologoId.value
    }
    val tipoSelecionado = tipos.firstOrNull {
        it.id == vm.tipoAtendimentoId.value
    }
    val convenioSelecionado = convenios.firstOrNull {
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
                    val eConcluida = numeroEtapa < etapa
                    val eAtual = numeroEtapa == etapa
                    val podeClicar = numeroEtapa <= etapa

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when {
                            eConcluida || eAtual -> VerdeClinica
                            else -> CremeCampo
                        },
                        border = BorderStroke(
                            1.dp,
                            if (eAtual || eConcluida) VerdeClinica else CardBordaCinza
                        ),
                        modifier = Modifier
                            .then(
                                if (podeClicar) {
                                    Modifier.clickable { etapa = numeroEtapa }
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (eConcluida) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Concluído",
                                    tint = Branco,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = "$numeroEtapa.",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (eAtual) Branco else TextoSecundario
                                )
                            }

                            Text(
                                text = titulo,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (eAtual || eConcluida) Branco else TextoSecundario
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            when (etapa) {
                1 -> EtapaDadosAtendimentoAgenda(
                    vm = vm,
                    pacientesDisponiveis = pacientesDisponiveis,
                    psicologosDisponiveis = psicologosDisponiveis,
                    tiposDisponiveis = tiposDisponiveis,
                    conveniosDisponiveis = conveniosDisponiveis,
                    pacienteSelecionado = pacienteSelecionado,
                    psicologoSelecionado = psicologoSelecionado,
                    tipoSelecionado = tipoSelecionado,
                    convenioSelecionado = convenioSelecionado,
                    onAvancar = {
                        if (
                            vm.pacienteId.value == null ||
                            vm.psicologoId.value == null ||
                            vm.tipoAtendimentoId.value == null ||
                            vm.convenioId.value == null
                        ) {
                            
                        } else {
                            vm.atualizarDisponibilidade()
                            etapa = 2
                        }
                    }
                )

                2 -> EtapaDataHorarioAgenda(
                    vm = vm,
                    horariosCadastrados = horariosCadastrados,
                    datasDisponiveis = datasDisponiveis,
                    dataSelecionada = dataSelecionada,
                    horariosDisponiveis = horariosDisponiveis,
                    horarioSelecionado = horarioSelecionado,
                    onVoltar = { etapa = 1 },
                    onEscolherSala = {
                        if (horarioSelecionado == null) {
                           
                        } else if (salasDisponiveis.isEmpty()) {
                        
                        } else {
                            etapa = 3
                        }
                    }
                )

                else -> EtapaSalaRevisaoAgenda(
                    vm = vm,
                    isEdicao = isEdicao,
                    salasDisponiveis = salasDisponiveis,
                    salaSelecionada = salaSelecionada,
                    pacienteSelecionado = pacienteSelecionado,
                    psicologoSelecionado = psicologoSelecionado,
                    tipoSelecionado = tipoSelecionado,
                    convenioSelecionado = convenioSelecionado,
                    dataSelecionada = dataSelecionada,
                    horarioSelecionado = horarioSelecionado,
                    onVoltar = { etapa = 2 },
                    onAgendar = {
                        vm.salvar { etapa = 2 }
                    }
                )
            }
        }
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