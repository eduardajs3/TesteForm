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
        onClick = onAvancar,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica)
    ) {
        Text("Ver disponibilidade")
    }
}
 