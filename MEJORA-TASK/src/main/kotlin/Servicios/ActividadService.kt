package Servicios

import AccesoDatos.RepoActividades
import Dominio.EstadoTarea
import Dominio.Tarea
import Presentacion.ConsolaUI

/**
 * Clase abierta que servirá para la gestion de programas
 * Hereda los metodos de ConsolaUI y la propiedad actividades de IActividadRepository
 */
class ActividadService {
    val consola = ConsolaUI()
    val repo = RepoActividades()
    /**
     * Funcion que gestionara el programa
     * Se hara un bucle donde mientras la opcion no sea 0, seguirá el programa
     * Se muestra menu
     * Se pide opcion
     * Se gestiona esa opcion
     */
    fun gestionarPrograma(){
        do{
            var opcion = -1
            consola.mostrarMenu()
            opcion = consola.pedirOpcion("Elige una opción",0,3)
            gestionarOpcion(opcion)
        }while(opcion != 0)
    }

    /**
     * Funcion privada que gestionara la opcion introducida
     * Si es 0 sale
     * Si es 1 aniade activiadad a la lista
     * Si es 2 lista las actividades
     * @param opcion-> Opcion introducida
     */
    private fun gestionarOpcion(opcion:Int){
        when(opcion){
            0-> return
            1-> anadirActividad()
            2-> listarActividades()
            3-> cambiarEstado(pedirIdTarea())
        }
    }

    /**
     * Funcion privada anadirActividad
     * Aniadira una actividad a la lista de actividades
     * Se pedira una opcion, la 1 es una tarea y la 2 un evento
     * Cuando la opcion es 0, sale
     * De lo contrario, crea una actividad con el metodo de la clase padre
     * Si la actividad es un null, entonces es porque se ha puesto un dato mal y no se aniade
     *
     */

    private fun pedirIdTarea():Tarea{
        var tareaPedida:Tarea? = null
        do {
            try {
                println("Introduzca el id de la tarea >> ")
                val id = readln().toInt()
                var existe = false

                for (tarea in repo.tareas) {
                    if (tarea.getIdActividad() == id) {
                        existe = true
                        tareaPedida = tarea
                    }
                }
                if (!existe) {
                    throw Exception("Id introducido no existente, siga intentando")
                }
            } catch (e: Exception) {
                println("¡Error! Detalle: $e")
            }
        }while(tareaPedida == null)
        return tareaPedida
    }

    private fun cambiarEstado(tarea:Tarea){
        var estadoValido = false
        do {
            val estadoNuevo = consola.pedirInfo("CAMBIE EL ESTADO DE LA TAREA: ABIERTA,EN_PROGRESO,FINALIZADA")

            if (EstadoTarea.getEstado(estadoNuevo) == null) {
                println("¡Error! Estado no válido, vuelve a introducir")
            }
            else{
                tarea.estado = EstadoTarea.getEstado(estadoNuevo)!! //ya está controlado
                if(tarea.estado.toString().uppercase() == "CERRADA"){
                    repo.actividades.remove(tarea)
                    repo.tareas.remove(tarea)
                }

                estadoValido = true
            }

        }while(!estadoValido)
    }

    private fun anadirActividad(){
        val opcion = consola.pedirOpcion("¿Que quieres crear?\n0)CANCELAR\n1)Tarea\n2)Evento",0,2)

        if(opcion == 0){
            return
        }

        val actividad = consola.crearActividad(opcion,repo)

        if(actividad != null){
            repo.actividades.add(actividad)
        }
        Thread.sleep(2000)
    }

    /**
     * Funcion privada que listara actividades
     * Lista todas las actividades de la lista de esta clase con
     * el metodo listarActividades de la clase padre.
     * Hace una pausa luego de eso para poder mirar bien.
     */
    private fun listarActividades(){
        consola.listarActividades(repo.actividades)
        Thread.sleep(5000)
    }
    companion object{
        val service = ActividadService()
        fun iniciarPrograma(){
            service.gestionarPrograma()
        }
    }
}