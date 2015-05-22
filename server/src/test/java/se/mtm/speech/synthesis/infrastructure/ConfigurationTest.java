package se.mtm.speech.synthesis.infrastructure;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {
    private final Configuration configuration = new Configuration();

    @Test
    public void get_capacity() { // NOPMD
        int expected = 17;
        int actual = configuration.getCapacity();
        assertThat(actual, is(expected)); // NOPMD
    }

    @Test
    public void get_filibusters() { // NOPMD
        int expected = 6;
        int actual = configuration.getFilibusters();
        assertThat(actual, is(expected)); // NOPMD
    }

    @Test
    public void get_idle_time() { // NOPMD
        int expected = 100;
        int actual = configuration.getIdleTime();
        assertThat(actual, is(expected)); // NOPMD
    }

    @Test
    public void get_timeout() { // NOPMD
        int expected = 30;
        int actual = configuration.getTimeout();
        assertThat(actual, is(expected)); // NOPMD
    }
}
