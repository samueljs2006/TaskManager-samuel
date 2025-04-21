package Presentacion

import AccesoDatos.RepoActividades
import AccesoDatos.RepoUsuarios
import Dominio.Actividad
import Dominio.EtiquetasTareas
import Dominio.Evento
import Dominio.Tarea
import Dominio.Usuario
import Servicios.ActividadService

/**
 * Esta clase hereda los metodos de consola para desarrolarlos.
 * Una vez desarrollado los metodos, estos pasaran a usarse por el servicio heredando esta clase.
 */
class ConsolaUI: Consola {

    override fun mostrarMenu() {
        println("Taskmanager Luis Miguel Gomila Dominguez")
        println("-----------------------------------------")
        println("1) Crear una nueva actividad")
        println("2) Listar todas las actividades")
        println("3) Cambiar estado tarea")
        println("4) Filtrar actividades")
        println("0) SALIR")
        println("-----------------------------------------")
    }

    /**
     * Función que pide información
     * @param msj-> Mensaje personalizado
     * @return Devuelve la información puesta por el usuario
     */

    fun pedirUsuario(repo: RepoUsuarios):Usuario{
        val usuarioNombre = pedirInfo("Introduzca el usuario al que asignar Actividad")
        var encontrado = false
        var usuarioPedido:Usuario? = null

        do {
            for (usuario in repo.usuarios) {
                if (usuario.nombre == usuarioNombre) {
                    println("¡Usuario $usuarioNombre encontrado!")
                    usuarioPedido = usuario
                    encontrado = true
                }
            }

            if (!encontrado) {
                println("Usuario $usuarioNombre no encontrado, creando...")
                repo.agregarUsuario(Usuario.creaInstancia(usuarioNombre))
                usuarioPedido = repo.usuarios.find { it.nombre == usuarioNombre }
            }

        }while(usuarioPedido == null)

        return usuarioPedido
    }
    override fun pedirInfo(msj: String): String {
        println(msj)
        print(">> ")
        return readln()
    }

    /**
     * Función que creará una actividad
     * Si la opción es 1, usará el metodo estatico crea intsancia de la clase Tarea
     * Si la opcion es 2, creara la instancia con el metodo estatico de Evento
     * A ambos se le pasan la funcion de pedirInformacion con un msj personalizado.
     *
     * @param opcion-> Opcion introducida por el usuario
     * @return la actividad creada o nulo en caso de dar error (se controla afuera)
     */

    private fun pedirEtiqueta(): EtiquetasTareas{
        for(etiqueta in EtiquetasTareas.entries){
            println(etiqueta)
        }
        var etiqueta: EtiquetasTareas? = null
        do {
            println("Introduzca una de las etiquetas >> ")
            etiqueta = EtiquetasTareas.getEtiqueta(readln().trim())
            if(etiqueta == null){
                println("ETIQUETA INTRODUCIDA NO VÁLIDA VUELVA A INTENTAR")
            }
        }while(etiqueta == null)

        return etiqueta
    }

    fun listarTareas(tareas: MutableList<Tarea>){
        for(tarea in tareas){
            println(tarea.obtenerDetalle())
        }
    }

    fun listarEventos(eventos: MutableList<Evento>){
        for(evento in eventos){
            println(evento.obtenerDetalle())
        }
    }
    override fun crearActividad(opcion:Int,repo:RepoActividades,repoUser: RepoUsuarios):Actividad? {
        var actividad:Actividad? = null
        try {
            when (opcion) {
                1 -> {
                    actividad = Tarea.creaInstancia(
                        pedirInfo("La descripción de la tarea"),
                        pedirUsuario(repoUser).nombre,
                        pedirEtiqueta()

                    )
                    repo.tareas.add(actividad)
                }

                2 ->{
                    actividad = Evento.creaInstancia(
                        pedirInfo("La descripción del evento"),
                        pedirUsuario(repoUser).nombre,
                        pedirInfo("La ubicación del evento"),
                        pedirInfo("La fecha en la que sucederá el evento")
                    )
                    repo.eventos.add(actividad)
                }

            }
        }catch(e:Exception){
            println("¡Error! Detalle: $e")
            actividad = null
        }
        return actividad
    }

    /**
     * Función pedir opcion
     * Controla que el numero este dentro del rango, mientras no lo este lo pedira.
     *
     * @param msj-> Mensaje personalizado que mostrar
     * @param min-> Numero minimo del rango de opciones
     * @param max-> Numero maximo del rango de opciones
     * @return Devuelve la opcion introducida
     */
    override fun pedirOpcion(msj: String, min: Int, max: Int):Int {
        println(msj)
        var opcion:Int? = null
        do{
            try{
                print(">>")
                opcion = readln().toInt()
                if(opcion <min || opcion >max){
                    throw IllegalArgumentException("¡Número introducido fuera de rango!")
                }
            }catch(e: IllegalArgumentException){
                println("¡Error! Vuelve a introducir. Detalle: $e")
                opcion = null
            }
        }while(opcion == null)
        return opcion
    }

    /**
     * Funcion listarActividades
     * Listara las actividades que haya en la lista usando el metodo de obtener detalles
     *
     * @param actividades-> Lista mutable de las actividades
     */

    fun preguntarSeguir(): Boolean {
        val opciones = mapOf("S" to true, "N" to false)
        var seguir: Boolean? = null

        do {
            println("¿Deseas seguir? S/N >> ")
            val siono = pedirInfo("").trim().uppercase()

            seguir = opciones[siono]

            if (seguir == null) {
                println("RESPUESTA DADA DE FORMA INCORRECTA, VUELVE A ESCRIBIR")
            }
        } while (seguir == null)

        return seguir
    }


    override fun listarActividades(actividades: MutableList<Actividad>) {
        for(actividad in actividades){
            println(actividad.obtenerDetalle())
        }
    }
}