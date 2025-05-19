package servicios

import accesodatos.RepoUsuarios
import presentacion.ConsolaUI

class UsuariosService(
    consola: ConsolaUI
) {
    val usuariosRepo = RepoUsuarios()

}