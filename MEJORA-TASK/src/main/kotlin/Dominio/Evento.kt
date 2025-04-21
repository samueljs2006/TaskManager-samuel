package Dominio

/**
 * Clase evento con constructor privado
 * @param descripcion-> String de la descripcion heredada de actividad
 * @param ubicacion-> String que contendrá una ubicación puesta por el usuario
 */

class Evento private constructor(
    descripcion: String,
    private val ubicacion: String,
    private val fecha: String
):Actividad(descripcion) {
    init{
        require(Utils.esFechaValida(fecha)){"La fecha tiene que tener un formato válida (dd-MM-yyyy)"}
    }
    /**
     * Función obtener detalle
     * Sobreescribe la de Actividad y le añade la fecha y ubicación
     * @return String
     */
    override var fechaCreacion = fecha

    override fun obtenerDetalle(): String {
        return super.obtenerDetalle() + "[Fecha: <$fecha>, Ubicación: <$ubicacion> Fecha de creación: $fecha]"
    }

    init{
        require(!ubicacion.isEmpty()){"¡La ubicación no puede estar vacía!"}
    }

    companion object{
        /**
         * Función crear instancia
         * que creará una instancia de la clase evento.
         * Se encuentra en companion object ya que la clase tiene
         * un constructor privado y no puedes instanciarla de otra
         * manera
         * @return Devuelve un objeto instanciado de la clase evento
         */
        fun creaInstancia(descripcion:String,ubicacion:String,fecha:String):Evento{
            return Evento(descripcion,ubicacion,fecha)
        }
    }
}