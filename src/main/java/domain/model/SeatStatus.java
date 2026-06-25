package domain.model;

public enum SeatStatus {
    AVAILABLE("available"),
    BOOKED("booked"),
    RESERVED("reserved");

    private final String dbValue;

    SeatStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static SeatStatus fromDbValue(String dbValue) {
        for (SeatStatus s : values()) {
            if (s.dbValue.equals(dbValue)) return s;
        }
        throw new IllegalArgumentException("Unknown SeatStatus: " + dbValue);
    }
}
