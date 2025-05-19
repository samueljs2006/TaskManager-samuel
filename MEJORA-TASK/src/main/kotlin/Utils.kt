import accesodatos.RepoActividades
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import dominio.*
import java.io.IOException

object Utils {
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun obtenerFechaActual(): String {
        return LocalDate.now().format(formatter)
    }

    fun esFechaValida(fecha: String): Boolean {
        return try {
            LocalDate.parse(fecha, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun deserializarUsuario(nombreUsuario: String): Usuario {
        return Usuario.creaInstancia(nombreUsuario)
    }

    fun esEtiqueta(etiqueta: String): Boolean {
        return EtiquetasTareas.getEtiqueta(etiqueta) != null
    }


    fun deserializarActividad(serializado: String): Actividad? {
        if (serializado.isBlank() || serializado == "Subtareas:" || serializado == "Sin subtareas") {
            // Ignorar líneas en blanco o irrelevantes
            return null
        }

        val partes = serializado.split(";")
        return try {
            if (partes.size >= 6) {
                val usuario = partes[0]
                val id = partes[1]
                val descripcion = partes[2]
                val fechaCreacion = partes[3]

                if (!esFechaValida(fechaCreacion)) {
                    println("Error: La fecha de creación no es válida para la actividad con ID $id")
                    return null
                }

                if (esEtiqueta(partes[5])) {
                    val estado = partes[4]
                    val etiqueta = partes[5]

                    // Crear tarea principal
                    val tarea = Tarea.creaInstancia(
                        usuario,
                        id,
                        EtiquetasTareas.getEtiqueta(etiqueta)!!,
                        fechaCreacion,
                        descripcion,
                        estado
                    )

                    // Procesar las subtareas
                    val indexSubtareas = serializado.indexOf("Subtareas:")
                    if (indexSubtareas != -1) {
                        val subtareasSerializadas = serializado.substring(indexSubtareas + 11).split("\n    - ")
                        subtareasSerializadas.drop(1).forEach { subTareaSerializada ->
                            val subTarea = deserializarActividad(subTareaSerializada) as? Tarea
                            if (subTarea != null) {
                                tarea.subTareas.add(subTarea)
                            }
                        }
                    }

                    tarea
                } else {
                    val fecha = partes[4]
                    val ubicacion = partes[5]

                    if (!esFechaValida(fecha)) {
                        println("Error: La fecha del evento no es válida para la actividad con ID $id")
                        return null
                    }

                    Evento.creaInstancia(EventoParams(usuario, id, descripcion, fechaCreacion, fecha, ubicacion))
                }
            } else {
                println("Error: Formato inválido en la actividad serializada: $serializado")
                null
            }
        } catch (e: Exception) {
            println("Error al deserializar actividad: ${e.message}")
            null
        }
    }

    fun leerArchivo(ruta: String): List<String> {
        return try {
            val archivo = File(ruta)
            if (archivo.exists()) {
                archivo.readLines().filter { it.isNotBlank() && it != "Subtareas:" && it != "Sin subtareas" }
            } else {
                println("El archivo no existe: $ruta")
                emptyList()
            }
        } catch (e: IOException) {
            println("Error al leer el archivo: ${e.message}")
            emptyList()
        }
    }

    fun actualizarTareaEnFichero(tarea: Tarea) {
        val archivo = File(RepoActividades.RUTA_FICHERO_ACTIVIDADES)
        if (!archivo.exists()) {
            archivo.createNewFile()
        }

        // Leer todas las líneas del archivo
        val lineas = archivo.readLines().toMutableList()
        val nuevaLista = mutableListOf<String>()
        var tareaEncontrada = false

        for (linea in lineas) {
            // Si encontramos la tarea, la reemplazamos con su nueva versión
            if (linea.startsWith("${tarea.obtenerUsuario()};${tarea.getIdActividad()};")) {
                // Escribir la tarea principal
                nuevaLista.add(tarea.obtenerDetalle())

                // Añadir también las subtareas, si existen
                if (tarea.subTareas.isNotEmpty()) {
                    tarea.subTareas.forEach { subTarea ->
                        nuevaLista.add("    - ${subTarea.obtenerDetalle()}")
                    }
                }

                tareaEncontrada = true
            } else {
                nuevaLista.add(linea)
            }
        }

        // Si la tarea no estaba en el archivo, la añadimos al final
        if (!tareaEncontrada) {
            nuevaLista.add(tarea.obtenerDetalle())
            if (tarea.subTareas.isNotEmpty()) {
                tarea.subTareas.forEach { subTarea ->
                    nuevaLista.add("    - ${subTarea.obtenerDetalle()}")
                }
            }
        }

        // Escribir el contenido actualizado en el archivo
        archivo.writeText(nuevaLista.joinToString("\n"))
    }

    fun aniadirActividad(ruta: String, actividad: Actividad) {
        try {
            val archivo = File(ruta)
            val detalle = actividad.obtenerDetalle()
            if (detalle.isNotBlank()) {
                archivo.appendText("$detalle\n")
                println("Actividad añadida al archivo: $detalle")
            } else {
                println("Error: La actividad no tiene detalles válidos para guardar.")
            }

            // Si la actividad es una tarea con subtareas, guardar también las subtareas
            if (actividad is Tarea && actividad.subTareas.isNotEmpty()) {
                actividad.subTareas.forEach { subTarea ->
                    archivo.appendText("    - ${subTarea.obtenerDetalle()}\n")
                }
            }
        } catch (e: Exception) {
            println("Error al añadir actividad: ${e.message}")
        }
    }

    fun aniadirUsuario(ruta: String, usuario: Usuario) {
        try {
            val archivo = File(ruta)
            archivo.appendText("${usuario.nombre}\n")
            println("Usuario añadido al archivo: ${usuario.nombre}")
        } catch (e: Exception) {
            println("Error al añadir usuario: ${e.message}")
        }
    }
}