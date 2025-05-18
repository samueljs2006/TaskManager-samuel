Aqui tienes la instalación de detekt
![img.png](img.png)
Analisis de código
![img_1.png](img_1.png)

1 error:
El error Function cargarActividades is nested too deeply. [NestedBlockDepth] indica que la función cargarActividades tiene demasiados niveles de anidamiento (bloques {} dentro de otros bloques), lo que dificulta la legibilidad y el mantenimiento del código. Detekt recomienda reducir la profundidad de anidamiento.
antes:
La función tenía varios niveles de anidamiento debido a los bloques try, if y when anidados.
![img_2.png](img_2.png)
Despues:
Se puede simplificar extrayendo partes del código en funciones auxiliares y usando guard clauses para reducir la anidación.
![img_3.png](img_3.png)

2 error:
El error The function creaInstancia(usuario: String, id: String, descripcion: String, fechaCreacion: String, fecha: String, ubicacion: String) has too many parameters. [LongParameterList] indica que la función creaInstancia tiene demasiados parámetros (6), lo que dificulta la legibilidad y el mantenimiento. Detekt recomienda reducir la cantidad de parámetros, por ejemplo, agrupándolos en un objeto de datos.
antes:
![img_4.png](img_4.png)

Despues:
Se crea un data class EventoParams para agrupar los parámetros y se modifica la función para recibir solo un parámetro de tipo EventoParams
![img_5.png](img_5.png)

3 error:
El error The function creaInstancia(usuario: String, id: String, etiqueta: EtiquetasTareas, fechaCreacion: String, descripcion: String, estado: String) has too many parameters. [LongParameterList] indica que la función creaInstancia tiene demasiados parámetros (6), lo que dificulta la legibilidad y el mantenimiento del código. Detekt recomienda reducir la cantidad de parámetros, por ejemplo, agrupándolos en un objeto de datos.
Antes:
![img_6.png](img_6.png)

Despues:
Se crea un data class TareaParams para agrupar los parámetros y se modifica la función para recibir solo un parámetro de tipo TareaParams.
![img_7.png](img_7.png)

4 error:
El error Function listarActividades is nested too deeply. [NestedBlockDepth] indica que la función listarActividades tiene demasiados niveles de anidamiento (por ejemplo, bucles, condicionales y bloques when dentro de otros), lo que dificulta la legibilidad y el mantenimiento del código. Detekt recomienda reducir la profundidad de anidamiento extrayendo partes en funciones auxiliares y usando guard clauses.
Antes:
La función tenía varios niveles de anidamiento por el uso de bucles, condicionales y bloques when anidados.
![img_8.png](img_8.png)
Despues:
Se extrajeron partes del código en funciones auxiliares y se usaron guard clauses para reducir la anidación.
![img_9.png](img_9.png)

5 error:
El error usuariosConActividades is nested too deeply. [NestedBlockDepth] indica que la función usuariosConActividades tiene demasiados niveles de anidamiento (bucles y condicionales), lo que dificulta la legibilidad y el mantenimiento del código. Detekt recomienda reducir la profundidad extrayendo partes en funciones auxiliares y usando guard clauses.
Antes:
La función tenía bucles y condicionales anidados:
![img_10.png](img_10.png)

Despues:
Se extrajeron partes en funciones auxiliares y se usaron guard clauses para reducir la anidación:
![img_11.png](img_11.png)

punto 5 una opción de configuración
Gracias a esta línea baseline = file("config/detekt/baseline.xml") se consigue que los errores que ya se hayan mostrado antes no se vuelvan a mostrar.
![img_12.png](img_12.png)