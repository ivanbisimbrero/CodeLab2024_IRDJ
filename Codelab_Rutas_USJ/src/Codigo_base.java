/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//    Archivo: codigo_base.java                                                //
//    Fecha de última revisión: 15/10/2023                                     //
//    Autores: Francisco Javier Pizarro 821259                                 //
//    Comms:                                                                   //
//          Este archivo contiene el esqueleto para facilitar la implementación//
//          de la solución del 1º problema del Unicode 23                      //
//          Modificar el contenido de la función solve_problem_instance        //
//          Para una bonita experiencia de desarrollo te recomendaría usar     //
//          uno de los otros dos lenguajes, pero eres libre de elegir :D       //
//    Use:  javac Codigo_base.java && java Codigo_base                         //
//                                                                             //
/////////////////////////////////////////////////////////////////////////////////

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Codigo_base {

    public static void main(String[] args) {
        String[] NOMBRES_FICHEROS = {"facil", "normal", "dificil", "aun_mas_dificil"};
        
        for (String nombre_fichero : NOMBRES_FICHEROS) {
            System.out.println("Ejecutando para el dataset: " + nombre_fichero);
            
            // Read data
            Data data = readData("./datasets/" + nombre_fichero + ".txt");
            
            // Calculate solutions
            Result result = solveProblemInstance(data.classes, data.maximumCapacity);
            
            // Write solutions
            writeResult("./soluciones/" + nombre_fichero + ".txt", result);
        }
    }

    public static Data readData(String fileName) {
        System.out.println("\tCargando datos del fichero de entrada");
        Data data = new Data();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String firstLine = reader.readLine().trim();
            String[] firstInts = firstLine.split(" ");
            data.maximumCapacity = Integer.parseInt(firstInts[0]);
            int numIntsToRead = Integer.parseInt(firstInts[1]);

            String secondLine = reader.readLine().trim();
            String[] secondInts = secondLine.split(" ");
            data.classes = new ArrayList<>();
            for (String num : secondInts) {
                data.classes.add(Integer.parseInt(num));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Result solveProblemInstance(List<Integer> classes, int maximum) {
        System.out.println("\tEjecutando el algoritmo para resolver el problema");
        int nClasses = classes.size();

        // Implement your solution here
        for (int clase : classes) {
            System.out.println(clase);
        }

        List<Integer> solution = new ArrayList<>();
        int mayorNAsistentes = 0;

        return new Result(mayorNAsistentes, solution, solution.size());
    }

    public static void writeResult(String fileName, Result result) {
        System.out.println("\tEscribiendo resultados en el fichero de salida");
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(result.numClassesSolution + "\n");
            String classSolution = String.join(" ", result.classesSolution.stream().map(String::valueOf).toArray(String[]::new));
            writer.write(classSolution);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Data {
        int maximumCapacity;
        List<Integer> classes;
    }

    static class Result {
        int numClassesSolution;
        List<Integer> classesSolution;
        int numClasses;

        Result(int numClassesSolution, List<Integer> classesSolution, int numClasses) {
            this.numClassesSolution = numClassesSolution;
            this.classesSolution = classesSolution;
            this.numClasses = numClasses;
        }
    }
}
