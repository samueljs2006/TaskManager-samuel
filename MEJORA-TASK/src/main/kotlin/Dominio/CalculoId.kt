package dominio

import java.io.File

abstract class CalculoId {
    /**
     * Genera un identificador único basado en:
     * - La fecha convertida a `YYYYMMDD` (año, mes y día).
     * - Un contador incremental que asegura un valor único en caso de múltiples eventos en la misma fecha.
     *
     * El contador se almacena en `mapaIdsFechas`, que mantiene el número de eventos generados para cada día.
     *
     * @param fecha La fecha en formato `dd-MM-yyyy` utilizada para generar el identificador único.
     * @return Un identificador único en formato `N`, donde `N` es el contador del evento en ese día.
     */
    companion object {
        private val fechasUnicas = mutableListOf<String>()
        private val mapaIdsFechas: MutableMap<String, Int> = mutableMapOf()
        const val RUTA_FICHERO_FECHAS = "MEJORA-TASK/src/main/kotlin/Datos/FechasId.txt"

        init {
            // Cargar datos al iniciar la clase
            cargarFechasDesdeFichero()
        }

        fun generarId(fecha: String?): Int {
            // Validar que la fecha no sea nula
            require(!fecha.isNullOrBlank()) { "Error: La fecha no puede ser nula o vacía." }

            // Si la fecha no está registrada, se agrega y asigna un nuevo ID.
            return if (fecha !in fechasUnicas) {
                fechasUnicas.add(fecha)
                val id = fechasUnicas.size
                // Guardar en el fichero y actualizar el mapa
                File(RUTA_FICHERO_FECHAS).appendText("Fecha:$fecha ID:$id\n")
                mapaIdsFechas[fecha] = id
                id
            } else {
                // Recupera el ID existente para la fecha.
                mapaIdsFechas[fecha] ?: error("Error: La fecha $fecha no tiene un ID asignado.")
            }
        }

        private fun cargarFechasDesdeFichero() {
            val archivo = File(RUTA_FICHERO_FECHAS)
            if (!archivo.exists()) {
                // Crear el fichero si no existe
                archivo.createNewFile()
                return
            }

            archivo.forEachLine { linea ->
                try {
                    // Procesar cada línea con el formato "Fecha:<fecha> ID:<id>"
                    val partes = linea.split(" ")
                    val fecha = partes[0].split(":")[1]
                    val id = partes[1].split(":")[1].toInt()

                    // Añadir a las estructuras internas
                    fechasUnicas.add(fecha)
                    mapaIdsFechas[fecha] = id
                } catch (e: Exception) {
                    println("Error al procesar la línea del fichero: $linea. Detalle: ${e.message}")
                }
            }
        }
    }
}