package dominio

/**
 * Clase evento con constructor privado
 * @param descripcion-> String de la descripcion heredada de actividad
 * @param ubicacion-> String que contendrá una ubicación puesta por el usuario
 */

class Evento private constructor(
    descripcion: String,
    usuario:String,
    private val ubicacion: String,
    private val fechaEvento: String
):Actividad(descripcion,usuario) {
    init{
        require(Utils.esFechaValida(fechaEvento)){"La fecha tiene que tener un formato válida (dd-MM-yyyy)"}
    }


    private constructor(params: EventoParams) : this(
        params.descripcion,
        params.usuario,
        params.ubicacion,
        params.fecha
    ) {
        this.id = params.id
        this.fechaCreacion = params.fechaCreacion
    }

    override var fecha = fechaEvento

    /**
     * Función obtener detalle
     * Sobreescribe la de Actividad y le añade la fecha y ubicación
     * @return String
     */

    override var id = "${CalculoId.generarId(fechaEvento)}"

    override fun obtenerDetalle(): String {
        return super.obtenerDetalle() + ";$fechaEvento;$ubicacion"
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
        fun creaInstancia(descripcion:String,usuario:String,ubicacion:String,fecha:String):Evento{
            return Evento(descripcion,usuario,ubicacion,fecha)
        }

        fun creaInstancia(parametros:EventoParams
        ):Evento
        {
            return Evento(EventoParams(parametros.usuario,parametros.id,parametros.descripcion,parametros.fechaCreacion,parametros.fecha,parametros.ubicacion))
        }
    }
}