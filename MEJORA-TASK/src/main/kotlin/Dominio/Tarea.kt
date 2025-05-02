package Dominio

class Tarea private constructor(
    descripcion: String,
    usuario: String,
    val etiqueta: EtiquetasTareas
) : Actividad(descripcion, usuario) {
    var estado = EstadoTarea.ABIERTA

    // Lista de subtareas asociadas
    val subTareas: MutableList<Tarea> = mutableListOf()

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
        val subTareasDetalle = if (subTareas.isEmpty()) {
            "Sin subtareas"
        } else {
            subTareas.joinToString(separator = "\n") { "    - ${it.obtenerDetalle()}" }
        }
        return super.obtenerDetalle() + ";$estado;$etiqueta;\nSubtareas:\n$subTareasDetalle"
    }

    fun actualizarEstado(estado: EstadoTarea) {
        this.estado = estado
        for(tarea in subTareas) {
            tarea.estado = estado
        }
    }

    fun agregarSubTarea(subTarea: Tarea) {
        // Validar que las subtareas no puedan tener más subtareas
        if (subTarea.subTareas.isNotEmpty()) {
            throw IllegalArgumentException("Una subtarea no puede tener más subtareas.")
        }
        subTareas.add(subTarea)
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