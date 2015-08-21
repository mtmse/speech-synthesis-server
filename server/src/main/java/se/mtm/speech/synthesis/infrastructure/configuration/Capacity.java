package se.mtm.speech.synthesis.infrastructure.configuration;

public class Capacity {
    private final int cap;

    /**
     * @param capacity The size of the in que on the server
     */
    public Capacity(int capacity) {
        this.cap = capacity;
    }

    public int getCapacity() {
        return cap;
    }
}

