package Dominio

/**
 * Clase tarea con constructor privado
 * @param descripcion-> String con la descripción de a tarea heredada de actividad
 */
class Tarea private constructor(
    descripcion:String
): Actividad(descripcion) {
    /*El estado se inicializará como tarea abierta*/
    var estado = EstadoTarea.ABIERTA

    /**
     * Función obtener detalle
     * Sobreescribe la de Actividad y le añade el estado de esta clase
     * @return String
     */
    override fun obtenerDetalle():String{
        return super.obtenerDetalle() + "[Estado: <$estado>]"
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
        fun creaInstancia(descripcion:String):Tarea{
            return Tarea(descripcion)
        }
    }

}