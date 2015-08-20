package se.mtm.speech.synthesis.infrastructure.configuration;

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
}
