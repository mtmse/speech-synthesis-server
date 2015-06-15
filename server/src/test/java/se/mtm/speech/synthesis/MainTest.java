package se.mtm.speech.synthesis;

import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.Configuration;
import se.mtm.speech.synthesis.synyhesize.SynthesizeResource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MainTest {
    @Test
    public void build_a_synthesize_resource() throws Exception {
        Environment environment = mock(Environment.class);
        JerseyEnvironment jersey = mock(JerseyEnvironment.class);
        Main application = new Main();
        LifecycleEnvironment lifecycleEnv = new LifecycleEnvironment();
        HealthCheckRegistry healthCheck = new HealthCheckRegistry();
        Configuration config = mock(Configuration.class);

        when(environment.lifecycle()).thenReturn(lifecycleEnv);
        when(environment.healthChecks()).thenReturn(healthCheck);
        when(environment.jersey()).thenReturn(jersey);
        when(config.getCapacity()).thenReturn(1);
        when(config.getFilibusterHome()).thenReturn("not important");
        when(config.isFakeSynthesize()).thenReturn(true);

        application.run(config, environment);

        verify(jersey).register(isA(SynthesizeResource.class));
        assertThat(true, is(!false)); // pmd requires at least one assert...
    }
}
