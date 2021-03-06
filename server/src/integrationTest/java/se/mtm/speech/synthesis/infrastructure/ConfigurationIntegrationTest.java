package se.mtm.speech.synthesis.infrastructure;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import se.mtm.speech.synthesis.Main;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurationIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<Configuration> APPLICATION =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("test-configuration.yaml"));

    @Test
    public void get_capacity() {
        int expected = 17;
        int actual = APPLICATION.getConfiguration().getCapacity().getCapacity();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_filibusters() {
        int expected = 3;
        int actual = APPLICATION.getConfiguration().getMaxFilibusters().getMax();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_idle_time() {
        long expected = 19;
        long actual = APPLICATION.getConfiguration().getIdleTime().getIdle();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_timeout() {
        int expected = 20;
        int actual = APPLICATION.getConfiguration().getTimeout().getTimeoutSeconds();
        assertThat(actual, is(expected));
    }
}
