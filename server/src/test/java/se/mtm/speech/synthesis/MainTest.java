package se.mtm.speech.synthesis;

import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.synyhesize.SynthesizeResource;

import static org.mockito.Mockito.*;

public class MainTest {
    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);
    private final Main application = new Main();
    private final LifecycleEnvironment lifecycleEnv = new LifecycleEnvironment();
    private final HealthCheckRegistry healthCheck = new HealthCheckRegistry();
    private final Configuration config = new Configuration();

    @Before
    public void setUp() throws Exception {
        when(environment.lifecycle()).thenReturn(lifecycleEnv);
        when(environment.healthChecks()).thenReturn(healthCheck);
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void buildsASynthesizeResourceResource() throws Exception { // NOPMD
        application.run(config, environment);

        verify(jersey).register(isA(SynthesizeResource.class));
    }
}
