public class Main {

    public static void main(String[] args) {
        for (int i=0; i<50;i++){
            PlaneadorViajes p = new PlaneadorViajes(i);
            p.solve();
            if(i == 3){ //Best route found in ALMERIA == 10310 h
                p.writeResult();
            }
        }
    }

}
