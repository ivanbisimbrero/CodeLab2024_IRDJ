public class Ruta implements Comparable{

    Ciudad origen;
    Ciudad destino;
    int tiempoTotalViaje;
    int costeViaje;

    public Ruta(Ciudad origen, Ciudad destino, int tiempoTotalViaje, int costeViaje){
        this.origen = origen;
        this.destino = destino;
        this.tiempoTotalViaje = tiempoTotalViaje;
        this.costeViaje = costeViaje;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Ruta)) {
            throw new ClassCastException("Un objeto Ruta esperado.");
        }
        Ruta otraRuta = (Ruta) o;
        int comparacionCoste = Integer.compare(this.costeViaje, otraRuta.costeViaje);
        if (comparacionCoste != 0) {
            return comparacionCoste;
        } else {
            return Integer.compare(this.tiempoTotalViaje, otraRuta.tiempoTotalViaje);
        }
    }

}
