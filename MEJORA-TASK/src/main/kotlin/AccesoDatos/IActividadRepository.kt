package AccesoDatos

import Dominio.Actividad
import Dominio.Tarea
import Dominio.Evento

interface IActividadRepository {
    val actividades: MutableList<Actividad>
    val tareas: MutableList<Tarea>
    val eventos: MutableList<Evento>
}