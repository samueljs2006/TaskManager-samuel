package Dominio

class Tarea private constructor(
    descripcion: String,
    usuario: String,
    val etiqueta: EtiquetasTareas
) : Actividad(descripcion, usuario) {
    var estado = EstadoTarea.ABIERTA

    // Una única sub-tarea asociada
    var subTarea: Tarea? = null

    private constructor(
        usuario: String,
        id: String,
        etiqueta: EtiquetasTareas,
        fechaCreacion: String,
        descripcion: String,
        estado: String
    ) : this(usuario, descripcion, etiqueta) {
        this.id = id
        this.fechaCreacion = fechaCreacion
        this.estado = EstadoTarea.getEstado(estado)!!
    }

    override fun obtenerDetalle(): String {
        val subTareaDetalle = subTarea?.obtenerDetalle() ?: "Sin subtarea"
        return super.obtenerDetalle() + ";$estado;$etiqueta;Subtarea:[$subTareaDetalle]"
    }

    fun actualizarEstado(estado: EstadoTarea) {
        this.estado = estado
        subTarea?.estado = estado // Sincroniza el estado con la subtarea
    }

    companion object {
        fun creaInstancia(descripcion: String, usuario: String, etiqueta: EtiquetasTareas): Tarea {
            return Tarea(descripcion, usuario, etiqueta)
        }

        fun creaInstancia(
            usuario: String,
            id: String,
            etiqueta: EtiquetasTareas,
            fechaCreacion: String,
            descripcion: String,
            estado: String
        ): Tarea {
            return Tarea(usuario, id, etiqueta, fechaCreacion, descripcion, estado)
        }
    }
}