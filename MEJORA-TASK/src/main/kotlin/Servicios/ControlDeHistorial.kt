package servicios

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime

open class ControlDeHistorial {

    private val logger: Logger = LoggerFactory.getLogger(ControlDeHistorial::class.java)

    fun agregarHistorial(msj: String) {
        val log = "${LocalDateTime.now()} -> $msj"
        logger.info(log) // Registrar en el logger
        try {
            File(RUTA_HISTORIAL).appendText("$log\n")
        } catch (e: Exception) {
            logger.error("Error al escribir en el historial: ${e.message}", e)
        }
    }

    companion object {
        const val RUTA_HISTORIAL = "MEJORA-TASK/src/main/kotlin/Datos/Historial.txt"
    }
}