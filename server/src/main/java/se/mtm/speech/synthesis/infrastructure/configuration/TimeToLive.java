package se.mtm.speech.synthesis.infrastructure.configuration;

public class TimeToLive {
    private final long ttl;

    /**
     * @param ttl Time to live in minutes
     */
    public TimeToLive(int ttl) {
        this.ttl = ttl;
    }

    public long getTtlInMilliseconds() {
        return ttl * 60 * 1000;
    }
}
