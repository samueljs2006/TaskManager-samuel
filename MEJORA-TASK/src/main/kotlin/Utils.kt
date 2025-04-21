import Dominio.Actividad
import Dominio.EstadoTarea
import Dominio.Evento
import Dominio.Tarea
import Dominio.Usuario
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Utils {

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    /**
     * Obtiene la fecha actual.
     *
     * @return Un `String` con la fecha actual en formato `"dd-MM-yyyy"`.
     */

    fun obtenerFechaActual(): String {
        return LocalDate.now().format(formatter)
    }

    /**
     * Verifica si una fecha proporcionada es válida según el formato "dd-MM-yyyy".
     *
     * @param fecha La fecha a validar en formato `"dd-MM-yyyy"`.
     * @return `true` si la fecha tiene el formato correcto y es válida en el calendario; `false` en caso contrario.
     */

    fun esFechaValida(fecha: String): Boolean {
        return try {
            LocalDate.parse(fecha, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun deserializarUsuario(nombreUsuario:String):Usuario{
        return Usuario.creaInstancia(nombreUsuario)
    }


    fun deserializarActividad(serializado:String):Actividad{
        val partes = serializado.split(";")
        var actividad: Actividad? = null
        do {
            when (partes.size) {

                3 -> {
                    actividad = Tarea.creaInstancia(partes[0], partes[1])
                    actividad.estado = EstadoTarea.getEstado(partes[2])!!
                }

                4 -> {
                    actividad = Evento.creaInstancia(partes[0], partes[1], partes[2], partes[3])
                }
            }
        }while(actividad == null)

        return actividad

    }

    fun leerArchivo(ruta: String): List<String> {
        return try {
            File(ruta).readLines()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun aniadirActividad(ruta: String,actividad: Actividad){
        File(ruta).appendText(actividad.obtenerDetalle()+"\n")
    }

    fun aniadirUsuario(ruta:String,usuario:Usuario){
        File(ruta).appendText(usuario.nombre +"\n")
    }
}