# Programacion-entera + programa dinámica el archivo nombrado demo

Diagrama de flujo 
1.	Inicio del programa.
2.	Carga de la biblioteca OR-Tools.
3.	Obtención de datos de entrada:
  •	Se obtiene el número de tareas (numTareas).
  •	Se obtiene el número de recursos (numRecursos).
  •	Se obtienen las duraciones de las tareas en forma de cadena (duracionesStr).
  •	Se obtienen las dependencias entre tareas en forma de cadena (dependenciasStr).
4.	Verificación de la longitud de dependenciasStr:
  •	Se verifica si la longitud de dependenciasStr es un múltiplo de 2.
  •	Si no es un múltiplo de 2, se muestra un mensaje de error y se sale del programa.
5.	Creación del array de dependencias:
  • Se crea un array bidimensional llamado dependencias para almacenar las dependencias entre tareas. Los pares de dependencias se extraen de dependenciasStr.
7.	Creación del solucionador de programación lineal entera (MPSolver):
  •	Se crea un solucionador de programación lineal entera llamado "AsignacionTareas" con el tipo de problema "CBC_MIXED_INTEGER_PROGRAMMING".
8.	Definición de variables de decisión:
  •	Se crean las variables de decisión llamadas asignacionTareas, que representan la asignación de cada tarea a un recurso. Estas variables son binarias (0 o 1) y se crean en una matriz.
9.	Definición de la función objetivo:
  •	Se define la función objetivo para minimizar la duración total del proyecto. Los coeficientes de la función objetivo se configuran en función de las duraciones de las tareas.
10.	Agregación de restricciones de dependencia:
  •	Se agregan restricciones para garantizar que las dependencias entre tareas se cumplan. Se recorre el array dependencias y se crea una restricción para cada dependencia. Las restricciones están relacionadas con las     variables de asignación de tareas.
11.	Resolución del problema:
  •	Se resuelve el problema de programación lineal entera utilizando el solucionador.
  •	Se verifica si se encontró una solución óptima.
12.	Impresión de resultados:
  •	Si se encontró una solución óptima, se muestra la planificación óptima de asignación de tareas y la duración total del proyecto. Esto se hace recorriendo las variables de asignación de tareas y sumando las duraciones de las tareas asignadas a cada recurso.
  •	Si no se encontró una solución óptima, se muestra un mensaje indicando que no se encontró una solución óptima.
13.	Fin del programa.

