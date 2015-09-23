package se.mtm.speech.synthesis.status;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConfigurationResourceTest {
    @Test
    public void get_logs_view() {
        ConfigurationResource resource = new ConfigurationResource();

        assertNotNull(resource.showConfigurationView());
    }

}
