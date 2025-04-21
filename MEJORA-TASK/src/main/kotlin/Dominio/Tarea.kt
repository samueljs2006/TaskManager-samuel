package Dominio

import Dominio.Evento

/**
 * Clase tarea con constructor privado
 * @param descripcion-> String con la descripción de a tarea heredada de actividad
 */
class Tarea private constructor(
    descripcion:String,
    usuario:String
): Actividad(descripcion,usuario) {
    /*El estado se inicializará como tarea abierta*/
    var estado = EstadoTarea.ABIERTA

    private constructor(
        usuario: String,
        id:String,
        fechaCreacion: String,
        descripcion: String,
        estado:String

    ) : this(usuario,descripcion) {
        this.id = id
        this.fechaCreacion = fechaCreacion
        this.estado = EstadoTarea.getEstado(estado)!!
    }

    /**
     * Función obtener detalle
     * Sobreescribe la de Actividad y le añade el estado de esta clase
     * @return String
     */
    override fun obtenerDetalle():String{
        return super.obtenerDetalle() + ";$estado"
    }

    companion object{
        /**
         * Función crear instancia
         * que creará una instancia de la clase tarea.
         * Se encuentra en companion object ya que la clase tiene
         * un constructor privado y no puedes instanciarla de otra
         * manera
         * @return Devuelve un objeto instanciado de la clase tarea.
         */
        fun creaInstancia(descripcion:String,usuario:String):Tarea{
            return Tarea(descripcion,usuario)
        }

        fun creaInstancia(
            usuario: String,
            id:String,
            fechaCreacion:String,
            descripcion: String,
            estado:String
        ):Tarea{
            return Tarea(usuario,id,fechaCreacion,descripcion,estado)
        }
    }

}