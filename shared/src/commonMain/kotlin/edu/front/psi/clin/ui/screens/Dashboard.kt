package edu.front.psi.clin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.front.psi.clin.ui.theme.CoresApp

@Composable
fun DashboardTela(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Painel Geral",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CoresApp.Primaria
        )
        Text(
            text = "Bem-vindo(a) ao sistema de gestão PsiClin.",
            fontSize = 14.sp,
            color = CoresApp.Primaria.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardMetrica("Consultas Hoje", "8", Modifier.weight(1f))
            CardMetrica("Pacientes", "24", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardMetrica("Salas Ativas", "4", Modifier.weight(1f))
            CardMetrica("Evoluções Pendentes", "2", Modifier.weight(1f))
        }
    }
}

@Composable
private fun CardMetrica(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = CoresApp.FundoFormulario),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = titulo, fontSize = 12.sp, color = CoresApp.Primaria)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = valor, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = CoresApp.Primaria)
        }
    }
}