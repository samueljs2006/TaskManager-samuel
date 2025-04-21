package Servicios

import AccesoDatos.*
import Dominio.*
import Presentacion.*

class ActividadService {
    private val consola = ConsolaUI()
    private val repo = RepoActividades()
    private val servicioUsuario = UsuariosService(consola)
    private val historial = ControlDeHistorial()

    fun gestionarPrograma() {
        usuariosConActividades()
        do {
            val opcion = consola.pedirOpcion("Elige una opción:\n1) Añadir actividad\n2) Listar actividades\n3) Cambiar estado de tarea\n0) Salir", 0, 3)
            gestionarOpcion(opcion)
        } while (opcion != 0)
    }

    private fun usuariosConActividades() {
        for (usuario in servicioUsuario.usuariosRepo.usuarios) {
            for (actividad in repo.actividades) {
                if (actividad.obtenerUsuario() == usuario.nombre && !usuario.repoActividades.actividades.contains(actividad)) {
                    usuario.repoActividades.actividades.add(actividad)
                    when (actividad) {
                        is Tarea -> usuario.repoActividades.tareas.add(actividad)
                        is Evento -> usuario.repoActividades.eventos.add(actividad)
                    }
                }
            }
        }
    }

    private fun cambiarEstado(tarea: Tarea) {
        val id = tarea.getIdActividad()
        var estadoValido = false
        do {
            val estadoNuevo = consola.pedirInfo("CAMBIE EL ESTADO DE LA TAREA: ABIERTA, EN_PROGRESO, FINALIZADA")
            val estado = EstadoTarea.getEstado(estadoNuevo)
            if (estado != null) {
                tarea.estado = estado
                if (estado == EstadoTarea.FINALIZADA) {
                    repo.actividades.remove(tarea)
                    repo.tareas.remove(tarea)
                    println("¡Tarea cerrada con éxito!")
                    historial.agregarHistorial("Tarea con id $id cerrada con éxito")
                }
                estadoValido = true
            } else {
                println("¡Error! Estado no válido, vuelve a intentarlo.")
                historial.agregarHistorial("Error de estado no válido, se vuelve a intentar")
            }
        } while (!estadoValido)
    }

    private fun gestionarOpcion(opcion: Int) {
        when (opcion) {
            1 -> anadirActividad()
            2 -> listarActividades()
            3 -> cambiarEstado(pedirIdTarea())
        }
    }

    private fun anadirActividad() {
        val opcion = consola.pedirOpcion("¿Qué quieres crear?\n1) Tarea\n2) Evento\n0) Cancelar", 0, 2)
        if (opcion == 0) return

        val actividad = consola.crearActividad(opcion, repo, servicioUsuario.usuariosRepo)
        if (actividad != null) {
            repo.aniadirActividad(actividad)
            println("¡Actividad añadida con éxito!")
            historial.agregarHistorial("Actividad agregada con éxito")
        } else {
            println("¡Error al crear la actividad!")
            historial.agregarHistorial("Error al crear actividad")
        }
    }

    private fun listarActividades() {
        consola.listarActividades(repo.actividades)
        historial.agregarHistorial("Se listan todas las actividades")
    }

    private fun pedirIdTarea(): Tarea {
        var tareaEncontrada: Tarea? = null
        var tareaValida = false
        do {
            try {
                println("Introduzca el ID de la tarea:")
                val id = readln().toInt()
                tareaEncontrada = repo.tareas.first { it.getIdActividad().toInt() == id }
                tareaValida = true
            } catch (e: NoSuchElementException) {
                println("¡Error! ID no encontrado, inténtelo de nuevo.")
                historial.agregarHistorial("ID de la actividad no encontrado, se intenta de nuevo")
            }
        } while (!tareaValida)
        return tareaEncontrada!!
    }

    companion object {
        fun iniciarPrograma() {
            ActividadService().gestionarPrograma()
        }
    }
}