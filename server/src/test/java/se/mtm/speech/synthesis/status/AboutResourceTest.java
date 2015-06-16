package se.mtm.speech.synthesis.status;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AboutResourceTest {
    @Test
    public void get_logs_view() {
        AboutResource resource = new AboutResource();

        assertNotNull(resource.showAboutView());
    }

}
