package Servicios

import AccesoDatos.RepoUsuarios
import Presentacion.ConsolaUI

class UsuariosService(
    consola: ConsolaUI
) {
    val usuariosRepo = RepoUsuarios()

}