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

    fun deserializarActividad(serializado: String): Actividad? {
        val partes = serializado.split(";")
        return when (partes.size) {
            3 -> Tarea.creaInstancia(partes[0], partes[1]).apply {
                estado = EstadoTarea.getEstado(partes[2])!!
            }
            4 -> Evento.creaInstancia(partes[0], partes[1], partes[2], partes[3])
            else -> null
        }
    }

    fun leerArchivo(ruta: String): List<String> {
        return try {
            val archivo = File(ruta)
            if (archivo.exists()) archivo.readLines() else emptyList()
        } catch (e: Exception) {
            println("Error al leer el archivo: ${e.message}")
            emptyList()
        }
    }

    fun aniadirActividad(ruta: String, actividad: Actividad) {
        try {
            val archivo = File(ruta)
            archivo.appendText("${actividad.obtenerDetalle()}\n")
        } catch (e: Exception) {
            println("Error al añadir actividad: ${e.message}")
        }
    }

    fun aniadirUsuario(ruta: String, usuario: Usuario) {
        try {
            val archivo = File(ruta)
            archivo.appendText("${usuario.nombre}\n")
        } catch (e: Exception) {
            println("Error al añadir usuario: ${e.message}")
        }
    }
}