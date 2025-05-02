package Presentacion
import AccesoDatos.RepoActividades
import AccesoDatos.RepoUsuarios
import Dominio.Actividad

interface Consola {
    fun mostrarMenu()
    fun crearActividad(opcion:Int,repo: RepoActividades,repoUser: RepoUsuarios):Actividad?
    fun listarActividades(actividades: MutableList<Actividad>)
    fun pedirInfo(msj:String):String
    fun pedirOpcion(msj:String,min:Int,max:Int):Int
    fun mostrarMensaje(s: String)
}