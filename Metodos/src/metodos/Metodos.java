package metodos;

import java.util.Arrays;

public class Metodos {

    /**
    Una universidad está programando las clases para el
    próximo semestre académico y requiere buscar la mejor asignación posible de
    profesores a los distintos cursos que se deben dictar. Considere que existen 5
    profesores: A, B, C, D, E y 5 cursos (asignaturas): C1, C2, C3, C4, C5.
    Adicionalmente, los profesores han manifestado sus preferencias por dictar los
    distintos cursos en una escala de 1 a 10, donde 10 es la máxima puntuación y 1 la
    mínima puntuación o preferencia. Se asume que cada profesor es apto para dictar
    cualquier curso, independiente del puntaje de su preferencia. 
     */
    
    public static void main(String[] args) {
        //Matriz 
        int[][] preferencias = {
            {5, 8, 5, 9, 7},
            {7, 2, 3, 6, 8},
            {9, 10, 8, 9, 8},
            {8, 7, 9, 7, 8},
            {6, 9, 9, 10, 5}
        };
        
        int[] asignacion = asignarProfesoresCursos(preferencias);
        
        //Encabezado
        System.out.println("Asignación de profesores a cursos:");
        for (int i = 0; i < asignacion.length; i++) {
            System.out.println("Profesor " + (char) ('A' + i) + " asignado a Curso C" + (asignacion[i] + 1));
        }
    }
    
    public static int[] asignarProfesoresCursos(int[][] preferencias) {
        int numProfesores = preferencias.length;
        int numCursos = preferencias[0].length;
        
        int[] asignacion = new int[numProfesores];
        Arrays.fill(asignacion, -1); // Inicialmente, ningún profesor asignado a ningún curso
        
        for (int curso = 0; curso < numCursos; curso++) {
            int mejorProfesor = -1;
            int maxPuntuacion = -1;
            
            for (int profesor = 0; profesor < numProfesores; profesor++) {
                if (asignacion[profesor] == -1 && preferencias[profesor][curso] > maxPuntuacion) {
                    mejorProfesor = profesor;
                    maxPuntuacion = preferencias[profesor][curso];
                }
            }
            
            if (mejorProfesor != -1) {
                asignacion[mejorProfesor] = curso;
            }
        }
        
        return asignacion;
    }
}
