package AccesoDatos

import Dominio.Actividad
import Dominio.Evento
import Dominio.Tarea

class RepoActividades(
    override val actividades: MutableList<Actividad> = mutableListOf(),
    override val tareas: MutableList<Tarea> = mutableListOf(),
    override val eventos: MutableList<Evento> = mutableListOf()
) : IActividadRepository {

    init {
        cargarActividades()
    }

    fun aniadirActividad(actividad: Actividad) {
        if (!actividades.contains(actividad)) { // Evitar duplicados
            actividades.add(actividad)

            when (actividad) {
                is Tarea -> tareas.add(actividad)
                is Evento -> eventos.add(actividad)
            }

            Utils.aniadirActividad(RUTA_FICHERO_ACTIVIDADES, actividad)
        }
    }

    private fun cargarActividades() {
        val ficheroActividades = Utils.leerArchivo(RUTA_FICHERO_ACTIVIDADES)
        for (linea in ficheroActividades) {
            try {
                val actividad = Utils.deserializarActividad(linea)
                if (actividad != null && !actividades.contains(actividad)) { // Verificar que no sea null y evitar duplicados
                    actividades.add(actividad)

                    when (actividad) {
                        is Tarea -> tareas.add(actividad)
                        is Evento -> eventos.add(actividad)
                    }
                }
            } catch (e: Exception) {
                println("Error al cargar una actividad desde el fichero: ${e.message}")
            }
        }
    }

    companion object {
        const val RUTA_FICHERO_ACTIVIDADES = "src/main/kotlin/Datos/Actividades.txt" // Usar const para constantes
    }
}