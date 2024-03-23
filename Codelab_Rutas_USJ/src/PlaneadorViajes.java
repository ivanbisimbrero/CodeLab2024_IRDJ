import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

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
        
        try (BufferedReader reader = new BufferedReader(new FileReader("./datasets/spain/ciudades.txt"))) {
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

        try(BufferedReader reader = new BufferedReader(new FileReader("./datasets/spain/rutas.txt"))) {
            String linea = reader.readLine();
            while(linea != null) {
                //System.out.println(linea);
                String[] regex = linea.split(";");
                this.routes.add(new Ruta(findCityByName(regex[0]), findCityByName(regex[1]), Integer.parseInt(regex[2]), Integer.parseInt(regex[3])));
                linea = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.origen = cities.get(0);
        this.bestRoute.add(this.origen);
        markAsVisited(this.origen);;
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
            //System.out.println(posiblesDestinos.get(0).destino.name);
            while(!posiblesDestinos.isEmpty()){
                if(!cityVisited(posiblesDestinos.get(0).destino)){
                    //System.out.println(posiblesDestinos.get(0).destino.visited);
                    this.bestRoute.add(posiblesDestinos.get(0).destino);
                    markAsVisited(posiblesDestinos.get(0).destino);
                    break;
                } else {
                    posiblesDestinos.remove(posiblesDestinos.get(0));
                }
            }

            //El grafo proporcionado no puede hacer un ciclo euleriano
            if(posiblesDestinos.isEmpty()){
                throw new Error("El grafo no puede ser recorrido solo una vez por cada nodo");
            }

            this.actual = posiblesDestinos.get(0).destino;
            posiblesDestinos.clear();
        }

        System.out.println(isValidRoute(this.actual,this.origen));
        if(isValidRoute(this.actual,this.origen)){
            this.bestRoute.add(origen);
        }
    }

    private void sortDestinos(ArrayList<Ruta> posiblesDestinos){
        Collections.sort(posiblesDestinos, new Comparator<Ruta>() {
            @Override
            public int compare(Ruta ruta1, Ruta ruta2) {
                return Integer.compare(ruta1.costeViaje, ruta2.costeViaje);
            }
        });
    }

    public Ciudad findCityByName(String name) {
        for (Ciudad city : this.cities) {
            if (city.name.equals(name)) {
                return city;
            }
        }
        return null; // o puedes lanzar una excepción si prefieres
    }

    public boolean cityVisited(Ciudad city){
        return city.visited;
    }

    public boolean isValidRoute(Ciudad origen, Ciudad destino) {
        for(Ruta ruta: this.routes){
            if(ruta.origen.equals(origen) && ruta.destino.equals(destino)){
                return true;
            }
        }
        return false;
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

        //Comprobación final de si es el mismo número de ciudades
        Set<Ciudad> sBestRoutes = new HashSet<>(this.bestRoute);
        System.out.println("Ciudades: "+this.cities.size()+"\nBest Route: "+sBestRoutes.size());
    }
}
