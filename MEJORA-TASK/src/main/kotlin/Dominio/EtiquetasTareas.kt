package dominio

enum class EtiquetasTareas(desc:String) {
    URGENTE("Urgente"),
    DOCUMENTACION("Documentación"),
    REVISION("Revisión"),
    SENCILLA("Sencilla");

    companion object {
        fun getEtiqueta(eti:String): EtiquetasTareas?{
            return when(eti.uppercase()){
                URGENTE.toString().uppercase()-> return URGENTE
                DOCUMENTACION.toString().uppercase()-> return DOCUMENTACION
                REVISION.toString().uppercase()-> return REVISION
                SENCILLA.toString().uppercase()-> return SENCILLA
                else-> null
            }
        }
    }
}