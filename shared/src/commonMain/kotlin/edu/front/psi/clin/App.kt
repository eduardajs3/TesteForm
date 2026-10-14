package edu.front.psi.clin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val tela = remember { mutableStateOf("menu") }
    val scrollState = rememberScrollState()

    PsiClinTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VerdeSalvia)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Ψ",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "PsyClin",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal
                                )
                                Text(
                                    text = "Clínica de Psicologia",
                                    fontSize = 12.sp,
                                    color = TextoSecundario
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CremeFundo,
                        titleContentColor = TextoPrincipal
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = VerdePastel.copy(alpha = 0.5f),
                    contentColor = TextoSecundario,
                    modifier = Modifier.height(48.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sistema de Gestão Clínica • Design Clean",
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )
                    }
                }
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CremeFundo)
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .verticalScroll(scrollState)
            ) {
                if (tela.value == "menu") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CremeCard),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CremeBorda, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Módulos de Cadastro",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            Text(
                                text = "Selecione o formulário desejado para registrar novas informações:",
                                fontSize = 14.sp,
                                color = TextoSecundario,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                            )

                            // Itens do Menu
                            val opcoes = listOf(
                                Triple("paciente", "Cadastrar Paciente", "Ficha cadastral, contatos e observações"),
                                Triple("psicologo", "Cadastrar Psicólogo", "Registro profissional CRP e especialidade"),
                                Triple("horario", "Cadastrar Horário Disponível", "Grade e horários de atendimento"),
                                Triple("sala", "Cadastrar Sala de Atendimento", "Consultórios e capacidade"),
                                Triple("tipo", "Cadastrar Tipo de Atendimento", "Modalidades e tempos de sessão"),
                                Triple("pagamento", "Cadastrar Pagamento", "Controle de repasses e coparticipação"),
                                Triple("funcionario", "Cadastrar Funcionário", "Equipe administrativa e recepção"),
                                Triple("convenio", "Cadastrar Convênio", "Operadoras de saúde e cobertura")
                            )

                            opcoes.forEach { (chave, titulo, desc) ->
                                ItemMenuCard(
                                    titulo = titulo,
                                    descricao = desc,
                                    onClick = { tela.value = chave }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        BotaoSecundario(
                            texto = "← Voltar ao Menu Principal",
                            onClick = { tela.value = "menu" }
                        )
                    }

                    when (tela.value) {
                        "paciente" -> PacienteFormulario()
                        "psicologo" -> PsicologoFormulario()
                        "pagamento" -> PagamentoFormulario()
                        "horario" -> HorarioDisponivelFormulario()
                        "sala" -> SalaAtendimentoFormulario()
                        "tipo" -> TipoAtendimentoFormulario()
                        "funcionario" -> FuncionarioFormulario()
                        "convenio" -> ConvenioFormulario()
                    }
                }
            }
        }
    }
}

@Composable
fun ItemMenuCard(
    titulo: String,
    descricao: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CremeFundo),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CremeBorda, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPrincipal
                )
                Text(
                    text = descricao,
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(VerdePastel)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Acessar →",
                    color = VerdeSalvia,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}