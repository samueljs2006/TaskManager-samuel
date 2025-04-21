package AccesoDatos

import Dominio.Actividad
import Dominio.Evento
import Dominio.Tarea

class RepoActividades(
    override val actividades: MutableList<Actividad> = mutableListOf<Actividad>(),
    override val tareas: MutableList<Tarea> = mutableListOf<Tarea>(),
    override val eventos: MutableList<Evento> = mutableListOf<Evento>()
) : IActividadRepository{

    init{
        cargarActividades()
    }

    fun aniadirActividad(actividad: Actividad){
        actividades.add(actividad)

        when(actividad){
            is Tarea-> tareas.add(actividad)
            is Evento-> eventos.add(actividad)
        }

        Utils.aniadirActividad(rutaFicheroActidades,actividad)
    }

    private fun cargarActividades(){
        val ficheroAct = Utils.leerArchivo(rutaFicheroActidades)
        for(act in ficheroAct){
            val actividad = Utils.deserializarActividad(act)
            actividades.add(actividad)

            when(actividad){
                is Tarea-> tareas.add(actividad)
                is Evento-> eventos.add(actividad)
            }
        }
    }

    companion object{
        val rutaFicheroActidades = "src/main/kotlin/Datos/Actividades.txt"
    }
}