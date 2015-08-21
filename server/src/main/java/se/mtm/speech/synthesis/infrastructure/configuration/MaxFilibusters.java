package se.mtm.speech.synthesis.infrastructure.configuration;

public class MaxFilibusters {
    private final int max;

    /**
     * @param max The maximum number of Filibusters the server should try to create
     */
    public MaxFilibusters(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
