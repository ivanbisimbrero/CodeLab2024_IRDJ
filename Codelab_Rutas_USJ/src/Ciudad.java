public class Ciudad {

    String name;
    double value;
    boolean visited;

    public Ciudad(String name) {
        this.name = name;
        this.visited = false;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object anotherObject) {
        if(anotherObject instanceof Ciudad) {
            Ciudad c = (Ciudad)anotherObject;
            return this.name.equals(c.name);
        }
        return false;
    }

}
