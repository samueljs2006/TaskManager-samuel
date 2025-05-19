package dominio

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
                "ABIERTA"-> ABIERTA
                "FINALIZADA"-> FINALIZADA
                "EN_PROGRESO"-> EN_PROGRESO
                "EN PROGRESO"-> EN_PROGRESO
                else -> null
            }
        }
    }
}