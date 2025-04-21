package Dominio

/**
 * Enum class que contiene los estados en los que se puede encontrar la tarea
 * La tarea puede ser ABIERTA o CERRADA
 * @param e-> Para poder poner un mensaje personalizado al instanciar
 */
enum class EstadoTarea(e:String) {
    ABIERTA("ABIERTA"),
    EN_PROGRESO("EN_PROGRESO"),
    FINALIZADA("FINALIZADA");

    companion object{
        fun getEstado(estado:String): EstadoTarea?{
            return when(estado.uppercase()){
                "ABIERTA"-> EstadoTarea.ABIERTA
                "FINALIZADA"-> EstadoTarea.FINALIZADA
                "EN_PROGRESO"-> EstadoTarea.EN_PROGRESO
                "EN PROGRESO"-> EstadoTarea.EN_PROGRESO
                else -> null
            }
        }
    }
}