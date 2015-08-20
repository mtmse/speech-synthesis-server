package se.mtm.speech.synthesis.infrastructure.configuration;

public class MaxFilibusters {
    private final int max;

    public MaxFilibusters(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
