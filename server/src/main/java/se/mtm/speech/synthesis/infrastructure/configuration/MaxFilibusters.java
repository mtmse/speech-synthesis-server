package se.mtm.speech.synthesis.infrastructure.configuration;


/**
 * The maximum number of Filibusters the server should try to create
 */
public class MaxFilibusters {
    private final int max;

    public MaxFilibusters(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
