package AccesoDatos

import Dominio.Actividad
import Dominio.EstadoTarea
import Dominio.Evento
import Dominio.Tarea
import Servicios.ControlDeHistorial
import java.io.File

class RepoActividades(
    override val actividades: MutableList<Actividad> = mutableListOf(),
    override val tareas: MutableList<Tarea> = mutableListOf(),
    override val eventos: MutableList<Evento> = mutableListOf()
) : IActividadRepository {

    init {
        cargarActividades()
    }


    fun cambiarEstado(tarea: Tarea, historial: ControlDeHistorial, estadoTarea: EstadoTarea) {
        val id = tarea.getIdActividad()
        tarea.estado = estadoTarea
        val archivo = File(RUTA_FICHERO_ACTIVIDADES)

        archivo.writeText("") // Limpiar el archivo antes de escribir

        tareas.forEach { tareaPrincipal ->
            archivo.appendText("${tareaPrincipal.obtenerDetalle()}\n")

            if (tareaPrincipal is Tarea && tareaPrincipal.subTareas.isNotEmpty()) {
                tareaPrincipal.subTareas.forEach { subTarea ->
                    archivo.appendText("    - ${subTarea.obtenerDetalle()}\n")
                }
            }
        }

        println("¡Tarea cerrada con éxito!")
        historial.agregarHistorial("Tarea con id $id con estado cambiado a $estadoTarea con éxito")
    }

    fun aniadirActividad(actividad: Actividad) {
        if (!actividades.contains(actividad)) { // Evitar duplicados en la lista de actividades
            actividades.add(actividad)

            when (actividad) {
                is Tarea -> if (!tareas.contains(actividad)) tareas.add(actividad)
                is Evento -> if (!eventos.contains(actividad)) eventos.add(actividad)
            }

            Utils.aniadirActividad(RUTA_FICHERO_ACTIVIDADES, actividad)
        } else {
            println("La actividad ya existe, no se añadirá de nuevo.")
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
        val RUTA_FICHERO_ACTIVIDADES =
            "${System.getProperty("user.dir")}/src/main/kotlin/Datos/Actividades.txt".replace(
                "/",
                File.separator
            )
    }
}