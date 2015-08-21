package se.mtm.speech.synthesis.infrastructure.configuration;

public class MinimumMemory {
    private final int min;

    /**
     * @param min The minimum memory that should be available before allocating a new Filibuster
     */
    public MinimumMemory(int min) {
        this.min = min;
    }

    public int getMin() {
        return min;
    }
}
