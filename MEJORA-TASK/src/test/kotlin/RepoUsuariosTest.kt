
import accesodatos.RepoUsuarios
import dominio.Usuario
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RepoUsuariosTest {
    private lateinit var repo: RepoUsuarios

    @BeforeEach
    fun setUp() {
        repo = RepoUsuarios(mutableSetOf())
    }

    @Test
    fun testAgregarUsuario() {
        val usuario = Usuario.creaInstancia("Luismi")
        repo.agregarUsuario(usuario)
        assertTrue(repo.usuarios.contains(usuario))
    }

    @Test
    fun testCargarUsuarios() {
        assertNotNull(repo.usuarios)
    }
}