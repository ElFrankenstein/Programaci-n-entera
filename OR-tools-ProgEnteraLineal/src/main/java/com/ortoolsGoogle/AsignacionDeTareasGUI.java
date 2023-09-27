package com.ortoolsGoogle;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AsignacionDeTareasGUI extends JFrame {

    private final JTextField numTareasField;
    private final JTextField numRecursosField;
    private final JTextField duracionesField;
    private final JTextField dependenciasField;
    private final JTextArea outputArea;

    public AsignacionDeTareasGUI() {
        setTitle("Asignación de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Número de Tareas:"));
        numTareasField = new JTextField();
        inputPanel.add(numTareasField);
        inputPanel.add(new JLabel("Número de Recursos:"));
        numRecursosField = new JTextField();
        inputPanel.add(numRecursosField);
        inputPanel.add(new JLabel("Duraciones (separadas por comas):"));
        duracionesField = new JTextField();
        inputPanel.add(duracionesField);
        inputPanel.add(new JLabel("Dependencias (pares separados por comas):"));
        dependenciasField = new JTextField();
        inputPanel.add(dependenciasField);
        JButton solveButton = new JButton("Resolver");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverProblema();
            }
        });
        inputPanel.add(solveButton);

        // Área de salida de mensajes y resultados
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        // Agregar componentes a la ventana
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private void resolverProblema() {
       /*******************************************/
       /* Carga la biblioteca OR-Tools de GOOGLE  */
       /*******************************************/
        Loader.loadNativeLibraries();

        // Obtener datos de entrada
        int numTareas = Integer.parseInt(numTareasField.getText());
        int numRecursos = Integer.parseInt(numRecursosField.getText());
        String[] duracionesStr = duracionesField.getText().split(",");
        int[] duraciones = new int[numTareas];
        for (int i = 0; i < numTareas; i++) {
            duraciones[i] = Integer.parseInt(duracionesStr[i].trim());
        }
        String[] dependenciasStr = dependenciasField.getText().split(",");

        // Verifica si la longitud de dependenciasStr es un múltiplo de 2
        if (dependenciasStr.length % 2 != 0) {
            outputArea.setText("Error: El número de elementos en las dependencias debe ser un múltiplo de 2.");
            return; // Sale del método sin continuar
        }
        
         /*******************************************/
        /* Crea el array dependencias              */
       /*******************************************/
        int[][] dependencias = new int[dependenciasStr.length / 2][2];
        for (int i = 0; i < dependenciasStr.length; i += 2) {
            dependencias[i / 2][0] = Integer.parseInt(dependenciasStr[i].trim());
            dependencias[i / 2][1] = Integer.parseInt(dependenciasStr[i + 1].trim());
        }

        // Antes de resolver el problema
        outputArea.append("Resolviendo el problema...\n");

     
         /********************************************************/
        /* Crea el solucionador de programación lineal entera ***/
       /********************************************************/
        MPSolver solver = new MPSolver("AsignacionTareas", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

        // Define las variables de decisión: asignación de cada tarea a un recurso
        MPVariable[][] asignacionTareas = new MPVariable[numTareas][numRecursos];

        for (int i = 0; i < numTareas; i++) {
            for (int j = 0; j < numRecursos; j++) {
                asignacionTareas[i][j] = solver.makeIntVar(0, 1, "Tarea_" + i + "_Recurso_" + j);

                // Mensajes de depuración sobre las variables
                outputArea.append("Variable: " + asignacionTareas[i][j].name() + "\n");
            }
        }

        // Define la función objetivo: minimiza la duración total del proyecto
        MPObjective objective = solver.objective();
        for (int i = 0; i < numTareas; i++) {
            for (int j = 0; j < numRecursos; j++) {
                objective.setCoefficient(asignacionTareas[i][j], duraciones[i]);
            }
        }
        objective.setMinimization();

        // Agregar restricciones para garantizar que las dependencias se cumplan
        for (int[] dependencia : dependencias) {
            int tareaAnterior = dependencia[0];
            int tareaDependiente = dependencia[1];
            for (int j = 0; j < numRecursos; j++) {
                MPConstraint constraint = solver.makeConstraint(0, 1, "Dependencia_" + tareaAnterior + "_" + tareaDependiente + "_Recurso_" + j);

                constraint.setCoefficient(asignacionTareas[tareaAnterior][j], 1);
                constraint.setCoefficient(asignacionTareas[tareaDependiente][j], -1);

                // Mensaje de depuración para imprimir coeficientes de la restricción
                System.out.println("Coeficiente de tarea " + tareaAnterior + " en recurso " + j + ": " + 1);
                System.out.println("Coeficiente de tarea " + tareaDependiente + " en recurso " + j + ": " + -1);

                // Mensajes de depuración sobre las restricciones
                outputArea.append("Restricción: " + constraint.name() + "\n");
            }
        }

        // Resuelve el problema de programación lineal entera
        final MPSolver.ResultStatus resultStatus = solver.solve();

        // Después de resolver el problema
        outputArea.append("Fin de la resolución del problema.\n");

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            // Muestra la planificación óptima de asignación de tareas y la duración total del proyecto
            int duracionTotal = 0;
            outputArea.append("Planificación óptima de asignación de tareas:\n");
            for (int i = 0; i < numTareas; i++) {
                for (int j = 0; j < numRecursos; j++) {
                   
                        if (asignacionTareas[i][j].ub() == 1) {
                        outputArea.append("Asignar Tarea " + i + " a Recurso " + j + "\n");
                        duracionTotal += duraciones[i];
                    }
                }
            }
            outputArea.append("Duración total del proyecto: " + duracionTotal + "\n");
        } else {
            outputArea.append("No se encontró una solución óptima.\n");
        }
    }
    
 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AsignacionDeTareasGUI app = new AsignacionDeTareasGUI();
                app.setVisible(true);
            }
        });
    }
}
