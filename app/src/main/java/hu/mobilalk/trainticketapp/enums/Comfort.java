package hu.mobilalk.trainticketapp.enums;

public enum Comfort {
    FAST_TRAIN(100),
    SECOND_CLASS(500),
    FIRST_CLASS(700);

    private int value;

    Comfort(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
