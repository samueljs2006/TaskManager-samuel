package dominio

/**
 * Representa una tarea que puede tener subtareas y un estado asociado.
 *
 * Hereda de [Actividad] y añade funcionalidades específicas para la gestión de tareas,
 * como el manejo de subtareas, etiquetas y estados.
 *
 * @property etiqueta Etiqueta asociada a la tarea.
 * @property estado Estado actual de la tarea.
 * @property subTareas Lista de subtareas asociadas a esta tarea.
 */
class Tarea private constructor(
    descripcion: String,
    usuario: String,
    val etiqueta: EtiquetasTareas
) : Actividad(descripcion, usuario) {

    init {
        contador += 1
    }

    /**
     * Estado actual de la tarea.
     */
    var estado = EstadoTarea.ABIERTA

    /**
     * Lista de subtareas asociadas a esta tarea.
     */
    val subTareas: MutableList<Tarea> = mutableListOf()

    /**
     * Constructor secundario para crear una tarea con todos los datos.
     *
     * @param usuario Usuario creador de la tarea.
     * @param id Identificador de la tarea.
     * @param etiqueta Etiqueta asociada.
     * @param fechaCreacion Fecha de creación.
     * @param descripcion Descripción de la tarea.
     * @param estado Estado de la tarea en formato String.
     */
    private constructor(
        usuario: String,
        id: String,
        etiqueta: EtiquetasTareas,
        fechaCreacion: String,
        descripcion: String,
        estado: String
    ) : this(usuario, descripcion, etiqueta) {
        this.id = id + contador
        this.fechaCreacion = fechaCreacion
        this.estado = EstadoTarea.getEstado(estado)!!
    }

    /**
     * Devuelve el detalle completo de la tarea, incluyendo subtareas.
     *
     * @return Cadena con la información detallada de la tarea.
     */
    override fun obtenerDetalle(): String {
        val subTareasDetalle = if (subTareas.isEmpty()) {
            "Sin subtareas"
        } else {
            subTareas.joinToString(separator = "\n") { "    - ${it.obtenerDetalle()}" }
        }
        return super.obtenerDetalle() + ";$estado;$etiqueta;\nSubtareas:\n$subTareasDetalle"
    }

    /**
     * Actualiza el estado de la tarea y de todas sus subtareas.
     *
     * @param estado Nuevo estado a asignar.
     */
    fun actualizarEstado(estado: EstadoTarea) {
        this.estado = estado
        for (tarea in subTareas) {
            tarea.estado = estado
        }
    }

    /**
     * Agrega una subtarea a la lista, validando que no existan duplicados ni subtareas anidadas.
     *
     * @param subTarea Subtarea a agregar.
     * @throws IllegalArgumentException Si la subtarea ya tiene sus propias subtareas.
     */
    fun agregarSubTarea(subTarea: Tarea) {
        if (!subTareas.contains(subTarea)) {
            if (subTarea.subTareas.isNotEmpty()) {
                throw IllegalArgumentException("Una subtarea no puede tener más subtareas.")
            }
            subTareas.add(subTarea)
        } else {
            println("La subtarea ya existe, no se añadirá de nuevo.")
        }
    }

    companion object {
        /**
         * Contador global de tareas creadas.
         */
        var contador = 0

        /**
         * Crea una nueva instancia de [Tarea] con los parámetros básicos.
         *
         * @param descripcion Descripción de la tarea.
         * @param usuario Usuario creador.
         * @param etiqueta Etiqueta asociada.
         * @return Nueva instancia de [Tarea].
         */
        fun creaInstancia(descripcion: String, usuario: String, etiqueta: EtiquetasTareas): Tarea {
            return Tarea(descripcion, usuario, etiqueta)
        }

        /**
         * Crea una nueva instancia de [Tarea] con todos los datos.
         *
         * @param usuario Usuario creador.
         * @param id Identificador de la tarea.
         * @param etiqueta Etiqueta asociada.
         * @param fechaCreacion Fecha de creación.
         * @param descripcion Descripción de la tarea.
         * @param estado Estado de la tarea en formato String.
         * @return Nueva instancia de [Tarea].
         */
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