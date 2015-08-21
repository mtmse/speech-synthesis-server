package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * The idle time the server will wait before looking asking for a synthesized result in milli seconds
 */
public class IdleTime {
    private final long idle;

    public IdleTime(long idle) {
        this.idle = idle;
    }

    public long getIdle() {
        return idle;
    }
}
