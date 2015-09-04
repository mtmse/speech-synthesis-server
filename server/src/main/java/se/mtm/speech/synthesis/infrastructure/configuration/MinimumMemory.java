package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * The minimum memory that should be available before allocating a new Filibuster
 */
public class MinimumMemory {
    private final int min;

    public MinimumMemory(int min) {
        this.min = min;
    }

    public int getMin() {
        return min;
    }

    @Override
    public String toString() {
        return "" + min;
    }
}
