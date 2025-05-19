package servicios


import accesodatos.RepoActividades
import dominio.*
import org.slf4j.LoggerFactory
import presentacion.ConsolaUI

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

class ActividadService {
    private val consola = ConsolaUI()
    private val repo = RepoActividades()
    private val servicioUsuario = UsuariosService(consola)
    private val historial = ControlDeHistorial()
    private val logger = LoggerFactory.getLogger(ActividadService::class.java)

    fun gestionarPrograma() {
        try {
            usuariosConActividades()
            do {
                consola.mostrarMenu()
                val opcion = consola.pedirOpcion("Elige una opción", 0, 6)
                gestionarOpcion(opcion)
            } while (opcion != 0)
        } catch (e: Exception) {
            logger.error("Error inesperado al gestionar el programa: ${e.message}", e)
        }
    }


    private fun agregarSubtarea() {
        try {
            // Listar las tareas disponibles para elegir la tarea madre
            consola.listarTareas(repo.tareas)

            // Pedir al usuario que seleccione la tarea madre por ID
            val idTareaMadre = consola.pedirInfo("Introduce el ID de la tarea madre a la que deseas agregar una subtarea:").toIntOrNull()
            val tareaMadre = repo.tareas.find { it.getIdActividad().toInt() == idTareaMadre }

            if (tareaMadre != null) {
                // Crear la subtarea
                val subtarea = Tarea.creaInstancia(
                    consola.pedirInfo("Descripción de la subtarea:"),
                    consola.pedirInfo("Usuario asignado:"),
                    consola.pedirEtiqueta()
                )

                // Asignar la subtarea a la tarea madre
                tareaMadre.agregarSubTarea(subtarea)

                // Actualizar el fichero para incluir la tarea madre con sus subtareas
                Utils.actualizarTareaEnFichero(tareaMadre)

                println("¡Subtarea añadida con éxito!")
                historial.agregarHistorial("Subtarea agregada a la tarea ${tareaMadre.getIdActividad()}")
            } else {
                println("Error: No se encontró la tarea madre con el ID proporcionado.")
            }
        } catch (e: Exception) {
            println("¡Error! Detalle: ${e.message}")
        }
    }


    private fun usuariosConActividades() {
        try {
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
            logger.trace("Usuarios asociados con actividades correctamente.")
        } catch (e: Exception) {
            logger.error("Error al asociar usuarios con actividades: ${e.message}", e)
        }
    }

    private fun cambiarEstado(tarea: Tarea) {
        val estadoNuevo = consola.pedirInfo("CAMBIE EL ESTADO DE LA TAREA: ABIERTA, EN_PROGRESO, FINALIZADA")
        val estado = EstadoTarea.getEstado(estadoNuevo)
        if (estado != null) {
            tarea.actualizarEstado(estado)
            repo.cambiarEstado(tarea, historial, estado)
            println("¡Estado de la tarea y su subtarea actualizado con éxito!")
        } else {
            println("¡Error! Estado no válido.")
        }
    }

    private fun filtradoPorTipo(){
        var opcion = -1

        do{
            try{
                println("1) Mostrar tareas")
                println("2) Mostrar eventos")
                println("0) SALIR")
                opcion = consola.pedirOpcion("Introduce opción",0,2)
                when(opcion){
                    1-> consola.listarTareas(repo.tareas)
                    2-> consola.listarEventos(repo.eventos)
                    else-> Exception("El valor introducido se sale del rango")
                }
            }catch(e: Exception){
                println("¡Error! Detalle: $e")
            }
        }while(opcion != 0)
    }

    private fun filtrarPorEstado(){
        var opcion = -1

        do{
            try{
                println("1) MOSTRAR ABIERTAS")
                println("2) MOSTRAR EN PROGRESO")
                println("3) MOSTRAR FINALIZADAS")
                println("0) SALIR")
                opcion = consola.pedirOpcion("Introduce opción",0,3)

                var filtrado: EstadoTarea? = null

                when(opcion){
                    1-> filtrado = EstadoTarea.ABIERTA
                    2-> filtrado = EstadoTarea.EN_PROGRESO
                    3-> filtrado = EstadoTarea.FINALIZADA
                    else-> Exception("El valor introducido se sale del rango")
                }

                for(tarea in repo.tareas){
                    if(tarea.estado == filtrado){
                        println(tarea.obtenerDetalle())
                    }
                }

            }catch(e: Exception){
                println("¡Error! Detalle: $e")
            }
        }while(opcion != 0)
    }

    private fun filtradoPorUsuarios(){
        var seguir = true
        do{
            var encontrado = false
            try{
                println("Introduzca el nombre del usuario")
                val nombre = readln().trim()

                for(usuario in servicioUsuario.usuariosRepo.usuarios){
                    if(usuario.nombre == nombre){
                        encontrado = true
                        for(actividad in usuario.repoActividades.actividades){
                            println(actividad.obtenerDetalle())
                        }
                    }
                }

                if(!encontrado){
                    println("El usuario introducido no ha sido encontrado")
                    seguir = consola.preguntarSeguir()
                }

                else{
                    seguir = consola.preguntarSeguir()
                }
            }catch(e: Exception){
                println("¡Error! Detalle: $e")
            }
        }while(seguir)
    }
    private fun filtradoPorEtiquetas(){
        var opcion = -1

        do{
            try{
                println("1) Mostrar tareas URGENTES")
                println("2) Mostrar tareas SENCILLAS")
                println("3) Mostrar tareas de DOCUMENTACIÓN")
                println("4) Mostrar tareas de REVISIÓN")
                println("0) SALIR")
                opcion = consola.pedirOpcion("Introduce opción",0,4)

                var filtrado: EtiquetasTareas? = null
                when(opcion){
                    1-> filtrado = EtiquetasTareas.URGENTE
                    2-> filtrado = EtiquetasTareas.SENCILLA
                    3-> filtrado = EtiquetasTareas.DOCUMENTACION
                    4-> filtrado = EtiquetasTareas.REVISION
                    else-> Exception("El valor introducido se sale del rango")
                }
                for(tarea in repo.tareas){
                    if(tarea.etiqueta == filtrado){
                        println(tarea.obtenerDetalle())
                    }
                }
            }catch(e: Exception){
                println("¡Error! Detalle: $e")
            }
        }while(opcion != 0)
    }

    private fun filtradoPorFechas() {
        var opcion: Int
        do {
            println("Selecciona una opción para filtrar actividades:")
            println("1) Enseñar actividades para hoy")
            println("2) Enseñar actividades para mañana")
            println("3) Enseñar actividades de esta semana")
            println("4) Enseñar actividades de este mes")
            println("0) SALIR")

            opcion = consola.pedirOpcion("Elige una opción", 0, 4)

            when (opcion) {
                1 -> filtrarActividades { it.fecha== Utils.obtenerFechaActual() }
                2 -> filtrarActividades { it.fecha == LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) }
                3 -> filtrarActividades { fechaDentroDeSemana(it.fecha) }
                4 -> filtrarActividades { fechaDentroDeMes(it.fecha) }
                0 -> println("Saliendo del filtrado por fechas...")
                else -> println("Opción no válida, intenta de nuevo.")
            }
        } while (opcion != 0)
    }

    private fun filtrarActividades(condicion: (Actividad) -> Boolean) {
        val actividadesFiltradas = repo.actividades.filter(condicion)
        if (actividadesFiltradas.isEmpty()) {
            println("No hay actividades que coincidan con el filtro.")
        } else {
            println("Actividades encontradas:")
            actividadesFiltradas.forEach { println(it.obtenerDetalle()) }
        }
    }

    private fun fechaDentroDeSemana(fecha: String): Boolean {
        val hoy = LocalDate.now()
        val inicioSemana = hoy.with(DayOfWeek.MONDAY)
        val finSemana = hoy.with(DayOfWeek.SUNDAY)
        val fechaActividad = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        return !fechaActividad.isBefore(inicioSemana) && !fechaActividad.isAfter(finSemana)
    }

    private fun fechaDentroDeMes(fecha: String): Boolean {
        val hoy = LocalDate.now()
        val fechaActividad = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        return fechaActividad.month == hoy.month && fechaActividad.year == hoy.year
    }
    private fun filtrar(){
        var opcion = -1
        do {
            try {
                println("SELECCIONE UN MODO DE FILTRADO")
                println("1.- Por tipo (Tarea/Evento)")
                println("2.- Por estado")
                println("3.- Por etiquetas")
                println("4.- Por usuarios")
                println("5.- Por fechas")
                println("0) SALIR")
                opcion = consola.pedirOpcion(">> ",0,5)

                when(opcion){
                    1-> filtradoPorTipo()
                    2-> filtrarPorEstado()
                    3-> filtradoPorEtiquetas()
                    4-> filtradoPorUsuarios()
                    5-> filtradoPorFechas()
                }
            }catch(e: Exception){
                println("¡Error! Vuelve a introducir Detalle: $e")
            }
        }while(opcion != 0)
    }

    private fun resumen() {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val hoy = LocalDate.now()
        val manana = hoy.plusDays(1)
        val semana = hoy.get(WeekFields.of(Locale.getDefault()).weekOfYear())
        val mes = hoy.monthValue

        // Filtrar actividades por fecha
        val actividadesHoy = repo.actividades.filter {
            LocalDate.parse(it.fecha, formatter) == hoy
        }
        val actividadesManana = repo.actividades.filter {
            LocalDate.parse(it.fecha, formatter) == manana
        }
        val actividadesSemana = repo.actividades.filter {
            val fechaActividad = LocalDate.parse(it.fecha, formatter)
            fechaActividad.get(WeekFields.of(Locale.getDefault()).weekOfYear()) == semana
        }
        val actividadesMes = repo.actividades.filter {
            val fechaActividad = LocalDate.parse(it.fecha, formatter)
            fechaActividad.monthValue == mes
        }

        // Imprimir resumen
        println("--------------------------------------------------")
        println("TOTAL DE TAREAS MADRES: ${repo.actividades.size}")
        println("TOTAL DE EVENTOS: ${repo.eventos.size}")
        println("EN TOTAL ${repo.actividades.size} ACTIVIDADES REALIZADAS")
        println("--------------------------------------------------")
        println("ACTIVIDADES PARA HOY: ${actividadesHoy.size}")
        println("ACTIVIDADES PARA MAÑANA: ${actividadesManana.size}")
        println("ACTIVIDADES PARA ESTA SEMANA: ${actividadesSemana.size}")
        println("ACTIVIDADES PARA ESTE MES: ${actividadesMes.size}")
        println("--------------------------------------------------")
        historial.agregarHistorial("Se ha mirado el resumen del programa")
    }

    private fun gestionarOpcion(opcion: Int) {
        when (opcion) {
            1 -> anadirActividad()
            2 -> listarActividades()
            3 -> cambiarEstado(pedirIdTarea())
            4 -> filtrar()
            5-> agregarSubtarea()
            6-> resumen()
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
        val actividadesUnicas = repo.actividades.distinct() // Eliminar duplicados
        consola.listarActividades(actividadesUnicas.toMutableList())
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