package se.mtm.speech.synthesis.infrastructure;

import org.junit.Test;
import se.mtm.speech.synthesis.infrastructure.configuration.FakeSynthesize;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {
    private final Configuration configuration = new Configuration();

    @Test
    public void get_capacity() {
        int expected = 17;
        int actual = configuration.getCapacity().getCapacity();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_filibusters() {
        int expected = 6;
        int actual = configuration.getMaxFilibusters();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_time_to_live() {
        long expected = 25 * 60 * 1000;
        long actual = configuration.getTimeToLive().getTtlInMilliseconds();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_idle_time() {
        long expected = 100;
        long actual = configuration.getIdleTime().getIdle();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_timeout_in_seconds() {
        int expected = 30;
        int actual = configuration.getTimeout().getTimeoutSeconds();
        assertThat(actual, is(expected));
    }

    @Test
    public void get_timeout_in_milli_seconds() {
        long expected = 30000;
        long actual = configuration.getTimeout().getTimeoutMilliseconds();
        assertThat(actual, is(expected));
    }

    @Test
    public void ensure_default_value_for_fake_is_false() throws Exception {
        FakeSynthesize actual = configuration.getFakeSynthesize();
        assertThat(actual.isFake(), is(false));
    }
}
