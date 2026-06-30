package domain.model;

public enum BookingStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    CANCELLED("cancelled");

    private final String dbValue;

    BookingStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static BookingStatus fromDbValue(String dbValue) {
        for (BookingStatus s : values()) {
            if (s.dbValue.equals(dbValue)) return s;
        }
        throw new IllegalArgumentException("Unknown BookingStatus: " + dbValue);
    }
}
