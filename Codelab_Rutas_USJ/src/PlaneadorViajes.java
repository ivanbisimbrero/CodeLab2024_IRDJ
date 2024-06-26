import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class PlaneadorViajes {
    int budget;
    int costRoute;
    int timeRoute;
    List<Ciudad> cities;
    List<Ruta> routes;
    List<Ciudad> bestRoute;
    List<Ruta> bestRoutePath;
    Ciudad origen;
    Ciudad actual;

    public PlaneadorViajes(int index) {
        this.cities = new ArrayList<Ciudad>();
        this.routes = new ArrayList<Ruta>();
        this.bestRoute = new ArrayList<Ciudad>();
        this.bestRoutePath = new ArrayList<Ruta>();
        this.costRoute = 0;
        this.timeRoute = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("./datasets/spain/ciudades.txt",StandardCharsets.UTF_8))) {
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

        try(BufferedReader reader = new BufferedReader(new FileReader("./datasets/spain/rutas.txt",StandardCharsets.UTF_8))) {
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

        this.origen = cities.get(index);
        this.bestRoute.add(this.origen);
        markAsVisited(this.origen);
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

            //Buscar las rutas más eficientes en tiempo que se adapten al presupuesto
            if(this.costRoute >= this.budget/2){
                sortDestinosCost(posiblesDestinos); //Minimizar presupuesto cuando empieza a importar
            } else{
                sortDestinosTime(posiblesDestinos); //Empiezas minimizando el tiempo
            }

            while(!posiblesDestinos.isEmpty()){
                if(!cityVisited(posiblesDestinos.get(0).destino)){
                    //System.out.println(posiblesDestinos.get(0).destino.visited);
                    this.bestRoute.add(posiblesDestinos.get(0).destino);
                    this.bestRoutePath.add(posiblesDestinos.get(0));
                    markAsVisited(posiblesDestinos.get(0).destino);
                    this.costRoute += posiblesDestinos.get(0).costeViaje;
                    this.timeRoute += posiblesDestinos.get(0).tiempoTotalViaje;
                    break;
                } else {
                    posiblesDestinos.remove(posiblesDestinos.get(0));
                }
            }

            //El grafo proporcionado no puede hacer un ciclo hamiltoniano
            if(posiblesDestinos.isEmpty()){
                throw new Error("El grafo no puede ser recorrido solo una vez por cada nodo");
            }

            this.actual = posiblesDestinos.get(0).destino;
            posiblesDestinos.clear();
        }

        //System.out.println(isValidRoute(this.actual,this.origen));
        ArrayList<Ruta> finalR = findRoutesByName(this.actual,this.origen);
        sortDestinosTime(finalR);
        if(!finalR.isEmpty()){
            this.bestRoute.add(origen);
            this.bestRoutePath.add(finalR.get(0));
            this.costRoute += finalR.get(0).costeViaje;
            this.timeRoute += finalR.get(0).tiempoTotalViaje;
        }

        //VALIDACIONES DE PReSUPUESTO
        if(this.costRoute <= this.budget){
            System.out.println("COSTE RUTA VALIDA EN "+this.origen.name+": "+this.costRoute+"; "+this.timeRoute+" minutos");
        } else {
            System.out.println(this.costRoute);
        }

    }

    private void sortDestinosTime(ArrayList<Ruta> posiblesDestinos){
        Collections.sort(posiblesDestinos, new Comparator<Ruta>() {
            @Override
            public int compare(Ruta ruta1, Ruta ruta2) {
                return Integer.compare(ruta1.tiempoTotalViaje, ruta2.tiempoTotalViaje);
            }
        });
    }

    private void sortDestinosCost(ArrayList<Ruta> posiblesDestinos){
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
        return null;
    }

    public boolean cityVisited(Ciudad city){
        return city.visited;
    }

    public ArrayList<Ruta> findRoutesByName(Ciudad origen, Ciudad destino) {
        ArrayList<Ruta> rutas = new ArrayList<Ruta>();
        for(Ruta ruta: this.routes){
            if(ruta.origen.equals(origen) && ruta.destino.equals(destino)){
                rutas.add(ruta);
            }
        }
        return rutas;
    }

    public void markAsVisited(Ciudad city){
        city.visited=true;
    }

    public void writeResult() {
        System.out.println("\tEscribiendo resultados en el fichero de salida");
        try (FileWriter writer = new FileWriter("./soluciones/solucion_ciudades.txt", StandardCharsets.UTF_8)) {
            for(int i = 0; i < bestRoute.size(); i++) {
                writer.write(bestRoute.get(i).name + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter("./soluciones/solucion_rutas.txt", StandardCharsets.UTF_8)) {
            for(int i = 0; i < bestRoutePath.size(); i++) {
                writer.write(bestRoutePath.get(i).toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Comprobación final de si es el mismo número de ciudades
        //Set<Ciudad> sBestRoutes = new HashSet<>(this.bestRoute);
        //System.out.println("\nBest Route Array: "+this.bestRoute.size());
        //System.out.println("Ciudades: "+this.cities.size()+"\nBest Route Set: "+sBestRoutes.size());
    }
}
