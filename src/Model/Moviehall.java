package Model;

public class Moviehall {

    private String id; // Simplified to guarantee JavaFX binding
    private String name;
    private int capacity;
    private double pricePerSeat;
    public Moviehall() {}

    public Moviehall(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    @Override
    public String toString() {
        return "MovieHall{id='" + id + "', name='" + name + "', capacity=" + capacity + "}";
    }


}