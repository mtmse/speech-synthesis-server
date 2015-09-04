package se.mtm.speech.synthesis.infrastructure.configuration;

/**
 * The time a job is allowed for synthesize before a timeout is returned to the caller in seconds
 */
public class Timeout {
    private final int seconds;

    public Timeout(int seconds) {
        this.seconds = seconds;
    }

    public int getTimeoutSeconds() {
        return seconds;
    }

    public long getTimeoutMilliseconds() {
        return seconds * 1000;
    }

    @Override
    public String toString() {
        return "" + seconds;
    }
}
