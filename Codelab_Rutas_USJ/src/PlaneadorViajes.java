import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

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
        this.actual = null;

    }

    public void solve() {
        Ciudad current = null;
        
        while(actual.equals(origen)) {
            ArrayList<Ruta> posiblesDestinos = new ArrayList<Ruta>();
            if(bestRoute.isEmpty()){
                current = origen;
                this.bestRoute.add(current);
            } else {
                current = actual;
            }
            for(Ruta ruta : routes) {
                if(ruta.origen.name.equals(current.name)) {
                    posiblesDestinos.add(ruta);
                }
            }
            Collections.sort(posiblesDestinos);
            while(!posiblesDestinos.isEmpty()){
                if(!checkCityVisited(posiblesDestinos.get(0).destino)){
                    this.bestRoute.add(posiblesDestinos.get(0).destino);
                    break;
                } else {
                    this.bestRoute.remove(posiblesDestinos.get(0).destino);
                }
            }
            this.actual = posiblesDestinos.get(0).destino;
            posiblesDestinos.clear();
        }
        this.bestRoute.add(origen);
    }

    public boolean checkCityVisited(Ciudad city){
        for(Ciudad cCity: this.cities){
            if(cCity.equals(city)){
                return true; //Visited
            }
        }
        return false; //Not visited
    }

    public void updateCitiesRoute(Ciudad city){
        this.cities.add(city);
    }

    public boolean allVisitCities(ArrayList<Ciudad> allCities){
        for(Ciudad cCity: allCities){
            if(!checkCityVisited(cCity)){
                return false;
            }
        }
        return true;
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
