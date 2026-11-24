package edu.front.psi.clin.ui.screens.horarioDisponivel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.api.SessionManager
import edu.front.psi.clin.model.HorarioDisponivel
import edu.front.psi.clin.security.PermissoesAcesso
import edu.front.psi.clin.security.RecursoSistema
import edu.front.psi.clin.ui.componentes.*
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.HorarioDisponivelViewModel
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

private data class DiaSemanaInfo(
    val chave: String,
    val sigla: String,
    val nomeCompleto: String
)

private val diasDaSemana = listOf(
    DiaSemanaInfo("SEGUNDA", "Seg", "Segunda-feira"),
    DiaSemanaInfo("TERCA", "Ter", "Terça-feira"),
    DiaSemanaInfo("QUARTA", "Qua", "Quarta-feira"),
    DiaSemanaInfo("QUINTA", "Qui", "Quinta-feira"),
    DiaSemanaInfo("SEXTA", "Sex", "Sexta-feira"),
    DiaSemanaInfo("SABADO", "Sáb", "Sábado"),
    DiaSemanaInfo("DOMINGO", "Dom", "Domingo")
)

@Composable
fun HorarioDisponivelListagem(
    innerPadding: PaddingValues,
    vm: HorarioDisponivelViewModel
) {
    val lista = vm.horariosFiltrados.collectAsState().value
    val filtro = vm.filtro.collectAsState().value
    val psicologos = vm.psicologos.collectAsState().value

    val permitirEscrita = remember(SessionManager.role) {
        PermissoesAcesso.podeGerenciar(
            SessionManager.role,
            RecursoSistema.HORARIOS_DISPONIVEIS
        )
    }

    val diaFiltroSelecionado = remember { mutableStateOf<String?>(null) }
    val horarioSelecionado = remember { mutableStateOf<HorarioDisponivel?>(null) }
    val horarioParaExcluir = remember { mutableStateOf<HorarioDisponivel?>(null) }

    val diasExpandidos = remember {
        mutableStateMapOf<String, Boolean>().apply {
            diasDaSemana.forEach { dia -> put(dia.chave, true) }
        }
    }

    val horariosPorDia = remember(lista) {
        lista.groupBy { it.diaSemana.trim().uppercase() }
    }

    val diasParaExibir = remember(horariosPorDia, diaFiltroSelecionado.value) {
        val selecionado = diaFiltroSelecionado.value
        if (selecionado != null) {
            diasDaSemana.filter { it.chave == selecionado }
        } else {
            diasDaSemana.filter { !horariosPorDia[it.chave].isNullOrEmpty() }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        val telaPequena = maxWidth < 650.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            CabecalhoTela(
                titulo = "Horários Disponíveis",
                textoBotao = if (permitirEscrita) "Novo Horário" else "",
                iconeBotao = painterResource(Res.drawable.add),
                onClickBotao = { if (permitirEscrita) vm.novoHorario() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BarraPesquisa(
                text = filtro,
                onTextChange = vm::atualizarFiltro,
                placeholderText = "Buscar por psicólogo, dia ou horário..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtro rápido por dia da semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotaoFiltroDia(
                    titulo = "Todos",
                    quantidade = lista.size,
                    selecionado = diaFiltroSelecionado.value == null,
                    temAtendimento = lista.isNotEmpty(),
                    onClick = { diaFiltroSelecionado.value = null }
                )

                diasDaSemana.forEach { dia ->
                    val qtd = horariosPorDia[dia.chave]?.size ?: 0
                    val selecionado = diaFiltroSelecionado.value == dia.chave

                    BotaoFiltroDia(
                        titulo = dia.sigla,
                        quantidade = qtd,
                        selecionado = selecionado,
                        temAtendimento = qtd > 0,
                        onClick = {
                            diaFiltroSelecionado.value = if (selecionado) null else dia.chave
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (diasParaExibir.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardFundoCinza),
                    border = BorderStroke(1.dp, CardBordaCinza)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Não há horários disponíveis cadastrados.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    diasParaExibir.forEach { dia ->
                        val turnosDoDia = horariosPorDia[dia.chave]
                            .orEmpty()
                            .sortedBy { it.horaInicio }

                        val isExpandido = diasExpandidos[dia.chave] ?: true
                        val resumoTopo = when (turnosDoDia.size) {
                            0 -> "Nenhum turno cadastrado"
                            1 -> "1 turno cadastrado"
                            else -> "${turnosDoDia.size} turnos cadastrados"
                        }

                        CardExpansivelBase(
                            tituloTopo = resumoTopo,
                            tituloPrincipal = dia.nomeCompleto,
                            isExpandido = isExpandido,
                            mostrarAcoesTopo = false,
                            onClickToggle = { diasExpandidos[dia.chave] = !isExpandido }
                        ) {
                            HorizontalDivider(color = LinhaDivisoria, thickness = 1.dp)

                            if (turnosDoDia.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Nenhum psicólogo escalado para este dia.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextoSecundario
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(6.dp))

                                turnosDoDia.forEachIndexed { index, horario ->
                                    val nomePsicologo = horario.psicologo
                                        ?: psicologos.firstOrNull { it.id == horario.psicologoId }?.nome
                                        ?: "Psicólogo #${horario.psicologoId}"

                                    val iniciais = remember(nomePsicologo) {
                                        nomePsicologo
                                            .removePrefix("Dr. ")
                                            .removePrefix("Dra. ")
                                            .trim()
                                            .split(" ")
                                            .filter { it.isNotBlank() }
                                            .take(2)
                                            .joinToString("") { it.first().uppercase() }
                                            .ifEmpty { "PS" }
                                    }

                                    val inicio = horario.horaInicio.take(5)
                                    val fim = horario.horaFim.take(5)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { horarioSelecionado.value = horario }
                                            .padding(vertical = 10.dp, horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Avatar com iniciais
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(VerdeClinica.copy(alpha = 0.12f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = iniciais,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = VerdeClinica
                                            )
                                        }

                                        // Nome + Descrição
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = nomePsicologo,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextoPrincipal,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (!horario.descricao.isNullOrBlank()) {
                                                    "$inicio às $fim  •  ${horario.descricao}"
                                                } else {
                                                    "$inicio às $fim"
                                                },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextoSecundario,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        if (!telaPequena) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Branco,
                                                border = BorderStroke(1.dp, CardBordaCinza)
                                            ) {
                                                Text(
                                                    text = "$inicio – $fim",
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextoPrincipal
                                                )
                                            }

                                            BadgeStatus(
                                                texto = if (horario.ativo) "Ativo" else "Inativo",
                                                isAtivo = horario.ativo
                                            )
                                        }

                                        // Ações do turno
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = { horarioSelecionado.value = horario },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(Res.drawable.visibility),
                                                    contentDescription = "Visualizar",
                                                    tint = TextoSecundario,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            if (permitirEscrita) {
                                                IconButton(
                                                    onClick = { vm.editar(horario) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        painter = painterResource(Res.drawable.edit_square_24px),
                                                        contentDescription = "Editar",
                                                        tint = VerdeClinica,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = { horarioParaExcluir.value = horario },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        painter = painterResource(Res.drawable.delete_24px),
                                                        contentDescription = "Excluir",
                                                        tint = TextoErro,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (index < turnosDoDia.lastIndex) {
                                        HorizontalDivider(color = LinhaDivisoria, thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Modal de Detalhes
        horarioSelecionado.value?.let { horario ->
            val nomePsicologo = horario.psicologo
                ?: psicologos.firstOrNull { it.id == horario.psicologoId }?.nome
                ?: "Psicólogo #${horario.psicologoId}"

            ModalDetalhesBase(
                titulo = "Detalhes do Horário",
                onDismiss = { horarioSelecionado.value = null },
                conteudo = {
                    ItemDetalhe(rotulo = "Psicólogo(a)", valor = nomePsicologo)
                    ItemDetalhe(rotulo = "Dia da semana", valor = horario.diaSemana)
                    ItemDetalhe(rotulo = "Horário de início", valor = horario.horaInicio)
                    ItemDetalhe(rotulo = "Horário de fim", valor = horario.horaFim)
                    ItemDetalhe(rotulo = "Descrição", valor = horario.descricao ?: "Nenhuma")
                    ItemDetalhe(
                        rotulo = "Status",
                        valor = if (horario.ativo) "Ativo" else "Inativo"
                    )
                }
            )
        }

        // Modal de Exclusão
        horarioParaExcluir.value?.let { horario ->
            AppDeleteModal(
                title = "Confirmar Exclusão",
                message = "Tem certeza de que deseja excluir este horário disponível? Esta ação não pode ser desfeita.",
                onDismiss = { horarioParaExcluir.value = null },
                onConfirm = {
                    vm.apagar(horario)
                    horarioParaExcluir.value = null
                }
            )
        }
    }
}

@Composable
private fun BotaoFiltroDia(
    titulo: String,
    quantidade: Int,
    selecionado: Boolean,
    temAtendimento: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (selecionado) VerdeClinica else Branco,
        border = BorderStroke(
            width = 1.dp,
            color = if (selecionado) VerdeClinica else CardBordaCinza
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            selecionado -> Branco
                            temAtendimento -> VerdeClinica
                            else -> CardBordaCinza
                        }
                    )
            )
            Text(
                text = if (quantidade > 0) "$titulo ($quantidade)" else titulo,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selecionado) FontWeight.SemiBold else FontWeight.Normal,
                color = when {
                    selecionado -> Branco
                    temAtendimento -> TextoPrincipal
                    else -> TextoSecundario
                }
            )
        }
    }
}