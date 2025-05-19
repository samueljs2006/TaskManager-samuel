package accesodatos

import dominio.Actividad
import dominio.EstadoTarea
import dominio.Evento
import dominio.Tarea
import servicios.ControlDeHistorial
import java.io.File
import java.nio.file.Paths

class RepoActividades(
    override val actividades: MutableList<Actividad> = mutableListOf(),
    override val tareas: MutableList<Tarea> = mutableListOf(),
    override val eventos: MutableList<Evento> = mutableListOf()
) : IActividadRepository {

    init {
        cargarActividades()
    }

    private fun cargarActividades() {
        val ficheroActividades = Utils.leerArchivo(RUTA_FICHERO_ACTIVIDADES)
        for (linea in ficheroActividades) {
            procesarLineaActividad(linea)
        }
    }
    /**
     * Procesa una línea del archivo de actividades y la convierte en un objeto Actividad.
     * Si la actividad no existe ya en la lista, se añade a la lista correspondiente.
     *
     * @param linea La línea del archivo que representa una actividad.
     */
    private fun procesarLineaActividad(linea: String) {
        try {
            val actividad = Utils.deserializarActividad(linea)
            if (actividad != null && !actividades.contains(actividad)) {
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



    companion object {
        val ruta_relativa = "MEJORA-TASK/src/main/kotlin/Datos/Actividades.txt"
        val RUTA_FICHERO_ACTIVIDADES = Paths.get(ruta_relativa).toAbsolutePath().toString() // Usar const para constantes
    }
}