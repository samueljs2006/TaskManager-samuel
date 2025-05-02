import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import Dominio.*

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
        val partes = serializado.split(";")
        return try {
            // Verificar que la línea no sea vacía o inválida
            if (serializado.isBlank()) {
                println("Línea ignorada: Está vacía.")
                return null
            }

            if (partes.size >= 6) { // Asegurarse de que hay suficientes partes
                val usuario = partes[0]
                val id = partes[1]
                val descripcion = partes[2]
                val fechaCreacion = partes[3]

                // Validar la fecha de creación
                if (!esFechaValida(fechaCreacion)) {
                    println("Error: La fecha de creación no es válida para la actividad con ID $id")
                    return null
                }

                if (esEtiqueta(partes[5])) {
                    // Caso: Tarea
                    val estado = partes[4]
                    val etiqueta = partes[5]
                    val subTareaSerializada = partes.getOrNull(6)
                        ?.removePrefix("Subtarea:[")
                        ?.removeSuffix("]")

                    val tarea = Tarea.creaInstancia(
                        usuario,
                        id,
                        EtiquetasTareas.getEtiqueta(etiqueta)!!,
                        fechaCreacion,
                        descripcion,
                        estado
                    )

                    tarea
                } else {
                    // Caso: Evento
                    val fecha = partes[4]
                    val ubicacion = partes[5]

                    // Validar la fecha del evento
                    if (!esFechaValida(fecha)) {
                        println("Error: La fecha del evento no es válida para la actividad con ID $id")
                        return null
                    }

                    Evento.creaInstancia(usuario, id, descripcion, fechaCreacion, fecha, ubicacion)
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
                archivo.readLines()
            } else {
                println("El archivo no existe: $ruta")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error al leer el archivo: ${e.message}")
            emptyList()
        }
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