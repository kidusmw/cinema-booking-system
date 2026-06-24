package domain.model;

public class Hall {
    private Long hallId;
    private String name;
    private int capacity;
    private String hallType;

    public Hall() {}

    public Hall(Long hallId, String name, int capacity, String hallType) {
        this.hallId = hallId;
        this.name = name;
        this.capacity = capacity;
        this.hallType = hallType;
    }

    public boolean isVip() {
        return "vip".equals(hallType);
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
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

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }
}
