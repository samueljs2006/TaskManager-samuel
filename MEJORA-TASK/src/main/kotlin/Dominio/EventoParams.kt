package dominio

data class EventoParams(
    val usuario: String,
    val id: String,
    val descripcion: String,
    val fechaCreacion: String,
    val fecha: String,
    val ubicacion: String
)