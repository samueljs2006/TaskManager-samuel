package Dominio

import AccesoDatos.RepoActividades

class Usuario private constructor(
    val nombre: String
){
    val repoActividades = RepoActividades()

    companion object{
        fun creaInstancia(nombre:String):Usuario{
            return Usuario(nombre)
        }
    }
}