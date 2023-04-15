package hu.mobilalk.trainticketapp.enums;

public enum Discount {
    NONE(1),
    STUDENT(0.5),
    WORKER(0.9);

    private double value;

    Discount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
