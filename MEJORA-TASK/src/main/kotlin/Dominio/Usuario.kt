package dominio

import accesodatos.RepoActividades

class Usuario private constructor(
    val nombre: String = "Anónimo"
){
    val repoActividades = RepoActividades()

    companion object{
        fun creaInstancia(nombre:String):Usuario{
            return Usuario(nombre)
        }
    }
}