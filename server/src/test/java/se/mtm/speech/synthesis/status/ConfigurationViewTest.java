package se.mtm.speech.synthesis.status;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class ConfigurationViewTest {
    @Test
    public void get_configuration() throws Exception {
        ConfigurationView view = new ConfigurationView();

        List<String> actual = view.getConfiguration();

        assertFalse("The configuration should not be empty", actual.isEmpty());
    }
}
