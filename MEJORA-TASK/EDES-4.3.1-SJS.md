[1]  
**1.a** ¿Qué herramienta has usado, y para qué sirve?
He usado la herramienta Detekt, que sirve para analizar estáticamente el código Kotlin y detectar problemas de estilo, errores comunes y malas prácticas.

**1.b** ¿Cuáles son sus características principales?

Sus características principales son: 
- Detección de code smells y errores de estilo. 
- Configuración flexible mediante archivos YAML. 
- Integración con Gradle y generación de informes. 
- Permite crear un baseline para ignorar errores existentes.

**1.c** ¿Qué beneficios obtengo al utilizar dicha herramienta?

Los beneficios son: 
- Mejora la calidad y legibilidad del código. 
- Facilita el mantenimiento. 
- Ayuda a detectar errores antes de ejecutar el programa. 
- Permite adoptar buenas prácticas de forma progresiva.

[2]  
**2.a** De los errores/problemas que la herramienta ha detectado y te ha ayudado a solucionar, ¿cuál es el que te ha parecido que ha mejorado más tu código?

El error que más ha mejorado mi código es el de funciones con demasiada anidación (NestedBlockDepth).

**2.b** ¿La solución que se le ha dado al error/problema la has entendido y te ha parecido correcta?

Sí, la solución la he entendido y me ha parecido correcta, ya que consiste en extraer partes del código en funciones auxiliares y usar guard clauses.

**2.c** ¿Por qué se ha producido ese error/problema?
Este error se produce cuando una función tiene muchos niveles de bucles o condicionales anidados, lo que complica su comprensión y mantenimiento.

[3]  
**3.a** ¿Qué posibilidades de configuración tiene la herramienta?  

Detekt permite configurar reglas como longitud máxima de línea, número máximo de funciones por clase, nombres de paquetes, uso de baseline, etc.

**3.b** De esas posibilidades de configuración, ¿cuál has configurado para que sea distinta a la que viene por defecto?

He configurado la opción baseline, que no viene activa por defecto, para ignorar errores existentes y centrarme solo en los nuevos.


**3.c** Pon un ejemplo de cómo ha impactado en tu código, enlazando al código anterior al cambio, y al posterior al cambio.

Antes de activar el baseline, Detekt mostraba todos los errores, incluidos los antiguos. Después de configurarlo con baseline = file("config/detekt/baseline.xml"), solo muestra los nuevos errores.


[4]  
**4** ¿Qué conclusiones sacas después del uso de estas herramientas?

El uso de herramientas como Detekt ayuda a mantener un código más limpio, uniforme y fácil de mantener. Permite detectar problemas de calidad de forma automática y fomenta buenas prácticas en el equipo de desarrollo.

