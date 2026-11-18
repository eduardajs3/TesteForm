package edu.front.psi.clin.ui.screens.agenda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Convenio
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.model.Psicologo
import edu.front.psi.clin.model.TipoAtendimento
import edu.front.psi.clin.ui.componentes.BotaoPadrao
import edu.front.psi.clin.ui.componentes.CampoDropdownPadrao
import edu.front.psi.clin.ui.theme.*
import edu.front.psi.clin.ui.viewmodel.AgendaViewModel

@Composable
internal fun EtapaDadosAtendimentoAgenda(
    vm: AgendaViewModel,
    pacientesDisponiveis: List<Paciente>,
    psicologosDisponiveis: List<Psicologo>,
    tiposDisponiveis: List<TipoAtendimento>,
    conveniosDisponiveis: List<Convenio>,
    pacienteSelecionado: Paciente?,
    psicologoSelecionado: Psicologo?,
    tipoSelecionado: TipoAtendimento?,
    convenioSelecionado: Convenio?,
    onAvancar: () -> Unit
) {
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
                    val sel = pacientesDisponiveis.firstOrNull { it.nome == nome }
                    vm.pacienteId.value = sel?.id
                    vm.pacienteIdValidado.definir(sel?.id?.toString().orEmpty())
                },
                isError = vm.pacienteIdValidado.temErro.value,
                errorMessage = if (vm.pacienteIdValidado.temErro.value) "Campo obrigatório" else null
            )
            CampoDropdownPadrao(
                label = "Psicólogo *",
                valorSelecionado = psicologoSelecionado?.nome.orEmpty(),
                opcoes = psicologosDisponiveis.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val selecionado = psicologosDisponiveis.firstOrNull { it.nome == nome }
                    vm.psicologoId.value = selecionado?.id
                    vm.psicologoIdValidado.definir(selecionado?.id?.toString().orEmpty())
                    vm.dataAtendimento.limpar()
                    vm.horaInicio.limpar()
                    vm.horaFim.limpar()
                    vm.salaId.value = null
                },
                isError = vm.psicologoIdValidado.temErro.value,
                errorMessage = if (vm.psicologoIdValidado.temErro.value) "Campo obrigatório" else null
            )
            CampoDropdownPadrao(
                label = "Tipo de atendimento *",
                valorSelecionado = tipoSelecionado?.nome.orEmpty(),
                opcoes = tiposDisponiveis.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val selecionado = tiposDisponiveis.firstOrNull { it.nome == nome }
                    vm.tipoAtendimentoId.value = selecionado?.id
                    vm.tipoAtendimentoIdValidado.definir(selecionado?.id?.toString().orEmpty())
                    vm.dataAtendimento.limpar()
                    vm.horaInicio.limpar()
                    vm.horaFim.limpar()
                    vm.salaId.value = null
                },
                isError = vm.tipoAtendimentoIdValidado.temErro.value,
                errorMessage = if (vm.tipoAtendimentoIdValidado.temErro.value) "Campo obrigatório" else null
            )
            CampoDropdownPadrao(
                label = "Convênio *",
                valorSelecionado = convenioSelecionado?.nome.orEmpty(),
                opcoes = conveniosDisponiveis.map { it.nome },
                onOpcaoSelecionada = { nome ->
                    val sel = conveniosDisponiveis.firstOrNull { it.nome == nome }
                    vm.convenioId.value = sel?.id
                    vm.convenioIdValidado.definir(sel?.id?.toString().orEmpty())
                },
                isError = vm.convenioIdValidado.temErro.value,
                errorMessage = if (vm.convenioIdValidado.temErro.value) "Campo obrigatório" else null
            )
        }
    }

    Spacer(Modifier.height(16.dp))
    BotaoPadrao(
        texto = "Ver disponibilidade",
        onClick = {
            if (vm.validarDadosAtendimento()) {
                vm.atualizarDisponibilidade()
                onAvancar()
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}