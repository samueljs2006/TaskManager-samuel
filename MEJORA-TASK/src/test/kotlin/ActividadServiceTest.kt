import AccesoDatos.RepoActividades
import Dominio.EstadoTarea
import Dominio.EtiquetasTareas
import Dominio.Tarea
import Presentacion.ConsolaUI
import Servicios.ActividadService
import Servicios.ControlDeHistorial
import Servicios.UsuariosService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class ActividadServiceTest : DescribeSpec({

    val mockConsola = mockk<ConsolaUI>(relaxed = true)
    val mockRepo = mockk<RepoActividades>(relaxed = true)
    val mockHistorial = mockk<ControlDeHistorial>(relaxed = true)
    val mockUsuariosService = mockk<UsuariosService>(relaxed = true)
    val actividadService = ActividadService(mockConsola, mockRepo, mockUsuariosService, mockHistorial)

    beforeEach {
        clearMocks(mockConsola, mockRepo, mockHistorial, mockUsuariosService)
    }

    describe("agregarSubtarea") {
        it("debería agregar una subtarea exitosamente") {
            val tareaMadre = mockk<Tarea>(relaxed = true)
            every { mockRepo.tareas } returns mutableListOf(tareaMadre)
            every { mockConsola.pedirInfo(any()) } returnsMany listOf("1", "Subtarea de prueba", "Usuario")
            every { mockConsola.pedirEtiqueta() } returns EtiquetasTareas.URGENTE
            every { tareaMadre.getIdActividad() } returns "1"

            actividadService.agregarSubtarea()

            verify { tareaMadre.agregarSubTarea(any()) }
            verify { mockHistorial.agregarHistorial("Subtarea agregada a la tarea 1") }
        }

        it("debería fallar si la tarea madre no existe") {
            every { mockRepo.tareas } returns mutableListOf()
            every { mockConsola.pedirInfo(any()) } returns "1"

            actividadService.agregarSubtarea()

            verify(exactly = 0) { mockHistorial.agregarHistorial(any()) }
        }
    }

    describe("cambiarEstado") {
        it("debería cambiar el estado de una tarea exitosamente") {
            val tarea = mockk<Tarea>(relaxed = true)
            every { mockConsola.pedirInfo(any()) } returns "EN_PROGRESO"

            actividadService.cambiarEstado(tarea)

            verify { tarea.actualizarEstado(EstadoTarea.EN_PROGRESO) }
            verify { mockHistorial.agregarHistorial("Estado de la tarea cambiado a EN_PROGRESO") }
        }

        it("debería fallar si el estado es inválido") {
            val tarea = mockk<Tarea>(relaxed = true)
            every { mockConsola.pedirInfo(any()) } returns "INVALIDO"

            actividadService.cambiarEstado(tarea)

            verify(exactly = 0) { tarea.actualizarEstado(any()) }
            verify(exactly = 0) { mockHistorial.agregarHistorial(any()) }
        }
    }

    describe("filtrarPorEstado") {
        it("debería filtrar tareas con estado ABIERTA y salir del bucle") {
            val tareaAbierta = mockk<Tarea>(relaxed = true)
            every { tareaAbierta.estado } returns EstadoTarea.ABIERTA
            every { mockRepo.tareas } returns mutableListOf(tareaAbierta)
            every { mockConsola.pedirOpcion(any(), any(), any()) } returnsMany listOf(1, 0)

            actividadService.filtrarPorEstado()

            verify { mockConsola.listarTareas(mutableListOf(tareaAbierta)) }
        }

        it("debería mostrar mensaje si no hay tareas con el estado solicitado y salir del bucle") {
            every { mockRepo.tareas } returns mutableListOf()
            every { mockConsola.pedirOpcion(any(), any(), any()) } returnsMany listOf(1, 0)

            actividadService.filtrarPorEstado()

            verify { mockConsola.mostrarMensaje("No hay tareas con el estado solicitado.") }
        }
    }

    describe("anadirActividad") {
        it("debería añadir una actividad exitosamente") {
            val tarea = mockk<Tarea>(relaxed = true)
            every { mockConsola.pedirOpcion(any(), any(), any()) } returns 1
            every { mockConsola.crearActividad(any(), any(), any()) } returns tarea

            actividadService.anadirActividad()

            verify { mockRepo.aniadirActividad(tarea) }
            verify { mockHistorial.agregarHistorial("Actividad agregada con éxito") }
        }

        it("debería fallar si los datos de la actividad son inválidos") {
            every { mockConsola.pedirOpcion(any(), any(), any()) } returns 1
            every { mockConsola.crearActividad(any(), any(), any()) } returns null

            actividadService.anadirActividad()

            verify(exactly = 0) { mockRepo.aniadirActividad(any()) }
            verify { mockHistorial.agregarHistorial("Error al crear actividad") }
        }
    }
})