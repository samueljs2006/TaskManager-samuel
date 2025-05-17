# Pruebas Unitarias para `ActividadService`

## Servicio seleccionado
**`ActividadService`**

---

## Métodos públicos identificados

| Método             | Parámetros de entrada | Resultado esperado/efecto en repositorio                          |
|---------------------|-----------------------|--------------------------------------------------------------------|
| `agregarSubtarea`   | Ninguno               | Subtarea agregada a tarea madre. Historial actualizado.           |
| `cambiarEstado`     | Tarea                 | Estado de la tarea actualizado. Historial registrado.             |
| `filtrarPorEstado`  | Ninguno               | Lista tareas filtradas por estado o muestra mensaje si no hay.    |
| `anadirActividad`   | Ninguno               | Nueva actividad añadida al repositorio o error si datos inválidos.|

---

## Diseño de casos de prueba

| Método             | Caso de prueba                           | Estado inicial del mock                     | Acción                          | Resultado esperado                                                                 |
|---------------------|------------------------------------------|---------------------------------------------|---------------------------------|-----------------------------------------------------------------------------------|
| `agregarSubtarea`   | Tarea madre existe                       | `mockRepo.tareas` contiene tarea con ID "1" | Llamar método                  | Subtarea agregada, historial actualizado                                          |
| `agregarSubtarea`   | Tarea madre no existe                    | `mockRepo.tareas` vacío                     | Llamar método                  | No se agrega subtarea, historial no se actualiza                                   |
| `cambiarEstado`     | Estado válido (`EN_PROGRESO`)            | Tarea mockeada                               | Llamar método con estado válido| Estado actualizado, historial registrado                                          |
| `cambiarEstado`     | Estado inválido                          | Tarea mockeada                               | Llamar método con estado inválido| Estado no cambia, historial no se registra                                        |
| `filtrarPorEstado`  | Tareas con estado `ABIERTA` existen       | `mockRepo.tareas` contiene tarea `ABIERTA`  | Llamar método y seleccionar filtro| Muestra lista filtrada                                                            |
| `filtrarPorEstado`  | No hay tareas con estado solicitado      | `mockRepo.tareas` vacío                     | Llamar método y seleccionar filtro| Muestra mensaje "No hay tareas..."                                                |
| `anadirActividad`   | Datos válidos                            | `mockConsola` devuelve actividad válida      | Llamar método                  | Actividad añadida al repo, historial actualizado                                  |
| `anadirActividad`   | Datos inválidos (actividad `null`)       | `mockConsola` devuelve `null`                | Llamar método                  | Actividad no se añade, historial registra error                                   |

---

## Implementación de tests (extensión del código original)
