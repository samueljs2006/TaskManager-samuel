package accesodatos
import dominio.Usuario
import java.nio.file.Paths

class RepoUsuarios(
    val usuarios: MutableSet<Usuario> = mutableSetOf<Usuario>()
) {

    init{
        cargarUsuarios()
    }

    fun agregarUsuario(usuario:Usuario){
        usuarios.add(usuario)
        Utils.aniadirUsuario(rutaFicheroUsuario,usuario)
    }

    private fun cargarUsuarios(){
        val usuariosFichero = Utils.leerArchivo(rutaFicheroUsuario)
        for(usuario in usuariosFichero){
            usuarios.add(Utils.deserializarUsuario(usuario))
        }
    }

    companion object{
        val rutaFicheroUsuario_relativa = "MEJORA-TASK/src/main/kotlin/Datos/Usuarios.txt"
        val rutaFicheroUsuario = Paths.get(rutaFicheroUsuario_relativa).toAbsolutePath().toString()
    }
}