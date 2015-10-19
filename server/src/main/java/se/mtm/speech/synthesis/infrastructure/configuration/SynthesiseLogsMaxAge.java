package se.mtm.speech.synthesis.infrastructure.configuration;

import java.time.LocalDate;

/**
 * The maximum age for the synthesise log files
 */
public class SynthesiseLogsMaxAge {
    private final int maxLogAge;

    /**
     * Logs should not be older than the max age.
     *
     * @param maxLogAge the max age for a log file in days
     */
    public SynthesiseLogsMaxAge(int maxLogAge) {
        this.maxLogAge = maxLogAge;
    }

    public LocalDate getMaxLogAge() {
        return LocalDate.now().minusDays(maxLogAge);
    }

    @Override
    public String toString() {
        return "" + maxLogAge;
    }
}
