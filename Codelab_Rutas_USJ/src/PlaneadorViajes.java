import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class PlaneadorViajes {
    int budget;
    List<Ciudad> cities;
    List<Ruta> routes;
    List<Ciudad> bestRoute;
    Ciudad origen;
    Ciudad actual;

    public PlaneadorViajes() {
        this.cities = new ArrayList<Ciudad>();
        this.routes = new ArrayList<Ruta>();
        this.bestRoute = new ArrayList<Ciudad>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("./datasets/simple/ciudades.txt"))) {
            String budgetString = reader.readLine();
            this.budget = Integer.parseInt(budgetString);
            String linea = reader.readLine();
            while(linea != null) {
                //System.out.println(linea);
                cities.add(new Ciudad(linea));
                linea = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(BufferedReader reader = new BufferedReader(new FileReader("./datasets/simple/rutas.txt"))) {
            String linea = reader.readLine();
            while(linea != null) {
                System.out.println(linea);
                String[] regex = linea.split(";");
                this.routes.add(new Ruta(new Ciudad(regex[0]), new Ciudad(regex[1]), Integer.parseInt(regex[2]), Integer.parseInt(regex[3])));
                linea = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.origen = cities.get(0);
        this.actual = this.origen;

    }

    public void solve() {
        
        while(bestRoute.size() < cities.size()) {
            ArrayList<Ruta> posiblesDestinos = new ArrayList<Ruta>();

            for(Ruta ruta : this.routes) {
                if(ruta.origen.equals(actual)) {
                    posiblesDestinos.add(ruta);
                }
            }

            sortDestinos(posiblesDestinos); //Al mínimo coste

            while(!posiblesDestinos.isEmpty()){
                if(!cityVisited(posiblesDestinos.get(0).destino)){
                    this.bestRoute.add(posiblesDestinos.get(0).destino);
                    markAsVisited(posiblesDestinos.get(0).destino);
                    break;
                } else {
                    this.bestRoute.remove(posiblesDestinos.get(0).destino);
                    sortDestinos(posiblesDestinos); //Volver a ordenar al mínimo
                }
            }

            //El grafo proporcionado no puede hacer un ciclo euleriano
            if(posiblesDestinos.isEmpty()){
                throw new Error("El grafo no puede ser recorrido solo una vez por cada nodo");
            }

            this.actual = posiblesDestinos.get(0).destino;
            posiblesDestinos.clear();
        }

        this.bestRoute.add(origen);
    }

    private void sortDestinos(ArrayList<Ruta> posiblesDestinos){
        Collections.sort(posiblesDestinos, new Comparator<Ruta>() {
            @Override
            public int compare(Ruta ruta1, Ruta ruta2) {
                return Integer.compare(ruta1.costeViaje, ruta2.costeViaje);
            }
        });
    }

    public boolean cityVisited(Ciudad city){
        return city.visited;
    }

    public void markAsVisited(Ciudad city){
        city.visited=true;
    }

    public void writeResult() {
        System.out.println("\tEscribiendo resultados en el fichero de salida");
        try (FileWriter writer = new FileWriter("./soluciones/solucion.txt")) {
            for(int i = 0; i < bestRoute.size(); i++) {
                writer.write(bestRoute.get(i).name + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
