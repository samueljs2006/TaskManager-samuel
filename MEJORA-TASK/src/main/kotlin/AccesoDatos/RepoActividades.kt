package AccesoDatos

import Dominio.Actividad
import Dominio.Evento
import Dominio.Tarea

class RepoActividades(
    override val actividades: MutableList<Actividad> = mutableListOf<Actividad>(),
    override val tareas: MutableList<Tarea> = mutableListOf<Tarea>(),
    override val eventos: MutableList<Evento> = mutableListOf<Evento>()
) : IActividadRepository