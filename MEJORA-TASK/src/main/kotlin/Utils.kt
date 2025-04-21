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

    fun esEtiqueta(etiqueta:String):Boolean{
        return EtiquetasTareas.getEtiqueta(etiqueta) != null
    }

    fun deserializarActividad(serializado: String): Actividad?{
        val partes = serializado.split(";")
        when(esEtiqueta(partes.last())){
            true->{
                val usuario = partes[0]
                val id = partes[1]
                val descripcion = partes[2]
                val fechaCreacion = partes[3]
                val estado = partes[4]
                val etiqueta = partes[5]
                return Tarea.creaInstancia(usuario,id, EtiquetasTareas.getEtiqueta(etiqueta)!!,fechaCreacion,descripcion,estado,)
            } //la etiqueta no será nula nunca en este caso.

            false->{
                val usuario = partes[0]
                val id = partes[1]
                val descripcion = partes[2]
                val fechaCreacion = partes[3]
                val fecha = partes[4]
                val ubicacion = partes[5]
                return Evento.creaInstancia(usuario,id,descripcion,fechaCreacion,fecha,ubicacion)
            }
        }
        return null

    }

    fun leerArchivo(ruta: String): List<String> {
        return try {
            val archivo = File(ruta)
            if (archivo.exists()) {
                val lineas = archivo.readLines()
                lineas
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