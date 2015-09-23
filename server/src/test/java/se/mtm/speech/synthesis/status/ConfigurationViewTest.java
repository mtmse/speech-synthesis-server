package se.mtm.speech.synthesis.status;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConfigurationViewTest {
    @Test
    public void get_configuration() throws Exception {
        ConfigurationView view = new ConfigurationView();

        String actual = view.getConfiguration();

        assertTrue("The configuration should not be empty", actual.length() > 10);
    }
}
