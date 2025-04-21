package Dominio

abstract class Actividad(
    private val descripcion:String
) {
    /*Al inicializar la clase se comprobará que la descripción no está vacía*/
    init{
        require(!descripcion.isEmpty()){"¡La descripción no puede estar vacía!"}
    }

    /*Se utiliza la función obtener fecha actual del paquete utils*/
    open var fechaCreacion = Utils.obtenerFechaActual()

    /*Se utiliza metodo estatico de generar ID*/
    protected var id = CalculoId.generarId(fechaCreacion)

    open fun getIdActividad():Int{
        return id
    }

    /**
     * Función obtener detalle
     * Es open para poder usarlo fuera de las clases
     * @return String que contendrá el id y la descripción
     */
    open fun obtenerDetalle():String{
        return "<$id> - <$descripcion>"
    }
}