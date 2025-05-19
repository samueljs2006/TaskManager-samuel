package accesodatos

import dominio.Actividad
import dominio.EstadoTarea
import dominio.Evento
import dominio.Tarea
import servicios.ControlDeHistorial
import java.io.File
import java.nio.file.Paths

/**
 * Repositorio para la gestión de actividades (tareas y eventos) en el sistema.
 *
 * Se encarga de cargar, almacenar y modificar actividades persistidas en fichero,
 * así como de mantener listas separadas para tareas y eventos.
 *
 * @property actividades Lista general de todas las actividades.
 * @property tareas Lista específica de tareas.
 * @property eventos Lista específica de eventos.
 */
class RepoActividades(
    override val actividades: MutableList<Actividad> = mutableListOf(),
    override val tareas: MutableList<Tarea> = mutableListOf(),
    override val eventos: MutableList<Evento> = mutableListOf()
) : IActividadRepository {

    init {
        cargarActividades()
    }

    /**
     * Carga las actividades desde el fichero persistente y las añade a las listas correspondientes.
     */
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

    /**
     * Cambia el estado de una tarea y actualiza el fichero de actividades.
     *
     * @param tarea Tarea a modificar.
     * @param historial Controlador de historial para registrar el cambio.
     * @param estadoTarea Nuevo estado a asignar.
     */
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

    /**
     * Añade una nueva actividad al repositorio y la persiste en el fichero.
     * Evita duplicados en las listas.
     *
     * @param actividad Actividad a añadir.
     */
    fun aniadirActividad(actividad: Actividad) {
        if (!actividades.contains(actividad)) {
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
        /**
         * Ruta relativa del fichero de actividades.
         */
        val ruta_relativa = "MEJORA-TASK/src/main/kotlin/Datos/Actividades.txt"
        /**
         * Ruta absoluta del fichero de actividades.
         */
        val RUTA_FICHERO_ACTIVIDADES = Paths.get(ruta_relativa).toAbsolutePath().toString()
    }
}