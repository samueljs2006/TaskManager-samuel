package Presentacion
import AccesoDatos.RepoActividades
import Dominio.Actividad

interface Consola {
    fun mostrarMenu()
    fun crearActividad(opcion:Int,repo: RepoActividades):Actividad?
    fun listarActividades(actividades: MutableList<Actividad>)
    fun pedirInfo(msj:String):String
    fun pedirOpcion(msj:String,min:Int,max:Int):Int
}