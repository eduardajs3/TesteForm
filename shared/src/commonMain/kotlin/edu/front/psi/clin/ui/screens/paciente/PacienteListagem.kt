package edu.front.psi.clin.ui.screens.paciente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.front.psi.clin.model.Paciente
import edu.front.psi.clin.ui.componentes.AppDeleteModal
import edu.front.psi.clin.ui.componentes.BarraPesquisa
import edu.front.psi.clin.ui.componentes.CardPaciente
import edu.front.psi.clin.ui.componentes.ModalDetalhesBase
import edu.front.psi.clin.ui.componentes.ItemDetalhe
import edu.front.psi.clin.ui.theme.Branco
import edu.front.psi.clin.ui.theme.TextoInativo
import edu.front.psi.clin.ui.theme.TextoSecundario
import edu.front.psi.clin.ui.theme.VerdeClinica
import edu.front.psi.clin.ui.viewmodel.PacienteViewModel
import frontpsiclinic.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun PacienteListagem(
    innerPadding: PaddingValues,
    vm: PacienteViewModel,
    onNavegarParaProntuario: (Paciente) -> Unit = {},
    onNavegarParaEvolucao: (Paciente) -> Unit = {}
) {
    val lista = vm.pacientesFiltrados.collectAsState()
    val filtro = vm.filtro.collectAsState()
    val pacienteSelecionado = remember { mutableStateOf<Paciente?>(null) }
    var pacienteParaExcluir by remember { mutableStateOf<Paciente?>(null) }
    var exibirDetalhesCadastrais by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pacientes",
                        style = MaterialTheme.typography.titleLarge,
                        color = VerdeClinica
                    )
                    Text(
                        text = "Gerencie os pacientes cadastrados na clínica",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }

                Button(
                    onClick = vm::novoPaciente,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.add),
                        contentDescription = "Novo Paciente",
                        tint = Branco,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Novo Paciente",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Branco
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BarraPesquisa(
                text = filtro.value,
                onTextChange = vm::atualizarFiltro,
                placeholderText = "Buscar por nome ou CPF..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lista.value) { paciente ->
                    CardPaciente(
                        paciente = paciente,
                        onClick = { pacienteSelecionado.value = paciente },
                        onEditarClick = { vm.editar(paciente) },
                        onExcluirClick = { pacienteParaExcluir = paciente }
                    )
                }
            }
        }

        pacienteSelecionado.value?.let { paciente ->
            if (!exibirDetalhesCadastrais) {
                ModalDetalhesBase(
                    titulo = paciente.nome,
                    onDismiss = { pacienteSelecionado.value = null },
                    conteudo = {
                        Text(
                            text = "Selecione a opção desejada:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    },
                    acoesExtras = {
                        Button(
                            onClick = {
                                val target = paciente
                                pacienteSelecionado.value = null
                                onNavegarParaProntuario(target)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Prontuário",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Branco
                            )
                        }

                        Button(
                            onClick = {
                                val target = paciente
                                pacienteSelecionado.value = null
                                onNavegarParaEvolucao(target)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeClinica),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Evolução Clínica",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Branco
                            )
                        }

                        OutlinedButton(
                            onClick = { exibirDetalhesCadastrais = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Informações Cadastrais",
                                style = MaterialTheme.typography.bodyLarge,
                                color = VerdeClinica
                            )
                        }
                    }
                )
            } else {
                ModalDetalhesBase(
                    titulo = "Informações Cadastrais",
                    onDismiss = {
                        exibirDetalhesCadastrais = false
                        pacienteSelecionado.value = null
                    },
                    conteudo = {
                        ItemDetalhe("Nome", paciente.nome)
                        ItemDetalhe("CPF", paciente.cpf)
                        ItemDetalhe("Data de nascimento", paciente.dataNascimento)
                        ItemDetalhe("E-mail", paciente.email)
                        ItemDetalhe("Telefone", paciente.telefone)
                        ItemDetalhe("Endereço", paciente.endereco)
                        ItemDetalhe("Observações", paciente.observacoes)
                        ItemDetalhe(
                            rotulo = "Status",
                            valor = if (paciente.ativo) "Ativo" else "Inativo"
                        )
                    }
                )
            }
        }

        pacienteParaExcluir?.let { paciente ->
            AppDeleteModal(
                title = "Exclusão de paciente",
                message = "Tem certeza que deseja excluir o cadastro de ${paciente.nome}?",
                onDismiss = { pacienteParaExcluir = null },
                onConfirm = {
                    vm.apagar(paciente)
                    pacienteParaExcluir = null
                }
            )
        }
    }
}