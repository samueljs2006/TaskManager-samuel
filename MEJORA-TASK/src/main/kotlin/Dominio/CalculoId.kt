package dominio

import java.io.File

/**
 * Clase abstracta para el cálculo y gestión de identificadores únicos basados en fechas.
 *
 * Su objetivo es generar un identificador incremental único para cada fecha dada,
 * almacenando la relación fecha-id en memoria y en un fichero persistente.
 *
 * El identificador se utiliza, por ejemplo, para asignar IDs únicos a eventos o tareas
 * creados en el sistema en una fecha concreta.
 */
abstract class CalculoId {
    companion object {
        /**
         * Lista de fechas únicas registradas.
         */
        private val fechasUnicas = mutableListOf<String>()

        /**
         * Mapa que asocia cada fecha con su identificador incremental.
         */
        private val mapaIdsFechas: MutableMap<String, Int> = mutableMapOf()

        /**
         * Ruta del fichero donde se almacenan las fechas y sus IDs.
         */
        const val RUTA_FICHERO_FECHAS = "MEJORA-TASK/src/main/kotlin/Datos/FechasId.txt"

        init {
            // Carga las fechas e IDs existentes desde el fichero al iniciar la clase.
            cargarFechasDesdeFichero()
        }

        /**
         * Genera un identificador único para una fecha dada.
         *
         * Si la fecha es nueva, se le asigna un nuevo ID incremental y se guarda en el fichero.
         * Si la fecha ya existe, devuelve el ID previamente asignado.
         *
         * @param fecha Fecha en formato `dd-MM-yyyy`.
         * @return Identificador único asociado a la fecha.
         * @throws IllegalArgumentException Si la fecha es nula o vacía.
         */
        fun generarId(fecha: String?): Int {
            require(!fecha.isNullOrBlank()) { "Error: La fecha no puede ser nula o vacía." }

            return if (fecha !in fechasUnicas) {
                fechasUnicas.add(fecha)
                val id = fechasUnicas.size
                File(RUTA_FICHERO_FECHAS).appendText("Fecha:$fecha ID:$id\n")
                mapaIdsFechas[fecha] = id
                id
            } else {
                mapaIdsFechas[fecha] ?: error("Error: La fecha $fecha no tiene un ID asignado.")
            }
        }

        /**
         * Carga las fechas e identificadores desde el fichero persistente.
         *
         * Si el fichero no existe, lo crea vacío.
         * Procesa cada línea con el formato `Fecha:<fecha> ID:<id>`.
         */
        private fun cargarFechasDesdeFichero() {
            val archivo = File(RUTA_FICHERO_FECHAS)
            if (!archivo.exists()) {
                archivo.createNewFile()
                return
            }

            archivo.forEachLine { linea ->
                try {
                    val partes = linea.split(" ")
                    val fecha = partes[0].split(":")[1]
                    val id = partes[1].split(":")[1].toInt()
                    fechasUnicas.add(fecha)
                    mapaIdsFechas[fecha] = id
                } catch (e: Exception) {
                    println("Error al procesar la línea del fichero: $linea. Detalle: ${e.message}")
                }
            }
        }
    }
}