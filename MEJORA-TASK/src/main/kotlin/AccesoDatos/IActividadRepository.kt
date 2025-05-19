package accesodatos

import dominio.Actividad
import dominio.Tarea
import dominio.Evento

interface IActividadRepository {
    val actividades: MutableList<Actividad>
    val tareas: MutableList<Tarea>
    val eventos: MutableList<Evento>
}