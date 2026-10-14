package edu.front.psi.clin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val horariosDisponiveis = mutableListOf<HorarioDisponivel>()

@Composable
fun HorarioDisponivelFormulario() {
    val psicologo = remember { mutableStateOf("") }
    val diaSemana = remember { mutableStateOf("") }
    val horaInicio = remember { mutableStateOf("") }
    val horaFim = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val ativo = remember { mutableStateOf("true") }
    val mensagem = remember { mutableStateOf("") }

    CardFormulario(
        titulo = "Cadastro de Horário Disponível",
        subtitulo = "Defina a grade de atendimento e disponibilidade dos psicólogos"
    ) {
        MeuTextField(
            value = psicologo.value,
            onValueChange = { psicologo.value = it },
            label = "Nome do Psicólogo",
            placeholder = "Ex: Dr. Roberto Alencar"
        )

        MeuTextField(
            value = diaSemana.value,
            onValueChange = { diaSemana.value = it },
            label = "Dia da Semana",
            placeholder = "Ex: Segunda-feira ou Terça-feira"
        )

        MeuTextField(
            value = horaInicio.value,
            onValueChange = { horaInicio.value = it },
            label = "Horário de Início",
            placeholder = "Ex: 08:00"
        )

        MeuTextField(
            value = horaFim.value,
            onValueChange = { horaFim.value = it },
            label = "Horário de Término",
            placeholder = "Ex: 09:00"
        )

        MeuTextField(
            value = descricao.value,
            onValueChange = { descricao.value = it },
            label = "Observação do Turno",
            placeholder = "Ex: Período matutino / Plantão presencial"
        )

        MeuTextField(
            value = ativo.value,
            onValueChange = { ativo.value = it },
            label = "Status Ativo",
            placeholder = "true ou false"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            BotaoSecundario(
                texto = "Limpar Campos",
                onClick = {
                    psicologo.value = ""
                    diaSemana.value = ""
                    horaInicio.value = ""
                    horaFim.value = ""
                    descricao.value = ""
                    ativo.value = "true"
                    mensagem.value = ""
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BotaoPrimario(
                texto = "Gravar Horário",
                onClick = {
                    if (
                        psicologo.value.isNotBlank() &&
                        diaSemana.value.isNotBlank() &&
                        horaInicio.value.isNotBlank() &&
                        horaFim.value.isNotBlank() &&
                        (ativo.value == "true" || ativo.value == "false")
                    ) {
                        val horario = HorarioDisponivel(
                            id = horariosDisponiveis.size + 1,
                            psicologo = psicologo.value,
                            diaSemana = diaSemana.value,
                            horaInicio = horaInicio.value,
                            horaFim = horaFim.value,
                            descricao = descricao.value,
                            ativo = ativo.value == "true"
                        )

                        horariosDisponiveis.add(horario)
                        mensagem.value = "Horário do(a) Dr(a). ${horario.psicologo} gravado com sucesso!"
                    } else {
                        mensagem.value = "Preencha os campos obrigatórios corretamente."
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        MensagemFeedback(mensagem = mensagem.value)
    }
}
