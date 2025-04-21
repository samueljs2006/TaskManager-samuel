package Servicios

import java.io.File
import java.time.LocalDateTime

open class ControlDeHistorial() {

    fun agregarHistorial(msj:String){
        File(RUTA_HISTORIAL).appendText(msj + " ${LocalDateTime.now()}" +"\n")
    }

    companion object{
        const val RUTA_HISTORIAL = "src/main/kotlin/Datos/Historial.txt"
    }
}