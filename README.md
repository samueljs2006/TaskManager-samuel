
# CORRECCIÓN DE ERRORES

## Error 1: Problema en las rutas de los ficheros

- Este error lo soluciona: Luismi

Al hacerle un clone a este repositorio para corregirle los errores, me encontré con un problema gordo, y es que las rutas de los ficheros estaban fallandome, me di cuenta de esto al ejecutar el programa de nuevo. Para darme cuenta de que era esto lo que me fallaba, lo que hice fue crearme una clase de logger y ponerlo en la clase de ActividadService ya que es la que gestiona todo el programa, entonces para manejar los errores en el logger la verdad es que es muchísimo más cómodo usarlo ahí.

![CAPTURA DE LA CLASE LOGGER](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/Screenshot_2.png)

Como se ve en la imagen, este logger lo meti en una variable en el control de historial, luego en actividadService lo que fui haciendo es ir dando errores en el logger o diciendo si ha salido bien, para que si da un error, te lo muestre en la terminal.

![CAPTURA DE LA CLASE ACTIVIDAD SERVICE](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/Screenshot_3.png)

Ahora, al ejecutar, me dio el siguiente fallo gracias al logger que le fui pusiendo en cada uno de los métodos:

![CAPTURA DEL ERROR 1](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/Screenshot_4.png)

Para solucionarlo, me puse manos a la obra y busqué el error através de dos puntos de rupturas:

![RUPTURA INICIAL](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/RUPTURA_INICIAL1.png)

![RUPTURA FINAL](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/RUPTURA_FINAL1.png)

Haciendo el debug, me fijé que al iniciar el programa, me decían que los ficheros no existían, ya no solo era el fichero de historial, eran todos los que me daban fallo, el error debía de tratarse en la especificación de la ruta más que otra cosa, ya que las variables se asignaban de forma correcta

![DEBUG1](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/DEBUG1.png)

Me fijé entonces en que el error residía en que empezaba la ruta en src/ pero como se ejecutaba desde fuera del módulo, tenía también que especificarle el nombre del módulo, al cambiarlo me fue de forma correcta, para probarlo hice que me mostrará el historial y como se ve en la imagen, el logger me muestra también que se ha ejecutado sin problema esta vez.

![SOLUCIÓN1](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR1/MUESTRA_SOLUCION1.png)

## Error 2 : Problema al crear subtareas

- Este error lo soluciona: Pablo

Al hacer el programa, el ejercicio pedía que una de las mejoras fuera crear una lista de tareas de una sola capa de profundidad, pero con lo de la capa de profundidad había entendido que solo se pudiera crear una tarea y claro, me estaba dando muchos problemas eso, porque no le podía asignar varias subtareas a una tarea. Como queríamos solucionar el error con las herramientas de depuración, cree un punto de ruptura que empezara desde la clase tarea y llevará hasta esa variable donde se almacenaba la tarea anteriormente. Vimos que el problema estaba en que teniamos una variable que era subTarea = Tarea? cuando tendría que ser una variable llamada subtareas que fuera una lista de tareas, lo cambiamos.

![CAMBIO VARIABLE](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR2/MUESTRA_ERROR2.png)

Al cambiarlo, ahora teníamos un problema, y es que no podíamos ejecutar ya que teníamos que cambiar muchas cosas en el programa que usaba la variable subTarea en lugar de esa lista que le estabamos dando

![NuevosErrores](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR2/ERROR_SUBTAREA.png)

Para ello, tuvimos que cambiar bastantes métodos para que cada vez que cogieran una tarea, a esta tarea se le cogiera esa lista y se le hiciera un for mostrando los detalles de cada una de sus subtareas.

![SolucionError](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR2/SOLUCION_ERROR.png)

## Error 3 : Identificadores únicos reales

- Este error lo soluciona: Samuel

Al seguir los pasos del ejercicio, nos dimos cuenta de que era imposible poder seleccionar una tarea por el id para poder agregarle a esta una subtarea ya que al crearse los id's con fechas y al las tareas crearse siempre con la fecha actual, siempre iban a tener el mismo id. Planteamos dos cosas sobre la mesa, la primera era hacer que solo se pudiera crear una tarea al día y añadirle a esta todo como subtareas, y la otra, fue la que hemos acabado ejecutando y la que tiene lógica, que es hacer que los id's se generen distintos para la clase tarea y la clase eventos, es decir, serán unicos para ambos, pero en el caso de las tareas, se aumenta siempre en 1 el id en vez de asociarselo a la fecha, ya que estas si las tenemos que seleccionar.

Nos pusimos manos a la obra y pusimos los puntos de ruptura y depuramos, al no ser error de ejecución, el logger aquí realmente no hizo mucho.

![PUNTOS DE RUPTURA](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR3/ERROR3.png)

![VARIABLES DEPURACIÓN](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR3/VARIABLES.png)

Como vemos, la cosa es que se le asigna la id siguiendo el mismo tratamiento que en los eventos, es decir, con la fecha. Para esto, simplemente vamos a hacer una variable contador que le vaya incrementando a las tareas su id y así lo podemos seleccionar por su id.

![CAPTURA SOLUCION 1](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR3/SOLUCION.png)

Creamos una variable contador en companion

![CAPTURA SOLUCION 2](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR3/SOLUCION_2.png)

A las id se le sumará ese contador

![CAPTURA SOLUCION 3](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR3/SOLUCION_3.png)

Vemos que ahora se le asigna perfectamente y además el log nos dice que se ha creado la subtarea en la tarea con id 11 con éxito

## Error 4: Subtareas en fichero de texto

- Este error lo soluciona: Angel

Este fue el problema que nos percatamos de primeras, y es que teníamos en mente empezar con este error pero nos surgieron algunos otros por el camino. La cosa es que nos fijamos que en el fichero de texto no se nos almacenaban las subtareas de las tareas, lo que hacia que la creación de subtareas solo se pudiera manejar en memoria y no con los ficheros de texto, que es como decidimos que funcionará nuestro programa por darle un poco más el toque de realismo de cerrar y tener los mismos datos.

Para empezar, volvimos a poner nuestros famosos puntos de ruptura para depurar

![PUNTO DE RUPTURA](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR4/PUNTOS_RUPTURA.png)

En cuanto lo pusimos, detectamos el problema enseguida ya que al final, pusimos el punto de ruptura en el método que sabíamos que iba a ser el que daba problemas. Como vemos, en las variables no se está utilizando correctamente la deserialización de tareas, de hecho, la variable de subtareas sale gris en el ide porque no se usa, es decir, que no estabamos almacenandolas porque se nos pasó de largo (también mas que nada porque esta era una tarea más costosa y decidimos dejarla para finales pero se nos pasó por completo y por eso sabíamos que este iba a ser un error que solucionar en este ejercicio nuevo)

![ERROR LOCALIZADO](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR4/ERROR_DETECTADO.png)


Al final, para este error, lo único que había que hacer era añadirle más cosas al código, era el más fácil de detectar, pero a la vez, el más dificil de solucionar porque había que trastear un poco más

![MUESTRA DE SUBTAREAS EN FICHERO](https://github.com/Luismi0202/TaskManager-LGOMDOM/blob/main/IMAGENES/ERROR4/ARREGLO.png)

## Conclusiones y Aprendizajes
1. **Importancia de la Validación**:
   - La falta de validaciones adecuadas permitió la aparición de un comportamiento no esperado. Implementar restricciones en los puntos críticos del código es esencial.

2. **Ventajas del Logging Centralizado**:
   - Usar un logger en una clase central permitió monitorear todo el programa sin necesidad de agregar loggers en cada clase individualmente.

3. **Depuración Eficiente**:
   - El uso de puntos de interrupción y la inspección de variables fue clave para identificar el origen del problema.
   - La combinación de depuración paso a paso y logging permitió solucionar el error rápidamente.

4. **Mantenimiento del Código**:
   - Al eliminar la posibilidad de jerarquías infinitas de subtareas, se mejoró la estabilidad y mantenibilidad del sistema.

También hemos visto que cada vez que se intenta arreglar un error, se generan otros 200 más, haciendonos ver la importancia de estas herramientas dadas en el temario para poder afrontar todos estos problemas, porque realmente solo teníamos dos errores que queríamos solucionar, pero de ahí encontramos otros dos.
