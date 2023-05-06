package hu.mobilalk.trainticketapp.enums;

public enum Discount {
    NONE(1),
    STUDENT(0.5),
    WORKER(0.1);

    private double value;

    Discount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case NONE: return "Teljes árú jegy";
            case STUDENT: return "50% kedvezmény";
            case WORKER: return "90% kedvezmény";
            default: return "";
        }
    }
}
