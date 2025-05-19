
import dominio.EstadoTarea
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EstadoTareaTest {
    @Test
    fun testGetEstadoVariantes() {
        assertEquals(EstadoTarea.EN_PROGRESO, EstadoTarea.getEstado("EN PROGRESO"))
        assertEquals(EstadoTarea.EN_PROGRESO, EstadoTarea.getEstado("EN_PROGRESO"))
        assertEquals(EstadoTarea.ABIERTA, EstadoTarea.getEstado("ABIERTA"))
        assertEquals(EstadoTarea.FINALIZADA, EstadoTarea.getEstado("FINALIZADA"))
        assertNull(EstadoTarea.getEstado("NO_EXISTE"))
    }
}