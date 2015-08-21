package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * The size of the in que on the server
 */
public class Capacity {
    private final int cap;

    public Capacity(int capacity) {
        this.cap = capacity;
    }

    public int getCapacity() {
        return cap;
    }
}

