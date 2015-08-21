package se.mtm.speech.synthesis.infrastructure.configuration;

public class IdleTime {
    private final long idle;

    public IdleTime(long idle) {
        this.idle = idle;
    }

    public long getIdle() {
        return idle;
    }
}
