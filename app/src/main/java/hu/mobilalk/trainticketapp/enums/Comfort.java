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

    @Override
    public String toString() {
        switch (this) {
            case FAST_TRAIN: return "Gyorsvonat";
            case SECOND_CLASS: return "2. osztály";
            case FIRST_CLASS: return "1. osztály";
            default: return "";
        }
    }
}
