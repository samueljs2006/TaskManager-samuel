package Servicios

import AccesoDatos.RepoActividades
import AccesoDatos.RepoUsuarios
import Dominio.EstadoTarea
import Dominio.Evento
import Dominio.Tarea
import Dominio.Usuario
import Presentacion.ConsolaUI

/**
 * Clase abierta que servirá para la gestion de programas
 * Hereda los metodos de ConsolaUI y la propiedad actividades de IActividadRepository
 */
class ActividadService {
    val consola = ConsolaUI()
    val repo = RepoActividades()
    val servicioUsuario = UsuariosService(consola)

    /**
     * Funcion que gestionara el programa
     * Se hara un bucle donde mientras la opcion no sea 0, seguirá el programa
     * Se muestra menu
     * Se pide opcion
     * Se gestiona esa opcion
     */

    fun usuariosConActividades(){
        for(usuario in servicioUsuario.usuariosRepo.usuarios){
            for(actividad in repo.actividades){
                if(actividad.obtenerUsuario() == usuario.nombre){
                    usuario.repoActividades.actividades.add(actividad)
                    when(actividad){
                        is Tarea-> usuario.repoActividades.tareas.add(actividad)
                        is Evento-> usuario.repoActividades.eventos.add(actividad)
                    }
                }
            }
        }
    }

    fun gestionarPrograma(){
        usuariosConActividades()
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
     * Si es 1 aniade actividad a la lista
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
                    println("¡Tarea cerrada con éxito!")
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

        val actividad = consola.crearActividad(opcion,repo,servicioUsuario.usuariosRepo)

        if(actividad != null){
            repo.aniadirActividad(actividad)
        }
        Thread.sleep(2000)
    }

    /**
     * Funcion privada que listara actividades
     * Lista todas las actividades de la lista de esta clase con
     * el metodo listarActividades de la clase padre.
     * Hace una pausa luego de eso para poder mirar bien.
     */

    private fun listarTodas(){
        consola.listarActividades(repo.actividades)
        Thread.sleep(5000)
    }

    private fun comprobarUsuario(usuarioIntroducido:String):Boolean{
        var existe = false

        for(usuario in servicioUsuario.usuariosRepo.usuarios){
            if(usuario.nombre == usuarioIntroducido){
                existe = true
            }
        }
        return existe
    }

    private fun pedirUsuario():Usuario?{
        var seguir = true
        var usuario: Usuario? = null

        do {
            println("ENTER VACÍO -> LISTAR ANÓNIMAS")
            println("0 PARA SALIR")
            var nombreUsuario = consola.pedirInfo("Introduzca el nombre de usuario >> ").trim()

            if(nombreUsuario.trim().isEmpty()){
                nombreUsuario = "Anónimo"
            }

            val existe = comprobarUsuario(nombreUsuario)

            if(!existe){
                println("El usuario no existe...")
                seguir = consola.preguntarSeguir()
            }
            else{
                println("¡Usuario $nombreUsuario encontrado!")
                usuario = servicioUsuario.usuariosRepo.usuarios.find { it.nombre == nombreUsuario }
                seguir = false
            }
        }while(seguir)
        return usuario
    }

    private fun listarPorUsuario(){
        val usuario = pedirUsuario()

        if(usuario != null){
            consola.listarActividades(usuario.repoActividades.actividades)
        }

        else{
            return
        }
    }

    private fun listarActividades(){
        var opcion = -1

        do {
            println("LISTADO DE ACTIVIDADES")
            println("1) Listar todas las actividades")
            println("2) Listar actividades por usuario")
            opcion = consola.pedirOpcion("",1,2)

            when(opcion){
                1-> listarTodas()
                2-> listarPorUsuario()
            }

        }while(opcion!= 0)
    }

    companion object{
        val service = ActividadService()

        fun iniciarPrograma(){
            service.gestionarPrograma()
        }
    }
}