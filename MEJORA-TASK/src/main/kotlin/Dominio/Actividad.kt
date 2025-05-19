package dominio

import Utils

abstract class Actividad(
    private val descripcion: String,
    private val usuario: String
) {
    init {
        require(descripcion.isNotBlank()) { "¡La descripción no puede estar vacía!" }
    }


    protected open var fechaCreacion: String = Utils.obtenerFechaActual()
    open var fecha = fechaCreacion
    protected open var id: String = "${CalculoId.generarId(fechaCreacion)}"

    open fun obtenerDesc() = descripcion
    open fun getIdActividad(): String = id
    open fun obtenerUsuario(): String = usuario
    open fun obtenerDetalle(): String = "$usuario;$id;$descripcion;$fechaCreacion"
}