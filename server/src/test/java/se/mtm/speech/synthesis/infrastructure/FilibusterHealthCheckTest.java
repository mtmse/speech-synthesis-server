package se.mtm.speech.synthesis.infrastructure;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Test;
import se.mtm.speech.synthesis.synthesize.SpeechSynthesizer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilibusterHealthCheckTest {
    @Test
    public void get_healthy() throws Exception {
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.isHealthy()).thenReturn(true);
        FilibusterHealthCheck healthCheck = new FilibusterHealthCheck(synthesizer);

        HealthCheck.Result actual = healthCheck.check();

        assertTrue("The Synthesiser should have been healthy", actual.isHealthy());
    }

    @Test
    public void get_unhealthy() throws Exception {
        SpeechSynthesizer synthesizer = mock(SpeechSynthesizer.class);
        when(synthesizer.isHealthy()).thenReturn(false);
        FilibusterHealthCheck healthCheck = new FilibusterHealthCheck(synthesizer);

        HealthCheck.Result actual = healthCheck.check();

        assertFalse("The Synthesiser should have been unhealthy", actual.isHealthy());
    }
}
