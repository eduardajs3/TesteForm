package edu.front.psi.clin

data class BloqueioAgenda(
    val id:Int,
    val psicologo: String,
    val dataHoraInicio: String,
    val dataHoraFim: String,
    val motivo: String,
    val ativo: Boolean,
    
)
