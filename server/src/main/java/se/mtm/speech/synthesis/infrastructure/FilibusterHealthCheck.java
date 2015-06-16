package se.mtm.speech.synthesis.infrastructure;

import com.codahale.metrics.health.HealthCheck;
import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;

public class FilibusterHealthCheck extends HealthCheck {
    private final SpeechSynthesizer speechSynthesizer;

    public FilibusterHealthCheck(SpeechSynthesizer speechSynthesizer) {
        this.speechSynthesizer = speechSynthesizer;
    }

    @Override
    protected Result check() throws Exception {
        if (speechSynthesizer.isHealthy()) {
            return Result.healthy();
        } else {
            return Result.unhealthy("At least one os process holding a Filibuster is dead.");
        }
    }
}
