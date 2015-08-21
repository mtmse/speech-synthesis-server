package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * ttl Time to live in minutes
 */
public class TimeToLive {
    private final long ttl;

    public TimeToLive(int ttl) {
        this.ttl = ttl;
    }

    public long getTtlInMilliseconds() {
        return ttl * 60 * 1000;
    }
}
